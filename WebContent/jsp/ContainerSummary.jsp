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
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Order Summary By Container";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
//document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
   var flag    = "false";
   /*var LOC     = document.form1.LOC.value;
   var ITEM    = document.form1.ITEM.value;
   var BATCH   = document.form1.BATCH.value;
  

   if(LOC != null     && LOC != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(BATCH != null    && BATCH  != "") { flag = true;}*/
 
  document.form1.action="../salesorder/containersummaryexcel?xlAction=GenerateXLSheet";
  document.form1.submit();

}

function onGo(){
	document.form1.action="../salesorder/containersummary";
	document.form1.submit();
}
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	InvUtil invUtil       = new InvUtil();
	invUtil.setmLogger(mLogger);
	ArrayList conQryList  = new ArrayList();
	PlantMstDAO plantMstDAO = new PlantMstDAO();

	String fieldDesc="";
	String USERID ="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_DESCRIP="", QTY ="",PRD_BRAND_ID= "",CUSTOMER="",CONTAINER="",ORDERNO="",CUSTOMERTYPE="";
	
	//-----Added by Deen on Feb 7 2014, Description: Include From and To date in container summary search
	String FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
	//-----Added by Deen on Feb 7 2014 end
	String html = "";
	double Total=0;

	String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	USERID= session.getAttribute("LOGIN_USER").toString();
	String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	CUSTOMER    = StrUtils.fString(request.getParameter("CUSTOMER"));
	CONTAINER = StrUtils.fString(request.getParameter("CONTAINER"));
	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	ITEM    = StrUtils.fString(request.getParameter("ITEM"));
	BATCH   = StrUtils.fString(request.getParameter("BATCH"));
	PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
	CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	
	//-----Added by Deen on Feb 7 2014, Description: Include From and To date in container summary search
	FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportcontainersmry", PLANT,LOGIN_USER);
		displaySummaryExport=true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportcontainersmry", PLANT,LOGIN_USER);
		displaySummaryExport=true;
	}
// 	String curDate =new DateUtils().getDate();
     String curDate =DateUtils.getDateMinusDays();//resvi
     if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
    		curDate =DateUtils.getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE=curDate;
	//-----Added by Deen on Feb 7 2014 end

	boolean cntRec=false;
	
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
                <li><label>Sales Order Summary By Container</label></li>                                   
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
<FORM class="form-horizontal" name="form1" method="post" action="../salesorder/containersummary">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">

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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="Customer Name" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
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
  		<input type="text" class="ac-selected form-control typehead" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"   placeholder="Customer Group" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>">
         <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changecustomertypeid(this)">
			<i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			</button> 
			<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control typehead" id="ORDERNO" name="ORDERNO" placeholder="Order Number" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
         <button type="button" style=" position: absolute; margin-left: -22px; z-index: 2; vertical-align: middle; font-size: 20px; opacity: 0.5;"
						onclick="changeorderno(this)">
			<i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i>	
			</button> 
			  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
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
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control " id="CONTAINER" name="CONTAINER" value="<%=StrUtils.forHTMLTag(CONTAINER)%>" placeholder="CONTAINER" >
			
  		</div>
  		</div>
  		  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control " id="BATCH" name="BATCH" value="<%=StrUtils.forHTMLTag(BATCH)%>" placeholder="BATCH" >
			
  		</div>
  		</div>
