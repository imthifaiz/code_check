<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.dao.*"%>
<%@ page import="com.track.constants.MenuConstants"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%--New page design begin --%>
<%
String title = "Dashboard";
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%@include file="header.jsp"%>
<%--New page design end --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<%
//String company = "";
//company=request.getParameter(COMPANY);
//System.out.println("company....indexpage3.."+company);
String imageRootPath = "images/IndexPage/";
String tableWidth = "85%";
String tableWidthLi = "85%";
String rootURI = HttpUtils.getRootURI(request);
%>
<style>
.nav>li>a:hover, .nav>li>a:active, .nav>li>a:focus {
	color: #444;
	background: #f7f7f7;
}
</style>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/dashboard.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/core_main.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/daygrid_main.css">
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
StrUtils strUtils = new StrUtils();
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String systemnow = session.getAttribute("SYSTEMNOW").toString();
String defaultTab = "";
boolean accMgt = false, activeClass = true, activeSection = true;
if (systemnow.equalsIgnoreCase("payroll")) {
	accMgt = ub.isCheckValPay("homeaccountingmanaget", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	accMgt = ub.isCheckValAcc("homeaccountingmanaget", plant, username);
} else {
	accMgt = ub.isCheckValinv("homeaccountingmanaget", plant, username);
}

if (accMgt == true) {
	System.out.println("homeaccountingmanaget");
}

boolean acc = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	acc = ub.isCheckValPay("homeaccounting", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	acc = ub.isCheckValAcc("homeaccounting", plant, username);
} else {
	acc = ub.isCheckValinv("homeaccounting", plant, username);
}

if (acc == true) {
	System.out.println("homeaccounting");
}

boolean pur = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	pur = ub.isCheckValPay("homepurchase", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	pur = ub.isCheckValAcc("homepurchase", plant, username);
} else {
	pur = ub.isCheckValinv("homepurchase", plant, username);
}

if (pur == true) {
	System.out.println("homepurchase");
}

boolean sal = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	sal = ub.isCheckValPay("homesales", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	sal = ub.isCheckValAcc("homesales", plant, username);
} else {
	sal = ub.isCheckValinv("homesales", plant, username);
}

if (sal == true) {
	System.out.println("homesales");
}

boolean war = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	war = ub.isCheckValPay("homewarehouse", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	war = ub.isCheckValAcc("homewarehouse", plant, username);
} else {
	war = ub.isCheckValinv("homewarehouse", plant, username);
}

if (war == true) {
	System.out.println("homewarehouse");
}

boolean hpay = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	hpay = ub.isCheckValPay("homePayroll", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	hpay = ub.isCheckValAcc("homePayroll", plant, username);
} else {
	hpay = ub.isCheckValinv("homePayroll", plant, username);
}

if (hpay == true) {
	System.out.println("homePayroll");
}

boolean dpos = false;
if (systemnow.equalsIgnoreCase("payroll")) {
	dpos = ub.isCheckValPay("homepos", plant, username);
} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
	dpos = ub.isCheckValAcc("homepos", plant, username);
} else {
	dpos = ub.isCheckValinv("homepos", plant, username);
}

if (dpos == true) {
	System.out.println("homepos");
}


PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

EmployeeDAO employeeDAO = new EmployeeDAO();
String acemp = employeeDAO.getActiveEmployee(plant);
//acemp = StrUtils.addZeroes(Float.parseFloat(acemp), numberOfDecimal);
%>

<form name="form">
	<input name="numberOfDecimal" type="hidden"
		value="<%=numberOfDecimal%>"> <input name="plant"
		type="hidden" value="<%=plant%>"> <input name="baseCurrency"
		type="hidden" value="<%=session.getAttribute("BASE_CURRENCY")%>">
</form>

