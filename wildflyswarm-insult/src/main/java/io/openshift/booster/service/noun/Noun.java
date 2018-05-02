package io.openshift.booster.service.noun;



	
	public class Noun {

	    private String noun;

	    public Noun() {
	    }

	    public Noun(String noun) {
	        this.noun = noun;
	    }

	    public String getNoun() {
	        return noun;
	    }

	    public Noun noun(String noun) {
	        this.noun = noun;
	        return this;
	    }
	}

