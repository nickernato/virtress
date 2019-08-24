/**
 * 
 */
package com.virtress.assets;

import java.util.List;
import java.util.regex.Pattern;

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
					if (!path.equalsIgnoreCase(urlPath)) {
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
			}
			if (allMatchesPass) {
				return group;
			}
		}
		return null;
	}
}
