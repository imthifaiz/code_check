<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%
String title = "Inventory Movement Report";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
 
</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%@include file="header.jsp" %>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",PRD_DEPT_ID="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  
TO_DATE = "",fdate="",tdate="";
DateUtils _dateUtils = new DateUtils();
FROM_DATE = _dateUtils.getDate();
TO_DATE = _dateUtils.getDate(); 
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",MODEL="";
boolean flag=false;
String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID")); 
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID")); 
LOC = StrUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3= StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
MODEL    = StrUtils.fString(request.getParameter("MODEL"));
String uom = StrUtils.fString(request.getParameter("UOM"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String collectionDate=DateUtils.getDate();
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
String fromAddress_Country = STATE + " " + COUNTRY;

ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);
boolean cntRec=false;
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Movement Report</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
                        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
            
              <div class="btn-group" role="group">
              <!-- <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button> -->
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					&nbsp;
				</div>
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="viewInventoryOpenCloseStock.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="plant" value="<%=PLANT%>">  
  <span style="text-align: center;"><small><%=fieldDesc%></small></span>

				<div id="target" style="display: none;">
					<div class="form-group">
						<label class="control-label col-sm-2" for="Currency ID">Search</label>
						<div class="form-inline">
							<!--  <label class="control-label col-sm-1" for="From Date"> </label> -->
							<div class="col-sm-4 ac-box">
								<div class="input-group">
									<INPUT class="form-control datepicker" name="FROM_DATE"
										id="FROM_DATE" type="TEXT" value="<%=FROM_DATE%>"
										placeholder="FROM DATE" size="20" MAXLENGTH=20 READONLY>
								</div>
							</div>
						</div>
						<div class="form-inline">
							<!--  <label class="control-label col-sm-1" for="To Date"> </label> -->
							<div class="col-sm-4 ac-box">
								<div class="input-group">
									<INPUT name="TO_DATE" type="TEXT" value="<%=TO_DATE%>"
										id="TO_DATE" placeholder="TO DATE" size="20" MAXLENGTH=20
										class="form-control datepicker" READONLY>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2" for="PRODUCT"> </label>
						<div class="col-sm-4 ac-box">
							<input type="hidden" value="hide" name="search_criteria_status"
								id="search_criteria_status" /> <INPUT
								class="ac-selected  form-control typeahead" name="ITEM"
								ID="ITEM" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>"
								placeholder="PRODUCT" size="30" MAXLENGTH=50> <span
								class="select-icon"
								onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span>
						</div>
						<div class="col-sm-4 ac-box">
							<INPUT class="ac-selected  form-control typeahead"
								name="LOC" ID="LOC" type="TEXT"
								value="<%=StrUtils.forHTMLTag(LOC)%>"
								placeholder="LOCATION" size="30" MAXLENGTH=20>
							<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>

					<input type="hidden" name="LOC_DESC" value="">
					<div class="form-group">
						<label class="control-label col-sm-2" for="Product Brand ID">
						</label>
						<div class="col-sm-4 ac-box ">
							
						</div>

						<div class="col-sm-4 ac-box">
							<button type="button" class="btn btn-success"
								onClick="javascript:return onGo();">Search</button>
							&nbsp;
						</div>
						<input type="hidden" name="PRD_BRAND_DESC" value=""> <INPUT
							name="ACTIVE" type="hidden" value="">
					</div>

					<div class="form-group">
						<div class="form-group">
							<div class="col-sm-offset-5 col-sm-4">

								<INPUT class="ac-selected  form-control typeahead"
									name="PRD_DESCRIP" type="hidden"
									value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>"
									placeholder="Description" style="width: 100%" MAXLENGTH=100>

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

  	</div>
        </div>
       	  </div> 


    
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer"> 
              <div class="row">
              <div class="col-sm-12">
              
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
									 class="table table-bordred table-striped" > 
					<thead>
		                <tr role="row">
		                 	<th style="font-size: smaller;vertical-align: middle;" rowspan="2">DATE</th>
		                  	<th style="font-size: smaller;vertical-align: middle;" rowspan="2">TRAN. NUMBER</th>
		                   	<th style="font-size: smaller;vertical-align: middle;" rowspan="2">TYPE</th>
		                    <th style="font-size: smaller;vertical-align: middle;" rowspan="2">REFERENCE</th>
		                  	<th style="font-size: smaller;" colspan="2" >IN</th>
		                   	<th style="font-size: smaller;" colspan="2">OUT</th>
		                    <th style="font-size: smaller;" colspan="2">BALANCE</th>
		                    <th style="font-size: smaller;" colspan="2">BALANCE INV.</th>
		                </tr>
		                <tr>
		                  	<th style="font-size: smaller;">IN UOM</th>
		                  	<th style="font-size: smaller;">IN QTY</th>
		                   	<th style="font-size: smaller;">OUT UOM</th>
		                   	<th style="font-size: smaller;">OUT QTY</th>
		                    <th style="font-size: smaller;">BALANCE UOM</th>
		                    <th style="font-size: smaller;">BALANCE QTY</th>
		                    <th style="font-size: smaller;">BALANCE INV. UOM</th>
		                    <th style="font-size: smaller;">BALANCE INV. QTY</th>

		                </tr>
		            </thead>
		                 
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
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="dvContents" style="display:none">
  <div class="col-md-12" style="text-align: center;border: 1px solid #ccc;">
    <h2><%=CNAME.trim()%></h2>
	<span><%=fromAddress_BlockAddress.trim()%><br><%=fromAddress_RoadAddress.trim()%><br>
	<%=fromAddress_Country.trim()%> <%=ZIP.trim()%><br>
	Phone <%=TELNO.trim()%>
	</span>
</div>  
  <div class="col-md-12">
    <h4><%=title%> <label id="lblftdate"></label></h4>
    <span style="font-weight:bold"><label id="lblloc"></label></span>	
</div>
<br>  
	<TABLE width="100%" border="0" cellspacing="1" cellpadding=2 style="border-collapse: collapse;" align="center" class="table1" id="tableInventorySummaryPrint">
	<thead>
	<TR style="background-color: #eaeafa;font-weight: bold;">
			<td style="text-align: center;vertical-align: middle;padding: 5px;border: 1px solid #ccc;table-layout: fixed;width:8% !important;" rowspan="2">DATE</td>
        	<td style="text-align: center;vertical-align: middle;padding: 5px;border: 1px solid #ccc;" rowspan="2">TRAN. NUMBER</td>
         	<td style="text-align: center;vertical-align: middle;padding: 5px;border: 1px solid #ccc;" rowspan="2">TYPE</td>
            <td style="text-align: center;vertical-align: middle;padding: 5px;border: 1px solid #ccc;" rowspan="2">REFERENCE</td>
        	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;" colspan="2" >IN</td>
         	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;" colspan="2">OUT</td>
          	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;" colspan="2">BALANCE</td>
          	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;" colspan="2">BALANCE INV.</td>
              </tr>
             <tr style="background-color: #eaeafa;font-weight: bold;">
	          	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;">UOM</td>
	          	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;">QTY</td>
	           	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;">UOM</td>
	           	<td style="text-align: center;padding: 5px;border: 1px solid #ccc;">QTY</td>
	             <td style="text-align: center;padding: 5px;border: 1px solid #ccc;">UOM</td>
	             <td style="text-align: center;padding: 5px;border: 1px solid #ccc;">QTY</td>
	             <td style="text-align: center;padding: 5px;border: 1px solid #ccc;">UOM</td>
	             <td style="text-align: center;padding: 5px;border: 1px solid #ccc;">QTY</td>
             </tr>
          </thead>
               
</table>
            		</div>
	
  <div id="spinnerImg" ></div>
 
  </FORM>
</div>
</div>
</div>

 <script>

 function storeUserPreferences(){
		storeInLocalStorage('viewInventoryMovHisDetail_ITEM', $('#ITEM').val());
		storeInLocalStorage('viewInventoryMovHisDetail_LOC', $('#LOC').val());
	}
 
 
$(document).ready(function(){
	
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('viewInventoryMovHisDetail_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.LOC.value == ''){
		   getLocalStorageValue('viewInventoryMovHisDetail_LOC','', 'LOC');
		} 

	   onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>  
   <script>
  var tableInventorySummary;
  var tableInventorySummaryPrint;
  var productId, desc, loc, prdBrand, prdClass,prdDept, prdType, fromDt,toDt, batch, currencyId, loctype,loctype2,loctype3,currencyDisplay, groupRowColSpan = 9,uom,LOCALEXPENSES;
function getParameters(){
	return {
		"ITEM": productId,
		"LOC":loc,
		"FROM_DATE":fromDt,
		"TO_DATE":toDt,
		"ACTION": "VIEW_INV_SUMMARY_MOVEMENT_REPORT",
		"PLANT":"<%=PLANT%>"
	}
}  



function onGo(){
     productId = document.form1.ITEM.value;
     loc = document.form1.LOC.value;
     fromDt = document.form1.FROM_DATE.value;
     toDt = document.form1.TO_DATE.value;
     var productdesc = document.form1.PRD_DESCRIP.value;
     document.getElementById('lblftdate').innerHTML = "From "+fromDt+" To "+toDt;
     document.getElementById('lblloc').innerHTML = "Location Code : "+loc+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Inventory : "+productId+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+productdesc;
    //storeUserPreferences();
 var urlStr = "../InvMstServlet";
	// Call the method of JQuery Ajax provided
	var groupColumn = 0;
	var totalQty = 0;
	var totalCost = 0;
	var totalPrice = 0;
 // End code modified by Deen for product brand on 11/9/12
 if (tableInventorySummary){
 	tableInventorySummary.ajax.url( urlStr ).load();
 	tableInventorySummaryPrint.ajax.url( urlStr ).load();
 }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

            searching: true, // Enable searching
		                search: {
		                    regex: true,   // Enable regular expression searching
		                    smart: false   // Disable smart searching for custom matching logic
		                },
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].TYPE === 'undefined'){
		        		return [];
		        	}else {
		        		<%-- for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			
		        			data.items[dataIndex]['AVERAGE_COST'] = addZeroes(parseFloat(data.items[dataIndex]['AVERAGE_COST'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['COST_PER_PC'] = addZeroes(parseFloat(data.items[dataIndex]['COST_PER_PC'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['TotalCost'] = addZeroes(parseFloat(data.items[dataIndex]['TotalCost']).toFixed(<%=numberOfDecimal%>));
		        		} --%>
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'TRANDATE', "orderable": true},
				{"data": 'TRAN_NUMBER', "orderable": true},
				{"data": 'TYPE', "orderable": true},
				{"data": 'REFERENCE', "orderable": true},
				{"data": 'IN_UOM', "orderable": true},
				{"data": 'IN_QTY', "orderable": true},
				{"data": 'OUT_UOM', "orderable": true},
				{"data": 'OUT_QTY', "orderable": true},
				{"data": 'BAL_UOM', "orderable": true},
				{"data": 'BAL_QTY', "orderable": true},
				{"data": 'BAL_INV_UOM', "orderable": true},
				{"data": 'BAL_INV_QTY', "orderable": true},
 			],
			/* "columnDefs": [{"className": "t-right", "targets": [14, 15, 16]}], */
			"orderFixed": [ ], 
			/* "dom": 'lBfrtip', */
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
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ]
		});
	    tableInventorySummaryPrint = $('#tableInventorySummaryPrint').DataTable({
	    	searching: false, paging: false, info: false,
			"processing": true,
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].TYPE === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'TRANDATE', "orderable": false},
				{"data": 'TRAN_NUMBER', "orderable": false},
				{"data": 'TYPE', "orderable": false},
				{"data": 'REFERENCE', "orderable": false},
				{"data": 'IN_UOM', "orderable": false},
				{"data": 'IN_QTY', "orderable": false},
				{"data": 'OUT_UOM', "orderable": false},
				{"data": 'OUT_QTY', "orderable": false},
				{"data": 'BAL_UOM', "orderable": false},
				{"data": 'BAL_QTY', "orderable": false},
				{"data": 'BAL_INV_UOM', "orderable": false},
				{"data": 'BAL_INV_QTY', "orderable": false},
 			],
			"orderFixed": [ ], 
	        buttons: [],
			"drawCallback": function ( settings ) {
				this.attr('width', '100%');
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
	                		
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                totalQty = parseFloat($(rows).eq( i ).find('td:last').html());
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group" style="font-weight: bold;"><td colspan=' + groupRowColSpan + '></td><td colspan="2">Closing Stock</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
                    );
                	
                }
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

$('.buttons-columnVisibility').each(function(){
var $li = $(this),
   $cb = $('<input>', {
           type:'checkbox',
           style:'margin:0 .25em 0 0; vertical-align:middle'}
         ).prop('checked', $(this).hasClass('active') );
$li.find('a').prepend( $cb );
});
	 
$('.buttons-columnVisibility').on('click', 'input:checkbox,li', function(){
var $li = $(this).closest('li'),
   $cb = $li.find('input:checkbox');
$cb.prop('checked', $li.hasClass('active') );
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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
	        	outPutdata = outPutdata+item.INVDETAILS
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
            '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PRODUCT CATEGORY ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PRODUCT SUB CATEGORY ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>Average Unit Cost</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>List Price</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Qty</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Cost</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Price</TH>'+
            '</TR>';
                
}
   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
 <style id="table_style" type="text/css">
    table1
    {
        border-collapse: collapse;
    }
    table1 td
    {
        padding: 5px;
        border: 1px solid #ccc;
    }
    table1 td:nth-last-child(7),
    td:nth-last-child(5),
    td:nth-last-child(3),
    td:nth-last-child(1) {
    text-align: right;
	}
	
</style>
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
    var plant= '<%=PLANT%>'; 
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
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
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
			return '<div onclick="document.form1.PRD_DESCRIP.value = \''+data.ITEMDESC+'\'"><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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
 function PrintTable() {
     var printWindow = window.open('', '', '');
     printWindow.document.write('<html><head><title>Print <%=title %></title>');

     //Print the Table CSS.
     var table_style = document.getElementById("table_style").innerHTML;
     printWindow.document.write('<style type = "text/css">');
     printWindow.document.write(table_style);
     printWindow.document.write('</style>');
     printWindow.document.write('</head>');

     //Print the DIV contents i.e. the HTML Table.
     printWindow.document.write('<body>');
     var divContents = document.getElementById("dvContents").innerHTML;
     printWindow.document.write(divContents);
     printWindow.document.write('</body>');

     printWindow.document.write('</html>');
     printWindow.document.close();
     printWindow.print();
 }
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>