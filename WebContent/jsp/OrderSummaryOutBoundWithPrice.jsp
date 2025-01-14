<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Sales Order Summary With Price";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.DIRTYPE.value;
  document.form1.CUSTOMER.value;
  //document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="/track/deleveryorderservlet?Submit=ExportExcelOutboundOrderSummary";
  document.form1.submit();
}
//---End Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell format
function ExportReportWithRemarks()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.DIRTYPE.value;
  document.form1.CUSTOMER.value;
 // document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="/track/deleveryorderservlet?Submit=ExportExcelOutboundOrderSummaryWithRemarks";
  document.form1.submit();
 }
 
var postatus =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },
	{
    "year": "OPEN",
    "value": "OPEN",
    "tokens": [
      "OPEN"
    ]
  },
  {
	    "year": "PARTIALLY PICKED",
	    "value": "PARTIALLY PICKED",
	    "tokens": [
	      "PARTIALLY PICKED"
	    ]
	  },
	  {
		    "year": "PICKED",
		    "value": "PICKED",
		    "tokens": [
		      "PICKED"
		    ]
		  }];

var postatuss =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },
	{
    "year": "OPEN",
    "value": "OPEN",
    "tokens": [
      "OPEN"
    ]
  },
  {
	    "year": "PARTIALLY ISSUED",
	    "value": "PARTIALLY ISSUED",
	    "tokens": [
	      "PARTIALLY ISSUED"
	    ]
	  },
	  {
		   "year": "SHIPPED",
		    "value": "SHIPPED",
		    "tokens": [
		      "SHIPPED"
		    ]
		  }];

var postatusss =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },
	{
    "year": "OPEN",
    "value": "OPEN",
    "tokens": [
      "OPEN"
    ]
  },
  {
	    "year": "PARTIALLY ISSUED",
	    "value": "PARTIALLY ISSUED",
	    "tokens": [
	      "PARTIALLY ISSUED"
	    ]
	  },
	  {
		    "year": "ISSUED",
		    "value": "ISSUED",
		    "tokens": [
		      "ISSUED"
		    ]
	  },
	  {
		    "year": "SHIPPED",
		    "value": "SHIPPED",
		    "tokens": [
		      "SHIPPED"
		    ]
	  },
	  {
		    "year": "DELIVERED",
		    "value": "DELIVERED",
		    "tokens": [
		      "DELIVERED"
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
<style>
	.textBckgnd{
	    BACKGROUND: #eeeeee;
	    BORDER-BOTTOM: #888888 1px solid;
	    BORDER-LEFT: #888888 1px solid;
	    BORDER-RIGHT: #888888 1px solid;
	    BORDER-TOP: #888888 1px solid;
	    COLOR: #000000;
	    FONT-FAMILY: Arial, Arial;
	    FONT-SIZE: 12px;
    }
</style>
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();

int k=0;
String rowColor="";		


session= request.getSession();

String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_DEPT_ID="",PRD_CLS_ID="",statusID="",EMP_NAME="",issuests="",picksts="",POSSEARCH="1";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",SORT="",CUSTOMERTYPE="";

float Total=0,subtotal=0,unitprice=0;
 String chargeBy="",salesRate="",noOfOrders="0";
      double totalAmountToBill=0;
DecimalFormat decformat = new DecimalFormat("#,##0.00");
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));

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

ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays();//RESVI
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;//RESVI

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
CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
//imti start
picksts = PICKSTATUS;
//imti end
ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
//imti start
issuests = ISSUESTATUS;
//imti end
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
SORT = StrUtils.fString(request.getParameter("SORT"));
EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
PlantMstDAO plantMstDAO = new PlantMstDAO();
String ENABLE_POS = plantMstDAO.getispos(plant);
POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
}
//imti start
if(PICKSTATUS.equalsIgnoreCase("OPEN"))
	PICKSTATUS="N";
else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
	PICKSTATUS="O";
else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
	PICKSTATUS="C";
