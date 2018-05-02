package io.openshift.booster.service.model;

public class Name {
    private String name;

    public String getName() {
        return name;
    }

    public Name name(String name) {
        this.name = name;
        return this;
    }
}
