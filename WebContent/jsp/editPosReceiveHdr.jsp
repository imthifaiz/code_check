<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Goods Receipt Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<!-- <script src="assets/js/jquery.min.js"></script>

<title>Edit Goods Receipt Printout</title> -->

<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;
		

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	

	function onClear()
	{
		
		document.form1.HEADER.value="";
		document.form1.PaymentMode.value="";
		document.form1.SalesRep.value="";
		document.form1.SerialNo.value="";
		document.form1.ReceiptNo.value="";
		document.form1.Product.value="";
		document.form1.Description.value="";
		document.form1.UnitCost.value="";
		document.form1.Qty.value="";
		document.form1.UOM.value="";
		document.form1.Discount.value="";
		document.form1.Total.value="";
		document.form1.Subtotal.value="";
		document.form1.Tax.value="";
		document.form1.TotalAmt.value="";
		document.form1.PaymentPaid.value="";
		document.form1.ChangeRemaining.value="";
		document.form1.GREET1.value="";
		document.form1.GREET2.value="";
		document.form1.FOOT1.value="";
		document.form1.FOOT2.value="";
		document.form1.DisplayByLoc.checked = false;
		document.form1.PrintWRcpt.checked = false;
		
	}
	function onAdd(){
   		/*document.form1.action  = "/track/GstTypeServlet?action=EDIT_POS_RECEIVE_HDR";
   	 	var formData = $('form#editposReceiveReceiptForm').serialize();
		$.ajax({
	    type: 'post',
	    url: "/track/GstTypeServlet?action=EDIT_POS_RECEIVE_HDR", 
	    dataType:'html',
	    data:  formData,
	   
	    success: function (data) {
	    	$('#appendbody').html(data); 
	        
	    },
	    error: function (data) {	        	
	        alert(data.responseText);
	    }
	});
		return false;*/
   		//document.form1.submit();
		
		 //document.form.cmd.value="ADD" ;
		 document.form1.action  = "/track/GstTypeServlet?action=EDIT_POS_RECEIVE_HDR";
		 document.form1.submit();
   	
	}
	

	
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	//String res = "";
	
	String action = "";
	String HEADER = "", GREET1 = "", GREET2 = "",FOOT1="",FOOT2="";
        String Product = "",PaymentMode="",SalesRep="",SerialNo="",ReceiptNo = "",Description="",Unitcost="",Qty="",UOM="",Discount="",Total="",Subtotal="",Tax="",TotalAmt="",PaymentPaid="",
        ChangeRemaining="", DisplayByLoc="",PrintWRcpt="",Orientation="";
	StrUtils strUtils = new StrUtils();
	String  res =  strUtils.fString(request.getParameter("result"));
	POSUtil posUtil = new POSUtil();
         Map mhdr= posUtil.getPosReceiveReceiptHdrDetails(plant);
           HEADER = (String) mhdr.get("HDR");

           PaymentMode = (String) mhdr.get("PAYMENT_MODE");
           SalesRep = (String) mhdr.get("SALES_REP");
           SerialNo = (String) mhdr.get("SERIAL_NO");
           ReceiptNo = (String) mhdr.get("RECEIPT_NO");
           Product = (String) mhdr.get("PRODUCT");
           Description = (String) mhdr.get("PROD_DESC");
           Unitcost = (String) mhdr.get("UNIT_COST");
           Qty = (String) mhdr.get("QTY");
           UOM = (String) mhdr.get("UNITMO");
           Discount = (String) mhdr.get("DISCOUNT");
           Total = (String) mhdr.get("TOTAL");
           Subtotal = (String) mhdr.get("SUBTOTAL");
           Tax = (String) mhdr.get("TAX");
           TotalAmt = (String) mhdr.get("TOTAL_AMT");
           PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
           ChangeRemaining = (String) mhdr.get("CHANGE");
        
           GREET1 = (String) mhdr.get("G1");
           GREET2 = (String) mhdr.get("G2");
           FOOT1 = (String) mhdr.get("F1");
           FOOT2 = (String) mhdr.get("F2");
           DisplayByLoc=(String) mhdr.get("ADDRBYLOC");
           PrintWRcpt=(String) mhdr.get("DOWNLOADPDF");
           Orientation=(String)mhdr.get("PrintOrientation");

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
                HEADER = "";
                Product = "";PaymentMode="";SalesRep="";SerialNo="";ReceiptNo = "";Description="";Unitcost="";Qty="";UOM="";Discount="";Total="";Subtotal="";Tax="";TotalAmt="";PaymentPaid="";ChangeRemaining="";
		GREET1 = "";
		GREET2 = "";
		FOOT1 = "";
		FOOT2 = "";
	}  
%>

