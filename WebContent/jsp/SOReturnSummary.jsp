<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%@ include file="header.jsp"%>
<%
	String title = "Sales Order Return Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
    
	boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryNew = ub.isCheckValAcc("newsalesreturn", plant,username);
		displaySummaryLink = ub.isCheckValAcc("summarylnksalesreturn", plant,username);
		displaySummaryExport = ub.isCheckValAcc("exportsalesreturn", plant,username);
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryNew = ub.isCheckValinv("newsalesreturn", plant,username);
		displaySummaryLink = ub.isCheckValinv("summarylnksalesreturn", plant,username);
		displaySummaryExport = ub.isCheckValinv("exportsalesreturn", plant,username);
	}
	String FROM_DATE = "", TO_DATE = "", USER = "", fdate = "", tdate = "";
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String collectionDate = du.getDate();
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
	//RESVI
	String curDate =du.getDateMinusDays();
	 if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	    	curDate =DateUtils.getDate();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_RETURN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript"src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/poReversal.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                                
                <li><label>Sales Order Return Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class="box-title pull-right">
			<% if (displaySummaryNew) { %>
               <button type="button" class="btn btn-default" onClick="window.location.href='../salesreturn/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              <!-- <button type="button" class="btn btn-default" onclick="window.location.href='../home'">Back</button> -->
             <% } %>
              </div>
		</div>
		<div class="box-body">
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden>
					<input type="hidden" name="CUST_CODE" value="" />
<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER" name="custName" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=MULTIPLEOB&CUSTOMER='+form.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>									 -->
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="" placeholder="ORDER NO">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="gino" name="gino" value="" placeholder="GINO" >
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'gino\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
 	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control typeahead" id="auto_invoiceNo" name="invoiceno" value="" placeholder="INVOICE NO">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'invoiceno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
  		</div>
  		</div>
  		
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
						<a href="#" class="Show" style="font-size: 15px">Show Advanced
							Search</a> <a href="#" class="Hide"
							style="font-size: 15px; display: none">Hide Advanced Search</a>
					</div>
					<div class="ShowSingle"></div>
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
		                  
							<table id="tableSOReturnSummary"
								class="table table-bordred table-striped">
								<thead>
									<th style="font-size: smaller;">SO RETURN NO</th>
									<th style="font-size: smaller;">ORDER NO</th>
									<th style="font-size: smaller;">GINO</th>
									<th style="font-size: smaller;">INVOICE</th>
									<th style="font-size: smaller;">CUSTOMER NAME</th>
									<th style="font-size: smaller;">RETURN DATE</th>
									<th style="font-size: smaller;">RETURN QTY</th>
									<%if(!systatus.equalsIgnoreCase("INVENTORY")){%>
									<th style="font-size: smaller;">STATUS</th>
									<%} %>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
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

var plant = document.form.plant.value;
var tableInventorySummary;
 var FROM_DATE,TO_DATE,USER,ORDERNO,ITEM, groupRowColSpan = 6;
function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,"ORDERNO":ORDERNO,"INVOICE":INVOICE,"GINO":GINO,
		"Submit": "VIEW_SALESORDERRETURN_SUMMARY_VIEW",
		"PLANT":"<%=plant%>"
	}
} 

function onGo(){
	   var flag    = "false";
	    FROM_DATE = document.form.FROM_DATE.value;
	    TO_DATE = document.form.TO_DATE.value;		    
	    USER = document.form.custName.value;
	    ORDERNO = document.form.ORDERNO.value;
	    INVOICE = document.form.invoiceno.value;
	    GINO = document.form.gino.value;
	   
	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

	   if(USER != null    && USER != "") { flag = true;}
	   
	    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
	    
	    var urlStr = "../ReturnOrderServlet";
	   	// Call the method of JQuery Ajax provided
	   	var groupColumn = 1;
	   	var totalQty = 0;
	    // End code modified by Deen for product brand on 11/9/12
	    if (tableInventorySummary){
	    	tableInventorySummary.ajax.url( urlStr ).load();
	    }else{
		    tableInventorySummary = $('#tableSOReturnSummary').DataTable({
				"processing": true,
				"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
			        	if(typeof data.ReturnDetails[0] === 'undefined'){
			        		return [];
			        	}else {				        		
			        		for(var dataIndex = 0; dataIndex < data.ReturnDetails.length; dataIndex ++){
			        			<% if (displaySummaryLink) { %>
			        			data.ReturnDetails[dataIndex]['SORETURN'] = '<a href="../salesreturn/detail?SORETURN=' +data.ReturnDetails[dataIndex]['SORETURN']+'&DONO=' +data.ReturnDetails[dataIndex]['dono']+'&GINO=' +data.ReturnDetails[dataIndex]['gino']+'&INVOICE=' +data.ReturnDetails[dataIndex]['invoice']+'">'+data.ReturnDetails[dataIndex]['SORETURN']+'</a>';
			        			<% }else{ %>
			        			data.ReturnDetails[dataIndex]['SORETURN'] = data.ReturnDetails[dataIndex]['SORETURN'];
			        			<% } %>
			        			data.ReturnDetails[dataIndex]['return_qty'] =addZeroes(parseFloat(data.ReturnDetails[dataIndex]['return_qty']).toFixed(3));
			        		}
			        		return data.ReturnDetails;
			        	}
			        }
			    },
		        "columns": [
		        	{"data": 'SORETURN', "orderable": true},
	    			{"data": 'dono', "orderable": true},
	    			{"data": 'gino', "orderable": true},
	    			{"data": 'invoice', "orderable": true},
	    			{"data": 'cname', "orderable": true},
	    			{"data": 'return_date', "orderable": true},
	    			{"data": 'return_qty', "orderable": true},    
	    			<%if(!systatus.equalsIgnoreCase("INVENTORY")){%>
	    			{"data": 'status', "orderable": true},	    
	    			<%}%>
	    			],
	    			/* "columnDefs": [{"className": "t-right", "targets": [6]}], */
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
			        "order": [],
			        drawCallback: function() {
			        	<%if(!displaySummaryExport){ %>
			        	$('.buttons-collection')[0].style.display = 'none';
			        	<% } %>
			        	} 
				});

		    $("#tableSOReturnSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
	        	tableSOReturnSummary.draw();
	        });
		    }
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>