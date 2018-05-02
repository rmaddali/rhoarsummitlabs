package io.openshift.booster.health.service;

import java.net.InetAddress;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

/**
 * @author Ken Finnigan
 */
@Path("/")
public class HealthResource {

	

	    @GET
	    @Health
	    @Path("/health")
	    public HealthStatus healthCheck() {
	        return HealthStatus
	                .named("status")
	                .withAttribute("details", "We're running!")
	                .up();
	    }
	    
	    /**
	     * We are sending a suspend signal to wildflyswarm to mimic error, so that Openshift would restart the pod
	     * @return
	     */
	    @GET
	    @Path("/stop")
	    public Response stop() {
	        ModelNode op = new ModelNode();
	        op.get("address").setEmptyList();
	        op.get("operation").set("suspend");

	        try (ModelControllerClient client = ModelControllerClient.Factory.create(
	                InetAddress.getByName("localhost"), 9990)) {
	            ModelNode response = client.execute(op);

	            if (response.has("failure-description")) {
	                throw new Exception(response.get("failure-description").asString());
	            }

	            return Response.ok(response.get("result").asString()).build();

	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	}


