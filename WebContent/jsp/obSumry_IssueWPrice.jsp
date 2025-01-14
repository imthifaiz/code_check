<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
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

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 21 2014, Description:To open sales order summary  in excel powershell format
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
//---End Modified by Deen on May 21 2014, Description:To open sales order summary  in excel powershell format
var postatus =   [{
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
		  },
		  {
			   "year": "INTRANSIT",
			    "value": "INTRANSIT",
			    "tokens": [
			      "INTRANSIT"
			    ]
			  },
		  {
			   "year": "DELIVERED",
			    "value": "DELIVERED",
			    "tokens": [
			      "DELIVERED"
			    ]
			  }];

var postatusss =   [{
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
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>

<%
Generator generator   = new Generator();
HTReportUtil movHisUtil = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();

int k=0;
String rowColor="";		


session= request.getSession();

String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="",statusID="",EMP_NAME="",picksts="",issuests="",LOC ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",POSSEARCH="1";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",SORT="",CUSTOMERTYPE="",INVOICENO="";

DecimalFormat decformat = new DecimalFormat("#,##0.00");
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));
LOC     = StrUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
PlantMstDAO plantMstDAO = new PlantMstDAO();
String ENABLE_POS = plantMstDAO.getispos(plant);
POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
}
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
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

boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportobordersumrywissprice", plant,LOGIN_USER);
	displaySummaryExport=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportobordersumrywissprice", plant,LOGIN_USER);
	displaySummaryExport=true;
}

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays();//resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;

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
INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
//imti start
if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
	PICKSTATUS="O";
else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
	PICKSTATUS="C";
//imti end
// //imti start
// if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
// 	ISSUESTATUS="O";
// else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
// 	ISSUESTATUS="C";
// //imti end

//imti start
	if(DELIVERYAPP.equals("1")){
		 if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
			ISSUESTATUS="O";
		else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
			ISSUESTATUS="C";
		else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
			ISSUESTATUS="S";
		else if(ISSUESTATUS.equalsIgnoreCase("DELIVERED"))
			ISSUESTATUS="D";
	}else{
		 if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
			ISSUESTATUS="O";
		else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
			ISSUESTATUS="C";
 		}
//imti end



if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ISS_WITH_PRICE";
}
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
%>

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
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button> 
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul> -->
					   <%} %>

					
				</div>

				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/salessummarybyprice" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="plant" value="<%=plant%>">
