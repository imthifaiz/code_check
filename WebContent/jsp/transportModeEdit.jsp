<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>

<%@ include file="header.jsp"%>

<%
String title = "Edit Transport Mode";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script src="js/general.js"></script>
<script>

function popWin(URL) {
	 subWin = window.open(URL, 'TRANSPORT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
function onClear(){
	document.form.TRANSPORT_MODE_ID.value = "";
	document.form.TRANSPORT_MODE_DESC.value = "";
}

function onUpdate(){
	 
    var TRANSPORT_MODE_ID   = document.form.TRANSPORT_MODE_ID.value;
    var TRANSPORT_MODE_DESC = document.form.TRANSPORT_MODE_DESC.value;
    if(TRANSPORT_MODE_ID == "" || TRANSPORT_MODE_ID == null) {alert("Please Enter Transport ID");document.form.TRANSPORT_MODE_ID.focus(); return false; }
    if(TRANSPORT_MODE_DESC == "" || TRANSPORT_MODE_DESC == null) {alert("Please Enter Transport Mode Description");document.form.TRANSPORT_MODE_DESC.focus(); return false; } 

  var chkmsg=confirm("Are you sure you would like to save?");
    if(chkmsg){
   document.form.action  = "<%=rootURI%>/transportmode/UPDATE";
   document.form.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var TRANSPORT_MODE_ID   = document.form.TRANSPORT_MODE_ID.value;
	if (TRANSPORT_MODE_ID == "" || TRANSPORT_MODE_ID == null) {
		alert("Please Enter Transport BrandID");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form.action = "<%=rootURI%>/transportmode/DELETE";
		document.form.submit();
	} else {
		return false;
	}
}

</script>
<%
String isActive = request.getParameter("isActive");
String responseMsg = request.getParameter("response");
String tranID = request.getParameter("tranID");
String tranModeDesc = request.getParameter("tranModeDesc");

if(responseMsg == null)
	responseMsg = "";

if(tranID == null)
	tranID = "";

if(tranModeDesc == null)
	tranModeDesc = "";
if(isActive == null)
	isActive = "";
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='<%=rootURI%>/transportmode/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=responseMsg%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post" action = "">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="ID">ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="TRANSPORT_MODE_ID" type="TEXT" value="<%=tranID%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon"  
   		 	onClick="javascript:popWin('<%=rootURI%>/transportmode/LIST?TRANSPORT_MODE_ID='+document.form.TRANSPORT_MODE_ID.value+'&formName=form');">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Transport Mode Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Transport Model Description">Transport Model Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transport Model Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="TRANSPORT_MODE_DESC" type="TEXT" value="<%=tranModeDesc%>"
			size="50" MAXLENGTH=100>
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
