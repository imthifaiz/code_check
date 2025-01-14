<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp" %>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Purchase Order Summary With Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
<jsp:param name="submenu" value="<%=IConstants.PURCHASE_ORDER%>"/>
</jsp:include>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 22 2014, Description:To open Purchase order summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.CUSTOMER.value;
  document.form1.LOCALEXPENSES.value;
  document.form1.action="/track/purchaseorderservlet?Submit=ExportExcelInboundOrderSummary";
  document.form1.submit();
 }
//---End Modified by Deen on May 22 2014, Description:To open Purchase order summary  in excel powershell format
function ExportReportWithProductRemarks()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.CUSTOMER.value;
  document.form1.action="/track/purchaseorderservlet?Submit=ExportExcelInboundOrderWithRemarksSummary";
  document.form1.submit();
 }
 
var postatus =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },{
    "year": "OPEN",
    "value": "OPEN",
    "tokens": [
      "OPEN"
    ]
  },
  {
    "year": "PARTIALLY RECEIVED",
    "value": "PARTIALLY RECEIVED",
    "tokens": [
      "PARTIALLY RECEIVED"
    ]
  },
  {
    "year": "RECEIVED",
    "value": "RECEIVED",
    "tokens": [
      "RECEIVED"
    ]
  }];
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="https://cdn.jsdelivr.net/npm/autosize@4.0.0/dist/autosize.min.js"></script>
<%
StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movSupplierList  = new ArrayList();


session= request.getSession();
String plant = (String)session.getAttribute("PLANT");
String USERID ="";
String FROM_DATE ="",  TO_DATE = "",status="", DIRTYPE ="",ORDERTYPE="",BATCH ="",USER="",ITEM="",DESC="",fdate="",
tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="",VIEWSTATUS="",SUPPLIERTYPE="",LOCALEXPENSES="",newsts="";
String PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="",statusID="",DELDATE="",CURRENCYDISPLAY="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));


String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
else
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);

// String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
USER          = StrUtils.fString(request.getParameter("USER"));
ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
DESC          = StrUtils.fString(request.getParameter("DESC"));
ORDERNO       = StrUtils.fString(request.getParameter("ORDERNO"));
CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
status 		= StrUtils.fString(request.getParameter("STATUS"));
//imti start
newsts = status;
//if end
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
SUPPLIERTYPE = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
LOCALEXPENSES = StrUtils.fString(request.getParameter("LOCALEXPENSES"));
DELDATE = StrUtils.fString(request.getParameter("DELDATE"));
if(VIEWSTATUS.equals(""))
{
	VIEWSTATUS="ByOrderDate";
}
if(LOCALEXPENSES.equals(""))
{
	LOCALEXPENSES="1";
}
float Total=0;
int j=0;
String rowColor="";			
if(DIRTYPE.length()<=0){
DIRTYPE = "IB_SUMMARY_ORD_WITH_COST";
}
String LOCALEXPENSESCHK = "checked";
if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
	//LOCALEXPENSESCHK="";
	//LOCALEXPENSES="0";	
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);

String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase(""))CURRENCYDISPLAY=basecurrency;

//RESVI STARTS
boolean displaySummaryLink=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkibsummarycost", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkibsummarycost", plant,USERID);
}
//RESVI ENDS

String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
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

String currency=_PlantMstDAO.getBaseCurrency(plant);
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
              <!-- <div class="col-sm-3"> -->
      				<ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                        <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                        <li><label> Purchase Order Summary with Cost</label></li>                                   
                     </ol>   
              <!-- </div>  -->     	                  
   <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul> -->
<!--               <button type="button" class="btn btn-default"
						onClick="javascript:ExportReportWithProductRemarks();">
						Export With Product Remarks</button>
					&nbsp; -->
				</div>
<!-- 				   <div class="btn-group" role="group">
				   
  	
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div> -->
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="container-fluid">
<FORM class="form-horizontal" name="form1" method="post" action="printSupplierPOInvoice.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="currencyuseqt" value="">

