<%@page import="com.track.dao.*"%>
<%@page import="com.track.db.object.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Edit Payroll Payment";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String ID = StrUtils.fString(request.getParameter("ID"));
	
	CurrencyDAO currencyDAO = new CurrencyDAO();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
	EmployeeDAO employeeDAO = new EmployeeDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	
	String curDate = _dateUtils.getDate();
	String[] splitdate = curDate.split("/");
	String cyear = splitdate[2];
	String cmonth = splitdate[1];
	int cimonth = Integer.valueOf(cmonth);

	String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
	
	String CURRENCYUSEQT="0",DISPLAY="";
	List curQryList=new ArrayList();
	curQryList = currencyDAO.getCurrencyDetails(curency,plant);
	for(int i =0; i<curQryList.size(); i++) {
		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
		DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
	 }
	
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	HrPayrollPaymentHdr payhdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(ID));
	
	double exchangerate = 0;
	List<PayrollPojo> PayrollPojoList = new ArrayList<PayrollPojo>();
	List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(plant, payhdr.getID());
	String payids = "0";
    for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
    	payids += ","+hrPayrollPaymentDet.getPAYID();
    	HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, hrPayrollPaymentDet.getPAYID());
    	exchangerate = exchangerate + hrPayrollPaymentDet.getCURRENCYUSEQT();
    	String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
	    String empcode = employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
		String monthyear = months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]+"-"+hrPayrollHDR.getYEAR();
			
		PayrollPojo PayrollPojo = new PayrollPojo();
		PayrollPojo.setID(hrPayrollHDR.getID());
		PayrollPojo.setEMPCODE(empcode);
		PayrollPojo.setEMPNAME(empname);
		PayrollPojo.setMONTH(monthyear);
		PayrollPojo.setPAYROLL(hrPayrollHDR.getPAYROLL());
		PayrollPojo.setATDAYS(String.valueOf(hrPayrollHDR.getPAYDAYS()));
		PayrollPojo.setSALARY(StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal));
		PayrollPojo.setSTATUS(hrPayrollHDR.getSTATUS());
		PayrollPojoList.add(PayrollPojo);
	}
    String amountmade = StrUtils.addZeroes((exchangerate*payhdr.getAMOUNTPAID()), numberOfDecimal);
    CURRENCYUSEQT = StrUtils.addZeroes(exchangerate, numberOfDecimal);
	
%>


<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_PAYMENT%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
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

.payment-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -12%;
	top: 15px;
}

.voucher-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -12%;
	top: 15px;
}
</style>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>

