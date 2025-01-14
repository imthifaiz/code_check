<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.LoanHdrDAO"%>
<%@ page import="com.track.dao.LoanDetDAO"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<%
String title = "Edit Rental and Service Customer";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'LoanAssignee', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "maint_loanAssignee.jsp?action=Clear";
   document.form.submit();
}
function isNumber(evt) {	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    
    if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
    	if( (charCode!=43 && charCode!=32 && charCode!=45))
    		{
    		
        alert("  Please enter only Numbers.  ");
        return false;
    		}
    	}
    return true;
}
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Rental and Service Customer ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Rental and Service Customer Name"); 
	   document.form.CUST_NAME.focus();
	   return false; 
	   }
   
    var radio_choice = false;
    for (i = 0; i < document.form.ACTIVE.length; i++)
    {
        if (document.form.ACTIVE[i].checked)
        radio_choice = true; 
    }
    if (!radio_choice)
    {
    alert("Please select Active or non Active mode.")
    return (false);
    }

 var chk = confirm("Are you sure you would like to save?");
	if(chk){
   document.form.action  = "maint_loanAssignee.jsp?action=UPDATE";
   document.form.submit();
	}
	else{
		return false;}	   
}

function onDelete(){
	   var CUST_CODE   = document.form.CUST_CODE.value;
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Rental and Service Customer ID");  return false; }
	var chkmsg=confirm("Are you sure you would like to delete? ");
	    if(chkmsg){
	   document.form.action  = "maint_loanAssignee.jsp?action=DELETE";
	   document.form.submit();}
	   else
	   { return false;
	   }
	}  

function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Rental and Service Customer ID"); return false; }

   document.form.action  = "maint_loanAssignee.jsp?action=VIEW";
   document.form.submit();
}
function onIDGen()
{
 document.form.action  = "maint_loanAssignee.jsp?action=Auto-ID";
   document.form.submit(); 

}

