<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.util.*"%>

<%
String title = "Create Location Type Three";
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

function showLoader(){
	$("#loader").show();
	$(".wrapper").css("opacity","0.1");
}

function hideLoader(){
	$("#loader").hide();
	$(".wrapper").css("opacity","1");
}

 function onGenID(){
		$.ajax({
			type: "GET",
			url: "../locationtypethree/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#LOC_TYPE_ID3").val(data.LOCTYPETHREE);
			},
			error: function(data) {
				alert('Unable to generate Location Type ID. Please try again later.');
			},
			complete: function(){
				hideLoader();
			}
		});
	}
	
function onAdd(){
	   var LOC_TYPE_ID3   = document.form1.LOC_TYPE_ID3.value;
	   var lOC_TYPE_DESC3 = document.form1.lOC_TYPE_DESC3.value;
	    if(LOC_TYPE_ID3 == "" || LOC_TYPE_ID3 == null) {alert("Please Enter Location Type ID");document.form1.LOC_TYPE_ID3.focus(); return false; }
	    if(lOC_TYPE_DESC3 == "" || lOC_TYPE_DESC3 == null) {alert("Please Enter Location Type Description");document.form1.lOC_TYPE_DESC3.focus(); return false; }
		var formData = new FormData($('#form1')[0]);
	    $.ajax({
	        type: 'POST',
	        url: "../locationtypethree/new",
		    data:  formData,
		    contentType: false,
		    processData: false,
	        success: function (data) {
	        	if(data.ERROR_CODE=="100")
	        		window.location.href="../locationtypethree/summary?msg="+data.MESSAGE;
	        	else
	        		alert(data.MESSAGE);
	        },
	        error: function (data) {
	            alert(data.responseText);
	        }
	    });
	    
	}
function onNew(){
			document.form1.LOC_TYPE_ID3.value = "";
			document.form1.lOC_TYPE_DESC3.value = "";
	}
</SCRIPT>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",locationTypeID="",locationTypeDesc="",sSAVE_RED;

String sNewEnb    = "enabled";
String sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
locationTypeID  = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
locationTypeDesc  = strUtils.fString(request.getParameter("LOC_DESC"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
            <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../locationtypethree/summary"><span class="underline-on-hover">Location Type Three Summary</span></a></li>                      
                <li><label>Create Location Type Three</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
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
    		<input name="LOC_TYPE_ID3" id="LOC_TYPE_ID3" type="TEXT" value="<%=locationTypeID%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>  
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    
 <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type Description">Location Type Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="lOC_TYPE_DESC3" type="TEXT" value="<%=locationTypeDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
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
