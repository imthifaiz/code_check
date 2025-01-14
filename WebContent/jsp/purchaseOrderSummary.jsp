<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Purchase Order Summary";
// String plant = (String)session.getAttribute("PLANT");
String fieldDesc = "", FROM_DATE="", TO_DATE="", ORDERNO="";
String msg = (String)request.getAttribute("Msg");
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
// RESVI STARTS
boolean displaySummaryNew=false,displaySummaryImport=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryNew = ub.isCheckValAcc("newpurchaseorder", plant,USERID);
displaySummaryLink = ub.isCheckValAcc("summarylnkpurchaseorder", plant,USERID);
displayPrintPdf = ub.isCheckValAcc("printpurchaseorder", plant,USERID);
displayMore = ub.isCheckValAcc("morepurchaseorder", plant,USERID);
displaySummaryEdit = ub.isCheckValAcc("editpurchaseorder", plant,USERID);
displaySummaryImport = ub.isCheckValAcc("importpurchaseorder", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("newpurchaseorder", plant,USERID);	
	displaySummaryLink = ub.isCheckValinv("summarylnkpurchaseorder", plant,USERID);
	displayPrintPdf = ub.isCheckValinv("printpurchaseorder", plant,USERID);
	displayMore = ub.isCheckValinv("morepurchaseorder", plant,USERID);
	displaySummaryEdit = ub.isCheckValinv("editpurchaseorder", plant,USERID);
	displaySummaryImport = ub.isCheckValinv("importpurchaseorder", plant,USERID);
}
displaySummaryImport=true;//res
// RESVI ENDS
PlantMstDAO plantMstDAO = new PlantMstDAO();
String currency=plantMstDAO.getBaseCurrency(plant);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

//Azees From Date -30
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);

PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();
boolean approvalcheck = false;
boolean approvalcheckc = pltApproval.CheckApproval(plant, "PURCHASE", "CREATE");
boolean approvalchecku = pltApproval.CheckApproval(plant, "PURCHASE", "UPDATE");
boolean approvalcheckd = pltApproval.CheckApproval(plant, "PURCHASE", "DELETE");
if(approvalcheckc || approvalchecku || approvalcheckd){
	approvalcheck = true;
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
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

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
<jsp:param name="submenu" value="<%=IConstants.PURCHASE_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/purchaseOrderSummary.js"></script>
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
                <li><label>Purchase Order Summary</label></li>                                   
            </ul>             
      	    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
	                 
	                       <!--        RESVI STARTS -->

         	 <div class="btn-group" role="group">
             <%if(displaySummaryImport){ %>
             <button type="button" class="btn btn-default"
             onClick="window.location.href='../jsp/importInboundOrderExcelSheet.jsp'">
<!--              	onClick="window.location.href='../purchaseorder/import'" > -->
						Import Purchase Order</button>
			      &nbsp;
				  <%}%>
				</div>
				
    
              <div class="btn-group" role="group">
              <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default"
              onClick="window.location.href='../jsp/importInboundProductRemarksExcelSheet.jsp'">
<!--               	onClick="window.location.href='../purchaseorder/importpurchase'" > -->
						Import Purchase Order Product Remarks</button>
					&nbsp;
					<%}%>
				</div>
<!-- 				ends -->
	          
	              <%if(displaySummaryNew){ %>
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../purchaseorder/new'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp; 
	              	   <%}%>             
	              </div>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="padding: 18px; display:none;">
				<input type="hidden" name="CUST_CODE" >
				<input type="hidden" name="plant" value="<%=plant%>" >
				<input type="hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>" >
				<input type="hidden" name="collectionDate" value="<%=collectionDate%>" >
				<input type="hidden" name="CNAME" value="<%=CNAME%>" >
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
						<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="fromdate"
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" id="todate" type = "text" 
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
<!-- 							<div class="input-group">  -->
								<input type="text" class="ac-selected  form-control typeahead" 
									id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER">
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon"  -->
<!-- 									onClick="javascript:popUpWin('supplierlist.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 							</div> -->
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
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="approve_status" name="approve_status" 
				  				value="" placeholder="APPROVE STATUS">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'approve_status\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
					</div>
					