<section class="content dashboard">
	<ul class="nav nav-tabs">
		<%
		if (accMgt) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#accountMgtDashboard">Accounting
				Management</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (acc) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#accountsDashboard">Accounting</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (pur) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#purchaseDashboard">Purchase</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (sal) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#salesDashboard">Sales</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (war) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#warehouseDashboard">Warehouse</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (hpay) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#payrollDashboard">Payroll</a></li>
		<%
		activeClass = false;
		}
		%>
		<%
		if (dpos) {
		%>
		<li class="<%=(activeClass) ? "active" : ""%>"><a
			data-toggle="tab" href="#posDashboard">POS</a></li>
		<%
		activeClass = false;
		}
		%>
	</ul>

	<div class="tab-content">

		<div id="accountMgtDashboard"
			class="tab-pane fade<%=(accMgt) ? " in active" : ""%>">
			<div class="row">
				<br>
				<%-- <div class="col-xs-12 col-sm-3">
	      		<div class="dashboard-stats__item dashboard-stats__item_red">
	            	<span class="dashboard_heading">Total Asset</span>
	            	<div class="custom-select totalAssetSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	            	</div>
	              <i class="fa fa-home"></i>
	              <h3 class="dashboard-stats__title">
	              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
	                <span id="totalAsset" data-decimals="<%=numberOfDecimal%>"></span>
	              </h3>
	            </div>
      		</div>
      		
      		<div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Total Liability</span>
            	<div class="custom-select totalLiabilitySel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-balance-scale"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="totalLiability" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div> --%>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Cash In Hand</span>
						<!-- <div class="custom-select totalCashInHandSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	            	</div> -->
						<i class="fa fa-home"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="totalCashInHand" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Cash At Bank</span>
						<!-- <div class="custom-select totalCashAtBankSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div> -->
						<i class="fa fa-balance-scale"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="totalCashAtBank" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_light-blue">
						<span class="dashboard_heading">Net Profit</span>
						<div class="custom-select netProfitSel">
							<select id="netProfitRange" style="width: 100%;">
								<option selected="selected">Today
								<option>
								<option>Today</option>
								<option>Yesterday</option>
								<option>Last 7 days</option>
								<option>This week</option>
								<option>Last 15 days</option>
								<option>This month</option>
								<option>Last 30 days</option>
								<option>Last month</option>
								<option>This quarter</option>
								<option>Last quarter</option>
								<option>This year</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-line-chart"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="netProfit" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_purple">
						<span class="dashboard_heading">Gross Profit</span>
						<div class="custom-select grossProfitSel">
							<select id="grossProfitRange" style="width: 100%;">
								<option selected="selected">Today
								<option>
								<option>Today</option>
								<option>Yesterday</option>
								<option>Last 7 days</option>
								<option>This week</option>
								<option>Last 15 days</option>
								<option>This month</option>
								<option>Last 30 days</option>
								<option>Last month</option>
								<option>This quarter</option>
								<option>Last quarter</option>
								<option>This year</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-money"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="grossProfit" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Total Purchases <span id="totalPurchaseByBillSumryForMgt"></span>
							</h3>
							<div class="custom-select totalPurchaseByBillSumrySelForMgt">
								<select id="totalPurchaseByBillSumryFromJournalRange" style="width: 100%;">
									<!-- <option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option> -->
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas17"></canvas>
						</div>
					</div>
				</div>


				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Purchase Summary <span id="totalpurchaseSummaryForMgt"></span>
							</h3>
							<div class="custom-select purchaseSummarySelForMgt">
								<select name="purtableselMgt" id="purtableselMgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnameMgt" placeholder="SUPPLIER" name="vendnameMgt">
										<span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnameMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" name="purstatusMgt" id="purstatusMgt"
											class="ac-selected form-control purstatusMgt"
											placeholder="STATUS"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'purstatusMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePurchaseSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Reference</th>
													<th>Supplier</th>
													<th>Status</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Total Sales <span id="totalSalesByInvoiceSumryForMgt"></span>
							</h3>
							<div class="custom-select totalSalesByInvoiceSumrySelForMgt">
								<select id="totalSalesSelMgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas18"></canvas>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Sales Summary <span id="totalSalesSummaryForMgt"></span>
							</h3>
							<div class="custom-select salesSummarySelForMgt">
								<select name="saltableselmgt" id="saltableselmgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnameMgt" placeholder="CUSTOMER" name="custnameMgt">
										<span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnameMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" name="salesstatusMgt" id="salesstatusMgt"
											class="ac-selected form-control salesstatusMgt"
											placeholder="STATUS"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'salesstatusMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableSalesSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Reference</th>
													<th>Customer</th>
													<th>Status</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Total Income <span id="totalIncomeForMgt"></span>
							</h3>
							<div class="custom-select totalIncomeForMgtSel">
								<select id="totalIncomeMgtRange" style="width: 100%;">
									<!-- <option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option> -->
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas19"></canvas>
							<h5 class="totalIncomeMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Income were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Total Income</h3>
							<div class="custom-select totalIncomeByInvoiceForMgtSumrySel">
								<select id="totalIncomeMgt" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas20"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Income Summary <span id="totalincomeSummaryForMgt"></span>
							</h3>
							<div class="custom-select incomeSummarySelForMgt">
								<select name="incometableselmgt" id="incometableselmgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountSearchMgt" placeholder="ACCOUNT NAME"
											name="accountSearchMgt"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountSearchMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnameIncomeMgt" placeholder="CUSTOMER"
											name="custnameIncomeMgt"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnameIncomeMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableIncomeSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer/Supplier</th>
													<th>Account Name</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Total Expense <span id="totalExpenseForMgt"></span>
							</h3>
							<div class="custom-select totalExpenseForMgtSel">
								<select id="totalExpenseMgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas21"></canvas>
							<h5 class="totalExpenseMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Expense were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Total Expense</h3>
							<div class="custom-select totalExpenseByBillSumryForMgtSel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas22"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Expense Summary <span id="totalexpenseSummaryForMgt"></span>
							</h3>
							<div class="custom-select expenseSummarySelForMgt">
								<select name="expensetableselmgt" id="expensetableselmgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountExpSearchMgt" placeholder="ACCOUNT NAME"
											name="accountExpSearchMgt"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountExpSearchMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnameExpenseMgt" placeholder="SUPPLIER"
											name="vendnameExpenseMgt"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnameExpenseMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableExpenseSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer/Supplier</th>
													<th>Account Name</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>

			<!-- ---------------payment made-------------------->
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Total Payment Issued <span id="totalPaymentMadeMgt"></span>
							</h3>
							<div class="custom-select totalPaymentMadeMgtSel">
								<select id="totalPaymentIssuedMgtRange" style="width: 100%;">
									<!-- <option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option> -->
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvaspaymadepi"></canvas>
							<h5 class="totalPaymentMadeMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Payment Issued were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Total Payment Made</h3>
							<div class="custom-select totalPaymentMadeSumryForMgtSel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvasPayMadeMgtBar"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Payment Issued Summary <span id="PaymentIssuedSummaryForMgt"></span>
							</h3>
							<div class="custom-select PaymentIssuedSummarySelForMgt">
								<select name="PaymentIssuedtableselmgt" id="PaymentIssuedtableselmgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountPayIssuedSearchMgt" placeholder="ACCOUNT NAME"
											name="accountPayIssuedSearchMgt"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountPayIssuedSearchMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnamePayIssueenseMgt" placeholder="SUPPLIER"
											name="vendnamePayIssueenseMgt"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnamePayIssueenseMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePayIssueSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Supplier-Account Name</th>
													<th>Paid Through</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>




				</div>
			</div>
			<!-- ----------------------------------->
			<!-- ---------------payment made Receipt------------------->
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Total Payment Receipt <span id="totalPaymentReceiptMgt"></span>
							</h3>
							<div class="custom-select totalPaymentReceiptMgtSel">
								<select id="totalPaymentReceiptMgtRange" style="width: 100%;">
									<!-- <option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option> -->
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvaspayReceiptpi"></canvas>
							<h5 class="totalPaymentReceiptMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Payment Receipt were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Total Receipt Receipt</h3>
							<div class="custom-select totalPaymentReceiptSumryForMgtSel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvasPayReceiptMgtBar"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Payment Receipt Summary <span id="PaymentReceiptSummaryForMgt"></span>
							</h3>
							<div class="custom-select PaymentReceiptSummarySelForMgt">
								<select name="PaymentReceipttableselmgt" id="PaymentReceipttableselmgtRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountPayReceiptSearchMgt" placeholder="ACCOUNT NAME"
											name="accountPayReceiptSearchMgt"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountPayReceiptSearchMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnamePayRecpMgt" placeholder="CUSTOMER"
											name="custnamePayRecpMgt"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnamePayRecpMgt\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePayReceiptSummaryForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer-Account Name</th>
													<th>Deposit To</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>


				</div>
			</div>
			<!-- ----------------------------------->

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="col-xs-12 no-padding">
						<div class="panel panel-default account-panel">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">
									Total Account Payable <span id="totalAccPayForMgt"></span>
								</h3>
								<div class="custom-select totalAccPaySelForMgt">
									<select id="totalAccountPayableMgtRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<canvas id="canvas23"></canvas>
								<h5 class="totalAccPayMsg"
									style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
									No Account Payable were found in this time frame</h5>
							</div>
						</div>
					</div>

					<div class="col-xs-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">
									Account Payable <span id="accPayForMgt"></span>
								</h3>
								<div class="custom-select accPaySelForMgt">
									<select id="accountPayableMgtRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-xs-12" style="margin-bottom: 2%;">
										<div class="col-sm-6 ac-box">
											<input type="text" class="ac-selected form-control typeahead"
												id="vendnameaccPay" placeholder="SUPPLIER"
												name="vendnameaccPay"> <span class="select-icon"
												onclick="$(this).parent().find('input[name=\'vendnameaccPay\']').focus()">
												<i class="glyphicon glyphicon-menu-down"></i>
											</span>
										</div>
										<div class="col-sm-6 ac-box"></div>
									</div>
									<div class="col-xs-12">
										<div class="table-responsive">
											<table id="tableAccPayForMgt"
												class="table table-bordred table-striped">
												<thead>
													<tr>
														<th>Supplier</th>
														<th>Total Amount Due</th>
														<th>Over Due</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
									<!-- <div class="col-xs-12">
								<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
								</div>
								<span>NON PDC PAYMENT</span>
								&nbsp;&nbsp;
								<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
								</div>
								<span>PDC PAYMENT</span>
							</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="col-sm-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">Supplier Ageing Summary</h3>
							</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table id="tableSupAgeForMgt"
										class="table table-bordred table-striped table-condensed">
										<thead>
											<tr>
												<th>Supplier</th>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>31-60 days</th>
												<th>61-90 days</th>
												<th>90+ days</th>
											</tr>
										</thead>
									</table>

									<table id="tableSupAgeTotal"
										class="table table-bordred table-striped table-condensed tableSupAgeTotal">
										<thead>
											<tr>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>30-60 days</th>
												<th>60-90 days</th>
												<th>90 &amp; above</th>
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
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="col-xs-12 no-padding">
						<div class="panel panel-default account-panel">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">
									Total Account Receivable <span id="totalAccRecvForMgt"></span>
								</h3>
								<div class="custom-select totalAccRecvForMgtSel">
									<select id="totalAccountReceivaleMgtRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<canvas id="canvas24"></canvas>
								<h5 class="totalAccRecvMsg"
									style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
									No Account Receivable were found in this time frame</h5>
							</div>
						</div>
					</div>

					<div class="col-xs-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">
									Account Receivable <span id="accRecvForMgt"></span>
								</h3>
								<div class="custom-select accRecvSelForMgt">
									<select id="accountReceivableMgtRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-xs-12" style="margin-bottom: 2%;">
										<div class="col-sm-6 ac-box">
											<input type="text"
												class="ac-selected  form-control typeahead"
												id="custnameaccRecv" placeholder="CUSTOMER"
												name="custnameaccRecv"> <span class="select-icon"
												onclick="$(this).parent().find('input[name=\'custnameaccRecv\']').focus()"><i
												class="glyphicon glyphicon-menu-down"></i></span>
										</div>
										<div class="col-sm-6 ac-box"></div>
									</div>
									<div class="col-xs-12">
										<div class="table-responsive">
											<table id="tableRecvPayForMgt"
												class="table table-bordred table-striped">
												<thead>
													<tr>
														<th>Customer</th>
														<th>Total Amount Due</th>
														<th>Over Due</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
									<!-- <br/>
								<div class="col-xs-12">
									<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
									</div>
									<span>NON PDC PAYMENT</span>
									&nbsp;&nbsp;
									<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
									</div>
									<span>PDC PAYMENT</span>
								</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6">
					<div class="col-sm-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">Customer Ageing Summary</h3>
							</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table id="tablecustAgeForMgt"
										class="table table-bordred table-striped">
										<thead>
											<tr>
												<th>Customer</th>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>31-60 days</th>
												<th>61-90 days</th>
												<th>90+ days</th>
											</tr>
										</thead>
									</table>
									<table id="tableCusAgeTotal"
										class="table table-bordred table-striped table-condensed tableCusAgeTotal">
										<thead>
											<tr>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>30-60 days</th>
												<th>60-90 days</th>
												<th>90 &amp; above</th>
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
			</div>

			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">PDC Issued</h3>
							<div class="custom-select payPdcSelForMgt">
								<select id="PDCIssuedRange" style="width: 100%;">
									<option selected="selected">Next 7 days
									<option>
									<option>Next 7 days</option>
									<option>Next 15 days</option>
									<option>Next 30 days</option>
									<option>&gt; 30 days</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnamepayPdc" placeholder="SUPPLIER"
											name="vendnamepayPdc"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnamepayPdc\']').focus()">
											<i class="glyphicon glyphicon-menu-down"></i>
										</span>
									</div>
									<div class="col-sm-6 ac-box"></div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePaymentPdcForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Payment ID</th>
													<th>Payment Date</th>
													<th>Supplier</th>
													<th>Bank</th>
													<th>Cheque No</th>
													<th>Cheque Date</th>
													<th>Cheque Reversed Date</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
										<br>
										<button class="btn btn-sm btn-default pull-right"
											onclick="redirectToPaymentPdcSummary()"
											style="color: #337ab7;">Process Payment</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">PDC Received</h3>
							<div class="custom-select payRecvPdcForMgtSel">
								<select id="PDCReceivedRange" style="width: 100%;">
									<option selected="selected">Next 7 days
									<option>
									<option>Next 7 days</option>
									<option>Next 15 days</option>
									<option>Next 30 days</option>
									<option>&gt; 30 days</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnamepayRecvPdc" placeholder="CUSTOMER"
											name="custnamepayRecvPdc"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnamepayRecvPdc\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box"></div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePaymentRecvPdcForMgt"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Payment ID</th>
													<th>Payment Date</th>
													<th>Customer</th>
													<th>Bank</th>
													<th>Cheque No</th>
													<th>Cheque Date</th>
													<th>Cheque Reversed Date</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
										<br>
										<button class="btn btn-sm btn-default pull-right"
											onclick="redirectToPaymentReceivePdcSummary()"
											style="color: #337ab7;">Process Payment</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
		if (accMgt) {
			activeSection = false;
			defaultTab = "accountMgtDashboard";
		}
		%>

		<div id="accountsDashboard"
			class="tab-pane fade<%=(acc && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<div class="col-xs-12  col-sm-offset-1 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Total Purchases</span>
						<div class="custom-select totalPurchaseByBillSel">
							<select id="TotalPurchasesRange" style="width: 100%;">
								<option selected="selected">Today
								<option>
								<option>Today</option>
								<option>Yesterday</option>
								<option>Last 7 days</option>
								<option>This week</option>
								<option>Last 15 days</option>
								<option>This month</option>
								<option>Last 30 days</option>
								<option>Last month</option>
								<option>This quarter</option>
								<option>Last quarter</option>
								<option>This year</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-money"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="total_purchase_by_bill"
								data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12  col-sm-offset-2 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Total Sales</span>
						<div class="custom-select totalSalesByInvoiceSel">
							<select id="TotalSalesRange" style="width: 100%;">
								<option selected="selected">Today
								<option>
								<option>Today</option>
								<option>Yesterday</option>
								<option>Last 7 days</option>
								<option>This week</option>
								<option>Last 15 days</option>
								<option>This month</option>
								<option>Last 30 days</option>
								<option>Last month</option>
								<option>This quarter</option>
								<option>Last quarter</option>
								<option>This year</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-truck fa-rotate-180"
							style="transform: rotate(180deg) scaleY(-1);"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="total_sales_by_invoice"
								data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Total Purchases</h3>
							<div class="custom-select totalPurchaseByBillSumrySel">
								<select id="TotalPurchasesBillRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas9"></canvas>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Purchase Summary <span id="totalpurchaseSummary"></span>
							</h3>
							<div class="custom-select purchaseSummarySel">
								<select id="PurchaseSummaryRange" name="purtablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendname" placeholder="SUPPLIER" name="vendname">
										<span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" name="purstatus" id="purstatus"
											class="ac-selected form-control purstatus"
											placeholder="STATUS"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'purstatus\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePurchaseSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Reference</th>
													<th>Supplier</th>
													<th>Status</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>




			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Total Sales</h3>
							<div class="custom-select totalSalesByInvoiceSumrySel">
								<select id="TotalSalesInvoiceRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas10"></canvas>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Sales Summary <span id="totalSalesSummary"></span>
							</h3>
							<div class="custom-select salesSummarySel">
								<select id="SalesSummaryRange" name="saltablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custname" placeholder="CUSTOMER" name="custname">
										<span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custname\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" name="salesstatus" id="salesstatus"
											class="ac-selected form-control salesstatus"
											placeholder="STATUS"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'salesstatus\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableSalesSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Reference</th>
													<th>Customer</th>
													<th>Status</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Total Income <span id="totalIncome"></span>
							</h3>
							<div class="custom-select totalIncomeSel">
								<select id="totalIncomeSummaryRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas11"></canvas>
							<h5 class="totalIncomeMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Income were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Total Income</h3>
							<div class="custom-select totalIncomeByInvoiceSumrySel">
								<select id="totalIncomeSummaryRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas13"></canvas>
						</div>
					</div>


					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Income Summary <span id="totalincomeSummary"></span>
							</h3>
							<div class="custom-select incomeSummarySel">
								<select id="incomeSummaryRange" name="incometablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountSearch" placeholder="ACCOUNT NAME"
											name="accountSearch"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountSearch\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnameIncome" placeholder="CUSTOMER"
											name="custnameIncome"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnameIncome\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableIncomeSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer/Supplier</th>
													<th>Account Name</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>



				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Total Expense <span id="totalExpense"></span>
							</h3>
							<div class="custom-select totalExpenseSel">
								<select id="totalExpenseRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas12"></canvas>
							<h5 class="totalExpenseMsg"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Expense were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Total Expense</h3>
							<div class="custom-select totalExpenseByBillSumrySel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas14"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Expense Summary <span id="totalexpenseSummary"></span>
							</h3>
							<div class="custom-select expenseSummarySel">
								<select id="expenseSummaryRange" name="expensetablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountExpSearch" placeholder="ACCOUNT NAME"
											name="accountExpSearch"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountExpSearch\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnameExpense" placeholder="SUPPLIER"
											name="vendnameExpense"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnameExpense\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tableExpenseSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer/Supplier</th>
													<th>Account Name</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>


				</div>
			</div>

			<!-- ---------------payment made account-------------------->
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Total Payment Issued <span id="totalPaymentMade"></span>
							</h3>
							<div class="custom-select totalPaymentMadeSel">
								<select id="totalPaymentIssuedRange" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvaspaymadepiAcc"></canvas>
							<h5 class="totalPaymentMadeMsgAcc"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Payment Issued were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Total Payment Made</h3>
							<div class="custom-select totalPaymentMadeSumrySel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvasPayMadeBar"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Payment Issued Summary <span id="PaymentIssuedSummary"></span>
							</h3>
							<div class="custom-select PaymentIssuedSummarySel">
								<select id="paymentIssuedSummaryRange" name="PaymentIssuedtablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountPayIssuedSearch" placeholder="ACCOUNT NAME"
											name="accountPayIssuedSearch"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountPayIssuedSearch\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendnamePayIssueense" placeholder="SUPPLIER"
											name="vendnamePayIssueense"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendnamePayIssueense\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePayIssueSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Supplier-Account Name</th>
													<th>Paid Through</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>


				</div>
			</div>
			<!-- ----------------------------------->

			<!-- ---------------payment Receipt account-------------------->
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default" style="min-height: 319px;">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">
								Total Payment Receipt <span id="totalPaymentReceipt"></span>
							</h3>
							<div class="custom-select totalPaymentReceiptSel">
								<select id="TotalPaymentReceiptRange"style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvaspayReceiptpiAcc"></canvas>
							<h5 class="totalPaymentReceiptMsgAcc"
								style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
								No Payment Receipt were found in this time frame</h5>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default account-panel" hidden>
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Total Payment Receipt</h3>
							<div class="custom-select totalPaymentReceiptSumrySel">
								<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvasPayReceiptBar"></canvas>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">
								Payment Receipt Summary <span id="PaymentReceiptSummary"></span>
							</h3>
							<div class="custom-select PaymentReceiptSummarySel">
								<select id="paymentReceiptSummaryRange" name="PaymentReceipttablesel" style="width: 100%;">
									<option selected="selected">Today
									<option>
									<option>Today</option>
									<option>Yesterday</option>
									<option>Last 7 days</option>
									<option>This week</option>
									<option>Last 15 days</option>
									<option>This month</option>
									<option>Last 30 days</option>
									<option>Last month</option>
									<option>This quarter</option>
									<option>Last quarter</option>
									<option>This year</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="accountPayReceiptSearch" placeholder="ACCOUNT NAME"
											name="accountPayReceiptSearch"> <span
											class="select-icon"
											onclick="$(this).parent().find('input[name=\'accountPayReceiptSearch\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custnamePayRecp" placeholder="CUSTOMER"
											name="custnamePayRecp"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custnamePayRecp\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePayReceiptSummary"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Date</th>
													<th>Customer-Account Name</th>
													<th>Deposit To</th>
													<th>Reference</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>


				</div>
			</div>
			<!-- ----------------------------------->

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="col-xs-12 no-padding">
						<div class="panel panel-default account-panel">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">
									Total Account Payable <span id="totalAccPay"></span>
								</h3>
								<div class="custom-select totalAccPaySel">
									<select id="totalAccountPayableRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<canvas id="canvas15"></canvas>
								<h5 class="totalAccPayMsg"
									style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
									No Account Payable were found in this time frame</h5>
							</div>
						</div>
					</div>

					<div class="col-xs-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">
									Account Payable <span id="accPay"></span>
								</h3>
								<div class="custom-select accPaySel">
									<select id="AccountPayableRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-xs-12" style="margin-bottom: 2%;">
										<div class="col-sm-6 ac-box">
											<input type="text" class="ac-selected form-control typeahead"
												id="vendNameAccPay" placeholder="SUPPLIER"
												name="vendNameAccPay"> <span class="select-icon"
												onclick="$(this).parent().find('input[name=\'vendNameAccPay\']').focus()">
												<i class="glyphicon glyphicon-menu-down"></i>
											</span>
										</div>
										<div class="col-sm-6 ac-box"></div>
									</div>
									<div class="col-xs-12">
										<div class="table-responsive">
											<table id="tableAccPay"
												class="table table-bordred table-striped">
												<thead>
													<tr>
														<th>Supplier</th>
														<th>Total Amount Due</th>
														<th>Over Due</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
									<!-- <div class="col-xs-12">
							<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
							</div>
							<span>NON PDC PAYMENT</span>
							&nbsp;&nbsp;
							<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
							</div>
							<span>PDC PAYMENT</span>
						</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6">
					<div class="col-sm-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">Supplier Ageing Summary</h3>
							</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table id="tableSupAge"
										class="table table-bordred table-striped">
										<thead>
											<tr>
												<th>Supplier</th>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>31-60 days</th>
												<th>61-90 days</th>
												<th>90+ days</th>
											</tr>
										</thead>
									</table>
									<table id="tableSupAgeTotal"
										class="table table-bordred table-striped table-condensed tableSupAgeTotal">
										<thead>
											<tr>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>30-60 days</th>
												<th>60-90 days</th>
												<th>90 &amp; above</th>
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
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="col-xs-12 no-padding">
						<div class="panel panel-default account-panel">
							<div class="panel-heading dashboard-stats__item_purple">
								<h3 class="panel-title">
									Total Account Receivable <span id="totalAccRecv"></span>
								</h3>
								<div class="custom-select totalAccRecvSel">
									<select id="totalAccountReceivableRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<canvas id="canvas16"></canvas>
								<h5 class="totalAccRecvMsg"
									style="position: absolute; width: 100%; top: 50%; text-align: center; right: 0;">
									No Account Receivable were found in this time frame</h5>
							</div>
						</div>
					</div>

					<div class="col-xs-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">
									Account Receivable <span id="accRecv"></span>
								</h3>
								<div class="custom-select accRecvSel">
									<select id="accountReceivableRange" style="width: 100%;">
										<option selected="selected">Next 7 days
										<option>
										<option>Next 7 days</option>
										<option>Next 15 days</option>
										<option>Next 30 days</option>
										<option>&gt; 30 days</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-xs-12" style="margin-bottom: 2%;">
										<div class="col-sm-6 ac-box">
											<input type="text"
												class="ac-selected  form-control typeahead"
												id="custNameAccRecv" placeholder="CUSTOMER"
												name="custNameAccRecv"> <span class="select-icon"
												onclick="$(this).parent().find('input[name=\'custNameAccRecv\']').focus()"><i
												class="glyphicon glyphicon-menu-down"></i></span>
										</div>
										<div class="col-sm-6 ac-box"></div>
									</div>
									<div class="col-xs-12">
										<div class="table-responsive">
											<table id="tableRecvPay"
												class="table table-bordred table-striped">
												<thead>
													<tr>
														<th>Customer</th>
														<th>Total Amount Due</th>
														<th>Over Due</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
									<!-- <br/>
								<div class="col-xs-12">
									<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
									</div>
									<span>NON PDC PAYMENT</span>
									&nbsp;&nbsp;
									<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
									</div>
									<span>PDC PAYMENT</span>
								</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6">
					<div class="col-sm-12 no-padding">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">Customer Ageing Summary</h3>
							</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table id="tablecustAge"
										class="table table-bordred table-striped tableCusAgeTotal">
										<thead>
											<tr>
												<th>Customer</th>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>31-60 days</th>
												<th>61-90 days</th>
												<th>90+ days</th>
											</tr>
										</thead>
									</table>
									<table id="tableCusAgeTotal"
										class="table table-bordred table-striped table-condensed tableCusAgeTotal">
										<thead>
											<tr>
												<th>Total O/s</th>
												<th>Not due</th>
												<th>1-30 days</th>
												<th>30-60 days</th>
												<th>60-90 days</th>
												<th>90 &amp; above</th>
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
			</div>

			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">PDC Issued</h3>
							<div class="custom-select payPdcSel">
								<select id="pdcIssuedRange" style="width: 100%;">
									<option selected="selected">Next 7 days
									<option>
									<option>Next 7 days</option>
									<option>Next 15 days</option>
									<option>Next 30 days</option>
									<option>&gt; 30 days</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected form-control typeahead"
											id="vendNamePayPdc" placeholder="SUPPLIER"
											name="vendNamePayPdc"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'vendNamePayPdc\']').focus()">
											<i class="glyphicon glyphicon-menu-down"></i>
										</span>
									</div>
									<div class="col-sm-6 ac-box"></div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePaymentPdc"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Payment ID</th>
													<th>Payment Date</th>
													<th>Supplier</th>
													<th>Bank</th>
													<th>Cheque No</th>
													<th>Cheque Date</th>
													<th>Cheque Reversed Date</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
										<br>
										<button class="btn btn-sm btn-default pull-right"
											onclick="redirectToPaymentPdcSummary()"
											style="color: #337ab7;">Process Payment</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">PDC Received</h3>
							<div class="custom-select payRecvPdcSel">
								<select id="pdcReceivedRange" style="width: 100%;">
									<option selected="selected">Next 7 days
									<option>
									<option>Next 7 days</option>
									<option>Next 15 days</option>
									<option>Next 30 days</option>
									<option>&gt; 30 days</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12" style="margin-bottom: 2%;">
									<div class="col-sm-6 ac-box">
										<input type="text" class="ac-selected  form-control typeahead"
											id="custNamePayRecvPdc" placeholder="CUSTOMER"
											name="custNamePayRecvPdc"> <span class="select-icon"
											onclick="$(this).parent().find('input[name=\'custNamePayRecvPdc\']').focus()"><i
											class="glyphicon glyphicon-menu-down"></i></span>
									</div>
									<div class="col-sm-6 ac-box"></div>
								</div>
								<div class="col-xs-12">
									<div class="table-responsive">
										<table id="tablePaymentRecvPdc"
											class="table table-bordred table-striped">
											<thead>
												<tr>
													<th>Payment ID</th>
													<th>Payment Date</th>
													<th>Customer</th>
													<th>Bank</th>
													<th>Cheque No</th>
													<th>Cheque Date</th>
													<th>Cheque Reversed Date</th>
													<th>Amount</th>
												</tr>
											</thead>
										</table>
										<br>
										<button class="btn btn-sm btn-default pull-right"
											onclick="redirectToPaymentReceivePdcSummary()"
											style="color: #337ab7;">Process Payment</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<%
		if (acc) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "accountsDashboard" : defaultTab;
		}
		%>


		<div id="purchaseDashboard"
			class="tab-pane fade<%=(pur && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<div class="col-xs-12 col-sm-offset-1 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Total Purchases</span>
						<div class="custom-select totalPurchaseSel">
							<select id="purchaseRange" style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-money"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="total_receipt" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>


				<div class="col-xs-12 col-sm-offset-2 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Number Of Product Purchased</span>
						<div class="custom-select NoItemPurSel">
							<select id="purchaseQuantityRange" style="width: 100%;"
								onchange="{getTotals(this.value);}">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-shopping-basket"></i>
						<h3 class="dashboard-stats__title">
							<span id="total_recv_qty" data-decimals="3"></span>
						</h3>
					</div>
				</div>

				<!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
			</div>

			<div class="row">

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Purchase Summary</h3>
							<div class="custom-select PurSumrySel">
								<select id="purchaseSummaryRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas"></canvas>
							<button class="btn btn-sm btn-default pull-right"
								onclick="redirectToPurchaseSummary()" style="color: #337ab7;">See
								More</button>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Top Suppliers</h3>
							<div class="custom-select TopSupSel">
								<select id="topSuppliersRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas2"></canvas>
						</div>
					</div>
				</div>

			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Stock Replenishment Products</h3>
						</div>
						<div class="panel-body">
							<div class="table-responsive" style="overflow-y: hidden;">
								<table class="table table-striped no-margin stkReplshPrd">
									<thead>
										<tr>
											<th>Product</th>
											<th>Description</th>
											<th>Record Point</th>
											<th colspan="2" class="text-center">Stock On Hand</th>
											<th>UOM</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">New Suppliers</h3>
						</div>
						<div class="panel-body">
							<div>
								<h4
									style="text-align: center; font-weight: 600; font-size: 14px;">Number
									of Suppliers</h4>
								<h3 id="total_suppliers"
									style="text-align: center; font-weight: 600; margin-top: 0px; color: #4e9251;">150</h3>
								<hr>
							</div>
							<table id="newSuppliers" class="table table-striped no-margin">
								<thead>
									<tr>
										<th>Supplier ID</th>
										<th>Supplier Name</th>
										<th>Contact Name</th>
										<th>Mobile Number</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
								<tfoot>
									<tr>
										<td colspan="5"><button
												class="btn btn-sm btn-default pull-right"
												onclick="window.location.href='<%=rootURI%>/jsp/vendorSummary.jsp'"
												style="color: #337ab7;">See More</button></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">PO Delivery Dates</h3>
						</div>
						<div class="panel-body">
							<div id='calendar'></div>
						</div>
					</div>
				</div>

				<div class="col-sm-6"></div>
			</div>
		</div>
		<%
		if (pur) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "purchaseDashboard" : defaultTab;
		}
		%>
		<div id="salesDashboard"
			class="tab-pane fade<%=(sal && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<div class="col-xs-12 col-sm-offset-1 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Total Sales</span>
						<div class="custom-select totalSalesSel">
							<select id="salesRange" style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-money"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="total_issue" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>


				<div class="col-xs-12 col-sm-offset-2 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Number Of Product Sold</span>
						<div class="custom-select NoItemSaleSel">
							<select id="salesQtyRange" style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-truck fa-rotate-180"
							style="transform: rotate(180deg) scaleY(-1);"></i>
						<h3 class="dashboard-stats__title">
							<span id="total_sales_qty" data-decimals="3"></span>
						</h3>
					</div>
				</div>

				<!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
			</div>
			<div class="row">

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Sales Summary</h3>
							<div class="custom-select saleSumrySel">
								<select id="salesSummaryRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas3"></canvas>
							<button class="btn btn-sm btn-default pull-right"
								onclick="redirectToSalesSummary()" style="color: #337ab7;">See
								More</button>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Top Customers</h3>
							<div class="custom-select TopCustSel">
								<select id="topCustomersRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas4"></canvas>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Top Sales Product</h3>
							<div class="custom-select TopSalPrdSel">
								<select id="topSalesRange" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas5"></canvas>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">New Customers</h3>
						</div>
						<div class="panel-body">
							<div>
								<h4
									style="text-align: center; font-weight: 600; font-size: 14px;">Number
									of Customers</h4>
								<h3 id="total_customers"
									style="text-align: center; font-weight: 600; margin-top: 0px; color: #4e9251;">150</h3>
								<hr>
							</div>
							<table id="newcustomers" class="table table-striped no-margin">
								<thead>
									<tr>
										<th>Customer ID</th>
										<th>Customer Name</th>
										<th>Contact Name</th>
										<th>Mobile Number</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
								<tfoot>
									<tr>
										<td colspan="5"><button
												class="btn btn-sm btn-default pull-right"
												onclick="window.location.href='<%=rootURI%>/jsp/custmerSummary.jsp'"
												style="color: #337ab7;">See More</button></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>

			</div>

			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Sales Order Delivery Dates</h3>
						</div>
						<div class="panel-body">
							<div id='calendar4'></div>
						</div>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Sales Estimate Order Pending</h3>
						</div>
						<div class="panel-body">
							<div id='calendar3'></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
		if (sal) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "salesDashboard" : defaultTab;
		}
		%>
		<div id="warehouseDashboard"
			class="tab-pane fade<%=(war && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<div class="col-xs-12 col-sm-offset-1 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Total Received Items</span>
						<div class="custom-select NoRcvItemSel">
							<select id="receivedItemsRange" style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-shopping-basket"></i>
						<h3 class="dashboard-stats__title">
							<span id="total_items_recv_qty" data-decimals="3"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-offset-2 col-sm-4">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Total Issued Items</span>
						<div class="custom-select NoItemIssueSel">
							<select id="TotalIssuedRage" style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa  fa-truck fa-rotate-180"
							style="transform: rotate(180deg) scaleY(-1);"></i>
						<h3 class="dashboard-stats__title">
							<span id="total_items_issue_qty" data-decimals="3"></span>
						</h3>
					</div>
				</div>
				<!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">PO Without Price Summary</h3>
							<div class="custom-select PoWopriceSel">
								<select id="POWithoutPrice" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas6"></canvas>
							<button class="btn btn-sm btn-default pull-right"
								onclick="redirectToPurchaseWOPSummary()" style="color: #337ab7;">See
								More</button>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">GRN Summary</h3>
							<div class="custom-select grnSel">
								<select id="GRNSummary" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas7"></canvas>
							<button class="btn btn-sm btn-default pull-right"
								onclick="redirectToGrnSummary()" style="color: #337ab7;">See
								More</button>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Stock Replenishment Products</h3>
						</div>
						<div class="panel-body">
							<div class="table-responsive" style="overflow-y: hidden;">
								<table class="table table-striped no-margin stkReplshPrd">
									<thead>
										<tr>
											<th>Product</th>
											<th>Description</th>
											<th>Record Point</th>
											<th colspan="2" class="text-center">Stock On Hand</th>
											<th>UOM</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">GI Summary</h3>
							<div class="custom-select griSel">
								<select id="GISummary" style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="canvas8"></canvas>
							<button class="btn btn-sm btn-default pull-right"
								onclick="redirectToGiSummary()" style="color: #337ab7;">See
								More</button>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">PO Delivery Dates</h3>
						</div>
						<div class="panel-body">
							<div id='calendar5'></div>
						</div>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Sales Order Delivery Date</h3>
						</div>
						<div class="panel-body">
							<div id='calendar6'></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green2">
							<h3 class="panel-title">Products Ready To Pack From Sales
								Order</h3>
							<div class="custom-select rtpSel">
								<select id="ProductsReadyToPackFromSalesOrder" style="width: 100%;">
									<option selected="selected">Today</option>
									<option>Today</option>
									<option>This week</option>
									<option>This month</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="table-responsive">
								<table id="tableReadtToPackProducts"
									class="table table-bordred table-striped">
									<thead>
										<tr>
											<th>Order No</th>
											<th>Customer Name</th>
											<th>Line No</th>
											<th>Order Date</th>
											<th>UOM</th>
											<th>Total Qty</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>

				<div class="col-sm-6">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Expiring Products</h3>
							<div class="custom-select expPrdSel">
								<select id="ExpiringProducts" style="width: 100%;">
									<option selected="selected">Today</option>
									<option>Today</option>
									<option>This week</option>
									<option>This month</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<div class="table-responsive">
								<table id="tableExpProducts"
									class="table table-bordred table-striped" style="width: 100%">
									<thead>
										<tr>
											<th>Product</th>
											<th>Location</th>
											<th>Batch</th>
											<th>Expiry Date</th>
											<th>Quantity</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
		if (war) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "warehouseDashboard" : defaultTab;
		}
		%>
		
		<div id="posDashboard"
			class="tab-pane fade<%=(dpos && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<!-- <div class="col-xs-12 col-sm-offset-1 col-sm-10"> -->
				<div class="col-xs-12 col-sm-12">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_green">
							<h3 class="panel-title">POS Terminal Sales Detail</h3>
							<!-- <div class="custom-select">
								<select id="possalesreloadss" style="width: 100%;">
									<option selected="selected" value="180000">Every 3 Min</option>
									<option value="180000">Every 3 Min</option>
									<option value="300000">Every 5 Min</option>
									<option value="600000">Every 10 Min</option>
								</select>
							</div> -->
						</div>
						<div class="panel-body">
						<div class="row">
						<div class="col-sm-8">
							<p>Date : <%=new DateUtils().getDatetimedisplay()%></p>
						</div>
						<div class="col-sm-2" style="text-align: right;">
    						<select id="possalesreload">
									<option selected="selected" value="180000">Every 3 Min</option>
									<option value="300000">Every 5 Min</option>
									<option value="600000">Every 10 Min</option>
								</select>
								</div>
						<div class="col-sm-2">
						<a href="#" id="possalesrefresh">
						<i class="fa fa-refresh" title="Refresh" style="font-size: 15px;"></i>
						</a>
						</div>
    					</div>
							<div id="VIEW_RESULT_HERE" class="table-responsive">
					<table id="tableposlivesales"
						class="table table-bordred table-striped display nowrap" style="width:100%">
						<thead>
							<tr>
								<th>Outlet/Terminal Name</th>
								<th>Total Sales Price</th>
								<!-- <th>Total Cost</th> -->
			                    <th>Log On</th>
			                    <th>Diff</th>
			                    <th>Log Off</th>
			                    <th>Diff</th>
							</tr>
						</thead>
						<tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<!-- <th></th> -->
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
		if (dpos) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "posDashboard" : defaultTab;
		}
		%>
		<div id="payrollDashboard"
			class="tab-pane fade <%=(hpay && activeSection) ? " in active" : ""%>">
			<div class="row">
				<br>
				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_red">
						<span class="dashboard_heading">Net Salary</span>
						<div class="custom-select netSalarysel">
							<select id="netSalaryRange" style="width: 100%;">
								<option selected="selected">This month</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-money"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="netSalary" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_dark-green">
						<span class="dashboard_heading">Gross Salary</span>
						<div class="custom-select grossSalarysel">
							<select id="grossSalaryRange" style="width: 100%;">
								<option selected="selected">This month</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-line-chart"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="grossSalary" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>
				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_light-blue">
						<span class="dashboard_heading">Total Deduction</span>
						<div class="custom-select deductionSel">
							<select id="TotalDeductionRange" style="width: 100%;">
								<option selected="selected">This month</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
						</div>
						<i class="fa fa-level-down"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
							<span id="sdeduction" data-decimals="<%=numberOfDecimal%>"></span>
						</h3>
					</div>
				</div>

				<div class="col-xs-12 col-sm-3">
					<div class="dashboard-stats__item dashboard-stats__item_purple">
						<span class="dashboard_heading">Total Active Employees</span>
						<!-- <div class="custom-select totalEmployeesel">
	            	<select style="width: 100%;">
						<option  selected="selected">This month</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div> -->
						<i class="fa fa-user-o"></i>
						<h3 class="dashboard-stats__title">
							<span style="float: left; font-size: 0.5em; font-weight: 600;">#</span>
							<span id="totalEmployee" data-decimals="<%=numberOfDecimal%>"><%=acemp%></span>
						</h3>
					</div>
				</div>
			</div>


			<div class="row">
				<br>
				<div class="col-xs-12 col-sm-12">
					<div class="panel panel-default">
						<div class="panel-heading dashboard-stats__item_purple">
							<h3 class="panel-title">Net Salary, Gross Salary And
								Deductions</h3>
							<div class="custom-select NetSalandGrosssSalSel">
								<select id="netGrossSalaryRange" style="width: 100%;">
									<option selected="selected">This month</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
							</div>
						</div>
						<div class="panel-body">
							<canvas id="lineChart"></canvas>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
		if (hpay) {
			activeSection = false;
			defaultTab = (defaultTab == "") ? "payrollDashboard" : defaultTab;
		}
		%>
	</div>
