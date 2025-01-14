<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit POS Receipt";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	

	function onClear()
	{
		 document.form1.action  = "editPosRcptHdr.jsp?action=Clear";
		   document.form1.submit();
		
		/* document.form1.GSTTYPE.selectedIndex=0;
		document.form1.GSTDESC.value="";
		document.form1.GSTPERCENTAGE.value="";
		document.form1.REMARKS.value="";
		document.form1.DisplayByLoc.checked = false;
		*/
	}
	function onAdd(){
   		document.form1.action  = "/track/GstTypeServlet?action=EDIT_POS_RCPT_HDR";
   		document.form1.submit();
   	
	}
	

	
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "";
	
	String action = "";
	String HEADER = "", GREET1 = "", GREET2 = "",FOOT1="",FOOT2="";
        String Product = "",PaymentMode="",SalesRep="",SerialNo="",ReceiptNo = "",Description="",UnitPrice="",Qty="",Discount="",Total="",Subtotal="",Tax="",TotalAmt="",PaymentPaid="",
        ChangeRemaining="", DisplayByLoc="";
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
	POSUtil posUtil = new POSUtil();
         Map mhdr= posUtil.getPosReceiptHdrDetails(plant);
           HEADER = (String) mhdr.get("HDR");

           PaymentMode = (String) mhdr.get("PAYMENT_MODE");
           SalesRep = (String) mhdr.get("SALES_REP");
           SerialNo = (String) mhdr.get("SERIAL_NO");
           ReceiptNo = (String) mhdr.get("RECEIPT_NO");
           Product = (String) mhdr.get("PRODUCT");
           Description = (String) mhdr.get("PROD_DESC");
           UnitPrice = (String) mhdr.get("UNIT_PRICE");
           Qty = (String) mhdr.get("QTY");
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

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
                HEADER = "";
                Product = "";PaymentMode="";SalesRep="";SerialNo="";ReceiptNo = "";Description="";UnitPrice="";Qty="";Discount="";Total="";Subtotal="";Tax="";TotalAmt="";PaymentPaid="";ChangeRemaining="";
		GREET1 = "";
		GREET2 = "";
		FOOT1 = "";
		FOOT2 = "";
	}  
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<div class="alert">
   <CENTER><strong><%=res%></strong></CENTER>
</div>
<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post">
   <div class="form-group">
      <label class="control-label col-sm-4" for="POS Reecipt Header">Header:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="HEADER" type="TEXT" value="<%=HEADER%>"
			size="50" MAXLENGTH=15>
      </div>
    <div class=form-inline>
      <div class="col-sm-4">    
      <label class="checkbox-inline">      
        <INPUT type = "checkbox" id = "DisplayByLoc" name = "DisplayByLoc" value = "DisplayByLoc" 
			<%if(DisplayByLoc.equals("1")) {%>checked <%}%> >Print with Address</label>
			</div>
    </div>
        </div>        
    <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Mode">Payment Mode:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PaymentMode" type="TEXT" value="<%=PaymentMode%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
                 
    <div class="form-group">
         <label class="control-label col-sm-4" for="Contact Name">Sales Person:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="SalesRep" type="TEXT" value="<%=SalesRep%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Serial No">Serial No:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SerialNo" type="TEXT" value="<%=SerialNo%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Receeit No">Receipt No:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ReceiptNo" type="TEXT" value="<%=ReceiptNo%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product">Product:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Product" type="TEXT" value="<%=Product%>"
			size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Desc">Description:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>"
			size="50" MAXLENGTH=20>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">Unit Price:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="UnitPrice" type="TEXT" value="<%=UnitPrice%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Quantity">Quantity:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Qty" type="TEXT" value="<%=Qty%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Discount">Discount:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Discount" type="TEXT" value="<%=Discount%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Total">Total:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Sub Total">Sub Total:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Subtotal" type="TEXT" value="<%=Subtotal%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Tax">Tax:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Tax" type="TEXT" value="<%=Tax%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Total Amount">Total Amount:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="TotalAmt" type="TEXT" value="<%=TotalAmt%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Done">Paid Amount:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PaymentPaid" type="TEXT" value="<%=PaymentPaid%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Change">Balance:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ChangeRemaining" type="TEXT" value="<%=ChangeRemaining%>"
			size="50" MAXLENGTH=15>
      </div>
    </div>
     <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Terms">Greeting:</label>
      <div class="col-sm-4">
        
    		<INPUT class="form-control" name="GREET1" type="TEXT" value="<%=GREET1%>"
			size="50" MAXLENGTH=100>
   		 	</div>
      </div>      
       <div class="form-group">
     <label class="control-label col-sm-4" for="Payment Terms"></label>
      <div class="col-sm-4">
         
    		<INPUT class="form-control" name="GREET2" type="TEXT" value="<%=GREET2%>"
			size="50" MAXLENGTH=100>
   		 	</div>
      </div>      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Terms">Footer:</label>
      <div class="col-sm-4">
        
    		<INPUT class="form-control" name="FOOT1" type="TEXT" value="<%=FOOT1%>"
			size="50" MAXLENGTH=100>
   		 	</div>
      </div>      
       <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Terms"></label>
      <div class="col-sm-4">
        
    		<INPUT class="form-control" name="FOOT2" type="TEXT" value="<%=FOOT2%>"
			size="50" MAXLENGTH=100>
   		 	</div>
      </div>      
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='settings.jsp'"><b>Back</b></button>&nbsp;&nbsp;
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