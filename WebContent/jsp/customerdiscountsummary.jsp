<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<!-- NAVAS -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->

<%
String title = "Customer Discount Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
  document.form1.action="/track/ReportServlet?action=Export_Customer_Discount_Excel";  
  document.form1.submit();
}


function onGo(){
 document.form1.action="../jsp/customerReport.jsp";
 document.form1.submit();
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
CustMstDAO custdao = new CustMstDAO();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",CUSTOMER="",STOCK_SALE="",STOCK_CLAIM="",BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0,STOCK_EXPIRE_INT=0,STOCK_CLAIM_INT=0, STOCK_FLOATING_INT=0,STOCK_SALE_INT=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",sCustomerTypeId="";
boolean flag=false;
String view_Inv="",RECS_PER_PAGE="";
long listRec=0;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
RECS_PER_PAGE = strUtils.fString(request.getParameter("RECS_PER_PAGE"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));

//NAVAS
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String collectionDate=_dateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
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
//END


ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);
boolean cntRec=false;

int curPage      = 1;
if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "100";
  int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
  long totalRec     = 0;
  String isDisabled ="disabled";
      view_Inv      = strUtils.fString(request.getParameter("VIEW_INV"));
String currentPage        = strUtils.fString(request.getParameter("cur_page"));

String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
if(listRecSize.length()==0){
listRecSize ="0";
}

if(PGaction.equalsIgnoreCase("View")){
 try{
	 currentPage="1";
	 
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
      
      
      Hashtable ht = new Hashtable();
      ht.put("M.PLANT",PLANT);
      if(strUtils.fString(PLANT).length() > 0)        
      if(strUtils.fString(ITEM).length() > 0)      
      {
        ht.put("I.ITEM",ITEM);
      } 
     
      
      if(strUtils.fString(CUSTOMER).length() > 0)      
      {
        ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
      }
      
      if(strUtils.fString(sCustomerTypeId).length() > 0)      
      {
        ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
      }
      invQryList = custdao.getCustomerCount(ht,PRD_DESCRIP); 
      Map recordArr = (Map) invQryList.get(0);
      listRec = Long.parseLong((String)recordArr.get("RECORDS"));
      int start =  Integer.parseInt(currentPage);
      int end = Integer.parseInt(currentPage)*recPerPage;
         invQryList = custdao.getCustomerForReportPgn(ht,PRD_DESCRIP,start,end);
      if(invQryList.size() <=0)
      {
    	  cntRec =true;

        fieldDesc="Data's Not Found";
       
      }
      if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
      isDisabled="";
      }
      if (currentPage.length() > 0)
      {
	     try  {   curPage = (new Integer(currentPage)).intValue(); 
	     System.out.println("curPage :: "+curPage); 
     	}
     	catch (Exception e)   {   curPage = 1;                                      
     	}
     }
      
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
}
if(PGaction.equalsIgnoreCase("PREVORNEXT")){
	 try{

		int start = (new Integer(currentPage)-1)*recPerPage;
		int end = (new Integer(currentPage)*recPerPage)+1;
	    Hashtable ht = new Hashtable();
	    ht.put("M.PLANT",PLANT);
	    if(strUtils.fString(PLANT).length() > 0)        
	    if(strUtils.fString(ITEM).length() > 0)      
	    {
	      ht.put("I.ITEM",ITEM);
	    } 
	   
	    
	    if(strUtils.fString(CUSTOMER).length() > 0)      
	    {
	      ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
	    }
	    
	    if(strUtils.fString(sCustomerTypeId).length() > 0)      
	    {
	      ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
	    }
		      invQryList = custdao.getCustomerForReportPgn(ht,PRD_DESCRIP,start,end);
	     if (invQryList != null)  {         totalRec = invQryList.size();
	     isDisabled="";
	     }
	     if (currentPage.length() > 0)
	     {
	     
	    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
	    catch (Exception e)   {   curPage = 1;                                      }
	    }
	 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
	}
int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

System.out.println("totalPages :: "+totalPages);
System.out.println("curPage :: "+curPage);


String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
%>
<SCRIPT LANGUAGE="JavaScript">

    var cur_page     = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form1.cur_page.value = cur_page;
       document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "../jsp/customerReport.jsp";
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form1.cur_page.value = cur_page;
      document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "customerReport.jsp";
      document.form1.submit();
      }
    }
 </SCRIPT>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Customer Discount Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onclick="window.location.href='../customer/importdiscount'" >
						Import Customer Discount</button>
					&nbsp;
					 <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
				</div>
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
            </div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="customerReport.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
<!--NAVAS-->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->

