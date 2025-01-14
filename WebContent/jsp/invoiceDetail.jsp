<%@page import="org.apache.commons.lang.NumberUtils"%>
<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@page import="com.track.constants.*"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="java.util.Set"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%@include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! 
	@SuppressWarnings({"rawtypes", "unchecked"}) 
%>
<%
	String rootURI = HttpUtils.getRootURI(request);
	String title = "Invoice Detail";
	boolean isInternalRequest = !"".equals(StrUtils.fString(request.getParameter("INTERNAL_REQUESET")));
	ItemMstDAO itemMstDAO = new ItemMstDAO();
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	if("".equals(plant) && isInternalRequest)
	{
		plant= StrUtils.fString(request.getParameter("PLANT"));
	}
	String systatus = StrUtils.fString((String) session.getAttribute("SYSTEMNOW"));
	if("".equals(systatus) && isInternalRequest)
	{
		systatus= StrUtils.fString(request.getParameter("SYSTEMNOW"));
	}
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String basecurrency = curency;
	if("".equals(curency) && isInternalRequest)
	{
		curency= StrUtils.fString(request.getParameter("BASE_CURRENCY"));
	}
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	if("".equals(username) && isInternalRequest)
	{
		username= StrUtils.fString(request.getParameter("LOGIN_USER"));
	}
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	String NOOFPAYMENT=StrUtils.fString((String) session.getAttribute("NOOFPAYMENT")); /* Resvi */
	if("".equals(NOOFPAYMENT) && isInternalRequest)
	{
		NOOFPAYMENT= StrUtils.fString(request.getParameter("NOOFPAYMENT"));
	}
	PlantMstUtil plantmstutil = new PlantMstUtil();
	String basecurency =curency;
	String CURRENCYUSEQT="1";
	String OrdValidNumber="";
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayApplyCredits=false,displayRecordPayments=false,displaySummaryMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editinvoice", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printwtinvoice", plant,username);
		displayApplyCredits = ub.isCheckValAcc("applycreditsinvoice", plant,username);
		displayRecordPayments = ub.isCheckValAcc("recordpaymentinvoice", plant,username);
		displaySummaryMore = ub.isCheckValAcc("moreinvoice", plant,username);	
	}
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	InvoiceUtil invoiceUtil = new InvoiceUtil();
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	//resvi starts
	InvoiceDAO invoiceDAO = new InvoiceDAO();
	String FROM_DATE = DateUtils.getDate();
	if (FROM_DATE.length() > 5)
		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
	
	String TO_DATE = DateUtils.getLastDayOfMonth();
	if (TO_DATE.length() > 5)
		TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
	
	int noordvalid =invoiceDAO.Paymentcount(plant,FROM_DATE,TO_DATE);
	if(!NOOFPAYMENT.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFPAYMENT);
		if(noordvalid>=convl)
		{
			OrdValidNumber=NOOFPAYMENT;
		}
	}
	//ends
	
	String VIEW_ORDERTYPE="";
	
	CustMstDAO custMstDAO = new CustMstDAO();
	CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String invoiceHdrId = StrUtils.fString(request.getParameter("INVOICE_HDR"));
	String cur = StrUtils.fString(request.getParameter("CUR")); // added by imthi
	Hashtable ht = new Hashtable();
	ht.put("ID", invoiceHdrId);
	ht.put("PLANT", plant);
	//List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
	List invoiceHdrList =  invoiceDAO.getConvInvoiceHdrById(ht);
	Map invoiceHdr=(Map)invoiceHdrList.get(0);
	ht.put("INVOICEHDRID", invoiceHdrId);
	//List invoiceDetList =  invoiceUtil.getInvoiceDetByHdrId(ht);
	List invoiceDetList =  invoiceDAO.getConvInvoiceDetByHdrId(ht);
	if(invoiceDetList.size()>0)
	{
		Map mcut=(Map)invoiceDetList.get(0);
		CURRENCYUSEQT = (String) mcut.get("CURRENCYUSEQT");
	}
	else
	{
		response.sendRedirect("../invoice/detail?result=No Data Found");
	}
	List taxList =new ArrayList();
	
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
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
	 String DISPLAYSIGNATURE = "";
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
	
	double shingpercentage =0.0;
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	String totalAmount= (String) invoiceHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	double dTotalQty = 0;
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) invoiceHdr.get("SUB_TOTAL");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = Numbers.toMillionFormat(dSubTotal, numberOfDecimal);	

	String adjustment = (String) invoiceHdr.get("ADJUSTMENT");
	double dAdjustment ="".equals(adjustment) ? 0.0d :  Double.parseDouble(adjustment);
	adjustment = Numbers.toMillionFormat(dAdjustment, numberOfDecimal);
	String custName = custMstDAO.getCustName(plant, (String) invoiceHdr.get("CUSTNO"));
	
	String orddisctype = (String) invoiceHdr.get("ORDERDISCOUNTTYPE");
	if(orddisctype.length() == 0){
		orddisctype = "%";
	}
	String disctype = (String) invoiceHdr.get("DISCOUNT_TYPE");
	int isodtax = Integer.valueOf((String) invoiceHdr.get("ISORDERDISCOUNTTAX"));
	int isdtax = Integer.valueOf((String) invoiceHdr.get("ISDISCOUNTTAX"));
	int isshiptax = Integer.valueOf((String) invoiceHdr.get("ISSHIPPINGTAX"));
	double ceqt = Double.valueOf((String) invoiceHdr.get("CURRENCYUSEQT"));
	int taxid = Integer.valueOf((String) invoiceHdr.get("TAXID"));
	String totalTax = (String) invoiceHdr.get("TAXAMOUNT");
	Double taxtotald= Double.valueOf(totalTax);
	totalTax = Numbers.toMillionFormat(taxtotald, numberOfDecimal);
	
	
	//imthi
	//june 29,2021
	//display transport mode in detail page and in pdf
	DOUtil doUtil = new DOUtil();
    Map invoice= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
	
	int printwithtransportmode = Integer.valueOf((String) invoice.get("PRINTWITHTRANSPORT_MODE"));
	String transmode = (String) invoice.get("TRANSPORT_MODE");
	int printwithproject = Integer.valueOf((String) invoice.get("PRINTWITHPROJECT"));
	String project=(String)invoice.get("PROJECT");
    //end
	
	String orderdiscount = (String) invoiceHdr.get("ORDER_DISCOUNT");
	double dorderdiscount ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
	String ordisc = Numbers.toMillionFormat(dorderdiscount, "3");
	double dorderdiscountcost = 0;
	String orderdiscountcost = "";
	if(orddisctype.equalsIgnoreCase("%")){
		dorderdiscountcost = (dSubTotal/100)*dorderdiscount;
		orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
	}else{
		dorderdiscountcost = dorderdiscount * ceqt;
		orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
	}
	
	
	
	String discounttype = (String) invoiceHdr.get("DISCOUNT_TYPE");
	String discount = "0";
	String discountpercent = (String) invoiceHdr.get("DISCOUNT");
	if(discounttype.equalsIgnoreCase("%")){
		discount = (String) invoiceHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		double pdiscount = ((dSubTotal-dorderdiscountcost)/100)*dDiscount;
		discount = Numbers.toMillionFormat(pdiscount, numberOfDecimal);
	}else{
		discount = (String) invoiceHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discount = Numbers.toMillionFormat(dDiscount, numberOfDecimal);
	}
	curency = (String) invoiceHdr.get("DISPLAY");
	
   VIEW_ORDERTYPE=(String)invoiceHdr.get("ORDERTYPE");
	
	String amoutcredit = "0";
	InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
	List advanceCridit = invpaymentdao.getInvoicePaymentDetails(plant, "ADVANCE", (String) invoiceHdr.get("CUSTNO"), (String) invoiceHdr.get("DONO"));
	
	List advanceCriditAMOUNT = invpaymentdao.getInvoicePaymentDetails(plant, "ADVANCE", (String) invoiceHdr.get("CUSTNO"), "0");
	
	InvoicePaymentDAO InvPaymentdao = new InvoicePaymentDAO();
	double InvoicePaymentAmt = InvPaymentdao.getbalacedue(plant, invoiceHdrId);
	InvoicePaymentAmt=(InvoicePaymentAmt*Float.parseFloat(CURRENCYUSEQT));
	double baldue=(Double.valueOf(totalAmount)-InvoicePaymentAmt);
	String balamtrp = StrUtils.addZeroes(baldue, numberOfDecimal);
	String balduest = Numbers.toMillionFormat(baldue, numberOfDecimal);
	balduest = balduest.replace("-", "");
	String conv_balduest = Numbers.toMillionFormat(((baldue) / Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
	 
	
	double paymentmade=InvPaymentdao.getbalacedue(plant, invoiceHdrId);
	double paymentMadeloc =paymentmade;
	paymentmade=(paymentmade*Float.parseFloat(CURRENCYUSEQT));
	String Spaymentmade = Numbers.toMillionFormat(paymentmade, numberOfDecimal);
	
	
	String showCreditBtn = "", showPaymentBtn = "";
	/* if(advanceCridit.size() <= 0){
		showCreditBtn = "style='display:none;'";
	} */
	
	if( ((String)invoiceHdr.get("BILL_STATUS")).equalsIgnoreCase("Paid")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
	}
	
	if( ((String)invoiceHdr.get("BILL_STATUS")).equalsIgnoreCase("Draft")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
	}
	
	if( ((String)invoiceHdr.get("BILL_STATUS")).equalsIgnoreCase("CANCELLED")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
	}
	
	double advancebalace = InvPaymentdao.getcreditamout(plant, (String) invoiceHdr.get("CUSTNO"));
	double orderbalance  = InvPaymentdao.getcreditamoutusingorderno(plant, (String) invoiceHdr.get("DONO"));
	double ovallbalace = advancebalace + orderbalance;
	String overallbalace = Numbers.toMillionFormat(ovallbalace, numberOfDecimal);
	
	String shipingcost = (String) invoiceHdr.get("SHIPPINGCOST");
	double dshipingcost ="".equals(shipingcost) ? 0.0d :  Double.parseDouble(shipingcost);
	shipingcost = Numbers.toMillionFormat(dshipingcost, numberOfDecimal);
	
	boolean displaySaveAsOpen=false, displaySaveAsDraft=true;
	displaySaveAsOpen = ub.isCheckVal("invoiceByOpen", plant,username);
	
	ArrayList arrCust = new CustUtil().getCustomerDetails((String) invoiceHdr.get("CUSTNO"),plant);
	String to_CompanyName = (String)arrCust.get(1);
	String to_RcbNo = (String) arrCust.get(23);
	String to_BlockAddress = (String)arrCust.get(2) + " " + (String)arrCust.get(3);
	String to_RoadAddress = (String)arrCust.get(4) + " " + (String)arrCust.get(14);
	String to_State = (String)arrCust.get(22);
	String to_Country = (String)arrCust.get(5);
	String to_Zip = (String)arrCust.get(6);
	String to_Attention = (String)arrCust.get(9);
	String to_TelNo = (String)arrCust.get(11);
	String to_Fax = (String)arrCust.get(13);
	String to_Email = (String)arrCust.get(14);
	String to_Remarks = (String)arrCust.get(15);
	String suenno = (String) arrCust.get(41);//imtiuen
	
	String shipId = (String) invoiceHdr.get("SHIPPINGID");
	String ship_CompanyName = "", ship_BlockAddress = "", ship_RoadAddress = "",
			ship_State = "", ship_Country = "", ship_Zip = "", ship_Attention = "",
			ship_TelNo = "";
	if(shipId.length()>0){
		ArrayList arrShipCust = new CustUtil().getCustomerDetails(shipId, plant);	
		ship_CompanyName = (String)arrShipCust.get(1);
		ship_BlockAddress = (String)arrShipCust.get(2) + " " + (String)arrShipCust.get(3);
		ship_RoadAddress = (String)arrShipCust.get(4) + " " + (String)arrShipCust.get(14);
		ship_State = (String)arrShipCust.get(22);
		ship_Country = (String)arrShipCust.get(5);
		ship_Zip = (String)arrShipCust.get(6);
		ship_Attention = (String)arrShipCust.get(9);
		ship_TelNo = (String)arrShipCust.get(11);
	}
	
	String InboundOrderHeader = "", InvoiceOrderToHeader = "", shipto = "", OrderNo = "", invoicedate = "",uenno = "", customeruenno = "", printuenno ="",printcustomeruenno ="",compsign = "",compname = "", compdate = "",compseal = "",
			rcbno = "", customerrcbno = "", hscode = "", coo = "", remark1 = "", orderdiscountlbl = "",
			Discount = "", shippingcost = "", incoterm = "", Terms = "", SoNo = "", Item = "",
			OrderQty = "", UOM = "", Rate = "", totalafterdiscount = "", PreparedBy = "", Seller = "",
			SellerSignature = "", Buyer = "", BuyerSignature = "",SubTotal = "",Attention = "",
			Telephone = "", FaxLBL = "", Email = "", Brand = "",TotalDiscount = "",
			Total = "", printDetailDesc = "", printwithbrand = "", printwithhscode ="",printEmployee="",
			printWithDeliveryDate = "", printwithProduct ="",remark3 = "",
			printwithcoo = "", Employee = "", duedate = "", Adjustment="",PaymentMade="",BalanceDue="";
			int PRINTWITHSHIPINGADD=0;
			int PRINTDEFAULT=0;
			int checkordertype=0;
			
	Map invHdrDetails= new DOUtil().getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
	if(!invHdrDetails.isEmpty()){
		InboundOrderHeader= (String) invHdrDetails.get("HDR1");
		PRINTDEFAULT= Integer.valueOf((String) invHdrDetails.get("ISPRINTDEFAULT"));
		InvoiceOrderToHeader = (String) invHdrDetails.get("HDR2");
		shipto = (String)invHdrDetails.get("SHIPTO");
		OrderNo = (String)invHdrDetails.get("ORDERNO");
		invoicedate = (String)invHdrDetails.get("INVOICEDATE");
		rcbno = (String)invHdrDetails.get("RCBNO");
		customerrcbno=(String)invHdrDetails.get("CUSTOMERRCBNO");
		
		uenno = (String)invHdrDetails.get("UENNO");
		customeruenno=(String)invHdrDetails.get("CUSTOMERUENNO");
		
		hscode=(String)invHdrDetails.get("HSCODE");
		coo=(String)invHdrDetails.get("COO");
		remark1=(String)invHdrDetails.get("REMARK1");
		orderdiscountlbl=(String)invHdrDetails.get("ORDERDISCOUNT");
		Discount = (String)invHdrDetails.get("DISCOUNT");
		shippingcost=(String)invHdrDetails.get("SHIPPINGCOST");
		incoterm=(String)invHdrDetails.get("INCOTERM");
		Terms = (String) invHdrDetails.get("TERMS");
		SoNo = (String) invHdrDetails.get("SONO");
		Item = (String) invHdrDetails.get("ITEM");
		OrderQty = (String) invHdrDetails.get("ORDERQTY");
		UOM = (String) invHdrDetails.get("UOM");
		Rate = (String) invHdrDetails.get("RATE");
		totalafterdiscount=(String)invHdrDetails.get("TOTALAFTERDISCOUNT");
		PreparedBy = (String) invHdrDetails.get("PREPAREDBY");
		Seller = (String) invHdrDetails.get("SELLER");
		SellerSignature = (String) invHdrDetails.get("SELLERSIGNATURE");
		Buyer = (String) invHdrDetails.get("BUYER");
        BuyerSignature = (String) invHdrDetails.get("BUYERSIGNATURE");
        SubTotal = (String) invHdrDetails.get("SUBTOTAL");
        Total = (String) invHdrDetails.get("TOTAL");
        Employee=(String)invHdrDetails.get("EMPLOYEE");
        duedate=(String)invHdrDetails.get("DELIVERYDATE");
        Adjustment = (String)invHdrDetails.get("ADJUSTMENT");
	    PaymentMade = (String)invHdrDetails.get("PAYMENTMADE");
	    BalanceDue = (String)invHdrDetails.get("BALANCEDUE");
	    Attention = (String) invHdrDetails.get("ATTENTION");
	    Telephone = (String) invHdrDetails.get("TELEPHONE");
	    FaxLBL = (String) invHdrDetails.get("FAX");
	    Email = (String) invHdrDetails.get("EMAIL");
	    Brand = (String) invHdrDetails.get("BRAND");
	    TotalDiscount = (String) invHdrDetails.get("TOTALDISCOUNT");
	    remark3 = (String) invHdrDetails.get("REMARK3");
	     checkordertype = Integer.valueOf((String) invHdrDetails.get("DISPLAYBYORDERTYPE"));
			
			compsign = (String) invHdrDetails.get("COMPANYSIG");
			compseal = (String) invHdrDetails.get("COMPANYSTAMP");
			compname = (String) invHdrDetails.get("COMPANYNAME");
			compdate = (String) invHdrDetails.get("COMPANYDATE");
			PRINTWITHCOMPANYSEAL = (String)invHdrDetails.get("PRINTWITHCOMPANYSEAL");
		    DISPLAYSIGNATURE= (String) invHdrDetails.get("PRINTWITHCOMPANYSIG");
		    if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		         sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		         }
				if(DISPLAYSIGNATURE.equalsIgnoreCase("0")){
					signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		         }
        
        printDetailDesc = (String)invHdrDetails.get("PRINTXTRADETAILS");
        printwithbrand = (String)invHdrDetails.get("PRINTWITHBRAND");
        printwithhscode = (String)invHdrDetails.get("PRITNWITHHSCODE");
        printwithcoo = (String)invHdrDetails.get("PRITNWITHCOO");
        printEmployee=(String)invHdrDetails.get("PRINTEMPLOYEE");
        printWithDeliveryDate = (String) invHdrDetails.get("PRINTWITHDELIVERYDATE");
        printwithProduct = (String)invHdrDetails.get("PRINTWITHPRODUCT");
        printuenno = (String)invHdrDetails.get("PRINTWITHUENNO");
        printcustomeruenno = (String)invHdrDetails.get("PRINTWITHCUSTOMERUENNO");
        PRINTWITHSHIPINGADD = Integer.valueOf((String)invHdrDetails.get("PRINTWITHSHIPINGADD"));
	}
	
	FinCountryTaxType  fintaxtype = new FinCountryTaxType();
	if(taxid != 0){
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	}
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(Integer.valueOf((String) invoiceHdr.get("PROJECTID")) > 0){
		finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String) invoiceHdr.get("PROJECTID")));
		projectname = finProject.getPROJECT_NAME();
	}

	TransportModeDAO transportmodedao = new TransportModeDAO();
	String transportmode="";
	int transportid = Integer.valueOf((String)invoiceHdr.get("TRANSPORTID"));
	if(transportid > 0){
		transportmode = transportmodedao.getTransportModeById(plant,transportid);
	}else{
		transportmode = "";
	}
	
	if (!isInternalRequest){
%>
<%@include file="sessionCheck.jsp" %>
<%
	}
