<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ClearanceUtil"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.ClearanceBeanDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Clearance Type";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">

<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'CLEARANCETYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.CLEARANCE_TYPE_ID.value = ""; 
	document.form1.TYPE.value = "";
}
function onUpdate(){

	 var CLEARANCE_TYPE_ID   = document.form1.CLEARANCE_TYPE_ID.value;
	 if(CLEARANCE_TYPE_ID == "" || CLEARANCE_TYPE_ID == null) {alert("Please Enter Clearance Type ID");document.form1.CLEARANCE_TYPE_ID.focus(); return false; }
	
    var chkmsg=confirm("Are you sure you would like to save?");
    if(chkmsg){
   document.form1.action  = "../jsp/editClearanceType.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var CLEARANCE_TYPE_ID   = document.form1.CLEARANCE_TYPE_ID.value;
	if (CLEARANCE_TYPE_ID == "" || CLEARANCE_TYPE_ID == null) {
		alert("Please Enter Clearance Type Id");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "../jsp/editClearanceType.jsp?action=DELETE";
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
	String sClearanceTypeId = "", sType = "",sSAVE_RED,sSAVE_REDELETE;
	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	ClearanceUtil clearanceutil = new ClearanceUtil();
	ClearanceBeanDAO clearancebeandao = new ClearanceBeanDAO();
	DateUtils dateutils = new DateUtils();
	clearanceutil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sClearanceTypeId  = strUtils.fString(request.getParameter("CLEARANCE_TYPE_ID"));
	sType = strUtils.fString(request.getParameter("TYPE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sClearanceTypeId = "";
		sType = "";
	}
	else if(action.equalsIgnoreCase("DELETE")){
		ClearanceBeanDAO dao = new ClearanceBeanDAO();
		boolean movementhistoryExist=false;
		Hashtable htmh = new Hashtable();
		htmh.put("CLEARANCETYPE",sClearanceTypeId);
		htmh.put(IConstants.PLANT,plant);
		movementhistoryExist = dao.isExisit(htmh,"");

		if(clearanceutil.isExistsClearanceType(htmh)) {
			boolean flag = clearanceutil.deleteClearanceTypeId(htmh);
			
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.DEL_CLEARANCE_TYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sClearanceTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sType);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			flag = mdao.insertIntoMovHis(htm);
			if(flag)
				{
				sSAVE_REDELETE ="Delete";
					}
			else {
                res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Clearance Type</font>";
                
      			}
		}else{
	           res = "<font class = "+IConstants.FAILED_COLOR+">Clearance Type doesn't  Exists. Try again</font>";
	    }
		}
	
	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT,sPlant);
	    ht.put(IDBConstants.CLEARANCETYPEID,sClearanceTypeId);
	    if ((clearanceutil.isExistsClearanceType(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.CLEARANCETYPEID,sClearanceTypeId);
			htUpdate.put(IDBConstants.CLEARANCETYPE,sType); 

			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);
			
			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.CLEARANCETYPEID,sClearanceTypeId);
			htCondition.put(IDBConstants.PLANT, sPlant);
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.UPDATE_CLEARANCE_TYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sClearanceTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			 if(sType.equalsIgnoreCase("0"))
	        	   htm.put("REMARKS","Import");
	           else 
	           htm.put("REMARKS","Export");
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean Updated = clearanceutil.updateClearanceTypeId(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				sSAVE_RED = "Clearance Type Updated Successfully";
	
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Clearance Type </font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Clearance Type doesn't not Exists. Try again</font>";

		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box">
	
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../clearanceType/summary"><span class="underline-on-hover">Clearance Type Summary</span></a></li>                       
                <li><label>Edit Clearance Type</label></li>                                   
            </ul>
          
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../clearanceType/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
     
      <label class="control-label col-form-label col-sm-4 required">Clearance Type</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CLEARANCE_TYPE_ID" readonly type="TEXT" value="<%=sClearanceTypeId%>"
			size="50" MAXLENGTH=200 class="form-control">
  	</div>
  	</div>
    </div>
      <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
    
 <div class="form-group">
  <label class="control-label col-form-label col-sm-4 required">Type</label>
  <div class="col-sm-4">
    <label class="radio-inline">
      <input type="radio" name="TYPE" value="0"<%if (sType.equalsIgnoreCase("Import")) {%> checked <%}%>>Import
    </label>
    <label class="radio-inline">
      <input type="radio" name="TYPE" value="1"<%if (sType.equalsIgnoreCase("Export")) {%> checked <%}%>>Export
    </label>
     </div>
</div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
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
	    if(document.form1.SAVE_RED.value!="")
		{
		document.form1.action  = "../clearanceType/summary?PGaction=View&result=Clearance Type Updated Successfully";
		document.form1.submit();
		}
	});
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	document.form1.action  = "../clearanceType/summary?PGaction=View&result=Clearance Type Deleted Successfully";
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