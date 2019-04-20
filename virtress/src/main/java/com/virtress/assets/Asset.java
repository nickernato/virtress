/**
 * 
 */
package com.virtress.assets;

import java.util.List;

/**
 * @author ThisIsDef
 *
 */
public class Asset {
	private String name;
	private String path;
	private List<Matcher> matchers;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Matcher> getMatchers() {
		return matchers;
	}
	public void setMatchers(List<Matcher> matchers) {
		this.matchers = matchers;
	}
}
