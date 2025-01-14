<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@page import="java.text.DecimalFormat"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Order Summary Details By Average Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
    <jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

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
  var currencyId     = document.form1.CURRENCYID.value;
  var currencyDisplay= document.form1.CURRENCYDISPLAY.value;
  
  
  if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
  }

  
  document.form1.action="/track/deleveryorderservlet?Submit=ExportExcelOutboundOrderSummary";
  document.form1.submit();
}
//---End Modified by Deen on May 21 2014, Description:To open sales order summary  in excel powershell format
var postatus =   [{
    "year": "N",
    "value": "N",
    "tokens": [
      "DRAFT"
    ]
  },
  {
	    "year": "O",
	    "value": "O",
	    "tokens": [
	      "OPEN"
	    ]
	  },
	  {
		    "year": "C",
		    "value": "C",
		    "tokens": [
		      "PAID"
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
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

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
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",statusID="",EMP_NAME="",CURRENCYID="",CURRENCYDISPLAY="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",SORT="",CUSTOMERTYPE="";

DecimalFormat decformat = new DecimalFormat("#,##0.00");
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE")); 

boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportobordersumrywavgcost", plant,LOGIN_USER);
	displaySummaryExport=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportobordersumrywavgcost", plant,LOGIN_USER);
	displaySummaryExport=true;
}
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEMNO        = _strUtils.fString(request.getParameter("ITEM"));
DESC          = _strUtils.fString(request.getParameter("DESC"));
ORDERNO       = _strUtils.fString(request.getParameter("ORDERNO"));
CUSTOMER      = _strUtils.fString(request.getParameter("CUSTOMER"));
CUSTOMERID      = _strUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = _strUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS  = _strUtils.fString(request.getParameter("ISSUESTATUS"));
PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
statusID = _strUtils.fString(request.getParameter("STATUS_ID"));
SORT = _strUtils.fString(request.getParameter("SORT"));
EMP_NAME = _strUtils.fString(request.getParameter("EMP_NAME"));
CUSTOMERTYPE = _strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
CURRENCYID= _strUtils.fString(request.getParameter("CURRENCYID"));
CURRENCYDISPLAY= _strUtils.fString(request.getParameter("CURRENCYDISPLAY"));


if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ISS_WITH_AVGCOST";
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
%>

<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                                    <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" data-toggle="dropdown" >Export All Data<span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					   <!-- <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li>
					  </ul>
					  <%}%>
				
				</div>

				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="obSumry_IssueWAvgCost.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">

		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="Customer Name" name="CUSTOMER" >				
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>"  placeholder="Customer Type">
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="Order Number" >
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="Reference No">				
  		</div>
		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ITEMNO)%>"placeholder="PRODUCT" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CLASS">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT TYPE" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=SALES&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  
 <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(PICKSTATUS)%>" placeholder="Pick Status" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PICKSTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  		<div class="col-sm-4 ac-box">
  	
 <input type="text" name="ISSUESTATUS" id="ISSUESTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ISSUESTATUS)%>" placeholder="Issue Status" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ISSUESTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  
    	
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="Employee" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="CURRENCYDISPLAY" id="CURRENCYDISPLAY" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(CURRENCYDISPLAY)%>" placeholder="Currency" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CURRENCYDISPLAY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('currencylist.jsp?CURRENCYDISPLAY='+form1.CURRENCYDISPLAY.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		</div>
  		  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
	<label class="radio-inline"><INPUT name="SORT" type = "radio"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>By Customer</label>
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="PRODUCT"    <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>checked <%}%>>By Product</label>			
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
			
			<INPUT  name="DESC" type = "Hidden" value="<%=_strUtils.forHTMLTag(DESC)%>"> 
  	</div>
		</div>
		
  		</div>
    <INPUT name="CURRENCYID" type="hidden" value="<%=CURRENCYID%>" size="1"  MAXLENGTH=80>  
   
    
  	
  	  <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
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
  	   
	 <INPUT type="Hidden" name="DIRTYPE" value="OB_SUMMARY_ISS_WITH_AVGCOST">	
	
     
     
      <div id="VIEW_RESULT_HERE" class="table-responsive">
 <table id="tableInventorySummaryByCustomer"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">Order No</th>
		                	<th style="font-size: smaller;">Order Type</th>
		                	<th style="font-size: smaller;">Ref No</th>
		                	<th style="font-size: smaller;">Customer Name</th>
		                	<th style="font-size: smaller;">Customer Type</th>
		                	<th style="font-size: smaller;">Customer Status</th>
		                	<th style="font-size: smaller;">Remark1</th>
		                	<th style="font-size: smaller;">Remark2</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Issued Date</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">Order Qty</th>
		                	<th style="font-size: smaller;">Pick Qty</th>
		                	<th style="font-size: smaller;">Issue Qty</th>
		                	<th style="font-size: smaller;">Employee</th>
		                	<th style="font-size: smaller;">Avg Cost</th>
		                	<th style="font-size: smaller;">Tax%</th>
		                	<th style="font-size: smaller;">Recve Cost</th>
		                	<th style="font-size: smaller;">Tax</th>
		                	<th style="font-size: smaller;">Total</th>
		                			                	
		                </tr>
		            </thead>
				</table>
				<table id="tableInventorySummaryByProduct"
									class="table table-bordred table-striped" style="display:none;">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">Order No</th>
		                	<th style="font-size: smaller;">Order Type</th>
		                	<th style="font-size: smaller;">Ref No</th>
		                	<th style="font-size: smaller;">Customer Name</th>
		                	<th style="font-size: smaller;">Customer Type</th>
		                	<th style="font-size: smaller;">Customer Status</th>
		                	<th style="font-size: smaller;">Remark1</th>
		                	<th style="font-size: smaller;">Remark2</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Issued Date</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">Order Qty</th>
		                	<th style="font-size: smaller;">Pick Qty</th>
		                	<th style="font-size: smaller;">Issue Qty</th>
		                	<th style="font-size: smaller;">Employee</th>
		                	<th style="font-size: smaller;">Avg Cost</th>
		                	<th style="font-size: smaller;">Tax%</th>
		                	<th style="font-size: smaller;">Recve Cost</th>
		                	<th style="font-size: smaller;">Tax</th>
		                	<th style="font-size: smaller;">Total</th>
		                    </tr>
		            </thead>
				</table>
  </div>
  <div id="spinnerImg" ></div>
 
  </FORM>
 <SCRIPT LANGUAGE="JavaScript">
 var tableInventorySummaryByCustomer, tableInventorySummaryByProduct;
 var ITEMNO,ITEMDESC,FROM_DATE,TO_DATE,DIRTYPE,USER,ORDERNO,JOBNO,PICKSTATUS,ISSUESTATUS,ORDERTYPE,PRD_BRAND_ID,PRD_TYPE_ID,PRD_CLS_ID,
 STATUS_ID,EMPNO,CUSTOMERTYPE,SORT,currencyId,currencyDisplay;
 var groupRowColSpan = 18;
