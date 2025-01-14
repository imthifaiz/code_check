package com.track.servlet;

import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.track.util.IMLogger;
import com.track.util.http.HttpUtils;

public class UcloServlet extends HttpServlet implements IMLogger {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136069338637043579L;

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {

	}
	
	protected String getRootURI(HttpServletRequest request) {
		return HttpUtils.getRootURI(request);
	}

}