%>
<!-- PDF Print Start 1 -->
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.INVOICE%>"/>
</jsp:include>
<script src="<%=rootURI %>/jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="<%=rootURI %>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI %>/jsp/css/accounting.css">
<script  src="<%=rootURI %>/jsp/js/general.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.plugin.autotable.js"></script>
<script src="<%=rootURI %>/jsp/js/JsBarcode.all.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/tabulator_bootstrap.min.css" />
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td{
	border: none;
	padding: 0px;
}
#table3>tbody>tr>td{
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
    position: absolute!important;
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
#footerTable{
	display:none;
}
.page-break-before {
page-break-before : always;
}
@media print {
 /*  @page { margin: 0; } */
  body { margin: 1cm 1.6cm 1.6cm 1.6cm; }
  #footerTable{
	display:table !important;
  }  
}
</style>
<!-- PDF Print End 1 -->
<div class="container-fluid m-t-20">
<%if(resultnew.equals("") || resultnew == null){}else{ %>
	  <div class="alert alert-danger alert-dismissible" style="width: max-content;margin:0 auto;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <%=resultnew %>
	  </div>
  <%} %>
  
  <%if(rsuccess.equals("") || resultnew == null){}else{ %>
	    <div class="alert alert-success alert-dismissible" style="width: max-content;margin:0 auto;">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <%=rsuccess %>
  </div>
  <%} %>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <%if(!cur.equals("")){ %>
                <li><a href="../salesorder/salessummarycustomer"><span class="underline-on-hover">Sales Invoice Summary (By Customer)</span> </a></li>                
                 <%} else { %>
                <li><a href="../invoice/summary"><span class="underline-on-hover">Invoice Summary</span> </a></li>                
                 <%} %>
                <li><label>Invoice Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->	
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryEdit) { 
					String editInvoiceUri;
					if(invoiceHdr.get("ISEXPENSE").equals("1")) {
						editInvoiceUri = "../expenses/converttoinvoice?TRANID=" + invoiceHdr.get("ID") + "&cmd=Edit";
					}else{ 		
						editInvoiceUri = "../invoice/edit?invnum=" + invoiceHdr.get("INVOICE") + "&DONO=" + invoiceHdr.get("DONO") + "&GINO=" + invoiceHdr.get("GINO") + "&CUST_CODE=" + invoiceHdr.get("CUSTNO") + "&EMPNO=" + invoiceHdr.get("EMPNO") + "&TRANID=" + invoiceHdr.get("ID") + "&cmd=Edit&invcusnum=" + custName;
					} %>
<%-- 					<a href="<%=editInvoiceUri%>"> 
				<button type="button" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					</a> --%>
					<button type="button" class="btn btn-default" onclick="window.location.href='<%=editInvoiceUri%>'" data-toggle="tooltip"  data-placement="bottom" title="Edit">
					<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					<%}%>
					
					<% if (displayPdfPrint) { %>
					<% if (COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { %>
					<button type="button" class="btn btn-default" onclick="generateKitchen()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<% } else {%>
<!-- 					<button type="button" class="btn btn-default" onclick="generate()" -->
<!-- 					data-toggle="tooltip"  data-placement="bottom" title="PDF"> -->
<!-- 						<i class="fa fa-file-pdf-o" aria-hidden="true"></i> -->
<!-- 					</button> -->
					<button type="button" class="btn btn-default" onclick="generateKitchen()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<!-- <button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button> -->
					<% } }%>
				</div>
				<% if (displayApplyCredits) { %>
				<%if(!invoiceHdr.get("BILL_STATUS").equals("Paid")){ %>
					<%if(ovallbalace > 0){ %>
						&nbsp;
						<div class="btn-group" role="group">
							<button type="button pull-right" class="btn btn-default" id="applycrd"
							<%=showCreditBtn%> data-toggle="modal" data-target="#applycredit" >Apply Credits</button>
						</div>
						&nbsp;
					<%} %>
				<%} %>
				<%}%>
			
				<div class="btn-group" role="group">
					<%if(invoiceHdr.get("BILL_STATUS").equals("Draft") && !invoiceHdr.get("ORIGIN").equals("manual")){ %>
						<%if(displaySaveAsOpen){ %>
						<%--
						<button type="button pull-right" class="btn btn-success " id="convertdrafttoopen">Convert To Open</button>
						 --%>
						<%} %>
					<%}else if(invoiceHdr.get("BILL_STATUS").equals("Draft") && invoiceHdr.get("INVOICE").toString().length() > 0 && invoiceHdr.get("GINO").toString().length() > 0 && invoiceHdr.get("DONO").toString().length() == 0){ %>
						<%--
						<button type="button pull-right" class="btn btn-success " id="convertdrafttoopen">Convert To Open</button>
						 --%>
					<%}else{ %>
						<%if(!invoiceHdr.get("BILL_STATUS").equals("Paid")){ %>
					       <% if (displayRecordPayments) { %>
							<button type="button pull-right" class="btn btn-success " <%=showPaymentBtn%> onclick="recordpayment()">Record Payment</button>
						    <%}%>
						<%} %>
					<%} %>
				</div>
				<%if(invoiceHdr.get("BILL_STATUS").equals("CANCELLED")){ %>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" onclick="convertToDraft()">Convert to Draft</button>
				</div>
				<%} %>
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  <% if (displaySummaryMore) { %>
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					   <li id="bill-copy"><a href="../invoice/copy?TRANID=<%=(String) invoiceHdr.get("ID")%>&DONO=<%=(String) invoiceHdr.get("DONO")%>&CUST_CODE=<%=(String) invoiceHdr.get("CUSTNO")%>&cmd=copy">Copy</a></li>
					    <li id="bill-cancel"><a href="#">Cancel</a></li>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					  </ul>
					  <%}%>
				</div>
				&nbsp;
				<!-- changes by imthi based on URL -->
				<%if(!cur.equals("")){ %>
					<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
						onclick="window.location.href='../salesorder/salessummarycustomer'">
						<i class="glyphicon glyphicon-remove"></i>
					</h1>
				<%} else { %>
					<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
						onclick="window.location.href='../invoice/summary'">
						<i class="glyphicon glyphicon-remove"></i>
					</h1>
				<%} %>
			</div>
		</div>
		<div class="invoice-banner clearfix"> 
			<div class="invoice-inner-banner">
				<span> <%=curency %><%=overallbalace%> Credits Available </span> 
			</div>
		</div>
		
		<input type="hidden" id="dnotes" name="dnotes" value="<%=invoiceHdr.get("NOTE")%>" >
		<input type="hidden" id="PRINTDEFAULT" name="PRINTDEFAULT" value="<%=PRINTDEFAULT%>" >
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
		<%
			if (!isInternalRequest){
		%>
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=invoiceHdr.get("BILL_STATUS")%></div>
		</div>
		<%
			}
		%>
		
		<div style="height: 0.700000in;"></div>
		<!-- PDF Print Start 2 -->
		<div id="print_id">
		<div class="row">
			<div class="col-xs-6">
				<img src="<%=rootURI %>/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
				<div style="font-size:24px;">
					<b><%=PLNTDESC%></b>
				</div>

<span style="white-space: pre-wrap; word-wrap: break-word;font-size:14px;">
<%=fromAddress_BlockAddress.trim()%>
<%=fromAddress_RoadAddress.trim()%>
<%=fromAddress_Country.trim()%> <%=ZIP.trim()%>
<%if(printuenno.equals("1")){ %><%=uenno%> :<%=companyregnumber.trim()%><br><%} %><%=rcbno.trim()%> <%=RCBNO.trim()%>
<%=Attention.trim()%> <%=NAME.trim()%>
<%=Telephone.trim()%> <%=TELNO.trim()%> <%=FaxLBL.trim()%> <%=FAX.trim()%>
<%=Email.trim()%> <%=EMAIL.trim()%>
</span>
			</div>

			<div class="col-xs-6 text-right">
			<%if(checkordertype == 1){%>
				<h1><%=VIEW_ORDERTYPE%></h1>
				 <%}else{ %>
				<h1 id="headerpage"><%=InboundOrderHeader%></h1>
				 <%} %>
				
				<figure class="pull-right text-center">
					<img id="barCode" style="width:215px;height:65px;">
					<figcaption># <%=invoiceHdr.get("INVOICE")%></figcaption>
				</figure>
				<br style="clear:both"> 
				<%if(invoiceHdr.get("DONO").equals("") || invoiceHdr.get("DONO").equals(null) ){ %>	
				<%}else{ %>
					<div style="clear:both">
					<span><%=OrderNo%></span>
					<p style="margin:0px;"><b># <%=invoiceHdr.get("DONO")%></b></p>
					</div>
				<%} %>
				<div style="clear:both">
					<span><%=BalanceDue%></span>
					<h3 style="margin:0px;"><%=curency%><%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal)) %></h3>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-7">
				<span style="font-size: 24px;"><b><%=InvoiceOrderToHeader%></b></span>	<br>	
				<span style="font-size: 14px;">
				<%if(to_CompanyName.trim().length()>0){%>
				<span><%=to_CompanyName%></span>	<br>	
				<%}if(printcustomeruenno.equals("1")){%>
				<span><%=customeruenno%> <%=suenno%></span>	<br>				
				<%}if(to_RcbNo.trim().length()>0){%>
				<span><%=customerrcbno%> <%=to_RcbNo%></span>	<br>				
				<%}if(to_BlockAddress.trim().length()>0){%>
				<span><%=to_BlockAddress%></span>	<br>					
				<%}if(to_RoadAddress.trim().length()>0){%>
				<span><%=to_RoadAddress%></span>	<br>	
				<%}if(to_State.trim().length()>0 || to_Country.trim().length()>0  || to_Zip.trim().length()>0 ){%>
				<span><%=to_State%> <%=to_Country%> <%=to_Zip%></span>	<br>		
				<%}if(to_Email.trim().length()>0){%>
				<span><%=Email%> <%=to_Email%></span>	<br>				
				<%}if(to_Remarks.trim().length()>0){%>
				<span>Remarks: <%=to_Remarks%></span>	<br>	
				<%}%>
				
				
			<%if(PRINTWITHSHIPINGADD==1){%>	
			<%if(shipId.length()>0){%>		
				<br>
				<span><%=shipto%></span>	<br>	
				<%if(ship_CompanyName.trim().length()>0){%>
				<span><%=ship_CompanyName%></span>	<br>
				<%}if(ship_BlockAddress.trim().length()>0){%>
				<span><%=ship_BlockAddress%></span>	<br>	
				<%}if(ship_RoadAddress.trim().length()>0){%>
				<span><%=ship_RoadAddress%></span>	<br>
				<%}if(ship_State.trim().length()>0 || ship_Country.trim().length()>0  || ship_Zip.trim().length()>0 ){%>
				<span><%=ship_State%> <%=ship_Country%> <%=ship_Zip%></span>	<br>	
				<%}if(ship_Attention.trim().length()>0){%>
				<span>Attention: <%=ship_Attention%></span>	<br>		
				<%}if(ship_TelNo.trim().length()>0){%>
				<span>Tel: <%=ship_TelNo%></span>	<br>
				<%}%>
			<%} }%>
				<br>
				<%if(printwithtransportmode == 1){ %>
				<span><%=transmode%>: <%=transportmode%></span><br>
				<%} %>	
				<%if(printwithproject == 1){ %>
				<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				<span><%=project%> : <%=projectname%></span><br>
				<%} }%>	
				</span>
			</div>
			<div class="col-xs-5 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td><%=invoicedate%></td>
							<td><%=invoiceHdr.get("INVOICE_DATE")%></td>
						</tr>
						<%if(printWithDeliveryDate.equals("1") && ((String)invoiceHdr.get("DUE_DATE")).length() > 0){%>
						<tr>
							<td><%=duedate%></td>
							<td><%=invoiceHdr.get("DUE_DATE")%></td>
						</tr>
						<%} %>
						<tr>
							<td><%=incoterm%></td>
							<td><%=invoiceHdr.get("INCOTERMS")%></td>
						</tr>
						<%if(printEmployee.equals("1") && ((String)invoiceHdr.get("EMP_NAME")).length() > 0){%>
						<tr>
							<td><%=Employee%></td>
							<td><%=invoiceHdr.get("EMP_NAME")%></td>
						</tr>
						<%} %>
						<tr>
							<td><%=PreparedBy%></td>
							<td><%=username%></td>
						</tr>
						<tr>
							<td><%=Terms%></td>
							<td><%=invoiceHdr.get("PAYMENT_TERMS")%></td>
						</tr>
					</tbody>
				</table>
			</div>
			
		</div>
		<br>
		
		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td class="text-center"><%=SoNo%></td>
							<td class="text-center"><%=Item%></td>
							<td class="text-center"><%=OrderQty%></td>
							<td class="text-center"><%=UOM%></td>
							<td class="text-center"><%=Rate%></td>
							<td class="text-center"><%=Discount%></td>
							<td class="text-right"><%=totalafterdiscount%></td>
						</tr>
					</thead>
					<tbody>
					<%
					String taxdisplay="";
					for(int i =0; i<invoiceDetList.size(); i++) {   
				  		Map m=(Map)invoiceDetList.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		
				  		int lnno = i+1;
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = Numbers.toMillionFormat(dQty, "3");
			  			
			  			dTotalQty += dQty;
			  			
			  			cost = (String) m.get("UNITPRICE");
			  			double dCost ="".equals(cost) ? 0.0d :  Double.parseDouble(cost);
			  			cost = Numbers.toMillionFormat(dCost, numberOfDecimal);
			  			
			  			amount = (String) m.get("AMOUNT");
			  			double dAmount ="".equals(amount) ? 0.0d :  Double.parseDouble(amount);
			  			amount = Numbers.toMillionFormat(dAmount, numberOfDecimal);
			  			taxdisplay =(String)m.get("TAX_TYPE");
			  			/* if(!((String)m.get("TAX_TYPE")).equalsIgnoreCase("")){
			  				String str =(String)m.get("TAX_TYPE");
			  				str=str.replace("%]","");			  				
			  				String[] arrOfStr = str.split("\\[");
			  			percentage = arrOfStr[1];
			  			//percentage = (String)m.get("GSTPERCENTAGE");
			  			double dPercntage = "".equals(cost) ? 0.0d :  Double.parseDouble(percentage);
			  			shingpercentage = dPercntage;
			  			double dTax = (dAmount * (dPercntage / 100));
			  			tax = Numbers.toMillionFormat(dTax, numberOfDecimal);
			  					
			  			Map<String, String> taxDetail = new HashMap();
			  			boolean match = false;
			  			//String display = (String)m.get("TAX_TYPE")+" ["+(String)m.get("GSTPERCENTAGE")+"%]";
			  			String display = (String)m.get("TAX_TYPE");
			  			taxDetail.put("name", display);
			  			taxDetail.put("types", arrOfStr[0]);
			  			taxDetail.put("tax", tax);
			  			
			  			if(taxList.size() == 0){
			  				taxList.add(taxDetail);
			  			}else{
			  				for(int j =0; j < taxList.size(); j++) {
			  					Map tMap=(Map)taxList.get(j);
			  					if(((String)tMap.get("types")).equalsIgnoreCase(arrOfStr[0])){			  						
			  						dTax = dTax + Double.parseDouble((String)tMap.get("tax"));
			  						tax = Numbers.toMillionFormat(dTax, numberOfDecimal);
			  						tMap.put("tax", tax);
			  						match = true;
			  					}
			  				}
			  				if(!match){
				  				taxList.add(taxDetail);
				  			}
			  			}
		  			} */
			  			
			  			String linediscount = (String)m.get("DISCOUNT");
			  			String linediscounytype = (String) m.get("DISCOUNT_TYPE");
			  			if(linediscounytype.equalsIgnoreCase("%")){
			  				linediscount = Numbers.toMillionFormat(Double.parseDouble(linediscount), "3");	
			  			}else{
			  				linediscount = Numbers.toMillionFormat(Double.parseDouble(linediscount), numberOfDecimal);	
			  			}
						
						String batch = (String) m.get("BATCH");
						String loc = (String) m.get("LOC");
						String ITEM = (String) m.get("ITEM");
						ITEM += "<br>"+(String) m.get("ITEMDESC");
						String HSCODE = (String) m.get("HSCODE");
						String COO = (String) m.get("COO");
						String BRAND = (String) m.get("BRAND");
						String detailDesc = (String) m.get("DETAILDESC");
						String itemClass="";
						if(printDetailDesc.equals("1") && detailDesc.length() > 0){
							ITEM += "<br>"+detailDesc;
						}	
									
						if(printwithbrand.equals("1") && BRAND.length() > 0){
							ITEM +="<br>"+Brand+" "+BRAND;
						}
						
						if(printwithhscode.equals("1") && HSCODE.length() > 0){
							ITEM +="<br>"+hscode+" "+HSCODE;
						}
						
						if(printwithcoo.equals("1") && COO.length() > 0){
							ITEM +="<br>"+coo+" "+COO;	
						}
						if(printwithProduct.equals("0")){
							itemClass = "no-print";
						}
			  		%>
			  		<%
									Hashtable hit = new Hashtable();
									hit.put("PLANT", plant);
									hit.put("ITEM", (String) m.get("ITEM"));
									hit.put("ISCOMPRO", "1");
									boolean itemexist = itemMstDAO.isExisit(hit, "");
									if(itemexist){
									%>
									<tr>
										<td class="text-left" colspan="7"><b>PARENT PRODUCT</b></td>
									</tr>
									<%} %>
			  		
				  		<tr>
<%-- 							<td class="text-center"><%=m.get("LNNO") %></td> --%>
							<td class="text-center"><%=lnno%></td>
<%-- 							<td class="text-center"><span class="<%=itemClass%>"><%=ITEM %></span></td> --%>
<%-- 							<td class="text-center"><%=ITEM %></td> --%>
							<td class="text-center"><%=m.get("ITEM")%></td>
							<td class="text-center"><%=qty%></td>
							<td class="text-center"><%=m.get("UOM")%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(cost, Integer.valueOf(numberOfDecimal)) %></td>
							<td class="text-center"><%=Numbers.toMillionFormat(linediscount, Integer.valueOf(numberOfDecimal))%><%=linediscounytype%></td>
							<td class="text-right"><%=Numbers.toMillionFormat(amount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr>
          					<td></td>
          					<td colspan="6">&emsp;&emsp;&emsp;<%=m.get("ITEMDESC") %></td>
						</tr>
						
						<%-- <%if(batch.length()>0) {%>
						<tr>
							<td colspan="3">Batch/Sno(Qty) <%=batch+"("+qty+")"%></td>
							<td colspan="4"></td>
						</tr>
						<%}%> --%>
						<%

									if(itemexist){
										
									%>
									<tr>
									<td class="text-left" colspan="7"><b>CHILD PRODUCT</b></td>
										<!-- <td class="text-center">CHILT ITEM</td>
										<td class="text-center"><b></b></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td> -->
									</tr>
									<%
									ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
									ArrayList  movQryList = _ProductionBomUtil.getProdBomList((String) m.get("ITEM"),plant, " AND BOMTYPE='KIT'");
									 if (movQryList.size() > 0) {
										for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
											 Map lineArr = (Map) movQryList.get(iCnt);
											String cchilditem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                           					String cdesc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            				String cqty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            				String cCUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            				
                            				Double childqty = Double.valueOf(qty) * Double.valueOf(cqty);
                            				
                            		%>
									
									<tr>
										<td class="text-center"></td>
										<td class="text-center"><%=cdesc %>(<%=cchilditem%>)</td>
										<td class="text-center"><%=Numbers.toMillionFormat(String.valueOf(childqty),Integer.valueOf(numberOfDecimal))%></td>
										<td class="text-center"><%=cCUOM%></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										
									</tr>
									<% 
										}
									 }
										}else{
											
											if(batch.length()>0) {%>
						
						<tr>
							<td colspan="3">Batch/Sno(Qty) <%=batch+"("+qty+")"%></td>
							<td colspan="4"></td>
						</tr>
				  	<%}
										}
				  	}%>
					</tbody>
				</table>
				<table id="table3" class="table" style="margin-bottom: 0px;">
					<tbody style="text-align:right;">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td class="text-center">Qty Total</td>
							<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
							<td><%=SubTotal%></td>
							<td><%=Numbers.toMillionFormat(subTotal, numberOfDecimal) %></td>
						</tr>
						<%-- <tr>
							<td colspan="6"><%=orderdiscountlbl%> (<%=ordisc%>%)</td>
							<td>(-) <%=orderdiscountcost%></td>
						</tr> --%>
						
						<%if(orddisctype.equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="6">
										<%=orderdiscountlbl%> (<%=Numbers.toMillionFormat(Double.valueOf(ordisc), "3")%>%)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="6">
										<%=orderdiscountlbl%> (<%=orddisctype%>)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
						<%} %>
						<%-- <tr>
							<td colspan="6"><%=TotalDiscount%></td>
							<td>(-) <%=discount%></td>
						</tr> --%>
						<%if(discounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="6">
										<%=TotalDiscount%> (<%=Numbers.toMillionFormat(Double.valueOf(discountpercent), "3") %>%)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="6">
										<%=TotalDiscount%> (<%=discounttype%>)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%} %>
						<%-- <tr>
							<td colspan="6"><%=shippingcost%></td>
							<td><%=shipingcost%></td>
						</tr> --%>
						<tr>
									<td colspan="6">
									<%=shippingcost%>
									<br>
										<%if(isshiptax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
									</td>
									<td><%=Numbers.toMillionFormat(shipingcost, Integer.valueOf(numberOfDecimal)) %></td>
								</tr>
						<%-- <%
						if(taxList.size() > 0){
						for(int i =0; i<taxList.size(); i++) {
							Map tMap=(Map)taxList.get(i);
							String name=(String)tMap.get("name");
							if(name.equalsIgnoreCase("EXEMPT[0.0%]") || name.equalsIgnoreCase("OUT OF SCOPE[0.0%]"))
							{
								
							}
							else
							{
								String totaltaxship="0";
								String sname = name.substring(0, name.indexOf("("));
								if(sname.equalsIgnoreCase("ZERO RATE")){
									totaltaxship  = Numbers.toMillionFormat( Double.parseDouble((String)tMap.get("tax")), numberOfDecimal);
									%>
									<tr>
										<td colspan="6"><%=(String)tMap.get("name")%></td>
										<td><%=totaltaxship%></td>
									</tr>
					<%
								}else{
									double dTaxship = (Double.parseDouble(shipingcost) * (shingpercentage / 100));
									double dTaxorderdiscount = (Double.parseDouble(orderdiscountcost) * (shingpercentage / 100));
									double dTaxdiscout = (Double.parseDouble(discount) * (shingpercentage / 100));
									
									double dtotaltaxship = (Double.parseDouble((String)tMap.get("tax")) + dTaxship)-(dTaxorderdiscount+dTaxdiscout);
									totaltaxship  = Numbers.toMillionFormat(dtotaltaxship, numberOfDecimal);
								
							
						%>
										<tr>
											<td colspan="6"><%=(String)tMap.get("name")%></td>
											<td><%=totaltaxship%></td>
										</tr>
						<%
						
								
								}
								}
							}
						}
						%> --%>
						<%if(taxid != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td colspan="6"><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(totalTax, Integer.valueOf(numberOfDecimal))  %></td>
										</tr>
								<%}
								}%>
						<tr>
							<td colspan="6"><%=Adjustment%></td>
							<td><%=Numbers.toMillionFormat(adjustment, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr style="font-size:14px;">
							<td colspan="6"><b><%=Total%> (<%=invoiceHdr.get("CURRENCYID") %>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal))  %></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)invoiceHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: August 04,2021  Description:  Total of Local Currency -->
						<tr style="font-size:14px;" id="showtotalcur">
							<td colspan="6"><b><%=Total%> (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(Numbers.toMillionFormat((dTotalAmount/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%}%>
						<tr>
							<td colspan="6"><%=PaymentMade%> (<%=invoiceHdr.get("CURRENCYID") %>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(Spaymentmade, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)invoiceHdr.get("CURRENCYID"))) {%>
						<tr id="showtotalPay">
							<td colspan="6"><%=PaymentMade%> (<%=basecurrency%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(paymentMadeloc, numberOfDecimal) %></td>
						</tr>
						<%}%>
						<tr class="grey-bg" style="font-size:14px;">
							<td colspan="6"><b><%=BalanceDue %> (<%=invoiceHdr.get("CURRENCYID") %>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)invoiceHdr.get("CURRENCYID"))) {%>
						<tr class="grey-bg" style="font-size:14px;" id="showtotalBal">
							<td colspan="6"><b><%=BalanceDue %> (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat((baldue/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal) %></b></td>
						</tr>
						<%}%>
						<tr>
						<td></td>
						<td></td>
						</tr>
					</tbody>
				</table>
				<table id="table4" class="table" style="display: none"><!--  Author: Azees  Create date: August 04,2021  Description:  Total of Local Currency -->
					<tbody style="text-align:right;">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td class="text-center">Qty Total</td>
							<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
							<td><%=SubTotal %></td>
							<td><%=Numbers.toMillionFormat(subTotal, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						
						<%if(orddisctype.equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="6">
										<%=orderdiscountlbl%> (<%=Numbers.toMillionFormat(Double.valueOf(ordisc), "3")%>%)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="6">
										<%=orderdiscountlbl%> (<%=orddisctype%>)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
						<%} %>
						<%if(discounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="6">
										<%=TotalDiscount%> (<%=Numbers.toMillionFormat(Double.valueOf(discountpercent), "3") %>%)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="6">
										<%=TotalDiscount%> (<%=discounttype%>)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
									</tr>
								<%} %>
						<tr>
									<td colspan="6">
									<%=shippingcost%>
									<br>
										<%if(isshiptax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
									</td>
									<td><%=Numbers.toMillionFormat(shipingcost, Integer.valueOf(numberOfDecimal)) %></td>
								</tr>
						<%if(taxid != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td colspan="6"><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(totalTax, Integer.valueOf(numberOfDecimal))  %></td>
										</tr>
								<%}
								}%>
						<tr>
							<td colspan="6"><%=Adjustment%></td>
							<td><%=Numbers.toMillionFormat(adjustment, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr style="font-size:20px;">
							<td colspan="6"><b><%=Total%>(<%=invoiceHdr.get("CURRENCYID") %>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal))  %></b></td>
						</tr>
						<tr>
							<td colspan="6"><%=PaymentMade%>(<%=invoiceHdr.get("CURRENCYID") %>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(Spaymentmade, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr class="grey-bg" style="font-size:20px;">
							<td colspan="6"><b><%=BalanceDue %>(<%=invoiceHdr.get("CURRENCYID") %>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<tr>
						<td></td>
						<td></td>
						</tr>
					</tbody>
				</table>
				<!-- PDF Print End 2 -->
				<table id="tablesign" class="table" >
							<tbody style="text-align: right;">
								<tr>
									<td><img src="<%=rootURI %>/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 130.00px;"  id="signimg"></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>______________</td> 
									<td></td>
									<td>______________</td> 
								</tr>
								<tr>
									<td><%=compsign%></td> 
									<td></td>
									<td><%=compname%></td> 
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td><img src="<%=rootURI %>/ReadFileServlet?fileLocation=<%=sealPath%>" style="width: 130.00px;" id="sealimg"></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
									<tr>
									<td>______________</td> 
									<td></td>
									<td>______________</td> 
								</tr>
									<tr>
									<td><%=compdate%></td> 
									<td></td>
									<td><%=compseal%></td> 
								</tr>
							</tbody>
						</table>
				<br><br><br>
				<footer>
				<table id="footerTable" class="table">		
					<tr>
						<td colspan="7"><%=remark3%> <%=(String) invoiceHdr.get("NOTE")%></td>
					</tr>	
					<tr>
						<td colspan="7"></td>
					</tr>		
					<tr>
						<td colspan="7"></td>
					</tr>		
					<tr>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><img src="<%=rootURI %>/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 100px; height: 50px;"></td>
						<td class="no-padding"></td>
					</tr>		
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=Seller%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding"><%=SellerSignature%><br><br><br></td>
					</tr>
					
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=Buyer%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding"><%=BuyerSignature%></td>
					</tr>
				</table>
				</footer>
				<!-- PDF Print Start 3 -->
			</div>
		</div>
		</div>
		<!-- PDF Print End 3 -->
		</div>		
		<div id="journal_detail_box" class="box">
		<div class="box-header">
			<div id="ember1145"
				class="bg-secondary  accord-title p-3 cursor-pointer no-border text-semibold ember-view">
				 <h1 style="font-size: 20px;" class="box-title">Display Journal</h1>
				<svg version="1.1" id="SVG_Up_Arrow" style="display: none;"
					xmlns="http://www.w3.org/2000/svg" idth="512" height="512"
					viewBox="0 0 512 512" xml:space="preserve"
					class="icon icon-xs align-text-bottom float-right text-dodgerblue mr-3 pull-right pointer-arrow"
					onclick="{hideJournalDetail('journal_detail_box_body');}">
					<path
						d="M495.7 419.6L295 225.1c-21.6-20.9-56.4-20.9-78 0L16.3 419.6c-21.7 21-21.8 55.2-.3 76.4 21.5 21.2 56.6 21.3 78.3.3L256 339.6l161.6 156.7c21.7 21 56.8 20.9 78.3-.3 10.7-10.5 16.1-24.3 16.1-38.1s-5.5-27.7-16.3-38.3z"></path></svg>
				<svg version="1.1" id="SVG_Down_Arrow"
					xmlns="http://www.w3.org/2000/svg" width="512" height="512"
					viewBox="0 0 512 512"
					class="icon icon-xs align-text-bottom float-right text-dodgerblue mr-3 pull-right pointer-arrow"
					onclick="{showJournalDetail('journal_detail_box_body', 'journal_detail');}">
					<path
						d="M2.157 159.57c0 13.773 5.401 27.542 16.195 38.02l198.975 192.867c21.411 20.725 55.94 20.725 77.34 0L493.63 197.59c21.508-20.846 21.637-54.778.269-75.773-21.35-20.994-56.104-21.098-77.612-.26L256.004 276.93 95.721 121.562c-21.528-20.833-56.268-20.734-77.637.26C7.472 132.261 2.157 145.923 2.157 159.57z"></path></svg>
			</div>
		</div>
		<div class="box-body" id="journal_detail_box_body" style="display:none;">
			<div class="row">
				<div class="col-sm-12">
					<div id="ember1146" class="ember-view collapse show" style="">
						<div class="p-3 border border-secondary">
							<div id="ember1147" class="ember-view">
								<div class="clearfix pb-3">
									<small class="text-muted">Amount is displayed in your invoice currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=invoiceHdr.get("CURRENCYID")%></span>
									<!---->
								</div>
								<p class="font-large">
									<b> <!---->
										<!---->
									</b>
								</p>
								
								<div id="ember1154" class="ember-view">
									<!---->
								</div>
							</div>
						</div>
						<br>
					</div>
					<div id="journal_detail"></div>
				</div>
			</div>		
		</div>
		<div class="modal fade" id="outstandingModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
        	<h4 class="modal-title pull-left">Preview</h4>
        	<div class="btn-toolbar pull-right"> 
        		<button type="button" class="btn btn-success" onclick="onPrintOutstanding(true)">Print</button> 
        		<button type="button" class="btn btn-default" data-dismiss="modal"> Close </button>
       		</div>
        </div>
        <div class="modal-body">
          <object id="outstandingPdfOutput" type="application/pdf" style="height:500px;width:100%;">
            <p>It appears you don't have PDF support in this web browser. <a href="#" id="download-link">Click here to download the PDF</a>.</p>
		  </object>
        </div>
      </div>
      
    </div>
  </div>
		<script >
		/* @license 
		 * jQuery.print, version 1.6.0
		 *  (c) Sathvik Ponangi, Doers' Guild
		 * Licence: CC-By (http://creativecommons.org/licenses/by/3.0/)
		 *--------------------------------------------------------------------------*/
		(function ($) {
		    "use strict";
		    // A nice closure for our definitions

		    function jQueryCloneWithSelectAndTextAreaValues(elmToClone, withDataAndEvents, deepWithDataAndEvents) {
		        // Replacement jQuery clone that also clones the values in selects and textareas as jQuery doesn't for performance reasons - https://stackoverflow.com/questions/742810/clone-isnt-cloning-select-values
		        // Based on https://github.com/spencertipping/jquery.fix.clone
		        var $elmToClone = $(elmToClone),
		            $result           = $elmToClone.clone(withDataAndEvents, deepWithDataAndEvents),
		            $my_textareas     = $elmToClone.find('textarea').add($elmToClone.filter('textarea')),
		            $result_textareas = $result.find('textarea').add($result.filter('textarea')),
		            $my_selects       = $elmToClone.find('select').add($elmToClone.filter('select')),
		            $result_selects   = $result.find('select').add($result.filter('select'));

		        for (var i = 0, l = $my_textareas.length; i < l; ++i) {
		            $($result_textareas[i]).val($($my_textareas[i]).val());
		        }
		        for (var i = 0, l = $my_selects.length;   i < l; ++i) {
		            for (var j = 0, m = $my_selects[i].options.length; j < m; ++j) {
		                if ($my_selects[i].options[j].selected === true) {
		                    $result_selects[i].options[j].selected = true;
		                }
		            }
		        }
		        return $result;
		    }

		    function getjQueryObject(string) {
		        // Make string a vaild jQuery thing
		        var jqObj = $("");
		        try {
		            jqObj = jQueryCloneWithSelectAndTextAreaValues(string);
		        } catch (e) {
		            jqObj = $("<span />")
		                .html(string);
		        }
		        return jqObj;
		    }

		    function printFrame(frameWindow, content, options) {
		        // Print the selected window/iframe
		        var def = $.Deferred();
		        try {
		            frameWindow = frameWindow.contentWindow || frameWindow.contentDocument || frameWindow;
		            var wdoc = frameWindow.document || frameWindow.contentDocument || frameWindow;
		            if(options.doctype) {
		                wdoc.write(options.doctype);
		            }
		            wdoc.write(content);
		            wdoc.close();
		            var printed = false;
		            var callPrint = function () {
		                if(printed) {
		                    return;
		                }
		                // Fix for IE : Allow it to render the iframe
		                frameWindow.focus();
		                try {
		                    // Fix for IE11 - printng the whole page instead of the iframe content
		                    if (!frameWindow.document.execCommand('print', false, null)) {
		                        // document.execCommand returns false if it failed -http://stackoverflow.com/a/21336448/937891
		                        frameWindow.print();
		                    }
		                    // focus body as it is losing focus in iPad and content not getting printed
		                    $('body').focus();
		                } catch (e) {
		                    frameWindow.print();
		                }
		                frameWindow.close();
		                printed = true;
		                def.resolve();
		            }
		            // Print once the frame window loads - seems to work for the new-window option but unreliable for the iframe
		            $(frameWindow).on("load", callPrint);
		            // Fallback to printing directly if the frame doesn't fire the load event for whatever reason
		            setTimeout(callPrint, options.timeout);
		        } catch (err) {
		            def.reject(err);
		        }
		        return def;
		    }

		    function printContentInIFrame(content, options) {
		        var $iframe = $(options.iframe + "");
		        var iframeCount = $iframe.length;
		        if (iframeCount === 0) {
		            // Create a new iFrame if none is given
		            $iframe = $('<iframe height="0" width="0" border="0" wmode="Opaque"/>')
		                .prependTo('body')
		                .css({
		                    "position": "absolute",
		                    "top": -999,
		                    "left": -999
		                });
		        }
		        var frameWindow = $iframe.get(0);
		        return printFrame(frameWindow, content, options)
		            .done(function () {
		                // Success
		                setTimeout(function () {
		                    // Wait for IE
		                    if (iframeCount === 0) {
		                        // Destroy the iframe if created here
		                        $iframe.remove();
		                    }
		                }, 1000);
		            })
		            .fail(function (err) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", err);
		                printContentInNewWindow(content, options);
		            })
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function printContentInNewWindow(content, options) {
		        // Open a new window and print selected content
		        var frameWindow = window.open();
		        return printFrame(frameWindow, content, options)
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function isNode(o) {
		        /* http://stackoverflow.com/a/384380/937891 */
		        return !!(typeof Node === "object" ? o instanceof Node : o && typeof o === "object" && typeof o.nodeType === "number" && typeof o.nodeName === "string");
		    }
		    $.print = $.fn.print = function () {
		        // Print a given set of elements
		        var options, $this, self = this;
		        // console.log("Printing", this, arguments);
		        if (self instanceof $) {
		            // Get the node if it is a jQuery object
		            self = self.get(0);
		        }
		        if (isNode(self)) {
		            // If `this` is a HTML element, i.e. for
		            // $(selector).print()
		            $this = $(self);
		            if (arguments.length > 0) {
		                options = arguments[0];
		            }
		        } else {
		            if (arguments.length > 0) {
		                // $.print(selector,options)
		                $this = $(arguments[0]);
		                if (isNode($this[0])) {
		                    if (arguments.length > 1) {
		                        options = arguments[1];
		                    }
		                } else {
		                    // $.print(options)
		                    options = arguments[0];
		                    $this = $("html");
		                }
		            } else {
		                // $.print()
		                $this = $("html");
		            }
		        }
		        // Default options
		        var defaults = {
		            globalStyles: true,
		            mediaPrint: false,
		            stylesheet: null,
		            noPrintSelector: ".no-print",
		            iframe: true,
		            append: null,
		            prepend: null,
		            manuallyCopyFormValues: true,
		            deferred: $.Deferred(),
		            timeout: 750,
		            title: null,
		            doctype: '<!doctype html>'
		        };
		        // Merge with user-options
		        options = $.extend({}, defaults, (options || {}));
		        var $styles = $("");
		        if (options.globalStyles) {
		            // Apply the stlyes from the current sheet to the printed page
		            $styles = $("style, link, meta, base, title");
		        } else if (options.mediaPrint) {
		            // Apply the media-print stylesheet
		            $styles = $("link[media=print]");
		        }
		        if (options.stylesheet) {
		            // Add a custom stylesheet if given
		            $styles = $.merge($styles, $('<link rel="stylesheet" href="' + options.stylesheet + '">'));
		        }
		        // Create a copy of the element to print
		        var copy = jQueryCloneWithSelectAndTextAreaValues($this);
		        // Wrap it in a span to get the HTML markup string
		        copy = $("<span/>")
		            .append(copy);
		        // Remove unwanted elements
		        copy.find(options.noPrintSelector)
		            .remove();
		        // Add in the styles
		        copy.append(jQueryCloneWithSelectAndTextAreaValues($styles));
		        // Update title
		        if (options.title) {
		            var title = $("title", copy);
		            if (title.length === 0) {
		                title = $("<title />");
		                copy.append(title);                
		            }
		            title.text(options.title);            
		        }
		        // Appedned content
		        copy.append(getjQueryObject(options.append));
		        // Prepended content
		        copy.prepend(getjQueryObject(options.prepend));
		        if (options.manuallyCopyFormValues) {
		            // Manually copy form values into the HTML for printing user-modified input fields
		            // http://stackoverflow.com/a/26707753
		            copy.find("input")
		                .each(function () {
		                    var $field = $(this);
		                    if ($field.is("[type='radio']") || $field.is("[type='checkbox']")) {
		                        if ($field.prop("checked")) {
		                            $field.attr("checked", "checked");
		                        }
		                    } else {
		                        $field.attr("value", $field.val());
		                    }
		                });
		            copy.find("select").each(function () {
		                var $field = $(this);
		                $field.find(":selected").attr("selected", "selected");
		            });
		            copy.find("textarea").each(function () {
		                // Fix for https://github.com/DoersGuild/jQuery.print/issues/18#issuecomment-96451589
		                var $field = $(this);
		                $field.text($field.val());
		            });
		        }
		        // Get the HTML markup string
		        var content = copy.html();
		        // Notify with generated markup & cloned elements - useful for logging, etc
		        try {
		            options.deferred.notify('generated_markup', content, copy);
		        } catch (err) {
		            console.warn('Error notifying deferred', err);
		        }
		        // Destroy the copy
		        copy.remove();
		        if (options.iframe) {
		            // Use an iframe for printing
		            try {
		                printContentInIFrame(content, options);
		            } catch (e) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", e.stack, e.message);
		                printContentInNewWindow(content, options);
		            }
		        } else {
		            // Use a new window for printing
		            printContentInNewWindow(content, options);
		        }
		        return this;
		    };
		})(jQuery);
		</script>
		<script>
			$(document).ready(function(){
								 
				$("#tablesign").hide();
				
				toDataURL($("#signimg").attr("src"),
						function(dataUrl) {
					setsignimg(dataUrl);
					  	},'image/jpeg');
				toDataURL($("#sealimg").attr("src"),
						function(dataUrl) {
					setsealimg(dataUrl);
					  	},'image/jpeg');
			  	
				document.getElementById('headerpage').innerHTML="<%=InboundOrderHeader%>";
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
			  $('[data-toggle="tooltip"]').tooltip();  
			  $('.printMe').click(function(){
				  <%if(!basecurrency.equalsIgnoreCase((String)invoiceHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: August 04,2021 Description:  Total of Local Currency -->
				  document.getElementById('showtotalcur').style.display = 'none';
				  document.getElementById('showtotalPay').style.display = 'none';
				  document.getElementById('showtotalBal').style.display = 'none';
				  <%}%>
				  if($("#table2").height() > 370){
					  $("#table3").addClass("page-break-before");
				  }else{
					  $("#table3").removeClass("page-break-before");
				  }
				  	 document.getElementById('headerpage').innerHTML="<%=InboundOrderHeader%>";
				     $("#print_id").print();
				     document.getElementById('headerpage').innerHTML="<%=InboundOrderHeader%>";
				     
				     <%if(!basecurrency.equalsIgnoreCase((String)invoiceHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: August 04,2021 Description:  Total of Local Currency -->
				     document.getElementById('showtotalcur').style.display = 'contents';
				     document.getElementById('showtotalPay').style.display = 'contents';
				     document.getElementById('showtotalBal').style.display = 'contents';
					  <%}%>
				});
			  $("#bill-cancel").click(function() {
				  var invoiceHdrId = "<%=invoiceHdr.get("ID")%>";
				  var invoice = "<%=invoiceHdr.get("INVOICE")%>";
				  var status = "<%=invoiceHdr.get("BILL_STATUS")%>";
				  var taxstatus = "<%=invoiceHdr.get("TAX_STATUS")%>";
				  var result="";
				  if(status.toLowerCase() == "open" || status.toLowerCase() == "draft" ) {
						if(taxstatus.toLowerCase() == "tax generated"){
							result = "Transaction that already filed Tax Return not allow to Cancel.";
							window.location.href = "../invoice/detail?INVOICE_HDR="+invoiceHdrId+"&resultnew="+ result;
						}else {
							$('#cancelinvoice').modal('show');
						}
					}else if(status.toLowerCase() == "cancelled"){
						result = "invoice that already cancelled";
						window.location.href = "../invoice/detail?INVOICE_HDR="+invoiceHdrId+"&resultnew="+ result;
					}else {
						result = "Invoice already marked as 'PAID or PARTIALLY PAID'not allow to cancel.";
						window.location.href = "../invoice/detail?INVOICE_HDR="+invoiceHdrId+"&resultnew="+ result;
					}
				  
			  });
			  
			  $("#cfmcancel").click(function(){
				   window.location.href = "/track/InvoiceServlet?ACTION=convertToCancel&INVID=<%=invoiceHdr.get("ID")%>&INVOICE=<%=invoiceHdr.get("INVOICE")%>&STATUS=<%=invoiceHdr.get("BILL_STATUS")%>&TAXSTATUS=<%=invoiceHdr.get("TAX_STATUS")%>";
				});
				   
			 $("#bill-delete").click(function() {    
				 var invoiceHdrId = "<%=invoiceHdr.get("ID")%>";
				  var invoice = "<%=invoiceHdr.get("INVOICE")%>";
				  var status = "<%=invoiceHdr.get("BILL_STATUS")%>";
				  var taxstatus = "<%=invoiceHdr.get("TAX_STATUS")%>";
				  var result="";
				  if(status.toLowerCase() == "open" || status.toLowerCase() == "draft" || status.toLowerCase() == "cancelled") {
						if(taxstatus.toLowerCase() == "tax generated"){
							result = "Transaction that already filed Tax Return not allow to Delete.";
							window.location.href = "../invoice/detail?INVOICE_HDR="+invoiceHdrId+"&resultnew="+ result;
						}else {
							$('#deleteinvoice').modal('show');
						}
					}else {
						result = "invoice already marked as 'PAID or PARTIALLY PAID'not allow to delete.";
						window.location.href = "../invoice/detail?INVOICE_HDR="+invoiceHdrId+"&resultnew="+ result;
					}
				  
			 });
			 
			 $("#cfmdelete").click(function(){
				 window.location.href = "/track/InvoiceServlet?ACTION=deleteinvoice&INVID=<%=invoiceHdr.get("ID")%>&INVOICE=<%=invoiceHdr.get("INVOICE")%>&STATUS=<%=invoiceHdr.get("BILL_STATUS")%>&TAXSTATUS=<%=invoiceHdr.get("TAX_STATUS")%>";
				});
			 <%
				Journal journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant, (String)invoiceHdr.get("INVOICE"), "INVOICE");
			  %>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
			});
			JsBarcode("#barCode", "<%=invoiceHdr.get("INVOICE")%>", {format: "CODE128",displayValue: false});
			function convertToDraft(){
				window.location.href = "/track/InvoiceServlet?ACTION=convertToDraft&INVID=<%=invoiceHdr.get("ID")%>";
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
				doc.text('<%=fromAddress_BlockAddress%>', 16, rY+=10);

				doc.text('<%=fromAddress_RoadAddress%>', 16, rY+=4);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, rY+=4);
				<%if(printuenno.equals("1")){ %>
				doc.text('<%=uenno%> <%=companyregnumber.trim()%>', 16, rY+=4);
				<%} %>
				
				doc.text('<%=rcbno%> <%=RCBNO%>', 16, rY+=4);
				
				doc.text('<%=Attention%> <%=NAME%>', 16, rY+=4);
				
				doc.text('<%=Telephone%> <%=TELNO%> <%=FaxLBL%> <%=FAX%>', 16, rY+=4);
				
				doc.text('<%=Email%> <%=EMAIL%>', 16, rY+=4);
				
				/*To Address*/
				rY+=6;
				doc.text('<%=InvoiceOrderToHeader%>', 16,  rY+=4);
				var lY = rY;	/*left Y-axis*/
				<%if(to_CompanyName.trim().length()>0){%>
				doc.text('<%=to_CompanyName%>', 16, rY+=4);
				<%}if(printcustomeruenno.equals("1")){%>
				doc.text('<%=customeruenno%> <%=suenno%>', 16, rY+=4);
				<%}if(to_RcbNo.trim().length()>0){%>
				doc.text('<%=customerrcbno%> <%=to_RcbNo%>', 16, rY+=4);				
				<%}if(to_BlockAddress.trim().length()>0){%>
				doc.text('<%=to_BlockAddress%>', 16, rY+=4);				
				<%}if(to_RoadAddress.trim().length()>0){%>
				doc.text('<%=to_RoadAddress%>', 16, rY+=4);
				<%}if(to_State.trim().length()>0 || to_Country.trim().length()>0  || to_Zip.trim().length()>0 ){%>
				doc.text('<%=to_State%> <%=to_Country%> <%=to_Zip%>', 16, rY+=4);				
				<%}if(to_Attention.trim().length()>0){%>
				doc.text('<%=Attention%> <%=to_Attention%>', 16, rY+=4);				
				<%}if(to_TelNo.trim().length()>0 || to_Fax.trim().length()>0){%>
				doc.text('<%=Telephone%> <%=to_TelNo%> <%=FaxLBL%> <%=to_Fax%>', 16, rY+=4);				
				<%}if(to_Email.trim().length()>0){%>
				doc.text('<%=Email%> <%=to_Email%>', 16, rY+=4);				
				<%}if(to_Remarks.trim().length()>0){%>
				doc.text('Remarks: <%=to_Remarks%>', 16, rY+=4);
				<%}%>
				
				/*Ship Address*/
				rY+=6;
				<%if(PRINTWITHSHIPINGADD==1){%>
				<%if(ship_CompanyName.trim().length()>0){%>
				doc.text('<%=shipto%>', 16,  rY+=4);
				doc.text('<%=ship_CompanyName%>', 16, rY+=4);
				<%}if(ship_BlockAddress.trim().length()>0){%>
				doc.text('<%=ship_BlockAddress%>', 16, rY+=4);				
				<%}if(ship_RoadAddress.trim().length()>0){%>
				doc.text('<%=ship_RoadAddress%>', 16, rY+=4);	
				<%}if(ship_State.trim().length()>0 || ship_Country.trim().length()>0  || ship_Zip.trim().length()>0 ){%>
				doc.text('<%=ship_State%> <%=ship_Country%> <%=ship_Zip%>', 16, rY+=4);				
				<%}if(ship_Attention.trim().length()>0){%>
				doc.text('Attention: <%=ship_Attention%>', 16, rY+=4);				
				<%}if(ship_TelNo.trim().length()>0){%>
				doc.text('Tel: <%=ship_TelNo%>', 16, rY+=4);
				<%}%>
				<%}%>
<%-- 				doc.text('<%=Terms%> <%=invoiceHdr.get("PAYMENT_TERMS")%>', 16, rY+=6); --%>
				/* **** */
				<%if(printwithtransportmode == 1){ %>
				doc.text('<%=transmode%>: <%=transportmode%>', 16, rY+=4);
				<%} %>	
				<%if(printwithproject == 1){ %>
				doc.text('<%=project%> : <%=projectname%>', 16, rY+=4);
				<%} %>	
				/* Top Right */
				doc.setFontSize(27);
				doc.text('<%=InboundOrderHeader%>', 195, 19, {align:'right'});
				
				
				const img = document.querySelector('img#barCode');
				doc.addImage(img.src, 'JPEG', 140, 20, 55,15);

				doc.setFontSize(10);
				//doc.setFontStyle("bold");
				doc.text('# <%=invoiceHdr.get("INVOICE")%>', 180, 37, {align:'right'});
				<%if(invoiceHdr.get("DONO").equals("") || invoiceHdr.get("DONO").equals(null)){%>
					doc.setFontSize(10);
					doc.text('Balance Due', 195, 46, {align:'right'});
	
					doc.setFontSize(12);
					doc.text('<%=curency%> <%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal))%>', 195, 52, {align:'right'});
				<%}else{%>
				
					doc.setFontSize(12);
					doc.text('<%=OrderNo%>', 195, 46, {align:'right'});
	
					doc.setFontSize(10);
					doc.text('# <%=invoiceHdr.get("DONO")%>', 195, 52, {align:'right'});
					
	
					doc.setFontSize(10);
					doc.text('Balance Due', 195, 63, {align:'right'});
	
					doc.setFontSize(12);
					doc.text('<%=curency%><%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal))%>', 195, 70, {align:'right'});
				 <%}%> 
				
				
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
					columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'},6: {halign: 'left'}},
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
							<%if(printwithProduct.equals("0")){%>
								if(data.table.body[i].cells[1] != undefined){
									data.table.body[i].cells[1].text=[];
								}
							<%}%>
							if(data.table.body[i].cells[6] != undefined){
								data.table.body[i].cells[6].styles["halign"]="right";
							}
						}
					}
				});
				let finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					html : '#table4',
					startY : finalY, 
					styles : {fontSize : 10},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'},2 : {halign : 'center'},3 : {halign : 'right'},4 : {halign : 'right'},5 : {halign : 'right'},6 : {halign : 'right'}},
					theme : 'plain'
				});
				finalY = doc.previousAutoTable.finalY;
				doc.autoTable({
					html : '#tablesign',
					//html : tblp3,
					startY : finalY, 
					styles : {fontSize : 10},
// 					styles : {cellPadding : 0, fontSize : 10},
					columnStyles : {0 : {halign : 'left'},1 : {halign : 'center'},2 : {halign : 'right'}},
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
					      else if (data.column.index === 2 && data.cell.section === 'body') {
					          var td = data.cell.raw;
					         var img = td.getElementsByTagName('img')[0];
					         if(img!=undefined){
					         var dim = data.cell.height - data.cell.padding('horizontal');					         
					         var textPos = data.cell.textPos;
					         doc.addImage(img.src, 170, textPos.y, 22, 22);
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
// 				finalY = doc.previousAutoTable.finalY;
				

				/* doc.autoTable({
					html : '#table3',
					margin : {left : 123},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				}); */
				
				doc.setFontStyle("normal");
				var notes = $('#dnotes').val();
				var remarks = "<%=remark3%>";
				remarks = remarks+" "+notes;
				var lines  = doc.splitTextToSize(remarks, (210-15-15));					
				
				<%-- doc.text(lines, 16, finalY+4); --%>
				
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
					
					
					<%-- doc.text("____________________", 16, pageHeight + lines.length - 30);
					doc.text("<%=Seller%> ", 16, pageHeight + lines.length - 26);
					
					doc.text("____________________", 16, pageHeight + lines.length - 16);
					doc.text("<%=SellerSignature%> ", 16, pageHeight + lines.length - 12);
					
					doc.text("____________________", 150, pageHeight + lines.length - 30);
					doc.text("<%=Buyer%>", 150, pageHeight + lines.length - 26);
					
					doc.text("____________________", 150, pageHeight + lines.length - 16);
					doc.text("<%=BuyerSignature%>", 150, pageHeight + lines.length - 12); --%>
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('Invoice.pdf');
			}
			function setsignimg(dataUrl)
			{
			$("#signimg").attr('src', dataUrl);
			}
			function setsealimg(dataUrl)
			{
			$("#sealimg").attr('src', dataUrl);
			}
			function generate() {
				
			var img = toDataURL($("#logo_content").attr("src"),
					function(dataUrl) {
						generatePdf(dataUrl);
				  	},'image/jpeg');
				
			}
			function generateKitchen() {
				var print = $('#PRINTDEFAULT').val();
				//defalut = 0,default & customer =1 ,default,customer&collection = 2
				//imthi added on 11/01/2023
				if(print=='0'){
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=defafult','_blank') 
					}, 0);
				}else if(print=='1'){
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=defafult','_blank') 
					}, 0);
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=customer','_blank') 
					}, 3000);// it open in second tab window with 4 sec of opening defalut page
				}else if(print=='2'){
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=defafult','_blank') 
					}, 0);
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=customer','_blank') 
					}, 3000); // it open in second tab window with 4 sec of opening defalut page
					setTimeout(function(){ 
						window.open('/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>&PRINT=collection','_blank') 
					}, 6000);// it open in second tab window with 8 sec of opening defalut page
				}
				//end
				
