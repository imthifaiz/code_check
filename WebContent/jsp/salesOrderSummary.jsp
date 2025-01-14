<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Order Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String fieldDesc = "", FROM_DATE="", TO_DATE="", ORDERNO="",POSSEARCH="1";
String msg = (String)request.getAttribute("Msg");
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryExport=false,displaySummaryImport=false,displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryNew = ub.isCheckValAcc("newsalesorder", plant,USERID);
displaySummaryLink = ub.isCheckValAcc("summarylnksalesorder", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportforsalesorder", plant,USERID);
displaySummaryImport = ub.isCheckValAcc("importsalesorder", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("newsalesorder", plant,USERID);	
	displaySummaryLink = ub.isCheckValinv("summarylnksalesorder", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportforsalesorder", plant,USERID);
	displaySummaryImport = ub.isCheckValinv("importsalesorder", plant,USERID);
}
displaySummaryImport=true;//res

PlantMstDAO plantMstDAO = new PlantMstDAO();
String currency=plantMstDAO.getBaseCurrency(plant);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String Shopify = plantMstDAO.getshopify(plant);
String Shopee = plantMstDAO.getshopee(plant);
String ENABLE_POS = plantMstDAO.getispos(plant);
//RESVI
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry

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

POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null")){
	if(ENABLE_POS.equals("1"))
		POSSEARCH="3";
	else
		POSSEARCH="1";
}
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/salesOrderSummary.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Order Summary</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
	                    <!--        RESVI STARTS -->
	                    
	                     <div class="btn-group" role="group">
              <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default" data-toggle="dropdown" >Import <span class="caret"></span></button>
						<ul class="dropdown-menu" style="min-width: 0px;">
			            <li id="Import-product"><a onClick="window.location.href='../salesorder/Impsalesorder'">Sales Order</a></li>
			            <li id="Import-product"><a onClick= "window.location.href='../salesorder/ImpSOproductremark'">Sales Order Product Remarks</a></li>
			            <%if(Shopify.equalsIgnoreCase("1")){ %>
			            <li id="Import-product"><a onClick= "window.location.href='../salesorder/ImpShopify'">Shopify Sales Order</a></li><%} %>
			            <%if (Shopee.equalsIgnoreCase("1")){ %>
			            <li id="Import-product"><a onClick= "window.location.href='../salesorder/ImpShopee'">Shopee Sales Order</a></li><%} %>
						</ul>
						
					&nbsp;
					<%}%>
				</div>
				
				

             
<!-- 	          ends -->
                  
	              <%if(displaySummaryNew){ %>
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../salesorder/new'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
	              <%}%>
	              </div>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="padding: 18px; display:none;">
				<input type="hidden" name="CUST_CODE" >
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
						<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE"
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE"
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
<!-- 							<div class="input-group">  -->
								<input type="text" class="ac-selected  form-control typeahead" 
									id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon"  -->
