<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@page import="com.track.constants.*"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="java.util.Set"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%@include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! 
	@SuppressWarnings({"rawtypes", "unchecked"}) 
%>
<%
	String title = "Bill Detail";
	String rootURI = HttpUtils.getRootURI(request);
	boolean isInternalRequest = !"".equals(StrUtils.fString(request.getParameter("INTERNAL_REQUESET")));
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
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	String NOOFPAYMENT=StrUtils.fString((String) session.getAttribute("NOOFPAYMENT")); /* Resvi */
	if("".equals(NOOFPAYMENT) && isInternalRequest)
	{
		NOOFPAYMENT= StrUtils.fString(request.getParameter("NOOFPAYMENT"));
	}
	PlantMstUtil plantmstutil = new PlantMstUtil();
	String CURRENCYUSEQT="1";
	String basecurency =curency;
	String OrdValidNumber="";
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayApplyCredits=false,displayApplyRecords=false,displaySummaryMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editbill", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printbill", plant,username);
		displayApplyCredits = ub.isCheckValAcc("applycreditsbill", plant,username);
		displayApplyRecords = ub.isCheckValAcc("recordpaymentbill", plant,username);
		displaySummaryMore = ub.isCheckValAcc("morebill", plant,username);	
	}
	
	String VIEW_ORDERTYPE="";
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	BillUtil billUtil = new BillUtil();
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	//resvi starts
	BillPaymentDAO billDao = new BillPaymentDAO();
	String FROM_DATE = DateUtils.getDate();
	if (FROM_DATE.length() > 5)
		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
	
	String TO_DATE = DateUtils.getLastDayOfMonth();
	if (TO_DATE.length() > 5)
		TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
	
	int noordvalid =billDao.Paymentcount(plant,FROM_DATE,TO_DATE);
	if(!NOOFPAYMENT.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFPAYMENT);
		if(noordvalid>=convl)
		{
			OrdValidNumber=NOOFPAYMENT;
		}
	}
	
