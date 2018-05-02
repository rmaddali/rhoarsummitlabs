package io.openshift.booster.insult.service;

public class Insult {
	
	private String content;
	

	public Insult() {
		
		this.content = null;
	}

	public Insult( String content) {
		
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	

}
