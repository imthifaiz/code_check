<%@ page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.object.ToDet"%>
<%@page import="com.track.db.object.ToHdr"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.Set"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Consignment Order Detail";
	String rootURI = HttpUtils.getRootURI(request);
	session.setAttribute("RFLAG", "10");
	boolean isInternalRequest = !"".equals(StrUtils.fString(request.getParameter("INTERNAL_REQUEST")));
	String attachmentType = StrUtils.fString(request.getParameter("ATTACHMENT_TYPE"));
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	if("".equals(plant) && isInternalRequest)
	{
		plant= StrUtils.fString(request.getParameter("PLANT"));
	}
	String USERID = StrUtils.fString((String)session.getAttribute("LOGIN_USER"));
	if("".equals(USERID) && isInternalRequest)
	{
		USERID= StrUtils.fString(request.getParameter("LOGIN_USER"));
	}
	String systatus = StrUtils.fString((String)session.getAttribute("SYSTEMNOW"));
	if("".equals(systatus) && isInternalRequest)
	{
		systatus= StrUtils.fString(request.getParameter("SYSTEMNOW"));
	}
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	ToHdr toHdr = (ToHdr) request.getAttribute("ToHdr");
	Map plntMap = (Map) request.getAttribute("PLNTMAP");
	Map toHdrDetails = (Map) request.getAttribute("TOHDRDETAILS");
	Map toHdrDetailsdo = (Map) request.getAttribute("TOHDRDETAILSDO");
	List<ToDet> toDetDetails = (ArrayList<ToDet>) request.getAttribute("ToDetList");
	PlantMstUtil plantmstutil = new PlantMstUtil();

	ArrayList custDetails = (ArrayList) request.getAttribute("CUSTDETAILS");
	String to_RcbNo = (String) custDetails.get(26);
	String to_CompanyName = (String) custDetails.get(1);
	String to_BlockAddress = (String) custDetails.get(2) + " " + (String) custDetails.get(3);
	String to_RoadAddress = (String) custDetails.get(4) + " " + (String) custDetails.get(16);
	String to_State_Country = (String) custDetails.get(24) + " " + (String) custDetails.get(5);
	String to_State = (String) custDetails.get(24);
	String to_Country = (String) custDetails.get(5);
	String to_Zip = (String) custDetails.get(6);
	String to_Attention = (String) custDetails.get(9);
	String to_TelNo = (String) custDetails.get(11);
	String to_Fax = (String) custDetails.get(13);
	String to_Email = (String) custDetails.get(14);
	String to_Remarks = (String) custDetails.get(15);
	String customerregno = (String) custDetails.get(27); //imtiuen
	String ship_CompanyName = "", ship_BlockAddress = "", ship_RoadAddress = "", ship_State = "",
		ship_Country = "", ship_Zip = "", ship_Attention = "", ship_TelNo = "";
	//imti
	ArrayList shippingCustDetails = (ArrayList) request.getAttribute("SHIPPINGCUSTDETAILS");

	if (shippingCustDetails.size() > 0) {
		ship_CompanyName = (String) shippingCustDetails.get(1);
		ship_BlockAddress = (String) shippingCustDetails.get(2) + " " + (String) shippingCustDetails.get(3);
		ship_State = (String) shippingCustDetails.get(12);
		ship_Country = (String) shippingCustDetails.get(11);
		ship_Zip = (String) shippingCustDetails.get(13);
		ship_Attention = (String) shippingCustDetails.get(9);
		ship_TelNo = (String) shippingCustDetails.get(11);
		ship_RoadAddress = (String) shippingCustDetails.get(4) + " " + (String) shippingCustDetails.get(10);
	}

	String imagePath = (String) request.getAttribute("IMAGEPATH");
	String numberOfDecimal = (String) request.getAttribute("NUMBEROFDECIMAL");
	boolean displaySummaryNew = false, displaySummaryEdit = false, displayPrintPdf = false,
			displaySummaryLink = false, displayMore = false;
	if (systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryLink = ub.isCheckValAcc("summarylnkconsignment", plant, USERID);
		displaySummaryEdit = ub.isCheckValAcc("editconsignment", plant, USERID);
		displayPrintPdf = ub.isCheckValAcc("printconsignment", plant, USERID);
		displayMore = ub.isCheckValAcc("moreconsignment", plant, USERID);
	}
	if (systatus.equalsIgnoreCase("INVENTORY")) {
		displaySummaryLink = ub.isCheckValinv("summarylnkconsignment", plant, USERID);
		displaySummaryEdit = ub.isCheckValinv("editconsignment", plant, USERID);
		displayPrintPdf = ub.isCheckValinv("printconsignment", plant, USERID);
		displayMore = ub.isCheckValinv("moreconsignment", plant, USERID);

	}

	String orderDesc = "", rcbno = "", Attention = "", Telephone = "", TELEPHONE = "", FaxLBL = "", Email = "",
			toHeader = "", fromHeader = "", customerRcbno = "", shipTo = "", terms = "", date = "", uenno = "",customeruenno = "",printuenno = "", printcustomeruenno = "",compsign = "",compseal = "",compname = "",compdate = "",
			fromlocation = "", tolocation = "", lblDeliveryDate = "", lblINCOTERM = "", lblEmployee = "",
			lblPreBy = "", lblSoNo = "", lblItem = "", lblOrderQty = "", lblUom = "", lblRate = "",
			lblDiscount = "", lblAmt = "", lblSubTotal = "Sub Total", lblOrderdiscount = "",
			lblTotalDiscount = "", lblShippingcost = "", lblTotal = "", printWithBrand = "",
			printWithHscode = "", printWithCoo = "", printWithProductDeliveryDate = "",
			printWithProductRemarks = "", prdXtraDetails = "", lblHscode = "", lblCoo = "",
			lblProductDeliveryDate = "", baldue = "", displayByOrderType = "",PRINTBARCODE="0",PRINTLOCSTOCK="0";
	
	String doorderDesc = "", rcbnodo = "", Attentiondo = "", Telephonedo = "", TELEPHONEdo = "", FaxLBLdo = "", Emaildo = "",
			toHeaderdo = "", fromHeaderdo = "", customerRcbnodo = "", shipTodo = "", termsdo = "", datedo = "", uennodo = "", customeruennodo = "",printuennodo = "", printcustomeruennodo ="",compsigndo = "",compsealdo = "",compnamedo = "",compdatedo = "",
			fromlocationdo = "", tolocationdo = "", lblDeliveryDatedo = "", lblINCOTERMdo = "", lblEmployeedo = "",PRINTWITHCOMPANYSIGdo = "", PRINTWITHCOMPANYSEALdo = "",
			lblPreBydo = "", lblSoNodo = "", lblItemdo = "", lblOrderQtydo = "", lblUomdo = "", lblRatedo = "",
			lblDiscountdo = "", lblAmtdo = "", lblSubTotaldo = "Sub Total", lblOrderdiscountdo = "",
			lblTotalDiscountdo = "", lblShippingcostdo = "", lblTotaldo = "", printWithBranddo = "",
			printWithHscodedo = "", printWithCoodo = "", printWithProductDeliveryDatedo = "",
			printWithProductRemarksdo = "", prdXtraDetailsdo = "", lblHscodedo = "", lblCoodo = "",
			lblProductDeliveryDatedo = "", balduedo = "", displayByOrderTypedo = "",Adjustmentdo="",orderdiscountdo="";

	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	if("".equals(curency) && isInternalRequest)
	{
		curency= StrUtils.fString(request.getParameter("BASE_CURRENCY"));
	}
	String ZIP = (String) plntMap.get("ZIP");
	String RCBNO = (String) plntMap.get("RCBNO");
	String NAME = (String) plntMap.get("NAME");
	String TELNO = (String) plntMap.get("TELNO");
	String FAX = (String) plntMap.get("FAX");
	String EMAIL = (String) plntMap.get("EMAIL");
	String companyregnumber = (String) plntMap.get("companyregnumber");
	String SEALNAME= "";
	String SIGNNAME= "";
	String PRINTWITHCOMPANYSEAL = "";
	String PRINTWITHCOMPANYSIG = "";
	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
    for (int i = 0; i < viewlistQry.size(); i++) {
        Map map = (Map) viewlistQry.get(i);
        SEALNAME=StrUtils.fString((String)map.get("SEALNAME"));
        SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
       
	}
    
    String sealPath = "", signPath = "";
    //imti get seal name from plantmst
    if(SEALNAME.equalsIgnoreCase("")){
    	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
    	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
    }
    if(SIGNNAME.equalsIgnoreCase("")){
    	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
       	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
    }
    //imti end
     
        String sealPathdo = "", signPathdo = "";
    //imti get seal name from plantmst
    if(SEALNAME.equalsIgnoreCase("")){
    	sealPathdo = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
    	sealPathdo = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
    }
    if(SIGNNAME.equalsIgnoreCase("")){
    	signPathdo = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
       	signPathdo = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
    }
    //imti end
    