function getParameters(){
	return {
		"ITEM":ITEMNO,"PRD_DESCRIP":ITEMDESC,"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,
		"PICKSTATUS":PICKSTATUS,"ISSUESTATUS":ISSUESTATUS,"ORDERTYPE":ORDERTYPE,"PRD_BRAND_ID":PRD_BRAND_ID,"PRD_TYPE_ID":PRD_TYPE_ID,
		"PRD_CLS_ID":PRD_CLS_ID,"STATUS_ID":STATUS_ID,"SORT":SORT,"EMPNO":EMPNO,"CUSTOMERTYPE":CUSTOMERTYPE,"CURRENCYID":currencyId,
		"CURRENCYDISPLAY":currencyDisplay,
			
		"ACTION": "VIEW_OUTBOUND_SUMMARY_ISSUE_WITH_AVGCOST",
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
    PICKSTATUS     = document.form1.PICKSTATUS.value;
    ISSUESTATUS    = document.form1.ISSUESTATUS.value;
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
   PRD_BRAND_ID   = document.form1.PRD_BRAND_ID.value;
    PRD_TYPE_ID    = document.form1.PRD_TYPE_ID.value;
    PRD_CLS_ID     = document.form1.PRD_CLS_ID.value;
   STATUS_ID      = document.form1.STATUS_ID.value; 
    SORT           = document.form1.SORT.value;
    EMPNO          = document.form1.EMP_NAME.value;
    CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
    currencyId     = document.form1.CURRENCYID.value;
    currencyDisplay= document.form1.CURRENCYDISPLAY.value;
   
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
    
    if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
     }
   
   var urlStr = "../OutboundOrderHandlerServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = document.forms[0].SORT[0].checked ? 4 : 9;
   
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (document.forms[0].SORT[0].checked){
    	$('#tableInventorySummaryByProduct').css('display', 'none');
    	$('#tableInventorySummaryByProduct_wrapper').css('display', 'none');
    	$('#tableInventorySummaryByCustomer').css('display', 'block');
    	$('#tableInventorySummaryByCustomer_wrapper').css('display', 'block');
        if (tableInventorySummaryByCustomer){
        	tableInventorySummaryByCustomer.destroy();
        }
    	groupRowColSpan = 18;//	Total gray line OR Grand total gray line
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
    			{"data": 'qtyPick', "orderable": true},
    			{"data": 'qty', "orderable": true},
    			{"data": 'empname', "orderable": true},
    			{"data": 'avgcost', "orderable": true},
    			{"data": 'gstpercentage', "orderable": true},
    			{"data": 'issueAvgcost', "orderable": true},
    			{"data": 'tax', "orderable": true},    			
    			{"data": 'issAvgcostwTax', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [18,19,20]}],
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
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A2'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
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
	                		groupTotalRCostVal="0.00000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.00000";
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>); 
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.00000";
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
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.00000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.00000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.00000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.00000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.00000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.00000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
                    );
                }
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
		groupRowColSpan = 18;
	    tableInventorySummaryByProduct = $('#tableInventorySummaryByProduct').DataTable({
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
    			{"data": 'qtyPick', "orderable": true},
    			{"data": 'qty', "orderable": true},
    			{"data": 'empname', "orderable": true},
    			{"data": 'avgcost', "orderable": true},
    			{"data": 'gstpercentage', "orderable": true},
    			{"data": 'issueAvgcost', "orderable": true},
    			{"data": 'tax', "orderable": true},    			
    			{"data": 'issAvgcostwTax', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [18,19,20]}],
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
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A2'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
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
	                		groupTotalRCostVal="0.00000";
	                	}else{
	                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal="0.000";
	                	}
	                	else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotal==null || groupTotal==0){
	                		groupTotalVal="0.00000";
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
	                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(19)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(20)').html().replace(',', '').replace('$', ''));
	                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                total += parseFloat($(rows).eq( i ).find('td:eq(21)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	if(totalRCost==null || totalRCost==0){
	            		totalRCostVal="0.00000";
                	}else{
                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotalRCost==null || groupTotalRCost==0){
                		groupTotalRCostVal="0.00000";
                	}else{
                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
                	}
                	if(totalTax==null || totalTax==0){
                		totalTaxVal="0.00000";
                	}else{
                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
                	}
                	if(groupTotalTax==null || groupTotalTax==0){
                		groupTotalTaxVal="0.00000";
                	}else{
                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
                	}if(total==null || total==0){
                		totalVal="0.00000";
                	}else{
                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
                	}if(groupTotal==null || groupTotal==0){
                		groupTotalVal="0.00000";
                	}else{
                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
                	}
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + addZeroes(totalRCostVal) + '</td><td class="t-right">' + addZeroes(totalTaxVal) + '</td><td class="t-right">' + addZeroes(totalVal) + '</td><td></td><td></td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + addZeroes(groupTotalRCostVal) + '</td><td class="t-right">' + addZeroes(groupTotalTaxVal) + '</td><td class="t-right">' + addZeroes(groupTotalVal) + '</td><td></td><td></td></tr>'
                    );
                }
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
          '<TH><font color="#ffffff" align="left"><b>Ave Cost</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Recv Cost</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total</TH>'+ 
          '</tr>';
              
}
 
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
            
  </SCRIPT> 
 </div></div></div>
 
                 <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
 //onGo();
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
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p></div>';
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
	/* Product Type Number Auto Suggestion */
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
			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
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
	/* Product Type Number Auto Suggestion */
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
			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
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
	/* CURRENCYID Auto Suggestion */
	$('#CURRENCYDISPLAY').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCYID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CURRENCY_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CURRENCY_MST);
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
			return '<div><p class="item-suggestion">'+data.CURRENCYID+'</p></div>';
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
 </script>
 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

