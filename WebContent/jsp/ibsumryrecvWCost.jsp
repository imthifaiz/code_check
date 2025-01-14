<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Purchase Order Summary Details By Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
   <jsp:param value="<%=title%>" name="title"/>
   <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
   <jsp:param name="submenu" value="<%=IConstants.PURCHASE_REPORTS%>"/>
</jsp:include>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 22 2014, Description:To open purchase order summary  in excel powershell format
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
var postatus =   [{
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
//---End Modified by Deen on May 22 2014, Description:To open purchase order summary  in excel powershell format
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>


<%
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movSupplierList  = new ArrayList();


session= request.getSession();
String plant = (String)session.getAttribute("PLANT");
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String USERID ="";
String FROM_DATE ="",  TO_DATE = "",status="", DIRTYPE ="",ORDERTYPE="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="",newstatus ="";
String PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="",statusID="",GRNO="",SUPPLIERTYPE="",LOCALEXPENSES="",CURRENCYDISPLAY="";
PGaction         = StrUtils.fString(request.getParameter("PGaction"));
String html = "",cntRec ="false";
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));

boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportrecvcost", plant,LOGIN_USER);
	displaySummaryExport = true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportrecvcost", plant,LOGIN_USER);
	displaySummaryExport = true;
}

String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
else
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
// String curDate = DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;


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
newstatus = status;
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
GRNO = StrUtils.fString(request.getParameter("GRNO"));
SUPPLIERTYPE = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
LOCALEXPENSES = StrUtils.fString(request.getParameter("LOCALEXPENSES"));
float Total=0;
int j=0;
String rowColor="";		
if(LOCALEXPENSES.equals(""))
{
	LOCALEXPENSES="1";
}
if(DIRTYPE.length()<=0){
DIRTYPE = "IB_SUMMARY_RECV_WITH_COST";
}	
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase(""))CURRENCYDISPLAY=basecurrency;

String LOCALEXPENSESCHK = "checked";
if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
	//LOCALEXPENSESCHK="";
	//LOCALEXPENSES="0";	
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

String currency=_PlantMstDAO.getBaseCurrency(plant);

%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Purchase Order summary Details By Cost</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                 <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();">Export All Data</button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul>	 -->
					   <%} %>				
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../jsp/home.jsp'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">

