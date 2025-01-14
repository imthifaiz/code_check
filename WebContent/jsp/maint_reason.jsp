<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Reason Code";
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
function onNew(){
	document.form1.ITEM_ID.value = ""; 
	document.form1.ITEM_DESC.value = "";
/*    document.form1.action  = "maint_reason.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }
   document.form1.action  = "../jsp/ReasonMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    var ITEM_DESC  = document.form1.ITEM_DESC.value;
     if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code ");document.form1.ITEM_ID.focus(); return false; }
     if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Reason Description");document.form1.ITEM_DESC.focus(); return false; }
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
alert("Please select Active or non Active mode.")
return (false);
}
	var chk = confirm("Are you sure you would like to save?");
	if(chk){   
   document.form1.action  = "../jsp/maint_reason.jsp?action=UPDATE";
   document.form1.submit();}
	else
	{
		return false;
	}
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }
    var chkmsg=confirm("Are you sure you would like to delete?");    
    if(chkmsg){
   document.form1.action  = "../jsp/maint_reason.jsp?action=DELETE";
   document.form1.submit();}
   else
   {
   return false;
   }
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }

   document.form1.action  = "../jsp/maint_reason.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){
     
   document.form1.action  = "../jsp/maint_reason.jsp?action=Auto_ID";
   document.form1.submit();
}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
String action     =   "";
String sItemId ="",sPrdClsId  =   "",isActive="",sSAVE_REDELETE,sSAVE_RED,
       sItemDesc  = "";

session= request.getSession();
StrUtils strUtils = new StrUtils();
RsnMstUtil rsnUtil = new RsnMstUtil();
DateUtils dateutils = new DateUtils();

rsnUtil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
isActive= strUtils.fString(request.getParameter("ACTIVE"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if(action.equalsIgnoreCase("Clear")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
	  isActive="";
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
  
    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.RSNCODE,sItemId);
    ht.put(IDBConstants.PLANT,sPlant);
    if((rsnUtil.isExistsItemId(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IDBConstants.RSNCODE,sItemId);
          htUpdate.put(IDBConstants.RSNDESC,sItemDesc);   
       
          htUpdate.put(IConstants.ISACTIVE,isActive);
          htUpdate.put(IDBConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IDBConstants.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(IDBConstants.RSNCODE,sItemId);
          htCondition.put(IDBConstants.PLANT,sPlant);
          
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
           Hashtable htm = new Hashtable();
           htm.put(IDBConstants.PLANT, sPlant);
		   htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPD_RSN);
           htm.put("RECID","");
           htm.put("ITEM",sItemId);
           htm.put(IDBConstants.UPBY, sUserId);
           htm.put(IDBConstants.REMARKS, sItemDesc);
		   htm.put(IDBConstants.CREATED_BY, sUserId);
		   htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		   htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		   htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
                   
        boolean Updated = rsnUtil.updateItemId(htUpdate,htCondition);
        boolean  inserted = mdao.insertIntoMovHis(htm);
          if(true) {
                   /*  res = "<font class = "+IDBConstants.SUCCESS_COLOR +" >ReasonCode  Updated Successfully</font>"; */
        	  sSAVE_RED = "Update";
          } else {
                    res = "<font class = "+IDBConstants.FAILED_COLOR +" >Failed to Update ReasonCode </font>";
          }
    }else{
           res = "<font class = "+IDBConstants.FAILED_COLOR +">ReasonCode  doesn't not Exists. Try again</font>";

    }
      
}
else if(action.equalsIgnoreCase("DELETE")){
	Hashtable htcondition = new Hashtable();
	htcondition.put(IDBConstants.RSNCODE,sItemId);
	htcondition.put(IDBConstants.PLANT,sPlant);
	
	
	if(rsnUtil.isExistsItemId(htcondition))
	 {
		boolean flag = rsnUtil.deleteItemId(htcondition);
		
		MovHisDAO mdao = new MovHisDAO(sPlant);
        mdao.setmLogger(mLogger);
        Hashtable htm = new Hashtable();
        htm.put(IDBConstants.PLANT, sPlant);
		
        htm.put(IDBConstants.DIRTYPE,TransactionConstants.DEL_RSN);
        htm.put("RECID","");
        htm.put("ITEM",sItemId);
        htm.put(IDBConstants.UPBY, sUserId);
        htm.put(IDBConstants.REMARKS, sItemDesc);
		htm.put(IDBConstants.CREATED_BY, sUserId);
		htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
         
     
			flag = mdao.insertIntoMovHis(htm);
     
		if(flag)
			{
			/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
				+ " >ReasonCode Deleted Successfully </font>";
			 */	sSAVE_REDELETE = "Delete";
			 }
		else {
            res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete ReasonCode</font>";
            
  			}
	}else{
           res = "<font class = "+IConstants.FAILED_COLOR+">ReasonCode doesn't  Exists. Try again</font>";
    }
	

	

	
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map  = rsnUtil.getItemDetails(sItemId);
   if(map.size()>0){
    sItemId   = strUtils.fString((String)map.get("RSNCODE"));
    sItemDesc   = strUtils.fString((String)map.get("RSNDESC"));
   isActive   = strUtils.fString((String)map.get("ISACTIVE"));
    }else{
     res = "<font class = "+IDBConstants.FAILED_COLOR +">Item doesn't not Exists. Try again</font>";
    }
}

%>


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
               <li><a href="../reasoncode/summary"><span class="underline-on-hover">Reason Summary</span></a></li>                         
                <li><label>Edit Reason Code</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		 <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
					onclick="window.location.href='../reasoncode/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
 <div class="box-body">
 
    <CENTER><strong><%=res%></strong></CENTER> 


  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Master ID">Reason Code</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Reason Code:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  
   		 	onClick="javascript:popWin('../jsp/list/ReasonMstList.jsp?ITEM_ID='+form1.ITEM_ID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Reason Code Details">
   		 	<i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span> -->
  		</div>
  	<INPUT type="hidden" name="ITEM_ID1" value="">
  	<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  	<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Reason Description">Reason Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Reason Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
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
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"<%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
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
	    	document.form1.action  = "../reasoncode/summary?PGaction=View&result=Reason Code Updated Successfully";
	    	document.form1.submit(); 
		}
	});
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	document.form1.action  = "../reasoncode/summary?PGaction=View&result=Reason Code Deleted Successfully";
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