//      String sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
//      String signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
//      String sealPathdo = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
//      String signPathdo = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
	
	// 	String balduest = StrUtils.addZeroes(baldue, numberOfDecimal);

	double shingpercentage = 0.0;
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	// 	Map toHdrDetails= new TOUtil().getTOReceiptHdrDetails(plant);
	if (!toHdrDetails.isEmpty()) {
		orderDesc = (String) toHdrDetails.get("HDR1");
		rcbno = (String) toHdrDetails.get("RCBNO");
		Telephone = (String) toHdrDetails.get("TELEPHONE");
		FaxLBL = (String) toHdrDetails.get("FAX");
		Email = (String) toHdrDetails.get("EMAIL");
		toHeader = (String) toHdrDetails.get("HDR2");
		fromHeader = (String) toHdrDetails.get("HDR3");
		customerRcbno = (String) toHdrDetails.get("CUSTOMERRCBNO");
		shipTo = (String) toHdrDetails.get("SHIPTO");
		terms = (String) toHdrDetails.get("TERMS");
		date = (String) toHdrDetails.get("DATE");
		
		uenno = (String) toHdrDetails.get("UENNO");
		customeruenno = (String) toHdrDetails.get("CUSTOMERUENNO");
		printuenno = (String)toHdrDetails.get("PRINTWITHUENNO");
        printcustomeruenno = (String)toHdrDetails.get("PRINTWITHCUSTOMERUENNO");
		
		fromlocation = (String) toHdrDetails.get("FROMWAREHOUSE");
		tolocation = (String) toHdrDetails.get("TOWAREHOUSE");

		lblDeliveryDate = (String) toHdrDetails.get("DELIVERYDATE");
		lblINCOTERM = (String) toHdrDetails.get("INCOTERM");
		lblEmployee = (String) toHdrDetails.get("EMPLOYEE");
		lblPreBy = (String) toHdrDetails.get("PREPAREDBY");
		lblSoNo = (String) toHdrDetails.get("SONO");
		lblItem = (String) toHdrDetails.get("ITEM");
		lblOrderQty = (String) toHdrDetails.get("ORDERQTY");
		lblUom = (String) toHdrDetails.get("UOM");
		lblRate = (String) toHdrDetails.get("RATE");
		lblDiscount = (String) toHdrDetails.get("DISCOUNT");
		lblAmt = (String) toHdrDetails.get("AMT");
		lblOrderdiscount = (String) toHdrDetails.get("ORDERDISCOUNT");
		lblShippingcost = (String) toHdrDetails.get("SHIPPINGCOST");
		lblTotalDiscount = (String) toHdrDetails.get("TOTALAFTERDISCOUNT");
		lblTotal = (String) toHdrDetails.get("TOTAL");
		lblHscode = (String) toHdrDetails.get("HSCODE");
		lblCoo = (String) toHdrDetails.get("COO");
		
		compsign = (String) toHdrDetails.get("COMPANYSIG");
		compseal = (String) toHdrDetails.get("COMPANYSTAMP");
		compname = (String) toHdrDetails.get("COMPANYNAME");
		compdate = (String) toHdrDetails.get("COMPANYDATE");
		PRINTWITHCOMPANYSEAL = (String)toHdrDetails.get("PRINTWITHCOMPANYSEAL");
		PRINTWITHCOMPANYSIG= (String) toHdrDetails.get("PRINTWITHCOMPANYSIG");
	    if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
	         sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	         }
			if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
				signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	         }

		prdXtraDetails = (String) toHdrDetails.get("PRINTXTRADETAILS");
		printWithBrand = (String) toHdrDetails.get("PRINTWITHBRAND");
		printWithHscode = (String) toHdrDetails.get("PRITNWITHHSCODE");
		printWithCoo = (String) toHdrDetails.get("PRITNWITHCOO");
		printWithProductDeliveryDate = (String) toHdrDetails.get("PRINTWITHPRODUCTDELIVERYDATE");
		printWithProductRemarks = (String) toHdrDetails.get("PRINTWITHPRODUCTREMARKS");

		PRINTBARCODE = (String) toHdrDetails.get("PRINTBARCODE");
		PRINTLOCSTOCK = (String) toHdrDetails.get("PRINTLOCSTOCK");
		
		displayByOrderType = (String) toHdrDetails.get("DISPLAYBYORDERTYPE");

	}
	
	if (!toHdrDetailsdo.isEmpty()) {
		doorderDesc = (String) toHdrDetailsdo.get("HDR1");
		rcbnodo = (String) toHdrDetailsdo.get("RCBNO");
		Telephonedo = (String) toHdrDetailsdo.get("TELEPHONE");
		FaxLBLdo = (String) toHdrDetailsdo.get("FAX");
		Emaildo = (String) toHdrDetailsdo.get("EMAIL");
		toHeaderdo = (String) toHdrDetailsdo.get("HDR2");
		fromHeaderdo = (String) toHdrDetailsdo.get("HDR3");
		customerRcbnodo = (String) toHdrDetailsdo.get("CUSTOMERRCBNO");
		shipTodo = (String) toHdrDetailsdo.get("SHIPTO");
		termsdo = (String) toHdrDetailsdo.get("TERMS");
		datedo = (String) toHdrDetailsdo.get("DATE");
		fromlocationdo = (String) toHdrDetailsdo.get("FROMWAREHOUSE");
		tolocationdo = (String) toHdrDetailsdo.get("TOWAREHOUSE");
		
		uennodo = (String) toHdrDetailsdo.get("UENNO");
		customeruennodo = (String) toHdrDetailsdo.get("CUSTOMERUENNO");
		printuennodo = (String)toHdrDetailsdo.get("PRINTWITHUENNO");
        printcustomeruennodo = (String)toHdrDetailsdo.get("PRINTWITHCUSTOMERUENNO");
        
        compsigndo = (String) toHdrDetailsdo.get("COMPANYSIG");
		compsealdo = (String) toHdrDetailsdo.get("COMPANYSTAMP");
		compnamedo = (String) toHdrDetailsdo.get("COMPANYNAME");
		compdatedo = (String) toHdrDetailsdo.get("COMPANYDATE");
		PRINTWITHCOMPANYSEALdo = (String)toHdrDetailsdo.get("PRINTWITHCOMPANYSEAL");
		PRINTWITHCOMPANYSIGdo= (String) toHdrDetailsdo.get("PRINTWITHCOMPANYSIG");
	    if(PRINTWITHCOMPANYSEALdo.equalsIgnoreCase("0")){
	         sealPathdo = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	         }
			if(PRINTWITHCOMPANYSIGdo.equalsIgnoreCase("0")){
				signPathdo = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	         }

		lblDeliveryDatedo = (String) toHdrDetailsdo.get("DELIVERYDATE");
		lblINCOTERMdo = (String) toHdrDetailsdo.get("INCOTERM");
		lblEmployeedo = (String) toHdrDetailsdo.get("EMPLOYEE");
		lblPreBydo = (String) toHdrDetailsdo.get("PREPAREDBY");
		lblSoNodo = (String) toHdrDetailsdo.get("SONO");
		lblItemdo = (String) toHdrDetailsdo.get("ITEM");
		lblOrderQtydo = (String) toHdrDetailsdo.get("ORDERQTY");
		lblUomdo = (String) toHdrDetailsdo.get("UOM");
		lblRatedo = (String) toHdrDetailsdo.get("RATE");
		lblDiscountdo = (String) toHdrDetailsdo.get("DISCOUNT");
		lblAmtdo = (String) toHdrDetailsdo.get("AMT");
		lblOrderdiscountdo = (String) toHdrDetailsdo.get("ORDERDISCOUNT");
		lblShippingcostdo = (String) toHdrDetailsdo.get("SHIPPINGCOST");
		lblTotalDiscountdo = (String) toHdrDetailsdo.get("TOTALAFTERDISCOUNT");
		lblTotaldo = (String) toHdrDetailsdo.get("TOTAL");
		lblHscodedo = (String) toHdrDetailsdo.get("HSCODE");
		lblCoodo = (String) toHdrDetailsdo.get("COO");
		Adjustmentdo = (String) toHdrDetailsdo.get("ADJUSTMENT");

		prdXtraDetailsdo = (String) toHdrDetailsdo.get("PRINTXTRADETAILS");
		printWithBranddo = (String) toHdrDetailsdo.get("PRINTWITHBRAND");
		printWithHscodedo = (String) toHdrDetailsdo.get("PRITNWITHHSCODE");
		printWithCoodo = (String) toHdrDetailsdo.get("PRITNWITHCOO");
		printWithProductDeliveryDatedo = (String) toHdrDetailsdo.get("PRINTWITHPRODUCTDELIVERYDATE");
		printWithProductRemarksdo = (String) toHdrDetailsdo.get("PRINTWITHPRODUCTREMARKS");
		displayByOrderTypedo = (String) toHdrDetailsdo.get("DISPLAYBYORDERTYPE");

	}
	double dTotalQty = 0, dTotalPickedQty = 0, dTotalIssuedQty = 0, totalTax = 0, subTotal = 0,dTotalInvoiceQty=0;
	List ItemList = (ArrayList) request.getAttribute("ItemList");
	List taxList = new ArrayList();

	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(toHdr.getTAXID());

	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject = new FinProject();
	String projectname = "";
	if (toHdr.getPROJECTID() > 0) {
		finProject = finProjectDAO.getFinProjectById(plant, toHdr.getPROJECTID());
		projectname = finProject.getPROJECT_NAME();
	}
	
	String empname = new EmployeeDAO().getEmpname(plant, toHdr.getEMPNO(), "");
	String ORDER_STATUS = toHdr.getORDER_STATUS();
	String STATUS = toHdr.getSTATUS();
     if (ORDER_STATUS.equalsIgnoreCase("Open")){ 
       if(STATUS.equalsIgnoreCase("O")){
    		 ORDER_STATUS="PARTIALLY PROCESSED";
    	 }
       else if(STATUS.equalsIgnoreCase("N")){
       ORDER_STATUS="OPEN";
    	 }
       else if(STATUS.equalsIgnoreCase("C")){
	       ORDER_STATUS="PROCESSED";
       	}
     }
	if (!isInternalRequest){
%>
<%@include file="sessionCheck.jsp" %>
<%
	}
