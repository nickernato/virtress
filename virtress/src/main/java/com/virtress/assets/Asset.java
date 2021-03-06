/**
 * 
 */
package com.virtress.assets;

import java.util.List;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.virtress.utils.Converter;
import com.virtress.utils.XpathParser;

/**
 * @author ThisIsDef
 *
 */
public class Asset {
	private final String EMPTY_STRING = "";
	private String name;
	private String path;
	private List<Group> groups;
	
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
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	/**
	 * Returns the first group that has all matching matchers.
	 * @param urlPath
	 * @param requestHeaders
	 * @return
	 */
	public Group getGroup(String urlPath, List<String> requestHeaders, String httpMethod, String requestBody,
							String contentType) {
		for (Group group : this.groups) {
			boolean allMatchesPass = true;
			for (Matcher matcher : group.getMatchers()) {
				if (matcher.getType().name().equalsIgnoreCase(MatcherType.PATH.name())) {
					String withoutParams = urlPath.split("\\?")[0];
					if (!path.equals(withoutParams)) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.HEADER.name())) {
					boolean foundMatchingHeader = false;
					for (String header : requestHeaders) {
						String[] headerInfo = header.split(":");
						if (headerInfo[0].equals(matcher.getName()) && headerInfo[1].trim().equals(matcher.getValue())) {
							System.out.println("Matched header: " + header);
							foundMatchingHeader = true;
						}
					}
					if (!foundMatchingHeader) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.HTTP_METHOD.name())) {
					if (!matcher.getValue().equals(httpMethod)) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.REQUEST_CONTAINS.name())) {
					if (!requestBody.contains(matcher.getValue())) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.XPATH.name())) {
					if (!EMPTY_STRING.equals(requestBody)) {
						String xml = requestBody;
						if (contentType.toLowerCase().contains("json")) {
							String xmlProlog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
							xml = xmlProlog + Converter.jsonToXML(requestBody);
						}
						System.out.println("JSON:\n" + requestBody);
						if (!XpathParser.parseXmlForBoolean(xml, matcher.getValue())) {
							allMatchesPass = false;
						}
					}
					else
					{
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.REGEX_BODY.name())) {
					Pattern regex = Pattern.compile(matcher.getValue());
					java.util.regex.Matcher match = regex.matcher(requestBody);
					if (!match.find()) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.URL_PARAM.name())) {
					boolean foundUrlParam = false;
					String[] urlSplit = urlPath.split("\\?");
					if (urlSplit.length > 1) {
						String paramsString = urlSplit[1];
						String[] sets = paramsString.split("&");
						for (String set : sets) {
							String[] keyValue = set.split("=");
							String variable = keyValue[0];
							String value = keyValue[1];
							if (variable.equals(matcher.getName()) && value.equals(matcher.getValue())) {
								foundUrlParam = true;
							}
						}
					}
					if (!foundUrlParam) {
						allMatchesPass = false;
					}
				}
				else if (matcher.getType().name().equalsIgnoreCase(MatcherType.CUSTOM_SCRIPT.name())) {
					ScriptEngineManager manager = new ScriptEngineManager();      
					ScriptEngine engine  = manager.getEngineByName("JavaScript");
			        String script = matcher.getValue();
			        boolean matched = false;
					try
					{
			        	engine.put("requestBody", requestBody);
			        	engine.put("urlPath", urlPath);
			        	engine.put("requestHeaders", requestHeaders);
			        	engine.put("requestHttpMethod", httpMethod);
			        	engine.put("contentType", contentType);
			        	matched = (boolean) engine.eval(script);
					} catch (ScriptException e1)
					{
						System.out.println("Custom script error: " + e1.getMessage());
					}
					
					if (!matched) {
						allMatchesPass = false;
					}
				}
			}
			if (allMatchesPass) {
				return group;
			}
		}
		return null;
	}
}
