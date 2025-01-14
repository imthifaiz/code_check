<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>

<html>
<head>
<title>Rental And Service Customer List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="../css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>

<body>

 <script type="text/javascript" src="../js/general.js"></script> 

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'LoanAssignee', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "loanAssigneehdrlist.jsp?action=Clear";
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
function onAdd(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME1.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Rental and Service Customer ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Rental and Service Customer Name"); 
   document.form.CUST_NAME1.focus();
   return false; 
   }
   
   /* if (IsValidStringWithoutSpace(form.CUST_CODE.value)==false){
       alert("Enter Loan Assignee ID without White Space");
       		 form.CUST_CODE.focus();
             return false;
     } */
   
   document.form.action  = "loanAssigneehdrlist.jsp?action=ADD";
   document.form.submit();
}
function onIDGen()
{
 document.form.action  = "loanAssigneehdrlist.jsp?action=Auto-ID";
   document.form.submit(); 

}

</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";
    TblControlDAO _TblControlDAO = new TblControlDAO();
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sState = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "", sRemarks = "";

	DateUtils dateutils = new DateUtils();
	/*  */
	MLogger mLogger = new MLogger();
	/*  */
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
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
	sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME1")));
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
	//User Check 	
		boolean al=false;
		al = ub.isCheckVal("popcustomer", plant,username);
		if(al==true)
		{		
			System.out.println("popcustomer");
		}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sCustCode = "";
		sCustName = "";
		sCustNameL = "";
		sAddr1 = "";
		sAddr2 = "";
		sAddr3 = "";
		sAddr4 = "";
		sCountry = "";
		sState = "";
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

	} else if (action.equalsIgnoreCase("Auto-ID")) {
		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		//TblControlDAO _TblControlDAO = new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "LOANASSIGNEE");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						"LOANASSIGNEE");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "LA");
				htTblCntInsert.put("MINSEQ", "000");
				htTblCntInsert.put("MAXSEQ", "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(
						htTblCntInsert, plant);

				sCustCode = "LA" + "001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				System.out.println("length" + sBatchSeq.length());

				int inxtSeq = Integer.parseInt(((String) sBatchSeq
						.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "00";
				} else if (updatedSeq.length() == 2) {
					sZero = "0";
				}

				//System.out.print("..................................."+rtnBatch);
				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						"LOANASSIGNEE");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "LA");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				//boolean updateFlag = _TblControlDAO.update(updateQyery
					//	.toString(), htTblCntUpdate, "", plant);
				sCustCode = "LA" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - loanAssigneehdrlist.jsp ", e);
		}
	}
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		if (!custUtil.isExistLoanAssignee(sCustCode, plant)) // if the Customer exists already
		{
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.LOAN_ASSIGNEE_CODE, sCustCode);
			ht.put(IConstants.CUSTOMER_NAME, sCustName);
			ht.put(IConstants.NAME, sContactName);
			ht.put(IConstants.DESGINATION, sDesgination);
			ht.put(IConstants.TELNO, sTelNo);
			ht.put(IConstants.HPNO, sHpNo);
			ht.put(IConstants.FAX, sFax);
			ht.put(IConstants.EMAIL, sEmail);
			ht.put(IConstants.ADDRESS1, sAddr1);
			ht.put(IConstants.ADDRESS2, sAddr2);
			ht.put(IConstants.ADDRESS3, sAddr3);
			ht.put(IConstants.ADDRESS4, sAddr4);
			ht.put(IConstants.STATE, sState);
			ht.put(IConstants.COUNTRY, sCountry);
			ht.put(IConstants.ZIP, sZip);
			ht.put(IConstants.USERFLG1, sCons);
			ht.put(IConstants.REMARKS, sRemarks);
			
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			ht.put("Comment1", " 0 ");
			ht.put(IConstants.ISACTIVE, "Y");

			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_CONSIGNEE);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put("CRBY", username);
			if(!sRemarks.equals(""))
			{
				htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
			}
			else
			{
				htm.put(IDBConstants.REMARKS, sCustName);
			}
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean updateFlag;
			if(sCustCode!="LA001")
	  		{	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "LOANASSIGNEE");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
	  		  	updateFlag=_TblControlDAO.updateSeqNo("LOANASSIGNEE",plant);
				else
				{
					boolean insertFlag = false;
					Map htInsert = null;
					Hashtable htTblCntInsert = new Hashtable();
					htTblCntInsert.put(IDBConstants.PLANT, plant);
					htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
							"LOANASSIGNEE");
					htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "LA");
					htTblCntInsert.put("MINSEQ", "000");
					htTblCntInsert.put("MAXSEQ", "999");
					htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
							(String) IDBConstants.TBL_FIRST_NEX_SEQ);
					htTblCntInsert.put(IDBConstants.CREATED_BY, username);
					htTblCntInsert.put(IDBConstants.CREATED_AT,
							(String) new DateUtils().getDateTime());
					insertFlag = _TblControlDAO.insertTblControl(
							htTblCntInsert, plant);
				}
			}
			boolean custInserted = custUtil.insertLoanAssignee(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {

				res = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Rental and Service Customer Added Successfully</font>";
				sCustEnb = "disabled";
				//clear data automatically when click save button
				sCustCode = "";
				sCustName = "";
				sCustNameL = "";
				sAddr1 = "";
				sAddr2 = "";
				sAddr3 = "";
				sAddr4 = "";
				sCountry = "";
				sState = "";
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
				//automatic clear end
			} else {
				res = "<font class = " + IConstants.FAILED_COLOR
						+ ">Failed to add New Rental and Service Customer</font>";
				sCustEnb = "enabled";
			}
		} else {
			res = "<font class = " + IConstants.FAILED_COLOR
					+ ">Rental and Service Customer Exists already. Try again</font>";
			sCustEnb = "enabled";
		}
	}
	
	%>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Rental And Service Customer List</h3> 