%>
<!-- PDF Print Start 1 -->
<jsp:include page="header2.jsp" flush="true">
	<jsp:param value="<%=title%>" name="title" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>" />
	<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT%>" />
</jsp:include>
<script src="<%=rootURI%>/jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<script src="<%=rootURI%>/jsp/js/calendar.js"></script>
<script src="<%=rootURI%>/jsp/js/general.js"></script>
<script src="<%=rootURI%>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI%>/jsp/js/jspdf.plugin.autotable.js"></script>
<script src="<%=rootURI%>/jsp/js/JsBarcode.all.js"></script>
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td {
	border: none;
	padding: 0px;
}

#table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}

.text-ellipsis {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.ribbon {
	position: absolute !important;
	top: -5px;
	left: -5px;
	overflow: hidden;
	width: 96px;
	height: 94px;
}

.ribbon .ribbon-draft {
	background-color: #94a5a6;
	border-color: #788e8f;
}

.ribbon .ribbon-inner {
	text-align: center;
	color: #fff;
	top: 24px;
	left: -31px;
	width: 135px;
	padding: 3px;
	position: relative;
	transform: rotate(-45deg);
}

.ribbon .ribbon-inner:before {
	left: 0;
	border-left: 2px solid transparent;
}

.ribbon .ribbon-inner:after {
	right: -2px;
	border-bottom: 3px solid transparent;
}

.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
	content: "";
	border-top: 5px solid transparent;
	border-left: 5px solid;
	border-left-color: inherit;
	border-right: 5px solid transparent;
	border-bottom: 5px solid;
	border-bottom-color: inherit;
	position: absolute;
	top: 20px;
	transform: rotate(-45deg);
}

.invoice-banner {
	margin-top: 15px;
	margin-bottom: 10px;
	font-size: 13px;
	background-color: #fdfae4;
	border: 1px solid #ede5ae;
	padding: 10px;
	overflow: visible;
}

#table2>tfoot>tr>td {
	border: none;
}

#footerTable>tbody>tr>td {
	border: none;
}

#footerTable {
	display: none;
}

.page-break-before {
	page-break-before: always;
}

@media print {
	/*  @page { margin: 0; } */
	body {
		margin: 1cm 1.6cm 1.6cm 1.6cm;
	}
	#footerTable {
		display: table !important;
	}
}
</style>
<!-- PDF Print End 1 -->
<div class="container-fluid m-t-20">
	<div class="alert alert-danger alert-dismissible"
		style="width: max-content; margin: 0 auto;" hidden>
		<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		<span id="err-msg"></span>
	</div>
	<div class="alert alert-success alert-dismissible"
		style="width: max-content; margin: 0 auto;" hidden>
		<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		<span id="success-msg"></span>
	</div>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><a href="../consignment/summary"><span class="underline-on-hover">Consignment Summary</span> </a></li>                       
                <li><label>Consignment Order Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<div class="btn-group" role="group">
				<%if("OPEN".equalsIgnoreCase(ORDER_STATUS) || "PARTIALLY PROCESSED".equalsIgnoreCase(ORDER_STATUS)){ %>
						<button type="button" class="btn btn-success" data-toggle="dropdown" 						
						onClick="{processConsignmentOrder()}">
						Consignment PICK &amp; ISSUE</button>
						<form method="POST" action="/track/TransferOrderServlet?Submit=MultipleView" id="frmTONO" style="display: none;">
							<input type="hidden" name="TONO" value="<%=toHdr.getTONO()%>" />
							<input type="hidden" name="RFLAG" value="10" />
							<input type="hidden" name="ENCRYPT_FLAG" value="1" />
						</form>
						<script>
							function processConsignmentOrder(){
								$('#frmTONO').submit();
							}
						</script>
				<%}%>
				</div>
			
				<div class="btn-group" role="group">
					<%
						if (displaySummaryEdit) {
					%>
						<%if (!toHdr.getORDER_STATUS().equalsIgnoreCase("FORCE CLOSE")){ %>
					<button type="button" class="btn btn-default" data-toggle="tooltip"
						data-placement="bottom" title="Edit">
						<a href="../consignment/edit?tono=<%=toHdr.getTONO()%>"><i
							class="fa fa-pencil" aria-hidden="true"></i></a>
					</button>
					<%
						}}
					%>

					<!-- 					<button type="button" class="btn btn-default" onclick="generate()" -->
					<!-- 					data-toggle="tooltip"  data-placement="bottom" title="PDF"> -->
					<!-- 						<i class="fa fa-file-pdf-o" aria-hidden="true"></i> -->
					<!-- 					</button> -->
					<!-- 					<button type="button" class="btn btn-default printMe"  -->
					<!-- 					 data-toggle="tooltip"  data-placement="bottom" title="Print"> -->
					<!-- 						<i class="fa fa-print" aria-hidden="true"></i> -->
					<!-- 					</button> -->

				</div>

				<div class="btn-group" role="group">
					<%
						if (displayPrintPdf) {
					%>
					<button type="button" class="btn btn-default"
						data-toggle="dropdown">
						Print <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
												<li id="" onClick="submitFormPrint('Print Transfer Order'); "><a href="#">Print Consignment Order </a></li>
												<li id="" onClick="submitFormPrint('Print Transfer Order With Price'); "><a href="#">Print Consignment Order With Price </a></li>
<!-- 						<li id="" onClick="generates()"><a href="#">Print -->
<!-- 								Consignment Order </a></li> -->
<!-- 						<li id="" onClick="generate()"><a href="#">Print -->
<!-- 								Consignment Order With Price</a></li> -->
						<li id="" onClick="submitForm('Export To Excel');"><a
							href="#">Export To Excel</a></li>
					</ul>
					<%
						}
					%>
				</div>
				<div class="btn-group" role="group">
					<%
						if (displayMore) {
					%>
					<button type="button" class="btn btn-default"
						data-toggle="dropdown">
						More <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id=""><a
							href="../consignment/copy?tono=<%=toHdr.getTONO()%>">Copy</a></li>
						<li id="so-delete"><a href="#">Delete</a></li>
						<%if (!toHdr.getORDER_STATUS().equalsIgnoreCase("FORCE CLOSE")){ %> 
						<%if(!toHdr.getORDER_STATUS().equalsIgnoreCase("INVOICED") && !toHdr.getORDER_STATUS().equalsIgnoreCase("Draft") && !toHdr.getSTATUS().equalsIgnoreCase("N")){%>
						<li id=""><a href="../consignment/convertToInvocie?tono=<%=toHdr.getTONO()%>">Convert To Invocie</a></li>
						<%} }%>
					</ul>
					<%
						}
					%>
				</div>
				&nbsp;
				<h1
					style="font-size: 18px; cursor: pointer; vertical-align: middle;"
					class="box-title pull-right"
					onclick="window.location.href='../consignment/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>


		<input type="hidden" id="remark1" name="remark1"
			value="<%=toHdr.getRemark1()%>" > <input type="hidden"
			id="remark3" name="remark3" value="<%=toHdr.getRemark3()%>" >
		<div
			style="position: relative; padding: 0px 20px; box-shadow: 0 0 6px #ccc;"
			id="print_id1">
			<div id="ember1308"
				class="ribbon text-ellipsis tooltip-container ember-view">
				<div class="ribbon-inner ribbon-draft"><%=ORDER_STATUS%></div>
			</div>
			<div style="height: 0.700000in;"></div>
			<!-- PDF Print Start 2 -->
			<div id="print_id">
				<div class="row">
					<div class="col-xs-6">
						<img src="<%=rootURI%>/ReadFileServlet/?fileLocation=<%=imagePath%>"
							style="width: 130.00px;" id="logo_content">
						<div style="font-size:24px;">
							<b><%=PLNTDESC%></b>
						</div>
						<span style="white-space: pre-wrap; word-wrap: break-word;font-size:14px;">
