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
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));

String title = "New Payroll Deduction";

PlantMstDAO plantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String curDate = _dateUtils.getDate();
String[] splitdate = curDate.split("/");
String cyear = splitdate[2];
String cmonth = splitdate[1];
int cimonth = Integer.valueOf(cmonth);

String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_DEDUCTION%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
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

/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.payadd-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
    top: 15px;
}

.payatable>table, ,.payatabl>table>td,.payatable>table>td>th {
  border: 1px solid #ddd;
}

.payatable table {
  border-collapse: collapse;
}

.paydtable>table,.paydtable>table>td,.paydtable>table>td>th {
  border: 1px solid #ddd;
}

.paydtable table {
  border-collapse: collapse;
}

.popupmodel>table,,.popupmodel>table>td,.popupmodel>table>td>th {
  border: 1px solid white;
}

.popupmodel table {
  border-collapse: collapse;
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
                <li><a href="../payroll/deduction"><span class="underline-on-hover">Payroll Deduction Summary</span> </a></li>                    
                <li><label>Create Payroll Deduction</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;margin-bottom: 30px;" class="box-title">Create Payroll Deduction</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../payroll/deduction'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form1" action="/track/PayrollServlet?Submit=SaveDetection"  method="post" onsubmit="return validateDeduction()">
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="Deduction_numberOfDecimal" value=<%=numberOfDecimal%>>
				<input type="hidden" name="Deduction_empid" value="">
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Month</label>
					</div>
					<div class="col-lg-4 form-group">
						<select name="Deduction_month" onclick="validatemonthyear()" class="form-control">
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
					
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Year</label>
					</div>
					<div class="col-lg-4 form-group">
						<select class="form-control text-left Deduction_year"  value="<%=cyear%>" name="Deduction_year" onclick="validatemonthyear()">
							<!-- <option selected value=''>--Select Year--</option> -->
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Deduction Name</label>
					</div>
					<div class="col-lg-4 form-group">
						<input type="text" class="ac-selected form-control typeahead" id="Deduction_name" name="Deduction_name" onclick="deductpopup(this)" placeholder="Select a deduction">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'Deduction_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Employee</label>
					</div>
					<div class="col-lg-4 form-group">
						<input type="text" class="ac-selected form-control typeahead" id="Deduction_employee" name="Deduction_employee" placeholder="Select a employee">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'Deduction_employee\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label>Employee Type</label>
					</div>
					<div class="col-lg-4 form-group">
						<input name="Deduction_emptype" class="form-control text-left"  type="text" readonly> 
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Addition Date</label>
					</div>
					<div class="col-lg-4 form-group">
						<input class="form-control datepicker" name="Deduction_date" type="text" value="<%=curDate%>" READONLY>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required">Addition Amount</label>
					</div>
					<div class="col-lg-4 form-group">
						<input class="form-control" name="Deduction_amount" type="text" value="<%=zeroval%>" onchange="validateamount(this)">
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-3 form-group">
						<label class="required dueamount">Deduction Per Month</label>
					</div>
					<div class="col-lg-4 form-group">
						<input class="form-control" name="Deduction_dueamount" type="text" value="<%=zeroval%>" onchange="validateamount(this)">
					</div>
				</div>
				
				
				<div class="row">
					<div class="col-lg-3 form-group">
						<label>Loan Against Gratuity?</label>
					</div>
					<div class="col-lg-4 form-group">
						<input type="hidden" name="Deduction_isgratuity" value="0"> 
						<input type="checkbox" name="Deduction_gratuity" onclick="setgratuity(this)">
					</div>
				</div>
				
				

				<div class="row form-group">      
					<div class="col-sm-offset-3  col-sm-9 txn-buttons">
						<button type="button" class="btn btn-default" onclick="window.location.href='../payroll/creatededuction'">Clear</button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>

	
	</div>
	<div>
	
	</div>
</div>

</div>
<!-- Modal -->
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="newBankModal.jsp"%>
	<%@include file="newPaymentTypeModal.jsp"%>
	<%@include file="Payrolladdmstmodel.jsp"%>
<!-- Modal -->




<script>
$(document).ready(function(){
	var plant = document.form1.plant.value;
	
    var start = new Date().getFullYear();
    var end = parseFloat(start)+parseFloat("100");
    var options = "";
    for(var year = start ; year <=end; year++){
    	  $('.pay_year')
          .append($("<option></option>")
                     .attr("value", year)
                     .text(year)); 
    }

    for(var year = start ; year <=end; year++){
  	  $('.Deduction_year')
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
						ACTION : "GET_EMPLOYEE_DATA",
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
		    	return '<p onclick="setemployee(\''+data.ID+'\',\''+data.EMPTYPE+'\')">' + data.EMPNO+' - '+data.FNAME +'</p>';
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
	
	/* Employee Auto Suggestion */
	$('#Deduction_employee').typeahead({
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
						ACTION : "GET_EMPLOYEE_DATA",
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
		    	return '<p onclick="setemployeedecuction(\''+data.ID+'\',\''+data.EMPTYPE+'\')">' + data.EMPNO+' - '+data.FNAME +'</p>';
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
	
	
    $('#Deduction_name').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'ADDITION_NAME',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/HrPayrollAdditionMstServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_PAYROLL_ADDTION_MST_DROPDOWN",
					DTYPE : "deduct",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PAYADDMSTLIST);
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
		    		return '<p>' + data.ADDITION_NAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#PayaddmstModal"><a href="#"> + New Addition</a></div>');
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

$('.pay_addname').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{		  
	  display: 'ADDITION_NAME',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/HrPayrollAdditionMstServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				CMD : "GET_PAYROLL_ADDTION_MST_DROPDOWN",
				DTYPE : "add",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PAYADDMSTLIST);
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
	    		return '<p>' + data.ADDITION_NAME + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#PayaddmstModal"><a href="#"> + New Addition</a></div>');
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
	
/* Payment Mode Auto Suggestion */
$("#payment_mode").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{	  
	  display: 'PAYMENTTYPE',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_PAYMENT_TYPE_LIST",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.payTypes);
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
			return '<p>' + data.PAYMENTTYPE + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Mode</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();	  
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		var menuElement = $(this).parent().find(".tt-menu");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});
	
/* Paid Through Auto Suggestion */
$("#paid_through_account_name").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	  classNames: {
		 	menu: 'smalldrop'
		  }
	},
	{	  
	  display: 'text',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ChartOfAccountServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "getSubAccountTypeGroup",
				module:"billpaymentpaidthrough",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.results);
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
		suggestion: function(item) {
			if (item.issub) {
				var $state = $(
					    '<span onclick="isbankcharge(this,\''+item.text+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
					  );
				}
			else
				{
				var $state = $(
						 '<span onclick="isbankcharge(this,\''+item.text+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
					  );
				}
			  
			  return $state;
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".smalldrop");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".smalldrop").css('display') != "block")
			menuElement.next().hide();	  
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		var menuElement = $(this).parent().find(".smalldrop");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		var menuElement = $(this).parent().find(".smalldrop");
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});
	
