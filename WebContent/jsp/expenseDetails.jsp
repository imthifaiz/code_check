<%@page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>

<%
String title = "Expense Details";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String baseCurrency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="",ACCOUNT_NAME="";
// String curDate = DateUtils.getDate();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
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
//resvi

String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);


%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/Expenses.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Expense Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
			<div class="dropdownbox-title pull-right">
                	<button type="button" class="btn btn-default" data-toggle="dropdown">
						Export As
					</button>
				    <ul class="dropdown-menu" style="top:100%">
				      <li><a href="#" onclick="javascript:return onPrint();">PDF</a></li>
				      <li><a href="#" onclick="javascript:return ExportReport();">Excel</a></li>
				    </ul>
				      &nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../accounting/reports'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
			</div>
		</div>
		 <div id="CHK1">   
		 </div>   
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="" >
				<input type="hidden" name="plant" value="<%=plant%>" >
			<input type="hidden" name="vendno" />
				<input type="hidden" name="cmd" />
				<input type="hidden" name="TranId" />
				<input type="hidden" name="curDate" value="<%=curDate%>" >
				<div id="target" style="padding: 18px; display:none;">
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2.5">		    	 
						  		<label class="control-label col-sm-2" for="search">Search</label>
					  		</div>
					  		<div class="col-sm-2">
								<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE"
								size="30"  maxlength=20 class ="form-control datepicker" readonly placeholder="FROM DATE">
							</div>
							<div class="col-sm-2">
								<input class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE"
								value="<%=TO_DATE%>" size="30"  maxlength=20 readonly placeholder="TO DATE">
							</div>
							
							
		<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								<input type="text" class="ac-selected  form-control typeahead" id="vendname" 
								placeholder="SUPPLIER" name="vendname" >
								<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'vendname\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								<!-- <span class="btn-danger input-group-addon" 
								onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form1.vendname.value);">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span>	 -->
								</div>
							</div>
						</div>
						<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
					  		<div class="col-sm-4 ac-box">
					  			<input type="text" id="expenses_account_name" name="expenses_account_name" class="form-control expensesaccountSearch" placeholder="Select Account">	
					  			<span class="select-icon" onclick="$(this).parent().find('input[name=\'expensesaccountSearch\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					  		</div>
					  		<div class="col-sm-4 ac-box">  		
					  			<input type="text" class="ac-selected form-control" id="REFERENCE" name="REFERENCE" placeholder="REFERENCE">				
					  		</div>
				  		</div>
				  		<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
					  		<div class="col-sm-4 ac-box">  	
				  		  		<input type="text" name="status" id="status" class="ac-selected form-control" placeholder="STATUS" >
					  			<span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					  		</div>
				  		</div>
				  		<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-10 txn-buttons">								
								<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
			      <div class="col-sm-3">
				      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
				      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
			      </div>
			      <div class="ShowSingle"></div>
	       	  	</div>
	       	  	<div id="VIEW_RESULT_HERE" class="table-responsive">
					<div class="row">
						<div class="col-sm-12">
							
						<div class="searchType-filter">
		                      	<select id="searchType" class="form-control" style="height: 30px;">
		                        	<option value="all">All</option>
		                        	<option value="first">First</option>
		                           	<option value="center">Middle</option>
		                           	<option value="last">Last</option>
		                       		</select>
            				</div>
						
							<table id="tableExpenseDetails" class="table table-bordred table-striped"> 
								<thead>
									<tr><th style="font-size: smaller;">STATUS</th>
									<th style="font-size: smaller;">DATE</th>
									<th style="font-size: smaller;">REFERENCE</th>
									<th style="font-size: smaller;">VENDOR NAME</th>
									<th style="font-size: smaller;">CATEGORY</th>
									<th style="font-size: smaller;">CUSTOMER NAME</th>
									<th style="font-size: smaller;">AMOUNT(<%=baseCurrency%>)</th>
									<th style="font-size: smaller;">AMOUNT WITH TAX(<%=baseCurrency%>)</th></tr>
								</thead> 
								 <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th style="text-align: left !important">Total</th>
		            <th colspan='5'></th>
		            <th style="text-align: right !important"></th>
		            <th style="text-align: right !important"></th>
		            </tr>
		        </tfoot>
							</table>
							<p>** Amount is displayed in your base currency <span class="badge text-semibold badge-success d-inline"><%=baseCurrency%></span></p>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<div hidden>
	<table id="table1">
		
	</table>
