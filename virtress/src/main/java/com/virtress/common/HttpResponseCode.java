/**
 * 
 */
package com.virtress.common;

/**
 * @author ThisIsDef
 *
 */
public enum HttpResponseCode {
	OK("200");
	
	private String responseCode;
	
	private HttpResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	/**
	 * Returns the HttpResponseCode for the passed in response code.
	 * @param responseCode
	 * @return
	 */
	public static HttpResponseCode getHttpResponseCode(String responseCode) {
		for (HttpResponseCode httpResponseCode : HttpResponseCode.values()) {
			if (httpResponseCode.getResponseCode().equals(responseCode)) {
				return httpResponseCode;
			}
		}
		return HttpResponseCode.OK;
	}
}
