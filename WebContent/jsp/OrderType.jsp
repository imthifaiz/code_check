<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Order Type Master</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'ORDERTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onNew(){
   		document.form1.action  = "/track/ordertypeservlet?action=NEW";
   		document.form1.submit();
	}
	
	function onAdd(){
   		var ORDERTYPE  = document.form1.ORDERTYPE.value;
   		if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType"); return false; }
   		document.form1.action  = "/track/OrderTypeServlet?action=ADD";
   		document.form1.submit();
	}
	
	function onView(){
   		var ORDERTYPE   = document.form1.ORDERTYPE.value;
   		if(ORDERTYPE == "" || ORDERTYPE == null) 
   		{
      		alert("Please Enter OrderType"); 
      		return false; 
   		}
             document.form1.action  = "/track/OrderTypeServlet?action=VIEW";
             document.form1.submit();
	}
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sOrderType = "", sOrderDesc = "", sType="",sRemarks = "";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		sOrderType= "";
		sOrderDesc = "";
		sType="";
		sRemarks = "";
	}  else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
		Hashtable arrCust = (Hashtable) request.getSession()
				.getAttribute("");
		
		sOrderType= (String) arrCust.get("ORDERTYPE");
		sOrderDesc = (String) arrCust.get("ORDERDESC");
		sType=(String) arrCust.get("TYPE");
		sRemarks =(String) arrCust.get("REMARKS");
	
	} else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Create
		Location Master </font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=res%>
</B> <br>
<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">* Order Type :</TH>
		<TD><INPUT name="ORDERTYPE" type="TEXT" value="<%=sOrderType%>"
			size="50" MAXLENGTH=20 <%=sCustEnb%>> 
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Description :</TH>
		<TD><INPUT name="ORDERDESC" type="TEXT" value="<%=sOrderDesc%>"
			size="50" MAXLENGTH=80></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Type :</TH>
		<TD><INPUT name="TYPE" type="TEXT" value="<%=sType%>"
			size="50" MAXLENGTH=80></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remarks :</TH>
		<TD><INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=80></TD>
	</TR>

	<TR>
		<TD COLSPAN=2><BR>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2>
		<center><INPUT class="Submit" type="BUTTON" value="Back"
			onClick="window.location.href='../home'">&nbsp;&nbsp; <INPUT
			class="Submit" type="BUTTON" value="Clear" onClick="onNew();"
			<%=sNewEnb%>>&nbsp;&nbsp; <INPUT class="Submit" type="BUTTON"
			value="Save" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
		</TD>
	</TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

