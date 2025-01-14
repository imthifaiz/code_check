<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.BillDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Bill Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<style>
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 18px;
}
.inactiveGry {
	BACKGROUND: #C0C0C0; 
	/* BORDER-BOTTOM: #888888 1px solid; 
	BORDER-LEFT: #888888 1px solid; 
	BORDER-RIGHT: #888888 1px solid; 
	BORDER-TOP: #888888 1px solid; 
	COLOR: #000000; 
	FONT-FAMILY: Arial, Arial; 
	FONT-SIZE: 12px; */
}
</style>
<script type="text/javascript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
		document.form1.BillHeader.value="";
		document.form1.BillToHeader.value="";
		document.form1.FromHeader.value="";
		document.form1.OrderDate.value="";
		document.form1.OrderNo.value="";
		document.form1.RefNo.value="";
		document.form1.PurchaseLocation.value="";
		document.form1.TermsDetails.value="";
		document.form1.TermsDetail.value="";
		document.form1.SoNo.value="";
		document.form1.Item.value="";
		document.form1.Description.value="";
		document.form1.BillQty.value="";
		document.form1.Rate.value="";
		document.form1.PaymentsMade.value="";
		document.form1.Amt.value="";
		document.form1.SubTotal.value="";
		document.form1.TotalTax.value="";
		document.form1.Total.value="";
		document.form1.Footer1.value="";
		document.form1.Footer2.value="";
        document.form1.Footer3.value="";
		document.form1.Footer4.value="";
		document.form1.Footer5.value="";
		document.form1.Footer6.value="";
		document.form1.Footer7.value="";
		document.form1.Footer8.value="";
		document.form1.Footer9.value="";
		
        document.form1.printDetailDesc.checked = false;
        document.form1.printSupTerms.checked = false;
        
 		document.form1.REMARK1.value="";
        
        document.form1.DueDate.value="";
        document.form1.ShipTo.value="";
        document.form1.CompanyDate.value="";
        document.form1.CompanyName.value="";
        document.form1.CompanyStamp.value="";
        document.form1.CompanySig.value="";
        document.form1.Discount.value="";
        document.form1.Adjustment.value="";
        document.form1.ProductRatesAre.value="";
        
        document.form1.SHIPPINGCOST.value="";
		document.form1.ORDERDISCOUNT.value="";
		document.form1.INCOTERM.value="";
		document.form1.RCBNO.value="";
		document.form1.SUPPLIERRCBNO.value="";

		document.form1.UENNO.value="";
		document.form1.SUPPLIERUENNO.value="";
		
		document.form1.GRNO.value="";
		document.form1.GRNDATE.value="";
		document.form1.printwithproductremarks.checked = false;
		document.form1.printwithbrand.checked = false;
		document.form1.ProductDiscount.value="";
		document.form1.PreparedBy.value="";
        document.form1.AuthSignature.value="";
		
        
        document.form1.BalanceDue.value="";
        document.form1.printWithPurchaseLocation.checked = false;
        
        document.form1.BILLNO.value="";
        document.form1.BILLDATE.value="";

//      RESVI STARTS
        document.form1.Project.value="";
        document.form1.printWithproject.checked = false;
        document.form1.printWithUENNO.checked = false;
        document.form1.printWithSupplierUENNO.checked = false;
        document.form1.printwithcompanysig.checked = false;
        document.form1.printwithcompanyseal.checked = false;
        document.form1.TransportMode.value="";
        document.form1.printwithtransportmode.checked = false;
        document.form1.printwithshipingadd.checked = false;
        document.form1.DISPLAYBYORDERTYPE.checked = false;
        document.form1.employee.value="";
        document.form1.printWithemployee.checked = false;
