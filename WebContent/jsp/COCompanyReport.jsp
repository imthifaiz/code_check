<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Order Summary By Customer";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
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
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
   var flag    = "false";
   var ITEM    = document.form1.ITEM.value;
   if(ITEM != null    && ITEM != "") { flag = true;}
   document.form1.action="../salesorder/customersummaryexcel?xlAction=GenerateXLSheetForCoCompanyReport";
   document.form1.submit();
}

function onGo(){
	//storeUserPreferences();
document.form1.action="../salesorder/customersummary";
document.form1.submit();
}

/* function storeUserPreferences(){
	storeInLocalStorage('COCompanyReport_FROMDATE', $('#FROM_DATE').val());
	storeInLocalStorage('COCompanyReport_TODATE', $('#TO_DATE').val());
	storeInLocalStorage('COCompanyReport_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('COCompanyReport_CUSTOMER_TYPE_ID', $('#CUSTOMER_TYPE_ID').val());
	storeInLocalStorage('COCompanyReport_ITEM', $('#ITEM').val());
} */
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ArrayList invQryListSumTotal  = new ArrayList();
Hashtable ht = new Hashtable();
ShipHisDAO shipdao = new ShipHisDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",CUSTOMER="",STOCK_SALE="",STOCK_CLAIM="",BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",POSSEARCH="1",SUPPLIER="",BYSUPPLIER="";
String html = "";
int Total=0,STOCK_EXPIRE_INT=0,STOCK_CLAIM_INT=0, STOCK_FLOATING_INT=0,STOCK_SALE_INT=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",sCustomerTypeId="";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
LOC     = StrUtils.fString(request.getParameter("LOC"));
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
CUSTOMER    = StrUtils.fString(request.getParameter("CUSTOMER"));
sCustomerTypeId  = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
String ENABLE_POS = plantMstDAO.getispos(PLANT);
POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
SUPPLIER    = StrUtils.fString(request.getParameter("SUPPLIER"));
BYSUPPLIER = StrUtils.fString(request.getParameter("BYSUPPLIER"));
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
}
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));


boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportcustomersmry", PLANT,LOGIN_USER);
	displaySummaryExport=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportcustomersmry", PLANT,LOGIN_USER);
	displaySummaryExport=true;
}
// String curDate =new DateUtils().getDate();
String curDate =DateUtils.getDateMinusDays();//resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;

ItemMstUtil itemMstUtil = new ItemMstUtil();

itemMstUtil.setmLogger(mLogger);



boolean cntRec=false;

/*if(PGaction.equalsIgnoreCase("View")){
 try{
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
      
      
      Hashtable ht = new Hashtable();
      ht.put("PLANT",PLANT);
      if(StrUtils.fString(PLANT).length() > 0)        
      if(StrUtils.fString(ITEM).length() > 0)      
      {
        ht.put("ITEM",ITEM);
      } 
     
      
      if(StrUtils.fString(CUSTOMER).length() > 0)      
      {
        ht.put("CNAME",new StrUtils().InsertQuotes(CUSTOMER));
      }
      
      if(StrUtils.fString(sCustomerTypeId).length() > 0)      
      {
        ht.put("CUSTTYPE",sCustomerTypeId);
      }
      
         invQryList = shipdao.getTotalStockSaleLstWithDates(ht,FROM_DATE,TO_DATE,PRD_DESCRIP);
      if(invQryList.size() <=0)
      {
    	  cntRec =true;

        fieldDesc="Data's Not Found";
       
      }
      
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
} */

String collectionDate=DateUtils.getDate();
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


%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Order Summary By Customer</label></li>                                   
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
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/customersummary">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="BYSUPPLIER" value="">
<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>">
    
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="Customer Name" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
                <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changecustomer(this)">
			    <i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			    </button>
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
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"   placeholder="Customer Group" value="<%=StrUtils.forHTMLTag(sCustomerTypeId)%>">
  		
        <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changecustomertypeid(this)">
			    <i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			    </button>
			    <!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control typehead" placeholder="PRODUCT" value="<%=StrUtils.forHTMLTag(ITEM)%>">
		
        <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changeproduct(this)">
			    <i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			    </button>
			    <!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		 -->
  		</div>				
  		</div>
  		</div>
  		
  		
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2"></div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
	  		<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="BYVENDOR" <%if(BYSUPPLIER.equalsIgnoreCase("1")) {%>checked <%}%>  value="0" id="BYVENDOR" >
                     Sales By Supplier</label>
		</div></div>
		
  		<div class="col-sm-4 ac-box" id="sup">
		<div class=""> 
		<input type="text" class="ac-selected  form-control typeahead" id="SUPPLIER"  placeholder="SUPPLIER" name="SUPPLIER" value="<%=StrUtils.forHTMLTag(SUPPLIER)%>">
		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'SUPPLIER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
		
		<button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changeSupplier(this)">
			    <i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			    </button>
  		</div></div>
  		
  		</div>
  		
  		
  		