<!-- 									onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
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
					  		<input type="text" name="approvestatus" id="approvestatus" class="ac-selected form-control" 
					  		placeholder="APPROVE STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'approvestatus\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div>
	<input type="hidden" value="<%=displaySummaryLink%>" name="displaySummaryLink" id="displaySummaryLink" />
	<input type="hidden" value="<%=displaySummaryExport%>" name="displaySummaryExport" id="displaySummaryExport" /> 
	<input type="hidden" value="<%=COMP_INDUSTRY%>" name="COMP_INDUSTRY" id="COMP_INDUSTRY" />
	<input type="hidden" name="POSSEARCH" value="<%=StrUtils.forHTMLTag(POSSEARCH)%>"> 
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
				      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
			      	</div>
      				<div class="ShowSingle"></div>
       	  		</div>
       	  		<div class="form-group">
       	  <% if(ENABLE_POS.equals("1")){ %>
					<label class="control-label col-sm-1" for="view">View By :</label>
				  	<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>Both Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="2" id="done" <%if(POSSEARCH.equalsIgnoreCase("2")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>ERP Sales</b></label>
  					<label class="radio-inline">
  					<input name="POSSTATUS" type="radio" value="3" id="notdone" <%if(POSSEARCH.equalsIgnoreCase("3")) {%>checked <%}%> onclick="changepostype(this.value)"> <b>POS Sales</b></label>
  		 <% } else {%>
  					<input name="POSSTATUS" type="radio" hidden value="1" id="all" <%if(POSSEARCH.equalsIgnoreCase("1")) {%>checked <%}%> onclick="changepostype(this.value)">
  		 <% } %>
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
										<th class="text-center" style="font-size: smaller;">DATE</th>
										<th class="text-center" style="font-size: smaller;">ORDER NO.</th>
										<th class="text-center" style="font-size: smaller;">CUSTOMER</th>									
										<th class="text-center" style="font-size: smaller;">DELIVERY DATE</th>
										<th class="text-center" style="font-size: smaller;">STATUS</th>
										<th class="text-center" style="font-size: smaller;">APPROVE STATUS</th>
										<th class="text-center" style="font-size: smaller;">AMOUNT</th>
										<th class="text-center" style="font-size: smaller;">EXCHANGE RATE</th>
										<th class="text-center" style="font-size: smaller;">AMOUNT (<%=currency%>)</th>
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
var FROM_DATE,TO_DATE,USER,ORDERNO,REFERENCE,STATUS,APPROVESTATUS,ORDERTYPE,POSSEARCH, groupRowColSpan = 6;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,"ORDERNO":ORDERNO,"REFERENCE":REFERENCE,"STATUS":STATUS,"APPROVESTATUS":APPROVESTATUS,"ORDERTYPE":ORDERTYPE,"POSSEARCH":POSSEARCH
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
    APPROVESTATUS = document.form1.approvestatus.value;
    ORDERTYPE = document.form1.ORDERTYPE.value;
	POSSEARCH = document.form1.POSSEARCH.value;
   var displaySummaryLink = document.form1.displaySummaryLink.value;
   var displaySummaryExport = document.form1.displaySummaryExport.value;
    
    if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   	if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
   	if(USER != null    && USER != "") { flag = true;}
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(REFERENCE != null     && REFERENCE != "") { flag = true;}
    if(STATUS != null     && STATUS != "") { flag = true;}
    if(APPROVESTATUS != null     && APPROVESTATUS != "") { flag = true;}
    if(ORDERTYPE != null     && ORDERTYPE != "") { flag = true;}
    
   /* storeInLocalStorage('salesOrderSummary_ORDERNO', ORDERNO);
	storeInLocalStorage('salesOrderSummary_ORDERTYPE', ORDERTYPE);
	storeInLocalStorage('salesOrderSummary_CUSTOMER', USER);
	storeInLocalStorage('salesOrderSummary_STATUS', STATUS);
	storeInLocalStorage('salesOrderSummary_REFERENCE', REFERENCE);
	storeInLocalStorage('salesOrderSummary_FROMDATE', FROM_DATE);
	storeInLocalStorage('salesOrderSummary_TODATE', TO_DATE);*/
    var urlStr = "../salesorder/summary";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    var numberOfDecimal =2;
    // End code modified by Deen for product brand on 11/9/12
    if (tableSalesOrderSummary){
    	tableSalesOrderSummary.ajax.url( urlStr ).load();
    }else{
    	tableSalesOrderSummary = $('#tableSalesOrderSummary').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
			searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
//			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
		        			if(displaySummaryLink == 'true'){ 
		        			data.ORDERS[dataIndex]['DONO'] = '<a href="../salesorder/detail?dono='+data.ORDERS[dataIndex]['DONO']+'">'+data.ORDERS[dataIndex]['DONO']+'</a>';
		        			}else{
		        				data.ORDERS[dataIndex]['DONO'] = data.ORDERS[dataIndex]['DONO'];	
		        			}
		        			data.ORDERS[dataIndex]['ORDAMT'] = '<div align="right">'+ data.ORDERS[dataIndex]['ORDAMT']+ '</div>';
		        			data.ORDERS[dataIndex]['EXCHANGE_RATE'] = '<div align="right">'+ data.ORDERS[dataIndex]['EXCHANGE_RATE']+ '</div>';
		        			data.ORDERS[dataIndex]['AMOUNT'] = '<div align="right">'+ data.ORDERS[dataIndex]['AMOUNT']+ '</div>';
		        			if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED')
		        				data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
		        			else if(data.ORDERS[dataIndex]['STATUS']=='Open')
			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        		else if(data.ORDERS[dataIndex]['STATUS']=='Draft')
			        			if(data.ORDERS[dataIndex]['POSSTATUS']=='CANCELLED')
				        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">DRAFT CANCELLED</span>';
				        		else
				        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        		else
			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
			        			
		        			if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='Approved')
		        				data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
		        			else if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='Waiting For Manager Approval')
			        			data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
			        		else if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='Waiting For HQ Manager Approval')
				        		data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
			        		else if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='OWNER APPROVED')
		        				data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
		        			else if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='NEW_WAITING FOR OWNER APPROVAL')
			        			data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
		        			else if(data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']=='EDIT_WAITING FOR OWNER APPROVAL')
			        			data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';
			        		else
			        			data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['APP_CUST_ORDER_STATUS']+'</span>';	
		        		}
		        		return data.ORDERS;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'DATE', "orderable": true},
    			{"data": 'DONO', "orderable": true},
    			{"data": 'CUSTOMER', "orderable": true},
    			{"data": 'DELIVERY_DATE', "orderable": true},
    			{"data": 'STATUS', "orderable": true},    			
    			{"data": 'APP_CUST_ORDER_STATUS', "orderable": true,},    			
    			{"data": 'ORDAMT', "orderable": true},
    			{"data": 'EXCHANGE_RATE', "orderable": true},
    			{"data": 'AMOUNT', "orderable": true}
			],
			/*"columnDefs": [{"className": "t-right", "targets": [6,7]}],*/
			"columnDefs": [{"className": "t-r7ight", "targets": [5,6,7]}],
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

function changepostype(count){
	  $("input[name ='POSSEARCH']").val(count);
	  onGo();
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>