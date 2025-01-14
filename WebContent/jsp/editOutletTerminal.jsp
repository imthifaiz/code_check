<!-- CREATED BY imthi -->
<!-- CREATED ON 30-03-2022 -->
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.PoHdrDAO"%>
<%@ page import="com.track.dao.PoDetDAO"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.CustomerBeanDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Outlet Terminals";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script src="../jsp/js/bootstrap-datepicker.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/clockpicker.js"></script>
<link rel="stylesheet" href="../jsp/css/clockpicker.css">


<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

	document.form.TERMINAL_NAME.value = "";
	document.form.OUTLET_NAME.value = ""; 
	document.form.DEVICE_NAME.value = ""; 
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

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function isChecked(obj){
	 if ($(obj).is(":checked")){
		 document.form.ISALLOWVARIANT.value = "1";
	 }else{
		 document.form.ISALLOWVARIANT.value = "0";
		 var allowvariant = "0";
		 var numOfDecimals = document.form.numberOfDecimal.value;  // number of decimal based on plntmst
		 var decimalValue = parseFloat(allowvariant).toFixed(numOfDecimals);
		 document.getElementById("ALLOWVARIANT").value = decimalValue;
	 }
}

function updateInput() {
	if (document.getElementById("ISALLOWVARIANT").checked == false) {
		alert("Please Check Allow Variant Checkbox To Enter Value")
		var allowvariant = "0";
		var numOfDecimals = document.form.numberOfDecimal.value;  // number of decimal based on plntmst
		var decimalValue = parseFloat(allowvariant).toFixed(numOfDecimals);
		document.getElementById("ALLOWVARIANT").value = decimalValue;
	}
}

function onUpdate(){
   var TERMINAL_CODE   = document.form.TERMINAL_CODE.value;
   var TERMINAL_NAME   = document.form.TERMINAL_NAME.value;
   var DEVICE_NAME   = document.form.DEVICE_NAME.value;
   var DEVICE_STATUS   = document.form.DEVICE_STATUS.value;
   var OUTLET_NAME   = document.form.OUTLET_NAME.value;
   var region = document.form.COUNTRY_REG.value;
   if(TERMINAL_CODE == "" || TERMINAL_CODE == null) {alert("Please Enter Terminal");document.form.TERMINAL_CODE.focus(); return false; }
   
   if(TERMINAL_NAME == "" || TERMINAL_NAME == null) {
	   alert("Please Enter Terminal Name"); 
	   document.form.TERMINAL_NAME.focus();
	   return false; 
	   }
   
   if(DEVICE_NAME == "" && DEVICE_STATUS == "1") {
 	 	  alert("To clear Device Name choose Device Status as Non Active");
 
 	 	return false; 
	}
   
   if(OUTLET_NAME == "" || OUTLET_NAME == null) {
	   alert("Please Enter Select Outlet"); 
	   document.form.OUTLET_NAME.focus();
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
		 document.form.action  = "../jsp/editOutletTerminal.jsp?action=UPDATE";
			document.form.submit();
   }
	else{
		return false;}	   
}
function onDelete(){
   var TERMINAL_CODE   = document.form.TERMINAL_CODE.value;
   if(TERMINAL_CODE == "" || TERMINAL_CODE == null) {alert("Please Enter Outlet ID");  return false; }
   var chkmsg=confirm("Are you sure you would like to delete? ");
   if(chkmsg){
   	document.form.action  = "../jsp/editOutletTerminal.jsp?action=DELETE";
   	document.form.submit();}
   else{
    return false;
   }
}
function onView(){
   var TERMINAL_CODE   = document.form.TERMINAL_CODE.value;
   if(TERMINAL_CODE == "" || TERMINAL_CODE == null) {alert("Please Enter Outlet ID"); return false; }
   document.form.action  = "../jsp/editOutletTerminal.jsp?action=VIEW";
   document.form.submit();
}

