<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Rental Order Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<style>
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 18px;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
		document.form1.OrderHeader.value="";
		document.form1.InvoiceOrderToHeader.value="";
		document.form1.FromHeader.value="";
		document.form1.Date.value="";
		document.form1.OrderNo.value="";
		document.form1.RefNo.value="";
		document.form1.REMARK1.value="";
		document.form1.SoNo.value="";
		document.form1.Item.value="";
		document.form1.Description.value="";
		document.form1.OrderQty.value="";
		document.form1.UOM.value="";
		document.form1.Footer1.value="";
		document.form1.Footer2.value="";
		document.form1.Footer3.value="";
		document.form1.Footer4.value="";
		document.form1.Footer5.value="";
		document.form1.Footer6.value="";
		document.form1.Footer7.value="";
		document.form1.Footer8.value="";
		document.form1.Footer9.value="";
		document.form1.Orientation.value = "Landscape";
		
		document.form1.DELDATE.value="";
		document.form1.ORDDISCOUNT.value="";
		document.form1.EMPNAME.value="";
		document.form1.PREPAREDBY.value="";
		document.form1.SELLER.value="";
		document.form1.SELLERAUTHORIZED.value="";
		document.form1.BUYER.value="";
		document.form1.BUYERAUTHORIZED.value="";
		document.form1.COMPANYSTAMP.value="";
		document.form1.COMPANYNAME.value="";
		document.form1.SIGNATURE.value="";
		document.form1.VAT.value="";
		document.form1.CUSTOMERVAT.value="";
		document.form1.SHIPPINGCOST.value="";
		document.form1.TOTALAFTERDISCOUNT.value="";
		document.form1.ORDDATE.value="";
		document.form1.SHIPTO.value="";
		document.form1.NETAMT.value="";
		document.form1.TAXAMT.value="";
		document.form1.TOTALAMT.value="";
		document.form1.RATE.value="";
		document.form1.PAYMENTTYPE.value="";
		
		document.form1.AMOUNT.value="";
		document.form1.TOTALTAX.value="";
		document.form1.TOTALWITHTAX.value="";
		
		document.form1.DISPLAYBYORDERTYPE.checked = false;
		document.form1.PRINTDETAILDESC.checked = false;
		document.form1.PRINTCUSTREMARKS.checked = false;
		document.form1.PRINTPRDREMARKS.checked = false;
		document.form1.PRINTWITHBRAND.checked = false;
		document.form1.TOTALROUNDOFF.checked = false;
		document.form1.PRINTWITHEMPLOYEE.checked = false;
		document.form1.PRINTWITHPRDID.checked = false;
		document.form1.ROUNDOFF.value="";
	}
	function onAdd(){
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_LO_RCPT_HDR";
   		document.form1.submit();
	}
	
	function headerReadable(){
		if(document.form1.DISPLAYBYORDERTYPE.checked)
		{
		document.form1.OutboundOrderHeader.readOnly = true;		
		document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="inactivegry";
		}
		else{
			document.form1.OutboundOrderHeader.readOnly = false;
			document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="";
		}
	}

