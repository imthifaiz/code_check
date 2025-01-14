<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%
String title = "New Shipment";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String pono = StrUtils.fString(request.getParameter("PONO"));
String shipmentCode = StrUtils.fString(request.getParameter("SHIPMENTCODE"));
String cmd = StrUtils.fString(request.getParameter("cmd"));
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script src="js/Shipment.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

			<div class="container-fluid">
	<form id="createShipmentForm" class="form-horizontal" name="form"  action="/track/MasterServlet?action=ADD_SHIPMENT"  method="post" enctype="multipart/form-data" onsubmit="return validateShipment()">
	<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Shipment Code</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="shipmentCode" name="shipmentCode" value="<%=shipmentCode%>">				
				<span class="auto-icon" onclick="Get_ShipmentCode();"><i class="fa fa-cog" style="font-size: 20px; color: #0059b3"></i></span>
			</div>
		</div>	
		
	<input type="text" name="plant" value="<%=plant%>" hidden>
	<input type="text" name="username" value=<%=username%> hidden>
	<input type="hidden" name="cmd" value=<%=cmd%> >
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">PONO</label>
			<div class="col-sm-4">				
				<input type="text" class="form-control" id="pono" name="pono" value="<%=pono%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
		<input name="Submit" value="Save" hidden>
		<input name="bill_status" value="Save" hidden>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
		
		</form>
		<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>