$(".bankAccountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'NAME',   
	  source: function (query, process,asyncProcess) {
		  var urlStr = "/track/BankServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
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
			return '<span><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	}).on('typeahead:open',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 150);
	});
	
	
/* 	$('.sdatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	    beforeShowDay: function(date){
	        var string = jQuery.datepicker.formatDate('dd/mm/yy', date);
	        return [ datesForDisable.indexOf(string) == -1 ]
	    }
	});
	
	$('.edatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	    beforeShowDay: function(date){
	        var string = jQuery.datepicker.formatDate('dd/mm/yy', date);
	        return [ datesForDisable.indexOf(string) == -1 ]
	    }
	});  */
	
	/* $( ".sdatepickerleave" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
		
	$( ".edatepickerleave" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  }); */
	  
	$('.sdatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	});
	
	$('.edatepickerleave').datepicker({
		 dateFormat: 'dd/mm/yy',
	});

});



function onNewpayroll()
{
	var plant = document.form1.plant.value;	
	var urlStr = "/track/PayrollServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : plant,
		Submit : "PAYROLL"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.payroll;
				document.form.pay_no.value= resultV;
	
			} else {
				alert("Unable to genarate PAYROLL NO");
				document.form.pay_no.value = "";
			}
		}
	});	
	
}