</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
    String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String OrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",Date="",OrderNo ="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="";
	String Description = "", OrderQty  = "", UOM = "",Footer1="",Footer2="",Footer3="",Footer4="",remark1="",Orientation="";
	String DELDATE="",EMPNAME="",ORDDISCOUNT="",PREPAREDBY="",SELLER="",SELLERAUTHORIZED="",BUYER="",BUYERAUTHORIZED="";
	String COMPANYNAME="",COMPANYSTAMP="",SIGNATURE="",SHIPPINGCOST="",VAT="",CUSTOMERVAT="",ORDDATE="",SHIPTO="",TOTALAFTERDISCOUNT="";
	String NETAMT="",TAXAMT="",RATE="",AMOUNT="",TOTALWITHTAX="",TOTALTAX="",TOTALAMT="",PAYMENTTYPE="",DISPLAYBYORDERTYPE="";
	String PRINTDETAILDESC="",PRINTCUSTREMARKS="",PRINTPRDREMARKS="",PRINTWITHBRAND="",TOTALROUNDOFF="",PRINTWITHEMPLOYEE="",PRINTWITHPRDID="";
	String Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",ROUNDOFF="";
	int FOOTER_SIZE=0;
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));

	LoanUtil loUtil = new LoanUtil();
	Map m= loUtil.getLOReceiptHdrDetails(plant);
         
    if(!m.isEmpty()){
     	OrderHeader= (String) m.get("HDR1");
        InvoiceOrderToHeader = (String) m.get("HDR2");
        FromHeader = (String) m.get("HDR3");
        Date = (String) m.get("DATE");
        OrderNo = (String) m.get("ORDERNO");
            
        RefNo = (String) m.get("REFNO");
        remark1=(String)m.get("REMARK1");
        SoNo = (String) m.get("SONO");
            
        DELDATE = (String) m.get("DELDATE");
        EMPNAME=(String)m.get("EMPNAME");
        ORDDISCOUNT = (String) m.get("ORDDISCOUNT");
        PREPAREDBY = (String) m.get("PREPAREDBY");
        SELLER = (String) m.get("SELLER");
        SELLERAUTHORIZED = (String) m.get("SELLERAUTHORIZED");
        BUYER = (String) m.get("BUYER");
        BUYERAUTHORIZED = (String) m.get("BUYERAUTHORIZED");
        COMPANYNAME = (String) m.get("COMPANYNAME");
        COMPANYSTAMP = (String) m.get("COMPANYSTAMP");
        SIGNATURE = (String) m.get("SIGNATURE");
        SHIPPINGCOST = (String) m.get("SHIPPINGCOST");
        VAT = (String) m.get("VAT");
        CUSTOMERVAT = (String) m.get("CUSTOMERVAT");
        ORDDATE = (String) m.get("ORDDATE");
        SHIPTO = (String) m.get("SHIPTO");
        TOTALAFTERDISCOUNT = (String) m.get("TOTALAFTERDISCOUNT");
        NETAMT = (String) m.get("NETAMT");
        TAXAMT = (String) m.get("TAXAMT");
        TOTALAMT = (String) m.get("TOTALAMT");
        RATE = (String) m.get("RATE");
        PAYMENTTYPE = (String) m.get("PAYMENTTYPE");
        
        AMOUNT = (String) m.get("AMOUNT");
        TOTALTAX = (String) m.get("TOTALTAX");
        TOTALWITHTAX = (String) m.get("TOTALWITHTAX");
        
        Item = (String) m.get("ITEM");
        Description = (String) m.get("DESCRIPTION");
        OrderQty = (String) m.get("ORDERQTY");
        UOM = (String) m.get("UOM");
             
        Footer1 = (String) m.get("F1");
        Footer2 = (String) m.get("F2");
        Footer3 = (String) m.get("F3");
        Footer4 = (String) m.get("F4");
        Footer5 = (String) m.get("F5");
        Footer6 = (String) m.get("F6");
        Footer7 = (String) m.get("F7");
        Footer8 = (String) m.get("F8");
        Footer9 = (String) m.get("F9");
        Orientation=(String)m.get("PrintOrientation");
 
        DISPLAYBYORDERTYPE = (String)m.get("DISPLAYBYORDERTYPE");
        PRINTDETAILDESC = (String)m.get("PRINTDETAILDESC");
        PRINTCUSTREMARKS = (String)m.get("PRINTCUSTREMARKS");
        PRINTPRDREMARKS = (String)m.get("PRINTPRDREMARKS");
        PRINTWITHBRAND = (String)m.get("PRINTWITHBRAND");
        TOTALROUNDOFF = (String)m.get("TOTALROUNDOFF");
        PRINTWITHEMPLOYEE = (String)m.get("PRINTWITHEMPLOYEE");
        PRINTWITHPRDID = (String)m.get("PRINTWITHPRDID");
        ROUNDOFF = (String)m.get("ROUNDOFF");
        if(!Footer1.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer2.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer3.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer4.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer5.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer6.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer7.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer8.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
           if(!Footer9.isEmpty())
          	 FOOTER_SIZE=FOOTER_SIZE+1;
     }
         
	 try {
		   action = strUtils.fString(request.getParameter("action"));
	 	 } catch (Exception e) {
	 }

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		OrderHeader  = "";
		InvoiceOrderToHeader  = "";
		FromHeader  = "";
		Date= "";
		OrderNo  = "";
		RefNo   = "";
		remark1="";
		SoNo=""; 
		Item   = "";
		Description ="";
		OrderQty =""; 
		UOM =""; 
		Footer1="";
		Footer2="";
        Footer3="";
		Footer4="";
		Footer5="";
		Footer6="";
        Footer7="";
		Footer8="";
		Footer9="";
		DELDATE="";
		EMPNAME="";
		ORDDISCOUNT="";
		PREPAREDBY="";
		SELLER="";
		SELLERAUTHORIZED="";
		BUYER="";
		BUYERAUTHORIZED="";
		COMPANYNAME="";
		COMPANYSTAMP="";
		SIGNATURE="";
		VAT="";
		CUSTOMERVAT="";
		TOTALAFTERDISCOUNT="";
		SHIPTO="";
		ORDDATE="";
		NETAMT="";
		TAXAMT="";
		TOTALAMT="";
		RATE="";
		PAYMENTTYPE="";
		AMOUNT="";
		TOTALTAX="";
		TOTALWITHTAX="";
		ROUNDOFF="";
	
	}  
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