<%-- 				window.open(
						"/track/deleveryorderservlet?Submit=Print Invoice With Price&INVOICE=<%=invoiceHdr.get("INVOICE")%>",
						  '_blank' // <- This is what makes it open in a new window.
						); --%>
					
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
			function recordpayment()
			{
				
				var credit = '<%=orderbalance%>';
				
				if(credit > 0){
					alert("Please Apply Available Credits");
				}else{
					window.location.href="../invoice/recordpayment?invnum=<%=(String) invoiceHdr.get("INVOICE")%>&DONO=<%=(String) invoiceHdr.get("DONO")%>&CUST_CODE=<%=(String) invoiceHdr.get("CUSTNO")%>&EMPNO=<%=(String) invoiceHdr.get("EMPNO")%>&TRANID=<%=(String) invoiceHdr.get("ID")%>&cmd=Edit&type=REGULAR&CUST_NAME=<%=custName%>&AMT=<%=balamtrp%>&INVOICEID=<%=invoiceHdrId%>";
				}
				
			}
			$(function(){
			    $('#convertdrafttoopen').click(function() {
			    	$("#contoopen").submit();
			    });
			    
			    $('#applycrd').click(function() {
			    	
			    	var flag=0;
			    	$(".creditListTable tbody tr").each(function() {
						 flag=1;
					});
					
					if(flag == 0){
						$("#statuscheck").find("input,button,textarea,select").removeAttr('disabled');
					}
			    	
			    });
			});
			
			

		</script>
	</div>
	<form id="contoopen" class="form-horizontal" name="contoopen" action="/track/InvoiceServlet?ACTION=CONVERT_INVOICE_DRAFT_TO_OPEN"  method="post">
							<input type="hidden" name="PLANT" value="<%=plant%>">
							<input type="hidden" name="INVID" value="<%=invoiceHdrId%>">
					</form>