<div class="container-fluid m-t-20">

	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>  
                <li><a href="../payroll/paymentsummary"><span class="underline-on-hover">Payroll Payment Summary</span> </a></li>                     
                <li><label>Edit Payroll Payment</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
					&nbsp;
				</div>
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../payroll/paymentsummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="/track/HrPayrollPaymentServlet?Submit=edit_payprocess" onsubmit="return validatePayment()">
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="number" id="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>>
				<input type="text" name="LOGIN_USER" value="<%=username%>" hidden>
				<input type="text" name="pay_ischeque" value="0" hidden>
				<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
				<input type="hidden" id="CURRENCYUSEQT" value=<%=CURRENCYUSEQT%>>
				<INPUT type="hidden" name="payids"  value="<%=payids%>">
				<INPUT type="hidden" name="Transid"  value="<%=ID%>">
				<div id="VIEW_RESULT_HERE" class="table-responsive">
					<table class="table table-bordred table-striped">
						<thead>
							<tr>
								<th><input type=Checkbox name ="select" value="select" onclick="checkAll(this.checked);"></th>
								<th style="font-size: smaller;">PAYROLL</th>
								<th style="font-size: smaller;">EMPLOYEE ID</th>
								<th style="font-size: smaller;">EMPLOYEE</th>
								<th style="font-size: smaller;">MONTH</th>
								<th style="font-size: smaller;">ATTENDANCE DAYS</th>
								<th style="font-size: smaller;">SALARY</th>
								
							</tr>
						</thead>
						<tbody>
						<%for(PayrollPojo payrollPojo:PayrollPojoList){%>
							<tr>
								<td><input type="checkbox" name="appcheck" checked onclick="getcheckedids();" value="<%=payrollPojo.getID()%>"><input type="hidden" name="amount" value="<%=payrollPojo.getSALARY()%>"></th>
								<td><%=payrollPojo.getPAYROLL()%></td>
								<td><%=payrollPojo.getEMPCODE()%></td>
								<td><%=payrollPojo.getEMPNAME()%></td>
								<td><%=payrollPojo.getMONTH()%></td>
								<td><%=payrollPojo.getATDAYS()%></td>
								<td><%=StrUtils.addZeroes(Double.valueOf(payrollPojo.getSALARY()), numberOfDecimal)%></td>
							</tr>
							<%}%>
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Payment Date&nbsp;</label>
					</div>
					<div class="col-lg-4 form-group">
						<input id="" class="form-control datepicker" name="payment_date" type="text" value="<%=payhdr.getPAYMENT_DATE()%>" READONLY required>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Currency</label> 
					</div>
					<div class="col-lg-4 form-group">
						<input type="text" class="ac-selected  form-control typeahead" id="CURRENCY" placeholder="Select a Currency" name="CURRENCY" value="<%=payhdr.getCURRENCYID()%>" required>
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Exchange Rate</label>
					</div>
					<div class="col-lg-4 form-group">
						<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onchange="currencychanged(this)" required>	
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required" id="PaymentMade" ></label>
					</div>
					<div class="col-lg-4 form-group">
						<input class="form-control" name="amount_paid_Curr" type="text" value="<%=amountmade%>" READONLY>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Payment Made&nbsp;(<%=curency%>)</label>
					</div>
					<div class="col-lg-4 form-group">
						<input class="form-control" name="pay_netamount" type="text" value="<%=payhdr.getAMOUNTPAID() %>" READONLY>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Payment Mode</label>
					</div>
					<div class="col-lg-4 form-group">
						<input id="payment_mode" name="payment_mode" class="ac-selected form-control" type="text" value="<%=payhdr.getPAYMENT_MODE()%>" onchange="checkpaymenttype(this.value)">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Paid Through</label>
					</div>
					<div class="col-lg-4 form-group">
						<input id="paid_through_account_name" name="paid_through_account_name" class="ac-selected form-control text-left" type="text" value="<%=payhdr.getPAID_THROUGH()%>"> 
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>	
				<div class="row">
					<div class="col-lg-3 form-group">
						<label>Bank Charges (if any)</label>
					</div>
					<div class="col-lg-4 form-group">
						<input id="bankCharge" name="bank_charge" class="form-control text-left" value="<%=StrUtils.addZeroes(payhdr.getBANK_CHARGE(), numberOfDecimal)%>" type="text" readonly>
					</div>
				</div>
				<div class="row hidecheque" hidden>
					<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table payment-table">
							<thead>
								<tr>
									<th>Choose a Bank</th>
									<th>Cheque No</th>
									<th>Cheque Date</th>
									<th>Cheque Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<!-- <td class="text-center">
										<input type="hidden" name="pdcstatus" value="0"> 
										<input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc(this)">
									</td> -->
									<td class="text-center">
										<input name="bank_branch" type="hidden"> 
										<input class="form-control text-left bankAccountSearch" name="bankname" value="<%=payhdr.getBANK_BRANCH()%>" type="text" placeholder="Select a bank" readonly> 
									</td>
									<td class="text-center">
										<input class="form-control text-left" type="text" name="cheque-no" value="<%=payhdr.getCHEQUE_NO() %>" placeholder="Enter Cheque No">
									</td>
									<td class="text-center">
										<input class="form-control text-left datepicker" type="text" value="<%=payhdr.getCHEQUE_DATE()%>" name="cheque-date" placeholder="Enter Cheque Date" readonly>
									</td>
									<td class="text-center">
										<input class="form-control text-right" type="text" name="cheque-amount" readonly value="<%=StrUtils.addZeroes(payhdr.getCHEQUE_AMOUNT(), numberOfDecimal)%>">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<%-- <div class="form-group hidepdc" hidden>
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
				</div> --%>
				
				
				<div class="form-group">
					<label>Reference</label> 
					<input maxlength="300" name="refrence" class="form-control" type="text" value="<%=payhdr.getREFERENCE()%>" placeholder="Maximun 300 characters"> 
				</div>
				
				<div class="form-group">
					<label>Notes</label> 
					<textarea rows="2" maxlength="1000" name="note"  id="" class="form-control"  placeholder="Maximun 1000 characters"><%=payhdr.getNOTE()%></textarea> 
				</div>
				
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="submit" class="btn btn-success">Save</button>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					</div>
				</div>

			</form>
			
			<!-- Modal -->
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newPaymentModeModal.jsp"%>
	<%@include file="Payrolladdmstmodel.jsp"%>
	<div id="paydeductdetail" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title" style="font-weight: bold;">Payment Deduction Details</h4>
		      	</div>
		      	
		      	<div class="modal-body">	
					<div class="row">
			            <div class="col-md-12 popupmodel">
			            	<table>
								<tbody>
									<tr>
										<td style="padding: 10px">Deduction  Name</td>
										<td style="padding: 10px">:</td>
										<td id="dname" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Total Amount</td>
										<td style="padding: 10px">:</td>
										<td id="damount" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Deduction Due Amount</td>
										<td style="padding: 10px">:</td>
										<td id="ddueamount" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Balance Amount</td>
										<td style="padding: 10px">:</td>
										<td id="dbalamount" style="padding: 10px"></td>
									</tr>
								</tbody>
							</table>
			            </div>
			        </div>
			        <strong><p>Due Details</p></strong>
			        <div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table deduct-popup">
							<thead>
								<tr>
									<th>Due Amount</th>
									<th>Month</th>
									<th>Year</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			
				
			    
			</div>
	</div>