<form class="form-horizontal" name="form1" method="post" action="ibsumryrecvWCostWCost.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="currencyuseqt" value="">

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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/supplierlist.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="SUPPLIER_TYPE_ID" name="SUPPLIER_TYPE_ID"   placeholder="SUPPLIER GROUP" value="<%=StrUtils.forHTMLTag(SUPPLIERTYPE)%>">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO"  placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="REFERENCE NO">				
  		</div>
		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" placeholder="PRODUCT" value="<%=StrUtils.forHTMLTag(ITEM)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeproduct(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" placeholder="PRODUCT DEPARTMENT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprddeptid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" placeholder="PRODUCT CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdclsid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status"  placeholder="PRODUCT SUB CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdtypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID"  placeholder="PRODUCT BRAND" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdbrdid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status"  placeholder="ORDER TYPE" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>	
        <INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>">
    	<INPUT type="Hidden" name="DIRTYPE" value="IB_SUMMARY_RECV_WITH_COST">
    	
    	<div class="col-sm-4 ac-box">                       
        <input type="text" name="STATUS" id="STATUS" class="ac-selected form-control"  placeholder="STATUS" value="<%=StrUtils.forHTMLTag(status)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changestatus(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="GRNO" id="GRNO" class="ac-selected form-control status"  placeholder="GRNO" value="<%=StrUtils.forHTMLTag(GRNO)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changegrno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/list/ib_grno_list.jsp?PONO='+form1.ORDERNO.value+'&GRNO='+form1.GRNO.value+'&STATUS='+form1.STATUS_ID.value+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="CURRENCYDISPLAY" name="CURRENCYDISPLAY"    placeholder="CURRENCY" value="<%=StrUtils.forHTMLTag(CURRENCYDISPLAY)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecurrency(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		</div>
  		
  		  	  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  	  		<div class="col-sm-4 ac-box">  	
			<div class="col-sm-10 txn-buttons">
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
			</div>
		</div>
		</div>
  		
<%--   		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="GRNO" id="GRNO" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(GRNO)%>" placeholder="GRNO" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'GRNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/list/ib_grno_list.jsp?PONO='+form1.ORDERNO.value+'&GRNO='+form1.GRNO.value+'&STATUS='+form1.STATUS_ID.value+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
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
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      <br>
      <input type="checkbox" class="form-check-input" id="withlocalexpenses"   Onclick="checkval()" <%if (LOCALEXPENSES.equalsIgnoreCase("1")) {%> checked <%}%>/>&nbsp;View By Landed Cost&nbsp;  		
 	  <INPUT class="form-control"  name="LOCALEXPENSES" type="hidden" value="<%=StrUtils.forHTMLTag(LOCALEXPENSES)%>"	size="30" MAXLENGTH=20>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
   	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RIB');}"> <b>Back</b></button> -->
  	  </div>
         </div>
       	   </div>     
       
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
		                	<th style="font-size: smaller;">GRNO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">SUPPLIER NAME</th>
		                	<th style="font-size: smaller;">REMARKS1</th>
		                	<th style="font-size: smaller;">REMARKS2</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">RECVD DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">RECEVD QTY</th>
		                	<th style="font-size: smaller;">UNIT COST (<%=currency%>)</th>
		                	<th style="font-size: smaller;">UNIT COST</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">EXCHANGE RATE</th>
		                	<th style="font-size: smaller;">RECVE COST (<%=currency%>)</th>
		                	<th style="font-size: smaller;">RECEV COST</th>
		                	<th style="font-size: smaller;">TAX (<%=currency%>)</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL (<%=currency%>)</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">USER</th>
		                </tr>
		            </thead>
		              <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='15'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <!-- <th></th> -->
		            <th style="text-align: right !important"></th><th style="text-align: right !important"><th style="text-align: right !important"><th style="text-align: right !important"></th><th style="text-align: right !important"></th>
		            <th></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
  </form>
  <script>
  var tableInventorySummary;
  var ITEMNO,ITEMDESC,FROM_DATE,TO_DATE,DIRTYPE,USER,ORDERNO,JOBNO,STATUS,ORDERTYPE,PRD_DEPT_ID,PRD_BRAND_ID,LOCALEXPENSES,PRD_TYPE_ID,PRD_CLS_ID,STATUS_ID, GRNO,SUPPLIERTYPE,currencyDisplay, groupRowColSpan = 16;
function getParameters(){
	return { 
		"ITEM": ITEMNO,"PRD_DESCRIP":ITEMDESC,"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,
		"JOBNO":JOBNO,"STATUS":STATUS,"ORDERTYPE":ORDERTYPE,"PRD_BRAND_ID":PRD_BRAND_ID, "PRD_DEPT_ID":PRD_DEPT_ID,"PRD_TYPE_ID":PRD_TYPE_ID,"PRD_CLS_ID":PRD_CLS_ID,
		"STATUS_ID":STATUS_ID,"CURRENCYDISPLAY":currencyDisplay,
		"GRNO":GRNO,"SUPPLIERTYPE":SUPPLIERTYPE,"LOCALEXPENSES":LOCALEXPENSES,
		"ACTION": "VIEW_IB_SUMMARY_RECV_WITH_COST",
		"PLANT":"<%=plant%>"
	}
}   
  
  function onGo(){
//debugger;
    flag    = "false";
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
    if(STATUS=="PARTIALLY RECEIVED")
  	STATUS="O";
  	else if(STATUS=="RECEIVED")
  	STATUS="C";
 //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value;
    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
    PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
    STATUS_ID      = document.form1.STATUS_ID.value;
    GRNO			= document.form1.GRNO.value;
    SUPPLIERTYPE     = document.form1.SUPPLIER_TYPE_ID.value;
    LOCALEXPENSES	= document.form1.LOCALEXPENSES.value; 
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(JOBNO != null     && JOBNO != "") { flag = true;}
   if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
   if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") { flag = true;}
   if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
   if(PRD_DEPT_ID != null     && PRD_DEPT_ID != "") { flag = true;}
   if(STATUS_ID != null     && STATUS_ID != "") { flag = true;}
   if(GRNO != null && GRNO != ""){flag = true;}
   if(SUPPLIERTYPE != null     && SUPPLIERTYPE != "") { flag = true;}
   if(LOCALEXPENSES != null     && LOCALEXPENSES != "") { flag = true;}
   
   /*storeInLocalStorage('ibsumryrecvWCost_FROMDATE', FROM_DATE);
	storeInLocalStorage('ibsumryrecvWCost_TODATE', TO_DATE);
	storeInLocalStorage('ibsumryrecvWCost_CUSTOMER', USER);
	storeInLocalStorage('ibsumryrecvWCost_SUPPLIER_TYPE_ID',SUPPLIERTYPE);
	storeInLocalStorage('ibsumryrecvWCost_ORDERNO', ORDERNO);
	storeInLocalStorage('ibsumryrecvWCost_JOBNO', JOBNO);
	storeInLocalStorage('ibsumryrecvWCost_ITEM', ITEMNO);
	storeInLocalStorage('ibsumryrecvWCost_PRD_CLS_ID', PRD_CLS_ID);
	storeInLocalStorage('ibsumryrecvWCost_PRD_TYPE_ID', PRD_TYPE_ID);
	storeInLocalStorage('ibsumryrecvWCost_PRD_BRAND_ID', PRD_BRAND_ID);
	storeInLocalStorage('ibsumryrecvWCost_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('ibsumryrecvWCost_STATUS', $('#STATUS').val());
	storeInLocalStorage('ibsumryrecvWCost_GRNO', GRNO);*/
   var urlStr = "../InboundOrderHandlerServlet";
  	// Call the method of JQuery Ajax provided
  	var groupColumn = 1;
  	var costColumn = 18;
  	var costColumn1 = 19;
  	var taxColumn = 20;
  	var taxColumn1 = 21;
  	var totalColumn = 22;
  	var totalColumn1 = 23;
  	//var totalQty = 0;
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
		        			data.items[dataIndex]['unitCost'] = parseFloat(data.items[dataIndex]['unitCost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['recvCost'] = parseFloat(data.items[dataIndex]['recvCost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['recvCostwTax'] = parseFloat(data.items[dataIndex]['recvCostwTax']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	   			{"data": 'Index', "orderable": true}, /* 0 */
	   			{"data": 'pono', "orderable": true}, /* 1 */
	   			{"data": 'jobNum', "orderable": true}, /* 2 */
	   			{"data": 'GRNO', "orderable": true}, /* 3 */
	   			{"data": 'ordertype', "orderable": true}, /* 4 */
	   			{"data": 'custname', "orderable": true}, /* 5 */
	   			{"data": 'remarks', "orderable": true}, /* 6 */
	   			{"data": 'remarks3', "orderable": true}, /* 7 */
	   			{"data": 'item', "orderable": true}, /* 8 */
	   			{"data": 'itemdesc', "orderable": true}, /* 9 */
	   			{"data": 'RECVDATE', "orderable": true}, /* 10 */
	   			{"data": 'UOM', "orderable": true}, /* 11 */
	   			{"data": 'qtyor', "orderable": true}, /* 12 */
	   			{"data": 'qty', "orderable": true}, /* 13 */
	   			{"data": 'unitCost', "orderable": true}, /* 14 */
	   			{"data": 'unitCostConv', "orderable": true}, /* 15 */
	   			{"data": 'gstpercentage', "orderable": true}, /* 16 */
	   			{"data": 'exchangerate', "orderable": true}, /* 17 */
	   			{"data": 'recvCost', "orderable": true}, /* 18 */
	   			{"data": 'recvConvCost', "orderable": true}, /* 19 */
	   			{"data": 'tax', "orderable": true}, /* 20 */
	   			{"data": 'taxcon', "orderable": true}, /* 21 */
	   			{"data": 'recvCostwTax', "orderable": true}, /* 22 */
	   			{"data": 'recvCostConvwTax', "orderable": true}, /* 23 */
	   			{"data": 'users', "orderable": true},
	   		],
			"columnDefs": [{"className": "t-right", "targets": [costColumn, taxColumn, totalColumn,costColumn1, taxColumn1, totalColumn1]}],
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
                   extend: 'colvis',
                  // columns: ':not(:eq('+groupColumn+')):not(:last)'
                   columns: ':not(:eq('+groupColumn+')):not(:eq(15)):not(:eq('+costColumn+')):not(:eq('+taxColumn+')):not(:eq('+totalColumn+'))'
               }
	        ],
	        "order":[],
			"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var totalReceiveCost = 0;
	            var groupTotalReceiveCost = 0;
	            var totalCost = 0;
	            var groupTotalCost = 0;
	            
	            var totalTax1 = 0;
	            var groupTotalTax1 = 0;
	            var totalReceiveCost1 = 0;
	            var groupTotalReceiveCost1 = 0;
	            var totalCost1 = 0;
	            var groupTotalCost1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                	var groupTotalReceiveCostVal=null,groupTotalTaxVal=null,groupTotalCostVal=null;
	                	
	                	if(groupTotalReceiveCost==null || groupTotalReceiveCost==0){
	                		groupTotalReceiveCostVal="0.000";
	                	}else{
	                		groupTotalReceiveCostVal=parseFloat(groupTotalReceiveCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal="0.000";
	                	}else{
	                		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td></td><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalReceiveCostVal + '</td><td class="t-right">' + parseFloat(groupTotalReceiveCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + parseFloat(groupTotalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalReceiveCost = 0;
	                    groupTotalTax = 0;
	                    groupTotalCost = 0;
	                    groupTotalReceiveCost1 = 0;
	                    groupTotalTax1 = 0;
	                    groupTotalCost1 = 0;
	                }
	                groupTotalReceiveCost += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn + ')').html().replace(',', '').replace('$', ''));
	                totalReceiveCost += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn + ')').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn + ')').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn + ')').html().replace(',', '').replace('$', ''));
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn + ')').html().replace(',', '').replace('$', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn + ')').html().replace(',', '').replace('$', ''));


	                groupTotalReceiveCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalReceiveCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                groupTotalTax1 += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalTax1 += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                groupTotalCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	var totalReceiveCostVal=null,groupTotalReceiveCostVal=null,totalTaxVal=null,groupTotalTaxVal=null,groupTotalCostVal=null,totalCostVal=null;
	            	
	            	if(totalReceiveCost==null || totalReceiveCost==0){
	            		totalReceiveCostVal="0.000";
	            	}else{
	            		totalReceiveCostVal=parseFloat(totalReceiveCost).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotalReceiveCost==null || groupTotalReceiveCost==0){
	            		groupTotalReceiveCostVal="0.000";
	            	}else{
	            		groupTotalReceiveCostVal=parseFloat(groupTotalReceiveCost).toFixed(<%=numberOfDecimal%>);
	            	}if(totalTax==null || totalTax==0){
	            		totalTaxVal="0.000";
	            	}else{
	            		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotalTax==null || groupTotalTax==0){
	            		groupTotalTaxVal="0.000";
	            	}else{
	            		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	            	}if(totalCost==null || totalCost==0){
	            		totalCostVal="0.000";
	            	}else{
	            		totalCostVal=parseFloat(totalCost).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotalCost==null || groupTotalCost==0){
	            		groupTotalCostVal="0.000";
	            	}else{
	            		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
	            	}
	            	
	            	 $(rows).eq( currentRow ).after(
	                        '<tr class="group"><td></td><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalReceiveCostVal + '</td><td class="t-right">' + parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + totalTaxVal + '</td><td class="t-right">' + parseFloat(totalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + totalCostVal + '</td><td class="t-right">' + parseFloat(totalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
                   ); 
               	$(rows).eq( currentRow ).after(
               			'<tr class="group"><td></td><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + groupTotalReceiveCostVal + '</td><td class="t-right">' + parseFloat(groupTotalReceiveCost1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + parseFloat(groupTotalTax1).toFixed(<%=numberOfDecimal%>) + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
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
	              .column(costColumn)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalReceiveCostVal = api
	              .column(costColumn)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(taxColumn)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalCostVal = api
	              .column(totalColumn)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              


	            groupTotalCost1 = api
	              .column(costColumn1)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            totalTax1 = api
	              .column(taxColumn1)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            totalCost1 = api
	              .column(totalColumn1)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
				  
				   var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var totalReceiveCost = 0;
	            var groupTotalReceiveCost = 0;
	            var totalCost = 0;
	            var groupTotalCost = 0;
	            
	            var totalTax1 = 0;
	            var groupTotalTax1 = 0;
	            var totalReceiveCost1 = 0;
	            var groupTotalReceiveCost1 = 0;
	            var totalCost1 = 0;
	            var groupTotalCost1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                	var groupTotalReceiveCostVal=null,groupTotalTaxVal=null,groupTotalCostVal=null;
	                	
	                	if(groupTotalReceiveCost==null || groupTotalReceiveCost==0){
	                		groupTotalReceiveCostVal="0.000";
	                	}else{
	                		groupTotalReceiveCostVal=parseFloat(groupTotalReceiveCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal="0.000";
	                	}else{
	                		groupTotalCostVal=parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalReceiveCost = 0;
	                    groupTotalTax = 0;
	                    groupTotalCost = 0;
	                    groupTotalReceiveCost1 = 0;
	                    groupTotalTax1 = 0;
	                    groupTotalCost1 = 0;
	                }
	                groupTotalReceiveCost += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn + ')').html().replace(',', '').replace('$', ''));
	                totalReceiveCost += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn + ')').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn + ')').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn + ')').html().replace(',', '').replace('$', ''));
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn + ')').html().replace(',', '').replace('$', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn + ')').html().replace(',', '').replace('$', ''));


	                groupTotalReceiveCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalReceiveCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + costColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                groupTotalTax1 += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalTax1 += parseFloat($(rows).eq( i ).find('td:eq(' + taxColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                groupTotalCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                totalCost1 += parseFloat($(rows).eq( i ).find('td:eq(' + totalColumn1 + ')').html().replace(/[^0-9.]/g, ''));
	                currentRow = i;
	            } );

	            // Update footer
	            // $(api.column(costColumn).footer()).html(parseFloat(totalReceiveCostVal).toFixed(<%=numberOfDecimal%>));
	            // $(api.column(taxColumn).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            // $(api.column(totalColumn).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
				$(api.column(costColumn).footer()).html(parseFloat(groupTotalReceiveCost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(taxColumn).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(totalColumn).footer()).html(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
// 	            Update footer
	            <%-- $(api.column(costColumn1).footer()).html(parseFloat(groupTotalCost1).toFixed(<%=numberOfDecimal%>));
	            $(api.column(taxColumn1).footer()).html(parseFloat(totalTax1).toFixed(<%=numberOfDecimal%>));
	            $(api.column(totalColumn1).footer()).html(parseFloat(totalCost1).toFixed(<%=numberOfDecimal%>)); --%>
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
          '<TH><font color="#ffffff" align="left"><b>Remarks1</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Remarks2</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Received Date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Receive Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Unit Cost</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Recv Cost</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Total</TH>'+ 
         '<TH><font color="#ffffff" align="center"><b>Order Status</TH>'+
         '<TH><font color="#ffffff" align="center"><b>User</TH>'+
       '</tr>';
            
}

//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
          

 </SCRIPT> 
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>

 function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}


 function changeorderno(obj){
	 $("#ORDERNO").typeahead('val', '"');
	 $("#ORDERNO").typeahead('val', '');
	 $("#ORDERNO").focus();
	}

 function changeproduct(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}

 function changeprddeptid(obj){
	 $("#PRD_DEPT_ID").typeahead('val', '"');
	 $("#PRD_DEPT_ID").typeahead('val', '');
	 $("#PRD_DEPT_ID").focus();
	}

 function changeprdclsid(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}

 function changeprdtypeid(obj){
	 $("#PRD_TYPE_ID").typeahead('val', '"');
	 $("#PRD_TYPE_ID").typeahead('val', '');
	 $("#PRD_TYPE_ID").focus();
	}

 function changeprdbrdid(obj){
	 $("#PRD_BRAND_ID").typeahead('val', '"');
	 $("#PRD_BRAND_ID").typeahead('val', '');
	 $("#PRD_BRAND_ID").focus();
	}

 function changeordertype(obj){
	 $("#ORDERTYPE").typeahead('val', '"');
	 $("#ORDERTYPE").typeahead('val', '');
	 $("#ORDERTYPE").focus();
	}


 function changecustomertypeid(obj){
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '"');
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '');
	 $("#SUPPLIER_TYPE_ID").focus();
	}

 function changegrno(obj){
	 $("#GRNO").typeahead('val', '"');
	 $("#GRNO").typeahead('val', '');
	 $("#GRNO").focus();
	}

 function changestatus(obj){
	 $("#STATUS").typeahead('val', '"');
	 $("#STATUS").typeahead('val', '');
	 $("#STATUS").focus();
	}

 
 function changecurrency(obj){
	 $("#CURRENCYDISPLAY").typeahead('val', '"');
	 $("#CURRENCYDISPLAY").typeahead('val', '');
	 $("#CURRENCYDISPLAY").focus();
	}
 
 $(document).ready(function(){
	 /*getLocalStorageValue('ibsumryrecvWCost_FROMDATE', $('#FROM_DATE').val() , 'FROM_DATE');
	 getLocalStorageValue('ibsumryrecvWCost_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('ibsumryrecvWCost_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('ibsumryrecvWCost_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
	 getLocalStorageValue('ibsumryrecvWCost_ORDERNO', '', 'ORDERNO');
	 getLocalStorageValue('ibsumryrecvWCost_JOBNO', '', 'JOBNO');
	 getLocalStorageValue('ibsumryrecvWCost_ITEM', '', 'ITEM');
	 getLocalStorageValue('ibsumryrecvWCost_PRD_CLS_ID', '', 'PRD_CLS_ID');
	 getLocalStorageValue('ibsumryrecvWCost_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
	 getLocalStorageValue('ibsumryrecvWCost_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
	 getLocalStorageValue('ibsumryrecvWCost_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('ibsumryrecvWCost_STATUS', '', 'STATUS');
	 getLocalStorageValue('ibsumryrecvWCost_GRNO', '', 'GRNO');*/
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
// 		    return '<p>' + data.VNAME + '</p>';
		    	return '<div><p class="item-suggestion">Name: ' + data.VNAME 
		    	   + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
		    	   + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
	
	/* GRNO Auto Suggestion */
	$('#GRNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GRNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_GRNO_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.GRNO_MST);
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
			return '<div><p class="item-suggestion">'+data.GRNO+'</p></div>';
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
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
