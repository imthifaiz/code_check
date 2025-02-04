<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Order Sales Summary";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes", "unchecked"})%>
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
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/JsBarcode.all.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
    
   document.form1.action = "/track/deleveryorderservlet?Submit=ExportOutboundSalesSummary";
   document.form1.submit();

}


function onGo(){
    //storeUserPreferences();
document.form1.action="../salesorder/salessummary";
document.form1.submit();
}

/* function storeUserPreferences(){
	storeInLocalStorage('OutboundOrderSalesSummary_FROMDATE', $('#FROM_DATE').val());
	storeInLocalStorage('OutboundOrderSalesSummary_TODATE', $('#TO_DATE').val());
	storeInLocalStorage('OutboundOrderSalesSummary_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('OutboundOrderSalesSummary_ITEM', $('#ITEM').val());
	//storeInLocalStorage('OutboundOrderSalesSummary_SORT',$('#SORT').val());
} */

function changetype(count){
	  $("input[name ='RADIOSEARCH']").val(count);
	  onGo();
	}
function changepostype(count){
	  $("input[name ='POSSEARCH']").val(count);
	  onGo();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ArrayList invQryListSumTotal  = new ArrayList();
ShipHisDAO shipdao = new ShipHisDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();


String fieldDesc="";
String USERID ="",PLANT="",ITEM = "",CUSTOMER="",PRD_DESCRIP="", QTY ="",FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",RADIOSEARCH="1",LOC="",POSSEARCH="1";
String html = "",SORT="";
int Total=0,STOCK_EXPIRE_INT=0,STOCK_CLAIM_INT=0, STOCK_FLOATING_INT=0,STOCK_SALE_INT=0;
String SumColor="";
boolean flag=false;

String resultDesc =  StrUtils.fString(request.getParameter("result"));
String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
CUSTOMER    = StrUtils.fString(request.getParameter("CUSTOMER"));
LOC    = StrUtils.fString(request.getParameter("LOC"));
SORT = StrUtils.fString(request.getParameter("SORT"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
RADIOSEARCH = StrUtils.fString(request.getParameter("RADIOSEARCH"));
String ENABLE_POS = plantMstDAO.getispos(PLANT);
POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
if(RADIOSEARCH.equalsIgnoreCase("") || RADIOSEARCH.equalsIgnoreCase("null"))
	RADIOSEARCH="2";
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null"))
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
//System.out.println("RADIOSEARCH:"+RADIOSEARCH);
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
boolean displaySummaryExport=false,displaySummaryEdit=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportobsalessmry", PLANT,LOGIN_USER);
	displaySummaryEdit = ub.isCheckValAcc("edituitem", PLANT,LOGIN_USER);
	displaySummaryExport=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportobsalessmry", PLANT,LOGIN_USER);
	displaySummaryEdit = ub.isCheckValinv("edituitem", PLANT,LOGIN_USER);
	displaySummaryExport=true;
}
// String curDate =new DateUtils().getDate();
String curDate =DateUtils.getDateMinusDays();//resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;
if(SORT.equals(""))
{
	SORT="PRODUCT";
}
ItemMstUtil itemMstUtil = new ItemMstUtil();

itemMstUtil.setmLogger(mLogger);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);

boolean cntRec=false;

if(PGaction.equalsIgnoreCase("View")){
 try{
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
         
      //invQryList = shipdao.getproductcustomersaleswithzeroqty(PLANT,ITEM,PRD_DESCRIP,CUSTOMER,SORT,FROM_DATE,TO_DATE);
      //if(invQryList.size() <=0)
      //{
    	  //cntRec =true;

        //fieldDesc="Data's Not Found";
       
     // }
      
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
}

String collectionDate=DateUtils.getDate();
String printdate=DateUtils.getDateFormatYYMMDD();
ArrayList al = plantMstDAO.getPlantMstDetails(PLANT);
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

String LabelType="Single",BarcodeWidth="3",BarcodeHeight="60",FontSize="30",TextAlign="Center",TextPosition="Bottom",DisplayText="Show";
ArrayList arrCust =new MasterDAO().getBarcodePrint("PRDBARCODE", PLANT, "");
if (arrCust.size() > 0) {
    for(int i =0; i<arrCust.size(); i++) {
    	Map arrCustLine = (Map)arrCust.get(i);
    	LabelType=(String)arrCustLine.get("LABEL_TYPE");
    	BarcodeWidth=(String)arrCustLine.get("BARCODE_WIDTH");
    	BarcodeHeight=(String)arrCustLine.get("BARCODE_HEIGHT");
    	FontSize=(String)arrCustLine.get("FONT_SIZE");
    	TextAlign=(String)arrCustLine.get("TEXT_ALIGN");
    	TextPosition=(String)arrCustLine.get("TEXT_POSITION");
    	DisplayText=(String)arrCustLine.get("DISPLAY_BARCODE_TEXT");
    }            
    }

%>
<center>
	<h2><small class="success-msg"><%=resultDesc%></small></h2>
</center>	
<div class="container-fluid m-t-20">
	<div class="box">
	
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                                 
                <li><label>Sales Order Sales Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                                      <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <%if(displaySummaryExport){ %>
  <!--                 <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button> --> 
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul>					 -->
					  <%} %>
				</div>

				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/salessummary">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
  
  <span style="text-align: center;"><small><font color="red"> <%=fieldDesc%> </font></small></span>
  
  <%-- <center><h1><small><font color="red"> <%=fieldDesc%> </font></small></h1></center> --%>
  
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER NAME" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" >				
                  <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
										onclick="changecustomer(this)">
										<i class="glyphicon glyphicon-menu-down"
											style="font-size: 8px;"></i>	</button>
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
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control typehead" placeholder="PRODUCT" value="<%=StrUtils.forHTMLTag(ITEM)%>" >
		
<button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
										onclick="changeproduct(this)">
										<i class="glyphicon glyphicon-menu-down"
											style="font-size: 8px;"></i>	</button>
		<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
  		<INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type ="TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>"  placeholder="LOCATION" size="30"  MAXLENGTH=20>
            <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changeloc(this)">
			<i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			</button>  		
		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
  		</div>				
  		</div>
		<div class="col-sm-4 ac-box">
  		<label class="radio-inline">
      	<INPUT  name="SORT" id="SORT" type = "radio" value="PRODUCT"    <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>checked <%}%>>By Product</label>
   		<label class="radio-inline">
   		<INPUT name="SORT" id="SORT" type = "radio"  value="CUSTOMER"   <%if(SORT.equalsIgnoreCase("CUSTOMER")) {%>checked <%}%>>By Customer</label>
  		</div>
  		</div>
<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>">
<input type="hidden" name="RADIOSEARCH" value="<%=StrUtils.forHTMLTag(RADIOSEARCH)%>">	
<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>">	
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
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
 	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>
       	  
       	  <div class="form-group">
<!--    <div class="col-sm-12 ac-box">  		 -->
					<label class="control-label col-sm-1" for="view">View By :</label>
				  	<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="1" id="all" <%if(RADIOSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changetype(this.value)"> <b>All Product</b></label>
  					<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="2" id="done" <%if(RADIOSEARCH.equalsIgnoreCase("2")) {%>checked <%}%> onclick="changetype(this.value)"> <b>Sales Product</b></label>
  					<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="3" id="notdone" <%if(RADIOSEARCH.equalsIgnoreCase("3")) {%>checked <%}%> onclick="changetype(this.value)"> <b>Zero Sales Product</b></label>
<!-- 	</div> -->
		</div>
       	  <div class="form-group">
       	  <% if(ENABLE_POS.equals("1")){ %>
					<label class="control-label col-sm-1" for="view"></label>
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
			
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
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
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
         <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
       	  <th style="font-size: smaller;">PRODUCT ID</TH>
          <th style="font-size: smaller;">DESCRIPTION</TH>
          <th style="font-size: smaller;">LIST PRICE</TH>
          <th style="font-size: smaller;">COST</TH>
          <th style="font-size: smaller;">SALES QUANTITY</TH>
          <th style="font-size: smaller;">INV QTY</TH>
          <th style="font-size: smaller;">AMOUNT</TH>
          <th style="font-size: smaller;">TAX</TH>
          <th style="font-size: smaller;">TOTAL AMOUNT</TH>
          <th style="font-size: smaller;">TOTAL COST</TH>
<!--           <th style="font-size: smaller;">Average COST</TH> -->
          <th style="font-size: smaller;">GP %</th>
          <th style="font-size: smaller;">LOW</th>
          <th style="font-size: smaller;">SEL</th>
          <th style="font-size: smaller;">CATALOG</th>
          <th style="font-size: smaller;">PRINT BARCODE</th>
          <th style="font-size: smaller;">EDIT</th>
     <%} else{ %>     
          <th style="font-size: smaller;">CUSTOMER ID</TH>
          <th style="font-size: smaller;">CUSTOMER NAME</TH>
          <th style="font-size: smaller;">SALES QUANTITY</TH>
          <th style="font-size: smaller;">INV QTY</TH>
          <th style="font-size: smaller;">TOTAL AMOUNT</TH>  
      <% }%>   
         
         
                     </tr>
		            </thead>
		            
		            <tbody>
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='1'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
<!-- 		            <th style="text-align: left !important"></th> -->
		            <th style="text-align: left !important"></th>
		            <% }%>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  
  <div class="form-group">
  	<div class="col-sm-12" align="center"> 
  	<button type="button" class="Submit btn btn-success"  value="Print"  name="action" onClick="return discnt()"><b>DISCONTINUE</b></button>&nbsp;&nbsp;
  	 </div>
  	</div>
<script>
  var tableData = [];
  var RADIOSEARCH = '<%=RADIOSEARCH%>';  
  var POSSEARCH = '<%=POSSEARCH%>';  
       <%
	   invQryList = shipdao.getproductcustomersaleswithzeroqty(PLANT,ITEM,PRD_DESCRIP,CUSTOMER,LOC,SORT,FROM_DATE,TO_DATE,POSSEARCH,RADIOSEARCH);
       if(invQryList.size() <=0)
       {
     	  cntRec =true;

         fieldDesc="Data's Not Found";
        
       }
          int j=0;
          double Totalqty=0;
          double Totalinvqty=0;
          double Totalprice=0;
          double Totcost=0;
          double GP=0;
          String rowColor="";
          DecimalFormat decformat = new DecimalFormat("#,##0.00");
          Hashtable htship = new Hashtable();
          htship.put("PLANT",PLANT);
          Hashtable htexp = new Hashtable();
          shipdao.setmLogger(mLogger);
          int total_stock_claim = 0;
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 rowColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#FFFFFF"; 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                
                double TOTQTYOR=0,BTOTQTYOR=0,BTOTQTY=0,STKVAL=0,TOTAMT=0,TOTTAX=0;
                if(SORT.equalsIgnoreCase("PRODUCT")) {
                TOTQTYOR = Double.parseDouble((String)lineArr.get("TOTQTYOR"));
                BTOTQTYOR = Double.parseDouble((String)lineArr.get("BTOTQTYOR"));
                BTOTQTY = Double.parseDouble((String)lineArr.get("BTOTQTY"));
                STKVAL = 0;
                STKVAL = (BTOTQTYOR-BTOTQTY)+(TOTQTYOR);
                
                
                
                TOTAMT = Double.parseDouble((String)lineArr.get("TOTAMT"));
                TOTAMT = StrUtils.RoundDB(TOTAMT,5);
                
                TOTTAX = Double.parseDouble((String)lineArr.get("TOTTAX"));
                TOTTAX = StrUtils.RoundDB(TOTTAX,5);
                }
                
                double TOTPRICE = Double.parseDouble((String)lineArr.get("TOTPRICE"));
                TOTPRICE = StrUtils.RoundDB(TOTPRICE,5);
                
                Totalqty = Totalqty+Double.parseDouble((String)lineArr.get("TOTQTY"));
                Totalqty = StrUtils.RoundDB(Totalqty,2);
                
                /* Totalinvqty = Totalinvqty+Double.parseDouble((String)lineArr.get("QTY"));
                Totalinvqty = StrUtils.RoundDB(Totalinvqty,2); */
                
                Totalprice = Totalprice+Double.parseDouble((String)lineArr.get("TOTPRICE"));
                Totalprice = StrUtils.RoundDB(Totalprice,5);
                
                String daysDifference = "";
                String UPAT="";
                if(SORT.equalsIgnoreCase("PRODUCT")) {
                	
                UPAT = (String)lineArr.get("UPAT");
				if (!UPAT.isEmpty()) {
                	if (UPAT.length() >= 8) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                        LocalDate upatDate = null;
                        try{
                        upatDate = LocalDate.parse(UPAT.substring(0, 8), formatter);
                        } catch (Exception e) {
                        formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                        upatDate = LocalDate.parse(UPAT.substring(0, 8), formatter);                        	
                        }
                        	
                        
                        LocalDate currentDate = LocalDate.now();
                        //System.out.println("currentDate: " + currentDate);
                        daysDifference = String.valueOf(ChronoUnit.DAYS.between(upatDate, currentDate));
                        long daysDifference1 = ChronoUnit.DAYS.between(currentDate, upatDate);

                        //System.out.println("Difference in days: " + daysDifference);
                        //System.out.println("Difference in days: " + daysDifference1);
                    } 
                }
                }
              
                String totQtyValue = (String)lineArr.get("TOTQTY");
                String totInvQtyValue = (String)lineArr.get("QTY");
                String cost="0"; 
                String price ="0";
                String gp ="0";
                if(SORT.equalsIgnoreCase("PRODUCT")) {
                Totcost=Double.parseDouble((String)lineArr.get("TOTCOST"));
                cost = (String)lineArr.get("COST");
                price = (String)lineArr.get("UNITPRICE");
                
                /*GP = Totalqty+Double.parseDouble((String)lineArr.get("TOTCOST"));
                GP = (GP-TOTPRICE)/(GP*100);*/
                GP =  (Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTPRICE"))) - Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTCOST")))) / Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTPRICE"))) ;
                GP = GP*100;
                gp = String.valueOf(GP);
                if(Double.isNaN(GP))
                	GP=0.0;
                //System.out.println(gp);
                gp = StrUtils.addZeroes(GP, "3");
                //System.out.println(gp);
                if(gp.contains("-")){
	                gp = gp.replace("-", "");
                }else{
                	gp = gp;
                }                
                }
                String PriceValue = String.valueOf(TOTAMT);
                String taxValue = String.valueOf(TOTTAX);
                String totalPriceValue = String.valueOf(TOTPRICE);
                String totalCostValue = String.valueOf(Totcost);
                
                float totQtyVal="".equals(totQtyValue) ? 0.0f :  Float.parseFloat(totQtyValue);
                float totInvQtyVal="".equals(totInvQtyValue) ? 0.0f :  Float.parseFloat(totInvQtyValue);
                /* float totalPriceVal="".equals(totalPriceValue) ? 0.0f :  Float.parseFloat(totalPriceValue); */
                double PriceVal ="".equals(PriceValue) ? 0.0d :  Double.parseDouble(PriceValue);
                double taxVal ="".equals(taxValue) ? 0.0d :  Double.parseDouble(taxValue);
                double totalPriceVal ="".equals(totalPriceValue) ? 0.0d :  Double.parseDouble(totalPriceValue);
                double costVal ="".equals(cost) ? 0.0d :  Double.parseDouble(cost);
                double priceVal ="".equals(price) ? 0.0d :  Double.parseDouble(price);
                
                if (totQtyVal == 0f) {
                	totQtyValue = "0.000";
				} else {
					totQtyValue = totQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				}
                 if (totInvQtyVal == 0f) {
                	totInvQtyValue = "0.000";
				} else {
					totInvQtyValue = totInvQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				} /* if (totalPriceVal == 0f) {
					totalPriceValue = "0.00000";
				} else {
					totalPriceValue = totalPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				} */
                totQtyValue = StrUtils.addZeroes(totQtyVal, "3");
                totInvQtyValue = StrUtils.addZeroes(totInvQtyVal, "3");
                PriceValue = StrUtils.addZeroes(PriceVal, numberOfDecimal);
                taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
                totalPriceValue = StrUtils.addZeroes(totalPriceVal, numberOfDecimal);
                cost = StrUtils.addZeroes(costVal, numberOfDecimal);
                price = StrUtils.addZeroes(priceVal, numberOfDecimal);
                totalCostValue = StrUtils.addZeroes(Totcost, numberOfDecimal);
                
                //added average cost in sales order sales summary report
