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
String title = "Edit Freight Forwarder";
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

//	document.form.SHIPPER_CODE.value = ""; 
	document.form.SHIPPER_NAME.value = "";
	document.form.transport.value = "";
	document.form.TELNO.value = ""; 
	document.form.ADDR1.value = "";
	document.form.ADDR2.value = "";
	document.form.ADDR3.value = "";
	document.form.ADDR4.value = "";
	document.form.ZIP.value = "";
	document.form.CONTACTNAME.value = "";
	document.form.DESGINATION.value = "";
	document.form.WORKPHONE.value = "";
	document.form.HPNO.value = "";
	document.form.EMAIL.value = "";
	document.form.REMARKS.value = "";
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
function onUpdate(){
   var SHIPPER_CODE   = document.form.SHIPPER_CODE.value;
   var SHIPPER_NAME   = document.form.SHIPPER_NAME.value;
   var region = document.form.COUNTRY_REG.value;
   if(SHIPPER_CODE == "" || SHIPPER_CODE == null) {alert("Please Enter Freight Forwarder ID");document.form.SHIPPER_CODE.focus(); return false; }
   
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
   
    
   var chk = confirm("Are you sure you would like to save?");
	if(chk){
   add_attachments();
   }
	else{
		return false;}	   
}
function onDelete(){
   var SHIPPER_CODE   = document.form.SHIPPER_CODE.value;
   if(SHIPPER_CODE == "" || SHIPPER_CODE == null) {alert("Please Enter Freight Forwarder ID");  return false; }
var chkmsg=confirm("Are you sure you would like to delete? ");
    if(chkmsg){
   document.form.action  = "../jsp/editShipper.jsp?action=DELETE";
   document.form.submit();}
   else
   { return false;
   }
}
function onView(){
   var SHIPPER_CODE   = document.form.SHIPPER_CODE.value;
   if(SHIPPER_CODE == "" || SHIPPER_CODE == null) {alert("Please Enter Freight Forwarder ID"); return false; }

   document.form.action  = "../jsp/editShipper.jsp?action=VIEW";
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
	String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sState = "", sCountry = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "",transport="",  sEmail = "", sRemarks = "",isActive = "";
	String desc = "",sCountryCode="";
	String WEBSITE="",WORKPHONE="";
	String sSAVE_RED="",transportmode="",sSAVE_REDELETE;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	ShipperUtil shipUtil = new ShipperUtil();
//	PoHdrDAO PoHdrDAO = new  PoHdrDAO();
//	PoDetDAO PoDetDAO = new  PoDetDAO();
//	RecvDetDAO RecvDetDAO = new RecvDetDAO();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	ShipperDAO shipperDAO = new ShipperDAO();
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

	sState = strUtils.fString(request.getParameter("STATE"));
	sCountry = strUtils.fString(request.getParameter("COUNTRY"));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sRemarks = strUtils.fString(request.getParameter("REMARKS"));
	
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	transport=strUtils.fString(request.getParameter("TRANSPORTID"));
	
	
	WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
	WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
	sCountryCode=strUtils.fString(request.getParameter("EDIT_COUNTRY"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	

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
		sEmail = "";
		sRemarks = "";
		sAddEnb = "";
		sCustEnb = "";
	//	suppliertypeid="";
		transport="";
		WEBSITE="";WORKPHONE="";
		sCountryCode="";
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		if (shipUtil.isExistShipper(sCustCode, plant)) {
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if(!shipperDAO.isExistsShipper(ht,
					" FREIGHT_FORWARDERNO <> '"+ sCustCode + "'  AND FREIGHT_FORWARDERNAME = '" + sCustName + "'"))
			{
				
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put("FREIGHT_FORWARDERNO" ,sCustCode);
			htUpdate.put("FREIGHT_FORWARDERNAME" ,sCustName);
			htUpdate.put(IConstants.NAME, sContactName);
			htUpdate.put(IConstants.DESGINATION, sDesgination);
			htUpdate.put("LICENCENO", sTelNo);
			htUpdate.put(IConstants.HPNO, sHpNo);
			htUpdate.put("CUSTOMEREMAIL", sEmail);
			htUpdate.put(IConstants.ADDRESS1, sAddr1);
			htUpdate.put(IConstants.ADDRESS2, sAddr2);
			htUpdate.put(IConstants.ADDRESS3, sAddr3);
			htUpdate.put(IConstants.ADDRESS4, sAddr4);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			htUpdate.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			htUpdate.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.ISACTIVE,"Y");
			htUpdate.put(IConstants.TRANSPORTID,transport);
			
			htUpdate.put(IConstants.WEBSITE,WEBSITE);
			htUpdate.put(IConstants.WORKPHONE,WORKPHONE);
			Hashtable htCondition = new Hashtable();
			htCondition.put("FREIGHT_FORWARDERNO", sCustCode);
			htCondition.put(IConstants.PLANT, plant);
			
			

			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_FREIGHTFORWARDER);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.REMARKS, sCustName+","+strUtils.InsertQuotes(sRemarks));
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean custUpdated = shipUtil.updateShipper(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			boolean flag = false;
		
			if (custUpdated) {
				sSAVE_RED = "Update";
			} else {
				sSAVE_RED = "Failed to Update Freight Forwarder";
			}
		} else {
			sSAVE_RED = "Freight Forwarder Name Exists already. Try again with diffrent Freight Forwarder Name";

			}
			
		} else {
			sSAVE_RED = "Freight Forwarder doesn't not Exists. Try again";

		}
	
			
	}
