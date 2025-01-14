package com.track.util.http;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 * Provides methods related to all HTTP related classes
 * @author Ravindra Gullapalli
 * @since 20180713 180042
 */
public class HttpUtils {
	
	public static String getHostInfo(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getHeader("host") + "/";
	}
	
	public static String connect(String URL) {
		return connect(URL, new Form());
	}
	
	public static String connect(String URL, Form form) {
		//Client client = ClientBuilder.newClient();
		ClientBuilder builder = ClientBuilder.newBuilder();
		builder.sslContext(HttpsConnectionFactory.getSslContext());
		builder.hostnameVerifier(HttpsConnectionFactory.getHostnameVerifier());
		Client client = builder.build();
		WebTarget target = client.target(URL);
		return target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	
	public static String getRootURI(HttpServletRequest request) {
		System.out.println("request.getServerPort() : " + request.getServerPort());
		System.out.println("X-Forwarded-Proto : " + request.getAttribute("X-Forwarded-Proto"));
		System.out.println("x-forwarded-proto : " + request.getAttribute("x-forwarded-proto"));
		String serverName = request.getServerName();
		String scheme = request.getScheme();
		if (!"https".equals(scheme)) {
			scheme += (!"localhost".equals(serverName) ? "s" : "");
		}
		scheme += "://";
		String serverPort = (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : (":" + request.getServerPort());
		String contextPath = request.getContextPath();
		String rootURI = scheme + serverName + serverPort + contextPath;
		System.out.println("rootURI : " + rootURI);
		return  rootURI;
	}
	
//	public String getUrl(HttpServletRequest request) {
//	    HttpRequest httpRequest = new ServletServerHttpRequest(request);
//	    UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
//
//	    String scheme = uriComponents.getScheme();             // http / https
//	    String serverName = request.getServerName();     // hostname.com
//	    int serverPort = request.getServerPort();        // 80
//	    String contextPath = request.getContextPath();   // /app
//
//	    // Reconstruct original requesting URL
//	    StringBuilder url = new StringBuilder();
//	    url.append(scheme).append("://");
//	    url.append(serverName);
//
//	    if (serverPort != 80 && serverPort != 443) {
//	        url.append(":").append(serverPort);
//	    }
//	    url.append(contextPath);
//	    return url.toString();
//	}

}
