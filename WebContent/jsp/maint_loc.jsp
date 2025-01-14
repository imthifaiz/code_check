<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Location";
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
	var subWin = null;
	var strShipping = "SHIPPINGAREA";
	var strShipping1 = "ShippingArea";
	var strShipping2 = "Shippingarea";
	var strShipping3 = "shippingarea";

	var strTemp = "TEMP_TO";
	var strTemp1 = "Temp_To";
	var strTemp2 = "Temp_to";
	var strTemp3 = "temp_to";

	/* var strHolding = "HOLDINGAREA";
	var strHolding1 = "HoldingArea";
	var strHolding2 = "Holdingarea";
	var strHolding3 = "holdingarea"; */

	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'LOCATIONS',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew() {
		document.form1.action = "/track/locmstservlet?action=NEW";
		document.form1.submit();
	}

	function locationTypeCallback(data){
		if(data.STATUS="SUCCESS"){
			$("#LOC_TYPE_ID").typeahead('val', data.LOC_TYPE_ID1);
		}
	}

	function locationType2Callback(data){
		if(data.STATUS="SUCCESS"){
			$("#LOC_TYPE_ID2").typeahead('val', data.LOC_TYPE_ID2);
		}
	}
	
	function locationType3Callback(data){
		if(data.STATUS="SUCCESS"){
			$("#LOC_TYPE_ID3").typeahead('val', data.LOC_TYPE_ID3);
		}
	}

	
	
	
	function onAdd() {
		var LOC_ID = document.form1.LOC_ID.value;
		var strShippingArea = LOC_ID.substr(0, 12);
		var strTempTo = LOC_ID.substr(0, 7);
		//var strHoldingArea = LOC_ID.substr(0, 11);

		if (LOC_ID == "" || LOC_ID == null) {
			alert("Please Enter Location Id");
			return false;
		}

		
		
		if (strShippingArea == strShipping || strShippingArea == strShipping1
				|| strShippingArea == strShipping2
				|| strShippingArea == strShipping3) {
			alert("This location used by system cannot created ");
			return false;
		}
		if (strTempTo == strTemp || strTempTo == strTemp1
				|| strTempTo == strTemp2 || strTempTo == strTemp3) {
			alert("This location cannot created used by system");
			return false;
		}
		/* if (strHoldingArea == strHolding || strHoldingArea == strHolding1
				|| strHoldingArea == strHolding2
				|| strHoldingArea == strHolding3) {
			alert("This location used by system cannot created");
			return false;
		} */

		document.form1.action = "/track/locmstservlet?action=ADD";
		document.form1.submit();
	}
	function onUpdate() {

		var LOC_ID = document.form1.LOC_ID.value;
		var LOC_TYPE_ID1   = document.form1.LOC_TYPE_ID.value;
   		var LOC_TYPE_ID2   = document.form1.LOC_TYPE_ID2.value;
   		var LOC_TYPE_ID3   = document.form1.LOC_TYPE_ID3.value;
		var COMNAME = document.form1.COMNAME.value;
		var strShippingArea = LOC_ID.substr(0, 12);
		var strTempTo = LOC_ID.substr(0, 7);
		//var strHoldingArea = LOC_ID.substr(0, 11);

		if (LOC_ID == "" || LOC_ID == null) {
			alert("Please Enter Location ID");
			document.form1.LOC_ID.focus();
			return false;
		}

		if(LOC_TYPE_ID1 != "")

	   	if(LOC_TYPE_ID2 != "")
		
		if(LOC_TYPE_ID1 == LOC_TYPE_ID2) {
			if(LOC_TYPE_ID1!="NOLOCTYPE"&&LOC_TYPE_ID2!="NOLOCTYPE"){
			alert("Please enter Location Type Two different from Location Type One");document.form1.LOC_TYPE_ID2.focus(); return false; 
			}
			}

   		if(LOC_TYPE_ID3 != "")
   	   		
   	   		if(LOC_TYPE_ID1 == LOC_TYPE_ID3) {
   	   			if(LOC_TYPE_ID1!="NOLOCTYPE"&&LOC_TYPE_ID3!="NOLOCTYPE"){
   	   			alert("Please enter Location Type Three different from Location Type One");document.form1.LOC_TYPE_ID3.focus(); return false; 
   	   			}
   	   		}else if(LOC_TYPE_ID2 == LOC_TYPE_ID3) {
   	   	   		if(LOC_TYPE_ID1!="NOLOCTYPE"&&LOC_TYPE_ID3!="NOLOCTYPE"){
   	   	    	alert("Please enter Location Type Three different from Location Type Two");document.form1.LOC_TYPE_ID3.focus(); return false; 
   	   	    	}
   	   	    }
		
		if (COMNAME == "" || COMNAME == null) {
			alert("Please Enter Company name");
			document.form1.COMNAME.focus();
			return false;
		}

		if (strShippingArea == strShipping || strShippingArea == strShipping1
				|| strShippingArea == strShipping2
				|| strShippingArea == strShipping3) {
			alert("This location used by system cannot created");
			return false;
		}
		if (strTempTo == strTemp || strTempTo == strTemp1
				|| strTempTo == strTemp2 || strTempTo == strTemp3) {
			alert("This location cannot created used by system");
			return false;
		}
		/* if (strHoldingArea == strHolding || strHoldingArea == strHolding1
				|| strHoldingArea == strHolding2
				|| strHoldingArea == strHolding3) {
			alert("This location used by system cannot created");
			return false;
		} */

		var radio_choice = false;

		// Loop from zero to the one minus the number of radio button selections
		for (i = 0; i < document.form1.ACTIVE.length; i++) {
			if (document.form1.ACTIVE[i].checked)
				radio_choice = true;
		}

		if (!radio_choice) {
			// If there were no selections made display an alert box 
			alert("Please select Active or non Active mode.")
			return (false);
		}
		var chk = confirm("Are you sure you would like to save?");
		if (chk) {
			document.form1.action = "/track/locmstservlet?action=UPDATE&LOC_ID="
					+ LOC_ID;
			document.form1.submit();
		} else {
			return false;
		}
	}
	function onDelete() {
		var LOC_ID = document.form1.LOC_ID.value;
		if (LOC_ID == "" || LOC_ID == null) {
			alert("Please Enter Location Id");
			return false;
		}
		var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
			document.form1.action = "/track/locmstservlet?action=DELETE&LOC_ID="
					+ LOC_ID;
			document.form1.submit();
		} else {
			return false;
		}
	}

	function onView() {

		var LOC_ID = document.form1.LOC_ID.value;
		if (LOC_ID == "" || LOC_ID == null) {
			alert("Please Enter Location Id");
			return false;
		}

		document.form1.action = "/Wms/locmstservlet?action=VIEW";
		document.form1.submit();

	}

	function onClear() {
		document.form1.LOC_ID.value = "";
		document.form1.LOC_DESC.value = "";
		document.form1.LOC_TYPE_ID.value = "";
		document.form1.LOC_TYPE_ID2.value = "";
		document.form1.REMARKS.value = "";
		document.form1.COMNAME.value = "";
		document.form1.RCBNO.value = "";
		document.form1.ADDR1.value = "";
		document.form1.ADDR2.value = "";
		document.form1.ADDR3.value = "";
		document.form1.ADDR4.value = "";
		document.form1.STATE.value = "";
		document.form1.COUNTRY.value = "";
		document.form1.ZIP.value = "";
		document.form1.TELNO.value = "";
		document.form1.FAX.value = "";
		
	}

	//added by deen
	 function DisplayAddress()
	    {
	        if(document.getElementById("CHK_ADDRESS").checked == true){
	         document.getElementById("divaddress").style.display = "inline";
	        }
	        else{
	         document.getElementById("divaddress").style.display = "none";}
	    }

	    //end
	    
	 
 
 	  
  
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String loc_type_id = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	String res = "";
	String sAddr1  = "", scomname = "", srcbno = "",
    sAddr2  = "",
    sAddr3  = "", sAddr4  = "",
    		sState   = "",
    sCountry   = "", CHK_ADDRESS = "",
    sZip   = "",
    sTelNo = "", sFax = "" ;

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String loc_type_id2="";
	String loc_type_id3="";
	String action = "";
	String sLocId = "", sLocDesc = "", sRemarks = "", isActive = "",loctypeid="",desc="" ,loctypeid2="",loctypeid3="",desc2="" ;

	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	LocTypeUtil loctypeutil = new LocTypeUtil();
	ArrayList arrCust=new ArrayList();
	LocUtil _LocUtil = new LocUtil();
	_LocUtil.setmLogger(mLogger);
	String sLoctype = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	
	 Hashtable ht = new Hashtable();
	 ht.put("LOC_TYPE_ID",sLoctype);
	 ht.put("LOC_TYPE_ID2","");
	 ht.put("LOC_TYPE_ID3","");

	try {

		action = strUtils.fString(request.getParameter("action"));
		CHK_ADDRESS = StrUtils.fString(request.getParameter("CHK_ADDRESS"));

	} catch (Exception e) {

	}

	//start code by deen to get location type list on 26 dec 2013
	List loctypelist=loctypeutil.getLocTypeList("",plant," AND ISACTIVE ='Y'");
	List loctypelisttwo=loctypeutil.getLocTypeListtwo("",plant," AND ISACTIVE ='Y'");
	//end code by deen to get location type list on 26 dec 2013

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		action = "";
		sLocId = "";
		loctypeid = "";
		loctypeid2 = "";
		loctypeid3 = "";
		sLocDesc = "";
		sRemarks = "";
		scomname = "";
		srcbno = "";
		sAddr1  = "";
		sAddr2  = "";
		sAddr3  = "";
		sAddr4  = "";
		sState  = "";
		sCountry  = "";
		sZip   = "";
		sTelNo = "";
		sFax = "";

	}
	if (action.equalsIgnoreCase("View")){
		String sLoc = strUtils.fString(request.getParameter("LOC_ID"));
		
		arrCust = _LocUtil.getLocDetails(sLoc,plant," ",ht);
		
		  Map arrCustLine = (Map)arrCust.get(0);
		  sLocId  = strUtils.fString((String)arrCustLine.get("LOC"));
    	 sLocDesc = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOCDESC")));
    	 loctypeid = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID")));
    	 loctypeid2= strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID2")));
    	 loctypeid3= strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID3")));
    	 sRemarks   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("USERFLD1")));
    	 isActive  = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
        //below lines added by deen
         scomname =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COMNAME")));
         srcbno =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("RCBNO")));
         sAddr1    =  strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD1")));
         sAddr2    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD2")));
         sAddr3    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD3")));
         sAddr4    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ADD4")));
         sState  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("STATE")));
        sCountry  = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("COUNTRY")));
        sZip      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ZIP")));
        sTelNo    = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("TELNO")));
        sFax      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("FAX")));
    //   schkstatus = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("CHKSTATUS")));
         loc_type_id = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID")));
         loc_type_id2 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID2")));
         loc_type_id3 = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOC_TYPE_ID3")));

	}
	else if (action.equalsIgnoreCase("SHOW_RESULT")) {

		res = request.getParameter("result");
        } else if (action.equalsIgnoreCase("UPDATE")) {

		sCustEnb = "disabled";

	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../location/summary"><span class="underline-on-hover">Location Summary</span></a></li>                      
                <li><label>Edit Location</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../location/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Location ID">Location ID</label>
       <INPUT type="hidden" name="LOC_ID1" value="<%=sLocId%>">
  	   <div class="col-sm-4 ac-box ">
      	<div class="input-group">
 <INPUT class="ac-selected  form-control typeahead" name="LOC_ID" ID="LOC_ID" type ="TEXT" value="<%=sLocId%>"  size="30"  MAXLENGTH=20>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/Loc_list.jsp?LOC_ID='+form1.LOC_ID.value);">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->

  		</div>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Location Description">Location Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="LOC_DESC" type="TEXT" value="<%=sLocDesc%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
      
<!-- RESVI adds loc 1 -->
      
   <div class="form-group">

          <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type One</label>
          <div class="col-sm-4 ac-box">
          <div class="input-group">
          <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=loctypeid %>"size="30" MAXLENGTH=20>
          <input type="hidden" name="Location Type" value="">
          <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	      <!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);" ><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
			
	
  	</div>
  	</div>
  	</div>
