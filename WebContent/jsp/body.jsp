<script language="javascript" src="js/sniffer.js"></script>
<%@ page import="com.track.util.*"%>
<jsp:useBean id="miscbean" class="com.track.gates.miscBean" />
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<script>
function convertCharToString(char){
	var str="";
	for(var i=0;i<char.length;i++){
		if (char[i] == "~")
	    	str = str.concat("SCTILDESC");
		else if (char[i] ==("`"))
			str = str.concat("SCBACKTICKSC");
		else if (char[i] ==("!"))
			str = str.concat("SCEXCLAMATIONSC");
		else if (char[i] ==("@"))
			str = str.concat("SCATTHERATESC");
		else if (char[i] ==("#"))
			str = str.concat("SCHASHSC");
		else if (char[i] ==("$"))
			str = str.concat("SCDOLLARSC");
		else if (char[i] ==("%"))
			str = str.concat("SCPERCENTAGESC");	
		else if (char[i] ==("^"))
			str = str.concat("SCCARETSC");
		else if (char[i] ==("&"))
			str = str.concat("SCAMPRASANDSC");		
		else if (char[i] ==("*"))
			str = str.concat("SCASTERISKSC");	
		else if (char[i] ==("("))
			str = str.concat("SCLEFTPARENTHESISSC");		
		else if (char[i] ==(")"))
			str = str.concat("SCRIGHTPARENTHESISSC");		
		else if (char[i] ==("_"))
		str = str.concat("SCUNDERSCORESC");		
		else if (char[i] ==("-"))
			str = str.concat("SCMINUSSC");	
		else if (char[i] ==("+"))
			str = str.concat("SCPLUSSC");		
		else if (char[i] ==("="))
			str = str.concat("SCEQUALSIGNSC");	
		else if (char[i] ==("{"))
			str = str.concat("SCLEFTBRACESC");	
		else if (char[i] ==("}"))
			str = str.concat("SCRIGHTBRACESC");		
		else if (char[i] ==("]"))
			str = str.concat("SCRIGHTBRACKETSC");		
		else if (char[i] ==("\\["))
		str = str.concat("SCLEFTBRACKETSC");		
		else if (char[i] ==("|"))
			str = str.concat("SCVERTICALBARSC");	
		else if (char[i] ==("\\"))
			str = str.concat("SCBACKSLASHSC");		
		else if (char[i] ==(":"))
			str = str.concat("SCKOLONSC");	
		else if (char[i] ==(";"))
			str = str.concat("SCSEMOCOLONSC");		
		else if (char[i] ==("\""))
			str = str.concat("SCDOUBLEQOUTSSC");		
		else if (char[i] ==("\'"))
			str = str.concat("SCSINGLEQOUTSSC");		
		else if (char[i] ==("<"))
			str = str.concat("SCLESSTHANSC");		
		else if (char[i] ==(">"))
			str = str.concat("SCGREATERTHANSC");	
		else if (char[i] ==("."))
			str = str.concat("SCFULLSTOPSC");		
		else if (char[i] ==("?"))
			str = str.concat("SCQUESTIONMARKSC");	
		else if (char[i] ==("/"))
			str = str.concat("SCSLASHSC");
		else if (char[i] ==(","))
			str = str.concat("SCCOMMASC");
		else {
			str = str.concat(char[i]);
		   }	   
	}
	return str;

	}
</script>
<%
String errmsg="Expirydate not found";
boolean bflag=false,errflg=false;String company1="",dateres="",companyname="";
try{
HashMap<String, String> loggerDetailsHasMap2 = new HashMap<String, String>();
loggerDetailsHasMap2.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap2.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger2 = new MLogger();
mLogger2.setLoggerConstans(loggerDetailsHasMap2);
ubean.setmLogger(mLogger2);
	session = request.getSession();
	//Retrieve expiry date
	String expirydatedb = ubean.getExpiryDate(session.getAttribute("PLANT").toString());
	companyname = ubean.getCompanyName(session.getAttribute("PLANT").toString()).toUpperCase();
	//String expirydatedb = session.getAttribute("ACTUALEXPIRY").toString();
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
					+ ":8080/track/home");
		}
	}
%>

<%@ include file="links/sessionMenuLinks.jsp"%>
<script language="javascript1.2" src="js/style.js"></script>
</head>
<body bgcolor="#ffffff" text="#000000" link="#efe7c6" alink="#9c8349"
	vlink="#535353">
<jsp:useBean id="dbean" class="com.track.gates.DbBean" />
<font face="Times New Roman">
<table border="0" cellpadding="0" cellspacing="0" width="100%"
	align="center">
	<tr>
		<td width="70%" align="left">
		<form name="homeform" action="">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			<td>
			  <img border="0" src="images/track/blank.gif"
						 width="30"	height='47'>
						
		    </td>
				
					
					<td>
						<A	href="../home"><img border="0" src="images/track/home.png"
							height='47'>
						</A>
					</td>
					<td></td>
					<td>
					
					<img border="0" src="images/track/blank.gif"
						 width="2"	height='47'>
						
					</td>
					
					<td>
						<A href="logout.jsp"><img border="0" src="images/track/logout.png"
							height='47'>
						</A>
					</td>
					
				
				<td valign="bottom" width="15%"><INPUT type="hidden"
					name="action" value="Home" onClick="return onforward();"></td>
				<td align="center" width="70%"><font class="mainred"><%=dbean.getBroadCastMessage()%><%if(errflg==true){ %><%=errmsg%><%} %></font></td>
			</tr>
		</table>
		</form>
		</td>
		<td width="60%" align="right">
		<table border="0" cellpadding="0" cellspacing="0" width="1000"
			align="right">
			<tr align="right" valign="top">
				<td width="78%" align="right"><font color="#666666" size="2"
					face="Arial, Helvetica, sans-serif"><b>User&nbsp;ID : </b></font></td>
				<td width="22%" align="left">&nbsp;<font color="#666666"
					size="2" face="Arial, Helvetica, sans-serif"><b><%=session.getAttribute("LOGIN_USER").toString()%></b></font></td>
			</tr>
			<tr align="right" valign="top">
				<td width="78%" align="right"><font color="#666666" size="2"
					face="Arial, Helvetica, sans-serif"><b>Company : </b></font></td>
				<td width="22%" align="left">&nbsp;<font color="#666666"
					size="2" face="Arial, Helvetica, sans-serif"><b><%=companyname%></b></font></td>
			</tr>
			<%
				if (bflag == true) {
			%>
			<tr align="right" valign="top">
				<td width="78%" align="right"><font color="Red"><b><font
					size="2" face="Arial, Helvetica, sans-serif">Expiry&nbsp;Date
				</font>: </b></font></td>
				<td width="22%" align="left">&nbsp;<font color="Red" size="2"
					face="Arial, Helvetica, sans-serif"><b><%=dateres%></b></font></td>
			</tr>
			<%
				} else {
			%>
			<tr align="right" valign="top">
				<td width="78%" align="right"><font color="#666666"><b><font
					size="2" face="Arial, Helvetica, sans-serif">Expiry&nbsp;Date
				</font>: </b></font></td>
				<td width="22%" align="left">&nbsp;<font color="#666666"
					size="2" face="Arial, Helvetica, sans-serif"><b><%=dateres%></b></font></td>
			</tr>
			<%
				}
			%>
			
		</table>
		</td>
	</tr>
</table>
<table>
 <tr></tr>
</table>
<br></br>
</font>
