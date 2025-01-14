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
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Create Payroll Addition Master" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_ADDITION%>" />
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

.payaddition-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -25%;
	top: 15px;
}
</style>
<center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <li><a href="../payroll/addsummary"><span class="underline-on-hover">Payroll Addition Summary</span> </a></li>                   
                <li><label>Create Payroll Addition</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">Create Payroll Addition</h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/addsummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form id="" class="form-vertical" name="form"
				action="/track/HrPayrollAdditionMstServlet?Submit=SAVE" method="post"
				onsubmit="return validatePayaddmst()">
				<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table payaddition-table">
						<thead>
							<tr>
								<th>Addition Name</th>
								<th>Addition Description</th>
								<th>Is Loan or Advance?</th>
								<th>Is Claim?</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input class="form-control text-left" name="add_name" type="text" placeholder="Enter addition name" onchange="addnamecheck(this)" maxlength="100">
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="add_description" placeholder="Enter addition description">
								</td>
								<td class="text-center">
									<input type="hidden" name="isrecoverable" value="0"> 
									<input type="checkbox" name="recoverable" onclick="setrecoverable(this)">
								</td>
								<td class="text-center">
									<input type="hidden" name="isclaim" value="0"> 
									<input type="checkbox" name="claim" onclick="setclaim(this)">
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
								onclick="addRow()">+ Add another payroll addition</a>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-12 txn-buttons">
						 <button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default"
							onclick="window.location.href='LeaveTypeSummary.jsp'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<%@include file="Employeetypepopup.jsp"%>
<script type="text/javascript">

	$(document).ready(function(){
		

	});
	
	function onClear()
	{
	  document.form.action  = "../payroll/createadd?action=Clear";
	  document.form.submit();
	}

	function validatePayaddmst() {
		var isItemValid = true;

		$("input[name ='add_name']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The addition name field cannot be empty.");
			return false;
		}

		return true;
	}

	function addRow() {

		var body = "";
		body += '<tr>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="add_name" type="text" placeholder="Enter addition name" onchange="addnamecheck(this)" maxlength="100">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" type="text" name="add_description" placeholder="Enter addition description">';
		body += '</td>';
		body += '<td class="text-center">';
		
		body += '<input type="hidden" name="isrecoverable" value="0">';
		body += '<input type="checkbox" name="recoverable" onclick="setrecoverable(this)">';
		body += '</td>';
		body += '<td class="text-center"  style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle payaddition-action" aria-hidden="true"></span>';
		body += '<input type="hidden" name="isclaim" value="0">';
		body += '<input type="checkbox" name="claim" onclick="setclaim(this)">';
		body += '</td>';
		body += '</tr>';
		$(".payaddition-table tbody").append(body);
	}

	$(".payaddition-table tbody").on('click', '.payaddition-action', function() {
		$(this).parent().parent().remove();
	});

	function setrecoverable(obj) {
		if ($(obj).is(":checked")) {
			$(obj).closest('tr').find("input[name ='isrecoverable']").val("1");
			$(obj).closest('tr').find("input[name ='isclaim']").val("0");
			//$("input[name ='claim']").prop('checked', false);
			$(obj).closest('tr').find("input[name ='claim']").prop('checked', false);
		} else {
			$(obj).closest('tr').find("input[name ='isrecoverable']").val("0");
		}
	}
	
	function setclaim(obj) {
		if ($(obj).is(":checked")) {
			$(obj).closest('tr').find("input[name ='isclaim']").val("1");
			$(obj).closest('tr').find("input[name ='isrecoverable']").val("0");
			//$("input[name ='recoverable']").prop('checked', false);
			$(obj).closest('tr').find("input[name ='recoverable']").prop('checked', false);
		} else {
			$(obj).closest('tr').find("input[name ='isclaim']").val("0");
		}
	}
	
	function addnamecheck(obj){
		var addname = $(obj).val();
		var count = "0";
		var i = parseInt("1");
		$("input[name ='add_name']").each(function() {
			if($(this).val() == addname){
				count = i;
				i = parseInt(i)+parseInt("1");
		    }	
		});
		if(count == parseInt("1")){
			$.ajax({
	    		type : "GET",
	    		url: '/track/HrPayrollAdditionMstServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_PAYADDITION_MST",
	    			ADDNAME : addname
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				alert("Payroll addition name alredy exist.");
	    				$(obj).closest('tr').remove();
	    			}
	    		}
	    	});
		}else{
			alert("Payroll addition name alredy exist.");
			$(obj).closest('tr').remove();
		}
	} 
</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="Create Employee Type" />
</jsp:include>