<input type="hidden" name="LOC_DESC" value="">
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
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
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
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"  placeholder="CUSTOMER GROUP" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO"  placeholder="ORDER NUMBER" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" placeholder="PRODUCT CATEGORY"value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>">
  		
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdclsid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID"  placeholder="PRODUCT BRAND" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>">
  		
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdbrdid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=SALES&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
  
        <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control"  placeholder="PICK STATUS" value="<%=StrUtils.forHTMLTag(PICKSTATUS)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changepicksatus(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>	
  		
  
    	
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  	
        <input type="text" name="ISSUESTATUS" id="ISSUESTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ISSUESTATUS)%>" <%if(DELIVERYAPP.equals("1")){%>placeholder="ISSUE STATUS" <%}else{%>placeholder="STATUS"<%} %> >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeissuestatus(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>	
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status"  placeholder="EMPLOYEE" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeempname(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<!--Begin Murali  -->
  		<!-- </div> -->
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="INVOICENO" id="INVOICENO" class="ac-selected form-control status"  placeholder="GINO" value="<%=StrUtils.forHTMLTag(INVOICENO)%>">
  		
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeinvoiceno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('list/ob_invoiceno_list.jsp?DONO='+form1.ORDERNO.value+'&INVOICENO='+form1.INVOICENO.value+'&STATUS='+form1.STATUS_ID.value+'&PICKISSUE=Y'+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>	
       <div class="col-sm-4 ac-box">
        <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type ="TEXT" placeholder="LOCATION" value="<%=StrUtils.forHTMLTag(LOC)%>" size="30"  MAXLENGTH=20>
        
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--    		<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  -->

<!--    	    <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!--    	    <a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		</div>  		
  	 </div>
  		<input type="hidden" name="PRD_BRAND_DESC" value="">
  		
 		<INPUT name="ACTIVE" type = "hidden" value="">
                <!--   Start code added by Deen for product brand on 11/9/12 -->
		<!--</div>  --> 
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
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
		
 		</div>
  		<!--End Murali  -->
  		  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
 <div class="">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT"  placeholder="LOCATION TYPE THREE" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" size="35" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc3(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--         <span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeThreeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
    	</div>
 		</div> 
 		<div class="col-sm-4 ac-box">
		<label class="radio-inline"><INPUT name="SORT" type = "radio"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>By Customer</label>
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="PRODUCT"    <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>checked <%}%>>By Product</label>
      	<%-- <label class="radio-inline" id="location_sort_option" style="display: none;"><input  name="sort" type = "radio" value="location"    <%if(sort.equalsignorecase("location")) {%>checked <%}%>>by location</label> --%>
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="LOCATION"    <%if(SORT.equalsIgnoreCase("LOCATION")) {%>checked <%}%>>By Location</label>		
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="DATE"    <%if(SORT.equalsIgnoreCase("DATE")) {%>checked <%}%>>By Date</label>		
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
			
			<INPUT  name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>"> 
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
     <!--  <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
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
   
      <INPUT type="Hidden" name="DIRTYPE" value="OB_SUMMARY_ISS_WITH_PRICE">
      
     <div id="VIEW_RESULT_HERE" class="table-responsive">
                
                
                <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div>
		                  
 <table id="tableInventorySummaryByCustomer"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">GINO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">CUSTOMER TYPE</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">REMARK1</th>
		                	<th style="font-size: smaller;">REMARK2</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">ISSUED DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ISSUE PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                	<th style="font-size: smaller;">USER</th>
		                	                	
		                </tr>
		            </thead>
		            
		             <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='19'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
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
		                	<th style="font-size: smaller;">GINO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">CUSTOMER TYPE</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">REMARK1</th>
		                	<th style="font-size: smaller;">REMARK2</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">ISSUED DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ISSUE PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                	<th style="font-size: smaller;">USER</th>
		                	
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='19'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
				<table id="tableInventorySummaryByDate"
									class="table table-bordred table-striped" style="display:none;">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">GINO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">CUSTOMER TYPE</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">REMARK1</th>
		                	<th style="font-size: smaller;">REMARK2</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">ISSUED DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ISSUE PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                	<th style="font-size: smaller;">USER</th>
		                	
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='19'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
				<table id="tableInventorySummaryByLocation"
									class="table table-bordred table-striped" style="display:none;">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">GINO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">LOC</th>
		                	<th style="font-size: smaller;">CUSTOMER GROUP</th>
		                	<th style="font-size: smaller;">CUSTOMER STATUS</th>
		                	<th style="font-size: smaller;">REMARK1</th>
		                	<th style="font-size: smaller;">REMARK2</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">ISSUED DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">PICK QTY</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">UNIT PRICE</th>
		                	<th style="font-size: smaller;">TAX%</th>
		                	<th style="font-size: smaller;">ISSUE PRICE</th>
		                	<th style="font-size: smaller;">TAX</th>
		                	<th style="font-size: smaller;">TOTAL</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                	<th style="font-size: smaller;">USER</th>
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='20'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
  </div>
  <div id="spinnerImg" ></div>
      
  </FORM>
 <script>


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

 function changepicksatus(obj){
	 $("#PICKSTATUS").typeahead('val', '"');
	 $("#PICKSTATUS").typeahead('val', '');
	 $("#PICKSTATUS").focus();
	}

 function changeissuestatus(obj){
	 $("#ISSUESTATUS").typeahead('val', '"');
	 $("#ISSUESTATUS").typeahead('val', '');
	 $("#ISSUESTATUS").focus();
	}

 function changeempname(obj){
	 $("#EMP_NAME").typeahead('val', '"');
	 $("#EMP_NAME").typeahead('val', '');
	 $("#EMP_NAME").focus();
	}

 function changeinvoiceno(obj){
	 $("#INVOICENO").typeahead('val', '"');
	 $("#INVOICENO").typeahead('val', '');
	 $("#INVOICENO").focus();
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

 
 var tableInventorySummaryByCustomer, tableInventorySummaryByProduct, tableInventorySummaryByLocation,tableInventorySummaryByDate;
 var ITEMNO,ITEMDESC,FROM_DATE,TO_DATE,DIRTYPE,USER,ORDERNO,PRD_DEPT_ID,JOBNO,PICKSTATUS,ISSUESTATUS,ORDERTYPE,PRD_BRAND_ID,PRD_TYPE_ID,PRD_CLS_ID,POSSEARCH,
 STATUS_ID,EMPNO,CUSTOMERTYPE,SORT,INVOICENO,loc,loctype, loctype2,loctype3;
 //var groupRowColSpan = 19;
 var issuePriceColumn = 20;
 var taxColumn = 21;
 var totalColumn = 22;
function getParameters(){
	return {
		"ITEM": ITEMNO,"PRD_DESCRIP":ITEMDESC,"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,
		"JOBNO":JOBNO,"PICKSTATUS":PICKSTATUS,"ISSUESTATUS":ISSUESTATUS,"ORDERTYPE":ORDERTYPE,"PRD_BRAND_ID":PRD_BRAND_ID,"PRD_TYPE_ID":PRD_TYPE_ID,
		"PRD_CLS_ID":PRD_CLS_ID,"STATUS_ID":STATUS_ID,"SORT":SORT,"PRD_DEPT_ID":PRD_DEPT_ID,"EMPNO":EMPNO,"CUSTOMERTYPE":CUSTOMERTYPE,"POSSEARCH":POSSEARCH,
		"ACTION": "VIEW_OUTBOUND_SUMMARY_ISSUE_WITH_PRICE","INVOICENO":INVOICENO,"PLANT":"<%=plant%>",
		"LOC": loc,"LOC_TYPE_ID": loctype,"LOC_TYPE_ID2": loctype2,"LOC_TYPE_ID3": loctype3
			
		
	}
}  
  
  function onGo(){

   var flag    = "false";
	var locationProvided = false;
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
	 if(PICKSTATUS=="PARTIALLY PICKED")
    	PICKSTATUS="O";
    else if(PICKSTATUS=="PICKED")
    	PICKSTATUS="C";
    //imti end
    ISSUESTATUS    = document.form1.ISSUESTATUS.value;
//     //imti start
//     if(ISSUESTATUS=="PARTIALLY ISSUED")
//     	ISSUESTATUS="O";
//     else if(ISSUESTATUS=="ISSUED")
//     	ISSUESTATUS="C";
//     //imti end
    
    //imti start
    <%if(DELIVERYAPP.equals("1")){%>
	    if(ISSUESTATUS=="PARTIALLY ISSUED")
	    	ISSUESTATUS="O";
    	else if(ISSUESTATUS=="ISSUED")
    		ISSUESTATUS="C";
    	else if(ISSUESTATUS=="SHIPPED")
    		ISSUESTATUS="S";
    	else if(ISSUESTATUS=="DELIVERED")
	    	ISSUESTATUS="D";
	<%}else{%>
	    if(ISSUESTATUS=="PARTIALLY ISSUED")
	    	ISSUESTATUS="O";
	    else if(ISSUESTATUS=="SHIPPED")
	    	ISSUESTATUS="C";
	<%}%>
    //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
    STATUS_ID      = document.form1.STATUS_ID.value; 
    PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value; 
    SORT      = document.form1.SORT.value;
    EMPNO = document.form1.EMP_NAME.value;
    CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
    INVOICENO = document.form1.INVOICENO.value;
    
   
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
    if(INVOICENO != null     && INVOICENO != "") { flag = true;}
    locationProvided = false;
    if(SORT=="CUSTOMER"){
  	   document.form1.LOC.value="";
  	   document.form1.LOC_TYPE_ID.value="";
  	   document.form1.LOC_TYPE_ID2.value="";
  	   document.form1.LOC_TYPE_ID3.value="";
     }
     if(SORT=="PRODUCT"){
  	   document.form1.LOC.value="";
  	   document.form1.LOC_TYPE_ID.value="";
  	   document.form1.LOC_TYPE_ID2.value="";
  	   document.form1.LOC_TYPE_ID3.value="";
     }
     if(SORT=="DATE"){
  	   document.form1.LOC.value="";
  	   document.form1.LOC_TYPE_ID.value="";
  	   document.form1.LOC_TYPE_ID2.value="";
  	   document.form1.LOC_TYPE_ID3.value="";
     }
     loc = document.form1.LOC.value;
     loctype = document.form1.LOC_TYPE_ID.value;
     loctype2 = document.form1.LOC_TYPE_ID2.value;
     loctype3 = document.form1.LOC_TYPE_ID3.value;
    if(loc != null     && loc != "") { flag = true;locationProvided = true;}
    if(loctype != null     && loctype != "") { flag = true;locationProvided = true;}
    if(loctype2 != null     && loctype2 != "") { flag = true;locationProvided = true;}
    if(loctype3 != null     && loctype3 != "") { flag = true;locationProvided = true;}

    /*storeInLocalStorage('obSumry_IssueWPrice_FROMDATE', FROM_DATE);
	storeInLocalStorage('obSumry_IssueWPrice_TODATE', TO_DATE);
	storeInLocalStorage('obSumry_IssueWPrice_CUSTOMER', USER);
	storeInLocalStorage('obSumry_IssueWPrice_CUSTOMER_TYPE_ID', CUSTOMERTYPE);
	storeInLocalStorage('obSumry_IssueWPrice_ORDERNO', ORDERNO);
	storeInLocalStorage('obSumry_IssueWPrice_JOBNO', JOBNO);
	storeInLocalStorage('obSumry_IssueWPrice_ITEM', ITEMNO);
	storeInLocalStorage('obSumry_IssueWPrice_PRD_CLS_ID', PRD_CLS_ID);
	storeInLocalStorage('obSumry_IssueWPrice_PRD_TYPE_ID', PRD_TYPE_ID);
	storeInLocalStorage('obSumry_IssueWPrice_PRD_BRAND_ID', PRD_BRAND_ID);
	storeInLocalStorage('obSumry_IssueWPrice_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('obSumry_IssueWPrice_PICKSTATUS', $('#PICKSTATUS').val());
	storeInLocalStorage('obSumry_IssueWPrice_ISSUESTATUS', $('#ISSUESTATUS').val());
	storeInLocalStorage('obSumry_IssueWPrice_EMPNAME', EMPNO);
	storeInLocalStorage('obSumry_IssueWPrice_INVOICENO', INVOICENO);
	storeInLocalStorage('obSumry_IssueWPrice_LOC', loc);
	storeInLocalStorage('obSumry_IssueWPrice_LOC_TYPE_ID', loctype);
	storeInLocalStorage('obSumry_IssueWPrice_LOC_TYPE_ID2',loctype2);
	storeInLocalStorage('obSumry_IssueWPrice_LOC_TYPE_ID3', loctype3);*/
     var urlStr = "../OutboundOrderHandlerServlet";
   	// Call the method of JQuery Ajax provided
   	//var groupColumn = document.forms[0].SORT[0].checked ? 5 : 10;
   var groupColumn =5;
  /*  if(locationProvided) {
		$('#location_sort_option').css('display', '');
   }else{
		$('#location_sort_option').css('display', 'none');
   } */
   if($('input[name=SORT]:checked').length > 0){
	   if ($('input[name=SORT]:checked').val() == 'CUSTOMER'){
			groupColumn=5;
	   }else if ($('input[name=SORT]:checked').val() == 'PRODUCT'){
			groupColumn=10;
	   }else if ($('input[name=SORT]:checked').val() == 'DATE'){
			groupColumn=12;
	   }else if ($('input[name=SORT]:checked').val() == 'LOCATION'){
			groupColumn=6;
	   }

   }
   
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    //if (document.forms[0].SORT[0].checked){
   if(locationProvided || $('input[name=SORT]:checked').val() == 'LOCATION'){
    	$('#tableInventorySummaryByLocation').css('display', 'block');
    	$('#tableInventorySummaryByLocation_wrapper').css('display', 'block');
    	$('#tableInventorySummaryByCustomer').css('display', 'none');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByProduct').css('display', 'none');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByDate').css('display', 'none');
    	$('#tableInventorySummaryByDate_wrapper').css('display', 'none');
	    if (tableInventorySummaryByLocation){
	    	tableInventorySummaryByLocation.destroy();
	    }
		groupRowColSpan = 20;
	    tableInventorySummaryByLocation = $('#tableInventorySummaryByLocation').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],

			searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
	        
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
		        				        			
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'INVOICENO', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'LOC', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'remarks', "orderable": true},
    			{"data": 'remarks2', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'issuedate', "orderable": true},
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
    			{"data": 'DELIVERY_STATUS', "orderable": true},
    			{"data": 'users', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [21,22,23]}],
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
	                    		doc.defaultStyle.fontSize = 8;
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
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 8;                     	       
//                      	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(20)):not(:eq(21)):not(:eq(22)):not(:eq(23))'
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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(23)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(23)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' +groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
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
	              .column(21)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(21)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(22)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(23)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(23)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(23)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(21).footer()).html(parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(23).footer()).html(parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>));
