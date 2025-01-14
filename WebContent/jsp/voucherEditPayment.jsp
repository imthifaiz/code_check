<%@page import="com.track.db.object.InvPaymentAttachment"%>
<%@page import="com.track.db.object.InvPaymentDetail"%>
<%@page import="com.track.db.object.InvPaymentHeader"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.db.object.FinProject"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Edit Receipt Voucher Payment";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
DateUtils _dateUtils = new DateUtils();
InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
String curDate =_dateUtils.getDate();
int paymentheaderid=Integer.parseInt(request.getParameter("paymentid"));
InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
InvPaymentHeader invoicePaymentHdr=invoicePaymentDAO.getInvoicePaymentById(paymentheaderid, plant, username);
List<InvPaymentDetail> invoicePaymentDetails=invoicePaymentDAO.getInvoicePaymentDetails(paymentheaderid, plant, username);
List<InvPaymentAttachment> invoicePaymentAttachments=invoicePaymentDAO.getInvoiceAttachmentDetails(paymentheaderid, plant, username);
CustMstDAO custMstDAO = new CustMstDAO();
String custno=invoicePaymentHdr.getCUSTNO();
String custname=custMstDAO.getCustName(plant, custno);
String cmd="EDIT";
InvoiceUtil invoiceUtil = new InvoiceUtil();
List<Map> invoiceHeaderList=new ArrayList<>();
Map< String,InvPaymentDetail> invoicePaymentDet =  new HashMap< String,InvPaymentDetail>();
List<Map> invoicePaymentDetList=new ArrayList<>();
for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetails)
	{
	if(invoicePaymentDetail.getTYPE().equalsIgnoreCase("REGULAR"))
	{
		Hashtable ht = new Hashtable();
		ht.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
		ht.put("PLANT", plant);
		List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
		if(invoiceHdrList.size()>0)
		{
			Map invoiceHdr=(Map)invoiceHdrList.get(0);
			invoiceHdr.put("Amountpaid", invoicePaymentDetail.getAMOUNT());
			invoiceHeaderList.add(invoiceHdr); 
			invoicePaymentDet.put((String) invoiceHdr.get("INVOICE"),invoicePaymentDetail);
			invoicePaymentDetList.add(invoicePaymentDet);
		}
	}
	
	}
String amountReceived = String.valueOf(invoicePaymentHdr.getAMOUNTRECEIVED());
	double damountReceived ="".equals(amountReceived) ? 0.0d :  Double.parseDouble(amountReceived);
	amountReceived = StrUtils.addZeroes(damountReceived, numberOfDecimal);
	
String bankCharges = String.valueOf(invoicePaymentHdr.getBANK_CHARGE());
double dbankCharges ="".equals(bankCharges) ? 0.0d :  Double.parseDouble(bankCharges);
bankCharges = StrUtils.addZeroes(dbankCharges, numberOfDecimal);

InvPaymentDetail invdetalepay = invoicePaymentDetails.get(0);
String paytypedet= invdetalepay.getTYPE();
List<InvPaymentDetail> invoicePaymentDetailscredit = new ArrayList<InvPaymentDetail>();
List<Map> invoiceHeaderListcredit=new ArrayList<>();
Map< String,InvPaymentDetail> invoicePaymentDetcredit =  new HashMap< String,InvPaymentDetail>();
if(!invoicePaymentHdr.getCREDITAPPLYKEY().equalsIgnoreCase("0")){
	 invoicePaymentDetailscredit=invoicePaymentDAO.getInvoicePaymentDetailsbyuuid(invoicePaymentHdr.getCREDITAPPLYKEY(), plant, username);
	 
	 for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetailscredit)
		{
		if(invoicePaymentDetail.getTYPE().equalsIgnoreCase("REGULAR"))
		{
			Hashtable ht = new Hashtable();
			ht.put("ID", String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
			ht.put("PLANT", plant);
			List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
			if(invoiceHdrList.size()>0)
			{
				Map invoiceHdr=(Map)invoiceHdrList.get(0);
				invoiceHdr.put("Amountpaid", invoicePaymentDetail.getAMOUNT());
				invoiceHeaderListcredit.add(invoiceHdr); 
				invoicePaymentDetcredit.put((String) invoiceHdr.get("INVOICE"),invoicePaymentDetail);
;
			}
		}
		
		}
	 
}
String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);