<div id="CHK1" ></div>
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="SUPPLIER" name="CUSTOMER" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon" onclick="javascript:popUpWin('supplierlist.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="SUPPLIER_TYPE_ID" name="SUPPLIER_TYPE_ID" placeholder="SUPPLIER GROUP">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="ORDER NO" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" placeholder="REFERENCE NO">				
  		</div>
		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" placeholder="PRODUCT" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" placeholder="PRODUCT DEPARTMENT">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" placeholder="PRODUCT CATEGORY">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" placeholder="PRODUCT SUB CATEGORY" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" placeholder="PRODUCT BRAND">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" placeholder="ORDER TYPE" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<%--      	<SELECT class="form-control" NAME ="STATUS" size="1">
      	<OPTION style="display:none;"  value="">Status</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(status.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(status.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(status.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
                             <input type="text" name="STATUS" id="STATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(newsts)%>" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'STATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  
  		</div>
  	
  	
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="form-control datepicker" id="DELDATE" name="DELDATE" value="<%=StrUtils.forHTMLTag(DELDATE)%>" READONLY placeholder="DELIVERY DATE">	
  		<INPUT type="text" class="ac-selected form-control hidden" name="DESC" type = "hidden" value="<%=StrUtils.forHTMLTag(DESC)%>" style="width: 100%"  MAXLENGTH=100 placeholder="Description">
  		<label class="radio-inline" ><INPUT name="VIEWSTATUS" type = hidden  value="ByOrderDate"  id="ByOrderDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByOrderDate")) {%>checked <%}%>></label>
    	<label class="radio-inline"><INPUT  name="VIEWSTATUS" type = "hidden" value="ByDeliveryDate"  id = "ByDeliveryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByDeliveryDate")) {%>checked <%}%>></label>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="CURRENCYDISPLAY" name="CURRENCYDISPLAY"  value="<%=CURRENCYDISPLAY%>"  placeholder="CURRENCY">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCYDISPLAY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		</div>
  		
  		  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
		</div>
  	
<!--   	  	<div class="row" style="padding:3px"> -->
<!--   		<div class="col-sm-2"> -->
<!--   		</div> -->
<!--   	  		<div class="col-sm-4 ac-box">   -->
<!--   	  		</div>	 -->
<!--   	  		<div class="col-sm-4 ac-box">  	 -->
<!-- 			<div class="col-sm-10 txn-buttons"> -->
<!-- 				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp; -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		</div> -->
  	
<%--   		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	<div class="col-sm-4 ac-box">  		
  		
      	 
  		<input type="text" class="form-control datepicker" id="DELDATE" name="DELDATE" value="<%=StrUtils.forHTMLTag(DELDATE)%>" READONLY placeholder="DELIVERY DATE">	
  		<INPUT type="text" class="ac-selected form-control hidden" name="DESC" type = "hidden" value="<%=StrUtils.forHTMLTag(DESC)%>" style="width: 100%"  MAXLENGTH=100 placeholder="Description">
  		<label class="radio-inline" ><INPUT name="VIEWSTATUS" type = hidden  value="ByOrderDate"  id="ByOrderDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByOrderDate")) {%>checked <%}%>></label>
    	<label class="radio-inline"><INPUT  name="VIEWSTATUS" type = "hidden" value="ByDeliveryDate"  id = "ByDeliveryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByDeliveryDate")) {%>checked <%}%>></label>			
  		</div>
  		<div class="col-sm-4 ac-box">  	
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
		</div>
  		</div> --%>
		</div>
		</div>
		
  	
  	<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  ">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-6">      
      <!-- <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</button>&nbsp; -->
  	  
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','IBO');}"><b>Back</b></button> -->
  	  </div>
        </div>
       	  </div>
		   
		   <div class="form-group">
      <div class="col-sm-3">
       	  <input type="checkbox" class="form-check-input" id="withlocalexpenses"   Onclick="checkval()" <%if (LOCALEXPENSES.equalsIgnoreCase("1")) {%> checked <%}%>/>&nbsp;View By Landed Cost&nbsp;  		
 	<INPUT class="form-control"  name="LOCALEXPENSES" type="hidden" value="<%=StrUtils.forHTMLTag(LOCALEXPENSES)%>"	size="30" MAXLENGTH=20>	
       	  </div></div>
       	  
    <INPUT type="Hidden" name="DIRTYPE" value="IB_SUMMARY_ORD_WITH_COST">
     

     
      <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordred table-striped">
									
					<thead>
		                <tr role="row">
		                
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">SUPPLIER NAME</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">DETAIL DESC</th>
		                	<th style="font-size: smaller;">DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">RECEIVE QTY</th>
		                	<th style="font-size: smaller;">UNIT COST</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">EXCHANGE RATE</th>
		                	<th style="font-size: smaller;">ORDER COST (<%=currency%>)</th>
		                	<th style="font-size: smaller;">ORDER COST</th>
		                	<th style="font-size: smaller;">TAX (<%=currency%>)</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL (<%=currency%>)</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">USER</th>
		                	<th style="font-size: smaller;">REMARKS</th>
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='12'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th></th>
		            <th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
  
  </FORM>
  </div></div></div>
   <div id="lastTranPriceModal" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg"> 		
  		<div class="modal-content">
  			<div class="modal-header">
          		<h4 class="modal-title">Remarks Details</h4>
        	</div>
        	<div class="modal-body">
	          <table class="table lastInvoicePriceDetails">
	          	<thead>
					<tr>
						<th>Remarks 1</th>
						<th>Remarks 2</th>
						<th>Product ID</th>
						<th>Remarks</th>
					</tr>
				</thead>
				<tbody>
				 
				</tbody>
	          </table>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
  		</div>
  	</div>
  </div> 
  <script>
  var tableInventorySummary;
  var ITEMNO,ITEMDESC,FROM_DATE,TO_DATE,DIRTYPE,USER,ORDERNO,JOBNO,STATUS,ORDERTYPE,PRD_DEPT_ID,PRD_BRAND_ID,PRD_TYPE_ID,PRD_CLS_ID, remarks, remarks3,SUPPLIERTYPE,currencyDisplay,
  STATUS_ID,VIEWSTATUS,LOCALEXPENSES,groupRowColSpan = 13;
