/**
 * 
 */
package com.virtress.assets;

import java.util.List;

/**
 * @author ThisIsDef
 *
 */
public class Group {

	private List<Matcher> matchers;
	private String response;
	private String contentType;
	public List<Matcher> getMatchers() {
		return matchers;
	}
	public void setMatchers(List<Matcher> matchers) {
		this.matchers = matchers;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