//              String convertedcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(PLANT,"",(String)lineArr.get("NAME"));
//              double sconvertedcost=Double.parseDouble(convertedcost);
//              convertedcost = StrUtils.addZeroes(sconvertedcost, numberOfDecimal);
                          
          %>
          var totQty = <%=totQtyVal%>;
          var stkval = <%=STKVAL%>;
          var rowData = [];
          if(RADIOSEARCH==='1'){
          rowData[rowData.length] = '<%=(String)lineArr.get("ID") %>';
          rowData[rowData.length] = '<%=(String)lineArr.get("NAME")%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=price%>';
          rowData[rowData.length] = '<%=cost%>';
          <% }%>
          rowData[rowData.length] = '<%=totQtyValue%>';
          rowData[rowData.length] = '<%=totInvQtyValue%>';
          rowData[rowData.length] = '<%=PriceValue%>';
          rowData[rowData.length] = '<%=taxValue%>';
          rowData[rowData.length] = '<%=totalPriceValue%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=totalCostValue%>';
<%--           rowData[rowData.length] = '<%=convertedcost%>'; --%>
          rowData[rowData.length] = '<%=gp%>';
<%--           <%if(totInvQtyValue.equalsIgnoreCase("0")) {%> --%>
<%--           <%}%> --%>
          <%if(totInvQtyVal!=0) {%>
          <%if(totInvQtyValue.contains("-")) {%>
          rowData[rowData.length] = '<%=daysDifference%>';
          <%}else{%>
          rowData[rowData.length] = '';
          <%}%>
          rowData[rowData.length] = '';
          <%}else{%>
          rowData[rowData.length] = '<%=daysDifference%>';
          rowData[rowData.length] = '<INPUT Type=checkbox style=border: 0; name=CHKSTKID value="<%=(String)lineArr.get("ID")%>" >';
          <%}%>
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showImage(\'<%=(String)lineArr.get("CATALOG")%>\')"><i class="fa fa-eye"></i></a>';
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showPrintBarcode(\'<%=(String)lineArr.get("ID")%>\',\'<%=(String)lineArr.get("NAME")%>\',\'<%=price%>\')"><i class="fa fa-barcode"></i></a>';
          <%if(displaySummaryEdit){ %>
          rowData[rowData.length] = '<a href="../product/edit?RTYPE=SALES&ITEM=<%=(String)lineArr.get("ID")%>"><i class="fa fa-pencil-square-o"></i></a>';
          <%}else{ %>
          rowData[rowData.length] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
          <%} }%>
          tableData[tableData.length] = rowData;
          } else if(RADIOSEARCH==='2') {
        	  if(totQty!=0){
          rowData[rowData.length] = '<%=(String)lineArr.get("ID") %>';
          rowData[rowData.length] = '<%=(String)lineArr.get("NAME")%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=price%>';
          rowData[rowData.length] = '<%=cost%>';
          <% }%>
          rowData[rowData.length] = '<%=totQtyValue%>';
          rowData[rowData.length] = '<%=totInvQtyValue%>';
          rowData[rowData.length] = '<%=PriceValue%>';
          rowData[rowData.length] = '<%=taxValue%>';
          rowData[rowData.length] = '<%=totalPriceValue%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=totalCostValue%>';
<%--           rowData[rowData.length] = '<%=convertedcost%>'; --%>
          rowData[rowData.length] = '<%=gp%>';
          <%if(totInvQtyVal!=0) {%>
          <%if(totInvQtyValue.contains("-")) {%>
          rowData[rowData.length] = '<%=daysDifference%>';
          <%}else{%>
          rowData[rowData.length] = '';
          <%}%>
          rowData[rowData.length] = '';
          <%}else{%>
          rowData[rowData.length] = '<%=daysDifference%>';
          rowData[rowData.length] = '<INPUT Type=checkbox style=border: 0; name=CHKSTKID value="<%=(String)lineArr.get("ID")%>" >';
          <%}%>
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showImage(\'<%=(String)lineArr.get("CATALOG")%>\')"><i class="fa fa-eye"></i></a>';
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showPrintBarcode(\'<%=(String)lineArr.get("ID")%>\',\'<%=(String)lineArr.get("NAME")%>\',\'<%=price%>\')"><i class="fa fa-barcode"></i></a>';
          <%if(displaySummaryEdit){ %>
          rowData[rowData.length] = '<a href="../product/edit?RTYPE=SALES&ITEM=<%=(String)lineArr.get("ID")%>"><i class="fa fa-pencil-square-o"></i></a>';
          <%}else{ %>
          rowData[rowData.length] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
          <%} }%>
          tableData[tableData.length] = rowData;
            	  }
          } else if(RADIOSEARCH==='3') {
        	  if(totQty===0 && stkval != 0){
          rowData[rowData.length] = '<%=(String)lineArr.get("ID") %>';
          rowData[rowData.length] = '<%=(String)lineArr.get("NAME")%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=price%>';
          rowData[rowData.length] = '<%=cost%>';
          <% }%>
          rowData[rowData.length] = '<%=totQtyValue%>';
          rowData[rowData.length] = '<%=totInvQtyValue%>';
          rowData[rowData.length] = '<%=PriceValue%>';
          rowData[rowData.length] = '<%=taxValue%>';
          rowData[rowData.length] = '<%=totalPriceValue%>';
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          rowData[rowData.length] = '<%=totalCostValue%>';
<%--           rowData[rowData.length] = '<%=convertedcost%>'; --%>
          rowData[rowData.length] = '<%=gp%>';
          <%if(totInvQtyVal!=0) {%>
          <%if(totInvQtyValue.contains("-")) {%>
          rowData[rowData.length] = '<%=daysDifference%>';
          <%}else{%>
          rowData[rowData.length] = '';
          <%}%>
          rowData[rowData.length] = '';
          <%}else{%>
          rowData[rowData.length] = '<%=daysDifference%>';
          rowData[rowData.length] = '<INPUT Type=checkbox style=border: 0; name=CHKSTKID value="<%=(String)lineArr.get("ID")%>" >';
          <%}%>
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showImage(\'<%=(String)lineArr.get("CATALOG")%>\')"><i class="fa fa-eye"></i></a>';
          rowData[rowData.length] = '<a href="javascript:void(0);" onclick="showPrintBarcode(\'<%=(String)lineArr.get("ID")%>\',\'<%=(String)lineArr.get("NAME")%>\',\'<%=price%>\')"><i class="fa fa-barcode"></i></a>';
          <%if(displaySummaryEdit){ %>
          rowData[rowData.length] = '<a href="../product/edit?RTYPE=SALES&ITEM=<%=(String)lineArr.get("ID")%>"><i class="fa fa-pencil-square-o"></i></a>';
          <%}else{ %>
          rowData[rowData.length] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
          <%} }%>
          tableData[tableData.length] = rowData;
            	  }
          }
          <%}%>    
          var groupColumn = 1;
          var orderColumn = 2;
          var orderColumn1 = 3;
          var orderColumn2 = 4;
          var orderColumn3 = 7;
          var orderColumn4 = 8;
          var orderColumn5 = 9;
          var orderColumn6 = 10;
          <%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
          	orderColumn = 4;
          	orderColumn1 = 5;
          	orderColumn2 = 6;
          	orderColumn3 = 7;
          	orderColumn4 = 8;
          	orderColumn5 = 9;
          	orderColumn6 = 10;
          <% }%>
    $(document).ready(function(){
    	/*  if (document.form1.FROM_DATE.value == ''){
    		 getLocalStorageValue('OutboundOrderSalesSummary_FROMDATE', '', 'FROM_DATE');
        }
    	 if (document.form1.TO_DATE.value == ''){
    		 getLocalStorageValue('OutboundOrderSalesSummary_TODATE', '', 'TO_DATE');
        }
    	 if (document.form1.CUSTOMER.value == ''){
    		 getLocalStorageValue('OutboundOrderSalesSummary_CUSTOMER', '', 'CUSTOMER');
        }
    	 if (document.form1.ITEM.value == ''){
    		 getLocalStorageValue('OutboundOrderSalesSummary_ITEM', '', 'ITEM');
        }
    	 if (document.form1.SORT.value == ''){
    		 getLocalStorageValue('OutboundOrderSalesSummary_SORT', '', 'SORT');
        }

    	  storeUserPreferences(); */
   	 $('#tableIssueSummary').DataTable({
   		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
   		searching: true, // Enable searching
        search: {
            regex: true,   // Enable regular expression searching
            smart: false   // Disable smart searching for custom matching logic
        },
//    		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
   		  	data: tableData,
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
      	              title: function () { var dataview = "<%=title%> - Location: <%=LOC%>  "; return dataview },
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
                      	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=FROM_DATE%> - <%=TO_DATE%> \n Location: <%=LOC%>                                             ."  ; return dataview },
                    	<%} else {%>
                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=FROM_DATE%> - <%=TO_DATE%> \n Location: <%=LOC%>                                             ."  ; return dataview },
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
                      	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                     As On <%=FROM_DATE%> - <%=TO_DATE%> \n Location: <%=LOC%>                                                                                                         ."  ; return dataview },
                    	<%} else {%>
                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                     As On <%=FROM_DATE%> - <%=TO_DATE%> \n Location: <%=LOC%>                                                                                                         ."  ; return dataview },
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
                      extend: 'colvis'
                    	  //,columns: ':not(:eq(0)):not(:eq(1)):not(:last)'
                  }
   	        ], "drawCallback": function ( settings ) {
   				
   				var groupRowColSpan= 1;
   			   	var api = this.api();
   	            var rows = api.rows( {page:'current'} ).nodes();
   	            var last=null;
   	            var totalPrice = 0;
   	            var grouptotalPrice = 0;
   	            var totalTax = 0;
   	            var grouptotalTax = 0;
   	            var totalgb = 0;
   	            var groupTotalgb = 0;
   	            var totalcost = 0;
   	            var groupTotalcost = 0;
   	            var totalPickQty = 0;
   	            var groupTotalPickQty = 0;
   	            var totalIssueQty = 0;
   	            var groupTotalinvQty = 0;
   	            var totalinvQty = 0;
   	            var groupTotalIssueQty = 0;
   	            var groupEnd = 0;
   	            var currentRow = 0;
   	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
   	                if ( last !== group ) {
   	                	if (i > 0) {
   	                		/* $(rows).eq( i ).before(
   			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(3) + '</td></tr>'
   			                    ); */
   	                	}
   	                    last = group;
   	                    groupEnd = i;    
   	                    groupTotalPickQty = 0;
   	                    groupTotalIssueQty = 0;
   	                    
   	                    	                    
   	                }
   	             	<%if(SORT.equalsIgnoreCase("PRODUCT")) {%> 
   	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                groupTotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
   	                totalinvQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
   	                grouptotalPrice += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
   	                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
   	                grouptotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
   	             	totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
   	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
   	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
   	             	groupTotalcost += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
   	                totalcost += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
   	             	groupTotalgb += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
   	                totalgb += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
   	             	<% } else { %>
   	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
   	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
   	                groupTotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
   	                totalinvQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
   	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	             	<% }%>
   	                                
   	                currentRow = i;
   	            } );
   	         if (groupEnd > 0 || rows.length == (currentRow + 1)){
   	        	 		
   	        	 	var totalPickQtyVal=null,totalIssueQtyVal=null,totalinvQtyVal=null,totalgbVal=null,totalcostVal=null,totalpriceVal=null,totaltaxVal=null;
   	        	 	
   	        	 	if(totalcost==null || totalcost==0){
   	        	 		totalcostVal="0.000";
	        	 	}else{
	        	 		totalcostVal=parseFloat(totalcost).toFixed(<%=numberOfDecimal%>);
 	        		}if(totalgb==null || totalgb==0){
   	        	    	totalgbVal="0.000";
   	        	 	}else{
   	        	 		totalgbVal=parseFloat(totalgb).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
    	        	}if(totalPickQty==null || totalPickQty==0){
   	        	 	totalPickQtyVal="0.000";
   	        	 	}else{
   	        	 	//totalPickQtyVal=parseFloat(totalPickQty).toFixed(3).replace(/\.?0+$/, '');
   	        	 	totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}if(totalIssueQty==null || totalIssueQty==0){
   	        	 	totalIssueQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
   	        	 	}else{
   	        	 	totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
   	        	 	}
   	        	 	if(totalPrice==null || totalPrice==0){
   	        	 	totalpriceVal=parseFloat("0.00000").toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}else{
   	        	 	totalpriceVal=parseFloat(totalPrice).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}
   	        	 	if(totalTax==null || totalTax==0){
   	        	 	totaltaxVal=parseFloat("0.00000").toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}else{
   	        	 	totaltaxVal=parseFloat(totalTax).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}
   	        	 	if(totalinvQty==null || totalinvQty==0){
   	        	 	totalinvQtyVal=parseFloat("0.00000").toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}else{
   	        	 	totalinvQtyVal=parseFloat(totalinvQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>);
   	        	 	}
   	        			 
   	        	 var gpvalue = ((parseFloat(totalIssueQtyVal)-parseFloat(totalcostVal))/(parseFloat(totalIssueQtyVal)))*100;
        			if(isNaN(gpvalue))
        				gpvalue=0;        		 
        			gpvalue = parseFloat(gpvalue).toFixed("3");
   	            	 $(rows).eq( currentRow ).after(
   	            			<%if(SORT.equalsIgnoreCase("PRODUCT")) {%>
   	            			'<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td></td><td></td><td>' + totalPickQtyVal + '</td><td>'+ totalinvQtyVal +'</td><td>' + totalpriceVal + '</td><td>' + totaltaxVal + '</td><td>' + totalIssueQtyVal + '</td><td>' + totalcostVal + '</td><td>' + gpvalue + '</td><td></td><td></td><td></td><td></td><td></td></tr>'
   	                     <% } else {%>
   	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + totalPickQtyVal + '</td><td>'+ totalinvQtyVal +'</td><td>' + totalIssueQtyVal + '</td></tr>'
   	                     <% }%>
    	                        ); 
                  	/* $(rows).eq( currentRow ).after(
   	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(3) + '</td></tr>'
                      ); */
                  } 
   	        },
   	     "order": [[orderColumn, 'desc']],
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
	              .column(orderColumn)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalQty = api
	              .column(orderColumn1)
	    	        /*       , {
	                page: 'current'
	              }) */
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalAmt = api
             .column(orderColumn2)
                     /* , {
               page: 'current'
             }) */
             .data()
             .reduce(function(a, b) {
               return intVal(a) + intVal(b);
             }, 0);


	            var rows = api.rows( {page:'current'} ).nodes();
   	            var last=null;
   	            var totalgb = 0;
   	            var groupTotalgb = 0;
   	            var totalcost = 0;
   	            var groupTotalcost = 0;
   	            var totalPickQty = 0;
   	            var groupTotalPickQty = 0;
   	            var totalIssueQty = 0;
   	            var groupTotalinvQty = 0;
   	            var totalinvQty = 0;
   	            var groupTotalIssueQty = 0;
   	            var groupEnd = 0;
   	            var currentRow = 0;
   	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
   	                if ( last !== group ) {
   	                	if (i > 0) {
   	                		/* $(rows).eq( i ).before(
   			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(3) + '</td></tr>'
   			                    ); */
   	                	}
   	                    last = group;
   	                    groupEnd = i;    
   	                    groupTotalPickQty = 0;
   	                    groupTotalIssueQty = 0;
   	                    
   	                    	                    
   	                }
   	             	<%if(SORT.equalsIgnoreCase("PRODUCT")) {%> 
   	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                groupTotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
   	                totalinvQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
   	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
   	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
   	             	groupTotalcost += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
   	                totalcost += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
   	             	groupTotalgb += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
   	                totalgb += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
   	             	<% } else { %>
   	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
   	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
   	                groupTotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
   	                totalinvQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
   	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
   	             	<% }%>
   	                                
   	                currentRow = i;
   	            } );
	              
	            // Update footer
	            <%-- $(api.column(orderColumn).footer()).html(parseFloat(total).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn1).footer()).html(parseFloat(totalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn2).footer()).html(parseFloat(totalAmt).toFixed(<%=numberOfDecimal%>)); --%>

	            var gpvalue = ((parseFloat(totalIssueQty)-parseFloat(totalcost))/(parseFloat(totalIssueQty)))*100;
    			if(isNaN(gpvalue))
    				gpvalue=0;        		 
    			gpvalue = parseFloat(gpvalue).toFixed("3");
	            
   	            <%if(SORT.equalsIgnoreCase("PRODUCT")) {%> 
	            $(api.column(orderColumn).footer()).html(parseFloat(totalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn1).footer()).html(parseFloat(totalinvQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn2).footer()).html(parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>));
	            $(api.column(orderColumn3).footer()).html(parseFloat(totalcost).toFixed(<%=numberOfDecimal%>));
	            $(api.column(orderColumn4).footer()).html(parseFloat(gpvalue).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
   	             	<% } else { %>	            
	            $(api.column(orderColumn).footer()).html(parseFloat(totalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn1).footer()).html(parseFloat(totalinvQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(orderColumn2).footer()).html(parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>));
   	             	<% }%>
	          } 
   		  });
   	$("#tableIssueSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
    	tableIssueSummary.draw();
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
 
 function changeproduct(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}

 function changeloc(obj){
	 $("#LOC").typeahead('val', '"');
	 $("#LOC").typeahead('val', '');
	 $("#LOC").focus();
	}

 function discnt(){
		    var checkedItems = [];
		    $('input[name="CHKSTKID"]:checked').each(function () {
		        checkedItems.push($(this).val());
		    });

		    if (checkedItems.length === 0) {
		        alert("please Select items.");
		        return false;
		    }

		    if (!confirm("Are you sure you want to discontinue the selected items?")) {
		        return false;
		    }
		    
		    document.form1.action  = "/track/salesorder/DISCONTINUE_PRD";
			document.form1.submit();
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
    var plant= '<%=PLANT%>';
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
 });
 function showPrintBarcode(item,desc,price)
 {
 	document.printProductBarcodeForm.ITEM_BARCODE.value=item;
 	document.printProductBarcodeForm.ITEM_DESC_BARCODE.value=desc;
 	document.printProductBarcodeForm.UNITPRICE_BARCODE.value=price;
 	document.printProductBarcodeForm.UNITPRICE_BARCODE.value=price;
 	document.printProductBarcodeForm.LabelType.value='<%=LabelType%>';
 	document.printProductBarcodeForm.BarcodeWidth.value='<%=BarcodeWidth%>';
 	document.printProductBarcodeForm.BarcodeHeight.value='<%=BarcodeHeight%>';
 	document.printProductBarcodeForm.FontSize.value='<%=FontSize%>';
 	document.printProductBarcodeForm.TextAlign.value='<%=TextAlign%>';
 	document.printProductBarcodeForm.TextPosition.value='<%=TextPosition%>';
 	document.printProductBarcodeForm.DisplayText.value='<%=DisplayText%>';
 	document.printProductBarcodeForm.CNAME.value='<%=CNAME%>';
 	document.printProductBarcodeForm.printdate.value='<%=printdate%>';
 	document.printProductBarcodeForm.PAGE_TYPE.value='PRDBARCODESALES';
 	ViewPrtBarcode();
 	$("#printProductBarcodeModal").modal();
 }
 function showImage(src){
	  $('#imageModal .modal-body img').attr('src', src);
     $('#imageModal').modal('show');

	  }
 </script>
  
  <jsp:include page="ImageProductModal.jsp"flush="true"></jsp:include>
  <jsp:include page="PrintProductBarcodeModal.jsp"flush="true"></jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>