<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Reason Code";
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
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.ITEM_ID.value = ""; 
	document.form1.ITEM_DESC.value = "";
  /*  document.form1.action  = "ReasonMaster.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
   var ITEM_DESC  = document.form1.ITEM_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code ");document.form1.ITEM_ID.focus(); return false; }
    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Reason Description");document.form1.ITEM_DESC.focus(); return false; }
   document.form1.action  = "../jsp/ReasonMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }

   
   document.form1.action  = "../jsp/ReasonMaster.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }
   document.form1.action  = "../jsp/ReasonMaster.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }

   document.form1.action  = "../jsp/ReasonMaster.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){

	$.ajax({
		type: "GET",
		url: "../reasoncode/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#ITEM_ID").val(data.REASON);
		},
		error: function(data) {
			alert('Unable to generate Reason Code. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});

	 
 /*   document.form1.action  = "ReasonMaster.jsp?action=Auto_ID";
   document.form1.submit(); */
}

</SCRIPT>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	String sItemId = "", sPrdClsId = "", sItemDesc = "",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	RsnMstUtil rsnUtil = new RsnMstUtil();
	DateUtils dateutils = new DateUtils();

	rsnUtil.setmLogger(mLogger);

	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sItemId = strUtils.fString(request.getParameter("ITEM_ID"));
	sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
	sSAVE_RED = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdClsId = "";

	}
/* 	else if (action.equalsIgnoreCase("Auto_ID")) {

		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		TblControlDAO _TblControlDAO = new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "REASON");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "REASON");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "R");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "0000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(
						htTblCntInsert, plant);

				sItemId = "R" + "0001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq
						.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "000";
				} else if (updatedSeq.length() == 2) {
					sZero = "00";
				} else if (updatedSeq.length() == 3) {
					sZero = "0";
				}

				//System.out.print("..................................."+rtnBatch);
				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "REASON");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "R");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				boolean updateFlag = _TblControlDAO.update(updateQyery
						.toString(), htTblCntUpdate, "", plant);

				sItemId = "R" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - ReasonMaster.jsp ", e);
		}
	} */
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {

		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.RSNCODE, sItemId);
		if (!(rsnUtil.isExistsItemId(ht))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put(IDBConstants.RSNCODE, sItemId);
			ht.put(IDBConstants.RSNDESC, sItemDesc);
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_RSN);
			htm.put("RECID", "");
			htm.put("ITEM",sItemId);
			htm.put(IDBConstants.UPBY, sUserId);
			htm.put(IDBConstants.REMARKS, sItemDesc);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean itemInserted = rsnUtil.insertRsnMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted && inserted) {
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Reason  Added Successfully</font>"; */
				sSAVE_RED="Update";

			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Reason </font>";

			}
		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">ReasonCode  Exists already. Try again</font>";

		}

	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";

		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.RSNCODE, sItemId);
		ht.put(IDBConstants.PLANT, sPlant);
		if ((rsnUtil.isExistsItemId(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.RSNCODE, sItemId);
			htUpdate.put(IDBConstants.RSNDESC, sItemDesc);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.RSNCODE, sItemId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, "UPD_RSN");
			htm.put("RECID", "");
			htm.put(IDBConstants.UPBY, sUserId);htm.put(IDBConstants.REMARKS, sItemId);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean Updated = rsnUtil.updateItemId(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >ReasonCode  Updated Successfully</font>"; */
				sSAVE_RED="Update";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update ReasonCode </font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">ReasonCode  doesn't not Exists. Try again</font>";

		}

	}

	//4. >> Delete
	else if (action.equalsIgnoreCase("DELETE")) {
		Hashtable htDelete = new Hashtable();
		htDelete.put(IDBConstants.RSNCODE, sItemId);
		htDelete.put(IDBConstants.PLANT, sPlant);
		if (rsnUtil.isExistsItemId(htDelete)) {
			boolean itemDeleted = rsnUtil.deleteItemId(htDelete);
			if (true) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">ReasonCode  Deleted Successfully</font>";
				sAddEnb = "disabled";
				sItemId = "";
				sItemDesc = "";
				sPrdClsId = "";

			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to Delete ReasonCode </font>";
				sAddEnb = "enabled";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">ReasonCode doesn't not Exists. Try again</font>";
		}
	}

	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		Map map = rsnUtil.getItemDetails(sItemId);
		if (map.size() > 0) {
			sItemId = strUtils.fString((String) map.get("RSNCODE"));
			sItemDesc = strUtils.fString((String) map.get("RSNDESC"));

		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Item doesn't not Exists. Try again</font>";
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box">
	 		<div class="box-header menu-drop"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../reasoncode/summary"><span class="underline-on-hover">Reason Summary</span></a></li>                      
                <li><label>Create Reason Code</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     

              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../reasoncode/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
  <%--  <CENTER><strong><%=res%></strong></CENTER> --%>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Reason ID">Reason Code</label>
<!--        <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Reason Code</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_ID" id="ITEM_ID"  type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="ITEM_ID1" value="">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Reason Description">Reason Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Reason Description</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC"  id="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"   
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../reasoncode/summary?PGaction=View&result=Reason Code Added Successfully";
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