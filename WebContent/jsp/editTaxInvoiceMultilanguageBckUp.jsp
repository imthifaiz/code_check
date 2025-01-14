<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Direct Tax Invoice Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
		
		//Tax Invoice English
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
		document.form1.printwithbrand.checked = false;
		//document.form1.printpackinglist.checked = false;
		//document.form1.printdeliverynote.checked = false;
        
		document.form1.HSCODE.value="";
        document.form1.COO.value="";
        document.form1.RCBNO.value="";
        document.form1.INVOICENO.value="";
        document.form1.INVOICEDATE.value="";
        document.form1.CUSTOMERRCBNO.value="";
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
        document.form1.prdDeliveryDate.value="";
        document.form1.printWithPrdDeliveryDate.checked = false;
		
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
		document.form1.INCOTERMDO.value="";
		document.form1.printwithproductremarksDO.checked = false;
		document.form1.printwithhscodeDO.checked = false;
		document.form1.printwithcooDO.checked = false;
		document.form1.printwithbrandDO.checked = false;
		//document.form1.printpackinglistDO.checked = false;
		//document.form1.printdeliverynoteDO.checked = false;
        
		document.form1.HSCODEDO.value="";
        document.form1.COODO.value="";
        document.form1.RCBNODO.value="";
        document.form1.INVOICENODO.value="";
        document.form1.INVOICEDATEDO.value="";
        document.form1.CUSTOMERRCBNODO.value="";
        document.form1.TOTALAFTERDISCOUNTDO.value="";
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
	}
	
	function onAdd(){
   	   		
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_DO_RCPT_INVOICE_HDR_OTHERLANGUAGE";
   		document.form1.submit();
   	
	}
	function headerReadable(){
		if(document.form1.DisplayByOrdertype.checked)
		{
		document.form1.OutboundOrderHeader.readOnly = true;		
		document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="inactivegry";
		}
		else{
			document.form1.OutboundOrderHeader.readOnly = false;
			document.getElementById('OutboundOrderHeader').getAttributeNode('class').value="";
		}
	}
	
	function headerReadableDO(){
		if(document.form1.DisplayByOrdertypeDO.checked)
		{
		document.form1.OutboundOrderHeaderDO.readOnly = true;		
		document.getElementById('OutboundOrderHeaderDO').getAttributeNode('class').value="inactivegry";
		}
		else{
			document.form1.OutboundOrderHeaderDO.readOnly = false;
			document.getElementById('OutboundOrderHeaderDO').getAttributeNode('class').value="";
		}
	}
	
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String taxbylabel= ub.getTaxByLable(plant);
        
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String InboundOrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",Date="",OrderNo ="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="",UOM="",DisplayByOrderType = "",printDetailDesc="",printCustTerms="",printCustRemarks="";
	String Description = "", OrderQty  = "", Rate = "",TaxAmount="",Amt="",Container="", 
	DisplayContainer="",SubTotal="",Tax="",Total="", hscode="",coo="",printwithbrand="",customerrcbno="",invoiceno="",totalafterdiscount="",invoicedate="",
	Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",printwithhscode="",printwithcoo="",
    remark1="",remark2="",shipto="",deliverydate="",Employee="",CompanyDate="",CompanyName="",CompanyStamp="",CompanySig="",
	printEmployee="",DisplaySignature="",Orientation="",orderdiscount="",shippingcost="",incoterm="",rcbno="",printwithproductremarks="",
	printpackinglist="",printdeliverynote="",PreparedBy="",Seller="",SellerSignature="",Buyer="",BuyerSignature="",printwithTaxInvoice="",
	ContactName="",Email="",Fax="",Telephone="",Handphone="",Attention="",QtyTotal="",remark3="",FooterPage="",FooterOf="",CashCustomer="",RoundoffTotalwithDecimal="",printRoundoffTotalwithDecimal="",printwithProduct="",
	printDiscountReport="",Discount="",NetRate="",
	printWithDeliveryDate="",prdDeliveryDate="",printWithPrdDeliveryDate="";
	
	String InboundOrderHeaderDO  = "", InvoiceOrderToHeaderDO  = "", FromHeaderDO  = "",DateDO="",OrderNoDO ="";
	String RefNoDO   = "", TermsDO  = "", TermsDetailsDO   = "",SoNoDO ="",ItemDO ="",UOMDO="",DisplayByOrderTypeDO = "",printDetailDescDO="",printCustTermsDO="",printCustRemarksDO="";
	String DescriptionDO = "", OrderQtyDO  = "", RateDO = "",TaxAmountDO="",AmtDO="",ContainerDO="", 
	DisplayContainerDO="",SubTotalDO="",TaxDO="",TotalDO="", hscodeDO="",cooDO="",printwithbrandDO="",customerrcbnoDO="",invoicenoDO="",totalafterdiscountDO="",invoicedateDO="",
	Footer1DO="",Footer2DO="",Footer3DO="",Footer4DO="",Footer5DO="",Footer6DO="",Footer7DO="",Footer8DO="",Footer9DO="",printwithhscodeDO="",printwithcooDO="",
    remark1DO="",remark2DO="",shiptoDO="",deliverydateDO="",EmployeeDO="",CompanyDateDO="",CompanyNameDO="",CompanyStampDO="",CompanySigDO="",
	printEmployeeDO="",DisplaySignatureDO="",OrientationDO="",orderdiscountDO="",shippingcostDO="",incotermDO="",rcbnoDO="",printwithproductremarksDO="",
	printpackinglistDO="",printdeliverynoteDO="",PreparedByDO="",SellerDO="",SellerSignatureDO="",BuyerDO="",BuyerSignatureDO="",printwithTaxInvoiceDO="",Multilanguprint="",
	ContactNameDO="",EmailDO="",FaxDO="",TelephoneDO="",HandphoneDO="",AttentionDO="",QtyTotalDO="",remark3DO="",FooterPageDO="",FooterOfDO="",CashCustomerDO="",RoundoffTotalwithDecimalDO="",printRoundoffTotalwithDecimalDO="",printwithProductDO="",
	printDiscountReportDO="",DiscountDO="",NetRateDO="",
	printWithDeliveryDateDO="",prdDeliveryDateDO="",printWithPrdDeliveryDateDO="";
			
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
    	DOUtil doUtil = new DOUtil();
    	
         Map m= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
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
	         //DisplaySignature = (String)m.get("DISPLAYSIGNATURE");
	         Orientation = (String)m.get("PrintOrientation");
	         orderdiscount=(String)m.get("ORDERDISCOUNT");
             shippingcost=(String)m.get("SHIPPINGCOST");
             incoterm=(String)m.get("INCOTERM");
             printwithbrand = (String)m.get("PRINTWITHBRAND");
             printwithproductremarks = (String)m.get("PRINTWITHPRODUCTREMARKS");
             printwithhscode = (String)m.get("PRITNWITHHSCODE");
             printwithcoo = (String)m.get("PRITNWITHCOO");
             printpackinglist = (String)m.get("PRINTPACKINGLIST");
             printdeliverynote = (String)m.get("PRINTDELIVERYNOTE");
             rcbno=(String)m.get("RCBNO");
             hscode=(String)m.get("HSCODE");
	         coo=(String)m.get("COO");
	         customerrcbno=(String)m.get("CUSTOMERRCBNO");
	         invoiceno=(String)m.get("INVOICENO");
	         invoicedate=(String)m.get("INVOICEDATE");
	         totalafterdiscount=(String)m.get("TOTALAFTERDISCOUNT");
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
		     
		     printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
	         prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
	         printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
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
             shippingcostDO=(String)mDO.get("SHIPPINGCOST");
             incotermDO=(String)mDO.get("INCOTERM");
             printwithbrandDO = (String)mDO.get("PRINTWITHBRAND");
             printwithproductremarksDO = (String)mDO.get("PRINTWITHPRODUCTREMARKS");
             printwithhscodeDO = (String)mDO.get("PRITNWITHHSCODE");
             printwithcooDO = (String)mDO.get("PRITNWITHCOO");
             printpackinglistDO = (String)mDO.get("PRINTPACKINGLIST");
             printdeliverynoteDO = (String)mDO.get("PRINTDELIVERYNOTE");
             rcbnoDO=(String)mDO.get("RCBNO");
             hscodeDO=(String)mDO.get("HSCODE");
	         cooDO=(String)mDO.get("COO");
	         customerrcbnoDO=(String)mDO.get("CUSTOMERRCBNO");
	         invoicenoDO=(String)mDO.get("INVOICENO");
	         invoicedateDO=(String)mDO.get("INVOICEDATE");
	         totalafterdiscountDO=(String)mDO.get("TOTALAFTERDISCOUNT");
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
         }
         
	try {
		action = strUtils.fString(request.getParameter("action"));
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
        shippingcost="";
        incoterm="";
        hscode="";
		coo="";
		rcbno="";
		printwithproductremarks="";
        printwithbrand="";
        invoiceno="";
        invoicedate="";
        customerrcbno="";
	    totalafterdiscount="";
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
	    
	    //
	    
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
  <input type="hidden" name="OrderType" value="Tax Invoice English">
  <input type="hidden" name="OrderType" value="Tax Invoice Other Languages">
 <div class="form-group">
 <div class="col-sm-6">
 <h2 style="text-align:center"><small>Direct Tax Invoice Printout(English)</small></h2>
     </div>
      <div class="form-inline">
      <div class="col-sm-6">
     <h2 style="text-align:center"><small>Direct Tax Invoice Printout(Multi Language)</small></h2>
     </div> 
    </div>
    </div>
        	       	  <div class="row">
       	  <div class="form-group">
 		<div style="text-align: center">
      <label class="control-label">      
       <input type = "checkbox" id = "Multilanguprint" name = "Multilanguprint" value = "Multilanguprint" 
			<%if(Multilanguprint.equals("1")) {%>checked <%}%> />&nbsp Print With Multi Language</label>
      </div></div></div>
      
  <%-- <div class="form-group" >
  <label class="control-label col-sm-2" for="Orientation">Orientation:</label>
    <div class="col-sm-3">
    <label class="radio-inline">
     <input type = "radio" name="Orientation" value="Landscape" <%if(Orientation.equals("Landscape")) {%>checked <%}%>/>Landscape
    </label>
    <label class="radio-inline"> --%>
      <input type = "hidden" name="Orientation" value="Portrait" <%if(Orientation.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
    <%-- </label>
     </div>
     <div class="form-inline">
     <label class="control-label col-sm-3" for="Orientation">Orientation:</label>
    <div class="col-sm-3">
    <label class="radio-inline">
     <input type = "radio" name="OrientationDO" value="Landscape" <%if(OrientationDO.equals("Landscape")) {%>checked <%}%>/>Landscape
    </label>
    <label class="radio-inline"> --%>
      <input type = "hidden" name="OrientationDO" value="Portrait" <%if(OrientationDO.equals("Portrait")) {%>checked <%}%>/><!-- Portrait -->
    <!-- </label>
     </div>
     </div>
</div> -->

<div class="form-group">
      <label class="control-label col-sm-2"  for="Inbound order header">Tax Invoice Header:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="OutboundOrderHeader" type="TEXT" value="<%=InboundOrderHeader%>" <%if(DisplayByOrderType.equals("1")) {%>readonly class = inactivegry<%} else{%> class = "" <%}%>
			size="25" MAXLENGTH=20>
		      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "DisplayByOrdertype" name = "DisplayByOrdertype" value = "DisplayByOrdertype" 
			<%if(DisplayByOrderType.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/>Display By OrderType</label>
      </div>
      </div>
      <div class="form-inline">
       <label class="control-label col-sm-2" for="Inbound order header">Tax Invoice Header:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="OutboundOrderHeaderDO" type="TEXT" value="<%=InboundOrderHeaderDO%>" <%if(DisplayByOrderTypeDO.equals("1")) {%>readonly class = inactivegry<%} else{%> class = "" <%}%>
			size="15" MAXLENGTH=20>
		      </div>
		      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline"> 
        <input type = "checkbox" id = "DisplayByOrdertypeDO" name = "DisplayByOrdertypeDO" value = "DisplayByOrdertypeDO" 
			<%if(DisplayByOrderTypeDO.equals("1")) {%>checked <%}%> onClick = "headerReadableDO();"/>Display By OrderType</label>
      </div>
   </div>
   </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Invoice order to header">Invoice To Header:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printDetailDesc" name = "printDetailDesc" value = "printDetailDesc" 
			<%if(printDetailDesc.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Invoice order to header">Invoice To Header:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="InvoiceOrderToHeaderDO" type="TEXT" value="<%=InvoiceOrderToHeaderDO%>"
			size="15" MAXLENGTH=100>
      </div>
          </div>
          <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printDetailDescDO" name = "printDetailDescDO" value = "printDetailDescDO" 
			<%if(printDetailDescDO.equals("1")) {%>checked <%}%> />Print With Detail Description</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="from header">From Header:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarks" name = "printCustRemarks" value = "printCustRemarks" 
			<%if(printCustRemarks.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="from header">From Header:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="FromHeaderDO" type="TEXT" value="<%=FromHeaderDO%>"
			size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarksDO" name = "printCustRemarksDO" value = "printCustRemarksDO" 
			<%if(printCustRemarksDO.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Ship To">Ship To:</label>
      <div class="col-sm-2">          
         <INPUT class="form-control" name="ShipTo" type="TEXT" value="<%=shipto%>"size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printwithproductremarks" name = "printwithproductremarks" value = "printwithproductremarks" 
			<%if(printwithproductremarks.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Ship To">Ship To:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="ShipToDO" type="TEXT" value="<%=shiptoDO%>"size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithproductremarksDO" name = "printwithproductremarksDO" value = "printwithproductremarksDO" 
			<%if(printwithproductremarksDO.equals("1")) {%>checked <%}%> />Print With Product Remarks</label>
      </div>
      </div>
    </div>
    
   <div class="form-group">
      <label class="control-label col-sm-2" for="Order header">Transaction Date:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
			size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrand" name = "printwithbrand" value = "pintwithbrand" 
			<%if(printwithbrand.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Order header">Transaction Date:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="DateDO" type="TEXT" value="<%=DateDO%>"
			size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithbrandDO" name = "printwithbrandDO" value = "pintwithbrandDO" 
			<%if(printwithbrandDO.equals("1")) {%>checked <%}%> />Print With Brand</label>
      </div>
      </div>
      </div>
    
 	  <div class="form-group">
      <label class="control-label col-sm-2" for="Order No">Transaction ID:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="OrderNo" type="TEXT" value="<%=OrderNo%>"
			size="25" MAXLENGTH=100>
      </div>
       <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynote" name = "printdeliverynote" value = "printdeliverynote" 
			<%if(printdeliverynote.equals("1")) {%>checked <%}%> />Print Delivery Note</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Order No">Transaction ID:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="OrderNoDO" type="TEXT" value="<%=OrderNoDO%>"
			size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynoteDO" name = "printdeliverynoteDO" value = "printdeliverynoteDO" 
			<%if(printdeliverynoteDO.equals("1")) {%>checked <%}%> />Print Delivery Note</label>
      </div>
      </div>
      </div> 
      
         
    
      <div class="form-group">
     <label class="control-label col-sm-2" for="invoice no">Invoice No:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="INVOICENO" type="TEXT" value="<%=invoiceno%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printpackinglist" name = "printpackinglist" value = "printpackinglist" 
			<%if(printpackinglist.equals("1")) {%>checked <%}%> />Print With Packing List</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="invoice no">Invoice No:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="INVOICENODO" type="TEXT" value="<%=invoicenoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printpackinglistDO" name = "printpackinglistDO" value = "printpackinglistDO" 
			<%if(printpackinglistDO.equals("1")) {%>checked <%}%> />Print With Packing List</label>
      </div>
      </div>      
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-2" for="invoice date">Invoice Date</label>
      <div class="col-sm-2">          
      <INPUT class="form-control" name="INVOICEDATE" type="TEXT" value="<%=invoicedate%>" size="15" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithTaxInvoice" name = "printwithTaxInvoice" value = "printwithTaxInvoice" 
			<%if(printwithTaxInvoice.equals("1")) {%>checked <%}%> />Print With Tax Invoice</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="invoice date">Invoice Date:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="INVOICEDATEDO" type="TEXT" value="<%=invoicedateDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithTaxInvoiceDO" name = "printwithTaxInvoiceDO" value = "printwithTaxInvoiceDO" 
			<%if(printwithTaxInvoiceDO.equals("1")) {%>checked <%}%> />Print With Tax Invoice</label>
      </div>
      </div>
      </div>
    
	<div class="form-group">
      <label class="control-label col-sm-2" for="Roundoff Total With Tax">Roundoff Total With Tax</label>
      <div class="col-sm-2">          
      <INPUT class="form-control" name="RoundoffTotalwithDecimal" type="TEXT" value="<%=RoundoffTotalwithDecimal%>" size="15" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimal" name = "printRoundoffTotalwithDecimal" value = "printRoundoffTotalwithDecimal" 
			<%if(printRoundoffTotalwithDecimal.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Roundoff Total With Tax">Roundoff Total With Tax:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="RoundoffTotalwithDecimalDO" type="TEXT" value="<%=RoundoffTotalwithDecimalDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printRoundoffTotalwithDecimalDO" name = "printRoundoffTotalwithDecimalDO" value = "printRoundoffTotalwithDecimalDO" 
			<%if(printRoundoffTotalwithDecimalDO.equals("1")) {%>checked <%}%> />Roundoff Total with Decimal</label>
      </div>
      </div>
      </div>
	
      <div class="form-group">
      <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
      <div class="col-sm-2">          
      <INPUT class="form-control" name="RefNo" type="TEXT" value="<%=RefNo%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printDiscountReport" name = "printDiscountReport" value = "printDiscountReport" 
			<%if(printDiscountReport.equals("1")) {%>checked <%}%> />Print with Discount and Unit Price</label>
      </div>
      </div> 	  
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="RefNoDO" type="TEXT" value="<%=RefNoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
	  <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printDiscountReportDO" name = "printDiscountReportDO" value = "printDiscountReportDO" 
			<%if(printDiscountReportDO.equals("1")) {%>checked <%}%> />Print with Discount and Unit Price</label>
      </div>
      </div>
    </div>
	
    <INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
	
    <div class="form-group">
      <label class="control-label col-sm-2" for="RCB NO" id="TaxLabel"></label>
      <div class="col-sm-2">          
      <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="RCB NO" id="TaxLabelDO"></label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="RCBNODO" type="TEXT" value="<%=rcbnoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-2" for="Customer RCB NO" id="CustomerTaxLabel"></label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="CUSTOMERRCBNO" type="TEXT" value="<%=customerrcbno%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Customer RCB NO" id="CustomerTaxLabelDO"></label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CUSTOMERRCBNODO" type="TEXT" value="<%=customerrcbnoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      </div>
          
      <div class="form-group">
      <label class="control-label col-sm-2" for="HSCODE">HSCODE(DN/PL):</label>
      <div class="col-sm-2">          
      <INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=hscode%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscode" name = "printwithhscode" value = "printwithhscode" 
			<%if(printwithhscode.equals("1")) {%>checked <%}%> />Direct Tax Print With HSCODE</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="HSCODE">HSCODE(DN/PL):</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="HSCODEDO" type="TEXT" value="<%=hscodeDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithhscodeDO" name = "printwithhscodeDO" value = "printwithhscodeDO" 
			<%if(printwithhscodeDO.equals("1")) {%>checked <%}%> />Direct Tax Print With HSCODE</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
     <label class="control-label col-sm-2" for="COO">COO(DN/PL):</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="COO" type="TEXT" value="<%=coo%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcoo" name = "printwithcoo" value = "printwithcoo" 
			<%if(printwithcoo.equals("1")) {%>checked <%}%> />Direct Tax Print With COO</label>
      </div>
      </div>
       <div class="form-inline">
      <label class="control-label col-sm-2" for="COO">COO(DN/PL):</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="COODO" type="TEXT" value="<%=cooDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithcooDO" name = "printwithcooDO" value = "printwithcooDO" 
			<%if(printwithcooDO.equals("1")) {%>checked <%}%> />Direct Tax Print With COO</label>
      </div>
      </div>
    </div>
 
    <div class="form-group">
      <label class="control-label col-sm-2" for="Remarks1">Remarks1:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" size="25" MAXLENGTH=100>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Remarks1">Remarks1:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="REMARK1DO" type="TEXT" value="<%=remark1DO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Remarks2">Remarks2:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="REMARK2" type="TEXT" value="<%=remark2%>" size="25" MAXLENGTH=100>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Remarks2">Remarks2:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="REMARK2DO" type="TEXT" value="<%=remark2DO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
     <div class="form-group">
     <label class="control-label col-sm-2" for="Delivery Date">Delivery Date:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="DeliveryDate" type="TEXT" value="<%=deliverydate%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithDeliveryDate" name = "printWithDeliveryDate" value = "printWithDeliveryDate" 
			<%if(printWithDeliveryDate.equals("1")) {%>checked <%}%> />Print With Delivery Date</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Delivery Date">Delivery Date:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="DeliveryDateDO" type="TEXT" value="<%=deliverydateDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-inline">
	      <div class="col-sm-2">
	      <label class="checkbox-inline">      
	       <input type = "checkbox" id = "printWithDeliveryDateDO" name = "printWithDeliveryDateDO" value = "printWithDeliveryDateDO" 
				<%if(printWithDeliveryDateDO.equals("1")) {%>checked <%}%> />Print With Delivery Date</label>
	      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="prdDeliveryDate">Product Delivery Date:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="prdDeliveryDate" type="TEXT" value="<%=prdDeliveryDate%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDate" name = "printWithPrdDeliveryDate" value = "printWithPrdDeliveryDate" 
			<%if(printWithPrdDeliveryDate.equals("1")) {%>checked <%}%> />Print With Product Delivery Date</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="prdDeliveryDateDO">Product Delivery Date:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="prdDeliveryDateDO" type="TEXT" value="<%=prdDeliveryDateDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
      
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printWithPrdDeliveryDateDO" name = "printWithPrdDeliveryDateDO" value = "printWithPrdDeliveryDateDO" 
			<%if(printWithPrdDeliveryDateDO.equals("1")) {%>checked <%}%> />Print With Product Delivery Date</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Employee">Employee:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="Employee" type="TEXT" value="<%=Employee%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployee" name = "printEmployee" value = "printEmployee" 
			<%if(printEmployee.equals("1")) {%>checked <%}%> />Print With Employee</label>
      </div>
      </div>
       <div class="form-inline">
      <label class="control-label col-sm-2" for="Employee">Employee:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="EmployeeDO" type="TEXT" value="<%=EmployeeDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printEmployeeDO" name = "printEmployeeDO" value = "printEmployeeDO" 
			<%if(printEmployeeDO.equals("1")) {%>checked <%}%> />Print With Employee</label>
      </div>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Order Discount">Order Discount:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="OrderDiscount" type="TEXT" value="<%=orderdiscount%>" size="25" MAXLENGTH=30>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Order Discount">Order Discount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="OrderDiscountDO" type="TEXT" value="<%=orderdiscountDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Shipping Cost">Shipping Cost:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="ShippingCost" type="TEXT" value="<%=shippingcost%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Shipping Cost">Shipping Cost:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="ShippingCostDO" type="TEXT" value="<%=shippingcostDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="INCOTERM">INCOTERM:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>" size="25" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="INCOTERM">INCOTERM:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="INCOTERMDO" type="TEXT" value="<%=incotermDO%>" size="15" MAXLENGTH=30>
      </div>
      </div>
    </div>
    
      <div class="form-group">
      <label class="control-label col-sm-2" for="Payment Type">Payment Type:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="Terms" type="TEXT" value="<%=Terms%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Payment Type">Payment Type:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="TermsDO" type="TEXT" value="<%=TermsDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Terms Details">Terms Details:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="TermsDetails" type="TEXT" value="<%=TermsDetails%>"	size="25" MAXLENGTH=100>
      </div>
       <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustTerms" name = "printCustTerms" value = "printCustTerms" 
			<%if(printCustTerms.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
         <div class="form-inline">
      <label class="control-label col-sm-2" for="Terms Details">Terms Details:</label>
      <div class="col-sm-2">
  	  <INPUT class="form-control" name="TermsDetailsDO" type="TEXT" value="<%=TermsDetailsDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustTermsDO" name = "printCustTermsDO" value = "printCustTermsDO" 
			<%if(printCustTermsDO.equals("1")) {%>checked <%}%> />Override with Customer terms</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="SoNo">SoNo:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="SoNo" type="TEXT" value="<%=SoNo%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="SoNo">SoNo:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="SoNoDO" type="TEXT" value="<%=SoNoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
      <div class="col-sm-2">          
         <INPUT class="form-control" name="Item" type="TEXT" value="<%=Item%>"	size="25" MAXLENGTH=100>
      </div>
	  <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithProduct" name = "printwithProduct" value = "printwithProduct" 
			<%if(printwithProduct.equals("1")) {%>checked <%}%> />Print With Product ID</label>
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="ItemDO" type="TEXT" value="<%=ItemDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
	  <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithProductDO" name = "printwithProductDO" value = "printwithProductDO" 
			<%if(printwithProductDO.equals("1")) {%>checked <%}%> />Print With Product ID</label>
      </div>
      </div>
    </div>
    
    <div class="form-group">
     <label class="control-label col-sm-2" for="Description">Description:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="Description" type="TEXT" value="<%=Description%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Description">Description:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="DescriptionDO" type="TEXT" value="<%=DescriptionDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Order Quantity">Order Quantity:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="OrderQty" type="TEXT" value="<%=OrderQty%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Order Quantity">Order Quantity:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="OrderQtyDO" type="TEXT" value="<%=OrderQtyDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
       <label class="control-label col-sm-2" for="UOM">UOM:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="UOM" type="TEXT" value="<%=UOM%>"	size="25" MAXLENGTH=100>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-4" for="UOM">UOM:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="UOMDO" type="TEXT" value="<%=UOMDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
   
    <div class="form-group">
      <label class="control-label col-sm-2" for="Container">Container:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	size="25" MAXLENGTH=100>
      </div>
       <!--<div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline"> -->     
       <input type = "Hidden" id = "DisplayContainer" name = "DisplayContainer" value = "DisplayContainer" 
		 <%if(DisplayContainer.equals("1")) {%>checked <%}%> /><!--Display Container</label>
      </div>
      </div>-->
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Container">Container:</label>
      <div class="col-sm-2">          
       <INPUT class="form-control" name="ContainerDO" type="TEXT" value="<%=ContainerDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
      <!--<div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline"> -->     
       <input type = "Hidden" id = "DisplayContainerDO" name = "DisplayContainerDO" value = "DisplayContainerDO" 
		 <%if(DisplayContainerDO.equals("1")) {%>checked <%}%> /><!--Display Container</label>
      </div>
      </div> -->
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Rate">Rate:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Rate" type="TEXT" value="<%=Rate%>"	size="25" MAXLENGTH=100>
      </div>
            
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Rate">Rate:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="RateDO" type="TEXT" value="<%=RateDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div> 

    <div class="form-group">
      <label class="control-label col-sm-2" for="Discount">Discount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Discount" type="TEXT" value="<%=Discount%>"	size="25" MAXLENGTH=100>
      </div>
            
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Discount">Discount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="DiscountDO" type="TEXT" value="<%=DiscountDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Net Amount">Net Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="NetRate" type="TEXT" value="<%=NetRate%>"	size="25" MAXLENGTH=100>
      </div>
            
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Net Amount">Net Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="NetRateDO" type="TEXT" value="<%=NetRateDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>   	
    
     <div class="form-group">
       <label class="control-label col-sm-2" for="Tax Amount">Tax Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="TaxAmount" type="TEXT" value="<%=TaxAmount%>"	size="25" MAXLENGTH=100>
      </div> 
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Tax Amount">Tax Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="TaxAmountDO" type="TEXT" value="<%=TaxAmountDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div> 
    
    <div class="form-group">
       <label class="control-label col-sm-2" for="Amount">Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Amt" type="TEXT" value="<%=Amt%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Amount">Amount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="AmtDO" type="TEXT" value="<%=AmtDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Total After Discount">Total After Discount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="TOTALAFTERDISCOUNT" type="TEXT" value="<%=totalafterdiscount%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Total After Discount">Total After Discount:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="TOTALAFTERDISCOUNTDO" type="TEXT" value="<%=totalafterdiscountDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>  
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Prepared By">Prepared By:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Prepared By">Prepared By:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="PreparedByDO" type="TEXT" value="<%=PreparedByDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Seller">Seller:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="Seller" type="TEXT" value="<%=Seller%>"	size="25" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Seller">Seller:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="SellerDO" type="TEXT" value="<%=SellerDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Seller Signature">Seller Authorized Signatory:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="SellerSignature" type="TEXT" value="<%=SellerSignature%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Seller Signature">Seller Authorized Signatory:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="SellerSignatureDO" type="TEXT" value="<%=SellerSignatureDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Buyer">Buyer:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="Buyer" type="TEXT" value="<%=Buyer%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Buyer">Buyer:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="BuyerDO" type="TEXT" value="<%=BuyerDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Buyer Signature">Buyer Authorized Signatory:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="BuyerSignature" type="TEXT" value="<%=BuyerSignature%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Buyer Signature">Buyer Authorized Signatory:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="BuyerSignatureDO" type="TEXT" value="<%=BuyerSignatureDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Date">Date:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyDate" type="TEXT" value="<%=CompanyDate%>" size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Date">Date:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyDateDO" type="TEXT" value="<%=CompanyDateDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>    
    
    <div class="form-group">
     <label class="control-label col-sm-2" for="Company Name">Company Name:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyName" type="TEXT" value="<%=CompanyName%>"	size="25" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-4" for="Company Name">Company Name:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyNameDO" type="TEXT" value="<%=CompanyNameDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
    </div>   
    
    <div class="form-group">
    <label class="control-label col-sm-2" for="Company Stamp">Company Stamp:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyStamp" type="TEXT" value="<%=CompanyStamp%>"	size="25" MAXLENGTH=100>
  		</div>
  		   
  		  <div class="form-inline">
          <label class="control-label col-sm-4" for="Company Stamp">Company Stamp:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanyStampDO" type="TEXT" value="<%=CompanyStampDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
      </div>
    
    <div class="form-group">
     <label class="control-label col-sm-2" for="Signature">Signature:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanySig" type="TEXT" value="<%=CompanySig%>" size="25" MAXLENGTH=100>
  		</div> 
  		<%-- <div class="col-sm-2">
       <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplaySignature" name = "DisplaySignature" value = "DisplaySignature" 
		 <%if(DisplaySignature.equals("1")) {%>checked <%}%> />Print With Sign Capture</label>
      </div> --%>
     <div class="form-inline">
     <label class="control-label col-sm-4" for="Signature">Signature:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CompanySigDO" type="TEXT" value="<%=CompanySigDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <%-- <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplaySignatureDO" name = "DisplaySignatureDO" value = "DisplaySignatureDO" 
		 <%if(DisplaySignatureDO.equals("1")) {%>checked <%}%> />Print With Sign Capture</label>
      </div> --%>
      </div>

	  
         <div class="form-group">
      <label class="control-label col-sm-2" for="Sub Total">Sub Total:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="SubTotal" type="TEXT" value="<%=SubTotal%>" size="25" MAXLENGTH=100>
  		        </div>
  		    <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Sub Total">Sub Total:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="SubTotalDO" type="TEXT" value="<%=SubTotalDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>   
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Total Tax">Total Tax:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Tax" type="TEXT" value="<%=Tax%>" size="25" MAXLENGTH=100>
  		        </div>
  		    <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Total Tax">Total Tax:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="TaxDO" type="TEXT" value="<%=TaxDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>   
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Total With Tax">Total:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Total" type="TEXT" value="<%=Total%>" size="25" MAXLENGTH=100>
  		        </div>
  		       <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Total With Tax">Total:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="TotalDO" type="TEXT" value="<%=TotalDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="ContactName" type="TEXT" value="<%=ContactName%>" size="25" MAXLENGTH=100>
  		        </div>
  		    <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="ContactNameDO" type="TEXT" value="<%=ContactNameDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Email">Email:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Email" type="TEXT" value="<%=Email%>" size="25" MAXLENGTH=100>
  		        </div>
  		    <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Email">Email:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="EmailDO" type="TEXT" value="<%=EmailDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Fax">Fax:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Fax" type="TEXT" value="<%=Fax%>" size="25" MAXLENGTH=100>
  		        </div>
  		      <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Fax">Fax:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="FaxDO" type="TEXT" value="<%=FaxDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Telephone">Telephone:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Telephone" type="TEXT" value="<%=Telephone%>" size="25" MAXLENGTH=100>
  		        </div>
  		       <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Telephone">Telephone:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="TelephoneDO" type="TEXT" value="<%=TelephoneDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Handphone">Handphone:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Handphone" type="TEXT" value="<%=Handphone%>" size="25" MAXLENGTH=100>
  		        </div>
  		       <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Handphone">Handphone:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="HandphoneDO" type="TEXT" value="<%=HandphoneDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Attention">Attention:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="Attention" type="TEXT" value="<%=Attention%>" size="25" MAXLENGTH=100>
  		        </div>
  		      <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Attention">Attention:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="AttentionDO" type="TEXT" value="<%=AttentionDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Qty Total">Qty Total:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="QtyTotal" type="TEXT" value="<%=QtyTotal%>" size="25" MAXLENGTH=100>
  		        </div>
  		     <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Qty Total">Qty Total:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="QtyTotalDO" type="TEXT" value="<%=QtyTotalDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
    </div>
    
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Customer Remarks">Customer Remarks:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="remark3" type="TEXT" value="<%=remark3%>" size="25" MAXLENGTH=100>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Customer Remarks">Customer Remarks:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="remark3DO" type="TEXT" value="<%=remark3DO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>
  		        </div>
  		        
  	<div class="form-group">
      <label class="control-label col-sm-2" for="Footer Page">Footer Page:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="FooterPage" type="TEXT" value="<%=FooterPage%>" size="25" MAXLENGTH=100>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Footer Page">Footer Page:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="FooterPageDO" type="TEXT" value="<%=FooterPageDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div>
  		        </div>
  		        
  		        <div class="form-group">
      <label class="control-label col-sm-2" for="Footer Of">Footer Of:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="FooterOf" type="TEXT" value="<%=FooterOf%>" size="25" MAXLENGTH=100>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Of">Footer Of:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="FooterOfDO" type="TEXT" value="<%=FooterOfDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
  		        </div>
  		        
  		        <div class="form-group">
      <label class="control-label col-sm-2" for="Cash Customer">Cash Customer:</label>
      <div class="col-sm-2">
      <INPUT class="form-control" name="CashCustomer" type="TEXT" value="<%=CashCustomer%>" size="25" MAXLENGTH=100>
  		        </div>
  		         <div class="form-inline">
  		        <label class="control-label col-sm-4" for="Cash Customer">Cash Customer:</label>
            <div class="col-sm-2">
    		<INPUT class="form-control" name="CashCustomerDO" type="TEXT" value="<%=CashCustomerDO%>" size="15" MAXLENGTH=100>
  		        </div>
    </div> 
  		        </div>
         
       
    <div class="form-group">
      <label class="control-label col-sm-2" for="Footer1">Footer1:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer1" type="TEXT" value="<%=Footer1%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer1.value+'&TYPE=Footer1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer1">Footer1:</label>
            <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer1DO" type="TEXT" value="<%=Footer1DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer1DO.value+'&TYPE=Footer1DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Footer2">Footer2:</label>
            <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer2" type="TEXT" value="<%=Footer2%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer2.value+'&TYPE=Footer2');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer2">Footer2:</label>
            <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer2DO" type="TEXT" value="<%=Footer2DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer2DO.value+'&TYPE=Footer2DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
    <div class="form-group">
       <label class="control-label col-sm-2" for="Footer3">Footer3:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer3" type="TEXT" value="<%=Footer3%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer3.value+'&TYPE=Footer3');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer3">Footer3:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer3DO" type="TEXT" value="<%=Footer3DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer3DO.value+'&TYPE=Footer3DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="Footer4">Footer4:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer4" type="TEXT" value="<%=Footer4%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer4.value+'&TYPE=Footer4');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer4">Footer4:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer4DO" type="TEXT" value="<%=Footer4DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer4DO.value+'&TYPE=Footer4DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     <div class="form-group">
       <label class="control-label col-sm-2" for="Footer5">Footer5:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer5" type="TEXT" value="<%=Footer5%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer5.value+'&TYPE=Footer5');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer5">Footer5:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer5DO" type="TEXT" value="<%=Footer5DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer5DO.value+'&TYPE=Footer5DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Footer6">Footer6:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer6" type="TEXT" value="<%=Footer6%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer6.value+'&TYPE=Footer6');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer6">Footer6:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer6DO" type="TEXT" value="<%=Footer6DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer6DO.value+'&TYPE=Footer6DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Footer7">Footer7:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer7" type="TEXT" value="<%=Footer7%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer7.value+'&TYPE=Footer7');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer7">Footer7:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer7DO" type="TEXT" value="<%=Footer7DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer7DO.value+'&TYPE=Footer7DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Footer8">Footer8:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer8" type="TEXT" value="<%=Footer8%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer8.value+'&TYPE=Footer8');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer8">Footer8:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer8DO" type="TEXT" value="<%=Footer8DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer8DO.value+'&TYPE=Footer8DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-2" for="Footer9">Footer9:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer9" type="TEXT" value="<%=Footer9%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer9.value+'&TYPE=Footer9');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
  		        <div class="form-inline">
  		        <label class="control-label col-sm-2" for="Footer9">Footer9:</label>
      <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="Footer9DO" type="TEXT" value="<%=Footer9DO%>" size="50" MAXLENGTH=200>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('footers_listDO.jsp?FOOTER='+form1.Footer9DO.value+'&TYPE=Footer9DO');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Footer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		        </div>
    </div>
    </div>
    
     
	   <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
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

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.:";
    document.getElementById('CustomerTaxLabel').innerHTML = "Customer "+ d +" No.:";
    //DO
    document.getElementById('TaxLabelDO').innerHTML = d +" No.:";
    document.getElementById('CustomerTaxLabelDO').innerHTML = "Customer "+ d +" No.:";	
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>