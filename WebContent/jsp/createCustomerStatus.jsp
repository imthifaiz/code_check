<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Create Customer Status";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	
	

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'CustomerStatus', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onNew(){
   		document.form1.action  = "/track/CustomerStatusServlet?action=NEW";
   		document.form1.submit();
	}
	function onGenID()
	{
		document.form1.action  = "createCustomerStatus.jsp?action=Auto_ID";
 		document.form1.submit();
	}
	function onAdd(){
   		var CUSTOMER_STATUS_ID   = document.form1.CUSTOMER_STATUS_ID.value;
   		if( CUSTOMER_STATUS_ID == "" || CUSTOMER_STATUS_ID == null) {alert("Please Enter Customer Status ID");document.form1.CUSTOMER_STATUS_ID.focus(); return false; }
   		document.form1.action  = "/track/CustomerStatusServlet?action=ADD";
   		document.form1.submit();
	}


</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sCustomerStatusId = "", sDesc = "", sRemarks = "";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	action = strUtils.fString(request.getParameter("action"));
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		sCustomerStatusId = "";
		sDesc = "";
		sRemarks = "";
			
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
		ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMERSTATUS");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERSTATUS");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "CS");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "0000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
				sCustomerStatusId = "CS" + "0001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;
				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "000";
				} else if (updatedSeq.length() == 2) {
					sZero = "00";
				} else if (updatedSeq.length() == 3) {
					sZero = "0";
				}
				Map htUpdate = null;
				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMERSTATUS");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "CS");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");
				boolean updateFlag = _TblControlDAO.update(updateQyery
						.toString(), htTblCntUpdate, "", plant);
				sCustomerStatusId = "CS" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - createCustomerStatus.jsp ", e);
		}
	} else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
	}
        else if (action.equalsIgnoreCase("SHOW_RESULT_VALUE")) {
		res = (String)request.getSession().getAttribute("errResult");
           	
	}else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Customer Class ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Status ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUSTOMER_STATUS_ID" type="TEXT" value="<%=sCustomerStatusId%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();"<%=sNewEnb %>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title=Auto-Generate>
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUSTOMER_STATUS_ID1"
			value="<%=sCustomerStatusId%>">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Type Description">Customer Status Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DESC" type="TEXT" value="<%=sDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Type Description">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
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