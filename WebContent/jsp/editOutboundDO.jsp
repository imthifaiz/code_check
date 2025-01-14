<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%

String title = "Edit Sales Order Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
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
            document.form1.OutboundOrderHeader.value="";
            document.form1.InvoiceOrderToHeader.value="";
            document.form1.FromHeader.value="";
            document.form1.Date.value="";
            document.form1.RefNo.value="";
            document.form1.OrderNo.value="";
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
            document.form1.DisplayByOrdertype.checked = false;
            document.form1.printBarcode.checked = false;
            document.form1.printLocStock.checked = false;
            document.form1.printDetailDesc.checked = false;
            document.form1.Container.value="";
            document.form1.DisplayContainer.checked = false;
            document.form1.printCustTerms.checked = false;
            document.form1.printCustRemarks.checked = false;
            document.form1.REMARK1.value="";
            document.form1.REMARK2.value="";
            document.form1.DeliveryDate.value="";
            document.form1.Employee.value="";
            document.form1.TermsDetails.value="";
            document.form1.ShipTo.value="";
            document.form1.CompanyDate.value="";
            document.form1.CompanyName.value="";
            document.form1.CompanyStamp.value="";
            document.form1.CompanySig.value="";
            document.form1.printEmployee.checked = false;
            document.form1.Orientation.value ="Landscape";
            document.form1.ShippingCost.value="";
    		document.form1.OrderDiscount.value="";
    		document.form1.INCOTERM.value="";
            document.form1.printincoterm.checked = false;
    		
    		document.form1.printwithhscode.checked = false;
    		document.form1.printwithcoo.checked = false;
    		document.form1.printwithremark1.checked = false;
    		document.form1.printwithremark2.checked = false;
    		document.form1.printwithproductremarks.checked = false;
    		document.form1.printwithbrand.checked = false;
    		document.form1.BRAND.value="";
    		document.form1.HSCODE.value="";
            document.form1.COO.value="";
            document.form1.RCBNO.value="";
            
            document.form1.CUSTOMERRCBNO.value="";
            document.form1.ProductRatesAre.value="";

        	document.form1.UENNO.value="";
    		document.form1.CUSTOMERUENNO.value="";
            
            document.form1.PreparedBy.value="";
            document.form1.Seller.value="";
            document.form1.SellerSignature.value="";
            document.form1.Buyer.value="";
            document.form1.BuyerSignature.value="";
            
            document.form1.printWithDeliveryDate.checked = false;
            document.form1.prdDeliveryDate.value="";
            document.form1.printWithPrdDeliveryDate.checked = false;

//          RESVI STARTS
            document.form1.Project.value="";
            document.form1.printWithproject.checked = false;
            document.form1.printWithUENNO.checked = false;
            document.form1.printWithCustomerUENNO.checked = false;
            document.form1.printwithcompanysig.checked = false;
	        document.form1.printwithcompanyseal.checked = false;
	        document.form1.TransportMode.value="";
	        document.form1.printwithtransportmode.checked = false;
	        document.form1.printwithshipingadd.checked = false;
