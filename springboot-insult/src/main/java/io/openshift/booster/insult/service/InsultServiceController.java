/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openshift.booster.insult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import io.openshift.booster.service.GreetingProperties;
import io.openshift.booster.*;
@RestController

public class InsultServiceController {
    
	@Autowired
	private GreetingProperties properties;
	@Autowired
	private final AdjectiveService adjService;
	@Autowired
	private final NounService nounService;
	
	public InsultServiceController(final AdjectiveService aService,final NounService nService)
	{
		adjService=aService;
		nounService=nService;
	}

	@RequestMapping("/api/insult")
	public Insult doGet() {
		 
		System.out.println("insult called=");
		 String adj=adjService.getAdjective();
		 System.out.println("adj returned="+adj);
		 
		 
		 
		 String noun=nounService.getNoun();
		 
		 System.out.println("Adjective="+adj);
		 System.out.println("Noun="+noun);
		 
		return new Insult(adj+noun);
	}
}
