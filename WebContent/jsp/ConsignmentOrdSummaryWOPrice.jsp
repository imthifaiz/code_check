<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%@ include file="header.jsp"%>
<%
String title = "Consignment Summay";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT%>"/>
</jsp:include>

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell form
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
 // document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="/track/TransferOrderServlet?Submit=ExportExcelConsignmentWOOrderSummary";
  document.form1.submit();
 }
//---End Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell form
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
  document.form1.action="/track/TransferOrderServlet?Submit=ExportExcelConsignmentSummaryWithRemarks";
  document.form1.submit();
 }
function onGo(){

   var flag    = "false";
  
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   var PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
   var STATUS_ID      = document.form1.STATUS_ID.value;
   
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
    
    //storeUserPreferences();
    
    document.form1.action="../consignment/consignsummary";
    document.form1.submit();
}

function storeUserPreferences(){
	   storeInLocalStorage('ConsignmentOrdSummaryWOPrice_FROMDATE', $('#FROM_DATE').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_TODATE', $('#TO_DATE').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_CUSTOMER', $('#CUSTOMER').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_CUSTOMER_TYPE_ID', $('#CUSTOMER_TYPE_ID').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_ORDERNO', $('#ORDERNO').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_FROMWAREHOUSE', $('#FROMWAREHOUSE').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_TOWAREHOUSE', $('#TOWAREHOUSE').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_JOBNO', $('#JOBNO').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_ITEM', $('#ITEM').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_PRD_CLS_ID', $('#PRD_CLS_ID').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_ORDERTYPE', $('#ORDERTYPE').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_PICKSTATUS',$('#PICKSTATUS').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_ISSUESTATUS',$('#ISSUESTATUS').val());
		storeInLocalStorage('ConsignmentOrdSummaryWOPrice_EMP_NAME',$('#EMP_NAME').val());
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
		    "year": "ISSUED",
		    "value": "ISSUED",
		    "tokens": [
		      "ISSUED"
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
CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();

session= request.getSession();


String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",FROMWAREHOUSE="",TOWAREHOUSE="",ORDERNO="",CUSTOMER="",UOM="",PGaction="";
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_DEPT_ID="",PRD_CLS_ID="",statusID="",EMP_NAME="",CUSTOMERTYPE="",customerstatusid="",
customertypeid="",customertypedesc="",picksts="",issuests="";

PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String plant = (String)session.getAttribute("PLANT");
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));
session.setAttribute("RFLAG","4");

String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryExport=false,displaySummaryLink=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkprintOB", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportforconsignment", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkprintOB", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportforconsignment", plant,USERID);
}

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
FROMWAREHOUSE       = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));
TOWAREHOUSE       = StrUtils.fString(request.getParameter("TOWAREHOUSE"));
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
//Start code added by deen for product brand,type on 2/sep/13
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
//End code added by deen for product brand,type on 2/sep/13
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//imti start
if(PICKSTATUS.equalsIgnoreCase("OPEN"))
	PICKSTATUS="N";
else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
	PICKSTATUS="O";
else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
	PICKSTATUS="C";
//imti end
//imti start
if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
	ISSUESTATUS="N";
else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
	ISSUESTATUS="O";
else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
	ISSUESTATUS="C";
