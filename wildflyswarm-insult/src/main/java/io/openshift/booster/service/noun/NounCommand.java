package io.openshift.booster.service.noun;


import java.net.URI;
import java.net.URISyntaxException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class NounCommand extends HystrixCommand<Noun> {
    private URI serviceURI;

    public NounCommand(URI serviceURI) {
        super(Setter
                      .withGroupKey(HystrixCommandGroupKey.Factory.asKey("NounGroup"))
                      .andCommandPropertiesDefaults(
                              HystrixCommandProperties.Setter()
                                      .withCircuitBreakerRequestVolumeThreshold(10)
                                      .withCircuitBreakerSleepWindowInMilliseconds(10000)
                                      .withCircuitBreakerErrorThresholdPercentage(50)
                      )
        );

        this.serviceURI = serviceURI;
    }
    
    public NounCommand(String serviceURI) {
        super(Setter
                      .withGroupKey(HystrixCommandGroupKey.Factory.asKey("NounGroup"))
                      .andCommandPropertiesDefaults(
                              HystrixCommandProperties.Setter()
                                      .withCircuitBreakerRequestVolumeThreshold(10)
                                      .withCircuitBreakerSleepWindowInMilliseconds(10000)
                                      .withCircuitBreakerErrorThresholdPercentage(50)
                      )
        );

        try {
			this.serviceURI = new URI(serviceURI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected Noun run() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(serviceURI);

        NounService nounService = target.proxy(NounService.class);
        return nounService.getNoun();
    }

    @Override
    protected Noun getFallback() {
        return new Noun().noun("Fallback Noun!");
    }
}
