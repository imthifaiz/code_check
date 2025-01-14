<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "New Invoice Receipt";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String NOOFPAYMENT=((String) session.getAttribute("NOOFPAYMENT")); /* Resvi */
String OrdValidNumber=""; //resvi
//Validate no.of Customers -- Azees 19.11.2020
	CustMstDAO custdao = new CustMstDAO();
	String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
	String ValidNumber="";
	ArrayList arrCustot =custdao.getTotalCustomers(plant);
	Map mCustot=(Map)arrCustot.get(0);
	String Custot = (String)mCustot.get("TOTAL_CUSTOMERS");
	int novalid = Integer.valueOf(Custot);
	if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFCUSTOMER);
		if(novalid>=convl)
		{
			ValidNumber=NOOFCUSTOMER;
		}
	}
	
	//resvi starts


	InvoiceDAO invoiceDAO = new InvoiceDAO();
	DateUtils _dateUtils = new DateUtils();
	String FROM_DATE = _dateUtils.getDate();
	if (FROM_DATE.length() > 5)
		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";

	String TO_DATE = _dateUtils.getLastDayOfMonth();
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

	// resvi ends
	
	
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);

PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
MasterUtil _MasterUtil=new  MasterUtil();
String curDate =_dateUtils.getDate();
String empno = StrUtils.fString(request.getParameter("EMPNO"));
String cmd =StrUtils.fString(request.getParameter("cmd"));
String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
String type =StrUtils.fString(request.getParameter("type"));
String custno=StrUtils.fString(request.getParameter("CUSTNO"));
List invoiceHeaderList=null;
if(custno!=null || custno!="")
{
	InvoiceDAO invoiceDao=new InvoiceDAO();
	Hashtable ht = new Hashtable();
	ht.put("CUSTNO", custno);
	ht.put("PLANT", plant);
	invoiceHeaderList =  invoiceDao.getInvoiceHdrByCustNo(ht);
	
}



String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));

String taxbylabel = ub.getTaxByLable(plant);