</div>
<script>
var ageingDetails;

var tableExpensesSummary;
var plant = document.form1.plant.value;
var tableBillAgingSummary;
var ITEMDESC,FROM_DATE,TO_DATE,USER,SUPPLIER,EXPENSES_ACOUNT_NAME,REFERENCE,STATUS, groupRowColSpan = 5;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"SUPPLIER":SUPPLIER,"ACCOUNT_NAME":EXPENSES_ACCOUNT_NAME,"REFERENCE":REFERENCE,"STATUS":STATUS,
		"ACTION": "VIEW_EXPENSES_DETAILS",
		"PLANT":"<%=plant%>"
	}
}

function storeUserPreferences(){
	storeInLocalStorage('expenseDetails_FROM_DATE', $('#FROM_DATE').val());
	storeInLocalStorage('expenseDetails_TO_DATE', $('#TO_DATE').val());
	storeInLocalStorage('expenseDetails_VENDNAME', $('#vendname').val());
	storeInLocalStorage('expenseDetails_EXPENSES_ACCOUNT_NAME', $('#expenses_account_name').val());
	storeInLocalStorage('expenseDetails_REFERENCE', $('#REFERENCE').val());
	storeInLocalStorage('expenseDetails_STATUS', $('#status').val());
}

function onGo(){
	var flag = "false";
	FROM_DATE = document.form1.FROM_DATE.value;
	TO_DATE = document.form1.TO_DATE.value;		    
	SUPPLIER = document.form1.vendname.value;
	EXPENSES_ACCOUNT_NAME = document.form1.expenses_account_name.value;
	REFERENCE = document.form1.REFERENCE.value;
	STATUS = document.form1.status.value;
	   	    		   
	if(FROM_DATE != null && FROM_DATE != "") { flag = true;}
	if(TO_DATE != null && TO_DATE != "") { flag = true;}		   
	if(SUPPLIER != null && SUPPLIER != "") { flag = true;}
	if(EXPENSES_ACCOUNT_NAME != null && EXPENSES_ACCOUNT_NAME != "") { flag = true;}
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){	   		    
		storeUserPreferences();    
    }
	var urlStr = "../ExpensesServlet";
	// Call the method of JQuery Ajax provided
	var groupColumn = 1;
	var totalQty = 0;
	// End code modified by Deen for product brand on 11/9/12
	if (tableExpensesSummary){
		tableExpensesSummary.ajax.url( urlStr ).load();
	}else{
		tableExpensesSummary = $('#tableExpenseDetails').DataTable({
		"processing": true,
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
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
		if(typeof data.items[0].expensesaccount === 'undefined'){
			return [];
		}else {				        		
			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
			data.items[dataIndex]['paidthrough'] = '<a href="../expense/expdetail?TRANID=' +data.items[dataIndex]['id']+ '">'+data.items[dataIndex]['paidthrough']+'</a>';
			if(data.items[dataIndex]['status']=='NON-BILLABLE')
				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
			else if(data.items[dataIndex]['status']=='UNBILLED')
				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
			else if(data.items[dataIndex]['status']=='BILLED')
				data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
			else
				data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
				data.items[dataIndex]['amount'] =  getNumberInMillionFormat(data.items[dataIndex]['amount']);
				data.items[dataIndex]['subtotal'] =  getNumberInMillionFormat(data.items[dataIndex]['subtotal']);
			}
       		return data.items;
       	}
        }
	    },
		"columns": [
			{"data": 'status', "orderable": true},
			{"data": 'expensesdate', "orderable": true},
			{"data": 'reference', "orderable": true},
			{"data": 'vendname', "orderable": true},		  
			{"data": 'expensesaccount', "orderable": true},
			{"data": 'custname', "orderable": true},
			{"data": 'subtotal', "orderable": true},
			{"data": 'amount', "orderable": true},
		],
		"columnDefs": [{"className": "t-right", "targets": [6,7]}],
		"orderFixed": [ ], 
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
                columns: ':not(:eq('+groupColumn+')):not(:eq(6)):not(:last)'
            }
        ],
        "order": [],
	        "drawCallback": function ( settings ) {
	        	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var totalAmount = 0;
	            var totalAmountTax = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	            	currentRow = i;
	            	var amount = $(rows).eq( i ).find('td:nth-last-child(2)').html();
	            	amount = amount.replace(/[^0-9.]/g, "");
	            	
	            	var amountTax = $(rows).eq( i ).find('td:last').html();
	            	amountTax = amountTax.replace(/[^0-9.]/g, "");	            	
	            	
	            	totalAmount += parseFloat(amount);
	            	totalAmountTax += parseFloat(amountTax);
	            });
	            if (rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
						'<tr class="group"><td>Total</td><td colspan=' + groupRowColSpan 
						+'></td><td class="t-right">' + addZeroes(parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>)) + '</td><td class="t-right">' 
						 + addZeroes(parseFloat(totalAmountTax).toFixed(<%=numberOfDecimal%>)) + '</td></tr>'
