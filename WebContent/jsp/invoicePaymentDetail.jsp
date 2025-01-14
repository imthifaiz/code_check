<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.db.object.JournalDetail"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.object.InvPaymentDetail"%>
<%@page import="com.track.db.object.InvPaymentHeader"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@page import="com.track.constants.*"%>
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
String title = "INVOICE RECEIPT";
String rootURI = HttpUtils.getRootURI(request);
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String custno=StrUtils.fString(request.getParameter("CUSTNO"));
String resultnew=StrUtils.fString(request.getParameter("resultnew"));
String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
int paymentheaderid=Integer.parseInt(request.getParameter("ID"));

boolean displaySummaryEdit=false,displayPdfPrint=false,displayDelete=false,displayApplyRecords=false,displaySummaryMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryEdit = ub.isCheckValAcc("editPaymentReceived", plant,username);
	displayPdfPrint = ub.isCheckValAcc("printPaymentReceived", plant,username);
	displayDelete = ub.isCheckValAcc("deletePaymentReceived", plant,username);
	displaySummaryMore = ub.isCheckValAcc("morePaymentReceived", plant,username);	
}
InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
InvPaymentHeader invoicePaymentHdr=invoicePaymentDAO.getInvoicePaymentById(paymentheaderid, plant, username);
List<InvPaymentDetail> invoicePaymentDetails=invoicePaymentDAO.getInvoicePaymentDetails(paymentheaderid, plant, username);
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
CustMstDAO custMstDAO = new CustMstDAO();
String custname=custMstDAO.getCustName(plant, custno);
InvoiceUtil invoiceUtil = new InvoiceUtil();
List<Map> invoiceHeaderList=new ArrayList<>();

Map< String,InvPaymentDetail> invoicePaymentDet =  new HashMap< String,InvPaymentDetail>();
List<Map> invoicePaymentDetList=new ArrayList<>();
for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetails)
	{
	Hashtable ht = new Hashtable();
	ht.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
	ht.put("PLANT", plant);
	List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
	if(invoiceHdrList.size()>0)
	{
		Map invoiceHdr=(Map)invoiceHdrList.get(0);
		invoiceHeaderList.add(invoiceHdr); 
		invoicePaymentDet.put((String) invoiceHdr.get("INVOICE"),invoicePaymentDetail);
		invoicePaymentDetList.add(invoicePaymentDet);
	}
	
	}



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

String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY;

String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
File checkImageFile = new File(imagePath);
if (!checkImageFile.exists()) {
	imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
}
String payover = StrUtils.addZeroes(invoicePaymentHdr.getAMOUNTRECEIVED(), numberOfDecimal);
String paymentcurrency = (String)invoicePaymentHdr.getCURRENCYID (); //RESVI
String custName = custMstDAO.getCustName(plant, invoicePaymentHdr.getCUSTNO());


String overPayment = "";

double doverPayment ="".equals(invoicePaymentHdr.getAMOUNTUFP()) ? 0.0d :  invoicePaymentHdr.getAMOUNTUFP();
overPayment = StrUtils.addZeroes(doverPayment, numberOfDecimal);


String overpayamt="";

if(doverPayment>0)
	
	overpayamt = "Unused Amount : " + curency +""+overPayment;

InvPaymentDetail invdetalepay = invoicePaymentDetails.get(0);
String paytypedet= invdetalepay.getTYPE();
List<InvPaymentDetail> invoicePaymentDetailscredit = new ArrayList<InvPaymentDetail>();
if(!invoicePaymentHdr.getCREDITAPPLYKEY().equalsIgnoreCase("0")){
	 invoicePaymentDetailscredit=invoicePaymentDAO.getInvoicePaymentDetailsbyuuid(invoicePaymentHdr.getCREDITAPPLYKEY(), plant, username);
}

