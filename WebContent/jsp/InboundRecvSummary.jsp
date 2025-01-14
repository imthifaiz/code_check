<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@page import="com.track.dao.ItemMstDAO"%><!-- <html> -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Order Receipt Summary";
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
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 30 2014, Description:To open Goods Receipt Summary  in excel powershell format
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
  document.form1.action="/track/ReportServlet?action=ExportExcelGRSummary";
  document.form1.submit();
 }
//---End Modified by Deen on May 30 2014, Description:To open Goods Receipt Summary  in excel powershell format
 var postatus =   [{
    "year": "Purchase Order",
    "value": "Purchase Order",
    "tokens": [
      "Purchase Order"
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
			    "year": "Goods Receipt",
			    "value": "Goods Receipt",
			    "tokens": [
			      "Goods Receipt"
			    ]
		  },
		  {
			    "year": "Bill",
			    "value": "Bill",
			    "tokens": [
			      "Bill"
			    ]
		  },
		  
		  {
			    "year": "De-Kitting",
			    "value": "De-Kitting",
			    "tokens": [
			      "De-Kitting"
			    ]
			  },
			  
			  {
				    "year": "Kitting",
				    "value": "Kitting",
				    "tokens": [
				      "Kitting"
				    ]
				  },
				  {
					    "year": "Stock Take",
					    "value": "Stock Take",
					    "tokens": [
					      "STOCK_TAKE"
					    ]
					  }/* ,
	  {
		    "year": "Tax Invoice",
		    "value": "Tax Invoice",
		    "tokens": [
		      "PAID"
		    ]
		  } */];
			  
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
<script src="../jsp/js/json2.js"></script>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();
	ArrayList movItemQryList = new ArrayList();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
        String systatus = session.getAttribute("SYSTEMNOW").toString();
	String FROM_DATE = "", TO_DATE = "", DIRTYPE = "", BATCH = "", USER = "", ITEM = "",GRNO = "", 
			fdate = "", tdate = "", JOBNO = "", ITEMNO = "",PRD_DEPT_ID="", ORDERNO = "", CUSTOMER = "", PGaction = "",ITEMDESC="";
	String TO_ASSIGNEE="",LOANASSIGNEE="",ORDERTYPE,LOC="",REASONCODE="",FILTER="",LOC_TYPE_ID2="",LOC_TYPE_ID3;
	PGaction = StrUtils.fString(request.getParameter("PGaction"))
			.trim();
	String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	String html = "", cntRec = "false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",LOC_TYPE_ID="";
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportrecvsummry", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportrecvsummry", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	
	double ireceivetotal=0;
	double ireversetotal=0;
	int k=0;

	FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String  fieldDesc="";

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
// 	String curDate = _dateUtils.getDate();
      String curDate =DateUtils.getDateMinusDays();//resvi
      if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
    		curDate =DateUtils.getDate();
	if (FROM_DATE.length() < 0 || FROM_DATE == null
			|| FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;//resvi
	if (FROM_DATE.length() > 5)

		//fdate = FROM_DATE.substring(6) + "-"+ FROM_DATE.substring(3, 5) + "-"+ FROM_DATE.substring(0, 2);
		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		// tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5)+ "-" + TO_DATE.substring(0, 2);
		 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);

	DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
	JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
	USER = StrUtils.fString(request.getParameter("USER"));
	ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	ORDERTYPE= StrUtils.fString(request.getParameter("ORDERTYPE"));
	CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
	TO_ASSIGNEE = StrUtils.fString(request.getParameter("TO_ASSIGNEE"));
	LOANASSIGNEE = StrUtils.fString(request.getParameter("LOANASSIGNEE"));
	BATCH = StrUtils.fString(request.getParameter("BATCH"));
	ITEMDESC = StrUtils.fString(request.getParameter("DESC"));
	LOC = StrUtils.fString(request.getParameter("LOC"));
	PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
	PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	FILTER = StrUtils.fString(request.getParameter("FILTER"));
	GRNO = StrUtils.fString(request.getParameter("grno"));
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	String collectionDate=DateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	if (DIRTYPE.length() <= 0) {
		DIRTYPE = "RECEIVE";
	}
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
	
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Order Receipt Summary</label></li>                             
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
<FORM class="form-horizontal" name="form1" method="post" action="InboundRecvSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

<Center><small><%=fieldDesc%></small></Center>

<%-- <Center>
<h1><small><%=fieldDesc%></small></h1>

  </Center> --%>
  
		<div id="target" style="padding: 18px; display:none;">
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
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
  		<!-- <div class="">  -->
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status"  placeholder="ORDER TYPE" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		<!-- <span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		<!-- </div> -->
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC" name="LOC" placeholder="LOCATION" value="<%=StrUtils.forHTMLTag(LOC)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
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
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" placeholder="LOCATION TYPE 3" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" size="30" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc3(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>	
  			
        <input type="hidden" name="LOC_DESC" value="">
  		<INPUT type="Hidden" name="DIRTYPE" value="RECEIVE">
     	<INPUT type="Hidden" name="JOBNO" value="">
     	<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>">
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">                       
        <input type="FILTER" name="sortby" id="FILTER" class="ac-selected form-control" placeholder="Sort By" value="<%=StrUtils.forHTMLTag(FILTER)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesortby(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>	
  			<div class="col-sm-4 ac-box">  		
	  		<input type="text" class="ac-selected form-control" id="grno" name="grno" placeholder="GRNO" value="<%=StrUtils.forHTMLTag(GRNO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changegrno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
	  		</div>  		  		
  		</div>
  		<div class="row" style="padding:3px">
	  		<div class="col-sm-2">
	  		</div>
	  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="REASONCODE" name="REASONCODE" placeholder="REASON CODE"value="<%=StrUtils.forHTMLTag(REASONCODE)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changereasoncode(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">  		
  		<div class="">
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				</div>
			</div>
		</div>
		
			
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	<div class="col-sm-4 ac-box">
  		<input type="hidden" class="ac-selected form-control" id="TO_ASSIGNEE" name="TO_ASSIGNEE" value="<%=StrUtils.forHTMLTag(TO_ASSIGNEE)%>" placeholder="To Customer">	
  		</div>
  		
  	<div class="col-sm-4 ac-box">  		
  		<input type="hidden" name="LOANASSIGNEE" id="LOANASSIGNEE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(LOANASSIGNEE)%>" placeholder="Rental Customer" > 		
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
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTR');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>     
 	     
      <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              
              <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		      </div>
              	<table id="tableInventorySummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">GRNO</th>
		                	<th style="font-size: smaller;">RECEIVED DATE</th>
		                	<th style="font-size: smaller;">SUPPLIER NAME</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">LOC</th>
		                	<th style="font-size: smaller;">BATCH</th>
		                	<th style="font-size: smaller;">EXPIRY DATE</th>
		                	<th style="font-size: smaller;">RECEIVED QTY</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">USER</th>
		                	<th style="font-size: smaller;">REMARKS</th>
		                	
		                </tr>
		            </thead>
		            <tfoot align="right" style="
    display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
 
  
  </FORM>
  <script>
  var tableInventorySummary;
  var ITEMNO, ITEMDESC, FROM_DATE, TO_DATE,PRD_DEPT_ID, DIRTYPE, SUPPLIER, ORDERNO, JOBNO, ORDERTYPE, PRD_CLS_ID,PRD_DEPT_ID, LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,
  TO_ASSIGNEE, LOANASSINEE, REASONCODE,BATCH,LOC,FILTER, GRNO, groupRowColSpan = 8;
function getParameters(){
	return {
		"ITEM": ITEMNO,
		"PRD_DESCRIP":ITEMDESC,
		"FDATE":FROM_DATE,
		"TDATE":TO_DATE,
		"DTYPE":DIRTYPE,
		"CNAME":SUPPLIER,
		"ORDERNO":ORDERNO,
		"JOBNO":JOBNO,
		"ORDERTYPE":ORDERTYPE,
		"PRD_BRAND_ID":PRD_BRAND_ID,
		"PRD_TYPE_ID":PRD_TYPE_ID,
		"PRD_CLS_ID":PRD_CLS_ID,
		"PRD_DEPT_ID":PRD_DEPT_ID,
		"LOC_TYPE_ID":LOC_TYPE_ID,
		"TOASSINEE":TO_ASSIGNEE,
		"LOANASSINEE":LOANASSINEE,
		"REASONCODE":REASONCODE,
		"BATCH":BATCH,
		"LOC":LOC,
		"FILTER":FILTER,	
		"LOC_TYPE_ID2":LOC_TYPE_ID2,
		"LOC_TYPE_ID3":LOC_TYPE_ID3,
		"GRNO":GRNO,
		"ACTION": "VIEW_GOODS_RECIPT_SMRY",
		"PLANT":"<%=plant%>"
	}
}  
function onGo(){
	var flag    = "false";

	    FROM_DATE      = document.form1.FROM_DATE.value;
	    TO_DATE        = document.form1.TO_DATE.value;
	    DIRTYPE        = document.form1.DIRTYPE.value;
	    JOBNO          = document.form1.JOBNO.value;
	    ITEMNO         = document.form1.ITEM.value;
	    ORDERNO        = document.form1.ORDERNO.value;
	   ORDERTYPE      = document.form1.ORDERTYPE.value;
	    SUPPLIER              = document.form1.CUSTOMER.value;
	    TO_ASSIGNEE           = document.form1.TO_ASSIGNEE.value;
	    LOANASSINEE           = document.form1.LOANASSIGNEE.value;
	    BATCH        = document.form1.BATCH.value;
	    LOC          = document.form1.LOC.value;
	    ITEMDESC       = document.form1.DESC .value;
	  
	    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
	    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
	    PRD_DEPT_ID      = document.form1.PRD_DEPT_ID.value;
	    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
	   REASONCODE      = document.form1.REASONCODE.value;
	    LOC_TYPE_ID         = document.form1.LOC_TYPE_ID.value;
	    FILTER         = document.form1.FILTER.value; 
	    LOC_TYPE_ID2         = document.form1.LOC_TYPE_ID2.value;
	    LOC_TYPE_ID3         = document.form1.LOC_TYPE_ID3.value;
	    GRNO = document.form1.grno.value;
	   
	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
	  // if(flag == "false"){ alert("Please define any one search criteria"); return false;}
	   
	    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	 // storeInLocalStorage('InboundRecvSummary_FROMDATE', FROM_DATE);
	//storeInLocalStorage('InboundRecvSummary_TODATE', TO_DATE);
	storeInLocalStorage('InboundRecvSummary_CUSTOMER', SUPPLIER);
	storeInLocalStorage('InboundRecvSummary_ORDERNO', $('#ORDERNO').val());
	storeInLocalStorage('InboundRecvSummary_ITEM', ITEMNO);
	storeInLocalStorage('InboundRecvSummary_PRD_CLS_ID', PRD_CLS_ID);
	storeInLocalStorage('InboundRecvSummary_PRD_TYPE_ID', PRD_TYPE_ID);
	storeInLocalStorage('InboundRecvSummary_PRD_BRAND_ID', PRD_BRAND_ID);
	storeInLocalStorage('InboundRecvSummary_PRD_DEPT_ID', PRD_DEPT_ID);
	storeInLocalStorage('InboundRecvSummary_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('InboundRecvSummary_LOC', LOC);
	storeInLocalStorage('InboundRecvSummary_BATCH', BATCH);
	storeInLocalStorage('InboundRecvSummary_LOC_TYPE_ID',LOC_TYPE_ID);
	storeInLocalStorage('InboundRecvSummary_LOC_TYPE_ID2',LOC_TYPE_ID2);
	storeInLocalStorage('InboundRecvSummary_LOC_TYPE_ID3', LOC_TYPE_ID3);
	storeInLocalStorage('InboundRecvSummary_FILTER',FILTER);
	storeInLocalStorage('InboundRecvSummary_REASONCODE',REASONCODE);
	storeInLocalStorage('InboundRecvSummary_GRNO', GRNO);
	storeInLocalStorage('InboundRecvSummary_TO_ASSIGNEE',$('#TO_ASSIGNEE').val());
	storeInLocalStorage('InboundRecvSummary_LOANASSIGNEE',$('#LOANASSIGNEE').val());
    }
	   var urlStr = "../InboundOrderHandlerServlet";
	  	// Call the method of JQuery Ajax provided
	  	var groupColumn = 3;
	  	var totalQty = 0;
	   // End code modified by Deen for product brand on 11/9/12
	   if (tableInventorySummary){
	   	tableInventorySummary.ajax.url( urlStr ).load();
	   }else{
		    tableInventorySummary = $('#tableInventorySummary').DataTable({
				"processing": true,
				"lengthMenu": [[50, 100, 500], [50, 100, 500]],
				searching: true, // Enable searching	
		        search: {
		            regex: true,   // Enable regular expression searching
		            smart: false   // Disable smart searching for custom matching logic
		        },
// 				"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
			        			data.items[dataIndex]['recqty'] = parseFloat(data.items[dataIndex]['recqty']).toFixed('3').replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        		}
			        		return data.items;
			        	}
			        }
			    },
		        "columns": [
	   			{"data": 'pono', "orderable": true},
	   			{"data": 'grno', "orderable": true},
	   			{"data": 'strDate', "orderable": true},
	   			{"data": 'cname', "orderable": true},
	   			{"data": 'item', "orderable": true},
	   			{"data": 'itemdesc', "orderable": true},
	   			{"data": 'loc', "orderable": true},
	   			{"data": 'batch', "orderable": true},
	   			{"data": 'expiredate', "orderable": true},
	   			{"data": 'recqty', "orderable": true},
	   			{"data": 'uom', "orderable": true},
	   			{"data": 'users', "orderable": true},
	   			{"data": 'remark', "orderable": true}
	   			
	   			],
				"columnDefs": [{"className": "t-right", "targets": [9]}],
				//"orderFixed": [ groupColumn, 'asc' ], 
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
	                   columns: ':not(:eq('+groupColumn+')):not(:eq(0)):not(:eq(8)):not(:eq(9))'
	               }
		        ],
		        "order": [],
		        "createdRow": function(row, data, dataIndex){
		        	var parts = data["expiredate"].split("/");
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

		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalQty = 0;
		            var groupTotalQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	
		                    last = group;
		                    groupEnd = i;
		                    groupTotalQty = 0;
		                }
		                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                totalQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                currentRow = i;
		            } );	         
		            		         
		            /* $( api.column( 8 ).footer() ).html('Total');
		            $( api.column( 9 ).footer() ).html(addZeroes(parseFloat(totalord).toFixed(3)));	 */

		            $( api.column( 8 ).footer() ).html('groupTotalQty');
		            $( api.column( 9 ).footer() ).html(addZeroes(parseFloat(totalQty).toFixed(3)));	            		            
		        },
				"drawCallback": function ( settings ) {
		            var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalQty = 0;
		            var groupTotalQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	if (i > 0) {
		                		groupTotalQty=parseFloat(groupTotalQty).toFixed(3);
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQty + '</td><td></td><td></td><td></td></tr>'
				                    );
		                	}
		                    last = group;
		                    groupEnd = i;
		                    groupTotalQty = 0;
		                }
		                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                totalQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', ''));
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	groupTotalQty=parseFloat(groupTotalQty).toFixed(3);
		            	totalQty=parseFloat(totalQty).toFixed(3);
		            	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalQty + '</td><td></td><td></td><td></td></tr>'
	                   );
	               	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + groupTotalQty + '</td><td></td><td></td><td></td></tr>'
	                   );
	               }
		        }/**/
			});

		    $("#tableInventorySummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
	        	tableInventorySummary.draw();
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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                   
	        	outPutdata = outPutdata+item.RECVDETAILS
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
         '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Received Date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Supplier Name</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Expire date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Receive Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
         '<TH><font color="#ffffff" align="left"><b>User</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Remarks</TH>'+
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


 function changegrno(obj){
	 $("#grno").typeahead('val', '"');
	 $("#grno").typeahead('val', '');
	 $("#grno").focus();
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
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	// getLocalStorageValue('InboundRecvSummary_FROMDATE',  $('#FROM_DATE').val(), 'FROM_DATE');
	// getLocalStorageValue('InboundRecvSummary_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('InboundRecvSummary_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('InboundRecvSummary_ORDERNO', '', 'ORDERNO');
	 getLocalStorageValue('InboundRecvSummary_ITEM', '', 'ITEM');
	 getLocalStorageValue('InboundRecvSummary_PRD_CLS_ID', '', 'PRD_CLS_ID');
	 getLocalStorageValue('InboundRecvSummary_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
	 getLocalStorageValue('InboundRecvSummary_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
	 getLocalStorageValue('InboundRecvSummary_PRD_DEPT_ID', '', 'PRD_DEPT_ID');
	 getLocalStorageValue('InboundRecvSummary_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('InboundRecvSummary_LOC', '', 'LOC');
	 getLocalStorageValue('InboundRecvSummary_BATCH', '', 'BATCH');
	 getLocalStorageValue('InboundRecvSummary_LOC_TYPE_ID', '', 'LOC_TYPE_ID');
	 getLocalStorageValue('InboundRecvSummary_LOC_TYPE_ID2', '', 'LOC_TYPE_ID2');
	 getLocalStorageValue('InboundRecvSummary_LOC_TYPE_ID3', '', 'LOC_TYPE_ID3');
	 getLocalStorageValue('InboundRecvSummary_FILTER', '', 'FILTER');
	 getLocalStorageValue('InboundRecvSummary_REASONCODE', '', 'REASONCODE');
	 getLocalStorageValue('InboundRecvSummary_GRNO', '', 'grno');
	 getLocalStorageValue('InboundRecvSummary_TO_ASSIGNEE', '', 'TO_ASSIGNEE');
	 getLocalStorageValue('InboundRecvSummary_LOANASSIGNEE', '', 'LOANASSIGNEE');
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
				Submit : "GET_ORDER_NO_FOR_ORDER_RECEIPT_AUTOSUGGESTION",
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
	
	$('#grno').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GRNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_GRNO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : document.form1.ORDERNO.value,
				GRNO : query
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
		    return '<p>' + data.GRNO + '</p>';
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