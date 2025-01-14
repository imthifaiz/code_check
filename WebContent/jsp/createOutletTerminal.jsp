<!-- CREATED BY imthi -->
<!-- CREATED ON 30-03-2022 -->
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Outlet Terminals";
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
window.onload = function(){
	var allowvariant = "0";
	var numOfDecimals = document.form.numberOfDecimal.value;  // number of decimal based on plntmst
	var decimalValue = parseFloat(allowvariant).toFixed(numOfDecimals);
	document.getElementById("ALLOWVARIANT").value = decimalValue;
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

function onNew(){
	document.form.TERMINAL_CODE.value = ""; 
	document.form.OUTLET_NAME.value = "";
	document.form.TERMINAL_NAME.value = "";
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
  
   var TERMINAL_CODE   = document.form.TERMINAL_CODE.value;
   var OUTLET_NAME   = document.form.OUTLET_NAME.value;
   var TERMINAL_NAME   = document.form.TERMINAL_NAME.value;
   var region = document.form.COUNTRY_REG.value;
   if(TERMINAL_CODE == "" || TERMINAL_CODE == null) {alert("Please Enter Terminal");;document.form.TERMINAL_CODE.focus(); return false; }
   
   if(OUTLET_NAME == "" || OUTLET_NAME == null) {
   alert("Please Select Outlet"); 
   document.form.OUTLET_NAME.focus();
   return false; 
   }
   if(TERMINAL_NAME == "" || TERMINAL_NAME == null) {
	   alert("Please Enter Terminal Name"); 
	   document.form.TERMINAL_NAME.focus();
	   return false; 
	   }
   document.form.action  = "../jsp/createOutletTerminal.jsp?action=ADD";
   document.form.submit();
}

function onIDGen()
{
	$.ajax({
		type: "GET",
		url: "../outletterminal/Auto-ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#TERMINAL_CODE").val(data.OUTLETTERMINAL);
		},
		error: function(data) {
			alert('Unable to generate Outlet. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
}

</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
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
	String sterminalCode = "", sOutlet = "", sTerminalName="",sOutCode="",TERMINAL_STARTTIME="",TERMINAL_ENDTIME="",sAllowVariant="",sIsAllowVariant="";
	String MON_STARTTIME="",MON_ENDTIME="",TUE_STARTTIME="",TUE_ENDTIME="",WED_STARTTIME="",WED_ENDTIME="",THUR_STARTTIME="",THUR_ENDTIME="",
			FRI_STARTTIME="",FRI_ENDTIME="",SAT_STARTTIME="",SAT_ENDTIME="",SUN_STARTTIME="",SUN_ENDTIME="",ISALLDAYS="",FLOATAMOUNT="0.0";
	String sSAVE_RED;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	OutletUtil outletUtil = new OutletUtil();
	OutletBeanDAO outletdao = new OutletBeanDAO();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	outletUtil.setmLogger(mLogger);
	
	List supplierattachlist= new ArrayList();
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String ValidNumber="";
	int novalid =outletdao.Outletcount(plant);
	sterminalCode = strUtils.fString(request.getParameter("TERMINAL_CODE"));
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
		if (sterminalCode.length() <= 0)
		sterminalCode = strUtils.fString(request.getParameter("OUTLET_CODE1"));
	sOutlet = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
	sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
	sTerminalName=StrUtils.fString(request.getParameter("TERMINAL_NAME"));
	TERMINAL_STARTTIME=StrUtils.fString(request.getParameter("TERMINAL_STARTTIME"));
	TERMINAL_ENDTIME=StrUtils.fString(request.getParameter("TERMINAL_ENDTIME"));
	
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
	
	sIsAllowVariant = strUtils.fString(request.getParameter("ISALLOWVARIANT"));
	sAllowVariant = strUtils.fString(request.getParameter("ALLOWVARIANT"));
	
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	MovHisDAO mdao = new MovHisDAO(plant);
	
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sterminalCode = "";
		sOutlet = "";
		sTerminalName="";
		TERMINAL_STARTTIME="";
		TERMINAL_ENDTIME="";
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
		sOutCode="";
		sAddEnb = "";
		sCustEnb = "";
		FLOATAMOUNT="";
		sIsAllowVariant = "";
		sAllowVariant = "";
	} 
	
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		if (!outletUtil.isExistOutlet(sterminalCode, plant) && !outletUtil.isExistTerminalName(sTerminalName, plant)) // if the Customer exists already
		{
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.TERMINAL, sterminalCode);
			ht.put(IConstants.OUTLETS_CODE, sOutCode);
		    ht.put(IConstants.TERMINAL_NAME,sTerminalName);
		    ht.put("TERMINAL_STARTTIME",TERMINAL_STARTTIME);
		    ht.put("TERMINAL_ENDTIME",TERMINAL_ENDTIME);
		    ht.put("ISALLDAYS",ISALLDAYS);
		    ht.put("FLOATAMOUNT",FLOATAMOUNT);
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put("ISALLOWVARIANT", sIsAllowVariant);
			ht.put("ALLOWVARIANT", sAllowVariant);
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);
			
			Hashtable htmon = new Hashtable();
			htmon.put(IConstants.PLANT, plant);
			htmon.put(IConstants.TERMINAL, sterminalCode);
			htmon.put("TERMINAL_LOGINTIME",MON_STARTTIME);
			htmon.put("TERMINAL_LOGOUTTIME",MON_ENDTIME);
			htmon.put("DAYS","MON");
			htmon.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htmon.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable httue = new Hashtable();
			httue.put(IConstants.PLANT, plant);
			httue.put(IConstants.TERMINAL, sterminalCode);
			httue.put("TERMINAL_LOGINTIME",TUE_STARTTIME);
			httue.put("TERMINAL_LOGOUTTIME",TUE_ENDTIME);
			httue.put("DAYS","TUE");
			httue.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			httue.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable htwed = new Hashtable();
			htwed.put(IConstants.PLANT, plant);
			htwed.put(IConstants.TERMINAL, sterminalCode);
			htwed.put("TERMINAL_LOGINTIME",WED_STARTTIME);
			htwed.put("TERMINAL_LOGOUTTIME",WED_ENDTIME);
			htwed.put("DAYS","WED");
			htwed.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htwed.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable htthur = new Hashtable();
			htthur.put(IConstants.PLANT, plant);
			htthur.put(IConstants.TERMINAL, sterminalCode);
			htthur.put("TERMINAL_LOGINTIME",THUR_STARTTIME);
			htthur.put("TERMINAL_LOGOUTTIME",THUR_ENDTIME);
			htthur.put("DAYS","THUR");
			htthur.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htthur.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable htfri = new Hashtable();
			htfri.put(IConstants.PLANT, plant);
			htfri.put(IConstants.TERMINAL, sterminalCode);
			htfri.put("TERMINAL_LOGINTIME",FRI_STARTTIME);
			htfri.put("TERMINAL_LOGOUTTIME",FRI_ENDTIME);
			htfri.put("DAYS","FRI");
			htfri.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htfri.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable htsat = new Hashtable();
			htsat.put(IConstants.PLANT, plant);
			htsat.put(IConstants.TERMINAL, sterminalCode);
			htsat.put("TERMINAL_LOGINTIME",SAT_STARTTIME);
			htsat.put("TERMINAL_LOGOUTTIME",SAT_ENDTIME);
			htsat.put("DAYS","SAT");
			htsat.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htsat.put(IConstants.CREATED_BY, sUserId);
			
			Hashtable htsun = new Hashtable();
			htsun.put(IConstants.PLANT, plant);
			htsun.put(IConstants.TERMINAL, sterminalCode);
			htsun.put("TERMINAL_LOGINTIME",SUN_STARTTIME);
			htsun.put("TERMINAL_LOGOUTTIME",SUN_ENDTIME);
			htsun.put("DAYS","SUN");
			htsun.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			htsun.put(IConstants.CREATED_BY, sUserId);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_OUTLETTERMINAL);
			htm.put("RECID", "");
			htm.put("ITEM",sterminalCode);
			
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		
		boolean updateFlag;
			if(sterminalCode!="TR001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "OUTLETTERMINAL");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
					updateFlag=_TblControlDAO.updateSeqNo("OUTLETTERMINAL",plant);				
			else
			{
				boolean insertFlag = false;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"OUTLETTERMINAL");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "TR");
				htTblCntInsert.put("MINSEQ", "000");
				htTblCntInsert.put("MAXSEQ", "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
			}
			}
		
			boolean custInserted = outletUtil.insertOutletTerminal(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
			if (custInserted) {
			boolean custmon= _OutletBeanDAO.insertIntoOutletTerminalTime(htmon);
			boolean custtue= _OutletBeanDAO.insertIntoOutletTerminalTime(httue);
			boolean custwed= _OutletBeanDAO.insertIntoOutletTerminalTime(htwed);
			boolean custthur= _OutletBeanDAO.insertIntoOutletTerminalTime(htthur);
			boolean custfry= _OutletBeanDAO.insertIntoOutletTerminalTime(htfri);
			boolean custsat= _OutletBeanDAO.insertIntoOutletTerminalTime(htsat);
			boolean custsun= _OutletBeanDAO.insertIntoOutletTerminalTime(htsun);

				sSAVE_RED="Outlet Terminal Added Successfully";
				sCustEnb = "disabled";
			} else {
				sSAVE_RED="Failed to add New Outlet Terminal";
				
				sCustEnb = "enabled";
			}
		} else {
			sSAVE_RED="Terminal Or Name Exists already. Try again with diffrent Terminal ID Or Name";
			
			sCustEnb = "enabled";
		}
	}

	//3. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = outletUtil.getOutletDetails(sterminalCode,plant);
			sterminalCode = (String) arrCust.get(0);
			sOutlet = (String) arrCust.get(1);
			sTerminalName = (String) arrCust.get(7);
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
                <li><label>Create Outlet Terminals</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../outletterminal/summary'" >
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="outletform" name="form" method="post">
   <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
   
    <div class="form-group">
      	<label class="control-label col-form-label col-sm-2 required" for="Terminal">Terminal</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<INPUT class="form-control" name="TERMINAL_CODE" id="TERMINAL_CODE" type="TEXT" value="<%=sterminalCode%>" size="50" MAXLENGTH=50 onkeypress="return blockSpecialChar(event)" width="50"<%=sCustEnb%>> 
   		 		<span class="input-group-addon"  onClick="onIDGen();">
   		 		<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 		<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  			</div>
  				<INPUT type="hidden" name="plant" value="<%=plant%>">
  				<INPUT type="hidden" name="OUTLET_CODE1" value="<%=sterminalCode%>">
  				<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  				<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  				<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
      		</div>
    </div>
    
       <div class="form-group">
      		<label class="control-label col-form-label col-sm-2 required" for="Terminal Name">Terminal Name</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TERMINAL_NAME" id="TERMINAL_NAME" type = "TEXT" value="<%=sTerminalName%>" size="50"  MAXLENGTH=50> 
  			</div>
      		</div>
    </div>
	    
    	<div class="form-group">
      		<label class="control-label col-form-label col-sm-2 required" for="Outlet Name">Outlet</label>
      			<div class="col-sm-4">
                	<INPUT class=" form-control" id="OUTLET_NAME" value="<%=sOutlet%>" name="OUTLET_NAME" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 		<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>    						
					<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
     			</div>
    	</div>
    	
    	 <div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Start Time">Drawer Amount</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="FLOATAMOUNT" id="FLOATAMOUNT" type="number" min="0" value="<%=FLOATAMOUNT%>" step="0.01"> 
  			</div>
      		</div>
    </div>
    
        
    <div class="form-group">
    	<label class="control-label col-form-label col-sm-2" for="IsAllowVariant">Is Allow Variant</label>
    	<div class="col-sm-4">
        	<input type="Checkbox" style="border:0;" name="ISALLOWVARIANT" value="" id="ISALLOWVARIANT"  onclick="isChecked(this);">
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
	     <label class="checkbox-inline"><INPUT Type=Checkbox   name ="ISALLDAYS" value="ISALLDAYS" id="ISALLDAYS" onclick="isalldays();">
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
    	
    </div>
    
  	  	    
    <div class="form-group">        
    <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>>Save</button>&nbsp;&nbsp;
    </div>
    </div>
  </form>
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
	 if(document.form.SAVE_RED.value!="")
		{
		document.form.action  = "../outletterminal/summary?PGaction=View&result=Outlet Terminal Added Successfully";
		document.form.submit();
		}



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
// 		return '<div onclick="document.form.OUTLET_NAME.value = \''+data.OUTLET_NAME+'\'"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
 });

 function setOutletData(OUTLET,OUTLET_NAME){
		$("input[name=OUTCODE]").val(OUTLET);
		$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
	}
</script>
<style>
	.bs-example{
    	margin: 20px;
    }
</style>

 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>