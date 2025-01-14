<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Inventory Summary Available Quantity ";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invWithAvlQty";
	document.form1.submit();
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
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  
TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",PRD_DEPT_ID="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",MODEL="";
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
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
MODEL    = StrUtils.fString(request.getParameter("MODEL"));
String uom = StrUtils.fString(request.getParameter("UOM"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
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
String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);

boolean cntRec=false;
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
%>

<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Summary Available Quantity</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
    <div class="box-header menu-drop">
         <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
         <div class="box-title pull-right">
         <div class="btn-group" role="group">
         <button type="button" class="btn btn-default" onClick="javascript:ExportReport();">Export All Data</button>
					&nbsp;
	</div>
				
         <h1
			 style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;" class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
	     </h1>
     </div>
	 </div>
	 
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="plant" value="<%=PLANT%>">
<span style="text-align: center;"><small><%=fieldDesc%></small></span>
  

    
    <div id="target" style="display:none;">
       <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-4 ac-box">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected  form-control typeahead" name="ITEM" ID="ITEM"  type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    	    <INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    	    <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		    <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
  		</div>
  			<input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value=""> 
 		</div>
 		
      
       <div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT CATEGORY ID"> </label>
      <div class="col-sm-4 ac-box">
    	    <INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    	    <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		    <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
  		</div>

        <div class="col-sm-4 ac-box">
    	   <INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" size="30"  MAXLENGTH=20>
    	   <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
        </div>
 		</div>
 		
 		 <input type="hidden" name="LOC_DESC" value="">

       <div class="form-group">
              <label class="control-label col-sm-2" for="UOM"> </label>
       <div class="col-sm-4 ac-box ">
    	    <INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    	    <span class="select-icon"   onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		    <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
  		</div>
 		
 		 <div class="col-sm-4 ac-box">
       <div class="input-group">
           	<SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" id="UOM" style="width: 100%" >
			<option value=""> SELECT UOM  </option>	  
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);					
					for(int i=0 ; i < ccList.size();i++)		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
						
		    <option value=<%=uom%>><%=uom%>  </option>	          
		           <%}%> 
       		        
			
	   </SELECT>
    	</div>
 		</div>
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="SEARCH"> </label>
	    <div class="col-sm-4 ac-box ">
        <div class="input-group">
 		   <button type="button" class="btn btn-success"  onClick="javascript:return onGo();">Search</button>&nbsp;
  		</div>
 		</div>		
 		</div>
 		
 		</div>
   </FORM>		
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
              	<table id="tableInventorySummary" class="table table-bordred table-striped" > 
				<thead>
		        <tr role="row">
		                 	
		                  	<th style="font-size: smaller;">PRODUCT</th>
		                   	<th style="font-size: smaller;">CATEGORY</th>
		                    <th style="font-size: smaller;">SUB CATEGORY</th>
		                  	<th style="font-size: smaller;">BRAND</th>
		                   	<th style="font-size: smaller;">DESCRIPTION</th>
		                   	<th style="font-size: smaller;">UOM</th>
		                    <th style="font-size: smaller;">STK ON HAND</th>
		                  	<th style="font-size: smaller;">EST.QTY</th>
		                   	<th style="font-size: smaller;">SALES.QTY</th>		                   
		                   	<th style="font-size: smaller;">AVL.QTY</th>
	
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
		    	 </tr>
			     </tfoot>
			     </table>
                 </div>
				 </div>
				 </div>
                 </div>
                 </div>
  <div id="spinnerImg" ></div>

 <script type="text/javascript">
 var tableInventorySummary;
 var productId, desc, prdBrand, prdDept,prdClass, prdType, uom, groupRowColSpan = 7;
function getParameters(){
	return { 
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDept,
		"PRD_TYPE_ID":prdType,
		"PRD_BRAND_ID":prdBrand,
		"UOM": uom,
		"ACTION": "GET_INV_LIST_SUMMARY_WITH_AVLQTY_MULTI_UOM",
		"PLANT":"<%=PLANT%>"
	}
} 

function storeUserPreferences(){
	storeInLocalStorage('viewInventorySummaryAvailableQty_ITEM', $('#ITEM').val());
	storeInLocalStorage('viewInventorySummaryAvailableQty_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('viewInventorySummaryAvailableQty_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('viewInventorySummaryAvailableQty_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('viewInventorySummaryAvailableQty_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('viewInventorySummaryAvailableQty_UOM', $('#UOM').val());
}

function onGo(){
	productId = document.form1.ITEM.value;
    //desc = document.form1.PRD_DESCRIP.value;
    prdClass = document.form1.PRD_CLS_ID.value;
    prdType = document.form1.PRD_TYPE_ID.value;
    prdDept = document.form1.PRD_DEPT_ID.value;
    prdBrand = document.form1.PRD_BRAND_ID.value;
    uom = document.form1.UOM.value;
    if (uom=="Select UOM")
      	 uom="";
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    	storeUserPreferences();
    }
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 0;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
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
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'ITEM', "orderable": true},
       			{"data": 'PRDCLSID', "orderable": true},
    			{"data": 'ITEMTYPE', "orderable": true},
    			{"data": 'PRD_BRAND_ID', "orderable": true},    			
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'INVENTORYUOM', "orderable": true},    			
    			{"data": 'QTY', "orderable": true},
    			{"data": 'ESTIMATEQTY', "orderable": true},
    			{"data": 'OUTBOUNDQTY', "orderable": true},
    			{"data": 'AVAILABLEQTY', "orderable": true}
    		],
			"columnDefs": [{"className": "t-right", "targets": [6,7,8,9]}],
			//"orderFixed": [ groupColumn, 'asc' ], 
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

</SCRIPT>
         
 <script>
 
$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }		   
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('viewInventorySummaryAvailableQty_UOM','','UOM');
		}
    }
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>  
 
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