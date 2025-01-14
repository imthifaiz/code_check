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
String title = "POS Expenses Summary";
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
DateUtils _dateUtils = new DateUtils();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//RESVI 
String curDate =DateUtils.getDateMinusDays();
//FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);

String curtDate = _dateUtils.getDate();
String basecurrency = _PlantMstDAO.getBaseCurrency(plant);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POSEXPENSES%>"/>
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
             <%--   <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../expenses/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
               <% } %> --%>
              <!-- <button type="button" class="btn btn-default" onclick="window.location.href='../home'">Back</button> -->
              </div>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="hidden" name="plant" value="<%=plant%>" >
		<input type="hidden" name="vendno" id="evendcode" >
		<INPUT type="hidden" name="OUTCODE">
		<INPUT type="hidden" name="TERMINALCODE">
		<INPUT type="hidden" name="cmd" />
			<INPUT type="hidden" name="TranId" />
			<input type="hidden" name="curency" value="<%=curency%>" >
			<div id="CHK1"></div>
		<div id="target" style="padding: 18px;">
		<div class="form-group">
		<div class="row">
		<div class="col-sm-2.5" hidden>		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
  		<div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" id="FROM_DATE" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div>
  		<div class="col-sm-2">
  		<INPUT class="form-control datepicker" id="TO_DATE" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-2">
  			<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="OUTLET_NAME" placeholder="OUTLETS" name="OUTLET_NAME" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
  		</div>
  		<div class="col-sm-2">
  			<div class="">   		
  		<input type="text" class="ac-selected form-control" id="TERMINALNAME" name="TERMINALNAME" placeholder="TERMINAL">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'TERMINALNAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
  		</div>
  		<div class="col-sm-2">
  			<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
  		</div>
  		<!-- <div class="col-sm-4 ac-box">

				<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="SUPPLIER" name="vendname" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div> -->
  		</div>
  		<div class="row" style="padding:3px" hidden>
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
  	<div class="row" style="padding:3px" hidden>
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
			<div class="col-sm-10 txn-buttons" hidden>
				
				<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
				
			</div>
		</div>
  		</div>
		</div>
		</FORM>
		<div class="form-group" hidden>
      <div class="row" >
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
                  <th style="font-size: smaller;">JOURNAL STATUS</th>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">EXPENSE</th>
                    <th style="font-size: smaller;">OUTLET</th>
                     <th style="font-size: smaller;">TERMINAL</th>
                   <!--  <th style="font-size: smaller;">PAID THROUGH</th>
                     <th style="font-size: smaller;">REFERENCE</th>
                     <th style="font-size: smaller;">SUPPLIER NAME</th>                     
                     <th style="font-size: smaller;">CUSTOMER NAME</th> -->
                     <th style="font-size: smaller;">STATUS</th>
                      <th style="font-size: smaller;">AMOUNT</th>
                      <th style="font-size: smaller;">EXCHANGE RATE</th>
                    <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                    <th style="font-size: smaller;">ADD JOURNAL</th>
                  </tr>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>

		