</section>
<!-- /.content -->
<%
//      Freeing up unused memory
session.removeAttribute("MSG_SEARCH");
session.removeAttribute("LOG_SEARCH");
session.removeAttribute("RESULT");
%>
<script src="<%=rootURI%>/jsp/dist/js/moment.min.js"></script>
<script src="<%=rootURI%>/jsp/js/jquery.countTo.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<script src="<%=rootURI%>/jsp/js/utils.js"></script>
<script src="<%=rootURI%>/jsp/js/core_main.js"></script>
<script src="<%=rootURI%>/jsp/js/daygrid_main.js"></script>
<script src="https://unpkg.com/@popperjs/core@2"></script>
<script src="https://unpkg.com/tippy.js@6"></script>
<script src="<%=rootURI%>/jsp/js/dashboard.js"></script>
<script>
var postatus =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },
  {
    "year": "OPEN",
    "value": "OPEN",
    "tokens": [
      "OPEN"
    ]
  },
  {
    "year": "PAID",
    "value": "PAID",
    "tokens": [
      "PAID"
    ]
  },
  {
    "year": "PARTIALLY PAID",
    "value": "PARTIALLY PAID",
    "tokens": [
      "PARTIALLY PAID"
    ]
  }];
$(document).ready(function(){
	getCurrentServerTime();
	
	$('#vendnameMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getpurchaseSummaryForMgt($('select[name ="purtableselMgt"]').val());
		});
	
	$('#vendnameExpenseMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getExpenseSummaryForMgt($('select[name ="expensetableselmgt"]').val());
		});
	
	$('#vendnamePayIssueenseMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getPaymentIssuedSummaryForMgt($('select[name ="PaymentIssuedtableselmgt"]').val());
		});
	
	$('#custnameMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getsalesSummaryForMgt($('select[name ="saltableselmgt"]').val());
		});
	
	
	$('#custnameIncomeMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getInvoiceSummaryForMgt($('select[name ="incometableselmgt"]').val());
		});
	
	$('#custnamePayRecpMgt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getPaymentReceiptSummaryForMgt($('select[name ="PaymentReceipttableselmgt"]').val());
		});
	
		/* To get the suggestion data for Status */
		$(".purstatusMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getpurchaseSummaryForMgt($('select[name ="purtableselMgt"]').val());
		});
		
		/* To get the suggestion data for Status */
		$(".salesstatusMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		}).on('typeahead:select',function(event,selection){
			getsalesSummaryForMgt($('select[name ="saltableselmgt"]').val());
		});
		
		/* To get the suggestion data for Account */
		$("#accountSearchMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						action : "getSubAccountTypeGroup",
						module:"incomesummaryfilter",
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getInvoiceSummaryForMgt($('select[name ="incometableselmgt"]').val());
			});
		
		/* To get the suggestion data for Account */
		$("#accountExpSearchMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						action : "getSubAccountTypeGroup",
						module:"Expensesummaryfilter",
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getExpenseSummaryForMgt($('select[name ="expensetableselmgt"]').val());
			});
		
		/* To get the suggestion data for Account */
		$("#accountPayIssuedSearchMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getPaymentIssuedSummaryForMgt($('select[name ="PaymentIssuedtableselmgt"]').val());
			});
		
		
		/* To get the suggestion data for Account */
		$("#accountPayReceiptSearchMgt").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getPaymentReceiptSummaryForMgt($('select[name ="PaymentReceipttableselmgt"]').val());
			});
		
		$('#custnameaccRecv').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  //name: 'states',
			  display: 'CNAME',  
			  async: true,   
			  //source: substringMatcher(states),
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : document.form.plant.value,
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
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
			    	return '<p>' + data.CNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();
				if(displayCustomerpop == 'true'){
				/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
				}
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getAccountReceivableByCustomerForMgt($('.accRecvSelForMgt select').val());
			});
		
		$('#custnamepayRecvPdc').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  //name: 'states',
			  display: 'CNAME',  
			  async: true,   
			  //source: substringMatcher(states),
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : document.form.plant.value,
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
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
			    	return '<p>' + data.CNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();
				if(displayCustomerpop == 'true'){
				/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
				}
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getPaymentRecvPdcForMgt($('.payRecvPdcForMgtSel select').val());
			});
		
		$('#custNameAccRecv').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  //name: 'states',
			  display: 'CNAME',  
			  async: true,   
			  //source: substringMatcher(states),
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : document.form.plant.value,
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
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
			    	return '<p>' + data.CNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getAccountReceivableByCustomer($('.accRecvSel select').val());
			});
		
		$('#custNamePayRecvPdc').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  //name: 'states',
			  display: 'CNAME',  
			  async: true,   
			  //source: substringMatcher(states),
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : document.form.plant.value,
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
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
			    	return '<p>' + data.CNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getPaymentRecvPdc($('.payRecvPdcSel select').val());
			});
		
		/* --------------------- account dashboard------------------------------ */
		
		$('#vendname').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getpurchaseSummary($('select[name ="purtablesel"]').val());
		});
	
	$('#vendnameExpense').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getExpenseSummary($('select[name ="expensetablesel"]').val());
		});
	
	$('#vendnamePayIssueense').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p>' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getPaymentIssuedSummary($('select[name ="PaymentIssuedtablesel"]').val());
		});
	
	$('#custname').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getsalesSummaryAcc($('select[name ="saltablesel"]').val());
		});
	
	
	$('#custnameIncome').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getInvoiceSummary($('select[name ="incometablesel"]').val());
		});
	
	$('#custnamePayRecp').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p>' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			/* $('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>'); */
			}
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			getPaymentReceiptSummary($('select[name ="PaymentReceipttablesel"]').val());
		});
	
		/* To get the suggestion data for Status */
		$(".purstatus").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		}).on('typeahead:select',function(event,selection){
			//$("input[name=PROJECTID]").val(selection.ID);
			getpurchaseSummary($('select[name ="purtablesel"]').val());
		});
		
		/* To get the suggestion data for Status */
		$(".salesstatus").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		}).on('typeahead:select',function(event,selection){
			getsalesSummaryAcc($('select[name ="saltablesel"]').val());
		});
		
		/* To get the suggestion data for Account */
		$("#accountSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						action : "getSubAccountTypeGroup",
						module:"incomesummaryfilter",
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getInvoiceSummary($('select[name ="incometablesel"]').val());
			});
		
		/* To get the suggestion data for Account */
		$("#accountExpSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
						action : "getSubAccountTypeGroup",
						module:"Expensesummaryfilter",
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getExpenseSummary($('select[name ="expensetablesel"]').val());
			});
		
		/* To get the suggestion data for Account */
		$("#accountPayIssuedSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getPaymentIssuedSummary($('select[name ="PaymentIssuedtablesel"]').val());
			});
		
		
		/* To get the suggestion data for Account */
		$("#accountPayReceiptSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : document.form.plant.value,
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
				  '<div style="padding:3px 20px;">',
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
				/* menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>'); */
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
			}).on('typeahead:select',function(event,selection){
				getPaymentReceiptSummary($('select[name ="PaymentReceipttablesel"]').val());
			});
		
		$('#vendnameaccPay').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'VNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : document.form.plant.value,
					ACTION : "GET_SUPPLIER_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.VENDMST);
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
			    return '<p>' + data.VNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}  
				/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getAccountPayableForMgt($('.accPaySelForMgt select').val(),0);
			});
		
		$('#vendnamepayPdc').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'VNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : document.form.plant.value,
					ACTION : "GET_SUPPLIER_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.VENDMST);
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
			    return '<p>' + data.VNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}  
				/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getPaymentPdcForMgt($('.payPdcSelForMgt select').val());
			});
		
		$('#vendNameAccPay').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'VNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : document.form.plant.value,
					ACTION : "GET_SUPPLIER_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.VENDMST);
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
			    return '<p>' + data.VNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}  
				/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getAccountPayableToSupplier($('.accPaySel select').val(), 0);
			});
		
		$('#vendNamePayPdc').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'VNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : document.form.plant.value,
					ACTION : "GET_SUPPLIER_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.VENDMST);
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
			    return '<p>' + data.VNAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}  
				/* menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				getPaymentPdc($('.payPdcSel select').val());
			});
		
		/* --------------------- account dashboard------------------------------ */
		
		
});

