<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@page import ="java.text.DecimalFormat" %>
<%@ include file="header.jsp"%>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

POUtil _poUtil=new POUtil();
DOUtil _doUtil=new DOUtil();
OrderPaymentDAO _pmtdao=new OrderPaymentDAO();
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
String FROM_DATE="",TO_DATE="";
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
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


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if(Order.length()==0)Order ="OUTBOUND";                            
if(paymentDate.length()<0|paymentDate==null||paymentDate.equalsIgnoreCase(""))paymentDate=curDate;
ArrayList al= new ArrayList();

System.out.println(" action :: "+action);
if(action.equalsIgnoreCase("View")){
  al = (ArrayList)request.getSession().getAttribute("PAYMENT_DETAILS");
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
<title>Edit Payment Receipt</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript">
var subWin = null;

function setpaymenttype()
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

}
function populatedata()
{
   var len = document.form.chkdOrdNo.length; 
   if(len == undefined) len = 1;
   for (var i = 0; i < len ; i++)
   {
   		if(len ==1 && document.form.chkdOrdNo.checked)
   		{
   			chkstring = document.form.chkdOrdNo.value;
   			chkdvalue = chkstring.split(',');
   		   	
   		   	document.form.paymentDate.value = chkdvalue[5];
   		   	document.form.paymentMode.value = chkdvalue[6];
   		   	document.form.paymentType.value = chkdvalue[7];
   		   	document.form.ORDERTYPE.value = chkdvalue[8];
   		   	document.form.PAYMENT_ID.value = chkdvalue[9];
   		   	document.form.paymentRefNo.value = chkdvalue[10];
   		   	document.form.paymentRemarks.value = chkdvalue[11];
   		}
   		else
   		{
   			if(document.form.chkdOrdNo[i].checked){
   				chkstring = document.form.chkdOrdNo[i].value;
   				chkdvalue = chkstring.split(',');
   			   	
   			   	document.form.paymentDate.value = chkdvalue[5];
   			   	document.form.paymentMode.value = chkdvalue[6];
   			   	document.form.paymentType.value = chkdvalue[7];
   			   	document.form.ORDERTYPE.value = chkdvalue[8];
   			   	document.form.PAYMENT_ID.value = chkdvalue[9];
   			   	document.form.paymentRefNo.value = chkdvalue[10];
   			   	document.form.paymentRemarks.value = chkdvalue[11];
   			}
   		}
   }
   
   	
 
}

