<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<!-- IMTIZIAF -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%
String title = "Inventory Valuation Summary";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
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
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invByProdMultiUOM";
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
String plant = (String) session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry

	
String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="", BATCH="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="", ALLOWPRDNAME="",  
TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_CLS_ID1="";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
/* ITEM    = StrUtils.fString(request.getParameter("ITEM")); */
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
ALLOWPRDNAME = StrUtils.fString(request.getParameter("ALLOWPRDNAME"));//Resvi
// Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
//  Start code added by Deen for product brand on 11/9/12 
LOC = StrUtils.fString(request.getParameter("LOC"));

//IMTIZIAF
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
	
//END
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
                <li><label>Inventory Valuation Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->    
                        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
            
              <!-- <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div> -->
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
						onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="viewInventoryGroupByProdMultiUOM.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
 <!-- end -->
  
  <span style="text-align: center;"><small><%=fieldDesc%></small></span>
  
  <%-- <center>
 <h1><small> <%=fieldDesc%></small></h1>      
    
  </center> --%>
  
    
    <div id="target" style="display:none;">
       <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		 <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>

  		</div>
  		
 		</div>
 		
 		 <input type="hidden" name="LOC_DESC" value="">
 		 <div class="form-group">
       <label class="control-label col-sm-2" for="Product Brand ID"> </label>
        <div class="col-sm-4 ac-box ">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    	<span class="select-icon"   onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		
  		<div class="col-sm-4 ac-box ">
    	<INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeLocation(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
        </div>
  		
  		
  		</div>
		
		<div class="form-group">
		<div class="col-sm-6 ac-box ">
		<label class="control-label col-sm-4" for="LOCATION"> </label>
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Department" checked>
  		<label class="control-label">Show by Department</label> 
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Category">
  		<label class="control-label">Show by Category</label> 
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Brand">
  		<label class="control-label">Show by Brand</label> 
      			<%--  <label class="radio-inline"><input type="radio" name="ALLOWPRDNAME" value="Department" id="ALLOWPRDNAME" <%if (ALLOWPRDNAME.equalsIgnoreCase("Department")) {%> checked <%}%>>Department</label>
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDNAME"  value="Category" id="ALLOWPRDNAME" <%if (ALLOWPRDNAME.equalsIgnoreCase("Category")) {%> checked <%}%>>Category</label>				
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDNAME"  value="Brand" id="ALLOWPRDNAME" <%if (ALLOWPRDNAME.equalsIgnoreCase("Brand")) {%> checked <%}%>>Brand</label> --%>	
       </div>
       
 		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		<input type="hidden" name="PRD_DEPT_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value=""> 
 		   
 		   <div class="col-sm-4 ac-box">
  	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  	</div>
 		    
 		</div>
 
       <!--  <div class="form-group">
        
  	</div> -->

  	   	
  	   	<div class="form-group">
  	<div class="col-sm-offset-5 col-sm-4">   
      
             <INPUT class="ac-selected  form-control typeahead" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description" style="width: 100%"  MAXLENGTH=100> 
    	
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
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									 class="table table-bordred table-striped" > 
<!-- 									role="grid" aria-describedby="tableInventorySummary_info"> -->
					<thead>
		                <tr role="row">
		                  	<th style="font-size: smaller;"><label id="NameLabel" style="font-weight: bold;"></label></th>
		                 	<th style="font-size: smaller;"><label id="LocationLabel" style="font-weight: bold;"></label></th>
		                   	<th style="font-size: smaller;">Total</th>
		                  
		                </tr>
		            </thead>
        <tfoot align="right" style="display: none;">
							<tr>
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
  <div id="spinnerImg" ></div>
 
  </FORM>
  <script type="text/javascript">
  var tableInventorySummary;
  var   loc, prdBrand, prdClass,prdDep,allowprdname, groupRowColSpan = 6;
function getParameters(){
	return { 
		"LOC":loc,
		"PRD_BRAND_ID":prdBrand,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDep,
		"ALLOWPRDNAME":allowprdname,
		"ACTION": "VIEW_INV_VALUATION_SUMMARY",
		"PLANT":"<%=PLANT%>"
	}
} 


function onGo(){
    loc = document.form1.LOC.value;
    prdDep = document.form1.PRD_DEPT_ID.value;
    prdClass = document.form1.PRD_CLS_ID.value;
    prdBrand = document.form1.PRD_BRAND_ID.value;
    allowprdname = document.form1.ALLOWPRDNAME.value;

	if(allowprdname=="Department")
    document.getElementById('NameLabel').innerHTML = "Department";
	else if(allowprdname=="Category")
    document.getElementById('NameLabel').innerHTML = "Category";
	else
    document.getElementById('NameLabel').innerHTML = "Brand";
    
	if(loc!="")
    document.getElementById('LocationLabel').innerHTML = loc;
	else
    document.getElementById('LocationLabel').innerHTML = "Location";
        
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
   	if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
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
		        	if(typeof data.items[0].Name === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'Name', "orderable": true},
    			{"data": 'Location', "orderable": true},
    			{"data": 'Total', "orderable": true}
    			
    			],
			"columnDefs": [{"className": "t-right", "targets": [1,2]}],
			//"orderFixed": [ groupColumn, 'asc' ], 
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
	    	                	columns: [':visible']
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	footer: true,
	                    	text: 'PDF',
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
                     	       //doc.content[1].table.widths =Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                     	       doc.content[1].table.widths = "*";

                     	      doc['footer']=(function(page, pages) {
                   	            return {
                   	                columns: [
                   	                    '',
                   	                    {
                   	                        // This is the right column
                   	                        alignment: 'right',
                   	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
                   	                    }
                   	                ],
                   	                margin: [10, 0]
                   	            }
                   	        });
                     	       
	                    	     },
	                    	     
                            pageSize: 'A4'
	                    },
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
	        "footerCallback": function ( row, data, start, end, display ) {
	            var api = this.api(), data;
	            var intVal = function ( i ) {
	                return typeof i === 'string' ?
	                    i.replace(/[\$,]/g, '')*1 :
	                    typeof i === 'number' ?
	                        i : 0;
	            };
	         // Total over all pages
	            totalqty = api
	                .column(1)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
	         
	            totalcost = api
                .column(2)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
	            
	           
	         // Total over this column 9
	            colTotal_9 = api
	                .column( 2, { page: 'current'} )
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
	            $( api.column( 0 ).footer() ).html('Total');	            
	            $( api.column( 1 ).footer() ).html(addZeroes(parseFloat(totalqty).toFixed(3)));
	            $( api.column( 2 ).footer() ).html(addZeroes(parseFloat(totalcost).toFixed(3)));
	        },
	        
	        "order":[],
	        "drawCallback": function ( settings ) {
				this.attr('width', '100%');
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var groupTotalQty = 0;
	            var totalQty1 = 0;
	            var groupTotalQty1 = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		/* $(rows).eq( i ).before(
			                        '<tr class="group"><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty1).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
			                    ); */
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                    groupTotalQty1 = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                groupTotalQty1 += parseFloat($(rows).eq( i ).find('td:eq(1)').html());
	                totalQty1 += parseFloat($(rows).eq( i ).find('td:eq(1)').html());
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td>Total</td><td class="t-right">' + parseFloat(totalQty1).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
                    );
                }
	        }
		});
}
}

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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.PRODUCT
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
                    '<TH><font color="#ffffff" align="left"><b>Name</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Location</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total</TH>'+
                    
                    '</TR>';
                
}
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
</SCRIPT>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
$(document).ready(function(){
	
	   	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_DEPT_ID','', 'PRD_DEPT_ID');
  		} 
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_CLS_ID','', 'PRD_CLS_ID');
  		} 	
	 	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_BRAND_ID','', 'PRD_BRAND_ID');
 		 }	
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('viewInventoryGroupByProdMultiUOM_LOC','','LOC');
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
// 	return '<p class="item-suggestion">'+ data.PRD_DEPT_ID +'</p>';
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
	
	/* Product Type Number Auto Suggestion */
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
	/* Product Type Number Auto Suggestion */
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
	
	
	
 });
 function changeitem(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}
 function changeCategory(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}
 function changeLocation(obj){
	 $("#LOC").typeahead('val', '"');
	 $("#LOC").typeahead('val', '');
	 $("#LOC").focus();
	}
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>