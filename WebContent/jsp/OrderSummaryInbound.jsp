<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Purchase Order Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Summary - Inbound Order Details', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
function onGo(){

	//storeUserPreferences();
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
  // var STATUS_ID      = document.form1.STATUS_ID.value;
   
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
   // if(STATUS_ID != null     && STATUS_ID != "") { flag = true;}

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}
	
document.form1.action="../purchaseorder/summarydetails";
//   document.form1.action="OrderSummaryInbound.jsp";
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

function escapeHtml(text) {
	  var map = {
	    '&': '&amp;',
	    '<': '&lt;',
	    '>': '&gt;',
	    '"': '&quot;',
	    "'": '&#039;'
	  };
	   return text.replace(/[&<>"']/g, function(m) { return map[m]; });
	}
			  
</script>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script src="js/calendar.js"></script>
<script src="js/general.js"></script>
<script src="https://cdn.jsdelivr.net/npm/autosize@4.0.0/dist/autosize.min.js"></script>

<%
CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();

PlantMstDAO plantMstDAO = new PlantMstDAO();
session= request.getSession();
String plant = (String)session.getAttribute("PLANT");
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees

String FROM_DATE ="",  TO_DATE = "",status="", DIRTYPE ="",ORDERTYPE="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String PRD_BRAND_ID = "",PRD_DEPT_ID="",PRD_TYPE_ID="",PRD_CLS_ID="",statusID="",VIEWSTATUS="",SUPPLIERTYPE="",suppliertypeid="",suppliertypedesc="",UOM="",newstatus="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";

FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
// RESVI STARTS
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryLink=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkibsummary", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkibsummary", plant,USERID);
}
// RESVI ENDS

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
status = StrUtils.fString(request.getParameter("STATUS"));
//imti start
newstatus = status;
//imti end

//Start code added by deen for product brand,type on 2/sep/13
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
//End code added by deen for product brand,type on 2/sep/13 
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
SUPPLIERTYPE = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
//imti start
if(status.equalsIgnoreCase("OPEN"))
	status="N";
else if(status.equalsIgnoreCase("PARTIALLY RECEIVED"))
	status="O";
else if(status.equalsIgnoreCase("RECEIVED"))
	status="C";
//imti end

if(DIRTYPE.length()<=0){
DIRTYPE = "INBOUND";
}

