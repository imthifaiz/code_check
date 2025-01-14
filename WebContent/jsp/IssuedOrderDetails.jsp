<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants" %>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">
function ExportReport()
{
   document.form1.action = "/track/ReportServlet?action=ExportIssuedOrderDetails";
   document.form1.submit();
  
}
</script>

<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<title>Issued Order Details</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
<br>
                  
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Outbound&nbsp;Order Details </font></TH>
		
	</TR>
</TABLE>
<br>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">

		<TH><font color="#ffffff" align="left"><b>Order No</TH>
		<TH><font color="#ffffff" align="left"><b>Line no</TH>
		<TH><font color="#ffffff" align="left"><b>Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Description</TH>
		<TH><font color="#ffffff" align="left"><b>Batch</TH>
		<TH><font color="#ffffff" align="left"><b>Loc</TH>
		<TH><font color="#ffffff" align="left"><b>Unitprice</TH>
		<TH><font color="#ffffff" align="left"><b>UOM</TH>
		<TH><font color="#ffffff" align="left"><b>Order Qty</TH>
		<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>
	</tr>
	
<%
StrUtils _strUtils = new StrUtils();
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
_ShipHisDAO.setmLogger(mLogger);
ArrayList movQryList =  null;

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String DONO="",PGaction="",fieldDesc="",DATE="",TODATE="",ISSUEDBY="";
	DONO = _strUtils.fString(request.getParameter("DONO"));
	DATE = _strUtils.fString(request.getParameter("DATE"));
	ISSUEDBY =  _strUtils.fString(request.getParameter("ISSUEDBY"));
	
	double ordqtytotal=0,issueqtytotal=0;
	int k=0;
		
	
	
	try{
		
		movQryList =  _ShipHisDAO.getIssuedOutboundOrderDetails(plant,DONO,DATE,TODATE);
	
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
			//Float ordqty =  Float.parseFloat(((String) lineArr.get("ordqty").toString())) ;
			//Float recqty =  Float.parseFloat(((String) lineArr.get("recqty").toString())) ;
			
	%>
	

	<TR bgcolor="<%=bgcolor%>">
		<TD width="10%"><%=(String) lineArr.get("dono")%></TD>
		<TD align= "center" width="5%"><%=(String)lineArr.get("dolno")%></TD>
		<TD align= "center" width="12%"><%=(String)lineArr.get("item")%></TD>
		<TD align="center" width="15%"><%=(String) lineArr.get("itemdesc")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("batch")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("loc")%></TD>
		<TD align="right" width="6%"><%=_strUtils.currencyWtoutSymbol((String) lineArr.get("unitprice"))%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("uom")%></TD>
		<TD align="right" width="6%"><%=_strUtils.formatNum((String)lineArr.get("ordqty"))%></TD>
		<TD align="right" width="6%"><%=_strUtils.formatNum((String)lineArr.get("pickqty"))%></TD>
		
		
	</TR>
 <INPUT type = "hidden" name="DONO" value = "<%=DONO%>">
 <INPUT type = "hidden" name="DATE" value = "<%=DATE%>">
 <INPUT type = "hidden" name="TODATE" value = "<%=TODATE%>">
<%

		//ordqtytotal   = ordqtytotal+ordqty ;
	issueqtytotal   = issueqtytotal+Double.parseDouble((String)lineArr.get("pickqty"));
		
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	if (movQryList.size() >0)
	{ 
		k=k+1;
		String bgItemcolor = ((k == 0) || (k % 2 == 0)) ? "#FFFFFF"
			: "#dddddd";
%>
	<TR BGCOLOR="<%=bgItemcolor %>">
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align= "right" ></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="right"></TD>
	<TD align= "right" ><b>Total:</b></TD>
	<TD align="right"><b><%=_strUtils.formatNum((new Double(issueqtytotal).toString()))%></b></TD>
	

	</TR>
<% 
	}
	//ordqtytotal=0;
	issueqtytotal=0;
	if(k==0)
	k=1;


%>

</TABLE>
<br>
<table align="center">
	<TR>
		<td><input type="button" value="Back" onClick="window.location.href='IssuedOrdersHistory.jsp'">&nbsp;</td>
		<td><input type="button" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
		
	</TR>

	
</table>


</FORM>
</html>
<%@ include file="footer.jsp"%>