<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>">
	
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
  		         
   
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              
              		<div class="searchType-filter">
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
        
         <th style="font-size: smaller;">Product ID</TH>
         <th style="font-size: smaller;">Product Description</TH>
         <th style="font-size: smaller;">Company</TH>
         <th style="font-size: smaller;">Total Goods Issue</TH>
                    </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='2'></th>
		            <th style="text-align: left !important">Total</th>
		            <th style="text-align: left !important"></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
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

	 function changecustomertypeid(obj){
		 $("#CUSTOMER_TYPE_ID").typeahead('val', '"');
		 $("#CUSTOMER_TYPE_ID").typeahead('val', '');
		 $("#CUSTOMER_TYPE_ID").focus();
		}

     function changeSupplier(obj){
		 $("#SUPPLIER").typeahead('val', '"');
		 $("#SUPPLIER").typeahead('val', '');
		 $("#SUPPLIER").focus();
		}

  
  var tableData = [];
        
       <%
	   if(PGaction.equalsIgnoreCase("View") || PGaction.equalsIgnoreCase("")){
    		   try{
    		  	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
    		        
    		        
    		        //Hashtable ht = new Hashtable();
    		        ht.put("PLANT",PLANT);
    		        if(StrUtils.fString(PLANT).length() > 0)        
    		        if(StrUtils.fString(ITEM).length() > 0)      
    		        {
    		          ht.put("ITEM",ITEM);
    		        } 
    		       
    		        
    		         if(StrUtils.fString(CUSTOMER).length() > 0)      
    		        {
    		          ht.put("CNAME",StrUtils.InsertQuotes(CUSTOMER));
    		        }
    		        
    		        if(StrUtils.fString(sCustomerTypeId).length() > 0)      
    		        {
    		          ht.put("CUSTTYPE",sCustomerTypeId);
    		        }
    		        
    		        
    		           if(BYSUPPLIER.equalsIgnoreCase("1")){
    		        	   ht.remove("PLANT");
    		        	   ht.remove("ITEM");
    		        	   ht.remove("CNAME");
    		        	   ht.remove("CUSTTYPE");
	    		           if(StrUtils.fString(SUPPLIER).length() > 0){ht.put("R.CNAME",StrUtils.InsertQuotes(SUPPLIER));}
	    		           if(StrUtils.fString(ITEM).length() > 0){ht.put("A.ITEM",ITEM);}
    		        	   ht.put("R.PLANT",PLANT);
    		           	   invQryList = shipdao.getTotalSupplierStockSalestWithDates(ht,FROM_DATE,TO_DATE,PRD_DESCRIP,POSSEARCH);
    		           }else{
    		           	   invQryList = shipdao.getTotalStockSaleLstWithDates(ht,FROM_DATE,TO_DATE,PRD_DESCRIP,POSSEARCH);
    		           }
    		        if(invQryList.size() <=0)
    		        {
    		      	  cntRec =true;

    		          fieldDesc="Data's Not Found";
    		         
    		        } 
    		        
    		   }catch(Exception e) { 
    		  	 invQryList.clear();
    		  	 cntRec=true;
    		  	 
    		   }
    		  }
          int j=0;
          String rowColor="";
         
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
              
              
          %>
          var rowData = [];
          rowData[rowData.length] = '<%=(String)lineArr.get("ITEM") %>';
          rowData[rowData.length] = '<%=(String)lineArr.get("ITEMDESC")%>';
          rowData[rowData.length] = '<%=(String)lineArr.get("CNAME")%>';
          rowData[rowData.length] = '<%= StrUtils.formatNum((String)lineArr.get("PICKQTY"))%>';
                   tableData[tableData.length] = rowData;
          <%}%>    
         
         
    $(document).ready(function(){
    	var groupColumn = 0;
    	/*  if (document.form1.FROM_DATE.value == ''){
    		 getLocalStorageValue('COCompanyReport_FROMDATE', '', 'FROM_DATE');
        }
    	 if (document.form1.TO_DATE.value == ''){
    		 getLocalStorageValue('COCompanyReport_TODATE', '', 'TO_DATE');
        }
    	 if (document.form1.CUSTOMER.value == ''){
    		 getLocalStorageValue('COCompanyReport_CUSTOMER', '', 'CUSTOMER');
        }
    	 if (document.form1.CUSTOMER_TYPE_ID.value == ''){
    		 getLocalStorageValue('COCompanyReport_CUSTOMER_TYPE_ID', '', 'CUSTOMER_TYPE_ID');
        }
    	 if (document.form1.ITEM.value == ''){
    		 getLocalStorageValue('COCompanyReport_ITEM', '', 'ITEM');
        }
	 
    	storeUserPreferences(); */
   	 $('#tableIssueSummary').DataTable({
   		"lengthMenu": [[50, 100, 500], [50, 100, 500]],
//    		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
          searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
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
                    columns: ':not(:eq(2)):not(:last)'
                  }
   	        ],
    	       "order": [],
   	        //drawCallback: function() {
   	        "drawCallback": function ( settings ) {
   	        	<%if(!displaySummaryExport){ %>
   	        	$('.buttons-collection')[0].style.display = 'none';
   	        	<% } %>
   	        	this.attr('width', '100%');
				var groupRowColSpan= 2;
			   	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalPickQty = 0;
	            var groupTotalPickQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	/* if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td></tr>'
			                    );
	                	} */
	                    last = group;
	                    groupEnd = i;
	                    groupTotalPickQty = 0;
	                   
	                    
	                }
	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', ''));
	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', ''));
	                
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + parseFloat(totalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>) + '</td></tr>'
                    );
                	/* $(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td></tr>'
                    ); */
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
 	              .column(3)
 	              .data()
 	              .reduce(function(a, b) {
 	                return intVal(a) + intVal(b);
 	              }, 0);

 	            // Total over this page
 	            totalAmt = api
 	              .column(3, {
 	                page: 'current'
 	              })
 	              .data()
 	              .reduce(function(a, b) {
 	                return intVal(a) + intVal(b);
 	              }, 0);


 	           var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalPickQty = 0;
	            var groupTotalPickQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalPickQty = 0;
	                   
	                    
	                }
	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', ''));
	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', ''));
	                
	                currentRow = i;
	            } );
 	              
 	            // Update footer
 	            <%-- $(api.column(3).footer()).html(parseFloat(total).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
 	            $(api.column(3).footer()).html(parseFloat(groupTotalPickQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
 	          } 
   	          
   		  });	
 		  //tablecode 

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
 $(document).ready(function(){

// 	 const supplierfield = document.getElementById('sup');
// 	 supplierfield.style.display = 'none';
// 	 $("input[name ='BYSUPPLIER']").val('0');

		var sup = '<%=BYSUPPLIER%>';

	 if (sup==1) {
    	 supplierfield.style.display = 'block'; 
    	 $('#sup').show(500);
    	 $("input[name ='BYSUPPLIER']").val('1');
     } else {
    	 supplierfield.style.display = 'none';
    	 $("input[name ='BYSUPPLIER']").val('0');
    	 $("input[name ='SUPPLIER']").val('');
    	 $('#sup').hide(500);
     }

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

	/* Supplier Auto Suggestion */
	$('#SUPPLIER').typeahead({
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
		    return '<div><p class="item-suggestion">'+data.VNAME+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			$("#SUPPLIER").typeahead('val', '"');
			 $("#SUPPLIER").typeahead('val', '');
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
			
		});
 });
 function changepostype(count){
	  $("input[name ='POSSEARCH']").val(count);
	  onGo();
	}

 const checkbox = document.getElementById('BYVENDOR');
 const supplierfield = document.getElementById('sup');

 checkbox.addEventListener('change', function() {
     if (this.checked) {
    	 supplierfield.style.display = 'block'; 
    	 $('#sup').show(500);
    	 $("input[name ='BYSUPPLIER']").val('1');
    	 $("#SUPPLIER").focus();
     } else {
    	 supplierfield.style.display = 'none';
    	 $('#sup').hide(500);
    	 $("input[name ='BYSUPPLIER']").val('0');
    	 $("input[name ='SUPPLIER']").val('');
     }
 });
	
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>