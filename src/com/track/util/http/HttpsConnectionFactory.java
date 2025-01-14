package com.track.util.http;

import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsConnectionFactory {
	Proxy proxy;

	String proxyHost;

	Integer proxyPort;

	public boolean canConnect = true;

	private static final Logger log = Logger.getLogger("ReportPortal");

	public HttpsConnectionFactory() {
	}

	/**
	 *
	 * @return
	 */
	public static SSLContext getSslContext() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, new SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException ex) {
			ex.printStackTrace();
		}
		return sslContext;
	}

	/**
	 *
	 * @return
	 */
	public static HostnameVerifier getHostnameVerifier() {
		return (String hostname, javax.net.ssl.SSLSession sslSession) -> true;
	}

	public Boolean isHttps(String url) {

		if (url.startsWith("https://")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