<!-- 					resvi adds -->
						<input type="hidden" value="<%=displaySummaryLink%>" name="displaySummaryLink" id="displaySummaryLink" />
<!-- 						resvi ends -->
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
				      <a href="#" class="Hide" style="font-size: 15px">Hide Advanced Search</a>
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
									<tr>
										<th style="font-size: smaller;">PAYMENT</th>
										<th style="font-size: smaller;">DATE</th>
										<th style="font-size: smaller;">ORDER NO.</th>
										<th style="font-size: smaller;">SUPPLIER</th>									
										<th style="font-size: smaller;">ORDER DATE</th>
										<th style="font-size: smaller;">STATUS</th>
										<th style="font-size: smaller;">APPROVE STATUS</th>
										<th style="font-size: smaller;">AMOUNT</th>
										<th style="font-size: smaller;">EXCHANGE RATE</th>
										<th style="font-size: smaller;">AMOUNT (<%=currency%>)</th>
									</tr>
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
var FROM_DATE,TO_DATE,USER,ORDERNO,REFERENCE,STATUS,APPROVE_STATUS,ORDERTYPE, groupRowColSpan = 6;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,"ORDERNO":ORDERNO,"REFERENCE":REFERENCE,"STATUS":STATUS,"APPROVE_STATUS":APPROVE_STATUS,"ORDERTYPE":ORDERTYPE
	}
}  

