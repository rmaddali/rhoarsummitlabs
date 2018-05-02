package io.openshift.booster.adjective.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import io.openshift.booster.adjective.model.Adjective;

/**
 * @author Ken Finnigan
 */
@Path("/")
@ApplicationScoped


public class AdjectiveResource {

    private List<Adjective> adjectives = new ArrayList<>();

    @PostConstruct
    public void loadData() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("adjectives.txt");
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                reader.lines()
                        .forEach(adj -> adjectives.add(new Adjective(adj.trim())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/adjective")
    public Adjective getAdjective() {
        return adjectives.get(new Random().nextInt(adjectives.size()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAdjective(Adjective adjective) {
        if (adjectives.contains(adjective)) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .build();
        }

        adjectives.add(adjective);

        return Response
                .status(Response.Status.CREATED)
                .entity(adjective)
                .build();
    }

    @DELETE
    @Path("/{adjective}")
    public Response deleteAdjective(@PathParam("adjective") String adjectiveName) {
        Adjective deletingAdjective = new Adjective(adjectiveName);

        if (!adjectives.contains(deletingAdjective)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        adjectives.remove(deletingAdjective);

        return Response
                .noContent()
                .build();
    }
}