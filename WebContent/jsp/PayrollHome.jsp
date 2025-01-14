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
%>
<style>
.nav>li>a:hover, .nav>li>a:active, .nav>li>a:focus {
    color: #444;
    background: #f7f7f7;
}
</style>
<link rel="stylesheet" href="css/dashboard.css">

<link rel="stylesheet" href="css/core_main.css">
<link rel="stylesheet" href="css/daygrid_main.css">
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
StrUtils strUtils = new StrUtils();
String plant = strUtils.fString((String) session.getAttribute("PLANT"));
String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
PlantMstUtil _PlantMstUtil =new PlantMstUtil();
java.util.List arr   = new java.util.ArrayList();
arr = _PlantMstUtil.getPlantMstDetails(plant);
String isPyrl="0";
if(arr.size()>0){
	for (int i =0; i<arr.size(); i++){
		 Map m = (Map) arr.get(i);
		 isPyrl = (String)m.get("ENABLE_PAYROLL");
	}
}
if (!isPyrl.equalsIgnoreCase("1"))    //  Invalid Session
{
	session.invalidate();
    System.out.println("New Session Divert it to Index Page");
	response.sendRedirect("../login");
	return;
}

String defaultTab="";
boolean accMgt=false, activeClass=true, activeSection=true;
accMgt = ub.isCheckVal("homeaccountingmanaget", plant,username);
	if(accMgt==true)
	{		
		System.out.println("homeaccountingmanaget");
	}
	
	boolean acc=false;
	acc = ub.isCheckVal("homeaccounting", plant,username);
	if(acc==true)
	{		
		System.out.println("homeaccounting");
	}
	
	boolean pur=false;
	pur = ub.isCheckVal("homepurchase", plant,username);
	if(pur==true)
	{		
		System.out.println("homepurchase");
	}
	
	boolean sal=false;
	sal = ub.isCheckVal("homesales", plant,username);
	if(sal==true)
	{		
		System.out.println("homesales");
	}
	
	boolean war=false;
	war = ub.isCheckVal("homewarehouse", plant,username);
	if(war==true)
	{		
		System.out.println("homewarehouse");
	}
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    
    EmployeeDAO employeeDAO = new EmployeeDAO();
    String acemp = employeeDAO.getActiveEmployee(plant);
    //acemp = StrUtils.addZeroes(Float.parseFloat(acemp), numberOfDecimal);
	%>
	
<form name="form">
	<input name="numberOfDecimal" type="hidden" value="<%=numberOfDecimal%>">
	<input name="baseCurrency" type="hidden" value="<%=session.getAttribute("BASE_CURRENCY")%>">	
</form>

<section class="content dashboard">
	<ul class="nav nav-tabs">
	    <li class="active"><a data-toggle="tab" href="#payrollDashboard">Payroll</a></li>
  	</ul>

  <div class="tab-content">

     <div id="payrollDashboard" class="tab-pane fade in active">
      	<div class="row">
     		<br>
      		<div class="col-xs-12 col-sm-3">
	      		<div class="dashboard-stats__item dashboard-stats__item_red">
	            	<span class="dashboard_heading">Net Salary</span>
	            	<div class="custom-select netSalarysel">
		            	<select style="width: 100%;">
							<option  selected="selected">This month</option>
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
	              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
	                <span id="netSalary" data-decimals="<%=numberOfDecimal%>"></span>
	              </h3>
	            </div>
      		</div>
      		
      		<div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Gross Salary</span>
            	<div class="custom-select grossSalarysel">
	            	<select style="width: 100%;">
						<option  selected="selected">This month</option>
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
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="grossSalary" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div>
         <div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_light-blue">
            	<span class="dashboard_heading">Total Deduction</span>
            	<div class="custom-select deductionSel">
	            	<select style="width: 100%;">
						<option  selected="selected">This month</option>
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
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
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
              	<span style="float: left;font-size: 0.5em;font-weight: 600;">#</span>
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
	             		<h3 class="panel-title">
	               			Net Salary, Gross Salary And Deductions
		               </h3>
		               <div class="custom-select NetSalandGrosssSalSel">
			            	<select style="width: 100%;">
								<option  selected="selected">This quarter</option>
								<option>This quarter</option>
								<option>This year</option>
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
  </div>
</section>
<!-- /.content -->
<%
	//      Freeing up unused memory
	session.removeAttribute("MSG_SEARCH");
	session.removeAttribute("LOG_SEARCH");
	session.removeAttribute("RESULT");
%>
<script type="text/javascript" src="dist/js/moment.min.js"></script>
<script type="text/javascript" src="js/jquery.countTo.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
<script type="text/javascript" src="js/core_main.js"></script>
<script type="text/javascript" src="js/daygrid_main.js"></script>
<script src="https://unpkg.com/@popperjs/core@2"></script>
<script src="https://unpkg.com/tippy.js@6"></script>
<script type="text/javascript" src="js/payrollDashboard.js"></script>
<script>
$(document).ready(function(){
	getCurrentServerTime();
});
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
tablecustAge,tableSupAgeForMgt,tableRecvPayForMgt,tablecustAgeForMgt,tablePaymentPdcForMgt,tablePaymentRecvPdcForMgt;
var calendar;

var currentServerTime;
function getCurrentServerTime(){
	var dataURL = '../DashboardServlet?ACTION=CURRENT_DATE';
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
	var dataURL = '../DashboardServlet?ACTION=TOP_RECEIVED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOP_ISSUED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
		getNetSalary('This month');
		getGrossSalary('This month');
		getDeduction('This month');
		getNetSalandGrosssSal('This quarter');
	}
	
	function count(options) {
		var $this = $(this);
		options = $.extend({}, options || {}, $this.data('countToOptions') || {});
		$this.countTo(options);
    }	
</script>

<script>
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
    c.addEventListener("click", function(e) {
        /*when an item is clicked, update the original select box,
        and the selected item:*/
        var y, i, k, s, h, sl, yl;
        s = this.parentNode.parentNode.getElementsByTagName("select")[0];
        sl = s.length;
        h = this.parentNode.previousSibling;
        for (i = 0; i < sl; i++) {
          if (s.options[i].innerHTML == this.innerHTML) {
            s.selectedIndex = i;
            h.innerHTML = this.innerHTML;
            y = this.parentNode.getElementsByClassName("same-as-selected");
            yl = y.length;
            for (k = 0; k < yl; k++) {
              y[k].removeAttribute("class");
            }
            this.setAttribute("class", "same-as-selected");
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