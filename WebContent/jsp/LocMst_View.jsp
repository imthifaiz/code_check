<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.LocTypeDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Location";
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
	var strShipping="SHIPPINGAREA";
	var strShipping1="ShippingArea";
	var strShipping2="Shippingarea";
	var strShipping3="shippingarea";
	
	var strTemp="TEMP_TO";
	var strTemp1="Temp_To";
	var strTemp2="temp_to";
	var strTemp3="temp_to";

	/* var strHolding="HOLDINGAREA";
	var strHolding1="HoldingArea";
	var strHolding2="Holdingarea";
	var strHolding3="holdingarea"; */
	

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'LOCATIONS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onNew(){
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
   		/* document.form1.action  = "/track/locmstservlet?action=NEW";
   		document.form1.submit(); */
	}
	function onGenID()
	{

		$.ajax({
			type: "GET",
			url: "../location/Auto_ID",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#LOC_ID").val(data.LOCATION);
			},
			error: function(data) {
				alert('Unable to generate Location ID. Please try again later.');
			},
			complete: function(){
				hideLoader();
			}
		});
		/* document.form1.action  = "LocMst_View.jsp?action=Auto_ID";
 		document.form1.submit(); */
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

	
	
	function onAdd(){
   		var LOC_ID   = document.form1.LOC_ID.value;
   		var COMNAME = document.form1.COMNAME.value; //added by deen
   		var LOC_TYPE_ID1   = document.form1.LOC_TYPE_ID.value;
   		var LOC_TYPE_ID2   = document.form1.LOC_TYPE_ID2.value;
   		var LOC_TYPE_ID3   = document.form1.LOC_TYPE_ID3.value;
   	 	var ValidNumber   = document.form1.ValidNumber.value;
   	 	if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" location you can create"); return false; }

   	   	var strShippingArea=LOC_ID.substr(0,12);
   		var strTempTo=LOC_ID.substr(0,7);
   		//var strHoldingArea=LOC_ID.substr(0,11);
   		
   		if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Location ID");document.form1.LOC_ID.focus(); return false; }   		

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

   		
   		//added by deen
   		if(document.getElementById("CHK_ADDRESS").checked == true){
   		if(COMNAME == "" || COMNAME == null) {alert("Please Enter Company name");document.form1.COMNAME.focus(); return false; }}
   		
   		//end
   		 if (IsValidStringWithoutSpace(form1.LOC_ID.value)==false){
                  alert("Enter Location ID without White Space");
                        form1.LOC_ID.focus()
                        return false
                }
   		if(strShippingArea  == strShipping ||strShippingArea  == strShipping1 || strShippingArea  == strShipping2 || strShippingArea  == strShipping3) {alert("This location used by system cannot created"); return false; }
   		if(strTempTo  == strTemp ||strTempTo  == strTemp1 || strTempTo  == strTemp2 || strTempTo  == strTemp3) {alert("This location used by system cannot created"); return false; }
   		//if(strHoldingArea  == strHolding ||strHoldingArea  == strHolding1 || strHoldingArea == strHolding2 || strHoldingArea  == strHolding3) {alert("This location used by system cannot created"); return false; }
   		
   		document.form1.action  = "/track/locmstservlet?action=ADD";
   		document.form1.submit();
	}
	
	
	function onView(){
   		var LOC_ID   = document.form1.LOC_ID.value;
   		if(LOC_ID == "" || LOC_ID == null) 
   		{
      		alert("Please Enter Location ID"); 
      		return false; 
   		}

     document.form1.action  = "/track/locmstservlet?action=VIEW";
     document.form1.submit();
   
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
	
	String action = "";
	String sLocId = "", sLocDesc = "", sRemarks = "";
	String sAddr1  = "", scomname = "", PLANTDESC = "", RCBNO = "", srcbno = "",
	sAddr2  = "",sAddr3  = "", sAddr4  = "",sState   = "",sCountry   = "",    sZip   = "",
	sTelNo = "", sFax = "",loctypeid="",desc="" ,loctypeid2="",loctypeid3="",desc2="" ;
	String res = "";
	
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";

	//ob initialise
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	PlantMstUtil pmUtil = new PlantMstUtil();//added by deen
	LocTypeUtil loctypeutil = new LocTypeUtil();
	TblControlDAO _TblControlDAO = new TblControlDAO();
	LocTypeDAO LocDao = new LocTypeDAO();
	
	
	//session
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String NOOFLOCATION=strUtils.fString((String) session.getAttribute("NOOFLOCATION"));
	String ValidNumber="";
	int novalid =LocDao.Loc_count(plant);
	if(!NOOFLOCATION.equalsIgnoreCase("Unlimited")) //imti
	{
		int convl = Integer.valueOf(NOOFLOCATION);
		if(novalid>=convl)
		{
			ValidNumber=NOOFLOCATION;
		}
	}
	
	try {
		action = strUtils.fString(request.getParameter("action"));
		//added by deen
		ArrayList listQry = pmUtil.getPlantMstDetails(plant);
		for (int i = 0; i < listQry.size(); i++) {
            Map map = (Map) listQry.get(i);
            PLANTDESC = StrUtils.fString((String) map.get("PLNTDESC"));
            RCBNO = StrUtils.fString((String) map.get("RCBNO"));
           
            scomname = PLANTDESC;
            srcbno = RCBNO; 
		}
		
		
		
                
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
		sLocDesc = "";
		sRemarks = "";
		scomname = "";
		srcbno = "";
		sAddr1  = "";
		sAddr2  = "";
		sAddr3  = "";
		sAddr4  = "";
		sState  = ""	;
		sCountry  = ""	;
		sZip   = "";
		sTelNo = "";
		sFax = "";
		
		
	}
	
	/* else if (action.equalsIgnoreCase("Auto_ID")) {
		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		

		_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();
		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "LOCATION");
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
						"LOCATION");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "L");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "0000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
				sLocId = "L" + "0001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				int inxtSeq = Integer.parseInt(((String) sBatchSeq
						.trim().toString())) + 1;
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
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						"LOCATION");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "L");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'"); */
// 				/* boolean updateFlag = _TblControlDAO.update(updateQyery
// 						.toString(), htTblCntUpdate, "", plant); */
				/* sLocId = "L" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - LocMst_View.jsp ", e);
		}
	} */
	
	
	else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
	}
	
	else if (action.equalsIgnoreCase("SHOW_RESULT_VALUE")) {
		res = (String)request.getSession().getAttribute("errResult");
           	
	} 
	
	else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
               <li><a href="../location/summary"><span class="underline-on-hover">Location Summary</span></a></li>                      
                <li><label>Create Location</label></li>                                   
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
    <div class="form-group" style="text-align:left">
      <label class="control-label col-form-label col-sm-2 required" for="Location ID">Location ID</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="LOC_ID" id="LOC_ID" type="TEXT" value="<%=sLocId%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50<%=sCustEnb%> class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" sNewEnb >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		 <INPUT type="hidden" name="plant" value=<%=plant%>> 
  		<INPUT type="hidden" name="LOC_ID1" value="<%=sLocId%>">
  		<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>"> <!-- imti -->
  		
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Location Description">Location Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="LOC_DESC"  id="LOC_DESC" type="TEXT" value="<%=sLocDesc%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>