String projectname = "";
int proid = invoicePaymentHdr.getPROJECTID();
FinProjectDAO finProjectDAO = new FinProjectDAO();
FinProject finProject=new FinProject();
if(proid > 0){
	finProject = finProjectDAO.getFinProjectById(plant, proid);
	projectname = finProject.getPROJECT_NAME();
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
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<!-- <script src="js/Invoice.js"></script> -->
<script src="../jsp/js/voucherEditPayment.js"></script>
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
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><a href="../banking/invoicepaysummary"><span class="underline-on-hover">Receipt Summary </span></a></li>	
                <li><a href="../banking/invoicepaydetail?ID=<%=paymentheaderid%>"><span class="underline-on-hover">Invoice Receipt</span></a></li>
                <li><label>Edit Receipt Voucher Payment</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/invoicepaydetail?ID=<%=paymentheaderid%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<div class="container-fluid">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="invoiceEditPaymentForm" class="form-horizontal" name="form1" enctype="multipart/form-data">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Account Name</label>
			<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="account_name" placeholder="Select a Account" readonly name="account_name" value="<%=invoicePaymentHdr.getACCOUNT_NAME()%>" required>
				<!-- <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
				<input type="text" name="headerid" id="headerid" value="<%=invoicePaymentHdr.getID()%>" hidden>
				<!-- <input name="bank_branch" type="hidden"> -->
				<input type="text" name="plant" id="plant" value="<%=plant%>" hidden>
				<input type="text" name="username" value=<%=username%> hidden>
				<input type = "hidden" name="PROJECTID" value="<%=invoicePaymentHdr.getPROJECTID()%>">
			<INPUT type = "hidden" name="CUST_CODE" value = "<%=custno%>">
			<INPUT type = "hidden" name="curency" value = "<%=curency%>">
			<INPUT type="hidden" name="cmd" value="<%=cmd%>" /> 
			<INPUT type="hidden" name="initialAmount" id="initialAmount" value="<%=amountReceived%>" />
			<input name="paidpdcstatus" type="hidden" value="0">
				<input name="pdcamount" type="hidden" value="0">
				<input name="vbank" type="hidden" value="">    
			</div>
				
		</div>
		
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" value="<%=projectname%>"> 
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
			<label class="control-label col-form-label col-sm-2 required">Amount</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="amount" name="amount" value="<%=amountReceived%>">
			</div>
		</div>
		
		
		<%-- <div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2">Choose a Bank</label>
			<div class="col-sm-4">
				<input id="bankAccountSearch" name="bank_name" class="form-control text-left " value="<%=invoicePaymentHdr.getBANK_BRANCH()%>" type="text"> 
			</div>
		</div>
		
		<div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2 required">Cheque No:</label>
			<div class="col-sm-4">
				<input name="checqueNo" class="form-control text-left " value="<%=invoicePaymentHdr.getCHECQUE_NO()%>" type="text"> 
			</div>
		</div>
		
		<div class="form-group bankAccountSection" hidden>
			<label class="control-label col-form-label col-sm-2 required">Cheque Date:</label>
			<div class="col-sm-4">
				<input name="checquedate" class="form-control datepicker " value="<%=invoicePaymentHdr.getCHEQUE_DATE()%>" type="text"> 
			</div>
		</div> --%>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Date of Receipt</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="payment_date" name="payment_date" value="<%=invoicePaymentHdr.getRECEIVE_DATE()%>">
			</div>
		</div>
		<div class="form-group">
			
			<label class="control-label col-form-label col-sm-2 required">Mode of Receipt</label>
			<div class="col-sm-4">
						<span class="pay-select-icon-invoice" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
						<input id="payment_mode" name="payment_mode" class="form-control" value="<%=invoicePaymentHdr.getRECEIVE_MODE()%>" type="text">
						
			</div>	
			
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Deposit To</label>
			<div class="col-sm-4">				
				<input type="text" id="paid_through_account_name" name="paid_through_account_name" class="form-control" value="<%=invoicePaymentHdr.getDEPOSIT_TO()%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Bank Charges (if any)</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="bankcharges" name="bankcharges" value="<%=bankCharges%>" readonly>
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
										<input class="form-control text-left bankAccountSearch" name="bankname" type="text" value="" placeholder="Select a bank"> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date">
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
				<input type="text" class="form-control" id="referenceno" maxlength="50" name="referenceno" value="<%=invoicePaymentHdr.getREFERENCE()%>">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Notes</label>
			<div class="col-sm-4">				
				<textarea rows="2" name="notes" id="notes" maxlength="1000" class="form-control"><%=invoicePaymentHdr.getNOTE()%></textarea>
			</div>
		</div>
	
		
		<!-- Attach Files -->
		<div class="row grey-bg">
			<div class="col-sm-4">
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="invAttch" name="file" multiple="true" accept=".xls,.xlsx,.jpeg,.png,.gif,.tiff,.doc,.docx,.txt,.pdf,jpg, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
					</div>
				</div>
				<%if(invoicePaymentAttachments.size()>0){ %>
						<div id="billAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=invoicePaymentAttachments.size()%> files Attached</a>
									<div class="tooltiptext">
										
										<%for(InvPaymentAttachment invAttach:invoicePaymentAttachments){ %>
												<div class="row" style="padding-left:10px;padding-top:10px">
													<span class="text-danger col-sm-3">
														<%if(invAttach.getFileType().equalsIgnoreCase("application/pdf")) {%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
														<%}else if(invAttach.getFileType().equalsIgnoreCase("image/jpeg") || invAttach.getFileType().equalsIgnoreCase("image/png") || invAttach.getFileType().equalsIgnoreCase("image/gif") || invAttach.getFileType().equalsIgnoreCase("image/tiff")){ %>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
														<%} else{%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
														<%} %>
													</span>
													<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=invAttach.getFileName() %></a></span><br><span class="fileTypeFont">File Size: <%=invAttach.getFileSize()/1024 %>KB</span></div>
												</div>
												<div class="row bottomline">
														<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=invAttach.getID() %>,'<%=invAttach.getFileName() %>')"> Download</i></span>
														<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=invAttach.getID() %>)"> Remove</i></span>
												</div>	
										<%} %>
										
									</div>
								</div>
								
							</small>
						</div>
				<%}else{ %>
					<div id="billAttchNote">
						<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
					</div>
				<%} %>
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
				<button id="btnInvoiceSave" type="submit" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>	
</form>
</div>
	<!-- Modal -->
	<%@include file="newBankModal.jsp"%>
	<%@include file="newCustomerModal.jsp" %>
	<%@include file="newEmployeeModal.jsp" %>
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
	<%@include file="newProductModal.jsp" %>
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="newPaymentTermsModal.jsp" %>
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
<div id="applycredit" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditForm" method="post">
		<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
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
<script>

$(document).ready(function(){   
 
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


function aCredit(id,dono,cno,invno,balamount){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	$('#invoiceno').text(invno);
	$('#invoicebal').text(parseFloat(balamount).toFixed(numberOfDecimal));
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
						body +='<input class="form-control text-right creditAmount creditAmountvaluec creditAmountentry" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'" disabled>'
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
					pid : pid
				},
				dataType : "json"
				
			});
		}
	  
	});
	
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
	
	/* $('#applycredit').modal('toggle'); */
	
}

function loadCOAData(){
	var accName=$("#create_account_modal #acc_name").val();
	$("#paid_through_account_name").val(accName);
}

</script>
<%@include file="newPaymentTypeModal.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>