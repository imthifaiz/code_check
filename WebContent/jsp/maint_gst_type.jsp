<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit VAT/GST/TAX Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;


	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GSTTYPE',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	
	function onUpdate() {

		var GSTTYPE = document.form1.GSTTYPE.value;
		var GSTTYPE_HIDDEN = document.form1.GSTTYPE_HIDDEN.value;
		 if(GSTTYPE == "" || GSTTYPE == null)
		  {
		  alert("Please Enter VAT/GST/TAX Type");
		  document.form1.GSTTYPE.focus(); return false;
		  }
		 if(GSTTYPE_HIDDEN == "" || GSTTYPE_HIDDEN == null)
		  {
		  alert("Please Enter VAT/GST/TAX Type");
		  document.form1.GSTTYPE.focus(); return false;
		  }
		 
		if(isNaN(document.form1.GSTPERCENTAGE.value)) {alert("Please enter valid VAT/GST/TAX Percentage.");document.form1.GSTPERCENTAGE.focus(); return false;}

		
		var chk = confirm("Are you sure you would like to save?");
		if (chk) {
			document.form1.action = "/track/GstTypeServlet?action=UPDATE&GSTTYPE="
					+ GSTTYPE;
			document.form1.submit();
		} else {
			return false;
		}
	}
	function onDelete() {
		 var GSTTYPE = document.form1.GSTTYPE.value;

		 if(GSTTYPE == "" || GSTTYPE == null) {
			alert("Please Enter VAT/GST/TAX Type");
			return false;
		}
		var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
			document.form1.action = "/track/GstTypeServlet?action=DELETE&GSTTYPE="
					+ GSTTYPE;
			document.form1.submit();
		} else {
			return false;
		}
	}

	function onClear()
	{
		document.form1.GSTTYPE.value ="";
		document.form1.GSTDESC.value="";
		document.form1.GSTPERCENTAGE.value="";
		document.form1.REMARKS.value="";
		
	}

	

	
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";

	String action = "";
	String sGSTOrdetType="",sGSTType = "", sGSTDesc = "", sPercentage = "",sRemarks="";
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();

	try {

		action = strUtils.fString(request.getParameter("action"));

	} catch (Exception e) {

	}

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sGSTType = "";
		sGSTDesc = "";
		sPercentage = "";
		sRemarks = "";

	}  else if (action.equalsIgnoreCase("SHOW_RESULT")) {

		res = request.getParameter("result");

		Hashtable arrCust = (Hashtable) request.getSession()
				.getAttribute("gstTypeDataUpdate");
			
		sGSTType = (String) arrCust.get("GSTTYPE");
		sGSTDesc = (String) arrCust.get("GSTDESC");
		sPercentage = (String) arrCust.get("GSTPERCENTAGE");
		sRemarks = (String) arrCust.get("REMARKS");

	} else if (action.equalsIgnoreCase("UPDATE")) {

		sCustEnb = "disabled";

	}
	else if (action.equalsIgnoreCase("Edit")) {
			
		sGSTType = request.getParameter("GSTTYPE");
		sGSTDesc = request.getParameter("GSTDESC");
		sPercentage = request.getParameter("GSTPERCENTAGE");
		sRemarks = request.getParameter("REMARKS");

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../gst/summary"><span class="underline-on-hover">GST Type Summary</span></a></li>                       
                <li><label>Edit GST Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><label for="rcbno" id="TaxLabelOrderManagementTitle"></label></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../gst/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>	
  <form class="form-horizontal" name="form1" method="post">
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="rcbno" id="TaxLabelOrderManagement"></label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="GSTTYPE" type="TEXT" value="<%=sGSTType%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon" 
   		  onClick="javascript: popUpWin('../jsp/GSTType_List.jsp?GSTTYPE=' + form1.GSTTYPE.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="VAT/GST/TAX Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="GSTTYPE_HIDDEN" value="<%=sGSTType%>">
      	      </div>
    </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="VAT/GST/TAX Type Description">Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="GSTDESC" type="TEXT" value="<%=sGSTDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
   
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Percentage">Percentage:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="GSTPERCENTAGE" type="TEXT" value="<%=sPercentage%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"<%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;   	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML =  d +" Type :";
    document.getElementById('TaxLabelOrderManagementTitle').innerHTML = "Edit "+ d +" DETAIL :";
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>