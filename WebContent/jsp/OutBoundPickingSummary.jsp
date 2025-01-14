<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%@ include file="header.jsp"%>
<%@page import="com.track.constants.IDBConstants"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- <html> -->
<%
String title = "Order Issue Summary";
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
<script src="js/json2.js"></script>
<script src="js/general.js"></script>
<script>

var subWin = null;

function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 27 2014, Description:To open Goods Issue Summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.CUSTOMER.value;
  document.form1.REASONCODE.value;
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.action="/track/ReportServlet?action=ExportExcelGISummary";
  document.form1.submit();
 }
//---End Modified by Deen on May 27 2014, Description:To open Goods Issue Summary  in excel powershell format
function onGo(){
   var flag    = "false";
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var INVOICENO     = document.form1.INVOICENO.value;
   var USER           = document.form1.CUSTOMER.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_DEPT_ID     = document.form1.PRD_DEPT_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   var REASONCODE      = document.form1.REASONCODE.value;
  
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(flag == "false"){ alert("Please select the Dirtype"); return false;}
   
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
   if(REASONCODE != null     && REASONCODE != "") { flag = true;}
   if(PRD_DEPT_ID != null     && PRD_DEPT_ID != "") { flag = true;}
   //storeUserPreferences();
  document.form1.action="../salesorder/issuesummary";
   document.form1.submit();
}
/* 
function storeUserPreferences(){
	storeInLocalStorage('OutBoundPickingSummary_FROMDATE', $('#FROM_DATE').val());
	storeInLocalStorage('OutBoundPickingSummary_TODATE', $('#TO_DATE').val());
	storeInLocalStorage('OutBoundPickingSummary_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('OutBoundPickingSummary_ORDERNO', $('#ORDERNO').val());
	storeInLocalStorage('OutBoundPickingSummary_ITEM', $('#ITEM').val());
	storeInLocalStorage('OutBoundPickingSummary_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('OutBoundPickingSummary_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('OutBoundPickingSummary_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('OutBoundPickingSummary_ORDERTYPE',$('#ORDERTYPE').val());
	storeInLocalStorage('OutBoundPickingSummary_BATCH',$('#BATCH').val());
	storeInLocalStorage('OutBoundPickingSummary_FILTER', $('#FILTER').val());
	storeInLocalStorage('OutBoundPickingSummary_REASONCODE', $('#REASONCODE').val());
	storeInLocalStorage('OutBoundPickingSummary_INVOICENO', $('#INVOICENO').val());
	storeInLocalStorage('OutBoundPickingSummary_LOC',  $('#LOC').val());
	storeInLocalStorage('OutBoundPickingSummary_LOC_TYPE_ID',  $('#LOC_TYPE_ID').val());
	storeInLocalStorage('OutBoundPickingSummary_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('OutBoundPickingSummary_LOC_TYPE_ID3',  $('#LOC_TYPE_ID3').val());

} */
var postatus =   [{
    "year": "Sales Order",
    "value": "Sales Order",
    "tokens": [
      "DRAFT"
    ]
  },
  /* {
	    "year": "Rental Order",
	    "value": "Rental Order",
	    "tokens": [
	      "OPEN"
	    ]
	  },
	  {
		    "year": "Consignment Order",
		    "value": "Consignment Order",
		    "tokens": [
		      "OPEN"
		    ]
		  }, */
		  {
			    "year": "Goods Issue",
			    "value": "Goods Issue",
			    "tokens": [
			      "OPEN"
			    ]
			  } ,
	  {
		    "year": "Invoice",
		    "value": "Invoice",
		    "tokens": [
		      "PAID"
		    ]
		  } ,
		  {
			    "year": "Kitting",
			    "value": "Kitting",
			    "tokens": [
			      "KITTING"
			    ]
			  },
			  {
				    "year": "De-Kitting",
				    "value": "De-Kitting",
				    "tokens": [
				      "DE-KITTING"
				    ]
				  },
				  {
					    "year": "Stock Take",
					    "value": "Stock Take",
					    "tokens": [
					      "STOCK_TAKE"
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

<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	HTReportUtil movHisUtil       = new HTReportUtil();
	movHisUtil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList  = new ArrayList();
	ArrayList movItemQryList  = new ArrayList();
	Hashtable htItem = new Hashtable();
	InvMstDAO invmstdao = new InvMstDAO();
	invmstdao.setmLogger(mLogger);
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String  fieldDesc="",loc="",expiredate="",item="",batch="",REASONCODE="",FILTER="";

	session= request.getSession();
	String plant = (String)session.getAttribute("PLANT");
        String userID = (String)session.getAttribute("LOGIN_USER");
        String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
        String systatus = session.getAttribute("SYSTEMNOW").toString();
        String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
        
        boolean displaySummaryExport=false;
        if(systatus.equalsIgnoreCase("ACCOUNTING"))
        {
        	displaySummaryExport = ub.isCheckValAcc("exportissuesummry", plant,LOGIN_USER);
        	displaySummaryExport=true;
        }
        if(systatus.equalsIgnoreCase("INVENTORY"))
        {
        	displaySummaryExport = ub.isCheckValinv("exportissuesummry", plant,LOGIN_USER);
        	displaySummaryExport=true;
        }
	String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",
	fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",ORDERTYPE="",CUSTOMER="",CUSTOMER_TO="",CUSTOMER_LO="",ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
	String html = "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="",LOC="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",INVOICENO="";

	FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	
	double iordertotal=0;
	double ipicktotal=0;
	double iissuetotal=0;
	double ireversetotal=0;
	int k=0;

	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// 	String curDate =_dateUtils.getDate();
     String curDate =DateUtils.getDateMinusDays();//resvi
     if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
    		curDate =DateUtils.getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE=curDate;

	if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	//fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	//MLogger.log(0," FROM_DATE  : "+ FROM_DATE);

	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	if (TO_DATE.length()>5)
	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);

	//MLogger.log(0," TO_DATE  : "+ TO_DATE);

	DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
	JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
	USER          = StrUtils.fString(request.getParameter("USER"));
	ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
	BATCH       = StrUtils.fString(request.getParameter("BATCH"));
	ORDERNO       = StrUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
	CUSTOMER_TO      = StrUtils.fString(request.getParameter("CUSTOMER_TO"));
	CUSTOMER_LO      = StrUtils.fString(request.getParameter("CUSTOMER_LO"));
	ITEMDESC      = StrUtils.fString(request.getParameter("DESC"));
	ORDERTYPE= StrUtils.fString(request.getParameter("ORDERTYPE"));
	//Start code added by deen for product brand,type on 4/sep/13
	PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
	PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
	LOC = StrUtils.fString(request.getParameter("LOC"));
	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
	FILTER = StrUtils.fString(request.getParameter("FILTER"));
	String collectionDate=DateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	PlantMstUtil plantmstutil = new PlantMstUtil();
	String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
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
	if(DIRTYPE.length()<=0){
		DIRTYPE = "ISSUE";
	}
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
 
 	try{
 
       //Hashtable htItem = new Hashtable();
        if(StrUtils.fString(ITEMNO).length() > 0)             htItem.put("a.ITEM",ITEMNO);
        if(StrUtils.fString(BATCH).length() > 0)              htItem.put("a.BATCH",BATCH);
        if(StrUtils.fString(ORDERNO).length() > 0)            htItem.put("a.DONO",ORDERNO);
        if(StrUtils.fString(CUSTOMER).length() > 0)           htItem.put("a.CNAME",CUSTOMER);
        if(StrUtils.fString(CUSTOMER_LO).length() > 0)        htItem.put("a.CNAME_LON",CUSTOMER_LO);
        if(StrUtils.fString(CUSTOMER_TO).length() > 0)        htItem.put("a.CNAME_TO",CUSTOMER_TO);
        if (StrUtils.fString(ORDERTYPE).length() > 0)   	   htItem.put("d.ORDERTYPE", ORDERTYPE);
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0)        htItem.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0)       htItem.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0)         htItem.put("C.PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(PRD_DEPT_ID).length() > 0)         htItem.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
        if(StrUtils.fString(REASONCODE).length() > 0)     	   htItem.put("a.REMARK",REASONCODE);
        if(StrUtils.fString(LOC).length() > 0)         	   htItem.put("a.LOC",LOC);
        if(StrUtils.fString(LOC_TYPE_ID).length() > 0)        htItem.put("LOC_TYPE_ID",LOC_TYPE_ID);
        if(StrUtils.fString(LOC_TYPE_ID2).length() > 0)        htItem.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
        if(StrUtils.fString(LOC_TYPE_ID3).length() > 0)        htItem.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
        if(StrUtils.fString(INVOICENO).length() > 0)        htItem.put("INVOICENO",INVOICENO);
        
     
     
 }catch(Exception e) { 
	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
 }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Order Issue Summary</label></li>                                   
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
					  <%} %>					
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/issuesummary" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

