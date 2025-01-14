<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<%
	String title = "Genertate Payroll";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String pmonth=StrUtils.fString(request.getParameter("MONTH"));
	String pyear=StrUtils.fString(request.getParameter("YEAR"));
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

	List<PayslipPojo> paysliplist = hrPayrollHDRDAO.getpaysliplist(plant, pmonth, pyear);
	String SEALNAME= "";
	String SIGNNAME= "";
	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
    for (int i = 0; i < viewlistQry.size(); i++) {
        Map map = (Map) viewlistQry.get(i);
        SEALNAME=StrUtils.fString((String)map.get("SEALNAME"));
        SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
	}
     
     String sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
     String signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;

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
<jsp:param name="submenu" value="<%=IConstants.PAYROLL%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
  <script type="text/javascript" src="js/calendar.js"></script>
  <script type="text/javascript" src="dist/js/moment.min.js"></script>
<link href="css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/tabulator.min.js"></script>
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<script src="js/Printpagepdf.js"></script>
<div class="container-fluid m-t-20">
		<div class="box">
			<div class="box-header menu-drop">
				<h1 style="font-size: 20px;margin-bottom: 30px;" class="box-title"><%=title %></h1>
             	<h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              		<i class="glyphicon glyphicon-remove"></i>
              	</h1>
			</div>
		</div>
		<div class="container-fluid">
				<form name="form">
					<input type="hidden" name="plant" value="<%=plant%>">
					<input type="hidden" name="username" value=<%=username%>>
					<input type="hidden" name="pay_numberOfDecimal" value=<%=numberOfDecimal%>>
					<div class="row">
						<!-- <div class="col-sm-1 form-group">
							<label class="required" style="font-size: 12px;">From Date</label>
						</div> -->
						<div class="col-sm-2 form-group">
							<label class="required">From Date</label>
							<input name="pay_fromdate" class="form-control sdatepickerleave" placeholder="Select from date" autocomplete="off" onchange="blocktodate(this)" type="text" value="<%=curDate%>"> 
						</div>
						
						<!-- <div class="col-sm-1 form-group">
							<label class="required" style="font-size: 12px;">To Date</label>
						</div> -->
						<div class="col-sm-2 form-group">
							<label class="required">To Date</label>
							<input name="pay_todate" class="form-control edatepickerleave" placeholder="Select to date" autocomplete="off" onchange="validatemonthyeardate()" type="text" value="<%=curDate%>"> 
						</div>
<!-- 
						<div class="col-sm-1 form-group">
							<label class="required" style="font-size: 12px;">Month</label>
						</div> -->
						<div class="col-sm-2 form-group">
							<label class="required">Month</label>
							<select name="pay_month" onclick="validatemonthyeardate()" class="form-control">
							   <!--  <option value=''>--Select Month--</option> -->
							    <option <%if(cimonth == 1){%> selected<%}%> value='1'>January</option>
							    <option <%if(cimonth == 2){%> selected<%}%> value='2'>February</option>
							    <option <%if(cimonth == 3){%> selected<%}%> value='3'>March</option>
							    <option <%if(cimonth == 4){%> selected<%}%> value='4'>April</option>
							    <option <%if(cimonth == 5){%> selected<%}%> value='5'>May</option>
							    <option <%if(cimonth == 6){%> selected<%}%> value='6'>June</option>
							    <option <%if(cimonth == 7){%> selected<%}%> value='7'>July</option>
							    <option <%if(cimonth == 8){%> selected<%}%> value='8'>August</option>
							    <option <%if(cimonth == 9){%> selected<%}%> value='9'>September</option>
							    <option <%if(cimonth == 10){%> selected<%}%> value='10'>October</option>
							    <option <%if(cimonth == 11){%> selected<%}%> value='11'>November</option>
							    <option <%if(cimonth == 12){%> selected<%}%> value='12'>December</option>
	   						</select> 
						</div>
						
						<!-- <div class="col-sm-1 form-group">
							<label class="required" style="font-size: 12px;">Year</label>
						</div> -->
						<div class="col-sm-2 form-group">
							<label class="required">Year</label>
							<select class="form-control text-left pay_year" name="pay_year" value="<%=cyear%>" onclick="validatemonthyeardate()">
								<!-- <option selected value=''>--Select Year--</option> -->
							</select>
						</div>
						<div class="col-sm-4 form-group">
							<button type="button" class="btn btn-success" style="margin-top: 19px;" id="genpayroll">Generate Payroll</button>
						</div>
					</div>
					<!-- <div class="col-sm-offset-4 col-sm-8">
      					<button type="button" class="btn btn-success" id="genpayroll">Generate Payroll</button>
      				</div> -->
				</form>
		</div>
		<%if(!paysliplist.isEmpty()){ %>
		<div style="height: 700px;overflow: scroll;background: #e1e1e1;">
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
						</table>
					</div>
					
					<div class="container" style="width: 103%;margin-left: -10px;">          
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
					        <td style="text-align: right;"><%=payslip.getGrosspay()%></td>
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
								<td style="text-align: right;"><%=payslip.getNetpay()%></td>
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
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
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
	var plant = document.form.plant.value;
	
	var start = new Date().getFullYear();
    var end = parseFloat(start)+parseFloat("100");
    var options = "";
    for(var year = start ; year <=end; year++){
    	  $('.pay_year')
          .append($("<option></option>")
                     .attr("value", year)
                     .text(year)); 
    }
    
    $('.sdatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	});
	
	$('.edatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	});
});

function validatemonthyeardate(){
	var fromdate = document.form.pay_fromdate.value;
	var todate = document.form.pay_todate.value;
	var month = document.form.pay_month.value;
	var year = document.form.pay_year.value;
	
	console.log("fromdate------"+fromdate);
	console.log("todate------"+todate);
	console.log("month------"+month);
	console.log("year------"+year);
	
	if(fromdate == "" || todate == "" || month == "" || year == ""){
		$("#genpayroll").prop('disabled', true);
	}else{
		$("#genpayroll").prop('disabled', false);
	}
	
	
}

function blocktodate(obj){
	var sdate = $(obj).val();
	var sp = sdate.split("/");
	$.datepicker._clearDate('.edatepickerleave');
	var minDate = $( ".edatepickerleave" ).datepicker( "option", "minDate" );
	var date = new Date(sp[2], sp[1] - 1, sp[0])
	date.setDate(date.getDate() + 1);
	$( ".edatepickerleave" ).datepicker( "option", "minDate", date );
	validatemonthyeardate();
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>