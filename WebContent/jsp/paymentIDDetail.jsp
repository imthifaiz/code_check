<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Payment ID Details</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
	function popWin(URL) {
		subWin = window
				.open(
						URL,
						'PRODUCT',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}

	
</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";
	
	String action = "";
	String spaymentId = "", spaymentDesc = "", isActive = "";

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	PrdClassUtil prdclsutil = new PrdClassUtil();

	prdclsutil.setmLogger(mLogger);

	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	spaymentId = strUtils.fString(request.getParameter("PAYMENT_ID"));
	spaymentDesc = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("PAYMENT_DESC")));
	isActive = strUtils.fString(request.getParameter("ISACTIVE"));
	
%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Payment ID Details</font></TH>
	</TR>
</TABLE>
<br>
<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Payment ID:</TH>
		<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=spaymentId%>"
			size="50" MAXLENGTH=20> 
		</TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Payment ID Description :</TH>
		<TD><INPUT name="PAYMENT_DESC" type="TEXT" value="<%=spaymentDesc%>"
			size="50" MAXLENGTH=50></TD>

	</TR>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD align=" left"><INPUT name="ACTIVE" type="radio" value="Y"
			disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked
			<%}%>>Active <INPUT name="ACTIVE" type="radio" value="N"
			disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked
			<%}%>>Non Active</TD>
	</tr>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>