String recpaycheck = "0";
String creditcheck = "0";
String delestatus = "0";
String alremarks = payover;
List<InvPaymentDetail> invoicedetreccheck =  invoicePaymentDAO.getInvoicePaymentDetailsbylineno(paymentheaderid,"0", plant);
if(invoicedetreccheck.size() > 0){
	recpaycheck = "1";
	 for(InvPaymentDetail invoicePaymentDetail:invoicedetreccheck)  {
		 if(invoicePaymentDetail.getCREDITNOTEHDRID() > 0){
			 creditcheck = "1";
		 }
		 if(!invoicePaymentDetail.getDONO().equalsIgnoreCase("0")){
			 alremarks = invoicePaymentDetail.getDONO()+","+payover;
		 }
	 }
}else{
	for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetails)
		{
		Hashtable htin = new Hashtable();
		htin.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
		htin.put("PLANT", plant);
		List invoiceHdrListin =  invoiceUtil.getInvoiceHdrById(htin);
		if(invoiceHdrListin.size()>0)
		{
			Map invoiceHdrin=(Map)invoiceHdrListin.get(0);
			alremarks = invoiceHdrin.get("DONO")+","+invoiceHdrin.get("INVOICE")+","+payover;
		}
		
		}
}


String projectname = "";
	int proid = invoicePaymentHdr.getPROJECTID();
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	if(proid > 0){
		finProject = finProjectDAO.getFinProjectById(plant, proid);
		projectname = finProject.getPROJECT_NAME();
	}

	
	  Journal journalHeaderDetail;
	  if ("Credit Note".equals(invoicePaymentHdr.getRECEIVE_MODE())){
		  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,invoicePaymentHdr.getREFERENCE(), "CUSTOMERCREDITNOTES");
	  }else{
		  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,String.valueOf(paymentheaderid), "SALESPAYMENT");
	  }
	  
	  boolean editcheck = true;
	  for (JournalDetail journaldetail : journalHeaderDetail.getJournalDetails()) {
	  	if(journaldetail.getRECONCILIATION() == 1){
	  		editcheck = false;
	  	}
	  }

%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/tabulator_bootstrap.min.css" />
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
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

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}

</style>
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
                <li><a href="../banking/invoicepaysummary"><span class="underline-on-hover">Receipt Summary</span> </a></li>                                    
                <li><label>INVOICE RECEIPT</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
		<%if(invoicePaymentHdr.getCUSTNO().equalsIgnoreCase("N/A")){ %>
			<h1 style="font-size: 20px;" class="box-title">RECEIPT VOUCHER PAYMENT</h1>
		<%}else{ %>
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
		<%} %>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryEdit) { %>
				<%if(invoicePaymentHdr.getCUSTNO().equalsIgnoreCase("N/A")){ %>
				<%if(editcheck){ %>
					<button type="button" onclick="window.location.href='../banking/editinvoicevoucherpay?paymentid=<%=invoicePaymentHdr.getID()%>'" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					<%} %>
				<%}else{ %>
					<%if(creditcheck.equalsIgnoreCase("0")){ %>
					<%if(editcheck){ %>
						<button type="button" onclick="window.location.href='../banking/editinvoicepay?paymentid=<%=invoicePaymentHdr.getID()%>'" class="btn btn-default"
						 data-toggle="tooltip"  data-placement="bottom" title="Edit">
							<i class="fa fa-pencil" aria-hidden="true"></i>
						</button>
						<%} %>
					<%} %>
				<%} %>
				<% } %>
				<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% } %>
				</div>
				<% if (displaySummaryMore) { %>
				<%if(creditcheck.equalsIgnoreCase("0")){ %>
					<div class="btn-group" role="group">
						  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
						  <% if (displayDelete) { %>
						  <%if(editcheck){ %>
						   <ul class="dropdown-menu" style="min-width: 0px;">
						    <li id="invoice-delete"><a href="#">Delete</a></li>
						  </ul>
						  <%} %>
						  <% } %>
					</div>
				<%} %>
				<% } %>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../banking/invoicepaysummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">		
		<div style="height: 0.700000in;"></div>
		<span id="print_id">
		<div class="row">
			<div class="col-xs-6">
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
				<div>
					<b><%=PLNTDESC%></b>
				</div>