function setemployee(id,emptype){
	$('input[name ="pay_empid"]').val(id);
	$('input[name ="pay_emptype"]').val(emptype);
	var fromdate = document.form.pay_fromdate.value;
	var todate = document.form.pay_todate.value;
	var month = document.form.pay_month.value;
	var year = document.form.pay_year.value;
	var numberOfDecimal = document.form.pay_numberOfDecimal.value;
	
	var urlStr = "/track/PayrollServlet";
	
	$.ajax( {
		type : "POST",
		url : urlStr,
		data: {
			Submit:"CALCULATE_EMPLOYEE_SALARY",
			EMPID:id,
			FROMDATE:fromdate,
			TODATE:todate,
			MONTH:month,
			YEAR:year	
		},
        success: function (data) {
        	var objc = JSON.parse(data);
        	console.log(objc);
        	$("#empsalarytypes").html("");
        	if(objc.STATUS == "REPEAT"){
        		$("#Deduction_employee").typeahead('');
        		$(".paydeduct_table tbody").html("");
        		$("#empsalarytypes").html("");
        		alert("Payroll alredy generated for this employee for the month of "+GetMonthName(month)+"-"+year+".");
        		
        	}else if(objc.STATUS == "OK"){
        		var body = "";
        		var addbody = "";
        		var subbody = "";
	        	$.each(objc.SALARYALLOWANCE,function(i,v){
	        		var stype = v.SALARYTYPE;
	        		var stypeid = stype.replace(/\s/g,'');
	        		body += '<div class="row">';
	        		body += '<div class="col-lg-3 form-group">';
	        		body += '<label class="required">'+v.SALARYTYPE+'</label>';
	        		body += '</div>';
	        		body += '<div class="col-lg-4 form-group">';
	        		body += '<input name="'+stypeid+'_'+id+'_A" type="text" value="'+v.ID+'" hidden>'; 
	        		body += '<input name="'+stypeid+'_'+id+'_B" class="form-control text-left"  type="text" value="'+parseFloat(v.SALARY).toFixed(numberOfDecimal)+'" readonly>'; 
	        		body += '</div>';
	        		body += '</div>';
						
	        	});
	        	
	        	/* $.each(objc.PAYADD,function(i,v){
	        		addbody +='<tr>';
	        		addbody +='<td class="text-center">';
	        		addbody +='<input class="form-control text-left" name="pay_addname" value="'+v.DEDUCTION_NAME+'" type="text" readonly>'; 
	        		addbody +='</td>';
	        		addbody +='<td class="text-center">';
	        		addbody +='<input class="form-control text-left" type="text" value="'+v.DEDUCTION_AMOUNT+'" name="pay_addamount" readonly>';
	        		addbody +='</td>';
	        		addbody +='</tr>';	
	        	}); */
	        	
	        	$.each(objc.PAYSUB,function(i,v){
	        		subbody +='<tr>';
	        		subbody +='<td class="text-center">';
	        		subbody +='<input name="pay_deductid" value="'+v.ID+'" type="text" hidden>'; 
	        		subbody +='<input class="form-control text-left" name="pay_deductname" value="'+v.DEDUCTION_NAME+'" type="text" readonly>'; 
	        		subbody +='</td>';
	        		subbody +='<td class="text-center">';
	        		subbody +='<input class="form-control text-left" type="text" value="'+parseFloat(v.DUE_AMOUNT).toFixed(numberOfDecimal)+'" name="pay_deductamount" readonly>';
	        		subbody +='</td>';
	        		subbody +='<td class="text-center">';
	        		subbody +='<a href="#" class="fa fa-info-circle" onclick="loaddeductdetail(\''+v.HDRID+'\')"></a>';
	        		subbody +='</td>';
	        		subbody +='</tr>';	
	        	});
	        	
	        	/* $(".payadd_table tbody").html(""); */
	        	$(".paydeduct_table tbody").html("");
	        	/* $(".payadd_table tbody").append(addbody); */
	        	$(".paydeduct_table tbody").append(subbody);
	        	
	        	$('input[name ="pay_attendance"]').val(parseFloat(objc.ATTDAYS).toFixed(1));
	        	$('input[name ="pay_netamount_h"]').val(parseFloat(objc.NETAMOUNT).toFixed(numberOfDecimal));
	        	$('input[name ="pay_netamount"]').val(parseFloat(objc.NETAMOUNT).toFixed(numberOfDecimal));
        		$("#empsalarytypes").append(body);
        		netamountcal();
        	}else{
        		alert("Error in payroll creation");
        	}

        }
		
	});
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

function validatemonthyeardate(){
	var fromdate = document.form.pay_fromdate.value;
	var todate = document.form.pay_todate.value;
	var month = document.form.pay_month.value;
	var year = document.form.pay_year.value;
	
	if(fromdate == "" || todate == "" || month == "" || year == ""){
		$("#pay_employee").prop('disabled', true);
		$("#pay_employee").css("background-color", "#eeeeee");
	}else{
		$("#pay_employee").prop('disabled', false);
		$("#pay_employee").css("background-color", "white");
	}
	
	
}

function setemployeedecuction(id,emptype){
	$('input[name ="Deduction_empid"]').val(id);
	$('input[name ="Deduction_emptype"]').val(emptype);
}


function validatemonthyear(){
	var month = document.form1.Deduction_month.value;
	var year = document.form1.Deduction_year.value;
	
	if(month == "" || year == ""){
		$("#Deduction_employee").prop('disabled', true);
		$("#Deduction_employee").css("background-color", "#eeeeee");
	}else{
		$("#Deduction_employee").prop('disabled', false);
		$("#Deduction_employee").css("background-color", "white");
	}
	
	
}

function setgratuity(obj) {
	var zval = "<%=zeroval%>";
	if ($(obj).is(":checked")) {
		$("input[name ='Deduction_isgratuity']").val("1");
		$("input[name ='Deduction_dueamount']").val(zval);
		$("input[name ='Deduction_dueamount']").prop('readonly', true);
		$( ".dueamount" ).removeClass( "required" );
	} else {
		
		$("input[name ='Deduction_isgratuity']").val("0");
		$("input[name ='Deduction_dueamount']").prop('readonly', false);
		$( ".dueamount" ).addClass( "required" );
	}
}

function validateDeduction(){
	
	var dmonth = document.form1.Deduction_month.value;
	if(dmonth == ""){
		alert("Please select deduction month.");
		document.form1.Deduction_month.focus();
		return false;
	}
	
	var dyear = document.form1.Deduction_year.value;
	if(dyear == ""){
		alert("Please select deduction year .");
		document.form1.Deduction_year.focus();
		return false;
	}
	
	var dname = document.form1.Deduction_name.value;
	if(dname == ""){
		alert("Please select deduction name.");
		document.form1.Deduction_name.focus();
		return false;
	}
	
	var demployee = document.form1.Deduction_employee.value;
	if(demployee == ""){
		alert("Please select employee.");
		document.form1.Deduction_employee.focus();
		return false;
	}
	
	var ddate = document.form1.Deduction_date.value;
	if(ddate == ""){
		alert("Please select date .");
		document.form1.Deduction_date.focus();
		return false;
	}
	
	var damount = document.form1.Deduction_amount.value;
	if(damount == ""){
		alert("Please enter deduction amount.");
		document.form1.Deduction_amount.focus();
		return false;
	}
	

	var isg = document.form1.Deduction_isgratuity.value;
	
	if(isg == "0"){
		var ddueamount = document.form1.Deduction_dueamount.value;
		if(ddueamount == ""){
			alert("Please enter deduction due amount.");
			document.form1.Deduction_dueamount.focus();
			return false;
		}
	}
	
	return true;
}

function validatePayroll(){
	var numberOfDecimal = document.form.pay_numberOfDecimal.value;
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	
	var payrollno = document.form.pay_no.value;
	if(payrollno == ""){
		alert("Please enter payroll number.");
		document.form.pay_no.focus();
		return false;
	}
	
	var fromdate = document.form.pay_fromdate.value;
	if(fromdate == ""){
		alert("Please select from date.");
		document.form.pay_fromdate.focus();
		return false;
	}
	
	var todate = document.form.pay_todate.value;
	if(todate == ""){
		alert("Please select to date.");
		document.form.pay_todate.focus();
		return false;
	}
	
	var pmonth  = document.form.pay_month.value;
	if(pmonth == ""){
		alert("Please select month.");
		document.form.pay_month.focus();
		return false;
	}
	
	var pyear = document.form.pay_year.value;
	if(pyear == ""){
		alert("Please select year.");
		document.form.pay_year.focus();
		return false;
	}
	
	var eployee = document.form.pay_employee.value;
	if(eployee == ""){
		alert("Please select employee.");
		document.form.pay_employee.focus();
		return false;
	}
	
	var pdate = document.form.pay_date.value;
	if(pdate == ""){
		alert("Please select date.");
		document.form.pay_date.focus();
		return false;
	}
	
	var attdays = document.form.pay_attendance.value;
	if(attdays == ""){
		alert("Please enter attendance days.");
		document.form.pay_attendance.focus();
		return false;
	}
	
	var netamount = document.form.pay_netamount.value;
	if(netamount == ""){
		alert("Please enter net amount.");
		document.form.pay_netamount.focus();
		return false;
	}
	
	var pmode = document.form.payment_mode.value;
	if(pmode == ""){
		alert("Please enter payment mode.");
		document.form.payment_mode.focus();
		return false;
	}
	
	var paidthrough = document.form.paid_through_account_name.value;
	if(paidthrough == ""){
		alert("Please enter paid through.");
		document.form.paid_through_account_name.focus();
		return false;
	}
	var ischeque = document.form.pay_ischeque.value;
	
	if(ischeque == "1"){
		var bankname = document.form.bankname.value;
		if(bankname == ""){
			alert("Please select bank.");
			document.form.bankname.focus();
			return false;
		}

		var chequeno = document.form.chequeno.value;
		if(chequeno == ""){
			alert("Please enter cheque number.");
			document.form.chequeno.focus();
			return false;
		}
		
		var cdate = document.form.chequedate.value;
		if(cdate == ""){
			alert("Please select cheque date.");
			document.form.chequedate.focus();
			return false;
		}
		
		var camount = document.form.chequeamount.value;
		if(camount == ""){
			alert("Please enter cheque amount.");
			document.form.chequeamount.focus();
			return false;
		}
	}
	
	var isItemValid = true;
	$("input[name ='pay_addamount']").each(function() {
		if($(this).val() == "" || $(this).val() == zeroval){
			$(this).focus();
			isItemValid = false;
	    }
	});
	
	if(!isItemValid){
		alert("Please enter addition amount.");
		return false;
	}
	
	return true;
}


function validateamount(obj)
{
	var invalue = $(obj).val();
	var numberOfDecimal = document.form1.Deduction_numberOfDecimal.value;
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(invalue.match(decimal) || invalue.match(numbers)) 
	{ 
		var amount=parseFloat(invalue).toFixed(numberOfDecimal);
		$(obj).val(amount);	
	}else{
		$(obj).val(zeroval);
		alert("Please Enter Valid Amount");
	}
	netamountcal();
}



function GetMonthName(monthNumber) {
    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    return months[monthNumber - 1];
}

function deductpopup(obj){
	var milliseconds = new Date().getTime();
	$(obj).attr('id', milliseconds);
	$('input[name = "pop_up_id"]').val(milliseconds);
	$('input[name = "pop_up_parent"]').val("DEDUCTION");
	
}
</script>

<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>