<div id="addjournalpopup" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form id="journalForm" class="form-horizontal" name="form12"  action="/track/JournalServlet?action=expjcreate" method="post" enctype="multipart/form-data">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Add journal</h3>
					</div>
					<div class="modal-body">	
							<input type="text" name="username" value=<%=username%> hidden>
							<input type="text" name="plant" value="<%=plant%>" hidden>
							<input type="text" name="currency"  value="<%=basecurrency%>" hidden>
							<input id="sub_total" name="sub_total" value="" hidden>
							<input id="summary" name="summary" value="1" hidden>
							<input id="total_amount" name="total_amount" value="" hidden>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Expense
										Amount</label>
									<div class="col-sm-4">
										<input class="form-control" name="expamount" id="expamount" type="text" value="" disabled> 
										<input name="eid" id="eid" type="text" value="" hidden>
									</div>
								</div>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Expense Date</label>
									<div class="col-sm-4">
										<input class="form-control" name="expdate" id="expdate" type="text" value="" disabled>
									</div>
								</div>
								<div class="row form-group">
									<label class="col-form-modal-label col-sm-2">Journal Date</label>
									<div class="col-sm-4">
										<input type="text" class="form-control datepicker" value="" id="journal_date" name="journal_date">
									</div>
								</div>



								<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:95%;">
			<thead>
			  <tr>
				<th class="journal-acc" >Account</th>
				<th class="journal-desc">Description</th>								
				<th class="journal-debit">Debits</th>
				<th class="journal-credit">Credits</th>
			  </tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-7">
				<!-- <a style="text-decoration:none;cursor: pointer;" onclick="addRow()" tabindex="0">+ Add another line</a> -->
				<a href="#" onclick="addRow(event)"><i class="" title="Add another line" style="font-size: 15px;">+ Add another line</i></a>
			</div>
			<div class="total-section col-sm-5">
				<div class="row sub-total">
					<div class="col-sm-4 total-label"> Sub Total <br>  
					</div> 
					<div class="subtotal-debitamount col-sm-3" id="subtot-debitamt"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
					<div class="subtotal-creditamount col-sm-3 col-sm-offset-1" id="subtot-creditamt"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
				</div>
				<br>
				<div class="row gross-total">
					<div class="col-sm-4 total-label"> Total </div> 
					<div class="total-debitamount col-sm-3" id="total-debitamount"><%=String.format("%."+numberOfDecimal+"f", 0.00)%> </div>
					<div class="total-creditamount col-sm-3 col-sm-offset-1" id="total-creditamount"><%=String.format("%."+numberOfDecimal+"f", 0.00)%></div>
				</div>
				
			</div>
		</div>
	
						
						

						
						<div class="row form-group">
							<div class="col-sm-5"></div>
							<div class="col-sm-3">
								<div class="alert-actions btn-toolbar">
									<button type="button" class="btn btn-primary ember-view"
										onclick="addjournal()" style="background: green;">
										Submit</button>
									<button type="button" class="btn btn-primary ember-view"
										data-dismiss="modal" style="background: red;">Cancel</button>
								</div>
							</div>
							<div class="col-sm-5"></div>
						</div>
	
					</div>
				</div>
			</form>
		</div>
	</div>
		
		
		</div>
		<script>
		var plant = document.form1.plant.value;
		var numberOfDecimal = "<%=numberOfDecimal%>";
		var tableExpensesSummary;
		 var ITEMDESC,FROM_DATE,TO_DATE,USER,SUPPLIER,EXPENSES_ACOUNT_NAME,REFERENCE,STATUS,OUTLET,TERMINAL, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,"OUTLET":OUTLET,"TERMINAL":TERMINAL,
				"SUPPLIER":SUPPLIER,"ACCOUNT_NAME":EXPENSES_ACCOUNT_NAME,"REFERENCE":REFERENCE,"STATUS":STATUS,
				"ACTION": "VIEW_POS_EXPENSES_SUMMARY",
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
		   OUTLET= document.form1.OUTCODE.value;
		   TERMINAL= document.form1.TERMINALCODE.value;
		   	    		   
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
					"lengthMenu": [[20, 50, 100, 500], [20, 50, 100, 500]],
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
				        				var lineamt = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal);
				        				var eid = data.items[dataIndex]['id'];
				        				data.items[dataIndex]['exchangerate'] = parseFloat(data.items[dataIndex]['exchangerate']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        				data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['id'] = '<a href="../expenses/posdetail?TRANID=' +data.items[dataIndex]['id']+ '">'+data.items[dataIndex]['id']+'</a>';
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
					        			
					        		if(data.items[dataIndex]['journal'] =='0'){
					        				data.items[dataIndex]['jstatus'] ='<i class="fa fa-circle" style="color:red" aria-hidden="true"></i>';
									}else{
										data.items[dataIndex]['jstatus'] ='<i class="fa fa-check-circle" style="color:green" aria-hidden="true"></i>';
									}
					        		if(data.items[dataIndex]['journal'] =='0'){
					        			data.items[dataIndex]['addjournal'] = '<button type="button pull-right" onclick="journalpopupmodel('+eid+',\''+data.items[dataIndex]['expensesdate']+'\','+lineamt+')" class="btn btn-success ">Add Journal</button>';
					        		}else{
										data.items[dataIndex]['addjournal'] ='';
									}
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
			        	{"data": 'jstatus', "orderable": true},
		    			{"data": 'expensesdate', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'OUTLETNAME', "orderable": true},
		    			{"data": 'TERMINALNAME', "orderable": true},	
		    			/* {"data": 'paidthrough', "orderable": true},
		    			{"data": 'reference', "orderable": true},
		    			{"data": 'vendname', "orderable": true},		    			
		    			{"data": 'custname', "orderable": true}, */
		    			{"data": 'status', "orderable": true},
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			{"data": 'addjournal', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [5,6,7]}],
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
		                     	     doc.content[1].table.body[i][5].alignment = 'right';
		                     	   
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
<%--  $('.Show').click(function() {
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
 } --%>
 
 /* outlet Auto Suggestion */
	$('#OUTLET_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'OUTLET_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_OUTLET_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.POSOUTLETS);
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
//		    return '<div><p class="item-suggestion"> ' + data.OUTLET_NAME + '</p></div>';
			return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
				document.form.OUTCODE.value = "";
			}
		});


