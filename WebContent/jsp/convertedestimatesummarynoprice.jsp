<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Estimate Order Summary";
%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
 <jsp:param name="submenu" value="<%=IConstants.ESTIMATE_REPORTS%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<script>
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

  document.form1.action="/track/EstimateServlet?Submit=ExportExcelEstimateOrderSummaryWithOutPrice";
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
    
    //storeUserPreferences();

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form1.action="../salesestimate/salesestordersumdetail";
  document.form1.submit();
}

function storeUserPreferences(){
	   storeInLocalStorage('convertedestimatesummarynopirce_FROMDATE', $('#FROM_DATE').val());
		storeInLocalStorage('convertedestimatesummarynopirce_TODATE', $('#TO_DATE').val());
		storeInLocalStorage('convertedestimatesummarynopirce_CUSTOMER', $('#CUSTOMER').val());
		storeInLocalStorage('convertedestimatesummarynopirce_ORDERNO', $('#ORDERNO').val());
		storeInLocalStorage('convertedestimatesummarynopirce_JOBNO', $('#JOBNO').val());
		storeInLocalStorage('convertedestimatesummarynopirce_ITEM', $('#ITEM').val());
		storeInLocalStorage('convertedestimatesummarynopirce_PRD_CLS_ID', $('#PRD_CLS_ID').val());
		storeInLocalStorage('convertedestimatesummarynopirce_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
		storeInLocalStorage('convertedestimatesummarynopirce_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
		storeInLocalStorage('convertedestimatesummarynopirce_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
		storeInLocalStorage('convertedestimatesummarynopirce_STATUS',$('#STATUS').val());
		storeInLocalStorage('convertedestimatesummarynopirce_EMP_NAME',$('#EMP_NAME').val());
}


var postatus =   [{
    "year": "Send Later",
    "value": "Send Later",
    "tokens": [
      "DRAFT"
    ]
  },
  {
	    "year": "Pending",
	    "value": "Pending",
	    "tokens": [
	      "OPEN"
	    ]
	  },
	  {
		    "year": "Accepted",
		    "value": "Accepted",
		    "tokens": [
		      "OPEN"
		    ]
		  },
		  {
			    "year": "Rejected",
			    "value": "Rejected",
			    "tokens": [
			      "OPEN"
			    ]
			  },
			  {
				    "year": "Closed",
				    "value": "Closed",
				    "tokens": [
				      "OPEN"
				    ]
				  },
	  {
		    "year": "Confirm",
		    "value": "Confirm",
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
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();

session= request.getSession();


String FROM_DATE ="",  TO_DATE = "", STATUS="",DIRTYPE ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_DEPT_ID="",PRD_TYPE_ID="",PRD_CLS_ID="",EMP_NAME="",VIEWBY="";

PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String plant = (String)session.getAttribute("PLANT");
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportsalesestimatereports", plant,LOGIN_USER);
	displaySummaryExport = true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportsalesestimatereports", plant,LOGIN_USER);
	displaySummaryExport = true;
}

session.setAttribute("RFLAG","3");



if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
   String curDate =DateUtils.getDateMinusDays();//resvi
   if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
		curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;//resvi

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
STATUS    = StrUtils.fString(request.getParameter("STATUS"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
VIEWBY = StrUtils.fString(request.getParameter("VIEWBY"));
if(DIRTYPE.length()<=0){
DIRTYPE = "ESTIMATEISSUE";
}
if(VIEWBY.equals(""))
{
	VIEWBY="ViewAll";
}


if(PGaction.equalsIgnoreCase("View")){
 
 try{
        //Hashtable ht = new Hashtable();
      
        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
        if(StrUtils.fString(ORDERNO).length() > 0)        ht.put("B.ESTNO",ORDERNO);
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
        if(StrUtils.fString(STATUS).length() > 0) ht.put("A.STATUS",STATUS);
        if(StrUtils.fString(EMP_NAME).length() > 0) ht.put("A.EMPNO",EMP_NAME);
             

       //movQryList = movHisUtil.getEstimateOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,VIEWBY);
        
		//if(movQryList.size()<=0)
			//cntRec ="true";



 }catch(Exception e) { }
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
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
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Estimate Order Summary</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                                   <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();"  >Export All Data </button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul>	 -->
					   <%} %>				
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="convertedestimatesummarynoprice.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

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
  				
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="Order Number" >
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="Reference No">
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ITEMNO)%>"placeholder="PRODUCT" >
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>"placeholder="PRODUCT DEPARTMENT">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CATEGORY">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		
  	<%-- 	  		     	<SELECT class="form-control" NAME ="STATUS" size="1">
      	<OPTION style="display:none;"  value="">Status</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='Send Later' <%if(STATUS.equalsIgnoreCase("Send Later")){ %>selected<%} %>>Send Later </OPTION>
     					<OPTION   value='Pending' <%if(STATUS.equalsIgnoreCase("Pending")){ %>selected<%} %>>Pending </OPTION>
     					<OPTION   value='Accepted' <%if(STATUS.equalsIgnoreCase("Accepted")){ %>selected<%} %>>Accepted </OPTION>
                      	<OPTION   value='Rejected' <%if(STATUS.equalsIgnoreCase("Rejected")){ %>selected<%} %>>Rejected </OPTION>
                       	<OPTION   value='Closed' <%if(STATUS.equalsIgnoreCase("Closed")){ %>selected<%} %>>Closed</OPTION>
                       	<OPTION   value='Confirm' <%if(STATUS.equalsIgnoreCase("Confirm")){ %>selected<%} %>>Confirm</OPTION>
                           </SELECT> --%>
 <input type="text" name="STATUS" id="STATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(STATUS)%>" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'STATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		
  		</div>
  		</div>
  		 		
    	<INPUT type="Hidden" name="DIRTYPE" value="ESTIMATEISSUE">
    	<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>">
  	

  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		
  	</div>
  <div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="Employee" >
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  			<div class="col-sm-4 ac-box">
  	<label class="radio-inline"><INPUT name="VIEWBY" type = "radio"  value="ByExpiryDate"  id="ByExpiryDate" <%if(VIEWBY.equalsIgnoreCase("ByExpiryDate")) {%>checked <%}%>>By Expiry Date</label>
 		<label class="radio-inline"><INPUT  name="VIEWBY" type = "radio" value="ViewAll"  id = "ViewAll" <%if(VIEWBY.equalsIgnoreCase("ViewAll")) {%>checked <%}%>>View All</label>
  	</div>
  	</div>
  	 	 <div class="col-sm-4 ac-box">
  		<div class=""> 
			<div class="col-sm-4 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				</div>
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
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RE');}"> <b>Back</b></button> -->
  	  </div>
         </div>
       	   </div>
  
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
         <th style="font-size: smaller;">S/N</TH>
         <th style="font-size: smaller;">Order No</TH>
         <th style="font-size: smaller;">Ref No</TH>
         <th style="font-size: smaller;">Sales No</TH>
         <th style="font-size: smaller;">Customer Name</TH>
         <th style="font-size: smaller;">Remarks</TH>
         <th style="font-size: smaller;">Product ID</TH>
         <th style="font-size: smaller;">Description</TH>
         <th style="font-size: smaller;">Issue Date</TH>
         <th style="font-size: smaller;">UOM</TH>
         <th style="font-size: smaller;">Order Qty</TH>
         <th style="font-size: smaller;">Converted Qty</TH>
         <th style="font-size: smaller;">Employee</TH>
         <th style="font-size: smaller;">Expiry Date</TH>
         <th style="font-size: smaller;">Status</TH>
         <th style="font-size: smaller;">Sales Order Status</TH>
         <th style="font-size: smaller;">User</TH>
                  
                     </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th style="text-align: left !important">Total</th>
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
  <script>
  var tableData = [];
		  <%
            movQryList = movHisUtil.getEstimateOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,VIEWBY);
	        
			if(movQryList.size()<=0)
				cntRec ="true";          
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
           String COLORCODE = StrUtils.fString((String)lineArr.get("COLORCODE"));
           
           String unitPriceValue = (String)lineArr.get("unitprice");
           String qtyOrValue = (String)lineArr.get("qtyor");
           String qtyIssueValue = (String)lineArr.get("qtyis");
           
           float unitPriceVal="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);
           float qtyOrVal="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
           float qtyIssueVal="".equals(qtyIssueValue) ? 0.0f :  Float.parseFloat(qtyIssueValue);
           
           if(unitPriceVal==0f){
        	   unitPriceValue="0.00000";
        	}else{
        		unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
        	}if(qtyOrVal==0f){
        		qtyOrValue="0.000";
         	}else{
         		qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
         	}if(qtyIssueVal==0f){
         		qtyIssueValue="0.000";
         	}else{
         		qtyIssueValue=qtyIssueValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
         	}
         	unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
         	qtyOrValue = StrUtils.addZeroes(qtyOrVal, numberOfDecimal);
         	qtyIssueValue = StrUtils.addZeroes(qtyIssueVal, numberOfDecimal);
           
       %>
       
       var rowData = [];
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=iIndex%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("estno")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("jobNum")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("dono")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("custname")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("remarks")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("item") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("itemdesc")%></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("TRANDATE") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("UOM") %></font>';
       rowData[rowData.length] = '<%=qtyOrValue%>';
       rowData[rowData.length] = '<%=qtyIssueValue%>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("empname") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("expiredate") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("status") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("dostatus") %></font>';
       rowData[rowData.length] = '<font color="<%=COLORCODE%>"><%=(String)lineArr.get("users") %></font>';
       tableData[tableData.length] = rowData;
       <%}%>
       var groupColumn = 1 ;
       $(document).ready(function(){
    	   
    	   /*if (document.form1.FROM_DATE.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_FROMDATE', '','FROM_DATE');
          }
    	   if (document.form1.TO_DATE.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_TODATE', '','TO_DATE');
          } 
    	   if (document.form1.CUSTOMER.value == ''){
    			getLocalStorageValue('convertedestimatesummarynopirce_CUSTOMER', '','CUSTOMER');
          } 	
    	   if (document.form1.ORDERNO.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_ORDERNO','','ORDERNO');
          } 
    	   if (document.form1.JOBNO.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_JOBNO','', 'JOBNO');
          } 
    	   if (document.form1.ITEM.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_ITEM','', 'ITEM');
          } 	
    	   if (document.form1.PRD_CLS_ID.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_PRD_CLS_ID','', 'PRD_CLS_ID');
          } 	
    	   if (document.form1.PRD_TYPE_ID.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_PRD_TYPE_ID','', 'PRD_TYPE_ID');
          }	
    	   if (document.form1.PRD_BRAND_ID.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_PRD_BRAND_ID','', 'PRD_BRAND_ID');
          }	
    	   if (document.form1.STATUS.value == ''){
    			getLocalStorageValue('convertedestimatesummarynopirce_STATUS','','STATUS');
    		}	
    	   if (document.form1.EMP_NAME.value == ''){
    		   getLocalStorageValue('convertedestimatesummarynopirce_EMP_NAME','','EMP_NAME');
    		}
    	   storeUserPreferences();*/
    	   $('#tableIssueSummary').DataTable({
    		   "lengthMenu": [[50, 100, 500], [50, 100, 500]],
//     		   "lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
    			  	data: tableData,
    			  	"columnDefs": [{"className": "t-right", "targets": [10,11]}],
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
    	                   columns: ':not(:eq(9)):not(:eq(10)):not(:eq(11))'
    	               }
    		        ], 
    		   	       "order": [],
    			        "drawCallback": function( settings ) {
    			        	<%if(!displaySummaryExport){ %>
    			        	$('.buttons-collection')[0].style.display = 'none';
    			        	<% } %>
    			  	        	this.attr('width', '100%');
    								var groupColumn = 0;
    								var groupRowColSpan= 9;
    							   	var api = this.api();
    					            var rows = api.rows( {page:'current'} ).nodes();
    					            var last=null;
    					            var totalPickQty = 0;
    					            var totalIssueQty = 0;
    					            var totalReceiveQty = 0;
    					            var groupEnd = 0;
    					            var currentRow = 0;
    					            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
    					                if ( last !== group ) {
    					                    last = group;
    					                    groupEnd = i;
    					                   // groupTotalPickQty = 0;
    					                   // groupTotalIssueQty = 0;
    					                   // groupTotalReceiveQty = 0;
    					                    
    					                }
    					                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', ''));
    					                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
    					                   
    					                
    					                currentRow = i;
    					            } );
    					            if (groupEnd > 0 || rows.length == (currentRow + 1)){
    					            	$(rows).eq( currentRow ).after(
    					                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(totalPickQty).toFixed(3) + '</td><td>' + parseFloat(totalIssueQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
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
    			    	              .column(10)
    			    	              .data()
    			    	              .reduce(function(a, b) {
    			    	                return intVal(a) + intVal(b);
    			    	              }, 0);

    			    	            // Total over this page
    			    	            totalOrdVal = api
    			    	              .column(10)
    			    	              .data()
    			    	              .reduce(function(a, b) {
    			    	                return intVal(a) + intVal(b);
    			    	              }, 0);
    			    	              
    			    	            totalPicVal = api
    			    	              .column(11)
    			    	              .data()
    			    	              .reduce(function(a, b) {
    			    	                return intVal(a) + intVal(b);
    			    	              }, 0);

    			    	            // Update footer
    			    	            $(api.column(10).footer()).html(parseFloat(totalOrdVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    			    	            $(api.column(11).footer()).html(parseFloat(totalPicVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    			    	          }
    			  });	 
    	});       

  
  </script>
 
</FORM>
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
				ACTION : "GET_CUSTOMER_DATA",
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
// 		    return '<p onclick="getvendname(\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
		    return '<div onclick="getvendname(\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ESTNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/EstimateServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				ESTNO : query
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
		    return '<p>' + data.ESTNO + '</p>';
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
 </script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>