if(VIEWSTATUS.equals(""))
{
	VIEWSTATUS="ByOrderDate";
}
if(PGaction.equalsIgnoreCase("View")){
 
 try{
        //Hashtable ht = new Hashtable();
       // if(StrUtils.fString(FROM_DATE).length() > 0)   ht.put(IConstants.CREATED_AT,FROM_DATE);
       // if(StrUtils.fString(TO_DATE).length() > 0)     ht.put(IConstants.CREATED_AT,TO_DATE);
       
       if(DIRTYPE.equalsIgnoreCase("INBOUND"))
       {
        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
        if(StrUtils.fString(ORDERNO).length() > 0)        ht.put("B.PONO",ORDERNO);
        if(StrUtils.fString(status).length() > 0) { 
        	if(status.equalsIgnoreCase("DRAFT"))
        	{
        		ht.put("ORDER_STATUS","DRAFT");
        		ht.put("B.LNSTAT","N");	
        	}
        	else
        	{
        	   ht.put("B.LNSTAT",status);
        	   
        	   if(status.equalsIgnoreCase("N"))
           		ht.put("ORDER_STATUS","OPEN");
        	}        	
        }
        if(StrUtils.fString(ORDERTYPE).length() > 0) ht.put("A.ORDERTYPE",ORDERTYPE);
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
		if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);
       }
       else
       {
        if(StrUtils.fString(JOBNO).length() > 0)             ht.put("A.JOBNUM",JOBNO);
        if(StrUtils.fString(ITEMNO).length() > 0)            ht.put("B.ITEM",ITEMNO);
        if(StrUtils.fString(ORDERNO).length() > 0)           ht.put("B.DONO",ORDERNO);
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
        if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);
       }
       

       //movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant, DESC,CUSTOMER,VIEWSTATUS);
        
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
              <!-- <div class="col-sm-3"> -->
      				<ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                        <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                        <li><label> Purchase Order Summary</label></li>                                   
                     </ol>   
              <!-- </div>  -->     	                  
      	    <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
               
                  <div class="btn-group" role="group">
				  <!-- <button type="button" class="btn btn-default" data-toggle="dropdown" >Export All Data<span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
					   <!-- <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
					   <!-- <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li>
					  </ul> -->
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();">Export All Data</button>
					  
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
		<FORM class="form-horizontal" name="form1" method="post" action="OrderSummaryInbound.jsp" >
		<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<input type="hidden" name="SUPPLIER_TYPE_DESC" value="">
		<div id="CHK1" ></div>
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" id="TO_DATE" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="SUPPLIER" name="CUSTOMER" >				
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
  		<input type="text" class="ac-selected form-control" id="SUPPLIER_TYPE_ID" name="SUPPLIER_TYPE_ID" value="<%=StrUtils.forHTMLTag(SUPPLIERTYPE)%>"  placeholder="SUPPLIER GROUP">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CATEGORY">
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
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
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
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		 <%--     	<SELECT class="form-control" NAME ="STATUS" size="1">
      	<OPTION style="display:none;"  value="">Status</OPTION>
           <!--  <OPTION selected  value=""> </OPTION> -->
     		<OPTION   value='N' <%if(status.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
     		<OPTION   value='O' <%if(status.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(status.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
                           </SELECT> --%>
<!--         imti  change the value name what u declare in the string -->
        <input type="text" name="STATUS" id="STATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(newstatus)%>" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'STATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
   <label class="radio-inline" ><INPUT name="VIEWSTATUS" type = "hidden"  value="ByOrderDate"  id="ByOrderDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByOrderDate")) {%>checked <%}%>></label>
    	<label class="radio-inline"><INPUT  name="VIEWSTATUS" type = "hidden" value="ByDeliveryDate"  id = "ByDeliveryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByDeliveryDate")) {%>checked <%}%>></label>
    	<INPUT type="Hidden" name="DIRTYPE" value="INBOUND"> 
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
      <!-- <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</button>&nbsp; -->
  	  
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','IBO');}"><b>Back</b></button> -->
  	  </div>
        </div>
      
      
       	  </div>
		
		 <div class="table-responsive">
  <div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory"
									class="table table-bordred table-striped">
									
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF NO</th>
		                	<th style="font-size: smaller;">SUPPLIER NAME</th>
							<th style="font-size: smaller;">SUPPLIER GROUP</th>
		                	<th style="font-size: smaller;">PRODUCT</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                    <th style="font-size: smaller;">DETAIL DESC</th>
		                	<th style="font-size: smaller;">DATE</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">RECEIVE QTY</th>
		                	<th style="font-size: smaller;">RECEIVE STATUS</th>
		                	<th style="font-size: smaller;">USER</th>
		                	<th style="font-size: smaller;">REMARKS</th>
		                			                	
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='10'></th>
		            <th style="text-align: left !important">Total</th>
		            <th style="text-align: right !important"></th><th style="text-align: right !important"></th>
		            <th></th><th></th><th></th>
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

 /*   function storeUserPreferences(){
	   storeInLocalStorage('OrderSummaryInbound_FROMDATE', document.form1.FROM_DATE.value);
  		storeInLocalStorage('OrderSummaryInbound_TODATE', document.form1.TO_DATE.value);
  		storeInLocalStorage('OrderSummaryInbound_CUSTOMER', $('#CUSTOMER').val());
  		storeInLocalStorage('OrderSummaryInbound_SUPPLIER_TYPE_ID', $('#SUPPLIER_TYPE_ID').val());
  		storeInLocalStorage('OrderSummaryInbound_ORDERNO', document.form1.ORDERNO.value);
  		storeInLocalStorage('OrderSummaryInbound_JOBNO', document.form1.JOBNO.value);
  		storeInLocalStorage('OrderSummaryInbound_ITEM', document.form1.ITEM.value);
  		storeInLocalStorage('OrderSummaryInbound_PRD_CLS_ID', document.form1.PRD_CLS_ID.value);
  		storeInLocalStorage('OrderSummaryInbound_PRD_TYPE_ID', document.form1.PRD_TYPE_ID.value);
  		storeInLocalStorage('OrderSummaryInbound_PRD_BRAND_ID', document.form1.PRD_BRAND_ID.value);
  		storeInLocalStorage('OrderSummaryInbound_ORDERTYPE',$('#ORDERTYPE').val());
  		storeInLocalStorage('OrderSummaryInbound_STATUS', $('#STATUS').val());
	} */
  var tableData = [];
       <%
       
       movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant, DESC,CUSTOMER,VIEWSTATUS,UOM,"");
       
		if(movQryList.size()<=0)
			cntRec ="true";
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		  <%}%>

      <%
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
			    Map lineArr = (Map) movQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          
          String qtyOrValue =(String)lineArr.get("qtyor");
          String qtyValue =(String)lineArr.get("qty");
          
          float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
          float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
          
          if(qtyOrVal==0f){
       	   qtyOrValue="0.000";
          }else{
       	   qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
          }if(qtyVal==0f){
        	  qtyValue="0.000";
          }else{
        	  qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
          }
			suppliertypeid = customerBeanDAO.getSupplierTypeId(plant,(String)lineArr.get("custname"));
			 if(suppliertypeid == null ||  suppliertypeid.equals(null) ||suppliertypeid.equals("")|| suppliertypeid.equals("NOSUPPLIERTYPE"))
			 {
				suppliertypedesc="";
			 }
			 else
			 {
				suppliertypedesc = customerBeanDAO.getSupplierTypeDesc(plant,suppliertypeid);
				if(suppliertypedesc == null)
					suppliertypedesc="";
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
		    	   lnstat="PARTIALLY RECEIVED";
		       else if(lnstat.equalsIgnoreCase("C"))
		    	   lnstat="RECEIVED";
//imti end
			
 			 double var = Double.parseDouble(qtyOrValue);
 			 qtyOrValue = StrUtils.addZeroes(var,DbBean.NOOFDECIMALPTSFORWEIGHT);
			 
 			 double qtvar = Double.parseDouble(qtyValue);
 			 qtyValue = StrUtils.addZeroes(qtvar,DbBean.NOOFDECIMALPTSFORWEIGHT);
	%>
	   
	          var rowData = [];
       rowData[rowData.length] = '<%=iIndex%>';
       <%-- rowData[rowData.length] = '<a href="/track/purchaseorderservlet?PONO=<%=StrUtils.replaceCharacters2Str((String)lineArr.get("pono"))%>&Submit=View&RFLAG=4"><%=(String)lineArr.get("pono")%></a>'; --%>
//     RESVI STARTS 
       <%if(displaySummaryLink){ %>
       rowData[rowData.length] = '<a href="../purchaseorder/detail?pono=<%=StrUtils.replaceCharacters2Str((String)lineArr.get("pono"))%>"><%=(String)lineArr.get("pono")%></a>';
       <% }else { %>   
       rowData[rowData.length] = '<%=(String)lineArr.get("pono")%>';
       <% } %>
//        RESVI ENDS    
       rowData[rowData.length] = '<%=(String)lineArr.get("ordertype")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("jobNum")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
	   rowData[rowData.length] = '<%=suppliertypedesc%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("item") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("itemdesc") %>';
       var ITEM_DESC = escapeHtml('<%=(String)lineArr.get("DetailItemDesc") %>');
       <%-- rowData[rowData.length] = '<%=(String)lineArr.get("DetailItemDesc") %>'; --%>
       rowData[rowData.length] = ITEM_DESC;
       rowData[rowData.length] = '<%=(String)lineArr.get("TRANDATE") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("UOM") %>';
       rowData[rowData.length] = '<%=qtyOrValue%>';
       rowData[rowData.length] = '<%=qtyValue%>';
       //imti start
       rowData[rowData.length] = '<%=lnstat%>';
       //imti end
       <%-- rowData[rowData.length] = '<%=(String)lineArr.get("status_id") %>'; --%>
       rowData[rowData.length] = '<%=(String)lineArr.get("users")%>';
       <%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="javascript:popUpWin(\'inboundOrderPrdRemarksList.jsp?REMARKS1=<%=(String)lineArr.get("remarks")%>&REMARKS2=<%=(String)lineArr.get("remarks3")%>&PONO=<%=(String)lineArr.get("pono")%>&ITEM=<%=(String)lineArr.get("item")%>&POLNNO=<%=(String)lineArr.get("polnno")%>\');">&#9432;</a> '; --%>  
       <%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\''+<%=(String)lineArr.get("remarks")%>+'\',\''+<%=(String)lineArr.get("remarks3")%>+'\',\''+<%=(String)lineArr.get("pono")%>+'\',\''+<%=(String)lineArr.get("item")%>+'\',\''+<%=(String)lineArr.get("polnno")%>+'\');"></a> '; --%>
       <%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails('+<%=(String)lineArr.get("remarks")%>+','+<%=(String)lineArr.get("remarks3")%>+','+<%=(String)lineArr.get("pono")%>+','+<%=(String)lineArr.get("item")%>+','+<%=(String)lineArr.get("polnno")%>+');"></a> '; --%>
       <%-- rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'<%=(String)lineArr.get("remarks")%>\',\'<%=(String)lineArr.get("remarks3")%>\',\'<%=(String)lineArr.get("pono")%>\',\'<%=(String)lineArr.get("item")%>\',\'<%=(String)lineArr.get("polnno")%>\');"></a> '; --%>
       rowData[rowData.length] = '<a href="#" class="fa fa-info-circle" onClick="loadLTPDetails(\'\',\'\',\'<%=(String)lineArr.get("pono")%>\',\'<%=(String)lineArr.get("item")%>\',\'<%=(String)lineArr.get("polnno")%>\');"></a> ';
       tableData[tableData.length] = rowData;
       
     <%}%>
     var groupColumn = 1;
     $(document).ready(function(){
         /* if (document.form1.FROM_DATE.value == ''){
    	 	getLocalStorageValue('OrderSummaryInbound_FROMDATE', '', 'FROM_DATE');
         }
         if (document.form1.TO_DATE.value == ''){
    	 	getLocalStorageValue('OrderSummaryInbound_TODATE', '', 'TO_DATE');
         }
         if (document.form1.CUSTOMER.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_CUSTOMER', '', 'CUSTOMER');
          }
         if (document.form1.SUPPLIER_TYPE_ID.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
          }
         if (document.form1.ORDERNO.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_ORDERNO', '', 'ORDERNO');
          }
         if (document.form1.JOBNO.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_JOBNO', '', 'JOBNO');
          }
         if (document.form1.ITEM.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_ITEM', '', 'ITEM');
          }
         if (document.form1.PRD_CLS_ID.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_PRD_CLS_ID', '', 'PRD_CLS_ID');
          }
         if (document.form1.PRD_TYPE_ID.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
          }
         if (document.form1.PRD_BRAND_ID.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
          }
         if (document.form1.ORDERTYPE.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_ORDERTYPE', '', 'ORDERTYPE');
          }
         if (document.form1.STATUS.value == ''){
        	 getLocalStorageValue('OrderSummaryInbound_STATUS', '', 'STATUS');
          }
         
    	storeUserPreferences(); */
    	 $('#tableMovementHistory').DataTable({
    		 "lengthMenu": [[50, 100, 500], [50, 100, 500]],
//     		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    		  	data: tableData,
    		  	"columnDefs": [{"className": "t-right", "targets": [11,12]}],
    			"orderFixed": [ ], 
    		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
    			"<'row'<'col-md-6'><'col-md-6'>>" +
    			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
    			"oLanguage": {"sEmptyTable": "No Data For This criteria"},
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
                        columns: ':not(:eq('+groupColumn+')):not(:eq(5)):not(:eq(14)):not(:eq(15)):not(:eq(16))'
                    }
    	        ],
    	        "order": [],
    	        drawCallback: function() {

    	        	this.attr('width', '100%');
					var groupColumn = 0;
					var groupRowColSpan= 10;
				   	var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalPickQty = 0;
		            var groupTotalPickQty = 0;
		            var totalIssueQty = 0;
		            var groupTotalIssueQty = 0;
		            var totalReceiveQty = 0;
		            var groupTotalReceiveQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                    last = group;
		                    groupEnd = i;
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalReceiveQty = 0;
		                    
		                }
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', ''));
		                groupTotalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
		                totalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
		                   
		                
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(totalPickQty).toFixed(3) + '</td><td>' + parseFloat(totalIssueQty).toFixed(3) + '</td><td></td><td></td><td></td></tr>'
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
    	              .column(11)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);

    	            // Total over this page
    	            totalOrdVal = api
    	              .column(11)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
    	              
    	            totalRecVal = api
    	              .column(12)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);

    	            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalPickQty = 0;
		            var groupTotalPickQty = 0;
		            var totalIssueQty = 0;
		            var groupTotalIssueQty = 0;
		            var totalReceiveQty = 0;
		            var groupTotalReceiveQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                    last = group;
		                    groupEnd = i;
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalReceiveQty = 0;
		                    
		                }
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', ''));
		                groupTotalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
		                totalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
		                   
		                
		                currentRow = i;
		            } );
    	              
    	            // Update footer
<%--     	            $(api.column(11).footer()).html(parseFloat(totalOrdVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(12).footer()).html(parseFloat(totalRecVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
    	            $(api.column(11).footer()).html(parseFloat(groupTotalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(12).footer()).html(parseFloat(groupTotalIssueQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
  function loadLTPDetails(remarks,remarks3,pono,item,polnno){
		
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
 function getvendname(TAXTREATMENT,VENDO){
	
	}

 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