//imti end
if(DIRTYPE.length()<=0){
DIRTYPE = "CONSIGNMENT";
}

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        //Hashtable ht = new Hashtable();
  
       
      
       
       
        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
        if(StrUtils.fString(ORDERNO).length() > 0)        ht.put("B.TONO",ORDERNO);
        //if(StrUtils.fString(CUSTOMER).length() > 0)        ht.put("A.CUSTNAME",CUSTOMER);
        if(StrUtils.fString(CUSTOMERID).length() > 0)        ht.put("A.CUSTCODE",CUSTOMERID);
        if(StrUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if (StrUtils.fString(FROMWAREHOUSE).length() > 0)
        	ht.put("FROMWAREHOUSE", FROMWAREHOUSE);

        	if (StrUtils.fString(TOWAREHOUSE).length() > 0)
        	ht.put("TOWAREHOUSE", TOWAREHOUSE);
        if(StrUtils.fString(ISSUESTATUS).length() > 0)    {
        	if(ISSUESTATUS.equalsIgnoreCase("DRAFT"))
        	{
        	ht.put("ORDER_STATUS","Draft");
        	ht.put("B.LNSTAT","N");
        	}
        	else
        	{
        		ht.put("B.LNSTAT",ISSUESTATUS);

        	if(ISSUESTATUS.equalsIgnoreCase("N"))
        	ht.put("ORDER_STATUS","OPEN");
        	}
        	}   
        
        if(StrUtils.fString(PICKSTATUS).length() > 0) {
        	if(PICKSTATUS.equalsIgnoreCase("DRAFT"))
        	{
        	ht.put("ORDER_STATUS","Draft");
        	ht.put("B.PICKSTATUS","N");
        	}
        	else
        	{
        	ht.put("B.PICKSTATUS",PICKSTATUS);

        	if(PICKSTATUS.equalsIgnoreCase("N"))
        	ht.put("ORDER_STATUS","OPEN");
        	}
        	}
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
        if(StrUtils.fString(EMP_NAME).length() > 0) ht.put("A.EMPNO",EMP_NAME);
        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
      
       
    

       //movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,"");
        
		//if(movQryList.size()<=0)
			//cntRec ="true";



 }catch(Exception e) { }
}

String collectionDate=DateUtils.getDate();
ArrayList al = plantMstDAO.getPlantMstDetails(plant);
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

