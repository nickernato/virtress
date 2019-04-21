/**
 * 
 */
package com.virtress.assets;

/**
 * @author ThisIsDef
 *
 */
public class Matcher {
	private MatcherType type;
	private String name;
	private String value;
	
	public MatcherType getType() {
		return type;
	}
	public void setType(MatcherType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
