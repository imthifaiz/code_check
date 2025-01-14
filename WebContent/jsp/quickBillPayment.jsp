<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.db.object.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String pono = StrUtils.fString(request.getParameter("pono"));
String type = StrUtils.fString(request.getParameter("type"));
String billHdrId = StrUtils.fString(request.getParameter("bill"));
String NOOFPAYMENT=((String) session.getAttribute("NOOFPAYMENT")); /* Resvi */
String Urlred="../home";
String CURRENCYUSEQT="0",DISPLAY="",CURRENCYID="";
PoHdrDAO poHdrDao = new PoHdrDAO();
PoDetDAO poDetDao = new PoDetDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();
BillUtil billUtil = new BillUtil();
VendMstDAO vendMstDAO =new VendMstDAO();
FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
DateUtils _dateUtils = new DateUtils();
String title = "Payment for "+pono;
		
String totalCost = "";
double dTotalCost =0.0;
String advancePayment = "", paymentMade = "", receivedAmount = "",paymentMade_Curr="",conv_paymentMade_Curr="";
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String vendno = poHdrDao.getSuppliercode(plant, pono);
String curDate ="",OrdValidNumber="";
curDate =_dateUtils.getDate();


    //resvi starts
    
	BillPaymentDAO billDao = new BillPaymentDAO();
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
	
   // resvi	ends




String vbankname="";

String title2 = "";
String title3 = "";
String projectid ="";



