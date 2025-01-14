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
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String NOOFPAYMENT = StrUtils.fString((String) session.getAttribute("NOOFPAYMENT"));
String OrdValidNumber="";

//Validate no.of Supplier -- Azees 18.11.2020
	CustomerBeanDAO venddao = new CustomerBeanDAO();
	String NOOFSUPPLIER=((String) session.getAttribute("NOOFSUPPLIER"));
	String SupValidNumber="";
	int nosupvalid =venddao.Vendorcount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(nosupvalid>=convl)
		{
			SupValidNumber=NOOFSUPPLIER;
		}
	}
	
	//resvi starts
		BillPaymentDAO billDao = new BillPaymentDAO();
		DateUtils _dateUtils = new DateUtils();
		String FROM_DATE = _dateUtils.getDate();
		if (FROM_DATE.length() > 5)
			FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
		
		String TO_DATE = _dateUtils.getLastDayOfMonth();
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
		
   //	 	ends

String pono = StrUtils.fString(request.getParameter("pono"));
String type = StrUtils.fString(request.getParameter("type"));
String billHdrId = StrUtils.fString(request.getParameter("bill"));
String vendno = StrUtils.fString(request.getParameter("VENDNO"));
String vendName = StrUtils.fString(request.getParameter("VEND_NAME"));

PoHdrDAO poHdrDao = new PoHdrDAO();
PoDetDAO poDetDao = new PoDetDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();
CurrencyDAO currencyDAO = new CurrencyDAO();

String title = "New Bill Payment";
		
String totalCost = poDetDao.getTotalReceiptCostByOrder(pono, plant);
double dTotalCost ="".equals(totalCost) ? 0.0d :  Double.parseDouble(totalCost);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String advancePayment = "", paymentMade = StrUtils.addZeroes(0,numberOfDecimal), exchangerate="", receivedAmount = "";
//String vendno = poHdrDao.getSuppliercode(plant, pono);
String curDate = _dateUtils.getDate();

if(type.equalsIgnoreCase("ADVANCE")){
	advancePayment = billDao.getAdvanceAmountByOrder(pono, plant);	
	double dAdvancePayment ="".equals(advancePayment) ? 0.0d :  Double.parseDouble(advancePayment);
	paymentMade = StrUtils.addZeroes((dTotalCost - dAdvancePayment),numberOfDecimal);
	
}else{
	if(!billHdrId.equalsIgnoreCase("")){
		receivedAmount = billDao.getreceivedAmountByBill(pono, billHdrId, plant);
		double dReceivedAmount ="".equals(receivedAmount) ? 0.0d :  Double.parseDouble(receivedAmount);
		paymentMade = StrUtils.addZeroes((dTotalCost - dReceivedAmount),numberOfDecimal);
	}
}

fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";

String taxbylabel = ub.getTaxByLable(plant);
String sVendorCode = StrUtils.fString(request.getParameter("sCustCode"));

UUID uniqueKey = UUID.randomUUID();

String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);

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
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/BillPayment.js"></script>
<style>
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

tbody {
    display: block;
    /*height: 200px;*/
    overflow: auto;
}
thead,tfoot, tbody tr {
    display: table;
    width: 100%;
    table-layout: fixed;/* even columns width , fix width of table too*/
}
thead {
    width: calc( 100% - 1em )/* scrollbar is average 1em/16px width, remove it from thead width */
}

tfoot{
    width: calc( 100% - 1em );/* scrollbar is average 1em/16px width, remove it from thead width */
    font-weight: bold;
}

.tbodyfixheight>tbody {
	height: 200px;
}

