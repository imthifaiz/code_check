<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Order Summary By Price";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/typeahead.jquery.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>


<script>
var subWin = null;

function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
 
   document.form1.action = "/track/ReportServlet?action=ExportIssuedHistory";
   document.form1.submit();
  
}
function onGo(){

   var flag    = "false",loc, loctype, loctype2,loctype3,SORT,POSSEARCH;

   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   loc = document.form1.LOC.value;
   loctype = document.form1.LOC_TYPE_ID.value;
   loctype2 = document.form1.LOC_TYPE_ID2.value;
   loctype3 = document.form1.LOC_TYPE_ID3.value;
   SORT      = document.form1.SORT.value;
   POSSEARCH = document.form1.POSSEARCH.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
  
   if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   
   if(loc != null    && loc != "") { flag = true;}
   
   if(loctype != null    && loctype != "") { flag = true;}
   if(loctype2 != null     && loctype2 != "") { flag = true;}
   if(loctype3 != null     && loctype3 != "") { flag = true;}
  
   //storeUserPreferences();

   if(SORT=="CUSTOMER"){
	   document.form1.LOC.value="";
	   document.form1.LOC_TYPE_ID.value="";
	   document.form1.LOC_TYPE_ID2.value="";
	   document.form1.LOC_TYPE_ID3.value="";
   }
  	// Call the method of JQuery Ajax provided
  	//var groupColumn = document.forms[0].SORT[0].checked ? 5 : 10;
   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}
storeUserPreferences();
 document.form1.action="../salesorder/orderhistory";
  document.form1.submit();
}
</script>

<%
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	ShipHisDAO _ShipHisDAO=new ShipHisDAO();
	ArrayList movQryList = new ArrayList();
	ArrayList prodGstList = new ArrayList();
	Hashtable ht = new Hashtable();	
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
        String userID = (String) session.getAttribute("LOGIN_USER");
        String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
        String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
	String FROM_DATE = "", TO_DATE = "", USER = "",DIRTYPE ="", 
			fdate = "", tdate = "", ORDERNO = "", cntRec = "false",CUSTOMER = "",PGaction = "",statusID="",
			sCustomerTypeId="",taxby="",LOC ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",locshow="",SORT="",POSSEARCH="1";
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportissuedobsummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportissuedobsummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	float subtotal=0;
	double gst=0,total=0;
	float gsttotal=0;
	float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
	int k=0;
	
	DecimalFormat decformat = new DecimalFormat("#,##0.00");

	FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String  fieldDesc="";

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
// 	String curDate = _dateUtils.getDate();
    String curDate = DateUtils.getDateMinusDays();//resvi
    if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
    	curDate =DateUtils.getDate();
	if (FROM_DATE.length() < 0 || FROM_DATE == null
			|| FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;//resvi
	if (FROM_DATE.length() > 5)
		
		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
		
	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		
		 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
		
	DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
	PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
	USER = StrUtils.fString(request.getParameter("USER"));
	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
	statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	sCustomerTypeId  = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	LOC     = StrUtils.fString(request.getParameter("LOC"));
	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	SORT = StrUtils.fString(request.getParameter("SORT"));
	String ENABLE_POS = _PlantMstDAO.getispos(plant);
	POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
	if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
		if(ENABLE_POS.equals("1"))
			POSSEARCH="3";
		else
			POSSEARCH="1";
	}
	if(DIRTYPE.length()<=0){
		DIRTYPE = "ISSUEDHISTORY";
		}
	
	if (PGaction.equalsIgnoreCase("View")) {
		
		try {
				//Hashtable ht = new Hashtable();	
			
					if (StrUtils.fString(ORDERNO).length() > 0)    ht.put("a.DONO", ORDERNO);
					if (StrUtils.fString(statusID).length() > 0)	ht.put("b.STATUS_ID", statusID);
					if (StrUtils.fString(sCustomerTypeId).length() > 0)	ht.put("CUSTTYPE", sCustomerTypeId);
					if (StrUtils.fString(LOC).length() > 0)	ht.put("LOC", LOC);
					if (StrUtils.fString(LOC_TYPE_ID).length() > 0)	ht.put("LOC_TYPE_ID", LOC_TYPE_ID);
					if (StrUtils.fString(LOC_TYPE_ID2).length() > 0)	ht.put("LOC_TYPE_ID2", LOC_TYPE_ID2);
					if (StrUtils.fString(LOC_TYPE_ID3).length() > 0)	ht.put("LOC_TYPE_ID3", LOC_TYPE_ID3);
					if (StrUtils.fString(SORT).length() > 0)	ht.put("SORT", SORT);
			       	//movQryList = movHisUtil.getIssuedOutboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER);
				//if (movQryList.size() <= 0)
					//cntRec = "true";
			    
			} catch (Exception e) {
				  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			}
	}	
	String collectionDate=DateUtils.getDate();
	PlantMstUtil plantmstutil = new PlantMstUtil();
	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
	    Map map = (Map) viewlistQry.get(0);
	String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
	String CNAME = (String) map.get("PLNTDESC");
	String ADD1 = (String) map.get("ADD1");
	String ADD2 = (String) map.get("ADD2");
	String ADD3 = (String) map.get("ADD3");
	String ADD4 = (String) map.get("ADD4");
	String STATE = (String) map.get("STATE");
	String COUNTRY = (String) map.get("COUNTY");
	String ZIP = (String) map.get("ZIP");
	String TELNO = (String) map.get("TELNO");

	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
	
		
