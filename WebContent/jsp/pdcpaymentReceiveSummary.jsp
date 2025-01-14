<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "PDC Received Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
	String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
	String BANK = StrUtils.fString(request.getParameter("BANK"));
	String CHEQUENO = StrUtils.fString(request.getParameter("CHEQUENO"));
	
	boolean displaySummaryExport=false,displaySummaryNew=false,displaySummaryEdit=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryNew = ub.isCheckValAcc("newPDCPaymentReceived", plant,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editPDCPaymentReceived", plant,USERID);
	displaySummaryExport = ub.isCheckValAcc("exportPDCPaymentReceived", plant,USERID);
	}
// 	String curDate = _dateUtils.getDate();
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String collectionDate=du.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//RESVI
	String curDate =du.getDateMinusDays();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PDC_PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/PDCPaymentReceive.js"></script>
<style>
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}

/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
</style>
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
                <li><label>PDC Received Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<div class="btn-group" role="group">
			<%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../banking/pdcpayreceiveprocess'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" > Process Payment</button>&nbsp;
              <%} %>              
              </div>
			<h1 style="font-size: 18px; cursor: pointer;position: relative;bottom: -7px;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
			</div>
		</div>
		
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="number" id="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>>
				<input type="text" name="LOGIN_USER" value="<%=username%>" hidden>
				<INPUT type = "hidden" name="CUST_CODE" value=<%=custCode%>>
				<input type="hidden" value="<%=displaySummaryEdit%>" name="displaySummaryEdit" id="displaySummaryEdit" />
				<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE" value="<%=FROM_DATE%>">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE" value="<%=TO_DATE%>">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="Select a customer"  name="CUSTOMER" value="<%=CUSTOMER%>" required>
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value+'&TYPE=ACCTCUST');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4">
  		<input id="bank_account_name" name="bank_account_name" placeholder="BANK" value="<%=BANK%>"
						 class="form-control text-left" type="text" required> 
						 <span class="pay-select-icon" 
						 onclick="$(this).parent().find('input[name=\'bank_account_name\']').focus()">						 	
						 </span>
  		</div>
  		<div class="col-sm-4">
  		<div class="input-group">   		
  		<input type="text" class="ac-selected form-control" id="CHEQUENO" name="CHEQUENO" placeholder="CHEQUE NO" value="<%=CHEQUENO%>">				
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="status" id="status" class="ac-selected form-control status" placeholder="STATUS" >
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
		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
				
		      	<div id="VIEW_RESULT_HERE" class="table-responsive">
		      	<table id="tablepdcpayment" class="table table-bordred table-striped">					
						<thead>
							<tr>
								<th style="font-size: smaller;">PAYMENT ID</th>
								<th style="font-size: smaller;">PAYMENT DATE</th>
								<th style="font-size: smaller;">CUSTOMER</th>
								<th style="font-size: smaller;">ACCOUNT NAME</th>
								<th style="font-size: smaller;">BANK</th>
								<th style="font-size: smaller;">CHEQUE NO.</th>
								<th style="font-size: smaller;">CHEQUE DATE</th>
								<th style="font-size: smaller;">CHEQUE REVERSED DATE</th>
								<th style="font-size: smaller;">AMOUNT</th>
								<th style="font-size: smaller;">STATUS</th>
								<th style="font-size: smaller;">EDIT</th>
							</tr>
						</thead>
						<tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
					</table>
				</div>
				
			</form>
		</div>		
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tablepdcpayment;
		 var FROM_DATE,TO_DATE,USER,CUSTOMER,BANK,CHEQUENO,TYPE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,"STATUS":STATUS,
				"CUSTOMER":CUSTOMER,"CHEQUENO":CHEQUENO,"BANK":BANK,"CMD":"GET_PDC_PAYMENT",TYPE:"SUMM"
			}
		}
		function onGo(){
			debugger;
			var flag    = "false";
			FROM_DATE      = document.form1.FROM_DATE.value;
			TO_DATE        = document.form1.TO_DATE.value;		    
			CUSTOMER         = document.form1.CUST_CODE.value;
			if(document.form1.CUSTOMER.value=="")
				SUPPLIER="";
			BANK         = document.form1.bank_account_name.value;
			CHEQUENO         = document.form1.CHEQUENO.value;
			STATUS         = document.form1.status.value;
			
			if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
			   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
			   if(CUSTOMER != null    && CUSTOMER != "") { flag = true;}		   
			   
			   var urlStr = "/track/InvoicePayment";
			   var groupColumn = 1;
			   var displaySummaryEdit = document.form1.displaySummaryEdit.value;
			    if (tablepdcpayment){
			    	tablepdcpayment.ajax.url( urlStr ).load();
			    }else{
				    tablepdcpayment = $('#tablepdcpayment').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	if(data.PDC.length>0)
					        		{
					        	if(typeof data.PDC[0].ID === 'undefined'){
					        		return [];
					        	}else {				        		
					        			for(var dataIndex = 0; dataIndex < data.PDC.length; dataIndex ++){
					        				var lineno = data.PDC[dataIndex].ID;
					        			if(data.PDC[dataIndex]['STATUS']=="NOT PROCESSED"){
					        				if(displaySummaryEdit == 'true'){
					        				data.PDC[dataIndex]['EDIT'] = '<a href="../banking/editpdcpayreceive?TRANID=' +lineno+'"><i class="fa fa-pencil-square-o"></i></a>';					        			
					        				}else{
					        				data.PDC[dataIndex]['EDIT'] = ' <a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a>';
					        					}
					        				}
					        				else{
					        				data.PDC[dataIndex]['EDIT'] = '';
					        			}
					        			data.PDC[dataIndex]['CHEQUE_AMOUNT'] = parseFloat(data.PDC[dataIndex]['CHEQUE_AMOUNT']).toFixed(<%=numberOfDecimal%>);
					        		}
					        		return data.PDC;
					        	}
					        }
					        	else
					        		return [];
					        }
					    },
				        "columns": [
				        	{"data": 'RECEIVEID', "orderable": true},
			    			{"data": 'RECEIVE_DATE', "orderable": true},
			    			{"data": 'CUSTOMER', "orderable": true},
			    			{"data": 'ACCOUNT', "orderable": true},
			    			{"data": 'BANK_BRANCH', "orderable": true},
			    			{"data": 'CHECQUE_NO', "orderable": true},
			    			{"data": 'CHEQUE_DATE', "orderable": true},
			    			{"data": 'CHEQUE_REVERSAL_DATE', "orderable": true},
			    			{"data": 'CHEQUE_AMOUNT', "orderable": true},
			    			{"data": 'STATUS', "orderable": true},
			    			{"data": 'EDIT', "orderable": true},
			    			
			    			],
						"columnDefs": [{
			            	"className": "t-right",
			            	"targets": 8,
					        "data": "CHEQUE_AMOUNT",
					        "render": function ( data, type, row, meta ) {
					        	var amount1=parseFloat(data);
					          return getNumberInMillionFormat(amount1);
					        }
			            },{"className": "text-center", "targets": [10]}],
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
			                     	     doc.content[1].table.body[i][8].alignment = 'right';
			                     	   
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
	  
	 });




</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>