.tbodyheight>tbody {
	height: 100%;
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
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<input type="number" id="inpayment" name="inpayment" style="display: none;" value="0">
	<div class="tab">
  		<button class="tablinks active" onclick="openPayment(event, 'payment')">Bill Payment</button>
 		 <button class="tablinks" onclick="openPayment(event, 'voucher')">Payment Voucher</button>
 	</div>
 	<div id="payment" class="tabcontent active" style="display: block;">
	<div> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                 <li><a href="../banking/billpaysummary"><span class="underline-on-hover">Bill and Voucher Payment Summary</span> </a></li>                    
                <li><label>New Bill Payment</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/billpaysummary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="billPaymentForm" class="form-vertical" name="form"  enctype="multipart/form-data" >
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="username" value=<%=username%>>
				<input name="vendno" type="hidden" value="<%=vendno%>">
				<input name="allowedAmount" type="hidden" value="<%=paymentMade%>">
				<input name="totalBillAmount" type="hidden" value="0">
				<input name="paytype" type="hidden" value="ADVANCE">
				<input name="uuid" type="hidden" value="<%=uniqueKey%>">
				<input name="newadv" type="hidden" value="NEW">
				<input name="ponoadv" type="hidden" value="">
				<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>	
				<input name="paidpdcstatus" type="hidden" value="0">
				<input name="pdcamount" type="hidden" value="0">
				<input name="vbank" type="hidden" value="">
				<input name="isbank" type="hidden" value="">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				<INPUT type="hidden" name="conv_amount_paid_Curr" id="conv_amount_paid_Curr"  value="<%=paymentMade%>">
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">    
				<input type="hidden" id="B_CURRENCYUSEQT" value=<%=CURRENCYUSEQT%>>
				<input type="hidden" name="PROJECTID" value="">
				<input type="hidden" name="excessamtcheck" value="">
				<!-- <div class="row">
					<div class="col-lg-4 form-group">
						<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc()">PDC</lable>
					</div>
				</div> -->
				
				<div class="row">
					<div class="col-lg-4 form-group vendor-section">
						<label class="required">Supplier Name</label>
						<div class="input-group"> 
							<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="Select a vendor" name="vendname" onchange="checksupplier(this.value)" value="<%=vendName%>" required>
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 							<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.vendno.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
						</div>
					</div>
					<div class="col-lg-4 form-group">
						<label>Project</label>
							<input id="project" placeholder="Select a project" name="project" class="form-control text-left" onchange="checkproject(this.value)" value="" type="text"> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'project\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Date of Payment&nbsp;</label>
						<input id="" class="form-control datepicker" name="payment_date" type="text" value="<%=curDate%>" READONLY required>
					</div>	
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Currency</label> 
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>" required>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>
					<div class="col-lg-4 form-group vendor-section">
						<label class="required" id="exchangerate" ></label>
						<div class="input-group"> 
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" onkeypress="return isNumberKey(event,this,4)" required>	
						</div>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required" id="PaymentMade" ></label>
						<input id="" class="form-control" name="amount_paid_Curr" id="amount_paid_Curr" type="text"  value="<%=paymentMade%>" onchange="calculatebasecurrency(this)" onkeypress="return isNumberKey(event,this,4)" required>
					</div>
												
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Amount&nbsp;(<%=curency%>)</label>
						<input id="amount_paid" name="amount_paid" class="form-control text-left" readonly onchange="amountchanged(this)" type="text" value="<%=paymentMade%>" required> 
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Mode of Payment</label>
						<input id="payment_mode" name="payment_mode" class="form-control" onchange="checkpaymenttype(this.value)" type="text" required>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					<div class="col-lg-4 form-group">
						 <input type="checkbox" id="paycaltype" name="paycaltype">
  						 <label for="paycaltype">Amount Calculated By Bill AMount</label><br>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Paid Through</label>
						<input id="paid_through_account_name" name="paid_through_account_name"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					<div class="col-lg-4 form-group">
						<label>Bank Charges (if any)</label>
						<input id="bankCharge" name="bank_charge" class="form-control text-left" value="<%=zeroval%>" type="text" onkeypress="return isNumberKey(event,this,4)"> 
					</div>
					<div class="col-lg-4 form-group">
					<label>Supplier Write-Off Amount</label>
						<input id="payablewo" name="payablewo" onchange="checkpayablewo(this.value)" class="form-control text-left" value="<%=zeroval%>" type="text" onkeypress="return isNumberKey(event,this,4)">
					
					</div>
		
				</div>
				
				<!-- <div class="row">
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Choose a Bank</label>
						<input id="bankAccountSearch" name="bank_name" class="form-control text-left " type="text"> 
					</div>
					
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Cheque No:</label>
						<input name="checqueNo" class="form-control text-left " type="text"> 
					</div>
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Cheque Date:</label>
						<input type="text" class="form-control datepicker" id="cheque_date" name="cheque_date" >
					</div>
				</div> -->
				
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
										<input class="form-control text-left bankAccountSearch" name="bankname" type="text" placeholder="Select a bank" readonly> 
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
					<label>Reference#</label> 
					<input maxlength="250" id="" name="refrence" class="form-control" type="text"> 
				</div>
				
				<div class="form-group">
					<label>Notes</label> 
					<textarea rows="2" maxlength="950" name="note"  id="" class="form-control"></textarea> 
				</div>
				
				<div class="row form-group">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch" id="billPaymentAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload File</button>
								</div>								
							</div>
						</div>
						<div id="billPaymentAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
					</div>
				</div>
				<div class="row form-group">  
					<div class="col-xs-12">
							<table id="table2" class="table">
								<thead>
									<tr>
										<td>Bill#</td>
										<td>Supplier Invoice No.</td>
										<td>Order Number</td>
										<td>Bill Date</td>
										<td>Bill Amount</td>
										<td>Exchange Rate</td>
										<td>Bill Amount(<%=curency%>)
										</td>
										<td>Bill Due</td>
										<td>Payment Amount</td>
										<td>Apply Credit</td>
									</tr>
								</thead>
								<tbody id="billlisttablebody">

								</tbody>
								<tfoot>
									<tr>
										<td></td>
										<td></td>
										<td class='text-center'>Total</td>
										<td></td>
										<td class="text-center tbillamt"></td>
										<td></td>
										<td class="text-center tcbillamt"></td>
										<td class="text-center tbilldue"></td>
										<td class="tpayamt"></td>
										<td></td>
									</tr>
								</tfoot>
							</table>
						</div>
				</div>
				
				<div class="row form-group">  
					<div class="col-xs-12">
						<table id="table3" class="table">
							<thead>
								<tr>
									<td>Expense ID</td>
									<td>Order Number</td>
									<td>Shipment Code</td>
									<td>Expense Date</td>
									<td>Expense Amount</td>
									<td>Exchange Rate</td>
									<td>Expense Amount(<%=curency%>)</td>
									<td>Expense Due</td>
									<td>Payment Amount</td>	
									<td>Apply Credit</td>							
								</tr>
							</thead>
							<tbody id="explisttablebody">
								
							</tbody>
							<tfoot>
									<tr>
										<td></td>
										<td></td>
										<td class='text-center'>Total</td>
										<td></td>
										<td class="text-center etbillamt"></td>
										<td></td>
										<td class="text-center etcbillamt"></td>
										<td class="text-center etbilldue"></td>
										<td class="etpayamt"></td>
										<td></td>
									</tr>
								</tfoot>
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
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="submit" class="btn btn-success" onClick="onPay()">Save</button>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	</div>
	
	
	
	
	
	<div id="voucher" class="tabcontent">
		<div> 
		<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                 <li><a href="../banking/billpaysummary"><span class="underline-on-hover">Bill and Voucher Payment Summary</span> </a></li>                    
                <li><label>New Payment Voucher</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">New Payment Voucher</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/billpaysummary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="BillvoucherPaymentForm" class="form-vertical" name="vform" enctype="multipart/form-data" >
				<input type="hidden" name="voucher_plant" value="<%=plant%>">
				<input type="hidden" name="voucher_username" value=<%=username%>>
				<!-- <input name="voucher_bank_branch" type="hidden">  -->
				<input type="hidden" id="voucher_numberOfDecimal" value=<%=numberOfDecimal%>>
				<input name="v_paidpdcstatus" type="hidden" value="0">
				<input name="v_pdcamount" type="hidden" value="0">
				<input name="v_vbank" type="hidden" value="">
				<input name="v_isbank" type="hidden" value="">
				<INPUT type="hidden" name="voucher_CURRENCYID"  value="<%=curency%>">
				<INPUT type="hidden" name="V_ValidNumber" value="<%=OrdValidNumber%>"> 
				<INPUT type="hidden" name="v_conv_amount_paid_Curr" id="v_conv_amount_paid_Curr"  value="<%=paymentMade%>">
				<input type="hidden" name="VPROJECTID" value="">
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Account Name</label>
						<input id="account_name" name="account_name" placeholder="Select a Acount"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					<div class="col-lg-4 form-group">
						<label>Project</label>
							<input id="vproject" placeholder="Select a project" name="vproject" class="form-control text-left" type="text"> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'vproject\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Date of Payment&nbsp;</label>
						<input id="voucher_payment_date" class="form-control datepicker" name="voucher_payment_date" type="text" value="<%=curDate%>" READONLY required>
					</div>	
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Currency</label> 
						<input type="text" class="ac-selected  form-control typeahead" id="voucher_CURRENCY" placeholder="Select a Currency" name="voucher_CURRENCY" value="<%=DISPLAY%>" required>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'voucher_CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>
				
					<div class="col-lg-4 form-group vendor-section">
						<label class="required" id="voucher_exchangerate"></label>
						<div class="input-group"> 
							<input type="text" class="form-control" id="voucher_CURRENCYUSEQT" name="voucher_CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="voucher_currencychanged(this)" >	
						</div>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required" id="voucher_PaymentMade"></label>
						<input id="" class="form-control" name="voucher_amount_paid_Curr" id="voucher_amount_paid_Curr" type="text" value="<%=paymentMade%>" onchange="v_calculatebasecurrency(this)" required>
					</div>	
				</div>
				
				<!-- <div class="row">
					<div class="col-lg-4 form-group voucher_bankAccountSection" hidden>
						<label class="required">Choose a Bank</label>
						<input id="voucher_bankAccountSearch" name="voucher_bank_name" class="form-control text-left " type="text"> 
					</div>
					
					<div class="col-lg-4 form-group voucher_bankAccountSection" hidden>
						<label class="required">Cheque No:</label>
						<input name="voucher_checqueNo" class="form-control text-left " type="text"> 
					</div>
					
					<div class="col-lg-4 form-group voucher_bankAccountSection" hidden>
						<label class="required">Cheque Date:</label>
						<input type="text" class="form-control datepicker" id="voucher_cheque_date" name="voucher_cheque_date" >
					</div>
				
				</div> -->
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Amount&nbsp;(<%=curency%>)</label> 
						<input id="voucher_amount_paid" name="voucher_amount_paid" class="form-control text-left" readonly onchange="voucher_amountchanged(this)" type="text" value="<%=paymentMade%>" required> 
					</div>
				
					<div class="col-lg-4 form-group">
						<label class="required">Mode of Payment</label>
						<input id="voucher_payment_mode" name="voucher_payment_mode" class="form-control" type="text" required>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'voucher_payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					
					<div class="col-lg-4 form-group">
						<label class="required">Paid Through</label>
						<input id="voucher_paid_through_account_name" name="voucher_paid_through_account_name"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'voucher_paid_through_account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label>Bank Charges (if any)</label>
						<input id="voucher_bankCharge" name="voucher_bank_charge" class="form-control text-left" type="text" value="<%=zeroval%>"> 
					</div>	
					<div class="col-lg-4 form-group">
					
					</div>
					
					<div class="col-lg-4 form-group">
					
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
										<input class="form-control text-left v_bankAccountSearch" name="v_bankname" type="text" placeholder="Select a bank" readonly> 
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
					<label>Reference#</label> 
					<input maxlength="250" id="" name="voucher_refrence" class="form-control" type="text"> 
				</div>
				
				<div class="form-group">
					<label>Notes</label> 
					<textarea rows="2" maxlength="950" name="voucher_note"  id="" class="form-control"></textarea> 
				</div>
				
				<div class="row form-group">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch" id="voucher_billPaymentAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload File</button>
								</div>								
							</div>
						</div>
						<div id="voucher_billPaymentAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="submit" class="btn btn-success" onClick="onV_Pay()">Save</button>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	</div>
	
	</div>
	<div>
	
	</div>
</div>
<!-- Modal -->
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newPaymentModeModal.jsp"%>
	
<!-- Modal -->
<!-- ----------------Modal-------------------------- -->

<div id="supplierModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form1" id="formsupplierid" method="post">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Supplier</h4>
				</div>
				<div class="modal-body">
					<input name="SUPPLIER_TYPE_DESC" type="hidden" value="">
					<input type="number" id="numberOfDecimal" style="display: none;"
						value=<%=numberOfDecimal%>>
						<input type="text" name="PLANT"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="LOGIN_USER" style="display: none;"
						value=<%=username%>>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Create Supplier ID">Supplier Id</label>
						<div class="col-sm-4">
							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE"
									type="TEXT" value=""
									onchange="checkitem(this.value)" size="50" MAXLENGTH=50
									width="50"> <span
									class="input-group-addon" onClick="onIDGen();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
  							<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
				    		<INPUT type="hidden" name="ValidNumber" value="<%=SupValidNumber%>">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Supplier Name">Supplier Name</label>
						<div class="col-sm-4">

							<INPUT class="form-control" name="CUST_NAME" type="TEXT"
								value="" size="50" MAXLENGTH=100>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="supplier type">Supplier Type</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input name="SUPPLIER_TYPE_ID" type="TEXT"
									value="" size="50" MAXLENGTH=50
									class="form-control"> <span class="input-group-addon"
									onClick="javascript:popUpWin('suppliertypelistsave.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value+'&TYPE=POPUP1');">
									<a href="#" data-toggle="tooltip" data-placement="top"
									title="Supplier Type Details"> <i
										class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
								</span>
							</div>
						</div>
					</div>

					<INPUT type="hidden" id="TaxByLabel" name="taxbylabel"
						value=<%=taxbylabel%>>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Telephone No">Supplier Phone</label>
						<div class="col-sm-4">

							<INPUT name="TELNO" type="TEXT" value="" size="50"
								class="form-control" onkeypress="return isNumber(event)"
								MAXLENGTH="30">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Fax">Supplier
							Fax</label>
						<div class="col-sm-4">

							<INPUT name="FAX" type="TEXT" value="" size="50"
								onkeypress="return isNumber(event)" MAXLENGTH="30"
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Customer Email">Supplier Email</label>
						<div class="col-sm-4">
							<INPUT name="CUSTOMEREMAIL" type="TEXT"
								value="" size="50" MAXLENGTH=50
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
						<div class="col-sm-4">
							<INPUT name="WEBSITE" type="TEXT" value="" size="50"
								MAXLENGTH=50 class="form-control">
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

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
				MasterUtil _MasterUtil=new  MasterUtil();
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
									<label class="control-label col-form-label col-sm-2"
										for="Unit No">Unit No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Building">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Street">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2" for="City">City</label>
									<div class="col-sm-4">

										<INPUT name="ADDR4" type="TEXT" value="" size="50"
											MAXLENGTH=80 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">State</label>
									<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Postal Code">Postal Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" type="TEXT" value="" size="50"
											MAXLENGTH=10 class="form-control">
									</div>
								</div>

							</div>

							<div class="tab-pane fade" id="profile">
								<br>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Contact Name">Contact Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Designation">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Work phone">Work Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Hand Phone">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Email">Email</label>
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
					   _MasterUtil=new  MasterUtil();
					   ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
						for(int i=0 ; i<ccList.size();i++)
			      		 {
							Map m=(Map)ccList.get(i);
							String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
					        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
					        <%
			       			}
						 %></SELECT>
						</div>
					</div>								
							<div class="form-group">
							<label class="control-label col-form-label col-sm-2" for="TRN No"
								id="TaxLabel"></label>
							<div class="col-sm-4">
	
								<INPUT name="RCBNO" type="TEXT" class="form-control"
									value="" size="50" MAXLENGTH="30">
							</div>
							</div>
								

								<div class="form-group" style="display: none;">
									<label class="control-label col-form-label col-sm-2"
										for="Opening Balance">Opening Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="hidden"
											value=""
											onkeypress="return isNumberKey(event,this,4)" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Payment Terms">Payment Type</label>
									<div class="col-sm-4">
										<div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly>
											<span class="input-group-addon"
												onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form1.PAYTERMS.value+'&PAYTYPE=POPUP1');">
												<a href="#" data-toggle="tooltip" data-placement="top"
												title="Payment Type"> <i
													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
											</span>
										</div>
									</div>

									<div class="form-inline">
										<label class="control-label col-form-label col-sm-1"
											for="Payment Terms">Days</label> <input name="PMENT_DAYS"
											type="TEXT" value="" size="5" MAXLENGTH=10
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Facebook">Facebook Id</label>
									<div class="col-sm-4">
										<INPUT name="FACEBOOK" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Twitter">Twitter Id</label>
									<div class="col-sm-4">
										<INPUT name="TWITTER" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">LinkedIn Id</label>
									<div class="col-sm-4">
										<INPUT name="LINKEDIN" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">Skype Id</label>
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
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				<%
		    _MasterUtil=new  MasterUtil();
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

										<INPUT name="REMARKS" type="TEXT" value=""
											size="50" MAXLENGTH=100 class="form-control ">
									</div>
								</div>

							</div>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="window.location.href='../home'">Back</button>
							&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default"
								onClick="onNew();" >Clear</button>
							&nbsp;&nbsp; -->
							<button type="button" class="btn btn-success" onClick="onAdd();" >
								Save
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>




				</div>
				<!-- <div class="modal-footer">
	      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onAdd();">Save</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div> -->
			</div>
		</form>
	</div>