String sCustCode = StrUtils.fString(request.getParameter("sCustCode"));
UUID uniqueKey = UUID.randomUUID();
String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
String v_amount=zeroval;
String v_amount_Curr=zeroval;
CurrencyDAO currencyDAO = new CurrencyDAO();
String CURRENCYUSEQT="0",DISPLAY="";
List curQryList=new ArrayList();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<!-- <script src="js/Invoice.js"></script> -->
<script src="../jsp/js/InvoicePaymentNew.js"></script>
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
</style>
<div class="container-fluid m-t-20">
	 <div>
	 <input type="number" id="inpayment" name="inpayment" style="display: none;" value="0">
	 <div class="tab">
  		<button class="tablinks active" onclick="openPayment(event, 'payment')">Invoice Receipt</button>
 		 <button class="tablinks" onclick="openPayment(event, 'voucher')">Receipt Voucher</button>
 	</div>
	 
	<div id="payment" class="tabcontent active" style="display: block;"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../banking/invoicepaysummary"><span class="underline-on-hover">Receipt Summary</span> </a></li>                                               
                <li><label>New Invoice Receipt</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/invoicepaysummary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<div class="container-fluid">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<INPUT type="hidden" name="B_CURRENCYUSEQT" id="B_CURRENCYUSEQT" value="<%=CURRENCYUSEQT%>">
	<form id="invoicePaymentForm" class="form-horizontal" name="form1" enctype="multipart/form-data">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Customer Name</label>
			<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="Select a customer"  name="CUSTOMER" onchange="checkcustomer(this.value)" value="<%=CUSTOMER%>" required>
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value+'&TYPE=ACCTCUST');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
				</div>
				<input type="text" name="plant" id="plant" value="<%=plant%>" hidden>
				<!-- <input name="bank_branch" type="hidden"> -->
				<input type="text" name="username" value=<%=username%> hidden>
				<input type = "hidden" name="PROJECTID" value="0">
			<INPUT type = "hidden" name="CUST_CODE" value=<%=custCode%>>
			<INPUT type = "hidden" name="curency" value = "<%=curency%>">
			<INPUT type = "hidden" name="NOOFDECIMAL" value="<%=numberOfDecimal%>">
			<INPUT type = "hidden" name="EMPNO" value = "<%=empno%>">
			<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
			<INPUT type="hidden" name="TranId" value="<%=sTranId%>" />
			<input name="uuid" type="hidden" value="<%=uniqueKey%>">
			<INPUT type="hidden" name="initialAmount" id="initialAmount" />  
			<INPUT type="hidden" name="type" value="<%=type%>" /> 
			<input name="paidpdcstatus" type="hidden" value="0">
			<input name="pdcamount" type="hidden" value="0">
			<input name="vbank" type="hidden" value="">
			<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
			<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
			<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">  <%--Resvi--%>
			<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
			<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
			<INPUT type="hidden" name="conv_amount_Curr" id="conv_amount_Curr" value=<%=v_amount_Curr%>>
			<input name="isbank" type="hidden" value="">	                     
			</div>
				
		</div>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value=""> 
								<span class="select-icon" style="right: 7px;"
								onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
						</div>
					</div>
				</div>
		<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Currency</label>
						<div class="col-sm-4"> 
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>" required>
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>
					</div>
		
					<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required" id="exchangerate" ></label>
					<div class="col-sm-4"> 
					<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" onkeypress="return isNumberKey(event,this,4)" >	
						</div>
					</div>
					<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required" id="PaymentMade" ></label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="amount_Curr" name="amount_Curr" required  value="<%=v_amount_Curr%>" onchange="calculatebasecurrency(this)" onkeypress="return isNumberKey(event,this,4)">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Amount&nbsp;(<%=curency%>)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="amount" name="amount" onchange="return amountchanged(this)" required value="<%=v_amount%>" READONLY>
			</div>
		</div>
		
		<!-- <div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2 required">Choose a Bank</label>
			<div class="col-sm-4">
				<input id="bankAccountSearch" name="bank_name" class="form-control text-left " type="text"> 
			</div>
		</div>
		
		<div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2 required">Cheque No:</label>
			<div class="col-sm-4">
				<input name="checqueNo" class="form-control text-left " type="text"> 
			</div>
		</div>
		
		<div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2 required">Cheque Date:</label>
			<div class="col-sm-4">
				<input name="checquedate" class="form-control datepicker" type="text"> 
			</div>
		</div> -->
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Date of Receipt</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="payment_date" name="payment_date" value="<%=curDate%>">
			</div>
		</div>
		<div class="form-group">
			
			<label class="control-label col-form-label col-sm-2 required">Mode of Receipt</label>
			<div class="col-sm-4">
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
						<input id="payment_mode" name="payment_mode" class="form-control" onchange="checkpaymenttype(this.value)" type="text">
						
			</div>	
			
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Deposit To</label>
			<div class="col-sm-4">				
				<input type="text" id="paid_through_account_name" name="paid_through_account_name" class="form-control">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank Charges (if any)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="bankcharges" name="bankcharges">
			</div>
		</div>
		
		<div class="row hidepdc" hidden>
					<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table payment-table">
							<thead>
								<tr>
									<th>PDC</th>
									<th>Choose a Bank</th>
									<th>Cheque No</th>
									<th>Cheque Date</th>
									<th>Cheque Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="text-center">
										<input type="hidden" name="pdcstatus" value="0"> 
										<input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc(this)">
									</td>
									<td class="text-center">
										<input name="bank_branch" type="hidden"> 
										<input class="form-control text-left bankAccountSearch" name="bankname" type="text" placeholder="Select a bank"> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="<%=curDate%>" disabled>
									</td>
									<td class="text-center">
										<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="<%=zeroval%>">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="form-group hidepdc" hidden>
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow()">+ Add another cheque details</a>
						</div>
						<div class="total-section col-sm-6">
							<div class="total-row sub-total">
								<div class="total-label" style="text-align: right;">
									Total Cheque Amount:
								</div>
								<div style="padding-right: 8%;" class="total-amount" id="subTotal"><%=zeroval%></div>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group hidepdc" hidden>
					<div class="row">
						<div class="col-sm-6">
						</div>
						<div class="total-section col-sm-6">
							<div class="total-row sub-total">
								<div class="total-label" style="text-align: right;">
									Balance Cheque Amount:
								</div>
								<div style="padding-right: 8%;" class="total-amount" id="balamount"><%=zeroval%></div>
							</div>
						</div>
					</div>
				</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" maxlength="50" id="referenceno" name="referenceno">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Notes</label>
			<div class="col-sm-4">				
				<textarea rows="2" name="notes" id="notes" maxlength="1000" class="form-control"></textarea>
			</div>
		</div>
	
		
		<!-- Attach Files -->
		<div class="row grey-bg">
			<div class="col-sm-4">
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="invAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<div id="billAttchNote">
					<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
				</div>
			</div>
			<!-- <div class="col-sm-6 notes-sec">
				<p>Customer Notes</p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			
				<p>Terms & Conditions</p>
				<div> <textarea rows="2" name="terms" class="ember-text-area form-control ember-view" placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea> </div>
			</div> -->
		</div>
		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>Invoice#</td>
							<td>Order Number</td>
							<td>Invoice Date</td>
							<td>Invoice Amount</td>
							<td>Exchange Rate</td>
							<td>Invoice Amount (<%=curency%>)</td>
							<td>Invoice Balance</td>
							<td>Payment Amount</td>
							<td>Apply Credit</td>								
						</tr>
					</thead>
					<tbody id="invoicelisttablebody">
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="offset-lg-7 col-lg-5 clearfix">
					<div class="row">
						<p class="col-lg-8 text-right">Total :</p>
						<p class="col-lg-4 text-right" id="totalInvoiceAmount"></p>
					</div>
				</div>
			</div>
		</div>		
		<div class="row">
			<div class="col-xs-12">
				<div class="extraInfo offset-lg-7 col-lg-5 clearfix">
					<div class="row">
						<p class="col-lg-8 text-right">Amount Received :</p>
						<p class="col-lg-4 text-right" id="amountreceived"></p>
					</div>
					<div class="row">
						<p class="col-lg-8 text-right">Amount used for payments :</p>
						<p class="col-lg-4 text-right" id="amountufp"></p>
					</div>
					<div class="row">
						<p class="col-lg-8 text-right">Amount Refunded :</span>
						<p class="col-lg-4 text-right" id="amountrefunded"></p>
					</div>
					<div class="row">
						<p class="col-lg-8 text-right">Amount in excess:</p>
						<p class="col-lg-4 text-right" id="amountexcess"></p>
					</div>
				</div>
			</div>
		</div>
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<!-- <button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
				<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button> -->
				<button id="btnInvoiceSave" type="submit"  onclick="paysubmit()" class="btn btn-success">Record Payment</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
