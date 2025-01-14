<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.dao.ItemMstDAO"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
	String title = "Consignment Order Summary By Price";
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="false">
	<jsp:param value="<%=title%>" name="title" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>" />
	<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT_REPORTS%>" />
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu {
	left: 50px;
	!
	important;
}
</style>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
 
   document.form1.action = "/track/ReportServlet?action=ExportConsignmentPrice";
   document.form1.submit();
  
}
function onGo(){

   var flag    = "false";
    FROM_DATE      = document.form1.FROM_DATE.value;
    TO_DATE        = document.form1.TO_DATE.value;
    USER           = document.form1.CUSTOMER.value;
    ORDERNO        = document.form1.ORDERNO.value;
    CUSTOMER_TYPE_ID = document.form1.CUSTOMER_TYPE_ID.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(CUSTOMER_TYPE_ID != null     && CUSTOMER_TYPE_ID != "") { flag = true;}

 document.form1.action="ConsignmentSummaryWithPrice.jsp";
  document.form1.submit();
}

</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript"
	src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript"
	src="../jsp/js/calendar.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/autosize@4.0.0/dist/autosize.min.js"></script>

<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	ShipHisDAO _ShipHisDAO = new ShipHisDAO();
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();
	ArrayList prodGstList = new ArrayList();
	Hashtable ht = new Hashtable();
	movHisUtil.setmLogger(mLogger);

	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String LOGIN_USER = session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String userID = (String) session.getAttribute("LOGIN_USER");
	movQryList = (ArrayList) request.getAttribute("CONSIWPDETAILS");
	String FROM_DATE = "", TO_DATE = "", USER = "", DIRTYPE = "", fdate = "", tdate = "", ORDERNO = "",
			cntRec = "false", CUSTOMER = "", PGaction = "", status = "", sCustomerTypeId = "", taxby = "";
	boolean displaySummaryExport = false;
	if (systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryExport = ub.isCheckValAcc("exportissuedobsummry", plant, LOGIN_USER);
		displaySummaryExport = true;
	}
	if (systatus.equalsIgnoreCase("INVENTORY")) {
		displaySummaryExport = ub.isCheckValinv("exportissuedobsummry", plant, LOGIN_USER);
		displaySummaryExport = true;
	}
	float subtotal = 0;
	double gst = 0, total = 0;
	float gsttotal = 0;
	float grandtotal = 0, gstpercentage = 0, prodgstsubtotal1 = 0;
	int k = 0;

	DecimalFormat decformat = new DecimalFormat("#,##0.00");

	FROM_DATE = _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = _strUtils.fString(request.getParameter("TO_DATE"));
	String fieldDesc = "";

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
	// 	String curDate = _dateUtils.getDate();
	String curDate = du.getDateMinusDays();//resvi
	if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;//resvi
	if (FROM_DATE.length() > 5)

		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) + FROM_DATE.substring(0, 2);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)

		tdate = TO_DATE.substring(6) + TO_DATE.substring(3, 5) + TO_DATE.substring(0, 2);

	DIRTYPE = _strUtils.fString(request.getParameter("DIRTYPE"));
	PGaction = _strUtils.fString(request.getParameter("PGaction")).trim();
	USER = _strUtils.fString(request.getParameter("USER"));
	ORDERNO = _strUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER = _strUtils.fString(request.getParameter("CUSTOMER"));
	status = _strUtils.fString(request.getParameter("STATUS"));
	sCustomerTypeId = _strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));

	if (DIRTYPE.length() <= 0) {
		DIRTYPE = "ISSUEDHISTORY";
	}

	if (PGaction.equalsIgnoreCase("View")) {

		try {

			if (_strUtils.fString(ORDERNO).length() > 0)
				ht.put("a.PONO", ORDERNO);
			if (_strUtils.fString(status).length() > 0)
				ht.put("b.STATUS", status);
			if (_strUtils.fString(sCustomerTypeId).length() > 0)
				ht.put("CUSTTYPE", sCustomerTypeId);

		} catch (Exception e) {
			fieldDesc = "<font class='mainred'>" + e.getMessage() + "</font>";
		}
	}