<span style="text-align: center;"><small><%=fieldDesc%></small></span>
  
<%-- <center><h1><small><%=fieldDesc%></small></h1></center> --%>

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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" >				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  			<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" placeholder="PRODUCT"  value="<%=StrUtils.forHTMLTag(ITEMNO)%>"  >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeproduct(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" placeholder="PRODUCT DEPARTMENT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprddeptid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" placeholder="PRODUCT CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdclsid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" placeholder="PRODUCT SUB CATEGORY" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdtypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" placeholder="PRODUCT BRAND" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdbrdid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<!-- <div class="">  -->
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" placeholder="ORDER TYPE" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		
<!--   		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		<!-- <span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		<!-- </div> -->
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC" name="LOC"  placeholder="LOCATION" value="<%=StrUtils.forHTMLTag(LOC)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		
  		<input type="text" name="BATCH" id="BATCH" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(BATCH)%>" placeholder="BATCH" > 		
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID" name="LOC_TYPE_ID"  placeholder="LOCATION TYPE 1" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc1(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID2" name="LOC_TYPE_ID2"  placeholder="LOCATION TYPE 2" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc2(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  	   <div class="col-sm-4 ac-box">
  		<div class=""> 
  			<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT"  placeholder="LOCATION TYPE 3" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" size="30" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc3(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
     	</div>
  		</div>
      
     	<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>">
  		</div>
  		<div class="row" style="padding:3px">
  			<div class="col-sm-2">
  			</div>
			<div class="col-sm-4 ac-box">                       
        <input type="text" name="FILTER" id="FILTER" class="ac-selected form-control" placeholder="SORT BY" value="<%=StrUtils.forHTMLTag(FILTER)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesortby(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
 			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="REASONCODE" name="REASONCODE"  placeholder="REASON CODE" value="<%=StrUtils.forHTMLTag(REASONCODE)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changereasoncode(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		  		<div class=""> 
			  		<input type="text" class="ac-selected form-control" id="INVOICENO" value="<%=StrUtils.forHTMLTag(INVOICENO)%>"
			  		name="INVOICENO" placeholder="GINO">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeinvoiceno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>

		  		</div>
	  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="hidden" class="ac-selected form-control" id="CUSTOMER_TO" name="CUSTOMER_TO" value="<%=StrUtils.forHTMLTag(CUSTOMER_TO)%>" placeholder="To Customer">	
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	<div class="col-sm-4 ac-box">
  		
  		<input type="hidden" name="CUSTOMER_LO" id="CUSTOMER_LO" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(CUSTOMER_LO)%>" placeholder="Rental Customer" > 		
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
      <div class="col-sm-offset-7">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTR');}"> <b>Back</b></button> -->
  	  </div>
        </div>
       	  </div> 
       	  
  <input type="hidden" name="LOC_DESC" value="">
  	<INPUT type="Hidden" name="DIRTYPE" value="ISSUE">
  	 <INPUT name="JOBNO" type = "Hidden" value="<%=JOBNO%>" size="20"  MAXLENGTH=20>
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tablePickSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tablePickSummary"
									class="table table-bordred table-striped">
   	<thead>
		                <tr role="row">
        
         <th style="font-size: smaller;">ORDER NO</TH>
         <th style="font-size: smaller;">GINO</TH>
         <th style="font-size: smaller;">ISSUED DATE</TH>
         <th style="font-size: smaller;">CUSTOMER NAME</TH>
         <th style="font-size: smaller;">PRODUCT ID</TH>
         <th style="font-size: smaller;">DESCRIPTION</TH>
         <th style="font-size: smaller;">LOC</TH>
         <th style="font-size: smaller;">BATCH</TH>
         <th style="font-size: smaller;">EXPIRE DATE</TH> 
         <th style="font-size: smaller;">ORD QTY</TH>
         <th style="font-size: smaller;">PICK QTY</TH>
         <th style="font-size: smaller;">ISSUE QTY</TH>
         <th style="font-size: smaller;">REVERSE QTY</TH>
         <th style="font-size: smaller;"><%=IDBConstants.UOM_LABEL%></TH>
	     <th style="font-size: smaller;">STATUS</TH>
	     <th style="font-size: smaller;">USER</TH>
         <th style="font-size: smaller;">REMARK</TH>
       </tr>
                   </thead>
                   <tfoot align="right" style="
    display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script>
  var tableData = [];
       <%
       ItemMstUtil itemMstUtil = new ItemMstUtil();
		 itemMstUtil.setmLogger(mLogger);
		 if (StrUtils.fString(ITEMNO).length() >0 &&  StrUtils.fString(ITEMDESC).length()==0){
			 String ItemDesc = itemMstUtil.ItemInItemmst( plant, ITEMNO);
			 ITEMDESC= ItemDesc;
		 }
      
       
	   //movItemQryList = movHisUtil.getPickingSummaryItemList(htItem,fdate,tdate,"ISSUE",plant,ITEMDESC);
	      //if(movQryList.size()<=0 && cntRec=="true" ){ %>
		   <!--  <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR> -->
		  <%//}%>

		  <%
		 
		  /* for(int iItemCnt = 0; iItemCnt < movItemQryList.size(); iItemCnt++) { */
			  
			 
			 
				
			    /* Map lineItemArr = (Map) movItemQryList.get(iItemCnt);
				int iItemIndex = iItemCnt + 1; */
						
				
				try{
			 		// if (StrUtils.fString(ITEM).length() >0)
			        // {
				 		// ItemMstUtil itemMstUtil = new ItemMstUtil();
						// itemMstUtil.setmLogger(mLogger);
						 /* String temItem = itemMstUtil.isValidAlternateItemInItemmst( plant, (String)lineItemArr.get("item"));
						 if (temItem != "") {
						 	ITEMNO = temItem;
						 } else {
						 	throw new Exception("Product not found!");
						 } */
			       //  }
			 		
			       Hashtable ht = new Hashtable();
			       if(DIRTYPE.equalsIgnoreCase("ISSUE"))
			       {
			    	
			        //if(StrUtils.fString(ITEMNO).length() > 0)         
			        //ht.put("a.ITEM",ITEMNO);
			      
			        //ht.put("a.ITEM", ITEMNO);
			        if(StrUtils.fString(BATCH).length() > 0)              ht.put("a.BATCH",BATCH);
			        if(StrUtils.fString(ORDERNO).length() > 0)            ht.put("a.DONO",ORDERNO);
			        if(StrUtils.fString(CUSTOMER).length() > 0)           ht.put("a.CNAME",CUSTOMER);
			        if(StrUtils.fString(CUSTOMER_LO).length() > 0)        ht.put("a.CNAME_LON",CUSTOMER_LO);
			        if(StrUtils.fString(CUSTOMER_TO).length() > 0)        ht.put("a.CNAME_TO",CUSTOMER_TO);
			        if (StrUtils.fString(ORDERTYPE).length() > 0)   	   ht.put("d.ORDERTYPE", ORDERTYPE);
			        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) 	   ht.put("C.ITEMTYPE",PRD_TYPE_ID);
			        if(StrUtils.fString(PRD_BRAND_ID).length() > 0)       ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
			        if(StrUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
			        if(StrUtils.fString(PRD_DEPT_ID).length() > 0)         ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
			        if(StrUtils.fString(REASONCODE).length() > 0)         ht.put("a.REMARK",REASONCODE);
			        if(StrUtils.fString(LOC).length() > 0)         	   ht.put("a.LOC",LOC);
			        if(StrUtils.fString(LOC_TYPE_ID).length() > 0)        ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
			        if(StrUtils.fString(LOC_TYPE_ID2).length() > 0)        ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
			        if(StrUtils.fString(LOC_TYPE_ID3).length() > 0)        ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
			        if(StrUtils.fString(INVOICENO).length() > 0)        ht.put("INVOICENO",INVOICENO);
			       }
			       if(FILTER.length()>0)
				   {
			    	   movQryList = movHisUtil.getPickingSummarybyfilter(ht,fdate,tdate,FILTER,plant,ITEMDESC);
				   }
			       else
			       {
			       		movQryList = movHisUtil.getPickingSummaryList(ht,fdate,tdate,"ISSUE",plant,ITEMDESC);
			       }
			  		      
			       if(movQryList.size()<=0)
						cntRec ="true";
			 }catch(Exception e) { 
				  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			 }
				
			 strTransactionDate="";
		 String strDate="";	
          for (int iCnt =0; iCnt< movQryList.size(); iCnt++){
			    Map lineArr = (Map) movQryList.get(iCnt);
         		int iIndex = iCnt + 1;
        		String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        		String uom = (String)lineArr.get("uom");
        		loc = (String)lineArr.get("loc");
        		String locarry[] = loc.split("_");
        		loc = locarry[locarry.length-1];
        		item = (String)lineArr.get("item");
        		batch = (String)lineArr.get("batch");
        		//expiredate =invmstdao.getInvExpireDate(plant,(String)lineArr.get("item"),loc,(String)lineArr.get("batch"));
        		
        		strDate=(String)lineArr.get("transactiondate");
        		if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
        		{
        			strDate=(String)lineArr.get("transactioncratdate");
        		}
        		expiredate=(String)lineArr.get("expiredate");
        	    if (expiredate.equalsIgnoreCase("null") || expiredate== null)
				   {
        			expiredate="";
				   }
					   
        		
       %>
       var rowData = [];
      <%--  rowData[rowData.length] = '<%=iIndex%>'; --%>
       rowData[rowData.length] = '<%=(String)lineArr.get("dono")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("InvoiceNo")%>';
       rowData[rowData.length] = '<%=strDate%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("cname")%>';
      
       rowData[rowData.length] = '<%=item%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("itemdesc")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("loc")%>';
       rowData[rowData.length] = '<%=batch%>';
        rowData[rowData.length] = '<%=expiredate%>'; 
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("ordqty")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("pickqty")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("issueqty")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("reverseqty")) %>';
       rowData[rowData.length] = '<%=uom%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("lnstat") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("users") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("remark") %>';
       tableData[tableData.length] = rowData;
       <%}%> 
       
       <%-- <% }%> --%>
      
       var groupColumn = 0;
       $(document).ready(function(){
    	  /*  if (document.form1.FROM_DATE.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_FROMDATE','','FROM_DATE');
          }
    	   if (document.form1.TO_DATE.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_TODATE', '','TO_DATE');
          } 
    	   if (document.form1.CUSTOMER.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_CUSTOMER', '','CUSTOMER');
          }	
    	   if (document.form1.ORDERNO.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_ORDERNO','','ORDERNO');
          }		
    	   if (document.form1.ITEM.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_ITEM','', 'ITEM');
          }		
    	   if (document.form1.PRD_CLS_ID.value == ''){
    			getLocalStorageValue('OutBoundPickingSummary_PRD_CLS_ID','', 'PRD_CLS_ID');
          }	
    	   if (document.form1.PRD_CLS_ID.value == ''){
   			getLocalStorageValue('OutBoundPickingSummary_PRD_CLS_ID','', 'PRD_CLS_ID');
         }	
    	   if (document.form1.PRD_CLS_ID.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_PRD_TYPE_ID','', 'PRD_TYPE_ID');
            }
    	   if (document.form1.PRD_BRAND_ID.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_PRD_BRAND_ID','', 'PRD_BRAND_ID');
            }
    	   if (document.form1.ORDERTYPE.value == ''){
    			getLocalStorageValue('OutBoundPickingSummary_ORDERTYPE','','ORDERTYPE');
            }	
    	   if (document.form1.BATCH.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_BATCH','','BATCH');
           }		
    	   if (document.form1.FILTER.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_FILTER', '','FILTER');
           }
    	   if (document.form1.REASONCODE.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_REASONCODE','', 'REASONCODE');
           }
    	   if (document.form1.INVOICENO.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_INVOICENO','', 'INVOICENO');
           }
    	   if (document.form1.LOC.value == ''){
    			getLocalStorageValue('OutBoundPickingSummary_LOC', '','LOC');
           }	
    	   if (document.form1.LOC_TYPE_ID.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_LOC_TYPE_ID', '','LOC_TYPE_ID');
          }
    	   if (document.form1.LOC_TYPE_ID2.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_LOC_TYPE_ID2','','LOC_TYPE_ID2');
          }	
    	   if (document.form1.LOC_TYPE_ID3.value == ''){
    		   getLocalStorageValue('OutBoundPickingSummary_LOC_TYPE_ID3','', 'LOC_TYPE_ID3');
          }		
    			
    	   storeUserPreferences(); */
      	 $('#tablePickSummary').DataTable({
      		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
//       		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
      		  	data: tableData,
      		  	"columnDefs": [{"className": "t-right", "targets": [8,9,10,11]}],
      			//"orderFixed": [ groupColumn, 'asc' ], 
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
                          columns: ':not(:eq('+groupColumn+')):not(:eq(2)):not(:eq(3))'
                      }
      	        ],
      	      "order": [],
		          "createdRow": function(row, data, dataIndex){
		        	var parts = data[7].split("/");
		        	var dt = new Date(parseInt(parts[2], 10),
		        	                  parseInt(parts[1], 10) - 1,
		        	                  parseInt(parts[0], 10));
		        /*	if (dt.getTime() < new Date().getTime()){
		        		$(row).css('color', 'red');
		        	} */
		        },		        	
		        "footerCallback": function ( row, data, start, end, display ) {
		            var api = this.api(), data;
		            var intVal = function ( i ) {
		                return typeof i === 'string' ?
		                    i.replace(/[\$,]/g, '')*1 :
		                    typeof i === 'number' ?
		                        i : 0;
		            };
		         // Total over all pages
		            totalord = api
		                .column(9)
		                .data()
		                .reduce( function (a, b) {
		                    return intVal(a) + intVal(b);
		                }, 0 );
		         
		            totalpick = api
	                .column(10)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
		            
		            totalissue = api
	                .column(11)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
		            
		            totalreverse = api
	                .column(12)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
		         
		            $( api.column( 8 ).footer() ).html('Total');
		            $( api.column( 9 ).footer() ).html(addZeroes(parseFloat(totalord).toFixed(3)));
		            $( api.column( 10 ).footer() ).html(addZeroes(parseFloat(totalpick).toFixed(3)));
		            $( api.column( 11 ).footer() ).html(addZeroes(parseFloat(totalissue).toFixed(3)));
		            $( api.column( 12 ).footer() ).html(addZeroes(parseFloat(totalreverse).toFixed(3)));
		        },
				"drawCallback": function ( settings ) {
					this.attr('width', '100%');
					var groupColumn = 0;
					var groupRowColSpan= 8;
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
		                	if (i > 0) {
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalReceiveQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
				                    );
		                	}
		                    last = group;
		                    groupEnd = i;
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalReceiveQty = 0;
		                    
		                }
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', ''));
		                groupTotalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                totalReceiveQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', ''));
		                   
		                
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + parseFloat(totalPickQty).toFixed(3) + '</td><td>' + parseFloat(totalIssueQty).toFixed(3) + '</td><td>' + parseFloat(totalReceiveQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
	                    );
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalReceiveQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
	                    );
	                }
		        }
			      		     
			});
		        		            
			  });
	 
    </script>
	    
      
  </FORM>
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

 function changesortby(obj){
	 $("#FILTER").typeahead('val', '"');
	 $("#FILTER").typeahead('val', '');
	 $("#FILTER").focus();
	}

 function changereasoncode(obj){
	 $("#REASONCODE").typeahead('val', '"');
	 $("#REASONCODE").typeahead('val', '');
	 $("#REASONCODE").focus();
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
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDERNO_FOR_AUTO_SUGGESTION",
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
	/* Reason code Auto Suggestion */
	$('#REASONCODE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'RSNCODE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_RSNCODE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.RSNCODE_MST);
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
			return '<div><p class="item-suggestion">'+data.RSNCODE+'</p></div>';
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
	$("#FILTER").typeahead({
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
	/* location Auto Suggestion */
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
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