//imti end
//imti start
	if(DELIVERYAPP.equals("1")){
		if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
			ISSUESTATUS="N";
		else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
			ISSUESTATUS="O";
		else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
			ISSUESTATUS="C";
		else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
			ISSUESTATUS="S";
		else if(ISSUESTATUS.equalsIgnoreCase("DELIVERED"))
			ISSUESTATUS="D";
	}else{
		if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
			ISSUESTATUS="N";
		else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
			ISSUESTATUS="O";
		else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
			ISSUESTATUS="C";
 		}
//imti end

if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ORD_WITH_PRICE";//OUTBOUNDSUMMARYWITHPRICE
}

String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryExport=false,displaySummaryLink=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkPrintOBInvoice", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportforsalesorder", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkPrintOBInvoice", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportforsalesorder", plant,USERID);
}
%>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                         
                <li><label>Sales Order Summary With Price</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                            <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <% if (displaySummaryExport) { %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();">Export All Data </button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul> -->
					  <% } %>
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
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="OrderSummaryOutBoundWithPrice.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<INPUT  name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>">
<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>">
 
		<div id="target" style="padding: 18px;display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" id="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="CUSTOMER NAME" name="CUSTOMER" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>"  placeholder="CUSTOMER GROUP">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="ORDER NO" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ITEMNO)%>"placeholder="PRODUCT" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>"placeholder="PRODUCT DEPARTMENT">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  	<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CATEGORY">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=SALES&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
  	<%-- 	     	<SELECT class="form-control" NAME ="PICKSTATUS" size="1">
      	<OPTION style="display:none;"  value="">PickStatus</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
 <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(picksts)%>" placeholder="PICK STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PICKSTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  <%--  <label class="radio-inline" ><INPUT name="VIEWSTATUS" type = "hidden"  value="ByOrderDate"  id="ByOrderDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByOrderDate")) {%>checked <%}%>></label>
    	<label class="radio-inline"><INPUT  name="VIEWSTATUS" type = "hidden" value="ByDeliveryDate"  id = "ByDeliveryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByDeliveryDate")) {%>checked <%}%>></label> --%>
    	<INPUT type="Hidden" name="DIRTYPE" value="OUTBOUND"> 
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  	<%-- 	     	<SELECT class="form-control" NAME ="ISSUESTATUS" size="1">
      	<OPTION style="display:none;"  value="">IssueStatus</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
 <input type="text" name="ISSUESTATUS" id="ISSUESTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(issuests)%>"  <%if(DELIVERYAPP.equals("1")){%>placeholder="ISSUE STATUS" <%}else{%>placeholder="SHIPPED STATUS"<%} %> >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ISSUESTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="EMPLOYEE" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
		</div>
		
  		</div>
		
    
      <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
    <div class="ShowSingle">
      <div class="col-sm-offset-6">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();" ><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReportWithRemarks();" ><b>Export Product Remarks</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	  <button type="button" class="Submit btn btn-default"  onClick="{backNavigation('orderManagement.jsp','OBO');}"><b>Back</b></button> -->
  	  </div>
        </div>
       	   </div>
       	   <div class="form-group">
       	  <% if(ENABLE_POS.equals("1")){ %>
					<label class="control-label col-sm-1" for="view">View by :</label>
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
       	   
<%--        	      <div class="form-group">
       	      <label class="control-label col-sm-1" for="Sort By">Sort By</label>
      <div class="col-sm-3">
       	 <label class="radio-inline"><INPUT name="SORT" type = "radio"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>By Customer</label>
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="PRODUCT"    <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>checked <%}%>>By Product</label>	
       	  </div></div> --%>
   
    <INPUT type="Hidden" name="DIRTYPE" value="OB_SUMMARY_ORD_WITH_PRICE">
     <INPUT name="SORT" type = "hidden"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>
      	<INPUT  name="SORT" type = "hidden" value="PRODUCT"    <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>checked <%}%>>
      <div id="VIEW_RESULT_HERE" class="table-responsive">
 <table id="tableInventorySummaryByCustomer"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">CUSTOMER TYPE</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">DETAIL DESC</th>
		                	<th style="font-size: smaller;">ORDER DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ORDER PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">USER</th> 
		                	<th style="font-size: smaller;">REMARKS</th> 
		                	
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='16'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th></th>
		            <th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
				<table id="tableInventorySummaryByProduct"
									class="table table-bordred table-striped" style="display:none;">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">CUSTOMER GROUP</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">DETAIL DESC</th>
		                	<th style="font-size: smaller;">ORDER DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ORDER PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">USER</th> 
		                	<th style="font-size: smaller;">REMARKS</th> 
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='16'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th></th>
		            <th style="text-align: right !important"></th><th style="text-align: right !important"></th><th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
  </div>
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
  <div id="spinnerImg" ></div>
  
  </FORM>

 <script>
 var tableInventorySummaryByCustomer, tableInventorySummaryByProduct;
 var ITEMNO,ITEMDESC,FROM_DATE,TO_DATE,PRD_DEPT_IDDIRTYPE,USER,ORDERNO,JOBNO,PICKSTATUS,ISSUESTATUS,ORDERTYPE,PRD_BRAND_ID,PRD_TYPE_ID,PRD_CLS_ID,
 STATUS_ID,EMPNO,CUSTOMERTYPE,SORT,POSSEARCH;
 //var groupRowColSpan = 16;
