<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants" %>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.dao.ItemMstDAO"%>
<%@page import="com.track.dao.CustomerBeanDAO"%>

<%@page import="com.track.dao.MerchantBeanDAO"%>
<%@page import="com.track.dao.CustomerReturnDAO"%><html>
<head>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'CustReturnSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function ExportReport()
{
	 document.form.xlAction.value="GenerateXLSheetForCustomerReturns";
	 document.form.action="customerReturnsSummaryExcel.jsp";
     document.form.submit();
  
}
function onGo(){

   var flag    = "false";

   var FROM_DATE      = document.form.EXPIREDATE.value;
 
   var USER           = document.form.CUST_NAME.value;
   var ITEMNO         = document.form.ITEM.value;
 
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
  
   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   

 document.form.action="customerReturnSummary.jsp";
  document.form.submit();
}

</script>

<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<title>OutBound Order Returns Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();

	CustomerReturnDAO custretdao = new CustomerReturnDAO();
	custretdao.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
	String FROM_DATE = "", TO_DATE = "", DIRTYPE = "", BATCH = "", EXPIREDATE="",USER = "",MERCHANT_NAME="", ITEM = "", 
			fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", PGaction = "",ITEMDESC="";
	
	PGaction = _strUtils.fString(request.getParameter("PGaction")).trim();
	String html = "", cntRec = "false";
	String  fieldDesc="";
	ITEMNO = _strUtils.fString(request.getParameter("ITEM"));
	EXPIREDATE= _strUtils.fString(request.getParameter("EXPIREDATE"));
	CUSTOMER = _strUtils.fString(request.getParameter("CUST_NAME"));
	FROM_DATE =  _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE =  _strUtils.fString(request.getParameter("TO_DATE"));
	ORDERNO = _strUtils.fString(request.getParameter("ORDERNO"));
        System.out.println("From_Date"+FROM_DATE+"To_Date"+TO_DATE);	
        String curDate =new DateUtils().getDate();
        if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	if (PGaction.equalsIgnoreCase("View")) {
		
		try {
			
			 if (_strUtils.fString(ITEMNO).length() >0)
                       {
                         ItemMstUtil itemMstUtil = new ItemMstUtil();
                         itemMstUtil.setmLogger(mLogger);
                         String temItem = itemMstUtil.isValidAlternateItemInItemmst( plant, ITEMNO);
                         if (temItem != "") {
                                ITEMNO = temItem;
                         } else {
                                throw new Exception("Product not found!");
                         }
                      }
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT,plant);
			if(ITEMNO.length()>0)
				ht.put(IDBConstants.ITEM,ITEMNO);
			if(CUSTOMER.length()>0)
				ht.put(IDBConstants.CUSTNAME,CUSTOMER);
			if(ORDERNO.length()>0)
				ht.put(IDBConstants.ORDERNO,ORDERNO);
			if(EXPIREDATE.length()>0)
				ht.put(IDBConstants.EXPIREDATE,EXPIREDATE);
				//ht.put(IDBConstants.EXPIREDATE,DateUtils.getDateinyyyy_mm_dd(EXPIREDATE));
			movQryList = custretdao.CustomerReturnSummary("item,itemdesc,serialno,isnull(custname,'') custname,isnull(orderno,'') orderno,isnull(rsncode,'') rsncode,expiredate,isnull(remarks,'') as remarks, (SUBSTRING(CRAT,7,2) + '/' + SUBSTRING(CRAT,5,2) + '/' + SUBSTRING(CRAT,1,4)) as returndate ",ht,FROM_DATE,TO_DATE,"");
		
				
			if (movQryList.size() <= 0)
				cntRec = "true";

		} catch (Exception e) {
			  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
		}
	}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="customerReturnSummary.jsp">
<input type="hidden" name="xlAction" value=""> <input
	type="hidden" name="PGaction" value="View"> <br>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="20"><font color="white">OutBound Order&nbsp;Returns
		Summary </font></TH>
	</TR>
