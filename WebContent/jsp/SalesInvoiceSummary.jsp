<!--
 PAGE CREATED BY -> IMTHI
DATE -> 30-08-2022 
DESC -> SALES INVOICE SUMMARY (BY CUSTOMER)
-->
<%@page import="com.track.util.http.HttpUtils"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String rootURI = HttpUtils.getRootURI(request);
String title = "Sales Invoice Summary (By Customer)";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
if ("".equals(fieldDesc)){
	fieldDesc = StrUtils.fString(request.getParameter("msg"));
}
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",ORDERNO="",CUSTOMER="",PGaction="",invoice="",STATUS="";

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newinvoice", plant,username);
	displaySummaryLink = ub.isCheckValAcc("summarylnkinvoice", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportinvoice", plant,username);
}
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
PlantMstUtil plantmstutil = new PlantMstUtil();
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
String FDate="Date From : "+FROM_DATE+" To : "+collectionDate;
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.plugin.autotable.js"></script>

<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Invoice Summary (By Customer)</label></li>                                   
            </ul>             
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class=" pull-right">
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div>
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="cmd" value="<%="cmd"%>" hidden>
		<input type="text" name="TranId" value="<%="TranId"%>" hidden>
		<input type="text" name="curency" value="<%="curency"%>" hidden>
		<input type="text" name="CUST_CODE" hidden>
		<input type="text" name="nTAXTREATMENT" hidden>
		<input type="text" name="STATE_PREFIX" hidden>
		<INPUT type="hidden" name="ORIGIN">
		<INPUT type="hidden" name="DEDUCT_INV"> 
		<input type="text" name="invoice" hidden>
		<INPUT type="hidden" name="EDUCATION"> 
		<input hidden name="displayCustomerpop" id="displayCustomerpop" />
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE" onchange="getdate()">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE" onchange="getdate()">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="auto_invoiceNo" name="invoiceno" placeholder="INVOICE NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoiceno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>  		
  		</div>
  		</div>
		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
  		<div class="col-sm-4 ac-box">
  		<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
  		</div>
  		<div class="col-sm-4 ac-box">
				
				
				
			</div>
		</div>
  		</div>
		</div>
		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
		<!-- PDF PRINT -->
<div id="dvContents" style="display:none">
<div class="Contents">
<div style="position: relative;float: left;">
<h3 style="margin-bottom: -15px;"><b><%=CNAME%></b></h3>
<h3 style="margin-bottom: 5px;"><b>Sales Invoice Summary</b></h3>
<h5 style="margin-bottom: 5px;margin-top: 0px;"><label id="FDATE"><%=FDate%></label></h5>
</div>
<!-- <div id="pageFooter" style="float: right;margin-top: 15px;"> -->
<div style="float: right;margin-top: 15px;">
<label>Date: <%=collectionDate%></label>
</div>
</div>
	<TABLE border="0" cellspacing="1" cellpadding=2	align="center" class="table printtable portrait-content" id="tableprint">
	<thead>
	 <tr>  
             <th style="font-size: smaller;">INVOICE</th>
           	 <th style="font-size: smaller;">DATE</th>
             <th style="font-size: smaller;">CUSTOMER</th>                   
             <th style="font-size: smaller;">CURRENCY</th>
             <th style="font-size: smaller;">INV.AMOUNT</th>
             <th style="font-size: smaller;">EXCHANGE RATE</th>
             <th style="font-size: smaller;">TOTAL</th>
          </tr>  
        </thead> 
        <tfoot style="display:none">
		            <tr class="group">
		            <th colspan='5'></th>
		            <th style="text-align: left !important">TOTAL</th>
		            <th style="text-align: right !important"></th>
		            </tr>
		        </tfoot>