<%=fromAddress_BlockAddress.trim()%>
<%=fromAddress_RoadAddress.trim()%>
<%=fromAddress_Country.trim()%> <%=ZIP.trim()%>
<%if(printuenno.equals("1")){ %><%=uenno%> :<%=companyregnumber.trim()%><br><%} %><%=rcbno.trim()%> <%=RCBNO.trim()%>
Contact: <%=NAME.trim()%>
Tel: <%=TELNO.trim()%> Fax : <%=FAX.trim()%>
Email: <%=EMAIL.trim()%>
						</span>
					</div>
					<div class="col-xs-6 text-right">
						<%
							if (displayByOrderType.equals("1")) {
								System.out.println(toHdr.getORDERTYPE());
						%>
						<h1 id="headerpage"><%=toHdr.getORDERTYPE()%></h1>
						<%
							} else {
						%>
						<h1 id="headerpage"><%=doorderDesc%></h1>
						<%
							}
						%>
						<figure class="pull-right text-center">
							<img id="barCode" style="width: 215px; height: 65px;">
							<figcaption>
								#
								<%=toHdr.getTONO()%></figcaption>
						</figure>
						<br style="clear: both">
					</div>
				</div>
				<div class="row">
					<div class="col-xs-7">
						<span  style="font-size: 24px;"><b><%=toHeader%></b></span> <br> 
						<span  style="font-size: 14px;">
						<span><%=custDetails.get(1)%></span>
						<br> 
						<%if(printcustomeruenno.equals("1")){%>  							
						<span><%=customeruenno%> <%=custDetails.get(27)%></span><br><%}%>
						 <span><%=customerRcbno%> <%=custDetails.get(26)%></span>
						<br> <span><%=custDetails.get(2)%> <%=custDetails.get(3)%></span>
						<br>
						<!-- Block Address (Add1 + Add2) -->
						<span><%=custDetails.get(4)%> <%=custDetails.get(16)%></span> <br>
						<!-- Road Address (Add3 + Add4) -->
						<span><%=custDetails.get(5)%> <%=custDetails.get(24)%> <%=custDetails.get(6)%></span>
						<br>
						<!-- Country + State + Zip -->
						<span>Email: <%=custDetails.get(14)%></span> <br> <span>Remarks:
							<%=custDetails.get(15)%></span> <br>
						<%
							if (shippingCustDetails.size() > 0) {
						%>
						<br> <span><%=shipTo%></span> <br> <span><%=shippingCustDetails.get(1)%></span>
						<br> <span><%=shippingCustDetails.get(7)%></span> <br> <span><%=shippingCustDetails.get(8)%></span>
						<br> <span><%=shippingCustDetails.get(9)%> <%=shippingCustDetails.get(10)%></span>
						<br> <span><%=shippingCustDetails.get(11)%> <%=shippingCustDetails.get(12)%></span>
						<br> <span><%=shippingCustDetails.get(4)%> <%=shippingCustDetails.get(10)%>
							<%=shippingCustDetails.get(13)%></span> <br>
						<!-- Country + State + Zip -->
						<span>Attention: </span> <br> <span>Tel: <%=shippingCustDetails.get(3)%></span>
						<br> <span></span> <br>
						<%
							}
						%>
						<br> <span><%=termsdo%> <%=custDetails.get(19)%> <%=custDetails.get(20)%></span><br>
						<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
						<span>Project : <%=projectname%></span><br>
						<%} %>
						</span>
					</div>
					<div class="col-xs-5 text-right">
						<table id="table1" class="table pull-right">
							<tbody>
								<!-- imthilocation -->
								<tr>
									<td>From Location</td>
									<td><%=toHdr.getFROMWAREHOUSE()%></td>
								</tr>
								<tr>
									<td>To Location</td>
									<td><%=toHdr.getTOWAREHOUSE()%></td>
								</tr>

								<tr>
									<td><%=datedo%></td>
									<td><%=toHdr.getDELDATE()%></td>
								</tr>
								<%-- <%if(printWithDeliveryDate.equals("1") && ((String)invoiceHdr.get("DUE_DATE")).length() > 0){%> --%>
								<tr>
									<td><%=lblDeliveryDatedo%></td>
									<td><%=toHdr.getDELIVERYDATE()%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblINCOTERMdo%></td>
									<td><%=toHdr.getINCOTERMS()%></td>
								</tr>
								<%-- <%if(printEmployee.equals("1") && ((String)invoiceHdr.get("EMPNO")).length() > 0){%> --%>
								<tr>
									<td><%=lblEmployeedo%></td>
									<td><%=empname%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblPreBydo%></td>
									<td><%=toHdr.getCRBY()%></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div> <br>
				<div class="row">
					<div class="col-xs-12">
						<%
						if(!isInternalRequest || (isInternalRequest && "consignment".equalsIgnoreCase(attachmentType))){
						%>
						<table id="tables2" class="table">
							<thead>
								<tr>
									<td><%=lblSoNo%></td>
									<td><%=lblItem%></td>
									<td><%=lblOrderQty%></td>
									<td>Picked Qty</td>
									<td>Issued Qty</td>
									<td>Invoiced Qty</td>
									<td><%=lblUom%></td>
								</tr>
							</thead>
							<tbody>
								<%
									int k = 0;

									for (ToDet toDet : toDetDetails) {
										Map m = (Map) ItemList.get(k);
										String qty = "", unitPrice = "", amount = "", percentage = "", tax = "", item_discounttype = "",
												lineDiscount = "", pickedQty = "", issuedQty = "", cost = "",AvailableIn="", invoiceQty = "";
										double discount = 0, dTax = 0;
										item_discounttype = toDet.getDISCOUNT_TYPE();
										qty = toDet.getQTYOR().toString();
										double dQty = "".equals(qty) ? 0.0d : Double.parseDouble(qty);
										qty = Numbers.toMillionFormat(dQty, "3");
										//dTotalQty += dQty;

										pickedQty = toDet.getQtyPick().toString();
										double dpQty = "".equals(pickedQty) ? 0.0d : Double.parseDouble(pickedQty);
										pickedQty = Numbers.toMillionFormat(dpQty, "3");
										//dTotalPickedQty += dpQty;

										issuedQty = toDet.getQtyPick().toString();
										double diQty = "".equals(pickedQty) ? 0.0d : Double.parseDouble(issuedQty);
										issuedQty = Numbers.toMillionFormat(diQty, "3");
										//dTotalIssuedQty += diQty;
										
										invoiceQty = toDet.getQTYAC().toString();
										double dvQty = "".equals(invoiceQty) ? 0.0d : Double.parseDouble(invoiceQty);
										invoiceQty = Numbers.toMillionFormat(dvQty, "3");
										
										if (PRINTLOCSTOCK.equals("1")) {
										String queryv=" ISNULL(STUFF((SELECT +'/ ' +LOC+' - '+CONVERT(VARCHAR(20),CAST(ROUND(SUM(QTY), 3, 1) AS decimal)) AS [text()] FROM ["+plant+"_INVMST] B WHERE ["+plant+"_todet].ITEM=B.ITEM AND B.QTY > 0 AND LOC not like 'SHIPPINGAREA%' and LOC not like 'TEMP_TO%' GROUP BY B.ITEM,B.LOC FOR XML PATH('')),1,1,' '),'') AS LOCSTOCK ";
										Hashtable htv = new Hashtable();
										htv.put("PLANT", plant);
										htv.put("TONO", toDet.getTONO());
										htv.put("ITEM", toDet.getITEM());
										Map mv = new ToDetDAO().selectRow(queryv,htv);
										if(!mv.isEmpty())
										AvailableIn=(String)mv.get("LOCSTOCK");
										}
								%>
								<tr>
									<td class="text-center"><%=toDet.getTOLNNO()%></td>
									<td class="text-center"><%=toDet.getITEM()%> <%
 	if (printWithBrand.equals("1")) {
 %>
										<br />Brand: <%=m.get("brand")%> <%
 	}
 %> <%
 	if (printWithHscode.equals("1")) {
 %>
										<br /><%=lblHscode%> <%=m.get("hscode")%> <%
 	}
 %> <%
 	if (printWithCoo.equals("1")) {
 %>
										<br /><%=lblCoo%> <%=m.get("coo")%> <%
 	}
 %> <%
 	if (printWithProductDeliveryDate.equals("1")) {
 %>
										<%=lblProductDeliveryDate%> <%
 	}
 %></td>
									<td class="text-center"><%=qty%></td>
									<td class="text-center"><%=invoiceQty%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(toDet.getQtyPick().doubleValue(), "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(toDet.getQTYRC().doubleValue(), "3")%></td>
									<td class="text-center"><%=toDet.getUNITMO()%></td>
								</tr>
								<% if (PRINTLOCSTOCK.equals("1")) { %>
								<tr>
								<td class="text-center"colspan="9">Available In: <%=AvailableIn%></td>
								</tr>
								<% } %>
								<%
									k++;
									}
								%>
							</tbody>
						</table>
						<table id="tables3" class="table" >
							<tbody style="text-align: right;">
								<tr>
									<td></td>
									<td class="text-center">Qty Total</td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalPickedQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalIssuedQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalInvoiceQty, "3")%></td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<%
						}if(!isInternalRequest){
						%>
						<table id="tableb2" class="table">
							<thead>
								<tr>
									<td><%=lblSoNo%></td>
									<td>Barcode</td>									
									<td><%=lblItem%></td>
									<td><%=lblOrderQty%></td>									
									<td><%=lblUom%></td>
								</tr>
							</thead>
							<tbody>
								<%
									int j = 0;

									for (ToDet toDet : toDetDetails) {
										Map m = (Map) ItemList.get(j);
										String qty = "", unitPrice = "", amount = "", percentage = "", tax = "", item_discounttype = "",
												lineDiscount = "", pickedQty = "", issuedQty = "", cost = "",AvailableIn="";
										double discount = 0, dTax = 0;
										item_discounttype = toDet.getDISCOUNT_TYPE();
										qty = toDet.getQTYOR().toString();
										double dQty = "".equals(qty) ? 0.0d : Double.parseDouble(qty);
										qty = Numbers.toMillionFormat(dQty, "3");
										dTotalQty += dQty;
										if (PRINTLOCSTOCK.equals("1")) {
										String queryv=" ISNULL(STUFF((SELECT +'/ ' +LOC+' - '+CONVERT(VARCHAR(20),CAST(ROUND(SUM(QTY), 3, 1) AS decimal)) AS [text()] FROM ["+plant+"_INVMST] B WHERE ["+plant+"_todet].ITEM=B.ITEM AND B.QTY > 0 AND LOC not like 'SHIPPINGAREA%' and LOC not like 'TEMP_TO%' GROUP BY B.ITEM,B.LOC FOR XML PATH('')),1,1,' '),'') AS LOCSTOCK ";
										Hashtable htv = new Hashtable();
										htv.put("PLANT", plant);
										htv.put("TONO", toDet.getTONO());
										htv.put("ITEM", toDet.getITEM());
										Map mv = new ToDetDAO().selectRow(queryv,htv);
										if(!mv.isEmpty())
										AvailableIn=(String)mv.get("LOCSTOCK");
										}
								%>
								<tr>
									<td class="text-right"><%=toDet.getTOLNNO()%></td>
									<td class="text-left code"><a><%=toDet.getITEM()%></a></td>
									<td class="text-left"><%=toDet.getITEM()%> <%
 	if (printWithBrand.equals("1")) {
 %>
										<br />Brand: <%=m.get("brand")%> <%
 	}
 %> <%
 	if (printWithHscode.equals("1")) {
 %>
										<br /><%=lblHscode%> <%=m.get("hscode")%> <%
 	}
 %> <%
 	if (printWithCoo.equals("1")) {
 %>
										<br /><%=lblCoo%> <%=m.get("coo")%> <%
 	}
 %> <%
 	if (printWithProductDeliveryDate.equals("1")) {
 %>
										<%=lblProductDeliveryDate%> <%
 	}
 %></td>
									<td class="text-right"><%=qty%></td>									
									<td class="text-left"><%=toDet.getUNITMO()%></td>
								</tr>
								<% if (PRINTLOCSTOCK.equals("1")) { %>
								<tr>
								<td class="text-center"colspan="9">Available In: <%=AvailableIn%></td>
								</tr>
								<% } %>
								<%
									j++;
									}
								%>
							</tbody>
						</table>
						<table id="tableb3" class="table" >
							<tbody style="text-align: right;">
								<tr>
									<td></td>
									<td class="text-center">Qty Total</td>
									<td></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<table id="tablesigns" class="table" >
							<tbody style="text-align: right;">
								<tr>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=signPathdo%>" style="width: 130.00px;"  id="signimgs"></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>______________</td> 
									<td></td>
									<td></td>
									<td></td>
									<td>______________</td> 
									<td></td>
								</tr>
									<tr>
									<td><%=compsigndo%></td> 
									<td></td>
									<td></td>
									<td></td>
									<td><%=compnamedo%></td> 
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=sealPathdo%>" style="width: 130.00px;" id="sealimgs"></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td>______________</td> 
									<td></td>
									<td></td>
									<td></td>
									<td>______________</td> 
									<td></td>
								</tr>
									<tr>
									<td><%=compdatedo%></td> 
									<td></td>
									<td></td>
									<td></td>
									<td><%=compsealdo%></td> 
									<td></td>
								</tr>
							</tbody>
						</table>
						<%							
						}
						dTotalQty = 0;
						if (!isInternalRequest || (isInternalRequest && "invoice".equalsIgnoreCase(attachmentType))){
						%>
						<table id="table2" class="table">
							<thead>
								<tr>
									<td><%=lblSoNodo%></td>
									<td><%=lblItemdo%></td>
									<td><%=lblOrderQtydo%></td>
									<td>Picked Qty</td>
									<td>Issued Qty</td>
									<td>Invoiced Qty</td>
									<td><%=lblUomdo%></td>
									<td><%=lblRatedo%></td>
									<td><%=lblDiscountdo%></td>
									<td class="text-right"><%=lblAmtdo%></td>
								</tr>
							</thead>
							<tbody>
								<%
								
									int i = 0;
									String taxdisplay = "";
									for (ToDet toDet : toDetDetails) {
										Map m = (Map) ItemList.get(i);
										String qty = "", unitPrice = "", amount = "", percentage = "", tax = "", item_discounttype = "",
												lineDiscount = "", pickedQty = "", issuedQty = "", cost = "", invoiceQty = "";
										double discount = 0, dTax = 0;
										item_discounttype = toDet.getDISCOUNT_TYPE();
										qty = toDet.getQTYOR().toString();
										double dQty = "".equals(qty) ? 0.0d : Double.parseDouble(qty);
										qty = Numbers.toMillionFormat(dQty, "3");
										dTotalQty += dQty;

										pickedQty = toDet.getQtyPick().toString();
										double dpQty = "".equals(pickedQty) ? 0.0d : Double.parseDouble(pickedQty);
										pickedQty = Numbers.toMillionFormat(dpQty, "3");
										dTotalPickedQty += dpQty;

										issuedQty = toDet.getQTYRC().toString();
										double diQty = "".equals(issuedQty) ? 0.0d : Double.parseDouble(issuedQty);
										issuedQty = Numbers.toMillionFormat(diQty, "3");
										dTotalIssuedQty += diQty;
										
										invoiceQty = toDet.getQTYAC().toString();
										double dvQty = "".equals(invoiceQty) ? 0.0d : Double.parseDouble(invoiceQty);
										invoiceQty = Numbers.toMillionFormat(dvQty, "3");
										dTotalInvoiceQty += dvQty;

										double dCost = toDet.getUNITPRICE() * toDet.getCURRENCYUSEQT();
										unitPrice = Numbers.toMillionFormat(dCost, numberOfDecimal);
										double item_amount = (dCost * dQty);

										if (item_discounttype.equalsIgnoreCase("%")) {
											double dDiscount = toDet.getDISCOUNT();
											discount = ((item_amount) / 100) * dDiscount;
											lineDiscount = Numbers.toMillionFormat(dDiscount, "3");
										} else {
											discount = toDet.getDISCOUNT() * toDet.getCURRENCYUSEQT();
											lineDiscount = Numbers.toMillionFormat(discount, numberOfDecimal);
										}
										item_amount = item_amount - discount;

										taxdisplay = toDet.getTAX_TYPE();

										subTotal += item_amount;										
								%>
								<tr>
									<td class="text-center"><%=toDet.getTOLNNO()%></td>
									<td class="text-center"><%=toDet.getITEM()%> <%
 	if (printWithBrand.equals("1")) {
 %>
										<br />Brand: <%=m.get("brand")%> <%
 	}
 %> <%
 	if (printWithHscode.equals("1")) {
 %>
										<br /><%=lblHscodedo%> <%=m.get("hscode")%> <%
 	}
 %> <%
 	if (printWithCoo.equals("1")) {
 %>
										<br /><%=lblCoodo%> <%=m.get("coo")%> <%
 	}
 %> <%
 	if (printWithProductDeliveryDate.equals("1")) {
 %>
										<%=lblProductDeliveryDatedo%> <%
 	}
 %></td>
									<td class="text-center"><%=qty%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(toDet.getQtyPick().doubleValue(), "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(toDet.getQTYRC().doubleValue(), "3")%></td>
									<td class="text-center"><%=invoiceQty%></td>
									<td class="text-center"><%=toDet.getUNITMO()%></td>
									<td class="text-center"><%=unitPrice%></td>
									<td class="text-center"><%=lineDiscount%><%=item_discounttype%></td>
									<td class="text-right"><%=Numbers.toMillionFormat(item_amount, numberOfDecimal)%></td>
								</tr>
						<tr>
          					<td></td>
          					<td colspan="9">&emsp;&emsp;&emsp;<%=toDet.getItemDesc() %></td>
						</tr>
								<%
									i++;
									}

									if (toHdr.getITEM_RATES() == 1) {
										subTotal = (subTotal * 100) / (toHdr.getCONSIGNMENT_GST() + 100);
									}

									double dorderdiscountcost = 0;
									String orderdiscountcost = "0";
									if (toHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
										dorderdiscountcost = (subTotal / 100) * toHdr.getORDERDISCOUNT();
										orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
									} else {
										dorderdiscountcost = toHdr.getORDERDISCOUNT();
										orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
									}

									String discounttype = toHdr.getDISCOUNT_TYPE();
									String discount = "0";
									double pdiscount = 0;
									if (discounttype.equalsIgnoreCase("%")) {
										double dDiscount = toHdr.getDISCOUNT();
										pdiscount = (subTotal / 100) * dDiscount;
										discount = Numbers.toMillionFormat(pdiscount, numberOfDecimal);
									} else {
										double dDiscount = toHdr.getDISCOUNT();
										pdiscount = toHdr.getDISCOUNT();
										discount = Numbers.toMillionFormat(dDiscount, numberOfDecimal);
									}

									if (toHdr.getTAXID() != 0) {
										if (fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1) {

											totalTax = (subTotal / 100) * toHdr.getCONSIGNMENT_GST();

											if (toHdr.getISSHIPPINGTAX() == 1) {
												totalTax = totalTax + ((toHdr.getSHIPPINGCOST() / 100) * toHdr.getCONSIGNMENT_GST());
											}

											if (toHdr.getISORDERDISCOUNTTAX() == 1) {
												totalTax = totalTax - ((dorderdiscountcost / 100) * toHdr.getCONSIGNMENT_GST());
											}

											if (toHdr.getISDISCOUNTTAX() == 1) {
												totalTax = totalTax - ((pdiscount / 100) * toHdr.getCONSIGNMENT_GST());
											}
										}
									}

									double totalAmount = (subTotal + totalTax + toHdr.getADJUSTMENT() + toHdr.getSHIPPINGCOST())
											- (dorderdiscountcost + pdiscount);
								%>
							</tbody>
						</table>
						<table id="table3" class="table" style="margin-bottom: 0px;">
							<tbody style="text-align: right;">
								<tr>
									<td></td>
									<td class="text-center">Qty Total</td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalPickedQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalIssuedQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalInvoiceQty, "3")%></td>
									<td></td>
									<td></td>
									<td><%=lblSubTotal%></td>
									<td><%=Numbers.toMillionFormat(subTotal, numberOfDecimal)%></td>
								</tr>

								<%
									if (toHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
								%>
								<tr>
									<td colspan="9"><%=lblOrderdiscountdo%>(<%=Numbers.toMillionFormat(toHdr.getORDERDISCOUNT(), "3")%>%)
										<br> <%
 	if (toHdr.getISORDERDISCOUNTTAX() == 1) {
 %> (Tax Inclusive) <%
 	} else {
 %> (Tax Exclusive) <%
 	}
 %>
									</td>
									<td>(-) <%=orderdiscountcost%></td>
								</tr>
								<%
									} else {
								%>
								<tr>
								
									<td colspan="9"><%=lblOrderdiscountdo%>(<%=toHdr.getORDERDISCOUNTTYPE()%>)
										<br> <%
 	if (toHdr.getISORDERDISCOUNTTAX() == 1) {
 %> (Tax Inclusive) <%
 	} else {
 %> (Tax Exclusive) <%
 	}
 %>
									</td>
									<td>(-) <%=orderdiscountcost%></td>
								</tr>
								<%
									}
								%>

		<%-- 						<%
									if (toHdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
								%>
								<tr>
									<td colspan="6">Discount (<%=Numbers.toMillionFormat(toHdr.getDISCOUNT(), "3")%>%)
										<br> <%
 	if (toHdr.getISDISCOUNTTAX() == 1) {
 %> (Tax
										Inclusive) <%
 	} else {
 %> (Tax Exclusive) <%
 	}
 %>
									</td>
									<td>(-) <%=discount%></td>
								</tr>
								<%
									}
								%> --%>
								<tr>
									<td colspan="9"><%=lblShippingcostdo%> <br> <%
 	if (toHdr.getISSHIPPINGTAX() == 1) {
 %>
										(Tax Inclusive) <%
 	} else {
 %> (Tax Exclusive) <%
 	}
 %></td>
									<td><%=Numbers.toMillionFormat(toHdr.getSHIPPINGCOST(), numberOfDecimal)%></td>
								</tr>

								<%
									if (toHdr.getTAXID() != 0) {
										if (fintaxtype.getSHOWTAX() == 1) {
								%>
								<tr>
									<td colspan="9"><%=taxdisplay%></td>
									<td><%=Numbers.toMillionFormat(totalTax, numberOfDecimal)%></td>
								</tr>
								<%
									}
									}
								%>

								<tr>
<!-- 									<td colspan="6">Adjustment</td> -->
										<td colspan="9"><%=Adjustmentdo%></td>
									<td><%=Numbers.toMillionFormat(toHdr.getADJUSTMENT(), numberOfDecimal)%></td>
								</tr>
								<tr style="font-size: 14px;">
									<td colspan="9"><b><%=lblTotaldo%> (<%=toHdr.getCURRENCYID()%>)</b></td>
									<td><b><%=Numbers.toMillionFormat(totalAmount, numberOfDecimal)%></b></td>
								</tr>
								<%if(!curency.equalsIgnoreCase(toHdr.getCURRENCYID())) {%><!--  Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency -->
									<tr id="showtotalcur">
									<td colspan="9"><b><%=lblTotaldo%> (<%=curency%>)</b></td>
									<td><b><%=Numbers.toMillionFormat((totalAmount/toHdr.getCURRENCYUSEQT()), numberOfDecimal)%></b></td>
									</tr>
								<%} else {%>
								<tr>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
								</tr>
								<%}%>
							</tbody>
						</table>						
						<%}if(!isInternalRequest){%>
						<table id="tablesign" class="table" >
							<tbody style="text-align: right;">
								<tr>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 130.00px;"  id="signimg"></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>______________</td> 
									<td></td>
									<td></td>
									<td></td>
									<td>______________</td> 
									<td></td>
								</tr>
									<tr>
									<td><%=compsign%></td> 
									<td></td>
									<td></td>
									<td></td>
									<td><%=compname%></td> 
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=sealPath%>" style="width: 130.00px;" id="sealimg"></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td>______________</td> 
									<td></td>
									<td></td>
									<td></td>
									<td>______________</td> 
									<td></td>
								</tr>
									<tr>
									<td><%=compdate%></td> 
									<td></td>
									<td></td>
									<td></td>
									<td><%=compseal%></td> 
									<td></td>
								</tr>
							</tbody>
						</table>
						<%} %>
					</div>
				</div>
			</div>
			<!-- PDF Print End 2 -->
		</div>
	</div>
</div>
<div id="deleteinvoice" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-lg-2">
						<i> <svg version="1.1" id="Layer_1"
								xmlns="http://www.w3.org/2000/svg" x="0" y="0"
								viewBox="0 0 512 512" xml:space="preserve"
								class="icon icon-xxlg-md icon-attention-circle"
								style="fill: red">
			            <path
									d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path
									d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
						</i>
					</div>
					<div class="col-lg-10" style="padding-left: 2px">
						<p>Deleted Sales Order information cannot be retrieved. Are
							you sure about deleting ?</p>

						<div class="alert-actions btn-toolbar">
							<button class="btn btn-primary ember-view" id="cfmdelete"
								style="background: red;">Yes</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">No</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- PDF Print Start 3 -->
<script>
$(document).ready(function(){
		JsBarcode("#barCode", "<%=toHdr.getTONO()%>", {format: "CODE128",displayValue: false});	
		$("#tables2").hide();
		$("#tableb2").hide();
		$("#tableb3").hide();
		$("#tables3").hide();
		$("#tablesign").hide();
		$("#tablesigns").hide();
		
		$(".code > a").each(function() {
		    var thecode =  $(this).text();
		    //$(".code a").text();		    
		    $(this).append(
		      "<div class='thebars'><br /><img class='barcodes'></img></div>"
		    );
		    //$(".barcodes")
		    $(this).find('.barcodes').JsBarcode(thecode, {
		    	format: "CODE128",
		    	displayValue: false
		    });
		  });
		toDataURL($("#signimg").attr("src"),
				function(dataUrl) {
			setsignimg(dataUrl);
			  	},'image/jpeg');
		toDataURL($("#sealimg").attr("src"),
				function(dataUrl) {
			setsealimg(dataUrl);
			  	},'image/jpeg');
		toDataURL($("#signimgs").attr("src"),
				function(dataUrl) {
			setsignimgs(dataUrl);
			  	},'image/jpeg');
		toDataURL($("#sealimgs").attr("src"),
				function(dataUrl) {
			setsealimgs(dataUrl);
			  	},'image/jpeg');
		
		$("#so-delete").click(function() {
		  	var status = "<%=toHdr.getSTATUS()%>";			 
		  	var result="";
	  		if(status == "N") {
				$('#deleteinvoice').modal('show');			
			}else {
				$("#err-msg").html("Order already marked as 'PROCESSED or PARTIALLY PROCESSED' not allow to delete.");
				$(".alert-danger").show();
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
			}
		 });
		
		$("#cfmdelete").click(function(){
		    $.ajax({
		        type: 'POST',
		        url: "../consignment/delete?tono=<%=toHdr.getTONO()%>",
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	window.location.href="../consignment/summary?msg="+data.MESSAGE;
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
		});
	
	  $('.printMe').click(function(){
		  if($("#table2").height() > 370){
			  $("#table3").addClass("page-break-before");
		  }else{
			  $("#table3").removeClass("page-break-before");
		  }
		  document.getElementById('headerpage').innerHTML="<%=doorderDesc%>";
		     $("#print_id").print();
		     document.getElementById('headerpage').innerHTML="<%=doorderDesc%>";
		});
	});


	function submitFormPrint(actionvalue){
		window.open(
				"<%=rootURI%>/TransferOrderServlet?Submit="+actionvalue+"&TONO=<%=toHdr.getTONO()%>",
				  '_blank' // <- This is what makes it open in a new window.
				);
	}
	function submitForm(actionvalue)
	{
		window.open(
				"<%=rootURI%>/TransferOrderServlet?Submit="+actionvalue+"&TONO=<%=toHdr.getTONO()%>&DIRTYPE=CONSIGNMENT"
				);
	}
	function generate() {
		
		var img = toDataURL($("#logo_content").attr("src"),
				function(dataUrl) {
					generatePdf(dataUrl);
			  	},'image/jpeg');
		
		}
	function generates() {
		
		var img = toDataURL($("#logo_content").attr("src"),
				function(dataUrl) {
					generatesPdf(dataUrl);
			  	},'image/jpeg');
					
		}
	function toDataURL(src, callback, outputFormat) {
		  var img = new Image();
		  img.crossOrigin = 'Anonymous';
		  img.onload = function() {
		    var canvas = document.createElement('CANVAS');
		    var ctx = canvas.getContext('2d');
		    var dataURL;
		    canvas.height = this.naturalHeight;
		    canvas.width = this.naturalWidth;
		    ctx.drawImage(this, 0, 0);
		    dataURL = canvas.toDataURL(outputFormat);
		    callback(dataURL);
		  };
		  img.src = src;
		  if (img.complete || img.complete === undefined) {
		    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
		    img.src = src;
		  }
	}
	function generatePdf(dataUrl){
		
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
		var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
		/* Top Left */
		/*From Address*/
		doc.addImage(dataUrl, 'JPEG', 16, 12, 35,15);
		var rY=35;	/*right Y-axis*/
		doc.setFontSize(10);
		doc.setFontStyle("bold");
		doc.text('<%=PLNTDESC%>', 16, rY+2);

		doc.setFontSize(9);
		doc.setFontStyle("normal");
		doc.text('<%=fromAddress_BlockAddress.trim()%>', 16, rY+=10);

		doc.text('<%=fromAddress_RoadAddress.trim()%>', 16, rY+=4);

		doc.text('<%=fromAddress_Country.trim()%> <%=ZIP%>', 16, rY+=4);
		<%if(printuennodo.equals("1")){%>
		doc.text('<%=uennodo%> <%=companyregnumber.trim()%>', 16, rY+=4);
		<%} %>
		doc.text('<%=rcbno%> <%=RCBNO.trim()%>', 16, rY+=4);
		
		doc.text('Attention: <%=NAME.trim()%>', 16, rY+=4);
		
		doc.text('Tel: <%=TELNO%> Fax: <%=FAX.trim()%>', 16, rY+=4);
		
		doc.text('Email: <%=EMAIL.trim()%>', 16, rY+=4);
		
		/*To Address*/
		rY+=6;
		doc.text('<%=toHeader%>', 16,  rY+=4);
		var lY = rY;	/*left Y-axis*/
		<%if (to_CompanyName.trim().length() > 0) {%>
		doc.text('<%=to_CompanyName.trim()%>', 16, rY+=4);
		<%}
		
		if(printcustomeruennodo.equals("1")){%>
		doc.text('<%=customeruennodo.trim()%> <%=customerregno%>', 16, rY+=4);				
		<%} //imtiuen
		
			if (to_RcbNo.trim().length() > 0) {%>
		doc.text('<%=customerRcbno.trim()%> <%=to_RcbNo%>', 16, rY+=4);				
		<%}
			if (to_BlockAddress.trim().length() > 0) {%>
		doc.text('<%=to_BlockAddress.trim()%>', 16, rY+=4);				
		<%}
			if (to_RoadAddress.trim().length() > 0) {%>
		doc.text('<%=to_RoadAddress.trim()%>', 16, rY+=4);
		<%}
			if (to_State_Country.trim().length() > 0 || to_Zip.trim().length() > 0) {%>
		doc.text('<%=to_State_Country.trim()%> <%=to_Zip.trim()%>', 16, rY+=4);				
		<%}
			if (to_Email.trim().length() > 0) {%>
		doc.text('Email: <%=to_Email.trim()%>', 16, rY+=4);				
		<%}
			if (to_Remarks.trim().length() > 0) {%>
		doc.text('Remarks: <%=to_Remarks%>', 16, rY+=4);
		<%}%>

		/*Ship Address*/
		rY+=6;
		<%if (ship_CompanyName.trim().length() > 0) {%>
		doc.text('<%=shipTodo%>', 16,  rY+=4);
		doc.text('<%=ship_CompanyName.trim()%>', 16, rY+=4);
		<%}
			if (ship_BlockAddress.trim().length() > 0) {%>
		doc.text('<%=ship_BlockAddress.trim()%>', 16, rY+=4);				
		<%}
			if (ship_RoadAddress.trim().length() > 0) {%>
		doc.text('<%=ship_RoadAddress.trim()%>', 16, rY+=4);	
		<%}
			if (ship_State.trim().length() > 0 || ship_Country.trim().length() > 0 || ship_Zip.trim().length() > 0) {%>
		doc.text('<%=ship_State%> <%=ship_Country%> <%=ship_Zip%>', 16, rY+=4);				
		<%}
			if (ship_Attention.trim().length() > 0) {%>
		doc.text('Attention: <%=ship_Attention%>', 16, rY+=4);				
		<%}
			if (ship_TelNo.trim().length() > 0) {%>
		doc.text('Tel: <%=ship_TelNo%>', 16, rY+=4);
		<%}%>
		
		doc.text('<%=termsdo%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%>', 16, rY+=6);

<%-- 		<span><%=terms%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%></span><br> --%>
		/* **** */
		<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
		doc.text('Project : <%=projectname%>', 16, rY+=6);
		<%} %>
	/* Top Right */
	doc.setFontSize(27);
	doc.text('<%=doorderDesc%>', 195, 17, {align:'right'});
	
	
	const img = document.querySelector('img#barCode');
	doc.addImage(img.src, 'JPEG', 140, 20, 55,15);

	doc.setFontSize(10);
	//doc.setFontStyle("bold");
	doc.text('# <%=toHdr.getTONO()%>', 180, 37, {align:'right'});

	<%-- doc.setFontSize(12);
	doc.text('<%=lblOrderQty%>', 195, 46, {align:'right'}); --%>

 doc.setFontSize(10);
	doc.autoTable({
		html : '#table1',
		startY : lY,
		margin : {left : 122},
		styles : {cellPadding : 0, fontSize : 9},
		columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
		theme : 'plain'
	});
	/* **** */
			var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : rY+=10,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center',
						fontSize : 10
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ],
						fontSize : 10
					},
					footStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ],
						fontStyle : 'normal',
						halign : 'right'
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'}},
					didDrawPage : function(data) {
						doc.setFontStyle("normal");
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(10);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, 185,
								pageHeight - 10);
					},
					didParseCell : function(data) {						
						for(i=0;i<data.table.body.length;i++){
							if(data.table.body[i].cells[6] != undefined){
								data.table.body[i].cells[6].styles["halign"]="right";
							}
						}
					}
				});
				let finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					html : '#table3',
					startY : finalY,
					styles : {fontSize : 10},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'},2 : {halign : 'center'},3 : {halign : 'right'},4 : {halign : 'right'},5 : {halign : 'right'},6 : {halign : 'right'},7 : {halign : 'right'},8 : {halign : 'right'},9 : {halign : 'right'}},
					theme : 'plain',
					 didParseCell: function (data) {
					        var rows = data.table.body;
					        if (data.row.index === rows.length - 1) {
					            data.cell.text = "";
					        }
					    }
				});
				finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					html : '#tablesigns',
					//html : tblp3,
					startY : finalY, 
					styles : {fontSize : 10},
