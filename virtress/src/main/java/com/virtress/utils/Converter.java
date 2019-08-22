/**
 * 
 */
package com.virtress.utils;

import org.json.JSONObject;
import org.json.XML;

/**
 * @author ThisIsDef
 *
 */
public class Converter
{
	/**
	 * Converts a json string to a xml string.
	 * @param json
	 * @return
	 */
	public static String jsonToXML(String json) {
		JSONObject obj = new JSONObject(json);
		String xml_data = XML.toString(obj);		
		return xml_data;
	}
	
	/**
	 * Converts xml string to a json string.
	 * @param xml
	 * @return
	 */
	public static String xmlToJson(String xml) {
		JSONObject obj = XML.toJSONObject(xml);
		return obj.toString();
	}
}
