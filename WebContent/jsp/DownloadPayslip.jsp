<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Download Payroll";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String action = StrUtils.fString(request.getParameter("ACTION"));
	String filterid=StrUtils.fString(request.getParameter("FILTER"));
	String employeeid=StrUtils.fString(request.getParameter("EMPID"));
	String fmonth=StrUtils.fString(request.getParameter("FMONTH"));
	String fyear=StrUtils.fString(request.getParameter("FYEAR"));
	String tmonth=StrUtils.fString(request.getParameter("TMONTH"));
	String tyear=StrUtils.fString(request.getParameter("TYEAR"));
	
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean displaySummaryEmail=false,displaySummaryPrint=false;
	if(systatus.equalsIgnoreCase("PAYROLL")) {
		displaySummaryPrint = ub.isCheckValPay("rpprintPayslip", plant,username);
		displaySummaryEmail = ub.isCheckValPay("rpemailPayslip", plant,username);
	}
	

	PlantMstDAO plantMstDAO = new PlantMstDAO();
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
	PlantMstUtil plantmstutil = new PlantMstUtil();
	
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String fiscalyear = plantMstDAO.getFiscalYear(plant);
	ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String ZIP = (String) plntMap.get("ZIP");
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String[] splitdate = curDate.split("/");
	String cyear = splitdate[2];
	String cmonth = splitdate[1];
	int cimonth = Integer.valueOf(cmonth);

	String cname = ubean.getCompanyName(plant).toUpperCase();
	
	String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);

	List<PayslipPojo> paysliplist = new ArrayList<PayslipPojo>();
	String SEALNAME= "";
	String SIGNNAME= "";
	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
    for (int i = 0; i < viewlistQry.size(); i++) {
        Map map = (Map) viewlistQry.get(i);
        SEALNAME=StrUtils.fString((String)map.get("SEALNAME"));
        SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
	}
     
    String sealPath = "", signPath = "";
    
    if(SEALNAME.equalsIgnoreCase("")){
    	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
    	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
    }
    if(SIGNNAME.equalsIgnoreCase("")){
    	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    }else {
       	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
    }
    
     String curDate1 = _dateUtils.addMonth(curDate, -2);
     String[] splitdate1 = curDate1.split("/");
     String cyear1 = splitdate1[2];
     String cmonth1 = splitdate1[1];
     int cimonth1 = Integer.valueOf(cmonth1);
     
     if(action.equalsIgnoreCase("ADD")){
    	 paysliplist = hrPayrollHDRDAO.getpaysliplistfilter(plant, filterid, fmonth, fyear, tmonth, tyear, employeeid);
     }

