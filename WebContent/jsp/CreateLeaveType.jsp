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
	<jsp:param name="submenu" value="Leave Type" />
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

.leavetype-action {
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
                <li><a href="../payroll/leavetype"><span class="underline-on-hover">Leave Type Summary</span> </a></li>   
                <li><label>Create Leave Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title">Create Leave Type</h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/leavetype'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form id="" class="form-vertical" name="form"
				action="/track/HrLeaveTypeServlet?Submit=SAVE" method="post"
				onsubmit="return validateEmployeetype()">
				<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table leavetype-table">
						<thead>
							<tr>
								<th>Leave Type</th>
								<th>Employee Type</th>
								<th>Total Entitlement</th>
								<th>Carry Forward</th>
								<th>Notes</th>
								<th>Is No Pay Leave</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center"><input
									class="form-control text-left" name="leavetype" type="text"
									placeholder="Enter Leave Type" onchange="leavetypecheck(this)" maxlength="100"></td>
								<td class="text-center"><input type="hidden"
									name="employeetypeid" value="0"> <input
									class="form-control text-left emptype" type="text" name="employeetype" onchange="leavetypeempcheck(this)"
									placeholder="Select Employee Type"></td>
								<td class="text-center"><input
									class="form-control text-left totalentitlement" type="text"
									name="totalentitlement" placeholder="Enter Totalentitlement" value="0.0">
								</td>
								<td class="text-center"><input
									class="form-control text-left carryforward" type="text" name="carryforward"
									placeholder="Enter carry forward" value="0.0"></td>
								<td class="text-center">
									<textarea  name="note" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>
								<td class="text-center">
									<input type="hidden" name="isnopayleave" value="0"> 
									<input type="checkbox" name="nopayleave" onclick="setnoplayleave(this)">
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
								onclick="addRow()">+ Add another Leave type</a>
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
		
		$('.emptype').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'EMPLOYEETYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/EmployeeTypeServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_EMPLOYEE_TYPE_DROPDOWN",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.EMPTYPELIST);
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
			    		return '<div onclick="setemployetprid(this,\''+data.ID+'\')"><p>' + data.EMPLOYEETYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer emptypepopup"  data-toggle="modal" data-target="#create_employee_type"><a href="#"> + New Employee Type</a></div>');
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
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
			$(document).on("focusout",".totalentitlement", function(){
				var value = $(this).val();
				var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
				var numbers = /^[0-9]+$/;
				if(value.match(decimal) || value.match(numbers)) 
				{ 
					var ldays=parseFloat(value).toFixed(1);
					$(this).val(ldays);
				}else{
					alert("Please enter valid total entitlement.");
					var ldays=parseFloat("0").toFixed(1);
					$(this).val(ldays);
				}
			});
			
			$(document).on("focusout",".carryforward", function(){
				var value = $(this).val();
				var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
				var numbers = /^[0-9]+$/;
				if(value.match(decimal) || value.match(numbers)) 
				{ 
					var ldays=parseFloat(value).toFixed(1);
					$(this).val(ldays);
				}else{
					alert("Please enter valid carry forward.");
					var ldays=parseFloat("0").toFixed(1);
					$(this).val(ldays);
				}
			});

	});
	
	function onClear()
	{
	  document.form.action  = "../payroll/createleavetype?action=Clear";
	  document.form.submit();
	}

	function validateEmployeetype() {
		var isItemValid = true;

		$("input[name ='leavetype']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The Leave type field cannot be empty.");
			return false;
		}

		$("input[name ='employeetype']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The employee type field cannot be empty.");
			return false;
		}
		
		$("input[name ='Totalentitlement']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The totalentitlement field cannot be empty.");
			return false;
		}
		
		
		$("input[name ='carryforward']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The carry forward field cannot be empty.");
			return false;
		}

		return true;
	}

	function addRow() {

		var body = "";
		body += '<tr>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="leavetype" type="text" onchange="leavetypecheck(this)" placeholder="Enter Leave Type" maxlength="100">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input type="hidden" name="employeetypeid" value="0">';
		body += '<input class="form-control text-left emptype" type="text" name="employeetype" onchange="leavetypeempcheck(this)" placeholder="Select Employee Type">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left totalentitlement" type="text" name="totalentitlement" placeholder="Enter Totalentitlement"  value="0.0">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left carryforward" type="text" name="carryforward" placeholder="Enter carry forward" value="0.0">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<textarea  name="note" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>';
		body += '</td>';
		body += '<td class="text-center grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle leavetype-action" aria-hidden="true"></span>';
		body += '<input type="hidden" name="isnopayleave" value="0">';
		body += '<input type="checkbox" name="nopayleave" onclick ="setnoplayleave(this)">';
		body += '</td>';
		body += '</tr>';
		$(".leavetype-table tbody").append(body);
		removerowclasses();
		addrowclasses();
	}

	$(".leavetype-table tbody").on('click', '.leavetype-action', function() {
		$(this).parent().parent().remove();
	});

	function setnoplayleave(obj) {
		if ($(obj).is(":checked")) {
			$(obj).closest('tr').find("input[name ='isnopayleave']").val("1");
			$(obj).closest('tr').find("input[name ='totalentitlement']").val("0.0");
			$(obj).closest('tr').find("input[name ='carryforward']").val("0.0");
			$(obj).closest('tr').find("input[name ='totalentitlement']").prop( "readonly", true );
			$(obj).closest('tr').find("input[name ='carryforward']").prop( "readonly", true );
		} else {
			$(obj).closest('tr').find("input[name ='isnopayleave']").val("0");
			$(obj).closest('tr').find("input[name ='totalentitlement']").prop( "readonly", false );
			$(obj).closest('tr').find("input[name ='carryforward']").prop( "readonly", false );
		}
	}
	
	function setemployetprid(obj, id){
		$(obj).closest('tr').find("input[name ='employeetypeid']").val(id);
		
		var leavetype =  $(obj).closest('tr').find('input[name = "leavetype"]').val();
			$.ajax({
	    		type : "GET",
	    		url: '/track/HrLeaveTypeServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_LEAVE_TYPE",
	    			EMPTYPEID : id,
	    			LEAVETYPE : leavetype
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				alert("Leave type alredy exist.");
	    				$(obj).closest('tr').remove();
	    			}
	    		}
	    	});
		
	}
	
	function removerowclasses(){
		$(".emptype").typeahead('destroy');
	}
	
	function addrowclasses(){
		$('.emptype').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'EMPLOYEETYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/EmployeeTypeServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_EMPLOYEE_TYPE_DROPDOWN",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.EMPTYPELIST);
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
			    		return '<div onclick="setemployetprid(this,\''+data.ID+'\')"><p>' + data.EMPLOYEETYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
				menuElement.after( '<div class="accountAddBtn footer emptypepopup"  data-toggle="modal" data-target="#create_employee_type"><a href="#"> + New Employee Type</a></div>');
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
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
	}
	
	function leavetypecheck(obj){
		var leavetype = $(obj).val();
		var emptypeid = $(obj).closest('tr').find('input[name = "employeetypeid"]').val();
		emptypeid != "0"
		if(emptypeid != "0"){
			$.ajax({
	    		type : "GET",
	    		url: '/track/HrLeaveTypeServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_LEAVE_TYPE",
	    			EMPTYPEID : emptypeid,
	    			LEAVETYPE : leavetype
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				alert("Leave type alredy exist.");
	    				$(obj).closest('tr').remove();
	    			}
	    		}
	    	});
		}
	} 
	
	/* function leavetypeempcheck(obj){
		var leavetype =  $(obj).closest('tr').find('input[name = "leavetype"]').val();
		var emptypeid = $(obj).closest('tr').find('input[name = "employeetypeid"]').val();
		if(emptypeid != "0"){
			$.ajax({
	    		type : "GET",
	    		url: '/track/HrLeaveTypeServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_LEAVE_TYPE",
	    			EMPTYPEID : emptypeid,
	    			LEAVETYPE : leavetype
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				alert("Leave type alredy exist.");
	    				$(obj).closest('tr').remove();
	    			}
	    		}
	    	});
		}
	}  */
</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="Create Employee Type" />
</jsp:include>