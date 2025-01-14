<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
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
	String title = "Expense Detail";
	String rootURI = HttpUtils.getRootURI(request);
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayConvertToInvoice=false,displaySummaryMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editexpenses", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printexpenses", plant,username);
		displayConvertToInvoice = ub.isCheckValAcc("convertexpenses", plant,username);
		displaySummaryMore = ub.isCheckValAcc("moreexpenses", plant,username);	
	}
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	CustMstDAO custMstDAO = new CustMstDAO();
	BillDAO bildao = new BillDAO();
	DateUtils _dateUtils = new DateUtils();

	ExpensesUtil expensesUtil = new ExpensesUtil();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String ExpenseHDRid = StrUtils.fString(request.getParameter("TRANID"));
	Hashtable ht = new Hashtable();
	ht.put("ID", ExpenseHDRid);
	//List expenselisthdrdet = expensesUtil.getExpensesforDetails(ht, plant);
	List expenselisthdrdet = expensesUtil.getConvExpensesforDetails(ht, plant);
	Map expenselist=(Map)expenselisthdrdet.get(0);
	
	double expgst = Double.valueOf((String) expenselist.get("EXPENSETAX"));
	double expgstamount = Double.valueOf((String) expenselist.get("EXPENSETAXAMOUNT"));
	double rgst = Double.valueOf((String) expenselist.get("TAXAMOUNT"));
	
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
	
	String expjnledit = (String) plntMap.get("IsExpJournalEdit");
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	
	String totalAmount= (String) expenselist.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) expenselist.get("SUB_TOTAL");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);	

	
	String custName = custMstDAO.getCustName(plant, (String) expenselist.get("CUST_CODE"));
	boolean mesflag =false;
	if(!expenselist.get("ORDERNO").equals("")){
		Hashtable Shipment =new Hashtable(); 
	    Shipment.put("PLANT", plant);
	    Shipment.put("PONO", expenselist.get("ORDERNO"));
	    Shipment.put("SHIPMENT_CODE", expenselist.get("SHIPMENT_CODE"));
	    mesflag = bildao.isShipmentcode(Shipment, "");
	}
	
	String taxidsting = (String) expenselist.get("TAXID");
	String stdtaxstring = (String) expenselist.get("STANDARDTAX");
	int taxid = Integer.valueOf(taxidsting);
	double stdtax = Double.valueOf(stdtaxstring);
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	String taxdisplay="";
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(Integer.valueOf((String) expenselist.get("PROJECTID")) > 0){
		finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String) expenselist.get("PROJECTID")));
		projectname = finProject.getPROJECT_NAME();
	}
	
	String zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);	
	String curDate = _dateUtils.getDate();
	String basecurrency = _PlantMstDAO.getBaseCurrency(plant);
	
	
	
	JournalDAO journalDAO=new JournalDAO();
	Journal journal = new Journal();
	boolean jstatus = journalDAO.isjournalExisit(plant, "EXPENSE", ExpenseHDRid);
	if(jstatus) {
		int hdrid = new JournalDAO().getjid(plant, "EXPENSE", ExpenseHDRid);
		journal=journalDAO.getJournalById(plant, hdrid);
	}

