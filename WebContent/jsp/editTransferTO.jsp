<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Consignment Order Printout (With Price)";
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


		document.form1.ShipTo.value="";
		 document.form1.Project.value="";
		 document.form1.RCBNO.value="";
		 document.form1.CUSTOMERRCBNO.value="";

		 document.form1.UENNO.value="";
		 document.form1.CUSTOMERUENNO.value="";
		 
		 document.form1.PreparedBy.value="";
		 document.form1.DeliveryDate.value="";
		  document.form1.prdDeliveryDate.value="";
			document.form1.INCOTERM.value="";
            document.form1.Employee.value="";
//             document.form1.Container.value="";
    		document.form1.BRAND.value="";
    		document.form1.HSCODE.value="";
    		 document.form1.COO.value="";
    		 document.form1.OrderDiscount.value="";
    		 document.form1.ShippingCost.value="";
    		 document.form1.Adjustment.value="";
    		 document.form1.Total.value="";
    		    document.form1.REMARK1.value="";
    		    document.form1.REMARK2.value="";
                document.form1.Seller.value="";
                document.form1.SellerSignature.value="";
                document.form1.Buyer.value="";
                document.form1.BuyerSignature.value="";
                document.form1.CompanyDate.value="";
                document.form1.CompanyName.value="";
                document.form1.CompanyStamp.value="";
                document.form1.CompanySig.value="";
                document.form1.ProductRatesAre.value="";
                document.form1.TermsDetails.value="";
		
		document.form1.Date.value="";
		document.form1.OrderNo.value="";
		document.form1.RefNo.value="";
		document.form1.SoNo.value="";
		  document.form1.Rate.value="";
	        document.form1.TaxAmount.value="";
	        document.form1.Amt.value="";
	        document.form1.Discount.value="";
	        document.form1.NetRate.value="";
	        document.form1.DisplayByOrderType.value="";

		document.form1.Item.value="";
		document.form1.Description.value="";
		document.form1.OrderQty.value="";
		document.form1.UOM.value="";

		document.form1.Footer1.value="";
		document.form1.Footer2.value="";
                document.form1.Footer3.value="";
		document.form1.Footer4.value="";
                 document.form1.printDetailDesc.checked = false;
                 document.form1.Orientation.value = "Landscape";
                 
                 document.form1.printLocStock.checked = false;
                 document.form1.printWithproject.checked = false;
                 document.form1.printWithDeliveryDate.checked = false;
                 document.form1.printWithPrdDeliveryDate.checked = false;
                 document.form1.printEmployee.checked = false;
//                  document.form1.DisplayContainer.checked = false;
         		document.form1.printwithbrand.checked = false;
        		document.form1.printwithhscode.checked = false;
        		document.form1.printwithcoo.checked = false;
        		 document.form1.DisplayByOrdertype.checked = false;
                 document.form1.printBarcode.checked = false;
                 document.form1.printCustRemarks.checked = false;
                 document.form1.printCustTerms.checked = false;
                 document.form1.printWithUENNO.checked = false;
                 document.form1.printWithCustomerUENNO.checked = false;
                 document.form1.printwithcompanysig.checked = false;
     	        document.form1.printwithcompanyseal.checked = false;
	}
	
	function onAdd(){
     		
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_TO_RCPT_HDR";
   		document.form1.submit();
   	
	}
