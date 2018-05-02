/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.springboot.noun.service.model.Noun
 *  org.springboot.noun.service.rest.GreetingProperties
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestMethod
 *  org.springframework.web.bind.annotation.RestController
 */
package org.springboot.noun.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springboot.noun.service.model.Noun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class NounServiceController {
   
    private final AtomicLong counter = new AtomicLong();
    private List<Noun> nouns = new ArrayList<Noun>();

    @RequestMapping(method={RequestMethod.GET}, value={"/api/noun"})
    public Noun doGet() {
        return this.nouns.get(new Random().nextInt(this.nouns.size()));
    }

    @PostConstruct
    public void loadData() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("classpath:noun.txt");
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                reader.lines().forEach(noun -> {
                    this.nouns.add(new Noun(noun.trim()));
                }
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}