<INPUT name="PRD_DESCRIP" type = "Hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>">
	
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
  		<!--  <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button> -->
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
		                	<th style="font-size: smaller;">Customer</th>
		                	<th style="font-size: smaller;">Container</th>
		                	<th style="font-size: smaller;">Issue Date</th>
		                	<th style="font-size: smaller;">Order No</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Batch/Serial No</th>
		                	<th style="font-size: smaller;">Quantity</th>
		                	</tr>
		            </thead>
		            <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='6'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <th style="text-align: right !important"></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
  
   </FORM>
  
  <script>

  function changecustomer(obj){
		 $("#CUSTOMER").typeahead('val', '"');
		 $("#CUSTOMER").typeahead('val', '');
		 $("#CUSTOMER").focus();
		}

  function changecustomertypeid(obj){
		 $("#CUSTOMER_TYPE_ID").typeahead('val', '"');
		 $("#CUSTOMER_TYPE_ID").typeahead('val', '');
		 $("#CUSTOMER_TYPE_ID").focus();
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

		


  
  var tableInventorySummary;
  var customername, container,orderno,productId,batch,desc,fromdate,todate,CUSTOMERTYPE,  groupRowColSpan = 6;
 function getParameters(){
 	return {
 		"CUSTOMERNAME": customername,"CONTAINER": container,"ORDERNO": orderno,"ITEM": productId,"BATCH":batch,"PRD_DESCRIP":desc,
 		"FROM_DATE":fromdate,"TO_DATE":todate,"CUSTOMERTYPE":CUSTOMERTYPE,
 			
 		"ACTION": "VIEW_CONTAINER_SUMMARY",
 		"PLANT":"<%=PLANT%>"
 	}
 }  
function onGo(){
    productId = document.form1.ITEM.value;
    customername = document.form1.CUSTOMER.value;    
    container = document.form1.CONTAINER.value;
    batch = document.form1.BATCH.value;
    orderno = document.form1.ORDERNO.value;
    desc= document.form1.PRD_DESCRIP.value;
   //-----Added by Deen on Feb 7 2014, Description: Include From and To date in container summary search
    fromdate=document.form1.FROM_DATE.value;
    todate=document.form1.TO_DATE.value;
    CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;

    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
   // storeInLocalStorage('ContainerSummary_FROMDATE', fromdate);
	//storeInLocalStorage('ContainerSummary_TODATE', todate);
	storeInLocalStorage('ContainerSummary_CUSTOMER', customername);
	storeInLocalStorage('ContainerSummary_CUSTOMER_TYPE_ID', CUSTOMERTYPE);
	storeInLocalStorage('ContainerSummary_ORDERNO', orderno);
	storeInLocalStorage('ContainerSummary_ITEM', productId);
	storeInLocalStorage('ContainerSummary_CONTAINER',container);
	storeInLocalStorage('ContainerSummary_BATCH', batch);
    }
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 4;
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
	        
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].item === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'cname', "orderable": true},
    			{"data": 'strContainer', "orderable": true},
    			{"data": 'transactiondate', "orderable": true},
    			{"data": 'dono', "orderable": true},
    			{"data": 'item', "orderable": true},
    			{"data": 'itemdesc', "orderable": true},
    			{"data": 'batch', "orderable": true},
    			{"data": 'qty', "orderable": true}
    			],
			"columnDefs": [{"className": "t-right", "targets": [7]}],
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
                   // columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(6):not(:last)'
                }
	        ],
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
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html());
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
                    );
                }
	        },/**/
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
	              .column(7)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalAmt = api
	              .column(7)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

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
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html());
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(7).footer()).html(parseFloat(groupTotalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
<%-- 	            $(api.column(7).footer()).html(parseFloat(total).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
	          }
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


    
  <%--   
    //-----Added by Deen on Feb 7 2014 end
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/InvMstServlet";
    // Call the method of JQuery Ajax provided
    /*-- Modification History
             1. Deen on Feb 07 2014, Description:Include From and To date in search
    */
      $.ajax({type: "POST",url: urlStr, data: { CUSTOMERNAME: customername,CONTAINER: container,ORDERNO: orderno,ITEM: productId,BATCH:batch,PRD_DESCRIP:desc,FROM_DATE:fromdate,TO_DATE:todate,CUSTOMERTYPE:CUSTOMERTYPE,ACTION: "VIEW_CONTAINER_SUMMARY",PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
   //  End code modified by Deen for product brand on 11/9/12 
        
} --%>

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
                       
	        	outPutdata = outPutdata+item.CONTAINERSUMMARY
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
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
            '<TR BGCOLOR="#000066">'+
	            '<TH><font color="#ffffff" align="left"><b>Customer</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Container</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Issued Date</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Product Id</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
	            '<TH><font color="#ffffff" align="left"><b>Batch/Serial No</TH>'+
	            '<TH><font color="#ffffff" align="Right"><b>Quantity</TH>'+
            '</TR>';
                
  }
   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
		// getLocalStorageValue('ContainerSummary_FROMDATE', $('#FROM_DATE').val(), 'FROM_DATE');
	 //getLocalStorageValue('ContainerSummary_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('ContainerSummary_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('ContainerSummary_CUSTOMER_TYPE_ID', '', 'CUSTOMER_TYPE_ID');
	 getLocalStorageValue('ContainerSummary_ORDERNO', '', 'ORDERNO');
	 getLocalStorageValue('ContainerSummary_ITEM', '', 'ITEM');
	 getLocalStorageValue('ContainerSummary_CONTAINER', '', 'CONTAINER');
	 getLocalStorageValue('ContainerSummary_BATCH', '', 'BATCH');
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
 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