<!-- RESVI adds loc 1 -->

    <div class="form-group">
     
         <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type One</label>
          <div class="col-sm-4 ac-box">
          <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(loctypeid)%>" size="30" MAXLENGTH=20>
       
          <span class="select-icon"   onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	      <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
	
  	</div>
  	</div>
  	
<!--   	ENDS -->

<!-- RESVI adds loc 2 -->

      <div class="form-group">
       
          <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type Two</label>
          <div class="col-sm-4 ac-box">
    	  <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=StrUtils.forHTMLTag(loctypeid2)%>" size="30" MAXLENGTH=20>
         
          <span class="select-icon"   onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	 	  <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
		

  	   </div>
       </div>
<!--        ENDS -->

<!-- Imthi add forLoc3 -->

      <div class="form-group">
       
          <label class="control-label col-form-label col-sm-2" for="Location Type">Location Type Three</label>
          <div class="col-sm-4 ac-box">
    	  <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=StrUtils.forHTMLTag(loctypeid3)%>" size="30" MAXLENGTH=20>
         
          <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--            <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeThreeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
		

       </div>
       </div>
<!--   Imthi end      -->

      
 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" id="REMARKS" type="TEXT" value="<%=sRemarks%>"
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
    
    <div id="divaddress" style="display: none;" >
    
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
      <label class="control-label col-form-label col-sm-2" for="Building">Building:</label>
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
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>>Save</button>&nbsp;&nbsp;
    
      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>



$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
//     RES ADDS
    var plant= '<%=plant%>';
    /* location 1 Auto Suggestion */
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
// 			menuElement.after( '<div class="locationtypeAddBtn footer"  data-toggle="modal" data-target="#locationType2Modal"><a href="#"> + New Location Type Two</a></div>');
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

 		
});


	    




</script>

 <!-- MODAL -->

<%@include file="../jsp/newLocation1TypeModal.jsp"%>
<%@include file="../jsp/newLocation2TypeModal.jsp"%>
<%@include file="../jsp/newLocation3TypeModal.jsp"%>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>