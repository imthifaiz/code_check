<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.LocMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Location Type Three";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'LOCTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.LOC_TYPE_ID3.value = "";
	document.form1.lOC_TYPE_DESC3.value = "";
}

function onUpdate(){

	 var LOC_TYPE_ID3   = document.form1.LOC_TYPE_ID3.value;
	 var lOC_TYPE_DESC3 = document.form1.lOC_TYPE_DESC3.value;
	 if(LOC_TYPE_ID3 == "" || LOC_TYPE_ID3 == null) {alert("Please Enter Location Type ID");document.form1.LOC_TYPE_ID3.focus(); return false; }
	 if(lOC_TYPE_DESC3 == "" || lOC_TYPE_DESC3 == null) {alert("Please Enter Location Type Description");document.form1.lOC_TYPE_DESC3.focus(); return false; }
	 var radio_choice = false;
	// Loop from zero to the one minus the number of radio button selections
	 for (i = 0; i < document.form1.IsActive.length; i++)
	 {
	 if (document.form1.IsActive[i].checked)
	 radio_choice = true; 
	 }

	 if (!radio_choice)
	 {
	 // If there were no selections made display an alert box 
	 alert("Please select Active or non Active mode.");
	 return (false);
	 }

	     var chkmsg=confirm("Are you sure you would like to save?");
	     if(chkmsg){
			var formData = new FormData($('#form1')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../locationtypethree/edit",
			    data:  formData,
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	if(data.ERROR_CODE == 100)
		        		window.location.href="../locationtypethree/summary?msg="+data.MESSAGE;
		        	else
		        		alert(data.MESSAGE);
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
}
    else
    { return false;
    }
}
function onDelete() {
	var LOC_TYPE_ID3   = document.form1.LOC_TYPE_ID3.value;
	if (LOC_TYPE_ID3 == "" || LOC_TYPE_ID3 == null) {
		alert("Please Enter Location Id");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		$.ajax({
			type: 'POST',
	        url: "../locationtypethree/delete?LOC_TYPE_ID3="+LOC_TYPE_ID3,
		    contentType: false,
		    processData: false,
    		success : function(data) {
    			if(data.STATUS == "NOT OK"){
    				alert("Location not deleted.");
    			}else{
    				window.location.href="../locationtypethree/summary?msg=Location Type Three deleted successfully";
    			}
    		}
    	});	
	} else {
		return false;
	}
}
</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	
	String action = "";
	String sLocTypeId = "", sLocTypeDesc = "", isActive = "",sSAVE_RED,sSAVE_REDELETE;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	LocTypeUtil loctypeutil = new LocTypeUtil();
	LocMstDAO locmstdao = new LocMstDAO();
	DateUtils dateutils = new DateUtils();

	loctypeutil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sLocTypeId  = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	sLocTypeDesc  = strUtils.fString(request.getParameter("lOC_TYPE_DESC3"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../locationtypethree/summary"><span class="underline-on-hover">Location Type Three Summary</span></a></li>                     
                <li><label>Edit Location Type Three</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
					onClick="window.location.href='../locationtypethree/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="form1" name="form1" method="post" 
			action="../locationtypethree/new" enctype="multipart/form-data">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type ID">Location Type ID</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="LOC_TYPE_ID3" id="LOC_TYPE_ID3" type="TEXT" value="<%=sLocTypeId%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  
   		 onClick="javascript:popWin('../jsp/LocTypeThreeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type Description">Location Type Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="lOC_TYPE_DESC3" id="lOC_TYPE_DESC3" type="TEXT" value="<%=sLocTypeDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
<div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="IsActive" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="IsActive" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="Submit btn btn-default" onClick="onNew();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
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