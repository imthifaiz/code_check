<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@page import ="java.text.DecimalFormat" %>
<%@ include file="header.jsp"%>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

POUtil _poUtil=new POUtil();
DOUtil _doUtil=new DOUtil();
_poUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
String action   = su.fString(request.getParameter("action")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
String result   = su.fString(request.getParameter("result")).trim();


String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
String Order="",orderType ="",ordRefNo = "",orderNo="",custName = "",chkString ="";;
String paymentRefNo="",payment="",paymentRemarks="",paymentDate="",paymentMode="",paymentType="",spaymentId="";

Order = StrUtils.fString(request.getParameter("Order"));
orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
orderNo     = StrUtils.fString(request.getParameter("orderNo"));
ordRefNo     = StrUtils.fString(request.getParameter("ordRefNo"));
custName     = StrUtils.fString(StrUtils.replaceCharacters2Recv(request.getParameter("custName")));
paymentDate     = StrUtils.fString(request.getParameter("paymentDate"));
paymentRefNo     = StrUtils.fString(request.getParameter("paymentRefNo"));
paymentMode     = StrUtils.fString(request.getParameter("paymentMode"));
paymentRemarks     = StrUtils.fString(request.getParameter("paymentRemarks"));
paymentType     = StrUtils.fString(request.getParameter("paymentType"));
spaymentId     = StrUtils.fString(request.getParameter("PAYMENT_ID"));
payment     = StrUtils.fString(request.getParameter("payment"));
String AFLAG=    (String) session.getAttribute("AFLAG");

if(Order.length()==0)
{Order ="OUTBOUND";paymentType="CREDIT";}                           
String curDate =_dateUtils.getDate();
if(paymentDate.length()<0|paymentDate==null||paymentDate.equalsIgnoreCase(""))paymentDate=curDate;
ArrayList al= new ArrayList();

System.out.println(" action :: "+action);
if(action.equalsIgnoreCase("View")){
  al = (ArrayList)request.getSession().getAttribute("PAYMENT_ORDERS");
  fieldDesc="";
   }
if(result.equalsIgnoreCase("sucess"))
{
 fieldDesc=su.fString((String)request.getSession().getAttribute("RESULT"));
} 
else if(result.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=su.fString((String)request.getSession().getAttribute("CATCHERROR"));
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  
} 

else if(result.equalsIgnoreCase("error"))
{
  fieldDesc=su.fString((String)request.getSession().getAttribute("RESULTERROR"));
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
 
} 
 
%>
<html>
<head>
<title>Create Payment Receipt</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript">
var subWin = null;
/*function setpaymenttype()
{	var order = document.form.Order.value;
	if(order=="OUTBOUND"){
	  document.getElementById("paymentType").value = "CREDIT";
    }
    else if(order=="INBOUND"){
    	document.getElementById("paymentType").value = "DEBIT";
     }
    else{
    	document.getElementById("paymentType").value = "";
	}

}*/

function showpaymentdiv()
{	var order = document.form.Order.value;
	if(order=="OTHERS"){
	  document.getElementById("divpayment").style.display = "inline";
    }
    else{
     document.getElementById("divpayment").style.display = "none";
     }
	if(order=="OUTBOUND"){
		  document.getElementById("paymentType").value = "CREDIT";
	    }
	    else if(order=="INBOUND"){
	    	document.getElementById("paymentType").value = "DEBIT";
	     }
	    else{
	    	document.getElementById("paymentType").value = "";
		}
}

function popUpWin(URL) {
 subWin = window.open(URL, 'OrderPaymentDetais', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=700,left = 200,top = 184');
}


function popUpWin1(URL) {
 subWin = window.open(URL, 'OrderPaymentDetais', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function viewOrdersDueForPaymnet(){
	
	 var flag    = "false";
	   var order = document.form.Order.value;
	   var orderNo    = document.form.orderNo.value;
	   var ordRefNo      = document.form.ordRefNo.value;
	   var custName      = document.form.custName.value;
	  
	   if(orderNo != null     && orderNo != "") { flag = true;}
	   if(ordRefNo != null    && ordRefNo != "") { flag = true;}
	   if(custName != null     && custName != "") { flag = true;}
	   if(order !="OTHERS"){
	   		if(flag == "false"){ alert("Please define any one search criteria"); return false;}
	   }
			document.form.action="/track/OrderPaymentServlet?action=viewPaymentOrders";
            document.form.submit();
		}
function onReceivePayment(form){
 	var Traveler ;
 	var concatTraveler="";
 	var order = document.form.Order.value;
 	var refno = document.form.paymentRefNo.value;
 	
 	/*if(order != "OTHERS"){
 	 	var count = $("[type='checkbox']:checked").length;
 		if(count > 1)
 		{
 			alert ("Please check only one checkbox.");
		    return false;
 		}
 	}*/
 	if(document.form.paymentMode.selectedIndex==0)
 	{
 		alert("Please choose payment Mode");
 		return false;	
 	}
 	if(document.form.paymentType.selectedIndex==0)
 	{
 		alert("Please choose payment Type");
 		return false;	
 	}
 	if (refno == "" || refno.length == 0 ) {
		alert("Enter Payment Reference Number");
		document.form.paymentRefNo.focus();
		return false;
	}
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	var order = document.form.Order.value;
 	if(order == "OTHERS"){
 		var payment = document.form.payment.value;
 		if (payment == "" || payment.length == 0 || payment == '0') {
			alert("Enter a valid payment Amount!");
			document.form.payment.focus();
			document.form.payment.select();
	        return false;
		}
	if(!isNumericInput(payment)){
			alert("Entered Amount is not a valid number!");
			document.form.payment.focus();
			document.form.payment.select();
	         return false;
		}
	if (payment.indexOf('.') == -1) payment += ".";
		var decNum = payment.substring(payment.indexOf('.')+1, payment.length);
		if (decNum.length > 2)
		{
			alert("Invalid more than 2 digits after decimal in Payment");
			document.form.payment.focus();
			return false;
			
		}
 		
 	}
 	else{
    var checkFound = false;  
	 var orderNo;
	 var len = document.form.chkdOrdNo.length; 
	 if(len == undefined) len = 1;
    for (var i = 0; i < len ; i++)
    {
    	if(len == 1 && (!document.form.chkdOrdNo.checked))
		{
			checkFound = false;
		}
    	
    	/*else if(len ==1 && document.form.chkdOrdNo.checked)
    	{
    		chkstring = document.form.chkdOrdNo.value;
    	}
    	
    	else if
    	{
    		chkstring = document.form.chkdOrdNo[i].value;
    	}
    	chkdvalue = chkstring.split(',');
		*/
		
		else if(len ==1 && document.form.chkdOrdNo.checked)
	     {
	    	 checkFound = true;
	    	 chkstring = document.form.chkdOrdNo.value;
	    	 chkdvalue = chkstring.split(',');
	    	 document.form.TRAVELER.value=document.form.chkdOrdNo.value+"=";
	    	 orderNo = chkdvalue[0];
	    	if(!verifyPayment(orderNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdOrdNo[i].checked){
		    	 checkFound = true;
		    	 chkstring = document.form.chkdOrdNo[i].value;
		    	 chkdvalue = chkstring.split(',');
		    	 Traveler=document.form.chkdOrdNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
	             orderNo = chkdvalue[0];
		    	 if(!verifyPayment(orderNo))	
			    	  return false;
		     }
		     document.form.TRAVELER.value=concatTraveler; 
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
 	}
 	
	   document.form.action ="/track/OrderPaymentServlet?action=processOrderPayment";
	   document.form.submit();
  }
  

function verifyPayment(orderNo)
{
		var amtPayment = document.getElementById("payment_"+(orderNo)).value;
		var dueToPay = document.getElementById("balToPay_"+(orderNo)).innerHTML;
		
		if (amtPayment == "" || amtPayment.length == 0 || amtPayment == '0') {
			alert("Enter a valid payment Amount!");
			document.getElementById("payment_"+(orderNo)).focus();
			document.getElementById("payment_"+(orderNo)).select();
	        return false;
		}
		if(!isNumericInput(amtPayment)){
			alert("Entered Amount is not a valid number!");
			document.getElementById("payment_"+(orderNo)).focus();
			document.getElementById("payment_"+(orderNo)).select();
	         return false;
		}
		
	   if (amtPayment.indexOf('.') == -1) amtPayment += ".";
			var decNum = amtPayment.substring(amtPayment.indexOf('.')+1, amtPayment.length);
			if (decNum.length > 2)
			{
				alert("Invalid more than 2 digits after decimal in payment");
				document.getElementById("payment_"+(orderNo)).focus();
				return false;
				
			}
		
		/*var orderedAmt = document.getElementById("amtOrdered_"+(orderNo)).innerHTML;
		orderedAmt = removeCommas(orderedAmt);
		orderedAmt = parseFloat(orderedAmt).toFixed(3);
		var receivedAmt = document.getElementById("amtReceived_"+(orderNo)).innerHTML;
		receivedAmt = removeCommas(receivedAmt);
		receivedAmt = parseFloat(receivedAmt).toFixed(3);
				
		var amtPayed = parseFloat(amtPayment).toFixed(3);
		dueToPay = removeCommas(dueToPay);
		var duePayment = parseFloat(dueToPay).toFixed(3);

	if(parseFloat(duePayment) < parseFloat(amtPayed))
	{
		alert("Exceeded the Ordered Amount of Order No:: "+orderNo);
		document.getElementById("amtPayment_"+(orderNo)).focus();
		document.getElementById("amtPayment_"+(orderNo)).select();
		return false;
	}*/
	return true;
}

function limitText(limitField, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} 
        }
function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}

function setIssuingpmt(i)
{
	var orderNo;
	var len = document.form.chkdOrdNo.length; 
	if(len == undefined) len = 1;
	if(len ==1)
	{
		chkstring = document.form.chkdOrdNo.value;
	}
	else
	{
		chkstring = document.form.chkdOrdNo[i].value;
	}
	chkdvalue = chkstring.split(',');
	orderNo = chkdvalue[0]; 
	
	if(len ==1 && document.form.chkdOrdNo.checked)
	{
		var balence = document.getElementById("balToPay_"+orderNo).innerHTML;
		balence = removeCommas(balence);
		document.getElementById("payment_"+orderNo).value = balence;
	}
	else if(len ==1 && !(document.form.chkdOrdNo.checked))
	{
		document.getElementById("payment_"+orderNo).value = "";	
	}
	else{
		if(document.form.chkdOrdNo[i].checked)
		{
			var balence = document.getElementById("balToPay_"+orderNo).innerHTML;
			balence = removeCommas(balence);
			document.getElementById("payment_"+orderNo).value = balence;
		}
		else
		{
			document.getElementById("payment_"+orderNo).value = "";	
		}
	}
}

function validDecimal(str){
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	if (decNum.length > 2)
	{
		alert("Invalid more than 2 digits after decimal in payment");
		document.form.UNITPRICE.focus();
		return false;
		
	}
}	

</script>
</head>

<%@ include file="body.jsp"%>

<body>
<form name="form" method="post" action="/track/OrderPaymentServlet?">
<INPUT name="ACTIVE" type = "hidden" >
<INPUT name="PAYMENT_DESC" type = "hidden" >
<br>
<table border="1" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Create Payment Receipt</FONT></TH>
</table>
<br>
<CENTER>
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<tr><td><font class="maingreen"> <%=fieldDesc%></font></td></tr>
</table>
</CENTER>
<table border="1" width="90%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd" >
	<tr>	
		<td width="100%">&nbsp; 
		
						<TABLE BORDER="0" CELLSPACING=1 WIDTH="90%" align = "center">
							<tr>
								<th WIDTH="20%" ALIGN="left">Order :</th>
								<TD><select name="Order" onchange="showpaymentdiv();">
									<option value="INBOUND" <%if(Order.equalsIgnoreCase("INBOUND")){ %>selected<%} %>>INBOUND</option>
									<option value="OUTBOUND" <%if(Order.equalsIgnoreCase("OUTBOUND")){ %>selected<%} %> >OUTBOUND</option>
									<option value="OTHERS" <%if(Order.equalsIgnoreCase("OTHERS")){ %>selected<%} %> >OTHERS</option>
									</select></TD>
							
								<th WIDTH="20%" ALIGN="left">Customer/Supplier :</th>
								<TD><INPUT name="custName" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="30" MAXLENGTH=80>
								 <a href="#" onClick="javascript:popUpWin1('list/orderContactPersonList.jsp?custName='+form.custName.value+'&orderType='+form.Order.value);"> <img src="images/populate.gif" border="0" /> </a></TD>
							
							<tr>
								<th WIDTH="20%" ALIGN="left">Ref No :</th>
								<TD><INPUT name="ordRefNo" type="TEXT" value="<%=su.forHTMLTag(ordRefNo)%>" size="30" MAXLENGTH=80></TD>
								 <th WIDTH="20%" ALIGN="left">Order No :</th>
								<TD><INPUT name="orderNo" type="TEXT" value="<%=su.forHTMLTag(orderNo)%>" size="30" MAXLENGTH=80></TD>
									<td><input type="button" value="View" name="actionButton"onclick="viewOrdersDueForPaymnet();"></td>
							</tr>
							
						</TABLE>
						</td>
						</tr>						
						</table>
						
						
						<br>
						
						
						<TABLE BORDER="1" CELLSPACING="0" WIDTH="90%" bgcolor="navy" ALIGN = "CENTER">
					<tr>

						<th align = "center" width="5%"><font color="#ffffff">Select</font> </th>
						<th align = "center" width="10%"><font color="#ffffff">Order No</th>
						<th align = "center" width="10%"><font color="#ffffff">Ref No</th>
						<th align = "center" width="8%"><font color="#ffffff">Order Date</th>
						<th align = "center" width="10%"><font color="#ffffff">Original Amount </th>
						<!--<th align = "center" width="10%"><FONT color="#ffffff">Amount Received</FONT></th>-->
						  <th align = "center" width="10%"><FONT color="#ffffff">Open Balance</FONT></th>
						<th align = "center" width="12%"><FONT color="#ffffff">Payment</FONT></th>
						
					</tr>
				</TABLE>
		 <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">	
		 <INPUT type="hidden" name="TRAVELER" value="">	
	<table width="90%" border="0" cellspacing="0" cellpadding="5" ALIGN = "CENTER" bgcolor="#eeeeee">
	<% 
	 DecimalFormat decformat = new DecimalFormat("#,##0.00");
	 if(al.size()==0)
     {
  	   AFLAG="";
     }
       
       if(al.size()>0)
       {
       AFLAG="DATA";
       String recvingAmt = "";
       for(int i=0 ; i<al.size();i++)
       {
    	   recvingAmt = "";
    	  
    	  Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          String ordNo = (String)m.get("orderNo");
          String refNo = (String)m.get("refNo");
          String ordDate = (String)m.get("ordDate");
          double amtOrd = Double.parseDouble((String)m.get("orderAmt"));
          amtOrd = StrUtils.RoundDB(amtOrd,2);
          double amtRecived = Double.parseDouble((String)m.get("amtReceived"));
          amtRecived = StrUtils.RoundDB(amtRecived,2);
          double balPay = Double.parseDouble((String)m.get("DueToPay"));
          balPay = StrUtils.RoundDB(balPay,2);
          String amtOrdered= decformat.format(amtOrd);
          String amtReceived= decformat.format(amtRecived);
         // double amountDue = Double.parseDouble((String)m.get("orderAmt")) - Double.parseDouble((String)m.get("amtReceived"));
          String balToPay= decformat.format(balPay);
          chkString  =ordNo+","+StrUtils.currencyWtoutCommSymbol(String.valueOf((String)m.get("DueToPay")))+","+ordDate+","+StrUtils.currencyWtoutCommSymbol((String)m.get("orderAmt"))+","+StrUtils.currencyWtoutCommSymbol((String)m.get("amtReceived"))+","+refNo;
                    
      %>
					<TR bgcolor="<%=bgcolor%>">

						<TD align = "center" width="5%" align="CENTER"><font color="black">
						<INPUT Type=checkbox style="border: 0;" name="chkdOrdNo" value="<%=chkString%>" onclick="setIssuingpmt(<%=i%>);"></font></TD>
						<TD align = "center" width="10%" align="center"><font color="black"><a href="#" onClick="javascript:popUpWin('IOBorderDetails.jsp?orderType=<%=Order%>&orderNo=<%=ordNo%>') "><%=ordNo%></a></font></TD>
						<TD align = "center" align="center" 	width="10%"><%=refNo%></TD>
						<TD align = "center" align="center" width="8%"><%=ordDate%></TD>
						<TD id = "amtOrdered_<%=ordNo%>" align="center" width="10%"><%=amtOrdered%></TD>
						<!--<TD id = "amtReceived_<%=ordNo%>" align="center" width="10%"><%=amtReceived%></TD>-->
						  <TD id = "balToPay_<%=ordNo%>" align="center" width="10%"><%=balToPay%></TD>
						<TD align="center" width="12%">
						<input type="text" name = "payment_<%=ordNo%>" id = "payment_<%=ordNo%>" size = "6" maxlength = "10" value = "<%=recvingAmt%>" onchange="validDecimal(this.value)">
						</TD>
						
					</TR>
					<%}} else 
    	   {%>
					<TR>
						<TD align="center" width="10%">Data's Not Found</TD>
					</TR>
					<%}%>

				</table>
				
				<div align="center">
				<center><br>
			<TABLE BORDER="0" CELLSPACING=1 WIDTH="60%"  align="center" bgcolor="#dddddd">
							<tr>
								<th WIDTH="20%" ALIGN="left"><b>Payment Date :  </b></th>
								<td><INPUT name="paymentDate" id="paymentDate" type="TEXT"
					 size="20" value = '<%=paymentDate%>' MAXLENGTH="80" readonly="readonly">
				<a href="javascript:show_calendar('form.paymentDate');" onmouseover="window.status='Date Picker';return true;"
			    onmouseout="window.status='';return true;"> <img src="images\show-calendar.gif" width="24" height="22" border="0" /></a></td>
					
					       <th WIDTH="20%" ALIGN="left">Payment Mode : </th>
					       <TD><select name="paymentMode">
					       <option selected value="">Choose</option>
					       <option value="GIRO" <%if(paymentMode.equalsIgnoreCase("GIRO")){ %>selected<%} %>>GIRO</option>
					       <option value="CHEQUE" <%if(paymentMode.equalsIgnoreCase("CHEQUE")){ %>selected<%} %> >CHEQUE</option>	
      			    	   <option   value='CASH' <%if(paymentMode.equalsIgnoreCase("CASH")){ %>selected<%} %>>CASH</OPTION>
      			    	   <option   value='OTHERS' <%if(paymentMode.equalsIgnoreCase("OTHERS")){ %>selected<%} %>>OTHERS</OPTION>
						   <option value="ELECTRONIC TRANSFER" <%if(paymentMode.equalsIgnoreCase("ELECTRONIC TRANSFER")){ %>selected<%} %>>ELECTRONIC TRANSFER</option>
					       <option value="NETS" <%if(paymentMode.equalsIgnoreCase("NETS")){ %>selected<%} %> >NETS</option>	
      			    	   <option   value='CREDIT CARD' <%if(paymentMode.equalsIgnoreCase("CREDIT CARD")){ %>selected<%} %>>CREDIT CARD</OPTION>
      			    	   <option   value='VOUCHER' <%if(paymentMode.equalsIgnoreCase("VOUCHER")){ %>selected<%} %>>VOUCHER</OPTION>
      			    	   </select>
      			    	   </TD>
							
					<tr>
						<th WIDTH="20%" ALIGN="left">Payment Type : </th>
					       <TD><select id="paymentType" name="paymentType">
					       <option selected value="">Choose</option>
					       <option value="CREDIT" <%if(paymentType.equalsIgnoreCase("CREDIT")){ %>selected<%} %>>CREDIT</option>
					       <option value="DEBIT" <%if(paymentType.equalsIgnoreCase("DEBIT")){ %>selected<%} %> >DEBIT</option>	
      			    	   </TD>
						 <th WIDTH="20%" ALIGN="left">Order Type :</th>
                     			 <td><INPUT type="TEXT" size="20" MAXLENGTH="20" name="ORDERTYPE" value="<%=orderType%>" />
								 <a href="#" onClick="javascript:popUpWin1('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE='+form.Order.value);"><img src="images/populate.gif" border="0"></a>
								 </td>
					</tr>			
					<tr>
					
						<TH WIDTH="20%" ALIGN="left">Payment ID:</TH>
						<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=spaymentId%>" size="20" MAXLENGTH=50> 
			 			<a href="#" onClick="javascript:popUpWin1('PaymentIdList.jsp?PAYMENT_ID='+form.PAYMENT_ID.value);">
			 			<img src="images/populate.gif" border="0"></a>
			 			</TD>
							    <th WIDTH="20%" ALIGN="left">Payment Reference :</th>
								<TD><INPUT name="paymentRefNo" type="TEXT" value="<%=su.forHTMLTag(paymentRefNo)%>" size="30" MAXLENGTH=80></TD>
						</tr>
						<tr>
								<th WIDTH="20%" ALIGN="left">Payment Remarks :</th>
								 <TD width="20%"><TEXTAREA NAME="paymentRemarks" COLS=40 ROWS=3 onkeypress="return limitText(this, 100);" ></TEXTAREA></TD>  	
						
						</tr>	
				</TABLE>
				</center>		
				</div>
					<div id="divpayment" style="display: none;" align="center">
					<center>	
								<TABLE BORDER="0" CELLSPACING=1 WIDTH="60%"  align="center" bgcolor="#dddddd">
								<TR>
								<th WIDTH="20%" ALIGN="left">Payment :</th>
								<TD><INPUT name="payment" type="TEXT" value="<%=su.forHTMLTag(payment)%>" size="30" MAXLENGTH=80></TD>
								</TR>
								</table>
					
					</center>	
					</div>		
					<div align="center">
					<center>	
								<TABLE BORDER="0" CELLSPACING=1 WIDTH="60%"  align="center" bgcolor="#dddddd">
								<TR>
								 <td align="center">
								 	<input type="Button" value="Cancel" onClick="window.location.href='../home'">
							  		<input type="button" value="Payment Confirmation" name="actionSubmit" onClick="onReceivePayment(document.form)"/>
									</TD>
								</TR>
								</table>
					
					</center>	
					</div>	
</form>
</body>
</html>
<%@ include file="footer.jsp"%>