function getParameters(){
	return {
		"ITEM": ITEMNO,"PRD_DESCRIP":ITEMDESC,"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,
		"PICKSTATUS":PICKSTATUS,"ISSUESTATUS":ISSUESTATUS,"ORDERTYPE":ORDERTYPE,"PRD_BRAND_ID":PRD_BRAND_ID,
		"PRD_TYPE_ID":PRD_TYPE_ID,"PRD_CLS_ID":PRD_CLS_ID,"PRD_DEPT_ID":PRD_DEPT_ID,"STATUS_ID":STATUS_ID,"SORT":SORT,"EMPNO":EMPNO,"CUSTOMERTYPE":CUSTOMERTYPE,"POSSEARCH":POSSEARCH,
		"ACTION": "VIEW_OUTBOUND_SUMMARY_ORD_WITH_PRICE","PLANT":"<%=plant%>",LOGIN_USER:"<%=USERID%>"
			
		
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
    PICKSTATUS     = document.form1.PICKSTATUS.value;
    POSSEARCH = document.form1.POSSEARCH.value;
    //imti start    
    if(PICKSTATUS=="OPEN")
    	PICKSTATUS="N";
    else if(PICKSTATUS=="PARTIALLY PICKED")
    	PICKSTATUS="O";
    else if(PICKSTATUS=="PICKED")
    	PICKSTATUS="C";
    //imti end
    ISSUESTATUS    = document.form1.ISSUESTATUS.value;
    //imti start
    <%if(DELIVERYAPP.equals("1")){%>
    	if(ISSUESTATUS=="OPEN")
    		ISSUESTATUS="N";
	    else if(ISSUESTATUS=="PARTIALLY ISSUED")
	    	ISSUESTATUS="O";
    	else if(ISSUESTATUS=="ISSUED")
    		ISSUESTATUS="C";
    	else if(ISSUESTATUS=="SHIPPED")
    		ISSUESTATUS="S";
    	else if(ISSUESTATUS=="DELIVERED")
	    	ISSUESTATUS="D";
	<%}else{%>
		if(ISSUESTATUS=="OPEN")
		   	ISSUESTATUS="N";
	    else if(ISSUESTATUS=="PARTIALLY ISSUED")
	    	ISSUESTATUS="O";
	    else if(ISSUESTATUS=="SHIPPED")
	    	ISSUESTATUS="C";
	<%}%>
    //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
    PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
    STATUS_ID      = document.form1.STATUS_ID.value; 
    SORT      = document.form1.SORT.value;
    EMPNO = document.form1.EMP_NAME.value;
    CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
   
   
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
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    //storeInLocalStorage('OrderSummaryOutBoundWithPrice_FROMDATE', FROM_DATE);
	//storeInLocalStorage('OrderSummaryOutBoundWithPrice_TODATE', TO_DATE);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_CUSTOMER_TYPE_ID', CUSTOMERTYPE);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_ORDERNO', ORDERNO);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_JOBNO', JOBNO);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_ITEM', ITEMNO);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_PRD_CLS_ID', PRD_CLS_ID);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_PRD_TYPE_ID', PRD_TYPE_ID);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_PRD_BRAND_ID', PRD_BRAND_ID);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_PRD_DEPT_ID', PRD_DEPT_ID);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_PICKSTATUS', $('#PICKSTATUS').val());
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_ISSUESTATUS', $('#ISSUESTATUS').val());
	storeInLocalStorage('OrderSummaryOutBoundWithPrice_EMPNAME', EMPNO);
    }
    var urlStr = "../OutboundOrderHandlerServlet";
   	// Call the method of JQuery Ajax provided
   	//var groupColumn = document.forms[0].SORT[0].checked ? 4 : 7;
   	var groupColumn ="1";
   if($('input[name=SORT]:checked').length > 0)
	   		groupColumn=4;
	   else
		   groupColumn=7;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    //if (document.forms[0].SORT[0].checked){
    if($('input[name=SORT]:checked').length > 0){
    	$('#tableInventorySummaryByProduct').css('display', 'none');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByCustomer').css('display', 'block');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'block');
        if (tableInventorySummaryByCustomer){
        	tableInventorySummaryByCustomer.destroy();
        }
    	groupRowColSpan = 16;//	Total gray line OR Grand total gray line
	    tableInventorySummaryByCustomer = $('#tableInventorySummaryByCustomer').DataTable({
			"processing": true,
			"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].dono === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['Index'] = dataIndex + 1;
		        			/* data.items[dataIndex]['remarks'] ='<a href="#" style="text-decoration: none;" onClick="javascript:popUpWin(\'outboundOrderPrdRemarksList.jsp?REMARKS1='+data.items[dataIndex]['remarks']+'&REMARKS2='+data.items[dataIndex]['remarks2']+'&ITEM=' + data.items[dataIndex]['item']+'&DONO='+data.items[dataIndex]['dono']+"&DOLNNO="+data.items[dataIndex]['dolnno']+'\');">&#9432;</a>'; */
		        			/* data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\''+data.items[dataIndex]['remarks']+'\',\''+data.items[dataIndex]['remarks2']+'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['dono']+'\',\''+data.items[dataIndex]['dolnno']+'\');"></a>'; */
		        			data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['dono']+'\',\''+data.items[dataIndex]['dolnno']+'\');"></a>';
		        			//data.items[dataIndex]['dono'] = '<a href="/track/deleveryorderservlet?DONO=' +data.items[dataIndex]['dono']+ '&Submit='+['View']+'&RFLAG='+['4']+'">' + data.items[dataIndex]['dono'] + '</a>';
		        			<% if (displaySummaryLink) { %>
		        			data.items[dataIndex]['dono'] = '<a href="../salesorder/detail?dono=' +data.items[dataIndex]['dono']+'">' + data.items[dataIndex]['dono'] + '</a>';
		        			<% } %>
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'DetailItemDesc', "orderable": true},
    			{"data": 'CollectionDate', "orderable": true},
    			{"data": 'UOM', "orderable": true},
    			{"data": 'qtyor', "orderable": true},
    			{"data": 'qtypick', "orderable": true},
    			{"data": 'qty', "orderable": true},
    			{"data": 'empname', "orderable": true},
    			{"data": 'avgcost', "orderable": true},
    			{"data": 'gstpercentage', "orderable": true},
    			{"data": 'issueAvgcost', "orderable": true},
    			{"data": 'tax', "orderable": true},    			
    			{"data": 'issAvgcostwTax', "orderable": true},
    			{"data": 'users', "orderable": true},
    			{"data": 'remarks', "orderable": true}
    			],
			"columnDefs": [{"className": "t-right", "targets": [17,18,19]}],
			"orderFixed": [ groupColumn, 'asc' ], 
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
                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+'))'
                }
	        ],"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalRCost = 0;
	            var groupTotalRCost = 0;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var total = 0;
	            var groupTotal = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {                
	                	
	                	if(groupTotalRCost==null || groupTotalRCost==0){
	                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>); 
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>); 
	                	}
	                	
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td></td><td class="t-right">' + groupTotalRCostVal + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + groupTotalVal + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td></td><td class="t-right">' + totalRCostVal + '</td><td class="t-right">' + totalTaxVal + '</td><td class="t-right">' + totalVal + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td></td><td class="t-right">' + groupTotalRCostVal + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + groupTotalVal + '</td><td></td><td></td></tr>'
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
	              .column(18)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(18)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(19)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Update footer
	            $(api.column(18).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(19).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(20).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
	          }
		});
	}else{
    	$('#tableInventorySummaryByProduct').css('display', 'block');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'block');
    	$('#tableInventorySummaryByCustomer').css('display', 'none');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'none');
	    if (tableInventorySummaryByProduct){
	    	tableInventorySummaryByProduct.destroy();
	    }
		groupRowColSpan = 16;
	    tableInventorySummaryByProduct = $('#tableInventorySummaryByProduct').DataTable({
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
		        	if(typeof data.items[0].dono === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['Index'] = dataIndex + 1;
		        			/* data.items[dataIndex]['remarks'] ='<a href="#" style="text-decoration: none;" onClick="javascript:popUpWin(\'outboundOrderPrdRemarksList.jsp?REMARKS1='+data.items[dataIndex]['remarks']+'&REMARKS2='+data.items[dataIndex]['remarks2']+'&ITEM=' + data.items[dataIndex]['item']+'&DONO='+data.items[dataIndex]['dono']+"&DOLNNO="+data.items[dataIndex]['dolnno']+'\');">&#9432;</a>'; */
		        			/* data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\''+data.items[dataIndex]['remarks']+'\',\''+data.items[dataIndex]['remarks2']+'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['dono']+'\',\''+data.items[dataIndex]['dolnno']+'\');"></a>'; */
		        			<%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\'<%=(String)lineArr.get("dono")%>\',\'<%=(String)lineArr.get("item")%>\',\'<%=(String)lineArr.get("dolnno")%>\');"></a> '; --%>
		        			data.items[dataIndex]['remarks'] ='<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\''+ data.items[dataIndex]['item']+'\',\''+data.items[dataIndex]['dono']+'\',\''+data.items[dataIndex]['dolnno']+'\');"></a>';
		        			//data.items[dataIndex]['dono'] = '<a href="/track/deleveryorderservlet?DONO=' +data.items[dataIndex]['dono']+ '&Submit='+['View']+'&RFLAG='+['4']+'">' + data.items[dataIndex]['dono'] + '</a>';
		        			<% if (displaySummaryLink) { %>
		        			data.items[dataIndex]['dono'] = '<a href="../salesorder/detail?dono=' +data.items[dataIndex]['dono']+'">' + data.items[dataIndex]['dono'] + '</a>';
		        			<% } %>
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'DetailItemDesc', "orderable": true},
    			{"data": 'CollectionDate', "orderable": true},
    			{"data": 'UOM', "orderable": true},
    			{"data": 'qtyor', "orderable": true},
    			{"data": 'qtypick', "orderable": true},
    			{"data": 'qty', "orderable": true},
    			{"data": 'empname', "orderable": true},
    			{"data": 'avgcost', "orderable": true},
    			{"data": 'gstpercentage', "orderable": true},
    			{"data": 'issueAvgcost', "orderable": true},
    			{"data": 'tax', "orderable": true},    			
    			{"data": 'issAvgcostwTax', "orderable": true},
    			{"data": 'users', "orderable": true},
    			{"data": 'remarks', "orderable": true}
    			],
			"columnDefs": [{"className": "t-right", "targets": [17,18,19]}],
			"orderFixed": [ groupColumn, 'asc' ], 
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
                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+'))'
                }
	        ],"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalRCost = 0;
	            var groupTotalRCost = 0;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var total = 0;
	            var groupTotal = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {	                	
	                	if(groupTotalRCost==null || groupTotalRCost==0){
	                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td></td><td class="t-right">' + groupTotalRCostVal + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + groupTotalVal + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(18)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td></td><td class="t-right">' + totalRCostVal + '</td><td class="t-right">' + totalTaxVal + '</td><td class="t-right">' + totalVal + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td></td><td class="t-right">' + groupTotalRCostVal + '</td><td class="t-right">' + groupTotalTaxVal + '</td><td class="t-right">' + groupTotalVal + '</td><td></td><td></td></tr>'
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
	              .column(18)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(18)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(19)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Update footer
	            $(api.column(18).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(19).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(20).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
	          }
		});
	}
}

