<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.track.db.object.FinProject"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String billHdrId = StrUtils.fString(request.getParameter("paymentid"));
BillPaymentDAO billDao = new BillPaymentDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
VendMstDAO vendMstDAO=new VendMstDAO();
RecvDetDAO recvDetDAO = new RecvDetDAO(); 

String title = "Edit Payment Voucher";
Hashtable ht = new Hashtable();
ht.put("ID", String.valueOf(billHdrId));
ht.put("PLANT", plant);
List billHdrList =  billDao.getBillPaymentHdrList(ht);
Map grnbillHdr=(Map)billHdrList.get(0);
Hashtable ht1 = new Hashtable();
ht1.put("PAYHDRID", String.valueOf(billHdrId));
ht1.put("PLANT", plant);
List billDetList=billDao.getBillPaymentDetList(ht1);
String vendno=(String) grnbillHdr.get("VENDNO");
Hashtable ht2 = new Hashtable();
ht2.put("VENDNO", vendno);
ht2.put("PLANT", plant);
String vendname=vendMstDAO.getVendorNameByNo(ht2);
List billAttachList=billDao.getBillPaymentAttachListById(ht1);
List capplyList=new ArrayList();
String capplykey = (String)grnbillHdr.get("CREDITAPPLYKEY");
if(!capplykey.isEmpty()){
	Hashtable htcappy = new Hashtable();	
	htcappy.put("CREDITAPPLYKEY",capplykey);
	String sql = "SELECT A.ID,LNNO,BILL,B.GRNO,B.PONO,BILL_DATE,(ISNULL((SELECT SUM(C.AMOUNT) FROM "+plant+"_FINPAYMENTDET C WHERE C.BILLHDRID=A.BILLHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "+plant+"_FINPAYMENTDET A JOIN "+plant+"_FINBILLHDR B on A.BILLHDRID=B.ID WHERE A.PLANT='"+ plant+"'";
	capplyList =  recvDetDAO.selectForReport(sql, htcappy, "");
}

fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
String CURRENCYUSEQT="0",DISPLAY="";

String bankCharges = String.valueOf(grnbillHdr.get("BANK_CHARGE"));
double dbankCharges ="".equals(bankCharges) ? 0.0d :  Double.parseDouble(bankCharges);
bankCharges = StrUtils.addZeroes(dbankCharges, numberOfDecimal);

Map mpay =(Map)billDetList.get(0);
CURRENCYUSEQT=(String)mpay.get("CURRENCYUSEQT");

if(CURRENCYUSEQT == null || CURRENCYUSEQT.equalsIgnoreCase("null")){
	CURRENCYUSEQT = "1";
}

