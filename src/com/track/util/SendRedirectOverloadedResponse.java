package com.track.util;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class SendRedirectOverloadedResponse extends HttpServletResponseWrapper {
	 private HttpServletRequest m_request;
	    private String prefix = null;
	    private String schema = null;
	    private String servername = null;
	    private int port = 0;
	    private String qString = null;

	    public SendRedirectOverloadedResponse(ServletRequest request,
	            ServletResponse response) {
	        super((HttpServletResponse) response);
	        m_request = (HttpServletRequest) request;
	        prefix = getPrefix((HttpServletRequest) request);
	    }

	    public void sendRedirect(String location) throws IOException {
	        String finalurl = null;
	        if (isUrlAbsolute(location)) {
	            finalurl = location;
	        } else {
	            finalurl = fixForScheme(location);
	        }
	        super.sendRedirect(finalurl);
	    }

	    public boolean isUrlAbsolute(String url) {
	        String lowercaseurl = url.toLowerCase();
	        if (lowercaseurl.startsWith("http") == true) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    public String fixForScheme(String url) {
	    	String furl = "";
	    	if(servername.contains("localhost")) {
	    		furl = "http://";   // "http" + "://
	    		furl += servername;       // "myhost"
	    		furl +=  ":" + port; // ":8080"
	    	}else {
	    		furl = "https://";   // "http" + "://
	    		furl += servername;       // "myhost"
	    	}
			if(url.contains("track/")){
				furl += url; 
			}else if(url.equalsIgnoreCase("login") || url.equalsIgnoreCase("authenticate") || 
					url.equalsIgnoreCase("selectcompany") || url.equalsIgnoreCase("selectapp") || url.equalsIgnoreCase("home")) {
				furl += "/track/"+url; 
			}else if(url.contains("jsp/")){
				furl += "/track/"+url; 
			}else{
				furl += "/track/jsp/"+url;       // "/people"
			}	
			return furl;
	        //alter the url here if you were to change the scheme return url;
	    }

	    public String getPrefix(HttpServletRequest request) { 
	        StringBuffer str = request.getRequestURL();
	        String url = str.toString();
	        String uri = request.getRequestURI();
	        int offset = url.indexOf(uri);
	        schema = request.getScheme();
    		servername = request.getServerName();
    		port = request.getServerPort();
    		qString = request.getQueryString(); // "lastname=Fox&age=30"
	        String prefix = url.substring(0,offset);
			return prefix;
	    }
}
