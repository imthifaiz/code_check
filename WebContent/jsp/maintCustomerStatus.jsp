<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Customer Status";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	

	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'CustomerStatus',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	/*function onNew() {
		document.form1.action = "/track/CustomerStatusServlet?action=NEW";
		document.form1.submit();
	}
	
	function onAdd() {
		var CUSTOMER_STATUS_ID   = document.form1.CUSTOMER_STATUS_ID.value;
        document.form1.action = "/track/locmstservlet?action=ADD";
		document.form1.submit();
	}*/
	function onUpdate() {

		var CUSTOMER_STATUS_ID   = document.form1.CUSTOMER_STATUS_ID.value;
		if (CUSTOMER_STATUS_ID == "" || CUSTOMER_STATUS_ID == null) {
			alert("Please Enter Customer Status ID");
			document.form1.CUSTOMER_STATUS_ID.focus();
			return false;
		}
		var radio_choice = false;

		// Loop from zero to the one minus the number of radio button selections
		for (i = 0; i < document.form1.ACTIVE.length; i++) {
			if (document.form1.ACTIVE[i].checked)
				radio_choice = true;
		}

		if (!radio_choice) {
			// If there were no selections made display an alert box 
			alert("Please select Active or non Active mode.")
			return (false);
		}
		var chk = confirm("Are you sure you would like to save?");
		if (chk) {
			document.form1.action = "/track/CustomerStatusServlet?action=UPDATE&CUSTOMERSTATUSID="
					+ CUSTOMER_STATUS_ID;
			document.form1.submit();
		} else {
			return false;
		}
	}
	function onDelete() {
		var CUSTOMER_STATUS_ID   = document.form1.CUSTOMER_STATUS_ID.value;
		if (CUSTOMER_STATUS_ID == "" || CUSTOMER_STATUS_ID == null) {
			alert("Please Enter Customer Status Id");
			return false;
		}
		var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
			document.form1.action = "/track/CustomerStatusServlet?action=DELETE&CUSTOMERSTATUSID="
					+ CUSTOMER_STATUS_ID;
			document.form1.submit();
		} else {
			return false;
		}
	}

	function onView() {

		var CUSTOMER_STATUS_ID   = document.form1.CUSTOMER_STATUS_ID.value;
		if (CUSTOMER_STATUS_ID == "" || CUSTOMER_STATUS_ID == null) {
			alert("Please Enter Customer Status Id");
			return false;
		}
		document.form1.action = "/track/CustomerStatusServlet?action=VIEW";
		document.form1.submit();

	}

	function onClear() {
		document.form1.CUSTOMER_STATUS_ID.value = "";
		document.form1.DESC.value = "";
		document.form1.REMARKS.value = "";
				
	}

</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sCustomerStatusId = "", sDesc = "", sRemarks = "";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	sCustomerStatusId  = StrUtils.fString(request.getParameter("CUSTOMER_STATUS_ID"));
	sDesc  = StrUtils.fString(request.getParameter("DESC"));
	sRemarks  = StrUtils.fString(request.getParameter("REMARKS"));
	String action = "";
	String sLocId = "", sLocDesc = "",  isActive = "", res = "";
	action = strUtils.fString(request.getParameter("action"));
	
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	
	if (action.equalsIgnoreCase("Clear")) {

		action = "";
		sCustomerStatusId = "";
		sDesc = "";
		sRemarks = "";
		
	}  else if (action.equalsIgnoreCase("SHOW_RESULT")) {

		   res = request.getParameter("result");
        } else if (action.equalsIgnoreCase("UPDATE")) {

		sCustEnb = "disabled";

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Customer Status ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Status ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUSTOMER_STATUS_ID" type="TEXT" value="<%=sCustomerStatusId%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="javascript: popUpWin('Customer_Status_List.jsp?CUSTOMERSTATUID=' + form1.CUSTOMER_STATUS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Status Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUSTOMER_STATUS_ID1"
			value="<%=sCustomerStatusId%>">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Customer Status Description">Customer Status Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DESC" type="TEXT" value="<%=sDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="return onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"<%=sUpdateEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onDelete();"><b>Delete</b></button>&nbsp;&nbsp;
      	      </div>
    </div>
  </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>