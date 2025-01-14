package com.track.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.Provider;
import java.security.Security;
import java.util.Calendar;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class Global {
	// DB connections expiration time & checking time
	public static int TIMEOUT = 5 * 1000; // 5 seconds
	public static int EXPIRATIONTIME = 24 * 60 * 60 * 1000; // 24 Hours
	// expiration time of the session of the web server
	public static int SESSIONEXPIRATION = 300000;
	public static String sDBUrl = "jdbc:db2:sms";
	public static String sDBUser = "db2admin";
	public static String sDBPass = "admin";
	public static String sDriver = "COM.ibm.db2.jdbc.app.DB2Driver";
	public static String sDateFormat = "MM/dd/yyyy";
	public static String sTimeFormat = "HH:mm";
	public static int iDebugTrace = 1;
	public static String sLogTrace = "";
	public static PrintStream LogStream = null;
	public static Cipher desCipher = null;
	public static KeyGenerator keyGenerator = null;
	public static String DownloadDir = "f:\\sms\\sms\\download";

	public static final int ENGLISH_LANG = 1;
	public static final int CHINESE_SIMP_LANG = 2;
	public static final int CHINESE_TRAD_LANG = 3;
	public static final String[] encoding = { "", "iso-8859-1", "gb2312",
			"Big5" };
	public static final String[] encoding_html = { "", "8859_1", "gb2312",
			"Big5" };
	public static final String PROPERTIES_FILENAME = "sms.properties";

	static {
		try {
			System.out.println("User directory: "
					+ System.getProperty("user.dir"));
			Properties prop = new Properties();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(PROPERTIES_FILENAME);
			} catch (FileNotFoundException fnfe) {
				System.out.println("Could not find " + PROPERTIES_FILENAME
						+ " file in user directory");
				System.exit(0);
			}
			prop.load(fis);

			sDBUrl = prop.getProperty("dburl", sDBUrl);
			sDBUser = prop.getProperty("dbuser", sDBUser);
			sDBPass = prop.getProperty("dbpass", sDBPass);
			sDriver = prop.getProperty("driver", sDriver);
			TIMEOUT = Integer.parseInt(prop
					.getProperty("timeout", "" + TIMEOUT));
			EXPIRATIONTIME = Integer.parseInt(prop.getProperty(
					"expirationtime", "" + EXPIRATIONTIME));
			SESSIONEXPIRATION = Integer.parseInt(prop.getProperty(
					"sessionexpiration", "" + SESSIONEXPIRATION));
			sDateFormat = prop.getProperty("dateformat", sDateFormat);
			sTimeFormat = prop.getProperty("timeformat", sTimeFormat);
			iDebugTrace = Integer.parseInt(prop.getProperty("debugtrace", ""
					+ iDebugTrace));
			sLogTrace = prop.getProperty("logtrace", sLogTrace);
			LogStream = new PrintStream(System.out, true);
			fis.close();

			System.out.println("DB Connection Manager initialized.");
			if (!sLogTrace.equals("")) {
				FileOutputStream LogFile = new FileOutputStream(sLogTrace);
				if (LogFile != null)
					LogStream = new PrintStream(LogFile, true);
			}

			Provider provider = new com.sun.crypto.provider.SunJCE();
			Security.addProvider(provider);
			desCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding", "SunJCE");
			keyGenerator = KeyGenerator.getInstance("DESede");
		} catch (Exception e) {
			trace(e);
		}
	}

	public static void trace(String sMess) {
		String sDate = "[" + Calendar.getInstance().getTime() + "]\r\n";
		if (iDebugTrace == 1) {
			System.out.println(sDate + sMess);
		}
		if (LogStream != null) {
			LogStream.println(sDate + sMess);
		}
	}

	public static void trace(Exception e) {
		String sDate = "[" + Calendar.getInstance().getTime() + "]\r\n";
		System.out.print(sDate);
		if (iDebugTrace == 1) {
			e.printStackTrace();
		}
		if (LogStream != null) {
			e.printStackTrace(LogStream);
		}
	}

	public static String fixString(String sStr) {
		if (sStr == null)
			return null;
		String sS = null;
		sS = sStr.trim();
		System.out.println(sS);
		if (sS.equalsIgnoreCase(""))
			return null;
		return sS;
	}

}
