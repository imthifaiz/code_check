<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%--New page design begin --%>
<%
String title = "Edit Order Status";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<%--<link rel="stylesheet" href="css/style.css"> --%>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'LOCTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "maint_orderStatus.jsp?action=Clear";
   document.form1.submit();
}

function onUpdate(){

	 var STATUS_ID   = document.form1.STATUS_ID.value;
	 var STATUS_DESC = document.form1.STATUS_DESC.value;
	 if(STATUS_ID == "" || STATUS_ID == null) {alert("Please Enter Order Status ID");document.form1.STATUS_ID.focus(); return false; }
	 if(STATUS_DESC  == "" || STATUS_DESC  == null) {alert("Please Enter Order Status Description");document.form1.STATUS_DESC.focus(); return false; }
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
   document.form1.action  = "maint_orderStatus.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var STATUS_ID   = document.form1.STATUS_ID.value;
	if(STATUS_ID == "" || STATUS_ID == null) {alert("Please Enter Order Status ID"); return false; }
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "maint_orderStatus.jsp?action=DELETE";
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
	String sstatusID = "", sstatusDesc = "", isActive = "";

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	OrderStatusUtil ordstatusutil = new OrderStatusUtil();
	//LocMstDAO locmstdao = new LocMstDAO();
	DateUtils dateutils = new DateUtils();

	ordstatusutil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sstatusID  = strUtils.fString(request.getParameter("STATUS_ID"));
	sstatusDesc  = strUtils.fString(request.getParameter("STATUS_DESC"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sstatusID = "";
		sstatusDesc = "";
		
	}

	else if(action.equalsIgnoreCase("DELETE")){
		
		PoHdrDAO dao = new PoHdrDAO();
		DoHdrDAO dodao = new DoHdrDAO();
	    EstHdrDAO estdao = new EstHdrDAO();
	    
	    boolean movementhistoryExist=false;
	    boolean isExistDO=false;
	    boolean isExistEstimate=false;
	   	Hashtable htmh = new Hashtable();
	   	htmh.put("STATUS_ID",sstatusID);
	   	htmh.put(IConstants.PLANT,plant);
	   	movementhistoryExist = dao.isExisit(htmh,"");
	   	isExistDO = dodao.isExisit(htmh,"");
	   	isExistEstimate= estdao.isExisit(htmh,"");
	   	if(movementhistoryExist || isExistDO || isExistEstimate)
	   	{	
	   		
	   		res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Order Status Exists In Transactions</font>";
	   	}else{
		Hashtable htCondition = new Hashtable();
		htCondition.put("STATUS_ID", sstatusID);
		htCondition.put("PLANT", plant);
		
		
		if(ordstatusutil.isExistsOrdStatus(htCondition)) {
			boolean flag = ordstatusutil.deleteOrdStatusId(htCondition);
			
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			//htm.put("DIRTYPE",TransactionConstants.DEL_ORDSTATUS);
			htm.put("RECID", "");
			htm.put("ITEM",sstatusID);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sstatusDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			flag = mdao.insertIntoMovHis(htm);

			if(flag)
				{res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ " >Order Status Deleted Successfully </font>";}
			else {
                res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Order Status</font>";
                
      			}
		}else{
	           res = "<font class = "+IConstants.FAILED_COLOR+">Order Status doesn't  Exists. Try again</font>";
	    }
	   	}

		
		
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT,sPlant);
	    ht.put(IDBConstants.ORDSTATUSID,sstatusID);
	    if ((ordstatusutil.isExistsOrdStatus(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.ORDSTATUSID,sstatusID);
			htUpdate.put(IDBConstants.ORDSTATUSDESC, sstatusDesc);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.ORDSTATUSID,sstatusID);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			//htm.put("DIRTYPE",TransactionConstants.UPDATE_ORDSTATUS);
			htm.put("RECID", "");
			htm.put("ITEM",sstatusID);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS", sstatusDesc);
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean Updated = ordstatusutil.updateOrdStatusId(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Order Status  Updated Successfully</font>";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Order Status </font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Order Status  doesn't not Exists. Try again</font>";

		}

	}
%>




<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Order Status">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Status:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="STATUS_ID" type="TEXT" value="<%=sstatusID%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popWin('OrderStatusList.jsp?ORDERSTATUS='+form1.STATUS_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Status Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		 </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="STATUS_DESC" type="TEXT" value="<%=sstatusDesc%>"
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
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onDelete();"><b>Delete</b></button>&nbsp;&nbsp;

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
 