//         RESVI ENDS
	}
	
	function onAdd(){
     	    document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_BILL_HDR";
            document.form1.submit();
   	
	}
 	function headerReadable(){
		if(document.form1.DISPLAYBYORDERTYPE.checked)
		{
		document.form1.BillHeader.readOnly = true;		
		document.getElementById('BillHeader').getAttributeNode('class').value="form-control inactiveGry";
		}
		else{
			document.form1.BillHeader.readOnly = false;
			document.getElementById('BillHeader').getAttributeNode('class').value="form-control";
		}
	} 


	
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sCountry = (String) session.getAttribute("COUNTRY");
	String taxbylabel= ub.getTaxByLable(plant);
        
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String OrderHeader  = "", ToHeader  = "", FromHeader  = "",OrderNo ="", OrderDate = "",DISPLAYBYORDERTYPE = "",
    RefNo   = "", PurchaseLocation  = "", TermsDetails   = "",TermsDetail = "",printSupTerms ="",SoNo ="",Item ="",
	printDetailDesc="",	Description = "", BillQty  = "",Uom="",project="",rcbno="",supplierrcbno="",ProductRatesAre="",UenNo="",SupUenNO="",grno="", grndate="", printwithproductremarks="", printwithbrand="",
	Rate = "",PaymentsMade="",Amt="",SubTotal="",    TotalTax="",Total="",
	Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",
	remark1="",DueDate="",shipto="",printwithproject="",TransportMode="",printwithtransportmode="",printWithIncoterm="",printWithUENNO="",printWithSupplierUENNO="",printwithshipingadd="",
	CompanyDate="",CompanyName="",CompanyStamp="",CompanySig="",ProductDiscount="",
	Discount="",Adjustment="",orderdiscount="",shippingcost="", incoterm="", 
	PreparedBy="",AuthSignature="",BalanceDue="",printWithPurchaseLocation="",printwithcompanysig="",printwithcompanyseal="",employee="",printWithemployee="",
	BillNo="",BillDate="";
	int FOOTER_SIZE=0;
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));
	BillDAO billDAO = new BillDAO();
         Map m= billDAO.getBillHeaderDetails(plant);
         if(!m.isEmpty()){
         OrderHeader= (String) m.get("BILLHEADER");
         FromHeader = (String) m.get("FROMHEADER");
         ToHeader = (String) m.get("TOHEADER");
         shipto=(String)m.get("SHIPTO");
         BillNo=(String) m.get("BILLNO");
	     BillDate=(String) m.get("BILLDATE");
	     grno=(String)m.get("GRNO");
	     grndate=(String)m.get("GRNDATE");
	     printwithproductremarks=(String)m.get("PRINTWITHPRODUCTREMARKS");
	     printwithbrand=(String)m.get("PRINTWITHBRAND");
	     OrderDate = (String) m.get("ORDERDATE");
	     OrderNo = (String) m.get("ORDERNO");
         RefNo = (String) m.get("REFNO");
         rcbno=(String)m.get("RCBNO");
         supplierrcbno=(String)m.get("SUPPLIERRCBNO");
         ProductRatesAre=(String) m.get("PRODUCTRATESARE");
         
         UenNo=(String)m.get("UENNO");
         SupUenNO=(String)m.get("SUPPLIERUENNO");
         
         PreparedBy = (String) m.get("PREPAREDBY");
         DueDate=(String)m.get("DUEDATE");
         
         TermsDetails = (String) m.get("PAYMENTTERMS");
         TermsDetail=(String)m.get("TERMSDETAILS");
         printSupTerms = (String)m.get("PRINTSUPTERMS");
         PurchaseLocation = (String) m.get("PURCHASELOCATION");
         printWithPurchaseLocation = (String) m.get("PRINTPURCHASELOCATION");
         
        	 
	     incoterm=(String)m.get("INCOTERM");
	     printWithIncoterm = (String) m.get("PRINTINCOTERM");
	      
         SoNo = (String) m.get("SONO");
         Item = (String) m.get("ITEM");
         Description = (String) m.get("DESCRIPTION");
         printDetailDesc = ((String)m.get("PRINTDETAILDESCRIPTION"));
        	 
         BillQty = (String) m.get("BILLQTY");
         Uom = (String) m.get("UOM");
         Rate = (String) m.get("RATE");
         ProductDiscount=(String)m.get("ITEMDISCOUNT");
         Amt = (String) m.get("ITEMAMOUNT");
         
         SubTotal = (String) m.get("SUBTOTAL");
         orderdiscount=(String)m.get("ORDERDISCOUNT");
         Discount = (String) m.get("DISCOUNT");
         shippingcost=(String)m.get("SHIPPINGCOST");
         TotalTax = (String) m.get("TAX");
         Adjustment = (String) m.get("ADJUSTMENT");
         Total = (String) m.get("TOTAL");
         PaymentsMade = (String) m.get("PAYMENTMADE");
         BalanceDue = (String) m.get("BALANCEDUE");

         CompanyDate=(String)m.get("COMPANYDATE");
         CompanyName=(String)m.get("COMPANYNAME");
         CompanyStamp=(String)m.get("COMPANYSTAMP");
         CompanySig=(String)m.get("COMPANYSIG");
         AuthSignature = (String) m.get("AUTHSIGNATURE");
         
         
         Footer1 = (String) m.get("FOOTER1");
         Footer2 = (String) m.get("FOOTER2");
         Footer3 = (String) m.get("FOOTER3");
         Footer4 = (String) m.get("FOOTER4");
         Footer5 = (String) m.get("FOOTER5");
         Footer6 = (String) m.get("FOOTER6");
         Footer7 = (String) m.get("FOOTER7");
         Footer8 = (String) m.get("FOOTER8");
         Footer9 = (String) m.get("FOOTER9");
         
         
     //  RESVI STARTS
         project=(String)m.get("PROJECT");
         printwithproject=(String)m.get("PRINTWITHPROJECT");
         printWithUENNO=(String)m.get("PRINTWITHUENNO");
         printWithSupplierUENNO=(String)m.get("PRINTWITHSUPPLIERUENNO");
         printwithcompanysig=(String)m.get("PRINTWITHCOMPANYSIG");
         printwithcompanyseal=(String)m.get("PRINTWITHCOMPANYSEAL");
         TransportMode=(String)m.get("TRANSPORT_MODE");
         printwithtransportmode=(String)m.get("PRINTWITHTRANSPORT_MODE");
         printwithshipingadd=(String)m.get("PRINTWITHSHIPINGADD");
         DISPLAYBYORDERTYPE=(String)m.get("DISPLAYBYORDERTYPE");
         employee=(String)m.get("EMPLOYEE");
         printWithemployee=(String)m.get("PRINTEMPLOYEE");
     //  RESVI ENDS
         
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
         
         remark1=(String)m.get("NOTES");
         } else {
         printWithPurchaseLocation="0";
         printDetailDesc="0";
         printWithIncoterm="0";
         
         }
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		FOOTER_SIZE=0;
		action = "";
		OrderHeader  = "";
		ToHeader  = "";
		FromHeader  = "";
		OrderDate="";
		OrderNo  = "";
		RefNo   = "";
		PurchaseLocation   = "";
		TermsDetails ="";
		printSupTerms = "";
		TermsDetail ="";
		SoNo=""; 
		Item   = "";
		Description ="";
		BillQty =""; 
        Uom =""; 
		Rate =""; 
		PaymentsMade =""; 
		SubTotal =""; 
		TotalTax =""; 
		Total =""; 
		Amt =""; 
		
		Footer1="";
		Footer2="";
        Footer3="";
		Footer4="";
		Footer5="";
        Footer6="";
		Footer7="";
		Footer8="";
		Footer9="";
        printDetailDesc="0";
		remark1="";
        DueDate="";
        shipto="";
        CompanyDate="";
        CompanyName="";
        CompanyStamp="";	
        CompanySig="";
        Discount="";
        Adjustment="";
        orderdiscount="";
        shippingcost="";
        incoterm="";
        rcbno="";
		supplierrcbno="";
		ProductRatesAre="";
		

		UenNo="";
		SupUenNO="";
		
		grno="";
		grndate="";
		printwithproductremarks="";
		printwithbrand="";
		ProductDiscount="";
		PreparedBy="";
		AuthSignature="";
	    
		BalanceDue="";
		printWithPurchaseLocation="0";
		printWithIncoterm="0";
		BillNo="";
		BillDate="";