//             RESVI ENDS
            
            document.form2.OutboundOrderHeaderDO.value="";
            document.form2.InvoiceOrderToHeaderDO.value="";
            document.form2.FromHeaderDO.value="";
            document.form2.DateDO.value="";
            document.form2.RefNoDO.value="";
            document.form2.OrderNoDO.value="";
            document.form2.SoNoDO.value="";
            document.form2.ItemDO.value="";
            document.form2.DescriptionDO.value="";
            document.form2.OrderQtyDO.value="";
            document.form2.UOMDO.value="";
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
            document.form2.ContainerDO.value="";
            document.form2.DisplayContainerDO.checked = false;
            document.form2.printCustRemarksDO.checked = false;
            document.form2.REMARK1DO.value="";
            document.form2.REMARK2DO.value="";
            document.form2.DeliveryDateDO.value="";
            document.form2.EmployeeDO.value="";
            document.form2.TermsDetailsDO.value="";
            document.form2.ShipToDO.value="";
            document.form2.CompanyDateDO.value="";
            document.form2.CompanyNameDO.value="";
            document.form2.CompanyStampDO.value="";
            document.form2.CompanySigDO.value="";
            document.form2.printEmployeeDO.checked = false;
            document.form2.printCustTermsDO.checked = false;
            document.form2.OrientationDO.value = "Landscape";
			document.form2.DisplaySignature.checked = false;
			document.form2.ShippingCostDO.value="";
			document.form2.OrderDiscountDO.value="";
			document.form2.INCOTERMDO.value="";
            document.form2.printincotermDO.checked = false;
			document.form2.BRANDDO.value="";
			document.form2.HSCODEDO.value="";
            document.form2.COODO.value="";
            document.form2.RCBNODO.value="";
            
            document.form2.CUSTOMERRCBNODO.value="";
            document.form2.ProductRatesAreDO.value="";

            document.form2.UENNODO.value="";
    		document.form2.CUSTOMERUENNODO.value="";
            
            document.form2.printhscodeDO.checked = false;
            document.form2.printcooDO.checked = false;
            document.form2.printwithproductremarksDO.checked = false;
            document.form2.printwithbrandDO.checked = false;
            document.form2.PreparedByDO.value="";
            document.form2.SellerDO.value="";
            document.form2.SellerSignatureDO.value="";
            document.form2.BuyerDO.value="";
            document.form2.BuyerSignatureDO.value="";
            
            document.form2.printWithDeliveryDateDO.checked = false;
            document.form2.prdDeliveryDateDO.value="";
            document.form2.printWithPrdDeliveryDateDO.checked = false;
            
            document.form2.GINODO.value="";
            document.form2.GINODATEDO.value="";
            document.form2.CAPTURESIGNATUREDO.checked = false;

//         2. RESVI STARTS
            document.form2.ProjectDO.value="";
            document.form2.printWithprojectDO.checked = false;
            document.form2.ISINVENTORYMINQTY.checked = false;
            document.form2.printWithUENNODO.checked = false;
            document.form2.printWithCustomerUENNODO.checked = false;
            document.form2.TransportModeDO.value="";
            document.form2.printwithtransportmodeDO.checked = false;
            document.form2.printwithshipingaddDO.checked = false;
