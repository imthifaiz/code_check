<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.HrEmpSalaryMst"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Edit Salary Type";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	String pcountry = StrUtils.fString((String) session.getAttribute("COUNTRY"));
	HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	HrEmpSalaryMst hrEmpType = hrEmpSalaryDAO.getSalaryById(plant, Integer.parseInt(Id));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALARY_TYPE%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
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
                <li><a href="../payroll/employeesalary"><span class="underline-on-hover">Salary Type Summary</span> </a></li>                
                <li><label>Edit Salary Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/employeesalary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form"
				action="/track/HrSalaryServlet?Submit=Update" method="post" onsubmit="return validateSalary()">
				<input type="text" name="plant" value="<%=plant%>" hidden> <input
					type="text" name="username" value=<%=username%> hidden> <input
					type="text" name="etid" value="<%=Id%>" hidden>
					<input
					type="text" name="edit_empSalary" value="<%=hrEmpType.getSALARYTYPE()%>" hidden>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Salary Type</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="empSalary" readonly onchange="checkitem(this.value)" maxlength="100"
							value="<%=hrEmpType.getSALARYTYPE()%>">
					</div>
				</div>	
				
				<div class="form-group">
				<div class="col-sm-offset-3 col-sm-9">
				 <label class="control-label col-form-label"> 
				 <input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
				 <% if(pcountry.equals("Singapore")) { %>
				   <input type="checkbox" value="1" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" <%if (hrEmpType.getISPAYROLL_BY_BASIC_SALARY()==1) {%> checked <%}%> onclick="payrollbybasicsalary(this)"> Deduct CPF Contribution&nbsp;&nbsp;
				   <%} else{%>  
       		  <input type="checkbox" value="1" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" <%if (hrEmpType.getISPAYROLL_BY_BASIC_SALARY()==1) {%> checked <%}%> onclick="payrollbybasicsalary(this)"> Deduct PF Contribution&nbsp;&nbsp;
              </label>
              <%}%> 
              </div>
              </div>
              
							
				<div class="form-group">
  					<div class="col-sm-offset-3 col-sm-9">
   						 <label class="radio-inline">
    						  <input type="radio" name="ACTIVE" id="ACTIVE_Y" value="Y"<%if (hrEmpType.getIsActive().equalsIgnoreCase("Y")) {%> checked <%}%>>Active
  					     </label>
    					<label class="radio-inline">
     						 <input type="radio" name="ACTIVE" id="ACTIVE_N" value="N"<%if (hrEmpType.getIsActive().equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    					</label>
  					</div>
				</div>
				<div class="row">
					<div class="col-sm-offset-3 col-sm-9">
						<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default"
							onclick="window.location.href='EmployeeSalarySummary.jsp'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<script>
	$(document).ready(function(){
	
	});
	
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
		var radio_choice = false;
	    for (i = 0; i < document.form.ACTIVE.length; i++)
	    {
	        if (document.form.ACTIVE[i].checked)
	        radio_choice = true; 
	    }
	    if (!radio_choice)
	    {
	    alert("Please select Active or non Active mode.")
	    return (false);
	    }
	    for (i = 0; i < document.form.ISPAYROLL_BY_BASIC_SALARY.length; i++)
	    {
	        if (document.form.ISPAYROLL_BY_BASIC_SALARY[i].checked)
	        radio_choice = true; 
	    }
		return true;
		
	}
	
	function onClear()
	{
		document.form.empSalary.value="";
		$('#ACTIVE_Y').prop('checked', false);
		$('#ACTIVE_N').prop('checked', false);
	}

	function payrollbybasicsalary(obj){
		var manageapp = $(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val();
		if(manageapp == 0)
		$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(1);
		else
		$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(0);
		
	}
	

	function checkitem(itemvalue)
	{	
		
		 if(itemvalue!="" || itemvalue.length>0 ) {
			
			 if(document.form.edit_empSalary.value!==itemvalue)
			 {
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
							document.form.empSalary.value="";
							return false;	
							
						} 
						else 
							return true;
						    
					}
				});
			 return true;
		 }
		 }
	}
		
</script>
