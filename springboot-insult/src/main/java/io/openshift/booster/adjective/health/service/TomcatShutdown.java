package io.openshift.booster.adjective.health.service;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.springframework.stereotype.Component;

@Component
public class TomcatShutdown {

	
	private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void shutdown() {
        if (context == null) {
            System.out.println("Tomcat context was not registered. Stopping JVM instead.");
            System.exit(0);
        }

        try {
            System.out.println("Stopping Tomcat context.");
            context.stop();
        } catch (LifecycleException e) {
            System.out.println("Error when stopping Tomcat context. Stopping JVM instead.");
            System.exit(0);
        }
    }
}