// 	    RESVI STARTS
		project="";
		printwithproject="";
		printWithUENNO="";
		printWithSupplierUENNO="";
		printwithcompanysig="";
		printwithcompanyseal="";
		TransportMode="";
		printwithtransportmode="";
		printwithshipingadd="";
		DISPLAYBYORDERTYPE="";
		employee="";
		printWithemployee="";
//      RESVI ENDS
	}  
%>

 <CENTER><strong><%=res%></strong></CENTER>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Bill Printout</label></li>                                   
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

  

  <form class="form-horizontal" name="form1" method="post" id="form">

<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Bill header">Bill Header</label>
      <div class="col-sm-4">          
        <INPUT  id="BillHeader" name="BillHeader" type="TEXT" value="<%=OrderHeader%>" 
         <%if(DISPLAYBYORDERTYPE.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%>   class = "form-control"  <%}%> size="50" MAXLENGTH=30 placeholder="Max 30 Characters"/>
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
      
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DISPLAYBYORDERTYPE" name = "DISPLAYBYORDERTYPE" value = "DISPLAYBYORDERTYPE" 
			<%if(DISPLAYBYORDERTYPE.equals("1")) {%>checked <%}%> onClick = "headerReadable();" /> Display By OrderType</label>  
      </div>
      </div>
      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Bill to header">Bill To Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BillToHeader" type="TEXT" value="<%=ToHeader%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="from header">From Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Ship To">Shipping Address</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShipTo" type="TEXT" value="<%=shipto%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>
		<div class="form-inline">
      	<div class="col-sm-4">
      	<label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithshipingadd" name = "printwithshipingadd" value = "printwithshipingadd" 
			<%if(printwithshipingadd.equals("1")) {%>checked <%}%> />Print With Shipping Address</label>
      </div>
      </div>
    </div>
    
    
      <!--       RESVI STARTS TRANSPORT MODE -->
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Transport Mode">Transport Mode</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TransportMode" type="TEXT" value="<%=TransportMode%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtransportmode" name = "printwithtransportmode" value = "printwithtransportmode" 
			<%if(printwithtransportmode.equals("1")) {%>checked <%}%> />Print With Transport Mode</label>
      </div>
      </div>
      </div>