<CENTER><strong><%=res%></strong></CENTER>

<form class="form-horizontal" name="form1" method="post">
  <%-- <div class="form-group">
    <label class="control-label col-sm-4" for="Orientation">Orientation:</label>
      <div class="col-sm-4">
       <label class="radio-inline">
        <input type = "radio" name="Orientation" value="Landscape" <%if(Orientation.equals("Landscape")) {%>checked <%}%>/>Landscape</label>
         <label class="radio-inline"> --%>
           <input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait --> 
           <!-- </label>
           </div>
             </div> -->
     
<div class="form-group">
   <label class="control-label col-form-label col-sm-3" for="Loan Order Header">Rental Order Header</label>
      <div class="col-sm-4">          
         <INPUT class="form-control" name="OrderHeader" type="TEXT" value="<%=OrderHeader%>" <%if(DISPLAYBYORDERTYPE.equals("1")) {%>readonly class = inactivegry<%} else{%> class = "" <%}%>
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DISPLAYBYORDERTYPE" name = "DISPLAYBYORDERTYPE" value = "DISPLAYBYORDERTYPE" 
			<%if(DISPLAYBYORDERTYPE.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/>Display By OrderType</label>
      </div>
      </div>
			</div>
			
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Invoice Order To Header">Invoice Order To Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTDETAILDESC" name = "PRINTDETAILDESC" value = "PRINTDETAILDESC" 
			<%if(PRINTDETAILDESC.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
			</div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="From Header">From Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTCUSTREMARKS" name = "PRINTCUSTREMARKS" value = "PRINTCUSTREMARKS" 
			<%if(PRINTCUSTREMARKS.equals("1")) {%>checked <%}%> />Print With Customer Remarks</label>
      </div>
      </div>
			</div>
						

			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ship To">Ship To</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SHIPTO" type="TEXT" value="<%=SHIPTO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTWITHBRAND" name = "PRINTWITHBRAND" value = "PRINTWITHBRAND" 
			<%if(PRINTWITHBRAND.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
			</div>

<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order Date">Order Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ORDDATE" type="TEXT" value="<%=ORDDATE%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
					<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTPRDREMARKS" name = "PRINTPRDREMARKS" value = "PRINTPRDREMARKS" 
			<%if(PRINTPRDREMARKS.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
			</div>			
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order No">Order Number</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderNo" type="TEXT" value="<%=OrderNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
					
				<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="VAT">VAT</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="VAT" type="TEXT" value="<%=VAT%>"
			placeholder="Max 50 Characters" size="50" MAXLENGTH=50>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="VAT">Customer VAT</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERVAT" type="TEXT" value="<%=CUSTOMERVAT%>"
			placeholder="Max 50 Characters" size="50" MAXLENGTH=50>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PREPAREDBY" type="TEXT" value="<%=PREPAREDBY%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			      <INPUT type="hidden" name="plant" value=<%=plant%>>
        <INPUT type="hidden" name="F1" id="F1" value="<%=Footer1%>">
        <INPUT type="hidden" name="F2" id="F2" value="<%=Footer2%>">
        <INPUT type="hidden" name="F3" id="F3" value="<%=Footer3%>">
        <INPUT type="hidden" name="F4" id="F4" value="<%=Footer4%>">
        <INPUT type="hidden" name="F5" id="F5" value="<%=Footer5%>">
        <INPUT type="hidden" name="F6" id="F6" value="<%=Footer6%>">
        <INPUT type="hidden" name="F7" id="F7" value="<%=Footer7%>">
        <INPUT type="hidden" name="F8" id="F8" value="<%=Footer8%>">
        <INPUT type="hidden" name="F9" id="F9" value="<%=Footer9%>">
			</div>
		    	<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#other" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#productDetail" class="nav-link" data-toggle="tab">Product Details</a>
        </li>
        <li class="nav-item">
            <a href="#cal" class="nav-link" data-toggle="tab">Calculations</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
         <li class="nav-item">
            <a href="#sign" class="nav-link" data-toggle="tab">Signature</a>
        </li>
        <li class="nav-item">
            <a href="#footer" class="nav-link" data-toggle="tab">Footer</a>
        </li>
        </ul>
        <div class="tab-content clearfix">
   <div class="tab-pane active" id="other">
        <br>
        	<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Payment Type">Payment Type</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PAYMENTTYPE" type="TEXT" value="<%=PAYMENTTYPE%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
				<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Delivery Date">Delivery Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DELDATE" type="TEXT" value="<%=DELDATE%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ref No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
					
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Employee">Employee</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="EMPNAME" type="TEXT" value="<%=EMPNAME%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTWITHEMPLOYEE" name = "PRINTWITHEMPLOYEE" value = "PRINTWITHEMPLOYEE" 
			<%if(PRINTWITHEMPLOYEE.equals("1")) {%>checked <%}%> />Print With Employee</label>
      </div>
      </div>
			</div>
		    
        </div>
        <div class="tab-pane fade" id="productDetail">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="SoNo">SoNo</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "PRINTWITHPRDID" name = "PRINTWITHPRDID" value = "PRINTWITHPRDID" 
			<%if(PRINTWITHPRDID.equals("1")) {%>checked <%}%> />Print With Product ID</label>
      </div>
      </div>
			</div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order Qty">Order Qty</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderQty" type="TEXT" value="<%=OrderQty%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
        		
					<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Rate">Rate</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RATE" type="TEXT" value="<%=RATE%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="NET AMOUNT">Net Amount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="NETAMT" type="TEXT" value="<%=NETAMT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Tax Amount">Tax Amount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TAXAMT" type="TEXT" value="<%=TAXAMT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
					<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Amount">Amount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="AMOUNT" type="TEXT" value="<%=AMOUNT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>    
        </div>
         <div class="tab-pane fade" id="cal">
        <br>
        	
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ORDDISCOUNT" type="TEXT" value="<%=ORDDISCOUNT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
        		<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Shipping Cost">Shipping Cost</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SHIPPINGCOST" type="TEXT" value="<%=SHIPPINGCOST%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
				<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Total After Discount">Total After Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TOTALAFTERDISCOUNT" type="TEXT" value="<%=TOTALAFTERDISCOUNT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Total Amount">Total Amount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TOTALAMT" type="TEXT" value="<%=TOTALAMT%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
	
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Tax Amount">Total Tax</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TOTALTAX" type="TEXT" value="<%=TOTALTAX%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Tax Amount">Total With Tax</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TOTALWITHTAX" type="TEXT" value="<%=TOTALWITHTAX%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
					<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Tax Amount">Total Roundoff</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ROUNDOFF" type="TEXT" value="<%=ROUNDOFF%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "TOTALROUNDOFF" name = "TOTALROUNDOFF" value = "TOTALROUNDOFF" 
			<%if(TOTALROUNDOFF.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
      </div>
			</div>   
        </div>
        <div class="tab-pane fade" id="remark">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
			</div>
			</div>
        
        </div>
        <div class="tab-pane fade" id="sign">
        <br>
        	<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Seller">Seller</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SELLER" type="TEXT" value="<%=SELLER%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Seller Authorized">Seller Authorized</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SELLERAUTHORIZED" type="TEXT" value="<%=SELLERAUTHORIZED%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BUYER" type="TEXT" value="<%=BUYER%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Buyer Authorized">Buyer Authorized</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BUYERAUTHORIZED" type="TEXT" value="<%=BUYERAUTHORIZED%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
	
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Comapny Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COMPANYNAME" type="TEXT" value="<%=COMPANYNAME%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Comapny Stamp">Company Stamp</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COMPANYSTAMP" type="TEXT" value="<%=COMPANYSTAMP%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Signature">Signature</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SIGNATURE" type="TEXT" value="<%=SIGNATURE%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
        </div>
                <div class="tab-pane fade" id="footer">
        <br>
     
    
    <div class="form-group">
        <TABLE id="footertbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD style="width: 17%;">
		<label class="control-label col-form-label col-sm-2" for="Footer1">Footer1</label></TD>		
		<TD align="center"  style="width: 83%;"><div class="col-sm-8"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('footertbl');return false;"></span>
		<INPUT class="form-control footerSearch" name="Footer1" id="Footer1"  placeholder="Max 200 Characters" type = "TEXT" value="<%=Footer1%>" size="100"  MAXLENGTH=200>		        
       	</div></div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="FOOTER_SIZE" value="<%=FOOTER_SIZE%>" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow('footertbl','');return false;">+ Add another Footer</a>
						</div>
					</div>
				</div>
        </div>
        
        </div>
        </div>	

			
		
	
		

    
    
    
    <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
       <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    var FOOTER_SIZE1 = document.form1.FOOTER_SIZE.value;
    if(FOOTER_SIZE1!=0)
	{
	//alert(FOOTER_SIZE1);
	
	for ( var i = 0; i < FOOTER_SIZE1; i++) {
		if(i!=0)
			{
				var footerval= document.getElementById("F"+(i+1)).value; 
				addRow('footertbl',footerval);
			}
	}
	}
    $(".footerSearch").typeahead('destroy');
	addSuggestionSearch();
});
function addRow(tableID,footer) {
	
  	
	var table = document.getElementById(tableID);
	
	var rowCount = table.rows.length;
	if(rowCount!=9)
		{
	var row = table.insertRow(rowCount);
	var newRowCount = rowCount + 1; 
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label class=\"control-label col-form-label col-sm-2\" for=\"Footer"+rowCount+"\">Footer"+newRowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"col-sm-8\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRow('footertbl');return false;\"></span><INPUT class=\"form-control footerSearch\" name=\"Footer"+newRowCount+"\" ";
	itemCellText = itemCellText+ " id=\"Footer"+newRowCount+"\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Max 200 Characters\" MAXLENGTH=\"200\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 9 footer ");
		}
	$(".footerSearch").typeahead('destroy');
	addSuggestionSearch();
}
function deleteRow(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}
function addSuggestionSearch()
{
	var plant = document.form1.plant.value;
    /* FOOTER Auto Suggestion */
	$('.footerSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'FOOTER',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_FOOTER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.FOOTERMST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.FOOTER+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}			
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});

}
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