// 					styles : {cellPadding : 0, fontSize : 10},
					columnStyles : {0 : {halign : 'left'},1 : {halign : 'center'},2 : {halign : 'right'},3 : {halign : 'right'},4 : {halign : 'right'},5 : {halign : 'right'}},
					theme : 'plain',
					didDrawCell: function(data) {
					      if (data.column.index === 0 && data.cell.section === 'body') {
					          var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         if(img!=undefined){
					         var dim = data.cell.height - data.cell.padding('vertical');
					         var textPos = data.cell.textPos;
					         var yval = textPos.y-2;
					         doc.addImage(img.src, 14, yval, 35, 15);
					         }
					      }
					      else if (data.column.index === 4 && data.cell.section === 'body') {
					          var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         if(img!=undefined){
					         var dim = data.cell.height - data.cell.padding('horizontal');					         
					         var textPos = data.cell.textPos;
					         doc.addImage(img.src, 161, textPos.y, 22, 22);
					         }
					      }
					}
				});
// 				finalY = doc.previousAutoTable.finalY;
// 				doc.autoTable({
// 					html : '#footerTable',
// 					startY : finalY, 
// 					styles : {cellPadding : 0, fontSize : 10},
// 					//columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
// 					theme : 'plain'
// 				});
				finalY = doc.previousAutoTable.finalY;
				doc.setFontStyle("normal");
				var notes = $('#remark1').val();
				var remarks = $('#remark3').val();
				remarks = remarks+" "+notes;
				var lines  = doc.splitTextToSize(remarks, (210-15-15));	

				if(pageNumber < doc.internal.getNumberOfPages()){
					// Footer
					var str = "Page " + doc.internal.getNumberOfPages()
					// Total page number plugin only available in jspdf v1.0+
					if (typeof doc.putTotalPages === 'function') {
						str = str + " of " + totalPagesExp;
					}
					doc.setFontSize(10);
	
					// jsPDF 1.4+ uses getWidth, <1.4 uses .width
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height
							: pageSize.getHeight();
					doc.text(str, 185, pageHeight - 10);
				}
				
				if(pageNumber == doc.internal.getNumberOfPages()){
					// Footer
					doc.setFontSize(10);
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
		doc.save('ConsignmentWithPrice.pdf');
		}
