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
	String title = "Purchase Transaction Dashboard";
%>
<%!@SuppressWarnings({"rawtypes"})%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE %>" />
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION %>" />
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
	System.out.println("rootURI : " + rootURI);
%>
<style>
.nav>li>a:hover, .nav>li>a:active, .nav>li>a:focus {
	color: #444;
	background: #f7f7f7;
}

#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td {
	border: none;
	padding: 0px;
}

#table3>tbody>tr>td {
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
	position: absolute !important;
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

#table2>tfoot>tr>td {
	border: none;
}

#footerTable>tbody>tr>td {
	border: none;
}

#footerTable {
	display: none;
}

.page-break-before {
	page-break-before: always;
}

@media print {
	/*  @page { margin: 0; } */
	body {
		margin: 1cm 1.6cm 1.6cm 1.6cm;
	}
	#footerTable {
		display: table !important;
	}
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
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><label>Purchase Transaction Dashboard</label></li>                                   
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
								<div class="row">
									<div class="col-sm-12 txn-buttons">
										<div class="text-center">
											<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
											<button class="btn btn-success dropdown-toggle" type="button"
												data-toggle="dropdown" style="border-radius: 25px;right: 100px; !important; height: 45px; width: 200px;">
												New Transaction <span class="caret"></span>
											</button>
											<ul class="dropdown-menu" style="left: 25% !important;top: 50px;">
												<%
													java.util.ArrayList menuListWithSequence = (java.util.ArrayList) session
															.getAttribute("DROPDOWN_MENU_WITH_SEQUENCE");
													java.util.Hashtable htMenuItems = (java.util.Hashtable) menuListWithSequence.get(11); //	Twelth row
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
												<li><a href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
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
												<li><a href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
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
												<li><a href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
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
												<li><a href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
												<%
													} else {
												%>
												<li><a href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle%></a></li>
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
											onclick="redirectToPurchaseWOPSummary()"
											style="color: #337ab7;">See More</button>
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

							<%
								if (war) {
									activeSection = false;
									defaultTab = (defaultTab == "") ? "warehouseDashboard" : defaultTab;
								}
							%>

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
						<div>
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
<script src="<%=rootURI%>/jsp/js/purchaseTransactionDashboard.js"></script>
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
			var totalReceivedItems = getPeriod('TotalReceivedItemsSummary',
					'Last 30 days', 'receivedItemsRange');
			getTotalReceivedItems(totalReceivedItems);
			var totalIssuedItems = getPeriod('TotalIssuedItemsSummary',
					'Last 30 days', 'TotalIssuedRage');
			getTotalIssuedItems(totalIssuedItems);
			var PoWoPriceSummary = getPeriod('POWithOutPriceSummary',
					'Last 30 days', 'POWithoutPrice');
			getPoWoPriceSummary(PoWoPriceSummary);
			var grnSummary = getPeriod('GrnSummary', 'Last 30 days',
					'GRNSummary');
			getGrnSummary(grnSummary);
			getReadyToPackOrders('Today');
			getStockReplanishmentProducts();
			calendar5.render();
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