%>
<div class="container-fluid m-t-20">
	<div class="box">
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class="box-title pull-right">
				<div class="btn-group" role="group">
					<%
						if (displaySummaryExport) {
					%>
					<button type="button" class="btn btn-default"
						data-toggle="dropdown">
						Export All Data<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li>
					</ul>
					<%
						}
					%>


				</div>

				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>

				</h1>
			</div>
		</div>
		<div class="box-body">
			<FORM class="form-horizontal" name="form1" method="post"
				action="/track/ReportServlet?">
				<input type="hidden" name="PGaction" value="View"> <INPUT
					type="Hidden" name="DIRTYPE" value="ISSUEDHISTORY"> <input
					type="hidden" name="CUSTOMERID" value=""> <input
					type="hidden" name="CUSTOMER_TYPE_DESC" value=""> <input
					type="hidden" name="STATUS_ID" value="">
				<Center>
					<small><%=fieldDesc%></small>
				</Center>


				<div id="target" style="display: none" style="padding: 18px;">
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2.5">
								<label class="control-label col-sm-2" for="search">Search</label>
							</div>
							<div class="col-sm-2">
								<input type="hidden" value="hide" name="search_criteria_status"
									id="search_criteria_status" /> <INPUT name="FROM_DATE"
									type="text" value="<%=FROM_DATE%>" size="30" MAXLENGTH=20
									class="form-control datepicker" READONLY
									placeholder="FROM DATE">
							</div>
							<div class="col-sm-2">
								<INPUT class="form-control datepicker" name="TO_DATE"
									type="text" value="<%=TO_DATE%>" size="30" MAXLENGTH=20
									READONLY placeholder="TO DATE">
							</div>
							<div class="col-sm-4 ac-box">
								<div class="">
									<input type="text" class="ac-selected form-control typeahead"
										id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>"
										placeholder="Customer Name" name="CUSTOMER"> <span
										class="select-icon" style="right: 45px;"
										onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i
										class="glyphicon glyphicon-menu-down"></i></span> <span
										class="select-search btn-danger input-group-addon"
										onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span
										class="glyphicon glyphicon-search" aria-hidden="true"
										style="left: -4px;"></span></span>
								</div>
							</div>
						</div>
						<div class="row" style="padding: 3px">
							<div class="col-sm-2"></div>
							<div class="col-sm-4 ac-box">
								<div class="">
									<input type="text" class="ac-selected form-control"
										id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"
										value="<%=StrUtils.forHTMLTag(sCustomerTypeId)%>"
										placeholder="Customer Type"> <span class="select-icon"
										style="right: 45px;"
										onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i
										class="glyphicon glyphicon-menu-down"></i></span> <span
										class="select-search btn-danger input-group-addon "
										onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span
										class="glyphicon glyphicon-search" aria-hidden="true"
										style="left: -4px;"></span></span>
								</div>
							</div>
							<div class="col-sm-4 ac-box">
								<input type="text" class="ac-selected form-control" id="ORDERNO"
									name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>"
									placeholder="Order Number"> <span class="select-icon"
									style="right: 45px;"
									onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i
									class="glyphicon glyphicon-menu-down"></i></span>
							</div>
						</div>


						<div class="row" style="padding: 3px">
							<div class="col-sm-2"></div>
							<div class="col-sm-10 txn-buttons">

								<button type="button" class="btn btn-success"
									onClick="javascript:return onGo();">Search</button>
								&nbsp;
							</div>


						</div>
					</div>

				</div>


				<div class="form-group">
					<div class="col-sm-3">
						<a href="#" class="Show" style="font-size: 15px">Show Advanced
							Search</a> <a href="#" class="Hide"
							style="font-size: 15px; display: none">Hide Advanced Search</a>
					</div>
					<div class="ShowSingle">
						<div class="col-sm-offset-9">
						</div>
					</div>
				</div>

			
				<div id="VIEW_RESULT_HERE" class="table-responsive">
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
							<table id="tableIssueSummary"
								class="table table-bordred table-striped">
								<thead>
									<th style="font-size: smaller;">Order No</TH>
									<th style="font-size: smaller;">Customer Name</TH>
									<th style="font-size: smaller;">Issued Date</TH>
									<th style="font-size: smaller;">SubTotal</TH>
									<th style="font-size: smaller;">Tax</TH>
									<th style="font-size: smaller;">Total</TH>
									<th style="font-size: smaller;">Issued By</TH>
								</thead>

							</table>
						</div>
					</div>
				</div>

				<script type="text/javascript">

 var tableIssueSummary;
 var FROM_DATE,TO_DATE,USER,ORDERNO,REFERENCE,STATUS,ORDERTYPE, groupRowColSpan = 6;

 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,
 		"USER":USER,"ORDERNO":ORDERNO,"CUSTOMER_TYPE_ID":CUSTOMER_TYPE_ID
 	}
 } 
   function onGo(){
		
   var flag = "false";
   FROM_DATE      = document.form1.FROM_DATE.value;
   TO_DATE        = document.form1.TO_DATE.value;
   USER           = document.form1.CUSTOMER.value;
   ORDERNO        = document.form1.ORDERNO.value;
   CUSTOMER_TYPE_ID = document.form1.CUSTOMER_TYPE_ID.value;
  
  if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
  if(TO_DATE != null    && TO_DATE != "") { flag = true;}
  if(USER != null    && USER != "") { flag = true;}
  if(ORDERNO != null     && ORDERNO != "") { flag = true;}
  if(CUSTOMER_TYPE_ID != null     && CUSTOMER_TYPE_ID != "") { flag = true;}

  
    
    var urlStr = "../consignment/consignmentwithprice";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
   
    // End code modified by Deen for product brand on 11/9/12
    if (tableIssueSummary){
    	tableIssueSummary.ajax.url( urlStr ).load();
    }else{
    	tableIssueSummary = $('#tableIssueSummary').DataTable({
			"processing": true,
			"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],

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
		        	if(typeof data.ORDERS[0] === 'undefined'){
		        		return [];
		        	}else {		
		        		return data.ORDERS;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'PONO', "orderable": true},
    			{"data": 'CUSTNAME', "orderable": true},
    			{"data": 'RECVDATE', "orderable": true},
    			{"data": 'SUBTOTAL', "orderable": true},
    			{"data": 'GSTVALUE', "orderable": true},
    			{"data": 'TOTALVALUE', "orderable": true},
    			{"data": 'ISSUEDBY', "orderable": true},    			
//     			{"data": 'STATUS', "orderable": true},
			],
			/*"columnDefs": [{"className": "t-right", "targets": [6,7]}],*/
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
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A3',
                            	extend : 'pdfHtml5',
    	                    	exportOptions: {
    	                    		columns: [':visible']
    	                    	},	                    	
<%--     	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                         --%>
                     		orientation: 'landscape',
                     		customize: function (doc) {
                     			doc.defaultStyle.fontSize = 16;
                     	        doc.styles.tableHeader.fontSize = 16;
                     	        doc.styles.title.fontSize = 20;
                     	       doc.content[1].table.body[0].forEach(function (h) {
                     	          h.fillColor = '#ECECEC';                 	         
                     	          alignment: 'center'
                     	      });
                     	      var rowCount = doc.content[1].table.body.length;
                     	      doc.styles.tableHeader.color = 'black';
                     	      
                     	        // Create a footer
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
                             pageSize: 'A2',
                             footer: true
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
//                     columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(1)):not(:eq(2)):not(:eq(4))'
                }
	        ],
	        "order": [],
	        drawCallback: function() {
				this.attr('width', '100%');
				var groupColumn = 1;
  				var groupRowColSpan = 2;
  			   	var api = this.api();
  	            var rows = api.rows( {page:'current'} ).nodes();
  	            var last=null;
  	            var totalSub = 0;
  	            var groupTotalSub = 0;
  	            var totalTax = 0;
  	            var groupTotalTax = 0;
  	            var totalPrice = 0;
  	            var groupTotalPrice = 0;
  	            var groupEnd = 0;
  	            var currentRow = 0;
  	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
  	                if ( last !== group ) {
  	                	debugger;
  	                	var groupTotalSubVal=null,groupTotalTaxVal=null,groupTotalPriceVal=null;
  	                	
  	                	if(groupTotalSub==null || groupTotalSub==0){
  	                		groupTotalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
  	                	}else{
  	                		groupTotalSubVal=parseFloat(groupTotalSub).toFixed(<%=numberOfDecimal%>);
  	                	}if(groupTotalTax==null || groupTotalTax==0){
  	                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
  	                	}else{
  	                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
  	                	}if(groupTotalPrice==null || groupTotalPrice==0){
  	                		groupTotalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
  	                	}else{
  	                		groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>);
  	                	}
	                	
		            	if (i > 0) {
  	                		$(rows).eq( i ).before(
  			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + groupTotalSubVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupTotalPriceVal + '</td><td></td><td></td></tr>'
  			                    );
  	                	}
	                    last = group;
  	                    groupEnd = i;    
  	                    groupTotalSub = 0;
  	                    groupTotalTax = 0;
  	                    groupTotalPrice = 0;
	                }
	                groupTotalSub += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
  	                totalSub += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
  	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
  	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
  	                groupTotalPrice += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
  	                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
  	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
  	            	
					var totalSubVal=null,groupTotalSubVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,groupTotalPriceVal=null;
	            	
	            	if(totalSub==null || totalSub==0){
	            		totalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		totalSubVal=parseFloat(totalSub).toFixed(<%=numberOfDecimal%>);
	            	}if(groupTotalSub==null || groupTotalSub==0){
	            		groupTotalSubVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		groupTotalSubVal=parseFloat(groupTotalSub).toFixed(<%=numberOfDecimal%>);
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
	            	}if(groupTotalPrice==null || groupTotalPrice==0){
	            		groupTotalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	            	}else{
	            		groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>);
	            	}
	            	
	            	$(rows).eq( currentRow ).after(
  	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + totalSubVal + '</td><td>' + totalTaxVal + '</td><td>' + totalPriceVal + '</td><td></td><td></td></tr>'
                     );
                 	$(rows).eq( currentRow ).after(
  	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalSubVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupTotalPriceVal + '</td><td></td><td></td></tr>'
                     );
                }
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
    }
}
   $('#tableIssueSummary').on('column-visibility.dt', function(e, settings, column, state ){
		if (!state){
			groupRowColSpan = parseInt(groupRowColSpan) - 1;
		}else{
			groupRowColSpan = parseInt(groupRowColSpan) + 1;
		}
		$('#tableIssueSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
		$('#tableIssueSummary').attr('width', '100%');
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
</script>

			


			</FORM>
		</div>
	</div>
</div>

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

	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/TransferOrderServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						Submit : "GET_ORDER_NUM_FOR_AUTO_SUGGESTION",
						CNAME : document.form1.CUSTOMER.value,
						PONO : query
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
		    return '<p>' + data.TONO + '</p>';
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#invoice").typeahead('val', '"');
			$("#invoice").typeahead('val', '');
			$("#auto_invoiceNo").typeahead('val', '"');
			$("#auto_invoiceNo").typeahead('val', '');
			$("#plno").typeahead('val', '"');
			$("#plno").typeahead('val', '');
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
 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>