<!--       RESVI ENDS -->


    <!--       RESVI STARTS PROJECT -->
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Project">Project</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Project" type="TEXT" value="<%=project%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithproject" name = "printwithproject" value = "printwithproject" 
			<%if(printwithproject.equals("1")) {%>checked <%}%> />Print With Project</label>
      </div>
      </div>
      </div>
<!--       RESVI ENDS -->
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="BILL No">Bill No.</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BILLNO" type="TEXT" value="<%=BillNo%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>	  
    </div>
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="BILL Date">Bill Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BILLDATE" type="TEXT" value="<%=BillDate%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="order No">Order Number</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="OrderNo" type="TEXT" value="<%=OrderNo%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>  
      </div>
         </div>
         
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Order Date">Order Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ORDERDATE" type="TEXT" value="<%=OrderDate%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>      
    </div>
         
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="GR No">GRNO</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="GRNO" type="TEXT" value="<%=grno%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>	  
    </div>
    
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="GRNO Date">GRN Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="GRNDATE" type="TEXT" value="<%=grndate%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div>
    
     
    
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Reference No">Reference No.</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
       <!-- RESVI UeN no 15.03.2021 -->
     <div class="form-group">
     <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-form-label col-sm-2" for="UEN No">Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-form-label col-sm-2" for="TRN No">TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UENNO" type="TEXT" value="<%=UenNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
  <%--     <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithUENNO" name = "printWithUENNO" value = "printWithUENNO" 
			<%if(printWithUENNO.equals("1")) {%>checked <%}%> />Print With UEN </label>
      </div>
      </div> --%>
      
       <div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
  		<label class="radio-inline">
  							<%if(sCountry.equals("Singapore")) {%>
				      	<input type="radio" name="printWithUENNO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With UEN
    						<%}else{ %>  
				      	<input type="radio" name="printWithUENNO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With TRN
    						<%} %>
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="printWithUENNO" type = "radio" value="0"  id = "NonStock" <%if (printWithUENNO.equalsIgnoreCase("0")) {%> checked <%}%> >Print With <%=taxbylabel%> No
				     	</label>
     	</div>
		</div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="RCB No" id="TaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>      
    </div>
     <div class="form-group">
     <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-form-label col-sm-2" for="Supplier UEN No">Supplier Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-form-label col-sm-2" for="Supplier UEN No">Supplier TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SUPPLIERUENNO" type="TEXT" value="<%=SupUenNO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    <div class="form-inline">
      <div class="col-sm-4">
    <%if(sCountry.equals("Singapore")) {%>
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithSupplierUENNO" name = "printWithSupplierUENNO" value = "printWithSupplierUENNO" 
			<%if(printWithSupplierUENNO.equals("1")) {%>checked <%}%> />Print With Supplier UEN </label>
    <%}else{ %>  
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithSupplierUENNO" name = "printWithSupplierUENNO" value = "printWithSupplierUENNO" 
			<%if(printWithSupplierUENNO.equals("1")) {%>checked <%}%> />Print With Supplier TRN </label>
    <%} %>
      </div>
      </div>
    </div>