<%-- 	            $(api.column(21).footer()).html(parseFloat(total).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(23).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); --%>
	          }
		});

		//tablecode

	    $("#tableInventorySummaryByLocation_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
             var searchType = $('#searchType').val(); // Get the selected search type
            if (!searchTerm) return true; // If no search term, show all rows

           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
           var searchPattern;

            // Define the search pattern based on the selected search type
            if (searchType === 'first') {
                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
            } else if (searchType === 'middle') {
                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
            } else if (searchType === 'last') {
                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
            }
            // Check each column in the row for a match
            for (var i = 0; i < data.length; i++) {
                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
                if (columnData.match(searchPattern)) {
                    return true; // Match found, include this row in results
                }
            }
            return false; // No match, exclude this row from results
        });

        // Redraw table when the search input changes
        $('.dataTables_filter input').on('keyup', function () {
        	tableInventorySummaryByLocation.draw();
        });
	}else if($('input[name=SORT]:checked').val() == 'CUSTOMER'){
    	$('#tableInventorySummaryByProduct').css('display', 'none');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByCustomer').css('display', 'block');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'block');
    	$('#tableInventorySummaryByLocation').css('display', 'none');
    	$('#tableInventorySummaryByLocation_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByDate').css('display', 'none');
    	$('#tableInventorySummaryByDate_wrapper').css('display', 'none');
        if (tableInventorySummaryByCustomer){
        	tableInventorySummaryByCustomer.destroy();
        }
        
    	groupRowColSpan = 19;//	Total gray line OR Grand total gray line
	    tableInventorySummaryByCustomer = $('#tableInventorySummaryByCustomer').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
			searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
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
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'INVOICENO', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'remarks', "orderable": true},
    			{"data": 'remarks2', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'issuedate', "orderable": true},
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
    			{"data": 'DELIVERY_STATUS', "orderable": true},
    			{"data": 'users', "orderable": true},
    			//{"data": 'LOC', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [19,20,21]}],
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
	                    		doc.defaultStyle.fontSize = 8;
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
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 8;                     	       
//                      	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(19)):not(:eq(20)):not(:eq(21)):not(:eq(22))'
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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>); 
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>); 
	                	}
	                	
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' +groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' +groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
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
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(21)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(22)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);


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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>); 
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>); 
	                	}
	                	
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(20).footer()).html(parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>));
<%-- 	            $(api.column(20).footer()).html(parseFloat(total).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); --%>
	          }
		});

	    $("#tableInventorySummaryByCustomer_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
             var searchType = $('#searchType').val(); // Get the selected search type
            if (!searchTerm) return true; // If no search term, show all rows

           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
           var searchPattern;

            // Define the search pattern based on the selected search type
            if (searchType === 'first') {
                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
            } else if (searchType === 'middle') {
                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
            } else if (searchType === 'last') {
                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
            }
            // Check each column in the row for a match
            for (var i = 0; i < data.length; i++) {
                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
                if (columnData.match(searchPattern)) {
                    return true; // Match found, include this row in results
                }
            }
            return false; // No match, exclude this row from results
        });

        // Redraw table when the search input changes
        $('.dataTables_filter input').on('keyup', function () {
        	tableInventorySummaryByCustomer.draw();
        });
	}else if($('input[name=SORT]:checked').val() == 'DATE'){
    	$('#tableInventorySummaryByDate').css('display', 'block');
    	$('#tableInventorySummaryByDate_wrapper').css('display', 'block');
		$('#tableInventorySummaryByProduct').css('display', 'none');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByCustomer').css('display', 'none');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByLocation').css('display', 'none');
    	$('#tableInventorySummaryByLocation_wrapper').css('display', 'none');
	    if (tableInventorySummaryByDate){
	    	tableInventorySummaryByDate.destroy();
	    }
	    
		groupRowColSpan = 19;
	    tableInventorySummaryByDate = $('#tableInventorySummaryByDate').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
             searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
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
		        				        			
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'INVOICENO', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'remarks', "orderable": true},
    			{"data": 'remarks2', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'issuedate', "orderable": true},
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
    			{"data": 'DELIVERY_STATUS', "orderable": true},
    			{"data": 'users', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [19,20,21]}],
			"orderFixed": [ groupColumn, 'desc' ], 
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
	                    		doc.defaultStyle.fontSize = 8;
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
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 8;                     	       
//                      	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                   // columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(19)):not(:eq(20)):not(:eq(21)):not(:eq(22))'
                }
	        ],