//             RESVI ENDS
	}
	function onAdd(){
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_DO_RCPT_HDR";
   		document.form1.submit();
   	}
	function onAddDO(){
   		document.form2.action  =  "/track/SettingsServlet?Submit=EDIT_DO_RCPT_HDRDO";
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
	function headerContainer(){
		if(document.form1.DisplayContainer.checked)
		{
		document.form1.Container.readOnly = true;		
		document.getElementById('Container').getAttributeNode('class').value="inactivegry";
		}
		else{
			document.form1.Container.readOnly = false;
			document.getElementById('Container').getAttributeNode('class').value="";
		}
	}
	function headerContainerDO(){
		if(document.form2.DisplayContainer.checked)
		{
		document.form2.Container.readOnly = true;		
		document.getElementById('Container').getAttributeNode('class').value="inactivegry";
		}
		else{
			document.form2.Container.readOnly = false;
			document.getElementById('Container').getAttributeNode('class').value="";
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
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="",DisplayByOrderType = "",printBarcode="",printDetailDesc="",printCustTerms="",printCustRemarks="";
	String Description = "", OrderQty  = "", UOM = "",
	Footer1="",Footer2="",Footer3="",Footer4="",Footer5="",Footer6="",Footer7="",Footer8="",Footer9="",printwithhscode="",printwithproject="",printWithUENNO="",printWithCustomerUENNO="",printwithcoo="",printwithproductremarks="",printwithremark1="",printwithremark2="",
	Container="", DisplayContainer="",printLocStock="";
	String remark1="",remark2="",shipto="",project="",deliverydate="",Employee="",CompanyDate="",CompanyName="",brand="",hscode="",coo="",rcbno="",customerrcbno="",ProductRatesAre="",UenNo="",CusUenNO="",
	CompanyStamp="",CompanySig="",printEmployee="",orderdiscount="",shippingcost="",incoterm="",printincoterm="",printwithbrand="",PreparedBy="",Seller="",SellerSignature="",Buyer="",BuyerSignature="",
	printWithDeliveryDate="",prdDeliveryDate="",printwithcompanysig="",TransportMode="",printwithtransportmode="",printwithshipingadd="",printwithcompanyseal="",printWithPrdDeliveryDate="";
	
	String InboundOrderHeaderDO  = "", InvoiceOrderToHeaderDO  = "", FromHeaderDO  = "",DateDO="",OrderNoDO ="";
	String RefNoDO   = "", TermsDO  = "", TermsDetailsDO   = "",SoNoDO ="",ItemDO ="",DisplayByOrderTypeDO = "",printwithprojectDO="",printWithUENNODO="",printWithCustomerUENNODO="",printBarcodeDO="",printDetailDescDO="",printCustRemarksDO="",ISINVENTORYMINQTY="";
	String DescriptionDO = "", OrderQtyDO  = "", UOMDO = "",ContainerDO="", DisplayContainerDO="",printLocStockDO="",printhscodeDO="",printcooDO="";
	String remark1DO="",remark2DO="",shiptoDO="",projectDO="",deliverydateDO="",EmployeeDO="",CompanyDateDO="",brandDO="",hscodeDO="",cooDO="",rcbnoDO="",
	Footer1DO="",Footer2DO="",Footer3DO="",Footer4DO="",Footer5DO="",Footer6DO="",Footer7DO="",printwithtransportmodeDO="",printwithshipingaddDO="",Footer8DO="",Footer9DO="",printpackinglistDO="",printdeliverynoteDO="",printwithproductremarksDO="",
	CompanyNameDO="",CompanyStampDO="",CompanySigDO="", printEmployeeDO="",printCustTermsDO="",
	DisplaySignatureDO="",Orientation="",OrientationDO="",orderdiscountDO="",shippingcostDO="",TransportModeDO="",incotermDO="",printincotermDO="",printwithbrandDO="",customerrcbnoDO="",ProductRatesAreDO="",UennoDO="",CusUennoDO="",PreparedByDO="",SellerDO="",SellerSignatureDO="",BuyerDO="",BuyerSignatureDO="",
	printWithDeliveryDateDO="",prdDeliveryDateDO="",printWithPrdDeliveryDateDO="",GINODO="",GINODATEDO="",CAPTURESIGNATUREDO="";
	int FOOTER_SIZE=0;
	int FOOTER_SIZEDO=0;
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));

	DOUtil doUtil = new DOUtil();
	     Map m= doUtil.getDOReceiptHdrDetails(plant,"Outbound order");
         if(!m.isEmpty()){
        	 InboundOrderHeader= (String) m.get("HDR1");
             InvoiceOrderToHeader = (String) m.get("HDR2");
             FromHeader = (String) m.get("HDR3");
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
             DisplayByOrderType = (String)m.get("DISPLAYBYORDERTYPE");
             printBarcode = (String)m.get("PRINTBARCODE");
             printDetailDesc = (String)m.get("PRINTXTRADETAILS");
             Container = (String)m.get("Container");
             DisplayContainer = (String)m.get("DisplayContainer");
             printLocStock = (String)m.get("PrintLocStock");
             printCustTerms = (String)m.get("PRINTCUSTERMS");
             printCustRemarks= (String)m.get("PCUSREMARKS");
             remark1=(String)m.get("REMARK1");
             remark2=(String)m.get("REMARK2");
             deliverydate=(String)m.get("DELIVERYDATE");
             Employee=(String)m.get("EMPLOYEE");
             TermsDetails=(String)m.get("TERMSDETAILS");
             shipto=(String)m.get("SHIPTO");
             CompanyDate=(String)m.get("COMPANYDATE");
             CompanyName=(String)m.get("COMPANYNAME");
             CompanyStamp=(String)m.get("COMPANYSTAMP");
             CompanySig=(String)m.get("COMPANYSIG");
             printEmployee=(String)m.get("PRINTEMPLOYEE");
             Orientation=(String)m.get("PrintOrientation");
             orderdiscount=(String)m.get("ORDERDISCOUNT");
             shippingcost=(String)m.get("SHIPPINGCOST");
             incoterm=(String)m.get("INCOTERM");
             printincoterm=(String)m.get("PRINTINCOTERM");
             
             printwithhscode = (String)m.get("PRITNWITHHSCODE");
             printwithcoo = (String)m.get("PRITNWITHCOO");
             printwithremark1 = (String)m.get("PRINTWITHREMARK1");
             printwithremark2 = (String)m.get("PRINTWITHREMARK2");
             printwithproductremarks = (String)m.get("PRINTWITHPRODUCTREMARKS");
             printwithbrand = (String)m.get("PRINTWITHBRAND");
             
             brand=(String)m.get("BRAND");
             hscode=(String)m.get("HSCODE");
             coo=(String)m.get("COO");
             rcbno=(String)m.get("RCBNO");
             
             customerrcbno=(String)m.get("CUSTOMERRCBNO");
             ProductRatesAre=(String) m.get("PRODUCTRATESARE");
             
             UenNo=(String)m.get("UENNO");
             CusUenNO=(String)m.get("CUSTOMERUENNO");
             
             PreparedBy = (String) m.get("PREPAREDBY");
             Seller = (String) m.get("SELLER");
             SellerSignature = (String) m.get("SELLERSIGNATURE");
             Buyer = (String) m.get("BUYER");
             BuyerSignature = (String) m.get("BUYERSIGNATURE");
             
             printWithDeliveryDate = (String) m.get("PRINTWITHDELIVERYDATE");
             prdDeliveryDate = (String) m.get("PRODUCTDELIVERYDATE");
             printWithPrdDeliveryDate = (String) m.get("PRINTWITHPRODUCTDELIVERYDATE");
             
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
         
         Map mDO= doUtil.getDOReceiptHdrDetailsDO(plant,"Outbound order");
         if(!mDO.isEmpty()){
        	 InboundOrderHeaderDO= (String) mDO.get("HDR1");
             InvoiceOrderToHeaderDO = (String) mDO.get("HDR2");
             FromHeaderDO = (String) mDO.get("HDR3");
             DateDO = (String) mDO.get("DATE");
             OrderNoDO = (String) mDO.get("ORDERNO");
             RefNoDO = (String) mDO.get("REFNO");
             SoNoDO = (String) mDO.get("SONO");
             ItemDO = (String) mDO.get("ITEM");
             DescriptionDO = (String) mDO.get("DESCRIPTION");
             OrderQtyDO = (String) mDO.get("ORDERQTY");
             UOMDO = (String) mDO.get("UOM");
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
             //printBarcodeDO = (String)mDO.get("PRINTBARCODE");
             printDetailDescDO = (String)mDO.get("PRINTXTRADETAILS");
             ContainerDO = (String)mDO.get("Container");
             DisplayContainerDO = (String)mDO.get("DisplayContainer");
             //printLocStockDO = (String)mDO.get("PrintLocStock");
             printCustRemarksDO= (String)mDO.get("PCUSREMARKS");
             remark1DO=(String)mDO.get("REMARK1");
             remark2DO=(String)mDO.get("REMARK2");
             deliverydateDO=(String)mDO.get("DELIVERYDATE");
             EmployeeDO=(String)mDO.get("EMPLOYEE");
             TermsDetailsDO=(String)mDO.get("TERMSDETAILS");
             shiptoDO=(String)mDO.get("SHIPTO");
             CompanyDateDO=(String)mDO.get("COMPANYDATE");
             CompanyNameDO=(String)mDO.get("COMPANYNAME");
             CompanyStampDO=(String)mDO.get("COMPANYSTAMP");
             CompanySigDO=(String)mDO.get("COMPANYSIG");
             printEmployeeDO=(String)mDO.get("PRINTEMPLOYEE");
             printCustTermsDO = (String)mDO.get("PRINTCUSTERMS");
             DisplaySignatureDO = (String)mDO.get("DISPLAYSIGNATURE");
             OrientationDO = (String)mDO.get("PrintOrientation");
             orderdiscountDO=(String)mDO.get("ORDERDISCOUNT");
             shippingcostDO=(String)mDO.get("SHIPPINGCOST");
             incotermDO=(String)mDO.get("INCOTERM");
             printincotermDO=(String)mDO.get("PRINTINCOTERM");
             
             brandDO=(String)mDO.get("BRAND");
             hscodeDO=(String)mDO.get("HSCODE");
             cooDO=(String)mDO.get("COO");
             rcbnoDO=(String)mDO.get("RCBNO");
             printhscodeDO = (String)mDO.get("PRITNWITHHSCODE");
             printcooDO = (String)mDO.get("PRITNWITHCOO");
             printpackinglistDO = (String)mDO.get("PRINTPACKINGLIST");
             printdeliverynoteDO = (String)mDO.get("PRINTDELIVERYNOTE");
             printwithproductremarksDO = (String)mDO.get("PRINTWITHPRODUCTREMARKS");
             printwithbrandDO = (String)mDO.get("PRINTWITHBRAND");
             
             customerrcbnoDO=(String)mDO.get("CUSTOMERRCBNO");
             ProductRatesAreDO=(String) mDO.get("PRODUCTRATESARE");
             
             UennoDO=(String)mDO.get("UENNO");
             CusUennoDO=(String)mDO.get("CUSTOMERUENNO");
             
             PreparedByDO = (String) mDO.get("PREPAREDBY");
	         SellerDO = (String) mDO.get("SELLER");
	         SellerSignatureDO = (String) mDO.get("SELLERSIGNATURE");
	         BuyerDO = (String) mDO.get("BUYER");
	         BuyerSignatureDO = (String) mDO.get("BUYERSIGNATURE");
	         
	         printWithDeliveryDateDO = (String) mDO.get("PRINTWITHDELIVERYDATE");
	         prdDeliveryDateDO = (String) mDO.get("PRODUCTDELIVERYDATE");
	         printWithPrdDeliveryDateDO = (String) mDO.get("PRINTWITHPRODUCTDELIVERYDATE");
	         GINODO=(String)mDO.get("GINO");
	         GINODATEDO=(String)mDO.get("GINODATE");
	         CAPTURESIGNATUREDO=(String)mDO.get("CAPTURESIGNATURE");
	         
	     //  RESVI STARTS
	         projectDO=(String)mDO.get("PROJECT");
	         printwithprojectDO=(String)mDO.get("PRINTWITHPROJECT");
	         ISINVENTORYMINQTY=(String)mDO.get("ISINVENTORYMINQTY");
	         printWithUENNODO=(String)mDO.get("PRINTWITHUENNO");
	         printWithCustomerUENNODO=(String)mDO.get("PRINTWITHCUSTOMERUENNO");
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
		UOM =""; 
		Container="";
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
        printincoterm="";
        printwithproductremarks="";
        brand="";
        hscode="";
		coo="";
		rcbno="";
		printwithbrand="";
        
        customerrcbno="";
        ProductRatesAre="";
        printwithremark1="";
        printwithremark2="";
        
        
        UenNo="";
		CusUenNO="";
        
        PreparedBy="";
        Seller="";
	    SellerSignature="";
	    Buyer="";
	    BuyerSignature="";
	    printWithDeliveryDate="";
		prdDeliveryDate="";
		printWithPrdDeliveryDate="";
		
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
		ItemDO   = "";
		DescriptionDO ="";
		OrderQtyDO =""; 
		UOMDO =""; 
		ContainerDO="";
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
        printCustTermsDO="";
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
		customerrcbnoDO="";
		ProductRatesAreDO="";
        UennoDO="";
		CusUennoDO="";
		PreparedByDO="";
	    SellerDO="";
	    SellerSignatureDO="";
	    BuyerDO="";
	    BuyerSignatureDO="";
	    printWithDeliveryDateDO="";
		prdDeliveryDateDO="";
		printWithPrdDeliveryDateDO="";
		GINODO="";
		GINODATEDO="";
		CAPTURESIGNATUREDO="";
		
//      RESVI STARTS
		projectDO="";
		printwithprojectDO="";
		ISINVENTORYMINQTY="";
		printWithUENNODO="";
		printWithCustomerUENNODO="";
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
	 <input type="number" id="inpayment" name="inpayment" style="display: none;" value="0">
            <div class="tab">
      
              <button class="tablinks active" onclick="openPayment(event, 'payment')">Upon Creation</button>
 		 <button class="tablinks" onclick="openPayment(event, 'voucher')">Upon Pick and Issue</button>
		</div>
	
	<div id="payment" class="tabcontent active" style="display: block;">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Sales Order Printout</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
	  <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
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
        <INPUT id="OutboundOrderHeader" name="OutboundOrderHeader" type="TEXT" value="<%=InboundOrderHeader%>" <%if(DisplayByOrderType.equals("1")) {%>readonly class ="form-control inactivegry"<%} else{%> class = "form-control" <%}%>
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
      <div class="col-sm-4">
      <label class="checkbox-inline">      
     <input type = "checkbox" id = "printBarcode" name = "printBarcode" value = "printBarcode" 
			<%if(printBarcode.equals("1")) {%>checked <%}%> />Print With Barcode for Product ID</label>
      </div>
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
			<%if(printCustRemarks.equals("1")) {%>checked <%}%> />Print Customer Remarks </label>
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
      
      <!--      . RESVI STARTS  PROJECT-->
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
		<div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printLocStock" name = "printLocStock" value = "printLocStock" 
			<%if(printLocStock.equals("1")) {%>checked <%}%> />Print Loc with Stock on hand</label>
      </div>
      </div>
      <!-- imti -->
      </div>
       <div class="form-group">
 <label class="control-label col-sm-3" for="Order header">Order Date</label>
      
      <div class="col-sm-4">          
       <INPUT class="form-control" name="Date" type="TEXT" value="<%=Date%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
      
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
			<%if(printWithUENNO.equals("1")) {%>checked <%}%> />Print With UEN  </label>
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
      <label class="control-label col-sm-3" for="RCBNO" id="TaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RCBNO" type="TEXT" value="<%=rcbno%>"  placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
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
<!--     RESVI -->
      	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
      
          <div class="form-group">
      <label class="control-label col-sm-3" for="CUSTOMER RCBNO" id="CustomerTaxLabel"></label>
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
        <INPUT class="form-control" name="PreparedBy" type="TEXT" value="<%=PreparedBy%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
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
     
      </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="INCOTERM">INCOTERM</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="INCOTERM" type="TEXT" value="<%=incoterm%>"  placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
            <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printincoterm" name = "printincoterm" value = "printincoterm" 
			<%if(printincoterm.equals("1")) {%>checked <%}%> />Print With INCOTERM</label>
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
       <input type = "checkbox" id = "printwithproductremarks" name = "printwithproductremarks" value = "printwithproductremarks" 
			<%if(printwithproductremarks.equals("1")) {%>checked <%}%> />Print With Product Remarks </label>
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
       <INPUT class="form-control" name="Container" type="TEXT" value="<%=Container%>"	 placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
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
        </div>       
        <div class="tab-pane fade" id="remark">
        <br>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks">Remarks1</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>"  placeholder="Max 100 Characters" size="25" MAXLENGTH=100>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithremark1" name = "printwithremark1" value = "printwithremark1" 
			<%if(printwithremark1.equals("1")) {%>checked <%}%> />Print With Remark1 </label>
      </div>
      </div>
      </div>
            <div class="form-group">
      <label class="control-label col-sm-3" for="Remarks2">Remarks2</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="REMARK2" type="TEXT" value="<%=remark2%>"  placeholder="Max 100 Characters" size="25" MAXLENGTH=100>
      </div>
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printwithremark2" name = "printwithremark2" value = "printwithremark2" 
			<%if(printwithremark2.equals("1")) {%>checked <%}%> />Print With Remark2 </label>
      </div>
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
      <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	
      </div>
    </div>
      
		</form>
		</div>
		
	</div>	

<div id="voucher" class="tabcontent">
<ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Sales Order Printout</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Edit Sales Order Printout</h1>
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
       <INPUT id="OutboundOrderHeaderDO" name="OutboundOrderHeaderDO" type="TEXT" value="<%=InboundOrderHeaderDO%>" <%if(DisplayByOrderTypeDO.equals("1")) {%>readonly class ="form-control inactiveGry"<%} else{%> class = "form-control" <%}%>
			size="15" MAXLENGTH=20>
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
      <label class="control-label col-sm-3" for="Invoice order to header">Invoice To Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="InvoiceOrderToHeaderDO" type="TEXT" value="<%=InvoiceOrderToHeaderDO%>"
			 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div>
        <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printCustRemarksDO" name = "printCustRemarksDO" value = "printCustRemarksDO" 
			<%if(printCustRemarksDO.equals("1")) {%>checked <%}%> />Print Customer Remarks</label>
      </div>
      </div>  
        
    </div>
          <div class="form-group">
      <label class="control-label col-sm-3" for="from header">From Header</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="FromHeaderDO" type="TEXT" value="<%=FromHeaderDO%>"
			 placeholder="Max 30 Characters"size="15" MAXLENGTH=30>
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
      <%--      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynoteDO" name = "printdeliverynoteDO" value = "printdeliverynoteDO" 
			<%if(printdeliverynoteDO.equals("1")) {%>checked <%}%> />Print Delivery Note</label>
      </div>
      </div>
       --%>  
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
      <%--<div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printpackinglistDO" name = "printpackinglistDO" value = "printpackinglistDO" 
			<%if(printpackinglistDO.equals("1")) {%>checked <%}%> />Print With Packing List</label>
      </div>
      </div> --%>  
      
      
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

      <!--       RESVI STARTS PROJECTDO -->
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
      
       <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "ISINVENTORYMINQTY" name = "ISINVENTORYMINQTY" value = "ISINVENTORYMINQTY " 
			<%if(ISINVENTORYMINQTY.equals("1")) {%>checked <%}%> />Show Inventory Minimum Quantity Alert</label>
      </div>
      </div>  
      
      </div>
             
      <div class="form-group">
      <label class="control-label col-sm-3" for="Order header">Order Date</label>      
      <div class="col-sm-4">          
       <INPUT class="form-control" name="DateDO" type="TEXT" value="<%=DateDO%>"
			 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
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
      <label class="control-label col-sm-3" for="GINO">GINO</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINODO" type="TEXT" value="<%=GINODO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
      <input type = "checkbox" id = "printdeliverynoteDO" name = "printdeliverynoteDO" value = "printdeliverynoteDO" 
			<%if(printdeliverynoteDO.equals("1")) {%>checked <%}%> />Print With Delivery Note</label>
      </div>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="GINO DATE">GINO Date</label>      
      <div class="col-sm-4">
      <INPUT class="form-control" name="GINODATEDO" type="TEXT" value="<%=GINODATEDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>
      </div>
          <!-- resvi UeN no 15.03.2021 -->
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
      <%--  <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printWithUENNODO" name = "printWithUENNODO" value = "printWithUENNODO" 
			<%if(printWithUENNODO.equals("1")) {%>checked <%}%> />Print With UEN </label>
      </div>
      </div> --%>
      
      <div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
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
     	</div>
		</div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="RCBNO" id="TaxLabelDO"></label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="RCBNODO" type="TEXT" value="<%=rcbnoDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
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
      <div class="form-group">
      <label class="control-label col-sm-3" for="CUSTOMER RCBNO" id="CustomerTaxLabelDO"></label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CUSTOMERRCBNODO" type="TEXT" value="<%=customerrcbnoDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
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
      <INPUT class="form-control" name="PreparedByDO" type="TEXT" value="<%=PreparedByDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
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
            <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "printincotermDO" name = "printincotermDO" value = "printincotermDO" 
			<%if(printincotermDO.equals("1")) {%>checked <%}%> />Print With INCOTERM</label>
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
      <INPUT class="form-control" name="SoNoDO" type="TEXT" value="<%=SoNoDO%>" size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="ItemDO" type="TEXT" value="<%=ItemDO%>"	size="15" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Description">Description</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="DescriptionDO" type="TEXT" value="<%=DescriptionDO%>" size="15" MAXLENGTH=100>
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
      <label class="control-label col-sm-3" for="Description">Order Quantity</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="OrderQtyDO" type="TEXT" value="<%=OrderQtyDO%>"	size="15" MAXLENGTH=100>
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
       <INPUT class="form-control" name="ContainerDO" type="TEXT" value="<%=ContainerDO%>"	 placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
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
       <input type = "checkbox" id = "printwithbrandDO" name = "printwithbrandDO" value = "printwithbrandDO" 
			<%if(printwithbrandDO.equals("1")) {%>checked <%}%> />Print With Brand </label>
      </div>
      </div>
     
      </div>
        <div class="form-group">
      <label class="control-label col-sm-3" for="Hscode">HSCODE(DN/PL)</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="HSCODEDO" type="TEXT" value="<%=hscodeDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>  
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printhscodeDO" name = "printhscodeDO" value = "printhscodeDO" 
			<%if(printhscodeDO.equals("1")) {%>checked <%}%> />Sales Print With HSCODE</label>
      </div>
      </div>
    </div>
    
        <div class="form-group">
      <label class="control-label col-sm-3" for="Conutry Of Origin ">COO(DN/PL)</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="COODO" type="TEXT" value="<%=cooDO%>"  placeholder="Max 50 Characters" size="15" MAXLENGTH=50>
      </div>     
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printcooDO" name = "printcooDO" value = "printcooDO" 
			<%if(printcooDO.equals("1")) {%>checked <%}%> />Sales Print With COO</label>
      </div>
      </div>
      </div>    
        </div>    
 <div class="tab-pane fade" id="cal2">
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
      <INPUT class="form-control" name="SellerDO" type="TEXT" value="<%=SellerDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Seller Signature">Seller Authorized Signatory</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SellerSignatureDO" type="TEXT" value="<%=SellerSignatureDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer">Buyer</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BuyerDO" type="TEXT" value="<%=BuyerDO%>"	 placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-3" for="Buyer Signature">Buyer Authorized Signatory</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="BuyerSignatureDO" type="TEXT" value="<%=BuyerSignatureDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
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
      <INPUT class="form-control" name="CompanyStampDO" type="TEXT" value="<%=CompanyStampDO%>"	size="15"  placeholder="Max 30 Characters" MAXLENGTH=30>
      </div>
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "CAPTURESIGNATUREDO" name = "CAPTURESIGNATUREDO" value = "CAPTURESIGNATUREDO" 
		 <%if(CAPTURESIGNATUREDO.equals("1")) {%>checked <%}%> />Print With Digital Stamp</label>
      </div>
      </div>
    </div>

  		        <div class="form-group">
      <label class="control-label col-sm-3" for="Signature">Signature</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="CompanySigDO" type="TEXT" value="<%=CompanySigDO%>"  placeholder="Max 30 Characters" size="15" MAXLENGTH=30>
      </div>
       <%-- <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
       <input type = "checkbox" id = "DisplaySignature" name = "DisplaySignature" value = "DisplaySignature" 
		 <%if(DisplaySignatureDO.equals("1")) {%>checked <%}%> />Print With Digital Signature</label>
      </div>
      </div> --%>
      <div class="form-inline">
 		<div class="col-sm-4">
  		<label class="radio-inline">
				      	<input type="radio" name="DisplaySignature" type = "radio"  value="0"  id="None" <%if (DisplaySignatureDO.equalsIgnoreCase("0")) {%> checked <%}%> >None
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="DisplaySignature" type = "radio" value="1"  id = "CompanySign1" <%if (DisplaySignatureDO.equalsIgnoreCase("1")) {%> checked <%}%> >Print With Company Signature
				     	</label>
				     	<label class="radio-inline" style="padding: 10px;">
				      	<input type="radio" name="DisplaySignature" type = "radio" value="2"  id = "DigitalSig" <%if (DisplaySignatureDO.equalsIgnoreCase("2")) {%> checked <%}%> >Print With Digital Signature
				     	</label>
     	</div>
		</div>
     </div>   
        
        </div>
            <div class="tab-pane fade" id="footer2">
        <br>
     
    
    <div class="form-group">
        <TABLE id="footertblDO" width="100%"  style="border-spacing: 0px 8px;">
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
     <!--   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAddDO();">Save</button>&nbsp;&nbsp;
      	
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
</script>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


