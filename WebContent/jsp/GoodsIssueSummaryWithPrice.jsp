<%@ page import="com.track.gates.DbBean"%> 
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.dao.FinCountryTaxTypeDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Order Issue Summary With Price";
%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
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
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
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
		  },*/ 
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
<script src="../jsp/js/general.js"></script>
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
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	invmstdao.setmLogger(mLogger);
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	String  fieldDesc="",loc="",expiredate="",item="",batch="",REASONCODE="",FILTER="";
	session= request.getSession();
	String plant = (String)session.getAttribute("PLANT");
    String userID = (String)session.getAttribute("LOGIN_USER");
    String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
    String systatus = session.getAttribute("SYSTEMNOW").toString();
    String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
    String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",
	fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",ORDERTYPE="",CUSTOMER="",CUSTOMER_TO="",CUSTOMER_LO="",ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
	String html = "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",PRD_DEPT_ID="",LOC="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",taxby="",INVOICENO="";
	FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportissuepricesummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportissuepricesummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	//float ordertotal=0, picktotal=0,issuetotal=0,unitprice=0,total=0,subtotal=0,grandtotal=0;
	double ordertotal=0, picktotal=0,issuetotal=0,unitprice=0,total=0,subtotal=0,grandtotal=0;
	double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;
    int k=0;
	float gstpercentage=0;
	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// 	String curDate =_dateUtils.getDate();
    String curDate =DateUtils.getDateMinusDays();//resvi
    if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
    	curDate =DateUtils.getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE=curDate;
	if (FROM_DATE.length()>5)
    fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	if (TO_DATE.length()>5)
	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
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
	PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
	PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
	LOC = StrUtils.fString(request.getParameter("LOC"));
	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	FILTER = StrUtils.fString(request.getParameter("FILTER"));
	INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if(DIRTYPE.length()<=0){
		DIRTYPE = "ISSUEWITHPRICE";
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
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Order Issue Summary With Price</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                             <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();">Export All Data </button>
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
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/issuesummaryprice" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">  
  
  <span style="text-align: center;"><small><%=fieldDesc%></small></span>
  
 <%-- <Center><h1><small><%=fieldDesc%></small></h1></Center> --%>
 
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="Order Number" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  			<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" placeholder="PRODUCT" value="<%=StrUtils.forHTMLTag(ITEM)%>">
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
  		<input type="text" class="ac-selected form-control" id="PRD_DEPT_ID" name="PRD_DEPT_ID" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>"placeholder="PRODUCT DEPARTMENT">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprddeptid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CATEGORY">
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
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeprdtypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
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
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		<!-- <span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		<!-- </div> -->
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC" name="LOC" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION">
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
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID" name="LOC_TYPE_ID" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" placeholder="LOCATION TYPE 1">
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
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID2" name="LOC_TYPE_ID2" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>" placeholder="LOCATION TYPE 2">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc2(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  			<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" placeholder="LOCATION TYPE 3" size="30" MAXLENGTH=20>
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeloc3(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
     	</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
<div class="col-sm-4 ac-box">
  		              <div class="">          
        <input type="text" name="FILTER" id="FILTER" class="ac-selected form-control" placeholder="Sort By" value="<%=StrUtils.forHTMLTag(FILTER)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesortby(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="REASONCODE" name="REASONCODE" value="<%=StrUtils.forHTMLTag(REASONCODE)%>" placeholder="REASON CODE">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changereasoncode(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  			<div class="col-sm-4 ac-box">
  		
  		</div>
      	<input type="hidden" name="LOC_DESC" value="">
  	<INPUT type="Hidden" name="DIRTYPE" value="ISSUEWITHPRICE">
  	<INPUT name="JOBNO" type = "Hidden" value="<%=JOBNO%>" size="20"  MAXLENGTH=20>
     	<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>">
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="INVOICENO" name="INVOICENO" value="<%=StrUtils.forHTMLTag(INVOICENO)%>" placeholder="GINO">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeinvoiceno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div>	
  		<div class="col-sm-4 ac-box">
  		<input type="hidden" class="ac-selected form-control" id="CUSTOMER_TO" name="CUSTOMER_TO" value="<%=StrUtils.forHTMLTag(CUSTOMER_TO)%>" placeholder="To Customer">	
  		</div>
  		</div>
  				<!-- <div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  			
  		</div>
  		</div> -->
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
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTR');}"> <b>Back</b></button> -->
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
        <!--  <TH><font color="#ffffff" align="center">S/N</TH> -->
         <th style="font-size: smaller;">Order No</TH>
         <th style="font-size: smaller;">GINO</TH>
         <th style="font-size: smaller;">Issued Date</TH>
         <th style="font-size: smaller;">Customer Name</TH>
         <th style="font-size: smaller;">Product ID</TH>
         <th style="font-size: smaller;">Description</TH>
         <th style="font-size: smaller;">Loc</TH>
         <th style="font-size: smaller;">Batch</TH>
         <th style="font-size: smaller;">Ord Qty</TH>
         <th style="font-size: smaller;">Pick Qty</TH>
         <th style="font-size: smaller;">Issue Qty</TH>
         <th style="font-size: smaller;"><%=IDBConstants.UOM_LABEL%></TH>
	     <th style="font-size: smaller;">Status</TH>
	     <th style="font-size: smaller;">Unit Price</TH>
	     <th style="font-size: smaller;">Tax%</TH>
         <th style="font-size: smaller;">Issue Price</TH>
         <th style="font-size: smaller;">Tax</TH>
         <th style="font-size: smaller;">Total</TH> 
         <th style="font-size: smaller;">User</TH> 
	     <th style="font-size: smaller;">Remark</TH>
                     </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='8'></th>
		            <th style="text-align: left !important">Total</th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            <th></th>
		            <th></th>
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
       ItemMstUtil itemMstUtil = new ItemMstUtil();
		 itemMstUtil.setmLogger(mLogger);
		 if (StrUtils.fString(ITEMNO).length() >0 &&  StrUtils.fString(ITEMDESC).length()==0){
			 String ItemDesc = itemMstUtil.ItemInItemmst( plant, ITEMNO);
			 ITEMDESC= ItemDesc;
		 }
	      movItemQryList = movHisUtil.getPickingSummaryItemList(htItem,fdate,tdate,"ISSUE",plant,ITEMDESC);	  		  
		  /* for(int iItemCnt = 0; iItemCnt < movItemQryList.size(); iItemCnt++) {
			    Map lineItemArr = (Map) movItemQryList.get(iItemCnt);
				int iItemIndex = iItemCnt + 1; */
				try{
			 		    // ItemMstUtil itemMstUtil = new ItemMstUtil();
						 //itemMstUtil.setmLogger(mLogger);
						/*  String temItem = itemMstUtil.isValidAlternateItemInItemmst( plant, (String)lineItemArr.get("item"));
						 if (temItem != "") {
						 	ITEMNO = temItem;
						 } else {
						 	throw new Exception("Product not found!");
						 } */
			        Hashtable ht = new Hashtable();
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
			       taxby=_PlantMstDAO.getTaxBy(plant);
			       if(FILTER.length()>0)
				   {
			    	   if(taxby.equalsIgnoreCase("BYORDER"))
			    	   {
			    	       movQryList = movHisUtil.getPickingSummarybyfilter(ht,fdate,tdate,FILTER,plant,ITEMDESC);
			    	   }
			    	   else
			    	   {
			    		   movQryList = movHisUtil.getPickingSummarybyfilterByProductGst(ht,fdate,tdate,FILTER,plant,ITEMDESC);
			    	   }
				   }
			       else
			       {
			    	   if(taxby.equalsIgnoreCase("BYORDER"))
			    	   {
			    	      
			    			       		  movQryList = movHisUtil.getPickingSummaryList(ht,fdate,tdate,"ISSUE",plant,ITEMDESC);
			    	   }
			    	   else
			    	   {
			    		   movQryList = movHisUtil.getPickingSummaryListByProductGst(ht,fdate,tdate,"ISSUE",plant,ITEMDESC);
			    	   }
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
        		expiredate =invmstdao.getInvExpireDate(plant,(String)lineArr.get("item"),loc,(String)lineArr.get("batch"));
        		
        		unitprice = Double.parseDouble((String)lineArr.get("unitprice"));
  	            //unitprice = StrUtils.RoundDB(unitprice,2);
  	             
  	            //total  = Double.parseDouble((String)lineArr.get("total"));
  	            //total  = StrUtils.RoundDB(total,2);
	  	        strDate=(String)lineArr.get("transactiondate");
	      		if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
	      		{
	      			strDate=(String)lineArr.get("transactioncratdate");
	      		}
	      		
	      		String gst = (String) lineArr.get("Tax");
	      		if(gst.length()>0)
	      		{
	      			gstpercentage =  Float.parseFloat(gst) ;
	      		}
	      		
	      		//indiviual subtotal price details
	 	       double issuePrice = Double.parseDouble((String)lineArr.get("total"));
	 	       //issuePrice = StrUtils.RoundDB(issuePrice,2);
// 	           double tax = (issuePrice*gstpercentage)/100; //comment
	           
	           //imti added on 29-03-2022 
			   double tax =0.0;
  	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
  	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
  	           if(taxid != 0){
  	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
  	        		   tax = (issuePrice*gstpercentage)/100;
  	        	   } else 
  	        		 gstpercentage=Float.parseFloat("0.000");
  	        	   
  	           }else 
  	        		 gstpercentage=Float.parseFloat("0.000");
              //end
  	           
	           double issPriceTax = issuePrice+tax;
               
               String unitPrice = String.valueOf(unitprice);
               String issuePriceValue = String.valueOf(issuePrice);
               String ordqty=(String)lineArr.get("ordqty");
               String pickQty=(String)lineArr.get("pickqty");
               String issueQty=(String)lineArr.get("issueqty");
               String taxValue = String.valueOf(tax);
               String issuePriceTax = String.valueOf(issPriceTax);
               String gstpercentageValue = String.valueOf(gstpercentage);
               
               float unitPriceVal ="".equals(unitPrice) ? 0.0f :  Float.parseFloat(unitPrice);
               float issuePriceVal ="".equals(issuePriceValue) ? 0.0f :  Float.parseFloat(issuePriceValue);
               float ordqtyVal ="".equals(ordqty) ? 0.0f :  Float.parseFloat(ordqty);
               float pickQtyVal ="".equals(pickQty) ? 0.0f :  Float.parseFloat(pickQty);
               float issueQtyVal ="".equals(issueQty) ? 0.0f :  Float.parseFloat(issueQty);
               float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
               float issuePriceTaxVal ="".equals(issuePriceTax) ? 0.0f :  Float.parseFloat(issuePriceTax);
               float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
               
               if(unitPriceVal==0f){
            	   unitPrice="0.00000";
               }else{
            	   unitPrice=unitPrice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(issuePriceVal==0f){
            	   issuePriceValue="0.00000";
               }else{
            	   issuePriceValue=issuePriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(ordqtyVal==0f){
            	   ordqty="0.00000";
               }else{
            	   ordqty=ordqty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(pickQtyVal==0f){
            	   pickQty="0.000";
               }else{
            	   pickQty=pickQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(issueQtyVal==0f){
            	   issueQty="0.000";
               }else{
            	   issueQty=issueQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(taxVal==0f){
            	   taxValue="0.000";
               }else{
            	   taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(issuePriceTaxVal==0f){
            	   issuePriceTax="0.00000";
               }else{
            	   issuePriceTax=issuePriceTax.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
               }if(gstpercentageVal==0f){
            	   gstpercentageValue="0.000";
               }else{
//             	   gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
            	   gstpercentageValue=(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
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
	           rowData[rowData.length] = '<%=ordqty%>';
	           rowData[rowData.length] = '<%=pickQty %>';
	           rowData[rowData.length] = '<%= issueQty%>';
	           rowData[rowData.length] = '<%=uom%>';
	           rowData[rowData.length] = '<%=(String)lineArr.get("lnstat") %>';
	           rowData[rowData.length] = '<%=unitPrice %>';
	           rowData[rowData.length] = '<%=gstpercentageValue%>';
	           rowData[rowData.length] = '<%=issuePriceValue %>';
	           rowData[rowData.length] = '<%=taxValue%>';
	           rowData[rowData.length] = '<%=issuePriceTax%>';
	           rowData[rowData.length] = '<%=(String)lineArr.get("users") %>';
	           rowData[rowData.length] = '<%=(String)lineArr.get("remark") %>';
	           tableData[tableData.length] = rowData;
	           <%}%>    
	           <%-- <%}%> --%>
	           var groupColumn = 0;
	           $(document).ready(function(){
	        	    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	        	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	        	   /* if (document.form1.FROM_DATE.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_FROMDATE', '','FROM_DATE');
	              }
	        	   if (document.form1.TO_DATE.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_TODATE', '','TO_DATE');
	              }  */
	        	   if (document.form1.CUSTOMER.value == ''){
	        			getLocalStorageValue('GoodsIssueSummaryWithPrice_CUSTOMER', '','CUSTOMER');
	              } 	
	        	   if (document.form1.ORDERNO.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_ORDERNO','','ORDERNO');
	              } 
	        	   if (document.form1.ITEM.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_ITEM','', 'ITEM');
	              } 	
	        	   if (document.form1.PRD_CLS_ID.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_PRD_CLS_ID','', 'PRD_CLS_ID');
	              } 

	        	   if (document.form1.PRD_DEPT_ID.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_PRD_DEPT_ID','', 'PRD_DEPT_ID');
	              } 	
	        	   if (document.form1.PRD_TYPE_ID.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_PRD_TYPE_ID','', 'PRD_TYPE_ID');
	              }	
	        	   if (document.form1.PRD_BRAND_ID.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_PRD_BRAND_ID','', 'PRD_BRAND_ID');
	              }	
	        	   if (document.form1.ORDERTYPE.value == ''){
	        			getLocalStorageValue('GoodsIssueSummaryWithPrice_ORDERTYPE','','ORDERTYPE');
	        		}	
	        	   if (document.form1.BATCH.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_BATCH','','BATCH');
	        		}
	        	   if (document.form1.FILTER.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_FILTER', '','FILTER');
	        		}
	        	   if (document.form1.REASONCODE.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_REASONCODE','', 'REASONCODE');
	        		}
	        	   if (document.form1.INVOICENO.value == ''){
	        			getLocalStorageValue('GoodsIssueSummaryWithPrice_INVOICENO','', 'INVOICENO');
	        		}	
	        	   if (document.form1.LOC.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_LOC', '','LOC');
	        		}	
	        	   if (document.form1.LOC_TYPE_ID.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_LOC_TYPE_ID', '','LOC_TYPE_ID');
	        		}	
	        	   if (document.form1.LOC_TYPE_ID2.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_LOC_TYPE_ID2','','LOC_TYPE_ID2');
	        		}	
	        	   if (document.form1.LOC_TYPE_ID3.value == ''){
	        		   getLocalStorageValue('GoodsIssueSummaryWithPrice_LOC_TYPE_ID3','', 'LOC_TYPE_ID3');
	        		}	
	        	   storeUserPreferences();	
	        	    }
				   for(var rowIndex = 0; rowIndex < tableData.length; rowIndex ++){
					   	 tableData[rowIndex][8] = parseFloat(tableData[rowIndex][8]).toFixed("3");
	        			 tableData[rowIndex][9] = parseFloat(tableData[rowIndex][9]).toFixed("3");
	        			 tableData[rowIndex][10] = parseFloat(tableData[rowIndex][10]).toFixed("3");
	        			 
	        			 tableData[rowIndex][13] = parseFloat(tableData[rowIndex][13]).toFixed(<%=numberOfDecimal%>);
	        			 tableData[rowIndex][15] = parseFloat(tableData[rowIndex][15]).toFixed(<%=numberOfDecimal%>);
	        			 tableData[rowIndex][16] = parseFloat(tableData[rowIndex][16]).toFixed(<%=numberOfDecimal%>);
	        			 tableData[rowIndex][17] = parseFloat(tableData[rowIndex][17]).toFixed(<%=numberOfDecimal%>);
	        		 } 
	        	   
	          	 $('#tableIssueSummary').DataTable({
	          		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 	          		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
	          		  	data: tableData,
	          		  	"columnDefs": [{"className": "t-right", "targets": [7]}],
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
	          	        "drawCallback": function ( settings ) {
					var groupColumn = 0;
					var groupRowColSpan= 8;
				   	var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalPickQty = 0;
		            var groupTotalPickQty = 0;
		            var totalIssueQty = 0;
		            var groupTotalIssueQty = 0;
		            var totalIssuePriceQty = 0;
		            var groupTotalIssuePriceQty = 0;
		            var totalTax = 0;
		            var totalunitPrice = 0;
		            var groupTotalUnitPrice=0;
		            var groupTotalTax = 0;
		            var totalPrice = 0;
		            var groupPrice = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            var totalGstTax=0;
		            var groupTotalGstTax=0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		               /*  var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g; */
		                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
		                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null;
		               
		                	if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(3);
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(3);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(3);
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(3);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}
		                	if (i > 0) {
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td></td><td></td><td></td><td></td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupPriceVal + '</td><td></td><td></td></tr>'
				                    );
		                	}
		                    last = group;
		                    groupEnd = i;    
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalIssuePriceQty = 0;
		                    groupTotalTax = 0;
		                    groupPrice = 0;
		                    totalunitPrice=0;
		                    groupTotalUnitPrice=0;
		                    groupTotalGstTax=0;
		                    
		                }
		                /* groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalunitPrice += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                groupTotalUnitPrice += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                totalGstTax += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                groupTotalGstTax += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', '')); */
		                
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));   
		                
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	
		            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
		            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,
		            	groupPriceVal=null,totalUnitPriceVal=null,groupTotalUnitPriceVal=null,groupTotalGstTaxVal=null,totalGstTaxVal=null;
		            	
		            	if(totalPickQty==null || totalPickQty==0){
		            		totalPickQtyVal="0.000";
	                	}else{
	                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(3);
	                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
	                		groupTotalPickQtyVal="0.000";
	                	}else{
	                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(3);
	                	}if(totalIssueQty==null || totalIssueQty==0){
	                		totalIssueQtyVal=parseFloat("0.000").toFixed(3);
	                	}else{
	                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(3);
	                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
	                		groupTotalIssueQtyVal="0.000";
	                	}else{
	                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(3);
	                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
	                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
	                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
	                	}if(totalTax==null || totalTax==0){
	                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalTax==null || groupTotalTax==0){
	                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
	                	}if(totalPrice==null || totalPrice==0){
	                		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupPrice==null || groupPrice==0){
	                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalunitPrice==null || totalunitPrice==0){
	                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalUnitPrice==null || groupTotalUnitPrice==0){
	                		groupTotalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalUnitPriceVal=parseFloat(groupTotalUnitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalGstTax==null || totalGstTax==0){
	                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(3);
	                	}if(groupTotalGstTax==null || groupTotalGstTax==0){
	                		groupTotalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalGstTaxVal=parseFloat(groupTotalGstTax).toFixed(3);
	                	}
		            	/* $(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + totalPickQtyVal + '</td><td>' + totalIssueQtyVal + '</td><td></td><td></td><td>'+totalUnitPriceVal+'</td><td>'+totalGstTaxVal+'</td><td>' + totalIssuePriceQtyVal + '</td><td>' + totalTaxVal + '</td><td>' + totalPriceVal + '</td><td></td></tr>'
	                    );
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td></td><td></td><td>'+groupTotalUnitPriceVal+'</td><td>'+groupTotalGstTaxVal+'</td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupPriceVal + '</td><td></td></tr>'
	                    ); */
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + totalPickQtyVal + '</td><td>' + totalIssueQtyVal + '</td><td></td><td></td><td></td><td></td><td>' + totalIssuePriceQtyVal + '</td><td>' + totalTaxVal + '</td><td>' + totalPriceVal + '</td><td></td><td></td><td></td></tr>'
	                    );
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td></td><td></td><td></td><td></td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupPriceVal + '</td><td></td><td></td></tr>'
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
    	            totalqty = api
    	              .column(9)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);

    	            // Total over this page
    	            totalissQty = api
    	              .column(10)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
    	              
    	            totalissCostVal = api
    	              .column(15)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
    	              
    	            totaltaxVal = api
    	              .column(16)
    	              .data()
    	              .reduce(function(a, b) {
    	                return intVal(a) + intVal(b);
    	              }, 0);
  	              
    	            totalVal = api
    	              .column(17)
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
		            var totalIssuePriceQty = 0;
		            var groupTotalIssuePriceQty = 0;
		            var totalTax = 0;
		            var totalunitPrice = 0;
		            var groupTotalUnitPrice=0;
		            var groupTotalTax = 0;
		            var totalPrice = 0;
		            var groupPrice = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            var totalGstTax=0;
		            var groupTotalGstTax=0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		               /*  var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g; */
		                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
		                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null;
		               
		                	if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(3);
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(3);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(3);
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(3);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}
		                	
		                    last = group;
		                    groupEnd = i;    
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalIssuePriceQty = 0;
		                    groupTotalTax = 0;
		                    groupPrice = 0;
		                    totalunitPrice=0;
		                    groupTotalUnitPrice=0;
		                    groupTotalGstTax=0;
		                    
		                }
		                /* groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalunitPrice += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                groupTotalUnitPrice += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                totalGstTax += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                groupTotalGstTax += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', '')); */
		                
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));   
		                
		                currentRow = i;
		            } );

    	            // Update footer
    	            $(api.column(9).footer()).html(parseFloat(groupTotalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(10).footer()).html(parseFloat(groupTotalIssueQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(15).footer()).html(parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(16).footer()).html(parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(17).footer()).html(parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>));
<%--     	            $(api.column(9).footer()).html(parseFloat(totalqty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(10).footer()).html(parseFloat(totalissQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
    	            $(api.column(15).footer()).html(parseFloat(totalissCostVal).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(16).footer()).html(parseFloat(totaltaxVal).toFixed(<%=numberOfDecimal%>));
    	            $(api.column(17).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); --%>
    	          }  
			  });	 
	 });
	 
	 
	
	
	</script>
      
  </FORM>
  </div></div></div>
  
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>

 function onGo(){
	   var flag    = "false";
	   var FROM_DATE      = document.form1.FROM_DATE.value;
	   var TO_DATE        = document.form1.TO_DATE.value;
	   var DIRTYPE        = document.form1.DIRTYPE.value;
	   var USER           = document.form1.CUSTOMER.value;
	   var ITEMNO         = document.form1.ITEM.value;
	   var ORDERNO        = document.form1.ORDERNO.value;
	   var JOBNO          = document.form1.JOBNO.value;
	   var PRD_BRAND_ID   = document.form1.PRD_BRAND_ID.value;
	   var PRD_TYPE_ID    = document.form1.PRD_TYPE_ID.value;
	   var PRD_CLS_ID     = document.form1.PRD_CLS_ID.value;
	   var PRD_DEPT_ID     = document.form1.PRD_DEPT_ID.value;
	   var REASONCODE     = document.form1.REASONCODE.value;
	   var INVOICENO     = document.form1.INVOICENO.value;
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
	   if(PRD_DEPT_ID != null     && PRD_DEPT_ID != "") { flag = true;}
	   if(REASONCODE != null     && REASONCODE != "") { flag = true;}
	   var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	   if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
		   storeUserPreferences();
	   }
	   document.form1.action="../salesorder/issuesummaryprice";
	   document.form1.submit();
	}
 
 function storeUserPreferences(){
	    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
		   //storeInLocalStorage('GoodsIssueSummaryWithPrice_FROMDATE', $('#FROM_DATE').val());
			//storeInLocalStorage('GoodsIssueSummaryWithPrice_TODATE', $('#TO_DATE').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_CUSTOMER', $('#CUSTOMER').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_ORDERNO', $('#ORDERNO').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_ITEM', $('#ITEM').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_PRD_CLS_ID', $('#PRD_CLS_ID').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_ORDERTYPE',$('#ORDERTYPE').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_BATCH',$('#BATCH').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_FILTER', $('#FILTER').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_REASONCODE', $('#REASONCODE').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_INVOICENO', $('#INVOICENO').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_LOC',  $('#LOC').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_LOC_TYPE_ID',  $('#LOC_TYPE_ID').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
			storeInLocalStorage('GoodsIssueSummaryWithPrice_LOC_TYPE_ID3',  $('#LOC_TYPE_ID3').val());
	}
	}

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
  