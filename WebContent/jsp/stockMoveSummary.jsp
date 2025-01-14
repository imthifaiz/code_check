<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Stock Move Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String fieldDesc = "", FROM_DATE="", TO_DATE="", ORDERNO="",FROM_LOC= "",TO_LOC="";
String msg = (String)request.getAttribute("Msg");



String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
PlantMstDAO plantMstDAO = new PlantMstDAO();
String currency=plantMstDAO.getBaseCurrency(plant);
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);

String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
TO_LOC = StrUtils.fString(request.getParameter("TO_LOC"));
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
<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/stockMoveSummary.js"></script>
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
                <li><label>Stock Move Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
                  
	              
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../inhouse/new'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
	              
	              </div>
		</div>
		
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="padding: 18px; display:none;">
				
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
						<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" 
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" 
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="TRANID" placeholder="ORDERNO" name="TRANID">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'TRANID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>
                                                        
                         <!--    resvi starts-->
                         
          <div class="row" style="padding:3px">
	      <div class="col-sm-2"></div>
	      <div class="col-sm-4 ac-box" >
          
               <input type="text" class="form-control" id="FROMLOC" name="FROM_LOC" placeholder="FROM LOCATION" value="" >
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'FROMLOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
           </div>
                    
            <div class="col-sm-4 ac-box" >
                 <input type="text" class="form-control" id="TOLOC" name="TOLOC" placeholder="TO LOCATION" value="" >
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'TOLOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
             </div>
             </div>
             
                                 <!-- ends -->

				
	  
	
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
									<th style="font-size: smaller;">ORDER NO</th>	
									<th style="font-size: smaller;">FROM LOCATION</th>	
									<th style="font-size: smaller;">TO LOCATION</th>
									<th style="font-size: smaller;">STATUS</th>
									<th style="font-size: smaller;">PRINT</th>
																		
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
 var FROM_DATE,TO_DATE,TRANID,FROMLOC,TOLOC,groupRowColSpan = 4;

 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,"TRANID":TRANID,"FROMLOC":FROMLOC,"TOLOC":TOLOC
 	}
 }  

 
 			function onGo() {
 	            FROM_DATE = document.form1.FROM_DATE.value;
 	            TO_DATE = document.form1.TO_DATE.value;
 	            TRANID = document.form1.TRANID.value;
 	            FROMLOC = document.form1.FROM_LOC.value;
 	            TOLOC = document.form1.TOLOC.value;

 	           

 	           if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
 	          if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
 	          if(TRANID != null     && TRANID != "") { flag = true;}
 	          if(FROMLOC != null     && FROMLOC != "") { flag = true;}
 	          if(TOLOC != null     && TOLOC != "") { flag = true;}

 	            var urlStr = "../inhouse/stockmove";

 	           var groupColumn = 1;
 	      	   var totalQty = 0;

 	            if (tableSalesOrderSummary) {
 	                tableSalesOrderSummary.ajax.url(urlStr).load();
 	            } else {
 	                tableSalesOrderSummary = $('#tableSalesOrderSummary').DataTable({
 	                    "processing": true,
 	                    "lengthMenu": [[50, 100, 500], [50, 100, 500]],
 	                   searching: true, // Enable searching
 	      	        search: {
 	      	            regex: true,   // Enable regular expression searching
 	      	            smart: false   // Disable smart searching for custom matching logic
 	      	        },
 	                    "ajax": {
 	                        "type": "GET",
 	                        "url": urlStr,
 	                        "data": function (d) {
 	                            return $.isEmptyObject(getParameters()) ? d : getParameters();
 	                        },
 	                        "contentType": "application/x-www-form-urlencoded; charset=utf-8",
 	                        "dataType": "json",
 	                        "dataSrc": function (data) {
 	                            if (!data.ORDERS || data.ORDERS.length === 0) {
 	                                return [];
 	                            } else {
 	                                for (var dataIndex = 0; dataIndex < data.ORDERS.length; dataIndex++) {
 	                                	if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED')
 	                                   		data.ORDERS[dataIndex]['Print'] = '<a href="../DynamicProductServlet?cmd=printstockmoveproduct&TRANID='+data.ORDERS[dataIndex]['TRANID']+'" target="_blank"><i class="fa fa-file-pdf-o"></i></a>';
 	                                   	else
 	                                   		data.ORDERS[dataIndex]['Print'] = '<a href="#"><i class="fa fa-file-pdf-o" style="color: grey;"></i></a>';

 	                                   	if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED')
 	       		        					data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
 	       		        				else
 	       		        					data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
 	                                   		
 	                                	data.ORDERS[dataIndex]['TRANID'] = '<a href="../inhouse/detail?TRANID='+data.ORDERS[dataIndex]['TRANID']+'">'+data.ORDERS[dataIndex]['TRANID']+'</a>'; 
//  	                                	data.ORDERS[dataIndex]['TRANID'] = '<a href="../inhouse/detail?TRANID='+data.ORDERS[dataIndex]['TRANID']+'&CHKBATCH=true&LOC=&cmd=ViewTran">'+data.ORDERS[dataIndex]['TRANID']+'</a>'; 
 	                                }
 	                                return data.ORDERS;
 	                            }
 	                        }
 	                    },
 	                    "columns": [
 	                        { "data": 'PURCHASEDATE', "orderable": true },
 	                        { "data": 'TRANID', "orderable": true },
 	                        { "data": 'FROM_LOC', "orderable": true },
 	                        { "data": 'TOLOC', "orderable": true },
 	                        { "data": 'STATUS', "orderable": true },
 	                       { "data": 'Print', "orderable": true }
 	                    ],
//  	                   	"columnDefs": [{"class": "text-center", "targets": [1,2,3,4,5]}],
 	                   	"columnDefs": [{"class": "text-center", "targets": [1,5]}],
 	                    "orderFixed": [],
 	                    "dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
 	                           "<'row'<'col-md-12't>>" +
 	                           "<'row'<'col-md-12'ip>>",
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

/* TRANID Auto Suggestion */
$('#TRANID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'TRAN',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=plant%>",
			ACTION : "GET_TRANID_STOCK_MOVE_LIST_FOR_SUGGESTION",
			QUERY : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.TRANS);
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
		return '<div><p class="item-suggestion">'+data.TRAN+'</p></div>';
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

/* From location Auto Suggestion */
$('#FROMLOC').typeahead({
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
			PLANT : "<%=plant%>",
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
		return '<div><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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
$('#TOLOC').typeahead({
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
			PLANT : "<%=plant%>",
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
		return '<div><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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