<span style="white-space: pre-wrap; word-wrap: break-word;">
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
			</div>

			
		</div>
		<div class="row">
			<div class="col-xs-12" >		
      <h1 style="font-size: 20px;text-align:center">PAYMENT DETAIL</h1>
    </div>
    </div>
	<div class="row">
		<div class="col-xs-8" >
			<div class="row">
						<div class="col-xs-4" style="color:#999;">
						<br><p>Payment Date</p>
						</div>
						<div class="col-xs-8">
						<br><p><%=invoicePaymentHdr.getRECEIVE_DATE() %></p>
						</div>
			</div>
			<div class="row">
					<div class="col-xs-4" style="color:#999;">
					<p>Project</p>
					</div>
					<div class="col-xs-8">
					<p><%=projectname%></p>
					</div>			
		</div>
			<div class="row">
						<div class="col-xs-4" style="color:#999;">
						<p>Reference Number</p>
						</div>
						<div class="col-xs-8">
						<p><%=invoicePaymentHdr.getREFERENCE()%></p>
						</div>			
			</div>
			<div class="row">
			<%if(invoicePaymentHdr.getCUSTNO().equalsIgnoreCase("N/A")){ %>
			<div class="col-xs-4" style="color:#999;">
					<p>Account Name</p>
					</div>
					<div class="col-xs-8">
					<p><%=invoicePaymentHdr.getACCOUNT_NAME()%></p>
					</div>	
			<%}else{ %>
					<div class="col-xs-4" style="color:#999;">
					<p>Received From</p>
					</div>
					<div class="col-xs-8">
					<p><%=custName%></p>
					</div>	
					
					<%} %>		
			</div>
			<div class="row">
						<div class="col-xs-4" style="color:#999;">
						<p>Payment Mode</p>
						</div>
						<div class="col-xs-8">
						<p><%=invoicePaymentHdr.getRECEIVE_MODE() %></p>
						</div>			
			</div>
			<div class="row">
					<div class="col-xs-4" style="color:#999;">
					<p>Deposit To</p>
					</div>
					<div class="col-xs-8">
					<p><%=invoicePaymentHdr.getDEPOSIT_TO()%></p>
					</div>			
		</div>
		</div>
		<div class="col-xs-4" >
			<div style="text-align:center;background:#78ae54;color:white;width: 50%;margin-top: 15%;">
				<%-- <p> Amount Paid<br>				
				<h4><%=curency%><%=amountpaid%></h4></p> --%>
				<h5>Payment Received</h5>				
				<p><%=curency%><%=Numbers.toMillionFormat(payover,numberOfDecimal)%></p>
				
		</div>
		</div>
	</div>
			
			
			
			