var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

var maintexpdayscount='<%=session.getAttribute("MAINTEXPCOUNT")%>';
var maintexpdate='<%=session.getAttribute("MAINTEXPDATE")%>';


if(parseInt(maintexpdayscount,10)<=30 && parseInt(maintexpdayscount,10)> 0)
{
	alert("Your maintenance is due for renewal on " + maintexpdate+ ".");
}
else if(parseInt(maintexpdayscount,10)<=0)
{
	alert("Your maintenance renewal is currently overdue since " + maintexpdate + ".");
}


<%session.setAttribute("MAINTEXPCOUNT", "");%>
<%session.setAttribute("MAINTEXPDATE", "");%>

var currentServerTime, tableTopIssuedProducts, tableTopReceivedProducts, tableExpProducts,tableReadyToPackOrders,
tableLowStockProducts,tablePaymentPdc,tablePaymentRecvPdc,tableAccPay,tableRecvPay,tableSupAge,tableAccPayForMgt,
tablecustAge,tableSupAgeForMgt,tableRecvPayForMgt,tablecustAgeForMgt,tablePaymentPdcForMgt,tablePaymentRecvPdcForMgt,
tablePurchaseSummaryForMgt,tableSalesSummaryForMgt,tableIncomeSummaryForMgt,tableExpenseSummaryForMgt,
tablePaymentIssuedSummaryForMgt,tablePayReceiptSummaryForMgt,tablePurchaseSummary,tableSalesSummary,
tableIncomeSummary,tableExpenseSummary,tablePaymentIssuedSummary,tablePayReceiptSummary;
var calendar;