</div>
<div id="creditListModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditForm" method="post">
		<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
		<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>"> <%--Resvi--%>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Apply credits for <span id="billnod"></span></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Bill Balance: </span><b><%=curency%><span id="creditBalanceAmountdisplay"></span></b>
		      		</div>
		      	</div>
		      	<h4>General Credit</h4>
				<table class="table creditListTable">
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
				
					</tbody>
				</table>
				<input type="hidden" name="ordertotal" value="">
				<input type="hidden" name="totalbalpopup" value="">
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
		      			<span id="creditBalanceAmount"></span>
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

<div id="creditListModalExpense" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditFormExpense" method="post">
		<input type="hidden" id="numberOfDecimalExp" value=<%=numberOfDecimal%>>
		<INPUT type="hidden" name="ValidNumberExp" value="<%=OrdValidNumber%>"> <%--Resvi--%>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Apply credits for Expense ID <span id="expPayid"></span></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Expense Balance: </span><b><%=curency%><span id="creditBalanceAmountdisplayExp"></span></b>
		      		</div>
		      	</div>
		      	<h4>General Credit</h4>
				<table class="table creditListTableExp">
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
					
					</tbody> 
				</table>
				<h4>Order Credit</h4>
				<table class="table ordercreditListTableExp">
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
				
					</tbody>
				</table>
				<input type="hidden" name="ordertotalExp" value="">
				<input type="hidden" name="totalbalpopupExp" value="">
				<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Total Amount Applied: </span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="creditTotalAmountExp">0.00</span>
		      		</div>
		      	</div>
		      	<br>
		      	<div class="row">
		      		<div class="col-sm-offset-4 col-sm-6 text-right">
		      			<span>Bill Balance: </span>
		      		</div>
		      		<div class="col-sm-2 text-right">
		      			<span id="creditBalanceAmountExp"></span>
		      		</div>
		      	</div>
				</div>
				<div class="modal-footer">
	      		    	<div class="form-group">  
				    		<button type="button" class="btn btn-success" onClick="applyCreditExp()">Save</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
				    </div>
				</div>			
		</form>
	</div>