$('#tableInventorySummaryByCustomer').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummaryByCustomer tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummaryByCustomer').attr('width', '100%');
});

$('#tableInventorySummaryByProduct').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummaryByProduct tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummaryByProduct').attr('width', '100%');
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
                     
	        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
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
   return '<TABLE WIDTH="95%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Type</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Cust Status</TH>'+
//           '<TH><font color="#ffffff" align="left"><b>Remarks1</TH>'+
//           '<TH><font color="#ffffff" align="left"><b>Remarks2</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Detail Description</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Date</TH>'+
          '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Pick Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Unit Price</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Price</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total</TH>'+ 
          '<TH><font color="#ffffff" align="left"><b>User</TH>'+ 
           '<TH><font color="#ffffff" align="center"><b>Remarks</TH>'+	
          '</tr>';
              
}
 
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
  autosize(document.querySelectorAll('textarea'));
  
  </script>
  </div></div></div>
  
                    <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	 //getLocalStorageValue('OrderSummaryOutBoundWithPrice_FROMDATE', $('#FROM_DATE').val(), 'FROM_DATE');
	 //getLocalStorageValue('OrderSummaryOutBoundWithPrice_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_CUSTOMER_TYPE_ID', '', 'CUSTOMER_TYPE_ID');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_ORDERNO', '', 'ORDERNO');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_JOBNO', '', 'JOBNO');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_ITEM', '', 'ITEM');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_PRD_CLS_ID', '', 'PRD_CLS_ID');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_PRD_DEPT_ID', '', 'PRD_DEPT_ID');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_PICKSTATUS', '', 'PICKSTATUS');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_ISSUESTATUS', '', 'ISSUESTATUS');
	 getLocalStorageValue('OrderSummaryOutBoundWithPrice_EMPNAME', '', 'EMP_NAME');
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
	
	
	/* Customer Type Auto Suggestion */
	$('#CUSTOMER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  async: true,   
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
	
	/* Employee Type Auto Suggestion */
	$('#EMP_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'EMPNO',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getEmployeeListStartsWithName",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.EMP_MST);
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
		    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
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
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
				OTYPE : "SALES",
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
	
	$("#PICKSTATUS").typeahead({
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
	
	$("#ISSUESTATUS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
			<%if(DELIVERYAPP.equals("1")){%>
			name: 'postatusss',
			<%}else{%>
	  		name: 'postatuss',
			<%}%>
		  display: 'value',  
		  <%if(DELIVERYAPP.equals("1")){%>
		  source: substringMatcher(postatusss),
		  <%}else{%>
		  source: substringMatcher(postatuss),
		  <%}%>
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
 function getvendname(VENDO){
		
	}
 function loadLTPDetails(remarks,remarks2,item,dono,dolnno){
		
	 var urlStr = "/track/OutboundOrderHandlerServlet";
	 var plant= '<%=plant%>';
		$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT :  plant,
			ACTION : "getRemarksDetails",
			REMARKS1 : remarks,
			REMARKS2 : remarks2,
			ITEM :  item,
			DONO : dono,			
			DOLNNO : dolnno
			
			
		},
		dataType : "json",
		success : function(dataList) {
			
			
		
	 	var body1="";
		
			dataList.REMARKSMST.forEach(function(data){
				
				
								
				body1 += "<tr>";
				//body1 += "<td>"+ remarks+"</td>";
				//body1 += "<td>"+ remarks2+"</td>";
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
 function changepostype(count){
	  $("input[name ='POSSEARCH']").val(count);
	  onGo();
	} 
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>