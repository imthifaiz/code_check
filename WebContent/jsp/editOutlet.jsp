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
<!-- Not in Use - Menus status 0 -->
<%
String title = "Edit Outlets";
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


<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

//	document.form.OUTLET_CODE.value = ""; 
	document.form.OUTLET_NAME.value = "";
	document.form.CONTACT_PERSON.value = ""; 
	document.form.TELNO.value = ""; 
	document.form.FAX.value = "";
	document.form.ADD1.value = "";
	document.form.ADD2.value = "";
	document.form.ADD3.value = "";
	document.form.ADD4.value = "";
	document.form.ZIP.value = "";
	document.form.DESGINATION.value = "";
	document.form.HPNO.value = "";
	document.form.EMAIL.value = "";
//	document.form.STATE.value = "";
//	document.form.COUNTRY_CODE.value = ""
	
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
   var OUTLET_CODE   = document.form.OUTLET_CODE.value;
   var OUTLET_NAME   = document.form.OUTLET_NAME.value;
   var CONTACT_PERSON   = document.form.CONTACT_PERSON.value;
   var region = document.form.COUNTRY_REG.value;
   if(OUTLET_CODE == "" || OUTLET_CODE == null) {alert("Please Enter Outlet");document.form.OUTLET_CODE.focus(); return false; }
   
   if(OUTLET_NAME == "" || OUTLET_NAME == null) {
	   alert("Please Enter Outlet Name"); 
	   document.form.OUTLET_NAME.focus();
	   return false; 
	   }
   if(CONTACT_PERSON == "" || CONTACT_PERSON == null) {
	   alert("Please Enter Contact Person"); 
	   document.form.CONTACT_PERSON.focus();
	   return false; 
	   }
   
   if(form.COUNTRY_CODE.selectedIndex==0)
	{
	 alert("Please Select Country");
	 form.COUNTRY_CODE.focus();
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
		 document.form.action  = "../jsp/editOutlet.jsp?action=UPDATE";
			document.form.submit();
   }
	else{
		return false;}	   
}
function onDelete(){
   var OUTLET_CODE   = document.form.OUTLET_CODE.value;
   if(OUTLET_CODE == "" || OUTLET_CODE == null) {alert("Please Enter Outlet ID");  return false; }
var chkmsg=confirm("Are you sure you would like to delete? ");
    if(chkmsg){
   document.form.action  = "../jsp/editOutlet.jsp?action=DELETE";
   document.form.submit();}
   else{
    return false;
   }
}
function onView(){
   var OUTLET_CODE   = document.form.OUTLET_CODE.value;
   if(OUTLET_CODE == "" || OUTLET_CODE == null) {alert("Please Enter Outlet ID"); return false; }

   document.form.action  = "../jsp/editOutlet.jsp?action=VIEW";
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
	String sOutletcode = "", sOutletName = "", contactperson= "",  sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sState = "", sCountry = "", sZip = "", sCons = "Y",sIsAllowVariant="",sAllowVariant="";
	String  sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",  isActive = "";
	String desc = "",sCountryCode="";
//	String CUSTOMEREMAIL="";
	String sSAVE_RED="",sSAVE_REDELETE;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	OutletUtil outletUtil = new OutletUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	outletUtil.setmLogger(mLogger);
	
	
	List supplierattachlist= new ArrayList();
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	sOutletcode = strUtils.fString(request.getParameter("OUTLET_CODE"));
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if (sOutletcode.length() <= 0)
		sOutletcode = strUtils.fString(request.getParameter("OUTLET_CODE1"));
	sOutletName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
	contactperson = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACT_PERSON")));

	sAddr1 = strUtils.fString(request.getParameter("ADD1"));
	sAddr2 = strUtils.fString(request.getParameter("ADD2"));
	sAddr3 = strUtils.fString(request.getParameter("ADD3"));
	sAddr4 = strUtils.fString(request.getParameter("ADD4"));

	sState = strUtils.fString(request.getParameter("STATE"));
	sCountry = strUtils.fString(request.getParameter("COUNTRY"));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sIsAllowVariant = strUtils.fString(request.getParameter("ISALLOWVARIANT"));
	sAllowVariant = strUtils.fString(request.getParameter("ALLOWVARIANT"));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sFax = strUtils.fString(request.getParameter("FAX"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sCountryCode=strUtils.fString(request.getParameter("EDIT_COUNTRY"));

	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sOutletcode = "";
		sOutletName = "";
		contactperson = "";
		sAddr1 = "";
		sAddr2 = "";
		sAddr3 = "";
		sAddr4 = "";
		sState = "";
		sCountry = "";
		sZip = "";
		sIsAllowVariant = "";
		sAllowVariant = "";
		sCons = "Y";
		sDesgination = "";
		sTelNo = "";
		sHpNo = "";
		sFax = "";
		sEmail = "";
		sAddEnb = "";
		sCustEnb = "";
		sCountryCode="";
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		if (outletUtil.isExistOutlet(sOutletcode, plant)) {
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if(!outletBeanDAO.isExistsOutlet(ht,
					" OUTLET <> '"+ sOutletcode + "'  AND OUTLET_NAME = '" + sOutletName + "'"))
			{
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.OUTLETS_CODE, sOutletcode);
			htUpdate.put(IConstants.OUTLET_NAME, sOutletName);
			htUpdate.put(IConstants.CONTACT_PERSON, contactperson);
			htUpdate.put(IConstants.DESGINATION, sDesgination);
			htUpdate.put(IConstants.TELNO, sTelNo);
			htUpdate.put(IConstants.HPNO, sHpNo);
			htUpdate.put(IConstants.FAX, sFax);
			htUpdate.put(IConstants.EMAIL, sEmail);
			htUpdate.put(IConstants.ADD1, sAddr1);
			htUpdate.put(IConstants.ADD2, sAddr2);
			htUpdate.put(IConstants.ADD3, sAddr3);
			htUpdate.put(IConstants.ADD4, sAddr4);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			htUpdate.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			htUpdate.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put("ISALLOWVARIANT", sIsAllowVariant);
			htUpdate.put("ALLOWVARIANT", sAllowVariant);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			
			Hashtable htCondition = new Hashtable();
			htCondition.put("OUTLET", sOutletcode);
			htCondition.put(IConstants.PLANT, plant);
			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_OUTLET);
			htm.put("RECID", "");
			htm.put("ITEM",sOutletcode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean outletUpdated = outletUtil.updateOutlet(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			boolean flag = false;
		
			if (outletUpdated) {
				sSAVE_RED = "Update";
			} else {
				sSAVE_RED = "Failed to Update Outlet";
			}
		} else {
			sSAVE_RED = "Outlet Name Exists already. Try again with diffrent Outlet Name";

			}
			
		} else {
			sSAVE_RED = "Outlet doesn't not Exists. Try again";

		}
	
			
	}
else if(action.equalsIgnoreCase("DELETE")){
	
	MovHisDAO mdao=new MovHisDAO(plant);
	ItemUtil itemUtil = new ItemUtil();
	sCustEnb = "enabled";
    boolean movementhistoryExist=false;
	Hashtable htmh = new Hashtable();
	htmh.put("OUTLET",strUtils.InsertQuotes(sOutletcode));
	htmh.put(IConstants.PLANT,plant);
	if(movementhistoryExist)
	{	
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Outlet Exists In Transactions</font>";
		
	}
	else{
    if(outletUtil.isExistOutlet(strUtils.InsertQuotes(sOutletcode),plant))
    {
          boolean custDeleted = outletUtil.deleteOutlet(strUtils.InsertQuotes(sOutletcode),plant);
          
          if((itemUtil.isExistsPosOutletminmax("",sOutletcode,plant))) 
	          custDeleted = new POSHdrDAO().deletePosOutletminmax(strUtils.InsertQuotes(sOutletcode),plant);
          
          if((itemUtil.isExistsPosOutletPrice("",sOutletcode,plant))) 
    	      custDeleted = new POSHdrDAO().deletePosOutletPrice(strUtils.InsertQuotes(sOutletcode),plant);
    	      
           mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.DEL_OUTLET);
			htm.put("RECID", "");
			htm.put("ITEM",sOutletcode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			
          if(custDeleted) {
                    sSAVE_REDELETE = "Delete";
                    sOutletcode  = "";
                    sOutletName  = "";
                    contactperson = "";
                    sAddr1     = "";
                    sAddr2     = "";
                    sAddr3     = "";sAddr4="";
                    sState   = "";
                    sCountry   = "";
                    sZip       = "";
                    sIsAllowVariant       = "";
                    sAllowVariant       = "";
                    sDesgination = "";
                    sTelNo       = "";
                    sHpNo        = "";
                    sFax         = "";
                    sEmail       = "";
					sCountryCode="";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Outlet</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Outlet doesn't not Exists. Try again</font>";
    }
	}
}
	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = outletUtil.getOutletDetails(sOutletcode,plant);
			sOutletcode = (String) arrCust.get(0);
			sOutletName = (String) arrCust.get(1);
			sAddr1 = (String) arrCust.get(2);
			sAddr2 = (String) arrCust.get(3);
			sAddr3 = (String) arrCust.get(4);
			sAddr4 = (String) arrCust.get(13);
			sCountry = (String) arrCust.get(5);
			sZip = (String) arrCust.get(6);
			sIsAllowVariant = (String) arrCust.get(17);
			sAllowVariant = (String) arrCust.get(18);
			contactperson = (String) arrCust.get(7);
			sDesgination = (String) arrCust.get(8);
			sTelNo = (String) arrCust.get(9);
			sHpNo = (String) arrCust.get(10);
			sEmail = (String) arrCust.get(12);
			sFax = (String) arrCust.get(11);
			isActive = (String) arrCust.get(14);
			sState = (String) arrCust.get(15);
			sCountryCode = (String) arrCust.get(16);
			
		} catch (Exception e) {
			res = "no details found for Outlet id : " + sOutletcode;
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../outlets/summary"><span class="underline-on-hover">Outlet Summary</span></a></li>                       
                <li><label>Edit Outlet</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../outlets/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="outletform" name="form" method="post">
<input type="number" id="numberOfDecimal" name="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Maintain Outlet ID">Outlet</label>
      <div class="col-sm-4">
       	<div class="input-group">    
    		<input name="OUTLET_CODE" type="TEXT" value="<%=sOutletcode%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
  		</div> 
  		
  	
  		<INPUT type="hidden" name="OUTLET_CODE1" value="">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=sState%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
       	</div>
       </div>
    
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Outlet Name">Outlet Name</label>
      <div class="col-sm-4">
      <div class="input-group">
        <INPUT  class="form-control" name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=sOutletName%>"
			size="50" MAXLENGTH=50>
			</div>
      </div>
    </div>
    
    <div class="form-group" id="CP">
	      <label class="control-label col-form-label col-sm-2 required" for="Contact Person">Contact Person</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="CONTACT_PERSON" id="CONTACT_PERSON" type = "TEXT" value="<%=contactperson%>" size="50"  MAXLENGTH=50> 
	  		</div>
	      </div>
	    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
                
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Telephone No</label>
      <div class="col-sm-4">
        <INPUT name="TELNO" id="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="10">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone">Mobile</label>
      <div class="col-sm-4">
              
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="10">
      </div>
    </div> 
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Fax</label>
      <div class="col-sm-4">
               
        <INPUT name="FAX" id="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return isNumber(event)"
			MAXLENGTH="10" class="form-control">
      </div>
    </div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Outlet Email">Email</label>
      	<div class="col-sm-4">  
      	<INPUT name="EMAIL" id="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH=10 class="form-control">
      	</div>
      	</div>
      	
      	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">  
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>      
      </div>
    </div>
      	
      	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
               
        <INPUT name="ADD1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
              
        <INPUT name="ADD2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADD3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADD4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=sState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>           
        <%-- <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control"> --%>
      </div>
    </div>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
        
    <div class="form-group" style="display: none;">
    	<label class="control-label col-form-label col-sm-2" for="IsAllowVariant">Is Allow Variant</label>
    	<div class="col-sm-4">
        	<input type="Checkbox" style="border:0;" name="ISALLOWVARIANT" value="<%=sIsAllowVariant%>" <%if (sIsAllowVariant.equalsIgnoreCase("1")) {%> checked <%}%> id="ISALLOWVARIANT"  onclick="isChecked(this);">
    	</div>
	</div>
	
	<div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-2" for="AllowVariant">Allow Variant</label>
      <div class="col-sm-4">
        <INPUT name="ALLOWVARIANT" id="ALLOWVARIANT" type="TEXT" value="<%=sAllowVariant%>" size="50" MAXLENGTH=10 onkeypress="return isNumberKey(event,this,4)" oninput="updateInput()" class="form-control">
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


 
<script>
$(document).ready(function(){


	
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form.SAVE_RED.value!="")
	{    
    	document.form.action  = "../outlets/summary?PGaction=View&result=Outlets Updated Successfully";
    	document.form.submit(); 
	}
  
});

$(document).ready(function(){
	
	var allowvariant = document.form.ALLOWVARIANT.value;
	var numOfDecimals = document.form.numberOfDecimal.value;  // number of decimal based on plntmst
	var decimalValue = parseFloat(allowvariant).toFixed(numOfDecimals);
	document.form.ALLOWVARIANT.value = decimalValue;
	
	if(document.form.EDIT_COUNTRY.value!="")
		{
		$("select[name ='COUNTRY_CODE']").val(document.form.EDIT_COUNTRY.value);
		OnCountry(document.form.EDIT_COUNTRY.value);		
		if(document.form.EDIT_STATE.value!="")
			{
			$("select[name ='STATE']").val(document.form.EDIT_STATE.value);
			   document.getElementById("STATE").value = document.form.EDIT_STATE.value;

			}
		}
	 $('[data-toggle="tooltip"]').tooltip();
		
	    if(document.form.SAVE_REDELETE.value!=""){
	    	document.form.action  = "../outlets/summary?PGaction=View&result=Outlet Deleted Successfully";
	    	 document.form.submit();
		}

});




function OnCountry(Country)
{	
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#STATE').empty();
			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 
						 if(document.form.EDIT_STATE.value==value.text)
						 	$('#STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
						 else					 
					   		$('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}

$('select[name="COUNTRY_CODE"]').on('change', function(){
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
    document.form.EDIT_STATE.value="";
});
$('select[name="STATE"]').on('change', function(){
$("#STATE option").removeAttr('selected');
document.form.EDIT_STATE.value="";
});



</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>