</form>
</div>

</div>


<div id="voucher" class="tabcontent">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../banking/invoicepaysummary"><span class="underline-on-hover">Receipt Summary</span> </a></li>                                               
                <li><label>New Receipt Voucher</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">New Receipt Voucher</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/invoicepaysummary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<div class="container-fluid">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="voucherPaymentForm" class="form-horizontal" name="v_form1" enctype="multipart/form-data">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Account Name</label>
			<div class="col-sm-4">
				<!-- <div class="input-group"> 
						<input id="account_name" name="account_name" placeholder="Select a Acount"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
				</div> -->
				<input type="text" id="account_name" name="account_name" class="form-control"  placeholder="Select a Account">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'account_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<input type="text" name="v_plant" id="plant" value="<%=plant%>" hidden>
				<!-- <input name="v_bank_branch" type="hidden"> -->
				<input type="text" name="v_username" value=<%=username%> hidden>
				<input type = "hidden" name="v_PROJECTID" value="0">
			<INPUT type = "hidden" name="v_CUST_CODE" value=<%=custCode%>>
			<INPUT type = "hidden" name="v_curency" value = "<%=curency%>">
			<INPUT type = "hidden" name="v_NOOFDECIMAL" value="<%=numberOfDecimal%>">
			<INPUT type = "hidden" name="v_EMPNO" value = "<%=empno%>">
			<INPUT type="hidden" name="v_cmd" value="<%=cmd%>" />
			<INPUT type="hidden" name="v_TranId" value="<%=sTranId%>" />
			<input name="v_uuid" type="hidden" value="<%=uniqueKey%>">
			<input name="v_paidpdcstatus" type="hidden" value="0">
			<input name="v_pdcamount" type="hidden" value="0">
			<input name="v_vbank" type="hidden" value="">   
			<input name="v_isbank" type="hidden" value="">
		<INPUT type="hidden" name="voucher_CURRENCYID"  value="<%=curency%>">
		<INPUT type="hidden" name="v_ValidNumber" value="<%=OrdValidNumber%>"> 
				<INPUT type="hidden" name="v_conv_amount_Curr" id="v_conv_amount_Curr" >
			<INPUT type="hidden" name="v_initialAmount" id="v_initialAmount" />  
			<INPUT type="hidden" name="v_type" value="<%=type%>" />           
			</div>
				
		</div>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="v_project" placeholder="Select a project" 
									name="v_project" value=""> 
								<span class="select-icon" style="right: 7px;"
								onclick="$(this).parent().find('input[name=\'v_project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
						</div>
					</div>
				</div>
		<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Currency</label>
						<div class="col-sm-4"> 
						<input type="text" class="ac-selected  form-control typeahead" id="v_CURRENCY" placeholder="Select a Currency" name="v_CURRENCY" value="<%=DISPLAY%>" required>
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'v_CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" id="v_exchangerate" ></label>
						<div class="col-sm-4"> 
							<input type="text" class="form-control" id="v_CURRENCYUSEQT" name="v_CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="v_currencychanged(this)" >	
						</div>
					</div>
					<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required" id="v_PaymentMade" ></label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="v_amount_Curr" name="v_amount_Curr" required  value=<%=v_amount_Curr%> onchange="v_calculatebasecurrency(this)">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Amount&nbsp;(<%=curency%>)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="v_amount" name="v_amount" onchange="return v_amountchanged(this)" required value=<%=v_amount%> READONLY>
			</div>
		</div>
		
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Date of Receipt</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="v_payment_date" name="v_payment_date" value="<%=curDate%>">
			</div>
		</div>
		<div class="form-group">
			
			<label class="control-label col-form-label col-sm-2 required">Mode of Receipt</label>
			<div class="col-sm-4">
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'v_payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
						<input id="v_payment_mode" name="v_payment_mode" class="form-control" type="text">
						
			</div>	
			
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Deposit To</label>
			<div class="col-sm-4">				
				<input type="text" id="v_paid_through_account_name" name="v_paid_through_account_name" class="form-control">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'v_paid_through_account_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank Charges (if any)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="v_bankcharges" name="v_bankcharges">
			</div>
		</div>
		
		<div class="row v_hidepdc" hidden>
					<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table voucher-table">
							<thead>
								<tr>
									<th>PDC</th>
									<th>Choose a Bank</th>
									<th>Cheque No</th>
									<th>Cheque Date</th>
									<th>Cheque Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="text-center">
										<input type="hidden" name="v_pdcstatus" value="0"> 
										<input type="checkbox" class="form-check-input" id="v_pdc" name="v_pdc" Onclick="v_checkpdc(this)">
									</td>
									<td class="text-center">
										<input name="v_bank_branch" type="hidden"> 
										<input class="form-control text-left v_bankAccountSearch" name="v_bankname" type="text" placeholder="Select a bank"> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="v_cheque-no" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" name="v_cheque-date" placeholder="Enter Cheque Date" value="<%=curDate%>" disabled>
									</td>
									<td class="text-center">
										<input class="form-control text-right" type="text" name="v_cheque-amount" onchange="v_calculateAmount(this)" value="<%=zeroval%>">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="form-group v_hidepdc" hidden>
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="v_addRow()">+ Add another cheque details</a>
						</div>
						<div class="total-section col-sm-6">
							<div class="total-row sub-total">
								<div class="total-label" style="text-align: right;">
									Total Cheque Amount:
								</div>
								<div style="padding-right: 8%;" class="total-amount" id="v_subTotal"><%=zeroval%></div>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group v_hidepdc" hidden>
					<div class="row">
						<div class="col-sm-6">
							
						</div>
						<div class="total-section col-sm-6">
							<div class="total-row sub-total">
								<div class="total-label" style="text-align: right;">
									Balance Cheque Amount:
								</div>
								<div style="padding-right: 8%;" class="total-amount" id="v_balamount"><%=zeroval%></div>
							</div>
						</div>
					</div>
				</div>
				
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" maxlength="50" id="v_referenceno" name="v_referenceno">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Notes</label>
			<div class="col-sm-4">				
				<textarea rows="2" name="v_notes" id="v_notes" maxlength="1000" class="form-control"></textarea>
			</div>
		</div>
	
		
		<!-- Attach Files -->
		<div class="row grey-bg">
			<div class="col-sm-4">
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="v_invAttch" name="v_file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<div id="v_billAttchNote">
					<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
				</div>
			</div>
			<!-- <div class="col-sm-6 notes-sec">
				<p>Customer Notes</p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			
				<p>Terms & Conditions</p>
				<div> <textarea rows="2" name="terms" class="ember-text-area form-control ember-view" placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea> </div>
			</div> -->
		</div>

		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<!-- <button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
				<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button> -->
				<button id="btnInvoiceSave" type="submit" onclick="vouchersubmit()" class="btn btn-success">Record Payment</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