<%-- 						+'></td><td class="t-right">' +'<%=baseCurrency%>'+ addZeroes(parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>)) + '</td><td class="t-right">' 
						 +'<%=baseCurrency%>'+ addZeroes(parseFloat(totalAmountTax).toFixed(<%=numberOfDecimal%>)) + '</td></tr>' --%>
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
	            grptotal = api
	              .column(6)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalVal = api
	              .column(6)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            TotalTaxVal = api
	              .column(7)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            var rows = api.rows( {page:'current'} ).nodes();
	            var totalAmount = 0;
	            var totalAmountTax = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	            	currentRow = i;
	            	var amount = $(rows).eq( i ).find('td:nth-last-child(2)').html();
	            	amount = amount.replace(/[^0-9.]/g, "");
	            	
	            	var amountTax = $(rows).eq( i ).find('td:last').html();
	            	amountTax = amountTax.replace(/[^0-9.]/g, "");	            	
	            	
	            	totalAmount += parseFloat(amount);
	            	totalAmountTax += parseFloat(amountTax);
	            });

	            // Update footer
	            <%--*$(api.column(6).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(7).footer()).html(parseFloat(TotalTaxVal).toFixed(<%=numberOfDecimal%>)); --%>
	            
	            $(api.column(6).footer()).html(parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>));
	            $(api.column(7).footer()).html(parseFloat(totalAmountTax).toFixed(<%=numberOfDecimal%>));
	          }
	        
		});

		 $("#tableExpenseDetails_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
	        	tableExpensesSummary.draw();
	        });
    }
}

$('#tableExpenseDetails').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableExpenseDetails tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableExpenseDetails').attr('width', '100%');
});

function ExportReport()
{
  document.form1.action="/track/ExpensesServlet?ACTION=ExportExcelExpenseDetails";
  document.form1.submit();
 }

function onPrint(){	
    var urlStr = "../ExpensesServlet";
    
    FROM_DATE = document.form1.FROM_DATE.value;
	TO_DATE = document.form1.TO_DATE.value;		    
	SUPPLIER = document.form1.vendname.value;
	EXPENSES_ACCOUNT_NAME = document.form1.expenses_account_name.value;
	REFERENCE = document.form1.REFERENCE.value;
	STATUS = document.form1.status.value;
	   	    		   
	if(FROM_DATE != null && FROM_DATE != "") { flag = true;}
	if(TO_DATE != null && TO_DATE != "") { flag = true;}		   
	if(SUPPLIER != null && SUPPLIER != "") { flag = true;}
	if(EXPENSES_ACCOUNT_NAME != null && EXPENSES_ACCOUNT_NAME != "") { flag = true;}
	
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			"FDATE":FROM_DATE,"TDATE":TO_DATE,
			"SUPPLIER":SUPPLIER,"ACCOUNT_NAME":EXPENSES_ACCOUNT_NAME,"REFERENCE":REFERENCE,"STATUS":STATUS,
			"ACTION": "VIEW_EXPENSES_DETAILS",
			"PLANT":"<%=plant%>"
		}, 
		dataType : "json",
		success : function(data) {
			generateExpenseDetail(data);
		}
	});
}

