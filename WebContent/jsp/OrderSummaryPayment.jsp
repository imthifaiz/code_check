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

function ExportReport()
{ 
  var flag    = "false";
  var FROM_DATE      = document.form.FROM_DATE.value;
  var ORDER           = document.form.Order.value;
  
  if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
  if(ORDER != null     && ORDER != "") { flag = true;}

  if(flag == "false"){ alert("Please define any one search criteria"); return false;}
  
  document.form.action="/track/ReportServlet?action=ExportPaymentSummary";
  document.form.submit();
 }

function onGo(){

   var flag    = "false";
  
   var FROM_DATE      = document.form.FROM_DATE.value;
   var TO_DATE        = document.form.TO_DATE.value;
   var ORDER           = document.form.Order.value;
   var CUSTNAME         = document.form.custName.value;
   var PMTREFNO        = document.form.paymentRefNo.value;
   var ORDNO          = document.form.orderNo.value;
   var PMTMODE      = document.form.paymentMode.value;
   var PMTTYPE      = document.form.paymentType.value;
   var ODRTYPE      = document.form.ORDERTYPE.value;
   var PMTID      = document.form.PAYMENT_ID.value;
   
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(ORDER != null     && ORDER != "") { flag = true;}

   if(CUSTNAME != null    && CUSTNAME != "") { flag = true;}
   if(PMTREFNO != null     && PMTREFNO != "") { flag = true;}
   
    if(ORDNO != null     && ORDNO != "") { flag = true;}
    if(PMTMODE != null     && PMTMODE != "") { flag = true;}

    if(PMTTYPE != null     && PMTTYPE != "") { flag = true;}
    if(ODRTYPE != null     && ODRTYPE != "") { flag = true;}
    if(PMTID != null     && PMTID != "") { flag = true;}
    
   

  if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form.action="OrderSummaryPayment.jsp";
  document.form.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Payment Summary </title>
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


session= request.getSession();

float ordamttot=0,paidamttot=0,ord_amt=0,paid_amt=0;String rowColor="";int k=0;	
String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="",PGaction="";
String Order="",orderType="",orderNo="",custName="",paymentRefNo="",paymentMode="",paymentType="",paymentId="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String plant = (String)session.getAttribute("PLANT");
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


Order = _strUtils.fString(request.getParameter("Order"));
custName     = _strUtils.fString(StrUtils.replaceCharacters2Recv(request.getParameter("custName")));
paymentRefNo     = _strUtils.fString(request.getParameter("paymentRefNo"));
orderNo     = _strUtils.fString(request.getParameter("orderNo"));
paymentMode     = _strUtils.fString(request.getParameter("paymentMode"));
paymentType     = _strUtils.fString(request.getParameter("paymentType"));
orderType = _strUtils.fString(request.getParameter("ORDERTYPE"));
paymentId     = _strUtils.fString(request.getParameter("PAYMENT_ID"));

if(Order.length()==0)Order ="OUTBOUND"; 

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        Hashtable ht = new Hashtable();
      
        if(_strUtils.fString(Order).length() > 0)        ht.put("ORDERNAME",Order);
        if(_strUtils.fString(paymentRefNo).length() > 0) ht.put("PAYMENT_REFNO",paymentRefNo);
        if(_strUtils.fString(orderNo).length() > 0)      ht.put("ORDNO",orderNo);
        if(_strUtils.fString(paymentMode).length() > 0)  ht.put("PAYMENT_MODE",paymentMode);
        if(_strUtils.fString(paymentType).length() > 0)  ht.put("PAYMENT_TYPE",paymentType);
        if(_strUtils.fString(orderType).length() > 0)    ht.put("ORDERTYPE",orderType);
        if(_strUtils.fString(paymentId).length() > 0)    ht.put("PAYMENT_ID",paymentId);
       
       movQryList = pmtutil.getPaymentSummaryDetails(ht,fdate,tdate,plant,Order,custName);
        
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="OrderSummaryPayment.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT name="ACTIVE" type = "hidden" >
<INPUT name="PAYMENT_DESC" type = "hidden" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Payment Summary </font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
 
  
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
       <tr>
       		<th WIDTH="20%" ALIGN="left">&nbsp;Order :</th>
			<TD><select name="Order" >
					<option value="INBOUND" <%if(Order.equalsIgnoreCase("INBOUND")){ %>selected<%} %>>INBOUND</option>
					<option value="OUTBOUND" <%if(Order.equalsIgnoreCase("OUTBOUND")){ %>selected<%} %> >OUTBOUND</option>
					<option value="OTHERS" <%if(Order.equalsIgnoreCase("OTHERS")){ %>selected<%} %> >OTHERS</option>
				</select>
			</TD>
			<th WIDTH="20%" ALIGN="left">Customer/Supplier :</th>
			<TD><INPUT name="custName" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="20" MAXLENGTH=80>
				 <a href="#" onClick="javascript:popUpWin('list/orderContactPersonList.jsp?custName='+form.custName.value+'&orderType='+form.Order.value);"> <img src="images/populate.gif" border="0" /> </a></TD>
		</tr>
		<tr>
			    <th WIDTH="20%" ALIGN="left">&nbsp;Payment Ref No :</th>
				<TD><INPUT name="paymentRefNo" type="TEXT" value="<%=su.forHTMLTag(paymentRefNo)%>" size="20" MAXLENGTH=80></TD>
				<th WIDTH="20%" ALIGN="left">Order No :</th>
			<TD><INPUT name="orderNo" type="TEXT" value="<%=su.forHTMLTag(orderNo)%>" size="20" MAXLENGTH=80></TD>
		</tr>
    
     <TR>
            <th WIDTH="20%" ALIGN="left">&nbsp;Payment Mode : </th>
			<TD><select name="paymentMode">
				<option selected value="">Choose</option>
				<option value="GIRO" <%if(paymentMode.equalsIgnoreCase("GIRO")){ %>selected<%} %>>GIRO</option>
				<option value="CHEQUE" <%if(paymentMode.equalsIgnoreCase("CHEQUE")){ %>selected<%} %> >CHEQUE</option>	
      			<option value='CASH' <%if(paymentMode.equalsIgnoreCase("CASH")){ %>selected<%} %>>CASH</OPTION>
      			<option value='OTHERS' <%if(paymentMode.equalsIgnoreCase("OTHERS")){ %>selected<%} %>>OTHERS</OPTION>
				<option value="ELECTRONIC TRANSFER" <%if(paymentMode.equalsIgnoreCase("ELECTRONIC TRANSFER")){ %>selected<%} %>>ELECTRONIC TRANSFER</option>
				<option value="NETS" <%if(paymentMode.equalsIgnoreCase("NETS")){ %>selected<%} %> >NETS</option>	
      			<option value='CREDIT CARD' <%if(paymentMode.equalsIgnoreCase("CREDIT CARD")){ %>selected<%} %>>CREDIT CARD</OPTION>
      			<option value='VOUCHER' <%if(paymentMode.equalsIgnoreCase("VOUCHER")){ %>selected<%} %>>VOUCHER</OPTION>
      			</select>
      			</TD>
				
			<th WIDTH="20%" ALIGN="left">Payment Type : </th>
			<TD><select name="paymentType">
				<option selected value="">Choose</option>
				<option value="CREDIT" <%if(paymentType.equalsIgnoreCase("CREDIT")){ %>selected<%} %>>CREDIT</option>
				<option value="DEBIT" <%if(paymentType.equalsIgnoreCase("DEBIT")){ %>selected<%} %> >DEBIT</option>	
      		</TD>
					         
             </TR>
		<TR>
            <th WIDTH="20%" ALIGN="left">&nbsp;Order Type :</th>
            <td><INPUT type="TEXT" size="20" MAXLENGTH="20" name="ORDERTYPE" value="<%=orderType%>" />
				 <a href="#" onClick="javascript:popUpWin('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE=PMTSMRY');"><img src="images/populate.gif" border="0"></a>
			</td>
			<TH WIDTH="20%" ALIGN="left"> Payment ID:</TH>
			<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=paymentId%>" size="20" MAXLENGTH=50> 
				<a href="#" onClick="javascript:popUpWin('PaymentIdList.jsp?PAYMENT_ID='+form.PAYMENT_ID.value);">
			 	<img src="images/populate.gif" border="0"></a>
			 	 <input type="button" value="View"  onClick="javascript:return onGo();">
			 	
			 </TD>
          </TR>
   
  </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Order Type</TH>
                <TH><font color="#ffffff" align="left"><b>Customer/Supplier</TH>
                <TH><font color="#ffffff" align="left"><b>Payment Ref No</TH>
                <TH><font color="#ffffff" align="left"><b>Payment Date</TH>
                <TH><font color="#ffffff" align="left"><b>Payment Type</TH>
                <TH><font color="#ffffff" align="left"><b>Payment Mode</TH>
                <TH><font color="#ffffff" align="left"><b>Payment ID</TH>
                <TH><font color="#ffffff" align="right"><b>Orig Amt</TH>
                <TH><font color="#ffffff" align="right"><b>Paid Amt</TH>
                <TH><font color="#ffffff" align="right"><b>Outstg Amt</TH>
                <TH><font color="#ffffff" align="left"><b>Remarks</TH>
                
       </tr>
       <%
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
		  DecimalFormat decformat = new DecimalFormat("#,##0.00");
		  String lastordno = "";double amount=0;
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               k=k+1;
               int iIndex = iCnt + 1;
               String color="000000";
               rowColor = ((k == 0) || (k % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               String ordNo = (String)lineArr.get("ordno");
               String order = (String)lineArr.get("ordername");
               String payment_type = (String)lineArr.get("payment_type");
               
               double amtOrd = Double.parseDouble((String)lineArr.get("orderAmt"));
               amtOrd = StrUtils.RoundDB(amtOrd,2);
                double paidamt = Double.parseDouble((String)lineArr.get("amount_paid"));
                
                
                if((!lastordno.equalsIgnoreCase("") && !lastordno.equalsIgnoreCase(ordNo)) )
                {
              	  amount = 0;
                }
                               
                amount = amount + paidamt;
                
                paidamt = StrUtils.RoundDB(paidamt,2);
                
               /* String totpaid =  _pmtdao.gettotalpaidamt(plant, ordNo);
                double DueToPay =  amtOrd - Double.parseDouble(totpaid);
                DueToPay = StrUtils.RoundDB(DueToPay,2);
               
                double totpaid = Double.parseDouble((String)lineArr.get("amtReceived"))-Double.parseDouble((String)lineArr.get("returnamt"));
                double DueToPay =  amtOrd - totpaid;*/
                double DueToPay =  amtOrd - amount;
                DueToPay = StrUtils.RoundDB(DueToPay,2);
                
                String amtOrdered= decformat.format(amtOrd);
                String amtpaid= decformat.format(paidamt);
                String amtbal = decformat.format(DueToPay);
                String fontcolor="";
                if(amtbal.indexOf("-")!=-1)
                {
                	amtbal = amtbal.replaceAll("[-]","");
                	fontcolor = "#FF0000";
                	
                }
                
                
                if(order.equalsIgnoreCase("OUTBOUND"))
                {
                	if(payment_type.equalsIgnoreCase("DEBIT"))
                		{
                			paid_amt = -(Float.parseFloat(((String)lineArr.get("amount_paid").toString())));
                			color = "#FF0000";
                		}
                	else
                		{
                			paid_amt = Float.parseFloat(((String)lineArr.get("amount_paid").toString()));
						}
                		
                }
                else if(order.equalsIgnoreCase("INBOUND"))
                {
                	if(payment_type.equalsIgnoreCase("CREDIT"))
                		{
                			paid_amt = -(Float.parseFloat(((String)lineArr.get("amount_paid").toString())));
                			color = "#FF0000";
                		}
                	else
                		{
                			paid_amt = Float.parseFloat(((String)lineArr.get("amount_paid").toString()));
						}
                		
                }
                if(order.equalsIgnoreCase("others"))
                {
                	amtbal="0.00";
                	fontcolor="";
                	paid_amt = Float.parseFloat(((String)lineArr.get("amount_paid").toString()));
                	if(payment_type.equalsIgnoreCase("DEBIT")){
                		color = "#FF0000";
                		paid_amt = -paid_amt;
                	}
                }
               
                
              if(!lastordno.equalsIgnoreCase(ordNo)){  
              	ordamttot=ordamttot+Float.parseFloat(((String)lineArr.get("orderAmt").toString()));
              	ordamttot = StrUtils.Round(ordamttot,2);
              	
              }
              
              paidamttot=paidamttot+paid_amt;
              paidamttot = StrUtils.Round(paidamttot,2);
              
              lastordno = ordNo;
               
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD align="center"><%=(String)lineArr.get("ordno")%></TD>
            <TD align= "left"><%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left"><%=(String)lineArr.get("custname")%></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_refno")%></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_dt")%></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_type")%></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_mode") %></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_id") %></TD>
            <TD align= "right"><%=amtOrdered%></TD>
            <TD align= "right"><font color="<%=color%>"><%=amtpaid%></font></TD>
            <TD align= "right"><font color="<%=fontcolor%>"><%=amtbal%></font></TD>
            <TD align= "left"><%=(String)lineArr.get("payment_remarks") %></TD> 
	        
           </TR>
           
       <%}%>
	  <%
       
       if(movQryList.size()>0){ %>
       
        <TR bgcolor = "<%=rowColor%>">
        <TD align= "center"></TD>
        <TD align= "center"></TD>
        <TD align= "center"></TD>
        <TD align= "center"></TD>
        <TD align= "center"></TD>
        <TD align= "center"></TD> 
        <TD align= "center"></TD>
        <TD align= "center"></TD>
        <TD align= "right" ><b>Total:</b></TD>
        <TD align= "right" ><b><%=StrUtils.currencyWtoutSymbol(String.valueOf(ordamttot))%></b></TD>  
        <TD align= "right" ><b><%=StrUtils.currencyWtoutSymbol((String.valueOf(paidamttot))) %></b></TD>
        <TD align="center" ></TD>
        <TD align="center" ></TD>
       
    </TR>
   
     <% 
     	} 
      %> 
  </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" > </td>
      </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>