<%if(invoiceHeaderList.size()>0 || invoicePaymentDetailscredit.size() >0) {%>
				<div class="row">
			<div class="col-xs-6">
			<h4>Payment For</h4>
			</div>
			<div class="col-xs-6 text-right">				
				<h5><%=overpayamt%></h5>
			</div>	 	
		</div>
		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>Invoice#</td>
							<td>Invoice Date</td>
							<td>Invoice Amount(<%=paymentcurrency%>)</td>
							<td>Exchange Rate(<%=curency%>/<%=paymentcurrency%>)</td>
							<td>Invoice Amount (<%=curency%>)</td>
							<td>Credit Payments (<%=curency%>)</td>	
							<td>Used Amount (<%=curency%>)</td>
							<td>Invoice Due (<%=curency%>)</td>
							<td></td>						
						</tr>
					</thead>
					<tbody>
					<% for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetails)  {
				  		/* Map m=(Map)invoiceHeaderList.get(i);
				  		String Paymentamount="",Invoiceamount;				  		
				  		InvPaymentDetail invDet=invoicePaymentDet.get((String) m.get("INVOICE")); */
				  		String Paymentamount="",Invoiceamount,invoiceno,invoicedate,invoicecurency,exchangeRate,covInvoiceamount="0";	
				  		Hashtable ht = new Hashtable();
				  		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
				  		ht.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
				  		ht.put("PLANT", plant);
				  		List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
				  		if(invoiceHdrList.size()>0)
				  		{
				  			Map invoiceHdr=(Map)invoiceHdrList.get(0);
				  			invoiceno = (String) invoiceHdr.get("INVOICE");
				  			invoicedate = (String) invoiceHdr.get("INVOICE_DATE");
				  			Invoiceamount = (String) invoiceHdr.get("TOTAL_AMOUNT");
				  			double dInvoiceamount ="".equals(Invoiceamount) ? 0.0d :  Double.parseDouble(Invoiceamount);
				  			Invoiceamount = StrUtils.addZeroes(dInvoiceamount, numberOfDecimal);
				  			
				  			covInvoiceamount= Numbers.toMillionFormat((Float.parseFloat(Invoiceamount)*Float.parseFloat( (String) invoiceHdr.get("CURRENCYUSEQT"))), numberOfDecimal);
				  			invoicecurency = (String) invoiceHdr.get("CURRENCYID");
				  			exchangeRate = (String) invoiceHdr.get("CURRENCYUSEQT");
				  			
				  		}else{
				  			continue;
				  		}
				  		
				  		
				  		String zeroval = Numbers.toMillionFormat((Double.parseDouble("0")), numberOfDecimal);
				  		
			  			Paymentamount = String.valueOf(invoicePaymentDetail.getAMOUNT());
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			double dbalancedue = invpaymentdao.getbalacedue(plant, String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
			  			String balancedue = Numbers.toMillionFormat((Double.parseDouble(Invoiceamount)-dbalancedue), numberOfDecimal);
		  			
			  		%>
				  		<tr>
							
							<td class="text-center"><%=invoiceno%></td>
							<td class="text-center"><%=invoicedate%></td>
							<%-- <td class="text-center"><%=invoicecurency%> <%=covInvoiceamount%></td> --%>
							<td class="text-center"><%=covInvoiceamount%></td>
							<td class="text-center"><%=exchangeRate%></td>
							<td class="text-center"><%=Invoiceamount%></td>
						<%if(paytypedet.equalsIgnoreCase("ADVANCE")){ %>
							<%if(!invoicePaymentHdr.getCREDITAPPLYKEY().equalsIgnoreCase("0")){ %>
								<td class="text-center"><%=zeroval%></td>
								<td class="text-center"><%=Paymentamount%></td>
							<%}else{ %>
								<td class="text-center"><%=Paymentamount%></td>
								<td class="text-center"><%=zeroval%></td>
							<%} %>
						<%}else{ %>
							<td class="text-center"><%=zeroval%></td>
							<td class="text-center"><%=Paymentamount%></td>
						<%} %>
							<td class="text-center"><%=balancedue%></td>
						<%if(recpaycheck.equalsIgnoreCase("1")){ %>
						<%if(editcheck){ %>
							<%if(paytypedet.equalsIgnoreCase("ADVANCE")){ %>
								<%if(!invoicePaymentHdr.getCREDITAPPLYKEY().equalsIgnoreCase("0")){ %>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=invoicePaymentDetail.getRECEIVEHDRID()%>','<%=paymentheaderid %>','<%=invoicePaymentDetail.getID()%>','<%=invoiceno%>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
								<%}else{ %>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=invoicePaymentDetail.getRECEIVEHDRID()%>','<%=paymentheaderid %>','<%=invoicePaymentDetail.getID()%>','<%=invoiceno%>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
								<%} %>
							<%}else{ %>
								<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=invoicePaymentDetail.getRECEIVEHDRID()%>','<%=paymentheaderid %>','<%=invoicePaymentDetail.getID()%>','<%=invoiceno%>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
							<%} %>
							<%}else{ %>
								<td class="text-center hideprintpdf">Reconciled</td>
							<%}%>
						<%} %>
						</tr>
				  	<%}%>
				  	
				  	
				  	<% for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetailscredit)  {
				  		/* Map m=(Map)invoiceHeaderList.get(i);
				  		String Paymentamount="",Invoiceamount;				  		
				  		InvPaymentDetail invDet=invoicePaymentDet.get((String) m.get("INVOICE")); */
				  		String Paymentamount="",Invoiceamount,invoiceno,invoicedate,invoicecurency,exchangeRate,covInvoiceamount="0";	
				  		Hashtable ht = new Hashtable();
				  		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
				  		ht.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
				  		ht.put("PLANT", plant);
				  		List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
				  		if(invoiceHdrList.size()>0)
				  		{
				  			Map invoiceHdr=(Map)invoiceHdrList.get(0);
				  			invoiceno = (String) invoiceHdr.get("INVOICE");
				  			invoicedate = (String) invoiceHdr.get("INVOICE_DATE");
				  			Invoiceamount = (String) invoiceHdr.get("TOTAL_AMOUNT");
				  			double dInvoiceamount ="".equals(Invoiceamount) ? 0.0d :  Double.parseDouble(Invoiceamount);
				  			Invoiceamount = Numbers.toMillionFormat(dInvoiceamount, numberOfDecimal);
				  			
				  			covInvoiceamount= Numbers.toMillionFormat((Float.parseFloat(Invoiceamount)*Float.parseFloat( (String) invoiceHdr.get("CURRENCYUSEQT"))), numberOfDecimal);
				  			invoicecurency = (String) invoiceHdr.get("CURRENCYID");
				  			exchangeRate = (String) invoiceHdr.get("CURRENCYUSEQT");
				  			
				  		}else{
				  			continue;
				  		}
				  		
				  		
				  		String zeroval = Numbers.toMillionFormat((Double.parseDouble("0")), numberOfDecimal);
				  		
			  			Paymentamount = String.valueOf(invoicePaymentDetail.getAMOUNT());
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			double dbalancedue = invpaymentdao.getbalacedue(plant, String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
			  			String balancedue = Numbers.toMillionFormat((Double.parseDouble(Invoiceamount)-dbalancedue), numberOfDecimal);
			  			
			  			delestatus = "1";
		  			
			  		%>
				  		<tr>
							
							<td class="text-center"><%=invoiceno%></td>
							<td class="text-center"><%=invoicedate%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(covInvoiceamount,numberOfDecimal)%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(exchangeRate,numberOfDecimal)%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(Invoiceamount,numberOfDecimal)%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(Paymentamount,numberOfDecimal)%><a href="../banking/invoicepaydetail?ID=<%=invoicePaymentDetail.getRECEIVEHDRID()%>" title="View Credit Payment Details" target="_blank"><i class="fa fa-external-link" aria-hidden="true"></i></a></td>
							<td class="text-center"><%=Numbers.toMillionFormat(zeroval,numberOfDecimal)%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(balancedue,numberOfDecimal)%></td>
							<%if(editcheck){ %>
							<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=invoicePaymentDetail.getRECEIVEHDRID()%>','<%=paymentheaderid %>','<%=invoicePaymentDetail.getID()%>','<%=invoiceno%>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
							<%}else{ %>
								<td class="text-center hideprintpdf">Reconciled</td>
							<%}%>
						</tr>
				  	<%}%>
				  	
					</tbody>
				</table>
			</div>
		</div>
		<%-- <%} else {%>
		<div class="row">
			<div class="col-xs-6">
			<h3>Over Payment</h3>				
				<h4><%=curency%><%=invoicePaymentHdr.getAMOUNTRECEIVED()%></h4>
			</div>
		</div>
		</span>
		</div> --%>
		<%} %>
		<div class="row">
			<div class="col-xs-9"></div>
			<div class="col-xs-3">
			<br><br><br>
			<br><br><br>		
		</div>
		</div>
		
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
									<small class="text-muted">Amount is displayed in your receipt currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=curency%></span>
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
<div id="unapplypayment" class="modal fade" role="dialog">
			  <div class="modal-dialog modal-sm">	
			    <!-- Modal content-->
			    <div class="modal-content">
			      <div class="modal-body">
			        <div class="row">
			        <INPUT type="hidden" name="refundid" value="">
			        <INPUT type="hidden" name="tranid" value="">
			         <INPUT type="hidden" name="rfinvoiceno" value="">
			          <INPUT type="hidden" name="rfamount" value="">
			           <INPUT type="hidden" name="logstatus" value="">
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
					      <p> Deleted Received payment information cannot be retrieved. Are you sure about deleting ?</p>
					      
					      <div class="alert-actions btn-toolbar">
					         <button class="btn btn-primary ember-view" onclick="refundpayment()" style="background:red;">
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
			<div id="deletepayment" class="modal fade" role="dialog">
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
					      <p> Deleted Received payment information cannot be retrieved. Are you sure about deleting ?</p>
					      
					      <div class="alert-actions btn-toolbar">
					         <button class="btn btn-primary ember-view" onclick="delpayment()" style="background:red;">
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
<script type="text/javascript">
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
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 4000);
			  $('[data-toggle="tooltip"]').tooltip();  
			  $('.printMe').click(function(){
				  $(".hideprintpdf").css("display", "none");
				     $("#print_id").print();
				     $(".hideprintpdf").css("display", "block");
				  
				});
			  
			  sortTable();
			  balanceDueTable();

			 <%--  <%
			  Journal journalHeaderDetail;
			  if ("Credit Note".equals(invoicePaymentHdr.getRECEIVE_MODE())){
				  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,invoicePaymentHdr.getREFERENCE(), "CUSTOMERCREDITNOTES");
			  }else{
				  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,String.valueOf(paymentheaderid), "SALESPAYMENT");
			  }
			  %> --%>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
			});
			
			$("#invoice-delete").click(function() {    
				 var trid = '<%=paymentheaderid%>';
				 var delestatuscheck ='<%=delestatus%>';
				 if(delestatuscheck == "0"){
						 $.ajax({
								type : "GET",
								url: '/track/InvoicePayment?CMD=checkpdcstatusfordelete',
								async : true,
								dataType: 'json',
								data : {
									PLANT : "<%=plant%>",
									ID : trid,
								},
								success : function(data) {
									var result = "";
									if(data == "0"){
										$('#deletepayment').modal('show');
									}else if(data == "2"){
										result = "PDC Status check Error";
										window.location.href = "invoicePaymentDetail.jsp?ID="+trid+"&resultnew="+ result;
									}else{
										result = "Receive payment not allow to delete once PDC cheque marked as processed";
										window.location.href = "invoicePaymentDetail.jsp?ID="+trid+"&resultnew="+ result;
									}
									
								}
							});
				 }else{
					result = "Once payment 'Apply Credit' in other payment not allow to delete. Please check Credit Payment hyperlink for Apply Credit payment details";
					window.location.href = "invoicePaymentDetail.jsp?ID="+trid+"&resultnew="+ result;
				 }
			 });
			 
			function delpayment(){
				var tranid = '<%=paymentheaderid%>';
				var remark = '<%=alremarks%>';
				window.location.href = "/track/InvoicePayment?CMD=deletepayment&TRANSID="+tranid+"&REMARK="+remark;
			}
			
			function refundpaymentcheck(id,trid,detid,rfinvoiceno,rfamount,status){
				$.ajax({
					type : "GET",
					url: '/track/InvoicePayment?CMD=checkpdcstatus',
					async : true,
					dataType: 'json',
					data : {
						PLANT : "<%=plant%>",
						ID : id,
					},
					success : function(data) {
						var result = "";
						if(data == "0"){
							$("input[name ='refundid']").val(detid);
							$("input[name ='tranid']").val(trid);
							$("input[name ='rfinvoiceno']").val(rfinvoiceno);
							$("input[name ='rfamount']").val(rfamount);
							$("input[name ='logstatus']").val(status);
							$('#unapplypayment').modal('show');
						}else if(data == "2"){
							result = "PDC Status check Error";
							window.location.href = "invoicePaymentDetail.jsp?ID="+trid+"&resultnew="+ result;
						}else{
							result = "Received payment not allow to UnApply Credit once PDC cheque marked as processed";
							window.location.href = "invoicePaymentDetail.jsp?ID="+trid+"&resultnew="+ result;
						}
						
					}
				});	
				
			}
			
			function refundpayment(){
				var id = $("input[name ='refundid']").val();
				var tranid = $("input[name ='tranid']").val();
				var invoiceno = $("input[name ='rfinvoiceno']").val();
				var amount = $("input[name ='rfamount']").val();
				var logstatus =$("input[name ='logstatus']").val();
				window.location.href = "/track/InvoicePayment?CMD=refundcredit&DETID="+id+"&TRANSID="+tranid+"&INVOICENO="+invoiceno+"&AMOUNT="+amount+"&LOGSTATUS="+logstatus;
			}
			
			function generatePdf(dataUrl){
				$(".hideprintpdf").css("display", "none");
				var doc = new jsPDF('l', 'mm', 'a4');
				var pageNumber;
			
			
				var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
				var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
				/* doc.setFont("courier");
				doc.setFontType("normal"); */
				/* Top Right */
				
				doc.setFillColor("#78ae54");
				doc.rect(150, 99, 40, 15, 'F');
				
				doc.setTextColor(255,255,255);
				doc.setFontSize(10);
				doc.text('Payment Received', 170, 105, {align:'center'});
				

				doc.setFontSize(10);
				
				//doc.setFontStyle("bold");
				<%-- doc.text('Payment: <%=invoicePaymentHdr.getID()%>', 194, 42, {align:'right'}); --%>
				doc.text('<%=curency%> <%=Numbers.toMillionFormat(payover,numberOfDecimal)%>', 170, 110, {align:'center'});
				doc.setTextColor(0, 0, 0);
				/* **** */

				/* Top Left */
				
				doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
				
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

				//doc.text('United Arab Emirates', 16, 73);

				
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('PAYMENT DETAIL', 80, 80);
				
				
				var ref = doc.splitTextToSize('<%=invoicePaymentHdr.getREFERENCE()%>', 90);
				var canme = doc.splitTextToSize('<%=custName%>', 90);
				var rmode = doc.splitTextToSize('<%=invoicePaymentHdr.getRECEIVE_MODE()%>', 90);
				var deptto = doc.splitTextToSize('<%=invoicePaymentHdr.getDEPOSIT_TO()%>', 90);
				
				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('Payment Date', 16, 90);
				doc.text('<%=invoicePaymentHdr.getRECEIVE_DATE()%>', 56, 90);
				doc.text('Reference Number', 16, 100);
				doc.text(ref, 56, 100);
				<%if(invoicePaymentHdr.getCUSTNO().equalsIgnoreCase("N/A")){ %>
					doc.text('Account Name', 16, 110);
					//doc.setFontStyle("bold");
					doc.text('<%=invoicePaymentHdr.getACCOUNT_NAME()%>', 56, 110);
				<%}else{%>
					doc.text('Recived From', 16, 110);
					//doc.setFontStyle("bold");
					doc.text(canme, 56, 110);
					<%}%>
				doc.setFontStyle("normal");
				doc.text('Payment Mode', 16, 120);
				doc.text(rmode, 56, 120);
				doc.text('Deposit To', 16, 130);
				doc.text(deptto, 56, 130);

				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.autoTable({
					html : '#table2',
					startY : 145,
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
					columnStyles : {0 : {halign : 'left',cellWidth: 35},1 : {halign : 'center'},2 : {halign : 'right'},3 : {halign : 'center'},4 : {halign : 'right'},5 : {halign : 'right'},6 : {halign : 'right'},7 : {halign : 'right'}},
					styles: {
				        fontSize: 10
				       /*  font: 'courier',
				        fontStyle:'bold' */
				    },
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
						doc.text(str, 180,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;
				
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
				doc.save('PaymentReceived.pdf');
				$(".hideprintpdf").css("display", "block");	
			}
function generate() {
	
	var img = toDataURL($("#logo_content").attr("src"),
			function(dataUrl) {
				generatePdf(dataUrl);
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

function sortTable() {
	  var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("table2");
	  if (table == null){
			return;
		  }
	  switching = true;
	  /* Make a loop that will continue until
	  no switching has been done: */
	  while (switching) {
	    // Start by saying: no switching is done:
	    switching = false;
	    rows = table.rows;
	    /* Loop through all table rows (except the
	    first, which contains table headers): */
	    for (i = 1; i < (rows.length - 1); i++) {
	      // Start by saying there should be no switching:
	      shouldSwitch = false;
	      /* Get the two elements you want to compare,
	      one from current row and one from the next: */
	      x = rows[i].getElementsByTagName("TD")[0];
	      y = rows[i + 1].getElementsByTagName("TD")[0];
	      // Check if the two rows should switch place:
	      if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
	        // If so, mark as a switch and break the loop:
	        shouldSwitch = true;
	        break;
	      }
	    }
	    if (shouldSwitch) {
	      /* If a switch has been marked, make the switch
	      and mark that a switch has been done: */
	      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
	      switching = true;
	    }
	  }
	}

function balanceDueTable() {
	  var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("table2");
	  if (table == null){
			return;
		  }
	  rows = table.rows;
	    for (i = 1; i < (rows.length - 1); i++) {
	      x = rows[i].getElementsByTagName("TD")[0];
	      y = rows[i + 1].getElementsByTagName("TD")[0];
	      if (x.innerHTML.toLowerCase() == y.innerHTML.toLowerCase()) { 
	       rows[i].getElementsByTagName("TD")[5].innerHTML = "";
	      }
	    }
	    
	 
	}
	
</script>
<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>
<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
