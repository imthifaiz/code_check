<!-- //CREATED BY NAVAS FEB17 TIME 1.31AM -->
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Consignment Order Printout";
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
<!-- <style>
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 18px;
}
</style> -->
<style>
  .pay-select-icon-invoice {
    position: absolute;
    right: 22px;
    top: 12px;
    z-index: 2;
    vertical-align: middle;
    font-size: 10px;
    opacity: 0.5;
}
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
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
			document.form1.TermsDetails.value="";
			
		 document.form1.PreparedBy.value="";
		 document.form1.ProductRatesAre.value="";
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
		
		document.form1.Date.value="";
		document.form1.OrderNo.value="";
		document.form1.RefNo.value="";
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
                 document.form1.printDetailDesc.checked = false;
                 document.form1.Orientation.value = "Landscape";
                 
                 document.form1.printLocStock.checked = false;
                 document.form1.printWithproject.checked = false;
                 document.form1.printWithDeliveryDate.checked = false;
                 document.form1.printWithPrdDeliveryDate.checked = false;
                 document.form1.printEmployee.checked = false;
                 document.form1.printCustTerms.checked = false;
//                  document.form1.DisplayContainer.checked = false;
         		document.form1.printwithbrand.checked = false;
        		document.form1.printwithhscode.checked = false;
        		document.form1.printwithcoo.checked = false;
        		 document.form1.DisplayByOrdertype.checked = false;
                 document.form1.printBarcode.checked = false;
                 document.form1.printCustRemarks.checked = false;
                 //resvi
                 document.form1.printWithUENNO.checked = false;
                 document.form1.printWithCustomerUENNO.checked = false;
                 document.form1.printwithcompanysig.checked = false;
     	        document.form1.printwithcompanyseal.checked = false;

                 //TO
                 document.form2.OrderHeaderTO.value="";
         		document.form2.InvoiceOrderToHeaderTO.value="";
         		document.form2.FromHeaderTO.value="";

         		document.form2.Shiptoo.value="";
         		 document.form2.ProjectTO.value="";
         		 document.form2.RCBNOTO.value="";
         		 document.form2.CUSTOMERRCBNOTO.value="";

         		 document.form2.UENNOTO.value="";
         		document.form2.CUSTOMERUENNOTO.value="";
         		 document.form2.printCustTermsTO.checked = false;
         		document.form2.TermsDetailsTO.value="";
         		 
         		 document.form2.PreparedByTO.value="";
         		 document.form2.ProductRatesAreTO.value="";
         		 document.form2.DeliveryDateTO.value="";
         		  document.form2.prdDeliveryDateTO.value="";
         			document.form2.INCOTERMTO.value="";
                     document.form2.EmployeeTO.value="";
//                      document.form2.Container.value="";
             		document.form2.BRANDTO.value="";
             		document.form2.HSCODETO.value="";
             		 document.form2.COOTO.value="";
             		 document.form2.OrderDiscountTO.value="";
             		 document.form2.ShippingCostTO.value="";
             		    document.form2.REMARK1TO.value="";
             		    document.form2.REMARK2TO.value="";
                         document.form2.SellerTO.value="";
                         document.form2.SellerSignatureTO.value="";
                         document.form2.Buyer.valueTO="";
                         document.form2.BuyerSignatureTO.value="";
                         document.form2.CompanyDateTO.value="";
                         document.form2.CompanyNameTO.value="";
                         document.form2.CompanyStampTO.value="";
                         document.form2.CompanySigTO.value="";
         		
         		document.form2.DateTO.value="";
         		document.form2.OrderNoTO.value="";
         		document.form2.RefNoTO.value="";
         		document.form2.SoNoTO.value="";

         		document.form2.ItemTO.value="";
         		document.form2.DescriptionTO.value="";
         		document.form2.OrderQtyTO.value="";
         		document.form2.UOMTO.value="";

         		document.form2.Footer1TO.value="";
         		document.form2.Footer2TO.value="";
                         document.form2.Footer3TO.value="";
         		document.form2.Footer4TO.value="";
         		document.form2.Footer5TO.value="";
        		document.form2.Footer6TO.value="";
        		document.form2.Footer7TO.value="";
        		document.form2.Footer8TO.value="";
        		document.form2.Footer9TO.value="";
                          document.form2.printDetailDescTO.checked = false;
                          document.form2.OrientationTO.value = "Landscape";
                          
                          document.form2.printLocStockTO.checked = false;
                          document.form2.printWithprojectTO.checked = false;
                          document.form1.printWithDeliveryDateTO.checked = false;
                          document.form2.printWithPrdDeliveryDateTO.checked = false;
                          document.form2.printEmployeeTO.checked = false;
//                           document.form2.DisplayContainer.checked = false;
                  		document.form2.printwithbrandTO.checked = false;
                 		document.form2.printwithhscodeTO.checked = false;
                 		document.form2.printwithcooTO.checked = false;
                 		 document.form2.DisplayByOrdertypeTO.checked = false;
                          document.form2.printBarcodeTO.checked = false;
                          document.form2.printCustRemarksTO.checked = false;
