<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Supplier Balance";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String baseCurrency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",pono="",vendname="",PGaction="";
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
// String curDate = DateUtils.getDate();
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
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/Bill.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Supplier Balance</label></li>                                   
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
			<form class="form-horizontal" name="form" method="post" action="" >
				<input type="hidden" name="plant" value="<%=plant%>" >
				<input type="hidden" name="curDate" value="<%=curDate%>" >
				<input type="hidden" name="vendno" />
				<input type="hidden" name="STATE_PREFIX" value="" >
				<input type="hidden" name="pono" value="" >
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
								 <input type="text" class="ac-selected form-control typeahead" id="billvendname" placeholder="SUPPLIER" name="vendname" >				
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'billvendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>								 -->
								</div>
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
							
							<table id="tableVendorBalances" class="table table-bordred table-striped"> 
								<thead>
									<tr><th style="font-size: smaller;">SUPPLIER NAME</th>
									<th style="font-size: smaller;">BILL BALANCE(<%=baseCurrency%>)</th>
									<th style="font-size: smaller;">EXCESS PAYMENT(<%=baseCurrency%>)</th>
									<th style="font-size: smaller;">BALANCE(<%=baseCurrency%>)</th></tr>
								</thead> 
								<tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th style="text-align: left !important">Total</th>
		            <th style="text-align: right !important"></th>
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
var tableSummary;
var plant = document.form.plant.value;
var FROM_DATE,TO_DATE,USER, groupRowColSpan = 6;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,
		"ACTION": "VIEW_VENDOR_BALANCES",
		"PLANT":"<%=plant%>"
	}
}

function storeUserPreferences(){
	storeInLocalStorage('vendorbalances_FROM_DATE', $('#FROM_DATE').val());
	storeInLocalStorage('vendorbalances_TO_DATE', $('#TO_DATE').val());
	storeInLocalStorage('vendorbalances_BILLVENDNAME', $('#billvendname').val());
}

function onGo(){
	var flag    = "false";
	FROM_DATE      = document.form.FROM_DATE.value;
    TO_DATE        = document.form.TO_DATE.value;		    
    USER           = document.form.vendname.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

   if(USER != null    && USER != "") { flag = true;}
   var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
   if(ALLOWCATCH_ADVANCE_SEARCH==='1'){    
   	storeUserPreferences();
   }   
    var urlStr = "../BillingServlet";
	// Call the method of JQuery Ajax provided
	var groupColumn = 1;
	var totalQty = 0;
	if (tableSummary){
		tableSummary.ajax.url( urlStr ).load();
	}else{
		tableSummary = $('#tableVendorBalances').DataTable({
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
	        	if(typeof data.items[0].vname === 'undefined'){
	        		return [];
	        	}else {
	        		return data.items;
	        	}
	        }
	    },
	    "columns": [
			{"data": 'vname', "orderable": true},
			{"data": 'billbalance', "orderable": true},
			{"data": 'excesspayment', "orderable": true},
			{"data": 'balance', "orderable": true},
		],
		"columnDefs": [{"className": "t-right", "targets": [1,2,3]}],
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
                columns: ':not(:eq('+groupColumn+')):not(:last)'
            }
        ],
	        "drawCallback": function ( settings ) {
	        	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var totalBillBalance = 0;
	            var totalExcessPayment = 0;
	            var totalBalance = 0;
	            
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	            	currentRow = i;	   
	            	
	            	var billbalance = $(rows).eq( i ).find('td:nth-last-child(3)').html();
	            	billbalance = billbalance.replace(/[^0-9.]/g, "");
	            	
	            	var excesspayment = $(rows).eq( i ).find('td:nth-last-child(2)').html();
	            	excesspayment = excesspayment.replace(/[^0-9.]/g, "");
	            	
	            	var balance = $(rows).eq( i ).find('td:last').html();
	            	balance = balance.replace(/[^0-9.]/g, "");	            	
	            	
	            	totalBillBalance += parseFloat(billbalance);
	            	totalExcessPayment += parseFloat(excesspayment);
	            	totalBalance += parseFloat(balance);
	            });
	            if (rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
						'<tr class="group"><td>Total</td>'
						+'<td class="t-right">' + addZeroes(parseFloat(totalBillBalance).toFixed(<%=numberOfDecimal%>)) + '</td>'
						+'<td class="t-right">' + addZeroes(parseFloat(totalExcessPayment).toFixed(<%=numberOfDecimal%>)) + '</td>'
						+'<td class="t-right">' + addZeroes(parseFloat(totalBalance).toFixed(<%=numberOfDecimal%>)) + '</td></tr>'
<%-- 						+'<td class="t-right">' +'<%=baseCurrency%>'+ addZeroes(parseFloat(totalBillBalance).toFixed(<%=numberOfDecimal%>)) + '</td>'
						+'<td class="t-right">' +'<%=baseCurrency%>'+ addZeroes(parseFloat(totalExcessPayment).toFixed(<%=numberOfDecimal%>)) + '</td>'
						+'<td class="t-right">' +'<%=baseCurrency%>'+ addZeroes(parseFloat(totalBalance).toFixed(<%=numberOfDecimal%>)) + '</td></tr>' --%>
                    );
	            }
	        },
	        "order": [],
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
	              .column(1)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalVal = api
	              .column(2)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            TotalTaxVal = api
	              .column(3)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	           

	            var rows = api.rows( {page:'current'} ).nodes();
	            var totalBillBalance = 0;
	            var totalExcessPayment = 0;
	            var totalBalance = 0;
	            
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	            	currentRow = i;	   
	            	
	            	var billbalance = $(rows).eq( i ).find('td:nth-last-child(3)').html();
	            	billbalance = billbalance.replace(/[^0-9.]/g, "");
	            	
	            	var excesspayment = $(rows).eq( i ).find('td:nth-last-child(2)').html();
	            	excesspayment = excesspayment.replace(/[^0-9.]/g, "");
	            	
	            	var balance = $(rows).eq( i ).find('td:last').html();
	            	balance = balance.replace(/[^0-9.]/g, "");	            	
	            	
	            	totalBillBalance += parseFloat(billbalance);
	            	totalExcessPayment += parseFloat(excesspayment);
	            	totalBalance += parseFloat(balance);
	            });
	       
	            // Update footer
	            <%--$(api.column(1).footer()).html(parseFloat(grptotal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(2).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(3).footer()).html(parseFloat(TotalTaxVal).toFixed(<%=numberOfDecimal%>));--%>

	            $(api.column(1).footer()).html(parseFloat(totalBillBalance).toFixed(<%=numberOfDecimal%>));
	            $(api.column(2).footer()).html(parseFloat(totalExcessPayment).toFixed(<%=numberOfDecimal%>));
	            $(api.column(3).footer()).html(parseFloat(totalBalance).toFixed(<%=numberOfDecimal%>));
	          }
		});
		 $("#tableVendorBalances_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
	        	tableSummary.draw();
	        });
    }
}