</div>



<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    
    var  curr = document.getElementById("CURRENCY").value;
    
    var basecurrency = '<%=curency%>';  <%--    resvi --%>
    
    document.getElementById('PaymentMade').innerHTML = "Amount Paid ("+curr+")";
    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
    
    document.getElementById('voucher_PaymentMade').innerHTML = "Amount Paid ("+curr+")";
    document.getElementById('voucher_exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
    
    
    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";	
    var numberOfDecimal = document.getElementById("numberOfDecimal").value;
 	var cust = "<%=sVendorCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	$("input[name ='CUST_CODE1']").val(cust);
    	 $('#supplierModal').modal('show');


    }
    
    
    $(document).on("focusout",".creditAmount", function(){
		  var value = $(this).val();
		  $(this).val(parseFloat(value).toFixed(numberOfDecimal));
		  var balance =  $(this).data("balance");
		  var ordertotal = $("input[name ='ordertotal']").val();
		  var ordertotalbal = $("input[name ='totalbalpopup']").val();
		  var originalBalance = ordertotalbal;
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
			  
			  var balamountorder = ordertotal;
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
    
    $(document).on("focusout",".creditAmountExp", function(){
		  var value = $(this).val();
		  $(this).val(parseFloat(value).toFixed(numberOfDecimal));
		  var balance =  $(this).data("balance");
		  var ordertotal = $("input[name ='ordertotalExp']").val();
		  var ordertotalbal = $("input[name ='totalbalpopupExp']").val();
		  var originalBalance = ordertotalbal;
		  if(isNaN(value)){ /* To check if valid input i.e., number */
			  $(this).val("0.00");
		  }
		  value = parseFloat(value);
		  if(value > balance){
			  $(this).val(parseFloat(balance).toFixed(numberOfDecimal));
		  }
		  var totalAmount = 0;
		  $(".creditAmountExp").each(function() {
			  totalAmount += parseFloat($(this).val())					    
		  });
		  		  
		  balance = parseFloat(originalBalance - totalAmount).toFixed(numberOfDecimal);
		  if(balance < 0){
			  $(this).val("0.00");
			  alert("Total Amount is exceeding Expense balance.");
		  }else{
			  
			  var balamountorder = ordertotal;
			  if(parseFloat(balamountorder) > 0){  
				
				  var tamt = "0";
				  $(".ordercreditListTableExp tbody tr").each(function() {
						var amt = $('td:eq(7)', this).find('input').val();
						tamt = tamt + amt;		
					});
				 
				  if(parseFloat(balamountorder) != parseFloat(tamt)){
					  $(".creditListTableExp tbody tr").each(function() {
							$('td:eq(7)', this).find('input').val(parseFloat("0").toFixed(numberOfDecimal));
						});
					  $( ".creditAmountvaluecExp" ).prop( "disabled", true );
					  totalAmount = 0;
					  $(".creditAmountExp").each(function() {
						  totalAmount += parseFloat($(this).val())					    
					  });
					  		  
					  balance = parseFloat(originalBalance - totalAmount).toFixed(numberOfDecimal);
					  
				  }else{
					  $( ".creditAmountvaluecExp" ).prop( "disabled", false );
				  }
			  
			  }
			  $('#creditTotalAmountExp').text(parseFloat(totalAmount).toFixed(numberOfDecimal));		
			  $('#creditBalanceAmountExp').text(balance);
		  }				  	  
	  });
    
    
    
});


