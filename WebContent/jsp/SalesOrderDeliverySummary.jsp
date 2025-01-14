<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!-- ifstart -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- ifend -->
<%
String title = "Sales Order Delivery Summary";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'SalesOrderDeliverySummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	ArrayList locQryList = new ArrayList();
	String fieldDesc = "";
	String PLANT = "";
	int Total = 0;
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg = (String)request.getAttribute("Msg");
	fieldDesc = (String)request.getAttribute("result");
	String FROM_DATE="", TO_DATE="", ORDERNO="";
	String PGaction = (String)request.getAttribute("PGaction");
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	/* ifstart */
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(PLANT);//Check Company Industry
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	String curDate =du.getDateMinusDays();
	if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
		curDate =DateUtils.getDate();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
	/* ifend */
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
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Sales Order Delivery Summary</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
<!-- 				<div class="box-title pull-right"> -->
<%-- 				 <%if(displaySummaryNew){ %> --%>
<!--               	<button type="button" class="btn btn-default" onclick="window.location.href='../contact/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp; -->
<%--               	 <%}%> --%>
<!--               	</div> -->
		</div>
		
 <div class="box-body">
 
<FORM class="form-horizontal" name="form1" method="post" action="SalesOrderDeliverySummary.jsp">
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="PGaction" value="View"> 
<div id="target" style="padding: 18px; display:none;">
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
								<input type="text" class="ac-selected  form-control typeahead" 
									id="DONO" placeholder="ORDER NUMBER" name="DONO">
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'DONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="INVOICE" name="INVOICE" 
				  				placeholder="INVOICE NO">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'INVOICE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ITEM" name="ITEM" 
				  				placeholder="PRODUCT">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ORDERSTATUS" name="ORDERSTATUS" 
				  				value="" placeholder="ORDER STATUS">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERSTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="SHIPPEDSTATUS" id="SHIPPEDSTATUS" class="ac-selected form-control" 
					  		placeholder="SHIPPED STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'SHIPPEDSTATUS\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" name="INTRANSITSTATUS" id="INTRANSITSTATUS" class="ac-selected form-control" 
					  		placeholder="INTRANSIT STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'INTRANSITSTATUS\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="DELIVERYSTATUS" id="DELIVERYSTATUS" class="ac-selected form-control" 
					  		placeholder="DELIVERY STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'DELIVERYSTATUS\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div>
					
					
	<!-- 				<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="DELIVERYSTATUS" id="DELIVERYSTATUS" class="ac-selected form-control" 
					  		placeholder="DELIVERY STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'DELIVERYSTATUS\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div> -->
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
	</form>
	<br>
	
<div style="overflow-x:auto;">

 <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
  </div>		                       
<table id="table" class="table table-bordred table-striped" > 
   
   <thead>
   <% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
          <tr>  
            <th style="text-align: left !important;">S/N</th>  
            <th style="text-align: left !important;">Order Number</th>
            <th style="text-align: left !important;">Invoice Number</th>
			<th style="text-align: left !important;">Product</th>
            <th style="text-align: left !important;">Qty</th>
            <th style="text-align: left !important;">Order Status</th>  
            <th style="text-align: left !important;">Shipped Status</th>  
            <th style="text-align: left !important;">Delivery Status</th>  
            <th style="text-align: left !important;">Delivery Date</th>  
            <th style="text-align: left !important;">Delivery Person</th>  
            <th style="text-align: left !important;">Received Person</th>  
          </tr>  
        </thead> 
          <!-- ifstart --> 
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
								<th></th>
							</tr>
						</tfoot>
						<!-- ifend -->
   <% }else if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
             <tr>  
            <th style="text-align: left !important;">S/N</th>  
            <th style="text-align: left !important;">Order Number</th>
            <th style="text-align: left !important;">Invoice Number</th>
			<th style="text-align: left !important;">Product</th>
            <th style="text-align: left !important;">Qty</th>
            <th style="text-align: left !important;">Order Status</th>  
            <th style="text-align: left !important;">Shipped Status</th>  
            <th style="text-align: left !important;">Shipped Date</th>  
            <th style="text-align: left !important;">InTransit Status</th>  
            <th style="text-align: left !important;">InTransit Date</th>  
            <th style="text-align: left !important;">Delivery Status</th>  
            <th style="text-align: left !important;">Delivery Date</th>  
            <th style="text-align: left !important;">Delivery Person</th>  
            <th style="text-align: left !important;">Received Person</th>  
          </tr>  
        </thead> 
          <!-- ifstart --> 
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
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- ifend -->
   <% }%>
