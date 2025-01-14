<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@page import ="java.text.DecimalFormat" %>
<%@ include file="header.jsp"%>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

DateUtils _dateUtils = new DateUtils();
String action   = su.fString(request.getParameter("action")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
String result   = su.fString(request.getParameter("result")).trim();

String fieldDesc="";
String orderType ="",orderNo="";
String paymentRefNo="",paymentRemarks="",paymentDate="",paymentMode="",paymentType="",paymentId="";

orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
orderNo     = StrUtils.fString(request.getParameter("ORDERNO"));
paymentDate     = StrUtils.fString(request.getParameter("PMTDATE"));
paymentRefNo     = StrUtils.fString(request.getParameter("PMTREFNO"));
paymentMode     = StrUtils.fString(request.getParameter("PMTMODE"));
paymentRemarks     = StrUtils.fString(StrUtils.replaceCharacters2Recv(request.getParameter("PMTREMARKS")));
paymentType     = StrUtils.fString(request.getParameter("PMTTYPE"));
paymentId     = StrUtils.fString(request.getParameter("PMTID"));
 
%>
<html>
<head>
<title>Order Payment Details</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript">
var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'OrderPaymentDetais', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=700,left = 200,top = 184');
}


</script>
</head>

<%@ include file="body.jsp"%>

<body>
<form name="form" method="post" action="">
<br>
<table border="1" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Order Payment Details</FONT></TH>
</table>
<br>

<table  width="90%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd" >
			<tr>
				<th WIDTH="20%" ALIGN="left"><b>Payment Date :  </b></th>
				<td><INPUT name="paymentDate" id="paymentDate" type="TEXT" size="20" value = '<%=paymentDate%>' MAXLENGTH="30" readonly="readonly"></TD>
				<th WIDTH="20%" ALIGN="left">Payment Mode : </th>
			    <TD><INPUT name="paymentMode" id="paymentMode" type="TEXT" size="20" value = '<%=paymentMode%>' MAXLENGTH="30" readonly="readonly"></TD>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Payment Type : </th>
				<TD><INPUT name="paymentMode" id="paymentMode" type="TEXT" size="20" value = '<%=paymentType%>' MAXLENGTH="30" readonly="readonly"></TD>
				<th WIDTH="20%" ALIGN="left">Order Type :</th>
                 <td><INPUT type="TEXT" size="20" MAXLENGTH="20" name="ORDERTYPE" value='<%=orderType%>' MAXLENGTH="30" readonly="readonly">
				 </td>
			</tr>			
			<tr>
				<TH WIDTH="20%" ALIGN="left">Payment ID:</TH>
				<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=paymentId%>" size="20" MAXLENGTH=30 readonly="readonly"> 
			 	</TD>
				<th WIDTH="20%" ALIGN="left">Payment Reference :</th>
				<TD><INPUT name="paymentRefNo" type="TEXT" value="<%=su.forHTMLTag(paymentRefNo)%>" size="20" MAXLENGTH=30 readonly="readonly"></TD>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Payment Remarks :</th>
				 <TD width="20%"><TEXTAREA NAME="paymentRemarks" COLS=40 ROWS=3 readonly="readonly" ><%=paymentRemarks%></TEXTAREA></TD>  	
				
			</TR>
				</table>
				</center>	
				<br>
<table align="center">
	<TR>
		<td><input type="button" value="Close" onclick="window.close();">&nbsp;</td>
	
	</TR>

	
</table>
				
					
</form>
</body>
</html>
<%@ include file="footer.jsp"%>