</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sState = "", sCountry = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "", sRemarks = "", isActive = "";

	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	LoanHdrDAO LoanHdrDAO = new LoanHdrDAO();
	LoanDetDAO LoanDetDAO = new LoanDetDAO();
	RecvDetDAO RecvDetDAO = new RecvDetDAO();

	custUtil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session
			.getAttribute("PLANT"));
	String username = strUtils.fString((String) session
			.getAttribute("LOGIN_USER"));
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));

	if (sCustCode.length() <= 0)
		sCustCode = strUtils
				.fString(request.getParameter("CUST_CODE1"));
	sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
	sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
	sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
	sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
	sAddr4 = strUtils.fString(request.getParameter("ADDR4"));
	sState = strUtils.fString(request.getParameter("STATE"));
	sCountry = strUtils.fString(request.getParameter("COUNTRY"));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sFax = strUtils.fString(request.getParameter("FAX"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sCustCode = "";
		sCustName = "";
		sCustNameL = "";
		sAddr1 = "";
		sAddr2 = "";
		sAddr3 = "";
		sAddr4 = "";
		sState = "";
		sCountry = "";
		sZip = "";
		sCons = "Y";
		sContactName = "";
		sDesgination = "";
		sTelNo = "";
		sHpNo = "";
		sFax = "";
		sEmail = "";
		sRemarks = "";
		sAddEnb = "";
		sCustEnb = "";

	}
	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "enabled";
		//    sAddEnb  = "disabled";
		if (custUtil.isExistLoanAssignee(sCustCode, plant)) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.LOAN_ASSIGNEE_CODE, sCustCode);
			htUpdate.put(IConstants.CUSTOMER_NAME, sCustName);
			htUpdate.put(IConstants.NAME, sContactName);
			htUpdate.put(IConstants.DESGINATION, sDesgination);
			htUpdate.put(IConstants.TELNO, sTelNo);
			htUpdate.put(IConstants.HPNO, sHpNo);
			htUpdate.put(IConstants.FAX, sFax);
			htUpdate.put(IConstants.EMAIL, sEmail);
			htUpdate.put(IConstants.ADDRESS1, sAddr1);
			htUpdate.put(IConstants.ADDRESS2, sAddr2);
			htUpdate.put(IConstants.ADDRESS3, sAddr3);
			htUpdate.put(IConstants.ADDRESS4, sAddr4);
			htUpdate.put(IConstants.STATE, sState);
			htUpdate.put(IConstants.COUNTRY, sCountry);
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put(IConstants.USERFLG1, sCons);
			htUpdate.put(IConstants.REMARKS, sRemarks);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils()
					.getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.ISACTIVE, isActive);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IConstants.LOAN_ASSIGNEE_CODE, sCustCode);
			htCondition.put(IConstants.PLANT, plant);
			
			Hashtable htloanhdr = new Hashtable();
			htloanhdr.put("CustCode", sCustCode);
			htloanhdr.put(IConstants.PLANT, plant);
			
			StringBuffer updateloanhdr = new StringBuffer("set ");
			updateloanhdr.append(IConstants.IN_CUST_CODE + " = '" + sCustCode + "'");
			updateloanhdr.append("," +IConstants.IN_CUST_NAME + " = '" + sCustName + "'");
			updateloanhdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ sContactName + "'");
			updateloanhdr.append(",Address = '"+ sAddr1 + "'");
			updateloanhdr.append(",Address2 = '"+ sAddr2 + "'");
			updateloanhdr.append(",Address3 = '"+ sAddr3 + "'");
            
			Hashtable htloandet = new Hashtable();
			htloandet.put(IConstants.PLANT, plant); 
			
			String loandetquery = "select Count(*) from "+plant+"_LOANDET where ORDNO in(select ORDNO from "+plant+"_LOANHDR where plant='"+plant+"' and  CustCode='"+sCustCode+"' )";
			String loandetupdate = "set userfld3='"+sCustName+"' ";

			String loanpickquery = "select Count(*) from "+plant+"_LOAN_PICK where ORDNO in(select ORDNO from "+plant+"_LOANHDR where  plant='"+plant+"' and CustCode='"+sCustCode+"' )";
			String loanpickupdate = "set cname='"+sCustName+"' ";

			String recvdetquery = "select Count(*) from "+plant+"_RECVDET where PONO in(select ORDNO from "+plant+"_LOANHDR where  plant='"+plant+"' and CustCode='"+sCustCode+"' )";
			String recvdetupdate = "set cname='"+sCustName+"' ";

			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_LOAN_ASSIGNEE);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put("UPBY", username);
			htm.put("CRBY", username);
			htm.put("REMARKS", sCustName+","+sRemarks);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean custUpdated = custUtil.updateLoanAssignee(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			boolean flag = false;
			if(custUpdated){
				 flag = LoanHdrDAO.isExisit(htloanhdr,"");
				 if(flag){
				 	flag = LoanHdrDAO.update(updateloanhdr.toString(), htloanhdr, " ");
				 }
				 if(flag){
					 flag = LoanDetDAO.isExisit(loandetquery);
					 if(flag)
					 {
						 flag = LoanDetDAO.update(loandetupdate, htloandet, " and ORDNO in(select ORDNO from "+plant+"_LOANHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 if(flag){
					 flag = LoanDetDAO.isExisit(loanpickquery);
					 if(flag)
					 {
						 flag = LoanDetDAO.updateLOPickTable(loanpickupdate, htloandet, " and ORDNO in(select ORDNO from "+plant+"_LOANHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 if(flag){
					 flag = RecvDetDAO.isExisit(recvdetquery);
					 if(flag)
					 {
						 flag = RecvDetDAO.update(recvdetupdate, htloandet, " and PONO in(select ORDNO from "+plant+"_LOANHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )", plant);
					 }
				 }
				 
			}
			if (custUpdated) {
				res = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Rental and Service Customer Updated Successfully</font>";
			} else {
				res = "<font class = " + IConstants.FAILED_COLOR
						+ ">Failed to Update Rental and Service Customer</font>";
			}
		} else {
			res = "<font class = "
					+ IConstants.FAILED_COLOR
					+ ">Rental and Service Customer doesn't Exists. Try again</font>";

		}
	}
	
	else if(action.equalsIgnoreCase("DELETE")){
	    sCustEnb = "disabled";
	    LoanHdrDAO dao = new LoanHdrDAO();
	    boolean movementhistoryExist=false;
		Hashtable htmh = new Hashtable();
		htmh.put("CUSTCODE",strUtils.InsertQuotes(sCustCode));
		htmh.put(IConstants.PLANT,plant);
		movementhistoryExist = dao.isExisit(htmh,"");
		if(movementhistoryExist)
		{	
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Rental and Service Customer Exists In Transactions</font>";
			
		}
		else{
	    if(custUtil.isExistLoanAssignee(strUtils.InsertQuotes(sCustCode),plant))
	    {
	          boolean custDeleted = custUtil.deleteLoanAssignee(strUtils.InsertQuotes(sCustCode),plant);
	         
	          MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", plant);
				htm.put("DIRTYPE", TransactionConstants.DEL_LOAN_ASSIGNEE);
				htm.put("RECID", "");
				htm.put("ITEM",sCustCode);
				htm.put("UPBY", username);
				htm.put("CRBY", username);
				htm.put("REMARKS", sCustName+","+sRemarks);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils
						.getDateinyyyy_mm_dd(dateutils.getDate()));
				
				custDeleted = mdao.insertIntoMovHis(htm);
	          
	          if(custDeleted) {
	                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Rental and Service Customer Deleted Successfully</font>";
//	                    sAddEnb    = "disabled";
	                    sCustCode  = "";
	                    sCustName  = "";
	                    sCustNameL = "";
	                    sAddr1     = "";
	                    sAddr2     = "";
	                    sAddr3     = "";sAddr4="";
	                    sState   = "";
	                    sCountry   = "";
	                    sZip       = "";
	                    sContactName = "";
	                    sDesgination = "";
	                    sTelNo       = "";
	                    sHpNo        = "";
	                    sFax         = "";
	                    sEmail       = "";
	                    sRemarks     = "";
	                    sCons      = "Y";

	          } else {
	                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Rental and Service Customer</font>";
	                    sAddEnb = "enabled";
	          }
	    }else{
	           res = "<font class = "+IConstants.FAILED_COLOR+">Rental and Service Customer doesn't Exists. Try again</font>";
	    }
		}
	}


	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = custUtil.getLoanAssigneeDetails(
					sCustCode, plant);
			sCustCode = (String) arrCust.get(0);
			sCustName = (String) arrCust.get(1);
			//sCustName   = (String)arrCust.get(2);
			sAddr1 = (String) arrCust.get(2);
			sAddr2 = (String) arrCust.get(3);
			sAddr3 = (String) arrCust.get(4);
			sAddr4 = (String) arrCust.get(15);
			
			sCountry = (String) arrCust.get(5);
			sZip = (String) arrCust.get(6);
			sCons = (String) arrCust.get(7);
			sContactName = (String) arrCust.get(8);
			sDesgination = (String) arrCust.get(9);
			sTelNo = (String) arrCust.get(10);
			sHpNo = (String) arrCust.get(11);
			sEmail = (String) arrCust.get(12);
			sFax = (String) arrCust.get(13);
			sRemarks = (String) arrCust.get(14);
			isActive = (String) arrCust.get(16);
			sState = (String) arrCust.get(17);
		} catch (Exception e) {
			res = "No details found for Rental and Service Customer : " + sCustCode;
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Maintain Loan Assignee ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/loanAssignee_list.jsp?CUST_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="">
       	</div>
       
    <div class=form-inline>
    <div class="col-sm-2">
      	<button type="button" class="Submit btn btn-default" onClick="onView();"><b>View</b></button>&nbsp;&nbsp;
    </div></div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Supplier Name">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
        
    <div class="form-group">
         <label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
      <div class="col-sm-4">          
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">          
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="50">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Telephone No">Telephone:</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Hand Phone">Mobile:</label>
      <div class="col-sm-4">          
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">          
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return isNumber(event)"
			MAXLENGTH="30" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">          
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Unit No">Unit No:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Building">Building:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Street">Street:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="City">City:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=50  class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="State">State:</label>
      <div class="col-sm-4">          
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Country">Country:</label>
      <div class="col-sm-4">          
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
      <div class="col-sm-4">          
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control">
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<button class="Submit btn btn-default" type="BUTTON"  onClick="return onDelete();" <%=sDeleteEnb%>><b>Delete</b></button>
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