<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Consignment Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String fieldDesc = "", FROM_DATE="", TO_DATE="", ORDERNO="",LOC="",LOC_ID="";
String msg = (String)request.getAttribute("Msg");
boolean displaySummaryExport=false,displaySummaryImport=false,displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryNew = ub.isCheckValAcc("newconsignment", plant,USERID);
displaySummaryLink = ub.isCheckValAcc("summarylnkconsignment", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportforconsignment", plant,USERID);
displaySummaryImport = ub.isCheckValAcc("importsalesorder", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("newconsignment", plant,USERID);	
	displaySummaryLink = ub.isCheckValinv("summarylnkconsignment", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportforconsignment", plant,USERID);
	displaySummaryImport = ub.isCheckValinv("importsalesorder", plant,USERID);
}
displaySummaryImport=true;//res

String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
PlantMstDAO plantMstDAO = new PlantMstDAO();
String currency=plantMstDAO.getBaseCurrency(plant);
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
String collectionDate=DateUtils.getDate();
ArrayList al = plantMstDAO.getPlantMstDetails(plant);
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
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/consignmentSummary.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Consignment Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
	              
	                    <!--        RESVI STARTS -->

         	 <div class="btn-group" role="group">
             <%if(displaySummaryImport){ %>
             <button type="button" class="btn btn-default"
             onClick="window.location.href='../consignment/import'">
<!--              onClick="window.location.href='../salesorder/import'" > --> 
						Import Consignment Order</button>
			      &nbsp;
				  <%}%>
				</div>
				
    
<!--               <div class="btn-group" role="group"> -->
<%--               <%if(displaySummaryImport){ %> --%>
<!--               <button type="button" class="btn btn-default" -->
<!--               onClick="window.location.href='../jsp/importOutboundProductRemarksExcelSheet.jsp'"> -->
<!-- <!--               onClick="window.location.href='../salesorder/importsales'" > --> 
<!-- 						Import Consignment Order Product Remarks</button> -->
<!-- 					&nbsp; -->
<%-- 					<%}%> --%>
<!-- 				</div> -->
		
             
<!-- 	          ends -->
                  
	              <%if(displaySummaryNew){ %>
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../consignment/new'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
	              <%}%>
	              </div>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="display:none" style="padding: 18px;">
				<input type="hidden" name="CUST_CODE" >
		
				
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" 
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" 
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" class="ac-selected  form-control typeahead" 
									id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
								<!-- <span class="btn-danger input-group-addon" 
									onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
							</div>
						</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" 
				  				value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="ORDER NO">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
				  			<input type="text" class="ac-selected form-control" id="Reference" 
				  				name="reference" placeholder="REFERENCE">							 		
			  			</div>
					</div>
                                                        
                         <!--    resvi starts-->
                         
          <div class="row" style="padding:3px">
	      <div class="col-sm-2"></div>
	      <div class="col-sm-4 ac-box" >
          <div class="input-group">
              <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type ="TEXT"  value="<%=LOC%>" placeholder= "FROM LOCATION" >
              <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	     <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!-- 	     <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
            </div>   
           </div>
                    
            <div class="col-sm-4 ac-box" >
            <div class="input-group">
                 <INPUT class="ac-selected  form-control typeahead" name="LOC_ID" ID="LOC_ID" type ="TEXT"  value="<%=LOC_ID%>" placeholder= "TO LOCATION" >
                 <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	         <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC_ID.value);"> -->
<!-- 	         <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
	             
             </div>
             </div>
             </div>
             
                                 <!-- ends -->

					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ORDERTYPE" name="ORDERTYPE" 
				  				value="" placeholder="ORDER TYPE">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="status" id="status" class="ac-selected form-control" 
					  		placeholder="STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div>
	  
	  <input type="hidden" value="<%=displaySummaryLink%>" name="displaySummaryLink" id="displaySummaryLink" />
	 <input type="hidden" value="<%=displaySummaryExport%>" name="displaySummaryExport" id="displaySummaryExport" /> 
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">
							<button type="button" class="btn btn-success" 
								onClick="javascript:return onGo();">Search</button>
						</div>
					</div>		
				</div>
				</div>
				<div class="form-group">
      				<div class="col-sm-3">
				      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
				      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
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
       	  					<table id="tableSalesOrderSummary" class="table table-bordred table-striped">                   
			                   <thead>
									<tr><th style="font-size: smaller;">DATE</th>
									<th style="font-size: smaller;">ORDER NO.</th>
									<th style="font-size: smaller;">CUSTOMER</th>	
									<th style="font-size: smaller;">FROM LOCATION</th>	
									<th style="font-size: smaller;">TO LOCATION</th>									
									<th style="font-size: smaller;">DELIVERY DATE</th>
									<th style="font-size: smaller;">STATUS</th>
									<th style="font-size: smaller;">AMOUNT</th>
									<th style="font-size: smaller;">EXCHANGE RATE</th>
									<th style="font-size: smaller;">AMOUNT (<%=currency%>)</th></tr>
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

 var tableSalesOrderSummary;
 var FROM_DATE,TO_DATE,USER,ORDERNO,REFERENCE,STATUS,LOC,LOC_ID,ORDERTYPE, groupRowColSpan = 6;

 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,
 		"CNAME":USER,"ORDERNO":ORDERNO,"REFERENCE":REFERENCE,"STATUS":STATUS,"LOC":LOC,"LOC_ID":LOC_ID,"ORDERTYPE":ORDERTYPE,
 	}
 }  

 function onGo(){
    var flag = "false";
     FROM_DATE = document.form1.FROM_DATE.value;
     TO_DATE = document.form1.TO_DATE.value;		    
     USER = document.form1.CUSTOMER.value;
     ORDERNO = document.form1.ORDERNO.value;
     REFERENCE = document.form1.reference.value;
     STATUS = document.form1.status.value;
     LOC = document.form1.LOC.value;
     LOC_ID = document.form1.LOC_ID.value;
     ORDERTYPE = document.form1.ORDERTYPE.value;
     
    var displaySummaryLink = document.form1.displaySummaryLink.value;
    var displaySummaryExport = document.form1.displaySummaryExport.value;
     
     if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
    	if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
    	if(USER != null    && USER != "") { flag = true;}
     if(ORDERNO != null     && ORDERNO != "") { flag = true;}
     if(REFERENCE != null     && REFERENCE != "") { flag = true;}
     if(STATUS != null     && STATUS != "") { flag = true;}
     if(LOC != null     && LOC != "") { flag = true;}
     if(LOC_ID != null     && LOC_ID != "") { flag = true;}
     if(ORDERTYPE != null     && ORDERTYPE != "") { flag = true;}
     
     var urlStr = "../consignment/summary";
    	// Call the method of JQuery Ajax provided
    	var groupColumn = 1;
    	var totalQty = 0;
     // End code modified by Deen for product brand on 11/9/12
     if (tableSalesOrderSummary){
     	tableSalesOrderSummary.ajax.url( urlStr ).load();
     }else{
     	tableSalesOrderSummary = $('#tableSalesOrderSummary').DataTable({
 			"processing": true,
 			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
 			//"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
 		        		for(var dataIndex = 0; dataIndex < data.ORDERS.length; dataIndex ++){
 		        			data.ORDERS[dataIndex]['ORDAMT'] = '<div align="right">'+ data.ORDERS[dataIndex]['ORDAMT']+ '</div>';
 		        			data.ORDERS[dataIndex]['AMOUNT'] = '<div align="right">'+ data.ORDERS[dataIndex]['AMOUNT']+ '</div>';
 		        			if(displaySummaryLink == 'true'){ 
 		        			data.ORDERS[dataIndex]['TONO'] = '<a href="../consignment/detail?tono='+data.ORDERS[dataIndex]['TONO']+'">'+data.ORDERS[dataIndex]['TONO']+'</a>';
 		        			}else{
 		        				data.ORDERS[dataIndex]['TONO'] = data.ORDERS[dataIndex]['TONO'];	
 		        			}
 		        			if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED')
 		        				data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
 		        			else if(data.ORDERS[dataIndex]['STATUS']=='Open')
 			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
 			        		else if(data.ORDERS[dataIndex]['STATUS']=='Draft')
 				        		data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
 			        		else
 			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
 		        		}
 		        		return data.ORDERS;
 		        	}
 		        }
 		    },
 	        "columns": [
     			{"data": 'DATE', "orderable": true},
     			{"data": 'TONO', "orderable": true},
     			{"data": 'CUSTOMER', "orderable": true},
     			{"data": 'FROMLOCATION', "orderable": true},
     			{"data": 'TOLOCATION', "orderable": true},
     			{"data": 'DELIVERY_DATE', "orderable": true},
     			{"data": 'STATUS', "orderable": true},    			
     			{"data": 'ORDAMT', "orderable": true},
     			{"data": 'EXCHANGE_RATE', "orderable": true},
     			{"data": 'AMOUNT', "orderable": true}
 			],
 			"columnDefs": [{"className": "t-right", "targets": [7,8,9]}],
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
	        	if(displaySummaryExport == 'false'){ 
	        	$('.buttons-collection')[0].style.display = 'none';
	        	 } 
	        	}
		});

    	$("#tableSalesOrderSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
        	tableSalesOrderSummary.draw();
        });
    }
}
 
var plant= '<%=plant%>';

/* From location Auto Suggestion */
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
		//return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
		return '<div onclick="document.form.LOC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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


 /* To location Auto Suggestion */
$('#LOC_ID').typeahead({
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
		//return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
			return '<div onclick="document.form.LOC_ID.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>