// 	ends
	SupplierCreditDAO supplierCreditDAO =new SupplierCreditDAO();
	double shingpercentage =0.0;
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String billHdrId = StrUtils.fString(request.getParameter("BILL_HDR"));
	Hashtable ht = new Hashtable();
	ht.put("ID", billHdrId);
	ht.put("PLANT", plant);
	List billHdrList =  billUtil.getBillHdrById(ht);
	
	if(billHdrList.size()>0)
	{}
	else
	{
			response.sendRedirect("../bill/summary?result=No Data Found");
		return;
	}
	Map billHdr=(Map)billHdrList.get(0);
	String isshiptax= (String) billHdr.get("ISSHIPPINGTAXABLE");
	
	ht.put("BILLHDRID", billHdrId);
	List billDetList =  billUtil.getBillDetByHdrId(ht);
	if(billDetList.size()>0)
	{
		Map mcut=(Map)billDetList.get(0);
		CURRENCYUSEQT = (String) mcut.get("CURRENCYUSEQT");
	}
	else
	{
		response.sendRedirect("../bill/summary?result=No Data Found");
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
	//imti
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
    
    
	VIEW_ORDERTYPE=(String)billHdr.get("ORDERTYPE");
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	String totalAmount= (String) billHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) billHdr.get("SUB_TOTAL");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);	
	
	String adjustment = (String) billHdr.get("ADJUSTMENT");
	double dAdjustment ="".equals(adjustment) ? 0.0d :  Double.parseDouble(adjustment);
	adjustment = StrUtils.addZeroes(dAdjustment, numberOfDecimal);
	String shipingcost = (String) billHdr.get("SHIPPINGCOST");
	double dshipingcost ="".equals(shipingcost) ? 0.0d :  Double.parseDouble(shipingcost);
	shipingcost = StrUtils.addZeroes(dshipingcost, numberOfDecimal);
	
	
	String orderdiscounttype = (String) billHdr.get("ORDERDISCOUNTTYPE");
	String orderdiscountcost = "0";
	String ordisc = "";
	if(orderdiscounttype.equalsIgnoreCase("%")){
		String orderdiscount = (String) billHdr.get("ORDER_DISCOUNT");
		double dorderdiscount ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
		ordisc = StrUtils.addZeroes(dorderdiscount, "3");
		double dorderdiscountcost = (dSubTotal/100)*dorderdiscount;
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}else{
		String orderdiscount = (String) billHdr.get("ORDER_DISCOUNT");
		double dorderdiscountcost ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}

	String discounttype = (String) billHdr.get("DISCOUNT_TYPE");
	String discount = "0";
	if(discounttype.equalsIgnoreCase("%")){
	    discount = (String) billHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		double pDiscount = ((dSubTotal)/100)*dDiscount;
		discount = StrUtils.addZeroes(pDiscount, numberOfDecimal);
	}else{
		discount = (String) billHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
	}
	curency = (String) billHdr.get("DISPLAY");
	
	
	ht.put("VENDNO", billHdr.get("VENDNO"));
	ht.put("BILLHDRID", billHdrId);
	List creditDetailList = billDao.getCreditDetails(ht);
	ht.put("PONO", billHdr.get("PONO"));

	String balanceAmount = billDao.getBalanceAmountByBill(ht);
	double dBalanceAmount ="".equals(balanceAmount) ? 0.0d :  Double.parseDouble(balanceAmount);
	String conv_balanceAmount=balanceAmount;
	conv_balanceAmount= StrUtils.addZeroes(Float.parseFloat(conv_balanceAmount), numberOfDecimal);
	balanceAmount = StrUtils.addZeroes(((dBalanceAmount) * Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
	
	String paymentMade = billDao.getConvpaymentMadeyBillwithbillno(ht);
	String paymentMadeloc =paymentMade;
	paymentMade = StrUtils.addZeroes((Float.parseFloat(paymentMade) * Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
	double dPaymentMade ="".equals(paymentMade) ? 0.0d :  Double.parseDouble(paymentMade);
	paymentMade = StrUtils.addZeroes(dPaymentMade, numberOfDecimal);
	
	double dbalanceDue = dTotalAmount - dPaymentMade;
	String balanceDue = StrUtils.addZeroes(dbalanceDue, numberOfDecimal);
	
	boolean creditFlag = false;
	String showCreditBtn = "", showPaymentBtn = "",showConvToDftBtn= "style='display:none;'", showConvToOpnBtn = "style='display:none;'";
	if(creditDetailList.size() <= 0){
		showCreditBtn = "style='display:none;'";
	}
	if( ((String)billHdr.get("BILL_STATUS")).equalsIgnoreCase("Draft")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
		showConvToOpnBtn = "";
	}
	if( ((String)billHdr.get("BILL_STATUS")).equalsIgnoreCase("Paid")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
	}
	if( ((String)billHdr.get("BILL_STATUS")).equalsIgnoreCase("CANCELLED")){
		showCreditBtn = "style='display:none;'";
		showPaymentBtn = "style='display:none;'";
		showConvToOpnBtn = "style='display:none;'";
		showConvToDftBtn="";
	}
	/* double cpayment = 0;
	for(int j =0; j < creditDetailList.size(); j++) {
		Map creditDetail=(Map)creditDetailList.get(j);
		String balforcheck = (String)creditDetail.get("BALANCE");
		double dbalforcheck ="".equals(balforcheck) ? 0.0d :  Double.parseDouble(balforcheck);
		cpayment = cpayment + dbalforcheck;
	}
	if(cpayment > 0){
		creditFlag = true;
	}else{
		creditFlag = false;
	} */
	  String showtax = "";
	  if(isshiptax.equalsIgnoreCase("1")){
		 showtax = "(Tax Inclusive)";
	  }else{
		  showtax = "(Tax Exclusive)";
	  } 
	  
	String isdisctax= (String) billHdr.get("ISDISCOUNTTAX");
	String isoddisctax= (String) billHdr.get("ISORDERDISCOUNTTAX");
	
	String showdsictax ="";
	String showorddisctax ="";
	
	 if(isdisctax.equalsIgnoreCase("1")){
		 showdsictax = "(Tax Inclusive)";
	  }else{
		  showdsictax = "(Tax Exclusive)";
	  } 
	 
	 if(isoddisctax.equalsIgnoreCase("1")){
		 showorddisctax = "(Tax Inclusive)";
	  }else{
		  showorddisctax = "(Tax Exclusive)";
	  } 
	String taxtodiaplay = "";
	String taxamount = (String) billHdr.get("TAXAMOUNT");
	double ptaxamount ="".equals(taxamount) ? 0.0d :  Double.parseDouble(taxamount);
	taxamount = StrUtils.addZeroes(ptaxamount, numberOfDecimal);
	
	String taxidsting = (String) billHdr.get("TAXID");
	int taxid = Integer.valueOf(taxidsting);
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(Integer.valueOf((String) billHdr.get("PROJECTID")) > 0){
		finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String) billHdr.get("PROJECTID")));
		projectname = finProject.getPROJECT_NAME();
	}
	String transportmode="";
	TransportModeDAO transportmodedao = new TransportModeDAO();
	transportmode = (String)billHdr.get("TRANSPORTID");
	int trans = Integer.valueOf(transportmode);
	if(trans > 0){
		transportmode = transportmodedao.getTransportModeById(plant,trans);
	}else{
		transportmode = "";
	}
	
	String  bill = (String) billHdr.get("BILL");
	
	ArrayList SupplyDetails = new ArrayList();	
	SupplyDetails = new CustUtil().getVendorDetailsForBILL(plant, bill);
	String customerregno = (String) SupplyDetails.get(25);
	
	BillDAO billDAO = new BillDAO();
    Map mpro= billDAO.getBillHeaderDetails(plant);
    String prolable = (String) mpro.get("PROJECT");
	int prostatus = Integer.valueOf((String) mpro.get("PRINTWITHPROJECT"));
	String transmode = (String) mpro.get("TRANSPORT_MODE");
	int printwithtransportmode = Integer.valueOf((String) mpro.get("PRINTWITHTRANSPORT_MODE"));
	String Terms = (String) mpro.get("PAYMENTTERMS");
	String billheader = (String) mpro.get("BILLHEADER");
	String billtoheader = (String) mpro.get("TOHEADER");
	String grno = (String) mpro.get("GRNO");
	String billdate = (String) mpro.get("BILLDATE");
	String duedate = (String) mpro.get("DUEDATE");
    String  employee=(String)mpro.get("EMPLOYEE");

	
	String SubTotal = (String) mpro.get("SUBTOTAL");
	String orderdiscount=(String)mpro.get("ORDERDISCOUNT");
	String Discount = (String) mpro.get("DISCOUNT");
	String shippingcost=(String)mpro.get("SHIPPINGCOST");
	String TotalTax = (String) mpro.get("TAX");
	String Adjustment = (String) mpro.get("ADJUSTMENT");
	String Total = (String) mpro.get("TOTAL");
	String PaymentsMade = (String) mpro.get("PAYMENTMADE");
	String BalanceDue = (String) mpro.get("BALANCEDUE");
	int checkordertype=Integer.valueOf((String) mpro.get("DISPLAYBYORDERTYPE"));
     
	String remarks = (String) mpro.get("NOTES");
	String tranno = (String) mpro.get("RCBNO");
	String suppliertranno = (String) mpro.get("SUPPLIERRCBNO");
	String compsign = (String) mpro.get("COMPANYSIG");
	String compseal = (String) mpro.get("COMPANYSTAMP");
	String compname = (String) mpro.get("COMPANYNAME");
	String compdate = (String) mpro.get("COMPANYDATE");
	PRINTWITHCOMPANYSEAL = (String)mpro.get("PRINTWITHCOMPANYSEAL");
	String printEmployee =(String)mpro.get("PRINTEMPLOYEE");
    DISPLAYSIGNATURE= (String) mpro.get("PRINTWITHCOMPANYSIG");
    if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
         sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
         }
		if(DISPLAYSIGNATURE.equalsIgnoreCase("0")){
			signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
         }
// 	String terms = (String) mpro.get("PAYMENTTERMS");
	String orderno = (String) mpro.get("ORDERNO");
	String uenno =  (String) mpro.get("UENNO");//imtiuen
	String supplieruenno = (String) mpro.get("SUPPLIERUENNO");//imtiuen
	int printuenno = Integer.valueOf((String) mpro.get("PRINTWITHUENNO"));//imtiuen
	int printsupplieruenno = Integer.valueOf((String) mpro.get("PRINTWITHSUPPLIERUENNO"));//imtiuen
	
	
	if (!isInternalRequest){
%>
<%@include file="sessionCheck.jsp" %>
<%
	}
%>
<!-- PDF Print Start 1 -->
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.BILL%>"/>
</jsp:include>
<script src="<%=rootURI%>/jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css"></link>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css"></link>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/font-awesome.min.css"></link>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/tabulator_bootstrap.min.css" />
<style>
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

#footerTable>tbody>tr>td {
	border: none;
}
#footerTable{
	display:none;
}

/* @media print {
  @page { margin: 0; }
  body { margin: 0.5cm; }
  
} */

@media print {
 /*  @page { margin: 0; } */
  body { margin: 1cm 1.6cm 1.6cm 1.6cm; }
  #footerTable{
	display:table !important;
  }  
}

