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

<!-- Not in Use - Menus status 0 -->
<%
String title = "Create Outlets";
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
window.onload = function(){
	var allowvariant = "0";
	var numOfDecimals = document.form.numberOfDecimal.value;  // number of decimal based on plntmst
	var decimalValue = parseFloat(allowvariant).toFixed(numOfDecimals);
	document.getElementById("ALLOWVARIANT").value = decimalValue;
}
function onNew(){
	document.form.OUTLET_CODE.value = ""; 
	document.form.OUTLET_NAME.value = "";
	document.form.CONTACT_PERSON.value = "";
	document.form.DESGINATION.value = "";  
	document.form.HPNO.value = ""; 
	document.form.TELNO.value = ""; 
	document.form.FAX.value = "";
	document.form.EMAIL.value = "";
	document.form.ADD1.value = "";
	document.form.ADD2.value = "";
	document.form.ADD3.value = "";
	document.form.ADD4.value = "";
	document.form.ZIP.value = "";
//	document.form.STATE.value = "";
//	document.form.COUNTRY_CODE.value = "";
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

function onAdd(){
  
   var OUTLET_CODE   = document.form.OUTLET_CODE.value;
   var OUTLET_NAME   = document.form.OUTLET_NAME.value;
   var CONTACT_PERSON   = document.form.CONTACT_PERSON.value;
   var region = document.form.COUNTRY_REG.value;
   var ValidNumber   = document.form.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" outlets you can create"); return false; }
   if(OUTLET_CODE == "" || OUTLET_CODE == null) {alert("Please Enter Outlet");;document.form.OUTLET_CODE.focus(); return false; }
   
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
   document.form.action  = "../jsp/createOutlet.jsp?action=ADD";
   document.form.submit();
}