%>
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><label>Sales Order Summary By Price</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                                           <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data </button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul> -->
					  <%}%>

					
				</div>

				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="/track/ReportServlet?">
<input type="hidden" name="PGaction" value="View"> 
<INPUT type="Hidden" name="DIRTYPE" value="ISSUEDHISTORY">
<input type="hidden" name="CUSTOMERID" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="plant" value="<%=plant%>">
<input type="hidden" name="LOC_DESC" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<INPUT name="ACTIVE" type = "hidden" value="">
<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>">

<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER NAME" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
             <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changecustomer(this)">
			    <i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			    </button>
			    <!-- 				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"  placeholder="CUSTOMER GROUP" value="<%=StrUtils.forHTMLTag(sCustomerTypeId)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO"  placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
 		<div class="col-sm-4 ac-box">
        <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC" type="text" ID="LOC" value="<%=StrUtils.forHTMLTag(LOC)%>"  placeholder="LOCATION" size="35" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--    		 	<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  -->

<!--    	    <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!--    	    <a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		</div>  		
       <div class="col-sm-4 ac-box">
        <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT"  placeholder="LOCATION TYPE ONE" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" size="35" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc1(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--    		 	<span class="select-search btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  -->

    	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    	</div>
 		</div> 		
  		</div> 
		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
    	<div class="col-sm-4 ac-box">
        <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT"  placeholder="LOCATION TYPE TWO" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>" size="35" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc2(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--    		 	<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  -->

    	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    	</div>
    	</div>
    	<div class="col-sm-4 ac-box">
    	  <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" placeholder="LOCATION TYPE THREE" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" size="35" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc3(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--         <span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeThreeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
    	</div>
    	</div>
 		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
			<label class="radio-inline"><INPUT name="SORT" type = "radio"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>By Customer</label>
      		<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="LOCATION"    <%if(SORT.equalsIgnoreCase("LOCATION")) {%>checked <%}%>>By Location</label>			
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
			
			
  	</div>
  		</div>
		
  		</div>
 		
	
	  <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button> -->
  	  </div>
         </div>
       	   </div>
       	   
       	   <div class="form-group">
       	  <% if(ENABLE_POS.equals("1")){ %>
					<label class="control-label col-sm-1" for="view">View By :</label>
				  	<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>Both Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="2" id="done" <%if(POSSEARCH.equalsIgnoreCase("2")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>ERP Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="3" id="notdone" <%if(POSSEARCH.equalsIgnoreCase("3")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>POS Sales</b></label>
  		 <% } else {%>
  					<input name="POSSTATUS" type="radio" hidden value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)">
  		 <% } %>
		</div>
	
<div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
        
          <th style="font-size: smaller;">ORDER NO</TH>
          <th style="font-size: smaller;">CUSTOMER NAME</TH>
          <%if((!"".equals(LOC) || !"".equals(LOC_TYPE_ID) || !"".equals(LOC_TYPE_ID2) || !"".equals(LOC_TYPE_ID3)) || (SORT.equalsIgnoreCase("LOCATION"))) {%>
           		<th id="locshow" style="font-size: smaller;">Loc</TH>
            <%} %>
          <th style="font-size: smaller;">ISSUED DATE</TH>
          <th style="font-size: smaller;">SUBTOTAL</TH>
          <th style="font-size: smaller;">TAX</TH>
          <th style="font-size: smaller;">TOTAL</TH>
          <th style="font-size: smaller;">ISSUED BY</TH>
        
                    </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <%if((!"".equals(LOC) || !"".equals(LOC_TYPE_ID) || !"".equals(LOC_TYPE_ID2) || !"".equals(LOC_TYPE_ID3)) || (SORT.equalsIgnoreCase("LOCATION"))) {%>
		            <th colspan='3'></th>
		             <%}else{ %>
		            <th colspan='2'></th>
		             <%} %>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script>
  

  
  
  
  var tableData = [];
	
	<%
	movQryList = movHisUtil.getIssuedOutboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER,POSSEARCH);
	if (movQryList.size() <= 0)
		cntRec = "true";
		if (movQryList.size() <= 0 && cntRec == "true") {
	%>
	<TR>
		<TD colspan=15 align=center>No Data For This criteria</TD>
	</TR>
	<%
		}
	%>

	<%taxby=_PlantMstDAO.getTaxBy(plant);
	   // String strprodgst="";
	   DecimalFormat decimalFormat = new DecimalFormat("#.#####");
				decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
			
			double subtotal1 =  Double.parseDouble(((String) lineArr.get("subtotal").toString())) ;
			
			if(taxby.equalsIgnoreCase("BYORDER"))
			{
				gstpercentage =  Float.parseFloat(((String) lineArr.get("outbound_gst").toString())) ;
				//gst = (subtotal1*gstpercentage)/100;
				gst =  Double.parseDouble(((String) lineArr.get("taxval").toString())) ;
			}
			else
			{
				prodGstList = _ShipHisDAO.getShippingProductGst(plant,(String) lineArr.get("dono").toString());
				prodgstsubtotal1=0;
				for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
					Map prodGstArr = (Map)prodGstList.get(jCnt);
					int jIndex = jCnt + 1;
					prodgstsubtotal1=prodgstsubtotal1+Float.parseFloat(((String) prodGstArr.get("subtotal").toString()));
					
				}
				 gst=prodgstsubtotal1;
			}
			
			
			total = subtotal1+gst;
			String dono = (String) lineArr.get("dono");
			String GSTValue = decimalFormat.format(gst);
			
			/* String GSTValue = String.valueOf(gst); */
			String TOTALValue =  decimalFormat.format(total); 
			/* String TOTALValue =  String.valueOf(total);  */
			String SUBTOTAL = (String)lineArr.get("subtotal");
			String issuedate = (String)lineArr.get("issuedate");
			
			float gstVal="".equals(GSTValue) ? 0.0f :  Float.parseFloat(GSTValue);
			float totalVal="".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
			float subTotalVal="".equals(SUBTOTAL) ? 0.0f :  Float.parseFloat(SUBTOTAL);
			
				if (gstVal == 0f) {
					GSTValue = "0.00000";
				} else {
					GSTValue = GSTValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				}
// 				if (totalVal == 0f) {
// 					TOTALValue = "0.00000";
// 				} else {
// 					TOTALValue = TOTALValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
// 				}if (subTotalVal == 0f) {
// 					SUBTOTAL = "0.00000";
// 				} else {
// 					SUBTOTAL = SUBTOTAL.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
// 				}
				double tot = Double.parseDouble(TOTALValue);
				TOTALValue = StrUtils.addZeroes(tot, numberOfDecimal);
				
				double subtot = Double.parseDouble(SUBTOTAL);
				SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
				%>
	
	 var rowData = [];
	 <%--  rowData[rowData.length] = '<%=(String)lineArr.get("dono")%>';  --%> 
     rowData[rowData.length] = '<a href="../salesorder/salespdfdetails?DONO=<%=(String)lineArr.get("dono")%>&INVOICE=Y&TYPE=&FROMDATE=<%=FROM_DATE%>&TODATE=<%=TO_DATE%>"> <%=(String)lineArr.get("dono")%> </a>';
     
     rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
     <%if(SORT.equalsIgnoreCase("LOCATION") || !"".equals(LOC) || !"".equals(LOC_TYPE_ID) || !"".equals(LOC_TYPE_ID2) || !"".equals(LOC_TYPE_ID3) || SORT.equalsIgnoreCase("LOCATION")) {%>
		rowData[rowData.length] ='<%=(String)lineArr.get("loc")==null?"":lineArr.get("loc")%>';
 <%} %>
     rowData[rowData.length] = '<%=issuedate%>';
     rowData[rowData.length] = '<%=SUBTOTAL%>';
     rowData[rowData.length] = '<%=GSTValue%>';
     rowData[rowData.length] = '<%=TOTALValue%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("issuedby")%>';
     <%-- rowData[rowData.length] = '<%=(String)lineArr.get("status_id") %>'; --%>
     
              tableData[tableData.length] = rowData;
     <%}%>  
     $(document).ready(function(){
    	  /* if (document.form1.FROM_DATE.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_FROMDATE','','FROM_DATE');
        }
    	 if (document.form1.TO_DATE.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_TODATE', '','TO_DATE');
        } */
        var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
        if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    	 if (document.form1.CUSTOMER.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_CUSTOMER', '','CUSTOMER');
        }
    	 if (document.form1.CUSTOMER_TYPE_ID.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_CUSTOMER_TYPE_ID', '','CUSTOMER_TYPE_ID');
        }
    	 if (document.form1.ORDERNO.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_ORDERNO','','ORDERNO');
        }
    	 if (document.form1.LOC.value == ''){
    			getLocalStorageValue('IssuedOrdersHistory_LOC', '','LOC');
        }
    	 if (document.form1.LOC_TYPE_ID.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_LOC_TYPE_ID', '','LOC_TYPE_ID');
     	}
    	 if (document.form1.LOC_TYPE_ID2.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_LOC_TYPE_ID2','','LOC_TYPE_ID2');
     	}
    	 if (document.form1.LOC_TYPE_ID3.value == ''){
    		 getLocalStorageValue('IssuedOrdersHistory_LOC_TYPE_ID3','', 'LOC_TYPE_ID3');
    	}
 			
    	 
    	 storeUserPreferences(); 
        }
    	 var groupColumn =1,islocationProvided=false;
    	 
    	 if ('<%=request.getParameter("SORT")%>' == 'CUSTOMER'){
	 			groupColumn=1;
	 			islocationProvided=false;
	 		}else if ('<%=request.getParameter("SORT")%>' == 'LOCATION'){
	 			groupColumn=2;
	 			islocationProvided=true;
	 	   }
	 	 var subtotalColumnIndex = 3;
	 	<%-- <%if((request.getParameter("SORT").equalsIgnoreCase("LOCATION")) || (!"".equals(LOC) || !"".equals(LOC_TYPE_ID) || !"".equals(LOC_TYPE_ID2))){%> --%>
	 	<%if(SORT.equalsIgnoreCase("LOCATION") || !"".equals(LOC) || !"".equals(LOC_TYPE_ID) || !"".equals(LOC_TYPE_ID2) || !"".equals(LOC_TYPE_ID3)){%>
	 		subtotalColumnIndex = 4;
	 	<%}%>
    	 for(var rowIndex = 0; rowIndex < tableData.length; rowIndex ++){
     		 tableData[rowIndex][3] = parseFloat(tableData[rowIndex][subtotalColumnIndex]).toFixed(<%=numberOfDecimal%>);
     		tableData[rowIndex][4] = parseFloat(tableData[rowIndex][subtotalColumnIndex + 1]).toFixed(<%=numberOfDecimal%>);
     		tableData[rowIndex][5] = parseFloat(tableData[rowIndex][subtotalColumnIndex + 2]).toFixed(<%=numberOfDecimal%>);
     		 
     	 }
    	 $('#tableIssueSummary').DataTable({
    		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    		  	data: tableData,
    		  	"orderFixed": [ groupColumn, 'asc' ], 
    		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
    			"<'row'<'col-md-6'><'col-md-6'>>" +
    			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
    	        buttons: [
    	        	{
    	                extend: 'collection',
    	                text: 'Export',
    	                buttons: [
    	                    {
    	                    	extend : 'excel',
    	                    	exportOptions: {
    	    	                	columns: [':visible']
    	    	                },
    	    	                title: '<%=title%>',
    	    	                footer: true
    	                    },
    	                    {
    	                    	extend : 'pdf',
                                footer: true,
    	                    	text: 'PDF Portrait',
    	                    	exportOptions: {
    	                    		columns: [':visible']
    	                    	},
    	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
    	                    	<%} else {%>
    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
    	                    	<%}%>
                        		orientation: 'portrait',
    	                    	customize: function(doc) {
    	                    		doc.defaultStyle.fontSize = 7;
                         	        doc.styles.tableHeader.fontSize = 7;
                         	        doc.styles.title.fontSize = 10;
                         	        doc.styles.tableFooter.fontSize = 7;
    	                    	},
                                pageSize: 'A4'
    	                    },
    	                    {
    	                    	extend : 'pdf',
    	                    	footer: true,
    	                    	text: 'PDF Landscape',
    	                    	exportOptions: {
    	                    		columns: [':visible']
    	                    	},
    	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
    	                    	<%} else {%>
    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
    	                    	<%}%>
                        		orientation: 'landscape',
                        		customize: function(doc) {
    	                    		doc.defaultStyle.fontSize = 6;
                         	        doc.styles.tableHeader.fontSize = 6;
                         	        doc.styles.title.fontSize = 8;                     	       
                         	        doc.content[1].table.widths = "*";
                         	       doc.styles.tableFooter.fontSize = 7;
    	                    	     },
                                pageSize: 'A4'
    	                    }
    	                ]
    	            },
    	            {
                       extend: 'colvis',
                    	   columns: ':not(:eq(2)):not(:eq(3)):not(:eq(4)):not(:eq(5))'
                   }
    	        ], "drawCallback": function ( settings ) {
    				var groupRowColSpan = groupColumn + 1;
    			   	var api = this.api();
    	            var rows = api.rows( {page:'current'} ).nodes();
    	            var last=null;
    	            var totalSub = 0;
    	            var groupTotalSub = 0;
    	            var totalTax = 0;
    	            var groupTotalTax = 0;
    	            var totalPrice = 0;
    	            var groupTotalPrice = 0;
    	            var groupEnd = 0;
    	            var currentRow = 0;
    	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
    	                if ( last !== group ) {
    	                	var groupTotalSubVal=null,groupTotalTaxVal=null,groupTotalPriceVal=null;
    	                	
    	                	if(groupTotalSub==null || groupTotalSub==0){
    	                		groupTotalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	                	}else{
    	                		groupTotalSubVal=parseFloat(groupTotalSub).toFixed(<%=numberOfDecimal%>);
    	                	}if(groupTotalTax==null || groupTotalTax==0){
    	                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	                	}else{
    	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
    	                	}if(groupTotalPrice==null || groupTotalPrice==0){
    	                		groupTotalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	                	}else{
    	                		groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>);
    	                	}
    	                	
    	                	if (i > 0) {
    	                		$(rows).eq( i ).before(
    			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + groupTotalSubVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupTotalPriceVal + '</td><td></td><td></td></tr>'
    			                    );
    	                	}
    	                    last = group;
    	                    groupEnd = i;    
    	                    groupTotalSub = 0;
    	                    groupTotalTax = 0;
    	                    groupTotalPrice = 0;
    	                    	                    
    	                }
    	                groupTotalSub += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
    	                totalSub += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
    	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
    	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
    	                groupTotalPrice += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
    	                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
    	                currentRow = i;
    	            } );
    	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
    	            	
						var totalSubVal=null,groupTotalSubVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,groupTotalPriceVal=null;
    	            	
    	            	if(totalSub==null || totalSub==0){
    	            		totalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		totalSubVal=parseFloat(totalSub).toFixed(<%=numberOfDecimal%>);
    	            	}if(groupTotalSub==null || groupTotalSub==0){
    	            		groupTotalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		groupTotalSubVal=parseFloat(groupTotalSub).toFixed(<%=numberOfDecimal%>);
    	            	}if(totalTax==null || totalTax==0){
    	            		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
    	            	}if(groupTotalTax==null || groupTotalTax==0){
    	            		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
    	            	}if(totalPrice==null || totalPrice==0){
    	            		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
    	            	}if(groupTotalPrice==null || groupTotalPrice==0){
    	            		groupTotalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
    	            	}else{
    	            		groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>);
    	            	}
    	            	
    	            	
    	            	$(rows).eq( currentRow ).after(
    	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + totalSubVal + '</td><td>' + totalTaxVal + '</td><td>' + totalPriceVal + '</td><td></td><td></td></tr>'
                       );
                   	$(rows).eq( currentRow ).after(
    	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalSubVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupTotalPriceVal + '</td><td></td><td></td></tr>'
                       );
                   }
    	        },
    	        "footerCallback": function(row, data, start, end, display) {
    	            var api = this.api(),
    	              data;

    	            // Remove the formatting to get integer data for summation
    	            var intVal = function(i) {
    	              return typeof i === 'string' ?
    	                i.replace(/[\$,]/g, '') * 1 :
    	                typeof i === 'number' ?
    	                i : 0;
    	            };

    	            // Total over all pages
    	            total = api
    	              .column(3)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);

    	            // Total over this page
    	            totalCostVal = api
    	              .column(3)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
    	              
    	            totalTaxVal = api
    	              .column(4)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
    	              
    	            totalVal = api
    	              .column(5)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);

    	            // Update footer
    	            $(api.column(3).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(4).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(5).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
    	          } 
    		  });	 
    });

    
    
     
    </script>
	