function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Supplier ID!");
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
					ACTION : "SUPPLIER_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Supplier Already Exists");
							document.getElementById("CUST_CODE").focus();
							//document.getElementById("ITEM").value="";
							return false;
						}
						else
							return true;
					}
	});	
			return true;
	}
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
   var CUST_CODE   = document.form1.CUST_CODE.value;
   var CUST_NAME   = document.form1.CUST_NAME.value;
   var TAXTREATMENT   = document.form1.TAXTREATMENT.value;
   var RCBNO   = document.form1.RCBNO.value;
   var rcbn = RCBNO.length;

 
      
   var ValidNumber   = document.form1.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }
   
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.form1.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Supplier Name"); 
   document.form1.CUST_NAME.focus();
   return false; 
   }
   if(document.form1.TAXTREATMENT.selectedIndex==0)
	{
	alert("Please Select TAXTREATMENT");
	document.form1.TAXTREATMENT.focus();
	return false;
	}
   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
   {
   var  d = document.getElementById("TaxByLabel").value;
   	if(RCBNO == "" || RCBNO == null) {
   		
	   alert("Please Enter "+d+" No."); 
	   document.form1.RCBNO.focus();
	   return false; 
	   }
   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
	//{
	if(!IsNumeric(RCBNO))
	{
    alert(" Please Enter "+d+" No. Input In Number"); 
   	document.form1.RCBNO.focus();
   	return false; 
  	}

	if("15"!=rcbn)
	{
	alert(" Please Enter your 15 digit numeric "+d+" No."); 
		document.form1.RCBNO.focus();
		return false; 
		}
	//}
   }
