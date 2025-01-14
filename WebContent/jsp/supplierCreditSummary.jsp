<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Debit Note Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String fieldDesc = StrUtils.fString(request.getParameter("result"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="";
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newSupplierCreditNote", plant,username);
	displaySummaryLink = ub.isCheckValAcc("summarylnkSupplierCreditNote", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportSupplierCreditNote", plant,username);
}
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
//RESVI 
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.PURCHASE_CREDIT_NOTES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/SupplierCredit.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                            
                <li><label>Debit Note Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../debit/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
              <% } %>
              </div>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form" method="post" action="" >
		<input type="hidden" name="plant" value="<%=plant%>" >
		<input type="hidden" name="curency" value="<%=curency%>" >
		<input type="hidden" name="DECIMALNO" value="<%=numberOfDecimal%>" >
		<input type="hidden" name="creditnote" >
		<input type="hidden" name="vendno" >
		<select name="nTAXTREATMENT" id="nTAXTREATMENT"  hidden> </select>
		
		<div id="CHK1" ></div>
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" id="FROM_DATE" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
<!-- 				<div class="input-group">  -->
				<input type="text" class="ac-selected form-control " id="VNAME" placeholder="SUPPLIER" name="vendname" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 				</div> -->
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="CREDITNOTE" name="CREDITNOTE" placeholder="DEBIT NOTE">  		
  		<!-- <input type="text" class="ac-selected form-control" id="bill" name="bill" placeholder="BILL NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'bill\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="REFERENCE" name="REFERENCE"  placeholder="REFERENCE">				
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
				
				<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
				
			</div>
		</div>
  		</div>
		</div>
				</FORM>
		
		<div class="form-group">
      <div class="row">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
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
              <table id="tableBillpaymentSummary" class="table table-bordred table-striped">                   
                   <thead>
                   <tr><th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">ID</th>
                    <th style="font-size: smaller;">DEBIT NOTE</th>
                     <th style="font-size: smaller;">REFERENCE</th>
                     <th style="font-size: smaller;">SUPPLIER NAME</th>
                     <th style="font-size: smaller;">PAYMENT MODE</th>
                     <th style="font-size: smaller;">STATUS</th>                     
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                      <th style="font-size: smaller;">BALANCE</th>
                    </tr>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</div>
		</div></div>
		
		<script>
		var plant = document.form.plant.value;
		var tableBillpaymentSummary;
		 var FROM_DATE,TO_DATE,USER,SUPPLIER,CURENCY,DECIMAL,BILL,CREDITNOTE,STATUS,REFERENCE, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"SUPPLIER":SUPPLIER,"CURENCY":CURENCY,"BILL":BILL,
				"CREDITNOTE":CREDITNOTE,"STATUS":STATUS,"REFERENCE":REFERENCE,
				"ACTION": "VIEW_SUPPLIERCREDIT_SUMMARY_VIEW",
				"PLANT":"<%=plant%>",
				"DECIMAL":DECIMAL
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form.FROM_DATE.value;
		    TO_DATE        = document.form.TO_DATE.value;		    
		    SUPPLIER         = document.form.vendname.value;		    
		    CURENCY         = document.form.curency.value;
		    DECIMAL         = document.form.DECIMALNO.value;
		    //BILL        = document.form.bill.value;
		    CREDITNOTE        = document.form.CREDITNOTE.value;
		    STATUS      = document.form.status.value;
		    REFERENCE      = document.form.REFERENCE.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(SUPPLIER != null    && SUPPLIER != "") { flag = true;}		   
		   /* storeInLocalStorage('supplierCreditSummary_FROMDATE', FROM_DATE);
			storeInLocalStorage('supplierCreditSummary_TODATE', TO_DATE);
			storeInLocalStorage('supplierCreditSummary_SUPPLIER', SUPPLIER);
			storeInLocalStorage('supplierCreditSummary_CREDITNOTE', CREDITNOTE);
			storeInLocalStorage('supplierCreditSummary_REFERNCE', REFERENCE);
			storeInLocalStorage('supplierCreditSummary_STATUS', STATUS); */
		    
		    var urlStr = "../SupplierCreditServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		   	var numberOfDecimal ="<%=numberOfDecimal%>";
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableBillpaymentSummary){
		    	tableBillpaymentSummary.ajax.url( urlStr ).load();
		    }else{
			    tableBillpaymentSummary = $('#tableBillpaymentSummary').DataTable({
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
				        	if(typeof data.items[0].creditnote === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        				data.items[dataIndex]['exchangerate'] = parseFloat(data.items[dataIndex]['exchangerate']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
					        			data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
					        			data.items[dataIndex]['balancedue'] = parseFloat(data.items[dataIndex]['balancedue']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
					        			
				        				
				        				<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['creditnote'] = '<a href="../debit/detail?TRANID=' +data.items[dataIndex]['id']+ '">'+data.items[dataIndex]['creditnote']+'</a>';
				        			<% }else { %>
				        			data.items[dataIndex]['creditnote'] = data.items[dataIndex]['creditnote'];
				        			<% } %>
				        			if(data.items[dataIndex]['status']=='APPLIED'||data.items[dataIndex]['status']=='Applied')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='OPEN'||data.items[dataIndex]['status']=='Open')
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else if(data.items[dataIndex]['status']=='DRAFT'||data.items[dataIndex]['status']=='Draft')
						        		data.items[dataIndex]['status'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'creditdate', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'creditnote', "orderable": true},
		    			{"data": 'reference', "orderable": true},
		    			{"data": 'vendname', "orderable": true},
		    			{"data": 'paymentmode', "orderable": true},
		    			{"data": 'status', "orderable": true},
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			{"data": 'balancedue', "orderable": true},
		    			
		    			],
					"columnDefs": [{"className": "t-right", "targets": [7,8,9,10]}],
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
			    $("#tableBillpaymentSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
		        	tableBillpaymentSummary.draw();
		        });
		    }
		}

		$('#tableBillpaymentSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tableBillpaymentSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tableBillpaymentSummary').attr('width', '100%');
		});

		  function callback(data){
				
				var outPutdata = getTable();
				var ii = 0;
				var errorBoo = false;
				$.each(data.errors, function(i,error){
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						
					}
				});
				
				if(!errorBoo){
					
			        
				}else{
			}
		      outPutdata = outPutdata +'</TABLE>';
		      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		       document.getElementById('spinnerImg').innerHTML ='';

		   
		 }

		function getTable(){
		   return '<TABLE>'+
		          '<TR>'+
		          '<TH><font color="#ffffff" align="left"><b>DATE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>ID</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>DEBIT NOTE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>REFERENCE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>SUPPLIER NAME</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>PAYMENT MODE</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>STATUS</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>AMOUNT</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>BALANCE</TH>'+
		           '</TR>';
		              
		}
	
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
		/* getLocalStorageValue('supplierCreditSummary_FROMDATE',  $('#FROM_DATE').val(), 'FROM_DATE');
		 getLocalStorageValue('supplierCreditSummary_TODATE', '', 'TO_DATE');
		 getLocalStorageValue('supplierCreditSummary_SUPPLIER', '', 'VNAME');
		 getLocalStorageValue('supplierCreditSummary_CREDITNOTE', '', 'CREDITNOTE');
		 getLocalStorageValue('supplierCreditSummary_REFERNCE', '', 'REFERENCE');
		 getLocalStorageValue('supplierCreditSummary_STATUS', '', 'status'); */
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