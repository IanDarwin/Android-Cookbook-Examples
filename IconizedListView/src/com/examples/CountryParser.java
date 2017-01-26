package com.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.util.Log;

public class CountryParser {

	private static final String tag = "CountryParser";
	private static final String FILE_EXTENSION= ".png";
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private final HashMap<String, String> map;
	private final List<Country> list;

	public CountryParser() {
		this.list = new ArrayList<Country>();
		this.map = new HashMap<String, String>();
	}

	private String getNodeValue(NamedNodeMap map, String key) {
		String nodeValue = null;
		Node node = map.getNamedItem(key);
		if (node != null) {
			nodeValue = node.getNodeValue();
		}
		return nodeValue;
	}

	public List<Country> getList() {
		return this.list;
	}

	public String getAbbreviation(String country) {
		return (String) this.map.get(country);
	}

	/**
	 * Parse XML file containing body part X/Y/Description
	 * 
	 * @param inStream
	 */
	public void parse(InputStream inStream) {
		try {
			// TODO: after we must do a cache of this XML!!!!
			this.factory = DocumentBuilderFactory.newInstance();
			this.builder = this.factory.newDocumentBuilder();
			this.builder.isValidating();
			Document doc = this.builder.parse(inStream, null);

			doc.getDocumentElement().normalize();

			NodeList countryList = doc.getElementsByTagName("country");
			final int length = countryList.getLength();

			for (int i = 0; i < length; i++) {
				final NamedNodeMap attr = countryList.item(i).getAttributes();
				final String countryName = getNodeValue(attr, "name");
				final String countryAbbr = getNodeValue(attr, "abbreviation");
				final String countryRegion = getNodeValue(attr, "region");

				// Construct Country object
				Country country = new Country(countryName, countryAbbr,
						countryRegion, countryAbbr + FILE_EXTENSION);
				
				// Add to list
				this.list.add(country);
				
				// Creat Map countrname-abbrev
				this.map.put(countryName, countryAbbr);
				Log.d(tag, country.toString());
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
