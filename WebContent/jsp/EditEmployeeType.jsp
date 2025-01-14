<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.HrEmpType"%>
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
	String title = "Edit Employee Type";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
	HrEmpType hrEmpType = hrEmpTypeDAO.getEmployeetypeById(plant, Integer.parseInt(Id));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Employee Type" />
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
                <li><a href="../payroll/employeetype"><span class="underline-on-hover">Employee Type Summary</span> </a></li>                                     
                <li><label>Edit Employee Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/employeetype'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form"
				action="/track/EmployeeTypeServlet?Submit=Update" method="post" onsubmit="return validateEmployeetype()">
				<input type="text" name="plant" value="<%=plant%>" hidden> <input
					type="text" name="username" value=<%=username%> hidden> <input
					type="text" name="etid" value="<%=Id%>" hidden>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Employee
						Type</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="emptype"  maxlength="50"
							value="<%=hrEmpType.getEMPLOYEETYPE()%>">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Employee
						Type Description</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="emptypedesc"  maxlength="100"
							value="<%=hrEmpType.getEMPLOYEETYPEDESC()%>">
					</div>
				</div>
				<div class="form-group">
  					<div class="col-sm-offset-4 col-sm-8">
   						 <label class="radio-inline">
    						  <input type="radio" id="ractive" name="ACTIVE" value="Y"<%if (hrEmpType.getIsActive().equalsIgnoreCase("Y")) {%> checked <%}%>>Active
  					     </label>
    					<label class="radio-inline">
     						 <input type="radio" name="ACTIVE" value="N"<%if (hrEmpType.getIsActive().equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    					</label>
  					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
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
	
	function validateEmployeetype(){
		
		var emptype=$('input[name = "emptype"]').val();
		var emptypedesc=$('input[name = "emptypedesc"]').val();
		
		
		if(emptype == ""){
			alert("Please enter employee type.");
			document.form.emptype.focus();
			return false;
		}
		
		if(emptypedesc == ""){
			alert("Please enter employee type description.");
			document.form.emptypedesc.focus();
			return false;
		}
		
		return true;
	}
	
	function onClear()
	{
		document.form.emptype.value="";
		document.form.emptypedesc.value="";
		$('input:radio[id=ractive]').prop('checked', true);
	}
		
</script>
