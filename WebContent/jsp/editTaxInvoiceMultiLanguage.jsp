<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Invoice Printout";
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
		
		//Tax Invoice English
        document.form1.OutboundOrderHeader.value="";
        document.form1.CustomerOrderHeader.value="";
        document.form1.CollectionOrderHeader.value="";
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
		document.form1.Footer10.value="";
		document.form1.Footer11.value="";
		document.form1.Footer12.value="";
		document.form1.Footer13.value="";
    	document.form1.DisplayByOrdertype.checked = false;
        document.form1.printDetailDesc.checked = false;
        document.form1.DisplayContainer.checked = false;
        document.form1.printCustTerms.checked = false;
        document.form1.printCustRemarks.checked = false;
        document.form1.printBalanceDue.checked = false;
        document.form1.printPaymentMade.checked = false;
        document.form1.printAdjustment.checked = false;
        document.form1.printShippingCost.checked = false;
        document.form1.printOrderDiscount.checked = false;
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
        document.form1.ProductRatesAre.value="";
        document.form1.Orientation.value = "Landscape";
        document.form1.ShippingCost.value="";
		document.form1.OrderDiscount.value="";
		document.form1.INCOTERM.value="";
		document.form1.printwithproductremarks.checked = false;
		document.form1.printwithhscode.checked = false;
		document.form1.printwithcoo.checked = false;
		document.form1.printwithbrand.checked = false;
		document.form1.isAutoInvoice.checked = false;
		//document.form1.printpackinglist.checked = false;
		//document.form1.printdeliverynote.checked = false;
        
		document.form1.HSCODE.value="";
        document.form1.COO.value="";
        document.form1.RCBNO.value="";
        document.form1.INVOICENO.value="";
        document.form1.INVOICEDATE.value="";
        document.form1.CUSTOMERRCBNO.value="";

    	document.form1.UENNO.value="";
		document.form1.CUSTOMERUENNO.value="";
		
        document.form1.TOTALAFTERDISCOUNT.value="";
        document.form1.PreparedBy.value="";
        document.form1.Seller.value="";
        document.form1.SellerSignature.value="";
        document.form1.Buyer.value="";
        document.form1.BuyerSignature.value="";
        document.form1.printwithTaxInvoice.checked = false;
		
		document.form1.SubTotal.value="";
        document.form1.Tax.value="";
        document.form1.Total.value="";
        document.form1.ContactName.value="";
        document.form1.Email.value="";
        document.form1.Fax.value="";
        document.form1.Telephone.value="";
        document.form1.Handphone.value="";
        document.form1.Attention.value="";
        document.form1.QtyTotal.value="";
        document.form1.remark3.value="";
        document.form1.FooterPage.value="";
        document.form1.FooterOf.value="";
        document.form1.CashCustomer.value="";
		document.form1.RoundoffTotalwithDecimal.value="";
        document.form1.printRoundoffTotalwithDecimal.checked = false;
		document.form1.printwithProduct.checked = false;
		document.form1.printDiscountReport.checked = false;
        document.form1.Discount.value="";
        document.form1.NetRate.value="";
        
		document.form1.printWithDeliveryDate.checked = false;
		document.form1.printincoterm.checked = false;
        document.form1.prdDeliveryDate.value="";
        document.form1.printWithPrdDeliveryDate.checked = false;
        
        document.form1.GINO.value="";
		document.form1.GINODATE.value="";
		
		//Tax Invoice other language
        document.form1.OutboundOrderHeaderDO.value="";
		document.form1.InvoiceOrderToHeaderDO.value="";
		document.form1.FromHeaderDO.value="";
		document.form1.DateDO.value="";
		document.form1.OrderNoDO.value="";
     	document.form1.RefNoDO.value="";
		document.form1.TermsDO.value="";
		document.form1.TermsDetailsDO.value="";
		document.form1.SoNoDO.value="";
		document.form1.ItemDO.value="";
		document.form1.DescriptionDO.value="";
		document.form1.OrderQtyDO.value="";
		document.form1.UOMDO.value="";
        document.form1.ContainerDO.value="";
		document.form1.Footer1DO.value="";
		document.form1.Footer2DO.value="";
        document.form1.Footer3DO.value="";
		document.form1.Footer4DO.value="";
		document.form1.Footer5DO.value="";
		document.form1.Footer6DO.value="";
		document.form1.Footer7DO.value="";
		document.form1.Footer8DO.value="";
		document.form1.Footer9DO.value="";
    	document.form1.DisplayByOrdertypeDO.checked = false;
        document.form1.printDetailDescDO.checked = false;
        document.form1.DisplayContainerDO.checked = false;
        document.form1.printCustTermsDO.checked = false;
        document.form1.printCustRemarksDO.checked = false;
        document.form1.RateDO.value="";
        document.form1.TaxAmountDO.value="";
        document.form1.AmtDO.value="";
        document.form1.REMARK1DO.value="";
        document.form1.REMARK2DO.value="";
        document.form1.DeliveryDateDO.value="";
        document.form1.EmployeeDO.value="";
        document.form1.ShipToDO.value="";
        document.form1.CompanyDateDO.value="";
        document.form1.CompanyNameDO.value="";
        document.form1.CompanyStampDO.value="";
        document.form1.CompanySigDO.value="";
        document.form1.printEmployeeDO.checked = false;
        document.form1.OrientationDO.value = "Landscape";
        document.form1.ShippingCostDO.value="";
		document.form1.OrderDiscountDO.value="";
		document.form1.CDISCOUNTDO.value="";
		document.form1.CTAXDO.value="";
		document.form1.INCOTERMDO.value="";
		document.form1.printwithproductremarksDO.checked = false;
		document.form1.printwithhscodeDO.checked = false;
		document.form1.printwithcooDO.checked = false;
		document.form1.printwithbrandDO.checked = false;
		//document.form1.printpackinglistDO.checked = false;
		//document.form1.printdeliverynoteDO.checked = false;
          document.form1.TransportModeDO.value="";
	       document.form1.printwithtransportmodeDO.checked = false;
		document.form1.HSCODEDO.value="";
        document.form1.COODO.value="";
        document.form1.RCBNODO.value="";
        document.form1.INVOICENODO.value="";
        document.form1.INVOICEDATEDO.value="";
        document.form1.CUSTOMERRCBNODO.value="";
        document.form1.TOTALAFTERDISCOUNTDO.value="";
        document.form1.BRANDDO.value="";
        document.form1.PreparedByDO.value="";
        document.form1.SellerDO.value="";
        document.form1.SellerSignatureDO.value="";
        document.form1.BuyerDO.value="";
        document.form1.BuyerSignatureDO.value="";
        document.form1.printwithTaxInvoiceDO.checked = false;
        document.form1.Multilanguprint.checked = false;
		
		document.form1.SubTotalDO.value="";
        document.form1.TaxDO.value="";
        document.form1.TotalDO.value="";
        document.form1.ContactNameDO.value="";
        document.form1.EmailDO.value="";
        document.form1.FaxDO.value="";
        document.form1.TelephoneDO.value="";
        document.form1.HandphoneDO.value="";
        document.form1.AttentionDO.value="";
        document.form1.QtyTotalDO.value="";
        document.form1.remark3DO.value="";
        document.form1.FooterPageDO.value="";
        document.form1.FooterOfDO.value="";
        document.form1.CashCustomerDO.value="";
		document.form1.RoundoffTotalwithDecimalDO.value="";
        document.form1.printRoundoffTotalwithDecimalDO.checked = false;
		document.form1.printwithProductDO.checked = false;
		document.form1.printDiscountReportDO.checked = false;
        document.form1.DiscountDO.value="";
        document.form1.NetRateDO.value="";
        
        document.form1.printWithDeliveryDateDO.checked = false;
        document.form1.prdDeliveryDateDO.value="";
        document.form1.printWithPrdDeliveryDateDO.checked = false;