else if(50<rcbn)
{
   var  d = document.getElementById("TaxByLabel").value;
   alert(" "+d+" No. length has exceeded the maximum value"); 
   document.form1.RCBNO.focus();
   return false; 
 }

   if(!IsNumeric(form1.PMENT_DAYS.value))
   {
     alert(" Please Enter Days In Number");
     form1.PMENT_DAYS.focus();  form1.PMENT_DAYS.select(); return false;
   }
   if(document.form1.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	   document.form.COUNTRY_CODE.focus();
	 return false;
	}
   /* document.form1.action  = "/track/CreateSupplierServlet?action=ADD&reurl=createBillpayment";
   document.form1.submit(); */
   
   var datasend = $('#formsupplierid').serialize();
   
	var urlStr = "/track/CreateSupplierServlet?action=JADD&reurl=createBillpayment";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : datasend,
	dataType : "json",
	success : function(data) {
		/* console.log(data);*/
		//alert(data.supplier[0].SID); 
		$("input[name ='vendno']").val(data.supplier[0].SID);
		$("input[name ='vendname']").val(data.supplier[0].SName);
		
		$('#supplierModal').modal('hide');
	}
	});

}



function onIDGen()
{
 /* document.form1.action  = "/track/CreateSupplierServlet?action=Auto-ID&reurl=createBillpayment";
   document.form1.submit(); */ 
	var urlStr = "/track/CreateSupplierServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : "<%=plant%>",
		action : "JAuto-ID",
		reurl : "createBillpayment"
	},
	dataType : "json",
	success : function(data) {
		
		$("input[name ='CUST_CODE']").val(data.supplier[0].SID);
		$("input[name ='CUST_CODE1']").val(data.supplier[0].SID);
		
	}
	});

}