</form>
</div>
</div>


	<!-- Modal -->
	<%-- <%@include file="newCustomerModal.jsp" %> --%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newEmployeeModal.jsp" %>
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
	<%@include file="newProductModal.jsp" %>
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="newPaymentTermsModal.jsp" %>
	<%@include file="newPaymentModeModal.jsp"%>
	<!-- Modal -->
	<!-- <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    Modal content
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h3 class="modal-title">Modal Header</h3>
	      </div>
	      <div class="modal-body">
	        <p>Some text in the modal.</p>
	      </div>
	      <div class="modal-footer">
	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div> -->

<!-- --------------modal---------------- -->
<div id="customerModal" class="modal fade"  name="form" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form" id="formCustomer" method="post">

			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Customer</h4>
				</div>
				<div class="modal-body">
					<input name="CUSTOMER_TYPE_DESC" type="hidden" value=""> 
					<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
					<input type="text" id="plant" name="PLANT" style="display:none;" value=<%=plant%>>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Id</label>
						<div class="col-sm-4">

							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE_C" id="CUST_CODE_C" onkeypress="return blockSpecialChar(event)"
									type="TEXT" value="" onchange="checkitem(this.value)" size="50"
									MAXLENGTH=50 width="50"> <span
									class="input-group-addon" onClick="onIDGen();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1_C" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
							<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Name</label>
						<div class="col-sm-4">
							<INPUT class="form-control" name="CUST_NAME_C" type="TEXT" value=""
								size="50" MAXLENGTH=100>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Type</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input name="CUSTOMER_TYPE_ID" type="TEXT" value="" size="50"
									MAXLENGTH=50 class="form-control"> <span
									class="input-group-addon"
									onClick="javascript:popUpWin('../jsp/customertypelistsave.jsp?CUSTOMER_TYPE_ID='+form.CUSTOMER_TYPE_ID.value);">
									<a href="#" data-toggle="tooltip" data-placement="top"
									title="Customer Type Details"> <i
										class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
								</span>
							</div>
						</div>
					</div>

					<INPUT type="text" id="TaxByLabel" name="taxbylabel"
						value="<%=taxbylabel%>" hidden>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Phone</label>
						<div class="col-sm-4">

							<INPUT name="TELNO" type="TEXT" value="" size="50"
								class="form-control" onkeypress="return isNumber(event)"
								MAXLENGTH="30">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Fax</label>
						<div class="col-sm-4">

							<INPUT name="FAX" type="TEXT" value="" size="50"
								onkeypress="return isNumber(event)" MAXLENGTH="30"
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Email</label>
						<div class="col-sm-4">
							<INPUT name="CUSTOMEREMAIL" type="TEXT" value="" size="50"
								MAXLENGTH=50 class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Website</label>
						<div class="col-sm-4">
							<INPUT name="WEBSITE" type="TEXT" value="" size="50" MAXLENGTH=50
								class="form-control">
						</div>
					</div>

					<div class="bs-example">
						<ul class="nav nav-tabs" id="myTab">
							<li class="nav-item active"><a href="#other"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile" class="nav-link"
								data-toggle="tab">Contact Details</a></li>
							<li class="nav-item"><a href="#home" class="nav-link"
								data-toggle="tab">Address</a></li>
							<li class="nav-item"><a href="#bank_cus" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>
							<li class="nav-item"><a href="#remark" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<div class="tab-pane fade" id="home">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
				ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Unit
										No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">City</label>
									<div class="col-sm-4">

										<INPUT name="ADDR4" type="TEXT" value="" size="50"
											MAXLENGTH=80 class="form-control">
									</div>
								</div>
								<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
									</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Postal
										Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" type="TEXT" value="" size="50" MAXLENGTH=10
											class="form-control">
									</div>
								</div>

							</div>

							<div class="tab-pane fade" id="profile">
								<br>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Contact
										Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Work
										Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Email</label>
									<div class="col-sm-4">

										<INPUT name="EMAIL" type="TEXT" value="" size="50"
											MAXLENGTH="50" class="form-control">
									</div>
								</div>


							</div>

							<div class="tab-pane active" id="other">
								<br>
								
								<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
				
		   ArrayList cvList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<cvList.size();i++)
      		 {
				Map m=(Map)cvList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div>
		
		<div class="form-group">

						<label class="control-label col-form-label col-sm-2" for="TRN No."
							id="TaxLabel"></label>
						<div class="col-sm-4">

							<INPUT name="RCBNO" type="TEXT" class="form-control" value=""
								size="50" MAXLENGTH="100">
						</div>
					</div>
								

								<div class="form-group" style="display: none;">
									<label class="control-label col-form-label col-sm-2">Opening
										Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="hidden" value=""
											onkeypress="return isNumberKey(event,this,4)" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Payment
										Type</label>
									<div class="col-sm-4">
										<div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly> <span
												class="input-group-addon"
												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
												<a href="#" data-toggle="tooltip" data-placement="top"
												title="Payment Type"> <i
													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
											</span>
										</div>
									</div>

									<div class="form-inline">
										<label class="control-label col-form-label col-sm-1">Days</label>
										<input name="PMENT_DAYS" type="TEXT" value="" size="5"
											MAXLENGTH=10 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Credit
										Limit</label>
									<div class="col-sm-4">
										<INPUT name="CREDITLIMIT" type="TEXT" value="" " size="50"
											MAXLENGTH=50 class="form-control"
											onkeypress="return isNumberKey(event,this,4)">
									</div>
									<!-- <div class="form-inline">
										<div class="col-sm-2">
											<label class="checkbox-inline"> <input
												type="checkbox" name="ISCREDITLIMIT" value="" />Apply
												Credit Limit
											</label>
										</div>
									</div> -->
								</div>
								
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"></label>
									<div class="col-sm-4">
										<input type = "radio" name="CREDIT_LIMIT_BY" value="DAILY"/>By Daily
      									<input type = "radio" name="CREDIT_LIMIT_BY" value="MONTHLY"/>By Monthly
      									<input type = "radio" name="CREDIT_LIMIT_BY" value="NOLIMIT" checked/>No Credit Limit
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Facebook
										Id</label>
									<div class="col-sm-4">
										<INPUT name="FACEBOOK" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Twitter
										Id</label>
									<div class="col-sm-4">
										<INPUT name="TWITTER" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">LinkedIn Id</label>
									<div class="col-sm-4">
										<INPUT name="LINKEDIN" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Skype">Skype Id</label>
									<div class="col-sm-4">
										<INPUT name="SKYPE" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

							</div>

														<div class="tab-pane fade" id="bank_cus">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="IBAN">IBAN</label>
      	<div class="col-sm-4">  
        <INPUT name="IBAN" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
       
       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" onchange="checkcustomerbank(this.value)" id="BANKNAME" name="BANKNAME" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				<%
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div>
		
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Branch">Branch</label>
      	<div class="col-sm-4">  
        <INPUT name="BRANCH" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
    	</div>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-4">  
        <INPUT name="BANKROUTINGCODE" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
        
        </div>
							
							<div class="tab-pane fade" id="remark">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Remarks">Remarks</label>
									<div class="col-sm-4">

										<INPUT name="REMARKS" type="TEXT" value="" size="50"
											MAXLENGTH=100 class="form-control ">
									</div>
								</div>

							</div>
						</div>
					</div>




					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">

							&nbsp;&nbsp;
							<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="onNew();">Clear</button> -->
							&nbsp;&nbsp;
							<button type="button" class="btn btn-success"
								data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<div id="applycredit" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditForm" method="post">
		<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
		<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>"> <%--Resvi--%>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title"><b>Apply credits for <span id="invoiceno"></span></b></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Invoice Balance: </span><b><%=curency%><span id="invoicebal"></span></b>
		      		</div>
		      	</div>
		      	<h4>General Credit</h4>
		      	<table class="table advanceTable" id="statuscheck">
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
					
					</tbody>
				</table>
				<input type="hidden" name="ordertotal" value="">
				<input type="hidden" name="totalbalpopup" value="">
				<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Amount to Credit: </span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="creditTotalAmount"></span>
		      		</div>
		      	</div>
		      	<br>
		      	<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Invoice Balance Due:</span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="invoiceBalanceAmountdue"></span>
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

<script type="text/javascript">
$(document).ready(function(){
	
    var  curr = document.getElementById("CURRENCY").value;
    
    var basecurrency = '<%=curency%>';  <%--    resvi --%>
    
    document.getElementById('PaymentMade').innerHTML = "Amount Received ("+curr+")";
    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi

    
    document.getElementById('v_PaymentMade').innerHTML = "Amount Received ("+curr+")";
    document.getElementById('v_exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
	
    var d = "<%=taxbylabel%>";
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    var cust = "<%=sCustCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	 $('#customerModal').modal('show');
    }
    
    $(document).on("focusout",".creditAmount", function(){
    	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
    	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
		  var value = $(this).val();
		  $(this).val(parseFloat(value).toFixed(numberOfDecimal));
		  var balance =  $(this).data("balance");
		  var ordertotal = $("input[name ='ordertotal']").val();
		  var ordertotalbal = $("input[name ='totalbalpopup']").val();
		  var originalBalance = ordertotalbal;
		  if(isNaN(value)){ /* To check if valid input i.e., number */
			  $(this).val(zeroshow);
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
			  $(this).val(zeroshow);
			  alert("Total Amount is exceeding Invoice balance.");
		  }else{
			  
			  var balamountorder = ordertotal;
			  if(parseFloat(balamountorder) > 0){  
				
				  var tamt = "0";
				  $(".creditListTable tbody tr").each(function() {
						var amt = $('td:eq(8)', this).find('input').val();
						tamt = tamt + amt;		
					});
				
				  if(parseFloat(balamountorder) != parseFloat(tamt)){
					  $(".advanceTable tbody tr").each(function() {
							$('td:eq(8)', this).find('input').val(parseFloat("0").toFixed(numberOfDecimal));
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
			  $('#invoiceBalanceAmountdue').text(balance);
		  }				  	  
	  });
    
});
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : aCustCode,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
					ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {

						alert("Customer ID Already Exists");
						document.getElementById("CUST_CODE").focus();
						//document.getElementById("ITEM").value="";
						return false;
					} else
						return true;
				}
			});
			return true;
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
	
	function isNumber(evt) {	
	  evt = (evt) ? evt : window.event;
	  var charCode = (evt.which) ? evt.which : evt.keyCode;
	  
	  if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
	  	if( (charCode!=43 && charCode!=32 && charCode!=45))
	  		{
	  		
	      alert("  Please enter only Numbers.  ");
	      return false;
	  		}
	  	}
	  return true;
	}
	function onAdd(){
		var CUST_CODE   = document.form.CUST_CODE_C.value;
		 var CUST_NAME   = document.form.CUST_NAME_C.value;
		 var CL   = document.form.CREDITLIMIT.value;
		 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
		   var RCBNO   = document.form.RCBNO.value;
		   var rcbn = RCBNO.length;
		   
		   var ValidNumber   = document.form.ValidNumber.value;
		   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
		   
		 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE.focus(); return false; }
		 if(CUST_NAME == "" || CUST_NAME == null) {
			 alert("Please Enter Customer Name"); 
			 document.form.CUST_NAME.focus();
			 return false; 
			 }
		 if(document.form.TAXTREATMENT.selectedIndex==0)
			{
			alert("Please Select TAXTREATMENT");
			document.form.TAXTREATMENT.focus();
			return false;
			}
		 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
		   {
		   var  d = document.getElementById("TaxByLabel").value;
		   	if(RCBNO == "" || RCBNO == null) {
		   		
			   alert("Please Enter "+d+" No."); 
			   document.form.RCBNO.focus();
			   return false; 
			   }
		   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
			//{
			if(!IsNumeric(RCBNO))
			{
		    alert(" Please Enter "+d+" No. Input In Number"); 
		   	document.form.RCBNO.focus();
		   	return false; 
		  	}

			if("15"!=rcbn)
			{
			alert(" Please Enter your 15 digit numeric "+d+" No."); 
				document.form.RCBNO.focus();
				return false; 
				}
			//}
		   }
		else if(50<rcbn)
		{
		   var  d = document.getElementById("TaxByLabel").value;
		   alert(" "+d+" No. length has exceeded the maximum value"); 
		   document.form.RCBNO.focus();
		   return false; 
		 }

		 if(CL < 0) 
		 {
			   alert("Credit limit cannot be less than zero");
			   document.form.CREDITLIMIT.focus(); 
			   return false; 
			   }	 
		// alert(isCL);
		
		 //alert("2nd");
		 if(!IsNumeric(CL))
		 {
		   alert(" Please Enter Credit Limit Input In Number");
		   form.CREDITLIMIT.focus();  
		   form.CREDITLIMIT.select(); 
		   return false;
		 }
		 if(!IsNumeric(form.PMENT_DAYS.value))
		 {
		   alert(" Please Enter Days Input In Number");
		   form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
		 }
		 
		 
		//  alert(CL);
		 /* if(this.form.ISCREDITLIMIT.checked == true && CL == ""){
			   alert("Please Enter Credit Limit");
			   document.form.CREDITLIMIT.focus();
			   return false; 
		 } */
		 if(document.form.COUNTRY_CODE_C.selectedIndex==0)
			{
			   alert("Please Select Country from Address");
			   document.form.COUNTRY_CODE_C.focus();
			 return false;
			}
		 /* if(isCL.equals("1") && CL.equals(""))
			  {
				  alert("Please Enter Credit Limit"); 
				   document.form.CREDITLIMIT.focus();
				   return false; 
			  }	 */
		 
		 /* document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=invoicePaymentNew";
		 document.form.submit(); */
		 var datasend = $('#formCustomer').serialize();
		   
		  
		   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=invoicePaymentNew";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : datasend,
			dataType : "json",
			success : function(data) {
				
				$("input[name ='CUST_CODE']").val(data.customer[0].CID);				
				document.getElementById("CUSTOMER").value  = data.customer[0].CName;
				$("input[name ='CUSTOMER']").val(data.customer[0].CName);
				//$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
				//document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				$('#customerModal').modal('hide');
			}
			});
 
	}
	
	function onIDGen()
	{
	 /* document.form.action  = "/track/CreateCustomerServlet?action=Auto-ID&reurl=invoicePaymentNew";
	 document.form.submit(); */
		var urlStr = "/track/CreateCustomerServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=plant%>",
			action : "JAuto-ID",
			reurl : "invoicePaymentNew"
		},
		dataType : "json",
		success : function(data) {
			
			$("input[name ='CUST_CODE_C']").val(data.customer[0].CID);
			$("input[name ='CUST_CODE1_C']").val(data.customer[0].CID);
			
		}
		});
	}
	
	function aCredit(id,dono,cno,invno,balamount,CURRENCYID,CURRENCYUSEQT){
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var conv_balanceAmount= parseFloat(parseFloat(balamount)*parseFloat(CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
		$('#invoiceno').text(invno);
		$('#invoicebal').text(' '+parseFloat(balamount).toFixed(numberOfDecimal) +' / '+CURRENCYID +' '+ conv_balanceAmount);
		$('#invoiceBalanceAmountdue').text(parseFloat(balamount).toFixed(numberOfDecimal));
		$('#creditTotalAmount').text(zeroshow);	
		$(".advanceTable tbody").html("");
    	$(".creditListTable tbody").html("");
		var urlStr = "/track/InvoicePayment?CMD=showcreditforapply";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				CUSTNO:cno,
				DONO:"0",
			},
	        success: function (data1) {
	    
	        	var obj = JSON.parse(data1);
	        	console.log(obj);
	        	var body="";
	        	var orderbody="";
	        	$.each(obj.CREDIT,function(i,v){
	        			var crdid =v.CREDITNOTEHDRID;
	        			var ref = "";
	  					if(crdid == "0"){
	  						ref = "Advance Payment";
	  					}else{
	  						ref = crdid;
	  					}
							

							var cAmount = v.AMOUNT;
							cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
							
							var cbalAmount = v.BALANCE;
							cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
							
							body +='<tr>';
							body +='<td hidden>'+dono+'></td>';
							body +='<td hidden>'+v.RECEIVEHDRID+'</td>';
							body +='<td hidden>'+id+'</td>';
							body +='<td hidden>'+v.ID+'</td>';
							body +='<td>'+ref+'</td>';
							body +='<td>'+v.RECEIVE_DATE+'</td>';
							body +='<td class="text-right">'+cAmount+'</td>';
							body +='<td class="text-right">'+cbalAmount+'</td>';
							body +='<td><div class="float-right">';
							body +='<input class="form-control text-right creditAmount creditAmountvaluec creditAmountentry" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'" onkeypress="return isNumberKey(event,this,4)" disabled>'
							body +='</div></td>';
							body +='</tr>';
						
						
	        	});
	        	if(dono == '' || dono == null){
	        		$("input[name ='ordertotal']").val(zeroshow);
		        	$("input[name ='totalbalpopup']").val(balamount);
	        		$(".advanceTable tbody").html(body);
	        		$( ".creditAmountvaluec" ).prop( "disabled", false );
	    		}else{
	    			var urlStr = "/track/InvoicePayment?CMD=showcreditforapply";
	    			$.ajax( {
	    				type : "GET",
	    				url : urlStr,
	    				data: {
	    					CUSTNO:cno,
	    					DONO:dono,
	    				},
	    		        success: function (data) {
	    		    
	    		        	var objc = JSON.parse(data);
	    		        	console.log(objc);
	    		        	var ordertottalamo = "0";
	    		        	$.each(objc.CREDIT,function(i,v){
	    		        			
	    								var cAmount = v.AMOUNT;
	    								cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
	    								
	    								var cbalAmount = v.BALANCE;	    								
	    								cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
	    								ordertottalamo = parseFloat(cbalAmount) + parseFloat(ordertottalamo);
	    								
	    								orderbody +='<tr>';
	    								orderbody +='<td hidden>'+dono+'></td>';
	    								orderbody +='<td hidden>'+v.RECEIVEHDRID+'</td>';
	    								orderbody +='<td hidden>'+id+'</td>';
	    								orderbody +='<td hidden>'+v.ID+'</td>';
	    								orderbody +='<td>'+dono+'</td>';
	    								orderbody +='<td>'+v.RECEIVE_DATE+'</td>';
	    								orderbody +='<td class="text-right">'+cAmount+'</td>';
	    								orderbody +='<td class="text-right">'+cbalAmount+'</td>';
	    								orderbody +='<td><div class="float-right">';
	    								orderbody +='<input class="form-control text-right creditAmount creditAmountvalue creditAmountentry" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'">'
	    								orderbody +='</div></td>';
	    								orderbody +='</tr>';
	    							
	    							
	    		        	});
	    		        	
	    		        	
	    		        	$("input[name ='ordertotal']").val(ordertottalamo);
	    		        	$("input[name ='totalbalpopup']").val(balamount);
	    		        	
	    		        	$(".advanceTable tbody").html(body);
	    		        	$(".creditListTable tbody").html(orderbody);
	    		        	
	    		        	if(ordertottalamo > 0){
	    		        		  
	    		        	}else{
	    		        		
	    		        		$( ".creditAmountvaluec" ).prop( "disabled", false );
	    		        	}
	    		        		
	    		    		
	    		        }
	    				
	    			});
	    		}
	        	
	        	
	        }
			
		});

	}