</style>
<!-- PDF Print End 1 -->
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
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
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../bill/summary"><span class="underline-on-hover">Bill Summary</span> </a></li>                                 
                <li><label>Bill Detail</label></li>                                   
            </ul>             
       <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				 <% if (displaySummaryEdit) { %>
					<button type="button" class="btn btn-default" 
					onclick="window.location.href='../bill/edit?action=View&BILL_HDR=<%=billHdrId%>'" 
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					 <% } %>
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
					 <%} }%>
<%-- 					  <% if (displayPdfPrint) { %> --%>
<!-- 					<button type="button" class="btn btn-default printMe"  -->
<!-- 					 data-toggle="tooltip"  data-placement="bottom" title="Print"> -->
<!-- 						<i class="fa fa-print" aria-hidden="true"></i> -->
<!-- 					</button> -->
<%-- 					<% } %> --%>
				</div>
				&nbsp;
				
				<% if (displayApplyCredits) { %>
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default pull-right" 
					data-toggle="modal" data-target="#creditListModal" id="applycrd" <%=showCreditBtn%>>Apply Credits</button>
				</div>
				&nbsp;
				<% } %>
				<% if (displayApplyRecords) { %>
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-success pull-right" onclick="advPayment()" <%=showPaymentBtn%>>Record Payment</button>
				</div>
				&nbsp;
				<% } %>
				<%--
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" onclick="convertToOpen()" <%=showConvToOpnBtn%>>Convert to Open</button>
				</div>
				--%>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-success pull-right" onclick="convertToDraft()" <%=showConvToDftBtn%>>Convert to Draft</button>
				</div>
				<% if (displaySummaryMore) { %>
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					   	<li id="bill-copy"><a href="../bill/copy?action=View&BILL_HDR=<%=billHdrId%>">Copy</a></li>
					    <li id="bill-cancel"><a href="#">Cancel</a></li>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					  </ul>
				</div>
				&nbsp;
				<% } %>
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../bill/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
		<%
			if (!isInternalRequest){
		%>
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=billHdr.get("BILL_STATUS")%></div>
		</div>
		<%
			}
		%>
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
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
<%if(printuenno == 1){ %><%=uenno%> :<%=companyregnumber.trim()%><br><%} %><%=tranno%>: <%=RCBNO.trim()%>
Contact: <%=NAME.trim()%>
Tel: <%=TELNO.trim()%> Fax : <%=FAX.trim()%>
Email: <%=EMAIL.trim()%>
</span>
			</div>

			<div class="col-xs-6 text-right">
			<%if(checkordertype == 1){%>
				 <h2><%=VIEW_ORDERTYPE%></h2>
				 <%}else{ %>
				 <h2><%=billheader%></h2>
				 <%} %>
			
			   
				
				<p>#<%=billHdr.get("BILL")%></p>
			<%if(billHdr.get("GRNO").equals("")|| billHdr.get("GRNO") == null){ %>
			
			<%}else{ %>
				<h2><%=grno%></h2>
				<p>#<%=billHdr.get("GRNO")%></p>
				<h2><%=orderno%></h2>
				<p>#<%=billHdr.get("PONO")%></p>
			<%} %>
				
				<br> <span><%=BalanceDue%></span>
				<h3><%=curency%><%=Numbers.toMillionFormat(balanceDue, Integer.valueOf(numberOfDecimal))%></h3>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-8">
				<span style="font-size: 24px;"><b><%=billtoheader%></b></span><br>	
				<span style="font-size: 14px;">
<span><%=SupplyDetails.get(1)%></span><br>	
<%if(printsupplieruenno == 1){ %><span><%=supplieruenno%> : <%=SupplyDetails.get(25)%></span><br><%}%>
<%if(((String)SupplyDetails.get(24)).length()>0){%>
<span><%=suppliertranno%> <%=SupplyDetails.get(24)%></span><br>
 <% } %>
<%if(((String)SupplyDetails.get(2)).length()>0 || ((String)SupplyDetails.get(3)).length()>0){%>
<span><%=SupplyDetails.get(2)%> <%=SupplyDetails.get(3)%></span><br>	 <!-- Block Address (Add1 + Add2) -->
 <% } %>
<%if(((String)SupplyDetails.get(4)).length()>0 || ((String)SupplyDetails.get(15)).length()>0){%>
<span><%=SupplyDetails.get(4)%> <%=SupplyDetails.get(15)%></span><br>	 <!-- Road Address (Add3 + Add4) -->
 <% } %>
<%if(((String)SupplyDetails.get(22)).length()>0){%>
<%=SupplyDetails.get(22)%></span><br>	 <!-- State -->
 <% } %>
<span><%=SupplyDetails.get(5)%> <%=SupplyDetails.get(6)%></span><br>	 <!-- Country + Zip -->
<%if(((String)SupplyDetails.get(12)).length()>0){%>
<span>Email: <%=SupplyDetails.get(12)%></span><br>	
 <% } %>
<%if(((String)SupplyDetails.get(15)).length()>0){%>
<span><%=remarks%>: <%=SupplyDetails.get(15)%></span><br>
 <% } %>


<%if(printwithtransportmode == 1){ %>
<span><%=transmode%> : <%=transportmode %></span>	<br>
<%} %>	