function onGo(){
	   var flag = "false";
	    var collectionDate = document.form1.collectionDate.value;
	    var CNAME = document.form1.CNAME.value;
	    var dataviewtitle =CNAME+' \n \n Purchase Order Summary \n \n As On '+collectionDate;
	    FROM_DATE = document.form1.FROM_DATE.value;
	    TO_DATE = document.form1.TO_DATE.value;		    
	    USER = document.form1.CUSTOMER.value;
	    ORDERNO = document.form1.ORDERNO.value;
	    REFERENCE = document.form1.reference.value;
	    STATUS = document.form1.status.value;
	    APPROVE_STATUS = document.form1.approve_status.value;
	    ORDERTYPE = document.form1.ORDERTYPE.value;
	    var displaySummaryLink = document.form1.displaySummaryLink.value;
	    var numberOfDecimal = document.form1.numberOfDecimal.value;
	    
	    if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   	if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
	   	if(USER != null    && USER != "") { flag = true;}
	    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
	    if(REFERENCE != null     && REFERENCE != "") { flag = true;}
	    if(STATUS != null     && STATUS != "") { flag = true;}
	    if(APPROVE_STATUS != null     && APPROVE_STATUS != "") { flag = true;}
	    if(ORDERTYPE != null     && ORDERTYPE != "") { flag = true;}
	   /*	storeInLocalStorage('purchaseorderSummary_ORDERNO', ORDERNO);
		storeInLocalStorage('purchaseorderSummary_ORDERTYPE', ORDERTYPE);
		storeInLocalStorage('purchaseorderSummary_CUSTOMER', USER);
		storeInLocalStorage('purchaseorderSummary_STATUS', STATUS);
		storeInLocalStorage('purchaseorderSummary_REFERENCE', REFERENCE);
		storeInLocalStorage('purchaseorderSummary_FROMDATE', FROM_DATE);
		storeInLocalStorage('purchaseorderSummary_TODATE', TO_DATE);*/
	    var urlStr = "../purchaseorder/summary";
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
			        			data.ORDERS[dataIndex]['EXCHAGERATE'] = parseFloat(data.ORDERS[dataIndex]['EXCHAGERATE']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
//			        			data.ORDERS[dataIndex]['CAMOUNT'] = parseFloat(data.ORDERS[dataIndex]['CAMOUNT']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
			        			
			        			if(displaySummaryLink == 'true'){ 
			        			data.ORDERS[dataIndex]['PONO'] = '<a href="../purchaseorder/detail?pono='+data.ORDERS[dataIndex]['PONO']+'">'+data.ORDERS[dataIndex]['PONO']+'</a>';
			        			 } else{
		        			    	 data.ORDERS[dataIndex]['PONO'] =  data.ORDERS[dataIndex]['PONO'];
//		        			    		 '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#">'+data.ORDERS[dataIndex]['PONO']+'</a>';
		        			     }
			        			if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED'){
			        				data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        			}else if(data.ORDERS[dataIndex]['STATUS']=='Open'){
				        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        			}else if(data.ORDERS[dataIndex]['STATUS']=='Draft'){
					        		data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        			}else{
				        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
			        			}
			        			if(data.ORDERS[dataIndex]['APPROVE_STATUS']=='OWNER APPROVED'){
			        				data.ORDERS[dataIndex]['APPROVE_STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APPROVE_STATUS']+'</span>';
			        			}else if(data.ORDERS[dataIndex]['APPROVE_STATUS']=='NEW_WAITING FOR OWNER APPROVAL'){
				        			data.ORDERS[dataIndex]['APPROVE_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APPROVE_STATUS']+'</span>';
			        			}else if(data.ORDERS[dataIndex]['APPROVE_STATUS']=='EDIT_WAITING FOR OWNER APPROVAL'){
				        			data.ORDERS[dataIndex]['APPROVE_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APPROVE_STATUS']+'</span>';
			        			}else{
				        			data.ORDERS[dataIndex]['APPROVE_STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APPROVE_STATUS']+'</span>';	
			        			}
			        			
			        			if(data.ORDERS[dataIndex]['PAY_STATUS'] =='0'){
									//data.ORDERS[dataIndex]['PAY_STATUS'] ='<i class="fa fa-times-circle" style="color:red" aria-hidden="true"></i>'
									data.ORDERS[dataIndex]['PAY_STATUS'] ='';
								}else if(data.ORDERS[dataIndex]['PAY_STATUS'] =='1'){
									data.ORDERS[dataIndex]['PAY_STATUS'] ='<i class="fa fa-circle" style="color:red" aria-hidden="true"></i>';
								}else{
									data.ORDERS[dataIndex]['PAY_STATUS'] ='<i class="fa fa-check-circle" style="color:green" aria-hidden="true"></i>';
								}	
			        			
			        			}
			        			
			        			
			        		return data.ORDERS;
			        	}
			        }
			    },
		        "columns": [
					{"data": 'PAY_STATUS', "orderable": true},
	    			{"data": 'DATE', "orderable": true},
	    			{"data": 'PONO', "orderable": true},
	    			{"data": 'SUPPLIER', "orderable": true},
	    			{"data": 'COLLECTION_DATE', "orderable": true},
	    			{"data": 'STATUS', "orderable": true}, 
	    			{"data": 'APPROVE_STATUS', "orderable": true},    			
	    			{"data": 'AMOUNT', "orderable": true},
	    			{"data": 'EXCHAGERATE', "orderable": true},
	    			{"data": 'CAMOUNT', "orderable": true}
				],
				/*"columnDefs": [{"className": "t-right", "targets": [6,7]}],*/
				"columnDefs": [{"className": "t-right", "targets": [7,8,9]},{"className": "text-center", "targets": [0]}],
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
	      	                	//columns: [':visible']
	                      columns: [1,2,3,4,5,6,7,8,9]
	      	                },
	      	                title: '<%=title%>',
	      	                footer: true
	                      },
	                      {
	                      	extend : 'pdf',
	                          footer: true,
	                      	text: 'PDF Portrait',
	                      	exportOptions: {
	                      	//	columns: [':visible']
	                      columns: [1,2,3,4,5,6,7,8,9]
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
	                      		//columns: [':visible']
	                      columns: [1,2,3,4,5,6,7,8,9]
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
$(document).ready(function(){
 onGo();
});

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>