//      RESVI STARTS
        document.form1.Project.value="";
        document.form1.printWithproject.checked = false;
        document.form1.printWithUENNO.checked = false;
        document.form1.printcopy.checked = false;
        document.form1.printWithCustomerUENNO.checked = false;
        document.form1.printwithcompanysig.checked = false;
        document.form1.printwithcustnameadrress.checked = false;
	    document.form1.printwithcompanyseal.checked = false;
	    document.form1.TransportMode.value="";
        document.form1.printwithtransportmode.checked = false;
        document.form1.printwithshipingadd.checked = false;
//         RESVI ENDS
	}
	
	function onAdd(){
   	   		
//    		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_DO_RCPT_INVOICE_HDR_OTHERLANGUAGE";
   		document.form1.action  =  "/track/InvoiceServlet?Submit=EDIT_DO_RCPT_INVOICE_HDR_OTHERLANGUAGE";
   		document.form1.submit();
   	
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
		if(document.form1.DisplayByOrdertypeDO.checked)
		{
		document.form1.OutboundOrderHeaderDO.readOnly = true;		
		document.getElementById('OutboundOrderHeaderDO').getAttributeNode('class').value="inactiveGry";
		}
		else{
			document.form1.OutboundOrderHeaderDO.readOnly = false;
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
        
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String InboundOrderHeader  = "",CollectionOrderHeader="",CustomerOrderHeader="", InvoiceOrderToHeader  = "", FromHeader  = "",Date="",OrderNo ="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="",project ="",UOM="",DisplayByOrderType = "",printDetailDesc="",printWithUENNO="",printcopy="",printWithCustomerUENNO="",printCustTerms="",printCustRemarks="",printBalanceDue="",printPaymentMade="",printAdjustment="",printShippingCost="",printOrderDiscount="";
	String Description = "", OrderQty  = "", Rate = "",TaxAmount="",Amt="",Container="", 
	DisplayContainer="",SubTotal="",Tax="",Total="", hscode="",coo="",printwithbrand="",customerrcbno="",UenNo="",CusUenNO="",invoiceno="",totalafterdiscount="",brand="",invoicedate="",
	Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",Footer10="",Footer11="",Footer12="",Footer13="",printwithhscode="",printwithcoo="",
    remark1="",remark2="",shipto="",deliverydate="",Employee="",CompanyDate="",CompanyName="",CompanyStamp="",CompanySig="",
	printEmployee="",ProductRatesAre="",DisplaySignature="",Orientation="",orderdiscount="",cdiscount="",ctax="",shippingcost="",incoterm="",printincoterm="",rcbno="",printwithproductremarks="",
	isAutoInvoice="",printpackinglist="",printdeliverynote="",printwithproject="",PreparedBy="",Seller="",SellerSignature="",Buyer="",BuyerSignature="",printwithTaxInvoice="",showPreviousSalesCost="",
	ContactName="",Email="",Fax="",Telephone="",Handphone="",Attention="",QtyTotal="",remark3="",FooterPage="",FooterOf="",CashCustomer="",printwithtransportmode="",printwithshipingadd="",printwithcompanysig="",printwithcustnameadrress="",printwithcompanyseal="",RoundoffTotalwithDecimal="",printRoundoffTotalwithDecimal="",printwithProduct="",
	printDiscountReport="",Discount="",NetRate="",Adjustment="",PaymentMade="",BalanceDue="",TransportMode="",GINO="",GINODATE="",
	printWithDeliveryDate="",prdDeliveryDate="",printWithPrdDeliveryDate="";
	
	String InboundOrderHeaderDO  = "", InvoiceOrderToHeaderDO  = "", FromHeaderDO  = "",DateDO="",OrderNoDO ="";
	String RefNoDO   = "", TermsDO  = "", TermsDetailsDO   = "",SoNoDO ="",ItemDO ="",UOMDO="",DisplayByOrderTypeDO = "",printDetailDescDO="",printCustTermsDO="",printCustRemarksDO="";
	String DescriptionDO = "", OrderQtyDO  = "", RateDO = "",TaxAmountDO="",AmtDO="",projectDO = "",ContainerDO="", 
	DisplayContainerDO="",SubTotalDO="",TaxDO="",TotalDO="", hscodeDO="",cooDO="",printwithbrandDO="",customerrcbnoDO="",invoicenoDO="",totalafterdiscountDO="",invoicedateDO="",
	Footer1DO="",Footer2DO="",Footer3DO="",Footer4DO="",Footer5DO="",Footer6DO="",Footer7DO="",Footer8DO="",Footer9DO="",printwithhscodeDO="",printWithUENNODO="",printWithCustomerUENNODO="",printwithcooDO="",
    remark1DO="",remark2DO="",shiptoDO="",deliverydateDO="",EmployeeDO="",CompanyDateDO="",CompanyNameDO="",CompanyStampDO="",CompanySigDO="",
	printEmployeeDO="",DisplaySignatureDO="",OrientationDO="",orderdiscountDO="",cdiscountDO="",ctaxDO="",shippingcostDO="",incotermDO="",rcbnoDO="",printwithproductremarksDO="",brandDO="",
	isAutoInvoiceDO="",printpackinglistDO="",printdeliverynoteDO="",printwithprojectDO="",PreparedByDO="",SellerDO="",SellerSignatureDO="",BuyerDO="",BuyerSignatureDO="",printwithTaxInvoiceDO="",Multilanguprint="",
	ContactNameDO="",EmailDO="",FaxDO="",TelephoneDO="",HandphoneDO="",AttentionDO="",QtyTotalDO="",TransportModeDO="",printwithtransportmodeDO="",remark3DO="",FooterPageDO="",FooterOfDO="",CashCustomerDO="",RoundoffTotalwithDecimalDO="",printRoundoffTotalwithDecimalDO="",printwithProductDO="",
	printDiscountReportDO="",DiscountDO="",NetRateDO="",
	printWithDeliveryDateDO="",prdDeliveryDateDO="",printWithPrdDeliveryDateDO="";
	int FOOTER_SIZE=0;		
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
    	DOUtil doUtil = new DOUtil();
    	
         Map m= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
         if(!m.isEmpty()){
	         InboundOrderHeader= (String) m.get("HDR1");
	         InvoiceOrderToHeader = (String) m.get("HDR2");
	         CustomerOrderHeader = (String) m.get("CUSTOMERHEADER");
	         CollectionOrderHeader = (String) m.get("COLLECTIONHEADER");
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
             Footer10 = (String) m.get("F10");
             Footer11 = (String) m.get("F11");
             Footer12 = (String) m.get("F12");
             Footer13 = (String) m.get("F13");
	         DisplayByOrderType = (String)m.get("DISPLAYBYORDERTYPE");
	         printDetailDesc = (String)m.get("PRINTXTRADETAILS");
	         printCustTerms = (String)m.get("PRINTCUSTERMS");
	         printCustRemarks = (String)m.get("PCUSREMARKS");
	         printBalanceDue = (String)m.get("PRINTBALANCEDUE");
	         printPaymentMade = (String)m.get("PRINTPAYMENTMADE");
	         printAdjustment = (String)m.get("PRINTADJUSTMENT");
	         printShippingCost = (String)m.get("PRINTSHIPPINGCOST");
	         printOrderDiscount = (String)m.get("PRINTORDERDISCOUNT");
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
	         ProductRatesAre=(String) m.get("PRODUCTRATESARE");
	         //DisplaySignature = (String)m.get("DISPLAYSIGNATURE");
	         Orientation = (String)m.get("PrintOrientation");
	         orderdiscount=(String)m.get("ORDERDISCOUNT");
	         cdiscount=(String)m.get("TOTALDISCOUNT");
	         ctax=(String)m.get("TAX");
             shippingcost=(String)m.get("SHIPPINGCOST");
             incoterm=(String)m.get("INCOTERM");
             printincoterm=(String)m.get("PRINTINCOTERM");
             printwithbrand = (String)m.get("PRINTWITHBRAND");
             printwithproductremarks = (String)m.get("PRINTWITHPRODUCTREMARKS");
             printwithhscode = (String)m.get("PRITNWITHHSCODE");
             printwithcoo = (String)m.get("PRITNWITHCOO");
             printpackinglist = (String)m.get("PRINTPACKINGLIST");
             isAutoInvoice = (String)m.get("ISAUTOINVOICE");
             printdeliverynote = (String)m.get("PRINTDELIVERYNOTE");
             rcbno=(String)m.get("RCBNO");
             hscode=(String)m.get("HSCODE");
	         coo=(String)m.get("COO");
	         customerrcbno=(String)m.get("CUSTOMERRCBNO");
	         
	         UenNo=(String)m.get("UENNO");
             CusUenNO=(String)m.get("CUSTOMERUENNO");
	         
	         invoiceno=(String)m.get("INVOICENO");
	         invoicedate=(String)m.get("INVOICEDATE");
	         totalafterdiscount=(String)m.get("TOTALAFTERDISCOUNT");
	         brand=(String)m.get("BRAND");
	         PreparedBy = (String) m.get("PREPAREDBY");
	         Seller = (String) m.get("SELLER");
	         SellerSignature = (String) m.get("SELLERSIGNATURE");
	         Buyer = (String) m.get("BUYER");
	         BuyerSignature = (String) m.get("BUYERSIGNATURE");
	         printwithTaxInvoice = (String)m.get("PRINTWITHTAXINVOICE");
	         Multilanguprint = (String)m.get("PRINTMULTILANG");
			 SubTotal = (String) m.get("SUBTOTAL");
	         Tax = (String) m.get("TOTALTAX");
	         Total = (String) m.get("TOTAL");
	         ContactName = (String) m.get("CONTACTNAME");
	         Email = (String) m.get("EMAIL");
	         Fax = (String) m.get("FAX");
	         Telephone = (String) m.get("TELEPHONE");
	         Handphone = (String) m.get("HANDPHONE");
	         Attention = (String) m.get("ATTENTION");
	         QtyTotal = (String) m.get("QTYTOTAL");
	         remark3 = (String) m.get("REMARK3");
	         FooterPage = (String) m.get("FOOTERPAGE");
	         FooterOf = (String) m.get("FOOTEROF");
	         CashCustomer = (String) m.get("CASHCUSTOMER");
			 RoundoffTotalwithDecimal = (String) m.get("ROUNDOFFTOTALWITHDECIMAL");
	         printRoundoffTotalwithDecimal = (String)m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
			 printwithProduct = (String)m.get("PRINTWITHPRODUCT");
			 printDiscountReport=(String)m.get("PRINTWITHDISCOUNT");
		     Discount = (String)m.get("DISCOUNT");
		     NetRate = (String)m.get("NETRATE");
		     Adjustment = (String)m.get("ADJUSTMENT");
		     PaymentMade = (String)m.get("PAYMENTMADE");
		     BalanceDue = (String)m.get("BALANCEDUE");
		     GINO = (String)m.get("GINO");
		     GINODATE = (String)m.get("GINODATE");
		     
		     printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
	         prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
	         printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
	         
	     //  RESVI STARTS
	         project=(String)m.get("PROJECT");
	         printwithproject=(String)m.get("PRINTWITHPROJECT");
	         printWithUENNO=(String)m.get("PRINTWITHUENNO");
	         printcopy=(String)m.get("ISPRINTDEFAULT");
	         printWithCustomerUENNO=(String)m.get("PRINTWITHCUSTOMERUENNO");
	         printwithcompanysig=(String)m.get("PRINTWITHCOMPANYSIG");
	         printwithcustnameadrress=(String)m.get("PRINTWITHCUSTNAMEADRRESS");
	         printwithcompanyseal=(String)m.get("PRINTWITHCOMPANYSEAL");
	         TransportMode=(String)m.get("TRANSPORT_MODE");
	         printwithtransportmode=(String)m.get("PRINTWITHTRANSPORT_MODE");
	         printwithshipingadd=(String)m.get("PRINTWITHSHIPINGADD");
	     //  RESVI ENDS
	         showPreviousSalesCost = (String) m.get("SHOWPREVIOUSINVOICECOST");
         }
         
         Map mDO= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice Other Languages");
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
	         //DisplaySignatureDO = (String)mDO.get("DISPLAYSIGNATURE");
	         OrientationDO = (String)mDO.get("PrintOrientation");
	         orderdiscountDO=(String)mDO.get("ORDERDISCOUNT");
	         cdiscountDO=(String)mDO.get("TOTALDISCOUNT");
	         ctaxDO=(String)mDO.get("TAX");
             shippingcostDO=(String)mDO.get("SHIPPINGCOST");
             incotermDO=(String)mDO.get("INCOTERM");
             printwithbrandDO = (String)mDO.get("PRINTWITHBRAND");
             printwithproductremarksDO = (String)mDO.get("PRINTWITHPRODUCTREMARKS");
             printwithhscodeDO = (String)mDO.get("PRITNWITHHSCODE");
             printwithcooDO = (String)mDO.get("PRITNWITHCOO");
             isAutoInvoiceDO = (String)mDO.get("ISAUTOINVOICE");
             printpackinglistDO = (String)mDO.get("PRINTPACKINGLIST");
             printdeliverynoteDO = (String)mDO.get("PRINTDELIVERYNOTE");
             rcbnoDO=(String)mDO.get("RCBNO");
             hscodeDO=(String)mDO.get("HSCODE");
	         cooDO=(String)mDO.get("COO");
	         customerrcbnoDO=(String)mDO.get("CUSTOMERRCBNO");
	         invoicenoDO=(String)mDO.get("INVOICENO");
	         invoicedateDO=(String)mDO.get("INVOICEDATE");
	         totalafterdiscountDO=(String)mDO.get("TOTALAFTERDISCOUNT");
	         brandDO=(String)mDO.get("BRAND");
	         PreparedByDO = (String) mDO.get("PREPAREDBY");
	         SellerDO = (String) mDO.get("SELLER");
	         SellerSignatureDO = (String) mDO.get("SELLERSIGNATURE");
	         BuyerDO = (String) mDO.get("BUYER");
	         BuyerSignatureDO = (String) mDO.get("BUYERSIGNATURE");
	         printwithTaxInvoiceDO = (String)mDO.get("PRINTWITHTAXINVOICE");
			 SubTotalDO = (String) mDO.get("SUBTOTAL");
	         TaxDO = (String) mDO.get("TOTALTAX");
	         TotalDO = (String) mDO.get("TOTAL");
	         ContactNameDO = (String) mDO.get("CONTACTNAME");
	         EmailDO = (String) mDO.get("EMAIL");
	         FaxDO = (String) mDO.get("FAX");
	         TelephoneDO = (String) mDO.get("TELEPHONE");
	         HandphoneDO = (String) mDO.get("HANDPHONE");
	         AttentionDO = (String) mDO.get("ATTENTION");
	         QtyTotalDO = (String) mDO.get("QTYTOTAL");
	         remark3DO = (String) mDO.get("REMARK3");
	         FooterPageDO = (String) mDO.get("FOOTERPAGE");
	         FooterOfDO = (String) mDO.get("FOOTEROF");
	         CashCustomerDO = (String) mDO.get("CASHCUSTOMER");
			 RoundoffTotalwithDecimalDO = (String) mDO.get("ROUNDOFFTOTALWITHDECIMAL");
	         printRoundoffTotalwithDecimalDO = (String)mDO.get("PRINTROUNDOFFTOTALWITHDECIMAL");
			 printwithProductDO = (String)mDO.get("PRINTWITHPRODUCT");
			 printDiscountReportDO=(String)mDO.get("PRINTWITHDISCOUNT");
		     DiscountDO = (String)mDO.get("DISCOUNT");
		     NetRateDO = (String)mDO.get("NETRATE");
		     
		     printWithDeliveryDateDO = (String) mDO.get("PRINTWITHDELIVERYDATE");
	         prdDeliveryDateDO = (String) mDO.get("PRODUCTDELIVERYDATE");
	         printWithPrdDeliveryDateDO = (String) mDO.get("PRINTWITHPRODUCTDELIVERYDATE");
	         
	         
		     //  RESVI STARTS
		         projectDO=(String)mDO.get("PROJECT");
		         printwithprojectDO=(String)mDO.get("PRINTWITHPROJECT");
		         printWithUENNODO=(String)mDO.get("PRINTWITHUENNO");
		         printWithCustomerUENNODO=(String)mDO.get("PRINTWITHCUSTOMERUENNO");
		         TransportModeDO=(String)mDO.get("TRANSPORT_MODE");
		         printwithtransportmodeDO=(String)mDO.get("PRINTWITHTRANSPORT_MODE");
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
	            if(!Footer10.isEmpty())
	           	 FOOTER_SIZE=FOOTER_SIZE+1;
	            if(!Footer11.isEmpty())
	           	 FOOTER_SIZE=FOOTER_SIZE+1;
	            if(!Footer12.isEmpty())
	           	 FOOTER_SIZE=FOOTER_SIZE+1;
	            if(!Footer13.isEmpty())
	           	 FOOTER_SIZE=FOOTER_SIZE+1;
         }
         
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		FOOTER_SIZE=0;
        InboundOrderHeader  = "";
        CustomerOrderHeader  = "";
        CollectionOrderHeader  = "";
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
		Footer10="";
		Footer11="";
		Footer12="";
		Footer13="";
        printDetailDesc="";
        printCustTerms="";
        printCustRemarks="";
        printBalanceDue="";
        printPaymentMade="";
        printAdjustment="";
        printShippingCost="";
        printOrderDiscount="";
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
        ProductRatesAre="";
        orderdiscount="";
        cdiscount="";
        ctax="";
        shippingcost="";
        incoterm="";
        printincoterm="";
        hscode="";
		coo="";
		rcbno="";
		printwithproductremarks="";
        printwithbrand="";
        isAutoInvoice="";
        invoiceno="";
        invoicedate="";
        customerrcbno="";
        
        UenNo="";
		CusUenNO="";
        
	    totalafterdiscount="";
	    brand="";
	    PreparedBy="";
	    Seller="";
	    SellerSignature="";
	    Buyer="";
	    BuyerSignature="";
	    printwithTaxInvoice="";
		SubTotal="";
        Tax="";
        Total="";
        ContactName="";
        Email="";
        Fax="";
        Telephone="";
        Handphone="";
        Attention="";
        QtyTotal="";
        remark3="";
        FooterPage="";
        FooterOf="";
        CashCustomer="";
		RoundoffTotalwithDecimal="";
        printRoundoffTotalwithDecimal="";
		printwithProduct="";
		printDiscountReport="";
		Discount="";
		NetRate="";
		printWithDeliveryDate="";
		prdDeliveryDate="";
		printWithPrdDeliveryDate="";
		GINO="";
		GINODATE="";
	    //
	    
	    
// 	    RESVI STARTS
		project="";
		printwithproject="";
		printWithUENNO="";
		printcopy="";
		printWithCustomerUENNO="";
		printwithcompanyseal="";
		printwithcompanysig="";
		printwithcustnameadrress="";
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
		ItemDO   = "";
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
        cdiscountDO="";
        ctaxDO="";
        shippingcostDO="";
        incotermDO="";
        hscodeDO="";
		cooDO="";
		rcbnoDO="";
		printwithproductremarksDO="";
        printwithbrandDO="";
        invoicenoDO="";
        invoicedateDO="";
        customerrcbnoDO="";
	    totalafterdiscountDO="";
	    brandDO="";
	    PreparedByDO="";
	    SellerDO="";
	    SellerSignatureDO="";
	    BuyerDO="";
	    BuyerSignatureDO="";
	    printwithTaxInvoiceDO="";
	    Multilanguprint="";
		SubTotalDO="";
        TaxDO="";
        TotalDO="";
        ContactNameDO="";
        EmailDO="";
        FaxDO="";
        TelephoneDO="";
        HandphoneDO="";
        AttentionDO="";
        QtyTotalDO="";
        remark3DO="";
        FooterPageDO="";
        FooterOfDO="";
        CashCustomerDO="";
		RoundoffTotalwithDecimalDO="";
        printRoundoffTotalwithDecimalDO="";
		printwithProductDO="";
		printDiscountReportDO="";
		DiscountDO="";
		NetRateDO="";
		printWithDeliveryDateDO="";
		prdDeliveryDateDO="";
		printWithPrdDeliveryDateDO="";
		
//      RESVI STARTS
		projectDO="";
		printwithprojectDO="";
		printWithUENNODO="";
		printWithCustomerUENNODO="";
		TransportModeDO="";
		printwithtransportmodeDO="";
		
//      RESVI ENDS
	}  