</table>
</div>  
<!-- ifstart -->
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var FROM_DATE,TO_DATE,ORDERNO,INVOICENO,PRODUCT,ORDERSTATUS,SHIPPEDSTATUS,DELIVERYSTATUS,INTRANSITSTATUS;
		var tabletype;
		var groupRowColSpan = 7;
		
		function getParameters(){
			return {
				"PLANT":plant,
				"FDATE":FROM_DATE,
				"TDATE":TO_DATE,
				"DONO":ORDERNO,
				"INVOICE":INVOICENO,
				"PRODUCT":PRODUCT,
				"STATUS":ORDERSTATUS,
				"SHIPPEDSTATUS":SHIPPEDSTATUS,
				"INTRANSITSTATUS":INTRANSITSTATUS,
				"DELIVERYSTATUS":DELIVERYSTATUS,
				"action":"GET_SALES_DELIVERY_SUMMARY"
			}
		}
		function onGo(){
		    FROM_DATE = document.form1.FROM_DATE.value;
		    TO_DATE = document.form1.TO_DATE.value;		    
		    ORDERNO = document.form1.DONO.value;
		    INVOICENO = document.form1.INVOICE.value;
		    PRODUCT = document.form1.ITEM.value;
		    ORDERSTATUS = document.form1.ORDERSTATUS.value;
		    SHIPPEDSTATUS = document.form1.SHIPPEDSTATUS.value;
		    DELIVERYSTATUS = document.form1.DELIVERYSTATUS.value;
		    INTRANSITSTATUS = document.form1.INTRANSITSTATUS.value;
			var urlStr = "/track/MasterServlet";
			   
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[50, 100, 500], [50, 100, 500]],

						searching: true, // Enable searching
				        search: {
				            regex: true,   // Enable regular expression searching
				            smart: false   // Disable smart searching for custom matching logic
				        },
				        
// 						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.DELIVERYLIST === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.DELIVERYLIST.length; dataIndex ++){
						        				var lineno = data.DELIVERYLIST[dataIndex].ID;
						        				var sno=dataIndex+1;
						        				data.DELIVERYLIST[dataIndex]['SNO'] = sno;
						        				data.DELIVERYLIST[dataIndex]['QTYPICK'] = parseFloat(data.DELIVERYLIST[dataIndex]['QTYPICK']).toFixed(3).replace(/\d(?=(\d{3})+\.)/g, "$&,");
						        			}
						        		return data.DELIVERYLIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	<% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'DONO', "orderable": true},
				        	{"data": 'INVOICENO', "orderable": true},
				        	{"data": 'ITEM', "orderable": true},
				        	{"data": 'QTYPICK', "orderable": true},
				        	{"data": 'STATUS', "orderable": true},
				        	{"data": 'PICKSTATUS', "orderable": true},
				        	{"data": 'BILL_STATUS', "orderable": true},
				        	{"data": 'DELIVERYDATE', "orderable": true},
			    			{"data": 'DELIVERYPERSON', "orderable": true},
			    			{"data": 'RECEIVEDPERSON', "orderable": true},
				        	<% }else if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'DONO', "orderable": true},
				        	{"data": 'INVOICENO', "orderable": true},
				        	{"data": 'ITEM', "orderable": true},
				        	{"data": 'QTYPICK', "orderable": true},
				        	{"data": 'STATUS', "orderable": true},
				        	{"data": 'SHIPPING_STATUS', "orderable": true},
				        	{"data": 'SHIPPING_DATE', "orderable": true},
				        	{"data": 'INTRANSIT_STATUS', "orderable": true},
				        	{"data": 'INTRANSIT_DATE', "orderable": true},
				        	{"data": 'DELIVERY_STATUS', "orderable": true},
				        	{"data": 'DELIVERY_DATE', "orderable": true},