</div>
<!-- Modal -->
		</div>
	</div>
<script>
	$(document).ready(function(){
		var plant = document.form.plant.value;
		var cbank = "<%=payhdr.getBANK_BRANCH()%>";
	    if(cbank=="" || cbank.length==0 || cbank==null) {
	    }else{
	    	$("input[name ='pay_ischeque']").val("1");
			$("#bankCharge").attr("readonly", false);
			$(".hidecheque").show();
	    }
		var  curr = document.getElementById("CURRENCY").value;
	    document.getElementById('PaymentMade').innerHTML = "Payment Made ("+curr+")";
		
	    /* Payment Mode Auto Suggestion */
		$("#payment_mode").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{	  
				  display: 'PAYMENTMODE',  
				  source: function (query, process,asyncProcess) {
						var urlStr = "/track/PaymentModeMst";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : plant,
							action : "GET_PAYMENT_MODE_LIST",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.PAYMENTMODE);
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
					suggestion: function(data) {
						return '<p>' + data.PAYMENTMODE + '</p>';
					}
				  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Mode</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
			
		/* Paid Through Auto Suggestion */
		$("#paid_through_account_name").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'smalldrop'
				  }
			},
			{	  
			  display: 'text',  
			  source: function (query, process,asyncProcess) {
					var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "getSubAccountTypeGroup",
						module:"billpaymentpaidthrough",
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
							    '<span onclick="isbankcharge(this,\''+item.text+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span onclick="isbankcharge(this,\''+item.text+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".smalldrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".smalldrop").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".smalldrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".smalldrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
			
		$(".bankAccountSearch").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'NAME',   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.orders);
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
				suggestion: function(data) {
					return '<span><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			});
		
		/* To get the suggestion data for Currency */
		$("#CURRENCY").typeahead({
			hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CURRENCY',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_CURRENCY_DATA",
					CURRENCY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CURRENCYMST);
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
				suggestion: function(data) {
				return '<div><p onclick="getCurrencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
				}
			  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
		
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				{
				$("input[name ='CURRENCYID']").val("");
				$("input[name ='CURRENCYUSEQT']").val("1");
				}
		});
			
	 });
	
	
	 function checkAll(isChk) {
		 var len = document.form.appcheck.length;	
		 if(len == undefined) len = 1;  
	     if (document.form.appcheck)
	     {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1){
	              		document.form.appcheck.checked = isChk;
	               	}
	              	else{
	              		document.form.appcheck[i].checked = isChk;
	              	}
	        }
	    }
	    getcheckedids();
	}
	 
	 function isbankcharge(obj,acountname){
			var plant = document.form.plant.value;
			$.ajax({
				type : "POST",
				url : "/track/ChartOfAccountServlet",
				async : true,
				data : {
					PLANT : plant,
					action : "isbankcharge",
					accountname : acountname
				},
				dataType : "json",
				success : function(data) {
					if(data.status == "OK"){
						$('input[name ="pay_ischeque"]').val("1");
						$("input[name ='bankname']").val(acountname);
						$("#bankCharge").attr("readonly", false);
						$(".hidecheque").show();
					}else{
						$('input[name ="pay_ischeque"]').val("0");
						$("input[name ='bankname']").val("");
						$("input[name ='bank_charge']").val(parseFloat("0").toFixed(numberOfDecimal));
						$("#bankCharge").attr("readonly", true);
						$(".hidecheque").hide();
					}
				}
			});
		}
	 function getcheckedids(){
			var numberOfDecimal = document.getElementById("numberOfDecimal").value;
			var tamount = "0";
			$("input[name ='appcheck']").each(function() {
				if($(this).is(":checked")){
					var amount = $(this).closest('tr').find("input[name ='amount']").val();
					tamount = parseFloat(tamount) + parseFloat(amount);
			    }
			});
			var exrate = $("input[name ='CURRENCYUSEQT']").val();
			var examount = parseFloat(tamount)/parseFloat(exrate)
			$("input[name ='pay_netamount']").val(parseFloat(examount).toFixed(numberOfDecimal));
			$("input[name ='amount_paid_Curr']").val(parseFloat(tamount).toFixed(numberOfDecimal));
			$("input[name ='chequeamount']").val(parseFloat(examount).toFixed(numberOfDecimal));

		}
	 
	 function currencychanged(data)
	 {
	 	//calculatecurrency();
	 	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	 	var baseCurrency = data.value;
	 	//var amountnode = document.getElementById("amount_paid_Curr").value;
	 	var amountnode = $("input[name ='amount_paid_Curr']").val();
	 	
	 	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	 	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	 	
	 	$("input[name ='pay_netamount']").val(baseamount);
	 	$("input[name ='chequeamount']").val(baseamount);

	 }
	 
	 function validatePayment()
	 {
	 	
	 	var paymentDate = document.form.payment_date.value;
	 	var paymentThrough = document.form.paid_through_account_name.value;
	 	var paymentMode = document.form.payment_mode.value;
	 	
	 	var bankCharges = document.form.bank_charge.value;
	 	var Currency = document.form.CURRENCY.value;
	 	var pdastatus=$('input[name = "pay_ischeque"]').val();

	 	
	 	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	 	var CURREQT = document.form.CURRENCYUSEQT.value;
	 	
	 	if (CURREQT.indexOf('.') == -1) CURREQT += ".";
	  	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
	  	if (decNum.length > numberOfDecimal)
	  	{
	  		alert("Invalid more than "+numberOfDecimal+" digits after decimal in Exchange Rate");
	  		document.form.CURRENCYUSEQT.focus();
	  		return false;
	  		
	  	}
	 	
	 	if(paymentDate == ""){
	 		alert("Please enter payment date.");
	 		document.form.payment_date.focus();
	 		return false;
	 	}
	 	var amount_paid=$('input[name = "pay_netamount"]').val();
	 	if(!(parseFloat(amount_paid)>0))
	 		{
	 			alert("Please enter paid amount greater than zero");
	 			return false;
	 		}
	 	var amount_paid_Curr=$('input[name = "amount_paid_Curr"]').val();
	 	if(!(parseFloat(amount_paid_Curr)>0))
	 		{
	 			alert("Please enter paid amount greater than zero");
	 			return false;
	 		}
	 	if(Currency == ""){
	 		alert("Please select a Currency.");
	 		return false;
	 	}
	 	if(paymentThrough == ""){
	 		alert("Please choose paid through.");
	 		document.form.paid_through_account_name.focus();
	 		return false;
	 	}
	 	
	 	if(paymentMode == ""){
	 		alert("Please choose payment mode.");
	 		document.form.payment_mode.focus();
	 		return false;
	 	}

	 	if(pdastatus == "1"){

	 		if(cno == ""){
	 			alert("The cheque number field cannot be empty.");
	 			document.form.cheque-no.focus();
	 			return false;
	 		}
	 		

	 		if(cdate == ""){
	 			alert("The cheque date field cannot be empty.");
	 			document.form.cheque-date.focus();
	 			return false;
	 		}
	 		
	 		if(camount == ""){
	 			alert("The cheque amount field cannot be zero.");
	 			document.form.cheque-amount.focus();
	 			return false;
	 		}
	 		
	 		if(parseFloat(amount_paid) != parseFloat(camount)){
	 			alert("Cheque amount should be equal to Payment made");
	 			return false;
	 		}
	 	}
	 	
	 	$('.datepicker').prop('disabled',false);
	 	
	 	return true;

	 }
	 function checkpaymenttype(paytype){	
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PAYMENTMODE : paytype,
					ACTION : "PAYTYPE_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "99") {
							alert("Payment Mode Does't Exists");
							document.getElementById("payment_mode").focus();
							$("#payment_mode").typeahead('val', '');
							return false;	
						} 
						else 
							return true;
					}
				});
			 return true;
	}
	 function paymentTypeCallback(data)
	 {
	 	$("input[name ='payment_mode']").val(data.PAYMENTMODE);
	 } 
</script>

</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>