//PDF for without price
	function generatesPdf(dataUrl){
		var PRINTBARCODE = '<%=PRINTBARCODE%>';
		var tblp="";
		var tblp3="";
			if(PRINTBARCODE=="1") {
				$("#tableb2").show();
				$("#tableb3").show();
				tblp='#tableb2';
				tblp3='#tableb3';
			}
			else{
				$("#tables2").show();
				$("#tables3").show();
				tblp='#tables2';
				tblp3='#tables3';
			}         		
			
    		
    		$("#table2").hide();
    		$("#table3").hide();
        
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
		var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
		/* Top Left */
		/*From Address*/
		doc.addImage(dataUrl, 'JPEG', 16, 12, 35,15);
		var rY=35;	/*right Y-axis*/
		doc.setFontSize(10);
		doc.setFontStyle("bold");
		doc.text('<%=PLNTDESC%>', 16, rY+2);

		doc.setFontSize(9);
		doc.setFontStyle("normal");
		doc.text('<%=fromAddress_BlockAddress.trim()%>', 16, rY+=10);

		doc.text('<%=fromAddress_RoadAddress.trim()%>', 16, rY+=4);

		doc.text('<%=fromAddress_Country.trim()%> <%=ZIP%>', 16, rY+=4);
		<%if(printuenno.equals("1")){%>
		doc.text('<%=uenno%> <%=companyregnumber.trim()%>', 16, rY+=4);	<%}%>
		
		doc.text('<%=rcbno%> <%=RCBNO.trim()%>', 16, rY+=4);
		
		doc.text('Attention: <%=NAME.trim()%>', 16, rY+=4);
		
		doc.text('Tel: <%=TELNO%> Fax: <%=FAX%>', 16, rY+=4);
		
		doc.text('Email: <%=EMAIL%>', 16, rY+=4);
		
		/*To Address*/
		rY+=6;
		doc.text('<%=toHeader%>', 16,  rY+=4);
		var lY = rY;	/*left Y-axis*/
		<%if (to_CompanyName.trim().length() > 0) {%>
		doc.text('<%=to_CompanyName.trim()%>', 16, rY+=4);
		<%}
		
		if(printcustomeruenno.equals("1")){%>
		doc.text('<%=customeruenno.trim()%> <%=customerregno%>', 16, rY+=4);				
		<%} //imtiuen
		
			if (to_RcbNo.trim().length() > 0) {%>
		doc.text('<%=customerRcbno.trim()%> <%=to_RcbNo.trim()%>', 16, rY+=4);				
		<%}
			if (to_BlockAddress.trim().length() > 0) {%>
		doc.text('<%=to_BlockAddress.trim()%>', 16, rY+=4);				
		<%}
			if (to_RoadAddress.trim().length() > 0) {%>
		doc.text('<%=to_RoadAddress.trim()%>', 16, rY+=4);
		<%}
			if (to_State_Country.trim().length() > 0 || to_Zip.trim().length() > 0) {%>
		doc.text('<%=to_State_Country.trim()%> <%=to_Zip.trim()%>', 16, rY+=4);				
		<%}
			if (to_Email.trim().length() > 0) {%>
		doc.text('<%=Email%> <%=to_Email%>', 16, rY+=4);				
		<%}
			if (to_Remarks.trim().length() > 0) {%>
		doc.text('Remarks: <%=to_Remarks%>', 16, rY+=4);
		<%}%>

		/*Ship Address*/
		rY+=6;
		<%if (ship_CompanyName.trim().length() > 0) {%>
		doc.text('<%=shipTo%>', 16,  rY+=4);
		doc.text('<%=ship_CompanyName.trim()%>', 16, rY+=4);
		<%}
			if (ship_BlockAddress.trim().length() > 0) {%>
		doc.text('<%=ship_BlockAddress.trim()%>', 16, rY+=4);				
		<%}
			if (ship_RoadAddress.trim().length() > 0) {%>
		doc.text('<%=ship_RoadAddress.trim()%>', 16, rY+=4);	
		<%}
			if (ship_State.trim().length() > 0 || ship_Country.trim().length() > 0 || ship_Zip.trim().length() > 0) {%>
		doc.text('<%=ship_State.trim()%> <%=ship_Country.trim()%> <%=ship_Zip.trim()%>', 16, rY+=4);				
		<%}
			if (ship_Attention.trim().length() > 0) {%>
		doc.text('Attention: <%=ship_Attention%>', 16, rY+=4);				
		<%}
			if (ship_TelNo.trim().length() > 0) {%>
		doc.text('Tel: <%=ship_TelNo%>', 16, rY+=4);
		<%}%>
		
		doc.text('<%=termsdo%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%>', 16, rY+=6);

<%-- 		<span><%=terms%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%></span><br> --%>
		/* **** */
		<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
		doc.text('Project : <%=projectname%>', 16, rY+=6);
		<%} %>
	/* Top Right */
	doc.setFontSize(27);
	doc.text('<%=orderDesc%>', 195, 17, {align:'right'});
	
	
	const img = document.querySelector('img#barCode');
	doc.addImage(img.src, 'JPEG', 140, 20, 55,15);

	doc.setFontSize(10);
	//doc.setFontStyle("bold");
	doc.text('# <%=toHdr.getTONO()%>', 180, 37, {align:'right'});

	<%-- doc.setFontSize(12);
	doc.text('<%=lblOrderQty%>', 195, 46, {align:'right'}); --%>

 doc.setFontSize(10);
	doc.autoTable({
		html : '#table1',
		startY : lY,
		margin : {left : 122},
		styles : {cellPadding : 0, fontSize : 9},
		columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
		theme : 'plain'
	});
	/* **** */
			var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					//html : '#tables2',
					html : tblp,
					startY : rY+=10,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center',
						fontSize : 10
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ],
						fontSize : 10
					},
					footStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ],
						fontStyle : 'normal',
						halign : 'right'
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'center'},1: {halign: 'left'},2: {halign: 'right'},3: {halign: 'right'},4: {halign: 'right'},5: {halign: 'right'},6: {halign: 'center'}},
					didDrawPage : function(data) {
						doc.setFontStyle("normal");
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(10);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, 185,
								pageHeight - 10);
					},
					didParseCell : function(data) {						
						for(i=0;i<data.table.body.length;i++){
							if(data.table.body[i].cells[6] != undefined){
								data.table.body[i].cells[6].styles["halign"]="right";
							}
						}
					},					
					didDrawCell: function(data) {
						<% if (PRINTBARCODE.equals("1")) { %>
					      if (data.column.index === 1 && data.cell.section === 'body') {
					         var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         var dim = data.cell.height - data.cell.padding('vertical');					         
					         var textPos = data.cell.textPos;
					         doc.addImage(img.src, textPos.x,  textPos.y, 35, dim);
					         //'JPEG', 16, 12, 35,15
					      }					    
						<% } %>
					}
				});
				let finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					//html : '#tables3',
					html : tblp3,
					startY : finalY, 
					styles : {fontSize : 10},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'center'},2 : {halign : 'right'},3 : {halign : 'right'},4 : {halign : 'right'},5 : {halign : 'right'},6 : {halign : 'center'}},
					theme : 'plain'
				});
				finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					html : '#tablesign',
					//html : tblp3,
					startY : finalY, 
					styles : {fontSize : 10},
