<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Packing List";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.ITEM_ID.value="";
/*    document.form1.action  = "packingMaster.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Packing list");document.form1.ITEM_ID.focus(); return false; }
   document.form1.action  = "../jsp/packingMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Packing list"); return false; }

   
   document.form1.action  = "../jsp/packingMaster.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter packing list  "); return false; }
   document.form1.action  = "../jsp/packingMaster.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter packing list"); return false; }

   document.form1.action  = "../jsp/packingMaster.jsp?action=VIEW";
   document.form1.submit();
}
/* function onGenID(){
     
   document.form1.action  = "packingMaster.jsp?action=Auto_ID";
   document.form1.submit();
}
 */
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
	PackingUtil PackingUtil = new PackingUtil();
	DateUtils dateutils = new DateUtils();

	PackingUtil.setmLogger(mLogger);

	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sItemId = strUtils.fString(request.getParameter("ITEM_ID"));
	sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdClsId = "";

	} 	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {

		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.PACKING, sItemId);
		if (!(PackingUtil.isExistsPacking(ht))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put(IDBConstants.PACKING, sItemId);
	//		ht.put(IDBConstants.UOMDESC, sItemDesc);
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_PACKING);
			htm.put("RECID", "");
			htm.put("ITEM",sItemId);
			htm.put(IDBConstants.UPBY, sUserId);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean itemInserted = PackingUtil.insertPacking(ht);

			if (itemInserted) {
				sSAVE_RED = "Updated";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Packing list  Added Successfully</font>"; */

			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Packing list </font>";

			}
		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Packing list Exists already. Try again</font>";

		}

	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";

		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PACKING, sItemId);
		ht.put(IDBConstants.PLANT, sPlant);
		if ((PackingUtil.isExistsPacking(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.PACKING, sItemId);
			htUpdate.put(IDBConstants.PACKING, sItemDesc);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.PACKING, sItemId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, "UPD_UOM");
			htm.put("RECID", "");
			htm.put(IDBConstants.UPBY, sUserId);//htm.put(IDBConstants.REMARKS, sItemId);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean Updated = PackingUtil.updatePacking(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Packing  Updated Successfully</font>";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Packing </font>";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Packing  doesn't not Exists. Try again</font>";

		}

	}

	//4. >> Delete
	else if (action.equalsIgnoreCase("DELETE")) {
		Hashtable htDelete = new Hashtable();
		htDelete.put(IDBConstants.PACKING, sItemId);
		htDelete.put(IDBConstants.PLANT, sPlant);
		if (PackingUtil.isExistsPacking(htDelete)) {
			boolean itemDeleted = PackingUtil.deletePacking(htDelete);
			if (true) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">packing list  Deleted Successfully</font>";
				sAddEnb = "disabled";
				sItemId = "";
				sItemDesc = "";
				sPrdClsId = "";

			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to Delete packing list </font>";
				sAddEnb = "enabled";
			}
		} else {
			res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Packing list doesn't not Exists. Try again</font>";
		}
	}

	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		Map map = PackingUtil.getPackingDetails(sItemId);
		if (map.size() > 0) {
			sItemId = strUtils.fString((String) map.get("PACKING"));
		//	sItemDesc = strUtils.fString((String) map.get("UOMDESC"));

		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Packing list doesn't not Exists. Try again</font>";
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../packinglist/summary"><span class="underline-on-hover">Packing Summary</span></a></li>                       
                <li><label>Create Packing List</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../packinglist/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Packing Master">Packing</label>

      <div class="col-sm-4">
      <input name="ITEM_ID" type="TEXT" value="<%=sItemId%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=15 class="form-control">
   		
  		  		<INPUT type="hidden" name="ITEM_ID1" value="">
  		  		 <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    

    
    <div class="form-group">        
      <div class="col-sm-offset-2 col-sm-8">
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
	document.form1.action  = "../packinglist/summary?PGaction=View&result=Packing list  Added Successfully";
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
