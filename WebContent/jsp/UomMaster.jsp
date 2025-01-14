<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%@ include file="header.jsp"%>

<%
String title = "Create Unit of Measure";
String PLANT = (String)session.getAttribute("PLANT");
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
	document.form1.Display.value = ""; 
	document.form1.QPUOM.value = "";
  /*  document.form1.action  = "UomMaster.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
   var Display   = document.form1.Display.value;
   var QPUOM   = document.form1.QPUOM.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  ");document.form1.ITEM_ID.focus(); return false; }
	
	else if(Display == "" || Display == null) 
    {
    	alert("Please Enter Display for UOM");
    	document.form1.Display.focus();
    	return false; 
    	}
    else if(!IsNumeric(document.form1.QPUOM.value))
    {
      alert("Please Enter valid Quantity Per UOM");
      form1.QPUOM.focus(); form1.QPUOM.select();
      return false;
    }
    else if(QPUOM == "" || QPUOM <= 0)
    {
        alert("Please Enter Quantity Per UOM");
        document.form1.QPUOM.focus();
        return false;
    }else{
    	 if (QPUOM != 1){
    		 if(CUOM == "" || CUOM <= 0)
    		    {
    			 	alert("Please select Convertible UOM");
    		        document.form1.CUOM.focus();
    		        return false;
    		    }
    	 }
    }
   document.form1.action  = "../jsp/UomMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  "); return false; }

   
   document.form1.action  = "../jsp/UomMaster.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  "); return false; }
   document.form1.action  = "../jsp/UomMaster.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  "); return false; }

   document.form1.action  = "../jsp/UomMaster.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){
     
   document.form1.action  = "../jsp/UomMaster.jsp?action=Auto_ID";
   document.form1.submit();
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
	String sItemId = "", sPrdClsId = "", sItemDesc = "",sDisplay = "",sQtyPerUom = "",sCUom = "",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	UomUtil uomUtil = new UomUtil();
	DateUtils dateutils = new DateUtils();

	uomUtil.setmLogger(mLogger);

	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = StrUtils.fString(request.getParameter("result"));
	sItemId = strUtils.fString(request.getParameter("ITEM_ID"));
	sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC"));
	sDisplay = strUtils.fString(request.getParameter("Display"));
	sQtyPerUom = strUtils.fString(request.getParameter("QPUOM"));
	sCUom = strUtils.fString(request.getParameter("CUOM"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdClsId = "";
		sDisplay ="";
		sQtyPerUom ="";

	} else if (action.equalsIgnoreCase("Auto_ID")) {

		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		TblControlDAO _TblControlDAO = new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "UOM");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "UOM");
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

				sItemId = "U" + "0001";
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
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "UOM");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "U");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				boolean updateFlag = _TblControlDAO.update(updateQyery
						.toString(), htTblCntUpdate, "", plant);

				sItemId = "R" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - UomMaster.jsp ", e);
		}
	}
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
        result="";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.UOMCODE, sItemId);
		if (!(uomUtil.isExistsUom(ht))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put(IDBConstants.UOMCODE, sItemId);
			ht.put(IDBConstants.UOMDESC, sItemDesc);
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_UOM);
			htm.put("RECID", "");
			htm.put("ITEM",sItemId);
			htm.put(IDBConstants.UPBY, sUserId);
			htm.put(IDBConstants.REMARKS, sItemDesc);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));	
			ht.put("Display",sDisplay);
			/*if(sQtyPerUom == "")
			{
				sQtyPerUom = "0";
			} */
			ht.put("QPUOM",sQtyPerUom);
			if(Double.valueOf(sQtyPerUom) != 1)
			{
				ht.put("ISCONVERTIBLE", "1");
				ht.put("CUOM", sCUom);
			}else{
				ht.put("ISCONVERTIBLE", "0");
				ht.put("CUOM", sItemId);
			}

			boolean itemInserted = uomUtil.insertUom(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted && inserted) {
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">UOM  Added Successfully</font>"; */
				sSAVE_RED = "Updated";
			} else {
				sSAVE_RED="Failed to add New UOM ";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New UOM </font>"; */

			}
		} else {
			sSAVE_RED="UOM  Exists already. Try again";
			/* res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">UOM  Exists already. Try again</font>"; */

		}

	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {

		sAddEnb = "disabled";
         result="";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.UOMCODE, sItemId);
		ht.put(IDBConstants.PLANT, sPlant);
		if ((uomUtil.isExistsUom(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.UOMCODE, sItemId);
			htUpdate.put(IDBConstants.UOMDESC, sItemDesc);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.UOMCODE, sItemId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, "UPD_UOM");
			htm.put("RECID", "");
			htm.put(IDBConstants.UPBY, sUserId);htm.put(IDBConstants.REMARKS, sItemId);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			htUpdate.put("Display",sDisplay);
			htUpdate.put("QPUOM",sQtyPerUom);

			boolean Updated = uomUtil.updateUom(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				sSAVE_RED="UOM  Updated Successfully";
				/* 
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >UOM  Updated Successfully</font>"; */
			} else {
				sSAVE_RED="Failed to Update UOM ";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update UOM </font>"; */
			}
		} else {
			sSAVE_RED="UOM  doesn't not Exists. Try again";
			/* res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">UOM  doesn't not Exists. Try again</font>"; */

		}

	}

	//4. >> Delete
	else if (action.equalsIgnoreCase("DELETE")) {
		Hashtable htDelete = new Hashtable();
		htDelete.put(IDBConstants.UOMCODE, sItemId);
		htDelete.put(IDBConstants.PLANT, sPlant);
		if (uomUtil.isExistsUom(htDelete)) {
			boolean itemDeleted = uomUtil.deleteUom(htDelete);
			if (true) {
				sSAVE_RED="UOM  Deleted Successfully";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">UOM  Deleted Successfully</font>"; */
				sAddEnb = "disabled";
				sItemId = "";
				sItemDesc = "";
				sPrdClsId = "";

			} else {
				sSAVE_RED="Failed to Delete UOM";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to Delete UOM </font>"; */
				sAddEnb = "enabled";
			}
		} else {
			sSAVE_RED="UOM doesn't not Exists. Try again";
			/* res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">UOM doesn't not Exists. Try again</font>"; */
		}
	}

	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		Map map = uomUtil.getUomDetails(sItemId);
		if (map.size() > 0) {
			sItemId = strUtils.fString((String) map.get("UOM"));
			sItemDesc = strUtils.fString((String) map.get("UOMDESC"));
			sDisplay = strUtils.fString((String) map.get("Display"));
			sQtyPerUom = strUtils.fString((String) map.get("QPUOM"));

		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">UOM doesn't not Exists. Try again</font>";
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../uom/summary"><span class="underline-on-hover">UOM Summary</span></a></li>                      
                <li><label>Create Unit Of Measure</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../uom/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="UOM Master">Unit Of Measure</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit Of Measure:</label> -->
      <div class="col-sm-4">
      <input name="ITEM_ID" type="TEXT" value="<%=sItemId%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=14 class="form-control">
   		
  		  		<INPUT type="hidden" name="ITEM_ID1" value="">
  		  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="UOM Description">Unit Of Measure Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=50>
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Display">Display</label>
	  <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Display:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="Display" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=14>
      </div>
      <font><i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;<b>Note : To Display in Purchase,Sales,Direct Sales,Sales Estimate,Rental 
       and Transfer Order Invoice.</b></font>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Qty Per UOM">Quantity Per UOM</label>
<!-- 	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Quantity Per UOM:</label> -->
      <div class="col-sm-2">          
        <INPUT  class="form-control" name="QPUOM" type="TEXT" value="<%=sQtyPerUom%>"
			size="50" MAXLENGTH=50 onchange="checkno()" style="width: 120%;">
      </div>
      <div class="col-sm-2 squom" hidden>
         <SELECT class="form-control " data-toggle="dropdown" data-placement="left" name="CUOM" id="CUOM" >
			
					<%
					
				  ArrayList ccList = UomDAO.getUOMList(PLANT);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						String uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
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
    if(document.form1.SAVE_RED.value!=""){
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Created Successfully";
	document.form1.submit();
	}
    else{
    	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Created Successfully";
    	document.form1.submit()
}
    }
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});

function checkno(){
	var baseamount = $("input[name=QPUOM]").val();
	var zeroval = "0";
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("input[name=QPUOM]").val(baseamount);
			if(baseamount == '1'){
				$(".squom").hide();
			}else{
				$(".squom").show();
			}
			
		}else{
			$("input[name=QPUOM]").val(zeroval);
			$(".squom").hide();
		}
	}else{
		$(".squom").hide();
		$("input[name=QPUOM]").val(zeroval);
	}
	
}

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