/* terminal Auto Suggestion */
	$('#TERMINALNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TERMINAL_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_TERMINAL_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.POSOUTLETS);
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
//		    return '<div><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
		    return '<div onclick="setTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
			removeterminaldropdown();
  		addterminaldropdown();
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.TERMINALCODE.value = "";
			}
		});
		


	  $(".journalaccountSearch").typeahead({
			input:".journalaccountSearch",
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{	  
		  display: 'accountname',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ChartOfAccountServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "getSubAccountTypeGroup",
					module:"journalaccount",
					ITEM : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.results);
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
			suggestion: function(item) {
				if (item.issub) {
					var $state = $(
						    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
						  );
					}
				else
					{
					var $state = $(
							 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
						  );
					}
				  
				  return $state;
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
		
 
 });
 
 function setOutletData(OUTLET,OUTLET_NAME){
		$("input[name=OUTCODE]").val(OUTLET);
	}

	function setTerminalData(TERMINAL,TERMINAL_NAME){
		$("input[name=TERMINALCODE]").val(TERMINAL);
	}
	
	function journalpopupmodel(eid,expdate,expamount){
		$("input[name ='eid']").val(eid);
		$("input[name ='expdate']").val(expdate);
		$("input[name ='expamount']").val(expamount);
		$("input[name ='journal_date']").val(expdate);
		
		
		var urlStr = "../ExpensesServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ID : eid,
				ACTION : "GET_EXPENSE_DET"
				},
				dataType : "json",
				success : function(data) {
					console.log(data.description);
					
					$(".bill-table tbody").html("");
					var body="";
					body += '<tr>';
					body += '<td class="journal-acc">';
					body += '<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account" value="General expense">';
					body += '</td>';
					body += '<td  class="col-sm-6 journal-desc">';
					body += '<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description">'+data.description+'</textarea>';
					body += '</td>';
					body += '<td class="journal-debit">';	
					body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';
					body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
					body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat(expamount).toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
					body += '</td>';	
					
					body += '</tr>';
					$(".bill-table tbody").append(body);

					calculateDebitTotal();
					calculateCreditTotal();
					$('#addjournalpopup').modal('show');
				}
			});
		
		
		
		
		
	}
	
		function addRow(event){
			event.preventDefault(); 
		    event.stopPropagation();
		    var debit=0;
		    var credit=0;
		    $(".bill-table tbody tr").each(function() {
				 var dval = $(this).find("input[name ='debit']").val();
				 var cval = $(this).find("input[name ='credit']").val();
				 if(dval.length > 0){
				 debit += parseFloat($(this).find("input[name ='debit']").val());
				 }
				 if(cval.length > 0){
				 credit += parseFloat($(this).find("input[name ='credit']").val());
				 }
			});
			var body="";
			body += '<tr>';
			body += '<td class="journal-acc">';
			body += '<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account">';
			body += '</td>';
			body += '<td  class="col-sm-6 journal-desc">';
			body += '<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"></textarea>';
			body += '</td>';
			
			if(debit > credit){
				var valdiff = parseFloat(debit) - parseFloat(credit);
				body += '<td class="journal-debit">';	
				body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';
				body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
				body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat(valdiff).toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';	
			}else if(credit > debit){
				var valdiff = parseFloat(credit) - parseFloat(debit);
				body += '<td class="journal-debit">';	
				body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat(valdiff).toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';
				body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
				body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';	
			}else{
				body += '<td class="journal-debit">';	
				body += '<input name="debit" type="text" onchange="journaldebit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';
				body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
				body += '<input type="text" name="credit" onchange="journalcredit(this)" value='+parseFloat("0.0").toFixed(numberOfDecimal)+' onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
				body += '</td>';	
			}
			body += '</tr>';
			$(".bill-table tbody").append(body);
			removeSuggestionToTable();
			addSuggestionToTable();
			calculateDebitTotal();
			calculateCreditTotal();
		}
		
		function removeSuggestionToTable(){
			$(".journalaccountSearch").typeahead('destroy');
		}
		
		function addSuggestionToTable(){
			$(".journalaccountSearch").typeahead({
				input:".journalaccountSearch",
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
			},
			{	  
			  display: 'accountname',  
			  source: function (query, process,asyncProcess) {
					var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "getSubAccountTypeGroup",
						module: "journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		}
		
		function journaldebit(node)
		{
			//alert($(node).val());
			var debit= checkamount($(node).val(),node);
			debit=parseFloat(debit).toFixed(numberOfDecimal);
			$(node).val(debit);
			$(node).closest('tr').find("td:nth-child(4)").find('input').val("");
			calculateDebitTotal();
			calculateCreditTotal();
			}
		function journalcredit(node)
		{
			//var credit=$(node).val();
			var credit= checkamount($(node).val(),node);
			credit=parseFloat(credit).toFixed(numberOfDecimal);
			$(node).val(credit);
			$(node).closest('tr').find("td:nth-child(3)").find('input').val("");
			calculateCreditTotal();
			calculateDebitTotal();
			}	
			function calculateDebitTotal()
			{
				
				var totaldebit=0.00;
				$('input[name=debit]').each(function(){
					var value=$(this).val();
					if(value!="")
						totaldebit+=parseFloat(value);
					
				})
				totaldebit=parseFloat(totaldebit).toFixed(numberOfDecimal);
				$('#subtot-debitamt').html(totaldebit);
				$('#total-debitamount').html(totaldebit);
				
			}
			function calculateCreditTotal()
			{
				var totalcredit=0.00;
				$('input[name=credit]').each(function(){
					var value=$(this).val();
					if(value!="")
						totalcredit+=parseFloat(value);
				})
				totalcredit=parseFloat(totalcredit).toFixed(numberOfDecimal);
				$('#subtot-creditamt').html(totalcredit);
				$('#total-creditamount').html(totalcredit);
			}
			
			function checkamount(baseamount,node){
				var zeroval = parseFloat("0").toFixed(numberOfDecimal);
				if(baseamount != ""){
					var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
					var numbers = /^[0-9]+$/;
					var dotdecimal = /^\.[0-9]+$/;
					if(baseamount.match(decimal) || baseamount.match(numbers)) 
					{ 
						//node.val(parseFloat(baseamount).toFixed(3));	
						return $(node).val();
					}else if(baseamount.match(dotdecimal)){
						return parseFloat("0"+baseamount).toFixed(numberOfDecimal);
					}else{
						alert("Please Enter Valid Amount");
						return zeroval;
					}
				}else{
					return $(node).val();
				}
			}

			function isNumberKey(evt, element, id) {
				  var charCode = (evt.which) ? evt.which : event.keyCode;
				  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
					  {
				    	return false;
					  }
				  return true;
				}
			
			$(".bill-table tbody").on('click','.bill-action',function(){
				debugger;	    
			    $(this).parent().parent().remove();
			    calculateCreditTotal();
				calculateDebitTotal();
			});
			
			function addjournal(){
				var totdebitamt= parseFloat($('#total-debitamount').html());
				var totcreditamt=parseFloat($('#total-creditamount').html());
				var expamt=$("input[name ='expamount']").val();
				var isItemValid = true;
				$("#sub_total").val(totdebitamt);
				$("#total_amount").val(totcreditamt);


				$("input[name ='journal_account_name']").each(function() {
				    if($(this).val() == ""){	    	
				    	alert("The journal account field cannot be empty.");
				    	isItemValid = false;
						return false;
				    }
				});

					if(totdebitamt!=totcreditamt)
					{
						alert("Debit and Credit should be equal");
						isItemValid = false;
						return false;
					}
				else if(totdebitamt<=0)
					{
						alert("Please fill all the details");
						isItemValid = false;
						return false;
					}
					
					if(parseFloat(expamt)!=parseFloat(totdebitamt)){
						alert("Expense and Journal Debit & Credit should be equal");
						isItemValid = false;
						return false;
					}
					
				if(isItemValid)
					$("#journalForm").submit();
			}

 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>