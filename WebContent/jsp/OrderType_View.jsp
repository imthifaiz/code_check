<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Create Order Type";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'ORDERTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew(){
		
		document.form1.ORDERTYPE.value = ""; 
		document.form1.ORDERDESC.value = "";
		document.form1.TYPE.value = ""; 
		document.form1.REMARKS.value = "";
   		/* document.form1.action  = "/track/OrderTypeServlet?action=NEW";
   		document.form1.submit(); */
	}
	
	function onAdd(){
   		var ORDERTYPE   = document.form1.ORDERTYPE.value;
                var ORDERDESC   = document.form1.ORDERDESC.value;
                if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType");	document.form1.ORDERTYPE.focus(); return false; }
                if(ORDERDESC == "" || ORDERDESC == null) {alert("Please Enter Order Description"); document.form1.ORDERDESC.focus(); return false; }
   		document.form1.action  = "/track/OrderTypeServlet?action=ADD";
   		document.form1.submit();
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
	String sOrderType = "", sOrderDesc = "", sType="",sRemarks = "";
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
	}

	else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../ordertype/summary"><span class="underline-on-hover">Order Type Summary</span></a></li>                       
                <li><label>Create Order Type</label></li>                                   
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
      	    <input name="ORDERTYPE"  id="ORDERTYPE"type="TEXT" value="<%=sOrderType%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50 class="form-control"<%=sCustEnb%>>
      	
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Order Type Description">Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ORDERDESC" id="ORDERDESC" type="TEXT" value="<%=sOrderDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Order Type">Type</label>
      <div class="col-sm-4">           	
  	<select name="TYPE" id="TYPE" class="form-control" data-toggle="dropdown" data-placement="right" style="width: 100%">
		<option value="PURCHASE">PURCHASE</option>
		<option value="BILL">BILL</option>
		<option value="SALES">SALES</option>
		<% if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
		<option value="INVOICE">INVOICE</option>
		<%} %>
		<option value="SALES ESTIMATE">SALES ESTIMATE</option>
		<!-- <option value="TAX INVOICE">TAX INVOICE</option> -->
		<option value="CONSIGNMENT">CONSIGNMENT</option>
		<!-- <option value="RENTAL">RENTAL</option> -->
		<option value="OTHERS">OTHERS</option>
		</select>
  	</div>
  	  </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" id="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>>Save</button>&nbsp;&nbsp;
      	

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