else if(action.equalsIgnoreCase("DELETE")){
	
	MovHisDAO mdao=new MovHisDAO(plant);
	sCustEnb = "enabled";
    PoHdrDAO dao = new PoHdrDAO();
    boolean movementhistoryExist=false;
	Hashtable htmh = new Hashtable();
	htmh.put("CUSTCODE",strUtils.InsertQuotes(sCustCode));
	htmh.put(IConstants.PLANT,plant);
	movementhistoryExist = dao.isExisit(htmh,"");
	if(movementhistoryExist)
	{	
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Freight Forwarder Exists In Transactions</font>";
		
	}
	else{
    if(shipUtil.isExistShipper(strUtils.InsertQuotes(sCustCode),plant))
    {
          boolean custDeleted = shipUtil.deleteShipper(strUtils.InsertQuotes(sCustCode),plant);
          
           mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.DEL_FREIGHTFORWARDER);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.REMARKS, sCustName+","+strUtils.InsertQuotes(sRemarks));
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			custDeleted = mdao.insertIntoMovHis(htm);
		//	supplierAttachDAO.deletesupplierAttachByNo(plant, sCustCode);	
          if(custDeleted) {
                    /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Supplier Deleted Successfully</font>"; */
                    sSAVE_REDELETE = "Delete";
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
                    sEmail       = "";
                    sRemarks     = "";
                    sCons      = "Y";
					transport="";
					WEBSITE="";WORKPHONE="";
					sCountryCode="";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Freight Forwarder</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Freight Forwarder doesn't not Exists. Try again</font>";
    }
	}
}
	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = shipUtil.getShipperDetails(sCustCode,plant);
			sCustCode = (String) arrCust.get(0);
			sCustName = (String) arrCust.get(1);
			sTelNo = (String) arrCust.get(3);
			sContactName = (String) arrCust.get(4);
			sDesgination = (String) arrCust.get(5);
			sAddr1 = (String) arrCust.get(6);
			sAddr2 = (String) arrCust.get(7);
			sAddr3 = (String) arrCust.get(8);
			sAddr4 = (String) arrCust.get(9);
			sState = (String) arrCust.get(10);
			sCountry = (String) arrCust.get(11);
			sZip = (String) arrCust.get(12);
			WORKPHONE = (String) arrCust.get(13);
			sHpNo = (String) arrCust.get(14);
			sEmail = (String) arrCust.get(15);
			WEBSITE = (String) arrCust.get(16);
			sRemarks = (String) arrCust.get(17);
			isActive = (String) arrCust.get(18);
			sCountryCode = (String) arrCust.get(19);
		
			TransportModeDAO transportmodedao = new TransportModeDAO();
			transport = (String) arrCust.get(2);
			int trans = Integer.valueOf(transport);
			if(trans > 0){
				transportmode = transportmodedao.getTransportModeById(plant,trans);
			}
		else{
			transportmode = "";
		}
		
			
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
                <li><label>Edit Freight Forwarder</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../shipper/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="supplierform" name="form" method="post">
<%-- <input name="SUPPLIER_TYPE_DESC" type="hidden" value="<%=desc%>"> --%>
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Create Shipper ID">Freight Forwarder Id</label>
      <div class="col-sm-4">
      	<div class="input-group">   
      	  	 <INPUT class="form-control" name="SHIPPER_CODE" id="SHIPPER_CODE" type="TEXT" value="<%=sCustCode%>" onchange="checkitem(this.value)" size="50" MAXLENGTH=50 onkeypress="return blockSpecialChar(event)" width="50" readonly <%=sCustEnb%>> 
   		 	<!-- <span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=sState%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  	    <input type="hidden" name="TRANSPORTID" value="<%=transport%>">
  	    <%--  <input type="hidden" name="SUPPLIER_TYPE_ID" value="<%=suppliertypeid%>"> --%>
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
    		<input name="transport"  id="transport"  type="TEXT" value="<%=transportmode%>" placeholder="Select a transport"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
  	</div>
  	</div>
	
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Licence Number</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO"  id="TELNO"  type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
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
            <a href="#home" class="nav-link" data-toggle="tab" aria-expanded="true">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
    <div class="tab-content clearfix">
        <div class="tab-pane active" id="home">
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
        <INPUT name="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=50 class="form-control">
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
    	<INPUT name="WEBSITE" id="WEBSITE"  type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
		     
        </div>
        
        
        <div class="tab-pane fade" id="remark">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4"> 
               
        <%-- <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"	size="50" MAXLENGTH=100 class="form-control "> --%>
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=100><%=sRemarks%></textarea>
      </div>
    </div>
    </div>
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

    <!-- modal page-->
<%@include file="../jsp/newTransportModeModal.jsp"%> 
  --%>
<script>
$(document).ready(function(){

	
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

	
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form.SAVE_RED.value!="")
	{    
    	document.form.action  = "../shipper/summary?PGaction=View&result=Freight Forwarder Updated Successfully";
    	document.form.submit(); 
	}
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();

	
    if(document.form.SAVE_REDELETE.value!=""){
    	document.form.action  = "../shipper/summary?PGaction=View&result=Freight Forwarder Deleted Successfully";
    	 document.form.submit();
	}
});

$(document).ready(function(){
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

});



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
		//	$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
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
	
}

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
        	document.form.action  = "../jsp/editShipper.jsp?action=UPDATE";
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