</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sterminalCode = "", sTerminalName = "", sDevicename="",sOutlet= "", sCons = "Y",sOutCode="",TERMINAL_STARTTIME="",TERMINAL_ENDTIME="",sIsAllowVariant="",sAllowVariant="";
	String isActive = "",isDevicestatus;
	String desc = "";
	String MON_STARTTIME="",MON_ENDTIME="",TUE_STARTTIME="",TUE_ENDTIME="",WED_STARTTIME="",WED_ENDTIME="",THUR_STARTTIME="",THUR_ENDTIME="",
			FRI_STARTTIME="",FRI_ENDTIME="",SAT_STARTTIME="",SAT_ENDTIME="",SUN_STARTTIME="",SUN_ENDTIME="",ISALLDAYS="",FLOATAMOUNT="0.0";
	String sSAVE_RED="",sSAVE_REDELETE;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	OutletUtil outletUtil = new OutletUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	outletUtil.setmLogger(mLogger);
	
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	sterminalCode = strUtils.fString(request.getParameter("TERMINAL_CODE"));
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	sTerminalName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINAL_NAME")));
	TERMINAL_STARTTIME = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINAL_STARTTIME")));
	TERMINAL_ENDTIME = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINAL_ENDTIME")));
	
	MON_STARTTIME=StrUtils.fString(request.getParameter("MON_STARTTIME"));
	MON_ENDTIME=StrUtils.fString(request.getParameter("MON_ENDTIME"));
	
	TUE_STARTTIME=StrUtils.fString(request.getParameter("TUE_STARTTIME"));
	TUE_ENDTIME=StrUtils.fString(request.getParameter("TUE_STARTTIME"));
	
	WED_STARTTIME=StrUtils.fString(request.getParameter("WED_STARTTIME"));
	WED_ENDTIME=StrUtils.fString(request.getParameter("WED_ENDTIME"));
	
	THUR_STARTTIME=StrUtils.fString(request.getParameter("THUR_STARTTIME"));
	THUR_ENDTIME=StrUtils.fString(request.getParameter("THUR_ENDTIME"));
	
	FRI_STARTTIME=StrUtils.fString(request.getParameter("FRI_STARTTIME"));
	FRI_ENDTIME=StrUtils.fString(request.getParameter("FRI_ENDTIME"));
	
	SAT_STARTTIME=StrUtils.fString(request.getParameter("SAT_STARTTIME"));
	SAT_ENDTIME=StrUtils.fString(request.getParameter("SAT_ENDTIME"));
	
	SUN_STARTTIME=StrUtils.fString(request.getParameter("SUN_STARTTIME"));
	SUN_ENDTIME=StrUtils.fString(request.getParameter("SUN_ENDTIME"));
	
	ISALLDAYS = (request.getParameter("ISALLDAYS") != null) ? "1": "0";
	
	FLOATAMOUNT = StrUtils.fString(request.getParameter("FLOATAMOUNT"));
	
	sDevicename = strUtils.InsertQuotes(strUtils.fString(request.getParameter("DEVICE_NAME")));
	sOutlet = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
	sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	isDevicestatus = strUtils.fString(request.getParameter("DEVICE_STATUS"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	
	
	sIsAllowVariant = strUtils.fString(request.getParameter("ISALLOWVARIANT"));
	sAllowVariant = strUtils.fString(request.getParameter("ALLOWVARIANT"));
	

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sterminalCode = "";
		sTerminalName = "";
		TERMINAL_STARTTIME = "";
		TERMINAL_ENDTIME = "";
		
		MON_STARTTIME="";
		MON_ENDTIME="";
		TUE_STARTTIME="";
		TUE_ENDTIME="";
		WED_STARTTIME="";
		WED_ENDTIME="";
		THUR_STARTTIME="";
		THUR_ENDTIME="";
		FRI_STARTTIME="";
		FRI_ENDTIME="";
		SAT_STARTTIME="";
		SAT_ENDTIME="";
		SUN_STARTTIME="";
		SUN_ENDTIME="";
		ISALLDAYS="";
		sDevicename = "";
		sOutlet = "";
		sOutCode="";
		sCons = "Y";
		sAddEnb = "";
		sCustEnb = "";
		FLOATAMOUNT="";
		sIsAllowVariant = "";
		sAllowVariant = "";
	}

	//2. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		if (outletUtil.isExistTerminal(sterminalCode, plant)) {
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if(!outletBeanDAO.isExistsOutletTerminals(ht,
					" TERMINAL <> '"+ sterminalCode + "'  AND TERMINAL_NAME = '" + sTerminalName + "'"))
			{
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.TERMINAL, sterminalCode);
			htUpdate.put(IConstants.TERMINAL_NAME, sTerminalName);
			htUpdate.put("TERMINAL_STARTTIME", TERMINAL_STARTTIME);
			htUpdate.put("TERMINAL_ENDTIME", TERMINAL_ENDTIME);
			htUpdate.put("ISALLDAYS",ISALLDAYS);
			htUpdate.put("DEVICE_NAME", sDevicename);
			htUpdate.put("DEVICE_STATUS", isDevicestatus);
			htUpdate.put("FLOATAMOUNT", FLOATAMOUNT);
			htUpdate.put(IConstants.OUTLETS_CODE, sOutCode);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put("ISALLOWVARIANT", sIsAllowVariant);
			htUpdate.put("ALLOWVARIANT", sAllowVariant);
			
			Hashtable htmon = new Hashtable();
			htmon.put(IConstants.PLANT, plant);
			htmon.put(IConstants.TERMINAL, sterminalCode);
			htmon.put("TERMINAL_LOGINTIME",MON_STARTTIME);
			htmon.put("TERMINAL_LOGOUTTIME",MON_ENDTIME);
			htmon.put("DAYS","MON");
			htmon.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htmon.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable httue = new Hashtable();
			httue.put(IConstants.PLANT, plant);
			httue.put(IConstants.TERMINAL, sterminalCode);
			httue.put("TERMINAL_LOGINTIME",TUE_STARTTIME);
			httue.put("TERMINAL_LOGOUTTIME",TUE_ENDTIME);
			httue.put("DAYS","TUE");
			httue.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			httue.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htwed = new Hashtable();
			htwed.put(IConstants.PLANT, plant);
			htwed.put(IConstants.TERMINAL, sterminalCode);
			htwed.put("TERMINAL_LOGINTIME",WED_STARTTIME);
			htwed.put("TERMINAL_LOGOUTTIME",WED_ENDTIME);
			htwed.put("DAYS","WED");
			htwed.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htwed.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htthur = new Hashtable();
			htthur.put(IConstants.PLANT, plant);
			htthur.put(IConstants.TERMINAL, sterminalCode);
			htthur.put("TERMINAL_LOGINTIME",THUR_STARTTIME);
			htthur.put("TERMINAL_LOGOUTTIME",THUR_ENDTIME);
			htthur.put("DAYS","THUR");
			htthur.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htthur.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htfri = new Hashtable();
			htfri.put(IConstants.PLANT, plant);
			htfri.put(IConstants.TERMINAL, sterminalCode);
			htfri.put("TERMINAL_LOGINTIME",FRI_STARTTIME);
			htfri.put("TERMINAL_LOGOUTTIME",FRI_ENDTIME);
			htfri.put("DAYS","FRI");
			htfri.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htfri.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htsat = new Hashtable();
			htsat.put(IConstants.PLANT, plant);
			htsat.put(IConstants.TERMINAL, sterminalCode);
			htsat.put("TERMINAL_LOGINTIME",SAT_STARTTIME);
			htsat.put("TERMINAL_LOGOUTTIME",SAT_ENDTIME);
			htsat.put("DAYS","SAT");
			htsat.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htsat.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htsun = new Hashtable();
			htsun.put(IConstants.PLANT, plant);
			htsun.put(IConstants.TERMINAL, sterminalCode);
			htsun.put("TERMINAL_LOGINTIME",SUN_STARTTIME);
			htsun.put("TERMINAL_LOGOUTTIME",SUN_ENDTIME);
			htsun.put("DAYS","SUN");
			htsun.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htsun.put(IConstants.UPDATED_BY, sUserId);
			
			Hashtable htCondition = new Hashtable();
			htCondition.put("TERMINAL", sterminalCode);
			htCondition.put(IConstants.PLANT, plant);
			
			Hashtable htConditionmon = new Hashtable();
			htConditionmon.put("TERMINAL", sterminalCode);
			htConditionmon.put("DAYS", "MON");
			htConditionmon.put(IConstants.PLANT, plant);
			
			
			Hashtable htConditiontue = new Hashtable();
			htConditiontue.put("TERMINAL", sterminalCode);
			htConditiontue.put("DAYS", "TUE");
			htConditiontue.put(IConstants.PLANT, plant);
			
			Hashtable htConditionwed = new Hashtable();
			htConditionwed.put("TERMINAL", sterminalCode);
			htConditionwed.put("DAYS", "WED");
			htConditionwed.put(IConstants.PLANT, plant);
			
			Hashtable htConditionthur = new Hashtable();
			htConditionthur.put("TERMINAL", sterminalCode);
			htConditionthur.put("DAYS", "THUR");
			htConditionthur.put(IConstants.PLANT, plant);
			
			Hashtable htConditionfri = new Hashtable();
			htConditionfri.put("TERMINAL", sterminalCode);
			htConditionfri.put("DAYS", "FRI");
			htConditionfri.put(IConstants.PLANT, plant);
			
			Hashtable htConditionsat = new Hashtable();
			htConditionsat.put("TERMINAL", sterminalCode);
			htConditionsat.put("DAYS", "SAT");
			htConditionsat.put(IConstants.PLANT, plant);
			
			Hashtable htConditionsun = new Hashtable();
			htConditionsun.put("TERMINAL", sterminalCode);
			htConditionsun.put("DAYS", "SUN");
			htConditionsun.put(IConstants.PLANT, plant);
			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_OUTLETTERMINAL);
			htm.put("RECID", "");
			htm.put("ITEM",sterminalCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean outletUpdated = outletUtil.updateOutletTerminal(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			boolean flag = false;
				OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
			if (outletUpdated) {
				boolean custmon= _OutletBeanDAO.updateOutletTerminalTime(htmon,htConditionmon);
				boolean custtue= _OutletBeanDAO.updateOutletTerminalTime(httue,htConditiontue);
				boolean custwed= _OutletBeanDAO.updateOutletTerminalTime(htwed,htConditionwed);
				boolean custthur= _OutletBeanDAO.updateOutletTerminalTime(htthur,htConditionthur);
				boolean custfry= _OutletBeanDAO.updateOutletTerminalTime(htfri,htConditionfri);
				boolean custsat= _OutletBeanDAO.updateOutletTerminalTime(htsat,htConditionsat);
				boolean custsun= _OutletBeanDAO.updateOutletTerminalTime(htsun,htConditionsun);
				
				sSAVE_RED = "Update";
			} else {
				sSAVE_RED = "Failed to Update Outlet Terminal";
			}
		} else {
			sSAVE_RED = "Terminal Name Exists already. Try again with diffrent Terminal Name";
			}
		} else {
			sSAVE_RED = "Terminal doesn't not Exists. Try again";
		}
	
			
	}

	//3. >> Delete
	else if(action.equalsIgnoreCase("DELETE")){
	
	MovHisDAO mdao=new MovHisDAO(plant);
	sCustEnb = "enabled";
    boolean movementhistoryExist=false;
	Hashtable htmh = new Hashtable();
	htmh.put("TERMINAL",strUtils.InsertQuotes(sterminalCode));
	htmh.put(IConstants.PLANT,plant);
	if(movementhistoryExist)
	{	
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Terminal Exists In Transactions</font>";
	}
	else{
    if(outletUtil.isExistTerminal(strUtils.InsertQuotes(sterminalCode),plant))
    {
          boolean custDeleted = outletUtil.deleteOutletTerminal(strUtils.InsertQuotes(sterminalCode),plant);
          
           mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.DEL_OUTLETTERMINAL);
			htm.put("RECID", "");
			htm.put("ITEM",sterminalCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			
          if(custDeleted) {
                    sSAVE_REDELETE = "Delete";
                    sterminalCode  = "";
                    sTerminalName  = "";
                    TERMINAL_STARTTIME  = "";
                    TERMINAL_ENDTIME  = "";
                    sOutlet = "";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Outlet Terminal</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Terminal doesn't not Exists. Try again</font>";
    }
	}
}
	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = outletUtil.getOutletTerminalDetails(sterminalCode,plant);
			sterminalCode = (String) arrCust.get(0);
			sTerminalName = (String) arrCust.get(1);
			sOutlet = (String) arrCust.get(6);
			sOutCode = (String) arrCust.get(2);
			isActive = (String) arrCust.get(3);
			sDevicename = (String) arrCust.get(4);
			isDevicestatus = (String) arrCust.get(5);
			TERMINAL_STARTTIME = (String) arrCust.get(7);
			TERMINAL_ENDTIME = (String) arrCust.get(8);
			ISALLDAYS = (String) arrCust.get(9);
			FLOATAMOUNT = (String) arrCust.get(10);
			sIsAllowVariant = (String) arrCust.get(11);
			sAllowVariant = (String) arrCust.get(12);
			
			ArrayList arrCustmon = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"MON");
			MON_STARTTIME = (String) arrCustmon.get(1);
			MON_ENDTIME = (String) arrCustmon.get(2);
			
			ArrayList arrCusttue = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"TUE");
			TUE_STARTTIME = (String) arrCusttue.get(1);
			TUE_ENDTIME = (String) arrCusttue.get(2);
			
			ArrayList arrCustwed = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"WED");
			WED_STARTTIME = (String) arrCustwed.get(1);
			WED_ENDTIME = (String) arrCustwed.get(2);
			
			ArrayList arrCustthur = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"THUR");
			THUR_STARTTIME = (String) arrCustthur.get(1);
			THUR_ENDTIME = (String) arrCustthur.get(2);
			
			ArrayList arrCustfri = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"FRI");
			FRI_STARTTIME = (String) arrCustfri.get(1);
			FRI_ENDTIME = (String) arrCustfri.get(2);
			
			ArrayList arrCustsat = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"SAT");
			SAT_STARTTIME = (String) arrCustsat.get(1);
			SAT_ENDTIME = (String) arrCustsat.get(2);
			
			ArrayList arrCustsun = outletUtil.getOutletTerminalTimeDetail(sterminalCode,plant,"SUN");
			SUN_STARTTIME = (String) arrCustsun.get(1);
			SUN_ENDTIME = (String) arrCustsun.get(2);
			
		} catch (Exception e) {
			res = "no details found for Terminal id : " + sterminalCode;
		}
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../outletterminal/summary"><span class="underline-on-hover">Outlet Terminals Summary</span></a></li>                       
                <li><label>Edit Outlet Terminals</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../outletterminal/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="outletform" name="form" method="post">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Maintain Outlet ID">Terminal</label>
      	<div class="col-sm-4">
       	<div class="input-group">    
    		<input name="TERMINAL_CODE" type="TEXT" value="<%=sterminalCode%>" size="50" MAXLENGTH=50 class="form-control" readonly>
  		</div> 
	  		<INPUT type="hidden" name="OUTLET_CODE1" value="">
	  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
	  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
	  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
       	</div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Terminal Name">Terminal Name</label>
      	<div class="col-sm-4">
      		<div class="input-group">
        		<INPUT  class="form-control" name="TERMINAL_NAME" id="TERMINAL_NAME" type="TEXT" value="<%=sTerminalName%>" size="50" MAXLENGTH=50>
			</div>
      	</div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Device Name">Device Name</label>
      	<div class="col-sm-4">
      		<div class="input-group">
        		<INPUT  class="form-control" name="DEVICE_NAME" id="DEVICE_NAME" type="TEXT" value="<%=sDevicename%>" size="50" MAXLENGTH=50>
			</div>
      	</div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Device Status">Device Status</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      		<label class="radio-inline">
        		<input type="radio" name="DEVICE_STATUS" value="1"<%if (isDevicestatus.equalsIgnoreCase("1")) {%> checked <%}%>>Active</label>
        	<label class="radio-inline">	
        		<input type="radio" name="DEVICE_STATUS" value="0"<%if (isDevicestatus.equalsIgnoreCase("0")) {%> checked <%}%>>Non Active</label>
        		
			</div>
      	</div>
    </div>
    
    
    <div class="form-group">
	      <label class="control-label col-form-label col-sm-2 required" for="Outlet">Outlet</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">   
	      		<INPUT class=" form-control" id="OUTLET_NAME" value="<%=sOutlet%>" name="OUTLET_NAME" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 	<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i></span>    
					<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
	  		</div>
	        </div>
	</div>
	
	 <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Drawer Amount">Drawer Amount</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="FLOATAMOUNT" id="FLOATAMOUNT" type="number" min="0" value="<%=FLOATAMOUNT%>" step="0.01"> 
  			</div>
      		</div>
    </div>
    
    <div class="form-group">
    	<label class="control-label col-form-label col-sm-2" for="IsAllowVariant">Is Allow Variant</label>
    	<div class="col-sm-4">
        	<input type="Checkbox" style="border:0;" name="ISALLOWVARIANT" value="<%=sIsAllowVariant%>" <%if (sIsAllowVariant.equalsIgnoreCase("1")) {%> checked <%}%> id="ISALLOWVARIANT"  onclick="isChecked(this);">
    	</div>
	</div>
	
	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="AllowVariant">Allow Variant</label>
      <div class="col-sm-4">
        <INPUT name="ALLOWVARIANT" id="ALLOWVARIANT" type="TEXT" value="<%=sAllowVariant%>" size="50" MAXLENGTH=10 onkeypress="return isNumberKey(event,this,4)" oninput="updateInput()" class="form-control">
     </div>
    </div>
	
	<div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Start Time">Start Time</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TERMINAL_STARTTIME" id="TERMINAL_STARTTIME" type = "TEXT" value="<%=TERMINAL_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="End Time">End Time</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TERMINAL_ENDTIME" id="TERMINAL_ENDTIME" type = "TEXT" value="<%=TERMINAL_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="form-inline">
      		<div class="col-sm-4">
	     <label class="checkbox-inline"><INPUT Type=Checkbox   name ="ISALLDAYS" id="ISALLDAYS" value="1"<%if (ISALLDAYS.equalsIgnoreCase("1")) {%> checked <%}%> onclick="isalldays();">
                     Apply to all Days</label>
         </div>
         </div>
    </div>
    
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Monday">Monday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="MON_STARTTIME" id="MON_STARTTIME" type = "TEXT" value="<%=MON_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="MON_ENDTIME" id="MON_ENDTIME" type = "TEXT" value="<%=MON_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Tuesday">Tuesday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TUE_STARTTIME" id="TUE_STARTTIME" type = "TEXT" value="<%=TUE_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TUE_ENDTIME" id="TUE_ENDTIME" type = "TEXT" value="<%=TUE_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Wednesday">Wednesday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="WED_STARTTIME" id="WED_STARTTIME" type = "TEXT" value="<%=WED_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="WED_ENDTIME" id="WED_ENDTIME" type = "TEXT" value="<%=WED_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Thursday">Thursday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="THUR_STARTTIME" id="THUR_STARTTIME" type = "TEXT" value="<%=THUR_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="THUR_ENDTIME" id="THUR_ENDTIME" type = "TEXT" value="<%=THUR_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Friday">Friday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="FRI_STARTTIME" id="FRI_STARTTIME" type = "TEXT" value="<%=FRI_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="FRI_ENDTIME" id="FRI_ENDTIME" type = "TEXT" value="<%=FRI_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Mon Time">Saturday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="SAT_STARTTIME" id="SAT_STARTTIME" type = "TEXT" value="<%=SAT_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="SAT_ENDTIME" id="SAT_ENDTIME" type = "TEXT" value="<%=SAT_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Sunday">Sunday</label>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="SUN_STARTTIME" id="SUN_STARTTIME" type = "TEXT" value="<%=SUN_STARTTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
      		<div class="col-sm-2">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="SUN_ENDTIME" id="SUN_ENDTIME" type = "TEXT" value="<%=SUN_ENDTIME%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
	
    <div class="form-group">
  	<div class="col-sm-offset-2 col-sm-8">
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
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button class="Submit btn btn-default" type="BUTTON"  onClick="return onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
     </div>
     </div>
  </form>
  </div>
