<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.HrEmpDepartmentMst"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Edit Department";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	HrDepartmentDAO hrDepartmentDAO = new HrDepartmentDAO();
	HrEmpDepartmentMst hrEmpType = hrDepartmentDAO.getDepartmentById(plant, Integer.parseInt(Id));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.DEPARTMENT%>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<script type="text/javascript" src="js/general.js"></script>
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
                <li><a href="../payroll/department"><span class="underline-on-hover">Department Summary</span> </a></li>   
                <li><label>Edit Department</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/department'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form"
				action="/track/HrDepartmentServlet?Submit=Update" method="post" onsubmit="return validateDepartment()">
				<input type="text" name="plant" value="<%=plant%>" hidden> <input
					type="text" name="username" value=<%=username%> hidden> <input
					type="text" name="etid" value="<%=Id%>" hidden>
					<input
					type="text" name="edit_department" value="<%=hrEmpType.getDEPARTMENT()%>" hidden>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">DEPARTMENT</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="" readonly onchange="checkitem(this.value)" maxlength="100"
							value="<%=hrEmpType.getDEPARTMENT()%>">
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
						<!-- <button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp; -->
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
	
	function validateDepartment(){
		var isItemValid = true;
		
		$("input[name ='department']").each(function() {
		    if($(this).val() == ""){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The Department field cannot be empty.");
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
		return true;
	}
	
	function onClear()
	{
		document.form.department.value="";
		$('#ACTIVE_Y').prop('checked', false);
		$('#ACTIVE_N').prop('checked', false);
	}
	

	function checkitem(itemvalue)
	{	
		
		 if(itemvalue!="" || itemvalue.length>0 ) {
			
			 if(document.form.edit_department.value!==itemvalue)
			 {
			var urlStr = "/track/HrDepartmentServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					QUERY : itemvalue,
					PLANT : "<%=plant%>",
					CMD : "DEPARTMENT_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							alert("Department Already Exists");
							document.form.department.value="";
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
