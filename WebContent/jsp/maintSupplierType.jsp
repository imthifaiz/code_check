<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.CustomerBeanDAO"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>



<%
String title = "Edit Supplier Group";
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
 subWin = window.open(URL, 'SUPPLIERTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.SUPPLIER_TYPE_ID.value = ""; 
	document.form1.SUPPLIER_TYPE_DESC.value = "";
   /* document.form1.action  = "maintSupplierType.jsp?action=Clear";
   document.form1.submit(); */
}
function onUpdate(){

	 var SUPPLIER_TYPE_ID   = document.form1.SUPPLIER_TYPE_ID.value;
	 var SUPPLIER_TYPE_DESC = document.form1.SUPPLIER_TYPE_DESC.value;
	 if(SUPPLIER_TYPE_ID == "" || SUPPLIER_TYPE_ID == null) {alert("Please Enter Supplier Group ID"); return false; }
	 if(SUPPLIER_TYPE_DESC == "" || SUPPLIER_TYPE_DESC == null) {alert("Please Enter Supplier Group Description"); return false; }
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
   document.form1.action  = "../jsp/maintSupplierType.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var SUPPLIER_TYPE_ID   = document.form1.SUPPLIER_TYPE_ID.value;
	if (SUPPLIER_TYPE_ID == "" || SUPPLIER_TYPE_ID == null) {
		alert("Please Enter Supplier Group Id");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "../jsp/maintSupplierType.jsp?action=DELETE";
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
	String sSupplierTypeId = "", sSupplierTypeDesc = "", isActive = "",sSAVE_RED,sSAVE_REDELETE;
	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	CustUtil custutil = new CustUtil();
	CustomerBeanDAO customerbeandao = new CustomerBeanDAO();
	DateUtils dateutils = new DateUtils();
	custutil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sSupplierTypeId  = strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
	sSupplierTypeDesc  = strUtils.fString(request.getParameter("SUPPLIER_TYPE_DESC"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sSupplierTypeId = "";
		sSupplierTypeDesc = "";
	}
	else if(action.equalsIgnoreCase("DELETE")){
		CustMstDAO dao = new CustMstDAO();
		boolean movementhistoryExist=false;
		Hashtable htmh = new Hashtable();
		htmh.put("SUPPLIER_TYPE_ID",sSupplierTypeId);
		htmh.put(IConstants.PLANT,plant);
		//movementhistoryExist = dao.isExisit(htmh,"");
		if(movementhistoryExist)
		{	
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Supplier Group Exists In Supplier Master</font>";
			
		}else
		{
		Hashtable htCondition = new Hashtable();
		htCondition.put("SUPPLIER_TYPE_ID", sSupplierTypeId);
		htCondition.put("PLANT", plant);
		
		boolean locflag  = customerbeandao.isExistsSupplierType(htCondition,"");
		if (locflag) {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Cannot Delete as Supplier are tagged to Supplier Group</font>";
		}
		else{
		if(custutil.isExistsSupplierType(htCondition)) {
			boolean flag = custutil.deleteSupplierTypeId(htCondition);
			
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.DEL_SUPPLIER_TYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sSupplierTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sSupplierTypeDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			flag = mdao.insertIntoMovHis(htm);
			if(flag)
				{
				sSAVE_REDELETE = "Delete";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ " >Supplier Type Deleted Successfully </font>"; */
				sSupplierTypeId = "";
				sSupplierTypeDesc = "";
					
				}
			else {
                res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Supplier Group</font>";
                
      			}
		}else{
	           res = "<font class = "+IConstants.FAILED_COLOR+">Supplier Group doesn't  Exists. Try again</font>";
	    }

		
		}
		}
	
	}
	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT,sPlant);
	    ht.put(IDBConstants.SUPPLIERTYPEID,sSupplierTypeId);
	    if ((custutil.isExistsSupplierType(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.SUPPLIERTYPEID,sSupplierTypeId);
			htUpdate.put(IDBConstants.SUPPLIERTYPEDESC, sSupplierTypeDesc);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);
			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.SUPPLIERTYPEID,sSupplierTypeId);
			htCondition.put(IDBConstants.PLANT, sPlant);
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE",TransactionConstants.UPDATE_SUPPLIER_TYPE);
			htm.put("RECID", "");
			htm.put("ITEM",sSupplierTypeId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sSupplierTypeDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean Updated = custutil.updateSupplierTypeId(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				sSAVE_RED = "Update";
									sSupplierTypeId = "";
				sSupplierTypeDesc = "";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Supplier Group </font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Supplier Group doesn't not Exists. Try again</font>";

		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	<!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
               <li><a href="../suppliertype/summary"><span class="underline-on-hover">Supplier Group Summary</span></a></li>                      
                <li><label>Edit Supplier Group</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../suppliertype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <!-- <label class="control-label col-sm-4" for="Supplier Type ID"> -->
       <label class="control-label col-form-label col-sm-4 required">Supplier Group ID</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="SUPPLIER_TYPE_ID" type="TEXT" value="<%=sSupplierTypeId%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon" 
   		  onClick="javascript:popWin('../jsp/SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  	</div>
  	</div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Supplier Group Description">Supplier Group Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="SUPPLIER_TYPE_DESC" type="TEXT" value="<%=sSupplierTypeDesc%>"
			size="50" MAXLENGTH=100>
      </div>
      <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='supplierTypeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
     	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
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
	    if(document.form1.SAVE_RED.value!="") {
		document.form1.action  = "../suppliertype/summary?PGaction=View&result=Supplier Group Updated Successfully";	   
	    document.form1.submit();
		}
	});
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	document.form1.action  = "../suppliertype/summary?PGaction=View&result=Supplier Group Deleted Successfully";
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