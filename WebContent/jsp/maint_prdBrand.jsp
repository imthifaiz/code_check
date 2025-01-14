
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>

<%@ include file="header.jsp"%>

<%
String title = "Edit Product Brand";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
	 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
function onClear(){
	document.form.PRD_BRAND_ID.value = "";
	document.form.PRD_BRAND_DESC.value = "";
}

function onUpdate(){
	 
    var ITEM_ID   = document.form.PRD_BRAND_ID.value;
    var ITEM_DESC = document.form.PRD_BRAND_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product BrandID ");document.form.PRD_BRAND_ID.focus(); return false; }
    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Product Brand Description");document.form.PRD_BRAND_DESC.focus(); return false; } 

  var chkmsg=confirm("Are you sure you would like to save?");
    if(chkmsg){
   document.form.action  = "/track/ProductBrandServlet?action=UPDATE";
   document.form.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var ITEM_ID   = document.form.PRD_BRAND_ID.value;
	if (ITEM_ID == "" || ITEM_ID == null) {
		alert("Please Enter Product BrandID");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form.action = "/track/ProductBrandServlet?action=DELETE";
		document.form.submit();
	} else {
		return false;
	}
}


</script>
<%
String isActive = request.getParameter("isActive");
String responseMsg = request.getParameter("response");
String productBrandID = request.getParameter("brandID");
String productBrandDesc = request.getParameter("brandDesc");

if(responseMsg == null)
	responseMsg = "";

if(productBrandID == null)
	productBrandID = "";

if(productBrandDesc == null)
	productBrandDesc = "";
if(isActive == null)
	isActive = "";
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../productbrand/summary"><span class="underline-on-hover">Product Brand Summary</span></a></li>                         
                <li><label>Edit Product Brand</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../productbrand/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=responseMsg%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post" action = "/track/ProductBrandServlet">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Brand ID">Product Brand ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="PRD_BRAND_ID" type="TEXT" value="<%=productBrandID%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  
   		 	onClick="javascript:popWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  	
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Brand Description">Product Brand Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PRD_BRAND_DESC" type="TEXT" value="<%=productBrandDesc%>"
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
<!--      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onDelete();">Delete</button>&nbsp;&nbsp;

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