<!-- 	ENDS -->
	
    <INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
    
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier RCB No" id="SupplierTaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SUPPLIERRCBNO" type="TEXT" value="<%=supplierrcbno%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
              <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Rates Are">Product Rates Are</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProductRatesAre" type="TEXT" value="<%=ProductRatesAre%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div> 
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Due Date">Due Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DueDate" type="TEXT" value="<%=DueDate%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="bs-example">
    <ul class="nav nav-tabs" id="myTab" style="font-size: 94%;"> 
     	<li class="nav-item active">
            <a href="#other" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#product" class="nav-link" data-toggle="tab">Product Details</a>
        </li>
        <li class="nav-item">
            <a href="#calculations" class="nav-link" data-toggle="tab">Calculations</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Customer Notes</a>
        </li>
        <li class="nav-item">
            <a href="#signature" class="nav-link" data-toggle="tab">Signature</a>
        </li>
        <li class="nav-item">
            <a href="#footer" class="nav-link" data-toggle="tab">Footer</a>
        </li>
        
        </ul>
        
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="other">
        <br>
          
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Purchase Location">Purchase Location</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PurchaseLocation" type="TEXT" value="<%=PurchaseLocation%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      	<div class="col-sm-4">
      		<label class="checkbox-inline">      
        		<input type = "checkbox" id = "printWithPurchaseLocation" name = "printWithPurchaseLocation" value = "printWithPurchaseLocation" 
				<%if(printWithPurchaseLocation.equals("1")) {%>checked <%}%> />Print With Purchase Location</label>
      	</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>
      <div class="form-inline">
      	<div class="col-sm-4">
      		<label class="checkbox-inline">      
        		<input type = "checkbox" id = "printWithIncoterm" name = "printWithIncoterm" value = "printWithIncoterm" 
				<%if(printWithIncoterm.equals("1")) {%>checked <%}%> />Print With INCOTERM </label>
      	</div>
      </div>
    </div>
    

     
   
<!--   Author: Resviya , Create date: July 10,2021 -->
   
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="employee">Employee</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="employee" type="TEXT" value="<%=employee%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      	<div class="col-sm-4">
      		<label class="checkbox-inline">      
        		<input type = "checkbox" id = "printWithemployee" name = "printWithemployee" value = "printWithemployee" 
				<%if(printWithemployee.equals("1")) {%>checked <%}%> />Print With Employee</label>
      	</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Payment Terms">Payment Terms</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TermsDetails" type="TEXT" value="<%=TermsDetails%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Terms Details">Terms Details</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TermsDetail" type="TEXT" value="<%=TermsDetail%>" placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
	 <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printSupTerms" name = "printSupTerms" value = "printSupTerms" 
			<%if(printSupTerms.equals("1")) {%>checked <%}%> />Override with Supplier terms</label>
      </div>
      </div> 
    </div>
          
