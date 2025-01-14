<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Peppol Invoice Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean displaySummaryExport=false,displaySummaryProcessClaim=false;
	/* if(systatus.equalsIgnoreCase("PAYROLL")){
		displaySummaryExport = ub.isCheckValPay("pexportClaim", plant,username);
		displaySummaryProcessClaim = ub.isCheckValPay("pnewClaim", plant,username);
	} */
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.CLAIM%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
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

.payment-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -12%;
	top: 15px;
}

.voucher-action {
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
                <li><label>Purchase Invoice Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">

					<button type="button" class="btn btn-default"
						onClick="window.location.href='../peppolapi/purchaseinvoicefrompeppol'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						Purchase Invoice Retrieval</button> 
				</div>
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../integrations/peppolintegration'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="number" id="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>>
				<input type="text" name="LOGIN_USER" value="<%=username%>" hidden>
				<div id="VIEW_RESULT_HERE" class="table-responsive">
					<table id="tableemployeetype"
						class="table table-bordred table-striped">
						<thead>
							<tr>
								<th style="font-size: smaller;">INVOICE DATE</th>
								<th style="font-size: smaller;">iNVOICE NUMBER</th>
								<th style="font-size: smaller;">CUSTOMER NAME</th>
								<th style="font-size: smaller;">STATUS</th>
								<th style="font-size: smaller;">ACCEPT/REFUSE</th>
								<th style="font-size: smaller;">RESPONSE</th>
								
							</tr>
						</thead>
						<tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								
							</tr>
						</tfoot>
					</table>
				</div>

			</form>
		</div>
		
		<div id="inveventspopup" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form id="journalForm" class="form-horizontal" name="form12"  action="/track/JournalServlet?action=expjcreate" method="post" enctype="multipart/form-data">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Invoice Events</h3>
					</div>
					<div class="modal-body">	




								<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:95%;">
			<thead>
			  <tr>
				<th class="journal-acc" >Date</th>
				<th class="journal-desc">Status</th>								
			  </tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
		
	
						
						

						
						
	
					</div>
				</div>
			</form>
		</div>
	</div>
		
		<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableemployeetype;
		var FROM_DATE,TO_DATE,USER,SUPPLIER,BANK,CHEQUENO,TYPE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"CMD":"GET_PEPPOL_INVOICE_SUMMARY"
			}
		}
		function onGo(){
			   var urlStr = "/track/peppolapi/getpurchaseinvoicesummary";
			   var groupColumn = 1;	
			    if (tableemployeetype){
			    	tableemployeetype.ajax.url( urlStr ).load();
			    }else{
				    tableemployeetype = $('#tableemployeetype').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50], [10, 25, 50]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	console.log(data.INVOICE);
					        	console.log(data.INVOICE[0]);
					        	console.log(data.INVOICE[0].PONO);
					        	if(data.INVOICE.length>0){
						        	if(typeof data.INVOICE[0].PONO === 'undefined'){
						        		return [];
						        	}else {	
						        		for(var dataIndex = 0; dataIndex < data.INVOICE.length; dataIndex ++){
						        			
						        			data.INVOICE[dataIndex]['PONO'] = '<a href="../peppolapi/pidetail?pono='+data.INVOICE[dataIndex]['PONO']+'">'+data.INVOICE[dataIndex]['PONO']+'</a>';
						        			
						        			if(data.INVOICE[dataIndex]['STATUS'] == "PENDING"){
						        				data.INVOICE[dataIndex]['PIDS'] = '<a href="#" onclick="acceptinvoice('+data.INVOICE[dataIndex]['PID']+'); return false;">ACCEPT</a>&nbsp;&nbsp;&nbsp;<a href="#" onclick="rejectinvoice('+data.INVOICE[dataIndex]['PID']+'); return false;">REJECT</a>';
						        			}else{
						        				data.INVOICE[dataIndex]['PIDS'] = '-';
						        			}
						        			
						        			if(data.INVOICE[dataIndex]['STATUS'] != "PENDING"){
						        				if(data.INVOICE[dataIndex]['REPOSNSEMESSAGE'] == ""){
						        					data.INVOICE[dataIndex]['REPOSNSEMESSAGE'] = '<a href="#" onclick="getinvoiceresponse('+data.INVOICE[dataIndex]['PID']+',\''+data.INVOICE[dataIndex]['STATUS']+'\'); return false;">SEND RESPONSE</a>';
						        				}
						        			}else{
						        				data.INVOICE[dataIndex]['REPOSNSEMESSAGE'] = '-';
						        			}
						        			
						        			//data.INVOICE[dataIndex]['sxml'] = '<a href="#" onclick="showxml('+data.SALESORDER[dataIndex]['SALESORDERUUID']+'); return false;">XML FILE</a>';	
						        			//data.INVOICE[dataIndex]['DONO'] = '<a href="../peppolapi/salesdetail?dono='+data.SALESORDER[dataIndex]['DONO']+'">'+data.SALESORDER[dataIndex]['DONO']+'</a>';
						        			/* if(data.INVOICE[dataIndex]['peppolstatus'] == "0"){
						        				data.INVOICE[dataIndex]['SENDINVOICE'] = '<a href="#" onclick="sendinvoice('+data.INVOICE[dataIndex]['peppoldocid']+'); return false;">SEND</a>';	
						        				data.INVOICE[dataIndex]['IEVENTS'] = '-';
						        			}else{
						        				data.INVOICE[dataIndex]['SENDINVOICE'] ='-';
						        				data.INVOICE[dataIndex]['IEVENTS'] = '<a href="#" onclick="invoiceevents('+data.INVOICE[dataIndex]['peppoldocid']+'); return false;">VIEW</a>';
						        			}
						        			
						        			
						        			data.INVOICE[dataIndex]['INVOICE'] = '<a href="../peppolapi/invoicedetail?dono=' +data.INVOICE[dataIndex]['INVOICE']+ '&INVOICE_HDR=' +data.INVOICE[dataIndex]['ID']+'">'+data.INVOICE[dataIndex]['INVOICE']+'</a>'; */
						        		}
						        		return data.INVOICE;
						        	}
					        	}else{
					        		return [];
					        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'INVDATE', "orderable": true},
			    			{"data": 'PONO', "orderable": true},
			    			{"data": 'SUPPNAME', "orderable": true},
			    			{"data": 'STATUS', "orderable": true},
			    			{"data": 'PIDS', "orderable": true},
			    			{"data": 'REPOSNSEMESSAGE', "orderable": true},
			    			],
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
				                    	/* exportOptions: {
				    	                	columns: [':visible']
				    	                } */
					                    exportOptions: {
					                        columns: [0,1,2,3,4,5]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
			    	                    		columns: [0,1,2,3,4,5]
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
			                     	     doc.content[1].table.body[i][1].alignment = 'center';
			  
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
			                    /* columns: ':not(:eq('+groupColumn+')):not(:last)' */
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
		


		</script>
	</div>
<script>
	$(document).ready(function(){
		onGo();		
	 });
	
	/* function deleteemptype(id){
		$.ajax({
    		type : "GET",
    		url: '/track/EmployeeTypeServlet',
    		async : true,
    		dataType: 'json',
    		data : {
    			CMD : "EMPLOYEE_TYPE_DELETE",
    			ID : id
    		},
    		success : function(data) {
    			if(data.STATUS == "NOT OK"){
    				alert("Employee type Exists In Employee or Leave type");
    			}else{
    				 //window.location.reload();
    				window.location.href="../payroll/employeetype?result=Employee type deleted successfully."
    			}
    		}
    	});	
	} */
	
	
	function acceptinvoice(id){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/acceptinvoice?SID="+id,
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
	        	console.log(data);
    			alert(data.response);
					location.reload();
			},
	        error: function (data) {	
	        	console.log(data);	
	        	//alert(data.Message);
	        }
		});	
	}
	
	function rejectinvoice(id){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/rejectinvoice?SID="+id,
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
	        	console.log(data);
    			alert(data.response);
					location.reload();
			},
	        error: function (data) {	
	        	console.log(data);	
	        	//alert(data.Message);
	        }
		});	
	}
	
	function getinvoiceresponse(id,status){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/getinvoiceresponse?SID="+id+"&ISTATUS="+status,
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
	        	console.log(data);
    			alert(data.response);
					location.reload();
			},
	        error: function (data) {	
	        	console.log(data);	
	        	//alert(data.Message);
	        }
		});	
	}
	
	function invoiceevents(id){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/getinvoiceevents?SID="+id,
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
					console.log(data);
					console.log(data.EVENTS);
					var data1 = JSON.parse(data.EVENTS);
					console.log(data1);
					console.log(data1.length);
					$(".bill-table tbody").html("");
					
					if(data1.length>0){
						for(var dataIndex = 0; dataIndex < data1.length; dataIndex ++){
			        			console.log("------------------------");
			        			console.log(data1[dataIndex]['invoice_uuid']);
			        			var body="";
			        			body += '<tr>';
								body += '<td class="journal-acc">';
								body += data1[dataIndex]['time'];
								body += '</td>';
								body += '<td class="journal-acc">';
								body += data1[dataIndex]['message'];
								body += '</td>';
								body += '</tr>';
								$(".bill-table tbody").append(body);
			        	}
		        	}

					$('#inveventspopup').modal('show');
			},
	        error: function (data) {	
	        	console.log(data);	
	        	//alert(data.Message);
	        }
		});	
	}
</script>

</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>