// 	        "order": [ groupColumn,'desc' ],
	        "drawCallback": function ( settings ) {
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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' +groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
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
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(21)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(22)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(20).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>));
<%-- 	            $(api.column(20).footer()).html(parseFloat(total).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); --%>
	          }
		});
//mubarak
	    $("#tableInventorySummaryByDate_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
             var searchType = $('#searchType').val(); // Get the selected search type
            if (!searchTerm) return true; // If no search term, show all rows

           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
           var searchPattern;

            // Define the search pattern based on the selected search type
            if (searchType === 'first') {
                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
            } else if (searchType === 'middle') {
                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
            } else if (searchType === 'last') {
                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
            }
            // Check each column in the row for a match
            for (var i = 0; i < data.length; i++) {
                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
                if (columnData.match(searchPattern)) {
                    return true; // Match found, include this row in results
                }
            }
            return false; // No match, exclude this row from results
        });

        // Redraw table when the search input changes
        $('.dataTables_filter input').on('keyup', function () {
        	tableInventorySummaryByDate.draw();
        });
	} else {
    	$('#tableInventorySummaryByProduct').css('display', 'block');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'block');
    	$('#tableInventorySummaryByCustomer').css('display', 'none');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByLocation').css('display', 'none');
    	$('#tableInventorySummaryByLocation_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByDate').css('display', 'none');
    	$('#tableInventorySummaryByDate_wrapper').css('display', 'none');
	    if (tableInventorySummaryByProduct){
	    	tableInventorySummaryByProduct.destroy();
	    }
	    
		groupRowColSpan = 19;
	    tableInventorySummaryByProduct = $('#tableInventorySummaryByProduct').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
             searching: true, // Enable searching
	         search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
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
		        				        			
		        			data.items[dataIndex]['avgcost'] = parseFloat(data.items[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['tax'] = parseFloat(data.items[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issueAvgcost'] = parseFloat(data.items[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>);
		        			data.items[dataIndex]['issAvgcostwTax'] = parseFloat(data.items[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Index', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'INVOICENO', "orderable": true},
    			{"data": 'orderType', "orderable": true},
    			{"data": 'jobnum', "orderable": true},
    			{"data": 'custname', "orderable": true},
    			{"data": 'customertypedesc', "orderable": true},
    			{"data": 'customerstatusdesc', "orderable": true},
    			{"data": 'remarks', "orderable": true},
    			{"data": 'remarks2', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'issuedate', "orderable": true},
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
    			{"data": 'DELIVERY_STATUS', "orderable": true},
    			{"data": 'users', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [19,20,21]}],
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
	                    		doc.defaultStyle.fontSize = 8;
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
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 8;                     	       
//                      	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                   // columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(19)):not(:eq(20)):not(:eq(21)):not(:eq(22))'
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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' +groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
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
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(20)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(21)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(22)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

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
	                		groupTotalRCostVal="0.000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.000";
	                	}else{
	                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalRCost = 0;
	                    groupTotalTax = 0;
	                    groupTotal = 0;
	                }
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(22)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(20).footer()).html(parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>));
<%-- 	            $(api.column(20).footer()).html(parseFloat(total).toFixed(<%=numberOfDecimal%>));
	            $(api.column(21).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(22).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); --%>
	          }
		});

	    $("#tableInventorySummaryByProduct_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
             var searchType = $('#searchType').val(); // Get the selected search type
            if (!searchTerm) return true; // If no search term, show all rows

           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
           var searchPattern;

            // Define the search pattern based on the selected search type
            if (searchType === 'first') {
                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
            } else if (searchType === 'middle') {
                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
            } else if (searchType === 'last') {
                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
            }
            // Check each column in the row for a match
            for (var i = 0; i < data.length; i++) {
                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
                if (columnData.match(searchPattern)) {
                    return true; // Match found, include this row in results
                }
            }
            return false; // No match, exclude this row from results
        });

        // Redraw table when the search input changes
        $('.dataTables_filter input').on('keyup', function () {
        	tableInventorySummaryByProduct.draw();
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

$('#tableInventorySummaryByDate').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummaryByDate tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummaryByDate').attr('width', '100%');
});