</TABLE>
<br>
<Center>
<font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
<%=fieldDesc%>
  </font>
  </Center>
  <input type="hidden" name="CUST_CODE1">
  <input type="hidden" name="CUST_CODE">
  
  <input type="hidden" name="L_CUST_NAME">
  <input type="hidden" name="ACTIVE">
<TABLE border="0" width="60%" height="20%" cellspacing="0"
	cellpadding="0" align="center" bgcolor="#dddddd">
	
	<TR>
          <TH ALIGN="right" >&nbsp;From_Date : </TH>
          <TD ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry" READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="right">&nbsp;To_Date : </TH>
          <TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry"  READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
	
    </TR>
    
	
	<TR>
		<TH ALIGN="right">&nbsp;ExpireDate :</TH>
		<TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="EXPIREDATE" type="TEXT" value="<%=EXPIREDATE%>"
			size="20" MAXLENGTH=20 class="inactivegry" READONLY>&nbsp;&nbsp;<a
			href="javascript:show_calendar('form.EXPIREDATE');"
			onmouseover="window.status='Date Picker';return true;"
			onmouseout="window.status='';return true;"><img
			src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		<TH ALIGN="right">&nbsp;Customer Name :</TH>
		<TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="CUST_NAME" type="TEXT" value="<%=CUSTOMER%>"
			size="20" MAXLENGTH=50>
			 <a href="#" onClick="javascript:popUpWin('customer_list.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
			</TD>
	</TR>
	<TR>
		<TH ALIGN="right">&nbsp;Product ID :</TH>
		<TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="ITEM" type="TEXT" value="<%=ITEMNO%>" size="20"
			MAXLENGTH=50>
				</TD>
		<TH ALIGN="left"></TH>
		<TD ALIGN="left"></TD>
	</TR>
	<TR>
		<TH ALIGN="right">&nbsp;Orderno :</TH>
		<TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="20"
			MAXLENGTH=50>
 			
				</TD>
		<TH ALIGN="left"></TH>
		<TD ALIGN="left"><input type="button" value="View"
			onClick="javascript:return onGo();"></TD>
	</TR>
	
	</TABLE>
<br>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">

		<TH><font color="#ffffff" align="center">S/N</TH>
		<TH><font color="#ffffff" align="left"><b>Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Serial No</TH>
		<TH><font color="#ffffff" align="left"><b>Customer Name</TH>
         <TH><font color="#ffffff" align="left"><b>Reason Code</TH>
         <TH><font color="#ffffff" align="left"><b>Return date</TH>
		<TH><font color="#ffffff" align="left"><b>Expire date</TH>
		<TH><font color="#ffffff" align="left"><b>Remarks</TH>
		
	</tr>
	<%
		if (movQryList.size() <= 0 && cntRec == "true") {
	%>
	<TR>
		<TD colspan=15 align=center>No Data For This criteria</TD>
	</TR>
	<%
		}
	%>

	<%
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
			
	%>

	<TR bgcolor="<%=bgcolor%>">
		<TD align="center"><%=iIndex%></TD>
		<TD><%=(String) lineArr.get("item")%></TD>
		<TD align="left"><%=(String) lineArr.get("serialno")%></TD>
		<TD align="left"><%=(String) lineArr.get("custname")%></TD>
		<TD align="left"><%=(String) lineArr.get("rsncode")%></TD>
		<TD align="left"><%=(String) lineArr.get("returndate")%></TD>
		<TD align="left"><%=(String) lineArr.get("expiredate")%></TD>
		<TD align="left"><%=(String) lineArr.get("remarks")%></TD>
	
	</TR>
	<%
		}
	%>
</TABLE>
<br>
<br>
<table align="center">
	<TR>
		<td><input type="button" value=" Back "
			onClick="window.location.href='../home'">&nbsp;</td>
		<%
			//if (movQryList.size() > 0) {
		%>
		<td><input type="button" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
		<%
		//	}
		%>
		
	</TR>

	
</table>
</FORM>
<%@ include file="footer.jsp"%>