%>
<%@include file="sessionCheck.jsp"%>
<style>
.paytable table tr td{
	padding: 5px;
}
</style>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_REPORT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<script src="../jsp/dist/js/jquery.toaster.js"></script>
<div class="container-fluid m-t-20 contentdata">

		 <div class="box"> 
		 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../payroll/reports"><span class="underline-on-hover">Payroll Reports</span> </a></li>                                     
                <li><label>Download Payroll</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
        <div class="box-header menu-drop">
            <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
            <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../payroll/reports'">
              		<i class="glyphicon glyphicon-remove"></i>
              	</h1>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1">
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="text" name="tabledata" hidden>
				<input type="text" name="empid" hidden>
				<div id="target" style="padding-left: 15px;">
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2">		    	 
				  			<select name="selfilter" class="form-control" onclick="searchfilter(this)">
							    <option selected value='1'>Between Months</option>
							    <option value='2'>Single Month</option>
							    <option value='3'>This Month</option>
							    <option value='4'>Last Quarter</option>
							    <option value='5'>This Year</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2">
					  		<input type="text" class="ac-selected form-control typeahead"
								id="pay_employee" name="pay_employee" placeholder="Select a employee">
							<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'pay_employee\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-2 selfrommonth">
							<select name="from_month" class="form-control" id="frommonth">
							    <option selected value=''>Select From Month</option>
							    <option value='1'>January</option>
							    <option value='2'>February</option>
							    <option value='3'>March</option>
							    <option value='4'>April</option>
							    <option value='5'>May</option>
							    <option value='6'>June</option>
							    <option value='7'>July</option>
							    <option value='8'>August</option>
							    <option value='9'>September</option>
							    <option value='10'>October</option>
							    <option value='11'>November</option>
							    <option value='12'>December</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2 selfromyear">
				  			<select class="form-control text-left pay_year" name="from_year" id="fromyear">
								<option selected value=''>Select From Year</option>
							</select>
				  		</div>
				  		<div class="col-sm-2 seltomonth">
				  			<select name="to_month" class="form-control" id="tomonth">
							    <option selected value=''>Select To Month</option>
							    <option value='1'>January</option>
							    <option value='2'>February</option>
							    <option value='3'>March</option>
							    <option value='4'>April</option>
							    <option value='5'>May</option>
							    <option value='6'>June</option>
							    <option value='7'>July</option>
							    <option value='8'>August</option>
							    <option value='9'>September</option>
							    <option value='10'>October</option>
							    <option value='11'>November</option>
							    <option value='12'>December</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2 seltoyear">
				  			<select class="form-control text-left pay_year" name="to_year" id="toyear">
								<option selected value=''>Select To Year</option>
							</select>
				  		</div>
				  		<div class="col-sm-12 ac-box" style="padding:20px;text-align:center;">
				  			<button type="button" class="btn btn-success" onclick="validatePayfilter()">Search</button>
				  		</div>
	
	  				</div>
	  			</div>
	  			</div>

			</form>
		</div>
	</div>
		<%if(!paysliplist.isEmpty()){ %>
		<div style="height: 700px;overflow: scroll;background: #e1e1e1;">
			<div class="box-header menu-drop" style="background: whitesmoke;">
				<h3 class="box-title">Payslip</h3>
				<div class=" pull-right">
					<div class="btn-group" role="group">
					
						<button type="button" class="btn btn-default" style="margin-right: 15px;"
						 data-toggle="tooltip"  data-placement="bottom"  title="Payslip">
							<a href="/track/PayrollServlet?CMD=GET_PAYSLIP_ALL&selfilter=<%=filterid%>&empid=<%=employeeid%>&from_year=<%=fyear%>&to_month=<%=tmonth%>&to_year=<%=tyear%>&from_month=<%=fmonth%>"><i class="fa fa-file-pdf-o" aria-hidden="true"></i></a>
						</button>
						<%-- <button type="button" class="btn btn-default" style="margin-right: 15px;"
							 data-toggle="tooltip"  data-placement="bottom"  title="Print Payslip">
						<a href="/track/PayrollServlet?CMD=GET_PAYSLIP_ALL_PRINT&selfilter=<%=filterid%>&empid=<%=employeeid%>&from_year=<%=fyear%>&to_month=<%=tmonth%>&to_year=<%=tyear%>&from_month=<%=fmonth%>" target="_blank"><i class="fa fa-print" aria-hidden="true"></i></a>
						</button> --%>
							<% if (displaySummaryPrint) { %>	
						<button type="button" class="btn btn-default" style="margin-right: 15px;"
							 data-toggle="tooltip"  data-placement="bottom"  title="Print Payslip">
						<a href="#" onclick="window.open('/track/PayrollServlet?CMD=GET_PAYSLIP_ALL_PRINT&selfilter=<%=filterid%>&empid=<%=employeeid%>&from_year=<%=fyear%>&to_month=<%=tmonth%>&to_year=<%=tyear%>&from_month=<%=fmonth%>','popup','width=600,height=600'); return false;"><i class="fa fa-print" aria-hidden="true"></i></a>
					</button><% } %>
					<%-- <button type="button" class="btn btn-default" style="margin-right: 15px;"
							 data-toggle="tooltip"  data-placement="bottom"  title="Print Payslip">
						<a href="/track/PayrollServlet?CMD=SEND_PAYSLIP_EMAIL&selfilter=<%=filterid%>&empid=<%=employeeid%>&from_year=<%=fyear%>&to_month=<%=tmonth%>&to_year=<%=tyear%>&from_month=<%=fmonth%>"><i class="fa fa-envelope-o" aria-hidden="true"></i></a>
						</button> --%>
							<% if (displaySummaryEmail) { %>	
					<button type="button" class="btn btn-default" style="margin-right: 15px;"
							 data-toggle="tooltip"  data-placement="bottom"  title="Email Payslip">
						<a href="#" onclick="sendemailpay('<%=fmonth%>','<%=fyear%>','<%=tmonth%>','<%=tyear%>','<%=employeeid%>','<%=filterid%>')"><i class="fa fa-envelope-o" aria-hidden="true"></i></a>
						</button><% } %>
					</div>
				</div>
			</div>

			<%for(PayslipPojo payslip:paysliplist){ %>
				<div class="container-fluid" style="margin: auto;width: 65%;border-style: groove;margin-top: 5%;background:white;">
					<div class="row">
						<div class="col-sm-2">
							<img src="<%=request.getContextPath() %>/GetCustomerLogoServlet" style="width:100px;height:50px;">
						</div>
						<div class="col-sm-10" style="text-align:center">
							<div class="col-sm-12">
								<h5 style="font-weight: bold;margin-right: 140px;">PAYSLIP</h5>
							</div>
							<div class="col-sm-12">
								<h5 style="font-weight: bold;margin-right: 140px;"><%=cname%></h5>
							</div>
						</div>
					</div>
					<div class="paytable">
						<table>
							<tr>
								<td style="width: 175px;">Employee Code</td>
								<td>:</td>
								<td><%=payslip.getEmpcode() %></td>
							</tr>
							<tr>
								<td style="width: 175px;">Name</td>
								<td>:</td>
								<td><%=payslip.getName()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Department </td>
								<td>:</td>
								<td><%=payslip.getDepartment()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Designation </td>
								<td>:</td>
								<td><%=payslip.getDesignation()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Passport Number</td>
								<td>:</td>
								<td><%=payslip.getPassport()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Date Of Joining </td>
								<td>:</td>
								<td><%=payslip.getDoj()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Labour Card Number</td>
								<td>:</td>
								<td><%=payslip.getLabourcard()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Bank Details</td>
								<td>:</td>
								<td><%=payslip.getBankname()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Pay Period</td>
								<td>:</td>
								<td><%=payslip.getPayperiod()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Date Of Payment</td>
								<td>:</td>
								<td><%=payslip.getPaymentdate()%></td>
							</tr>
							<tr>
								<td style="width: 175px;">Mode Of Payment</td>
								<td>:</td>
								<td><%=payslip.getPaidthrough()%></td>
							</tr>
						</table>
					</div>
					
					<div class="container" style="width: 103%;margin-left: -10px;margin-top: 10px;">          
					  <table class="table">
					    <thead>
					      <tr>
					        <th style="background: gainsboro;">Earnings</th>
					        <th style="background: gainsboro;text-align: right;">Amount</th>
					      </tr>
					    </thead>
					    <tbody>
					      <%for(HrPayrollDET allowance:payslip.getSalary()){ %>
					      <tr>
					        <td><%=allowance.getSALARYTYPE() %></td>
					        <td style="text-align: right;"><%=StrUtils.addZeroes(allowance.getAMOUNT(), numberOfDecimal)%></td>
					      </tr>
					      <%} %>
					      <tr>
					        <td style="font-weight: bold;">Gross Salary</td>
					        <td style="text-align: right;"><%=StrUtils.addZeroes(payslip.getGrosspay(), numberOfDecimal)%></td>
					      </tr>
					    </tbody>
					  </table>
					</div>
					
					<%if(!payslip.getAddition().isEmpty()){ %>
					<h4 style="font-weight: bold;padding-left: 2%;">Additions</h4>
					<div class="container" style="width: 100%;padding-bottom: 2%;">  
						<table style="width: 100%;">
							<%for(HrPayrollDET addition:payslip.getAddition()){ %>
							<tr>
								<td><%=addition.getSALARYTYPE() %></td>
								<td style="text-align: right;"><%=StrUtils.addZeroes(addition.getAMOUNT(), numberOfDecimal) %></td>
							</tr>
							<%} %>
						</table>
					</div>
					<%} %>
					<%if(!payslip.getDeduction().isEmpty()){ %>
					<h4 style="font-weight: bold;padding-left: 2%;">Deductions</h4>
					<div class="container" style="width: 100%;padding-bottom: 2%;">  
						<table style="width: 100%;">
						<%for(HrPayrollDET deduction:payslip.getDeduction()){ %>
							<tr>
								<td><%=deduction.getSALARYTYPE()%></td>
								<td style="text-align: right;"><%=StrUtils.addZeroes(deduction.getAMOUNT(), numberOfDecimal) %></td>
							</tr>
						<%} %>
						</table>
					</div>
					<%} %>
					<div class="container" style="width: 100%;padding-bottom: 2%;">  
						<table style="width: 100%;">
							<tr>
								<td style="font-weight: bold;">Net Salary</td>
								<td style="text-align: right;"><%=StrUtils.addZeroes(payslip.getNetpay(), numberOfDecimal)%></td>
							</tr>
						</table>
					</div>
					<div class="container" style="width: 100%;padding-bottom: 2%;">  
						<table style="width: 100%;">
							<tr>
								<td><img src="/track/ReadFileServlet?fileLocation=<%=sealPath%>" style="width: 100px; height: 50px;"></td>
								<td style="text-align: right;"><img src="/track/ReadFileServlet?fileLocation=<%=signPath%>" style="width: 100px; height: 50px;"></td>
							</tr>
							<tr>
								<td></td>
								<td style="text-align: right;padding:15px">Signature</td>
							</tr>
						</table>
					</div>
				</div>
		<%} %>
		
		</div>
		<%} %>
		
	</div>
