/*
 *
 *  Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.openshift.booster;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.arquillian.cube.kubernetes.annotations.Named;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;

import io.fabric8.kubernetes.api.model.Service;
import static org.hamcrest.CoreMatchers.containsString;


/**
 * @author Heiko Braun
 */
@RunWith(Arquillian.class)
public class OpenshiftIT {

    @RouteURL("${app.name}")
    private URL url;
    
    @Named("wildflyswarm-insult")
    @ArquillianResource
    Service insultService;

    @Before
    public void setup() {
        await().atMost(5, TimeUnit.MINUTES).until(() -> {
            try {
                return get(url).getStatusCode() == 200;
            } catch (Exception e) {
                return false;
            }
        });

        RestAssured.baseURI = url + "api/insult";
    }
    
    
    @Test
    public void should_verify_insult_service_should_not_be_null() throws IOException {
        assertThat(insultService).isNotNull();
        assertThat(insultService.getSpec()).isNotNull();
        assertThat(insultService.getSpec().getPorts()).isNotNull();
        assertThat(insultService.getSpec().getPorts()).isNotEmpty();
    }
    
    @Test
    public void should_test_insultservice_entry_endpoint_is_reachable() {
        RequestSpecBuilder requestSpecBuilder = getRequestSpecBuilder();

        given(requestSpecBuilder.build())
                .when().get()
                .then()
                .statusCode(200);
                
    }
    

   
    
    private RequestSpecBuilder getRequestSpecBuilder() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(String.format("http://%s/api/insult/", Objects.requireNonNull(url).getHost()));
        return requestSpecBuilder;
    }
}
