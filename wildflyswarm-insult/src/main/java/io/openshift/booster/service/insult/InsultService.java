package io.openshift.booster.service.insult;
import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import io.openshift.booster.service.adjective.AdjectiveService;
import io.openshift.booster.service.model.Insult;
import io.openshift.booster.service.model.Name;
import io.openshift.booster.service.noun.Noun;
import io.openshift.booster.service.noun.NounCommand;
import io.openshift.booster.service.noun.NounService;

@Path("/")
@ApplicationScoped
public class InsultService {
	
	
	

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/insult")
    public Insult getInsult() throws Exception {
    	
    	 System.out.println("cakked-");
        return insultByName(null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/nameinsult")
    public Insult insultByName(Name name) throws Exception {
        
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget rtarget = client.target("http://wildflyswarm-noun:8080/api");
        
       
       URI uri= new URI("http", null, serviceEntry.getAddress(), serviceEntry.getPort(), null, null, null);
        
        
      
        NounService nounService = rtarget.proxy(NounService.class);
       
        
        Noun n=new NounCommand("http://wildflyswarm-noun:8080/api").execute().getNoun();
       
        
        
        
       
        ResteasyClient adjClient = new ResteasyClientBuilder().build();
        ResteasyWebTarget adjTarget = adjClient.target("http://wildflyswarm-adj:8080/api");
        AdjectiveService adjService = adjTarget.proxy(AdjectiveService.class);
        
       

        return new Insult()
                .noun(nounService.getNoun().getNoun())
                .adj1(adjService.getAdjective().getAdjective())
                .adj2(adjService.getAdjective().getAdjective())
                .name(name != null ? name.getName() : null);
    }

   
}