</div>
</div>
</div>
 
<script type="text/javascript">
$('#TERMINAL_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#TERMINAL_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#MON_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#MON_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#TUE_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#TUE_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#WED_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#WED_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#THUR_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#THUR_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#FRI_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#FRI_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#SAT_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#SAT_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#SUN_STARTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#SUN_ENDTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
</script> 
<script>

function isalldays(){

	if (document.getElementById("ISALLDAYS").checked == true) {
		document.getElementById("ISALLDAYS").value = "1";
		var terminal_starttime = document.getElementById("TERMINAL_STARTTIME").value;
		var terminal_endtime = document.getElementById("TERMINAL_ENDTIME").value;
			     
		document.getElementById("MON_STARTTIME").value = terminal_starttime;
		document.getElementById("TUE_STARTTIME").value = terminal_starttime;
		document.getElementById("WED_STARTTIME").value = terminal_starttime;
		document.getElementById("THUR_STARTTIME").value = terminal_starttime;
		document.getElementById("FRI_STARTTIME").value = terminal_starttime;
		document.getElementById("SAT_STARTTIME").value = terminal_starttime;
		document.getElementById("SUN_STARTTIME").value = terminal_starttime;
		
		document.getElementById("MON_ENDTIME").value = terminal_endtime;
		document.getElementById("TUE_ENDTIME").value = terminal_endtime;
		document.getElementById("WED_ENDTIME").value = terminal_endtime;
		document.getElementById("THUR_ENDTIME").value = terminal_endtime;
		document.getElementById("FRI_ENDTIME").value = terminal_endtime;
		document.getElementById("SAT_ENDTIME").value = terminal_endtime;
		document.getElementById("SUN_ENDTIME").value = terminal_endtime;
	}else {
		document.getElementById("ISALLDAYS").value = "0";
		document.getElementById("MON_STARTTIME").value = "";
		document.getElementById("TUE_STARTTIME").value = "";
		document.getElementById("WED_STARTTIME").value = "";
		document.getElementById("THUR_STARTTIME").value = "";
		document.getElementById("FRI_STARTTIME").value = "";
		document.getElementById("SAT_STARTTIME").value = "";
		document.getElementById("SUN_STARTTIME").value = "";
		
		document.getElementById("MON_ENDTIME").value = "";
		document.getElementById("TUE_ENDTIME").value = "";
		document.getElementById("WED_ENDTIME").value = "";
		document.getElementById("THUR_ENDTIME").value = "";
		document.getElementById("FRI_ENDTIME").value = "";
		document.getElementById("SAT_ENDTIME").value = "";
		document.getElementById("SUN_ENDTIME").value = "";
		}
}



$(document).ready(function(){


	
    $('[data-toggle="tooltip"]').tooltip();

    $('#OUTLET_NAME').typeahead({
  	  hint: true,
  	  minLength:0,  
  	  searchOnFocus: true
  	},
  	{
  	  display: 'OUTLET_NAME',  
  	  async: true,   
  	  source: function (query, process,asyncProcess) {
  		var urlStr = "/track/MasterServlet";
  		$.ajax( {
  		type : "POST",
  		url : urlStr,
  		async : true,
  		data : {
  			PLANT : "<%=plant%>",
  			ACTION : "GET_OUTLET_DATA",
  			QUERY : query
  		},
  		dataType : "json",
  		success : function(data) {
  			return asyncProcess(data.POSOUTLETS);
  		}
  		});
  	  },
  	  limit: 9999,
  	  templates: {
  	  empty: [
  	      '<div style="padding:3px 20px">',
  	        'No results found',
  	      '</div>',
  	    ].join('\n'),
  	    suggestion: function(data) {
//   		return '<div onclick="document.form.OUTLET_NAME.value = \''+data.OUTLET_NAME+'\'"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
  	    	return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
  		}
  	  }
  	}).on('typeahead:open',function(event,selection){
  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
  		element.toggleClass("glyphicon-menu-up",true);
  		element.toggleClass("glyphicon-menu-down",false);
  	}).on('typeahead:close',function(){
  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
  		element.toggleClass("glyphicon-menu-up",false);
  		element.toggleClass("glyphicon-menu-down",true);
  	}).on('typeahead:change',function(event,selection){
		if($(this).val() == ""){
			document.form.OUTCODE.value = "";
		}
  	});
  	
    if(document.form.SAVE_RED.value!="")
	{    
    	document.form.action  = "../outletterminal/summary?PGaction=View&result=Outlet Terminal Updated Successfully";
    	document.form.submit(); 
	}
  
});

$(document).ready(function(){
	 $('[data-toggle="tooltip"]').tooltip();
		
	    if(document.form.SAVE_REDELETE.value!=""){
	    	document.form.action  = "../outletterminal/summary?PGaction=View&result=Outlet Terminal Deleted Successfully";
	    	 document.form.submit();
		}

});

function setOutletData(OUTLET,OUTLET_NAME){
	$("input[name=OUTCODE]").val(OUTLET);
	$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
}

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>