</div>

<style>
.searchAccFilter
{
	background-color: #d9534f;
    border-color: #d43f3a;
    padding: 9px;
    margin-top: 20px;
    margin-left: 12px;
    color: white;
}
.tabulator-row>span {
  display: none;
}

</style>
<script type="text/javascript">
$(document).ready(function(){
	var plant = document.form1.plant.value;
	var start1 = new Date().getFullYear();
 	var start = parseFloat(start1)-parseFloat("10");
    var end = parseFloat(start1)+parseFloat("100");
    var options = "";
    for(var year = start ; year <=end; year++){
    	  $('.pay_year')
          .append($("<option></option>")
                     .attr("value", year)
                     .text(year)); 
    }
    
    /* Employee Auto Suggestion */
	$('#pay_employee').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'FNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						ACTION : "GET_EMPLOYEE_DATA_PAYROLL",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.EMPMST);
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
		    	return '<p onclick="setemployee(\''+data.ID+'\')">' + data.EMPNO+' - '+data.FNAME +'</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/* menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#employeeModal"><a href="#"> + New Employee</a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
	
});

function searchfilter(obj){
	 var value = $(obj).val();
	 var cmonth = "<%=cimonth%>";
	 var cyear = "<%=cyear%>";
	 
	 var threemonth = "<%=cimonth1%>";
	 var threeyear = "<%=cyear1%>";
	 
	 var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	 $('#pizza_kind').prop('disabled', false);
	 if(value == "1"){
		 $('#frommonth').prop('disabled', false);
		 $('#fromyear').prop('disabled', false);
		 $('#tomonth').prop('disabled', false);
		 $('#toyear').prop('disabled', false);
		 
		 $('.selfrommonth option:eq(0)').prop('selected', true);
		 $('.selfromyear option:eq(0)').prop('selected', true);
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "2"){
		 $('#frommonth').prop('disabled', false);
		 $('#fromyear').prop('disabled', false);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
		 $('.selfrommonth option:eq(0)').prop('selected', true);
		 $('.selfromyear option:eq(0)').prop('selected', true);
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "3"){
		 var eq = parseInt(cmonth);
		 console.log(cyear);
		 $('#fromyear').val(cyear);
		 $('.selfrommonth option:eq('+eq+')').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
		 
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "4"){
		 var eq = parseInt(cmonth);
		 var eq3 = parseInt(threemonth);
		 
		 $('#fromyear').val(threeyear);
		 $('.selfrommonth option:eq('+eq3+')').prop('selected', true);
		 
		 $('#toyear').val(cyear);
		 $('.seltomonth option:eq('+eq+')').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
	 }
	 
	 if(value == "5"){
		 var eq = parseInt(cmonth);
		 console.log(cyear);
		 $('#fromyear').val(cyear);
		 $('.selfrommonth option:eq(1)').prop('selected', true);
		 
		 $('#toyear').val(cyear);
		 $('.seltomonth option:eq(12)').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
	 }
}