function getParameters(){
	return { 
		 "ITEM": ITEMNO,"PRD_DESCRIP":ITEMDESC,"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,"REMARKS1":remarks,"REMARKS2":remarks3,
		"STATUS":STATUS,"ORDERTYPE":ORDERTYPE,"PRD_BRAND_ID":PRD_BRAND_ID,"PRD_DEPT_ID":PRD_DEPT_ID,"PRD_TYPE_ID":PRD_TYPE_ID,"PRD_CLS_ID":PRD_CLS_ID,"STATUS_ID":STATUS_ID,
		"VIEWSTATUS":VIEWSTATUS,"SUPPLIERTYPE":SUPPLIERTYPE,"LOCALEXPENSES":LOCALEXPENSES,"DELDATE":DELDATE,"CURRENCYDISPLAY":currencyDisplay,
		"ACTION": "VIEW_IB_SUMMARY_ORD_WITH_COST",
		"PLANT":"<%=plant%>"
		 
		}
}  
  function onGo(){

    var flag    = "false";

    FROM_DATE      = document.form1.FROM_DATE.value;
    TO_DATE        = document.form1.TO_DATE.value;
    DIRTYPE        = document.form1.DIRTYPE.value;
    USER           = document.form1.CUSTOMER.value;
    ITEMNO         = document.form1.ITEM.value;
    ITEMDESC       = document.form1.DESC .value;
    ORDERNO        = document.form1.ORDERNO.value;
    JOBNO          = document.form1.JOBNO.value;
    STATUS         = document.form1.STATUS.value;  
    currencyDisplay = document.form1.CURRENCYDISPLAY.value;
  //imti start
    if(STATUS=="OPEN")
    	STATUS="N";
    else if(STATUS=="PARTIALLY RECEIVED")
    	STATUS="O";
    else if(STATUS=="RECEIVED")
    	STATUS="C";
    //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value;
    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
    PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
    STATUS_ID      = document.form1.STATUS_ID.value;
    SUPPLIERTYPE     = document.form1.SUPPLIER_TYPE_ID.value;
    VIEWSTATUS=document.form1.VIEWSTATUS.value; 
    LOCALEXPENSES=document.form1.LOCALEXPENSES.value;
    DELDATE=document.form1.DELDATE.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}

    if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
    if(PRD_DEPT_ID != null     && PRD_DEPT_ID != "") { flag = true;}
    if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") { flag = true;}
    if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
    if(SUPPLIERTYPE != null     && SUPPLIERTYPE != "") { flag = true;}
    if(LOCALEXPENSES != null     && LOCALEXPENSES != "") { flag = true;}
    if(DELDATE != null     && DELDATE != "") { flag = true;}
    //if(STATUS_ID != null     && STATUS_ID != "") { flag = true;}
    
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    //storeInLocalStorage('printSupplierPOInvoice_FROMDATE', FROM_DATE);
	//storeInLocalStorage('printSupplierPOInvoice_TODATE', TO_DATE);
	storeInLocalStorage('printSupplierPOInvoice_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('printSupplierPOInvoice_SUPPLIER_TYPE_ID', $('#SUPPLIER_TYPE_ID').val());
	storeInLocalStorage('printSupplierPOInvoice_ORDERNO', ORDERNO);
	storeInLocalStorage('printSupplierPOInvoice_JOBNO', JOBNO);
	storeInLocalStorage('printSupplierPOInvoice_ITEM', ITEMNO);
	storeInLocalStorage('printSupplierPOInvoice_PRD_CLS_ID', PRD_CLS_ID);
	storeInLocalStorage('printSupplierPOInvoice_PRD_DEPT_ID', PRD_DEPT_ID);
	storeInLocalStorage('printSupplierPOInvoice_PRD_TYPE_ID', PRD_TYPE_ID);
	storeInLocalStorage('printSupplierPOInvoice_PRD_BRAND_ID', PRD_BRAND_ID);
	storeInLocalStorage('printSupplierPOInvoice_ORDERTYPE',$('#ORDERTYPE').val());
	storeInLocalStorage('printSupplierPOInvoice_STATUS', $('#STATUS').val());
	storeInLocalStorage('printSupplierPOInvoice_DELDATE', $('#DELDATE').val());
    }
    var urlStr = "../InboundOrderHandlerServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 9;
	
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].pono === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['Index'] = dataIndex + 1;
		        			data.items[dataIndex]['qtyor'] = addZeroes(parseFloat(data.items[dataIndex]['qtyor']).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
		        			data.items[dataIndex]['qty'] = addZeroes(parseFloat(data.items[dataIndex]['qty']).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
		        			data.items[dataIndex]['gstpercentage'] = addZeroes(parseFloat(data.items[dataIndex]['gstpercentage']).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
		        			data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['pono']+'\',\''+data.items[dataIndex]['polnno']+'\');"></a>';
		        			//data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\''+data.items[dataIndex]['remarks']+'\',\''+data.items[dataIndex]['remarks3']+'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['pono']+'\',\''+data.items[dataIndex]['polnno']+'\');"></a>';
		        		//	data.items[dataIndex]['remarks'] ='<a href="#" style="text-decoration: none;" onClick="javascript:popUpWin(\'inboundOrderPrdRemarksList.jsp?REMARKS1='+data.items[dataIndex]['REMARKS1']+'&REMARKS2='+data.items[dataIndex]['REMARKS2']+'&ITEM=' + data.items[dataIndex]['item']+'&PONO='+data.items[dataIndex]['pono']+"&POLNNO="+data.items[dataIndex]['polno']+'\');">&#9432;</a>';		        			
		        			data.items[dataIndex]['unitcost'] = parseFloat(data.items[dataIndex]['unitcost']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['orderCost'] = parseFloat(data.items[dataIndex]['orderCost']).toFixedSpecial1(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixedSpecial1(<%=DbBean.NOOFDECIMALPTSFORTAX%>);
		        			data.items[dataIndex]['ordCostwTax'] = parseFloat(data.items[dataIndex]['ordCostwTax']).toFixedSpecial1(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			//data.items[dataIndex]['pono'] = '<a href="/track/purchaseorderservlet?PONO=' +data.items[dataIndex]['pono']+ '&Submit='+['View']+'&RFLAG='+['4']+'">' + data.items[dataIndex]['pono'] + '</a>';
		        			//data.items[dataIndex]['pono'] = '<a href="/track/purchaseorderservlet?PONO='+data.items[dataIndex]['pono']+'&Submit='+data.items[dataIndex]['View']+'&RFLAG='+data.items[dataIndex]['4']+'">' + data.items[dataIndex]['pono'] + '</a>';
		        			
		        		<%if (displaySummaryLink) { %>
		        			data.items[dataIndex]['pono'] = '<a href="../purchaseorder/detail?pono=' +data.items[dataIndex]['pono']+'">' + data.items[dataIndex]['pono'] + '</a>';
		        		<%	} %>
		        			data.items[dataIndex]['unitCost'] = addZeroes(parseFloat(data.items[dataIndex]['unitCost']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['tax'] = addZeroes(parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['orderCost'] = addZeroes(parseFloat(data.items[dataIndex]['orderCost']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['ordCostwTax'] = addZeroes(parseFloat(data.items[dataIndex]['ordCostwTax']).toFixed(<%=numberOfDecimal%>));
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				//{"data": 'CHKPO', "orderable": false},
    			{"data": 'Index', "orderable": false}, /* 0 */
    			{"data": 'pono', "orderable": false}, /* 1 */
    			{"data": 'jobnum', "orderable": false}, /* 2 */
    			{"data": 'ordertype', "orderable": false}, /* 3 */
    			{"data": 'custname', "orderable": false}, /* 4 */
    			{"data": 'item', "orderable": false}, /* 5 */
    			{"data": 'itemdesc', "orderable": false}, /* 6 */
    			{"data": 'DetailItemDesc', "orderable": false}, /* 7 */
    			{"data": 'CollectionDate', "orderable": false}, /* 8 */
    			{"data": 'UOM', "orderable": false}, /* 9 */
    			{"data": 'qtyor', "orderable": false}, /* 10 */ 
    			{"data": 'qty', "orderable": false}, /* 11 */
    			{"data": 'unitCost', "orderable": false}, /* 12 */
    			{"data": 'gstpercentage', "orderable": false}, /* 13 */
    			{"data": 'exchangerate', "orderable": true}, /* 14 */
    			{"data": 'orderCost', "orderable": false}, /* 15 */
    			{"data": 'recvConvCost', "orderable": true}, /* 16 */
    			{"data": 'tax', "orderable": false}, /* 17 */
    			{"data": 'taxcon', "orderable": true}, /* 18 */
    			{"data": 'ordCostwTax', "orderable": false}, /* 19 */
    			{"data": 'recvCostConvwTax', "orderable": true}, /* 20 */
    			{"data": 'users', "orderable": false}, /* 21 */
    			{"data": 'remarks', "orderable": false} /* 22 */
    			],
			"columnDefs": [{"className": "t-right", "targets": [12,13,14]}],
			//"columnDefs": [{"visible": false, "targets":[12,13,14] }],
// 			"orderFixed": [ groupColumn, 'asc' ],
			"orderFixed": [ ], 
			/*"dom": 'lBfrtip',*/
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
                    extend: 'colvis'
                   //,columns: ':not(:eq('+groupColumn+')):not(:eq(1)):not(:eq(2)):not(:eq(4))'
                }
	        ],
	        "order":[],
			"drawCallback": function ( settings ) {
				this.attr('width', '100%');
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalCost = 0;
	            var groupTotalCost = 0;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var total = 0;
	            var groupTotal = 0;
	            
	            var totalCost1 = 0;
	            var groupTotalCost1 = 0;
	            var totalTax1 = 0;
	            var groupTotalTax1 = 0;
	            var total1 = 0;
	            var groupTotal1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                
	                	
	                	if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
		            	}if(groupTotalTax==null || groupTotalTax==0){
		            		groupTotalTaxVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		            	}if(groupTotal==null || groupTotal==0){
		            		groupTotalVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
		            	}
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td></td><td class="t-right">' + addZeroes(groupTotalCostVal) + '</td><td class="t-right">' + parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + parseFloat(groupTotalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td class="t-right">' + parseFloat(groupTotal1).toFixed(<%=numberOfDecimal%>) + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                    groupTotalCost1 = 0;
	                    groupTotalTax1 = 0;
	                    groupTotal1 = 0;
	                }
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                
	                groupTotalCost1 += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(/[^0-9.]/g, ''));
	                totalCost1 += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(/[^0-9.]/g, ''));
	                groupTotalTax1 += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(/[^0-9.]/g, ''));
	                totalTax1 += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(/[^0-9.]/g, ''));
	                groupTotal1 += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(/[^0-9.]/g, ''));
	                total1 += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(/[^0-9.]/g, ''));
	                
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	
	            	 if(totalCost==null || totalCost==0){
	            		totalCostVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		totalCostVal=parseFloat(totalCost).toFixed(<%=numberOfDecimal%>);
	            	} 
	            	if(groupTotalCost==null ||groupTotalCost==0){
	            		groupTotalCostVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
	            	}if(totalTax==null || totalTax==0){
	            		totalTaxVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotalTax==null || groupTotalTax==0){
	            		groupTotalTaxVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	            	}if(total==null || total==0){
	            		totalVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotal==null || groupTotal==0){
	            		groupTotalVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	            	}
	            	
	            	 $(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td></td><td class="t-right">' + addZeroes(totalCostVal) + '</td><td class="t-right">' + parseFloat(totalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + parseFloat(totalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td class="t-right">' + parseFloat(total1).toFixed(<%=numberOfDecimal%>) + '</td><td></td><td></td></tr>'
                    ); 
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td></td><td class="t-right">' + addZeroes(groupTotalCostVal) + '</td><td class="t-right">' + parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + parseFloat(groupTotalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td class="t-right">' + parseFloat(groupTotal1).toFixed(<%=numberOfDecimal%>) + '</td><td></td><td></td></tr>'
                    );
                }
	        },/**/
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
	              .column(14)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(15)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(17)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(19)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
				  
				  var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalCost = 0;
	            var groupTotalCost = 0;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var total = 0;
	            var groupTotal = 0;
	            
	            var totalCost1 = 0;
	            var groupTotalCost1 = 0;
	            var totalTax1 = 0;
	            var groupTotalTax1 = 0;
	            var total1 = 0;
	            var groupTotal1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                
	                	
	                	if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
		            	}if(groupTotalTax==null || groupTotalTax==0){
		            		groupTotalTaxVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		            	}if(groupTotal==null || groupTotal==0){
		            		groupTotalVal=parseFloat(0).toFixed(<%=numberOfDecimal%>);
		            	}else{
		            		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
		            	}
	                	
	                
	                    last = group;
	                    groupEnd = i;
	                    groupTotalCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                    groupTotalCost1 = 0;
	                    groupTotalTax1 = 0;
	                    groupTotal1 = 0;
	                }
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                
	                groupTotalCost1 += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(/[^0-9.]/g, ''));
	                totalCost1 += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(/[^0-9.]/g, ''));
	                groupTotalTax1 += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(/[^0-9.]/g, ''));
	                totalTax1 += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(/[^0-9.]/g, ''));
	                groupTotal1 += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(/[^0-9.]/g, ''));
	                total1 += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(/[^0-9.]/g, ''));
	                
	                currentRow = i;
	            } );

	            // Update footer
	            // $(api.column(15).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
	            // $(api.column(17).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            // $(api.column(19).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
				
				$(api.column(15).footer()).html(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(17).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(19).footer()).html(parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>));
	          }
		});
    }
}

