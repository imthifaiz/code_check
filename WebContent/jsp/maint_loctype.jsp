<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.LocMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Location Type One";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<style type="text/css">
.backpageul
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

function popWin(URL) {
 subWin = window.open(URL, 'LOCTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

	document.form1.LOC_TYPE_ID.value = ""; 
	document.form1.LOC_DESC.value = "";
 /*   document.form1.action  = "maint_loctype.jsp?action=Clear";
   document.form1.submit(); */
}

function onUpdate(){

	 var LOC_TYPE_ID   = document.form1.LOC_TYPE_ID.value;
	 var LOC_DESC = document.form1.LOC_DESC.value;
	 if(LOC_TYPE_ID == "" || LOC_TYPE_ID == null) {alert("Please Enter Location Type ID");document.form1.LOC_TYPE_ID.focus(); return false; }
	 if(LOC_DESC == "" || LOC_DESC == null) {alert("Please Enter Location Type Description");document.form1.LOC_DESC.focus(); return false; }
	 var radio_choice = false;

// Loop from zero to the one minus the number of radio button selections
for (i = 0; i < document.form1.ACTIVE.length; i++)
{
if (document.form1.ACTIVE[i].checked)
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
   document.form1.action  = "../jsp/maint_loctype.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var LOC_TYPE_ID   = document.form1.LOC_TYPE_ID.value;
	if (LOC_TYPE_ID == "" || LOC_TYPE_ID == null) {
		alert("Please Enter Location Id");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "../jsp/maint_loctype.jsp?action=DELETE";
		document.form1.submit();
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
	sLocTypeId  = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	sLocTypeDesc  = strUtils.fString(request.getParameter("LOC_DESC"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sLocTypeId = "";
		sLocTypeDesc = "";
		
	}

	else if(action.equalsIgnoreCase("DELETE")){
		Hashtable htCondition = new Hashtable();
		htCondition.put("LOC_TYPE_ID", sLocTypeId);
		htCondition.put("PLANT", plant);
		
		boolean locflag  = locmstdao.isExisit(htCondition,"");
		if (locflag) {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Location Type Exists In Location</font>";
		}
		else{
		if(loctypeutil.isExistsLocType(htCondition)) {
			boolean flag = loctypeutil.deleteLocTypeId(htCondition);
			
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.DEL_LOCTYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sLocTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sLocTypeDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
		
			flag = mdao.insertIntoMovHis(htm);
			
			if(flag)
				{
				sSAVE_REDELETE ="Delete";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ " >Location Type Deleted Successfully </font>"; */
					}
			else {
                res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Location Type One</font>";
                
      			}
		}else{
	           res = "<font class = "+IConstants.FAILED_COLOR+">Location Type One doesn't Exists. Try again</font>";
	    }

		
		}
		
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT,sPlant);
	    ht.put(IDBConstants.LOCTYPEID,sLocTypeId);
	    if ((loctypeutil.isExistsLocType(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.LOCTYPEID,sLocTypeId);
			htUpdate.put(IDBConstants.LOCTYPEDESC, sLocTypeDesc);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.LOCTYPEID,sLocTypeId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.UPDATE_LOCTYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sLocTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sLocTypeDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean Updated = loctypeutil.updateLoctypeId(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				sSAVE_RED="Update";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Location Type  Updated Successfully</font>"; */
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Location Type One</font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Location Type One doesn't not Exists. Try again</font>";

		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../loctypeone/summary"><span class="underline-on-hover">Location Type One Summary</span></a></li>                       
                <li>Edit Location Type One</li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../loctypeone/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type ID">Location Type ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location Type ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="LOC_TYPE_ID" type="TEXT" value="<%=sLocTypeId%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  
   		 	onClick="javascript:popWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type Description">Location Type Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location Type Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="LOC_DESC" type="TEXT" value="<%=sLocTypeDesc%>"
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
	    if(document.form1.SAVE_RED.value!="") {
		document.form1.action  = "../loctypeone/summary?PGaction=View&result=Location Type One Updated Successfully";	   
	    document.form1.submit();
		}
	});
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	document.form1.action  = "../loctypeone/summary?PGaction=View&result=Location Type One Deleted Successfully";
	    	 document.form1.submit();
		}
	});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>