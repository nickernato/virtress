/**
 * 
 */
package com.virtress.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author ThisIsDef
 *
 */
public class XpathParser
{
	/**
	 * Parses xml supplied with the xpath specified.
	 * @param xml
	 * @param xpath
	 * @return
	 */
	public static boolean parseXmlForBoolean(String xml, String xpath) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document xmlDocument = null;
		boolean foundMatch = false;
		try
		{
			builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(new InputSource(new StringReader(xml)));
			XPath xPath = XPathFactory.newInstance().newXPath();
			foundMatch = (boolean) xPath.compile(xpath).evaluate(xmlDocument, XPathConstants.BOOLEAN);
		}
		catch (XPathExpressionException e)
		{
			System.out.println("There was an exception while parsing the XPATH for " + xpath + " Error: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("There was an error reading in xml to parse with xpath: " + e.getMessage());
		}
		catch (SAXException e)
		{
			System.out.println("There was a SAX exception while parsing xml: " + e.getMessage());
		}
		catch (ParserConfigurationException e)
		{
			System.out.println("There was an error while creating the build factory for xml: " + e.getMessage());
		}
		return foundMatch;
	}
}
