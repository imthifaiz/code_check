<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.db.object.JournalDetail"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
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
	String title = "Payment Made Detail";
	String rootURI = HttpUtils.getRootURI(request);
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayDelete=false,displayMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editPaymentMade", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printPaymentMade", plant,username);
		displayDelete = ub.isCheckValAcc("deletePaymentMade", plant,username);
		displayMore = ub.isCheckValAcc("morePaymentMade", plant,username);
	}
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	BillUtil billUtil = new BillUtil();
	BillDAO billDao = new BillDAO();
	RecvDetDAO recvDetDAO = new RecvDetDAO(); 
	SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String id = StrUtils.fString(request.getParameter("TRANID"));
	Hashtable ht = new Hashtable();	
	ht.put("ID",id);	
	String sql = "SELECT ID,PAYMENT_DATE,REFERENCE,VENDNO,ACCOUNT_NAME,ISNULL(A.PROJECTID,'') AS PROJECTID,ISNULL(WO_AMOUNT,'0.0') AS WO_AMOUNT,ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL(A.CREDITAPPLYKEY,'') AS CREDITAPPLYKEY,ISNULL((SELECT VNAME FROM "+plant+"_VENDMST V WHERE V.VENDNO=A.VENDNO),0) AS VNAME,PAID_THROUGH,PAYMENT_MODE,AMOUNTPAID,AMOUNTPAID-(ISNULL((SELECT SUM(B.AMOUNT) FROM "+plant+"_FINPAYMENTDET B WHERE B.PAYHDRID=A.ID AND B.LNNO != 0),0)) AS OVERPAYMENT FROM "+plant+"_FINPAYMENTHDR A WHERE A.PLANT='"+ plant+"'";
	List arrList = billDao.selectForReport(sql, ht, "");
	Map grnbillHdr=(Map)arrList.get(0);
	ht = new Hashtable();	
	ht.put("PAYHDRID",id);
	sql = "SELECT A.ID,A.PAYHDRID as HDRID,ISNULL(B.REFERENCE_NUMBER,'') AS REFNUMBER,(ISNULL((SELECT SUM(C.AMOUNT) FROM "+plant+"_FINPAYMENTDET C WHERE C.BILLHDRID=A.BILLHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,ISNULL(B.CURRENCYID,'') as CURRENCYID,ISNULL((SELECT TOP 1 C.CURRENCYUSEQT FROM " + plant +"_FINBILLDET C WHERE C.BILLHDRID=B.ID),1) CURRENCYUSEQT,LNNO,BILL,BILL_DATE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "+plant+"_FINPAYMENTDET A JOIN "+plant+"_FINBILLHDR B on A.BILLHDRID=B.ID WHERE A.PLANT='"+ plant+"'";
	List grnbillDetList =  recvDetDAO.selectForReport(sql, ht, "");
	Hashtable htadv = new Hashtable();	
	sql ="SELECT * FROM "+plant+"_FINPAYMENTDET WHERE PAYHDRID='"+id+"' ORDER BY LNNO ASC";
	List checkadv =  recvDetDAO.selectForReport(sql, htadv, "");
	Map chekadvmap=(Map)checkadv.get(0);
	String advancecheck = (String)chekadvmap.get("TYPE");
	List capplyList=new ArrayList();
	String capplykey = (String)grnbillHdr.get("CREDITAPPLYKEY");
 	String paymentcurrency = (String)grnbillHdr.get("CURRENCYID"); //RESVI
	if(!capplykey.isEmpty()){
		Hashtable htcappy = new Hashtable();	
		htcappy.put("CREDITAPPLYKEY",capplykey);
		sql = "SELECT A.ID,A.PAYHDRID as HDRID,ISNULL(B.REFERENCE_NUMBER,'') AS REFNUMBER,LNNO,BILL,BILL_DATE,(ISNULL((SELECT SUM(C.AMOUNT) FROM "+plant+"_FINPAYMENTDET C WHERE C.BILLHDRID=A.BILLHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,TOTAL_AMOUNT as BILL_AMOUNT,ISNULL(B.CURRENCYID,'') as CURRENCYID,ISNULL((SELECT TOP 1 C.CURRENCYUSEQT FROM " + plant +"_FINBILLDET C WHERE C.BILLHDRID=B.ID),1) CURRENCYUSEQT,A.AMOUNT from "+plant+"_FINPAYMENTDET A JOIN "+plant+"_FINBILLHDR B on A.BILLHDRID=B.ID WHERE A.PLANT='"+ plant+"'";
		capplyList =  recvDetDAO.selectForReport(sql, htcappy, "");
	}
	
	String projectid = (String)grnbillHdr.get("PROJECTID");
	String projectname = "";
	if(!projectid.isEmpty()){
		int proid = Integer.valueOf(projectid);
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		if(proid > 0){
			finProject = finProjectDAO.getFinProjectById(plant, proid);
			projectname = finProject.getPROJECT_NAME();
		}
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
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	String totalAmount= (String) grnbillHdr.get("AMOUNTPAID");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String overPayment = (String) grnbillHdr.get("OVERPAYMENT");
	double doverPayment ="".equals(overPayment) ? 0.0d :  Double.parseDouble(overPayment);
	overPayment = StrUtils.addZeroes(doverPayment, numberOfDecimal);
	
	String amountpaid = (String) grnbillHdr.get("AMOUNTPAID");
	double damountpaid ="".equals(amountpaid) ? 0.0d :  Double.parseDouble(amountpaid);
	amountpaid = StrUtils.addZeroes(damountpaid, numberOfDecimal);
	
	String overpayamt=""; 
	if(doverPayment>0)
		overpayamt = "Unused Amount : " + curency +""+overPayment;
	
	if(grnbillHdr.get("VENDNO").equals("N/A")){
		title = "Payment Voucher Detail";
	}else{
		title = "Bill Payment Detail";
	}
	
	
	Hashtable htpaydet = new Hashtable();	
	htpaydet.put("PAYHDRID",id);
	htpaydet.put("TYPE","ADVANCE");
	String sql12 = "SELECT PONO FROM "+plant+"_FINPAYMENTDET WHERE PLANT='"+ plant+"'";
	List arrListdet = billDao.selectForReport(sql12, htpaydet, "");
	String alremarks = amountpaid;
	String odno = "";
	if(arrListdet.size() > 0){
		Map billDET=(Map)arrListdet.get(0);
		odno = (String)billDET.get("PONO");
		alremarks = odno+","+amountpaid;
	}
	
	
	String delestatus = "0";
	String delecredit = "0";
	String crstatus ="0";
	List creditstatus = supplierCreditDAO.getSupplierCrdnotebycreditnote(plant, (String)grnbillHdr.get("REFERENCE"));
	if(creditstatus.size() > 0){
		crstatus = "1";
	}
	
	Hashtable htrecpay1 = new Hashtable();	
	htrecpay1.put("A.ID",id);
	htrecpay1.put("B.LNNO","0");	
	String sqlrepay1 = "SELECT A.AMOUNTPAID,B.BILLHDRID FROM "+plant+"_FINPAYMENTHDR A LEFT JOIN "+plant+"_FINPAYMENTDET B ON B.PAYHDRID = A.ID WHERE A.PLANT='"+ plant+"'";
	List arrrepaylist1 = billDao.selectForReport(sqlrepay1, htrecpay1, "");
	String reccheck = "0";
	if(arrrepaylist1.size() > 0){
		reccheck = "0";
	}else{
		reccheck = "1";
		String rebillno = "0";
		Hashtable htrecpay2 = new Hashtable();	
		htrecpay2.put("A.ID",id);
		htrecpay2.put("B.LNNO","1");	
		String sqlrepay2 = "SELECT A.AMOUNTPAID,B.BILLHDRID FROM "+plant+"_FINPAYMENTHDR A LEFT JOIN "+plant+"_FINPAYMENTDET B ON B.PAYHDRID = A.ID WHERE A.PLANT='"+ plant+"'";
		List arrrepaylist2 = billDao.selectForReport(sqlrepay2, htrecpay2, "");
		if(arrrepaylist2.size() > 0){
			Map rebillDET=(Map)arrrepaylist2.get(0);
			rebillno = (String)rebillDET.get("BILLHDRID");
		}
		
		Hashtable htbillhdr = new Hashtable();
		htbillhdr.put("ID", rebillno);
		htbillhdr.put("PLANT", plant);
		List rebillhdrlist =  billUtil.getBillHdrById(htbillhdr);
		if(rebillhdrlist.size()>0)
		{
			Map rebillHdr=(Map)rebillhdrlist.get(0);
			
			String repono = (String) rebillHdr.get("PONO");
			if(!repono.isEmpty()){
				repono = repono+",";
			}
			String rebill = (String)rebillHdr.get("BILL");
			alremarks = repono+rebill+","+amountpaid;
		}
	}
	
	
	ht = new Hashtable();	
	ht.put("PAYHDRID",id);
	sql = "SELECT A.ID,A.PAYHDRID as HDRID,(ISNULL((SELECT SUM(C.AMOUNT) FROM "+plant+"_FINPAYMENTDET C WHERE C.EXPHDRID=A.EXPHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,ISNULL(B.CURRENCYID,'') as CURRENCYID,ISNULL(B.EXBILL,'') as EXBILL,ISNULL((SELECT TOP 1 C.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET C WHERE C.EXPENSESHDRID=B.ID),1) CURRENCYUSEQT,LNNO,B.ID as EXPID,EXPENSES_DATE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "+plant+"_FINPAYMENTDET A JOIN "+plant+"_FINEXPENSESHDR B on A.EXPHDRID=B.ID WHERE A.PLANT='"+ plant+"'";
	List expDetList =  recvDetDAO.selectForReport(sql, ht, "");
	List capplyListExp=new ArrayList();
	if(!capplykey.isEmpty()){
		Hashtable htcappyExp = new Hashtable();	
		htcappyExp.put("CREDITAPPLYKEY",capplykey);
		sql = "SELECT A.ID,A.PAYHDRID as HDRID,A.CURRENCYUSEQT,B.CURRENCYID,LNNO,B.ID as EXPID,ISNULL(B.EXBILL,'') as EXBILL,EXPENSES_DATE,(ISNULL((SELECT SUM(C.AMOUNT) FROM "+plant+"_FINPAYMENTDET C WHERE C.EXPHDRID=A.EXPHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "+plant+"_FINPAYMENTDET A JOIN "+plant+"_FINEXPENSESHDR B on A.EXPHDRID=B.ID WHERE A.PLANT='"+ plant+"'";
		capplyListExp =  recvDetDAO.selectForReport(sql, htcappyExp, "");
	}
	
	String tamtdiaply = curency+" "+Numbers.toMillionFormat(amountpaid,numberOfDecimal);
	
	
	 Journal journalHeaderDetail;
	  if ("Advance".equals((String)grnbillHdr.get("PAID_THROUGH"))){
		  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,(String)grnbillHdr.get("ID"), "SUPPLIERCREDITNOTES");
	  }else{
		  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,id, "PURCHASEPAYMENT");
		  Journal journalHeaderDetailexp;
		  journalHeaderDetailexp = new JournalEntry().getJournalByTransactionId(plant,id, "EXPENSEPAYMENT");
		  
		  if(journalHeaderDetailexp.getJournalDetails().size() > 0){
			  journalHeaderDetail.setJournalHeader(journalHeaderDetailexp.getJournalHeader());
			  journalHeaderDetail.setJournalDetails(journalHeaderDetailexp.getJournalDetails());
			  journalHeaderDetail.setJournalAttachment(journalHeaderDetailexp.getJournalAttachment());
		  }
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
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/tabulator_bootstrap.min.css" />
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}
#table3 thead {
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
#table3>tbody>tr>td {
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
                 <li><a href="../banking/billpaysummary"><span class="underline-on-hover">Bill and Voucher Payment Summary</span> </a></li>                    
                <li><label>Payment Voucher Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryEdit) { %>
				<%if(grnbillHdr.get("VENDNO").equals("N/A")){ %>
					<%if(editcheck){ %>
						<button type="button" class="btn btn-default"
						 data-toggle="tooltip"  data-placement="bottom" title="Edit" onclick="window.location.href='../banking/editvoucherpay?paymentid=<%=grnbillHdr.get("ID")%>'">
							<i class="fa fa-pencil" aria-hidden="true"></i>
						</button>
					<%}%>
					<%}else{ %>
						<%if(crstatus.equalsIgnoreCase("0")){ %>
						<%if(editcheck){ %>
							<button type="button" class="btn btn-default"
							 data-toggle="tooltip"  data-placement="bottom" title="Edit" onclick="window.location.href='../banking/editbillpay?paymentid=<%=grnbillHdr.get("ID")%>'">
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
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% } %>
					
				</div>
				<%if(crstatus.equalsIgnoreCase("0")){ %>
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  <% if (displayMore) { %>
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					   <% if (displayDelete) { %>
					   <%if(editcheck){ %>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					    <%} %>
					  </ul>
					  <% } %>
					  <% } %>
				</div>
				<%} %>
				
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../banking/billpaysummary'">
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

	<div class="col-xs-8">
		<div class="row">
			<div class="col-xs-4" style="color:#999;">
			<br><p>Payment Date</p>
			</div>
			<div class="col-xs-8">
			<br><p><%=grnbillHdr.get("PAYMENT_DATE")%></p>
			
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
					<p>Reference</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("REFERENCE")%></p>
					</div>			
		</div>
		<div class="row">
			<%if(grnbillHdr.get("VENDNO").equals("N/A")){ %>
					<div class="col-xs-4" style="color:#999;">
					<p>Account Name</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("ACCOUNT_NAME")%></p>
					</div>	
			<%}else{ %>		
					<div class="col-xs-4" style="color:#999;">
					<p>Paid To</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("VNAME")%></p>
					</div>
			<%} %>
		</div>
		<div class="row">
					<div class="col-xs-4" style="color:#999;">
					<p>Payment Mode</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("PAYMENT_MODE")%></p>
					</div>			
		</div>
		<div class="row">
					<div class="col-xs-4" style="color:#999;">
					<p>Paid Through</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("PAID_THROUGH")%></p>
					</div>			
		</div>
		
		<div class="row">
					<div class="col-xs-4" style="color:#999;">
					<p>Supplier Write-Off Amount</p>
					</div>
					<div class="col-xs-8">
					<p><%=grnbillHdr.get("WO_AMOUNT")%></p>
					</div>			
		</div>
	</div>
	<div class="col-xs-4" >
		
		<div style="text-align:center;background:#78ae54;color:white;width: 50%;margin-top: 15%;">
				<p> Amount Paid<br>				
				<h4><%=curency%><%=Numbers.toMillionFormat(amountpaid,numberOfDecimal)%></h4></p>
				
		</div>
	</div>

</div>

<%if(grnbillDetList.size()>0 || capplyList.size() > 0) {%>

				<div class="row">
			<div class="col-xs-6">
			<h4>Payment</h4>
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
							<td>Bill</td>
							<td>Supplier Invoice</td>
							<td>Bill Date</td>
							<td>Bill Amount(<%=paymentcurrency%>)</td>
							<td>Exchange Rate(<%=curency%>/<%=paymentcurrency%>)</td>   <%--    resvi --%>
							<td>Bill Amount (<%=curency%>)</td>
							<td>Credit Payments</td>
							<td>Used Amount</td>
							<td>Bill Due</td>
							<td class="hideprintpdf"></td>							
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<grnbillDetList.size(); i++) {
						delecredit = "1";
				  		Map m=(Map)grnbillDetList.get(i);
				  		String  Billamount="", Paymentamount="",covunitCostValue="0";				  		
			  			
			  			
				  		Billamount = (String) m.get("BILL_AMOUNT");
			  			double dBillamount ="".equals(Billamount) ? 0.0d :  Double.parseDouble(Billamount);
			  			Billamount = StrUtils.addZeroes(dBillamount, numberOfDecimal);
			  			
			  			covunitCostValue= Numbers.toMillionFormat((Float.parseFloat(Billamount)*Float.parseFloat( (String) m.get("CURRENCYUSEQT"))), numberOfDecimal);
			  			
			  			Paymentamount = (String) m.get("AMOUNT");
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			String Paymentdoneamount = (String) m.get("PAYMENTDONE");
			  			double dPaymentdoneamount ="".equals(Paymentdoneamount) ? 0.0d :  Double.parseDouble(Paymentdoneamount);
			  			Paymentdoneamount = Numbers.toMillionFormat(dPaymentdoneamount, numberOfDecimal);
			  			
			  			double dbilldue = dBillamount - dPaymentdoneamount;
			  			String billdue  = Numbers.toMillionFormat(dbilldue, numberOfDecimal);
			  			
			  			String showzero = Numbers.toMillionFormat(Double.parseDouble("0"), numberOfDecimal);
		  			
			  		%>
				  		<tr>
							
							<td class="text-center"><%=m.get("BILL") %></td>
							<td class="text-center"><%=m.get("REFNUMBER") %></td>
							<td class="text-center"><%=m.get("BILL_DATE")%></td>
							<%-- <td class="text-center"><%=m.get("CURRENCYID")%> <%=covunitCostValue%></td> --%>
							<td class="text-center"><%=covunitCostValue%></td>
							<td class="text-center"><%=m.get("CURRENCYUSEQT")%></td>
							<td class="text-center"><%=Billamount%></td>
							
						<%if(advancecheck.equalsIgnoreCase("ADVANCE")){ %>
							<%if(capplykey.isEmpty()){%>
								<td class="text-center"><%=Paymentamount%></td>
								<td class="text-center"><%=showzero%></td>
								<td class="text-center"><%=billdue%></td>
							<%}else{ %>
								<td class="text-center"><%=showzero%></td>
								<td class="text-center"><%=Paymentamount%></td>
								<td class="text-center"><%=billdue%></td>
							<%} %>
						<%}else{ %>
							<td class="text-center"><%=showzero%></td>
							<td class="text-center"><%=Paymentamount%></td>
							<td class="text-center"><%=billdue%></td>
						<%} %>
						<%if(reccheck.equalsIgnoreCase("0")){ %>
							<%if(editcheck){ %>
							<%if(advancecheck.equalsIgnoreCase("ADVANCE")){ %>
								<%if(capplykey.isEmpty()){%>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("BILL") %>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
								<%}else{ %>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("BILL") %>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
								<%} %>
							<%}else{ %>
								<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("BILL") %>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
							<%} %>
							<%}else{ %>
							<td class="text-center hideprintpdf">Reconciled</td>
							<%}%>
						<%}%>
						</tr>
				  
				  	<%}%>
				  	
				  	<%for(int i =0; i<capplyList.size(); i++) {
				  		delestatus = "1";
				  		Map m=(Map)capplyList.get(i);
				  		String  Billamount="", Paymentamount="",covunitCostValue="0";						  		
			  			
			  			
				  		Billamount = (String) m.get("BILL_AMOUNT");
			  			double dBillamount ="".equals(Billamount) ? 0.0d :  Double.parseDouble(Billamount);
			  			Billamount = Numbers.toMillionFormat(dBillamount, numberOfDecimal);

			  			covunitCostValue= Numbers.toMillionFormat((Float.parseFloat(Billamount)*Float.parseFloat( (String) m.get("CURRENCYUSEQT"))), numberOfDecimal);
			  			
			  			Paymentamount = (String) m.get("AMOUNT");
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			String Paymentdoneamount = (String) m.get("PAYMENTDONE");
			  			double dPaymentdoneamount ="".equals(Paymentdoneamount) ? 0.0d :  Double.parseDouble(Paymentdoneamount);
			  			Paymentdoneamount = Numbers.toMillionFormat(dPaymentdoneamount, numberOfDecimal);
			  			
			  			double dbilldue = dBillamount - dPaymentdoneamount;
			  			String billdue  = Numbers.toMillionFormat(dbilldue, numberOfDecimal);
			  			
			  			String showzero = Numbers.toMillionFormat(Double.parseDouble("0"), numberOfDecimal);
		  			
			  		%>
				  		<tr>
							
							<td class="text-center"><%=m.get("BILL") %></td>
							<td class="text-center"><%=m.get("REFNUMBER") %></td>
							<td class="text-center"><%=m.get("BILL_DATE")%></td>
							<td class="text-center"><%=covunitCostValue%></td>
							<td class="text-center"><%=m.get("CURRENCYUSEQT")%></td>
							<td class="text-center"><%=Billamount%></td>
							<td class="text-center"><%=Paymentamount%> <a href="../banking/billpaydetail?TRANID=<%=m.get("HDRID")%>" title="View Credit Payment Details" target="_blank" class="hideprintpdf"><i class="fa fa-external-link" aria-hidden="true"></i></a></td>
							<td class="text-center"><%=showzero%></td>
							<td class="text-center"><%=billdue%></td>
							<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheck('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("BILL") %>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
							
							
						</tr>
				  	<%}%>
				  	
					</tbody>
				</table>
					
			</div>
		</div>
		<%}%>
		
		<%if(expDetList.size()>0 || capplyListExp.size() > 0) {%>

		<div class="row">
			<div class="col-xs-12">
			
				<table id="table3" class="table">
					<thead>
						<tr>
							<!-- <td>Expense ID</td> -->
							<td>Bill</td>
							<td>Expense Date</td>
							<td>Expense Amount(<%=paymentcurrency%>)</td>
							<td>Exchange Rate(<%=curency%>/<%=paymentcurrency%>)</td>   <%--    resvi --%>
							<td>Expense Amount(<%=curency%>)</td>
							<td>Credit Payments</td>
							<td>Used Amount</td>
							<td>Expense Due</td>
							<td class="hideprintpdf"></td>							
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<expDetList.size(); i++) {
						delecredit = "1";
				  		Map m=(Map)expDetList.get(i);
				  		String  Billamount="", Paymentamount="",covunitCostValue="0";				  		
			  			
			  			
				  		Billamount = (String) m.get("BILL_AMOUNT");
			  			double dBillamount ="".equals(Billamount) ? 0.0d :  Double.parseDouble(Billamount);
			  			Billamount = Numbers.toMillionFormat(dBillamount, numberOfDecimal);
			  			
			  			covunitCostValue= Numbers.toMillionFormat((dBillamount*Float.parseFloat( (String) m.get("CURRENCYUSEQT"))), numberOfDecimal);
			  			
			  			Paymentamount = (String) m.get("AMOUNT");
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			String Paymentdoneamount = (String) m.get("PAYMENTDONE");
			  			double dPaymentdoneamount ="".equals(Paymentdoneamount) ? 0.0d :  Double.parseDouble(Paymentdoneamount);
			  			Paymentdoneamount = Numbers.toMillionFormat(dPaymentdoneamount, numberOfDecimal);
			  			
			  			double dbilldue = dBillamount - dPaymentdoneamount;
			  			String billdue  = Numbers.toMillionFormat(dbilldue, numberOfDecimal);
			  			
			  			String showzero = Numbers.toMillionFormat(Double.parseDouble("0"), numberOfDecimal);
		  			
			  		%>
				  		<tr>
							
							<%-- <td class="text-center"><%=m.get("EXPID") %></td> --%>
							<td class="text-center"><%=m.get("EXBILL") %></td>
							<td class="text-center"><%=m.get("EXPENSES_DATE")%></td>
							<%-- <td class="text-center"><%=m.get("CURRENCYID")%> <%=covunitCostValue%></td> --%>
							<td class="text-center"><%=covunitCostValue%></td>
							<td class="text-center"><%=m.get("CURRENCYUSEQT")%></td>
							<td class="text-center"><%=Billamount%></td>
							
						<%if(advancecheck.equalsIgnoreCase("ADVANCE")){ %>
							<%if(capplykey.isEmpty()){%>
								<td class="text-center"><%=Paymentamount%></td>
								<td class="text-center"><%=showzero%></td>
								<td class="text-center"><%=billdue%></td>
							<%}else{ %>
								<td class="text-center"><%=showzero%></td>
								<td class="text-center"><%=Paymentamount%></td>
								<td class="text-center"><%=billdue%></td>
							<%} %>
						<%}else{ %>
							<td class="text-center"><%=showzero%></td>
							<td class="text-center"><%=Paymentamount%></td>
							<td class="text-center"><%=billdue%></td>
						<%} %>
						<%if(reccheck.equalsIgnoreCase("0")){ %>
						<%if(editcheck){ %>
							<%if(advancecheck.equalsIgnoreCase("ADVANCE")){ %>
								<%if(capplykey.isEmpty()){%>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheckexp('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("EXPID") %>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
								<%}else{ %>
									<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheckexp('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("EXPID") %>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
								<%} %>
							<%}else{ %>
								<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheckexp('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("EXPID") %>','<%=Paymentamount%>','delete')">Delete Payment</button></td>
							<%} %>
							<%}else{ %>
							<td class="text-center hideprintpdf">Reconciled</td>
							<%}%>
						<%}%>
						</tr>
				  
				  	<%}%>
				  	
				  	<%for(int i =0; i<capplyListExp.size(); i++) {
				  		delestatus = "1";
				  		Map m=(Map)capplyListExp.get(i);
				  		String  Billamount="", Paymentamount="",covunitCostValue="0";				  		
			  			
			  			
				  		Billamount = (String) m.get("BILL_AMOUNT");
			  			double dBillamount ="".equals(Billamount) ? 0.0d :  Double.parseDouble(Billamount);
			  			Billamount = Numbers.toMillionFormat(dBillamount, numberOfDecimal);
			  			
			  			covunitCostValue= Numbers.toMillionFormat((Float.parseFloat(Billamount)*Float.parseFloat( (String) m.get("CURRENCYUSEQT"))), numberOfDecimal);
			  			Paymentamount = (String) m.get("AMOUNT");
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = Numbers.toMillionFormat(dPaymentamount, numberOfDecimal);
			  			
			  			String Paymentdoneamount = (String) m.get("PAYMENTDONE");
			  			double dPaymentdoneamount ="".equals(Paymentdoneamount) ? 0.0d :  Double.parseDouble(Paymentdoneamount);
			  			Paymentdoneamount = Numbers.toMillionFormat(dPaymentdoneamount, numberOfDecimal);
			  			
			  			double dbilldue = dBillamount - dPaymentdoneamount;
			  			String billdue  = Numbers.toMillionFormat(dbilldue, numberOfDecimal);
			  			
			  			String showzero = Numbers.toMillionFormat(Double.parseDouble("0"), numberOfDecimal);
		  			
			  		%>
				  		<tr>
							
							<%-- <td class="text-center"><%=m.get("EXPID") %></td> --%>
							<td class="text-center"><%=m.get("EXBILL") %></td>
							<td class="text-center"><%=m.get("EXPENSES_DATE")%></td>
							<td class="text-center"><%=covunitCostValue%></td>
							<td class="text-center"><%=m.get("CURRENCYUSEQT")%></td>
							<td class="text-center"><%=Billamount%></td>
							<td class="text-center"><%=Paymentamount%> <a href="billPaymentDetail.jsp?TRANID=<%=m.get("HDRID")%>" title="View Credit Payment Details" target="_blank" class="hideprintpdf"><i class="fa fa-external-link" aria-hidden="true"></i></a></td>
							<td class="text-center"><%=showzero%></td>
							<td class="text-center"><%=billdue%></td>
							<%if(editcheck){ %>
								<td class="text-center hideprintpdf"><button type="button"  class="btn btn-default" onclick="refundpaymentcheckexp('<%=m.get("HDRID")%>','<%=id%>','<%=m.get("ID")%>','<%=m.get("EXPID") %>','<%=Paymentamount%>','unapply')">UnApply Credits</button></td>
							<%}else{ %>
								<td class="text-center hideprintpdf">Reconciled</td>
							<%}%>
						</tr>
				  	<%}%>
				  	
					</tbody>
				</table>
					
			</div>
		</div>
		<%}%>
		
		<div class="row">
			<div class="col-xs-9"></div>
			<div class="col-xs-3">
			<br><br><br>
			<br><br><br>		
		</div>
		</span>
		</div>
		  <div id="unapplypayment" class="modal fade" role="dialog">
			  <div class="modal-dialog modal-sm">	
			    <!-- Modal content-->
			    <div class="modal-content">
			      <div class="modal-body">
			        <div class="row">
			        <INPUT type="hidden" name="refundid" value="">
			        <INPUT type="hidden" name="tranid" value="">
			         <INPUT type="hidden" name="rfbillno" value="">
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
					      <p> Deleted Payment information cannot be retrieved. Are you sure about deleting ?</p>
					      
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
			
			  <div id="unapplypaymentexp" class="modal fade" role="dialog">
			  <div class="modal-dialog modal-sm">	
			    <!-- Modal content-->
			    <div class="modal-content">
			      <div class="modal-body">
			        <div class="row">
			        <INPUT type="hidden" name="refundidexp" value="">
			        <INPUT type="hidden" name="tranidexp" value="">
			         <INPUT type="hidden" name="rfexpid" value="">
			          <INPUT type="hidden" name="rfamountexp" value="">
			           <INPUT type="hidden" name="logstatusexp" value="">
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
					      <p> Deleted Payment information cannot be retrieved. Are you sure about deleting ?</p>
					      
					      <div class="alert-actions btn-toolbar">
					         <button class="btn btn-primary ember-view" onclick="refundpaymentexp()" style="background:red;">
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
			        <INPUT type="hidden" name="rid" value="<%=id%>">
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
					      <p> Deleted Payment information cannot be retrieved. Are you sure about deleting ?</p>
					      
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
									<small class="text-muted">Amount is displayed in your payment currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=curency%></span> 
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
		</div>
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
				  if ("Advance".equals((String)grnbillHdr.get("PAID_THROUGH"))){
					  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,(String)grnbillHdr.get("ID"), "SUPPLIERCREDITNOTES");
				  }else{
					  journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant,id, "PURCHASEPAYMENT");
				  }
			  %> --%>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail', 'All', '<%=numberOfDecimal%>');
			});
			
			 $("#bill-delete").click(function() {    
				 var trid = $("input[name ='rid']").val();
				 var delestatuscheck ='<%=delestatus%>';
				 var delecredit = '<%=delecredit%>';
				 var crstatus ='<%=crstatus%>';
				 if(delestatuscheck == "0"){
					 if (crstatus != '0') {
						result = "Credit Notes payment are not allow to delete";
						window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
					 }else{
						 $.ajax({
								type : "GET",
								url: '/track/BillPaymentServlet?CMD=checkpdcstatusfordelete',
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
										window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
									}else{
										result = "Payment not allow to delete once PDC cheque marked as processed";
										window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
									}
									
								}
							});
					 }
				 }else{
					result = "Once payment 'Apply Credit' in other payment not allow to delete. Please check Credit Payment hyperlink for Apply Credit payment details";
					window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
				 }
			 });
			 
			 function delpayment(){
				var tranid = $("input[name ='rid']").val();
				var remark = '<%=alremarks%>';
				window.location.href = "/track/BillPaymentServlet?CMD=deletepayment&TRANSID="+tranid+"&REMARK="+remark;
			}
			
			function refundpaymentcheck(id,trid,detid,rfbillno,rfamount,status){
				$.ajax({
					type : "GET",
					url: '/track/BillPaymentServlet?CMD=checkpdcstatus',
					async : true,
					dataType: 'json',
					data : {
						PLANT : "<%=plant%>",
						DETID : id,
					},
					success : function(data) {
						var result = "";
						if(data == "0"){
							$("input[name ='refundid']").val(detid);
							$("input[name ='tranid']").val(trid);
							$("input[name ='rfbillno']").val(rfbillno);
							$("input[name ='rfamount']").val(rfamount);
							$("input[name ='logstatus']").val(status);
							$('#unapplypayment').modal('show');
						}else if(data == "2"){
							result = "PDC Status check Error";
								window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
						}else{
							result = "Payment not allow to UnApply Credit once PDC cheque marked as processed";
							window.location.href = "billPaymentDetail.jsp?TRANID="+trid+"&resultnew="+ result;
						}
						
					}
				});	
				
			}
			
			function refundpaymentcheckexp(id,trid,detid,rfexpid,rfamount,status){
				$.ajax({
					type : "GET",
					url: '/track/BillPaymentServlet?CMD=checkpdcstatus',
					async : true,
					dataType: 'json',
					data : {
						PLANT : "<%=plant%>",
						DETID : id,
					},
					success : function(data) {
						var result = "";
						if(data == "0"){
							$("input[name ='refundidexp']").val(detid);
							$("input[name ='tranidexp']").val(trid);
							$("input[name ='rfexpid']").val(rfexpid);
							$("input[name ='rfamountexp']").val(rfamount);
							$("input[name ='logstatusexp']").val(status);
							$('#unapplypaymentexp').modal('show');
						}else if(data == "2"){
							result = "PDC Status check Error";
							window.location.href = "billPaymentDetail.jsp?TRANID="+trid+"&resultnew="+ result;
						}else{
							result = "Payment not allow to UnApply Credit once PDC cheque marked as processed";
							window.location.href = "../banking/billpaydetail?TRANID="+trid+"&resultnew="+ result;
						}
						
					}
				});	
				
			}
			
			function refundpayment(){
				var id = $("input[name ='refundid']").val();
				var tranid = $("input[name ='tranid']").val();
				var billno = $("input[name ='rfbillno']").val();
				var amount = $("input[name ='rfamount']").val();
				var logstatus =$("input[name ='logstatus']").val();
				window.location.href = "/track/BillPaymentServlet?CMD=refundcredit&DETID="+id+"&TRANSID="+tranid+"&BILLNO="+billno+"&AMOUNT="+amount+"&LOGSTATUS="+logstatus;
			}
			
			function refundpaymentexp(){
				var id = $("input[name ='refundidexp']").val();
				var tranid = $("input[name ='tranidexp']").val();
				var expid = $("input[name ='rfexpid']").val();
				var amount = $("input[name ='rfamountexp']").val();
				var logstatus =$("input[name ='logstatusexp']").val();
				window.location.href = "/track/BillPaymentServlet?CMD=refundcreditexp&DETID="+id+"&TRANSID="+tranid+"&EXPID="+expid+"&AMOUNT="+amount+"&LOGSTATUS="+logstatus;
			}
			
			function generatePdf(dataUrl){
				$(".hideprintpdf").css("display", "none");
				var doc = new jsPDF('l', 'mm', 'a4');
				var pageNumber;
				/* Top Right */
				
				

				doc.autoTable({
					html : '#table1',
					startY : 63,
					margin : {left : 142},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				});
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

				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('PAYMENT DETAIL', 100, 69);
				doc.setFontStyle("normal");
				
				
				
				doc.setFillColor("#78ae54");
				doc.rect(162, 87, 45, 15, 'F');
				
				doc.setTextColor(255,255,255);
				doc.text('Amount Paid ', 165, 93);
				<%-- doc.text('<%=curency%><%=Numbers.toMillionFormat(amountpaid,numberOfDecimal)%>', 165, 107); --%>
				doc.text('<%=tamtdiaply%>', 165, 97);
				
				doc.setTextColor(0, 0, 0);

				var lref='<%=grnbillHdr.get("REFERENCE")%>';
				var nlen = lref.length;
				//alert(nlen);
				if(nlen >= 150){
					
					doc.text('Payment Date ', 16, 83);
					doc.text('<%=grnbillHdr.get("PAYMENT_DATE")%>', 56, 83);
					
					<%-- doc.text('Project ', 16, 86);
					doc.text('<%=projectname%>', 56, 86); --%>
					
					var reference = doc.splitTextToSize('<%=grnbillHdr.get("REFERENCE")%>', 100);
					doc.text('Reference ', 16, 88);
					doc.text(reference, 56, 88);
				}else if(150 > nlen > 100){
					doc.text('Payment Date ', 16, 83);
					doc.text('<%=grnbillHdr.get("PAYMENT_DATE")%>', 56, 83);
					
					<%-- doc.text('Project ', 16, 88);
					doc.text('<%=projectname%>', 56, 88); --%>
					
					var reference = doc.splitTextToSize('<%=grnbillHdr.get("REFERENCE")%>', 100);
					doc.text('Reference ', 16, 88);
					doc.text(reference, 56, 88);
				
				}else{
					doc.text('Payment Date ', 16, 83);
					doc.text('<%=grnbillHdr.get("PAYMENT_DATE")%>', 56, 83);
					
					<%-- doc.text('Project ', 16, 92);
					doc.text('<%=projectname%>', 56, 92); --%>
					
					var reference = doc.splitTextToSize('<%=grnbillHdr.get("REFERENCE")%>', 100);
					doc.text('Reference ', 16, 88);
					doc.text(reference, 56, 88);
				}
				
				<%if(grnbillHdr.get("VENDNO").equals("N/A")){ %>
					var vname = doc.splitTextToSize('<%=grnbillHdr.get("ACCOUNT_NAME")%>', 90);
					doc.text('Account Name ', 16, 93);
					doc.setFontStyle("bold");
					doc.text(vname, 56, 93);
				<%}else{%>
					var vname = doc.splitTextToSize('<%=grnbillHdr.get("VNAME")%>', 90);
					doc.text('Paid To ', 16, 93);
					doc.setFontStyle("bold");
					doc.text(vname, 56, 93);
				<%}%>
				var pmode = doc.splitTextToSize('<%=grnbillHdr.get("PAYMENT_MODE")%>', 100);
				doc.setFontStyle("normal");
				doc.text('Payment Mode ', 16, 98);
				doc.text(pmode, 56, 98);
				var paidthrough = doc.splitTextToSize('<%=grnbillHdr.get("PAID_THROUGH")%>', 100);
				doc.text('Paid Through ', 16, 103);
				doc.text(paidthrough, 56, 103);
				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : 110,
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
					//columnStyles : {0 : {halign : 'left',cellWidth: 35},1 : {halign : 'center'},2 : {halign : 'right'},3 : {halign : 'center'},4 : {halign : 'right'},5 : {halign : 'right'},6 : {halign : 'right'},7 : {halign : 'right'}},
					columnStyles : {0 : {halign : 'left',cellWidth: 35},1 : {halign : 'left'},2 : {halign : 'center',cellWidth: 25},3 : {halign : 'right'},4 : {halign : 'center'},5 : {halign : 'right'},6 : {halign : 'right'},7 : {halign : 'right'},8 : {halign : 'right'}},
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

				doc.autoTable({
					html : '#table3',
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center'
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ]
					},
					//margin : {left : 123},
					columnStyles : {0 : {halign : 'left',cellWidth: 35},1 : {halign : 'left'},2 : {halign : 'center',cellWidth: 25},3 : {halign : 'right'},4 : {halign : 'center'},5 : {halign : 'right'},6 : {halign : 'right'},7 : {halign : 'right'},8 : {halign : 'right'}},
					theme : 'plain'
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
				doc.save('Payment.pdf');
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