var currentServerTime;
function getCurrentServerTime(){
	var dataURL = '<%=rootURI%>/DashboardServlet?ACTION=CURRENT_DATE';
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		currentServerTime = data.time[0].CURRENT_DATE;
		doRestOfInitialization();
	});
}

function getFormattedDate(momentObj, formatString){
	if (typeof formatString === 'undefined'){
		return momentObj.utcOffset(currentServerTime, true).format("DD-MMM-YYYY");
	}else{
		return momentObj.utcOffset(currentServerTime, true).format(formatString);
	}
}

function getTopReceivedProducts(period){
	var dataURL = '<%=rootURI%>/DashboardServlet?ACTION=TOP_RECEIVED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableTopReceivedProducts){
		tableTopReceivedProducts.ajax.url( dataURL ).load();
	}else{
		tableTopReceivedProducts = $('#tableTopReceivedProducts').DataTable({
			"processing": true,
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",	
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.products[0].PRODUCT === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.products;
		        	}
		        }
		    },
			"columns": [{'data': 'PRODUCT', "orderable": false},{'data': 'ITEMDESC', "orderable": false},{'data': 'RECEIVEDQTY', "orderable": false}],
			"columnDefs": [{"className": "t-right", "targets": [2]}]
		});
	}
}

function getTopIssuedProducts(period){
	var dataURL = '<%=rootURI%>/DashboardServlet?ACTION=TOP_ISSUED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableTopIssuedProducts){	
		tableTopIssuedProducts.ajax.url( dataURL ).load();
	}else{
		tableTopIssuedProducts = $('#tableTopIssuedProducts').DataTable({
			"processing": true,
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.products[0].PRODUCT === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.products;
		        	}
		        }
		    },
			"columns": [{'data': 'PRODUCT', "orderable": false},{'data': 'ITEMDESC', "orderable": false},{'data': 'ISSUEDQTY', "orderable": false}],
			"columnDefs": [{"className": "t-right", "targets": [2]}]
		});
	}
}


	

	function doRestOfInitialization() {		
		if("<%=defaultTab%>" == "accountMgtDashboard"){
			getTotalPurchaseByBillForMgt('Last 30 days');
			getTotalSalesByInvoiceForMgt('Last 30 days');
			var totalPurchaseSummaryByBillForMgt = getPeriod('TotalPurchaseSummaryByBillForMgt', 'Today','totalPurchaseByBillSumryFromJournalRange');
			getTotalPurchaseSummaryByBillForMgt(totalPurchaseSummaryByBillForMgt);
			//getTotalPurchaseSummaryByBillForMgt('Today');
			var totalSalesSummaryByInvoiceForMgt = getPeriod('TotalSalesSummaryByInvoiceForMgt', 'Today','totalSalesSelMgtRange');
			getTotalSalesSummaryByInvoiceForMgt(totalSalesSummaryByInvoiceForMgt);
			//getTotalSalesSummaryByInvoiceForMgt('Today');
			var totalIncomeForMgt = getPeriod('TotalIncomeSummaryMgt', 'Last 30 days','totalIncomeMgtRange');
			getTotalIncomeForMgt(totalIncomeForMgt);
			//getTotalIncomeForMgt('Last 30 days');
			getTotalIncomeSummaryByInvoiceForMgt('Last 30 days');
			var totalExpenseForMgt = getPeriod('TotalExpenseSummaryMgt', 'Today','totalExpenseMgtRange');
			getTotalExpenseForMgt(totalExpenseForMgt);
			//getTotalExpenseForMgt('Today');			
			getTotalExpenseSummaryByBillForMgt('Last 30 days');
			var totalPaymentMadeForMgt = getPeriod('TotalPaymentMadeSummary', 'Today','totalPaymentIssuedMgtRange');
			getTotalPaymentMadeForMgt(totalPaymentMadeForMgt);
			//getTotalPaymentMadeForMgt('Today');
			getTotalPaymentMadeSummaryForMgt('Last 30 days');
			var totalPaymentReceiptForMgt = getPeriod('TotalPaymentReceiptForMgt', 'Today','totalPaymentReceiptMgtRange');
			getTotalPaymentReceiptForMgt(totalPaymentReceiptForMgt);
			//getTotalPaymentReceiptForMgt('Today');
			getTotalPaymentReceiptSummaryForMgt('Last 30 days');
			 var AccountpayableForMgt = getPeriod('AccountpayableForMgtSummary', 'Today','totalAccountPayableMgtRange');
			 getAccountpayableForMgt(AccountpayableForMgt);
			//getAccountpayableForMgt('Today');
			var AccountPayableForMgt = getPeriod('AccountPayableForMgt', 'Today','accountPayableMgtRange');
			getAccountPayableForMgt(AccountPayableForMgt);
			getSupplierAgeingForMgt('Last 30 days',1);
			getCustomerAgeingForMgt('Last 30 days',1);
			var accountReceivableForMgt = getPeriod('AccountReceivableForMgt', 'Today','totalAccountReceivaleMgtRange');
			getAccountReceivableForMgt(accountReceivableForMgt);
			var AccountReceivableByCustomerForMgt = getPeriod('AccountReceivableByCustomerSummary', 'Next 7 days','accountReceivableMgtRange');
			getAccountReceivableByCustomerForMgt(AccountReceivableByCustomerForMgt);
			//getAccountReceivableByCustomerForMgt('Next 7 days');
			var PaymentPdc = getPeriod('PaymentPdcSummary', 'Today','PDCIssuedRange');
			getPaymentPdcForMgt(PaymentPdc);
			//getPaymentPdcForMgt('Today');
			var PaymentRecvPdc = getPeriod('PaymentRecvPdcSummary', 'Today','PDCReceivedRange');
			getPaymentRecvPdcForMgt(PaymentRecvPdc);
			//getPaymentRecvPdcForMgt('Today');
			getTotalAsset('Last 30 days');
			//getCashInHand('Last 30 days');
			getCashInHandAll();
		    getTotalLiability('Last 30 days');
		    //getTotalCashAtBank('Last 30 days');
		    getTotalCashAtBankAll();
		    var netProfit = getPeriod('netProfitSummary', 'Today','netProfitRange');
			getNetProfit(netProfit);
		    //getNetProfit('Today');
		    var grossProfit = getPeriod('grossProfitSummary', 'Today','grossProfitRange');
			getGrossProfit(grossProfit);
		    //getGrossProfit('Today');
		    var purchaseSummaryForMgt = getPeriod('purchaseSummaryForMgt', 'Today','purtableselMgtRange');
			getpurchaseSummaryForMgt(purchaseSummaryForMgt);
		    //getpurchaseSummaryForMgt('Today');
		    var salesSummaryForMgt = getPeriod('salesSummaryForMgt', 'Today','saltableselmgtRange');
			getsalesSummaryForMgt(salesSummaryForMgt);
		    //getsalesSummaryForMgt('Today');
		    var invoiceSummaryForMgt = getPeriod('InvoiceSummaryMgt', 'Today','incometableselmgtRange');
			getInvoiceSummaryForMgt(invoiceSummaryForMgt);
		    //getInvoiceSummaryForMgt('Today');
		    var ExpenseSummaryForMgt = getPeriod('ExpenseSummaryForMgt', 'Today','expensetableselmgtRange');
			getExpenseSummaryForMgt(ExpenseSummaryForMgt);
		    //getExpenseSummaryForMgt('Today');
		    var PaymentIssuedSummaryForMgt = getPeriod('PaymentIssuedSummaryForMgt', 'Today','PaymentIssuedtableselmgtRange');
			getPaymentIssuedSummaryForMgt(PaymentIssuedSummaryForMgt);
		    //getPaymentIssuedSummaryForMgt('Today');
		   	var PaymentReceiptSummaryForMgt = getPeriod('PaymentReceiptSummaryForMgt', 'Today', 'PaymentReceipttableselmgtRange');
			getPaymentReceiptSummaryForMgt(PaymentReceiptSummaryForMgt);
		    //getPaymentReceiptSummaryForMgt('Today');
		}else if("<%=defaultTab%>" == "accountsDashboard"){
			  /* getTotalPurchaseByBill('Today');
			  getTotalSalesByInvoice('Today');
			  getTotalPurchaseSummaryByBill('Today');
			  getTotalSalesSummaryByInvoice('Today');
			  getTotalIncome('Today');
			  getTotalExpense('Today');
			  getTotalIncomeSummaryByInvoice('Last 30 days');
			  getTotalExpenseSummaryByBill('Last 30 days');
			  getTotalPaymentMade('Today');
			  getTotalPaymentMadeSummary('Last 30 days');
			  getTotalPaymentReceipt('Today');
			  getTotalPaymentReceiptSummary('Last 30 days');
			  getAccountpayable('Today');
			  getAccountReceivable('Today');
			  getAccountPayableBySupplier('Today');
			  getAccountReceivableByCustomer('Next 7 days');
			  getPaymentPdc('Today');
			  getPaymentRecvPdc('Today');
			  getSupplierAgeing('Last 30 days',1);
			  getpurchaseSummary('Today');
			  getsalesSummaryAcc('Today');
			  getInvoiceSummary('Today');
			  getExpenseSummary('Today');
			  getPaymentIssuedSummary('Today');
			  getPaymentReceiptSummary('Today'); */
			  var totalPurchaseByBill = getPeriod('TotalPurchaseByBillSummary', 'Today','TotalPurchasesRange');
			  getTotalPurchaseByBill(totalPurchaseByBill);
			  var totalSalesByInvoice = getPeriod('TotalSalesByInvoiceSummary', 'Today','TotalSalesRange');
			  getTotalSalesByInvoice(totalSalesByInvoice);
			  var totalPurchaseSummaryByBill = getPeriod('TotalPurchaseSummaryByBill', 'Today','TotalPurchasesBillRange');
			  getTotalPurchaseSummaryByBill(totalPurchaseSummaryByBill);
			  var totalSalesSummaryByInvoice = getPeriod('TotalSalesSummaryByInvoice', 'Today', 'TotalSalesInvoiceRange');
			  getTotalSalesSummaryByInvoice(totalSalesSummaryByInvoice);
			  var totalIncome = getPeriod('TotalIncomeSummary', 'Today','totalIncomeSummaryRange');
			  getTotalIncome(totalIncome);
			  var totalExpense = getPeriod('TotalExpenseSummary', 'Today','totalExpenseRange');
			  getTotalExpense(totalExpense);
			  getTotalIncomeSummaryByInvoice('Last 30 days');
			  getTotalExpenseSummaryByBill('Last 30 days');
			  var totalPaymentMade = getPeriod('TotalPaymentMadeSummary', 'Today','totalPaymentIssuedRange');
			  getTotalPaymentMade(totalPaymentMade);
			  getTotalPaymentMadeSummary('Last 30 days');
			  var totalPaymentReceipt = getPeriod('TotalPaymentReceiptSummary', 'Today','TotalPaymentReceiptRange');
			  getTotalPaymentReceipt(totalPaymentReceipt);
			  getTotalPaymentReceiptSummary('Last 30 days');
			  var accountpayable = getPeriod('AccountpayableSummary', 'Next 7 days','totalAccountPayableRange');
			  getAccountpayable(accountpayable);
			  var accountReceivable = getPeriod('AccountReceivableSummary', 'Next 7 days','totalAccountReceivableRange');
			  getAccountReceivable(accountReceivable);
			  var accountPayableToSupplier = getPeriod('AccountPayableToSupplierSummary', 'Next 7 days','AccountPayableRange');
			  getAccountPayableToSupplier(accountPayableToSupplier);
			  var accountReceivableByCustomer = getPeriod('AccountReceivableByCustomerSummary', 'Next 7 days','accountReceivableRange');
			  getAccountReceivableByCustomer(accountReceivableByCustomer);
			  var paymentPdc = getPeriod('PaymentPdcSummary', 'Next 7 days','pdcIssuedRange');
			  getPaymentPdc(paymentPdc);
			  var paymentRecvPdc = getPeriod('PaymentRecvPdcSummary', 'Next 7 days','pdcReceivedRange');
			  getPaymentRecvPdc(paymentRecvPdc);
			  getSupplierAgeing('Last 30 days',1);
			  getCustomerAgeing('Last 30 days',1);
			  var purchaseSummary = getPeriod('PurchaseSummary', 'Today','PurchaseSummaryRange');
			  getpurchaseSummary(purchaseSummary);
			  var salesSummaryAcc = getPeriod('salesSummaryAcc', 'Today','SalesSummaryRange');
			  getsalesSummaryAcc(salesSummaryAcc);
			  var invoiceSummary = getPeriod('InvoiceSummary', 'Today','incomeSummaryRange');
			  getInvoiceSummary(invoiceSummary);
			  var expenseSummary = getPeriod('ExpenseSummary', 'Today','expenseSummaryRange');
			  getExpenseSummary(expenseSummary);
			  var paymentIssuedSummary = getPeriod('PaymentIssuedSummary', 'Today','paymentIssuedSummaryRange');
			  getPaymentIssuedSummary(paymentIssuedSummary);
			  var paymentReceiptSummary = getPeriod('PaymentReceiptSummary', 'Today','paymentReceiptSummaryRange');
			  getPaymentReceiptSummary(paymentReceiptSummary);
		}else if("<%=defaultTab%>" == "purchaseDashboard"){
			  getTotals('Last 30 days');
			  getTopIssuedProducts('Last 30 days');
			  getTopReceivedProducts('Last 30 days');
			  var ExpiringProducts = getPeriod('ExpiringProductsSummary', 'Tomorrow','ExpiringProducts');
			  getExpiringProducts(ExpiringProducts);
			  //getExpiringProducts('Tomorrow');
			  getStockReplanishmentProducts();
			  getNewSuppliers();
			  getTotalSuppliers();
			  calendar.destroy();
			  calendar.render();
		}else if("<%=defaultTab%>" == "salesDashboard"){
			  var TotalIssuePeriod = getPeriod('TotalIssue','Last 30 days', 'salesRange');
			  getTotalIssue(TotalIssuePeriod);
			  var TotalNumberSales = getPeriod('TotalNumberSales','Last 30 days', 'salesQtyRange');
			  getTotalNumberSales(TotalNumberSales);
			  var SalesSummaryPeriod = getPeriod('SalesSummary','Last 30 days', 'salesSummaryRange');
			  getSalesSummary(SalesSummaryPeriod);
			  var TopCustomersPeriod = getPeriod('TopCustomers','Last 30 days', 'topCustomersRange');
			  getTopCustomers(TopCustomersPeriod);
			  var TopSalesPeriod = getPeriod('TopSales','Last 30 days', 'topSalesRange');
			  getTopSalesPrd(TopSalesPeriod);
			  getNewCustomers();
			  getTotalCustomers();
			  calendar3.render();
			  calendar4.render();
		}else if("<%=defaultTab%>" == "warehouseDashboard"){
			  var totalReceivedItems = getPeriod('TotalReceivedItemsSummary', 'Last 30 days', 'receivedItemsRange');
			  getTotalReceivedItems(totalReceivedItems);
			  var totalIssuedItems = getPeriod('TotalIssuedItemsSummary', 'Last 30 days', 'TotalIssuedRage');
			  getTotalIssuedItems(totalIssuedItems);
			  var PoWoPriceSummary = getPeriod('POWithOutPriceSummary','Last 30 days','POWithoutPrice' );
			  getPoWoPriceSummary(PoWoPriceSummary);
			  var grnSummary = getPeriod('GrnSummary', 'Last 30 days','GRNSummary');
			  getGrnSummary(grnSummary);
			  var griSummary = getPeriod('GriSummary', 'Last 30 days','GISummary');
			  getGriSummary(griSummary);
			  getReadyToPackOrders('Today');
			  var ExpiringProducts = getPeriod('ExpiringProductsSummary', 'Today','ExpiringProducts');
			  getExpiringProducts(ExpiringProducts);
			  getStockReplanishmentProducts();
			  calendar5.render();
			  calendar6.render();
		}else if("<%=defaultTab%>" == "payrollDashboard") {
		    var NetSalary = getPeriod('NetSalarySummary', 'This Month','netSalaryRange');
		  	getNetSalary(NetSalary);
		  	var GrossSalary = getPeriod('GrossSalarySummary', 'This month','grossSalaryRange');
			getGrossSalary(GrossSalary);
			var Deduction = getPeriod('DeductionSummary', 'This month','TotalDeductionRange');
			getDeduction(Deduction);
			var NetSalandGrosssSal = getPeriod('NetSalandGrosssSalSummary', 'This month','netGrossSalaryRange');
			getNetSalandGrosssSal(NetSalandGrosssSal);
		}

	}

	function count(options) {
		var $this = $(this);
		options = $.extend({}, options || {}, $this.data('countToOptions')
				|| {});
		$this.countTo(options);
	}
	var x, i, j, l, ll, selElmnt, a, b, c;
	/*look for any elements with the class "custom-select":*/
	x = document.getElementsByClassName("custom-select");
	l = x.length;
	for (i = 0; i < l; i++) {
		selElmnt = x[i].getElementsByTagName("select")[0];
		ll = selElmnt.length;
		/*for each element, create a new DIV that will act as the selected item:*/
		a = document.createElement("DIV");
		a.setAttribute("class", "select-selected");
		a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
		x[i].appendChild(a);
		/*for each element, create a new DIV that will contain the option list:*/
		b = document.createElement("DIV");
		b.setAttribute("class", "select-items select-hide");
		for (j = 1; j < ll; j++) {
			/*for each option in the original select element,
			create a new DIV that will act as an option item:*/
			c = document.createElement("DIV");
			c.innerHTML = selElmnt.options[j].innerHTML;
			c
					.addEventListener(
							"click",
							function(e) {
								/*when an item is clicked, update the original select box,
								and the selected item:*/
								var y, i, k, s, h, sl, yl;
								s = this.parentNode.parentNode
										.getElementsByTagName("select")[0];
								sl = s.length;
								h = this.parentNode.previousSibling;
								for (i = 0; i < sl; i++) {
									if (s.options[i].innerHTML == this.innerHTML) {
										s.selectedIndex = i;
										h.innerHTML = this.innerHTML;
										y = this.parentNode
												.getElementsByClassName("same-as-selected");
										yl = y.length;
										for (k = 0; k < yl; k++) {
											y[k].removeAttribute("class");
										}
										this.setAttribute("class",
												"same-as-selected");
										break;
									}
								}
								h.click();
							});
			b.appendChild(c);
		}
		x[i].appendChild(b);
		a.addEventListener("click", function(e) {
			/*when the select box is clicked, close any other select boxes,
			and open/close the current select box:*/
			e.stopPropagation();
			closeAllSelect(this);
			this.nextSibling.classList.toggle("select-hide");
			this.classList.toggle("select-arrow-active");
		});
	}
	function closeAllSelect(elmnt) {
		/*a function that will close all select boxes in the document,
		except the current select box:*/
		var x, y, i, xl, yl, arrNo = [];
		x = document.getElementsByClassName("select-items");
		y = document.getElementsByClassName("select-selected");
		xl = x.length;
		yl = y.length;
		for (i = 0; i < yl; i++) {
			if (elmnt == y[i]) {
				arrNo.push(i)
			} else {
				y[i].classList.remove("select-arrow-active");
			}
		}
		for (i = 0; i < xl; i++) {
			if (arrNo.indexOf(i)) {
				x[i].classList.add("select-hide");
			}
		}
	}
	/*if the user clicks anywhere outside the select box,
	 then close all select boxes:*/
	document.addEventListener("click", closeAllSelect);
</script>

<%--New page design begin --%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%--New page design end --%>