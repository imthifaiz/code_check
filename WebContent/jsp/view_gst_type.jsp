<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Create VAT/GST/TAX Detail";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<style type="text/css">.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew(){
   		document.form1.action  = "/track/GstTypeServlet?action=NEW";
   		document.form1.submit();
	}

	function onClear()
	{
		document.form1.GSTTYPE.selectedIndex=0;
		document.form1.GSTDESC.value="";
		document.form1.GSTPERCENTAGE.value="";
		document.form1.REMARKS.value="";
		
	}
	function onAdd(){
   		
        
   		if(document.form1.GSTTYPE.selectedIndex==0)
   		{
           alert("Please Select VAT/GST/TAX TYPE");
           document.form1.GSTTYPE.focus();
           return false;
   		}

   		if(isNaN(document.form1.GSTPERCENTAGE.value)) {alert("Please Enter valid VAT/GST/TAX Percentage.");document.form1.GSTPERCENTAGE.focus(); return false;}
   		 		
   		
   		
   		document.form1.action  = "/track/GstTypeServlet?action=ADD";
   		document.form1.submit();
	}
	function onUpdate(){
		if(document.form1.GSTTYPE.selectedIndex==0)
   		{
           alert("Please choose VAT/GST/TAX TYPE");
           return false;
   		}
   		
   		document.form1.action  = "/track/GstTypeServlet?action=UPDATE&GSTTYPE=" + GSTTYPE;
  		document.form1.submit();
	}

	function onDelete(){
		if(document.form1.GSTTYPE.selectedIndex==0)
   		{
           alert("Please choose VAT/GST/TAX TYPE");
           return false;
   		}
   		document.form1.action  = "/track/GstTypeServlet?action=DELETE&GSTTYPE=" + GSTTYPE;
   		document.form1.submit();
   
	}
	function onView(){
		if(document.form1.GSTTYPE.selectedIndex==0)
   		{
           alert("Please choose VAT/GST/TAX TYPE");
           return false;
   		}
     document.form1.action  = "/track/GstTypeServlet?action=VIEW";
     document.form1.submit();
   
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
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	String sGSTType = "", sGSTDesc = "", sPercentage = "",sRemarks="";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		sGSTType = "";
		sGSTDesc = "";
		sPercentage = "";
		sRemarks = "";
	}  else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
		Hashtable arrCust = (Hashtable) request.getSession()
				.getAttribute("gstTypeDataAdd");
		sGSTType = (String) arrCust.get("GSTTYPE");
		sGSTDesc = (String) arrCust.get("GSTDESC");
		sPercentage = (String) arrCust.get("GSTPERCENTAGE");
		sRemarks = (String) arrCust.get("REMARKS");
		
	} else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../gst/summary"> <span class="underline-on-hover">GST Type Summary</span></a></li>                       
                <li>Create GST Detail</li>                                   
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
  	
  <SELECT NAME="GSTTYPE" style="width: 100%" class="form-control" data-toggle="dropdown" data-placement="right" maxlength="9">
			<OPTION selected value="0">Choose</OPTION>
			<OPTION value="PURCHASE">PURCHASE</OPTION>
			<OPTION value="SALES ESTIMATE">SALES ESTIMATE</OPTION>
			<OPTION value="SALES">SALES</OPTION>
			<OPTION value="CONSIGNMENT">CONSIGNMENT</OPTION>
			<OPTION value="RENTAL">RENTAL</OPTION>
	<!--  	<OPTION value=<%=sGSTType%> <%if(sGSTType.equalsIgnoreCase(sGSTType)){%> selected <%} %>><%=sGSTType%></OPTION> -->
			</SELECT>
  
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
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	

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
    document.getElementById('TaxLabelOrderManagementTitle').innerHTML = "Create "+ d +" DETAIL :";
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>