%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Consignment Summary</label></li>                                   
            </l>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                          <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <% if (displaySummaryExport) { %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data </button>
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
<FORM class="form-horizontal" name="form1" method="post" action="ConsignmentOrdSummaryWOPrice.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
 
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="Customer Name" name="CUSTOMER" >				
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>"  placeholder="Customer Group">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="Order Number" >
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		
  		  	<div class="col-sm-4 ac-box">
  		  	  	<div class="">   		
			<input type="text" class="ac-selected form-control" id="FROMWAREHOUSE" name="FROMWAREHOUSE"value="<%=StrUtils.forHTMLTag(FROMWAREHOUSE)%>" placeholder="From Location" >
			 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'FROMWAREHOUSE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					<!-- <span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.FROMWAREHOUSE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
					</div>
					</div>
			
				<div class="col-sm-4 ac-box">
					<div class="">     		
			<input type="text" class="ac-selected form-control" id="TOWAREHOUSE" name="TOWAREHOUSE"value="<%=StrUtils.forHTMLTag(TOWAREHOUSE)%>" placeholder="To Location" >
			 <span class="select-icon"   onclick="$(this).parent().find('input[name=\'TOWAREHOUSE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
						<!-- <span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.TOWAREHOUSE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
						</div>
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
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CATEGORY">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" >
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/displayOrderType.jsp?OTYPE=SALES&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
 <%--  		     	<SELECT class="form-control" NAME ="PICKSTATUS" size="1">
      	<OPTION style="display:none;"  value="">PickStatus</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
        <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(picksts)%>" placeholder="Pick Status" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PICKSTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		
  <%--  <label class="radio-inline" ><INPUT name="VIEWSTATUS" type = "hidden"  value="ByOrderDate"  id="ByOrderDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByOrderDate")) {%>checked <%}%>></label>
    	<label class="radio-inline"><INPUT  name="VIEWSTATUS" type = "hidden" value="ByDeliveryDate"  id = "ByDeliveryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByDeliveryDate")) {%>checked <%}%>></label> --%>
    	<INPUT type="Hidden" name="DIRTYPE" value="CONSIGNMENT"> 
    	<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>">
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		     	<%-- <SELECT class="form-control" NAME ="ISSUESTATUS" size="1">
      	<OPTION style="display:none;"  value="">IssueStatus</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
  <input type="text" name="ISSUESTATUS" id="ISSUESTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(issuests)%>" placeholder="Issue Status" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ISSUESTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>                         
                           
  		</div>	
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="Employee" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--       <div class="ShowSingle">
      <div class="col-sm-offset-6">
      <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReportWithRemarks();"><b>Export With Product Remarks</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	 </div>
        </div> -->
       	  </div> 
  		
  		<INPUT type="Hidden" name="DIRTYPE" value="CONSIGNMENT">      
     
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">Order No</th>
		                	<th style="font-size: smaller;">Order Type</th>
		                	<th style="font-size: smaller;">Ref No</th>
		                	<th style="font-size: smaller;">Customer Name</th>
		                	<th style="font-size: smaller;">Customer Group</th>
		            <!--     	<th style="font-size: smaller;">Customer Status</th> -->
		                	<th style="font-size: smaller;">From Location</th>
		                	<th style="font-size: smaller;">To Location</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Detail Desc</th>
		                	<th style="font-size: smaller;">Unit Price</th>
		                	<th style="font-size: smaller;">Order Date</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">Order Qty</th>
		                	<th style="font-size: smaller;">Pick Qty</th>
		                	<th style="font-size: smaller;">Issue Qty</th>
		                	<th style="font-size: smaller;">Invoiced Qty</th>
		                	<th style="font-size: smaller;">Employee</th>
		                	<th style="font-size: smaller;">Pick Status</th>
		                	<th style="font-size: smaller;">Issue Status</th>
		                	<th style="font-size: smaller;">User</th>
		                   <th style="font-size: smaller;">Remarks</th>
		                 </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='13'></th>
		            <th style="text-align: left !important">Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th><th></th><th></th><th></th><th></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
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
  <script>
  var tableData = [];
 
       <%
	   movQryList = movHisUtil.getConsignmentWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,"",UOM);
       
		if(movQryList.size()<=0)
			cntRec ="true";
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
                 
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
 				
               
               
               
             customertypeid = customerBeanDAO.getCustomerTypeId(plant,(String)lineArr.get("custname"));
   			 if(customertypeid == null ||  customertypeid.equals(null) ||customertypeid.equals("")|| customertypeid.equals("NOCUSTOMERTYPE"))
   			 {
   				customertypedesc="";
   			 }
   			 else
   			 {
   				customertypedesc = customerBeanDAO.getCustomerTypeDesc(plant,customertypeid);
   			 }
   			 
  
   		//imti start		 
 			String lnstat= (String)lineArr.get("lnstat");
 			String ordstat= (String)lineArr.get("ORDER_STATUS");
			if(lnstat.equalsIgnoreCase("N"))
			{
				lnstat="OPEN";
				if(ordstat.equalsIgnoreCase("Draft"))
					lnstat="DRAFT";				
			}
 		       else if(lnstat.equalsIgnoreCase("O"))
 		    	   lnstat="PARTIALLY ISSUED";
 		       else if(lnstat.equalsIgnoreCase("C"))
 		    	   lnstat="ISSUED";
 //imti end
 
   	   		//imti start		 
  			String pickstatus= (String)lineArr.get("pickstatus");
  			String ordstats= (String)lineArr.get("ORDER_STATUS");
			if(pickstatus.equalsIgnoreCase("N"))
			{
				pickstatus="OPEN";
				if(ordstats.equalsIgnoreCase("Draft"))
					pickstatus="DRAFT";				
			}
  		       else if(pickstatus.equalsIgnoreCase("O"))
  		    	 pickstatus="PARTIALLY PICKED";
  		       else if(pickstatus.equalsIgnoreCase("C"))
  		   	  pickstatus="PICKED";
  //imti end
   			 
   			String sUnitPrice= (String)lineArr.get("unitprice");
   			double dUnitPrice ="".equals(sUnitPrice) ? 0.0d :  Double.parseDouble(sUnitPrice);
   			sUnitPrice = StrUtils.addZeroes(dUnitPrice, numberOfDecimal);
          
       %>
       var rowData = [];
       rowData[rowData.length] = '<%=iIndex%>';
       <%-- rowData[rowData.length] = '<a href="/track/TransferOrderServlet?TONO=<%=(String)lineArr.get("tono")%>&Submit=View&RFLAG=4"><%=(String)lineArr.get("tono")%></a>'; --%>
       <%if(displaySummaryLink){ %>
       rowData[rowData.length] = '<a href="../consignment/detail?tono=<%=(String)lineArr.get("tono")%>"><%=(String)lineArr.get("tono")%></a>';
       <% }else { %>
       rowData[rowData.length] = '<%=(String)lineArr.get("tono")%>';
       <% } %>
       rowData[rowData.length] = '<%=(String)lineArr.get("ordertype")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("jobNum")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
       rowData[rowData.length] = '<%=customertypedesc%>';