function popUpWin(URL) {
 subWin = window.open(URL, 'OrderPaymentDetais', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=850,height=500,left = 200,top = 184');
}


function popUpWin1(URL) {
 subWin = window.open(URL, 'OrderPaymentDetais', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function viewPaymnetDetails(){
	
	 var flag    = "false";
	   var order = document.form.Order.value;
	   var orderNo    = document.form.orderNo.value;
	   var ordRefNo      = document.form.ordRefNo.value;
	   var custName      = document.form.custName.value;
	  
	   if(orderNo != null     && orderNo != "") { flag = true;}
	   if(ordRefNo != null    && ordRefNo != "") { flag = true;}
	   if(custName != null     && custName != "") { flag = true;}
	   
	   document.form.action="/track/OrderPaymentServlet?action=getPaymentdetails";
       document.form.submit();
}
function onEditPayment(form){
 	var Traveler ;
 	var concatTraveler="";
 	var order = document.form.Order.value;
 	//if(order != "OTHERS"){
 	 	var count = $("[type='checkbox']:checked").length;
 		if(count > 1)
 		{
 			alert ("Please check only one checkbox.");
		    return false;
 		}
 	//}
 	if(document.form.paymentMode.selectedIndex==0)
 	{
 		alert("Please choose paymentMode");
 		return false;	
 	}
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
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
	//if(order != "OTHERS"){	
 	 var checkFound = false;  
	 var len = document.form.chkdOrdNo.length;
	 if(len == undefined) len = 1;
	 for (var i = 0; i < len ; i++)
	 {
	 	if(len == 1 && (!document.form.chkdOrdNo.checked))
		{
			checkFound = false;
		}
	 	else if(len ==1 && document.form.chkdOrdNo.checked)
	     {
	    	 checkFound = true;
	    	 document.form.TRAVELER.value=document.form.chkdOrdNo.value+"=";
	    	 
	     }
	
	     else {
		     if(document.form.chkdOrdNo[i].checked){
		    	 checkFound = true;
		    	 Traveler=document.form.chkdOrdNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
		     }
		     document.form.TRAVELER.value=concatTraveler; 
			
	     }
	 }
	 
	 if (checkFound != true) {
		    alert ("Please check any one checkbox.");
		    return false;
		    }
 	
	//}
	   document.form.action ="/track/OrderPaymentServlet?action=editOrderPayment";
	   document.form.submit();
  }
  
function ondeletePayment(form){
 	var Traveler ;
 	var concatTraveler="";
 	var order = document.form.Order.value;
 	 	 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
	 var checkFound = false;  
	 var len = document.form.chkdOrdNo.length;
	 if(len == undefined) len = 1;
	 for (var i = 0; i < len ; i++)
	 {
	 	if(len == 1 && (!document.form.chkdOrdNo.checked))
		{
			checkFound = false;
		}
	 	else if(len ==1 && document.form.chkdOrdNo.checked)
	     {
	    	 checkFound = true;
	    	 document.form.TRAVELER.value=document.form.chkdOrdNo.value+"=";
	    	 
	     }
	
	     else {
		     if(document.form.chkdOrdNo[i].checked){
		    	 checkFound = true;
		    	 Traveler=document.form.chkdOrdNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
		     }
		     document.form.TRAVELER.value=concatTraveler; 
			
	     }
	 }
	 
	 if (checkFound != true) {
		    alert ("Please check any one checkbox.");
		    return false;
		    }
 		
	   document.form.action ="/track/OrderPaymentServlet?action=deleteOrderPayment";
	   document.form.submit();
  }

function limitText(limitField, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} 
        }
function isNumericInput(strString) {
	var strValidChars = "0123456789.";
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

function onPrint(){
	var checkFound = false;
	var len = document.form.chkdOrdNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form.chkdOrdNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form.chkdOrdNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form.chkdOrdNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form.action="/track/OrderPaymentServlet?action=print";
    document.form.submit();
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
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Maintain Payment Receipt</FONT></TH>
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
							<TR>
          						<TH ALIGN="left" >From_Date : </TH>
          						<TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          						<TH ALIGN="left">To_Date : </TH>
          						<TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    						</TR>
							<tr>
								<th WIDTH="20%" ALIGN="left">Order :</th>
								<TD><select name="Order" onchange="setpaymenttype();">
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
									<td><input type="button" value="View" name="actionButton"onclick="viewPaymnetDetails();"></td>
							</tr>
							
						</TABLE>
						</td>
						</tr>						
						</table>
						
						
						<br>
						
						
						<TABLE BORDER="1" CELLSPACING="0" WIDTH="90%" bgcolor="navy" ALIGN = "CENTER">
					<tr>

						<th align = "center" width="5%"><font color="#ffffff">Select</font> </th>
						<th align = "center" width="10%"><font color="#ffffff">Order No </th>
						<th align = "center" width="10%"><font color="#ffffff">Ref No </th>
						<th align = "center" width="8%"><font color="#ffffff">Order Date </th>
						<th align = "center" width="10%"><font color="#ffffff">Original Amount </th>
						<th align = "center" width="12%"><FONT color="#ffffff">Open Balance</FONT></th>
						<th align = "center" width="12%"><FONT color="#ffffff">Paid Amount</FONT></th>
						
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
          String id = (String)m.get("id");
          String ordNo = (String)m.get("ordno");
          String order = (String)m.get("ordername");
          String pmtdate = (String)m.get("payment_dt");
          String ordtype = (String)m.get("ordertype");
          String pmtmode = (String)m.get("payment_mode");
          String pmtrefno = (String)m.get("payment_refno");
          String pmttype = (String)m.get("payment_type");
          String pmtid = (String)m.get("payment_id");
          String pmtremarks = (String)m.get("payment_remarks");
          String refNo = (String)m.get("refNo");
          String ordDate = (String)m.get("ordDate");
         double amtOrd = Double.parseDouble((String)m.get("orderAmt"));
         amtOrd = StrUtils.RoundDB(amtOrd,2);
          double paidamt = Double.parseDouble((String)m.get("amount_paid"));
          paidamt = StrUtils.RoundDB(paidamt,2);
          
          /*String totpaid =  _pmtdao.gettotalpaidamt(plant, ordNo);
          double DueToPay =  amtOrd - Double.parseDouble(totpaid);
          String BaltoPay = Double.toString(DueToPay);
          DueToPay = StrUtils.RoundDB(DueToPay,2);*/
          
          double totalpaid = Double.parseDouble((String)m.get("amtReceived"))-Double.parseDouble((String)m.get("returnamt"));
          double DueToPay =  amtOrd - totalpaid;
          DueToPay = StrUtils.RoundDB(DueToPay,2);
          String BaltoPay = Double.toString(DueToPay);
          String totpaid = Double.toString(totalpaid);

                    
          String amtOrdered= decformat.format(amtOrd);
          String amtpaid= decformat.format(paidamt);
          String amtbal = decformat.format(DueToPay);
          
          if(ordNo.equalsIgnoreCase("OTHERS"))
          {
        	  amtbal="0.00";
          }
         
          chkString  =id+","+ordNo+","+StrUtils.currencyWtoutCommSymbol((String)m.get("orderAmt"))+","+StrUtils.currencyWtoutCommSymbol((String)m.get("amount_paid"))+","+StrUtils.currencyWtoutCommSymbol(BaltoPay)+","+pmtdate+","+pmtmode+","+pmtrefno+","+ordDate+","+pmttype+","+ordtype+","+pmtid+","+pmtremarks+","+StrUtils.currencyWtoutCommSymbol(totpaid);
                    
      %>
					<TR bgcolor="<%=bgcolor%>">

						<TD align = "center" width="5%" ><font color="black">
						<INPUT Type=checkbox style="border: 0;" name="chkdOrdNo" value="<%=chkString%>" onclick="populatedata();"></font></TD>
						<TD align = "center" width="10%" ><font color="black"><a href="#" onClick="javascript:popUpWin('Orderpaymentdetails.jsp?ORDERTYPE=<%=ordtype%>&ORDERNO=<%=ordNo%>&PMTDATE=<%=pmtdate%>&PMTREFNO=<%=pmtrefno%>&PMTMODE=<%=pmtmode%>&PMTREMARKS=<%=StrUtils.replaceCharacters2Send(pmtremarks)%>&PMTTYPE=<%=pmttype%>&PMTID=<%=pmtid%>') "><%=ordNo%></a></font></TD>
						<TD align = "center" align="center" 	width="10%"><%=refNo%></TD>
						<TD align = "center" align="center" width="8%"><%=ordDate%></TD>
						<TD  align="center" width="10%"><%=amtOrdered%></TD>
						<TD  align="center" width="10%"><%=amtbal%></TD>
						<TD  align="center" width="10%"><%=amtpaid%></TD>
						
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
			<TABLE BORDER="0" CELLSPACING=1 WIDTH="40%"  align="center" >
						<!--  	<tr>
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
      			    	   <option   value='OTHERS' <%if(paymentMode.equalsIgnoreCase("OTHERS")){ %>selected<%} %>>OTHERS</OPTION></select></TD>
					<tr>
						<th WIDTH="20%" ALIGN="left">Payment Type : </th>
					       <TD><select name="paymentType">
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
					
						<TH WIDTH="20%" ALIGN="left"> Payment ID:</TH>
						<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=spaymentId%>" size="20" MAXLENGTH=50> 
			 			<a href="#" onClick="javascript:popUpWin1('PaymentIdList.jsp?PAYMENT_ID='+form.PAYMENT_ID.value);">
			 			<img src="images/populate.gif" border="0"></a>
			 			</TD>
							    <th WIDTH="20%" ALIGN="left">payment Reference :</th>
								<TD><INPUT name="paymentRefNo" type="TEXT" value="<%=su.forHTMLTag(paymentRefNo)%>" size="30" MAXLENGTH=80></TD>
						</tr>
						<tr>
								<th WIDTH="20%" ALIGN="left">Payment Remarks :</th>
								 <TD width="20%"><TEXTAREA NAME="paymentRemarks" COLS=40 ROWS=3 onkeypress="return limitText(this, 100);" ></TEXTAREA></TD>  	
								<th WIDTH="20%" ALIGN="left"></th>
								<TD></TD>
						</TR>-->
						<tr>
							 <td align="center"><input type="Button" value="Cancel" onClick="window.location.href='../home'">
							   <input type="button" value="Delete Payment" name="actionSubmit" onClick="ondeletePayment(document.form)"/>
							   <input type="button" value="Print" name="action" onClick="javascript:return onPrint();"/>
							 	
							  </td>
						  </tr>
								</table>
					
					</center>	
					</div>	
</form>
</body>
</html>
<%@ include file="footer.jsp"%>