<%if(prostatus == 1){ %>
<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
<span><%=prolable%> : <%=projectname %></span>	<br>
<%} }%>		
</span>		
			</div>
			<div class="col-xs-4 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td><%=billdate%></td>
							<td><%=billHdr.get("BILL_DATE")%></td>
						</tr>
						<%-- <tr>
							<td>Project :</td>
							<td><%=projectname%></td>
						</tr> --%>
						<tr>
							<td><%=duedate%></td>
							<td><%=billHdr.get("DUE_DATE")%></td>
						</tr>
						<%if(printEmployee.equals("1") && ((String)billHdr.get("EMP_NAME")).length() > 0){%>
						<tr>
							<td><%=employee%></td>
							<td><%=billHdr.get("EMP_NAME")%></td>
						</tr>
					<%} %>
						<tr>
							<td><%=Terms%></td>
							<td><%=billHdr.get("PAYMENT_TERMS")%></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td class="text-center">#</td>
							<td class="text-center">Product ID</td>
							<td class="text-center">Qty</td>
							<td class="text-center">UOM</td>
							<td class="text-center">Rate</td>
							<td class="text-center">Discount</td>
							<td class="text-right">Amount</td>
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<billDetList.size(); i++) {   
				  		Map m=(Map)billDetList.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = StrUtils.addZeroes(dQty, "3");
			  			
			  			cost = (String) m.get("COST");
			  			double dCost ="".equals(cost) ? 0.0d :  Double.parseDouble(cost);
			  			cost = StrUtils.addZeroes(dCost, numberOfDecimal);
			  			
			  			amount = (String) m.get("AMOUNT");
			  			double dAmount ="".equals(amount) ? 0.0d :  Double.parseDouble(amount);
			  			amount = StrUtils.addZeroes(dAmount, numberOfDecimal);
			  			if(!((String)m.get("TAX_TYPE")).equalsIgnoreCase("")){
			  			//percentage = (String)m.get("GSTPERCENTAGE");
			  			Map<String, String> taxDetail = new HashMap();
			  			boolean match = false;
			  			String display = (String)m.get("TAX_TYPE");
			  			taxtodiaplay = (String)m.get("TAX_TYPE");
			  			/* percentage = display.substring(display.indexOf("[")+1, display.lastIndexOf("%"));
			  			double dPercntage = "".equals(cost) ? 0.0d :  Double.parseDouble(percentage);
			  			shingpercentage = dPercntage;
			  			double dTax = (dAmount * (dPercntage / 100));
			  			tax = StrUtils.addZeroes(dTax, numberOfDecimal);
			  					
			  			
			  			taxDetail.put("name", display);
			  			taxDetail.put("types", (String)m.get("TAX_TYPE"));
			  			taxDetail.put("tax", tax);
			  			
			  			if(taxList.size() == 0){
			  				taxList.add(taxDetail);
			  			}else{
			  				for(int j =0; j < taxList.size(); j++) {
			  					Map tMap=(Map)taxList.get(j);
			  					if(((String)tMap.get("types")).equalsIgnoreCase((String)m.get("TAX_TYPE"))){			  						
			  						dTax = dTax + Double.parseDouble((String)tMap.get("tax"));
			  						tax = StrUtils.addZeroes(dTax, numberOfDecimal);
			  						tMap.put("tax", tax);
			  						match = true;
			  					}
			  				}
			  				if(!match){
				  				taxList.add(taxDetail);
				  			}
			  			} */
		  			}
			  			String linediscount = (String)m.get("DISCOUNT");
			  			String linediscounytype = (String) m.get("DISCOUNT_TYPE");
			  			if(linediscounytype.equalsIgnoreCase("%")){
			  				linediscount = StrUtils.addZeroes(Double.parseDouble(linediscount), "3");	
			  			}else{
			  				linediscount = StrUtils.addZeroes(Double.parseDouble(linediscount), numberOfDecimal);	
			  			}
			  			
			  		%>
				  		<tr>
							<td class="text-center"><%=m.get("LNNO") %></td>
							<td class="text-center"><%=m.get("ITEM") %></td>
							<td class="text-center"><%=qty%></td>
							<td class="text-center"><%=m.get("UOM")%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(cost,Integer.valueOf(numberOfDecimal))%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(linediscount,Integer.valueOf(numberOfDecimal))%><%=linediscounytype%></td>
							<td class="text-right"><%=Numbers.toMillionFormat(amount,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr>
          					<td></td>
          					<td colspan="6">&emsp;&emsp;&emsp;<%=m.get("ITEMDESC") %></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<!-- <div class="col-xs-6"></div> -->
			<div class="col-xs-12">
				<table id="table3" class="table text-right" style="margin-bottom: 0px;">
					<tbody>
						<tr>
							<td><%=SubTotal%></td>
							<td><%=Numbers.toMillionFormat(subTotal,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%if(orderdiscounttype.equalsIgnoreCase("%")){ %>
						<tr>
							<td><%=orderdiscount%> (<%=ordisc%>%) <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%}else{ %>
						<tr>
							<td><%=orderdiscount%> <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%} %>
						<tr>
							<td><%=Discount%> <%=showdsictax%></td>
							<td>(-) <%=Numbers.toMillionFormat(discount,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr>
							<td><%=shippingcost%> <%=showtax%></td>
							<td><%=Numbers.toMillionFormat(shipingcost,Integer.valueOf(numberOfDecimal))%></td>
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
								double dTaxship = 0.0;
								if(isshiptax.equalsIgnoreCase("1")){
									dTaxship = (Double.parseDouble(shipingcost) * (shingpercentage / 100));
								}
								double dTaxorderdiscount = (Double.parseDouble(orderdiscountcost) * (shingpercentage / 100));
								double dTaxdiscout = (Double.parseDouble(discount) * (shingpercentage / 100));
								
								double dtotaltaxship = (Double.parseDouble((String)tMap.get("tax")) + dTaxship)-(dTaxorderdiscount+dTaxdiscout);
								String totaltaxship  = StrUtils.addZeroes(dtotaltaxship, numberOfDecimal);
								
						%>
							<tr>
								<td><%=(String)tMap.get("name")%></td>
								<td><%=totaltaxship%></td>
							</tr>
						<%}
							}
						}
						%> --%>
						<%if(taxid != 0){if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxtodiaplay%></td>
											<td><%=Numbers.toMillionFormat(taxamount,Integer.valueOf(numberOfDecimal))%></td>
										</tr>
						<%} }%>
						<tr>
							<td><%=Adjustment%></td>
							<td><%=adjustment%></td>
						</tr>
						<tr style="font-size:14px;">
							<td><b><%=Total%> (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount,Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 30,2021  Description:  Total of Local Currency -->
						<tr style="font-size:14px;" id="showtotalcur">
							<td><b><%=Total%> (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(StrUtils.addZeroes((dTotalAmount/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%}%>
						<tr>
							<td><%=PaymentsMade%> (<%=billHdr.get("CURRENCYID")%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(paymentMade,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%>
						<tr id="showtotalPay">
							<td><%=PaymentsMade%> (<%=basecurrency%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(StrUtils.addZeroes(Double.parseDouble(paymentMadeloc), numberOfDecimal),Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%}%>
						<tr class="grey-bg" style="font-size:14px;">
							<td><b><%=BalanceDue%> (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balanceDue,Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%>
						<tr class="grey-bg" style="font-size:14px;" id="showtotalBal">
							<td><b><%=BalanceDue%> (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(StrUtils.addZeroes((dbalanceDue/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%}%>						
					</tbody>
				</table>
				<table id="table4" class="table text-right" style="display: none"> <!--  Author: Azees  Create date: July 30,2021  Description:  Total of Local Currency -->
					<tbody>
						<tr>
							<td><%=SubTotal%></td>
							<td><%=Numbers.toMillionFormat(subTotal,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%if(orderdiscounttype.equalsIgnoreCase("%")){ %>
						<tr>
							<td><%=orderdiscount%> (<%=ordisc%>%) <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%}else{ %>
						<tr>
							<td><%=orderdiscount%> <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%} %>
						<tr>
							<td><%=Discount%> <%=showdsictax%></td>
							<td>(-) <%=Numbers.toMillionFormat(discount,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr>
							<td><%=shippingcost%> <%=showtax%></td>
							<td><%=Numbers.toMillionFormat(shipingcost,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%if(taxid != 0){if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxtodiaplay%></td>
											<td><%=Numbers.toMillionFormat(taxamount,Integer.valueOf(numberOfDecimal))%></td>
										</tr>
						<%} }%>
						<tr>
							<td><%=Adjustment%></td>
							<td><%=Numbers.toMillionFormat(adjustment,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr style="font-size:20px;">
							<td><b><%=Total%> (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount,Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<tr>
							<td><%=PaymentsMade%> (<%=billHdr.get("CURRENCYID")%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(paymentMade,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr class="grey-bg" style="font-size:20px;">
							<td><b><%=BalanceDue%> (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balanceDue,Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
					</tbody>
				</table>
		<!-- PDF Print End 2 -->
				<table id="tablesign" class="table" >
							<tbody style="text-align: right;" >
								<tr>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 130.00px;"  id="signimg"></td>
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
									<td class="col-xs-2 text-left"><%=compname%></td> 
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=sealPath%>" style="width: 130.00px;" id="sealimg"></td>
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
						<td colspan="7"></td>
					</tr>		
					<tr>
						<td colspan="7"></td>
					</tr>		
					<tr>
						<td colspan="7"></td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 100px; height: 50px;"></td>
						<td class="no-padding"></td>
					</tr>		
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=compsign%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="col-xs-2 text-left"><%=compname%><br><br></td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"></td>
						<td class="no-padding"></td>
						<td colspan="3" class="col-xs-2 text-left"><img src="<%=rootURI%>/ReadFileServlet?fileLocation=<%=sealPath%>" style="width: 110px; "></td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding" style="vertical-align:middle;margin:45px 6px">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=compdate%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="col-xs-2 text-left"><%=compseal%></td>
					</tr>
				</table>
				</footer>
				<!-- PDF Print Start 3 -->
			</div>
		</div>
		</div>
		<!-- PDF Print End 3 -->
		</div>
		
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
									<small class="text-muted">Amount is displayed in your bill currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=billHdr.get("CURRENCYID")%></span>
									<!---->
								</div>
								<p class="font-large">
									<b>  <!---->
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
	</div>

	<div id="creditListModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<form name="creditForm" method="post">
			<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
			<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>"> <%--Resvi--%>
			<input type="hidden" id="CURRENCYUSEQT" value=<%=CURRENCYUSEQT%>>
				<div class="modal-content">
					<div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal">&times;</button>
				        <h4 class="modal-title">Apply credits for <%=billHdr.get("BILL")%></h4>
			      	</div>
			      	<div class="modal-body">
			      	<div class="row">
			      		<div class="col-sm-12 text-right">
			      			<span>Bill Balance: </span><b><%=basecurency%> <%=conv_balanceAmount%> / <%=curency%> <%=balanceAmount%></b>
			      		</div>
			      	</div>
			      	<h4>General Credit</h4>
					<table class="table creditListTable">
						<thead>
							<tr>
								<th>Reference#</th>
								<th>Bill</th>
								<th>Order Number</th>
								<th>Date</th>
								<th class="text-right">Amount</th>
								<th class="text-right">Balance</th>
								<th class="text-right">Amount to Apply</th>
							</tr>
						</thead>
						<tbody> 
						<%
							for(int j =0; j < creditDetailList.size(); j++) {
			  					Map creditDetail=(Map)creditDetailList.get(j);
			  					
			  					String adfrom = (String) creditDetail.get("ADVANCEFROM");
			  					String pono = (String) creditDetail.get("PONO");
			  					System.out.println("-----");
			  					System.out.println(pono);
			  					System.out.println("-----");
			  					
			  					if(pono.equals("") || pono == null){
			  						
			  						String reference ="Excess Payment";
			  						String ponocredit = "";
			  						String billcredit = "";
				  					if(adfrom.equalsIgnoreCase("GENERAL")){
				  						reference = (String) creditDetail.get("REFERENCE");
				  						List creditnotelist = supplierCreditDAO.getSupplierCrdnotebycreditnote(plant, reference);
				  						Map cnotebymap = (Map) creditnotelist.get(0);
				  						ponocredit = (String) cnotebymap.get("PONO");
				  						billcredit = (String) cnotebymap.get("BILL");
				  					}
			  					
			  					/* String reference = (String) creditDetail.get("REFERENCE");
			  					reference = (reference.equalsIgnoreCase("")) ? "Excess Payment" : reference; */
			  					String creditPono = (String)creditDetail.get("PONO");
			  					if(!creditPono.equalsIgnoreCase("")){
			  						creditFlag = true;
			  					}	  	
			  					
			  					String cAmount = (String)creditDetail.get("AMOUNT");
			  					double dcAmount ="".equals(cAmount) ? 0.0d :  Double.parseDouble(cAmount);
			  					cAmount = StrUtils.addZeroes(dcAmount, numberOfDecimal);
			  					
			  					String cbalAmount = (String)creditDetail.get("BALANCE");
			  					double dcbalAmount ="".equals(cbalAmount) ? 0.0d :  Double.parseDouble(cbalAmount);
			  					cbalAmount = StrUtils.addZeroes(dcbalAmount, numberOfDecimal);
			  					
				  		%>
					  		<tr>
					  			<td hidden><%=billHdr.get("ID")%></td>
					  			<td hidden><%=billHdr.get("PONO")%></td>
					  			<td hidden><%=creditDetail.get("PAYHDRID")%></td>
					  			<td><%=reference%></td> 
					  			<td><%=billcredit%></td> 
					  			<td><%=ponocredit%></td> 
					  			<td><%=creditDetail.get("PAYMENT_DATE")%> </td>
					  			<td class="text-right"><%=cAmount%></td> 
								<td class="text-right"><%=cbalAmount%></td>
								<td> 
									<div class="float-right"> 
										<input class="form-control text-right creditAmount creditAmountvaluec" type="text" 
										data-balance="<%=creditDetail.get("BALANCE")%>" value="0.00" disabled> 
									</div>
								</td>
								<td hidden><%=creditDetail.get("ID")%></td>
					  		</tr>
				  		<%			
			  					}
			  					
							}
						 %>
						</tbody>
					</table>
					<h4>Order Credit</h4>
					<table class="table ordercreditListTable">
						<thead>
							<tr>
								<th>Reference#</th>
								<th>Date</th>
								<th class="text-right">Amount</th>
								<th class="text-right">Balance</th>
								<th class="text-right">Amount to Apply</th>
							</tr>
						</thead>
						<tbody> 
						<%
							double balaceorderamt = 0.0;
							for(int j =0; j < creditDetailList.size(); j++) {
			  					Map creditDetail=(Map)creditDetailList.get(j);
			  					
								String adfrom = (String) creditDetail.get("ADVANCEFROM");
			  					
								String pono = (String) creditDetail.get("PONO");
			  					
			  					if(pono.equals("") || pono == null){
			  					}else{
			  						if(billHdr.get("PONO").equals(pono)){
			  							
				  						/* String reference = (String) creditDetail.get("REFERENCE");
					  					reference = (reference.equalsIgnoreCase("")) ? "Excess Payment" : reference; */
				  					String creditPono = (String)creditDetail.get("PONO");
				  					if(!creditPono.equalsIgnoreCase("")){
				  						creditFlag = true;
				  					}	  	
				  					
				  					String cAmount = (String)creditDetail.get("AMOUNT");
				  					double dcAmount ="".equals(cAmount) ? 0.0d :  Double.parseDouble(cAmount);
				  					cAmount = StrUtils.addZeroes(dcAmount, numberOfDecimal);
				  					
				  					String cbalAmount = (String)creditDetail.get("BALANCE");
				  					double dcbalAmount ="".equals(cbalAmount) ? 0.0d :  Double.parseDouble(cbalAmount);
				  					cbalAmount = StrUtils.addZeroes(dcbalAmount, numberOfDecimal);
				  					balaceorderamt = balaceorderamt +dcbalAmount;
			  					
				  		%>
					  		<tr>
					  			<td hidden><%=billHdr.get("ID")%></td>
					  			<td hidden><%=billHdr.get("PONO")%></td>
					  			<td hidden><%=creditDetail.get("PAYHDRID")%></td>
					  			<td><%=creditPono%></td> 
					  			<td><%=creditDetail.get("PAYMENT_DATE")%> </td>
					  			<td class="text-right"><%=cAmount%></td> 
								<td class="text-right"><%=cbalAmount%></td>
								<td> 
									<div class="float-right"> 
										<input class="form-control text-right creditAmount creditAmountvalue" type="text" 
										data-balance="<%=creditDetail.get("BALANCE")%>" value="0.00"> 
									</div>
								</td>
								<td hidden><%=creditDetail.get("ID")%></td>
					  		</tr>
				  		<%	
			  						}
			  				}
			  			}
						 %>
						</tbody>
					</table>
					<div class="row">
			      		<div class="col-sm-offset-4 col-sm-6 text-right">
			      			<span>Total Amount Applied: </span>
			      		</div>
			      		<div class="col-sm-2 text-right">
			      			<span id="creditTotalAmount">0.00</span>
			      		</div>
			      	</div>
			      	<br>
			      	<div class="row">
			      		<div class="col-sm-offset-4 col-sm-6 text-right">
			      			<span>Bill Balance: </span>
			      		</div>
			      		<div class="col-sm-2 text-right">
			      			<%-- <span id="creditBalanceAmount"><%=balanceAmount%></span> --%>
			      			<span id="creditBalanceAmount"><%=conv_balanceAmount%></span>
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
	
	<div id="cancelbill" class="modal fade" role="dialog">
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
				      <p> Are you sure about cancel the bill?</p>
				      
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
	
	<div id="deletebill" class="modal fade" role="dialog">
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
			      <p> Deleted Bill information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
</div>	

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

		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 2000);
	  var numberOfDecimal = $("#numberOfDecimal").val();
	  $('[data-toggle="tooltip"]').tooltip();

	  $('.printMe').click(function(){
		  <%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 30,2021  Description:  Total of Local Currency -->
		  document.getElementById('showtotalcur').style.display = 'none';
		  document.getElementById('showtotalPay').style.display = 'none';
		  document.getElementById('showtotalBal').style.display = 'none';
		  <%}%>
		     $("#print_id").print({
		        	globalStyles: true,
		        	mediaPrint: false,
		        	stylesheet: null,
		        	noPrintSelector: ".no-print",
		        	iframe: false,
		        	append: null,
		        	prepend: null,
		        	manuallyCopyFormValues: true,
		        	deferred: $.Deferred(),
		        	timeout: 750,
		        	title: " ",
		        	doctype: '<!doctype html>'
			});		     
		     <%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 30,2021  Description:  Total of Local Currency -->
		     document.getElementById('showtotalcur').style.display = 'contents';
		     document.getElementById('showtotalPay').style.display = 'contents';
		     document.getElementById('showtotalBal').style.display = 'contents';
			  <%}%>	  
		});

	  var balcheck= "<%=balaceorderamt%>";
	 
	  if(parseFloat(balcheck) === parseFloat("0")){
		 
		  $( ".creditAmountvaluec" ).prop( "disabled", false );
	  }
	  
	  $("#bill-cancel").click(function() {    
		  var billHdrId = "<%=billHdr.get("ID")%>";
		  var bill = "<%=billHdr.get("BILL")%>";
		  var billstatus = "<%=billHdr.get("BILL_STATUS")%>";
		  var taxstatus = "<%=billHdr.get("TAX_STATUS")%>";
		  var result="";
		  if(billstatus.toLowerCase() == "open" || billstatus.toLowerCase() == "draft" ) {
				if(taxstatus.toLowerCase() == "tax generated"){
					result = "Transaction that already filed Tax Return not allow to Cancel.";
					window.location.href = "billDetail.jsp?BILL_HDR="+billHdrId+"&resultnew="+ result;
				}else {
					$('#cancelbill').modal('show');
				}
			}else if(billstatus.toLowerCase() == "cancelled"){
				result = "Bill that already cancelled";
				window.location.href = "billDetail.jsp?BILL_HDR="+billHdrId+"&resultnew="+ result;
			}else {
				result = "Bill already marked as 'PAID or PARTIALLY PAID'not allow to cancel.";
				window.location.href = "billDetail.jsp?BILL_HDR="+billHdrId+"&resultnew="+ result;
			}
		  
	  });
	  
	  $("#cfmcancel").click(function(){
		  window.location.href = "/track/BillingServlet?Submit=convertToCancel&billHdrId=<%=billHdr.get("ID")%>&bill=<%=billHdr.get("BILL")%>&status=<%=billHdr.get("BILL_STATUS")%>&taxstatus=<%=billHdr.get("TAX_STATUS")%>";
		});
		   
	 $("#bill-delete").click(function() {    
		 var billHdrId = "<%=billHdr.get("ID")%>";
		  var bill = "<%=billHdr.get("BILL")%>";
		  var billstatus = "<%=billHdr.get("BILL_STATUS")%>";
		  var taxstatus = "<%=billHdr.get("TAX_STATUS")%>";
		  var result="";
		  if(billstatus.toLowerCase() == "open" || billstatus.toLowerCase() == "draft" || billstatus.toLowerCase() == "cancelled") {
				if(taxstatus.toLowerCase() == "tax generated"){
					result = "Transaction that already filed Tax Return not allow to Delete.";
					//window.location.href = "billDetail.jsp?BILL_HDR="+billHdrId+"&resultnew="+ result;
					window.location.href = "../bill/detail??BILL_HDR="+billHdrId+"&resultnew="+ result;
				}else {
					$('#deletebill').modal('show');
				}
			}else {
				result = "Bill already marked as 'PAID or PARTIALLY PAID'not allow to delete.";
				//window.location.href = "billDetail.jsp?BILL_HDR="+billHdrId+"&resultnew="+ result;
				window.location.href = "../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result;
			}
		  
	 });
	 
	 $("#cfmdelete").click(function(){
		 window.location.href = "/track/BillingServlet?Submit=deletebill&billHdrId=<%=billHdr.get("ID")%>&bill=<%=billHdr.get("BILL")%>&status=<%=billHdr.get("BILL_STATUS")%>&taxstatus=<%=billHdr.get("TAX_STATUS")%>";
		});
	  
	  $(document).on("focusout",".creditAmount", function(){
		  var value = $(this).val();
		  $(this).val(parseFloat(value).toFixed(numberOfDecimal));
		  var balance =  parseFloat($(this).data("balance"));
		  <%-- var originalBalance = parseFloat('<%=balanceAmount%>'); --%>
		  var originalBalance = parseFloat('<%=conv_balanceAmount%>');
		  if(isNaN(value)){ /* To check if valid input i.e., number */
			  $(this).val("0.00");
		  }
		  value = parseFloat(value);
		  if(value > balance){
			  $(this).val(parseFloat(balance).toFixed(numberOfDecimal));
		  }
		  var totalAmount = 0;
		  $(".creditAmount").each(function() {
			  totalAmount += parseFloat($(this).val())					    
		  });
		  		  
		  balance = parseFloat(originalBalance - totalAmount).toFixed(numberOfDecimal);
		  if(balance < 0){
			  $(this).val("0.00");
			  alert("Total Amount is exceeding Bill balance.");
		  }else{
			  
			  var balamountorder = "<%=balaceorderamt%>";
			  if(parseFloat(balamountorder) > 0){  
				
				  var tamt = "0";
				  $(".ordercreditListTable tbody tr").each(function() {
						var amt = $('td:eq(7)', this).find('input').val();
						tamt = tamt + amt;		
					});
				 
				  if(parseFloat(balamountorder) != parseFloat(tamt)){
					  $(".creditListTable tbody tr").each(function() {
							$('td:eq(7)', this).find('input').val(parseFloat("0").toFixed(numberOfDecimal));
						});
					  $( ".creditAmountvaluec" ).prop( "disabled", true );
					  totalAmount = 0;
					  $(".creditAmount").each(function() {
						  totalAmount += parseFloat($(this).val())					    
					  });
					  		  
					  balance = parseFloat(originalBalance - totalAmount).toFixed(numberOfDecimal);
					  
				  }else{
					  $( ".creditAmountvaluec" ).prop( "disabled", false );
				  }
			  
			  }
			  $('#creditTotalAmount').text(parseFloat(totalAmount).toFixed(numberOfDecimal));		
			  $('#creditBalanceAmount').text(balance);
		  }				  	  
	  });
	  <%
		Journal journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant, (String)billHdr.get("BILL"), "BILL");
	  %>
	  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
	});
	
	function applyCredit(){

		//Resvi start
		  var ValidNumber   = document.creditForm.ValidNumber.value;
		  if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
		// end
					
	
		var CURRENCYUSEQTs = $("#CURRENCYUSEQT").val();
		var billHdrId = [], pono = [], payHdrId = [], amount = [], payDetId = [];
		$(".creditListTable tbody tr").each(function() {
			/* console.log($('td:eq(0)', this).text());
			console.log($('td:eq(1)', this).text());
			console.log($('td:eq(2)', this).text());
			console.log($('td:eq(3)', this).text());
			console.log($('td:eq(4)', this).text());
			console.log($('td:eq(5)', this).text());
			console.log($('td:eq(6)', this).text());
			console.log($('td:eq(7)', this).find('input').val()); */
			var amt = $('td:eq(9)', this).find('input').val();
			if(amt > 0){
				billHdrId.push($('td:eq(0)', this).text());
				pono.push($('td:eq(1)', this).text());
				payHdrId.push($('td:eq(2)', this).text());
				payDetId.push($('td:eq(10)', this).text());
				amount.push(amt);
			}					
		});

		$(".ordercreditListTable tbody tr").each(function() {
			
			var amt = $('td:eq(7)', this).find('input').val();
			if(amt > 0){
				billHdrId.push($('td:eq(0)', this).text());
				pono.push($('td:eq(1)', this).text());
				payHdrId.push($('td:eq(2)', this).text());
				payDetId.push($('td:eq(8)', this).text());
				amount.push(amt);
			}					
		});

		$.ajax({
			type : "POST",
			url : "/track/BillPaymentServlet?Submit=applyCredit",
			async : true,
			data : {
				BILLHDRID : billHdrId,
				PONO : pono,
				PAYHDRID : payHdrId,
				AMOUNT : amount,
				PAYDETID : payDetId,
				CURRENCYUSEQT : CURRENCYUSEQTs,
			},
			dataType : "json",
			success : function(data) {
				if(data.ERROR_CODE == 100){
					location.reload();
				}else{
					alert(data.MESSAGE);
				}
			}
		});
	}
	
	function generatePdf(dataUrl){
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		/* Top Right */
		
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.text('BILL', 195, 30, {align:'right'});
	
		doc.setFontSize(10);
		//doc.setFontStyle("bold");
		doc.text('#<%=billHdr.get("BILL")%>', 195, 35, {align:'right'});
		
		<%if(billHdr.get("GRNO").equals("")|| billHdr.get("GRNO") == null){%>
		
		<%}else{%>
			doc.setFontSize(17);
			doc.setFontStyle("normal");
			doc.text('GRNO', 195, 42, {align:'right'});
		
			doc.setFontSize(10);
			//doc.setFontStyle("bold");
			doc.text('#<%=billHdr.get("GRNO")%>', 195, 47, {align:'right'});
			
			doc.setFontSize(17);
			doc.setFontStyle("normal");
			doc.text('ORDER NUMBER', 195, 54, {align:'right'});
		
			doc.setFontSize(10);
			//doc.setFontStyle("bold");
			doc.text('#<%=billHdr.get("PONO")%>', 195, 59, {align:'right'});
			
		<%}%>
		
		doc.setFontSize(8);
		doc.setFontStyle("normal");
		doc.text('Balance Due', 195, 64, {align:'right'});

		doc.setFontSize(12);
		//doc.setFontStyle("bold");
		doc.text('<%=curency%><%=Numbers.toMillionFormat(balanceDue, Integer.valueOf(numberOfDecimal))%>', 195, 70, {align:'right'});

		doc.autoTable({
			html : '#table1',
			startY : 83,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
			theme : 'plain'
		});
		/* **** */

		/* Top Left */
		
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		
		doc.setFontSize(10);
		//doc.setFontStyle("bold");
		doc.text('<%=PLNTDESC%>', 16, 46);

		doc.setFontSize(9);
		doc.setFontStyle("normal");
		doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

		doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

		doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
		<%if(printuenno == 1){ %>
		doc.text('<%=uenno%> :<%=companyregnumber%>', 16, 62);
		<%} %>
		doc.text('<%=tranno%>: <%=RCBNO.trim()%>', 16, 66);
		
		doc.text('Contact: <%=NAME.trim()%>', 16, 70);
		
		doc.text('Tel: <%=TELNO.trim()%> Fax:<%=FAX.trim()%>', 16, 74);

		doc.text('Email: <%=EMAIL.trim()%>', 16, 78);

		//doc.text('United Arab Emirates', 16, 73);

		doc.setFontSize(10);
		doc.text('<%=billtoheader%>', 16, 86);
		doc.text('<%=SupplyDetails.get(1)%>', 16, 90);
		<%if(printsupplieruenno == 1){ %>
		doc.text('<%=supplieruenno%> : <%=SupplyDetails.get(25)%>', 16, 94);
		<%}%>
		doc.text('<%=suppliertranno%> <%=SupplyDetails.get(24)%>', 16, 98);
		doc.text('<%=SupplyDetails.get(3)%> <%=SupplyDetails.get(4)%>', 16, 102);
		doc.text('<%=SupplyDetails.get(5)%> <%=SupplyDetails.get(16)%>', 16, 106);
		doc.text('<%=SupplyDetails.get(5)%> <%=SupplyDetails.get(22)%> <%=SupplyDetails.get(6)%>', 16, 110);
		doc.text('Email:<%=SupplyDetails.get(12)%>', 16, 114);
		doc.text('Remarks:<%=SupplyDetails.get(15)%>', 16, 118);
		<%if(printwithtransportmode == 1){ %>
		doc.text('<%=transmode%> : <%=transportmode %>', 16, 122);
		<%} %>	
		<%if(prostatus == 1){ %>
		doc.text('<%=prolable%> : <%=projectname %>', 16, 126);
		<%} %>
		<%-- doc.text('Bill From', 16, 85);

		//doc.setFontStyle("bold");
		var splitvendname = doc.splitTextToSize('<%=billHdr.get("VNAME")%>', 100);
		doc.text(splitvendname, 16, 90);
		/* **** */
		<%if(printsupplieruenno == 1){ %>
		doc.text('<%=supplieruenno%>:<%=SupplyDetails.get(25)%>', 16, 94);
		<%}%>
		<%if(prostatus == 1){ %>
			doc.setFontSize(10);
			doc.text('<%=prolable%>:<%=projectname%>', 16, 98);
		<%} %> --%>
		var totalPagesExp = "{total_pages_cont_string}";
		doc.fromHTML(document.getElementById('table'));
		doc.autoTable({
			html : '#table2',
			startY : 130,
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center'
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ]
			},
			theme : 'plain',
			columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'},6: {halign: 'right'}},
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
				doc.text(str, 178,
						pageHeight - 10);
			}
		});

		let finalY = doc.previousAutoTable.finalY;

		doc.autoTable({
			html : '#table4',
			margin : {left : 95},
			columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
			theme : 'plain'
		});
		finalY = doc.previousAutoTable.finalY;
		var finale = <%=billDetList.size()%> 
				if (finale>=2 && finale<=6 ){
				finalY=280;
				}
// 				var finale = i + finalY;
				doc.autoTable({
					html : '#tablesign',
					//html : tblp3,
// 					startY : finale, 
					startY : finalY, 
					styles : {fontSize : 10},
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
				
				doc.setFontStyle("normal");
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
					doc.text(str, 16, pageHeight - 10);
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('Bill.pdf');

				
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
				window.open(
						"/track/deleveryorderservlet?Submit=Print Bill With Price&BILL=<%=billHdr.get("BILL")%>",
						  '_blank' // <- This is what makes it open in a new window.
						);
					
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
			
			function advPayment(){	
			<%-- 	if('<%=creditFlag%>' == 'true'){ --%>
	var balamountorder = "<%=balaceorderamt%>";
	  if(balamountorder > 0){  
			alert("You have credits for this Bill.");
		}else{
			window.location.href = "../bill/record?type=REGULAR&pono=<%=billHdr.get("PONO")%>&bill=<%=billHdr.get("ID")%>";
		}
						
	}
	
	function convertToOpen(){
		window.location.href = "/track/BillingServlet?Submit=convertToOpen&billHdrId=<%=billHdr.get("ID")%>";
	}
	
	function convertToDraft(){
		window.location.href = "/track/BillingServlet?Submit=convertToDraft&billHdrId=<%=billHdr.get("ID")%>";
	}
	
	 $('#applycrd').click(function() {
		  <%-- var originalBalance = parseFloat('<%=balanceAmount%>'); --%>
		  var originalBalance = parseFloat('<%=conv_balanceAmount%>');
		  var numberOfDecimal = $("#numberOfDecimal").val();
		  var zerof = parseFloat("0").toFixed(numberOfDecimal);
		 
		  
		  $(".creditListTable tbody tr").each(function() {
			  
			  $( ".creditAmountvaluec" ).prop( "disabled", false );
				$('td:eq(9)', this).find('input').val(parseFloat("0").toFixed(numberOfDecimal));
				$( ".creditAmountvaluec" ).prop( "disabled", true );
			});
		  
		  $(".ordercreditListTable tbody tr").each(function() {
			 
				$('td:eq(9)', this).find('input').val(parseFloat("0").toFixed(numberOfDecimal));
			});
		
		 
		$('#creditTotalAmount').text(parseFloat("0").toFixed(numberOfDecimal));		
		$('#creditBalanceAmount').text(originalBalance);
		
		 var balcheck= "<%=balaceorderamt%>";
		 
		  if(parseFloat(balcheck) === parseFloat("0")){
			 
			  $( ".creditAmountvaluec" ).prop( "disabled", false );
		  }
	    	
	 });
			 

	   
</script>
<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>
<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
<script src="<%=rootURI%>/jsp/js/general.js"></script>
<script src="<%=rootURI%>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI%>/jsp/js/jspdf.plugin.autotable.js"></script>
<script src="<%=rootURI%>/jsp/js/Printpagepdf.js"></script>
<!-- PDF Print Start 4 -->		
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<!-- PDF Print End 4 -->