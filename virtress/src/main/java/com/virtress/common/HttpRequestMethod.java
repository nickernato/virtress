/**
 * 
 */
package com.virtress.common;

/**
 * @author ThisIsDef
 *
 */
public enum HttpRequestMethod {
	GET,
	POST,
	OPTIONS,
	DELETE,
	PUT,
	HEAD,
	CONNECT,
	TRACE,
	PATCH;
	
	public static boolean contains(String httpMethod) {
		for(HttpRequestMethod method : HttpRequestMethod.values()) {
			if (httpMethod.equalsIgnoreCase(method.name())) {
				return true;
			}
		}
		return false;
	}
}
