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
<%@page import="com.track.constants.IConstants"%>
<%--New page design begin --%>
<%
	String title = "Sales Transaction Dashboard";
%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES %>" />
	<jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION %>" />
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
	boolean war = false;
	if (systemnow.equalsIgnoreCase("payroll")) {
		war = ub.isCheckValPay("homewarehouse", plant, username);
	} else if (systemnow.equalsIgnoreCase("ACCOUNTING")) {
		war = ub.isCheckValinv("homewarehouse", plant, username);
	} else {
		war = ub.isCheckValinv("homewarehouse", plant, username);
	}

	if (war == true) {
		System.out.println("homewarehouse");
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
	<div class="container-fluid m-t-20">
		<div class="alert alert-danger alert-dismissible"
			style="width: max-content; margin: 0 auto;" hidden>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<span id="err-msg"></span>
		</div>
		<div class="alert alert-success alert-dismissible"
			style="width: max-content; margin: 0 auto;" hidden>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<span id="success-msg"></span>
		</div>
		<div class="box">
		<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Transaction Dashboard</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
			<div class="box-header menu-drop">
				<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
				<div class=" pull-right">
					<h1 style="font-size: 18px; cursor: pointer; vertical-align: top;"
						class="box-title pull-right"
						onclick="window.location.href='../track/home'">
						<i class="glyphicon glyphicon-remove"></i>
					</h1>
				</div>
				<div>
					<br /> <br />
				</div>
				<div
					style="position: relative; padding: 0px 20px; box-shadow: 0 0 6px #ccc;"
					id="print_id1">
					<div id="warehouseDashboard"
						class="tab-pane fade<%=(war && activeSection) ? " in active" : ""%>">
						<div class="row">
							<br>
							<div class="col-xs-12 col-sm-offset-1 col-sm-4">
								<div
									class="dashboard-stats__item dashboard-stats__item_dark-green">
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


							<div class="col-xs-12 col-sm-offset-2 col-sm-4">
								<div class="row">
									<div class="col-sm-12 txn-buttons">
										<div class="text-center">
											<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
											<button class="btn btn-success dropdown-toggle" type="button" 
												data-toggle="dropdown" style="border-radius: 25px;right: 100px; !important; height: 45px; width: 200px; ">
												New Transaction <span class="caret"></span>
											</button>
											<ul class="dropdown-menu" style="left: 25% !important;top: 50px;">
												<%
													java.util.ArrayList menuListWithSequence = (java.util.ArrayList) session
															.getAttribute("DROPDOWN_MENU_WITH_SEQUENCE");
													java.util.Hashtable htMenuItems = (java.util.Hashtable) menuListWithSequence.get(13); //	Twelth row
													java.util.Map htMenuItemDetail = (java.util.Map) htMenuItems.get(1);//	First column
													if (htMenuItemDetail == null) {
														htMenuItemDetail = new java.util.LinkedHashMap();
													} //	Just to overcome NPE
													java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
													while (iterator.hasNext()) {
														String menuItemTitle = "" + iterator.next();
														String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
														if (!menuItemURL.contains(".jsp")) {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													}
													}
													htMenuItemDetail = (java.util.Map) htMenuItems.get(2);//	Second column
													if (htMenuItemDetail == null) {
														htMenuItemDetail = new java.util.LinkedHashMap();
													} //	Just to overcome NPE
													iterator = htMenuItemDetail.keySet().iterator();
													while (iterator.hasNext()) {
														String menuItemTitle = "" + iterator.next();
														String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
														if (!menuItemURL.contains(".jsp")) {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													}
													}
													htMenuItemDetail = (java.util.Map) htMenuItems.get(3);//	Third column
													if (htMenuItemDetail == null) {
														htMenuItemDetail = new java.util.LinkedHashMap();
													} //	Just to overcome NPE
													iterator = htMenuItemDetail.keySet().iterator();
													while (iterator.hasNext()) {
														String menuItemTitle = "" + iterator.next();
														String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
														if (!menuItemURL.contains(".jsp")) {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													}
													}
													htMenuItemDetail = (java.util.Map) htMenuItems.get(4);//	Fourth column
													if (htMenuItemDetail == null) {
														htMenuItemDetail = new java.util.LinkedHashMap();
													} //	Just to overcome NPE
													iterator = htMenuItemDetail.keySet().iterator();
													while (iterator.hasNext()) {
														String menuItemTitle = "" + iterator.next();
														String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
														if (!menuItemURL.contains(".jsp")) {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a class="menu-link"
													href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													}
													}
												%>
											</ul>
										</div>
									</div>
								</div>
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
				</div>

				<div class="row">
					<div class="col-sm-6">
						<div class="panel panel-default">
							<div class="panel-heading dashboard-stats__item_green2">
								<h3 class="panel-title">Products Ready To Pack From Sales
									Order</h3>
								<div class="custom-select rtpSel">
									<select id="ProductsReadyToPackFromSalesOrder"
										style="width: 100%;">
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

				<%
					if (war) {
						activeSection = false;
						defaultTab = (defaultTab == "") ? "warehouseDashboard" : defaultTab;
					}
				%>
			</div>
		</div>
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
<script src="<%=rootURI%>/jsp/js/salesTransactiondashboard.js"></script>
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
		if("<%=defaultTab%>" == "warehouseDashboard") {
			var totalIssuedItems = getPeriod('TotalIssuedItemsSummary',
					'Last 30 days', 'TotalIssuedRage');
			getTotalIssuedItems(totalIssuedItems);
			var griSummary = getPeriod('GriSummary', 'Last 30 days',
					'GISummary');
			getGriSummary(griSummary);
			getReadyToPackOrders('Today');
			var ExpiringProducts = getPeriod('ExpiringProductsSummary',
					'Today', 'ExpiringProducts');
			getExpiringProducts(ExpiringProducts);
			getStockReplanishmentProducts();
			calendar6.render();
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