function create_account() {
	
	if ($('#create_account_modal #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modal #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modal #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modal #acc_name').focus();
		return false;
	}
	
	if(document.create_form.acc_is_sub.checked)
	{
		if ($('#create_account_modal #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modal #acc_subAcct').focus();
			return false;
		}
		else
			{
			 var parType=$('#create_account_modal #acc_det_type').val();
			 subType=subType.trim();
			 var n = parType.localeCompare(subType);
			    if(n!=0)
			    	{
			    	$(".alert").show();
			    	$('.alert').html("For subaccounts, you must select the same account and extended type as their parent.");
			    	/* setTimeout(function() {
			            $(".alert").alert('close');
			        }, 5000); */
			    	 return false;
			    	}
			}
	}
	/* if ($('#create_account_modal #acc_balance').val() != "") {
		if ($('#create_account_modal #acc_balance').val() != "0") {
		if ($('#create_account_modal #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modal #acc_balanceDate').focus();
		return false;
		}
		}
	} */
	
	var formData = $('form[name="create_form"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				var pay = $("input[name ='inpayment']").val();
				if(pay == "1"){
					$("input[name ='voucher_paid_through_account_name']").val(data.ACCOUNT_NAME);
				}else{
					$("input[name ='paid_through_account_name']").val(data.ACCOUNT_NAME);
				}
				
				$('#create_account_modal').modal('toggle');
				
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
}

	

function aCredit(id,pono,vno,billno,balamount,CURRENCYID,CURRENCYUSEQT){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	var ozero = "0";
	var uuid = $("input[name ='uuid']").val();
	var conv_balanceAmount= parseFloat(parseFloat(balamount)*parseFloat(CURRENCYUSEQT)).toFixed(numberOfDecimal);
	$('#billnod').text(billno);
	$('#creditBalanceAmountdisplay').text(' '+parseFloat(balamount).toFixed(numberOfDecimal) +' / '+CURRENCYID +' '+ conv_balanceAmount);
	$('#creditBalanceAmount').text(parseFloat(balamount).toFixed(numberOfDecimal));
	$('#creditTotalAmount').text(zeroshow);	
	var urlStr = "/track/BillPaymentServlet?CMD=showcreditforapply";
	$.ajax( {
		type : "GET",
		url : urlStr,
		data: {
			vendno:vno,
			pono:pono,
			bid:id
		},
        success: function (data1) {
    
        	var obj = JSON.parse(data1);
        	console.log(obj);
        	
        	var body="";
        	var orderbody="";
        	$.each(obj.CREDIT,function(i,v){
        			var adfrom = v.ADVANCEFROM;
					var ponore = v.PONO;
					
					if(ponore == "" || ponore == null){
						var reference ="Excess Payment";
  						if(adfrom == "GENERAL"){
  							reference = v.REFERENCE;
  						}

						var cAmount = v.AMOUNT;
						cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
						
						var cbalAmount = v.BALANCE;
						cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
						
						body +='<tr>';
						body +='<td hidden>'+id+'</td>';
						body +='<td hidden>'+pono+'</td>';
						body +='<td hidden>'+v.PAYHDRID+'</td>';
						body +='<td>'+reference+'</td>';
						body +='<td>'+v.PAYMENT_DATE+'</td>';
						body +='<td class="text-right">'+cAmount+'</td>';
						body +='<td class="text-right">'+cbalAmount+'</td>';
						body +='<td><div class="float-right">';
						body +='<input class="form-control text-right creditAmount creditAmountvaluec" onkeypress="return isNumberKey(event,this,4)" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'" disabled>'
						body +='</div></td>';
						body +='<td hidden>'+v.ID+'</td>';
						body +='<td hidden>'+uuid+'</td>';
						body +='</tr>';
					
					}
        	});
        	var ordertottalamo = "0";
        	$.each(obj.CREDIT,function(i,v){
    			var adfrom = v.ADVANCEFROM;
				var ponore = v.PONO;
				if(ponore == "" || ponore == null){
					
				}else{
					if(pono == ponore){
						var cAmount = v.AMOUNT;
						cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
						
						var cbalAmount = v.BALANCE;
						cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
						ordertottalamo = parseFloat(cbalAmount) + parseFloat(ordertottalamo);
						
						orderbody +='<tr>';
						orderbody +='<td hidden>'+id+'</td>';
						orderbody +='<td hidden>'+pono+'</td>';
						orderbody +='<td hidden>'+v.PAYHDRID+'</td>';
						orderbody +='<td>'+v.PONO+'</td>';
						orderbody +='<td>'+v.PAYMENT_DATE+'</td>';
						orderbody +='<td class="text-right">'+cAmount+'</td>';
						orderbody +='<td class="text-right">'+cbalAmount+'</td>';
						orderbody +='<td><div class="float-right">';
						orderbody +='<input class="form-control text-right creditAmount creditAmountvalue" type="text"data-balance="'+v.BALANCE+'" value="'+zeroshow+'">'
						orderbody +='</div></td>';
						orderbody +='<td hidden>'+v.ID+'</td>';
						orderbody +='<td hidden>'+ozero+'</td>';
						orderbody +='</tr>';
					}
				}
    	});
        	
        	$("input[name ='ordertotal']").val(ordertottalamo);
        	$("input[name ='totalbalpopup']").val(balamount );
        	
			$(".creditListTable tbody").html(body);
			$(".ordercreditListTable tbody").html(orderbody);	
			
			if(ordertottalamo > 0){
  
        	}else{
        		
        		$( ".creditAmountvaluec" ).prop( "disabled", false );
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
	var billHdrId = [], pono = [], payHdrId = [], amount = [], payDetId = [], capplyKey = [];
	$(".creditListTable tbody tr").each(function() {
		
		var amt = $('td:eq(7)', this).find('input').val();
		if(amt > 0){
			billHdrId.push($('td:eq(0)', this).text());
			pono.push($('td:eq(1)', this).text());
			payHdrId.push($('td:eq(2)', this).text());
			payDetId.push($('td:eq(8)', this).text());
			capplyKey.push($('td:eq(9)', this).text());
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
			capplyKey.push($('td:eq(9)', this).text());
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
			CAPPLYKEY : capplyKey,
			CURRENCYUSEQT : CURRENCYUSEQTs,
		},
		dataType : "json",
		success : function(data) {
			if(data.ERROR_CODE == 100){
				$("#billlisttablebody").html("");
				vendorchanged();
				$('#creditListModal').modal('toggle');
				var numberOfDecimal = document.getElementById("numberOfDecimal").value;
				callTotalDetail(numberOfDecimal);
			}else{
				alert(data.MESSAGE);
			}
		}
	});
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
$('select[name="COUNTRY_CODE"]').on('change', function(){		
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

</script>

<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>