<center><strong><font color="red"><%=fieldDesc%></font></strong></center> 
   
  <%-- <center>
  
     <h1><small><font color="red"> <%=fieldDesc%> </font></small></h1>
   
  </center> --%>
  
  <div id="target" style="display:none">
  <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-3 ac-box">
      	    <div class="input-group">
    		<INPUT class="ac-selected form-control" name="ITEM" id="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+convertCharToString(form1.ITEM.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  		</div>
  		</div>
  		
  		<div class="form-inline">
  		    <label class="control-label col-sm-2" for="Customer Name">Customer Name/ID:</label>
       <div class="col-sm-3 ac-box">
      	    <div class="input-group">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected form-control" name="CUSTOMER" id="CUSTOMER"type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" size="30"  MAXLENGTH=100>
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+convertCharToString(form1.CUSTOMER.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  		</div>
  		</div>
 		</div>
 		</div> 
 		
 		
  		<div class="form-group">
       <label class="control-label col-sm-2" for="Description"hidden>Description:</label>
        <div class="col-sm-3 ac-box"style="display: none;">
        <div class="input-group">
        <INPUT class="ac-selected form-control" name="PRD_DESCRIP" id="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="30"  MAXLENGTH=100> 
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DESCRIP\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
    	<!-- <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?DESC='+convertCharToString(form1.PRD_DESCRIP.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
    	</div> 
 		</div> 
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Customer Type">Customer Type:</label>
        <div class="col-sm-3 ac-box">
        <div class="input-group">
        <INPUT class="ac-selected form-control" name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(sCustomerTypeId)%>" size="30"  MAXLENGTH=100> 
<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--     	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+convertCharToString(form1.CUSTOMER_TYPE_ID.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
    	</div> 
 		</div> 
 		</div>
 		</div>
 		
 		
  <div class="form-group">
  	<div class="col-sm-offset-5 col-sm-4">   
  	   <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
<!--   	   <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp; -->
  	   <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  	   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  	</div> 
  	</div>
  	
  	 <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
    <!--   <div class="ShowSingle">
       <div class="col-sm-offset-9">
         <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  	     <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
  	     <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	  	 <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>
  	  </div>
         </div> -->
       	    </div>
       	 
  <INPUT name="RECS_PER_PAGE" type = "hidden" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4>
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
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
              	<table id="tableMovementHistory"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">Customer ID</th>
		                	<th style="font-size: smaller;">Customer Type Desc</th>
		                	<th style="font-size: smaller;">Customer Name</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Price</th>
		                	<th style="font-size: smaller;">Discount</th>
		                	<th style="font-size: smaller;">Price With Discount</th>
		                	
		                </tr>
		            </thead>
		                <!-- NAVAS -->
					<tfoot align="right" style="display: none;">
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							</tr>
							</tfoot>
							<!-- ENDS -->
				</table>
            		</div>
						</div>
					</div>
  </div>
  
  <%-- <script type="text/javascript">
  var tableData = [];
          
       <%
          int j=0;
          String rowColor="";
         
          Hashtable htship = new Hashtable();
          htship.put("PLANT",PLANT);
          Hashtable htexp = new Hashtable();
          custdao.setmLogger(mLogger);
          int total_stock_claim = 0;
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 rowColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd"; 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                String discountType="";
                String discount=(String)lineArr.get("OBDISCOUNT");
                double UnitPrice =Double.parseDouble((String)lineArr.get("UnitPrice"));
                double discountedcost=0.00d;
                double converteddiscount=0.00d;
                double discount1=0.00d;
                double discountedper=0.00d;
                int plusIndex = discount.indexOf("%");
                if (plusIndex != -1) {
                	discount = discount.substring(0, plusIndex);
                	converteddiscount=Double.parseDouble(discount);
                	discountType = "BYPERCENTAGE";
                	discountedcost=(converteddiscount/100)*UnitPrice;
                	discountedper=UnitPrice-discountedcost;
                   }
                else{
                	 discount1 =Double.parseDouble(discount);
                }
              
          %>
          var rowData = [];
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTID"))%>';
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTTYPE")) %>';
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTOMERNAME"))%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("ITEM"))%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC"))%>';
          rowData[rowData.length] = '<%= StrUtils.addZeroes(UnitPrice,numberOfDecimal)%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("OBDISCOUNT"))%>';
      	<%if(discountType.equalsIgnoreCase("BYPERCENTAGE")){ %>
          rowData[rowData.length] = '<%= StrUtils.addZeroes(discountedper,numberOfDecimal)%>';
          <%}else{ %>
          rowData[rowData.length] = '<%= StrUtils.addZeroes(discount1,numberOfDecimal)%>';
          <%} %>
          tableData[tableData.length] = rowData;
        <%}%>
                
    var groupColumn = 0;
    $(document).ready(function(){
   	 $('#tableMovementHistory').DataTable({
   		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
   		  	data: tableData,
   		  	//"columnDefs": [{"className": "t-right", "targets": [7]}],
   			"orderFixed": [ groupColumn, 'asc' ], 
   		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
   			"<'row'<'col-md-6'><'col-md-6'>>" +
   			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
   	        buttons: [
   	        	  /* {
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
                               pageSize: 'A3'
   	                    } 
   	                ]
   	            } , 
   	            {
   	            	extend: 'colvis',
                       columns: ':not(:eq('+groupColumn+')):not(:eq(2)):not(:eq(3))'
                   }
 */   	        ],
   		  });	 
    });
    
    
    
     </script> --%>
     <!--     NAVAS START -->
    <script type="text/javascript">

     var plant = document.form1.plant.value;
     var tabletype;
     var ITEM,PRDDESCRIP,CUSTOMERS,CUSTOMERTYPE_ID, groupRowColSpan = 6;
     function getParameters(){
    		return {
    			"ITEM":ITEM,
    			"PRDDESCRIP":PRDDESCRIP,
    			"CUSTOMERS":CUSTOMERS,
    		   	"PLANT":plant,
    		   	"CUSTOMERTYPE_ID":CUSTOMERTYPE_ID,
    			"action":"GET_CUSTOMERERDISCOUNT_FOR_SUMMARY"
    		}
    	}
     
     function onGo(){
    	 ITEM=document.form1.ITEM.value;
    	 PRDDESCRIP=document.form1.PRD_DESCRIP.value;
    	 CUSTOMERS=document.form1.CUSTOMER.value;
    	 CUSTOMERTYPE_ID=document.form1.CUSTOMER_TYPE_ID.value;
    	
    		   var urlStr = "/track/MasterServlet";
    		   var groupColumn = 1;	
    		    if (tabletype){
    		    	tabletype.ajax.url( urlStr ).load();
    		    }else{
    		    	tabletype = $('#tableMovementHistory').DataTable({
    					"processing": true,
    					"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

    					searching: true, // Enable searching
				        search: {
				            regex: true,   // Enable regular expression searching
				            smart: false   // Disable smart searching for custom matching logic
				        },
    					"ajax": {
    						"type": "GET",
    						"url": urlStr,
    						"data": function(d){
    							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
    						}, 
    						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
    				        "dataType": "json",
    				        "dataSrc": function(data){
    				        	
    					        	if(typeof data.SUPPLIERTYPELIST[0].CUSTID === 'undefined'){
    					        		return [];
    					        	}else {				        		
    					        		return data.SUPPLIERTYPELIST;
    					        	}
    				        	
    				        }
    				    },
    			        "columns": [
    			        	{"data": 'CUSTID', "orderable": true},
    			        	{"data": 'CUSTTYPE', "orderable": true},
    			        	{"data": 'CUSTOMERNAME', "orderable": true},
    			        	{"data": 'ITEM', "orderable": true},
    			        	{"data": 'ITEMDESC', "orderable": true},
    			        	{"data": 'OBDISCOUNT', "orderable": true},
    			        	{"data": 'UnitPrice', "orderable": true},
    			        	{"data": 'discountedper', "orderable": true},
    		    			],
    					"orderFixed": [ ], 
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
    			                    		columns: [0,1,2,3,4,5,6,7]
    			    	                },
    			    	                title: '<%=title%>',
    			    	                footer: true
    			                    },
    			                    {
    			                    	extend : 'pdf',
    		                            footer: true,
    			                    	text: 'PDF Portrait',
    			                    	exportOptions: {
    			                    		columns: [0,1,2,3,4,5,6,7]
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
    			                    		columns: [0,1,2,3,4,5,6,7]
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
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
    			        "order": [],

    				});

    		    	$("#tableMovementHistory_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
			        	tableMovementHistory.draw();
			        });
    		    }
    		    
    		}

     
     </script>
   
		   <input type = "hidden" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;

   <input type = "hidden" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
          
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	   
   
  </FORM>
  </div></div></div>
  
            <!-- Below Jquery Script used for Show/Hide Function-->
<script>
 $(document).ready(function(){
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
 });
 </script>
 
 <script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// PrdId Auto Suggestion //
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
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
	}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
	});


$('#CUSTOMER').typeahead({
	  hint: true,
	  minLength:0,
	  searchOnFocus: true
},
{
	display: 'CNAME',
	async: true,
	source: function (query, process,asyncProcess) {
		var urlStr = "../MasterServlet";
		$.ajax({
			type : "GET",
			url : urlStr,
			async : true,
			data : {
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
		   return '<div> <p class="item-suggestion">' + data.CNAME + '</p> </div>';
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
	<jsp:param name="nobackblock" value="1" />
</jsp:include>