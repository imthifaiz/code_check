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
	<jsp:param name="title" value="Create Employee Type" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Holiday" />
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

.holiday-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -5%;
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
                <li><a href="../payroll/holiday"><span class="underline-on-hover">Holiday Summary</span> </a></li>
                <li><label>Create Holiday</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">Create Holiday</h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/holiday'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form id="" class="form-vertical" name="form"
				action="/track/HrHolidayServlet?Submit=SAVE" method="post"
				onsubmit="return validateholiday()">
				<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table holiday-table">
						<thead>
							<tr>
								<th>HOLIDAY DATE</th>
								<th>HOLIDAY DESCRIPTION</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input class="form-control text-left datepicker holidaydate" onchange="dateselect(this)" name="holidaydate" type="text" placeholder="Select Date" readonly >
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="holidaydesc" placeholder="Max 100 characters" maxlength="100">
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
								onclick="addRow()">+ Add another holiday</a>
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
		$( ".datepicker" ).datepicker({
		    changeMonth: true,
		    changeYear: true,
			dateFormat: 'dd/mm/yy',
			yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
		  });
		
		/* $(document).on("focusout",".holidaydate", function(){
	    	var hdate = $(this).val();
	    	alert(hdate);
	    }); */
	    
	});
	
	function onClear()
	{
	  document.form.action  = "../payroll/createholiday?action=Clear";
	  document.form.submit();
	}
	
	function dateselect(obj){
		var hdate = $(obj).val();
		var count = "0";
		var i = parseInt("1");
		$("input[name ='holidaydate']").each(function() {
			if($(this).val() == hdate){
				count = i;
				i = parseInt(i)+parseInt("1");
		    }	
		});
		if(count == parseInt("1")){
			$.ajax({
	    		type : "GET",
	    		url: '/track/HrHolidayServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_HOLIDAY_DATE",
	    			hdate : hdate
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				alert("Holiday date alredy exist.");
	    				$(obj).closest('tr').remove();
	    			}
	    		}
	    	});	
		}else{
			alert("Holiday date alredy selected.");
			$(obj).closest('tr').remove();
		}
	} 

	function validateholiday() {
		var isItemValid = true;

		$("input[name ='holidaydate']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The Holiday Date field cannot be empty.");
			return false;
		}

		return true;
	}

	function addRow() {

		var body = "";
		body += '<tr>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left datepicker holidaydate" name="holidaydate" onchange="dateselect(this)" type="text" placeholder="Select Date" readonly>';
		body += '</td>';
		body += '<td class="text-center grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle holiday-action" aria-hidden="true"></span>';
		body += '<input class="form-control text-left" type="text" name="holidaydesc" placeholder="Max 100 characters" maxlength="100">';
		body += '</td>';
		body += '</tr>';
		$(".holiday-table tbody").append(body);
		addrowclasses();
	}

	$(".holiday-table tbody").on('click', '.holiday-action', function() {
		$(this).parent().parent().remove();
	});

	
	
	function addrowclasses(){
		$( ".datepicker" ).datepicker({
		    changeMonth: true,
		    changeYear: true,
			dateFormat: 'dd/mm/yy',
			yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
		  });
		
		/* $(document).on("focusout",".holidaydate", function(){
	    	var hdate = $(this).val();
	    	alert(hdate);
	    }); */
	}
</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="Create Employee Type" />
</jsp:include>