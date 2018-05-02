package io.openshift.booster.adjective.health.service;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Path("/")
@Component
public class ShutdownController {

	private final TomcatShutdown tomcatShutdown;

    @Autowired
    public ShutdownController(TomcatShutdown tomcatShutdown) {
        this.tomcatShutdown = tomcatShutdown;
    }
    @GET
    @Path("/stop")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String shutdown() throws Exception {
        tomcatShutdown.shutdown();
        return "Shutting down springboot instance..";
    }
}