$('#tableInventorySummaryByLocation').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummaryByLocation tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummaryByLocation').attr('width', '100%');
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
          '<TH><font color="#ffffff" align="left"><b>Customer Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Customer Type</TH>'+
		  '<TH><font color="#ffffff" align="left"><b>Customer Status</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Remarks1</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Remarks2</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issued Date</TH>'+
          '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Pick Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Unit Price</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Price</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total</TH>'+
          '<TH><font color="#ffffff" align="left"><b>User</TH>'+ 
          '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
           '</tr>';
              
}
 
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
            
  </script> 
  </div></div></div>
  
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	    /*getLocalStorageValue('obSumry_IssueWPrice_FROMDATE', $('#FROM_DATE').val(),'FROM_DATE');
		getLocalStorageValue('obSumry_IssueWPrice_TODATE', '','TO_DATE');
		getLocalStorageValue('obSumry_IssueWPrice_CUSTOMER', '','CUSTOMER');
		getLocalStorageValue('obSumry_IssueWPrice_CUSTOMER_TYPE_ID', '','CUSTOMER_TYPE_ID');
		getLocalStorageValue('obSumry_IssueWPrice_ORDERNO','','ORDERNO');
		getLocalStorageValue('obSumry_IssueWPrice_JOBNO','', 'JOBNO');
		getLocalStorageValue('obSumry_IssueWPrice_ITEM','', 'ITEM');
		getLocalStorageValue('obSumry_IssueWPrice_PRD_CLS_ID','', 'PRD_CLS_ID');
		getLocalStorageValue('obSumry_IssueWPrice_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		getLocalStorageValue('obSumry_IssueWPrice_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		getLocalStorageValue('obSumry_IssueWPrice_ORDERTYPE','','ORDERTYPE');
		getLocalStorageValue('obSumry_IssueWPrice_PICKSTATUS', '','PICKSTATUS');
		getLocalStorageValue('obSumry_IssueWPrice_ISSUESTATUS','', 'ISSUESTATUS');
		getLocalStorageValue('obSumry_IssueWPrice_EMPNAME', '','EMP_NAME');
		getLocalStorageValue('obSumry_IssueWPrice_INVOICENO','', 'INVOICENO');
		getLocalStorageValue('obSumry_IssueWPrice_LOC', '','LOC');
		getLocalStorageValue('obSumry_IssueWPrice_LOC_TYPE_ID', '','LOC_TYPE_ID');
		getLocalStorageValue('obSumry_IssueWPrice_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		getLocalStorageValue('obSumry_IssueWPrice_LOC_TYPE_ID3','', 'LOC_TYPE_ID3');*/
		
 
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
	/* INVOICENO Auto Suggestion */
	$('#INVOICENO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICENO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_INVOICENO_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.INVOICENO_MST);
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
			return '<div><p class="item-suggestion">'+data.INVOICENO+'</p></div>';
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
