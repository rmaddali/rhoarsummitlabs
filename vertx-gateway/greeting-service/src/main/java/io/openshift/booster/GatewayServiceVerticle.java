package io.openshift.booster;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

import java.util.HashMap;
import java.util.Map;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.rxjava.circuitbreaker.CircuitBreaker;
import io.vertx.rxjava.circuitbreaker.HystrixMetricHandler;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.handler.StaticHandler;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import rx.Single;

public class GatewayServiceVerticle extends AbstractVerticle {

    protected static final String template = "Congrats, %s %s!";
    private Map<String, String> response;
    
    private CircuitBreaker circuit;
    private WebClient clientSpringboot;
    private WebClient clientSwarm;

    @Override
    public void start() throws Exception {
        circuit = CircuitBreaker.create("circuit-breaker", vertx,
            new CircuitBreakerOptions()
                .setFallbackOnFailure(true)
                .setMaxFailures(3)
                .setResetTimeout(5000)
                .setNotificationAddress("circuit-breaker")
                .setTimeout(1000)
        );

        clientSpringboot = WebClient.create(vertx, new WebClientOptions()
            .setDefaultHost("springboot-adjective-service.rhoar.svc")
            .setDefaultPort(8080));
        
        clientSwarm = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("wildflyswarm-noun.rhoar.svc")
                .setDefaultPort(8080));
            
        response = new HashMap<String, String>();

        Router router = Router.router(vertx);

        router.get("/health").handler(rc -> rc.response().end("OK"));
        router.get("/eventbus/*").handler(getSockJsHandler());
        // The address is the circuit breaker notification address configured above.
        router.get("/metrics").handler(HystrixMetricHandler.create(vertx, "circuit-breaker"));


        router.get("/api/greeting").handler(this::dispatch);
        router.get("/api/cb-state").handler(
            rc -> rc.response()
                .putHeader(CONTENT_TYPE.toString(), APPLICATION_JSON.toString())
                .end(new JsonObject().put("state", circuit.state()).encodePrettily()));
        router.get("/*").handler(StaticHandler.create());

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080);
    }

   
    private void dispatch(RoutingContext rc) {
        
    	Single<String> adjectiveCommandWithFallback = circuit.rxExecuteCommandWithFallback(
            future ->
                clientSpringboot.get("/api/adjective").rxSend()
                    .doOnEach(r -> System.out.println(r.getValue().bodyAsString()))
                    .map(HttpResponse::bodyAsJsonObject)
                    .map(json -> json.getString("adjective"))                    
                    .subscribe(
                        future::complete,
                        future::fail
                    ),
            error -> {
                System.out.println("Fallback called for " + error.getMessage());
                error.printStackTrace();
                return "fallback-adjective";
            }
        );
        
        Single<String> nounCommandWithFallback = circuit.rxExecuteCommandWithFallback(
                future ->
                clientSwarm.get("/api/noun").rxSend()
                        .doOnEach(r -> System.out.println(r.getValue().bodyAsString()))
                        .map(HttpResponse::bodyAsJsonObject)
                        .map(json -> json.getString("noun"))                    
                        .subscribe(
                            future::complete,
                            future::fail
                        ),
                error -> {
                    System.out.println("Fallback called for " + error.getMessage());
                    error.printStackTrace();
                    return "fallback-noun";
                }
            );
              
        adjectiveCommandWithFallback.subscribe(
        		adjective -> {
                    response.put("adjective", adjective);
                }
            );
        
        nounCommandWithFallback.subscribe(
        		noun -> {
        			response.put("noun", noun);
                }
            );
        
        JsonObject jsonResponse = new JsonObject()
                .put("content", String.format(template, response.get("adjective"), response.get("noun")));
            rc.response()
                .putHeader(CONTENT_TYPE.toString(), APPLICATION_JSON.toString())
                .end(jsonResponse.encode());
    }

    private Handler<RoutingContext> getSockJsHandler() {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions();
        options.addInboundPermitted(
            new PermittedOptions().setAddress("circuit-breaker"));
        options.addOutboundPermitted(
            new PermittedOptions().setAddress("circuit-breaker"));
        return sockJSHandler.bridge(options);
    }
}
