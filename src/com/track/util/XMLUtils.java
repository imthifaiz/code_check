package com.track.util;

public class XMLUtils {
	private static final String AND = "&";
	private static final String R_AND = "&amp;";

	public XMLUtils() {
	}

	public static String getXMLMessage(int status, String message) {
		String xmlStr;
		xmlStr = "<?xml version='1.0' encoding='UTF-8'?>";
		xmlStr += "<message>";
		xmlStr += "<status>" + String.valueOf(status).trim() + "</status>";
		xmlStr += "<description>" + message.trim() + "</description>";
		xmlStr += "</message>";
		return xmlStr.trim();
	}
	
	public static String getXMLMessageWithServerTime(int status, String message) {
		String xmlStr;
		xmlStr = "<?xml version='1.0' encoding='UTF-8'?>";
		xmlStr += "<message>";
		xmlStr += "<status>" + String.valueOf(status).trim() + "</status>";
		xmlStr += "<description>" + message.trim() + "</description>";
		xmlStr += "<serverDateTime>" + DateUtils.getDateAtTime() + "</serverDateTime>";
		xmlStr += "</message>";
		return xmlStr.trim();
	}

	public static String getXMLMessagePL(int status, String message,
			String plant) {
		String xmlStr;
		xmlStr = "<?xml version='1.0' encoding='UTF-8'?>";
		xmlStr += "<message>";
		xmlStr += "<status>" + String.valueOf(status).trim() + "</status>";
		xmlStr += "<description>" + message.trim() + "</description>";
		xmlStr += "<plant>" + plant.trim() + "</plant>";
		xmlStr += "</message>";
		return xmlStr.trim();
	}

	public static String getXMLNode(String nodeName, String nodeValue) {
		String xmlStr = "";
		try {
			xmlStr = "<" + nodeName.trim() + ">" + nodeValue.trim() + "</"
					+ nodeName.trim() + ">";
		} catch (Exception e) {
			xmlStr = "<" + nodeName.trim() + ">" + "</" + nodeName.trim() + ">";
		}
		return xmlStr.trim();
	}

	public static String getXMLAttrib(String nodeName, String attribName,
			String attribValue) {
		String xmlStr = "";
		try {
			xmlStr = "<" + nodeName.trim() + "  " + attribName.trim() + "='"
					+ attribValue.trim() + "'>";
			;
		} catch (Exception e) {
		}
		return xmlStr.trim();
	}

	public static String getXMLHeader() {
		return "<?xml version='1.0' encoding='utf-8' ?>";
	}

	public static String getStartNode(String nodeName) {
		return "<" + nodeName.trim() + ">";
	}

	public static String getEndNode(String nodeName) {
		return "</" + nodeName.trim() + ">";
	}

	public static String getValidXMLString(String str) {
		String retStr = "";

		if (!str.equalsIgnoreCase("")) {
			retStr = str.replaceAll(AND, R_AND);
		}
		return retStr;
	}
}