</FORM>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 function storeUserPreferences(){
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	  // storeInLocalStorage('IssuedOrdersHistory_FROMDATE', $('#FROM_DATE').val());
		//storeInLocalStorage('IssuedOrdersHistory_TODATE', $('#TO_DATE').val());
		storeInLocalStorage('IssuedOrdersHistory_CUSTOMER', $('#CUSTOMER').val());
		storeInLocalStorage('IssuedOrdersHistory_CUSTOMER_TYPE_ID', $('#CUSTOMER_TYPE_ID').val());
		storeInLocalStorage('IssuedOrdersHistory_ORDERNO', $('#ORDERNO').val());
		storeInLocalStorage('IssuedOrdersHistory_LOC', $('#LOC').val());
		storeInLocalStorage('IssuedOrdersHistory_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
		storeInLocalStorage('IssuedOrdersHistory_LOC_TYPE_ID2',$('#LOC_TYPE_ID2').val());
		storeInLocalStorage('IssuedOrdersHistory_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	    }
	    }


 function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}
 
 function changecustomertypeid(obj){
	 $("#CUSTOMER_TYPE_ID").typeahead('val', '"');
	 $("#CUSTOMER_TYPE_ID").typeahead('val', '');
	 $("#CUSTOMER_TYPE_ID").focus();
	}

 function changeorderno(obj){
	 $("#ORDERNO").typeahead('val', '"');
	 $("#ORDERNO").typeahead('val', '');
	 $("#ORDERNO").focus();
	}

function changeloc(obj){
	 $("#LOC").typeahead('val', '"');
	 $("#LOC").typeahead('val', '');
	 $("#LOC").focus();
	}

 function changeloc1(obj){
	 $("#LOC_TYPE_ID").typeahead('val', '"');
	 $("#LOC_TYPE_ID").typeahead('val', '');
	 $("#LOC_TYPE_ID").focus();
	}

 function changeloc2(obj){
	 $("#LOC_TYPE_ID2").typeahead('val', '"');
	 $("#LOC_TYPE_ID2").typeahead('val', '');
	 $("#LOC_TYPE_ID2").focus();
	}

 function changeloc3(obj){
	 $("#LOC_TYPE_ID3").typeahead('val', '"');
	 $("#LOC_TYPE_ID3").typeahead('val', '');
	 $("#LOC_TYPE_ID3").focus();
	}
 $(document).ready(function(){
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
    var plant= '<%=plant%>';
    
	/* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTMST);
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
		    return '<p onclick="getvendname(\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});

	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				DONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
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
		    return '<p>' + data.DONO + '</p>';
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
		
		});


	
	
	/* Customer Type Auto Suggestion */
	$('#CUSTOMER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  //async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListTypeData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUST_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	/* location Auto Suggestion */
	$('#LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
				
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
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
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* location Auto Suggestion */
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
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
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
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
 });
 function changepostype(count){
	  $("input[name ='POSSEARCH']").val(count);
	  onGo();
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>