%>

<CENTER><strong><%=res%></strong></CENTER>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Invoice Printout</label></li>                                   
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
  <input type="hidden" name="OrderType" value="Tax Invoice English">
  <input type="hidden" name="OrderType" value="Tax Invoice Other Languages">      

      <input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->

      <input type = "hidden" name="OrientationDO" value="Portrait" <%if(OrientationDO.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->

	<div class="form-group">
      <label class="control-label col-form-label col-sm-3"  for="Inbound order header">Tax Invoice Header</label>
      <div class="col-sm-4">          
        <INPUT  id="OutboundOrderHeader" name="OutboundOrderHeader" type="TEXT" value="<%=InboundOrderHeader%>"  <%if(DisplayByOrderType.equals("1")) {%>readonly class = "form-control inactiveGry"<%} else{%>   class = "form-control"  <%}%>
			placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertype" name = "DisplayByOrdertype" value = "DisplayByOrdertype" 
			<%if(DisplayByOrderType.equals("1")) {%>checked <%}%>  onClick = "headerReadable();" /> Display By OrderType</label>
      </div>
      </div>
   </div>

	<div class="form-group">
      <label class="control-label col-form-label col-sm-3"  for="Inbound Customer order header">Tax Invoice Customer Copy Header</label>
      <div class="col-sm-4">          
        <INPUT  id="CustomerOrderHeader" name="CustomerOrderHeader" type="TEXT" value="<%=CustomerOrderHeader%>"class = "form-control" 
			placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
 		<div class="col-sm-4">
			<label class="radio-inline">
	      	<input type="radio" name="printcopy" type = "radio"  value="0"  id="default" <%if (printcopy.equalsIgnoreCase("0")) {%> checked <%}%> >Print Default
	    	</label>
	    	<label class="radio-inline">
	      	<input type="radio" name="printcopy" type = "radio" value="1"  id = "customer" <%if (printcopy.equalsIgnoreCase("1")) {%> checked <%}%> >Print Default and Customer Copy
	     	</label>
	    	<label class="radio-inline" style="right: 10px;">
	      	<input type="radio" name="printcopy" type = "radio" value="2"  id = "collection" <%if (printcopy.equalsIgnoreCase("2")) {%> checked <%}%> >Print Default, Customer and Collection Copy
	     	</label>
     	</div>
		</div>
   </div>

	<div class="form-group">
      <label class="control-label col-form-label col-sm-3"  for="Inbound Collection order header">Tax Invoice Collection Copy Header</label>
      <div class="col-sm-4">          
        <INPUT  id="CollectionOrderHeader" name="CollectionOrderHeader" type="TEXT" value="<%=CollectionOrderHeader%>"    class = "form-control"  
			placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
   </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Invoice order to header">Invoice To Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
    <label class="checkbox-inline">      
       <input type = "checkbox" id = "isAutoInvoice" name = "isAutoInvoice" value = "isAutoInvoice" 
			<%if(isAutoInvoice.equals("1")) {%>checked <%}%> />Is Auto Invoice</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="from header">From Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
    <label class="checkbox-inline">      
       <input type = "checkbox" id = "printpackinglist" name = "printpackinglist" value = "printpackinglist" 
			<%if(printpackinglist.equals("1")) {%>checked <%}%> />Print With Packing List</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Ship To">Shipping Address</label>
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
    
    <%-- <div class="form-group">
      <label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ContactName" type="TEXT" value="<%=ContactName%>" size="25" MAXLENGTH=100>
  		        </div>
    </div> --%>
    
    
     <!--       RESVI STARTS TRANSPORT MODE -->
    <div class="form-group">
    <label class="control-label  col-form-label col-sm-3" for="Transport Mode">Transport Mode</label>
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
    
    
     <!--      RESVI STARTS PROJECT-->
            <div class="form-group">
      <label class="control-label  col-form-label col-sm-3" for="Project">Project</label>
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
      <label class="control-label  col-form-label col-sm-3" for="Order No">Order No</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="OrderNo" type="TEXT" value="<%=OrderNo%>"
			placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
            <div class="form-inline">
      <div class="col-sm-4">
        <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynote" name = "printdeliverynote" value = "printdeliverynote" 
			<%if(printdeliverynote.equals("1")) {%>checked <%}%> />Print Delivery Note</label>
      
      </div>
      </div> 
      
      </div> 
      
   <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order header">Order Date</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
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
      <label class="control-label col-form-label col-sm-3" for="GINO">GINO</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINO" type="TEXT" value="<%=GINO%>" size="15" MAXLENGTH=50 placeholder="Max 50 Characters">
      </div>
<%--       	<div class="form-inline">
      	<div class="col-sm-4">
      		<label class="checkbox-inline">      
       		<input type = "checkbox" id = "printBalanceDue" name = "printBalanceDue" value = "printBalanceDue" 
			<%if(printBalanceDue.equals("1")) {%>checked <%}%> />Print With BalanceDue</label>
      	</div>
      	</div> --%>
     </div>
 	   
 	   <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="GINO DATE">GINO Date</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINODATE" type="TEXT" value="<%=GINODATE%>" size="15" MAXLENGTH=30 placeholder="Max 30 Characters">
      </div>
      </div>
 	   
      <div class="form-group">
     <label class="control-label col-form-label col-sm-3" for="invoice no">Invoice No</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INVOICENO" type="TEXT" value="<%=invoiceno%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4" style="display: none;">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithTaxInvoice" name = "printwithTaxInvoice" value = "printwithTaxInvoice" 
			<%if(printwithTaxInvoice.equals("1")) {%>checked <%}%> />Print With Tax Invoice</label>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="invoice date">Invoice Date</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="INVOICEDATE" type="TEXT" value="<%=invoicedate%>" placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
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
          <%-- <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNO" name = "printWithUENNO" value = "printWithUENNO" 
			<%if(printWithUENNO.equals("1")) {%>checked <%}%> />Print With UEN </label>
      </div>
      </div> --%>
      
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
      <label class="control-label col-form-label col-sm-3" for="RCB NO" id="TaxLabel"></label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
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
			<%if(printWithCustomerUENNO.equals("1")) {%>checked <%}%> />Print With Customer UEN</label>
    <%}else{ %>  
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithCustomerUENNO" name = "printWithCustomerUENNO" value = "printWithCustomerUENNO" 
			<%if(printWithCustomerUENNO.equals("1")) {%>checked <%}%> />Print With Customer TRN</label>
    <%} %>
      </div>
      </div>
      </div>

    