$('#tableInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummary').attr('width', '100%');
});

$('.buttons-columnVisibility').each(function(){
  var $li = $(this),
      $cb = $('<input>', {
              type:'checkbox',
              style:'margin:0 .25em 0 0; vertical-align:middle'}
            ).prop('checked', $(this).hasClass('active') );
  $li.find('a').prepend( $cb );
});
	 
$('.buttons-columnVisibility').on('click', 'input:checkbox,li', function(){
  var $li = $(this).closest('li'),
      $cb = $li.find('input:checkbox');
  $cb.prop('checked', $li.hasClass('active') );
});	

   function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				
			}
		});
		
		if(!errorBoo){
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                   
	        	outPutdata = outPutdata+item.INBOUNDDETAILS
                    	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
    outPutdata = outPutdata +'</TABLE>';
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML ='';

 
}

function getTable(){
 return  '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
         '<TH><font color="#ffffff" align="center">S/N</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Supplier Name</TH>'+
          /* '<TH><font color="#ffffff" align="left"><b>Remarks1</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Remarks2</TH>'+ */
         '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Detail Description</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Receive Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Unit Cost</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Cost</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Total</TH>'+ 
         '<TH><font color="#ffffff" align="center">User</TH>'+
          '<TH><font color="#ffffff" align="center">Remarks</TH>'+
       '</tr>';
            
}
 $(document).ready(function(){
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	 //getLocalStorageValue('printSupplierPOInvoice_FROMDATE', $('#FROM_DATE').val(), 'FROM_DATE');
	 //getLocalStorageValue('printSupplierPOInvoice_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('printSupplierPOInvoice_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('printSupplierPOInvoice_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
	 getLocalStorageValue('printSupplierPOInvoice_ORDERNO', '', 'ORDERNO');
	 getLocalStorageValue('printSupplierPOInvoice_JOBNO', '', 'JOBNO');
	 getLocalStorageValue('printSupplierPOInvoice_ITEM', '', 'ITEM');
	 getLocalStorageValue('printSupplierPOInvoice_PRD_CLS_ID', '', 'PRD_CLS_ID');
	 getLocalStorageValue('printSupplierPOInvoice_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
	 getLocalStorageValue('printSupplierPOInvoice_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
	 getLocalStorageValue('printSupplierPOInvoice_PRD_DEPT_ID', '', 'PRD_DEPT_ID');
	 getLocalStorageValue('printSupplierPOInvoice_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('printSupplierPOInvoice_STATUS', '', 'STATUS');
	 getLocalStorageValue('printSupplierPOInvoice_DELDATE', '', 'DELDATE');
	    }
 onGo();
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
	/* Supplier Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')"> <p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
	
	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.SUPPLIER_TYPE_ID+'</p></div>';
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
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : query
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
		    return '<p>' + data.PONO + '</p>';
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
	

	/* Order Number Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
				OTYPE : "PURCHASE",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.ORDERTYPEMST);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
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
				document.form1.ORDERTYPE.value = "";
			}
		
		});
	
	/* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				//ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_REPORT",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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
	
	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
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


	/* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
// 	return '<p class="item-suggestion">'+ data.prd_dep_id +'</p>';
				return '<div onclick="document.form1.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
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
	
	/* PRODUCT SUB CATEGORY Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_TYPE_ID.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
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
	/* PRODUCT SUB CATEGORY Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_BRAND_ID.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
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


	/* To get the suggestion data for Currency */
	$("#CURRENCYDISPLAY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CURRENCY_DATA",
					CURRENCY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CURRENCYMST);
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
// 			return '<div><p onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			return '<div onclick="document.form1.currencyuseqt.value = \''+data.CURRENCYUSEQT+'\'"><p class="item-suggestion">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		
	});
	
	/* To get the suggestion data for Status */
	$("#STATUS").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
 });
 function checkval()
 {
	 var checkBox = document.getElementById("withlocalexpenses");
	 if (checkBox.checked == true){		 
		 document.form1.LOCALEXPENSES.value="1";		 
	 }
	 else
		 {		 
		 document.form1.LOCALEXPENSES.value="0";		 
		 }
	 
 }
 <%-- function loadLTPDetails(remarks,remarks3,pono,item,polnno){
		
	 var urlStr = "/track/InboundOrderHandlerServlet";
	 var plant= '<%=plant%>';
		$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT :  plant,
			ACTION : "getRemarksDetails",
			REMARKS1 : remarks,
			REMARKS2 : remarks3,
			PONO : pono,
			ITEM :  item,
			POLNNO : polnno
			
			
		},
		dataType : "json",
		success : function(dataList) {
			
			
		
	 	var body1="";
		
			dataList.REMARKSMST.forEach(function(data){
				
				
								
				body1 += "<tr>";
				body1 += "<td>"+ remarks+"</td>";
				body1 += "<td>"+ remarks3+"</td>";
				body1 += "<td>"+ item+"</td>";
				body1 += "<td>"+ data.REMARKS+"</td>";
				body1 += "</tr>";
			});
	
		
			$(".lastInvoicePriceDetails tbody").html(body1);

			$("#lastTranPriceModal").modal();
			
		}
		});
	 
	} --%> 
 function getvendname(TAXTREATMENT,VENDO){
		
	}
 function loadLTPDetails(remarks,remarks3,item,pono,polnno){
		
	 var urlStr = "/track/InboundOrderHandlerServlet";
	 var plant= '<%=plant%>';
		$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT :  plant,
			ACTION : "getRemarksDetails",
			REMARKS1 : remarks,
			REMARKS2 : remarks3,
			ITEM :  item,
			PONO : pono,			
			POLNNO : polnno
		},
		dataType : "json",
		success : function(dataList) {
	 		var body1="";
			dataList.REMARKSMST.forEach(function(data){
				body1 += "<tr>";
				//body1 += "<td>"+ remarks+"</td>";
				//body1 += "<td>"+ remarks3+"</td>";
				body1 += "<td>"+ data.REMARK1+"</td>";
				body1 += "<td>"+ data.REMARK2+"</td>";
				body1 += "<td>"+ item+"</td>";
				body1 += "<td>"+ data.REMARKS+"</td>";
				body1 += "</tr>";
			});
			$(".lastInvoicePriceDetails tbody").html(body1);
			$("#lastTranPriceModal").modal();
		}
		});
	 
	} 
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>