<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%> 
<%@ include file="header.jsp"%>
<%
String title = "Edit Payment Receipt";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onClear()
	{
            document.form1.HEADER.value="";
            document.form1.RECEIVEDFROM.value="";
            document.form1.Date.value="";
    		document.form1.PaymentMode.value="";
    		document.form1.ReferenceNo.value="";

    		document.form1.InvoiceNumber.value="";
    		document.form1.InvoiceDate.value="";
    		document.form1.DueDate.value="";
    		document.form1.OriginalAmount.value="";

    		//document.form1.Balance.value="";
    		document.form1.Payment.value="";
    		document.form1.AmountCredited.value="";
    		document.form1.Total.value="";
            document.form1.Memo.value="";
    		document.form1.Signature.value="";
    		
	}
	function onAdd(){
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_PAYMENT_RCPT_HDR";
   		document.form1.submit();
   	}
	
/*
	function onClear()
	{
		document.form1.GSTTYPE.selectedIndex=0;
		document.form1.GSTDESC.value="";
		document.form1.GSTPERCENTAGE.value="";
		document.form1.REMARKS.value="";
		document.form1.DisplayByLoc.checked = false;
		
	}
	function onAdd(){
   		document.form1.action  = "/track/GstTypeServlet?action=EDIT_POS_RCPT_HDR";
   		document.form1.submit();
   	
	}
	*/

	
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "";
	
	String action = "";
	String HEADER = "",RECEIVEDFROM="", Date="",PaymentMode="",ReferenceNo="",InvoiceNumber="";
    String InvoiceDate = "",DueDate="",OriginalAmount="",Balance="",Payment = "",AmountCredited="",Total="",Memo="",Signature="";
        
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
        OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
         Map mhdr= paymentUtil.getPaymentReceiptHdrDetails(plant);
           
           HEADER = (String) mhdr.get("HDR");
           RECEIVEDFROM = (String) mhdr.get("RECEIVEDFROM");
           Date = (String) mhdr.get("PMTDATE");
           PaymentMode = (String) mhdr.get("PAYMENTMODE");
           ReferenceNo = (String) mhdr.get("REFERENCENO");
           InvoiceNumber = (String) mhdr.get("INVOICENUMBER");
           InvoiceDate = (String) mhdr.get("INVOICEDATE");
           DueDate = (String) mhdr.get("DUEDATE");
           OriginalAmount = (String) mhdr.get("ORIGINALAMOUNT");
           //Balance = (String) mhdr.get("BALANCE");
           Payment = (String) mhdr.get("PAYMENT");
           AmountCredited = (String) mhdr.get("AMOUNTCREDITED");
           Total = (String) mhdr.get("TOTAL");
           Memo = (String) mhdr.get("MEMO");
           Signature = (String) mhdr.get("SIGNATURE");
          
           
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
                HEADER = "";RECEIVEDFROM="";
                Date = "";PaymentMode="";ReferenceNo="";InvoiceNumber="";InvoiceDate = "";DueDate="";OriginalAmount="";Balance="";Payment="";AmountCredited="";Total="";Memo="";Signature="";
		
	}  
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
        
    <div class="form-group">
      <label class="control-label col-sm-4" for="Header">Header:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="HEADER" type="TEXT" value="<%=HEADER%>" size="50" MAXLENGTH=20>
      </div>
    </div>
                 
    <div class="form-group">
         <label class="control-label col-sm-4" for="Received From">Received from:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="RECEIVEDFROM" type="TEXT" value="<%=RECEIVEDFROM%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Date">Date:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>" size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Mode">Payment Mode:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PaymentMode" type="TEXT" value="<%=PaymentMode%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Reference No">Reference No:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ReferenceNo" type="TEXT" value="<%=ReferenceNo%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Invoice No">Invoice No:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="InvoiceNumber" type="TEXT" value="<%=InvoiceNumber%>"	size="50" MAXLENGTH=25>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Invoice Date">Invoice Date:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="InvoiceDate" type="TEXT" value="<%=InvoiceDate%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Due Date">Due Date:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DueDate" type="TEXT" value="<%=DueDate%>"	size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Origianl Amount">Original Amount:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OriginalAmount" type="TEXT" value="<%=OriginalAmount%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Payment">Payment:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Payment" type="TEXT" value="<%=Payment%>" size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Amount Paid">Amount Paid:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="AmountCredited" type="TEXT" value="<%=AmountCredited%>" size="50" MAXLENGTH=25>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Total">Total:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>" size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Memo">Memo:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Memo" type="TEXT" value="<%=Memo%>" size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Signature">Signature:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Signature" type="TEXT" value="<%=Signature%>"	size="50" MAXLENGTH=20>
      </div>
    </div>
        
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

