<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
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

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Create Employee Type" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Employee Type" />
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
                <li><a href="../payroll/employeetype"><span class="underline-on-hover">Employee Type Summary</span> </a></li>                                     
                <li><label>Create Employee Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Create Employee Type</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right"    onclick="window.location.href='../payroll/employeetype'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="/track/EmployeeTypeServlet?Submit=SAVE"  method="post" onsubmit="return validateEmployeetype()">
				<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
					<table class="table table-bordered line-item-table emptype-table">
						<thead>
							<tr>
								<th style="width:30%">Employee Type</th>
								<th>Employee Type Description</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input class="form-control text-left" name="emptype" type="text" onchange="emptypecheck(this)" placeholder="Enter Employee Type" maxlength="50"> 
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="emptypedesc" placeholder="Enter Employee Type Description" maxlength="100">
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
								onclick="addRow()">+ Add another employee type</a>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">

function validateEmployeetype(){
	var isItemValid = true;
	
	$("input[name ='emptype']").each(function() {
	    if($(this).val() == ""){	
	    	$(this).focus();
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The employee type field cannot be empty.");
		return false;
	}
	
	$("input[name ='emptypedesc']").each(function() {
	    if($(this).val() == ""){
	    	$(this).focus();
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The employee type description field cannot be empty.");
		return false;
	}
	
	return true;
}

function addRow(){

	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" name="emptype" type="text" onchange="emptypecheck(this)" placeholder="Enter Employee Type" maxlength="50">';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-left" type="text" name="emptypedesc" placeholder="Enter Employee Type Description" maxlength="100">';
	body += '</td>';
	body += '</tr>';
	$(".emptype-table tbody").append(body);
}

$(".emptype-table tbody").on('click','.emptype-action',function(){
    $(this).parent().parent().remove();
});

function emptypecheck(obj){
	var emptype = $(obj).val();
	var count = "0";
	var i = parseInt("1");
	$("input[name ='emptype']").each(function() {
		if($(this).val() == emptype){
			count = i;
			i = parseInt(i)+parseInt("1");
	    }	
	});
	if(count == parseInt("1")){
		$.ajax({
    		type : "GET",
    		url: '/track/EmployeeTypeServlet',
    		async : true,
    		dataType: 'json',
    		data : {
    			CMD : "CHECK_EMPLOYEE_TYPE",
    			EMPTYPE : emptype
    		},
    		success : function(data) {
    			if(data.STATUS == "NOT OK"){
    				alert("Employee type alredy exist.");
    				$(obj).closest('tr').remove();
    			}
    		}
    	});	
	}else{
		alert("Employee type alredy selected.");
		$(obj).closest('tr').remove();
	}
} 

function onClear()
{
  document.form.action  = "../payroll/createemployeetype?action=Clear";
  document.form.submit();
}
</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="Create Employee Type" />
</jsp:include>