if(type.equalsIgnoreCase("ADVANCE")){
	//totalCost = poDetDao.getTotalReceiptCostByOrder(pono, plant);
	PoHdr poheader = poHdrDao.getPoHdrByPono(plant, pono);
	List<PoDet> podetail = poDetDao.getPoDetByPono(plant, pono);
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(poheader.getTAXID());
	double dTotalQty=0,dTotalPickedQty=0,shingpercentage=0,subTotal=0,totax=0;
	String taxdisplay="";
	for(PoDet poDet :podetail){

		String qty="", unitPrice="", amount="", percentage="", tax="",
				item_discounttype = "", lineDiscount = "",
				pickedQty = "", cost="";
		
		double discount = 0, dTax = 0;
		taxdisplay = poDet.getTAX_TYPE();
		item_discounttype = poDet.getDISCOUNT_TYPE();
		qty = poDet.getQTYOR().toString();
		double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			qty = StrUtils.addZeroes(dQty, "3");
			dTotalQty += dQty;
			
			pickedQty = poDet.getQTYRC().toString();
			double dpQty ="".equals(pickedQty) ? 0.0d :  Double.parseDouble(pickedQty);
			pickedQty = StrUtils.addZeroes(dpQty, "3");
			dTotalPickedQty += dpQty;

			
			double dCost = poDet.getUNITCOST();
			unitPrice = StrUtils.addZeroes(dCost, numberOfDecimal);
			double item_amount = (dCost*dQty);
			
			if(item_discounttype.equalsIgnoreCase("%")){
			double dDiscount = poDet.getDISCOUNT();
			discount = ((item_amount)/100)*dDiscount;
			lineDiscount = StrUtils.addZeroes(dDiscount, "3");
		}else{
			discount = poDet.getDISCOUNT();
			lineDiscount = StrUtils.addZeroes(discount, numberOfDecimal);
		}
			item_amount = item_amount - discount;
			subTotal += item_amount;
			
	}
	
	if(poheader.getISTAXINCLUSIVE() == 1){
		subTotal = (subTotal*100)/(poheader.getINBOUND_GST()+100);
	}
	double dorderdiscountcost=0;
	String orderdiscountcost="0";
	if(poheader.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){
		dorderdiscountcost = (subTotal/100)*poheader.getORDERDISCOUNT();
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}else{
		dorderdiscountcost = poheader.getORDERDISCOUNT();
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}
	
	if(poheader.getTAXID() != 0){
		if(fintaxtype.getSHOWTAX() == 1){
			
			totax = (subTotal/100)*poheader.getINBOUND_GST();
			
			if(poheader.getISSHIPPINGTAX() == 1){
				totax = totax + ((poheader.getSHIPPINGCOST()/100)*poheader.getINBOUND_GST());
			}
			
			if(poheader.getISDISCOUNTTAX() == 1){
				totax = totax - ((dorderdiscountcost/100)*poheader.getINBOUND_GST());
			}
		}
	}

	double totalAmount = (subTotal + poheader.getSHIPPINGCOST() + totax + poheader.getADJUSTMENT()) - dorderdiscountcost ;
	
	totalCost = String.valueOf(totalAmount);
	dTotalCost ="".equals(totalCost) ? 0.0d :  Double.parseDouble(totalCost);
	
	
	Hashtable ht2 = new Hashtable();
	ht2.put("PONO", pono);
	ht2.put("PLANT", plant);
	List POhdrList=poHdrDao.getOrderDetailsForBilling(ht2);
	Map pobillHdr=(Map)POhdrList.get(0);
	CURRENCYID = (String)pobillHdr.get("CURRENCY_CODE");
	DISPLAY = (String)pobillHdr.get("CURRENCYID");
	CURRENCYUSEQT = StrUtils.addZeroes(poheader.getCURRENCYUSEQT(), numberOfDecimal);
	advancePayment = billDao.getAdvancebBalanceByOrder(pono, plant);	
	double dAdvancePayment ="".equals(advancePayment) ? 0.0d :  Double.parseDouble(advancePayment);
	
	String paiedPayment = billDao.getpaymentamountByOrder(pono, plant);	
	double dpaidPayment ="".equals(paiedPayment) ? 0.0d :  Double.parseDouble(paiedPayment);
	
	double pdcamount =0.0;
	
	List paydetlist = billDao.getAdvancebBalanceByOrderlist(pono, plant);	
	for(int i =0; i < paydetlist.size(); i++) {
		Map paydetmap=(Map)paydetlist.get(i);
		Hashtable htpdc = new Hashtable();
		htpdc.put("PAYMENTID",(String)paydetmap.get("PAYHDRID"));
		htpdc.put("PLANT", plant);
		List pdcDetailListpc = billDao.getpdcbipayid(htpdc);
		for(int j =0; j < pdcDetailListpc.size(); j++) {
			Map pdcdet=(Map)pdcDetailListpc.get(j);
			String status = (String)pdcdet.get("STATUS");
			if(status.equalsIgnoreCase("NOT PROCESSED")) {
				pdcamount = pdcamount+Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT"));
			}
		}
		
	}
	
	paymentMade = StrUtils.addZeroes(((dTotalCost/poheader.getCURRENCYUSEQT()) - (dAdvancePayment +dpaidPayment+pdcamount)),numberOfDecimal);
	conv_paymentMade_Curr=String.valueOf((Double.valueOf(paymentMade)*Double.valueOf(CURRENCYUSEQT)));
	System.out.println(Double.valueOf(conv_paymentMade_Curr));
	paymentMade_Curr=StrUtils.addZeroes((Double.valueOf(conv_paymentMade_Curr)), numberOfDecimal);
	//paymentMade_Curr = StrUtils.addZeroes(Float.parseFloat(paymentMade_Curr), numberOfDecimal);
	
	/* conv_paymentMade_Curr=String.valueOf((Float.parseFloat(paymentMade)));
	paymentMade_Curr=StrUtils.addZeroes((Float.parseFloat(paymentMade)), numberOfDecimal);
	paymentMade_Curr = StrUtils.addZeroes(Float.parseFloat(paymentMade_Curr), numberOfDecimal);
	
	paymentMade = StrUtils.addZeroes((Float.parseFloat(paymentMade)/Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal); */
	
	Hashtable ht1 = new Hashtable();
	ht1.put("VENDNO", vendno);
	ht1.put("PLANT", plant);
	String vname = vendMstDAO.getVendorNameByNo(ht1);
	vbankname = vendMstDAO.getVendorbankByNo(ht1);
	
	title2 = "Supplier : "+vname;
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	if(poheader.getPROJECTID() > 0){
		finProject = finProjectDAO.getFinProjectById(plant, poheader.getPROJECTID());
		title3 = "Project : "+finProject.getPROJECT_NAME();
		projectid = String.valueOf(poheader.getPROJECTID());
	}
	
	//Urlred="maintIncomingOrder.jsp?PONO="+pono+"&action=View";
	Urlred="../purchaseorder/edit?pono="+pono;
}else{
	
	Hashtable ht = new Hashtable();
	ht.put("ID", billHdrId);
	ht.put("PLANT", plant);
	List billHdrList =  billUtil.getBillHdrById(ht);
	Map billHdr=(Map)billHdrList.get(0);
	Hashtable ht1 = new Hashtable();
	vendno = (String)billHdr.get("VENDNO"); 
	CURRENCYID = (String)billHdr.get("CURRENCYID");
	DISPLAY = (String)billHdr.get("DISPLAY");
	CURRENCYUSEQT = (String)billHdr.get("CURRENCYUSEQT");
	CURRENCYUSEQT = StrUtils.addZeroes(Float.parseFloat(CURRENCYUSEQT), numberOfDecimal);
	ht1.put("VENDNO",billHdr.get("VENDNO") );
	ht1.put("PLANT", plant);
	vbankname = vendMstDAO.getVendorbankByNo(ht1);
	totalCost = (String)billHdr.get("TOTAL_AMOUNT");
	dTotalCost ="".equals(totalCost) ? 0.0d :  Double.parseDouble(totalCost);
	
	receivedAmount = billDao.getreceivedAmountByBill(pono, billHdrId, plant);
	receivedAmount=StrUtils.addZeroes((Float.parseFloat(receivedAmount)*Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
	double dReceivedAmount ="".equals(receivedAmount) ? 0.0d :  Double.parseDouble(receivedAmount);
	
	paymentMade_Curr = StrUtils.addZeroes((dTotalCost - dReceivedAmount),numberOfDecimal);
	paymentMade=StrUtils.addZeroes((Float.parseFloat(paymentMade_Curr)/Float.parseFloat(CURRENCYUSEQT)), numberOfDecimal);
	conv_paymentMade_Curr=String.valueOf((Float.parseFloat(paymentMade_Curr)/Float.parseFloat(CURRENCYUSEQT)));
	//paymentMade_Curr = StrUtils.addZeroes(Float.parseFloat(paymentMade_Curr), numberOfDecimal);
	
		
	if(type.equalsIgnoreCase("REGULAR")){
		Urlred="../bill/detail?BILL_HDR="+billHdrId;
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		if(Integer.valueOf((String)billHdr.get("PROJECTID")) > 0){
			finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String)billHdr.get("PROJECTID")));
			title3 = "Project : "+finProject.getPROJECT_NAME();
			projectid = (String)billHdr.get("PROJECTID");
		}
	}
	
}




fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/quickBillPayment.js"></script>
<style>
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
	<h2><small><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
             <ul class="breadcrumb backpageul" >      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                  <% if(type.equalsIgnoreCase("REGULAR")){ %>
                  <li><a href="../bill/summary"><span class="underline-on-hover">Bill Summary</span> </a></li> 
                	<li><a href="<%=Urlred %>"><span class="underline-on-hover">Bill Detail</span> </a></li> 
                  <% } else {%>
                  <li><a href="../purchaseorder/summary"><span class="underline-on-hover">Purchase Order Summary</span></a></li> 
                  <li><a href="../purchaseorder/detail?pono=<%=pono%>"><span class="underline-on-hover">Purchase Order Detail</span></a></li>  
                  <li><a href="<%=Urlred %>"><span class="underline-on-hover">Edit Purchase Order</span></a></li>
                  <% } %>      
                  <li><label>Advance Payment</label></li>                                    
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<%if(type.equalsIgnoreCase("ADVANCE")){ %>
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 20px;margin-left: 13%;" class="box-title" ><%=title2 %></h1><br>
              <h1 style="font-size: 20px;margin-top: 2%;" class="box-title" ><%=title3 %></h1>
            <%}else if(type.equalsIgnoreCase("REGULAR")){ %>
             <h1 style="font-size: 20px;" class="box-title">Payment</h1><br>
             <h1 style="font-size: 20px;margin-top: 2%;" class="box-title" ><%=title3 %></h1>
            <%}else{ %>
               <h1 style="font-size: 20px;" class="box-title">Payment</h1>
            <%} %>
              
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='<%=Urlred %>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="/track/BillPaymentServlet?Submit=Save"  method="post" enctype="multipart/form-data" onsubmit="return validatePayment()">
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="username" value=<%=username%>>
				<!-- <input name="bank_branch" type="hidden">  -->
				<input name="vendno" type="hidden" value="<%=vendno%>">
				<input name="pono" type="hidden" value="<%=pono%>">
				<input name="ponoadv" type="hidden" value="<%=pono%>">
				<input name="billHdrId" type="hidden" value="<%=billHdrId%>">
				<input name="amount" type="hidden" value="<%=totalCost%>">
				<input name="type" type="hidden" value="<%=type%>">
				<input name="paytype" type="hidden" value="<%=type%>">
				<input name="allowedAmount" type="hidden" value="<%=paymentMade%>">
				<input name="billamount" type="hidden" value="<%=paymentMade%>">
				<input type="hidden" id="numberOfDecimal" value=<%=numberOfDecimal%>>	
				<input name="paidpdcstatus" type="hidden" value="0">
				<input name="pdcamount" type="hidden" value="0">
				<input name="vbank" type="hidden" value="<%=vbankname%>">
				<input name="isbank" type="hidden" value="">
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">    
				<INPUT type="hidden" name="CURRENCYID"  value="<%=CURRENCYID%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				 
				<INPUT type="hidden" name="conv_amount_paid_Curr" id="conv_amount_paid_Curr"  value="<%=conv_paymentMade_Curr%>">
				<input type="hidden" name="PROJECTID" value="<%=projectid%>">
				<div class="row">
					<div class="col-lg-4 form-group">
						<!-- <label class="required" id="BalanceDue"></label> -->
						<label class="required">Balance Due&nbsp;(<%=curency%>)</label>
						<input id="baldue" name="baldue" disabled class="form-control text-left"  type="text" value="<%=paymentMade%>"> 
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Date of Payment&nbsp;</label>
						<input id="" class="form-control datepicker" name="payment_date" type="text" value="<%=curDate%>" READONLY>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Currency</label> 
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" value="<%=DISPLAY%>" required readonly>
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						</span> 
					</div>				
				</div>
				
				<div class="row">
					<div class="col-lg-4 form-group vendor-section">
						<label class="required" id="exchangerate" ></label>  <%--    resvi --%>
						<div class="input-group"> 
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" onkeypress="return isNumberKey(event,this,4)" >	
						</div>
					</div>
					<div class="col-lg-4 form-group">
						<label class="required" id="PaymentMade" ></label>
						<input id="" class="form-control" name="amount_paid_Curr" id="amount_paid_Curr" type="text" READONLY  value="<%=paymentMade_Curr%>">
					</div>
					<div class="col-lg-4 form-group">
						<label class="required">Amount&nbsp;(<%=curency%>)</label>
						<input id="amount_paid" name="amount_paid" class="form-control text-left"  onchange="amountchanged(this)" onkeypress="return isNumberKey(event,this,4)" type="text" value="<%=paymentMade%>"> 
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
				
				<div class="row">
					<div class="col-lg-4 form-group">
						<label class="required">Mode of Payment</label>
						<input id="payment_mode" name="payment_mode" class="form-control" onchange="checkpaymenttype(this.value)" value="" type="text">
						<span class="pay-select-icon" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					
					<div class="col-lg-4 form-group">
						<label class="required">Paid Through</label>
						<input id="paid_through_account_name" name="paid_through_account_name"
						 class="form-control text-left" type="text"> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i>
						 </span>
					</div>
					
					<div class="col-lg-4 form-group">
						<label>Bank Charges (if any)</label>
						<input id="bankCharge" name="bank_charge" class="form-control text-left" type="text" readonly> 
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
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newPaymentModeModal.jsp"%>
<!-- Modal -->
<script type="text/javascript">

$(document).ready(function(){
	var  curr = document.getElementById("CURRENCY").value;
	var basecurrency = '<%=curency%>';  <%--    resvi --%>
	
    document.getElementById('PaymentMade').innerHTML = "Amount Paid ("+curr+")";
    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
    
    //document.getElementById('BalanceDue').innerHTML = "Balance Due ("+curr+")";
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
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>