//                           resvi
                          document.form2.printWithUENNOTO.checked = false;
                          document.form2.printWithCustomerUENNOTO.checked = false;
                          document.form2.printwithcompanysealTO.checked = false;
                          document.form2.printwithcompanysigTO.checked = false;
	}
	
	function onAdd(){
     		
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_RCPT_HDR";
   		document.form1.submit();
   	
	}
	function onAddTO(){
 		
   		document.form2.action  =  "/track/SettingsServlet?Submit=EDIT_RCPT_HDRTO";
   		document.form2.submit();
   	
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

	function headerReadableTO(){
		if(document.form2.DisplayByOrdertypeTO.checked)
		{
		document.form2.OrderheaderTO.readOnly = true;		
		document.getElementById('OrderheaderTO').getAttributeNode('class').value="form-control inactiveGry";
		}
		else{
			document.form2.OrderheaderTO.readOnly = false;
			document.getElementById('OrderheaderTO').getAttributeNode('class').value="form-control";
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
	String fieldDesc=StrUtils.fString(request.getParameter("result"));  
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String OrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",printCustRemarks="",printBarcode="",CompanySig="",CompanyStamp="",coo="",CompanyName="",CompanyDate="",Buyer="",BuyerSignature="",SellerSignature="",Seller="",remark1="",remark2="",shippingcost="",orderdiscount="",printwithcoo="",
			printwithhscode="",hscode="",printwithbrand="",brand="",DisplayByOrderType = "",/* DisplayContainer="", *//* Container="", */printCustTerms="",printEmployee="",printwithcompanysig="",printwithcompanyseal="",Employee="",incoterm="",prdDeliveryDate="",printWithDeliveryDate="",printWithPrdDeliveryDate="",printWithUENNO="",printWithCustomerUENNO="",deliverydate="",shipto="",ProductRatesAre="",PreparedBy="",customerrcbno="",UenNo="",CusUenNO="",rcbno="",project="",printwithproject="",Date="",OrderNo ="",printDetailDesc="",printLocStock="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="";
	String Description = "", OrderQty  = "", UOM = "",Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",Orientation="";
	int FOOTER_SIZE=0;
	
	//TO
	String OrderHeaderTO  = "", InvoiceOrderToHeaderTO  = "", FromHeaderTO  = "",printCustRemarksTO="",printBarcodeTO="",CompanySigTO="",CompanyStampTO="",cooTO="",CompanyNameTO="",CompanyDateTO="",BuyerTO="",BuyerSignatureTO="",SellerSignatureTO="",SellerTO="",remark1TO="",remark2TO="",shippingcostTO="",orderdiscountTO="",printwithcooTO="",
			printwithhscodeTO="",hscodeTO="",printwithbrandTO="",brandTO="",DisplayByOrderTypeTO = "",/* DisplayContainer="", *//* Container="", */printEmployeeTO="",EmployeeTO="",incotermTO="",prdDeliveryDateTO="",printWithUENNOTO="",printWithCustomerUENNOTO="",printWithDeliveryDateTO="",printWithPrdDeliveryDateTO="",deliverydateTO="",shiptoTO="",PreparedByTO="",ProductRatesAreTO="",customerrcbnoTO="",UennoTO="",CusUennoTO="",rcbnoTO="",projectTO="",printwithprojectTO="",DateTO="",OrderNoTO ="",printDetailDescTO="",printLocStockTO="";
	String RefNoTO   = "", TermsTO  = "", TermsDetailsTO   = "",printCustTermsTO="",SoNoTO ="",ItemTO ="";
	String DescriptionTO = "", OrderQtyTO  = "", UOMTO = "",Footer1TO="",Footer2TO="",Footer3TO="",Footer4TO="",Footer5TO="",Footer6TO="",Footer7TO="",Footer8TO="",Footer9TO="",printwithcompanysigTO="",printwithcompanysealTO="",OrientationTO="";
	int FOOTER_SIZETO=0;
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));

	     TOUtil toUtil = new TOUtil();
	
         Map m= toUtil.getReceiptHdrDetails(plant,"Upon Creation");
         
         if(!m.isEmpty()){
        	 OrderHeader= (String) m.get("HDR1");
             InvoiceOrderToHeader = (String) m.get("HDR2");
             FromHeader = (String) m.get("HDR3");
             
           
            shipto=(String)m.get("SHIPTO");
            printLocStock = (String)m.get("PRINTLOCSTOCK");
            project=(String)m.get("PROJECT");
            printwithproject=(String)m.get("PRINTWITHPROJECT");
            printWithUENNO=(String)m.get("PRINTWITHUENNO");
            printWithCustomerUENNO=(String)m.get("PRINTWITHCUSTOMERUENNO");
            printwithcompanysig=(String)m.get("PRINTWITHCOMPANYSIG");
		     printwithcompanyseal=(String)m.get("PRINTWITHCOMPANYSEAL");
            rcbno=(String)m.get("RCBNO");
            customerrcbno=(String)m.get("CUSTOMERRCBNO");
            
            UenNo=(String)m.get("UENNO");
            CusUenNO=(String)m.get("CUSTOMERUENNO");
            
            PreparedBy = (String) m.get("PREPAREDBY");
            ProductRatesAre=(String) m.get("PRODUCTRATESARE");
            deliverydate=(String)m.get("DELIVERYDATE");
            printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
            prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
            printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
             incoterm=(String)m.get("INCOTERM");
             Employee=(String)m.get("EMPLOYEE");
             printEmployee=(String)m.get("PRINTEMPLOYEE");
             printCustTerms = (String)m.get("PRINTCUSTERMS");
             TermsDetails = (String) m.get("TERMSDETAILS");
//              Container = (String)m.get("Container");
//              DisplayContainer = (String)m.get("DisplayContainer");
             brand=(String)m.get("BRAND");
             printwithbrand = (String)m.get("PRINTWITHBRAND");
             hscode=(String)m.get("HSCODE");
             printwithhscode = (String)m.get("PRITNWITHHSCODE");
             coo=(String)m.get("COO");
             printwithcoo = (String)m.get("PRITNWITHCOO");
             orderdiscount=(String)m.get("ORDERDISCOUNT");
             shippingcost=(String)m.get("SHIPPINGCOST");
             remark1=(String)m.get("REMARK1");
             remark2=(String)m.get("REMARK2");
             Seller = (String) m.get("SELLER");
             SellerSignature = (String) m.get("SELLERSIGNATURE");
             Buyer = (String) m.get("BUYER");
             BuyerSignature = (String) m.get("BUYERSIGNATURE");
             CompanyDate=(String)m.get("COMPANYDATE");
             CompanyName=(String)m.get("COMPANYNAME");
             CompanyStamp=(String)m.get("COMPANYSTAMP");
             CompanySig=(String)m.get("COMPANYSIG");
             DisplayByOrderType = (String)m.get("DISPLAYBYORDERTYPE");
             printBarcode = (String)m.get("PRINTBARCODE");
             printCustRemarks= (String)m.get("PCUSREMARKS");
             
             
             Date = (String) m.get("DATE");
             OrderNo = (String) m.get("ORDERNO");
             
             RefNo = (String) m.get("REFNO");
             SoNo = (String) m.get("SONO");
             
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
              printDetailDesc = (String)m.get("PRINTXTRADETAILS");
              Orientation=(String)m.get("PrintOrientation");
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
         //TO
  Map mTO= toUtil.getReceiptHdrDetails(plant,"Pick and Issue");
         
         if(!mTO.isEmpty()){
        	 OrderHeaderTO= (String) mTO.get("HDR1");
             InvoiceOrderToHeaderTO = (String) mTO.get("HDR2");
             FromHeaderTO = (String) mTO.get("HDR3");
             
           
            shiptoTO=(String)mTO.get("SHIPTO");            
            projectTO=(String)mTO.get("PROJECT");
            printwithprojectTO=(String)mTO.get("PRINTWITHPROJECT");
            printWithUENNOTO=(String)mTO.get("PRINTWITHUENNO");
            printWithCustomerUENNOTO=(String)mTO.get("PRINTWITHCUSTOMERUENNO");
            rcbnoTO=(String)mTO.get("RCBNO");
            customerrcbnoTO=(String)mTO.get("CUSTOMERRCBNO");
            
            UennoTO=(String)mTO.get("UENNO");
            CusUennoTO=(String)mTO.get("CUSTOMERUENNO");
            
            PreparedByTO = (String) mTO.get("PREPAREDBY");
            ProductRatesAreTO = (String) mTO.get("PRODUCTRATESARE");
            deliverydateTO=(String)mTO.get("DELIVERYDATE");
            printWithDeliveryDateTO = (String) mTO.get("PRINTWITHDELIVERYDATE");
            prdDeliveryDateTO = (String) mTO.get("PRODUCTDELIVERYDATE");
            printWithPrdDeliveryDateTO = (String) mTO.get("PRINTWITHPRODUCTDELIVERYDATE");
             incotermTO=(String)mTO.get("INCOTERM");
             EmployeeTO=(String)mTO.get("EMPLOYEE");
             printEmployeeTO=(String)mTO.get("PRINTEMPLOYEE");
             printCustTermsTO = (String)mTO.get("PRINTCUSTERMS");
             TermsDetailsTO = (String) mTO.get("TERMSDETAILS");
//              Container = (String)m.get("Container");
//              DisplayContainer = (String)m.get("DisplayContainer");
             brandTO=(String)mTO.get("BRAND");
             printwithbrandTO = (String)mTO.get("PRINTWITHBRAND");
             hscodeTO=(String)mTO.get("HSCODE");
             printwithhscodeTO = (String)mTO.get("PRITNWITHHSCODE");
             cooTO=(String)mTO.get("COO");
             printwithcooTO = (String)mTO.get("PRITNWITHCOO");
             orderdiscountTO=(String)mTO.get("ORDERDISCOUNT");
             shippingcostTO=(String)mTO.get("SHIPPINGCOST");
             remark1TO=(String)mTO.get("REMARK1");
             remark2TO=(String)mTO.get("REMARK2");
             SellerTO = (String) mTO.get("SELLER");
             SellerSignatureTO = (String) mTO.get("SELLERSIGNATURE");
             BuyerTO = (String) mTO.get("BUYER");
             BuyerSignatureTO = (String) mTO.get("BUYERSIGNATURE");
             CompanyDateTO=(String)mTO.get("COMPANYDATE");
             CompanyNameTO=(String)mTO.get("COMPANYNAME");
             CompanyStampTO=(String)mTO.get("COMPANYSTAMP");
             CompanySigTO=(String)mTO.get("COMPANYSIG");
             DisplayByOrderTypeTO = (String)mTO.get("DISPLAYBYORDERTYPE");
             printBarcodeTO = (String)mTO.get("PRINTBARCODE");
             printCustRemarksTO= (String)mTO.get("PCUSREMARKS");
             printwithcompanysigTO= (String)mTO.get("PRINTWITHCOMPANYSIG");
             printwithcompanysealTO= (String)mTO.get("PRINTWITHCOMPANYSEAL");
             
             DateTO = (String) mTO.get("DATE");
             OrderNoTO = (String) mTO.get("ORDERNO");
             
             RefNoTO = (String) mTO.get("REFNO");
             SoNoTO = (String) mTO.get("SONO");
             
             ItemTO = (String) mTO.get("ITEM");
             DescriptionTO = (String) mTO.get("DESCRIPTION");
             OrderQtyTO = (String) mTO.get("ORDERQTY");
             UOMTO = (String) mTO.get("UOM");
             
             Footer1TO = (String) mTO.get("F1");
             Footer2TO = (String) mTO.get("F2");
             Footer3TO = (String) mTO.get("F3");
             Footer4TO = (String) mTO.get("F4");
             Footer5TO = (String) mTO.get("F5");
             Footer6TO = (String) mTO.get("F6");
             Footer7TO = (String) mTO.get("F7");
             Footer8TO = (String) mTO.get("F8");
             Footer9TO = (String) mTO.get("F9");
              printDetailDescTO = (String)mTO.get("PRINTXTRADETAILS");
              OrientationTO=(String)mTO.get("PrintOrientation");
              if(!Footer1TO.isEmpty())
            	   	 FOOTER_SIZETO=FOOTER_SIZETO+1;
            	    if(!Footer2TO.isEmpty())
            	   	 FOOTER_SIZETO=FOOTER_SIZETO+1;
            	    if(!Footer3TO.isEmpty())
            	   	 FOOTER_SIZETO=FOOTER_SIZETO+1;
            	    if(!Footer4TO.isEmpty())
            	   	 FOOTER_SIZETO=FOOTER_SIZETO+1;
            	    if(!Footer5TO.isEmpty())
           	      	 FOOTER_SIZETO=FOOTER_SIZETO+1;
           	       if(!Footer6TO.isEmpty())
           	      	 FOOTER_SIZETO=FOOTER_SIZETO+1;
           	       if(!Footer7TO.isEmpty())
           	      	 FOOTER_SIZETO=FOOTER_SIZETO+1;
           	       if(!Footer8TO.isEmpty())
           	      	 FOOTER_SIZETO=FOOTER_SIZETO+1;
           	       if(!Footer9TO.isEmpty())
           	      	 FOOTER_SIZETO=FOOTER_SIZETO+1;
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
		
		 shipto="";
		 customerrcbno="";
		 
		 UenNo="";
	     CusUenNO="";
	     
			rcbno="";
			  PreparedBy="";
			  ProductRatesAre="";
			  project="";
				printwithproject="";
				  deliverydate="";
				  printWithDeliveryDate="";
				  prdDeliveryDate="";
				  printWithPrdDeliveryDate="";
				  incoterm="";
				  Employee="";
				  printEmployee="";
				  printCustTerms="";
				  TermsDetails  = "";
// 				  Container="";
				  brand="";
				  printwithbrand="";
				  hscode="";
				  printLocStock="";
				  coo="";
				  orderdiscount="";
				  shippingcost="";
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
				
					 printBarcode ="";
					 printWithUENNO="";
					 printWithCustomerUENNO="";
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
		Footer5="";
        Footer6="";
		Footer7="";
		Footer8="";
		Footer9="";
                printDetailDesc="";
                
                //TO CREATED BY NAVAS
                OrderHeaderTO  = "";
        		InvoiceOrderToHeaderTO  = "";
        		FromHeaderTO  = "";
        		printLocStockTO ="";
        		printBarcodeTO = "";
        		
        		shiptoTO="";
        		 customerrcbnoTO="";
        		 UennoTO="";
        	     	CusUennoTO="";
        			rcbnoTO="";
        			  PreparedByTO="";
        			  ProductRatesAreTO="";
        			  projectTO="";
        				printwithprojectTO="";
        				  deliverydateTO="";
        				  printWithDeliveryDateTO="";
        				  prdDeliveryDateTO="";
        				  printWithPrdDeliveryDateTO="";
        				  incotermTO="";
        				  EmployeeTO="";
        				  printEmployeeTO="";
//         				  Container="";
        				  brandTO="";
        				  printwithbrandTO="";
        				  hscodeTO="";
        				  cooTO="";
        				  orderdiscountTO="";
        				  shippingcostTO="";
        				  remark1TO="";
        					remark2TO="";
        					SellerTO="";
        					SellerSignatureTO="";
        					BuyerTO="";
        					BuyerSignatureTO="";
        					CompanyDateTO="";
        					CompanyNameTO="";
        					CompanyStampTO="";	
        					 CompanySigTO="";
        					 printCustRemarksTO="";
        					 printWithUENNOTO="";
        					 printWithCustomerUENNOTO="";
        					 printwithcompanysigTO="";
        					 printwithcompanysealTO="";
        					 TermsDetailsTO="";
        					 printCustTermsTO="";
        		DateTO= "";
        		OrderNoTO  = "";
        		RefNoTO   = "";
        		SoNoTO=""; 
        		ItemTO   = "";
        		DescriptionTO ="";
        		OrderQtyTO =""; 
        		UOMTO =""; 
        		Footer1TO="";
        		Footer2TO="";
                        Footer3TO="";
        		Footer4TO="";
        		Footer5TO="";
        		Footer6TO="";
                Footer7TO="";
        		Footer8TO="";
        		Footer9TO="";
                        printDetailDescTO="";
	}  
%>

      <!-- //created by navas -->
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div>
	 <input type="number" id="inpayment" name="inpayment" style="display: none;" value="0">
            <div class="tab">
      
              <button class="tablinks active" onclick="openPayment(event, 'payment')">Upon Creation</button>
 		 <button class="tablinks" onclick="openPayment(event, 'voucher')">After Pick and Issue</button>
		</div>
	
	<div id="payment" class="tabcontent active" style="display: block;">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Consignment Printout Upon Creation</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
	  <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Consignment Printout Upon Creation</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="container-fluid">
		<form id="creationForm" class="form-horizontal" name="form1"  method="post">
		
		 <input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
		<input type="hidden" name="OrderType" value="Upon Creation">
		 
		<div class="form-group">
      <label  class="control-label col-form-label col-sm-3"  for="Transfer Order Header">Consignment Order Header</label>
      <div class="col-sm-4">          
        <INPUT id="OrderHeader" name="OrderHeader" type="TEXT" value="<%=OrderHeader%>" <%if(DisplayByOrderType.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%> class = "form-control" <%}%>
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
			
      </div>
          <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertype" name = "DisplayByOrdertype" value = "DisplayByOrdertype" 
		 <%if(DisplayByOrderType.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/>Display By OrderType</label>
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
     <input type = "checkbox" id = "printBarcode" name = "printBarcode" value = "printBarcode" 
			 <%if(printBarcode.equals("1")) {%>checked <%}%>  />Print With Barcode for Product ID</label>
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
       <input type = "checkbox" id = "printLocStock" name = "printLocStock" value = "printLocStock" 
			 <%if(printLocStock.equals("1")) {%>checked <%}%>  />Print Loc with Stock on hand</label>
      </div>
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
			 <!-- resvi UeN no 15.03.2021 -->
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
     <%-- <div class="form-inline">
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
<!--       RESVI ENDS -->
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
        <!-- <li class="nav-item">
            <a href="#cal" class="nav-link" data-toggle="tab">Calculations</a>
        </li> -->
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
			 <%if(printWithDeliveryDate.equals("1")) {%>checked <%}%>/>Print With Delivery Date</label>
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
      <label class="control-label col-sm-3" for="SoNo">SoNo</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
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
			
         <%--  <div class="form-group">
      <label class="control-label col-sm-3" for="Container">Container</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "DisplayContainer" name = "DisplayContainer" value = "DisplayContainer" 
		 <%if(DisplayContainer.equals("1")) {%>checked <%}%> />Display Container </label>
      </div>
      </div>
      </div> --%>
      
          <div class="form-group">
      <label class="control-label col-sm-3" for="brand">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRAND" type="TEXT" value="<%=brand%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "printwithbrand" 
			<%if(printwithbrand.equals("1")) {%>checked <%}%> />Print With Brand </label>
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
			<%if(printwithhscode.equals("1")) {%>checked <%}%> />Sales Print With HSCODE</label>
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
			<%if(printwithcoo.equals("1")) {%>checked <%}%> />Sales Print With COO</label>
      </div>
      </div>
      </div>
        </div>
  <%-- <div class="tab-pane fade" id="cal">
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
        </div>       --%> 
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

<div id="voucher" class="tabcontent">
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Consignment Printout After Pick and Issue</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
	<div class="container-fluid">	
	<form id="AfterPickAndIssueForm" class="form-horizontal" name="form2" method="post">
	<input type="hidden" name="OrderTypeTO" value="Pick and Issue">
	
	<input type = "hidden" name="OrientationTO" value="Portrait" <%if(OrientationTO.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
	
	  <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Transfer Order Header">Consignment Order Header</label>
      <div class="col-sm-4">          
        <INPUT id= "OrderheaderTO" name="OrderHeaderTO" type="TEXT" value="<%=OrderHeaderTO%>" <%if(DisplayByOrderTypeTO.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%> class = "form-control"<%}%>
			placeholder="Max 30 Characters" size="50" MAXLENGTH=20>
			
      </div>
          <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertypeTO" name = "DisplayByOrdertypeTO" value = "DisplayByOrdertypeTO" 
		 <%if(DisplayByOrderTypeTO.equals("1")) {%>checked <%}%> onClick = "headerReadableTO();"/>Display By OrderType</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Invoice order to header">To Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="InvoiceOrderToHeaderTO" type="TEXT" value="<%=InvoiceOrderToHeaderTO%>"
			placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
        <%--  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarksTO" name = "printCustRemarksTO" value = "printCustRemarksTO" 
			<%if(printCustRemarksTO.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>   --%>
      </div>
      
            
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="From Header">From Header</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="FromHeaderTO" type="TEXT" value="<%=FromHeaderTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
			</div>
			<%--  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printLocStockTO" name = "printLocStockTO" value = "printLocStockTO" 
			 <%if(printLocStockTO.equals("1")) {%>checked <%}%>  />Print Loc with Stock on hand</label>
      </div>
      </div> --%>
			</div>
     
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ship To">Ship To</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShipToo" type="TEXT" value="<%=shiptoTO%>" placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <%-- <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printBarcode" name = "printBarcode" value = "printBarcode" 
			 <%if(printBarcode.equals("1")) {%>checked <%}%>  />Print With Barcode for Product ID</label>
      </div>
      </div> --%>
      </div>
      
       <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Project">Project</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProjectTO" type="TEXT" value="<%=projectTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithprojectTO" name = "printwithprojectTO" value = "printwithprojectTO" 
			 <%if(printwithprojectTO.equals("1")) {%>checked <%}%> />Print With Project</label>
      </div>
      </div>
      </div>

       
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order No">Order Number</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderNoTO" type="TEXT" value="<%=OrderNoTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
			</div>
			</div>
             
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DateTO" type="TEXT" value="<%=DateTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
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
        <INPUT class="form-control" name="UENNOTO" type="TEXT" value="<%=UennoTO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    <%-- <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNOTO" name = "printWithUENNOTO" value = "printWithUENNOTO" 
			 <%if(printWithUENNOTO.equals("1")) {%>checked <%}%> />Print With UEN</label>
      </div>
      </div> --%>
      
      
      <div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
  		<label class="radio-inline">
				      	<%if(sCountry.equals("Singapore")) {%>
				      	<input type="radio" name="printWithUENNOTO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNOTO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With UEN
    					<%}else{ %>  
				      	<input type="radio" name="printWithUENNOTO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNOTO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With TRN
    					<%} %>
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="printWithUENNOTO" type = "radio" value="0"  id = "NonStock" <%if (printWithUENNOTO.equalsIgnoreCase("0")) {%> checked <%}%> >Print With <%=taxbylabel%> No
				     	</label>
     	</div>
		</div>
      </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="RCBNO" id="TaxLabelTO"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RCBNOTO" type="TEXT" value="<%=rcbnoTO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
    
     <div class="form-group">
      <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-form-label col-sm-3" for="Customer UEN No">Customer Unique Entity Number (UEN)</label>
      <%}else{ %>  
      <label class="control-label col-form-label col-sm-3" for="Customer UEN No">Customer TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERUENNOTO" type="TEXT" value="<%=CusUennoTO%>"
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
<!--     ENDS -->
			
	       
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="CUSTOMER RCBNO" id="CustomerTaxLabelTO"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERRCBNOTO" type="TEXT" value="<%=customerrcbnoTO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
      
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Rates Are">Product Rates Are</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProductRatesAreTO" type="TEXT" value="<%=ProductRatesAreTO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div>  
      
	    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PreparedByTO" type="TEXT" value="<%=PreparedByTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div> 
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ref No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNoTO" type="TEXT" value="<%=RefNoTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=50>
			</div>
			     <INPUT type="hidden" name="plant" value=<%=plant%>>
        <INPUT type="hidden" name="F1" id="F1TO" value="<%=Footer1TO%>">
        <INPUT type="hidden" name="F2" id="F2TO" value="<%=Footer2TO%>">
        <INPUT type="hidden" name="F3" id="F3TO" value="<%=Footer3TO%>">
        <INPUT type="hidden" name="F4" id="F4TO" value="<%=Footer4TO%>">
        <INPUT type="hidden" name="F5" id="F5TO" value="<%=Footer5TO%>">
        <INPUT type="hidden" name="F6" id="F6TO" value="<%=Footer6TO%>">
        <INPUT type="hidden" name="F7" id="F7TO" value="<%=Footer7TO%>">
        <INPUT type="hidden" name="F8" id="F8TO" value="<%=Footer8TO%>">
        <INPUT type="hidden" name="F9" id="F9TO" value="<%=Footer9TO%>">
			</div>
      
	  	<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#other2" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#productDetail2" class="nav-link" data-toggle="tab">Product Details</a>
        </li>
       <!--  <li class="nav-item">
            <a href="#cal2" class="nav-link" data-toggle="tab">Calculations</a>
        </li> -->
        <li class="nav-item">
            <a href="#remark2" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
         <li class="nav-item">
            <a href="#sign2" class="nav-link" data-toggle="tab">Signature</a>
        </li>
        <li class="nav-item">
            <a href="#footer2" class="nav-link" data-toggle="tab">Footer</a>
        </li>
        </ul>
         <div class="tab-content clearfix">
   <div class="tab-pane active" id="other2">
        <br>
        
         <div class="form-group">
      <label class="control-label col-sm-3" for="Delivery">Delivery Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DeliveryDateTO" type="TEXT" value="<%=deliverydateTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithDeliveryDateTO" name = "printWithDeliveryDateTO" value = "printWithDeliveryDateTO" 
			 <%if(printWithDeliveryDateTO.equals("1")) {%>checked <%}%>/>Print With Delivery Date</label>
      </div>
      </div>
      </div>
    
       <div class="form-group">
      <label class="control-label col-sm-3" for="prdDeliveryDate">Product Delivery Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="prdDeliveryDateTO" type="TEXT" value="<%=prdDeliveryDateTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDateTO" name = "printWithPrdDeliveryDateTO" value = "printWithPrdDeliveryDateTO" 
			<%if(printWithPrdDeliveryDateTO.equals("1")) {%>checked <%}%>  />Print With Product Delivery Date</label>
      </div>
      </div>
      </div>  
      
       <div class="form-group">
      <label class="control-label col-sm-3" for="Reference No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNoTO" type="TEXT" value="<%=RefNoTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
     
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERMTO" type="TEXT" value="<%=incotermTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Employee">Employee</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="EmployeeTO" type="TEXT" value="<%=EmployeeTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployeeTO" name = "printEmployeeTO" value = "printEmployeeTO" 
			<%if(printEmployeeTO.equals("1")) {%>checked <%}%>  />Print With Employee </label>
      </div>
      </div>
      </div> 
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Terms Details">Terms Details</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TermsDetailsTO" type="TEXT" value="<%=TermsDetailsTO%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printCustTermsTO" name = "printCustTermsTO" value = "printCustTermsTO" 
			<%if(printCustTermsTO.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
      </div>   
       
        </div>
        
    <div class="tab-pane fade" id="productDetail2">
        <br>
               <div class="form-group">
      <label class="control-label col-sm-3 for="SoNo">SoNo</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNoTO" type="TEXT" value="<%=SoNoTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=100>
			</div>
			</div>
			
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ItemTO" type="TEXT" value="<%=ItemTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=100>
			</div>
			</div>
			
      <div class="form-group">
      <label class="control-label col-sm-3 for="Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DescriptionTO" type="TEXT" value="<%=DescriptionTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=100>
			</div>
			    <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printDetailDescTO" name = "printDetailDescTO" value = "printDetailDescTO" 
			<%if(printDetailDescTO.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
			</div>
			
      <div class="form-group">
      <label class="control-label col-sm-3 for="Order Qty">Order Qty</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderQtyTO" type="TEXT" value="<%=OrderQtyTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=100>
			</div>
			</div>
			
     <div class="form-group">
      <label class="control-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UOMTO" type="TEXT" value="<%=UOMTO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=100>
			</div>
			</div>
			
     <%--  <div class="form-group">
      <label class="control-label col-sm-3" for="Container">Container</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="ContainerDO" type="TEXT" value="<%=ContainerDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>     
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplayContainerDO" name = "DisplayContainerDO" value = "DisplayContainerDO" 
		 <%if(DisplayContainerDO.equals("1")) {%>checked <%}%> />Display Container</label>
      </div>
      </div>
      </div> --%>
      
        <div class="form-group">
      <label class="control-label col-sm-3" for="brand">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRANDTO" type="TEXT" value="<%=brandTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrandTO" name = "printwithbrandTO" value = "printwithbrandTO" 
			<%if(printwithbrandTO.equals("1")) {%>checked <%}%> />Print With Brand </label>
      </div>
      </div>
      </div>
      
        <div class="form-group">
      <label class="control-label col-sm-3" for="HScode">HSCODE(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="HSCODETO" type="TEXT" value="<%=hscodeTO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscodeTO" name = "printwithhscodeTO" value = "printwithhscodeTO" 
			<%if(printwithhscodeTO.equals("1")) {%>checked <%}%> />Sales Print With HSCODE</label>
      </div>
      </div>
      </div>
    
        <div class="form-group">
      <label class="control-label col-sm-3" for="Country Of Origin">COO(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COOTO" type="TEXT" value="<%=cooTO%>" size="25"  placeholder="Max 50 Characters" size = "15" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcooTO" name = "printwithcooTO" value = "printwithcooTO" 
			<%if(printwithcooTO.equals("1")) {%>checked <%}%> />Sales Print With COO</label>
      </div>
      </div>
      </div>
      
        </div>
            
<%--  <div class="tab-pane fade" id="cal2">
        <br>
        <div class="form-group">
      <label class="control-label col-sm-3" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="OrderDiscountDO" type="TEXT" value="<%=orderdiscountDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Shipping Cost">Shipping Cost</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ShippingCostDO" type="TEXT" value="<%=shippingcostDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>     
        </div> --%>
        
        <div class="tab-pane fade" id="remark2">
        <br>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks">Remarks1</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK1TO" type="TEXT" value="<%=remark1TO%>"  placeholder="Max 100 Characters" size="15" MAXLENGTH=100>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks2">Remarks2</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK2TO" type="TEXT" value="<%=remark2TO%>"  placeholder="Max 100 Characters" size="15" MAXLENGTH=100>
      </div>
       </div>      
        </div>
        
        <div class="tab-pane fade" id="sign2">
        <br>
              <div class="form-group">
      <label class="control-label col-sm-3" for="Seller">Seller</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SellerTO" type="TEXT" value="<%=SellerTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
        </div>
        
     <div class="form-group">
      <label class="control-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SellerSignatureTO" type="TEXT" value="<%=SellerSignatureTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BuyerTO" type="TEXT" value="<%=BuyerTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      
     <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BuyerSignatureTO" type="TEXT" value="<%=BuyerSignatureTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyDateTO" type="TEXT" value="<%=CompanyDateTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-3" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyNameTO" type="TEXT" value="<%=CompanyNameTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
     
     <div class="form-group">
      <label class="control-label col-sm-3" for="Company Stamp">Company Stamp</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyStampTO" type="TEXT" value="<%=CompanyStampTO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
          <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithcompanysealTO" name = "printwithcompanysealTO" value = "printwithcompanysealTO" 
		 <%if(printwithcompanysealTO.equals("1")) {%>checked <%}%> />Print With Digital Stamp</label>
      </div>
      </div>
      </div> 

  		         <div class="form-group">
      <label class="control-label col-sm-3" for="Signature">Signature</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="CompanySigTO" type="TEXT" value="<%=CompanySigTO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div> 
        <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithcompanysigTO" name = "printwithcompanysigTO" value = "printwithcompanysigTO" 
		 <%if(printwithcompanysigTO.equals("1")) {%>checked <%}%> />Print With Digital Signature</label>
      </div>
      </div> 
        </div>
        
        </div>
            <div class="tab-pane fade" id="footer2">
        <br>
     
    
    <div class="form-group">
        <TABLE id="footertblTO" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD style="width: 17%;">
		<label class="control-label col-form-label col-sm-2" for="Footer1">Footer1</label></TD>		
		<TD align="center"  style="width: 83%;"><div class="col-sm-8"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRowTO('footertblTO');return false;"></span>
		<INPUT class="form-control footerSearch" name="Footer1TO" id="Footer1TO"  placeholder="Max 200 Characters" type = "TEXT" value="<%=Footer1TO%>" size="100"  MAXLENGTH=200>		        
       	</div></div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="FOOTER_SIZETO" value="<%=FOOTER_SIZETO%>" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowTO('footertblTO','');return false;">+ Add another Footer</a>
						</div>
					</div>
				</div>
        </div>    
        
        </div>
        </div>
		
			   <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
     <!--   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAddTO();">Save</button>&nbsp;&nbsp;
      	
      </div>
    </div>
		
		</form>
		</div>
		</div>

  


</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabel').innerHTML = "Customer "+ d +" No";
    //TO
    document.getElementById('TaxLabelTO').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabelTO').innerHTML = "Customer "+ d +" No";	
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
	
    var FOOTER_SIZE2 = document.form2.FOOTER_SIZETO.value;
    if(FOOTER_SIZE2!=0)
	{
	//alert(FOOTER_SIZE1);
	
	for ( var i = 0; i < FOOTER_SIZE2; i++) {
		if(i!=0)
			{
				var footerval= document.getElementById("F"+(i+1)+"TO").value; 
				addRowTO('footertblTO',footerval);
			}
	}
	}
    $(".footerSearch").typeahead('destroy');
	addSuggestionSearchTO();
});
function openPayment(evt, pay) {
	  // Declare all variables
	  var i, tabcontent, tablinks;

	  // Get all elements with class="tabcontent" and hide them
	  tabcontent = document.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  }

	  // Get all elements with class="tablinks" and remove the class "active"
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }

	  // Show the current tab, and add an "active" class to the button that opened the tab
	  document.getElementById(pay).style.display = "block";
	  evt.currentTarget.className += " active";
	  
	  if(pay=="payment")
		  $("input[name ='inpayment']").val("0");
	  else if(pay=="voucher")
		  $("input[name ='inpayment']").val("1");
	}
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
function addRowTO(tableID,footer) {
	
  	
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
	var itemCellText =  "<div class=\"col-sm-8\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowTO('footertblTO');return false;\"></span><INPUT class=\"form-control footerSearch\" name=\"Footer"+newRowCount+"TO\" ";
	itemCellText = itemCellText+ " id=\"Footer"+newRowCount+"TO\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Max 200 Characters\" MAXLENGTH=\"200\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 9 footer ");
		}
	$(".footerSearch").typeahead('destroy');
	addSuggestionSearchTO();
}
function deleteRowTO(tableID) {
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
function addSuggestionSearchTO()
{
	var plant = document.form2.plant.value;
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


