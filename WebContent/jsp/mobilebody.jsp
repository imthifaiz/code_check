
<%@ page import="com.track.util.*"%>
<jsp:useBean id="miscbean" class="com.track.gates.miscBean" />
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<%
String errmsg="Expirydate not found";
boolean bflag=false,errflg=false;String company1="",dateres="";
try{
	//HashMap<String, String> loggerDetailsHasMap2 = new HashMap<String, String>();
	//loggerDetailsHasMap2.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	//loggerDetailsHasMap2.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger2 = new MLogger();
	//mLogger2.setLoggerConstans(loggerDetailsHasMap2);
	ubean.setmLogger(mLogger2);
	session = request.getSession();
	String expirydatedb = ubean.getExpiryDate(session.getAttribute("PLANT").toString());
	String expirydate2arr[] = expirydatedb.split("-");
	String expirydate2 = StrUtils.arrayToString(expirydate2arr, "");
	String actualexpirydate = DateUtils.addByMonth(expirydatedb,1);
	String expirydate1[] = actualexpirydate.split("-");
	String actexpirydate2 = StrUtils.arrayToString(expirydate1, "");
	bflag = miscbean.iSExistStartndEND(expirydate2,actexpirydate2);
	String s3 = session.getAttribute("EXPIRYDATE").toString();
	
	String[] test = null;
	test = s3.split("-");
	StrUtils.reverse(test);
	dateres = StrUtils.arrayToString(test, "-");
	company1 = session.getAttribute("PLANT").toString();
	company1 = company1.toUpperCase();
}
catch(Exception e)
{
	errflg=true;
	System.out.println("in body exception msg" +e.getMessage());
	errmsg =e.getMessage();
}
%>
<script>
function onforward()
{
	document.homeform.action="body.jsp?action="+hme.value;
	document.homeform.submit();
}
</script>
<%
	String serverName = request.getServerName();
	String contxtName = request.getContextPath();
	if (request.getParameter("action") != null) {
		if (request.getParameter("action").equalsIgnoreCase("Home")) {
			response.sendRedirect("http://" + serverName
					+ ":8081/track/home");
		}
	}
%>



<link rel="stylesheet" href="css/style.css" type="text/css" >
</head>
<body bgcolor="#FFFFFF" text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">

<jsp:useBean id="dbean" class="com.track.gates.DbBean" />
<font face="Times New Roman">
<table border="0" cellpadding="0" cellspacing="0" width="100%"	align="center">
	<tr>
		<td width="70%" align="left">
		<form class="form" name="homeform" action="">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				&nbsp;&nbsp;&nbsp;&nbsp;<td align="left" width="15%">
				
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
</font>