<!--   	ends -->

<!--   	RESVI adds loc 2 -->

    <div class="form-group"> 
         <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type Two</label>
         <div class="col-sm-4 ac-box">
      	 <div class="input-group">
    	 <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=loctypeid2 %>" size="30" MAXLENGTH=20>
         <input type="hidden" name="Location Type" value="">
         <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
		<!-- <span class="btn-danger input-group-addon"onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->	
		

  	  </div>
      </div>
      </div>
<!--       ends -->
<!--   	Imthi add loc3 -->

    <div class="form-group"> 
         <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type Three</label>
         <div class="col-sm-4 ac-box">
      	 <div class="input-group">
    	 <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=loctypeid3 %>" size="30" MAXLENGTH=20>
         <input type="hidden" name="Location Type" value="">
         <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  	  </div>
      </div>
      </div>
<!--     imthi  end -->
  <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100>
			</div>
    </div>
 <div class="form-group">
      
      <div class="col-sm-offset-2 col-sm-8">    
      <label class="checkbox-inline">      
        <INPUT name="CHK_ADDRESS" type="checkbox" value="Y" id="CHK_ADDRESS"
			size="50" MAXLENGTH=100 onclick="DisplayAddress();">Location Address</label>
			</div>
    </div>
    
   <div id="divaddress" 
	<% if((CHK_ADDRESS.equalsIgnoreCase("N"))||(CHK_ADDRESS.equalsIgnoreCase(""))){ System.out.println(CHK_ADDRESS);%>
	style="display: none;" <%}%>>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Company Name">Company Name</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="COMNAME" type="TEXT" value="<%=scomname%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Business Registration">Business Registration No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="RCBNO" type="TEXT" value="<%=srcbno%>"
			size="50" MAXLENGTH=50>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR1" type="TEXT" value="<%=sAddr1%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR2" type="TEXT" value="<%=sAddr2%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR3" type="TEXT" value="<%=sAddr3%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ADDR4" type="TEXT" value="<%=sAddr4%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="STATE" type="TEXT" value="<%=sState%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Country">Country</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ZIP" type="TEXT" value="<%=sZip%>"
			size="50" MAXLENGTH=50>
      </div>
      </div>
     
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Tel No">Telephone</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="TELNO" type="TEXT" value="<%=sTelNo%>"
			size="50" MAXLENGTH=30>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Fax</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="FAX" type="TEXT" value="<%=sFax%>"
			size="50" MAXLENGTH=30>
      </div>
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="return onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onDelete();">Delete</button>&nbsp;&nbsp;

      </div>
    </div>
  </form>