// 				        	{"data": 'DELIVERYDATE', "orderable": true},
			    			{"data": 'DELIVERYPERSON', "orderable": true},
			    			{"data": 'RECEIVEDPERSON', "orderable": true},
				        	<% }%>
			    			],
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
		    	    	                	//columns: [':visible']
			    	                    		<% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10]
						                    	<% }else if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
						                    	<% }%>
		    	    	                },
		    	    	                title: '<%=title%>',
		    	    	                footer: true
		    	                    },
		    	                    {
		    	                    	extend : 'pdf',
		                                footer: true,
		    	                    	text: 'PDF Portrait',
		    	                    	exportOptions: {
			    	                    		<% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10]
						                    	<% }else if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
						                    	<% }%>
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
			    	                    		<% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10]
						                    	<% }else if(!COMP_INDUSTRY.equals("Centralised Kitchen")){%>
						                    	columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
						                    	<% }%>
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
			                }		                
				        ],
				        "order": [],

					});

			    	 $("#table_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
			         	table.draw();
			         });
			    }
			    
			}

		</script>
		<!-- END -->
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
	 
    $('[data-toggle="tooltip"]').tooltip();

    /* Order Number Auto Suggestion */
	$('#DONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						CNAME : document.form1.DONO.value,
						DONO : query
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
		    return '<p>' + data.DONO + '</p>';
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
				document.form1.DONO.value = "";
			}
		});

	$("#ORDERSTATUS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		});

	/* Invoice No Auto Suggestion */
	$('#INVOICE').typeahead({
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
			$("#plnoInvoice").typeahead('val', '"');
			$("#plnoInvoice").typeahead('val', '');
		});

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

	$("#SHIPPEDSTATUS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'picksts',
		  display: 'value',  
		  source: substringMatcher(picksts),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		});
	
	$("#DELIVERYSTATUS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'delsts',
		  display: 'value',  
		  source: substringMatcher(delsts),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		});
	
	$("#INTRANSITSTATUS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'intransts',
		  display: 'value',  
		  source: substringMatcher(intransts),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		});
	
});

	var postatus =   [{
		    "year": "OPEN",
		    "value": "OPEN",
		    "tokens": [
		      "OPEN"
		    ]
		  },
		  {
			    "year": "PROCESSED",
			    "value": "PROCESSED",
			    "tokens": [
			      "PROCESSED"
			    ]
			  },
			  {
				    "year": "PARTIALLY PROCESSED",
				    "value": "PARTIALLY PROCESSED",
				    "tokens": [
				      "PARTIALLY PROCESSED"
				    ]
				  }];

	var picksts =   [{
	    "year": "PARTIALLY SHIPPED",
	    "value": "PARTIALLY SHIPPED",
	    "tokens": [
	      "PARTIALLY SHIPPED"
	    ]
	  },
	  {
		    "year": "SHIPPED",
		    "value": "SHIPPED",
		    "tokens": [
		      "SHIPPED"
		    ]
		  }];

	var delsts =   [{
	    "year": "DELIVERED",
	    "value": "DELIVERED",
	    "tokens": [
	      "DELIVERED"
	    ]
	  }];

	var intransts =   [{
	    "year": "INTRANSIT",
	    "value": "INTRANSIT",
	    "tokens": [
	      "INTRANSIT"
	    ]
	  }];
				  
				  
	var substringMatcher = function(strs) {
		  return function findMatches(q, cb) {
		    var matches, substringRegex;
		    matches = [];
		    substrRegex = new RegExp(q, 'i');
		    $.each(strs, function(i, str) {
		      if (substrRegex.test(str.value)) {
		        matches.push(str);
		      }
		    });
		    cb(matches);
		  };
	};

</script>

<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>