</div>
</div>

<form class="form-horizontal" name="form" method="post">
<!-- <form method="post" name="form1"> -->

<div class="box-body">
<CENTER><strong><%=res%></strong></CENTER>
<div id="target" style="display:none">
 <div class="form-group">
      <label class="control-label col-sm-3"  for="Create Loan Assignee ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer ID:</label>
      <div class="col-sm-3">
      	<div class="input-group">
		    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />   
    		<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE" type="TEXT" value="<%=sCustCode%>" onchange="checkitem(this.value)" size="50" MAXLENGTH=50 width="50"<%=sCustEnb%>>
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i  class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
      	
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Supplier Name">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Name:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="CUST_NAME1" type="TEXT" value="<%=sCustName%>"
			style="width:100%" MAXLENGTH=100>
      </div>
    </div>
    </div>
    
    
        
         
    <div class="form-group">
         <label class="control-label col-sm-3" for="Contact Name">Contact Name:</label>
      <div class="col-sm-3">          
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
       <div class="form-inline">
      <label class="control-label col-sm-2" for="Designation">Designation:</label>
      <div class="col-sm-3">          
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" style="width:100%" MAXLENGTH="50">
      </div>
    </div>
    </div>
   
    <div class="form-group">
      <label class="control-label col-sm-3" for="Telephone No">Telephone:</label>
      <div class="col-sm-3">          
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
       <div class="form-inline">
      <label class="control-label col-sm-2" for="Hand Phone">Mobile:</label>
      <div class="col-sm-3">          
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" style="width:100%" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    </div>
   
    <div class="form-group">
      <label class="control-label col-sm-3" for="Fax">Fax:</label>
      <div class="col-sm-3">          
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return isNumber(event)"
			MAXLENGTH="30" class="form-control">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Email">Email:</label>
      <div class="col-sm-3">          
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" style="width:100%"
			MAXLENGTH="50" class="form-control">
      </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-3" for="Unit No">Unit No:</label>
      <div class="col-sm-3">          
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Building">Building:</label>
      <div class="col-sm-3">          
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" style="width:100%"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-3" for="Street">Street:</label>
      <div class="col-sm-3">          
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="City">City:</label>
      <div class="col-sm-3">          
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" style="width:100%"
			MAXLENGTH=50  class="form-control">
      </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-3" for="State">State:</label>
      <div class="col-sm-3">          
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Country">Country:</label>
      <div class="col-sm-3">          
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			style="width:100%" MAXLENGTH=50 class="form-control">
      </div>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-3" for="Postal Code">Postal Code:</label>
      <div class="col-sm-3">          
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
    <!-- <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">  -->         
        <INPUT name="REMARKS" type="hidden" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control">
      <!-- </div>
    </div> -->
    
    <div class="form-group">        
     <div class="text-center">
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<% if (al) { %>
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<% } else { %>
      	<button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<% } %>
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>

