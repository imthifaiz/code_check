<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.HrHolidayMst"%>
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
	String title = "Edit Holiday";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	HrHolidayMstDAO hrHolidayMstDAO = new HrHolidayMstDAO();
	HrHolidayMst  hrHolidayMst = hrHolidayMstDAO.getHolidayById(plant, Integer.parseInt(Id));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Holiday" />
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
                <li><a href="../payroll/holiday"><span class="underline-on-hover">Holiday Summary</span> </a></li>
                <li><label>Edit Holiday</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/holiday'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form"
				action="/track/HrHolidayServlet?Submit=Update" method="post" onsubmit="return validateholiday()">
				<input type="text" name="plant" value="<%=plant%>" hidden> 
				<input type="text" name="username" value=<%=username%> hidden> 
				<input type="text" name="hid" value="<%=Id%>" hidden>
				<input type="text" name="hdate" value="<%=hrHolidayMst.getHOLIDAY_DATE()%>" hidden>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Holiday Date</label>
					<div class="col-sm-6">
						<input class="form-control text-left datepicker" onchange="dateselect(this)" name="holidaydate" type="text" value="<%=hrHolidayMst.getHOLIDAY_DATE()%>" placeholder="Select Date" readonly >
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Holiday Description</label>
					<div class="col-sm-6">
						<input class="form-control text-left" type="text" name="holidaydesc" placeholder="Max 100 characters" value="<%=hrHolidayMst.getHOLIDAY_DESC()%>" maxlength="100">
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
					 	<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default" onclick="window.location.href='EmployeeTypeSummary.jsp'">Cancel</button> -->
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
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
    
});

function onClear()
{
	document.form.holidaydate.value = "";
	document.form.holidaydesc.value = "";
}

	function dateselect(obj){
		var hdate = $(obj).val();
		$.ajax({
	    		type : "GET",
	    		url: '/track/HrHolidayServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_HOLIDAY_DATE_EDIT",
	    			hdate : hdate,
	    			hid : document.form.hid.value
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				$('input[name = "holidaydate"]').val($('input[name = "hdate"]').val());
	    				alert("Holiday date alredy exist.");
	    			}
	    		}
	    	});	
	} 
	
	function validateholiday(){
		
		var holidaydate=$('input[name = "holidaydate"]').val();
		
		
		if(holidaydate == ""){
			alert("Please enter holiday Date.");
			document.form.holidaydate.focus();
			return false;
		}

		
		return true;
	}
		
</script>
