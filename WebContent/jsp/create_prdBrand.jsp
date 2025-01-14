<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ include file="header.jsp"%>


<%
String title = "Create Product Brand";
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<SCRIPT LANGUAGE="JavaScript">


function onGenID(){
    
	   document.form1.action  = "/track/ProductBrandServlet?action=Auto_ID";
	   document.form1.submit();
	}
function onAdd(){
	   var ITEM_ID   = document.form1.ITEM_ID.value;
	   var ITEM_DESC = document.form1.ITEM_DESC.value;
	    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Brand ID");document.form1.ITEM_ID.focus(); return false; }
	    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Product Brand Description");document.form1.ITEM_DESC.focus(); return false; }
	   document.form1.action  = "/track/ProductBrandServlet?action=ADD";
	   document.form1.submit();
	}

function onClear(){
	document.form1.ITEM_ID.value = "";
	document.form1.ITEM_DESC.value = "";
}
	
</SCRIPT>


<%
String responseMsg = request.getParameter("response");
String productBrandID = request.getParameter("brandID");
String productBrandDesc = request.getParameter("brandDesc");
String sSAVE_RED = request.getParameter("SAVE_RED");
if(responseMsg == null)
	responseMsg = "";

if(productBrandID == null)
	productBrandID = "";

if(productBrandDesc == null)
	productBrandDesc = "";      
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../productbrand/summary"><span class="underline-on-hover">Product Brand Summary</span></a></li>                        
                <li><label>Create Product Brand</label></li>                                   
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

  <form class="form-horizontal" name="form1" method="post" action = "/track/ProductBrandServlet">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Brand ID">Product Brand ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_ID" type="TEXT" value="<%=productBrandID%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Brand Description">Product Brand Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Brand Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=productBrandDesc%>"
			size="50" MAXLENGTH=100>
      </div> 
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
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
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../productbrand/summary?PGaction=View&result=Product Brand Created Successfully";
	document.form1.submit();
	}
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