</div>

<div class="form-group">
      <div class="col-sm-6">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Rental And Service Order Customer </a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Rental And Service Order Customer</a>
      </div>
       	  </div>
</div>

<div>
   <table id="myTable" class="table">
    <thead style="background: #eaeafa">
        <TR>
      <TH >Customer ID</TH>
      <TH >Customer Name</TH>
      
       </TR>
       </thead>
       <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
/* MLogger mLogger = new MLogger(); */
mLogger.setLoggerConstans(loggerDetailsHasMap);
    CustUtil custUtils = new CustUtil();
    custUtils.setmLogger(mLogger);
    strUtils = new StrUtils();
    plant= (String)session.getAttribute("PLANT");
    sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
    

    String sBGColor = "";
    
   try{
    ArrayList arrCust = custUtils.getLoanAssigneeListStartsWithName(sCustName,"",plant," AND ISACTIVE='Y' " );
   
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        sCustCode     = (String)arrCustLine.get("LASSIGNNO");
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get("CNAME"));
        sContactName     = strUtils.removeQuotes((String)arrCustLine.get("NAME"));
        sTelNo     = strUtils.removeQuotes((String)arrCustLine.get("TELNO"));
        sEmail     = strUtils.removeQuotes((String)arrCustLine.get("EMAIL"));
        String sAdd1     = strUtils.removeQuotes((String)arrCustLine.get("ADDR1"));
        String sAdd2     = strUtils.removeQuotes((String)arrCustLine.get("ADDR2"));
       
        String sAdd3     = strUtils.removeQuotes((String)arrCustLine.get("ADDR3"));
        
        sRemarks     = strUtils.removeQuotes((String)arrCustLine.get("REMARKS"));
        String sAdd4     = strUtils.removeQuotes((String)arrCustLine.get("ADDR4"));
        sCountry    = strUtils.removeQuotes((String)arrCustLine.get("COUNTRY"));
        sZip     = strUtils.removeQuotes((String)arrCustLine.get("ZIP"));
       

%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUST_CODE.disabled = false;
                     window.opener.form.TOLOC.value='<%=sCustCode%>';
                     window.opener.form.CUST_CODE.value='<%=sCustCode%>';
                     window.opener.form.CUST_CODE1.value='<%=sCustCode%>';
                     window.opener.form.CUST_NAME.value='<%=sCustName1%>';
                     window.opener.form.PERSON_INCHARGE.value='<%=sContactName%>';
                     window.opener.form.TELNO.value='<%=sTelNo%>';
                     window.opener.form.EMAIL.value='<%=sEmail%>';
                     window.opener.form.ADD1.value='<%=sAdd1%>';
                     window.opener.form.ADD2.value='<%=sAdd2%>';
                     window.opener.form.ADD3.value='<%=sAdd3%>';
                     window.opener.form.REMARK2.value='<%=sRemarks%>';
                     window.opener.form.ADD4.value='<%=sAdd4%>';
                     window.opener.form.COUNTRY.value='<%=sCountry%>';
                     window.opener.form.ZIP.value='<%=sZip%>';
                     window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	</TR>
    
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
  
 </tbody>
 </table>
 <!-- <br>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>  -->   
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip();

//Below Jquery Script used for Show/Hide Function
    
    $('#Show').click(function() {
	    $('#target').show(500);
	    $('#Show').hide(0);
	    $('#Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('#Hide').click(function() {
	    $('#target').hide(500);
	    $('#Show').show(0);
	    $('#Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('#Show').click();
    }else{
    	$('#Hide').click();
    }	
});
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : aCustCode,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
				ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                            
						alert("Customer Already Exists");
						document.getElementById("CUST_CODE").focus();
						//document.getElementById("ITEM").value="";
						return false;
					}
					else
						return true;
				}
});	
		return true;
}
}	 
</script>