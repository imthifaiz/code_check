<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Expenses Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="",ACCOUNT_NAME="";

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newexpenses", plant,username);
	displaySummaryLink = ub.isCheckValAcc("summarylnkexpenses", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportexpense", plant,username);
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//RESVI 
String curDate =DateUtils.getDateMinusDays();
//FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.APEXPENSES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/Expenses.js"></script>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Expenses Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
               <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../expenses/apnew'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
               <% } %>
              <!-- <button type="button" class="btn btn-default" onclick="window.location.href='../home'">Back</button> -->
              </div>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="hidden" name="plant" value="<%=plant%>" >
		<input type="hidden" name="vendno" id="evendcode" >
		<INPUT type="hidden" name="cmd" />
			<INPUT type="hidden" name="TranId" />
			<input type="hidden" name="curency" value="<%=curency%>" >
			<div id="CHK1"></div>
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" id="FROM_DATE" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" id="TO_DATE" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
<!-- 				<div class="input-group">  -->
				<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="SUPPLIER" name="vendname" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form1.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 				</div> -->
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" id="expenses_account_name" name="expenses_account_name" class="form-control expensesaccountSearch" placeholder="Select Account">	
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'expensesaccountSearch\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		
  		
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="REFERENCE" name="REFERENCE" placeholder="REFERENCE">				
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
		<div class="row">
		
		<div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableExpensesSummary" class="table table-bordred table-striped">                   
                   <thead>
                  <tr>
                  <th style="font-size: smaller;">PAYMENT</th>
                   <th style="font-size: smaller;">DATE</th>
                   <th style="font-size: smaller;">BILL NO</th>
                    <th style="font-size: smaller;">EXPENSE</th>
                    <th style="font-size: smaller;">OUTLET</th>
                    <th style="font-size: smaller;">TERMINAL</th>
                    <th style="font-size: smaller;">PAID THROUGH</th>
                     <th style="font-size: smaller;">REFERENCE</th>
                     <th style="font-size: smaller;">SUPPLIER NAME</th>                     
                     <th style="font-size: smaller;">CUSTOMER NAME</th>
                     <!-- <th style="font-size: smaller;">STATUS</th> -->
                      <th style="font-size: smaller;">AMOUNT</th>
                      <th style="font-size: smaller;">EXCHANGE RATE</th>
                    <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                  </tr>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		
		</div>
		<script>
		var plant = document.form1.plant.value;
		var numberOfDecimal = "<%=numberOfDecimal%>";
		var tableExpensesSummary;
		 var ITEMDESC,FROM_DATE,TO_DATE,USER,SUPPLIER,EXPENSES_ACOUNT_NAME,REFERENCE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"SUPPLIER":SUPPLIER,"ACCOUNT_NAME":EXPENSES_ACCOUNT_NAME,"REFERENCE":REFERENCE,"STATUS":STATUS,
				"ACTION": "VIEW_AP_EXPENSES_SUMMARY",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		   FROM_DATE      = document.form1.FROM_DATE.value;
		   TO_DATE        = document.form1.TO_DATE.value;		    
		   SUPPLIER         = $('#vendname').val();//document.form1.vendname.value;
		   EXPENSES_ACCOUNT_NAME       = $('#expenses_account_name').val();//document.form1.expenses_account_name.value;
		   REFERENCE       = document.form1.REFERENCE.value;
		   STATUS       = $('#status').val();//document.form1.status.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(SUPPLIER != null    && SUPPLIER != "") { flag = true;}
		   if(EXPENSES_ACCOUNT_NAME != null     && EXPENSES_ACCOUNT_NAME != "") { flag = true;}
		   
		   /* storeInLocalStorage('expensesSummary_FROMDATE', FROM_DATE);
		   storeInLocalStorage('expensesSummary_TODATE', TO_DATE);
		   storeInLocalStorage('expensesSummary_SUPPLIER', SUPPLIER);
		   storeInLocalStorage('expensesSummary_EXPENSES_ACCOUNT_NAME', EXPENSES_ACCOUNT_NAME);
		   storeInLocalStorage('expensesSummary_REFERENCE', REFERENCE);
		   storeInLocalStorage('expensesSummary_STATUS', STATUS); */
		    var urlStr = "../ExpensesServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableExpensesSummary){
		    	tableExpensesSummary.ajax.url( urlStr ).load();
		    }else{
			    tableExpensesSummary = $('#tableExpensesSummary').DataTable({
					"processing": true,
					"lengthMenu": [[50, 100, 500], [50, 100, 500]],
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
				        	if(typeof data.items[0].expensesaccount === 'undefined'){
				        		return [];
				        	}else {
				        		
			        			
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        				data.items[dataIndex]['exchangerate'] = parseFloat(data.items[dataIndex]['exchangerate']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        				data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['id'] = '<a href="../expenses/apdetail?TRANID=' +data.items[dataIndex]['id']+ '">'+data.items[dataIndex]['id']+'</a>';
				        			<% }else { %>
				        			data.items[dataIndex]['id'] = data.items[dataIndex]['id'];
				        			<% }%>
				        			if(data.items[dataIndex]['status']=='NON-BILLABLE')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='UNBILLED')
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else if(data.items[dataIndex]['status']=='BILLED')
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
					        			<%-- data.items[dataIndex]['amount'] =  addZeroes(parseFloat(data.items[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>)); --%>
					        			 data.items[dataIndex]['convamount'] = data.items[dataIndex]['currency'] +parseFloat(data.items[dataIndex]['convamount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
					        			<%-- data.items[dataIndex]['convamount'] = data.items[dataIndex]['currency'] + addZeroes(parseFloat(data.items[dataIndex]['convamount']).toFixed(<%=numberOfDecimal%>)); --%>
					        			
					        		if(data.items[dataIndex]['paystatus'] =='0'){
											//data.ORDERS[dataIndex]['PAY_STATUS'] ='<i class="fa fa-times-circle" style="color:red" aria-hidden="true"></i>'
										data.items[dataIndex]['paystatus'] ='';
									}else if(data.items[dataIndex]['paystatus'] =='1'){
										data.items[dataIndex]['paystatus'] ='<i class="fa fa-circle" style="color:red" aria-hidden="true"></i>';
									}else{
										data.items[dataIndex]['paystatus'] ='<i class="fa fa-check-circle" style="color:green" aria-hidden="true"></i>';
									}	
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
			        	{"data": 'paystatus', "orderable": true},
		    			{"data": 'expensesdate', "orderable": true},
		    			{"data": 'bill', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'OUTLETNAME', "orderable": true},
		    			{"data": 'TERMINALNAME', "orderable": true},
		    			{"data": 'paidthrough', "orderable": true},
		    			{"data": 'reference', "orderable": true},
		    			{"data": 'vendname', "orderable": true},		    			
		    			{"data": 'custname', "orderable": true},
		    			//{"data": 'status', "orderable": true},
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			
		    			],
					"columnDefs": [{"className": "t-right", "targets": [10,11,12]},{"className": "text-center", "targets": [0]}],
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
			    	                	columns: [1,2,3,4,5,6,7,8,9,10,11,12]
			    	                }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	exportOptions: {
			                    		columns: [1,2,3,4,5,6,7,8,9,10,11,12]
			                    	},
		                    		orientation: 'landscape',
		                            pageSize: 'A3',
		                            	extend : 'pdfHtml5',
		    	                    	exportOptions: {
		    	                    		columns: [1,2,3,4,5,6,7,8,9,10,11,12]
		    	                    	},	                    	
		    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
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
		                     	     for (i = 1; i < rowCount; i++) {                     	     
		                     	     doc.content[1].table.body[i][9].alignment = 'right';
		                     	   
		                     	     } 
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
		    }
		}

		$('#tableExpensesSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tableExpensesSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tableExpensesSummary').attr('width', '100%');
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
		          /* '<TH><font color="#ffffff" align="left"><b>EXPENSES ACCOUNT</TH>'+ */
		          '<TH><font color="#ffffff" align="left"><b>PAID THROUHG</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>REFERENCE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>SUPPLIER NAME</TH>'+				  
				  '<TH><font color="#ffffff" align="left"><b>CUSTOMER NAME</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>STATUS</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>AMOUNT</TH>'+
		           '</TR>';
		              
		}
	
		</script>
		</div>
		</div>
		</div></div>
		 <script>
 $(document).ready(function(){
	 <%-- getLocalStorageValue('expensesSummary_FROMDATE', '<%=FROM_DATE%>', 'FROM_DATE');
	 getLocalStorageValue('expensesSummary_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('expensesSummary_REFERENCE', '', 'REFERENCE');
	 var vendname = getLocalStorageValue('expensesSummary_SUPPLIER', '', 'vendname');
	 $('#vendname').typeahead('val', vendname);
	 var expenses_account_name = getLocalStorageValue('expensesSummary_EXPENSES_ACCOUNT_NAME', '', 'expenses_account_name');
	 $('#expenses_account_name').typeahead('val', expenses_account_name);
	 var STATUS = getLocalStorageValue('expensesSummary_STATUS', '', 'status');
	 $('#STATUS').typeahead('val', STATUS); --%>
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