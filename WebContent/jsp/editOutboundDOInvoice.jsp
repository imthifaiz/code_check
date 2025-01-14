<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Edit Sales Order Printout (With Price)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
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
<script>

	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
		document.form1.OutboundOrderHeader.value="";
		document.form1.InvoiceOrderToHeader.value="";
		document.form1.FromHeader.value="";
		document.form1.Date.value="";
		document.form1.OrderNo.value="";
     	document.form1.RefNo.value="";
		document.form1.Terms.value="";
		document.form1.TermsDetails.value="";
		document.form1.SoNo.value="";
		document.form1.Item.value="";
		document.form1.Description.value="";
		document.form1.OrderQty.value="";
		document.form1.UOM.value="";
        document.form1.Container.value="";
		document.form1.Footer1.value="";
		document.form1.Footer2.value="";
        document.form1.Footer3.value="";
		document.form1.Footer4.value="";
		document.form1.Footer5.value="";
 		document.form1.Footer6.value="";
 		document.form1.Footer7.value="";
 		document.form1.Footer8.value="";
 		document.form1.Footer9.value="";
    	document.form1.DisplayByOrdertype.checked = false;
        document.form1.printDetailDesc.checked = false;
        document.form1.DisplayContainer.checked = false;
        document.form1.printCustTerms.checked = false;
        document.form1.printCustRemarks.checked = false;
        document.form1.printwithbrand.checked = false;
        document.form1.Rate.value="";
        document.form1.TaxAmount.value="";
        document.form1.Amt.value="";
        document.form1.REMARK1.value="";
        document.form1.REMARK2.value="";
        document.form1.DeliveryDate.value="";
        document.form1.Employee.value="";
        document.form1.ShipTo.value="";
        document.form1.CompanyDate.value="";
        document.form1.CompanyName.value="";
        document.form1.CompanyStamp.value="";
        document.form1.CompanySig.value="";
        document.form1.printEmployee.checked = false;
        document.form1.Orientation.value = "Landscape";
        document.form1.ShippingCost.value="";
		document.form1.OrderDiscount.value="";
		document.form1.INCOTERM.value="";
		document.form1.printwithproductremarks.checked = false;
		document.form1.printwithhscode.checked = false;
		document.form1.printwithcoo.checked = false;
		document.form1.printbuyer.checked = false;
		document.form1.printbuyersign.checked = false;
		document.form1.BRAND.value="";
		document.form1.HSCODE.value="";
        document.form1.COO.value="";
        document.form1.RCBNO.value="";
        //document.form1.INVOICENO.value="";
        document.form1.CUSTOMERRCBNO.value="";

    	document.form1.UENNO.value="";
		document.form1.CUSTOMERUENNO.value="";
        
        document.form1.TOTALAFTERDISCOUNT.value="";
        document.form1.PreparedBy.value="";
        document.form1.Seller.value="";
        document.form1.SellerSignature.value="";
        document.form1.Buyer.value="";
        document.form1.BuyerSignature.value="";
		document.form1.printRoundoffTotalwithDecimal.checked = false;
		document.form1.printwithProduct.checked = false;
		document.form1.printDiscountReport.checked = false;
		document.form1.printincoterm.checked = false;
        document.form1.Discount.value="";
        document.form1.NetRate.value="";
        
        document.form1.printWithDeliveryDate.checked = false;
        document.form1.prdDeliveryDate.value="";
        document.form1.printWithPrdDeliveryDate.checked = false;
        document.form1.calculateTaxwithShippingCost.checked = false;
        
        document.form1.Adjustment.value="";
        document.form1.ProductRatesAre.value="";
        
        document.form1.PaymentMade.value="";
        document.form1.BalanceDue.value="";
        document.form1.printPaymentMade.checked = false;
        document.form1.printBalanceDue.checked = false;
        document.form1.printShippingCost.checked = false;
        document.form1.printOrderDiscount.checked = false;
        document.form1.printAdjustment.checked = false;

//      RESVI STARTS
        document.form1.Project.value="";
        document.form1.printWithproject.checked = false;
        document.form1.printWithUENNO.checked = false;
        document.form1.printWithCustomerUENNO.checked = false;
        document.form1.printwithcompanysig.checked = false;
        document.form1.printwithcompanyseal.checked = false;
        document.form1.TransportMode.value="";
        document.form1.printwithtransportmode.checked = false;
        document.form1.printwithshipingadd.checked = false;
//         RESVI ENDS
        
        document.form2.OutboundOrderHeaderDO.value="";
		document.form2.InvoiceOrderToHeaderDO.value="";
		document.form2.FromHeaderDO.value="";
		document.form2.DateDO.value="";
		document.form2.OrderNoDO.value="";
     	document.form2.RefNoDO.value="";
		document.form2.TermsDO.value="";
		document.form2.TermsDetailsDO.value="";
		document.form2.SoNoDO.value="";
		document.form2.ItemDO.value="";
		document.form2.DescriptionDO.value="";
		document.form2.OrderQtyDO.value="";
		document.form2.UOMDO.value="";
        document.form2.ContainerDO.value="";
		document.form2.Footer1DO.value="";
		document.form2.Footer2DO.value="";
        document.form2.Footer3DO.value="";
		document.form2.Footer4DO.value="";
		document.form2.Footer5DO.value="";
		document.form2.Footer6DO.value="";
		document.form2.Footer7DO.value="";
		document.form2.Footer8DO.value="";
		document.form2.Footer9DO.value="";
    	document.form2.DisplayByOrdertypeDO.checked = false;
        document.form2.printDetailDescDO.checked = false;
        document.form2.DisplayContainerDO.checked = false;
        document.form2.printCustTermsDO.checked = false;
        document.form2.printCustRemarksDO.checked = false;
        document.form2.RateDO.value="";
        document.form2.TaxAmountDO.value="";
        document.form2.AmtDO.value="";
        document.form2.REMARK1DO.value="";
        document.form2.REMARK2DO.value="";
        document.form2.DeliveryDateDO.value="";
        document.form2.EmployeeDO.value="";
        document.form2.ShipToDO.value="";
        document.form2.CompanyDateDO.value="";
        document.form2.CompanyNameDO.value="";
        document.form2.CompanyStampDO.value="";
        document.form2.CompanySigDO.value="";
        document.form2.printEmployeeDO.checked = false;
        document.form2.OrientationDO.value = "Landscape";
        document.form2.ShippingCostDO.value="";
		document.form2.OrderDiscountDO.value="";
		document.form2.SubTotalDO.value="";
		document.form2.TotalDO.value="";
		document.form2.RoundoffDO.value="";
		document.form2.INCOTERMDO.value="";
		document.form2.printwithproductremarksDO.checked = false;
		document.form2.printwithhscodeDO.checked = false;
		document.form2.printwithcooDO.checked = false;
		document.form2.printwithbrandDO.checked = false;
		//document.form1.printpackinglist.checked = false;
		//document.form1.printdeliverynote.checked = false;
        document.form2.BRANDDO.value="";
		document.form2.HSCODEDO.value="";
        document.form2.COODO.value="";
        document.form2.RCBNODO.value="";
        document.form2.INVOICENODO.value="";
        document.form2.INVOICEDATEDO.value="";
        document.form2.CUSTOMERRCBNODO.value="";

        document.form2.UENNODO.value="";
		document.form2.CUSTOMERUENNODO.value="";
        
        document.form2.TOTALAFTERDISCOUNTDO.value="";
        document.form2.PreparedByDO.value="";
        document.form2.SellerDO.value="";
        document.form2.SellerSignatureDO.value="";
        document.form2.BuyerDO.value="";
        document.form2.BuyerSignatureDO.value="";
		document.form2.printRoundoffTotalwithDecimalDO.checked = false;
		document.form2.printwithProductDO.checked = false;
		document.form2.printDiscountReportDO.checked = false;
		document.form2.printincotermDO.checked = false;
        document.form2.DiscountDO.value="";
        document.form2.NetRateDO.value="";
       
        document.form2.printWithDeliveryDateDO.checked = false;
        document.form2.prdDeliveryDateDO.value="";
        document.form2.printWithPrdDeliveryDateDO.checked = false;
        document.form2.calculateTaxwithShippingCostDO.checked = false;
        
        document.form2.GINODO.value="";
        document.form2.GINODATEDO.value="";
        
        document.form2.AdjustmentDO.value="";
        document.form2.ProductRatesAreDO.value="";

        document.form2.PaymentMadeDO.value="";
        document.form2.BalanceDueDO.value="";
        document.form2.printPaymentMadeDO.checked = false;
        document.form2.printBalanceDueDO.checked = false;
        document.form2.printShippingCostDO.checked = false;
        document.form2.printOrderDiscountDO.checked = false;
        document.form2.printAdjustmentDO.checked = false;

//      2. RESVI STARTS
        document.form2.ProjectDO.value="";
        document.form2.printWithprojectDO.checked = false;
        document.form2.printWithUENNODO.checked = false;
        document.form2.printWithCustomerUENNODO.checked = false;
        document.form2.printwithcompanysealDO.checked = false;
        document.form2.DisplaySignature.checked = false;
        document.form1.TransportModeDO.value="";
        document.form1.printwithtransportmodeDO.checked = false;
        document.form1.printwithshipingaddDO.checked = false;
        