String vamount = String.valueOf(grnbillHdr.get("AMOUNTPAID"));
double dvamount ="".equals(vamount) ? 0.0d :  Double.parseDouble(vamount);
vamount = StrUtils.addZeroes((dvamount*Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
String conv_vamount_Curr =String.valueOf((dvamount*Float.parseFloat(CURRENCYUSEQT)));

String vamount_Curr = String.valueOf(grnbillHdr.get("AMOUNTPAID"));
double dvamount_Curr ="".equals(vamount_Curr) ? 0.0d :  Double.parseDouble(vamount_Curr);
vamount_Curr = StrUtils.addZeroes(dvamount_Curr, numberOfDecimal);

String vendbank = (String)grnbillHdr.get("PAID_THROUGH");

String projectid = (String)grnbillHdr.get("PROJECTID");
String projectname = "";
if(projectid == null || projectid.equalsIgnoreCase("null")){
	
}else{
	if(!projectid.isEmpty()){
		int proid = Integer.valueOf(projectid);
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		if(proid > 0){
			finProject = finProjectDAO.getFinProjectById(plant, proid);
			projectname = finProject.getPROJECT_NAME();
		}
	}
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
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/VoucherPaymentEdit.js"></script>
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
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../banking/billpaysummary"><span class="underline-on-hover">Bill and Voucher Payment Summary</span> </a></li> 
                <li><a href="../banking/billpaydetail?TRANID=<%=billHdrId %>"><span class="underline-on-hover">Payment Voucher Detail</span> </a></li>                    
                <li><label>Edit Payment Voucher</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/billpaydetail?TRANID=<%=billHdrId %>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="/track/BillPaymentServlet?Submit=Edit_voucher"  method="post" enctype="multipart/form-data" onsubmit="return validatePayment()">
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="username" value="<%=username%>">
				<input type="hidden" name="paymenthdrid" value="<%=billHdrId%>">
				<input name="isbank" type="hidden" value="0">
				<input name="vendno" type="hidden" value="<%=vendno%>">
				<input name="totalBillAmount" type="hidden" value="0">
				<input type="hidden" id="numberOfDecimal" value="<%=numberOfDecimal%>">	 
				<input name="paidpdcstatus" type="hidden" value="0">
				<input name="pdcamount" type="hidden" value="0">
				<input name="vbank" type="hidden" value="<%=vendbank%>">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=grnbillHdr.get("CURRENCYID")%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				<INPUT type="hidden" name="conv_amount_paid_Curr" id="conv_amount_paid_Curr"  value="<%=conv_vamount_Curr%>">
				<input type="hidden" name="PROJECTID" value="<%=projectid%>">
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Account Name</label>
						<input type="text" class="ac-selected  form-control typeahead" id="account_name" placeholder="Select a Acount" name="account_name" value="<%=grnbillHdr.get("ACCOUNT_NAME")%>"  readonly>
						<!-- <input id="account_name" name="account_name" placeholder="Select a Acount"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span> -->
					</div>
					
					<div class="col-lg-4 form-group">
						<label>Project</label>
							<input id="project" placeholder="Select a project" name="project" class="form-control text-left" value="<%=projectname %>" type="text"> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'project\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					
					<div class="col-lg-4 form-group">
						<label class="required">Date of Receipt&nbsp;</label>
						<input id="" class="form-control datepicker" name="payment_date" type="text" READONLY value="<%=grnbillHdr.get("PAYMENT_DATE") %>">
					</div>
					
					
				</div>
				
				<div class="row">
				<div class="col-lg-4 form-group">
						<label class="required">Currency</label> 
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" value="<%=grnbillHdr.get("DISPLAY")%>" required readonly>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>	
					<div class="col-lg-4 form-group vendor-section">
						<label class="required" id="exchangerate" ></label> <%--Resvi--%>
						<div class="input-group"> 
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" >	
						</div>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required" id="PaymentMade" ></label>
						<input id="" class="form-control" name="amount_paid_Curr" id="amount_paid_Curr" type="text"  value="<%=vamount%>" onchange="calculatebasecurrency(this)" required>
					</div>
					
					
							
				</div>
				
				<%-- <div class="row">
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Choose a Bank</label>
						<input id="bankAccountSearch" name="bank_name" class="form-control text-left " type="text" value="<%=grnbillHdr.get("BANK_BRANCH") %>"> 
					</div>
					
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Cheque No:</label>
						<input name="checqueNo" class="form-control text-left " type="text" value="<%=grnbillHdr.get("CHECQUE_NO") %>"> 
					</div>
					
					<div class="col-lg-4 form-group bankAccountSection" hidden>
						<label class="required">Cheque Date:</label>
						<input type="text" class="form-control datepicker" id="cheque_date" name="cheque_date" value="<%=grnbillHdr.get("CHEQUE_DATE") %>">
					</div>
				</div> --%>
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Amount&nbsp;(<%=curency%>)</label>
						<input id="amount_paid" name="amount_paid" class="form-control text-left" readonly onchange="amountchanged(this)" type="text" value="<%=vamount_Curr%>" required>  
					</div>
					
					<div class="col-lg-4 form-group">
						<label class="required">Mode of Receipt</label>
						<input id="payment_mode" name="payment_mode" class="form-control" type="text" value="<%=grnbillHdr.get("PAYMENT_MODE") %>">
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					
					<div class="col-lg-4 form-group">
						<label class="required">Paid Through</label>
						<input id="paid_through_account_name" name="paid_through_account_name"
						 class="form-control text-left" type="text" value="<%=grnbillHdr.get("PAID_THROUGH") %>"> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
							
				</div>
				
				<div class="row">
					
					<div class="col-lg-4 form-group">
						<label>Bank Charges (if any)</label>
						<input id="bankCharge" name="bank_charge" class="form-control text-left" type="text" value="<%=bankCharges %>"> 
					</div>	
					
					<div class="col-lg-4 form-group">
					
					</div>
					
					<div class="col-lg-4 form-group">
					
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
										<input class="form-control text-left bankAccountSearch" name="bankname" type="text" placeholder="Select a bank" readonly> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="<%=grnbillHdr.get("PAYMENT_DATE")%>" disabled>
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
					<input maxlength="250" id="" name="refrence" class="form-control" type="text" value="<%=grnbillHdr.get("REFERENCE") %>"> 
				</div>
				
				<div class="form-group">
					<label>Notes</label> 
					<textarea rows="2" maxlength="950" name="note"  id="" class="form-control"><%=grnbillHdr.get("NOTE") %></textarea> 
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
						<%if(billAttachList.size()>0){ %>
						<div id="billPaymentAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=billAttachList.size()%> files Attached</a>
									<div class="tooltiptext">
										<%for(int i =0; i<billAttachList.size(); i++) {   
									  		Map attach=(Map)billAttachList.get(i); %>
												<div class="row" style="padding-left:10px;padding-top:10px">
													<span class="text-danger col-sm-3">
														<%if(attach.get("FileType").toString().equalsIgnoreCase("application/pdf")) {%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
														<%}else if(attach.get("FileType").toString().equalsIgnoreCase("image/jpeg") || attach.get("FileType").toString().equalsIgnoreCase("image/png") || attach.get("FileType").toString().equalsIgnoreCase("image/gif") || attach.get("FileType").toString().equalsIgnoreCase("image/tiff")){ %>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
														<%} else{%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
														<%} %>
													</span>
													<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.get("FileName").toString() %></a></span><br><span class="fileTypeFont">File Size: <%=Integer.parseInt(attach.get("FileSize").toString())/1024 %>KB</span></div>
												</div>
												<div class="row bottomline">
														<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.get("ID") %>,'<%=(String) attach.get("FileName") %>')"> Download</i></span>
														<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.get("ID") %>)"> Remove</i></span>
												</div>	
										<%} %>
										
									</div>
								</div>
								
							</small>
						</div>
						<%}else{ %>
						<div id="billPaymentAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
					</div>
				</div>

				
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="submit" class="btn btn-success">Save</button>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- Modal -->
	<%@include file="NewChartOfAccountAdd.jsp"%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newPaymentTypeModal.jsp"%>
	<div id="creditListModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="creditForm" method="post">
		<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Apply credits for <span id="billnod"></span></h4>
		      	</div>
		      	<div class="modal-body">
		      	<div class="row">
		      		<div class="col-sm-12 text-right">
		      			<span>Bill Balance: </span><b><span id="creditBalanceAmountdisplay"></span></b>
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
<!-- Modal -->
<script type="text/javascript">

$(document).ready(function(){
    
	var  curr = document.getElementById("CURRENCY").value;
	var basecurrency = '<%=curency%>';  <%--    resvi --%>
	
    document.getElementById('PaymentMade').innerHTML = "Amount Paid ("+curr+")";
    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
    
    $(document).on("focusout",".creditAmount", function(){
		  var value = $(this).val();
		  var numberOfDecimal = document.getElementById("numberOfDecimal").value;
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
    
    
    
});


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
	if ($('#create_account_modal #acc_balance').val() != "") {
		if ($('#create_account_modal #acc_balance').val() != "0") {
		if ($('#create_account_modal #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modal #acc_balanceDate').focus();
		return false;
		}
		}
	}
	
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
				$("input[name ='paid_through_account_name']").val(data.ACCOUNT_NAME);
				$('#create_account_modal').modal('toggle');
				
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
}

function aCredit(id,pono,vno,billno,balamount){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	$('#billnod').text(billno);
	$('#creditBalanceAmountdisplay').text(parseFloat(balamount).toFixed(numberOfDecimal));
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
						body +='<input class="form-control text-right creditAmount creditAmountvaluec" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'" disabled>'
						body +='</div></td>';
						body +='<td hidden>'+v.ID+'</td>';
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
	
	var billHdrId = [], pono = [], payHdrId = [], amount = [], payDetId = [];
	$(".creditListTable tbody tr").each(function() {
		
		var amt = $('td:eq(7)', this).find('input').val();
		if(amt > 0){
			billHdrId.push($('td:eq(0)', this).text());
			pono.push($('td:eq(1)', this).text());
			payHdrId.push($('td:eq(2)', this).text());
			payDetId.push($('td:eq(8)', this).text());
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
		},
		dataType : "json",
		success : function(data) {
			if(data.ERROR_CODE == 100){
				/* $("#billlisttablebody").html("");
				vendorchanged();
				$('#creditListModal').modal('toggle'); */
				location.reload();
			}else{
				alert(data.MESSAGE);
			}
		}
	});
}

</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>