// 					styles : {cellPadding : 0, fontSize : 10},
					columnStyles : {0 : {halign : 'left'},1 : {halign : 'center'},2 : {halign : 'right'},3 : {halign : 'right'},4 : {halign : 'right'},5 : {halign : 'right'}},
					theme : 'plain',
					didDrawCell: function(data) {
					      if (data.column.index === 0 && data.cell.section === 'body') {
					          var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         if(img!=undefined){
					         var dim = data.cell.height - data.cell.padding('vertical');					         
					         var textPos = data.cell.textPos;
					         var yval = textPos.y-2;
					         doc.addImage(img.src, 14, yval, 35, 15);
					         }
					      }
					      else if (data.column.index === 4 && data.cell.section === 'body') {
					          var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         if(img!=undefined){
					         var dim = data.cell.height - data.cell.padding('horizontal');					         
					         var textPos = data.cell.textPos;
					         doc.addImage(img.src, 161, textPos.y, 22, 22);
					         }
					      }
					}
				});
// 				finalY = doc.previousAutoTable.finalY;
// 				doc.autoTable({
// 					html : '#footerTable',
// 					startY : finalY, 
// 					styles : {cellPadding : 0, fontSize : 10},
// 					//columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
// 					theme : 'plain'
// 				});
				finalY = doc.previousAutoTable.finalY;
				doc.setFontStyle("normal");
				var notes = $('#remark1').val();
				var remarks = $('#remark3').val();
				remarks = remarks+" "+notes;
				var lines  = doc.splitTextToSize(remarks, (210-15-15));	

				if(pageNumber < doc.internal.getNumberOfPages()){
					// Footer
					var str = "Page " + doc.internal.getNumberOfPages()
					// Total page number plugin only available in jspdf v1.0+
					if (typeof doc.putTotalPages === 'function') {
						str = str + " of " + totalPagesExp;
					}
					doc.setFontSize(10);
	
					// jsPDF 1.4+ uses getWidth, <1.4 uses .width
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height
							: pageSize.getHeight();
					doc.text(str, 185, pageHeight - 10);
				}
				
				if(pageNumber == doc.internal.getNumberOfPages()){
					// Footer
					doc.setFontSize(10);
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
		doc.save('ConsignmentWithoutPrice.pdf');
// 		document.getElementById("table3").style.display = "block"; 
//      document.getElementById("tables3").style.display = "none";
//      document.getElementById("table2").style.display = "block"; 
//     	document.getElementById("tables2").style.display = "none";

    	$("#tables2").hide();
    	$("#tableb2").hide();
    	$("#tableb3").hide();
//     	$("#tablesign").hide();
		$("#tables3").hide();
		$("#table2").show();
		$("#table3").show();
		
		}
	function setsignimg(dataUrl)
	{
	$("#signimg").attr('src', dataUrl);
	}
	function setsealimg(dataUrl)
	{
	$("#sealimg").attr('src', dataUrl);
	}
	function setsignimgs(dataUrl)
	{
	$("#signimgs").attr('src', dataUrl);
	}
	function setsealimgs(dataUrl)
	{
	$("#sealimgs").attr('src', dataUrl);
	}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<!-- PDF Print End 3 -->