function applyCredit(){


	//Resvi start
	  var ValidNumber   = document.creditForm.ValidNumber.value;
	  if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	// end
			
	var CURRENCYUSEQTs = $("#B_CURRENCYUSEQT").val();
		var prid = "0";
		var pinid = "0";
		var pordno = "0";
		var pcamot = "0";
		var pid = "0";
		var uid ="0";
		
		
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
						uid : uid,
						CURRENCYUSEQT : CURRENCYUSEQTs
					},
					dataType : "json"
					
				});
			}
		  
		});
		
		$(".advanceTable tbody tr").each(function() {
			
			uid = document.form1.uuid.value;
			
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
						uid : uid,
						CURRENCYUSEQT : CURRENCYUSEQTs
					},
					dataType : "json"
					
				});
			}
		  
		});
		
		$("#invoicelisttablebody").html("");
		customerchanged();
		$('#applycredit').modal('toggle');
		sortTable();
	}
	
function loadCOAData(){
	var accName=$("#create_account_modal #acc_name").val();
	$("#paid_through_account_name").val(accName);
}

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
	
function OnTaxChange(TAXTREATMENT)
{
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	}
function OnBank(BankName)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : "<%=plant%>",
			NAME : BankName,
		},
		success : function(dataitem) {
			var BankList=dataitem.BANKMST;
			var myJSON = JSON.stringify(BankList);						
			var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
			if (dt) {
			  var result = jQuery.parseJSON(dt);			  
			  var val = result.BRANCH;			  
			  $("input[name ='BRANCH']").val(val);
			}
		}
	});		
}
function OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#STATE').empty();
			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}
$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
    var text = $("#COUNTRY_CODE_C option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});
</script>


</div>
</div>
<%-- <%@include file="newPaymentTypeModal.jsp"%> --%>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>