%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EXPENSES%>"/>
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
.invoice-banner {
    margin-top: 15px;
    margin-bottom: 10px;
    font-size: 13px;
    background-color: #fdfae4;
    border: 1px solid #ede5ae;
    padding: 10px;
    overflow: visible;
}

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<div class="container-fluid m-t-20">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
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
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../expenses/summary"><span class="underline-on-hover">Expenses Summary</span></a></li> 
                <li><label>Expense Detail</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<%if(expjnledit.equals("1")){
				if(expenselist.get("ISPOS").equals("1")){ %>	
				<div class="btn-group" role="group">
					<button type="button pull-right" id="addjournal" class="btn btn-success ">Add Journal</button>
				</div>
				<%}
			}%>
				&nbsp;
				<div class="btn-group" role="group">
				 <% if (displaySummaryEdit) { %>
					<button type="button" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
					 <a href="../expenses/new?cmd=Edit&TRANID=<%=ExpenseHDRid%>">
						<i class="fa fa-pencil" aria-hidden="true"></i>
						</a>
					</button>
					 <%} %>
					 <% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					 <% } %>
					  <% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					 <% } %>
				</div>
				
				<div class="btn-group" role="group">
				<% if (displayConvertToInvoice) { %>
					<%if(expenselist.get("STATUS").equals("UNBILLED")){ %>
						<a href="../expenses/converttoinvoice?cmd=insert&TRANID=<%=ExpenseHDRid%>"><button type="button pull-right" class="btn btn-success ">Convert to Invoice</button></a>
					<%}%>
					<%}%>
				</div>
				&nbsp;
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  	<% if (displaySummaryMore) { %>
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					  	
					   <ul class="dropdown-menu" style="min-width: 0px;">
					    <li id="bill-copy"><a href="../expenses/new?cmd=Copy&TRANID=<%=ExpenseHDRid%>">Copy</a></li>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					  </ul>
					  	<% } %>
				</div>
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../expenses/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=expenselist.get("STATUS")%></div>
		</div>
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

			<div class="col-xs-6 text-right">
			<%if(expenselist.get("ORDERNO").equals("") || expenselist.get("ORDERNO").equals(null) ){ %>	
				<%}else{ %>
				<h3>Purchase Order</h3>
				<p># <%=expenselist.get("ORDERNO")%></p>
				
				<h3>Shipment Reference</h3>
				<p># <%=expenselist.get("SHIPMENT_CODE")%></p>
				<%} %>
				
				<%if(expenselist.get("EXBILL").equals("") || expenselist.get("EXBILL").equals(null) ){ %>	
				<%}else{ %>
				<h3>Bill</h3>
				<p># <%=expenselist.get("EXBILL")%></p>
				<%} %>
				
				<br> <span>Expense Amount</span>
				<h3><%=expenselist.get("CURRENCYID")%><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal))%></h3>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-7">
				
				<span>Customer</span>	<br>			
				<a href="#"><%=expenselist.get("CUSTOMER")%></a><br>
					<span>Paid To</span>	<br>			
				<a href="#"><%=expenselist.get("VNAME")%></a><br>
				<span>Project : <%=projectname %></span><br>
					
			</div>
			<div class="col-xs-5 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td>Paid Through  :</td>
							<td><%=expenselist.get("PAID_THROUGH")%></td>
						</tr>						
						<tr>
							<td>Date :</td>
							<td><%=expenselist.get("EXPENSES_DATE")%></td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table" style="table-layout: fixed;">
					<thead>
						<tr>
							<td class="text-left">Account Code</td>
							<td class="text-left">Expenses Account</td>
							<td class="text-left">Notes</td>
							<td class="text-left">Tax</td>
							<td class="text-right">Amount</td>
						</tr>
					</thead>
					<tbody>
					<%
					double acpayble=0.0;
					for(int i =0; i<expenselisthdrdet.size(); i++) {   
				  		Map m=(Map)expenselisthdrdet.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		String isexpgst = (String)m.get("ISEXPENSEGST");
				  		if(isexpgst.equalsIgnoreCase("0")){
				  			taxdisplay = (String)m.get("TAX_TYPE");
				  		}
			  			amount = (String) m.get("AMOUNT");
			  			double dAmount ="".equals(amount) ? 0.0d :  Double.parseDouble(amount);
			  			amount = StrUtils.addZeroes(dAmount, numberOfDecimal);
			  			CoaDAO coaDAO=new CoaDAO();
			  			String accountcode = coaDAO.GetAccountCodeByName((String)m.get("EXPENSES_ACCOUNT"), plant);
			  			String eaccount = (String)m.get("EXPENSES_ACCOUNT");
			  			if(eaccount.equalsIgnoreCase("Account Payable")){
			  				acpayble = acpayble + dAmount;
			  			}
			  		%>
				  		<tr>
				  			<td class="text-left"><%=accountcode%></td>
							<td class="text-left"><%=(String)m.get("EXPENSES_ACCOUNT") %></td>
							<td class="text-left" style="word-wrap: break-word;"><%=(String)m.get("DESCRIPTION")%></td>
							<td class="text-left"><%=(String)m.get("TAX_TYPE")%></td>
							<td class="text-right"><%=Numbers.toMillionFormat(amount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<!-- <div class="col-xs-9"></div> -->
			<div class="col-xs-12">
				<table id="table3" class="table text-right">
					<tbody>
						<tr>
							<td>Sub Total</td>
							<td><%=Numbers.toMillionFormat(subTotal, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%
						double totax = 0;
						if(taxid != 0){
								if(fintaxtype.getISZERO() == 1){
									totax = 0;
								}else{
									 //totax = (Double.valueOf(subTotal)/100)*stdtax;
									totax = rgst;
								}
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(StrUtils.addZeroes(totax, numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></td>
										</tr>
								<%}
						 }%>
						 
						 <%
						 if(expgstamount != 0){
							 String newtaxdisplay="STANDARD RATED ["+StrUtils.addZeroes(expgst, "1")+"%]";%>
							 <tr>
								<td><%=newtaxdisplay%></td>
								<td><%=Numbers.toMillionFormat(StrUtils.addZeroes(expgstamount, numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></td>
							</tr>
						 <%}
						 %>
						
						 <%
						 if(acpayble != 0){%>
							 <tr>
								<td>Account Payable</td>
								<td><%=Numbers.toMillionFormat(StrUtils.addZeroes(acpayble, numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></td>
							</tr>
						 <%}
						 %>
						<tr>
							<td><b>Total (<%=expenselist.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%if(!curency.equalsIgnoreCase((String)expenselist.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency -->
						<tr id="showtotalcur">
							<td><b>Total (<%=curency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(StrUtils.addZeroes((dTotalAmount/Double.parseDouble((String)expenselist.get("CURRENCYTOBASE"))), numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%} else {%>
						<tr id="showtotalcur">
							<td></td>
							<td></td>
						</tr>
						<%}%>
					</tbody>
				</table>
			</div>
		</div>
		</span>
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
									<small class="text-muted">Amount is displayed in your expense currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=expenselist.get("CURRENCYID")%></span>
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
	<div id="deleteexpense" class="modal fade" role="dialog">
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
			      <p> Deleted Expenses information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
		

	
		<div id="addjournalpopup" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form id="journalForm" class="form-horizontal" name="form1"  action="/track/JournalServlet?action=expjcreate" method="post" enctype="multipart/form-data">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Add journal</h3>
					</div>
					<div class="modal-body">	
							<input type="text" name="username" value=<%=username%> hidden>
							<input type="text" name="plant" value="<%=plant%>" hidden>
							<input type="text" name="currency"  value="<%=basecurrency%>" hidden>
							<input id="sub_total" name="sub_total" value="" hidden>
							<input id="total_amount" name="total_amount" value="" hidden>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Expense
										Amount</label>
									<div class="col-sm-4">
										<input class="form-control" name="expamount" id="expamount" type="text" value="<%=totalAmount%>" disabled> 
										<input name="eid" id="eid" type="text" value="<%=ExpenseHDRid%>" hidden>
									</div>
								</div>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Expense Date</label>
									<div class="col-sm-4">
										<input class="form-control" name="expdate" id="expdate" type="text" value="<%=expenselist.get("EXPENSES_DATE")%>" disabled>
									</div>
								</div>
								
								<%if(jstatus) {%>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Journal Date</label>
									<div class="col-sm-4">
										<input type="text" class="form-control datepicker" value="<%=journal.getJournalHeader().getJOURNAL_DATE()%>" id="journal_date" name="journal_date">
									</div>
								</div>
								<%}else{%>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Journal Date</label>
									<div class="col-sm-4">
										<input type="text" class="form-control datepicker" value="<%=curDate%>" id="journal_date" name="journal_date">
									</div>
								</div>
								<%}%>



								<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:95%;">
			<thead>
			  <tr>
				<th class="journal-acc" >Account</th>
				<th class="journal-desc">Description</th>								
				<th class="journal-debit">Debits</th>
				<th class="journal-credit">Credits</th>
			  </tr>
			</thead>
			<tbody>
			  
			  <%if(jstatus) {
				  List<JournalDetail> journalDetails=journal.getJournalDetails();
				  for (JournalDetail jd:journalDetails){%>
				  <tr>
			  <td class="journal-acc">
					<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account" value="<%=jd.getACCOUNT_NAME()%>">
				</td>
				<td  class="col-sm-6 journal-desc">
					<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"><%=jd.getDESCRIPTION()%></textarea>
				</td>
				<td class="journal-debit">
					<input name="debit" type="text" onchange="journaldebit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off" value="<%=jd.getDEBITS()%>">
				</td>
				<td class="journal-credit">
					<input type="text" name="credit" onchange="journalcredit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off" value="<%=jd.getCREDITS()%>">
				</td>
				  </tr>
			  <%}
				  }else{%>
			  <tr>
				<td class="journal-acc">
					<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account">
				</td>
				<td  class="col-sm-6 journal-desc">
					<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"></textarea>
				</td>
				<td class="journal-debit">
					<input name="debit" type="text" onchange="journaldebit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off" value="<%=zeroval%>">
				</td>
				<td class="journal-credit">
					<input type="text" name="credit" onchange="journalcredit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off" value="<%=zeroval%>">
				</td>	
				  </tr>			
				<%}%>
				
				
			

			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-7">
				<!-- <a style="text-decoration:none;cursor: pointer;" onclick="addRow()" tabindex="0">+ Add another line</a> -->
				<a href="#" onclick="addRow(event)"><i class="" title="Add another line" style="font-size: 15px;">+ Add another line</i></a>
			</div>
			<div class="total-section col-sm-5">
				<div class="row sub-total">
					<div class="col-sm-4 total-label"> Sub Total <br>  
					</div> 
					<div class="subtotal-debitamount col-sm-3" id="subtot-debitamt"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
					<div class="subtotal-creditamount col-sm-3 col-sm-offset-1" id="subtot-creditamt"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
				</div>
				<br>
				<div class="row gross-total">
					<div class="col-sm-4 total-label"> Total </div> 
					<div class="total-debitamount col-sm-3" id="total-debitamount"><%=String.format("%."+numberOfDecimal+"f", 0.00)%> </div>
					<div class="total-creditamount col-sm-3 col-sm-offset-1" id="total-creditamount"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
				</div>
				
			</div>
		</div>
	
						
						

						
						<div class="row form-group">
							<div class="col-sm-5"></div>
							<div class="col-sm-3">
								<div class="alert-actions btn-toolbar">
									<button type="button" class="btn btn-primary ember-view"
										onclick="addjournal()" style="background: green;">
										Submit</button>
									<button type="button" class="btn btn-primary ember-view"
										data-dismiss="modal" style="background: red;">Cancel</button>
								</div>
							</div>
							<div class="col-sm-5"></div>
						</div>
	
					</div>
				</div>
			</form>
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
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
			  $('[data-toggle="tooltip"]').tooltip();  
			  $('.printMe').click(function(){
				  <%if(!curency.equalsIgnoreCase((String)expenselist.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency -->
				  document.getElementById("showtotalcur").style.display = "none";
				  <%}%>
				     $("#print_id").print();
				     <%if(!curency.equalsIgnoreCase((String)expenselist.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency -->
					  document.getElementById("showtotalcur").style.display = "contents";
					  <%}%>
				});
			  $("#bill-delete").click(function() {    
				  var expid ="<%=ExpenseHDRid%>";
		          var status = "<%=expenselist.get("STATUS")%>";
		          var taxstatus = "<%=expenselist.get("TAX_STATUS")%>"
		          var expstatus = "<%=mesflag%>"
		          if(status.toLowerCase() == "billed"){
		        	  result = "Expenses that has marked as Billed not allow to delete.";
					  window.location.href = "../expenses/detail?TRANID="+expid+"&resultnew="+ result;
		          }else  if(taxstatus.toLowerCase() == "tax generated"){
		        	  result = "Transaction that already filed Tax Return not allow to Delete";
					  window.location.href = "../expenses/detail?TRANID="+expid+"&resultnew="+ result;
	        	  }else{
	        		  if(expstatus != "true"){
	        			  $('#deleteexpense').modal('show');
	        		  }else{
	        			  result = "Expenses that has Shipment Reference already processed with a Bill not allow to delete.";
						  window.location.href = "../expenses/detail?TRANID="+expid+"&resultnew="+ result; 
	        		  }
	        	  }
		          
				 });
			  var plant = '<%=plant%>';
			  $(".journalaccountSearch").typeahead({
					input:".journalaccountSearch",
				  hint: true,
				  minLength:0,  
				  searchOnFocus: true,
				  classNames: {
					 	menu: 'bigdrop'
					  }
				},
				{	  
				  display: 'accountname',  
				  source: function (query, process,asyncProcess) {
						var urlStr = "/track/ChartOfAccountServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : plant,
							action : "getSubAccountTypeGroup",
							module:"journalaccount",
							ITEM : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.results);
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
					suggestion: function(item) {
						if (item.issub) {
							var $state = $(
								    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
								  );
							}
						else
							{
							var $state = $(
									 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
								  );
							}
						  
						  return $state;
					}
				  }
				}).on('typeahead:render',function(event,selection){
					var menuElement = $(this).parent().find(".bigdrop");
					var top = menuElement.height()+35;
					top+="px";	
					if(menuElement.next().hasClass("footer")){
						menuElement.next().remove();  
					}
					menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
					menuElement.next().width(menuElement.width());
					menuElement.next().css({ "top": top,"padding":"3px 20px" });
					if($(this).parent().find(".bigdrop").css('display') != "block")
						menuElement.next().hide();	  
				}).on('typeahead:open',function(event,selection){
					var menuElement = $(this).parent().find(".bigdrop");
					menuElement.next().show();
				}).on('typeahead:close',function(){
					var menuElement = $(this).parent().find(".bigdrop");
					setTimeout(function(){ menuElement.next().hide();}, 180);
				});
			  
			  $("#cfmdelete").click(function(){
				  window.location.href = "/track/ExpensesServlet?ACTION=deleteexpense&ID=<%=ExpenseHDRid%>&ORDERNO=<%=expenselist.get("ORDERNO")%>&STATUS=<%=expenselist.get("STATUS")%>&TAXSTATUS=<%=expenselist.get("TAX_STATUS")%>&SHIPMENT_CODE=<%=expenselist.get("SHIPMENT_CODE") %>";
					});
			  <%
				Journal journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant, ExpenseHDRid, "EXPENSE", true);
			  %>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
			});
			
			
			
			
function generatePdf(dataUrl){
				
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
			
			
				var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
				var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
				
				
				<%if(expenselist.get("ORDERNO").equals("") || expenselist.get("ORDERNO").equals(null) ){%>	
					doc.setFontSize(8);
					doc.text('Expense Amount', 195, 46, {align:'right'});
	
					doc.setFontSize(12);
					doc.text('<%=curency%><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal))%>', 195, 52, {align:'right'});
				<%}else{%>
				
					/* Top Right */
					doc.setFontSize(14);
					doc.text('Purchase Order', 195, 30, {align:'right'});
					
					doc.setFontSize(8);
					doc.text('# <%=expenselist.get("ORDERNO")%>', 195, 37, {align:'right'});
				
					doc.setFontSize(14);
					doc.text('Shipment Reference', 195, 46, {align:'right'});
	
					doc.setFontSize(8);
					doc.text('# <%=expenselist.get("SHIPMENT_CODE")%>', 195, 52, {align:'right'});
					
	
					doc.setFontSize(8);
					doc.text('Expense Amount', 195, 63, {align:'right'});
	
					doc.setFontSize(8);
					doc.text('<%=expenselist.get("CURRENCYID")%><%=totalAmount%>', 195, 70, {align:'right'});
				 <%}%> 
				
				
				
				doc.autoTable({
					html : '#table1',
					startY : 83,
					margin : {left : 137},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					styles: {fontSize: 8},
					theme : 'plain'
				});
				/* **** */

				/* Top Left */
				
				doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
				
				doc.setFontSize(8);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

				//doc.text('United Arab Emirates', 16, 73);
				
				doc.setFontSize(8);
				doc.text('Customer', 16, 88);

				doc.setFontStyle("bold");
				var splitcustname = doc.splitTextToSize('<%=expenselist.get("CUSTOMER")%>', 100);
				doc.text(splitcustname, 16, 92);

				doc.setFontSize(8);
				doc.setFontStyle("normal");
				doc.text('Paid To', 16, 99);
				

				doc.setFontStyle("bold");
				var splitvendname = doc.splitTextToSize('<%=expenselist.get("VNAME")%>', 100);
				doc.text(splitvendname, 16, 103);
				
				doc.setFontSize(8);
				doc.setFontStyle("normal");
				doc.text('Project : <%=projectname%>', 16, 108);
				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : 110,
					styles: {fontSize: 8},
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
					//columnStyles: {0: {halign: 'left',columnWidth: 30},1: {halign: 'center',columnWidth: 30},2: {halign: 'left',columnWidth: 70},3: {halign: 'center',columnWidth: 30},4: {halign: 'center',columnWidth: 30}},
					columnStyles: {0: {halign: 'center',cellWidth: 'auto'},1: {halign: 'center',cellWidth: 'auto'},2: {halign: 'left',cellWidth: 'auto'},3: {halign: 'center',cellWidth: 'auto'},4: {halign: 'right',cellWidth: 'auto'}},
					didDrawPage : function(data) {
						doc.setFontStyle("normal");
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(8);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, 178,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;
/* 
				doc.autoTable({
					html : '#table3',
					margin : {left : 123},
					styles: {fontSize: 8},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				}); */
				
				doc.autoTable({
					html : '#table3',
					margin : {left : 100},
					styles: {fontSize: 8},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain',
					 didParseCell: function (data) {
					        var rows = data.table.body;
					        if (data.row.index === rows.length - 1) {
					            data.cell.text = "";
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
					doc.setFontSize(8);
	
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
				doc.save('Expenses.pdf');
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
			
			$("#addjournal").click(function() {  
					calculateDebitTotal();
					calculateCreditTotal();
					$('#addjournalpopup').modal('show');
				 });
			
			var numberOfDecimal = $("#numberOfDecimal").val();
			function addRow(event){
				event.preventDefault(); 
			    event.stopPropagation();
			    var debit=0;
			    var credit=0;
			    $(".bill-table tbody tr").each(function() {
					 var dval = $(this).find("input[name ='debit']").val();
					 var cval = $(this).find("input[name ='credit']").val();
					 if(dval.length > 0){
					 debit += parseFloat($(this).find("input[name ='debit']").val());
					 }
					 if(cval.length > 0){
					 credit += parseFloat($(this).find("input[name ='credit']").val());
					 }
				});
				var body="";
				body += '<tr>';
				body += '<td class="journal-acc">';
				body += '<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account">';
				body += '</td>';
				body += '<td  class="col-sm-6 journal-desc">';
				body += '<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"></textarea>';
				body += '</td>';
				
				if(debit > credit){
					var valdiff = parseFloat(debit) - parseFloat(credit);
					body += '<td class="journal-debit">';	
					body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';
					body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
					body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat(valdiff).toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';	
				}else if(credit > debit){
					var valdiff = parseFloat(credit) - parseFloat(debit);
					body += '<td class="journal-debit">';	
					body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat(valdiff).toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';
					body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
					body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';	
				}else{
					body += '<td class="journal-debit">';	
					body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';
					body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
					body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';	
				}
				body += '</tr>';
				$(".bill-table tbody").append(body);
				removeSuggestionToTable();
				addSuggestionToTable();
				calculateDebitTotal();
				calculateCreditTotal();
			}
			
			function removeSuggestionToTable(){
				$(".journalaccountSearch").typeahead('destroy');
			}
			
			function addSuggestionToTable(){
				var plant =  '<%=plant%>';	
				$(".journalaccountSearch").typeahead({
					input:".journalaccountSearch",
				  hint: true,
				  minLength:0,  
				  searchOnFocus: true,
				  classNames: {
					 	menu: 'bigdrop'
					  }
				},
				{	  
				  display: 'accountname',  
				  source: function (query, process,asyncProcess) {
						var urlStr = "/track/ChartOfAccountServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : plant,
							action : "getSubAccountTypeGroup",
							module: "journalaccount",
							ITEM : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.results);
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
					suggestion: function(item) {
						if (item.issub) {
							var $state = $(
								    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
								  );
							}
						else
							{
							var $state = $(
									 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
								  );
							}
						  
						  return $state;
					}
				  }
				}).on('typeahead:render',function(event,selection){
					var menuElement = $(this).parent().find(".bigdrop");
					var top = menuElement.height()+35;
					top+="px";	
					if(menuElement.next().hasClass("footer")){
						menuElement.next().remove();  
					}
					menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
					menuElement.next().width(menuElement.width());
					menuElement.next().css({ "top": top,"padding":"3px 20px" });
					if($(this).parent().find(".bigdrop").css('display') != "block")
						menuElement.next().hide();	  
				}).on('typeahead:open',function(event,selection){
					var menuElement = $(this).parent().find(".bigdrop");
					menuElement.next().show();
				}).on('typeahead:close',function(){
					var menuElement = $(this).parent().find(".bigdrop");
					setTimeout(function(){ menuElement.next().hide();}, 180);
				});
			}
			
			function journaldebit(node)
			{
				//alert($(node).val());
				var debit= checkamount($(node).val(),node);
				debit=parseFloat(debit).toFixed(numberOfDecimal);
				$(node).val(debit);
				$(node).closest('tr').find("td:nth-child(4)").find('input').val("");
				calculateDebitTotal();
				calculateCreditTotal();
				}
			function journalcredit(node)
			{
				//var credit=$(node).val();
				var credit= checkamount($(node).val(),node);
				credit=parseFloat(credit).toFixed(numberOfDecimal);
				$(node).val(credit);
				$(node).closest('tr').find("td:nth-child(3)").find('input').val("");
				calculateCreditTotal();
				calculateDebitTotal();
				}	
				function calculateDebitTotal()
				{
					
					var totaldebit=0.00;
					$('input[name=debit]').each(function(){
						var value=$(this).val();
						if(value!="")
							totaldebit+=parseFloat(value);
						
					})
					totaldebit=parseFloat(totaldebit).toFixed(numberOfDecimal);
					$('#subtot-debitamt').html(totaldebit);
					$('#total-debitamount').html(totaldebit);
					
				}
				function calculateCreditTotal()
				{
					var totalcredit=0.00;
					$('input[name=credit]').each(function(){
						var value=$(this).val();
						if(value!="")
							totalcredit+=parseFloat(value);
					})
					totalcredit=parseFloat(totalcredit).toFixed(numberOfDecimal);
					$('#subtot-creditamt').html(totalcredit);
					$('#total-creditamount').html(totalcredit);
				}
				
				function checkamount(baseamount,node){
					var zeroval = parseFloat("0").toFixed(numberOfDecimal);
					if(baseamount != ""){
						var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
						var numbers = /^[0-9]+$/;
						if(baseamount.match(decimal) || baseamount.match(numbers)) 
						{ 
							//node.val(parseFloat(baseamount).toFixed(3));	
							return $(node).val();
						}else{
							alert("Please Enter Valid Amount");
							return zeroval;
						}
					}else{
						return $(node).val();
					}
				}

				function isNumberKey(evt, element, id) {
					  var charCode = (evt.which) ? evt.which : event.keyCode;
					  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
						  {
					    	return false;
						  }
					  return true;
					}
				
				$(".bill-table tbody").on('click','.bill-action',function(){
					debugger;	    
				    $(this).parent().parent().remove();
				    calculateCreditTotal();
					calculateDebitTotal();
				});
				
				function addjournal(){
					var totdebitamt= parseFloat($('#total-debitamount').html());
					var totcreditamt=parseFloat($('#total-creditamount').html());
					var expamt=$("input[name ='expamount']").val();
					var isItemValid = true;
					$("#sub_total").val(totdebitamt);
					$("#total_amount").val(totcreditamt);


					$("input[name ='journal_account_name']").each(function() {
					    if($(this).val() == ""){	    	
					    	alert("The journal account field cannot be empty.");
					    	isItemValid = false;
							return false;
					    }
					});

						if(totdebitamt!=totcreditamt)
						{
							alert("Debit and Credit should be equal");
							isItemValid = false;
							return false;
						}
					else if(totdebitamt<=0)
						{
							alert("Please fill all the details");
							isItemValid = false;
							return false;
						}
						
						if(parseFloat(expamt)!=parseFloat(totdebitamt)){
							alert("Expense and Journal Debit & Credit should be equal");
							isItemValid = false;
							return false;
						}
						
					if(isItemValid)
						$("#journalForm").submit();
				}
			
		</script>
		<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>
		<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
		
	</div>
	
</div>
<!-- model -->
<%-- <%@include file="applyCreditInvoice.jsp" %>  --%>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>