</div>
<!-- model -->

<div id="cancelinvoice" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Are you sure about cancel the invoice?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmcancel" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
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
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Deleted invoice information cannot be retrieved. Are you sure about deleting ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmdelete" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>


<%-- <%@include file="applyCreditInvoice.jsp" %>  --%>

<div id="applycredit" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditForm" method="post">
		<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
		<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>"> <%--Resvi--%>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title"><b>Apply credits for <%=(String) invoiceHdr.get("INVOICE")%></b></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Invoice Balance: </span><b><%=basecurency%> <%=conv_balduest%> / <%=curency%> <%=balduest%></b>
		      		</div>
		      	</div>
		      	<h4>General Credit</h4>
		      	<table class="table advanceTable" id="statuscheck">
					<thead>
						<tr>
							<th>Reference#</th>
							<th>Invoice</th>
							<th>Order Number</th>
							<th>Date</th>
							<th>Amount</th>
							<th>Balance</th>
							<th>Amount to Apply</th>
						</tr>
					</thead>
					<tbody> 
					<%
						for(int j =0; j < advanceCriditAMOUNT.size(); j++) {
		  					Map creditDetail=(Map)advanceCriditAMOUNT.get(j);	
		  					String crdid =(String)creditDetail.get("CREDITNOTEHDRID");
		  					String ref = "";
		  					String donocredit ="";
		  					String invoicecredit ="";
		  					if(crdid.equals("0")){
		  						ref = "Advance Payment";
		  					}else{
		  						Hashtable ht11 = new Hashtable();
		  						ht11.put("ID", crdid);
		  						ht11.put("PLANT", plant);
		  						List CustcrdnoteHdrList =  custcrdnotedao.getCustCreditnoteHdrById(ht11);
		  						Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
		  						ref = (String)CustcrdnoteHdr.get("CREDITNOTE");
		  						donocredit = (String)CustcrdnoteHdr.get("REFERENCE");
		  						invoicecredit = (String)CustcrdnoteHdr.get("INVOICE");
		  					}
		  					
		  					
		  					
			  		%>
						<tr>
							
							<td hidden><%=invoiceHdr.get("DONO")%></td>
							<td hidden><%=(String)creditDetail.get("RECEIVEHDRID")%></td>
							<td hidden><%=invoiceHdrId%></td>
							<td hidden><%=(String)creditDetail.get("ID")%></td>
							<td><%=ref%></td>
							<td><%=invoicecredit%></td>
							<td><%=donocredit%></td>
	        				<td><%=(String)creditDetail.get("RECEIVE_DATE")%></td>
	        				<td><%=Numbers.toMillionFormat(Double.parseDouble((String)creditDetail.get("AMOUNT")), numberOfDecimal)%></td>
	        				<td><%=Numbers.toMillionFormat(Double.parseDouble((String)creditDetail.get("BALANCE")), numberOfDecimal)%></td>
	        				<td><div class="float-right"> 
									<input class="form-control text-right creditAmountentry" onkeypress="return isNumberKey(event,this,4)" type="text" value="0" disabled> 
								</div></td>
							
	        			</tr>	
        			<%} %>
					</tbody>
				</table>
				<h4>Order Credit</h4>
				<table class="table creditListTable">
					<thead>
						<tr>
							<th>Reference#</th>
							<th>Date</th>
							<th>Amount</th>
							<th>Balance</th>
							<th>Amount to Apply</th>
						</tr>
					</thead>
					<tbody> 
					<%
						for(int j =0; j < advanceCridit.size(); j++) {
		  					Map creditDetail=(Map)advanceCridit.get(j);	
		  					double balcheking = Double.parseDouble((String)creditDetail.get("BALANCE"));
		  					if(balcheking > 0){
			  		%>
						<tr>
							
							<td hidden><%=invoiceHdr.get("DONO")%></td>
							<td hidden><%=(String)creditDetail.get("RECEIVEHDRID")%></td>
							<td hidden><%=invoiceHdrId%></td>
							<td hidden><%=(String)creditDetail.get("ID")%></td>
							<td><%=invoiceHdr.get("DONO")%></td>
	        				<td><%=(String)creditDetail.get("RECEIVE_DATE")%></td>
	        				<td><%=Numbers.toMillionFormat(Double.parseDouble((String)creditDetail.get("AMOUNT")), numberOfDecimal)%></td>
	        				<td><%=Numbers.toMillionFormat(Double.parseDouble((String)creditDetail.get("BALANCE")), numberOfDecimal)%></td>
	        				<td><div class="float-right"> 
									<input class="form-control text-right creditAmountentry" id=criditadd[<%=j%>] type="text" value="0"> 
								</div></td>
							
	        			</tr>	
	        			
        			<%} 
        			}%>
					</tbody>
				</table>
				<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Amount to Credit: </span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="creditTotalAmount">0.00</span>
		      		</div>
		      	</div>
		      	<br>
		      	<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Invoice Balance Due:</span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="invoiceBalanceAmountdue">00</span>
		      		</div>
		      	</div>
				</div>
				
				<div class="modal-footer">
	      		    	<div class="form-group">  
				    		<button type="button" class="btn btn-success" onClick="applyCredit()">Save</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
				    </div>
				</div>			
		</form>
	</div>