<CENTER><strong><font style="font-size:18px;"><%=res%></font></strong></CENTER> 

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Goods Receipt Printout</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                       <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
  
 <FORM class="form-horizontal" name="form1" id="editposReceiveReceiptForm" method="post">
	<!-- <CENTER><strong><font style="font-size:40px;"><%=res%></font></strong></CENTER>-->
		
		<%-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Orientation">Orientation:</label>
      	<div class="col-sm-3">
      	<label class="radio-inline">
      	<input type = "radio" name="Orientation" value="Landscape" <%if(Orientation.equals("Landscape")) {%>checked <%}%>>Landscape
      	</label>
      	<label class="radio-inline"> --%>
      	<input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>><!-- Portrait -->
      	<!-- </label>
  		</div>
  		</div> -->
  		
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Header">Header</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="HEADER" type="TEXT" value="<%=HEADER%>" size="30" MAXLENGTH=20>
  		</div>
  		<div class="form-inline">
      	<div class="col-sm-5">
      	<lable class="radio-inline"><input <%if(DisplayByLoc.equals("0")) {%>checked <%}%> id = "DisplayByLoc" type="radio" name="DisplayByLoc" value="0">Print With Company Address</lable>
      	<lable class="radio-inline"><input <%if(DisplayByLoc.equals("1")) {%>checked <%}%> type="radio" id = "DisplayByLoc" name="DisplayByLoc" value="1">Print With Location Address</lable>
  		</div>
  		</div>
  		</div>
		<!-- Payment Mode -->
		<INPUT class="form-control" name="PaymentMode" type="hidden" value="<%=PaymentMode%>" size="50" MAXLENGTH=15>				
		
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Employee</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="SalesRep" type="TEXT" value="<%=SalesRep%>" size="30" MAXLENGTH=15>
  		</div>
  		<div class="form-inline">
      	<div class="col-sm-5">
      	<lable class="checkbox-inline"><input type = "checkbox" id = "PrintWRcpt" name = "PrintWRcpt" value = "PrintWRcpt" 
		<%if(PrintWRcpt.equals("1")) {%>checked <%}%>>Print Receipt during completion of Check In</lable>
      	</div>
  		</div>
  		</div>
  		
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Serial No</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="SerialNo" type="TEXT" value="<%=SerialNo%>"	size="30" MAXLENGTH=15>
  		</div>
  		</div>	
  		
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Receipt No</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="ReceiptNo"  type="TEXT" value="<%=ReceiptNo%>"	size="30" MAXLENGTH=15>
  		</div>
  		</div>	
  		
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Product</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="Product" type="TEXT" value="<%=Product%>"	size="30" MAXLENGTH=15>
  		</div>
  		</div>	
  		
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Description</label>
      	<div class="col-sm-3">
      	<textarea rows="1" class="form-control" name="Description" type="TEXT" value=""	
					size="50" MAXLENGTH=20><%=Description%></textarea>
  		</div>
  		</div>
  		<!-- Unit Cost -->
  		<INPUT class="form-control" name="UnitCost" type="hidden" value="<%=Unitcost%>"	size="50" MAXLENGTH=15>	
		    	
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Quantity</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="Qty" type="TEXT" value="<%=Qty%>"	size="30" MAXLENGTH=15>
  		</div>
  		</div>
  		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">UOM</label>
      	<div class="col-sm-3">
      	<INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"	size="30" MAXLENGTH=15>
  		</div>
  		</div>	 
  		<!-- Discount -->
  		<INPUT class="form-control" name="Discount" type="hidden" value="<%=Discount%>"	size="50" MAXLENGTH=15>	         
		<!-- Total -->
		<INPUT class="form-control" name="Total" type="hidden" value="<%=Total%>"		size="50" MAXLENGTH=15>	  
		<!-- Sub Total -->	
		<INPUT class="form-control" name="Subtotal" type="hidden" value="<%=Subtotal%>" size="50" MAXLENGTH=15>	
		<!-- Tax -->	
		<INPUT class="form-control" name="Tax" type="hidden" value="<%=Tax%>"	size="50" MAXLENGTH=15> 
		<!-- Total Amount -->
		<INPUT class="form-control" name="TotalAmt" type="hidden" value="<%=TotalAmt%>" size="50" MAXLENGTH=15>	 
		<!-- Payment Paid -->
		<INPUT class="form-control" name="PaymentPaid" type="hidden" value="<%=PaymentPaid%>"	size="50" MAXLENGTH=15>	 
		<!-- Change -->	 
		<INPUT class="form-control" name="ChangeRemaining" type="hidden" value="<%=ChangeRemaining%>" size="50" MAXLENGTH=15>
					
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Remarks</label>
      	<div class="col-sm-3">
      	<textarea class="form-control" rows="1" name="GREET1" type="TEXT" value=""	size="30" MAXLENGTH=100><%=GREET1%></textarea>
  		</div>
  		</div>
  		
  		<input class="form-control" rows="1" name="GREET2" type="hidden" size="50" MAXLENGTH=100 value="<%=GREET2%>">			
					 
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Employee">Footer</label>
      	<div class="col-sm-3">
      	<textarea class="form-control" name="FOOT2" rows="1" type="TEXT" value=""size="30" MAXLENGTH=100><%=FOOT2%></textarea>
  		</div>
  		</div>
  		
  		<INPUT class="form-control" name="FOOT1" type="hidden" value="<%=FOOT1%>"	size="50" MAXLENGTH=100>	        
			
		<div class="form-group">        
      	<div class="col-sm-offset-3 col-sm-9">
<!--       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	</div>
    	</div>	
				
		</FORM>
		</div></div></div>
<!--<script>
autosize(document.querySelectorAll('textarea'));
</script>-->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
