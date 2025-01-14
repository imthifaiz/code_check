<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String pcountry = StrUtils.fString((String) session.getAttribute("COUNTRY"));

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Create Salary Type" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALARY_TYPE%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
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
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
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
                <li><a href="../payroll/employeesalary"><span class="underline-on-hover">Salary Type Summary</span> </a></li>                
                <li><label>Create Salary Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->  
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Create Salary Type</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../payroll/employeesalary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="/track/HrSalaryServlet?Submit=SAVE"  method="post" onsubmit="return validateSalary()">
				<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
					<table class="table table-bordered line-item-table emptype-table">
						<thead>
							<tr>
								<th>Salary Type</th>
								<% if(pcountry.equals("Singapore")) { %> 
								<th>Deduct CPF Contribution</th>
								<%} else{%> 
								<th>Deduct PF Contribution</th>
								<%}%> 
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input class="form-control text-left" name="empSalary" type="text" placeholder="Enter Salary Type" onchange="checkitem(this.value)" maxlength="100"> 
								</td>
								<td class="text-center">
									  <input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
									  <input type="checkbox" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" />&nbsp;&nbsp;
								</td>						
							</tr>
						</tbody>
					</table>
				</div>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow()">+ Add another salary type</a>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
					<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default" onclick="window.location.href='EmployeeSalarySummary.jsp'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">

function validateSalary(){
	var isItemValid = true;
	
	$("input[name ='empSalary']").each(function() {
	    if($(this).val() == ""){	
	    	$(this).focus();
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Salary type field cannot be empty.");
		return false;
	}
	
	return true;
}

function addRow(){

	var body="";
	body += '<tr>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
//	body += '<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-left" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="100" onchange="checkitem(this.value)" >';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true"></span>'; 
	body += '<input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)">';
	body += '</td>';
	body += '</tr>';
	$(".emptype-table tbody").append(body);
}

$(".emptype-table tbody").on('click','.emptype-action',function(){
    $(this).parent().parent().remove();
});

function checkitem(itemvalue)
{	
	
	 if(itemvalue!="" || itemvalue.length>0 ) {
		
		var urlStr = "/track/HrSalaryServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				QUERY : itemvalue,
				PLANT : "<%=plant%>",
				CMD : "SALARYTYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Salary Type Already Exists");
						return false;	
						
					} 
					else 
						return true;
					    
				}
			});
		 return true;
	 }
}

function payrollbybasicsalary(obj){
	var manageapp = $(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val();
	if(manageapp == 0)
	$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(1);
	else
	$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(0);
	
}

function onClear()
{
	document.form.action  = "../payroll/createsalary";
	document.form.submit();
}

</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="Create Salary Type" />
</jsp:include>