function setemployee(id){
	$('input[name ="empid"]').val(id);
}

function validatePayfilter(){
	var filtervalue = document.form1.selfilter.value;
	var frommonth = document.form1.from_month.value;
	var fromyear = document.form1.from_year.value;
	var tomonth = document.form1.to_month.value;
	var toyear = document.form1.to_year.value;
	var pay_employee = document.form1.pay_employee.value;
	var employeeid = document.form1.empid.value;
	
	if(pay_employee == ""){
		alert("Please select Employee");
		document.form1.pay_employee.focus();
		return false;
	}
	
	if(filtervalue == "1"){
		if(frommonth == ""){
			alert("Please select from month");
			document.form1.from_month.focus();
			return false;
		}
		
		if(fromyear == ""){
			alert("Please select from year");
			document.form1.from_year.focus();
			return false;
		}
		
		if(tomonth == ""){
			alert("Please select to month");
			document.form1.to_month.focus();
			return false;
		}
		
		if(toyear == ""){
			alert("Please select to year");
			document.form1.to_year.focus();
			return false;
		}

	}
	if(filtervalue == "2"){
		if(frommonth == ""){
			alert("Please select from month");
			document.form1.from_month.focus();
			return false;
		}
		
		if(fromyear == ""){
			alert("Please select from year");
			document.form1.from_year.focus();
			return false;
		}	
	}
	
	window.location.href="../payroll/payslip?FILTER="+filtervalue+"&EMPID="+employeeid+"&FYEAR="+fromyear+"&TMONTH="+tomonth+"&TYEAR="+ toyear+"&FMONTH="+frommonth+"&ACTION=ADD";
	
/* 	$.ajax({
		type : "POST",
		url : "/track/PayrollServlet?Submit=paysearch",
		async : true,
		data : {
			from_month : frommonth,
			from_year : fromyear,
			to_month : tomonth,
			to_year : toyear,
			empid : pay_employee,
			selfilter : filtervalue
		},
		dataType : "json",
		success : function(data) {
			window.location.href="DownloadPayslip.jsp?selfilter="+ filtervalue+"&empid="+ employeeid+"&from_year="+ fromyear+"&to_month="+ tomonth+"&to_year="+ toyear+"&from_month="+ frommonth+"&action=ADD";
		}
	}); */
	
	 /* document.form1.action  = "DownloadPayslip.jsp?action=ADD";
	 document.form1.submit(); */
	 }
	 
	 function sendemailpay(frommonth,fromyear,tomonth,toyear,pay_employee,filtervalue){

			$(".contentdata").css("display", "none");
			$("#loader").show();
			$.ajax({
				type : "GET",
				url : "/track/PayrollServlet?CMD=SEND_PAYSLIP_EMAIL",
				async : true,
				data : {
					from_month : frommonth,
					from_year : fromyear,
					to_month : tomonth,
					to_year : toyear,
					empid : pay_employee,
					selfilter : filtervalue
				},
				dataType : "json",
				success : function(data) {
					//var objc = JSON.parse(data);
		        	if(data.STATUS == "OK"){
		        		$(".contentdata").css("display", "block");
		    			$("#loader").hide();
		    			$.toaster({ priority: 'success', title: '', message: 'Email Sent Successfully!' });
		        	}else{
		        		$(".contentdata").css("display", "block");
		    			$("#loader").hide();
		    			$.toaster({ priority: 'danger', title: '', message: 'Email Sent Failed!' });
		        	}
				}
					
			}); 
	 }

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>