$('#tableVendorBalances').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableVendorBalances tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableVendorBalances').attr('width', '100%');
});

function ExportReport()
{
  document.form.action="/track/BillingServlet?ACTION=ExportExcelVendorBalances";
  document.form.submit();
 }

function onPrint(){	    
	FROM_DATE      = document.form.FROM_DATE.value;
    TO_DATE        = document.form.TO_DATE.value;		    
    USER           = document.form.vendname.value;
   
    if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
    if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

    if(USER != null    && USER != "") { flag = true;}
    
    var urlStr = "../BillingServlet";
	
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			"FDATE":FROM_DATE,"TDATE":TO_DATE,
			"CNAME":USER,
			"ACTION": "VIEW_VENDOR_BALANCES",
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
	var row = 0;
	
	FROM_DATE = document.form.FROM_DATE.value;
	TO_DATE = document.form.TO_DATE.value;
	CUR_DATE = document.form.curDate.value;
	
	if(FROM_DATE === ""){
		FROM_DATE = CUR_DATE;
	}
	
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
	table1body += "<th>Supplier Name</th>";
	table1body += "<th>Bill Balance</th>";
	table1body += "<th>Excess Payment</th>";
	table1body += "<th>Balance</th>";
	table1body += "</tr>";
	table1body += "</thead>";
	
	
	if(data.items != ""){	
		table1body += "<tbody>";
		var totalBillBalance = 0;
        var totalExcessPayment = 0;
        var totalBalance = 0;
        
		$(data.items).each(function(){
			table1body += "<tr>";
			table1body += "<td>"+this.vname+"</td>";
			table1body += "<td>"+this.billbalance+"</td>";
			table1body += "<td>"+this.excesspayment+"</td>";
			table1body += "<td>"+this.balance+"</td>";
			table1body += "</tr>";
			totalBillBalance += parseFloat(this.billbalance.replace(/[^0-9.]/g, ""));
			totalExcessPayment += parseFloat(this.excesspayment.replace(/[^0-9.]/g, ""));
			totalBalance += parseFloat(this.balance.replace(/[^0-9.]/g, ""));
        	row += 1;
		});
		
		table1body +='<tr><tfoot><td>Total</td>' 
			+'<td>'+'<%=baseCurrency%>'+ addZeroes(parseFloat(totalBillBalance).toFixed(<%=numberOfDecimal%>)) + '</td>'
			+'<td>'+'<%=baseCurrency%>'+ addZeroes(parseFloat(totalExcessPayment).toFixed(<%=numberOfDecimal%>)) + '</td>'
			+'<td>'+'<%=baseCurrency%>'+ addZeroes(parseFloat(totalBalance).toFixed(<%=numberOfDecimal%>)) + '</td></tfoot></tr>';
		table1body += "</tbody>";
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
			0: {halign: 'left'},
			1: {halign: 'right'},
			2: {halign: 'right'},
			3: {halign: 'right'},
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
		},
		didParseCell: function(data) {
			data.table.foot[0].cells[0].styles.fontStyle="bold";
			data.table.foot[0].cells[0].styles.halign="left";
			
			data.table.foot[0].cells[1].styles.fontStyle="bold";
			data.table.foot[0].cells[1].styles.halign="right";
			
			data.table.foot[0].cells[2].styles.fontStyle="bold";
			data.table.foot[0].cells[2].styles.halign="right";
			
			data.table.foot[0].cells[3].styles.fontStyle="bold";
			data.table.foot[0].cells[3].styles.halign="right";
	  	}
	});
	doc.setTextColor(118, 109, 111);
	doc.text("**Amount is displayed in your base currency <%=baseCurrency%>", 14, doc.autoTable.previous.finalY + 10);
	doc.setTextColor(0, 0, 0 );
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	doc.save('vendor_balances.pdf');
}

$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	//getLocalStorageValue('vendorbalances_FROM_DATE',document.form.FROM_DATE.value, 'FROM_DATE');
	//getLocalStorageValue('vendorbalances_TO_DATE','', 'TO_DATE');
	getLocalStorageValue('vendorbalances_BILLVENDNAME','', 'billvendname');	
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