<%--        rowData[rowData.length] = '<%=customerstatusdesc%>'; --%>
       rowData[rowData.length] = '<%=(String)lineArr.get("fromwarehouse") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("towarehouse") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("item") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("itemdesc") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("DetailItemDesc") %>';
       rowData[rowData.length] = '<%=sUnitPrice%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("TRANDATE") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("UOM") %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qtyor")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qtyPick")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qty")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qtyac")) %>'; 
       rowData[rowData.length] = '<%=(String)lineArr.get("empname") %>';
       rowData[rowData.length] = '<%=pickstatus%>';
       rowData[rowData.length] = '<%=lnstat%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("users") %>';
        <%-- rowData[rowData.length] = '<a href="#" style="text-decoration: none;" onClick="javascript:popUpWin(\'ConsignmentOrderPrdRemarksList.jsp?REMARKS1=<%=(String)lineArr.get("remarks")%>&REMARKS2=<%=(String)lineArr.get("remarks3")%>&TONO=<%=(String)lineArr.get("tono")%>&ITEM=<%=(String)lineArr.get("item")%>&TOLNNO=<%=(String)lineArr.get("tolnno")%>\');">&#9432;</a>'; --%>
        <%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'<%=(String)lineArr.get("remarks2")%>\',\'<%=(String)lineArr.get("remarks3")%>\',\'<%=(String)lineArr.get("tono")%>\',\'<%=(String)lineArr.get("item")%>\',\'<%=(String)lineArr.get("tolnno")%>\');"></a> '; --%>
        rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\'<%=(String)lineArr.get("tono")%>\',\'<%=(String)lineArr.get("item")%>\',\'<%=(String)lineArr.get("tolnno")%>\');"></a> ';
       tableData[tableData.length] = rowData;
     <%}%>
     var groupColumn = 1; 
  $(document).ready(function(){
	  /*if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_FROMDATE', '','FROM_DATE');
     }
	   if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_TODATE', '','TO_DATE');
     } 
	   if (document.form1.CUSTOMER.value == ''){
			getLocalStorageValue('ConsignmentOrdSummaryWOPrice_CUSTOMER', '','CUSTOMER');
     } 	
	   if (document.form1.CUSTOMER_TYPE_ID.value == ''){
			getLocalStorageValue('ConsignmentOrdSummaryWOPrice_CUSTOMER_TYPE_ID', '','CUSTOMER_TYPE_ID');
    } 
	   if (document.form1.ORDERNO.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_ORDERNO','','ORDERNO');
     } 
	   if (document.form1.FROMWAREHOUSE.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_FROMWAREHOUSE','','FROMWAREHOUSE');
     } 
	   if (document.form1.TOWAREHOUSE.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_TOWAREHOUSE','','TOWAREHOUSE');
     } 
	   if (document.form1.JOBNO.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_JOBNO','', 'JOBNO');
     } 
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_ITEM','', 'ITEM');
     } 	
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_PRD_CLS_ID','', 'PRD_CLS_ID');
     } 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_PRD_TYPE_ID','', 'PRD_TYPE_ID');
     }	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_PRD_BRAND_ID','', 'PRD_BRAND_ID');
     }	
	   if (document.form1.ORDERTYPE.value == ''){
			getLocalStorageValue('ConsignmentOrdSummaryWOPrice_ORDERTYPE','','ORDERTYPE');
		}
	   if (document.form1.PICKSTATUS.value == ''){
			getLocalStorageValue('ConsignmentOrdSummaryWOPrice_PICKSTATUS','','PICKSTATUS');
		}
	   if (document.form1.ISSUESTATUS.value == ''){
			getLocalStorageValue('ConsignmentOrdSummaryWOPrice_ISSUESTATUS','','ISSUESTATUS');
		}
	   if (document.form1.EMP_NAME.value == ''){
		   getLocalStorageValue('ConsignmentOrdSummaryWOPrice_EMP_NAME','','EMP_NAME');
		}
	   storeUserPreferences();*/
 	 $('#tableMovementHistory').DataTable({
 		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
//  		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
 		  	data: tableData,
 		  	 "columnDefs": [{"className": "t-right", "targets": [0]}], 
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
 	            	columns: ':not(:eq('+groupColumn+')):not(:eq(6)):not(:eq(7)):not(:eq(11)):not(:eq(12))'
                 }
 	        ],
 	       "drawCallback": function ( settings ) {
 	        	this.attr('width', '100%');
					var groupColumn = 0;
					var groupRowColSpan= 13;
				   	var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalPickQty = 0;
		            var totalIssueQty = 0;
		            var totalReceiveQty = 0;
		            var totalinvoiceQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                    last = group;
		                    groupEnd = i;
		                   // groupTotalPickQty = 0;
		                    //groupTotalIssueQty = 0;
		                   // groupTotalReceiveQty = 0;
		                    
		                }
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', ''));
		                totalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', ''));
		                totalinvoiceQty += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', ''));
		                   
		                
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(totalPickQty).toFixed(3) + '</td><td>' + parseFloat(totalIssueQty).toFixed(3) + '</td><td>' + parseFloat(totalReceiveQty).toFixed(3) + '</td><td>' + parseFloat(totalinvoiceQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
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
   	              .column(14)
   	              .data()
   	              .reduce(function(a, b) {
   	                return intVal(a) + intVal(b);
   	              }, 0);

   	            // Total over this page
   	            totalOrdVal = api
   	              .column(15)
   	              .data()
   	              .reduce(function(a, b) {
   	                return intVal(a) + intVal(b);
   	              }, 0);
   	              
   	            totalPicVal = api
   	              .column(16)
   	              .data()
   	              .reduce(function(a, b) {
   	                return intVal(a) + intVal(b);
   	              }, 0);

   	            totalIseVal = api
 	              .column(17)
 	              .data()
 	              .reduce(function(a, b) {
 	                return intVal(a) + intVal(b);
 	              }, 0);
   	              
   	            // Update footer
   	            $(api.column(14).footer()).html(parseFloat(total).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
   	            $(api.column(15).footer()).html(parseFloat(totalOrdVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
   	            $(api.column(16).footer()).html(parseFloat(totalPicVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
   	            $(api.column(17).footer()).html(parseFloat(totalIseVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
   	          }
 		  });	 
  });
  
  
  
   </script>
      
  </FORM>
  
  <script>
  autosize(document.querySelectorAll('textarea'));
  
  </script>
 </div></div></div> 
 
                 <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
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

	/* location Auto Suggestion */
	$('#FROMWAREHOUSE').typeahead({
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
	$('#TOWAREHOUSE').typeahead({
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
	
	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/TransferOrderServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NUM_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				TONO : query
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
		    return '<p>' + data.TONO + '</p>';
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
		  name: 'postatuss',
		  display: 'value',  
		  source: substringMatcher(postatuss),
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
 function loadLTPDetails(remarks,remarks3,tono,item,tolnno){
		
	 var urlStr = "/track/OutboundOrderHandlerServlet";
	 var plant= '<%=plant%>';
		$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT :  plant,
			ACTION : "getConsignmentRemarksDetails",
			REMARKS1 : remarks,
			REMARKS2 : remarks3,
			TONO : tono,
			ITEM :  item,						
			TOLNNO : tolnno
			
			
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