function generateExpenseDetail(data){
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	
	FROM_DATE = document.form1.FROM_DATE.value;
	TO_DATE = document.form1.TO_DATE.value;
	CUR_DATE = document.form1.curDate.value;
	
	if(TO_DATE === ""){
		TO_DATE = CUR_DATE;
	}
	
	doc.setFontSize(15);
	doc.text('<%=CNAME%>', 88, 28);
	doc.setFontSize(12);
	doc.text('<%=title%>', 98, 38);
	doc.setFontSize(10);
	doc.text('From ' + FROM_DATE +' To ' + TO_DATE , 86, 45);
	
	doc.setFontSize(10);
	var table1body = "";
	table1body = "<thead>";
	table1body += "<tr>";
	table1body += "<th>Status</th>";
	table1body += "<th>Expense Date</th>";
	table1body += "<th>Reference</th>";
	table1body += "<th>Vendor Name</th>";
	table1body += "<th>Category</th>";
	table1body += "<th>Customer Name</th>";
	table1body += "<th>Amount</th>";
	table1body += "<th>Amount With Tax</th>";
	table1body += "</tr>";
	table1body += "</thead>";
	
	
	if(data.items != ""){	
		table1body += "<tbody>";
		var totalAmount = 0;
        var totalAmountTax = 0;
        
		$(data.items).each(function(){
			table1body += "<tr>";
			table1body += "<td>"+this.status+"</td>";
			table1body += "<td>"+this.expensesdate+"</td>";
			table1body += "<td>"+this.reference+"</td>";
			table1body += "<td>"+this.vendname+"</td>";
			table1body += "<td>"+this.paidthrough+"</td>";
			table1body += "<td>"+this.custname+"</td>";
			table1body += "<td>"+this.basecurrency+""+getNumberInMillionFormat(this.subtotal)+"</td>";
			table1body += "<td>"+this.basecurrency+""+getNumberInMillionFormat(this.amount)+"</td>";		
			table1body += "</tr>";
			totalAmount += parseFloat(this.subtotal);
        	totalAmountTax += parseFloat(this.amount);
		});
		table1body += "</tbody>";
		table1body +='<tr><tfoot><td>Total</td><td></td><td></td><td></td><td></td><td></td><td>' 
			+'<%=baseCurrency%>'+ getNumberInMillionFormat(totalAmount)  + '</td><td>' 
			+'<%=baseCurrency%>'+ getNumberInMillionFormat(totalAmountTax) + '</td> </tfoot></tr>';
	}	
	
	
	$("#table1").html("");
	$("#table1").html(table1body);
	
	var totalPagesExp = "{total_pages_cont_string}";
	doc.autoTable({
		html : '#table1',
		startY : 55,
		headStyles : {
			fillColor : [ 236, 236, 236],
			textColor : [ 0, 0, 0 ],
			fontSize : 9,
			fontStyle : 'normal',
			cellWidth : 'wrap',
			halign : 'center'
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ],
			fontSize : 9
		},
		theme : 'plain',
		columnStyles: {
			0: {halign: 'left',cellWidth : 'wrap'},
			1: {halign: 'center',cellWidth : 10},
			2: {halign: 'center',cellWidth : 20},
			3: {halign: 'center',cellWidth : 20},
			4: {halign: 'center',cellWidth : 10},
			5: {halign: 'center',cellWidth : 10},
			6: {halign: 'right',cellWidth : 28},
			7: {halign: 'right',cellWidth : 28},
			
		},
		didDrawPage : function(data) {
			doc.setFontStyle("normal");
			// Footer
			pageNumber = doc.internal.getNumberOfPages();
			var str = "Page " + doc.internal.getNumberOfPages();
			// Total page number plugin only available in jspdf v1.0+
			if (typeof doc.putTotalPages === 'function') {
				str = str + " of " + totalPagesExp;
			}
			doc.setFontSize(10);

			// jsPDF 1.4+ uses getWidth, <1.4 uses .width
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height
					: pageSize.getHeight();
		}
	});
	doc.setTextColor(118, 109, 111);
	doc.text("**Amount is displayed in your base currency <%=baseCurrency%>", 14, doc.autoTable.previous.finalY + 10);
	doc.setTextColor(0, 0, 0 );
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	doc.save('expense_details.pdf');
}

$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	/* if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('expenseDetails_FROM_DATE','', 'FROM_DATE');
		 }
	 if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('expenseDetails_TO_DATE','', 'TO_DATE');
		 } */
	   if (document.form1.vendname.value == ''){
		   getLocalStorageValue('expenseDetails_VENDNAME','', 'vendname');
		 } 	
	   if (document.form1.expenses_account_name.value == ''){
		   getLocalStorageValue('expenseDetails_EXPENSES_ACCOUNT_NAME','', 'expenses_account_name');
		} 	
	   if (document.form1.REFERENCE.value == ''){
		   getLocalStorageValue('expenseDetails_REFERENCE','', 'REFERENCE');
		}	
	   if (document.form1.status.value == ''){
		   getLocalStorageValue('expenseDetails_STATUS','', 'status');
		 }	
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
});
</script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>