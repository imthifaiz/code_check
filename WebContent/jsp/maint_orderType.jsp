<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Order Type";
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
						'ORDERTYPE',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}


	function onUpdate() {

		var ORDERTYPE = document.form1.ORDERTYPE.value;
                var ORDERDESC = document.form1.ORDERDESC.value;
	

		if (ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType");document.form1.ORDERTYPE.focus(); return false;}
                if(ORDERDESC == "" || ORDERDESC == null) {alert("Please Enter Order Description");document.form1.ORDERDESC.focus(); return false; }
		

		var radio_choice = false;

		// Loop from zero to the one minus the number of radio button selections
		for (i = 0; i < document.form1.ACTIVE.length; i++) {
			if (document.form1.ACTIVE[i].checked)
				radio_choice = true;
		}

		if (!radio_choice) {
			// If there were no selections made display an alert box 
			alert("Please select Active or non Active mode.");
			return (false);
		}
		var chk = confirm("Are you sure you would like to save?");
		if (chk) {
			document.form1.action = "/track/OrderTypeServlet?action=UPDATE&ORDERTYPE="
					+ ORDERTYPE;
			document.form1.submit();
		} else {
			return false;
		}
	}
	
	
	
	function onDelete() {
		var ORDERTYPE = document.form1.ORDERTYPE.value;
		if (ORDERTYPE == "" || ORDERTYPE == null) {
			alert("Please Enter OrderType");
			return false;
		}
		var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
			document.form1.action = "/track/OrderTypeServlet?action=DELETE&ORDERTYPE="
					+ ORDERTYPE;
			document.form1.submit();
		} else {
			return false;
		}
	}


	function onClear() {
		document.form1.ORDERTYPE.value = "";
		document.form1.ORDERDESC.value = "";
		document.form1.TYPE.value = "";
		document.form1.REMARKS.value = "";
	}
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";

	String action = "";
	String sOrderType = "", sOrderDesc = "", sType = "",sRemarks = "", isActive = "";

	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();

	try {

		action = strUtils.fString(request.getParameter("action"));

	} catch (Exception e) {

	}

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		action = "";
		sOrderType = "";
		sOrderDesc = "";
		sType = "";
		sRemarks = "";

	} else if (action.equalsIgnoreCase("SHOW_RESULT")) {

		 res = request.getParameter("result");

		Hashtable arrCust = (Hashtable) request.getSession()
				.getAttribute("orderTypeData");

		sOrderType = (String) arrCust.get("ORDERTYPE");
		sOrderDesc = (String) arrCust.get("ORDERDESC");
		sType = (String) arrCust.get("TYPE");
		sRemarks = (String) arrCust.get("REMARKS");
		isActive = (String) arrCust.get("ISACTIVE");

	} else if (action.equalsIgnoreCase("UPDATE")) {

		sCustEnb = "disabled";

	}
	else if (action.equalsIgnoreCase("Edit")) {
		sOrderType = request.getParameter("ORDERTYPE");
		sOrderDesc = request.getParameter("ORDERDESC");
		sType = request.getParameter("TYPE");
		isActive = request.getParameter("ISACTIVE");
		sRemarks = request.getParameter("REMARKS");
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../ordertype/summary"><span class="underline-on-hover">Order Type Summary</span></a></li>                       
                <li><label>Edit Order Type</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
		onclick="window.location.href='../ordertype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Order Type Master">Order Type</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Type:</label> -->
      <div class="col-sm-4">
      	    <div class="input-group">
    		<input name="ORDERTYPE" type="TEXT" value="<%=sOrderType%>"
			size="50" MAXLENGTH=50 class="form-control"<%=sCustEnb%> readonly>
   		 	<!-- <span class="input-group-addon"  onClick="javascript: popUpWin('../jsp/OrderType_list.jsp?CUST_NAME=' + form1.ORDERTYPE.value+'&FORMTYPE=notype');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="ORDERTYPE1" value="<%=sOrderType%>">
  		<INPUT type="hidden" name="EDIT_TYPE" value="<%=sType%>">
       </div>
    </div>
 
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Order Type Description">Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ORDERDESC" type="TEXT" value="<%=sOrderDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
 
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Order Type">Type</label>
      <div class="col-sm-4">           	
  	<select name="TYPE" class="form-control"  data-toggle="dropdown" data-placement="right" style="width: 100%">
		<option value="PURCHASE">PURCHASE</option>
		<option value="BILL">BILL</option>
		<option value="SALES">SALES</option>
		<% if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
		<option value="INVOICE">INVOICE</option>
		<%} %>
		<option value="SALES ESTIMATE">SALES ESTIMATE</option>
		<option value="CONSIGNMENT">CONSIGNMENT</option>
		<!-- <option value="TAX INVOICE">TAX INVOICE</option>
		<option value="RENTAL">RENTAL</option> -->
		<option value="OTHERS">OTHERS</option>
		</select>
  	</div>
  	  </div>
 
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"<%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onDelete();">Delete</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
    if(document.form1.EDIT_TYPE.value!="")
	{
	$("select[name ='TYPE']").val(document.form1.EDIT_TYPE.value);
	}
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>