</div>


<div id="advancecredit" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="advanceForm" method="post">
		<input type="hidden" id="numberOfDecimaladvance" value=<%=numberOfDecimal%>>
		<input type="hidden" id="CURRENCYUSEQT" value=<%=CURRENCYUSEQT%>>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Apply credits for <%=(String) invoiceHdr.get("INVOICE")%></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Bill Balance: </span><b><%=curency%> <%=balduest%></b>
		      		</div>
		      	</div>
				<table class="table advanceTableelse">
					<thead>
						<tr>
							<th>Credit Note#</th>
							<th>Date</th>
							<th class="text-right">Amount</th>
							<th class="text-right">Balance</th>
							<th class="text-right">Amount to Apply</th>
						</tr>
					</thead>
					<tbody> 
					<%
						for(int j =0; j < advanceCriditAMOUNT.size(); j++) {
		  					Map creditDetail=(Map)advanceCriditAMOUNT.get(j);	  					
			  		%>
						<tr>
							
							<td hidden><%=invoiceHdr.get("DONO")%></td>
							<td hidden><%=(String)creditDetail.get("RECEIVEHDRID")%></td>
							<td hidden><%=invoiceHdrId%></td>
							<td hidden><%=(String)creditDetail.get("ID")%></td>
							<td><%=(String)creditDetail.get("REFERENCE")%></td>
	        				<td><%=(String)creditDetail.get("RECEIVE_DATE")%></td>
	        				<td><%=(String)creditDetail.get("AMOUNT")%></td>
	        				<td><%=(String)creditDetail.get("BALANCE")%></td>
	        				<td><div class="float-right"> 
									<input class="form-control text-right advanceAmountentry" id=advanceadd[<%=j%>] type="text" value="0"> 
								</div></td>
							
	        			</tr>	
        			<%} %>
					</tbody>
				</table>
				<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Amount to Credit: </span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="advanceTotalAmount">0.00</span>
		      		</div>
		      	</div>
		      	<br>
		      	<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Invoice Balance Due:</span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="invoiceBalAmountdue">00</span>
		      		</div>
		      	</div>
				</div>
				
				<div class="modal-footer">
	      		    	<div class="form-group">  
				    		<button type="button" class="btn btn-success" onClick="applyadvance()">Save</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
				    </div>
				</div>			
		</form>
	</div>