function onIDGen()
{

	$.ajax({
		type: "GET",
		url: "../outlets/Auto-ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#OUTLET_CODE").val(data.OUTLET);
		},
		error: function(data) {
			alert('Unable to generate Outlet. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
	
 /* document.form.action  = "vendor_view.jsp?action=Auto-ID";
   document.form.submit(); 
 */
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
	String sOutletCode = "", sOutletName = "", sCustNameL = "",contactperson="",sAdd1 = "", sAdd2 = "", sAdd3 = "", sAdd4 = "", sCountry = "", sZip = "" ,sIsAllowVariant="",sAllowVariant="";
	String sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "", 
			desc="",sState="";
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
	String NOOFSUPPLIER=strUtils.fString((String) session.getAttribute("NOOFSUPPLIER"));
	String ValidNumber="";
	int novalid =outletdao.Outletcount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(novalid>=convl)
		{
			ValidNumber=NOOFSUPPLIER;
		}
	}
	
	 sOutletCode = strUtils.fString(request.getParameter("OUTLET_CODE"));
	
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if (sOutletCode.length() <= 0)
		sOutletCode = strUtils.fString(request.getParameter("OUTLET_CODE1"));
	sOutletName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
	contactperson=StrUtils.fString(request.getParameter("CONTACT_PERSON"));
	sAdd1 = strUtils.fString(request.getParameter("ADD1"));
	sAdd2 = strUtils.fString(request.getParameter("ADD2"));
	sAdd3 = strUtils.fString(request.getParameter("ADD3"));
	sAdd4 = strUtils.fString(request.getParameter("ADD4"));

	sState = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
	sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sIsAllowVariant = strUtils.fString(request.getParameter("ISALLOWVARIANT"));
	sAllowVariant = strUtils.fString(request.getParameter("ALLOWVARIANT"));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sFax = strUtils.fString(request.getParameter("FAX"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	MovHisDAO mdao = new MovHisDAO(plant);
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sOutletCode = "";
		sOutletName = "";
		sCustNameL = "";
		sAdd1 = "";
		sAdd2 = "";
		sAdd3 = "";
		contactperson="";
		sAdd4 = "";
		sCountry = "";
		sZip = "";
		sIsAllowVariant = "";
		sAllowVariant = "";
		sDesgination = "";
		sTelNo = "";
		sHpNo = "";
		sFax = "";
		sEmail = "";
		sAddEnb = "";
		sCustEnb = "";
		sState="";
	} 
	

	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		if (!outletUtil.isExistOutlet(sOutletCode, plant) && !outletUtil.isExistOutletName(sOutletName, plant)) // if the Customer exists already
		{
			
			  
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.OUTLETS_CODE, sOutletCode);
			ht.put(IConstants.OUTLET_NAME, sOutletName);
		    ht.put(IConstants.CONTACT_PERSON,contactperson);
			ht.put(IConstants.DESGINATION, sDesgination);
			ht.put(IConstants.TELNO, sTelNo);
			ht.put(IConstants.HPNO, sHpNo);
			ht.put(IConstants.FAX, sFax);
			ht.put(IConstants.EMAIL, sEmail);
			ht.put(IConstants.ADD1, sAdd1);
			ht.put(IConstants.ADD2, sAdd2);
			ht.put(IConstants.ADD3, sAdd3);
			ht.put(IConstants.ADD4, sAdd4);
			ht.put(IConstants.COUNTRY, sCountry);
			ht.put(IConstants.ZIP, sZip);
			ht.put("ISALLOWVARIANT", sIsAllowVariant);
			ht.put("ALLOWVARIANT", sAllowVariant);
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			ht.put(IConstants.ISACTIVE, "Y");
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			ht.put(IConstants.STATE, sState);
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_OUTLET);
			htm.put("RECID", "");
			htm.put("ITEM",sOutletCode);
			
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		
		boolean updateFlag;
			if(sOutletCode!="OT001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "OUTLET");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
					updateFlag=_TblControlDAO.updateSeqNo("OUTLET",plant);				
			else
			{
				boolean insertFlag = false;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"OUTLET");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "OT");
				htTblCntInsert.put("MINSEQ", "000");
				htTblCntInsert.put("MAXSEQ", "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
			}
			}
		
			boolean custInserted = outletUtil.insertOutlet(ht);
			
			 Hashtable htData = new Hashtable();	
// 	          String posquery="select PLANT,OUTLET from "+plant+"_POSOUTLETS WHERE PLANT ='"+plant+"'";
	          String itemquery="SELECT PLANT,ITEM,SALESUOM,UnitPrice FROM "+plant+"_ITEMMST WHERE PLANT ='"+plant+"'";
	          ArrayList itemList = new ItemMstUtil().selectForReport(itemquery,htData,"");
	          Map items = new HashMap();
	          if (itemList.size() > 0) {
	        	  for (int j=0; j < itemList.size(); j++ ) {
	        		  items = (Map) itemList.get(j);
	        		  String itemplant = (String) items.get("PLANT");
	        		  String item = (String) items.get("ITEM");
	        		  String salesuom = (String) items.get("SALESUOM");
	        		  String unitprice = (String) items.get("UnitPrice");
	       	       	Hashtable htCondition = new Hashtable();
	       	       		htCondition.put(IConstants.ITEM,item);
     	        		htCondition.put("OUTLET",sOutletCode);
     	        		htCondition.put(IConstants.PLANT,plant);
	        	    Hashtable hPos = new Hashtable();	
	        		  hPos.put(IConstants.PLANT,plant);
	        		  hPos.put("OUTLET",sOutletCode);
	        		  hPos.put(IConstants.ITEM,item);
	        		  hPos.put("SALESUOM",salesuom);
	        		  hPos.put(IConstants.PRICE,unitprice);
	        		  hPos.put("CRBY",username);
	        		  hPos.put("CRAT",dateutils.getDateTime());
	        		  if(!(new ItemUtil().isExistsPosOutletPrice(item,sOutletCode,plant))) {
	        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
	        		  }else {
	        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
	        		  }
	        	  }
	          }
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {

				sSAVE_RED="Outlets Added Successfully";
				sCustEnb = "disabled";
			} else {
				sSAVE_RED="Failed to add New Outlets";
				
				sCustEnb = "enabled";
			}
		} else {
			sSAVE_RED="Outlet Or Name Exists already. Try again with diffrent Outlet ID Or Name";
			
			sCustEnb = "enabled";
		}
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
		//    sAddEnb  = "disabled";
		if (outletUtil.isExistOutlet(sOutletCode, plant)) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.OUTLETS_CODE, sOutletCode);
			htUpdate.put(IConstants.OUTLET_NAME, sOutletName);
			 htUpdate.put(IConstants.CONTACT_PERSON,contactperson);
			//          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
			htUpdate.put(IConstants.DESGINATION, sDesgination);
			htUpdate.put(IConstants.TELNO, sTelNo);
			htUpdate.put(IConstants.HPNO, sHpNo);
			htUpdate.put(IConstants.FAX, sFax);
			htUpdate.put(IConstants.EMAIL, sEmail);
			htUpdate.put(IConstants.ADD1, sAdd1);
			htUpdate.put(IConstants.ADD2, sAdd2);
			htUpdate.put(IConstants.ADD3, sAdd3);
			htUpdate.put(IConstants.ADD4, sAdd4);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			htUpdate.put(IConstants.STATE, sState);
			htUpdate.put(IConstants.COUNTRY, sCountry);
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put("ISALLOWVARIANT", sIsAllowVariant);
			htUpdate.put("ALLOWVARIANT", sAllowVariant);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put("OUTLETNO", sOutletCode);
			htCondition.put(IConstants.PLANT, plant);

			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", "UPD_OUTLET");
			htm.put("RECID", "");
			htm.put("UPBY", username);
			htm.put("CRBY", username);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean custUpdated = outletUtil.updateOutlet(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custUpdated) {
				sSAVE_RED="Outlets Updated Successfully";
			} else {
				sSAVE_RED="Failed to Update Outlet";
			}
		} else {
			sSAVE_RED="Outlet doesn't not Exists. Try again";
			

		}
	}

	//4. >> Delete
	else if (action.equalsIgnoreCase("DELETE")) {
		sCustEnb = "disabled";
		
			if (outletUtil.isExistOutlet(sOutletCode, plant)) {
			boolean custDeleted = outletUtil.deleteOutlet(sOutletCode,
					plant);
			if (custDeleted) {
				res = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Outlet Deleted Successfully</font>";
				sOutletCode = "";
				sOutletName = "";
				sCustNameL = "";
				sAdd1 = "";
				sAdd2 = "";
				sAdd3 = "";
				sAdd4 = "";
				sState = "";
				sCountry = "";
				sZip = "";
				sIsAllowVariant = "";
				sAllowVariant = "";
				sDesgination = "";
				sTelNo = "";
				sHpNo = "";
				sFax = "";
				sEmail = "";

			} else {
				res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to Delete Outlet</font>";
				sAddEnb = "enabled";
			}
		} else {
			res = "<font class = " + IConstants.FAILED_COLOR+ ">Outlet doesn't not Exists. Try again</font>";
		}
	
	}

	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = outletUtil.getOutletDetails(sOutletCode,plant);

			sOutletCode = (String) arrCust.get(0);
			sOutletName = (String) arrCust.get(1);
			sAdd1 = (String) arrCust.get(2);
			sAdd2 = (String) arrCust.get(3);
			sAdd3 = (String) arrCust.get(4);
			sAdd4 = (String) arrCust.get(13);
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
			
			

		} catch (Exception e) {
			res = "no details found for Vendor id : " + sOutletCode;
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../outlets/summary"><span class="underline-on-hover">Outlet Summary</span></a></li>                         
                <li><label>Create Outlets</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../outlets/summary'" >
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="outletform" name="form" method="post">
  <input name="SUPPLIER_TYPE_DESC" type="hidden" value="<%=desc%>">
   <input type="number" id="numberOfDecimal" name="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Create Outlet ID">Outlet</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <INPUT class="form-control" name="OUTLET_CODE" id="OUTLET_CODE" type="TEXT" value="<%=sOutletCode%>" onchange="checkitem(this.value)" size="50" MAXLENGTH=50 onkeypress="return blockSpecialChar(event)" width="50"<%=sCustEnb%>> 
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  			<INPUT type="hidden" name="plant" value="<%=plant%>">
  		<INPUT type="hidden" name="OUTLET_CODE1" value="<%=sOutletCode%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Outlet Name">Outlet Name</label>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=sOutletName%>"
			size="50" MAXLENGTH=100>
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
      	<INPUT name="EMAIL"  type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	
      	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">  
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry1(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
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
       <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
      </div>
    </div>
      	
      	    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
               
        <INPUT name="ADD1" type="TEXT" value="<%=sAdd1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
              
        <INPUT name="ADD2" type="TEXT" value="<%=sAdd2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADD3" type="TEXT" value="<%=sAdd3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADD4" type="TEXT" value="<%=sAdd4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=sState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>           
       
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
        	<input type="Checkbox" style="border:0;" name="ISALLOWVARIANT" value="" id="ISALLOWVARIANT"  onclick="isChecked(this);">
    	</div>
	</div>
	
	<div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-2" for="AllowVariant">Allow Variant</label>
      <div class="col-sm-4">
        <INPUT name="ALLOWVARIANT" id="ALLOWVARIANT" type="TEXT" value="<%=sAllowVariant%>" size="50" MAXLENGTH=10 onkeypress="return isNumberKey(event,this,4)" oninput="updateInput()" class="form-control">
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
       
 <script>
 $(document).ready(function(){
	 $('[data-toggle="tooltip"]').tooltip();
	 if(document.form.SAVE_RED.value!="")
		{
		document.form.action  = "../outlets/summary?PGaction=View&result=Outlet Added Successfully";
		document.form.submit();
		}
 });


function checkitem(aOutletCode)
{	
	 if(aOutletCode=="" || aOutletCode.length==0 ) {
		alert("Enter Outlet ID!");
		document.getElementById("OUTLET_CODE").focus();
		return false;
	 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					OUTLET_CODE : aOutletCode,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "OUTLET_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Outlet Already Exists");
							document.getElementById("OUTLET_CODE").focus();
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

function OnCountry1(Country)
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
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}
$('select[name="COUNTRY_CODE"]').on('change', function(){
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

</script>
<style>
	.bs-example{
    	margin: 20px;
    }
</style>

 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>