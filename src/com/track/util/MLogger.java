package com.track.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.track.audit.domain.AuditMessage;
import com.track.audit.domain.AuditMessageType;

public class MLogger {
	public static final String COMPANY_CODE = "COMPANY_CODE";
	public static final String USER_CODE = "USER_CODE";
	public static final String CLASS_CODE = "CLASS_CODE";
	private HashMap<String, String> loggerConstans = new HashMap<String, String>();
	private static Integer QUERY_LOGGING_TYPE = 1;
	private static Integer INFO_LOGGING_TYPE = 2;
	private static Integer ERROR_LOGGING_TYPE = 3;

	public static int previousLevel = 0;

	private static StringBuffer strLevel = new StringBuffer("");

	public void exception(Boolean islogError, String errorMessage, Exception ex) {
		if (islogError) {
			StringBuffer stackTrace = new StringBuffer();
			StackTraceElement[] stackTraceElementArray = ex.getStackTrace();
			for (StackTraceElement stackTraceElement : stackTraceElementArray) {
				stackTrace.append("\t" + stackTraceElement.toString());
				stackTrace.append("\n");
			}

			sendLoggerMessageToListner("[ACCESS USER: "
					+ loggerConstans.get(USER_CODE) + "] " + errorMessage
					+ " :: " + ex.getMessage() + "\n" + stackTrace.toString(),
					ERROR_LOGGING_TYPE);
		}
	}

	public HashMap<String, String> getLoggerConstans() {
		return loggerConstans;
	}

	public void setLoggerConstans(HashMap<String, String> loggerConstans) {
		this.loggerConstans = loggerConstans;
	}

	public static void log(String s, boolean debug) {
		if (debug) {
			System.out.println(s);
		}
	}

	public static void info(String company, String userid, String msg) {
		System.out.println(company + "\t: " + userid + "\t: " + msg);
	}

	public void info(String s) {
		sendLoggerMessageToListner("[ACCESS USER: "
				+ loggerConstans.get(USER_CODE) + "] " + s + "\n",
				INFO_LOGGING_TYPE);

	}

	public void info(boolean flag, String s) {
		if (flag) {
			sendLoggerMessageToListner("[ACCESS USER: "
					+ loggerConstans.get(USER_CODE) + "] " + s + "\n",
					INFO_LOGGING_TYPE);

		}
	}

	public void auditInfo(boolean flag, String s) {

		if (flag) {
			sendLoggerMessageToListner("[ACCESS USER: "
					+ loggerConstans.get(USER_CODE) + "] " + s,
					INFO_LOGGING_TYPE);

		}

	}

	public void Exception(String exp) {
		StringBuffer s = new StringBuffer();

		String printExp = "[ACCESS USER: " + loggerConstans.get(USER_CODE)
				+ "] " + exp;
		s.append(printExp);
		sendLoggerMessageToListner(s.toString(), ERROR_LOGGING_TYPE);

	}

	public void query(boolean flag, String s) {
		if (flag) {
			sendLoggerMessageToListner("[ACCESS USER: "
					+ loggerConstans.get(USER_CODE) + "] " + s,
					QUERY_LOGGING_TYPE);
			

		}

	}
	public static void log(int l, String s) {
		if (true) {
			System.out.println(strLevel.toString() + " Enter : " + s);
		}
	}

	private void sendLoggerMessageToListner(String loggerMessage,
			Integer logingType) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			AuditMessage auditMessage = new AuditMessage();
			auditMessage.setCompanyName(this.loggerConstans.get(COMPANY_CODE));
			auditMessage.setAuditMessage(loggerMessage);
			auditMessage.setAuditMessageDateAndTime(sdf.format(cal.getTime()));
			switch (logingType) {
			case 1:
				auditMessage.setAuditMessageType(AuditMessageType.QUERY);
				break;
			case 2:
				auditMessage.setAuditMessageType(AuditMessageType.INFO);
				break;
			case 3:
				auditMessage.setAuditMessageType(AuditMessageType.LOG);
				break;
			default:
				auditMessage.setAuditMessageType(AuditMessageType.OTHER_STATE);
				break;
			}
			StringBuffer auditMesageStrinngBuffer = new StringBuffer();
			auditMesageStrinngBuffer.append(""
					+ auditMessage.getAuditMessageDateAndTime() + " ");
			auditMesageStrinngBuffer.append(""
					+ auditMessage.getAuditMessageType() + " ");
			auditMesageStrinngBuffer.append(auditMessage.getAuditMessage());
			auditMesageStrinngBuffer.append("\n");

			System.out.println(auditMesageStrinngBuffer.toString());
                  // AuditLoggerControler.auditTopicPublisher.produce(auditMessage);
		} catch (Exception e) {
			System.out.println("Unable to get the JMS Topic to send the logging message");
		}
	}
}