//         RESVI ENDS
        
	}
	
	function onAdd(){
   	   		
   		document.form1.action  =  "/track/InvoiceServlet?Submit=EDIT_DO_RCPT_INVOICE_HDR";
   		document.form1.submit();
   	
	}
	function onAddDO(){
	   		
   		document.form2.action  =  "/track/InvoiceServlet?Submit=EDIT_DO_RCPT_INVOICE_HDRDO";
   		document.form2.submit();
   	
	}
	function headerReadable(){
		if(document.form1.DisplayByOrdertype.checked)
		{
		document.form1.OutboundOrderHeader.readOnly = true;		
		document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="form-control inactiveGry";
		}
		else{
			document.form1.OutboundOrderHeader.readOnly = false;
			document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="form-control";
		}
	}
	
	function headerReadableDO(){
		if(document.form2.DisplayByOrdertypeDO.checked)
		{
		document.form2.OutboundOrderHeaderDO.readOnly = true;		
		document.getElementById('OutboundOrderHeaderDO').getAttributeNode('class').value="form-control inactiveGry";
		}
		else{
			document.form2.OutboundOrderHeaderDO.readOnly = false;
			document.getElementById('OutboundOrderHeaderDO').getAttributeNode('class').value="form-control";
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
	String InboundOrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",Date="",OrderNo ="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="",UOM="",DisplayByOrderType = "",printDetailDesc="",printwithproject="",printWithUENNO="",printWithCustomerUENNO="",printCustTerms="",printCustRemarks="";
	String Description = "", OrderQty  = "", Rate = "",TaxAmount="",Amt="",Container="",totalafterdiscount="",
    DisplayContainer="",SubTotal="",Tax="",Total="",Roundoff="",
    Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",customerrcbno="",UenNo="",CusUenNO="",
	remark1="",remark2="",shipto="",project="",deliverydate="",Employee="",CompanyDate="",CompanyName="",brand="",hscode="",coo="",printwithhscode="",printwithcoo="",printbuyer="",printbuyersign="",
	CompanyStamp="",CompanySig="",printEmployee="",orderdiscount="",shippingcost="",incoterm="",printincoterm="",rcbno="",printwithproductremarks="",printwithbrand="",PreparedBy="",Seller="",SellerSignature="",Buyer="",BuyerSignature="",printRoundoffTotalwithDecimal="",printwithProduct="",
	printDiscountReport="",Discount="",NetRate="",Adjustment="",PaymentMade="",BalanceDue="",printPaymentMade="",printBalanceDue="",printShippingCost="",printOrderDiscount="",printAdjustment="",ProductRatesAre="",replacePreviousSalesCost="",
	printWithDeliveryDate="",prdDeliveryDate="",printWithPrdDeliveryDate="",TransportMode="",printwithtransportmode="",printwithshipingadd="",showPreviousPurchaseCost="",printwithcompanysig="",printwithcompanyseal="",showPreviousSalesCost="",
	ShowSalesPriceByAverageCost="",ShowSalesPricebyLastOrderSellingPrice="",ShowSalesPricebyListedPrice="",calculateTaxwithShippingCost="";
			
	String InboundOrderHeaderDO  = "", InvoiceOrderToHeaderDO  = "", FromHeaderDO  = "",DateDO="",OrderNoDO ="";
	String RefNoDO   = "", TermsDO  = "", TermsDetailsDO   = "",SoNoDO ="",TransportModeDO="",ItemDO ="",UOMDO="",DisplayByOrderTypeDO = "",printDetailDescDO="",printCustTermsDO="",printCustRemarksDO="";
	String DescriptionDO = "", OrderQtyDO  = "", RateDO = "",TaxAmountDO="",AmtDO="",ContainerDO="", 
	DisplayContainerDO="",SubTotalDO="",TaxDO="",TotalDO="",RoundoffDO="",brandDO="",projectDO="", hscodeDO="",cooDO="",printwithbrandDO="",printwithprojectDO="",printWithUENNODO="",printWithCustomerUENNODO="",customerrcbnoDO="",UennoDO="",CusUennoDO="",invoicenoDO="",totalafterdiscountDO="",invoicedateDO="",
	Footer1DO="",Footer2DO="",Footer3DO="",Footer4DO="",Footer5DO="",Footer6DO="",Footer7DO="",Footer8DO="",Footer9DO="",printwithhscodeDO="",printwithcooDO="",
    remark1DO="",remark2DO="",shiptoDO="",deliverydateDO="",EmployeeDO="",CompanyDateDO="",CompanyNameDO="",CompanyStampDO="",CompanySigDO="",
	printEmployeeDO="",DisplaySignatureDO="",Orientation="",OrientationDO="",orderdiscountDO="",shippingcostDO="",incotermDO="",printincotermDO="",rcbnoDO="",printwithproductremarksDO="",
	printpackinglistDO="",printdeliverynoteDO="",PreparedByDO="",SellerDO="",SellerSignatureDO="",BuyerDO="",BuyerSignatureDO="",printRoundoffTotalwithDecimalDO="",printwithProductDO="",
	printDiscountReportDO="",DiscountDO="",NetRateDO="",GINODO="",GINODATEDO="",AdjustmentDO="",PaymentMadeDO="",BalanceDueDO="",printPaymentMadeDO="",printBalanceDueDO="",printShippingCostDO="",printOrderDiscountDO="",printAdjustmentDO="",ProductRatesAreDO="",
	printWithDeliveryDateDO="",prdDeliveryDateDO="",printWithPrdDeliveryDateDO="",printwithcompanysealDO="",printwithtransportmodeDO="",printwithshipingaddDO="",calculateTaxwithShippingCostDO="";
	
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	
	int FOOTER_SIZE=0;
	int FOOTER_SIZEDO=0;
	StrUtils strUtils = new StrUtils();
        res =  StrUtils.fString(request.getParameter("result"));
    	DOUtil doUtil = new DOUtil();
    	Map m= doUtil.getDOReceiptInvoiceHdrDetails(plant,"Outbound Order");
        
         if(!m.isEmpty()){
         InboundOrderHeader= (String) m.get("HDR1");
         InvoiceOrderToHeader = (String) m.get("HDR2");
         FromHeader = (String) m.get("HDR3");
         Date = (String) m.get("DATE");
         OrderNo = (String) m.get("ORDERNO");
         
         RefNo = (String) m.get("REFNO");
         Terms = (String) m.get("TERMS");
         TermsDetails = (String) m.get("TERMSDETAILS");
         SoNo = (String) m.get("SONO");
         
         Item = (String) m.get("ITEM");
         Description = (String) m.get("DESCRIPTION");
         OrderQty = (String) m.get("ORDERQTY");
         UOM = (String) m.get("UOM");
         Rate = (String) m.get("RATE");
         TaxAmount = (String) m.get("TAXAMOUNT");
         Amt = (String) m.get("AMT");
         
         Footer1 = (String) m.get("F1");
         Footer2 = (String) m.get("F2");
         Footer3 = (String) m.get("F3");
         Footer4 = (String) m.get("F4");
         Footer5 = (String) m.get("F5");
         Footer6 = (String) m.get("F6");
         Footer7 = (String) m.get("F7");
         Footer8 = (String) m.get("F8");
         Footer9 = (String) m.get("F9");
         DisplayByOrderType = (String)m.get("DISPLAYBYORDERTYPE");
         printDetailDesc = (String)m.get("PRINTXTRADETAILS");
         printCustTerms = (String)m.get("PRINTCUSTERMS");
         printCustRemarks = (String)m.get("PCUSREMARKS");
         Container = (String)m.get("Container");
         DisplayContainer = (String)m.get("DisplayContainer");
         remark1=(String)m.get("REMARK1");
         remark2=(String)m.get("REMARK2");
         deliverydate=(String)m.get("DELIVERYDATE");
         Employee=(String)m.get("EMPLOYEE");
         shipto=(String)m.get("SHIPTO");
         CompanyDate=(String)m.get("COMPANYDATE");
         CompanyName=(String)m.get("COMPANYNAME");
         CompanyStamp=(String)m.get("COMPANYSTAMP");
         CompanySig=(String)m.get("COMPANYSIG");
         printEmployee=(String)m.get("PRINTEMPLOYEE");
         Orientation=(String)m.get("PrintOrientation");
         orderdiscount=(String)m.get("ORDERDISCOUNT");
         SubTotal=(String)m.get("SUBTOTAL");
         Total=(String)m.get("TOTAL");
         Roundoff=(String)m.get("ROUNDOFFTOTALWITHDECIMAL");
         shippingcost=(String)m.get("SHIPPINGCOST");
         incoterm=(String)m.get("INCOTERM");
         printincoterm=(String)m.get("PRINTINCOTERM");
         printwithproductremarks = (String)m.get("PRINTWITHPRODUCTREMARKS");
         printwithhscode = (String)m.get("PRITNWITHHSCODE");
         printwithcoo = (String)m.get("PRITNWITHCOO");
         printbuyer = (String)m.get("PRINTBUYER");
         printbuyersign = (String)m.get("PRINTBUYERSIGN");
         printpackinglistDO = (String)m.get("PRINTPACKINGLIST");
         printdeliverynoteDO = (String)m.get("PRINTDELIVERYNOTE");
         printwithbrand = (String)m.get("PRINTWITHBRAND");
         rcbno=(String)m.get("RCBNO");
         brand=(String)m.get("BRAND");
         hscode=(String)m.get("HSCODE");
         coo=(String)m.get("COO");
         customerrcbno=(String)m.get("CUSTOMERRCBNO");
         
         UenNo=(String)m.get("UENNO");
         CusUenNO=(String)m.get("CUSTOMERUENNO");
         
         //invoiceno=(String)m.get("INVOICENO");
         totalafterdiscount=(String)m.get("TOTALAFTERDISCOUNT");
         PreparedBy = (String) m.get("PREPAREDBY");
         Seller = (String) m.get("SELLER");
         SellerSignature = (String) m.get("SELLERSIGNATURE");
         Buyer = (String) m.get("BUYER");
         BuyerSignature = (String) m.get("BUYERSIGNATURE");
		 printRoundoffTotalwithDecimal = (String)m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
		 printwithProduct = (String)m.get("PRINTWITHPRODUCT");
		 printDiscountReport=(String)m.get("PRINTWITHDISCOUNT");
	     Discount = (String)m.get("DISCOUNT");
	     NetRate = (String)m.get("NETRATE");
	     
	     printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
         prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
         printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
         showPreviousPurchaseCost = (String) m.get("SHOWPREVIOUSPURCHASECOST");
         showPreviousSalesCost = (String) m.get("SHOWPREVIOUSSALESCOST");
         
         
         
         calculateTaxwithShippingCost=(String) m.get("CALCULATETAXWITHSHIPPINGCOST");
         
         Adjustment=(String) m.get("ADJUSTMENT");
	     ProductRatesAre=(String) m.get("PRODUCTRATESARE");

	     PaymentMade=(String) m.get("PAYMENTMADE");
	     BalanceDue=(String) m.get("BALANCEDUE");
	     printPaymentMade=(String) m.get("PRINTPAYMENTMADE");
	     printBalanceDue=(String) m.get("PRINTBALANCEDUE");
	     printShippingCost=(String) m.get("PRINTSHIPPINGCOST");
	     printOrderDiscount=(String) m.get("PRINTORDERDISCOUNT");
	     printAdjustment=(String) m.get("PRINTADJUSTMENT");
	     
	     //  RESVI STARTS
         project=(String)m.get("PROJECT");
         printwithproject=(String)m.get("PRINTWITHPROJECT");
         printWithUENNO=(String)m.get("PRINTWITHUENNO");
         printWithCustomerUENNO=(String)m.get("PRINTWITHCUSTOMERUENNO");
         printwithcompanysig=(String)m.get("PRINTWITHCOMPANYSIG");
	     printwithcompanyseal=(String)m.get("PRINTWITHCOMPANYSEAL");
	     TransportMode=(String)m.get("TRANSPORT_MODE");
	     printwithtransportmode=(String)m.get("PRINTWITHTRANSPORT_MODE");
	     printwithshipingadd=(String)m.get("PRINTWITHSHIPINGADD");
     //  RESVI ENDS
     
     replacePreviousSalesCost = (String) m.get("REPLACEPREVIOUSSALESCOST");
         
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
         
         Map mDO= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Outbound Order");
         if(!mDO.isEmpty()){
	         InboundOrderHeaderDO= (String) mDO.get("HDR1");
	         InvoiceOrderToHeaderDO = (String) mDO.get("HDR2");
	         FromHeaderDO = (String) mDO.get("HDR3");
	         DateDO = (String) mDO.get("DATE");
	         OrderNoDO = (String) mDO.get("ORDERNO");
	         RefNoDO = (String) mDO.get("REFNO");
	         TermsDO = (String) mDO.get("TERMS");
	         TermsDetailsDO = (String) mDO.get("TERMSDETAILS");
	         SoNoDO = (String) mDO.get("SONO");
	         ItemDO = (String) mDO.get("ITEM");
	         DescriptionDO = (String) mDO.get("DESCRIPTION");
	         OrderQtyDO = (String) mDO.get("ORDERQTY");
	         UOMDO = (String) mDO.get("UOM");
	         RateDO = (String) mDO.get("RATE");
	         TaxAmountDO = (String) mDO.get("TAXAMOUNT");
	         AmtDO = (String) mDO.get("AMT");
	         Footer1DO = (String) mDO.get("F1");
	         Footer2DO = (String) mDO.get("F2");
	         Footer3DO = (String) mDO.get("F3");
	         Footer4DO = (String) mDO.get("F4");
	         Footer5DO = (String) mDO.get("F5");
             Footer6DO = (String) mDO.get("F6");
             Footer7DO = (String) mDO.get("F7");
             Footer8DO = (String) mDO.get("F8");
             Footer9DO = (String) mDO.get("F9");
	         DisplayByOrderTypeDO = (String)mDO.get("DISPLAYBYORDERTYPE");
	         printDetailDescDO = (String)mDO.get("PRINTXTRADETAILS");
	         printCustTermsDO = (String)mDO.get("PRINTCUSTERMS");
	         printCustRemarksDO = (String)mDO.get("PCUSREMARKS");
	         ContainerDO = (String)mDO.get("Container");
	         DisplayContainerDO = (String)mDO.get("DisplayContainer");
	         remark1DO=(String)mDO.get("REMARK1");
	         remark2DO=(String)mDO.get("REMARK2");
	         deliverydateDO=(String)mDO.get("DELIVERYDATE");
	         EmployeeDO=(String)mDO.get("EMPLOYEE");
	         shiptoDO=(String)mDO.get("SHIPTO");
	         CompanyDateDO=(String)mDO.get("COMPANYDATE");
	         CompanyNameDO=(String)mDO.get("COMPANYNAME");
	         CompanyStampDO=(String)mDO.get("COMPANYSTAMP");
	         CompanySigDO=(String)mDO.get("COMPANYSIG");
	         printEmployeeDO=(String)mDO.get("PRINTEMPLOYEE");
	         DisplaySignatureDO = (String)mDO.get("DISPLAYSIGNATURE");
	         OrientationDO = (String)mDO.get("PrintOrientation");
	         orderdiscountDO=(String)mDO.get("ORDERDISCOUNT");
	         SubTotalDO=(String)mDO.get("SUBTOTAL");
	         TotalDO=(String)mDO.get("TOTAL");
	         RoundoffDO=(String)mDO.get("ROUNDOFFTOTALWITHDECIMAL");
             shippingcostDO=(String)mDO.get("SHIPPINGCOST");
             incotermDO=(String)mDO.get("INCOTERM");
             printincotermDO=(String)mDO.get("PRINTINCOTERM");
             printwithbrandDO = (String)mDO.get("PRINTWITHBRAND");
             printwithproductremarksDO = (String)mDO.get("PRINTWITHPRODUCTREMARKS");
             printwithhscodeDO = (String)mDO.get("PRITNWITHHSCODE");
             printwithcooDO = (String)mDO.get("PRITNWITHCOO");
             printpackinglistDO = (String)mDO.get("PRINTPACKINGLIST");
             printdeliverynoteDO = (String)mDO.get("PRINTDELIVERYNOTE");
             rcbnoDO=(String)mDO.get("RCBNO");
             brandDO=(String)mDO.get("BRAND");
             hscodeDO=(String)mDO.get("HSCODE");
	         cooDO=(String)mDO.get("COO");
	         customerrcbnoDO=(String)mDO.get("CUSTOMERRCBNO");
	         
	         UennoDO=(String)mDO.get("UENNO");
             CusUennoDO=(String)mDO.get("CUSTOMERUENNO");
             
	         invoicenoDO=(String)mDO.get("INVOICENO");
	         invoicedateDO=(String)mDO.get("INVOICEDATE");
	         totalafterdiscountDO=(String)mDO.get("TOTALAFTERDISCOUNT");
	         PreparedByDO = (String) mDO.get("PREPAREDBY");
	         SellerDO = (String) mDO.get("SELLER");
	         SellerSignatureDO = (String) mDO.get("SELLERSIGNATURE");
	         BuyerDO = (String) mDO.get("BUYER");
	         BuyerSignatureDO = (String) mDO.get("BUYERSIGNATURE");
			 printRoundoffTotalwithDecimalDO = (String)mDO.get("PRINTROUNDOFFTOTALWITHDECIMAL");
			 printwithProductDO = (String)mDO.get("PRINTWITHPRODUCT");
			 printDiscountReportDO=(String)mDO.get("PRINTWITHDISCOUNT");
		     DiscountDO = (String)mDO.get("DISCOUNT");
		     NetRateDO = (String)mDO.get("NETRATE");
		     
		     printWithDeliveryDateDO = (String) mDO.get("PRINTWITHDELIVERYDATE");
	         prdDeliveryDateDO = (String) mDO.get("PRODUCTDELIVERYDATE");
	         printWithPrdDeliveryDateDO = (String) mDO.get("PRINTWITHPRODUCTDELIVERYDATE");
	         calculateTaxwithShippingCostDO=(String) mDO.get("CALCULATETAXWITHSHIPPINGCOST");
	         
	         GINODO=(String)mDO.get("GINO");
	         GINODATEDO=(String)mDO.get("GINODATE");
	         
	         AdjustmentDO=(String) mDO.get("ADJUSTMENT");
		     ProductRatesAreDO=(String) mDO.get("PRODUCTRATESARE");
		     
		     PaymentMadeDO=(String) mDO.get("PAYMENTMADE");
		     BalanceDueDO=(String) mDO.get("BALANCEDUE");
		     printPaymentMadeDO=(String) mDO.get("PRINTPAYMENTMADE");
		     printBalanceDueDO=(String) mDO.get("PRINTBALANCEDUE");
		     printShippingCostDO=(String) mDO.get("PRINTSHIPPINGCOST");
		     printOrderDiscountDO=(String) mDO.get("PRINTORDERDISCOUNT");
		     printAdjustmentDO=(String) mDO.get("PRINTADJUSTMENT");
		     
		     
		     //  RESVI STARTS
		         projectDO=(String)mDO.get("PROJECT");
		         printwithprojectDO=(String)mDO.get("PRINTWITHPROJECT");
		         printWithUENNODO=(String)mDO.get("PRINTWITHUENNO");
		         printWithCustomerUENNODO=(String)mDO.get("PRINTWITHCUSTOMERUENNO");
		         printwithcompanysealDO=(String)mDO.get("PRINTWITHCOMPANYSEAL");
		         TransportModeDO=(String)mDO.get("TRANSPORT_MODE");
		         printwithtransportmodeDO=(String)mDO.get("PRINTWITHTRANSPORT_MODE");
		         printwithshipingaddDO=(String)mDO.get("PRINTWITHSHIPINGADD");
		         
		     //  RESVI ENDS
	         
	         if(!Footer1DO.isEmpty())
	           	 FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer2DO.isEmpty())
	           	 FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer3DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer4DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer5DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer6DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer7DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer8DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
	            if(!Footer9DO.isEmpty())
	            	FOOTER_SIZEDO=FOOTER_SIZEDO+1;
         }
       

	try {
		action = StrUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		InboundOrderHeader  = "";
		InvoiceOrderToHeader  = "";
		FromHeader  = "";
		Date= "";
		OrderNo  = "";
		RefNo   = "";
		Terms   = "";
		TermsDetails ="";
		SoNo=""; 
		Item   = "";
		Description ="";
		OrderQty =""; 
        UOM="";
        Container="";
		Rate =""; 
		TaxAmount =""; 
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
        printDetailDesc="";
        printCustTerms="";
        printCustRemarks="";
        remark1="";
		remark2="";
        deliverydate="";	
        Employee="";
        shipto="";
        CompanyDate="";
        CompanyName="";
        CompanyStamp="";	
        CompanySig="";
        printEmployee="";
        orderdiscount="";
        SubTotal="";
        Total="";
        Roundoff="";
        shippingcost="";
        incoterm="";
        printincoterm="";
        brand="";
        hscode="";
		coo="";
		rcbno="";
		printwithproductremarks="";
		printwithbrand="";
		printbuyersign="";
		printbuyer="";
		//invoiceno="";
		customerrcbno="";
		
	    UenNo="";
		CusUenNO="";
		
        totalafterdiscount="";
        PreparedBy="";
        Seller="";
	    SellerSignature="";
	    Buyer="";
	    BuyerSignature="";
		printRoundoffTotalwithDecimal="";
		printwithProduct="";
		printDiscountReport="";
		Discount="";
		NetRate="";
		printWithDeliveryDate="";
		prdDeliveryDate="";
		printWithPrdDeliveryDate="";
		calculateTaxwithShippingCost="";
		
		Adjustment="";
		ProductRatesAre="";

		PaymentMade="";
		BalanceDue="";
		printPaymentMade="";
		printBalanceDue="";
		printShippingCost="";
		printOrderDiscount="";
		printAdjustment="";
		
//      RESVI STARTS
		project="";
		printwithproject="";
		printWithUENNO="";
		printWithCustomerUENNO="";
		printwithcompanysig="";
		printwithcompanyseal="";
		TransportMode="";
		printwithtransportmode="";
		printwithshipingadd="";
//      RESVI ENDS

        
        InboundOrderHeaderDO  = "";
		InvoiceOrderToHeaderDO  = "";
		FromHeaderDO  = "";
		DateDO= "";
		OrderNoDO  = "";
		RefNoDO   = "";
		TermsDO   = "";
		TermsDetailsDO ="";
		SoNoDO=""; 
		Item   = "";
		DescriptionDO ="";
		OrderQtyDO =""; 
        UOMDO="";
        ContainerDO="";
		RateDO =""; 
		TaxAmountDO =""; 
		AmtDO =""; 
		Footer1DO="";
		Footer2DO="";
        Footer3DO="";
		Footer4DO="";
		Footer5DO="";
        Footer6DO="";
		Footer7DO="";
		Footer8DO="";
		Footer9DO="";
        printDetailDescDO="";
        printCustTermsDO="";
        printCustRemarksDO="";
        remark1DO="";
		remark2DO="";
        deliverydateDO="";	
        EmployeeDO="";
        shiptoDO="";
        CompanyDateDO="";
        CompanyNameDO="";
        CompanyStampDO="";	
        CompanySigDO="";
        printEmployeeDO="";
        orderdiscountDO="";
        shippingcostDO="";
        incotermDO="";
        printincotermDO="";
        brandDO="";
        hscodeDO="";
		cooDO="";
		rcbnoDO="";
		printwithproductremarksDO="";
        printwithbrandDO="";
        invoicenoDO="";
        invoicedateDO="";
        customerrcbnoDO="";
        
        UennoDO="";
     	CusUennoDO="";
        
	    totalafterdiscountDO="";
	    PreparedByDO="";
	    SellerDO="";
	    SellerSignatureDO="";
	    BuyerDO="";
	    BuyerSignatureDO="";
		printRoundoffTotalwithDecimalDO="";
		printwithProductDO="";
		printDiscountReportDO="";
		DiscountDO="";
		NetRateDO="";
		printWithDeliveryDateDO="";
		prdDeliveryDateDO="";
		printWithPrdDeliveryDateDO="";
		calculateTaxwithShippingCostDO="";
		GINODO="";
		GINODATEDO="";
		
		AdjustmentDO="";
		ProductRatesAreDO="";
		
		PaymentMadeDO="";
		BalanceDueDO="";
		printPaymentMadeDO="";
		printBalanceDueDO="";
		printShippingCostDO="";
		printOrderDiscountDO="";
		printAdjustmentDO="";
		
//      RESVI STARTS
		projectDO="";
		printwithprojectDO="";
		printWithUENNODO="";
		printWithCustomerUENNODO="";
		printwithcompanysealDO="";
		TransportModeDO="";
		printwithtransportmodeDO="";
		printwithshipingaddDO="";
//      RESVI ENDS
	}  
%>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div>
	 
	 <div class="tab">
            <button class="tablinks active" onclick="openPayment(event, 'payment')">Upon Creation</button>
 		 <button class="tablinks" onclick="openPayment(event, 'voucher')">After Pick and Issue</button>
		</div>
		
	<div id="payment" class="tabcontent active" style="display: block;">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Sales Order Printout (With Price)</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
	  <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="container-fluid">
		<form id="creationForm" class="form-horizontal" name="form1"  method="post">
		<input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
       <input type="hidden" name="OrderType" value="Outbound Order">
		<div class="form-group">
      <label class="control-label col-sm-3"  for="Outbound order header">Sales Order Header</label>
      <div class="col-sm-4">          
        <INPUT id="OutboundOrderHeader" name="OutboundOrderHeader" type="TEXT" value="<%=InboundOrderHeader%>" <%if(DisplayByOrderType.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%> class = "form-control" <%}%>
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
		      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertype" name = "DisplayByOrdertype" value = "DisplayByOrdertype" 
			<%if(DisplayByOrderType.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/>Display By OrderType</label>
      </div>
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
		    <div class="form-group">
      <label class="control-label col-sm-3" for="Invoice order to header">Invoice To Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
     <div class="form-inline">
      </div>
	  </div>
		   <div class="form-group">
      <label class="control-label col-sm-3" for="from header">From Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarks" name = "printCustRemarks" value = "printCustRemarks" 
			<%if(printCustRemarks.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>
	  </div>
		    <div class="form-group">
      <label class="control-label col-sm-3" for="Ship To">Shipping Address</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShipTo" type="TEXT" value="<%=shipto%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
            <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithshipingadd" name = "printwithshipingadd" value = "printwithshipingadd" 
			<%if(printwithshipingadd.equals("1")) {%>checked <%}%> />Print with shipping address</label>
      </div>
      </div>
      </div>
      
       <!--       RESVI STARTS TRANSPORT MODE -->
      <div class="form-group">
     <label class="control-label col-sm-3" for="Transport Mode">Transport Mode</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TransportMode" type="TEXT" value="<%=TransportMode%>"
			placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-sm-3" for="Project">Project</label>
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
<!--       RESVI ENDS -->
      
         <div class="form-group">
      <label class="control-label col-sm-3" for="Order No">Order Number</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="OrderNo" type="TEXT" value="<%=OrderNo%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30> 
      </div>
  
      </div>
       	  <div class="form-group">
 	  <label class="control-label col-sm-3" for="Order header">Order Date</label>      
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
	   
      <INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
      </div>
      
        
       <!-- resvi UeN no 15.03.2021 -->
     <div class="form-group">
     <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-sm-3" for="UEN No">Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-sm-3" for="UEN No">TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UENNO" type="TEXT" value="<%=UenNo%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
   <%-- <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNO" name = "printWithUENNO" value = "printWithUENNO" 
			<%if(printWithUENNO.equals("1")) {%>checked <%}%> />Print With UEN</label>
      </div>
      </div> --%>
      <!-- N.Muruganantham UeN no 15.01.2022 -->
      <div class="form-inline">
 		<div class="col-sm-4" style="padding: 0px;">
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
				    	<label class="radio-inline">
				      	<input type="radio" name="printWithUENNO" type = "radio" value="2"  id = "Both" <%if (printWithUENNO.equalsIgnoreCase("2")) {%> checked <%}%> >Both
				     	</label>
     	</div>
		</div>
      </div>
      
      <div class="form-group">      
     <label class="control-label col-sm-3" for="RCB NO" id="TaxLabel"></label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>  
      </div>
      <div class="form-inline" style="display: none;">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "calculateTaxwithShippingCost" name = "calculateTaxwithShippingCost" value = "calculateTaxwithShippingCost" 
			<%if(calculateTaxwithShippingCost.equals("1")) {%>checked <%}%> />Calculate Tax with Shipping Cost</label>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-sm-3" for="Customer UEN No">Customer Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-sm-3" for="Customer UEN No">Customer TRN</label>
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
      
        <div class="form-group">
      <label class="control-label col-sm-3" for="Customer RCB NO" id="CustomerTaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERRCBNO" type="TEXT" value="<%=customerrcbno%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      </div>
    
     
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product Rates Are">Product Rates Are</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProductRatesAre" type="TEXT" value="<%=ProductRatesAre%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div>  
	   <div class="form-group">
    <label class="control-label col-sm-3" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
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
			<%if(printWithDeliveryDate.equals("1")) {%>checked <%}%> />Print With Delivery Date</label>
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
			<%if(printWithPrdDeliveryDate.equals("1")) {%>checked <%}%> />Print With Product Delivery Date</label>
      </div>
      </div>
      </div>
                <div class="form-group">
     <label class="control-label col-sm-3" for="Reference No">Reference No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
	  <div class="form-inline" style="display: none;">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printDiscountReport" name = "printDiscountReport" value = "printDiscountReport" 
			<%if(printDiscountReport.equals("1")) {%>checked <%}%> />Print with Discount and Unit Price</label>
      </div>
      </div>
      </div>
           <div class="form-group">
      <label class="control-label col-sm-3" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline" >
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printincoterm" name = "printincoterm" value = "printincoterm" 
			<%if(printincoterm.equals("1")) {%>checked <%}%> />Print with INCOTERM</label>
      </div>
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
			<%if(printEmployee.equals("1")) {%>checked <%}%> />Print With Employee </label>
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
      <label class="control-label col-sm-3" for="SoNo">SoNo</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-sm-3" for="Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-sm-3" for="Order Quantity">Order Quantity</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderQty" type="TEXT" value="<%=OrderQty%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithproductremarks" name = "printwithproductremarks" value = "pintwithproductremarks" 
			<%if(printwithproductremarks.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
      </div>
          <div class="form-group">
     <label class="control-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="Container">Container</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
	  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "DisplayContainer" name = "DisplayContainer" value = "DisplayContainer" 
		 <%if(DisplayContainer.equals("1")) {%>checked <%}%> />Display Container </label>
      </div>
      </div>
      </div>
              <div class="form-group">
      <label class="control-label col-sm-3" for="brand">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRAND" type="TEXT" value="<%=brand%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "pintwithbrand" 
			<%if(printwithbrand.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
      </div>     
    <div class="form-group">
      <label class="control-label col-sm-3" for="HSCODE">HSCODE(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=hscode%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-sm-3" for="COO">COO(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COO" type="TEXT" value="<%=coo%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcoo" name = "printwithcoo" value = "printwithcoo" 
			<%if(printwithcoo.equals("1")) {%>checked <%}%> />Sales Print With COO</label>
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
      <label class="control-label col-sm-3" for="Sub Total">Sub Total</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SubTotal" type="TEXT" value="<%=SubTotal%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
           
     <div class="form-group">
      <label class="control-label col-sm-3" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderDiscount" type="TEXT" value="<%=orderdiscount%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      		<div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
			<input type = "checkbox" id = "printOrderDiscount" name = "printOrderDiscount" value = "printOrderDiscount" 
			<%if(printOrderDiscount.equals("1")) {%>checked <%}%> />Print With Order Discount</label>
		</div>
		</div> 
      </div>
           <div class="form-group">
      <label class="control-label col-sm-3" for="Shipping Cost">Shipping Cost</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShippingCost" type="TEXT" value="<%=shippingcost%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
			<input type = "checkbox" id = "printShippingCost" name = "printShippingCost" value = "printShippingCost" 
			<%if(printShippingCost.equals("1")) {%>checked <%}%> />Print With Shipping Cost</label>
		</div>
		</div>
<%--       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "showPreviousSalesCost" name = "showPreviousSalesCost" value = "showPreviousSalesCost" 
			<%if(showPreviousSalesCost.equals("1")) {%>checked <%}%> />Show Previous Order Sales Price </label>
      </div>
      </div> --%> 
      </div>
            <div class="form-group" style="display: none;">
    <label class="control-label col-sm-3" for="Total After Discount">Total After Discount</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="TOTALAFTERDISCOUNT" type="TEXT" value="<%=totalafterdiscount%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Adjustment">Adjustment</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Adjustment" type="TEXT" value="<%=Adjustment%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
      	<div class="form-inline">
	<div class="col-sm-4">
		<label class="checkbox-inline">      
		<input type = "checkbox" id = "printAdjustment" name = "printAdjustment" value = "printAdjustment" 
		<%if(printAdjustment.equals("1")) {%>checked <%}%> />Print With Adjustment</label>
	</div>
	</div>
      <div class="form-inline" style="display: none;">
      <div class="col-sm-5" style="padding: 0px;">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "replacePreviousSalesCost" name = "replacePreviousSalesCost" value = "replacePreviousSalesCost" 
			<%if(replacePreviousSalesCost.equals("1")) {%>checked <%}%> />Replace Previous Order Sales Price </label>
      </div>
      </div>
    </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Total">Total</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
    </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Roundoff">Roundoff</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="Roundoff" type="TEXT" value="<%=Roundoff%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
       <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimal" name = "printRoundoffTotalwithDecimal" value = "printRoundoffTotalwithDecimal" 
			<%if(printRoundoffTotalwithDecimal.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
    </div>
    
      <div class="form-group">
      <label class="control-label col-sm-3" for="PaymentMades">Payment Made</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="PaymentMade" type="TEXT" value="<%=PaymentMade%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  	  </div>
  	<%if(COMP_INDUSTRY.equalsIgnoreCase("Textile and Garment")){%>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
				<input type = "checkbox" id = "printPaymentMade" name = "printPaymentMade" value = "printPaymentMade" 
				<%if(printPaymentMade.equals("1")) {%>checked <%}%> />Print With Payment Made</label>
		</div>
	  </div> 
	  <%}%>
      </div>
   
      <div class="form-group">
      <label class="control-label col-sm-3" for="Qty Total">Balance Due</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BalanceDue" type="TEXT" value="<%=BalanceDue%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
      <%if(COMP_INDUSTRY.equalsIgnoreCase("Textile and Garment")){%>
  		    <div class="form-inline">
      		<div class="col-sm-4">
      			<label class="checkbox-inline">      
       			<input type = "checkbox" id = "printBalanceDue" name = "printBalanceDue" value = "printBalanceDue" 
				<%if(printBalanceDue.equals("1")) {%>checked <%}%> />Print With Balance Due</label>
      		</div>
      		</div>
      		<%}%>
       </div>   
       
         <div class="form-group">
      <div class="col-sm-4" style="margin-left:305px">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "showPreviousPurchaseCost" name = "showPreviousPurchaseCost" value = "showPreviousPurchaseCost" 
			<%if(showPreviousPurchaseCost.equals("1")) {%>checked <%}%> />Show Previous Order Purchase Cost </label>
      </div>
      </div> 
       
       <div class="form-group">
 		<div class="col-sm-4" style="margin-left:305px">
  		<label > 
  		<input type="radio"  name="showPreviousSalesCost" value="0" id="ByAverageCost" <%if (showPreviousSalesCost.equalsIgnoreCase("0")) {%> checked <%}%> onclick="isPreviousSalesCost();">Show Sales Price By Average Cost
		<br><input type="radio" name="showPreviousSalesCost" value="1" <%if (showPreviousSalesCost.equalsIgnoreCase("1")) {%> checked <%}%> onclick="isPreviousSalesCost();">Show Sales Price by Last Order Selling Price (By Customer)
		<%-- <br><input type="radio" name="showPreviousSalesCost" value="3" <%if (showPreviousSalesCost.equalsIgnoreCase("3")) {%> checked <%}%> onclick="isPreviousSalesCost();">Show Sales Price by Last Order Selling Price (By Customer) --%>
		<br><input type="radio" name="showPreviousSalesCost" value="2" <%if (showPreviousSalesCost.equalsIgnoreCase("2")) {%> checked <%}%> onclick="isPreviousSalesCost();">Show Sales Price by Listed Price
		<br><input type="radio" name="showPreviousSalesCost" value="3" <%if (showPreviousSalesCost.equalsIgnoreCase("3")) {%> checked <%}%> onclick="isPreviousSalesCost();">Show Sales Price by Purchase Cost & Increased Value 
		</label>
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
        <INPUT class="form-control" name="Seller" type="TEXT" value="<%=Seller%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      </div>
          <div class="form-group">
     <label class="control-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SellerSignature" type="TEXT" value="<%=SellerSignature%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      </div>
       <div class="form-group">
    <label class="control-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Buyer" type="TEXT" value="<%=Buyer%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printbuyer" name = "printbuyer" value = "printbuyer" 
			<%if(printbuyer.equals("1")) {%>checked <%}%> />Print With Buyer </label>
      </div>
      </div>
      </div>
        <div class="form-group">
    <label class="control-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BuyerSignature" type="TEXT" value="<%=BuyerSignature%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printbuyersign" name = "printbuyersign" value = "printbuyersign" 
			<%if(printbuyersign.equals("1")) {%>checked <%}%> />Print With BuyerSign </label>
      </div>
      </div>
      </div>
      <div class="form-group">
     <label class="control-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyDate" type="TEXT" value="<%=CompanyDate%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
         <div class="form-group">
    <label class="control-label col-sm-3" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyName" type="TEXT" value="<%=CompanyName%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      </div>
        <div class="form-group">
    <label class="control-label col-sm-3" for="Company Stamp">Company Stamp</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CompanyStamp" type="TEXT" value="<%=CompanyStamp%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
       <div class="form-inline" style="display: none;">
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
       <INPUT class="form-control" name="CompanySig" type="TEXT" value="<%=CompanySig%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcompanysig" name = "printwithcompanysig" value = "printwithcompanysig" 
			<%if(printwithcompanysig.equals("1")) {%>checked <%}%> />Print With Company Digital Signature</label>
      </div>
      </div>
      </div>
         
       </div>
       
                       <div class="tab-pane fade" id="footer">
        <br>
     
    
    <div class="form-group">
        <TABLE id="footertbl" style="border-spacing: 0px 8px;width: 100%;">
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
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	
      </div>
    </div>
		
		
		</form>
		</div>
		
		
		</div>

  

    
  <%-- <div class="form-group" >
  <label class="control-label col-sm-2" for="Orientation">Orientation:</label>
    <div class="col-sm-3">
    <label class="radio-inline">
     <input type = "radio" name="Orientation" value="Landscape" <%if(Orientation.equals("Landscape")) {%>checked <%}%>/>Landscape
    </label>
    <label class="radio-inline"> --%>
      
    <%-- </label>
     </div>
     <div class="form-inline">
     <label class="control-label col-sm-3" for="Orientation">Orientation:</label>
    <div class="col-sm-3">
    <label class="radio-inline">
     <input type = "radio" name="OrientationDO" value="Landscape" <%if(OrientationDO.equals("Landscape")) {%>checked <%}%>/>Landscape
    </label>
    <label class="radio-inline"> --%>
      
    <!-- </label>
     </div>
     </div>
</div> -->
<div id="voucher" class="tabcontent">
<ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Sales Order Printout (With Price)</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="container-fluid">	
	<form id="AfterPickAndIssueForm" class="form-horizontal" name="form2" method="post">
	<input type="hidden" name="OrderTypeDO" value="Outbound Order">
	<input type = "hidden" name="OrientationDO" value="Portrait" <%if(OrientationDO.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
	      <div class="form-group">
       <label class="control-label col-sm-3" for="Inbound order header">Sales Order Header</label>
      <div class="col-sm-4">          
       <INPUT  id="OutboundOrderHeaderDO" name="OutboundOrderHeaderDO" type="TEXT" value="<%=InboundOrderHeaderDO%>" <%if(DisplayByOrderTypeDO.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%> class = "form-control" <%}%>
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
		      </div>
		      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline"> 
      
        <input type = "checkbox" id = "DisplayByOrdertypeDO" name = "DisplayByOrdertypeDO" value = "DisplayByOrdertypeDO" 
			<%if(DisplayByOrderTypeDO.equals("1")) {%>checked <%}%> onClick = "headerReadableDO();"/>Display By OrderType</label>
      </div>
   </div>
   </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Invoice order to header">Invoice To Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="InvoiceOrderToHeaderDO" type="TEXT" value="<%=InvoiceOrderToHeaderDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
         <div class="form-inline">
      </div> 
     </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="from header">From Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="FromHeaderDO" type="TEXT" value="<%=FromHeaderDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
     
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarksDO" name = "printCustRemarksDO" value = "printCustRemarksDO" 
			<%if(printCustRemarksDO.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>
        <INPUT type="hidden" name="plant" value=<%=plant%>>
        <INPUT type="hidden" name="F1" id="F1DO" value="<%=Footer1DO%>">
        <INPUT type="hidden" name="F2" id="F2DO" value="<%=Footer2DO%>">
        <INPUT type="hidden" name="F3" id="F3DO" value="<%=Footer3DO%>">
        <INPUT type="hidden" name="F4" id="F4DO" value="<%=Footer4DO%>">
        <INPUT type="hidden" name="F5" id="F5DO" value="<%=Footer5DO%>">
        <INPUT type="hidden" name="F6" id="F6DO" value="<%=Footer6DO%>">
        <INPUT type="hidden" name="F7" id="F7DO" value="<%=Footer7DO%>">
        <INPUT type="hidden" name="F8" id="F8DO" value="<%=Footer8DO%>">
        <INPUT type="hidden" name="F9" id="F9DO" value="<%=Footer9DO%>">
     </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="Ship To">Shipping Address</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ShipToDO" type="TEXT" value="<%=shiptoDO%>" placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
            <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithshipingaddDO" name = "printwithshipingaddDO" value = "printwithshipingaddDO" 
			<%if(printwithshipingaddDO.equals("1")) {%>checked <%}%> />Print with shipping address</label>
      </div>
      </div>
      
    </div>
    
    <!--       RESVI STARTS TRANSPORT MODE DO-->
      <div class="form-group">
     <label class="control-label col-sm-3" for="Transport Mode">Transport Mode</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TransportModeDO" type="TEXT" value="<%=TransportModeDO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtransportmodeDO" name = "printwithtransportmodeDO" value = "printwithtransportmodeDO" 
			<%if(printwithtransportmodeDO.equals("1")) {%>checked <%}%> />Print With Transport Mode</label>
      </div>
      </div>
      </div>
<!--       RESVI ENDS -->


      <!--       RESVI STARTS PROJECT DO -->
      <div class="form-group">
      <label class="control-label col-sm-3" for="Project">Project</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProjectDO" type="TEXT" value="<%=projectDO%>"
			placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithprojectDO" name = "printwithprojectDO" value = "printwithprojectDO" 
			<%if(printwithprojectDO.equals("1")) {%>checked <%}%> />Print With Project</label>
      </div>
      </div>
      </div>
<!--       RESVI ENDS -->
       
        <div class="form-group">
      <label class="control-label col-sm-3" for="Order No">Order Number</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="OrderNoDO" type="TEXT" value="<%=OrderNoDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
            
    <div class="form-inline" style="display:none;">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printpackinglistDO" name = "printpackinglistDO" value = "printpackinglistDO" 
			<%if(printpackinglistDO.equals("1")) {%>checked <%}%> />Print With Packing List</label>
      </div>
      </div>
      </div>
	      <div class="form-group">
      <label class="control-label col-sm-3" for="Order header">Order Date</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="DateDO" type="TEXT" value="<%=DateDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      
      <div class="form-inline" style="display:none;">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynoteDO" name = "printdeliverynoteDO" value = "printdeliverynoteDO" 
			<%if(printdeliverynoteDO.equals("1")) {%>checked <%}%> />Print With Delivery Note</label>
      </div>
       <INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="GINO">GINO:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINODO" type="TEXT" value="<%=GINODO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
	     <div class="form-group">
      <label class="control-label col-sm-3" for="GINO DATE">GINO Date:</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINODATEDO" type="TEXT" value="<%=GINODATEDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>      
      <div class="form-group"  >
      <label class="control-label col-sm-3" for="invoice no">Invoice No:</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="INVOICENODO" type="TEXT" value="<%=invoicenoDO%>" size="15" MAXLENGTH=100>      
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="invoice date">Invoice Date</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="INVOICEDATEDO" type="TEXT" value="<%=invoicedateDO%>" size="15" MAXLENGTH=100>      
      </div>
      </div> 
      
       <!-- RESVI UeN no 15.03.2021 -->
     <div class="form-group">
     <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-sm-3" for="UEN No">Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-sm-3" for="UEN No">TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="UENNODO" type="TEXT" value="<%=UennoDO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    <%--   <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNODO" name = "printWithUENNODO" value = "printWithUENNODO" 
			<%if(printWithUENNODO.equals("1")) {%>checked <%}%> />Print With UEN </label>
      </div>
      </div> --%>
      
       <div class="form-inline">
 		<div class="col-sm-4" style="padding: 0px;">
  		<label class="radio-inline">
  						<%if(sCountry.equals("Singapore")) {%>
				      	<input type="radio" name="printWithUENNODO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNODO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With UEN
    					<%}else{ %>  
				      	<input type="radio" name="printWithUENNODO" type = "radio"  value="1"  id="NotNonStock" <%if (printWithUENNODO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With TRN
    					<%} %>
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="printWithUENNODO" type = "radio" value="0"  id = "NonStock" <%if (printWithUENNODO.equalsIgnoreCase("0")) {%> checked <%}%> >Print With <%=taxbylabel%> No
				     	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="printWithUENNODO" type = "radio" value="2"  id = "Both" <%if (printWithUENNODO.equalsIgnoreCase("2")) {%> checked <%}%> >Both
				     	</label>
     	</div>
		</div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-3" for="RCB NO" id="TaxLabelDO"></label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="RCBNODO" type="TEXT" value="<%=rcbnoDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>      
      </div>
      <div class="form-inline" style="display: none;">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "calculateTaxwithShippingCostDO" name = "calculateTaxwithShippingCostDO" value = "calculateTaxwithShippingCostDO" 
			<%if(calculateTaxwithShippingCostDO.equals("1")) {%>checked <%}%> />Calculate Tax with Shipping Cost</label>
      </div>
      </div>
	  
      </div>
    
     <div class="form-group">
     <%if(sCountry.equals("Singapore")) {%>
      <label class="control-label col-sm-3" for="Customer UEN No">Customer Unique Entity Number (UEN)</label>
    <%}else{ %>  
      <label class="control-label col-sm-3" for="Customer UEN No">Customer TRN</label>
    <%} %>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERUENNODO" type="TEXT" value="<%=CusUennoDO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
    <%if(sCountry.equals("Singapore")) {%>
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithCustomerUENNODO" name = "printWithCustomerUENNODO" value = "printWithCustomerUENNODO" 
			<%if(printWithCustomerUENNODO.equals("1")) {%>checked <%}%> />Print With Customer UEN </label>
    <%}else{ %>  
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithCustomerUENNODO" name = "printWithCustomerUENNODO" value = "printWithCustomerUENNODO" 
			<%if(printWithCustomerUENNODO.equals("1")) {%>checked <%}%> />Print With Customer TRN </label>
    <%} %>
      </div>
      </div>
      </div>
    
<!--     ENDS -->
         
             <div class="form-group">
       <label class="control-label col-sm-3" for="Customer RCB NO" id="CustomerTaxLabelDO"></label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="CUSTOMERRCBNODO" type="TEXT" value="<%=customerrcbnoDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>      
      </div>
      
	  <div class="form-inline" style="display: none;">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printDiscountReportDO" name = "printDiscountReportDO" value = "printDiscountReportDO" 
			<%if(printDiscountReportDO.equals("1")) {%>checked <%}%> />Print with Discount and Unit Price</label>
      </div>
      </div>
    </div>
    
   
    
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product Rates Are">Product Rates Are</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ProductRatesAreDO" type="TEXT" value="<%=ProductRatesAreDO%>"
			placeholder="Max 30 Characters" size="50" MAXLENGTH=30>
      </div>
    </div>
	  <div class="form-group">
      <label class="control-label col-sm-3" for="Prepared By">Prepared By</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="PreparedByDO" type="TEXT" value="<%=PreparedByDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
    </div>
    	  	<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#other2" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#productDetail2" class="nav-link" data-toggle="tab">Product Details</a>
        </li>
        <li class="nav-item">
            <a href="#cal2" class="nav-link" data-toggle="tab">Calculations</a>
        </li>
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
      <label class="control-label col-sm-3" for="Delivery Date">Delivery Date</label>      
      <div class="col-sm-4">      
      <INPUT class="form-control" name="DeliveryDateDO" type="TEXT" value="<%=deliverydateDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      
      <div class="form-inline">
	      <div class="col-sm-4">
	      <label class="checkbox-inline">      
	       <input type = "checkbox" id = "printWithDeliveryDateDO" name = "printWithDeliveryDateDO" value = "printWithDeliveryDateDO" 
				<%if(printWithDeliveryDateDO.equals("1")) {%>checked <%}%> />Print With Delivery Date</label>
	      </div>
      </div>
    </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="prdDeliveryDateDO">Product Delivery Date</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="prdDeliveryDateDO" type="TEXT" value="<%=prdDeliveryDateDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDateDO" name = "printWithPrdDeliveryDateDO" value = "printWithPrdDeliveryDateDO" 
			<%if(printWithPrdDeliveryDateDO.equals("1")) {%>checked <%}%> />Print With Product Delivery Date</label>
      </div>
      </div>
    </div>   
              <div class="form-group">
      <label class="control-label col-sm-3" for="Reference No">Reference No</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="RefNoDO" type="TEXT" value="<%=RefNoDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
            
      </div>
         <div class="form-group">
         <label class="control-label col-sm-3" for="INCOTERM">INCOTERM</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="INCOTERMDO" type="TEXT" value="<%=incotermDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>  	  
      </div>
            <div class="form-inline" >
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printincotermDO" name = "printincotermDO" value = "printincotermDO" 
			<%if(printincotermDO.equals("1")) {%>checked <%}%> />Print with INCOTERM</label>
      </div>
      </div>
      </div>      
    <div class="form-group">
      <label class="control-label col-sm-3" for="Employee">Employee</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="EmployeeDO" type="TEXT" value="<%=EmployeeDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployeeDO" name = "printEmployeeDO" value = "printEmployeeDO" 
			<%if(printEmployeeDO.equals("1")) {%>checked <%}%> />Print With Employee</label>
      </div>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="Payment Type">Payment Type</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="TermsDO" type="TEXT" value="<%=TermsDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      </div>
           <div class="form-group">
      <label class="control-label col-sm-3" for="Terms Details">Terms Details</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="TermsDetailsDO" type="TEXT" value="<%=TermsDetailsDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustTermsDO" name = "printCustTermsDO" value = "printCustTermsDO" 
			<%if(printCustTermsDO.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
    </div>
        </div>
         <div class="tab-pane fade" id="productDetail2">
        <br>
              <div class="form-group">
      <label class="control-label col-sm-3" for="SoNo">SoNo</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SoNoDO" type="TEXT" value="<%=SoNoDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
   

      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ItemDO" type="TEXT" value="<%=ItemDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
     
      
	  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithProductDO" name = "printwithProductDO" value = "printwithProductDO" 
			<%if(printwithProductDO.equals("1")) {%>checked <%}%> />Print With Product ID</label>
      </div>
      </div>
     </div>
    

      <div class="form-group">
      <label class="control-label col-sm-3" for="Description">Description</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="DescriptionDO" type="TEXT" value="<%=DescriptionDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printDetailDescDO" name = "printDetailDescDO" value = "printDetailDescDO" 
			<%if(printDetailDescDO.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
      </div>   
      <div class="form-group">
      <label class="control-label col-sm-3" for="Description">Order Quantity:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="OrderQtyDO" type="TEXT" value="<%=OrderQtyDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithproductremarksDO" name = "printwithproductremarksDO" value = "printwithproductremarksDO" 
			<%if(printwithproductremarksDO.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
      </div>
    
    

      <div class="form-group">
      <label class="control-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="UOMDO" type="TEXT" value="<%=UOMDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
   
    

      <div class="form-group">
      <label class="control-label col-sm-3" for="Container">Container</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="ContainerDO" type="TEXT" value="<%=ContainerDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
     
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplayContainerDO" name = "DisplayContainerDO" value = "DisplayContainerDO" 
		 <%if(DisplayContainerDO.equals("1")) {%>checked <%}%> />Display Container</label>
      </div>
      </div>
     </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="brand">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRANDDO" type="TEXT" value="<%=brandDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrandDO" name = "printwithbrandDO" value = "pintwithbrandDO" 
			<%if(printwithbrandDO.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
     
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="HSCODE">HSCODE(DN/PL)</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="HSCODEDO" type="TEXT" value="<%=hscodeDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>      
      </div>
      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscodeDO" name = "printwithhscodeDO" value = "printwithhscodeDO" 
			<%if(printwithhscodeDO.equals("1")) {%>checked <%}%> />Sales Print With HSCODE</label>
      </div>
      </div>
      </div>
           
      <div class="form-group">
      <label class="control-label col-sm-3" for="COO">COO(DN/PL)</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="COODO" type="TEXT" value="<%=cooDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>      
      </div>
           
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcooDO" name = "printwithcooDO" value = "printwithcooDO" 
			<%if(printwithcooDO.equals("1")) {%>checked <%}%> />Sales Print With COO</label>
      </div>
      </div>
      </div>
             <div class="form-group">
      <label class="control-label col-sm-3" for="Rate">Rate</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="RateDO" type="TEXT" value="<%=RateDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
   
    
  
      <div class="form-group">
    <label class="control-label col-sm-3" for="Discount">Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DiscountDO" type="TEXT" value="<%=DiscountDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
    
   

      <div class="form-group" style="display: none;">
    <label class="control-label col-sm-3" for="Net Amount">Net Amount</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="NetRateDO" type="TEXT" value="<%=NetRateDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div> 
      </div>
  
    
  
      <div class="form-group">
      <label class="control-label col-sm-3" for="Tax Amount">Tax Amount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="TaxAmountDO" type="TEXT" value="<%=TaxAmountDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
     <%--  <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printpackinglistDO" name = "printpackinglistDO" value = "printpackinglistDO" 
			<%if(printpackinglistDO.equals("1")) {%>checked <%}%> />Print Packing List</label>
      </div>
      </div> --%>
     
    

      <div class="form-group">
      <label class="control-label col-sm-3" for="Amount">Amount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="AmtDO" type="TEXT" value="<%=AmtDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>      
      <%-- <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printdeliverynoteDO" name = "printdeliverynoteDO" value = "printdeliverynoteDO" 
			<%if(printdeliverynoteDO.equals("1")) {%>checked <%}%> />Print Delivery Note</label>
      </div>
      </div> --%> 
        
        </div>
        <div class="tab-pane fade" id="cal2">
        <br>
         <div class="form-group">
      <label class="control-label col-sm-3" for="Sub Total">Sub Total</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="SubTotalDO" type="TEXT" value="<%=SubTotalDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      </div>
         <div class="form-group">
      <label class="control-label col-sm-3" for="Order Discount">Order Discount</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="OrderDiscountDO" type="TEXT" value="<%=orderdiscountDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
			<input type = "checkbox" id = "printOrderDiscountDO" name = "printOrderDiscountDO" value = "printOrderDiscountDO" 
			<%if(printOrderDiscountDO.equals("1")) {%>checked <%}%> />Print With Order Discount</label>
		</div>
		</div>
      </div>
      
           <div class="form-group" >
      <label class="control-label col-sm-3" for="Shipping Cost">Shipping Cost</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="ShippingCostDO" type="TEXT" value="<%=shippingcostDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>      
      </div>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
			<input type = "checkbox" id = "printShippingCostDO" name = "printShippingCostDO" value = "printShippingCostDO" 
			<%if(printShippingCostDO.equals("1")) {%>checked <%}%> />Print With Shipping Cost</label>
		</div>
		</div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Adjustment">Adjustment</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="AdjustmentDO" type="TEXT" value="<%=AdjustmentDO%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
	<div class="form-inline">
	<div class="col-sm-4">
		<label class="checkbox-inline">      
		<input type = "checkbox" id = "printAdjustmentDO" name = "printAdjustmentDO" value = "printAdjustmentDO" 
		<%if(printAdjustmentDO.equals("1")) {%>checked <%}%> />Print With Adjustment</label>
	</div>
	</div>
    </div>
        
      <div class="form-group">
      <label class="control-label col-sm-3" for="Total">Total</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="TotalDO" type="TEXT" value="<%=TotalDO%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
    </div>
        
      <div class="form-group">
      <label class="control-label col-sm-3" for="Roundoff">Roundoff</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="RoundoffDO" type="TEXT" value="<%=RoundoffDO%>"
			placeholder="Max 100 Characters" size="50" MAXLENGTH=100>
      </div>
       <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimalDO" name = "printRoundoffTotalwithDecimalDO" value = "printRoundoffTotalwithDecimalDO" 
			<%if(printRoundoffTotalwithDecimalDO.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
    </div>
    
          <div class="form-group">
      <label class="control-label col-sm-3" for="PaymentMades">Payment Made</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="PaymentMadeDO" type="TEXT" value="<%=PaymentMadeDO%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  	  </div>
  	  <%if(COMP_INDUSTRY.equalsIgnoreCase("Textile and Garment")){%>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
				<input type = "checkbox" id = "printPaymentMadeDO" name = "printPaymentMadeDO" value = "printPaymentMadeDO" 
				<%if(printPaymentMadeDO.equals("1")) {%>checked <%}%> />Print With Payment Made</label>
		</div>
	  </div> 
	  <%}%>
      </div>
   
      <div class="form-group">
      <label class="control-label col-sm-3" for="Qty Total">Balance Due</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BalanceDueDO" type="TEXT" value="<%=BalanceDueDO%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
      <%if(COMP_INDUSTRY.equalsIgnoreCase("Textile and Garment")){%>
  		    <div class="form-inline">
      		<div class="col-sm-4">
      			<label class="checkbox-inline">      
       			<input type = "checkbox" id = "printBalanceDueDO" name = "printBalanceDueDO" value = "printBalanceDueDO" 
				<%if(printBalanceDueDO.equals("1")) {%>checked <%}%> />Print With Balance Due</label>
      		</div>
      		</div>
      		<%}%>
       </div> 
        
      <div class="form-group" style="display: none;">
      <label class="control-label col-sm-3" for="Total After Discount">Total After Discount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="TOTALAFTERDISCOUNTDO" type="TEXT" value="<%=totalafterdiscountDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>  
        </div>
        <div class="tab-pane fade" id="remark2">
        <br>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks1">Remarks1</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="REMARK1DO" type="TEXT" value="<%=remark1DO%>"  placeholder="Max 100 Characters" size="15" MAXLENGTH=100>
      </div>  
    </div> 
    
 
      <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks2">Remarks2</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="REMARK2DO" type="TEXT" value="<%=remark2DO%>"  placeholder="Max 100 Characters" size="15" MAXLENGTH=100>      
      </div>
      </div>  
        
        </div>
         <div class="tab-pane fade" id="sign2">
        <br>
              <div class="form-group">
      <label class="control-label col-sm-3" for="Seller">Seller</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SellerDO" type="TEXT" value="<%=SellerDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>       
   
    
  
      <div class="form-group">
      <label class="control-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SellerSignatureDO" type="TEXT" value="<%=SellerSignatureDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
    
    
  
      <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BuyerDO" type="TEXT" value="<%=BuyerDO%>"	 placeholder="Max 580 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
    
    
   
      <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BuyerSignatureDO" type="TEXT" value="<%=BuyerSignatureDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
    
    
  
      <div class="form-group">
      <label class="control-label col-sm-3" for="Date">Date</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyDateDO" type="TEXT" value="<%=CompanyDateDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>  
   
    
     
      <div class="form-group">
      <label class="control-label col-sm-3" for="Company Name">Company Name</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyNameDO" type="TEXT" value="<%=CompanyNameDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>

    
 
      <div class="form-group">
          <label class="control-label col-sm-3" for="Company Stamp">Company Stamp</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyStampDO" type="TEXT" value="<%=CompanyStampDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithcompanysealDO" name = "printwithcompanysealDO" value = "printwithcompanysealDO" 
		 <%if(printwithcompanysealDO.equals("1")) {%>checked <%}%> />Print With Digital Stamp</label>
      </div>
      </div>
      </div>
      
    
    
      <div class="form-group">
     <label class="control-label col-sm-3" for="Signature">Signature</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanySigDO" type="TEXT" value="<%=CompanySigDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplaySignature" name = "DisplaySignature" value = "DisplaySignature" 
		 <%if(DisplaySignatureDO.equals("1")) {%>checked <%}%> />Print With Digital Signature</label>
      </div>
     </div>
        </div>
                    <div class="tab-pane fade" id="footer2">
        <br>
     
    
    <div class="form-group">
        <TABLE id="footertblDO" style="border-spacing: 0px 8px;width: 100%;">
		<TR>
		<TD style="width: 17%;">
		<label class="control-label col-form-label col-sm-2" for="Footer1">Footer1</label></TD>		
		<TD align="center"  style="width: 83%;"><div class="col-sm-8"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRowDO('footertblDO');return false;"></span>
		<INPUT class="form-control footerSearch" name="Footer1DO" id="Footer1DO"  placeholder="Max 200 Characters" type = "TEXT" value="<%=Footer1DO%>" size="100"  MAXLENGTH=200>		        
       	</div></div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="FOOTER_SIZEDO" value="<%=FOOTER_SIZEDO%>" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowDO('footertblDO','');return false;">+ Add another Footer</a>
						</div>
					</div>
				</div>
        </div>
           
        </div>
        </div>
	   <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
<!--        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAddDO();">Save</button>&nbsp;&nbsp;
      	
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
    //DO
    document.getElementById('TaxLabelDO').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabelDO').innerHTML = "Customer "+ d +" No";
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
    var FOOTER_SIZE2 = document.form2.FOOTER_SIZEDO.value;
    if(FOOTER_SIZE2!=0)
	{
	//alert(FOOTER_SIZE1);
	
	for ( var i = 0; i < FOOTER_SIZE2; i++) {
		if(i!=0)
			{
				var footerval= document.getElementById("F"+(i+1)+"DO").value; 
				addRowDO('footertblDO',footerval);
			}
	}
	}
    $(".footerSearch").typeahead('destroy');
	addSuggestionSearchDO();
	 isPreviousSalesCost();
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
function addRowDO(tableID,footer) {
	
  	
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
	var itemCellText =  "<div class=\"col-sm-8\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowDO('footertblDO');return false;\"></span><INPUT class=\"form-control footerSearch\" name=\"Footer"+newRowCount+"DO\" ";
	itemCellText = itemCellText+ " id=\"Footer"+newRowCount+"DO\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Max 200 Characters\" MAXLENGTH=\"200\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 9 footer ");
		}
	$(".footerSearch").typeahead('destroy');
	addSuggestionSearchDO();
}
function deleteRowDO(tableID) {
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
function addSuggestionSearchDO()
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
function isPreviousSalesCost(){
	if (document.getElementById("ByAverageCost").checked == true) {
		$('#showPreviousPurchaseCost').attr('disabled',true);
		document.form1.showPreviousPurchaseCost.checked = false;
	}  else {
		$('#showPreviousPurchaseCost').attr('disabled',false);
	}
}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>