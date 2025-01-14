<%@ page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.object.InvPaymentDetail"%>
<%@page import="com.track.db.object.InvPaymentHeader"%>
<%@page import="com.track.db.object.*"%>
<%@page import="com.track.service.*"%>
<%@page import="com.track.serviceImplementation.*"%>
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
String INVOICEID=StrUtils.fString(request.getParameter("INVOICEID"));
String INVOICENO = StrUtils.fString(request.getParameter("invnum"));
String title = "Payment for "+INVOICENO;
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String NOOFPAYMENT=((String) session.getAttribute("NOOFPAYMENT")); /* Resvi */
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
InvoicePaymentDAO invpayao = new InvoicePaymentDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
CustMstDAO custMstDAO = new CustMstDAO();
String OrdValidNumber="";

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

// resvi	ends

FinProjectDAO finProjectDAO = new FinProjectDAO();
String curDate =_dateUtils.getDate();
String ORDERNO = StrUtils.fString(request.getParameter("DONO"));
String DONO = StrUtils.fString(request.getParameter("DONO"));
String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
String empno = StrUtils.fString(request.getParameter("EMPNO"));
String cmd =StrUtils.fString(request.getParameter("cmd"));
String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
String invoiceAmount = StrUtils.fString(request.getParameter("AMT"));
double dinvoiceAmount ="".equals(invoiceAmount) ? 0.0d :  Double.parseDouble(invoiceAmount);
invoiceAmount = StrUtils.addZeroes(dinvoiceAmount, numberOfDecimal);
String type =StrUtils.fString(request.getParameter("type"));
String custbank=custMstDAO.getCustBank(plant, custCode);
String Urlred="../home";
String projectname = "";
int projectid=0;
String CURRENCYUSEQT="1",DISPLAY="",CURRENCYID="",conv_invoiceAmount="",conv_amount_Curr="";
Hashtable htbill = new Hashtable();
htbill.put("ID", sTranId);
htbill.put("PLANT", plant);
List invoiceHdrList =  invoiceDAO.getConvInvoiceHdrById(htbill);
if(invoiceHdrList.size()>0)
{
	Map invoiceHdr=(Map)invoiceHdrList.get(0);
	CURRENCYID = (String)invoiceHdr.get("CURRENCYID");
	DISPLAY = (String)invoiceHdr.get("DISPLAY");
	CURRENCYUSEQT = (String)invoiceHdr.get("CURRENCYUSEQT");
	conv_amount_Curr=String.valueOf((Double.parseDouble(invoiceAmount)/Double.parseDouble(CURRENCYUSEQT)));
	conv_invoiceAmount=StrUtils.addZeroes((Double.parseDouble(invoiceAmount)/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal);
	
	
	
	String pid = (String)invoiceHdr.get("PROJECTID");
	if(!pid.equalsIgnoreCase("")){
		projectid = Integer.valueOf(pid);
		if(projectid > 0){
			FinProject finProject = finProjectDAO.getFinProjectById(plant, projectid);
			projectname = "Project : "+finProject.getPROJECT_NAME();
		}
	}
	
	
}
if(type.equalsIgnoreCase("ADVANCE")){
	DoDetDAO doDetDao = new DoDetDAO();
	DoDetService doDetService = new DoDetServiceImpl();
	DoHDRService doHDRService = new DoHdrServiceImpl();
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	//String totalCost = doDetDao.getTotalReceiptCostByOrder(ORDERNO, plant);
	DoHdr doHdr = doHDRService.getDoHdrById(plant, ORDERNO);
	List<DoDet> doDetDetails = doDetService.getDoDetById(plant, ORDERNO);
	double subTotal =0,totalTax = 0;
	for(DoDet doDet :doDetDetails){
		String qty="", unitPrice="", amount="", percentage="", tax="",
				item_discounttype = "", lineDiscount = "",
				pickedQty = "", issuedQty = "", cost="";
		double discount = 0, dTax = 0;
		item_discounttype = doDet.getDISCOUNT_TYPE();
		qty = doDet.getQTYOR().toString();
		double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);

		double dCost = doDet.getUNITPRICE() * doDet.getCURRENCYUSEQT();
		unitPrice = StrUtils.addZeroes(dCost, numberOfDecimal);
		double item_amount = (dCost*dQty);
			
		if(item_discounttype.equalsIgnoreCase("%")){
				double dDiscount = doDet.getDISCOUNT();
				discount = ((item_amount)/100)*dDiscount;
				lineDiscount = StrUtils.addZeroes(dDiscount, "3");
		}else{
				discount = doDet.getDISCOUNT() * doDet.getCURRENCYUSEQT();
				lineDiscount = StrUtils.addZeroes(discount, numberOfDecimal);
		}
			item_amount = item_amount - discount;
			
			subTotal += item_amount;
	}
	
	
	if(doHdr.getITEM_RATES() == 1){
		subTotal = (subTotal*100)/(doHdr.getOUTBOUND_GST()+100);
	}
	
	double dorderdiscountcost=0;
	String orderdiscountcost="0";
	if(doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){
		dorderdiscountcost = (subTotal/100)*doHdr.getORDERDISCOUNT();
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}else{
		dorderdiscountcost = doHdr.getORDERDISCOUNT();
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}

	String discounttype = doHdr.getDISCOUNT_TYPE();
	String discount = "0";
	double pdiscount=0;
	if(discounttype.equalsIgnoreCase("%")){
		double dDiscount = doHdr.getDISCOUNT();
		pdiscount = (subTotal/100)*dDiscount;
		discount = StrUtils.addZeroes(pdiscount, numberOfDecimal);
	}else{
		double dDiscount = doHdr.getDISCOUNT();
		pdiscount = doHdr.getDISCOUNT();
		discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
	}
	
	
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(doHdr.getTAXID());
	if(doHdr.getTAXID() != 0){
		if(fintaxtype.getSHOWTAX() == 1){
			
			totalTax = (subTotal/100)*doHdr.getOUTBOUND_GST();
			
			if(doHdr.getISSHIPPINGTAX() == 1){
				totalTax = totalTax + ((doHdr.getSHIPPINGCOST()/100)*doHdr.getOUTBOUND_GST());
			}
			
			if(doHdr.getISORDERDISCOUNTTAX() == 1){
				totalTax = totalTax - ((dorderdiscountcost/100)*doHdr.getOUTBOUND_GST());
			}
			
			if(doHdr.getISDISCOUNTTAX() == 1){
				totalTax = totalTax - ((pdiscount/100)*doHdr.getOUTBOUND_GST());
			}
		}
	}
	
	double totalAmount = (subTotal + totalTax + doHdr.getADJUSTMENT()+doHdr.getSHIPPINGCOST())-(dorderdiscountcost+pdiscount);
	

	double dTotalCost ="".equals(totalAmount) ? 0.0d : totalAmount;
	InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
	double orderadvanceamt = invoicePaymentDAO.getcreditamoutusingorderno(plant, ORDERNO);
	double paidamountfororder = invoicePaymentDAO.getpaidamoutusingorderno(plant, ORDERNO);	
	double pdcamount = 0.0;
	
	List<InvPaymentDetail> invdetlist = invpayao.getInvoicePaymentDetailsbydono(ORDERNO, plant, username);
	for(InvPaymentDetail invdet:invdetlist){
		Hashtable ht = new Hashtable();	
		ht.put("PAYMENTID",String.valueOf(invdet.getRECEIVEHDRID()));
		ht.put("PLANT",plant);
		List pdcDetailListpc = invpayao.getpdcbipayid(ht);
		for(int j =0; j < pdcDetailListpc.size(); j++) {
			Map pdcdet=(Map)pdcDetailListpc.get(j);
			String status = (String)pdcdet.get("STATUS");
			if(status.equalsIgnoreCase("NOT PROCESSED")) {
				pdcamount = pdcamount+(Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT")) * Double.parseDouble((String)pdcdet.get("CURRENCYUSEQT")));
			}
		}
	}
	
	
	invoiceAmount=StrUtils.addZeroes((dTotalCost - (orderadvanceamt + paidamountfororder + pdcamount)), numberOfDecimal);
	
	Hashtable htdono = new Hashtable();
	htdono.put("DONO", ORDERNO);
	htdono.put("PLANT",plant);
	Map dodet=doDetDao.selectRow("CURRENCYUSEQT,ISNULL((SELECT TOP 1 CURRENCYID from "+plant+"_DOHDR D WHERE D.DONO='"+ORDERNO+"'),'') CURRENCYID, ISNULL((SELECT TOP 1 DISPLAY from "+plant+"_CURRENCYMST C WHERE C.CURRENCYID in (ISNULL((SELECT TOP 1 CURRENCYID from "+plant+"_DOHDR D WHERE D.DONO='"+ORDERNO+"'),''))),'') DISPLAY ",htdono);
	CURRENCYID = (String)dodet.get("CURRENCYID");
	DISPLAY = (String)dodet.get("DISPLAY");
	CURRENCYUSEQT = (String)dodet.get("CURRENCYUSEQT");
	conv_amount_Curr=String.valueOf((Double.parseDouble(invoiceAmount)/Double.parseDouble(CURRENCYUSEQT)));
	conv_invoiceAmount=StrUtils.addZeroes((Double.parseDouble(invoiceAmount)/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal);
	
	projectid = doHdr.getPROJECTID();
	if(projectid > 0){
		FinProject finProject = finProjectDAO.getFinProjectById(plant, projectid);
		projectname = "Project : "+finProject.getPROJECT_NAME();
	}

	
	
	title="Payment for "+ORDERNO;
	Urlred="../salesorder/edit?dono="+ORDERNO;		
}

if(type.equalsIgnoreCase("REGULAR"))
	Urlred="../invoice/detail?dono="+ORDERNO+"&custno="+custCode+"&INVOICE_HDR="+sTranId;

String zeroval = StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal);



%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<!-- <script src="js/Invoice.js"></script> -->
<script src="../jsp/js/InvoicePayment.js"></script>
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
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
</style>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <%  if(type.equalsIgnoreCase("REGULAR")){%>
                <li><a href="../invoice/summary"><span class="underline-on-hover">Invoice Summary</span> </a></li>
                <li><a href="../invoice/detail?dono=<%=ORDERNO %>&custno=<%=custCode %>&INVOICE_HDR=<%=sTranId %>"><span class="underline-on-hover">Invoice Detail</span> </a></li>
                <li><label>Record Payment</label></li> 
                <% }else {%>
                <li><a href="../salesorder/summary"><span class="underline-on-hover">Sales Order Summary</span></a></li>
                 <li><a href="../salesorder/detail?dono=<%=DONO%>"><span class="underline-on-hover">Sales Order Detail</span></a></li>	
                 <li><a href="../salesorder/edit?dono=<%=ORDERNO %>"><span class="underline-on-hover">Edit Sales Order</span> </a></li>
                <li><label>Advance Payment</label></li> 
                <%}%> 
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1></br>
              <h1 style="font-size: 20px;margin-top: 1%;margin-bottom: 1%;" class="box-title">
              		<%=projectname%>
              </h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='<%=Urlred %>'">
              	<i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<div class="container-fluid">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="invoicePaymentForm" class="form-horizontal" name="form1" enctype="multipart/form-data">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Customer Name</label>
			<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="Select a customer" readonly name="customer" value="<%=CUSTOMER%>" required>
				<!-- <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
				<input type="text" name="plant" id="plant" value="<%=plant%>" hidden>
				<input type="text" name="username" value=<%=username%> hidden>
				<input type = "hidden" name="PROJECTID" value="<%=projectid%>">
			    <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
			    <INPUT type = "hidden" name="curency" value = "<%=curency%>">
			    <INPUT type = "hidden" name="EMPNO" value = "<%=empno%>">
			    <INPUT type="hidden" name="cmd" value="<%=cmd%>" />
			    <INPUT type="hidden" name="TranId" value="<%=sTranId%>" />
			    <INPUT type="hidden" name="invoiceheaderid" value="<%=INVOICEID%>" />
			    <INPUT type="hidden" name="invoicenumber" value="<%=INVOICENO%>" />
			    <INPUT type="hidden" name="initialAmount" id="initialAmount" value="<%=conv_invoiceAmount%>" />  
			    <INPUT type="hidden" name="type" value="<%=type%>" />      
			    <INPUT type="hidden" name="dono" value="<%=ORDERNO%>" /> 
			    <INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>"> 
			    <input name="paidpdcstatus" type="hidden" value="0">
				<input name="pdcamount" type="hidden" value="0">
				<input name="vbank" type="hidden" value="<%=custbank%>">   
				<INPUT type="hidden" name="CURRENCYID"  value="<%=CURRENCYID%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				<INPUT type="hidden" name="conv_amount_Curr" id="conv_amount_Curr"  value="<%=conv_amount_Curr%>"> 
				<input name="isbank" type="hidden" value="">
			</div>
				
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Balance Due&nbsp;(<%=curency%>)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="baldue" name="baldue" value="<%=conv_invoiceAmount%>" readonly>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Date of Receipt</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="payment_date" name="payment_date" value="<%=curDate%>">
			</div>
		</div>
		<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Currency</label>
						<div class="col-sm-4"> 
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" value="<%=DISPLAY%>" required readonly>
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" id="exchangerate" ></label>   <%--    resvi --%>
						<div class="col-sm-4"> 
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" onkeypress="return isNumberKey(event,this,4)" >	
						</div>
					</div>
					<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required" id="PaymentMade" ></label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="amount_Curr" name="amount_Curr" value="<%=invoiceAmount%>" required READONLY>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Amount&nbsp;(<%=curency%>)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="amount" name="amount" value="<%=conv_invoiceAmount%>" onkeypress="return isNumberKey(event,this,4)" required>
			</div>
		</div>
		
		
		<div class="form-group">
			
			<label class="control-label col-form-label col-sm-2 required">Mode of Receipt</label>
			<div class="col-sm-4">
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
						<input id="payment_mode" name="payment_mode" class="form-control" type="text" onchange="checkpaymenttype(this.value)" value="">
						
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
										<input type="hidden" name="chequestatus" value="NOT APPLICABLE">
										<input type="hidden" name="pdcid" value="0">
										<input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc(this)">
									</td>
									<td class="text-center">
										<input name="bank_branch" type="hidden"> 
										<input class="form-control text-left bankAccountSearch" name="bankname" type="text" value="<%=custbank%>" placeholder="Select a bank"> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date"  value="<%=curDate%>" disabled> 
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
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<!-- <button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
				<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button> -->
				<button id="btnInvoiceSave" type="submit" class="btn btn-success">Record Payment</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
</form>
</div>
	<!-- Modal -->
<%-- 	<%@include file="newBankModal.jsp"%> --%>
<%-- 	<%@include file="newCustomerModal.jsp" %> --%>
<%-- 	<%@include file="newEmployeeModal.jsp" %> --%>
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
<%-- 	<%@include file="newProductModal.jsp" %> --%>
	<%@include file="CoaNewAccountModal.jsp"%>
<%-- 	<%@include file="newGstDetailModal.jsp" %> --%>
<%-- 	<%@include file="newPaymentTermsModal.jsp" %> --%>
	<%@include file="newPaymentModeModal.jsp"%>
	<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    <!-- Modal content-->
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
	</div>


</div>
</div>
<%-- <%@include file="newPaymentTypeModal.jsp"%> --%>
<script type="text/javascript">

$(document).ready(function(){
	var  curr = document.getElementById("CURRENCY").value;
	var basecurrency = '<%=curency%>';  <%--    resvi --%>
	
    document.getElementById('PaymentMade').innerHTML = "Amount Received ("+curr+")";
    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
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
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>