</div>
<script >


	$( ".creditAmountentry" ).change(function() {
		var numberOfDecimal = $("#numberOfDecimal").val();
		var  amount = $(this).val();
		amount = parseFloat(amount).toFixed(numberOfDecimal);
		$(this).val(amount);
		calculateTotal();
		});
	
	function calculateTotal(){
		var numberOfDecimal = $("#numberOfDecimal").val();
		var amount = 0, discountValue=0, adjustmentvalue=0,totalvalue=0;
		
		var flag=0;
		<%-- var invamount = <%=balduest%>; --%>
		<%-- var invamount = <%=conv_balduest%>; --%>
		var invamount = <%=(baldue/Double.parseDouble(CURRENCYUSEQT))%>;
		var orderbal = <%=orderbalance%>
		 
		if(invamount > orderbal){
			
			
			
			
			$(".creditListTable tbody tr").each(function() {
				 if($.isNumeric($('td:eq(8)', this).find('input').val())){
					
					 var aa = $('td:eq(7)', this).text();
					 var bb = $('td:eq(8)', this).find('input').val();
					 var diff = aa - bb;
					 if(diff < 0){
						 $('td:eq(8)', this).find('input').val("0");
						 
					 }else{
						 amount =  parseFloat(amount) + parseFloat($('td:eq(8)', this).find('input').val());
						 amount = parseFloat(amount).toFixed(numberOfDecimal);
					 }
				 }else{
					 $('td:eq(8)', this).find('input').val("0");
				 }
				 flag=1;
			});
			if(flag == 1){
				if(orderbal == amount){
					$("#statuscheck").find("input,button,textarea,select").removeAttr('disabled');
				}else{
					$(".advanceTable tbody tr").each(function() {
						 $('td:eq(10)', this).find('input').val("0");
					});
					$("#statuscheck").find("input,button,textarea,select").attr("disabled", "disabled");
				}
			}
			
			
			
			$(".advanceTable tbody tr").each(function() {
				 if($.isNumeric($('td:eq(10)', this).find('input').val())){
					 
					 var aa = $('td:eq(9)', this).text();
					 var bb = $('td:eq(10)', this).find('input').val();
					 var diff = aa - bb;
					 if(diff < 0){
						 $('td:eq(10)', this).find('input').val("0");
						
					 }else{
						 amount =  parseFloat(amount) + parseFloat($('td:eq(10)', this).find('input').val());
						 amount = parseFloat(amount).toFixed(numberOfDecimal);
					 }
				 }else{
					 $('td:eq(10)', this).find('input').val("0");
				 }
			});
			
			
			
			
			
			
			
			
			
			$("#creditTotalAmount").html(amount);
			
			var balaceamount = invamount-amount;
			
			
			if(balaceamount < 0){
				$(".advanceTable tbody tr td:last-child").each(function() {
					$(this).find('input').val("0");
					balaceamount = parseFloat(0).toFixed(numberOfDecimal);
				});
				$(".creditListTable tbody tr td:last-child").each(function() {
					$(this).find('input').val("0");
					balaceamount = parseFloat(0).toFixed(numberOfDecimal);
					
				});
				$("#invoiceBalanceAmountdue").html(balaceamount);
				calculateTotal();
				alert("Total Amount is exceeding Invoice balance.");
			}else{
				balaceamount = parseFloat(balaceamount).toFixed(numberOfDecimal);
				$("#invoiceBalanceAmountdue").html(balaceamount);
			}
		}else{
			$(".creditListTable tbody tr td:last-child").each(function() {
				 if($.isNumeric($(this).find('input').val())){
					 amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
					 amount = parseFloat(amount).toFixed(numberOfDecimal);
				 }else{
					 $(this).find('input').val("0");
				 }
				
			});
			$("#creditTotalAmount").html(amount);
			var balaceamount = invamount-amount;
			if(balaceamount < 0){
				$(".creditListTable tbody tr td:last-child").each(function() {
					$(this).find('input').val("0");
					balaceamount = parseFloat(0).toFixed(numberOfDecimal);
					$("#invoiceBalanceAmountdue").html(balaceamount);
					calculateTotal();
					alert("Total Amount is exceeding Invoice balance.");
				});
			}else{
				balaceamount = parseFloat(balaceamount).toFixed(numberOfDecimal);
				$("#invoiceBalanceAmountdue").html(balaceamount);
			}
		}
		
		
		
		
	}
	
	function applyCredit(){
		//Resvi start
		  var ValidNumber   = document.creditForm.ValidNumber.value;
		  if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
		// end

		var CURRENCYUSEQTs = $("#CURRENCYUSEQT").val();
		var prid = "0";
		var pinid = "0";
		var pordno = "0";
		var pcamot = "0";
		var pid = "0";
		
		
		
		$(".creditListTable tbody tr").each(function() {
			
			prid=$('td:eq(1)', this).text();
			pinid=$('td:eq(2)', this).text();
			pordno=$('td:eq(0)', this).text();
			pcamot=$('td:eq(8)', this).find('input').val();
			pid=$('td:eq(3)', this).text();
			
			if(pcamot != 0){
		  
				$.ajax({
					type : "POST",
					url : "/track/InvoiceCredit?cmd=Apply_Credit",
					async : true,
					data : {
						plant : "<%=plant%>",
						rid : prid,
						inid : pinid,
						ordno : pordno,
						camot : pcamot,
						pid : pid,
						CURRENCYUSEQT : CURRENCYUSEQTs,
					},
					dataType : "json"
					
				});
			}
		  
		});
		
		$(".advanceTable tbody tr").each(function() {
			
			prid=$('td:eq(1)', this).text();
			pinid=$('td:eq(2)', this).text();
			pordno=$('td:eq(0)', this).text();
			pcamot=$('td:eq(10)', this).find('input').val();
			pid=$('td:eq(3)', this).text();
			
			if(pcamot != 0){
		  
				$.ajax({
					type : "POST",
					url : "/track/InvoiceCredit?cmd=Apply_Credit",
					async : true,
					data : {
						plant : "<%=plant%>",
						rid : prid,
						inid : pinid,
						ordno : pordno,
						camot : pcamot,
						pid : pid,
						CURRENCYUSEQT : CURRENCYUSEQTs,
					},
					dataType : "json"
					
				});
			}
		  
		});
		//location.reload();
		window.location.href = "../invoice/detail?INVOICE_HDR="+<%=invoiceHdr.get("ID")%>;
	}
	
	
	
	
	
	
	
	$( ".advanceAmountentry" ).change(function() {
		
		calculateadvanceTotal();
		});
	
	function calculateadvanceTotal(){
		var numberOfDecimal = $("#numberOfDecimaladvance").val();
		var amount = 0, discountValue=0, adjustmentvalue=0,totalvalue=0;
		
		$(".advanceTable tbody tr td:last-child").each(function() {
			 if($.isNumeric($(this).find('input').val())){
				 amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
				 amount = parseFloat(amount).toFixed(numberOfDecimal);
			 }else{
				 $(this).find('input').val("0");
			 }
			
		});
		$("#advanceTotalAmount").html(amount);
		
		
		<%-- var invamount = <%=balduest%>; --%>
		var invamount = <%=baldue%>;
		
		var balaceamount = invamount-amount;
		
		if(balaceamount < 0){
			$(".advanceTable tbody tr td:last-child").each(function() {
				$(this).find('input').val("0");
				balaceamount = parseFloat(0).toFixed(numberOfDecimal);
				$("#invoiceBalAmountdue").html(balaceamount);
				calculateadvanceTotal();
			});
		}else{
			balaceamount = parseFloat(balaceamount).toFixed(numberOfDecimal);
			
			$("#invoiceBalAmountdue").html(balaceamount);
		}
	}
		
		function applyadvance(){
			
			var prid = "0";
			var pinid = "0";
			var pordno = "0";
			var pcamot = "0";
			var pid = "0";
			
			$(".advanceTable tbody tr").each(function() {
				
				prid=$('td:eq(1)', this).text();
				pinid=$('td:eq(2)', this).text();
				pordno=$('td:eq(0)', this).text();
				pcamot=$('td:eq(8)', this).find('input').val();
				pid=$('td:eq(3)', this).text();
				
				if(pcamot != 0){
			  
					$.ajax({
						type : "POST",
						url : "/track/InvoiceCredit?cmd=Apply_Credit",
						async : true,
						data : {
							plant : "<%=plant%>",
							rid : prid,
							inid : pinid,
							ordno : pordno,
							camot : pcamot,
							pid : pid
						},
						dataType : "json"
						
					});
				}
			  
			});
			
			location.reload();
			
		}
		
		function checkadvance(){
			var amcheck = <%=InvPaymentdao.getcreditamoutusingorderno(plant, (String) invoiceHdr.get("DONO")) %>;
			if(amcheck <= 0){
				$("#advancecredit").modal("show");
			}else{
				alert("Please Use Your Order Credit Amount");
			}
			
		}
		
		$( "#applycrd" ).click(function() {
			var numberOfDecimal = $("#numberOfDecimaladvance").val();
			var zeroamount = parseFloat(0).toFixed(numberOfDecimal);
			$(".advanceTable tbody tr td:last-child").each(function() {
				$(this).find('input').val(zeroamount);
				balaceamount = parseFloat(0).toFixed(numberOfDecimal);
			});
			$(".creditListTable tbody tr td:last-child").each(function() {
				$(this).find('input').val(zeroamount);
				balaceamount = parseFloat(0).toFixed(numberOfDecimal);
				
			});
			<%-- var balamount = '<%=balduest%>'; --%>
			var balamount = '<%=conv_balduest%>';
			$("#invoiceBalanceAmountdue").html(balamount);
			calculateTotal();
			});

		function isNumberKey(evt, element, id) {
			  var charCode = (evt.which) ? evt.which : event.keyCode;
			  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
				  {
			    	return false;
				  }
			  return true;
			}

</script>
<!-- PDF Print Start 4 -->
<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>
<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<!-- PDF Print End 4 -->