<!--     ENDS -->
    
    
	<div class="form-group hidden">
      <label class="control-label col-sm-4" for="Roundoff Total With Tax">Roundoff Total With Tax</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="RoundoffTotalwithDecimal" type="TEXT" value="<%=RoundoffTotalwithDecimal%>" size="15" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimal" name = "printRoundoffTotalwithDecimal" value = "printRoundoffTotalwithDecimal" 
			<%if(printRoundoffTotalwithDecimal.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
      </div> 
      </div>
	
   <%--    <div class="form-group">
      <label class="control-label col-sm-4" for="Reference No">Reference No:</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printDiscountReport" name = "printDiscountReport" value = "printDiscountReport" 
			<%if(printDiscountReport.equals("1")) {%>checked <%}%> />Print with Discount and Unit Price</label>
      </div>
      </div> 
    </div> --%>
	
    <INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
	
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Customer RCB NO" id="CustomerTaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="CUSTOMERRCBNO" type="TEXT" value="<%=customerrcbno%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
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
        <INPUT type="hidden" name="F10" id="F10" value="<%=Footer10%>">
        <INPUT type="hidden" name="F11" id="F11" value="<%=Footer11%>">
        <INPUT type="hidden" name="F12" id="F12" value="<%=Footer12%>">
        <INPUT type="hidden" name="F13" id="F13" value="<%=Footer13%>">
      </div>
      
 
     	<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#contact" class="nav-link" data-toggle="tab" aria-expanded="true">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#other" class="nav-link" data-toggle="tab">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#productDetail" class="nav-link" data-toggle="tab">Product Details</a>
        </li>
        <li class="nav-item">
            <a href="#cal" class="nav-link" data-toggle="tab">Calculations</a>
        </li>
        <li class="nav-item">
            <a href="#cnote" class="nav-link" data-toggle="tab">Customer Notes</a>
        </li>       
         <li class="nav-item">
            <a href="#sign" class="nav-link" data-toggle="tab">Signature</a>
        </li>
        <li class="nav-item">
            <a href="#footer" class="nav-link" data-toggle="tab">Footer</a>
        </li>
        </ul>
        
        <div class="tab-content clearfix">
   <div class="tab-pane active" id="contact">
        <br>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Attention">Attention</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Attention" type="TEXT" value="<%=Attention%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Email">Email</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Email" type="TEXT" value="<%=Email%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Fax">Fax</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Fax" type="TEXT" value="<%=Fax%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Telephone">Telephone</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Telephone" type="TEXT" value="<%=Telephone%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Handphone">Handphone</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Handphone" type="TEXT" value="<%=Handphone%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
        </div>
        <div class="tab-pane fade" id="other">
        <br>
           <div class="form-group">
     <label class="control-label col-form-label col-sm-3" for="Delivery Date">Due Date</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DeliveryDate" type="TEXT" value="<%=deliverydate%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithDeliveryDate" name = "printWithDeliveryDate" value = "printWithDeliveryDate" 
			<%if(printWithDeliveryDate.equals("1")) {%>checked <%}%> />Print With Due Date</label>
      </div>
      </div>
    </div>
    
    <div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-3" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
            <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printincoterm" name = "printincoterm" value = "printincoterm" 
			<%if(printincoterm.equals("1")) {%>checked <%}%> />Print With iNCOTERM</label>
      </div>
      </div> 
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Employee">Employee</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Employee" type="TEXT" value="<%=Employee%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployee" name = "printEmployee" value = "printEmployee" 
			<%if(printEmployee.equals("1")) {%>checked <%}%> />Print With Employee</label>
      </div>
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
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>"	placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Payment Type">Payment Type</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Terms" type="TEXT" value="<%=Terms%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div>
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Terms Details">Terms Details</label>
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
      <label class="control-label col-form-label col-sm-3" for="SoNo">SoNo</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">          
         <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
     <label class="control-label col-form-label col-sm-3" for="Description">Description</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-form-label col-sm-3" for="Order Quantity">Invoice Quantity</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="OrderQty" type="TEXT" value="<%=OrderQty%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printwithproductremarks" name = "printwithproductremarks" value = "printwithproductremarks" 
			<%if(printwithproductremarks.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
    </div>
    
        <div class="form-group">
       <label class="control-label col-form-label col-sm-3" for="UOM">UOM</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
    </div>
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Rate">Rate</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Rate" type="TEXT" value="<%=Rate%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div> 
   
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Container">Container:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	size="25" MAXLENGTH=100>
      </div>    
       <input type = "Hidden" id = "DisplayContainer" name = "DisplayContainer" value = "DisplayContainer" 
		 <%if(DisplayContainer.equals("1")) {%>checked <%}%> />     
       <input type = "Hidden" id = "DisplayContainerDO" name = "DisplayContainerDO" value = "DisplayContainerDO" 
		 <%if(DisplayContainerDO.equals("1")) {%>checked <%}%> />
    </div>
    
   

    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Discount">Discount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Discount" type="TEXT" value="<%=Discount%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div>
    
    <div class="form-group hidden">
      <label class="control-label col-form-label col-sm-2" for="Net Amount">Net Amount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="NetRate" type="TEXT" value="<%=NetRate%>"	size="25" MAXLENGTH=100>
      </div>
    </div>   	
    
     <div class="form-group hidden">
       <label class="control-label col-form-label col-sm-2" for="Tax Amount">Tax Amount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="TaxAmount" type="TEXT" value="<%=TaxAmount%>"	size="25" MAXLENGTH=100>
      </div> 
    </div> 
    
    <div class="form-group hidden">
       <label class="control-label col-sm-4" for="Amount">Amount:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Amt" type="TEXT" value="<%=Amt%>"	placeholder="Max 100 Characters" size="25" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Total After Discount">Total After Discount</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="TOTALAFTERDISCOUNT" type="TEXT" value="<%=totalafterdiscount%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div> 
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Total After Discount">Brand</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BRAND" type="TEXT" value="<%=brand%>"	placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
         <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "pintwithbrand" 
			<%if(printwithbrand.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
    </div> 
       <div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-3" for="HSCODE">HSCODE(DN/PL)</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=hscode%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscode" name = "printwithhscode" value = "printwithhscode" 
			<%if(printwithhscode.equals("1")) {%>checked <%}%> />Print With HSCODE</label>
      </div>
      </div>
    </div>
    
    <div class="form-group" style="display: none;">
     <label class="control-label col-form-label col-sm-3" for="COO">COO(DN/PL)</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="COO" type="TEXT" value="<%=coo%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcoo" name = "printwithcoo" value = "printwithcoo" 
			<%if(printwithcoo.equals("1")) {%>checked <%}%> />Print With COO</label>
      </div>
      </div>
    </div>

    
     
      
    
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Total Tax">Total Tax:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Tax" type="TEXT" value="<%=Tax%>" size="25" MAXLENGTH=100>
  		        </div>
  		    
    </div>
    
   
    
    
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Qty Total">Qty Total</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="QtyTotal" type="TEXT" value="<%=QtyTotal%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div> 
    </div>        
        </div>
        <div class="tab-pane fade" id="cal">
        <br>
           <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Sub Total">Sub Total</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SubTotal" type="TEXT" value="<%=SubTotal%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  		        </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Order Discount">Order Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderDiscount" type="TEXT" value="<%=orderdiscount%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
      <label class="control-label col-form-label col-sm-3" for="Discount">Discount</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TOTALDISCOUNT" type="TEXT" value="<%=cdiscount%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
    </div>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Shipping Cost">Shipping Cost</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ShippingCost" type="TEXT" value="<%=shippingcost%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
		<div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
			<input type = "checkbox" id = "printShippingCost" name = "printShippingCost" value = "printShippingCost" 
			<%if(printShippingCost.equals("1")) {%>checked <%}%> />Print With Shipping Cost</label>
		</div>
		</div> 
    </div>
     <div class="form-group hidden">
      <label class="control-label col-form-label col-sm-3" for="Tax">Tax</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TAX" type="TEXT" value="<%=ctax%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Qty Total">Adjustment</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ADJUSTMENT" type="TEXT" value="<%=Adjustment%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  	 </div> 
  	<div class="form-inline">
	<div class="col-sm-4">
		<label class="checkbox-inline">      
		<input type = "checkbox" id = "printAdjustment" name = "printAdjustment" value = "printAdjustment" 
		<%if(printAdjustment.equals("1")) {%>checked <%}%> />Print With Adjustment</label>
	</div>
	</div>
    </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Total With Tax">Total</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
       </div>
    </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Qty Total">Payment Made</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="PAYMENTMADE" type="TEXT" value="<%=PaymentMade%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  	  </div>
      <div class="form-inline">
		<div class="col-sm-4">
			<label class="checkbox-inline">      
				<input type = "checkbox" id = "printPaymentMade" name = "printPaymentMade" value = "printPaymentMade" 
				<%if(printPaymentMade.equals("1")) {%>checked <%}%> />Print With Payment Made</label>
		</div>
	</div> 
    </div>
    
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Qty Total">Balance Due</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BALANCEDUE" type="TEXT" value="<%=BalanceDue%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
  		    <div class="form-inline">
      		<div class="col-sm-4">
      			<label class="checkbox-inline">      
       			<input type = "checkbox" id = "printBalanceDue" name = "printBalanceDue" value = "printBalanceDue" 
				<%if(printBalanceDue.equals("1")) {%>checked <%}%> />Print With Balance Due</label>
      		</div>
      		</div>
    </div>
    
    <div class="form-group">
 		<div class="col-sm-6" style="margin-left:305px">
  		<label > 
  		<input type="radio" style="display:none" name="showPreviousSalesCost" value="0" id="ByAverageCost" <%if (showPreviousSalesCost.equalsIgnoreCase("0")) {%> checked <%}%> ><!-- Show Sales Invoice Price By Average Cost
		<br> --><input type="radio" name="showPreviousSalesCost" value="1" <%if (showPreviousSalesCost.equalsIgnoreCase("1")) {%> checked <%}%> >Show Sales Invoice Price by Last Order Selling Price (By Customer)
		<br><input type="radio" name="showPreviousSalesCost" value="2" <%if (showPreviousSalesCost.equalsIgnoreCase("2")) {%> checked <%}%> >Show Sales Invoice Price by Listed Price
		<br><input type="radio" name="showPreviousSalesCost" value="3" <%if (showPreviousSalesCost.equalsIgnoreCase("3")) {%> checked <%}%> >Show Sales Price by Purchase Cost & Increased Value 
		</label>
     	</div>
		</div>   
    
<%--     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Qty Total">Balance Due</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BALANCEDUE" type="TEXT" value="<%=BalanceDue%>" placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
  		        </div> 
    </div>    --%>  
        </div> 
         <div class="tab-pane fade" id="cnote">
        <br>
          <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Customer Remarks">Customer Notes</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="remark3" type="TEXT" value="<%=remark3%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
  		        </div>        
        </div>
        
        <div class="tab-pane fade" id="sign">
        <br>
            <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Seller">Seller</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Seller" type="TEXT" value="<%=Seller%>"	placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SellerSignature" type="TEXT" value="<%=SellerSignature%>"	placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="Buyer" type="TEXT" value="<%=Buyer%>"	placeholder="Max 50 Characters"size="25" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="BuyerSignature" type="TEXT" value="<%=BuyerSignature%>"	placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Date">Date:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyDate" type="TEXT" value="<%=CompanyDate%>" size="25" MAXLENGTH=100>
      </div>
    </div>    
    
    <div class="form-group">
    <label class="control-label col-form-label col-sm-3" for="Company Name">Company Name:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyName" type="TEXT" value="<%=CompanyName%>"	size="25" MAXLENGTH=100>
      </div>
    </div>   
    
    <div class="form-group ">
   <label class="control-label col-form-label col-sm-3" for="Company Stamp">Company Stamp:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanyStamp" type="TEXT" value="<%=CompanyStamp%>"	size="25" MAXLENGTH=100>
  		</div>
  		     <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcompanyseal" name = "printwithcompanyseal" value = "printwithcompanyseal" 
			<%if(printwithcompanyseal.equals("1")) {%>checked <%}%> />Print With Digital Stamp</label>
      </div>
      </div>
      </div>
    
    <div class="form-group ">
    <label class="control-label col-form-label col-sm-3" for="Signature">Signature:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanySig" type="TEXT" value="<%=CompanySig%>" size="25" MAXLENGTH=100>
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
    	<h1 style="font-weight: bold;font-size: 14px;color: red;">* Configure Your Terms &amp; Conditions from Footer1 to Footer 7 <br>* Configure Your Bank Details from Footer8 to Footer 13 </h1>
        <TABLE id="footertbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD style="width: 23%;">
		<label class="control-label col-form-label col-sm-2" for="Footer1">Footer1</label></TD>		
		<TD align="center"  style="width: 56%;"><div class="col-sm-8"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('footertbl');return false;"></span>
		<INPUT class="form-control footerSearch" name="Footer1" id="Footer1"  placeholder="Max 200 Characters" type = "TEXT" value="<%=Footer1%>" size="100"  MAXLENGTH=200>		        
       	</div>
       	</div>&nbsp;
        </TD>
        <TD>
      	<div style="width: 95%; left:485px;">
      	<label class="checkbox-inline" style="right: 150px;">    
       	<input type = "checkbox" id = "printwithcustnameadrress" name = "printwithcustnameadrress" value = "printwithcustnameadrress" 
		<%if(printwithcustnameadrress.equals("1")) {%>checked <%}%> />Print With Customer Name And Address</label>
    	</div>
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="FOOTER_SIZE" value="<%=FOOTER_SIZE%>" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-3"></div>
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
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Remarks1">Remarks1:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" size="25" MAXLENGTH=100>
      </div> 
    </div>
    
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Remarks2">Remarks2:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="REMARK2" type="TEXT" value="<%=remark2%>" size="25" MAXLENGTH=100>
      </div> 
    </div>
    
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="prdDeliveryDate">Product Delivery Date:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="prdDeliveryDate" type="TEXT" value="<%=prdDeliveryDate%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDate" name = "printWithPrdDeliveryDate" value = "printWithPrdDeliveryDate" 
			<%if(printWithPrdDeliveryDate.equals("1")) {%>checked <%}%> />Print With Product Delivery Date</label>
      </div>
      </div>
    </div>
      
    
<%--     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Terms Details">Terms Details:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="TermsDetails" type="TEXT" value="<%=TermsDetails%>"	size="25" MAXLENGTH=100>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustTerms" name = "printCustTerms" value = "printCustTerms" 
			<%if(printCustTerms.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
    </div> --%>
       
  
  		        
  	<div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer Page">Footer Page:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="FooterPage" type="TEXT" value="<%=FooterPage%>" size="25" MAXLENGTH=100>
  		        </div>
  		        </div>
  		        
  		        <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer Of">Footer Of:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="FooterOf" type="TEXT" value="<%=FooterOf%>" size="25" MAXLENGTH=100>
  		        </div>
  		        </div>
  		        
  		        <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Cash Customer">Cash Customer:</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CashCustomer" type="TEXT" value="<%=CashCustomer%>" size="25" MAXLENGTH=100>
  		        </div>
  		        </div>
         
       
<%--     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer1">Footer1:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer1" type="TEXT" value="<%=Footer1%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer1.value+'&TYPE=Footer1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer2">Footer2:</label>
            <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer2" type="TEXT" value="<%=Footer2%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer2.value+'&TYPE=Footer2');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
    <div class="form-group hidden">
       <label class="control-label col-sm-4" for="Footer3">Footer3:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer3" type="TEXT" value="<%=Footer3%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer3.value+'&TYPE=Footer3');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
    <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer4">Footer4:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer4" type="TEXT" value="<%=Footer4%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer4.value+'&TYPE=Footer4');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
     <div class="form-group hidden">
       <label class="control-label col-sm-4" for="Footer5">Footer5:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer5" type="TEXT" value="<%=Footer5%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer5.value+'&TYPE=Footer5');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer6">Footer6:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer6" type="TEXT" value="<%=Footer6%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer6.value+'&TYPE=Footer6');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer7">Footer7:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer7" type="TEXT" value="<%=Footer7%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer7.value+'&TYPE=Footer7');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer8">Footer8:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer8" type="TEXT" value="<%=Footer8%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer8.value+'&TYPE=Footer8');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    
     <div class="form-group hidden">
      <label class="control-label col-sm-4" for="Footer9">Footer9:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer9" type="TEXT" value="<%=Footer9%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer9.value+'&TYPE=Footer9');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div> --%>
    
     
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

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabel').innerHTML = "Customer "+ d +" No";
    //DO
    /* document.getElementById('TaxLabelDO').innerHTML = d +" No";
    document.getElementById('CustomerTaxLabelDO').innerHTML = "Customer "+ d +" No"; */
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
	if(rowCount!=13)
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
		alert("Can not add more then 13 footer ");
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