// 	function headerContainer(){
// 		if(document.form1.DisplayContainer.checked)
// 		{
// 		document.form1.Container.readOnly = true;		
// 		document.getElementById('Container').getAttributeNode('class').value="inactivegry";
// 		}
// 		else{
// 			document.form1.Container.readOnly = false;
// 			document.getElementById('Container').getAttributeNode('class').value="";
// 		}
// 	}
	function headerReadable(){
		if(document.form1.DisplayByOrdertype.checked)
		{
		document.form1.OrderHeader.readOnly = true;		
		document.getElementById('OrderHeader').getAttributeNode('class').value="form-control inactiveGry";
		}
		else{
			document.form1.OrderHeader.readOnly = false;
			document.getElementById('OrderHeader').getAttributeNode('class').value="form-control";
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
	String OrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",printCustTerms="",printCustRemarks="",printWithUENNO="",printWithCustomerUENNO="",printwithProduct="",printBarcode="",CompanySig="",CompanyStamp="",printRoundoffTotalwithDecimal="",coo="",CompanyName="",CompanyDate="",Buyer="",BuyerSignature="",SellerSignature="",Seller="",remark1="",remark2="",shippingcost="",Adjustment="",Total="",orderdiscount="",printwithcoo="",
			printwithhscode="",hscode="",printwithbrand="",brand="",Amt="",NetRate="",Discount="",Rate="",TaxAmount="",DisplayByOrderType = ""/* ,DisplayContainer="",Container="" */,printEmployee="",Employee="",incoterm="",prdDeliveryDate="",printwithcompanysig="",printwithcompanyseal="",printWithDeliveryDate="",printWithPrdDeliveryDate="",deliverydate="",shipto="",PreparedBy="",customerrcbno="",UenNo="",CusUenNO="",rcbno="",project="",printwithproject="",Date="",OrderNo ="",printDetailDesc="",printLocStock="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="";
	String Description = "", OrderQty  = "",ProductRatesAre="", UOM = "",Footer1="",Footer2="",Footer3="",Footer4="",Orientation="";
	int FOOTER_SIZE=0;
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));

	     TOUtil toUtil = new TOUtil();
	
         Map m= toUtil.getTOReceiptHdrDetails(plant);
         
         if(!m.isEmpty()){
        	 OrderHeader= (String) m.get("HDR1");
             InvoiceOrderToHeader = (String) m.get("HDR2");
             FromHeader = (String) m.get("HDR3");
             
           
            shipto=(String)m.get("SHIPTO");
//             printLocStock = (String)m.get("PrintLocStock");
            project=(String)m.get("PROJECT");
            printwithproject=(String)m.get("PRINTWITHPROJECT");
            rcbno=(String)m.get("RCBNO");
            customerrcbno=(String)m.get("CUSTOMERRCBNO");
            
            UenNo=(String)m.get("UENNO");
            CusUenNO=(String)m.get("CUSTOMERUENNO");
            
            PreparedBy = (String) m.get("PREPAREDBY");
            deliverydate=(String)m.get("DELIVERYDATE");
            printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
            prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
            printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
            printWithUENNO = (String) m.get("PRINTWITHUENNO");
            printWithCustomerUENNO = (String) m.get("PRINTWITHCUSTOMERUENNO");
             incoterm=(String)m.get("INCOTERM");
             Employee=(String)m.get("EMPLOYEE");
             printEmployee=(String)m.get("PRINTEMPLOYEE");
//              Container = (String)m.get("Container");
//              DisplayContainer = (String)m.get("DisplayContainer");
             brand=(String)m.get("BRAND");
             Amt=(String)m.get("AMT");
             TaxAmount=(String)m.get("TAXAMOUNT");
             NetRate=(String)m.get("NETRATE");
             Discount=(String)m.get("DISCOUNT");
             Rate=(String)m.get("RATE");
             printwithbrand = (String)m.get("PRINTWITHBRAND");
             hscode=(String)m.get("HSCODE");
             printwithhscode = (String)m.get("PRITNWITHHSCODE");
             coo=(String)m.get("COO");
             printwithcoo = (String)m.get("PRITNWITHCOO");
             orderdiscount=(String)m.get("ORDERDISCOUNT");
             shippingcost=(String)m.get("SHIPPINGCOST");
             Adjustment=(String)m.get("ADJUSTMENT");
             Total=(String)m.get("TOTAL");
             remark1=(String)m.get("REMARK1");
             remark2=(String)m.get("REMARK2");
             Seller = (String) m.get("SELLER");
             SellerSignature = (String) m.get("SELLERSIGNATURE");
             Buyer = (String) m.get("BUYER");
             BuyerSignature = (String) m.get("BUYERSIGNATURE");
             printwithcompanysig=(String)m.get("PRINTWITHCOMPANYSIG");
		     printwithcompanyseal=(String)m.get("PRINTWITHCOMPANYSEAL");
             CompanyDate=(String)m.get("COMPANYDATE");
             CompanyName=(String)m.get("COMPANYNAME");
             CompanyStamp=(String)m.get("COMPANYSTAMP");
             CompanySig=(String)m.get("COMPANYSIG");
             DisplayByOrderType = (String)m.get("DISPLAYBYORDERTYPE");
//              printBarcode = (String)m.get("PRINTBARCODE");
             printCustRemarks= (String)m.get("PCUSREMARKS");
             TermsDetails = (String) m.get("TERMSDETAILS");
             RefNo = (String) m.get("REFNO");
             
             Date = (String) m.get("DATE");
             OrderNo = (String) m.get("ORDERNO");
             
             RefNo = (String) m.get("REFNO");
             SoNo = (String) m.get("SONO");
             
             Item = (String) m.get("ITEM");
             Description = (String) m.get("DESCRIPTION");
             OrderQty = (String) m.get("ORDERQTY");
             UOM = (String) m.get("UOM");
             ProductRatesAre=(String) m.get("PRODUCTRATESARE");
             Terms = (String) m.get("TERMS");
             printRoundoffTotalwithDecimal = (String)m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
             printwithProduct = (String)m.get("PRINTWITHPRODUCT");
             
             Footer1 = (String) m.get("F1");
             Footer2 = (String) m.get("F2");
              Footer3 = (String) m.get("F3");
             Footer4 = (String) m.get("F4");
              printDetailDesc = (String)m.get("PRINTXTRADETAILS");
              printCustTerms = (String)m.get("PRINTCUSTERMS");
              Orientation=(String)m.get("PrintOrientation");
              if(!Footer1.isEmpty())
            	   	 FOOTER_SIZE=FOOTER_SIZE+1;
            	    if(!Footer2.isEmpty())
            	   	 FOOTER_SIZE=FOOTER_SIZE+1;
            	    if(!Footer3.isEmpty())
            	   	 FOOTER_SIZE=FOOTER_SIZE+1;
            	    if(!Footer4.isEmpty())
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
		TermsDetails  = "";
		
		 shipto="";
		 customerrcbno="";
		 
		 UenNo="";
		 CusUenNO="";
			
		 rcbno="";
   	     PreparedBy="";
		 project="";
		 printwithproject="";
		 deliverydate="";
	  printWithDeliveryDate="";
	  printWithUENNO="";
	  printWithCustomerUENNO="";
				  prdDeliveryDate="";
				  printWithPrdDeliveryDate="";
				  incoterm="";
				  Employee="";
				  printEmployee="";
// 				  Container="";
				  brand="";
				  Amt="";
				  TaxAmount="";
				  printBarcode="";
				  NetRate="";
				  Discount="";
				  Rate="";
				  printwithbrand="";
				  hscode="";
				  coo="";
				  orderdiscount="";
				  shippingcost="";
				  Adjustment="";
				  Total="";
				  remark1="";
					remark2="";
					Seller="";
					SellerSignature="";
					Buyer="";
					BuyerSignature="";
					CompanyDate="";
					CompanyName="";
					CompanyStamp="";	
					 CompanySig="";
					 printCustRemarks="";
					 ProductRatesAre="";
					  printCustTerms="";
					  Terms="";
						printRoundoffTotalwithDecimal="";
						printwithProduct="";
						 printwithcompanysig="";
							printwithcompanyseal="";
		Date= "";
		OrderNo  = "";
		RefNo   = "";
		SoNo=""; 
		Item   = "";
		Description ="";
		OrderQty =""; 
		UOM =""; 
		Footer1="";
		Footer2="";
                Footer3="";
		Footer4="";
                printDetailDesc="";
	}  
