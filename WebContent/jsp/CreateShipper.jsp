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
String title = "Create Freight Forwarder";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
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
	document.form.SHIPPER_CODE.value = ""; 
	document.form.SHIPPER_NAME.value = "";
	/* document.form.SUPPLIER_TYPE_ID.value = "";
	document.form.SUPPLIER_TYPE_DESC.value = ""; */
	document.form.transport.value = "";
	document.form.TELNO.value = ""; 
	document.form.WEBSITE.value = "";
	
	
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
  
   var SHIPPER_CODE   = document.form.SHIPPER_CODE.value;
   var SHIPPER_NAME   = document.form.SHIPPER_NAME.value;
   var ValidNumber   = document.form.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Freight Forwarder you can create"); return false; }
   if(SHIPPER_CODE == "" || SHIPPER_CODE == null) {alert("Please Enter Freight Forwarder ID");;document.form.SHIPPER_CODE.focus(); return false; }
   
   if(SHIPPER_NAME == "" || SHIPPER_NAME == null) {
   alert("Please Enter Freight Forwarder Name"); 
   document.form.SHIPPER_NAME.focus();
   return false; 
   }

   if(form.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	 form.COUNTRY_CODE.focus();
	 return false;
	}
   add_attachments();
   
}
function onIDGen()
{

	$.ajax({
		type: "GET",
		url: "../shipper/Auto-ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#SHIPPER_CODE").val(data.FREIGHT_FORWARDER);
		},
		error: function(data) {
			alert('Unable to generate Freight Forwarder ID. Please try again later.');
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
	String sCustCode = "", sCustName = "", sCustNameL = "",sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "", 
			sRemarks = "",transport="",desc="",sState="";
	String WEBSITE="",WORKPHONE="",sSAVE_RED;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	ShipperUtil shipUtil = new ShipperUtil();
	ShipperDAO shipdao = new ShipperDAO();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//	SupplierAttachDAO supplierAttachDAO = new SupplierAttachDAO();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
//	CurrencyDAO currencyDAO = new CurrencyDAO();
	shipUtil.setmLogger(mLogger);
	
	List supplierattachlist= new ArrayList();
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String ISPEPPOL = plantMstDAO.getisPeppol(plant);
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	//Validate no.of supplier -- Azees 15.11.2020
	String NOOFSUPPLIER=strUtils.fString((String) session.getAttribute("NOOFSUPPLIER"));
	 String ValidNumber="";
	int novalid =shipdao.Shippercount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(novalid>=convl)
		{
			ValidNumber=NOOFSUPPLIER;
		}
	} 

	sCustCode = strUtils.fString(request.getParameter("SHIPPER_CODE"));
	
	String taxbylabel= ub.getTaxByLable(plant);
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if (sCustCode.length() <= 0)
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
	sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SHIPPER_NAME")));
	sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
	sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
	sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
	sAddr4 = strUtils.fString(request.getParameter("ADDR4"));
	WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));

	sState = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
	sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
	transport=strUtils.fString(request.getParameter("TRANSPORTID"));
	
	WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	