<!--     End -->

        </div>
        <div class="tab-pane fade" id="product">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="SoNo">So No.</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Id">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printDetailDesc" name = "printDetailDesc" value = "printDetailDesc" 
			<%if(printDetailDesc.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Bill Quantity">Bill Quantity</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BillQty" type="TEXT" value="<%=BillQty%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
       <div class="form-inline" style="display: none;">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithproductremarks" name = "printwithproductremarks" value = "printwithproductremarks" 
			<%if(printwithproductremarks.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="UOM">Unit Of Measure</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="UOM" type="TEXT" value="<%=Uom%>" placeholder="Max 20 Characters"
			size="50" MAXLENGTH=20>
      </div>
         <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "printwithbrand" 
			<%if(printwithbrand.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Rate">Rate</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Rate" type="TEXT" value="<%=Rate%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Discount">Product Discount</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="ProductDiscount" type="TEXT" value="<%=ProductDiscount%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Amount">Amount</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Amt" type="TEXT" value="<%=Amt%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
        
        </div>
        <div class="tab-pane fade" id="calculations">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Sub Total">Sub Total</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="SubTotal" type="TEXT" value="<%=SubTotal%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderDiscount" type="TEXT" value="<%=orderdiscount%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Discount">Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Discount" type="TEXT" value="<%=Discount%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Shipping Charge">Shipping Charge</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShippingCost" type="TEXT" value="<%=shippingcost%>" size="50" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Total Tax">Tax</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="TotalTax" type="TEXT" value="<%=TotalTax%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Adjustment">Adjustment</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Adjustment" type="TEXT" value="<%=Adjustment%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
    
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Total">Total</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>

<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Payments Made">Payments Made</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="PaymentsMade" type="TEXT" value="<%=PaymentsMade%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>    
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Balance Due">Balance Due</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BalanceDue" type="TEXT" value="<%=BalanceDue%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
        
        </div>
        <div class="tab-pane fade" id="remark">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Customer Notes</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
        
        </div>
        <div class="tab-pane fade" id="signature">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Authorized Signature">Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="AuthSignature" type="TEXT" value="<%=AuthSignature%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
 
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyDate" type="TEXT" value="<%=CompanyDate%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="CompanyName" type="TEXT" value="<%=CompanyName%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Company Stamp">Company Stamp</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyStamp" type="TEXT" value="<%=CompanyStamp%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcompanyseal" name = "printwithcompanyseal" value = "printwithcompanyseal" 
			<%if(printwithcompanyseal.equals("1")) {%>checked <%}%> />Print With Digital Stamp</label>
      </div>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Signature">Signature</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="CompanySig" type="TEXT" value="<%=CompanySig%>" placeholder="Max 30 Characters"
			size="50" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcompanysig" name = "printwithcompanysig" value = "printwithcompanysig" 
			<%if(printwithcompanysig.equals("1")) {%>checked <%}%> />Print With Digital Signature</label>
      </div>
      </div>
    </div>
        
        
        </div>
        <div class="tab-pane fade" id="footer">
        <br>
        <div class="form-group">
        <TABLE id="footertbl" width="100%" style="border-spacing: 0px 8px;">
		<TR>
		<TD style="width: 17%;"><label class="control-label col-form-label col-sm-2" for="Footer1">Footer1</label></TD>		
		<TD align="center" style="width: 83%;"><div class="col-sm-8"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('footertbl');return false;"></span><INPUT class="form-control footerSearch" name="Footer1" id="Footer1"  placeholder="Max 200 Characters" type = "TEXT" value="<%=Footer1%>" size="100"  MAXLENGTH=200>		        
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
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
	
    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    document.getElementById('SupplierTaxLabel').innerHTML = "Supplier "+ d +" No.";
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