</div>
</div>
</div>




  <script>

  
  

   $(document).ready(function(){
 	    $('[data-toggle="tooltip"]').tooltip(); 
// 	     RES ADDS  
 	    var plant= '<%=plant%>';

 	  
 	    /* location Auto Suggestion */
 		$('#LOC_TYPE_ID').typeahead({
 			  hint: true,
 			  minLength:0,  
 			  searchOnFocus: true
 			},
 			{
 			  display: 'LOC_TYPE_ID',  
 			  source: function (query, process,asyncProcess) {
 				var urlStr = "/track/ItemMstServlet";
 				$.ajax( {
 				type : "POST",
 				url : urlStr,
 				async : true,
 				data : {
 					PLANT : plant,
 					ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
 					QUERY : query
 				},
 				dataType : "json",
 				success : function(data) {
 					return asyncProcess(data.LOCTYPE_MST);
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
 				return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
 				}
 			  }
 			}).on('typeahead:render',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				var top = menuElement.height()+35;
 				top+="px";	
 				if(menuElement.next().hasClass("footer")){
 					menuElement.next().remove();  
 				}
 				menuElement.after( '<div class="locationtypeAddBtn footer"  data-toggle="modal" data-target="#locationTypeModal"><a href="#"> + New Location Type One</a></div>');
//                  menuElement.after( '<div class="locationtypeAddBtn footer"  data-toggle="modal" data-target="#locationType2Modal"><a href="#"> + New Location Type Two</a></div>');
 				menuElement.next().width(menuElement.width());
 				menuElement.next().css({ "top": top,"padding":"3px 20px" });
 				if($(this).parent().find(".tt-menu").css('display') != "block")
 					menuElement.next().hide();
 				  
 			}).on('typeahead:open',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				menuElement.next().show();
 				}).on('typeahead:close',function(){
 				var menuElement = $(this).parent().find(".tt-menu");
 				setTimeout(function(){ menuElement.next().hide();}, 150);
 				});  

 		  /* location three Auto Suggestion */
 		$('#LOC_TYPE_ID3').typeahead({
 			  hint: true,
 			  minLength:0,  
 			  searchOnFocus: true
 			},
 			{
 			  display: 'LOC_TYPE_ID3',  
 			  source: function (query, process,asyncProcess) {
 				var urlStr = "/track/ItemMstServlet";
 				$.ajax( {
 				type : "POST",
 				url : urlStr,
 				async : true,
 				data : {
 					PLANT : plant,
 					ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
 					QUERY : query
 				},
 				dataType : "json",
 				success : function(data) {
 					return asyncProcess(data.LOCTYPE_MST);
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
 				return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
 				}
 			  }
 			}).on('typeahead:render',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				var top = menuElement.height()+35;
 				top+="px";	
 				if(menuElement.next().hasClass("footer")){
 					menuElement.next().remove();  
 				}
 				menuElement.after( '<div class="locationtypeAddBtn footer"  data-toggle="modal" data-target="#locationType3Modal"><a href="#"> + New Location Type Three</a></div>');
 				menuElement.next().width(menuElement.width());
 				menuElement.next().css({ "top": top,"padding":"3px 20px" });
 				if($(this).parent().find(".tt-menu").css('display') != "block")
 					menuElement.next().hide();
 			  
 			  
 			}).on('typeahead:open',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				menuElement.next().show();
 				}).on('typeahead:close',function(){
 				var menuElement = $(this).parent().find(".tt-menu");
 				setTimeout(function(){ menuElement.next().hide();}, 150);
 				});
		  
 		/* location 2 Auto Suggestion */
 		$('#LOC_TYPE_ID2').typeahead({
 			  hint: true,
 			  minLength:0,  
 			  searchOnFocus: true
 			},
 			{
 			  display: 'LOC_TYPE_ID2',  
 			  source: function (query, process,asyncProcess) {
 				var urlStr = "/track/ItemMstServlet";
 				$.ajax( {
 				type : "POST",
 				url : urlStr,
 				async : true,
 				data : {
 					PLANT : plant,
 					ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
 					QUERY : query
 				},
 				dataType : "json",
 				success : function(data) {
 					return asyncProcess(data.LOCTYPE_MST);
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
 				return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
 				}
 			  }
 			}).on('typeahead:render',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				var top = menuElement.height()+35;
 				top+="px";	
 				if(menuElement.next().hasClass("footer")){
 					menuElement.next().remove();  
 				}
 				menuElement.after( '<div class="locationtypeAddBtn footer"  data-toggle="modal" data-target="#locationType2Modal"><a href="#"> + New Location Type Two</a></div>');
 				menuElement.next().width(menuElement.width());
 				menuElement.next().css({ "top": top,"padding":"3px 20px" });
 				if($(this).parent().find(".tt-menu").css('display') != "block")
 					menuElement.next().hide();
 			  
 			  
 			}).on('typeahead:open',function(event,selection){
 				var menuElement = $(this).parent().find(".tt-menu");
 				menuElement.next().show();
 				}).on('typeahead:close',function(){
 				var menuElement = $(this).parent().find(".tt-menu");
 				setTimeout(function(){ menuElement.next().hide();}, 150);
 				});

 		   //ends
 		 
 	});




</script>

<!-- MODAL -->

<%@include file="../jsp/newLocation1TypeModal.jsp"%>
<%@include file="../jsp/newLocation2TypeModal.jsp"%>
<%@include file="../jsp/newLocation3TypeModal.jsp"%>
 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>