/* 	List suppliertypelist=shipUtil.getVendorTypeList("",plant," AND ISACTIVE ='Y'"); */
	MovHisDAO mdao = new MovHisDAO(plant);
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
		sState="";
		transport="";
	
		WEBSITE="";WORKPHONE="";
		
	} 
	

	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		if (!shipUtil.isExistShipper(sCustCode, plant) && !shipUtil.isExistShipperName(sCustName, plant)) // if the Customer exists already
		{
			
			  
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put("FREIGHT_FORWARDERNO", sCustCode);
			ht.put("FREIGHT_FORWARDERNAME", sCustName);
			ht.put(IConstants.TRANSPORTID, transport);
			ht.put(IConstants.NAME, sContactName);
			ht.put(IConstants.DESGINATION, sDesgination);
			ht.put("LICENCENO", sTelNo);
			ht.put("CUSTOMEREMAIL", sEmail);
			ht.put(IConstants.HPNO, sHpNo);
			ht.put(IConstants.ADDRESS1, sAddr1);
			ht.put(IConstants.ADDRESS2, sAddr2);
			ht.put(IConstants.ADDRESS3, sAddr3);
			ht.put(IConstants.ADDRESS4, sAddr4);
			ht.put(IConstants.COUNTRY, sCountry);
			ht.put(IConstants.ZIP, sZip);
			ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			ht.put(IConstants.ISACTIVE, "Y");
	        ht.put(IConstants.WEBSITE,WEBSITE);
	        ht.put(IConstants.WORKPHONE,WORKPHONE);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			ht.put(IConstants.STATE, sState);
			
	
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_FREIGHTFORWARDER);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			if(!sRemarks.equals(""))
			{
				htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
			}
			else
			{
				htm.put(IDBConstants.REMARKS, sCustName);
			}
			
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		
		boolean updateFlag;
			if(sCustCode!="FF0001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "FREIGHT_FORWARDER");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
					updateFlag=_TblControlDAO.updateSeqNo("FREIGHT_FORWARDER",plant);				
			else
			{
				boolean insertFlag = false;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"FREIGHT_FORWARDER");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "FF");
				htTblCntInsert.put("MINSEQ", "0000");
				htTblCntInsert.put("MAXSEQ", "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
			}
			}
		
			boolean custInserted = shipUtil.insertShipper(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {

				sSAVE_RED="Freight Forwarder Added Successfully";
				sCustEnb = "disabled";
			} else {
				sSAVE_RED="Failed to add New Freight Forwarder";
				sCustEnb = "enabled";
			}
		} else {
			sSAVE_RED="Freight Forwarder ID Or Name Exists already. Try again with diffrent Freight Forwarder ID Or Name";
			sCustEnb = "enabled";
		}
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
		//    sAddEnb  = "disabled";
		if (shipUtil.isExistShipper(sCustCode, plant)) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.FREIGHTFORWARDER_CODE, sCustCode);
			htUpdate.put(IConstants.FREIGHTFORWARDER_NAME, sCustName);
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
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			htUpdate.put(IConstants.STATE, sState);
			htUpdate.put(IConstants.COUNTRY, sCountry);
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put(IConstants.USERFLG1, sCons);
			htUpdate.put(IConstants.REMARKS, sRemarks);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.TRANSPORTID, transport);

			Hashtable htCondition = new Hashtable();
			htCondition.put("FREIGHT_FORWARDERNO", sCustCode);
			htCondition.put(IConstants.PLANT, plant);

			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", "UPD_FREIGHTFORWARDER");
			htm.put("RECID", "");
			htm.put("UPBY", username);
			htm.put("CRBY", username);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean custUpdated = shipUtil.updateShipper(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custUpdated) {
				sSAVE_RED="Freight Forwarder Updated Successfully";
			} else {
				sSAVE_RED="Failed to Update Freight Forwarder";
			}
		} else {
			sSAVE_RED="Shipper doesn't not Exists. Try again";
			

		}
	}

	//4. >> Delete
	else if (action.equalsIgnoreCase("DELETE")) {
		sCustEnb = "disabled";
		
			if (shipUtil.isExistShipper(sCustCode, plant)) {
			boolean custDeleted = shipUtil.deleteShipper(sCustCode,
					plant);
			if (custDeleted) {
				res = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Freight Forwarder Deleted Successfully</font>";
				//                    sAddEnb    = "disabled";
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
				sContactName = "";
				sDesgination = "";
				sTelNo = "";
				sHpNo = "";
				sEmail = "";
				sRemarks = "";
				sCons = "Y";
				transport="";
				WEBSITE="";WORKPHONE="";

			} else {
				res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to Delete Freight Forwarder</font>";
				sAddEnb = "enabled";
			}
		} else {
			res = "<font class = " + IConstants.FAILED_COLOR+ ">Freight Forwarder doesn't not Exists. Try again</font>";
		}
	
	}

	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = shipUtil.getShipperDetails(sCustCode,
					plant);
			sCustCode = (String) arrCust.get(0);
			sCustName = (String) arrCust.get(1);
			transport = (String) arrCust.get(2);
			sTelNo = (String) arrCust.get(3);//licence
			sContactName = (String) arrCust.get(4);//name
			sDesgination = (String) arrCust.get(5);
			sAddr1 = (String) arrCust.get(6);
			sAddr2 = (String) arrCust.get(7);
			sAddr3 = (String) arrCust.get(8);
			sAddr4 = (String) arrCust.get(9);
			sCountry = (String) arrCust.get(11);
			sZip = (String) arrCust.get(12);
			WORKPHONE = (String) arrCust.get(13);
			sHpNo = (String) arrCust.get(14);//MOBILE
			sEmail = (String) arrCust.get(15);
			sRemarks = (String) arrCust.get(17);
			sState = (String) arrCust.get(10);
			
			
			
			

		} catch (Exception e) {
			res = "no details found for Freight Forwarder id : " + sCustCode;
		}

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../shipper/summary"><span class="underline-on-hover">Freight Forwarder Summary</span></a></li>                         
                <li><label>Create Freight Forwarder</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../shipper/summary'" >
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="supplierform" name="form" method="post">
   <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Create Shipper ID">Freight Forwarder Id</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <INPUT class="form-control" name="SHIPPER_CODE" id="SHIPPER_CODE" type="TEXT" value="<%=sCustCode%>" onchange="checkitem(this.value)" size="50" MAXLENGTH=50 onkeypress="return blockSpecialChar(event)" width="50"<%=sCustEnb%>> 
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  			<INPUT type="hidden" name="plant" value="<%=plant%>">
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		 <INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>"> 
  	    <input type="hidden" name="TRANSPORTID" value="">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Shipper Name">Freight Forwarder Name</label>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="SHIPPER_NAME" id="SHIPPER_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
	    
	
	
	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      <div class="col-sm-4">           	
    		<input name="transport" id="transport" type="TEXT" placeholder="Select a transport"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
  	</div>
	</div>
	
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Licence Number</label>
      <div class="col-sm-4">
                 
        <INPUT name="TELNO" id="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    
    <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="Contact Name">Contact Name</label>
      <div class="col-sm-4">
                 
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
                
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30">
      </div>
    </div>
    	