%>

 <CENTER><strong><%=res%></strong></CENTER>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Consignment Order Printout (With Price)</label></li>                                   
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


  


<form class="form-horizontal" name="form1" method="post">
  <%-- <div class="form-group">
  <label class="control-label col-sm-4" for="Orientation">Orientation:</label>
    <div class="col-sm-4">
    <label class="radio-inline">
     <input type = "radio" name="Orientation" value="Landscape" <%if(Orientation.equals("Landscape")) {%>checked <%}%>/>Landscape
    </label> 
    <label class="radio-inline">--%>
      <input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
    <!-- </label>
     </div>
     </div> -->
     
     
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Transfer Order Header">Consignment Order Header</label>
      <div class="col-sm-4">          
        <INPUT id ="OrderHeader" name="OrderHeader" type="TEXT" value="<%=OrderHeader%>" <%if(DisplayByOrderType.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%> class = "form-control" <%}%>
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			
      </div>
          <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertype" name = "DisplayByOrdertype" value = "DisplayByOrdertype" 
			 <%if(DisplayByOrderType.equals("1")) {%>checked <%}%>  onClick = "headerReadable();"/>Display By OrderType</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Invoice order to header">To Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
		<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimal" name = "printRoundoffTotalwithDecimal" value = "printRoundoffTotalwithDecimal" 
			<%if(printRoundoffTotalwithDecimal.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="From Header">From Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
	
			</div>
			
			 <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ship To">Ship To</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShipTo" type="TEXT" value="<%=shipto%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>

      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Project">Project</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Project" type="TEXT" value="<%=project%>"
			placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithproject" name = "printwithproject" value = "printwithproject" 
			 <%if(printwithproject.equals("1")) {%>checked <%}%> />Print With Project</label>
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
      <label class="control-label col-form-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
			<!-- RESVI UeN no 15.03.2021 -->
     <div class="form-group">
   <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-form-label col-sm-3" for="UEN No">Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-form-label col-sm-3" for="UEN No">TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UENNO" type="TEXT" value="<%=UenNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
   <%--  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNO" name = "printWithUENNO" value = "printWithUENNO" 
			 <%if(printWithUENNO.equals("1")) {%>checked <%}%> />Print With UEN</label>
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
      <label class="control-label col-form-label col-sm-3" for="RCBNO" id="TaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      </div>
    
     <div class="form-group">
      <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-form-label col-sm-3" for="Customer UEN No">Customer Unique Entity Number (UEN)</label>
      <%}else{ %>  
      <label class="control-label col-form-label col-sm-3" for="Customer UEN No">Customer TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERUENNO" type="TEXT" value="<%=CusUenNO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    <div class="form-inline">
      <div class="col-sm-4">
      <%if(sCountry.equals("Singapore")) {%>
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithCustomerUENNO" name = "printWithCustomerUENNO" value = "printWithCustomerUENNO" 
			<%if(printWithCustomerUENNO.equals("1")) {%>checked <%}%> />Print With Customer UEN </label>
			<%}else{ %>  
			 <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithCustomerUENNO" name = "printWithCustomerUENNO" value = "printWithCustomerUENNO" 
			<%if(printWithCustomerUENNO.equals("1")) {%>checked <%}%> />Print With Customer TRN </label>
			<%} %>
      </div>
      </div>
      </div>
      
			
			<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
      
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="CUSTOMER RCBNO" id="CustomerTaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERRCBNO" type="TEXT" value="<%=customerrcbno%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      </div>
      
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Rates Are">Product Rates Are</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProductRatesAre" type="TEXT" value="<%=ProductRatesAre%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div>  
      
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
      </div>
			
<div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ref No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			     <INPUT type="hidden" name="plant" value=<%=plant%>>
        <INPUT type="hidden" name="F1" id="F1" value="<%=Footer1%>">
        <INPUT type="hidden" name="F2" id="F2" value="<%=Footer2%>">
        <INPUT type="hidden" name="F3" id="F3" value="<%=Footer3%>">
        <INPUT type="hidden" name="F4" id="F4" value="<%=Footer4%>">
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
      <label class="control-label col-sm-3" for="Delivery">Delivery Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DeliveryDate" type="TEXT" value="<%=deliverydate%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithDeliveryDate" name = "printWithDeliveryDate" value = "printWithDeliveryDate" 
			 <%if(printWithDeliveryDate.equals("1")) {%>checked <%}%>  />Print With Delivery Date</label>
      </div>
      </div>
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="prdDeliveryDate">Product Delivery Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="prdDeliveryDate" type="TEXT" value="<%=prdDeliveryDate%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDate" name = "printWithPrdDeliveryDate" value = "printWithPrdDeliveryDate" 
		 	<%if(printWithPrdDeliveryDate.equals("1")) {%>checked <%}%>  />Print With Product Delivery Date</label>
      </div>
      </div>
      </div>  
            <div class="form-group">
      <label class="control-label col-sm-3" for="Reference No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
     
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
         <div class="form-group">
      <label class="control-label col-sm-3" for="Employee">Employee</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Employee" type="TEXT" value="<%=Employee%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployee" name = "printEmployee" value = "printEmployee" 
		 	<%if(printEmployee.equals("1")) {%>checked <%}%>  />Print With Employee </label>
      </div>
      </div>
      </div> 
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Payment Type">Payment Type</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Terms" type="TEXT" value="<%=Terms%>"  placeholder="Max 30 Characters"	size="25" MAXLENGTH=30>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-3" for="Terms Details">Terms Details</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TermsDetails" type="TEXT" value="<%=TermsDetails%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printCustTerms" name = "printCustTerms" value = "printCustTerms" 
			<%if(printCustTerms.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
      </div>                      
        </div>
   <div class="tab-pane fade" id="productDetail">
        <br>
        <div class="form-group">
      <label class="control-label col-sm-3 for="SoNo">SoNo</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			<div class="form-inline">
      	<div class="col-sm-4">
      	<label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithProduct" name = "printwithProduct" value = "printwithProduct" 
		<%if(printwithProduct.equals("1")) {%>checked <%}%> />Print With Product ID</label>
      </div>
      </div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-3 for="Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
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
      <label class="control-label col-sm-3 for="Order Qty">Order Qty</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderQty" type="TEXT" value="<%=OrderQty%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			</div>
			</div>
<!-- 			<div class="form-group"> -->
<!--       <label class="control-label col-sm-3" for="Container">Container</label> -->
<!--       <div class="col-sm-4">           -->
<%--        <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50> --%>
<!--       </div> -->
<!--       <div class="form-inline"> -->
<!--       <div class="col-sm-4"> -->
<!--       <label class="checkbox-inline">       -->
<!--       <input type = "checkbox" id = "DisplayContainer" name = "DisplayContainer" value = "DisplayContainer"  -->
		 <%--  <%if(DisplayContainer.equals("1")) {%>checked <%}%>  />Display Container </label> --%>
<!--       </div> -->
<!--       </div> -->
<!--       </div> -->
           <div class="form-group">
      <label class="control-label col-sm-3" for="brand">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRAND" type="TEXT" value="<%=brand%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "printwithbrand" 
			 <%if(printwithbrand.equals("1")) {%>checked <%}%>  />Print With Brand </label>
      </div>
      </div>
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="HScode">HSCODE(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=hscode%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscode" name = "printwithhscode" value = "printwithhscode" 
			 <%if(printwithhscode.equals("1")) {%>checked <%}%>  />Sales Print With HSCODE</label>
      </div>
      </div>
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="Country Of Origin">COO(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COO" type="TEXT" value="<%=coo%>" size="25"  placeholder="Max 50 Characters" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcoo" name = "printwithcoo" value = "printwithcoo" 
			 <%if(printwithcoo.equals("1")) {%>checked <%}%>  />Sales Print With COO</label>
      </div>
      </div>
      </div>
              <div class="form-group">
      <label class="control-label col-sm-3" for="Rate">Rate</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Rate" type="TEXT" value="<%=Rate%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div> 
      </div>
            <div class="form-group">
    <label class="control-label col-sm-3" for="Discount">Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Discount" type="TEXT" value="<%=Discount%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group" style="display: none;">
    <label class="control-label col-sm-3" for="Net Amount">Net Amount</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="NetRate" type="TEXT" value="<%=NetRate%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>   
      </div>
             <div class="form-group">
     <label class="control-label col-sm-3" for="Tax Amount">Tax Amount</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="TaxAmount" type="TEXT" value="<%=TaxAmount%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group">
     <label class="control-label col-sm-3" for="Amount">Amount</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Amt" type="TEXT" value="<%=Amt%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
        </div>
          <div class="tab-pane fade" id="cal">
        <br>
           <div class="form-group">
      <label class="control-label col-sm-3" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderDiscount" type="TEXT" value="<%=orderdiscount%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
           <div class="form-group">
      <label class="control-label col-sm-3" for="Shipping Cost">Shipping Cost</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShippingCost" type="TEXT" value="<%=shippingcost%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div> 
              <div class="form-group">
      <label class="control-label col-sm-3" for="Adjustment">Adjustment</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Adjustment" type="TEXT" value="<%=Adjustment%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
    </div>  
    <div class="form-group">
      <label class="control-label col-sm-3" for="Total">Total</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
    </div>      
        </div> 
        <div class="tab-pane fade" id="remark">
        <br>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks">Remarks1</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>"  placeholder="Max 100 Characters" size="25" MAXLENGTH=100>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks2">Remarks2</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK2" type="TEXT" value="<%=remark2%>"  placeholder="Max 100 Characters" size="25" MAXLENGTH=100>
      </div>
       </div>      
        </div>
        <div class="tab-pane fade" id="sign">
        <br>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Seller">Seller</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Seller" type="TEXT" value="<%=Seller%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
        </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SellerSignature" type="TEXT" value="<%=SellerSignature%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Buyer" type="TEXT" value="<%=Buyer%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BuyerSignature" type="TEXT" value="<%=BuyerSignature%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyDate" type="TEXT" value="<%=CompanyDate%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=100>
      </div>
      </div>
           <div class="form-group">
      <label class="control-label col-sm-3" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyName" type="TEXT" value="<%=CompanyName%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=100>
      </div>
      </div>
       <div class="form-group">
      <label class="control-label col-sm-3" for="Company Stamp">Company Stamp</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyStamp" type="TEXT" value="<%=CompanyStamp%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=100>
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
      <label class="control-label col-sm-3" for="Signature">Signature</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="CompanySig" type="TEXT" value="<%=CompanySig%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=100>
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
     <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
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
    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabel').innerHTML = "Customer "+ d +" No";
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
	if(rowCount!=4)
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
		alert("Can not add more then 4 footer ");
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