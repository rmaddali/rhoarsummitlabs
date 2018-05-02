package io.openshift.booster.insult.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




@Service
public class NounService {

	
	private String nounServiceName;
	 private final RestTemplate restTemplate = new RestTemplate();
	 private final String nounHost = System.getProperty("noun.host", "http://springboot-noun-service:8080");
	
	public String getNoun() {
		
		System.out.println("NounServiceName="+nounHost);
	    return restTemplate.getForObject(nounHost + "/api/noun", String.class);
	  }
}