<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	
        <li class="nav-item active">
            <a href="#home" class="nav-link" data-toggle="tab">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
    <div class="tab-content clearfix">
        <div class="tab-pane active"  id="home">
        <br>
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
               
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
              
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
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
    
    <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Work phone">Work Phone</label>
      	<div class="col-sm-4">  
        <INPUT name="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=30 class="form-control">
      	</div>
    	</div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone">Mobile</label>
      <div class="col-sm-4">
              
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>  
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Email">Email</label>
      <div class="col-sm-4"> 
              
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
    </div>
     <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      	<div class="col-sm-4">  
    	<INPUT name="WEBSITE" id="WEBSITE" type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
		     
        </div>
        
        
        
        <div class="tab-pane fade" id="remark">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4"> 
               
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=100><%=sRemarks%></textarea>
      </div>
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
           <!-- modal page-->
<%@include file="../jsp/newTransportModeModal.jsp"%> 

 <script>
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form.SAVE_RED.value!="")
		{
		document.form.action  = "../shipper/summary?PGaction=View&result=Freight Forwarder Added Successfully";
		document.form.submit();
		}




		//transport
		$('#transport').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TRANSPORT_MODE',  
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "../MasterServlet";
					$.ajax({
						type : "POST",
						url : urlStr,
						async : true,
						data : {				
							ACTION : "GET_TRANSPORT_LIST",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.TRANSPORTMODE);
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
			    return '<p>' + data.TRANSPORT_MODE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				$('.transportAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:select',function(event,selection){
				$("input[name=TRANSPORTID]").val(selection.ID);
			});
			
	});

function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Freight Forwarder ID!");
		document.getElementById("SHIPPER_CODE").focus();
		return false;
	 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					SHIPPER_CODE : aCustCode,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "FREIGHT_FORWARDER_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Freight Forwarder Already Exists");
							document.getElementById("SHIPPER_CODE").focus();
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




function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}


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
			//$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
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
<script>
    $(document).ready(function(){ 
        $("#myTab a").click(function(e){
            e.preventDefault();
            $(this).tab('show');
        });
        
      
    });
    function add_attachments(){
        var formData = new FormData($('#supplierform')[0]);
        var userId= form.SHIPPER_CODE.value;
    	if(userId){
        $.ajax({
            type: 'post',
            url: "/track/SupplierAttachmentServlet?Submit=add_attachments",
    	    data:  formData,//{key:val}
    	    contentType: false,
    	    processData: false,
            success: function (data) {
            	console.log(data);
            //	window.location.reload();
            	document.form.action  = "../jsp/CreateShipper.jsp?action=ADD";
             document.form.submit();
            },
            error: function (data) {
                alert(data.responseText);
            }
        });
    	}else{
    		alert("Please enter Freight Forwarder Id");
    	}
            return false; 
      }
</script>
 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>