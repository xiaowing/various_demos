package org.wingsdak.sessmgr.beans;

import java.io.Serializable;

public class Greeting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2145603238199515514L;
	
	private final long id;
    private final String content;
    
    private String sessionId;
    private String attribute;
    private String lastVisit;
    
	public long getId() {
		return id;
	}
	
    public String getContent() {
		return content;
	}

	public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(String lastVisit) {
		this.lastVisit = lastVisit;
	}
}
