<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@page import ="java.text.DecimalFormat" %>
<%@ include file="header.jsp"%>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function onPrint(){
	
    document.form.action="/track/OrderPaymentServlet?action=printAgeingReportbyOrder";
    document.form.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Order Payment Summary(Ageing)  </title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
OrderPaymentUtil pmtutil      = new OrderPaymentUtil();
OrderPaymentDAO _pmtdao=new OrderPaymentDAO();
pmtutil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
CustUtil cUtil = new CustUtil();
CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
ArrayList arrCust = new ArrayList();
session= request.getSession();

String Order="",OrderNo="",PGaction="",cntRec ="false",CustName="",StatementDate="",fromDate="",toDate="";
String fdate="",tdate="";
String plant = (String)session.getAttribute("PLANT");


Order    = _strUtils.fString(request.getParameter("Order"));
OrderNo  = _strUtils.fString(request.getParameter("OrderNo"));
CustName  = _strUtils.fString(request.getParameter("CustName"));
StatementDate  = _strUtils.fString(request.getParameter("StatementDate"));
fdate  = _strUtils.fString(request.getParameter("fromDate"));
tdate  = _strUtils.fString(request.getParameter("toDate"));
PGaction  = _strUtils.fString(request.getParameter("action"));

if (fdate.length()>5)

fromDate    = fdate.substring(6)+"-"+ fdate.substring(3,5)+"-"+fdate.substring(0,2);

if(tdate==null) tdate=""; else tdate = tdate.trim();
if (toDate.length()>5)
toDate    = tdate.substring(6)+"-"+ tdate.substring(3,5)+"-"+tdate.substring(0,2);

if(CustName=="" || CustName.length()<0){
	if(Order.equalsIgnoreCase("OUTBOUND")){
		arrCust = cUtil.getCustomerDetailsForDO(OrderNo, plant);
	}
	else if(Order.equalsIgnoreCase("INBOUND")){
		 arrCust = customerBeanDAO.getVendorDetailsForPO(plant, OrderNo);
	}
	 if(arrCust.size() > 0){
		 CustName = (String) arrCust.get(1);
	 }

}
if(Order.length()==0)Order ="OUTBOUND"; 

if(PGaction.equalsIgnoreCase("View")){
 
 try{
              
       movQryList = pmtutil.getPaymentOrderStatementDetails(plant,fromDate,toDate,Order,OrderNo);
        
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="PaymentOrderStatement.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT name="Order" type = "hidden" value="<%=Order%>" >
<INPUT name="OrderNo" type = "hidden" value="<%=OrderNo%>">
<INPUT name="CustName" type = "hidden" value="<%=CustName%>">
<INPUT name="StatementDate" type = "hidden" value="<%=StatementDate%>">
<INPUT name="fromDate" type = "hidden" value="<%=fromDate%>">
<INPUT name="toDate" type = "hidden" value="<%=toDate%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Order Payment Summary(Ageing) </font></TH>
    </TR>
  </TABLE>
  <br>
  
  <br>
  <TABLE WIDTH="70%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

                <TH><font color="#ffffff" align="center">Date</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                 <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
                <TH><font color="#ffffff" align="left"><b>Amount</TH>
                <TH><font color="#ffffff" align="left"><b>Balance</TH>
                
       </tr>
       <%
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
		  DecimalFormat decformat = new DecimalFormat("#,##0.00");
		  double amount=0;
		  double balance=0;
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               String ordNo = (String)lineArr.get("ORDNO");
          		String ordDate = (String)lineArr.get("TRANDATE");
          			
          		amount = Double.parseDouble((String)lineArr.get("AMOUNT"));
          		amount = StrUtils.RoundDB(amount,2);
          		balance = Double.parseDouble((String)lineArr.get("BALANCE"));
          		balance = StrUtils.RoundDB(balance,2);
          		
          		String amtReceived= decformat.format(amount);
          		String balToPay= decformat.format(balance);
                             
               
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=ordDate%></TD>
            <TD align= "left"><%=ordNo%></TD>
            <TD align= "center"><%=CustName%></TD>
            <TD align= "right"><%=amtReceived%></TD>
            <TD align= "right"><%=balToPay%></TD>
            </TR>           
       <%}%>
	  
  </TABLE>
      <br>
    <table align="center" >
     <TR>
   		<td>  
   			<input type="button" value=" Back " onClick="window.location.href='javascript:history.back()'">&nbsp; 
   			<input type="button" value="Print Ageing Report"  name="action" onclick="javascript:return onPrint();" />
       	</td>
     </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>
