<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
			.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, com.track.util.StrUtils.fString(
			(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
%>