</TABLE>
</div>
		<div style="overflow-x:auto;" >
		<!-- <font face="Proxima Nova" > -->       
		
		<div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		 </div>                               
              <table id="tableInventorySummary" class="table table-bordred table-striped">                   
                   <thead>
                     <th style="font-size: smaller;">INVOICE</th>
                   	 <th style="font-size: smaller;">DATE</th>
                     <th style="font-size: smaller;">CUSTOMER</th>                   
                     <th style="font-size: smaller;">CURRENCY</th>
                     <th style="font-size: smaller;">INV.AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">TOTAL</th>
                   </thead>
                     <tbody>
				 
				</tbody>
				<tfoot style="display:none">
		            <tr class="group">
		            <th colspan='5'></th>
		            <th style="text-align: left !important">TOTAL</th>
		            <th style="text-align: right !important"></th>
		            </tr>
		        </tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tableInventorySummary;
		var tablePrint;
		var numberOfDecimal = "<%=numberOfDecimal%>";
		 var FROM_DATE,TO_DATE,USER,ORDERNO,INVOICENO, groupRowColSpan = 6,groupColSpan = 5;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"CNAME":USER,"INVOICENO":INVOICENO,					
				"ACTION": "VIEW_INVOICE_SUMMARY_VIEW",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form1.FROM_DATE.value;
		    TO_DATE        = document.form1.TO_DATE.value;		    
		    USER           = document.form1.CUSTOMER.value;
		    INVOICENO        = document.form1.invoiceno.value;
		    
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(USER != null    && USER != "") { flag = true;}
		   if(INVOICENO != null     && INVOICENO != "") { flag = true;}		    
		    var urlStr = "../InvoiceServlet";
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    if (tableInventorySummary){
		    	tableInventorySummary.ajax.url( urlStr ).load();
		    	tableprint.ajax.url( urlStr ).load();
		    }else{
			    tableInventorySummary = $('#tableInventorySummary').DataTable({
					"processing": true,
					"lengthMenu": [[50, 100, 500], [50, 100, 500]],

					searching: true, // Enable searching
			        search: {
			            regex: true,   // Enable regular expression searching
			            smart: false   // Disable smart searching for custom matching logic
			        },
			        
// 					"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.items[0].bill === 'undefined'){
				        		return [];
				        	}else {				        		
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			
				        			data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['bill'] = '<a href="../invoice/detail?custno=' +data.items[dataIndex]['custno']+'&INVOICE_HDR=' +data.items[dataIndex]['invoiceid']+'&CUR=' +data.items[dataIndex]['currency']+'">'+data.items[dataIndex]['bill']+'</a>';
				        			<% }else{ %>
				        			data.items[dataIndex]['bill'] = data.items[dataIndex]['bill'];
				        			<% } %> 
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'bill', "orderable": true},
		    			{"data": 'billdate', "orderable": true},
		    			{"data": 'custname', "orderable": true},
		    			{"data": 'currency', "orderable": true},
		    			{"data": 'invamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [4,5,6]}],
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
		                    columns: ':not(:eq(0)):not(:eq(5)):not(:last)'
		                }
			        ],
			        "order": [],
			        drawCallback: function() {
			        	<%if(!displaySummaryExport){ %>
			        	$('.buttons-collection')[0].style.display = 'none';
			        	<% } %>
			        	 var api = this.api();
				            var rows = api.rows( {page:'current'} ).nodes();
				            var last=null;
				            var totalRCost = 0;
				            var groupTotalRCost = 0;
				            var totalTax = 0;
				            var groupTotalTax = 0;
				            var total = 0;
				            var groupTotal = 0;
				            var groupEnd = 0;
				            var currentRow = 0;
				            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
				                if ( last !== group ) {                
				                	
				                	if(groupTotalRCost==null || groupTotalRCost==0){
				                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
				                	}if(groupTotalTax==null || groupTotalTax==0){
				                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>); 
				                	}if(groupTotal==null || groupTotal==0){
				                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>); 
				                	}
				                	
				       /*          	if (i > 0) {
				                		$(rows).eq( i ).before(
						                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalRCostVal + '</td><td></td><td class="t-right">' + groupTotalVal + '</td></tr>'
						                    );
				                	} */
				                    last = group;
				                    groupEnd = i;
				                    groupTotalRCost = 0;
				                    groupTotalTax = 0;
				                    groupTotal = 0;
				                }
				                groupTotalRCost += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalRCost += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                groupTotal += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                total += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                currentRow = i;
				            } );
				            if (groupEnd > 0 || rows.length == (currentRow + 1)){
				            	if(totalRCost==null || totalRCost==0){
				            		totalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalRCostVal=parseFloat(totalRCost).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalRCost==null || groupTotalRCost==0){
			                		groupTotalRCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalRCostVal=parseFloat(groupTotalRCost).toFixed(<%=numberOfDecimal%>);
			                	}
			                	if(totalTax==null || totalTax==0){
			                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
			                	}
			                	if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(total==null || total==0){
			                		totalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotal==null || groupTotal==0){
			                		groupTotalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalVal=parseFloat(groupTotal).toFixed(<%=numberOfDecimal%>);
			                	}
				            	
			                	$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupColSpan + '></td><td class="t-right">TOTAL</td><td class="t-right">' + totalVal + '</td></tr>'
			                    );
			                	/* $(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + groupTotalRCostVal + '</td><td></td><td class="t-right">' + groupTotalVal + '</td></tr>'
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
			 	              .column(6)
			 	              .data()
			 	              .reduce(function(a, b) {
			 	                return intVal(a) + intVal(b);
			 	              }, 0);

			 	            // Total over this page
			 	            totalCostVal = api
			 	              .column(6)
			 	              .data()
			 	              .reduce(function(a, b) {
			 	                return intVal(a) + intVal(b);
			 	              }, 0);
			 	              
			 	            // Update footer
			 	            $(api.column(6).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
			 	          }
				});
			    tableprint = $('#tableprint').DataTable({
					"processing": true,
					"lengthMenu": [[-1],["All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.items[0].bill === 'undefined'){
				        		return [];
				        	}else {				        		
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'bill', "orderable": true},
		    			{"data": 'billdate', "orderable": true},
		    			{"data": 'custname', "orderable": true},
		    			{"data": 'currency', "orderable": true},
		    			{"data": 'invamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [4,5,6]}],
					"orderFixed": [ ], 
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
					buttons: [],
			        "order": [],
			        searching: false,
			        paging: false,
			        info: false,
			        "bLengthChange" : false, //thought this line could hide the LengthMenu
			        "drawCallback": function ( settings ) {
			        	this.attr('width', '100%');
						var groupColumn = 0;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var groupEnd = 0;
			            var currentRow = 0;
			            var total = 0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                total += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	if(total==null || total==0){
		                		totalVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalVal=parseFloat(total).toFixed(<%=numberOfDecimal%>);
		                	}
			            	
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupColSpan + '></td><td class="t-right">TOTAL</td><td class="t-right">' + totalVal + '</td></tr>'
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
			
		</script>
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
		    return '<div onclick="(\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
			
		});

	/* Invoice No Auto Suggestion */
	$('#auto_invoiceNo').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_BILL_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				NAME : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.InvoiceDetails);
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
		    return '<p>' + data.INVOICE + '</p>';
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

		});
 });
 </script>
 <script type="text/javascript">
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
    function getdate()
    {
    	var  fdate = document.form1.FROM_DATE.value;
    	var  tdate = document.form1.TO_DATE.value;
    	if(tdate=='')
    		tdate='<%=collectionDate%>';
    	document.getElementById('FDATE').innerHTML ="Date From : "+fdate+" To : "+tdate;
      }
    function generatePdf(){
		
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
	
		var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
		var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
		var rY=15;	/*right Y-axis*/
		doc.setFontSize(20);
		doc.setFontStyle("bold");
		doc.text('<%=CNAME%>', 16, rY+2);
		doc.text('Sales Invoice Summary', 16, rY+=10);
		doc.setFontStyle("normal");
		doc.text('<%=FDate%>', 16, rY+=4);
		var totalPagesExp = "{total_pages_cont_string}";
		
		//doc.fromHTML(document.getElementById('table'));
		doc.autoTable({
			html : '#tableprint',
			startY : rY+=5,
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center',
				fontSize : 10
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontSize : 10
			},
			footStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontStyle : 'normal',
				halign : 'right'
			},
			theme : 'plain',
			columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'},6: {halign: 'center'}},
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
				doc.text(str, 185,
						pageHeight - 10);
			},
			didParseCell : function(data) {						
				for(i=0;i<data.table.body.length;i++){
					
					if(data.table.body[i].cells[6] != undefined){
						data.table.body[i].cells[6].styles["halign"]="right";
					}
				}
			}
		});
		let finalY = doc.previousAutoTable.finalY;
		if(pageNumber < doc.internal.getNumberOfPages()){
			// Footer
			var str = "Page " + doc.internal.getNumberOfPages()
			// Total page number plugin only available in jspdf v1.0+
			if (typeof doc.putTotalPages === 'function') {
				str = str + " of " + totalPagesExp;
			}
			doc.setFontSize(10);

			// jsPDF 1.4+ uses getWidth, <1.4 uses .width
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height
					: pageSize.getHeight();
			doc.text(str, 185, pageHeight - 10);
		}
		
		if(pageNumber == doc.internal.getNumberOfPages()){
			// Footer
			doc.setFontSize(10);
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
		}
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
		doc.save('SalesInvoiceSummary.pdf');
	}
    function generate() {
		
		var img = toDataURL($("#logo_content").attr("src"),
				function(dataUrl) {
					generatePdf(dataUrl);
			  	},'image/jpeg');
			
		}
    function toDataURL(src, callback, outputFormat) {
		  var img = new Image();
		  img.crossOrigin = 'Anonymous';
		  img.onload = function() {
		    var canvas = document.createElement('CANVAS');
		    var ctx = canvas.getContext('2d');
		    var dataURL;
		    canvas.height = this.naturalHeight;
		    canvas.width = this.naturalWidth;
		    ctx.drawImage(this, 0, 0);
		    dataURL = canvas.toDataURL(outputFormat);
		    callback(dataURL);
		  };
		  img.src = src;
		  if (img.complete || img.complete === undefined) {
		    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
		    img.src = src;
		  }
	}
</script>
<style id="table_style" type="text/css">
	body { counter-reset: page; }
 	.printtable
    {
        /* border: 1px solid #ccc; */
        border-collapse: collapse;
        font-size: smaller;
    }
 	.printtable th, .printtable td
    {
        padding: 5px;
        /* border: 1px solid #ccc; */
    }
	.printtable tr:last-child {
    font-weight: bold;
    border-top: 1pt solid black;
	}
	.printtable tr:first-child {
    border-top: 1pt solid black;
	}
	.printtable td:nth-last-child(1), td:nth-last-child(2), td:nth-last-child(3) {
    text-align: right;
	}
	#dvContents {
    display: table;
}
	#pageFooter {
    display: table-footer-group;
}

#pageFooter:after {
    counter-increment: page;
    content:"Page No: " counter(page);
    left: 0; 
    top: 100%;
    white-space: nowrap; 
    z-index: 20px;
    -moz-border-radius: 5px; 
    -moz-box-shadow: 0px 0px 4px #222;  
    background-image: -moz-linear-gradient(top, #eeeeee, #cccccc);  
    background-image: -moz-linear-gradient(top, #eeeeee, #cccccc);  
  }
	
div.portrait-content {
page: portrait;
}
</style>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>