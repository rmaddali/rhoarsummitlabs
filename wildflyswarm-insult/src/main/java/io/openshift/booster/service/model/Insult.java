package io.openshift.booster.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Insult {
    private String noun;

    private String adjective;

    @JsonIgnore
    private String adj2;

    @JsonIgnore
    private String name;

    public String getNoun() {
        return noun;
    }

    public Insult noun(String noun) {
        this.noun = noun;
        return this;
    }

    public String getAdjective() {
        return adjective;
    }

    public Insult adj1(String adj1) {
        this.adjective = adj1;
        return this;
    }

    public String getAdj2() {
        return adj2;
    }

    public Insult adj2(String adj2) {
        this.adj2 = adj2;
        return this;
    }

    public String getName() {
        return name;
    }

    public Insult name(String name) {
        this.name = name;
        return this;
    }
}
