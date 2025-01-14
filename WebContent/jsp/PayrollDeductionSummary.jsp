<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Payroll Deduction Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean displaySummaryNew=false,displaySummaryExport=false,displaySummaryEdit=false,displaySummaryDelete=false;
	if(systatus.equalsIgnoreCase("PAYROLL")){
		displaySummaryExport = ub.isCheckValPay("pexporteduction", plant,username);
		displaySummaryNew = ub.isCheckValPay("pnewdeduction", plant,username);
		displaySummaryEdit = ub.isCheckValPay("peditdeduction", plant,username);
		}
	
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
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_DEDUCTION%>" />
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
                <li><label>Payroll Deduction Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryNew) { %>
					<button type="button" class="btn btn-default"
						onClick="window.location.href='../payroll/creatededuction'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
					<% } %>
				</div>
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
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
								<th style="font-size: smaller;">EMPLOYEE ID</th>
								<th style="font-size: smaller;">EMPLOYEE</th>
								<th style="font-size: smaller;">MONTH</th>
								<th style="font-size: smaller;">DEDUCTION NAME</th>
								<th style="font-size: smaller;">LOAN/ADVANCE AMOUNT</th>
								<th style="font-size: smaller;">DUE PER MONTH</th>
								<th style="font-size: smaller;">DECDUCTION DESCRIPTION</th>
								<th style="font-size: smaller;">EDIT</th>
							</tr>
						</thead>
						<tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>

			</form>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableemployeetype;
		var FROM_DATE,TO_DATE,USER,SUPPLIER,BANK,CHEQUENO,TYPE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"CMD":"GET_PAYROLL_DEDUCTION_SUMMARY"
			}
		}
		function onGo(){
			   var urlStr = "/track/PayrollServlet";
			   var groupColumn = 1;	
			    if (tableemployeetype){
			    	tableemployeetype.ajax.url( urlStr ).load();
			    }else{
				    tableemployeetype = $('#tableemployeetype').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	if(data.PAYROLLDEDUCTION.length>0){
						        	if(typeof data.PAYROLLDEDUCTION[0].ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.PAYROLLDEDUCTION.length; dataIndex ++){
						        				var lineno = data.PAYROLLDEDUCTION[dataIndex].ID;
						        				var ISG = data.PAYROLLDEDUCTION[dataIndex].ISGRATUITY;
						        				var ISP = data.PAYROLLDEDUCTION[dataIndex].ISPROCESSED ;
						        				
						        				if(ISP == "0"){
						        					<% if (displaySummaryEdit) { %>	
						        					data.PAYROLLDEDUCTION[dataIndex]['EDIT'] = '<a href="../payroll/editdeduction?ID=' +lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
						        					<% } else { %>
							        				data.PAYROLLDEDUCTION[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a>'
							        				<% } %>
						        				}else{
						        					data.PAYROLLDEDUCTION[dataIndex]['EDIT'] = '';
						        				}
						        				if(ISG == "0"){
						        					data.PAYROLLDEDUCTION[dataIndex]['DESCRIPTION'] = '<a href="#" class="fa fa-info-circle" style="margin-left: 65px;" onclick="loaddeductdetail(\''+lineno+'\')"></a>';
						        				}else{
						        					data.PAYROLLDEDUCTION[dataIndex]['DESCRIPTION'] = '';
						        				}
						        				
						        				/* data.PAYROLLDEDUCTION[dataIndex]['DELETE'] = '<button type="button" class="btn btn-danger" onclick="deleteemptype('+lineno+')">Delete</button>'; */
						        			}
						        		return data.PAYROLLDEDUCTION;
						        	}
					        	}else{
					        		return [];
					        	}
					        }
					    },
				        "columns": [
				        	{"data": 'EMPCODE', "orderable": true},
			    			{"data": 'EMPNAME', "orderable": true},
			    			{"data": 'MONTH', "orderable": true},
			    			{"data": 'DNAME', "orderable": true},
			    			{"data": 'DAMOUNT', "orderable": true},
			    			{"data": 'DUEAMOUNT', "orderable": true},
			    			{"data": 'DESCRIPTION', "orderable": true},
			    			{"data": 'EDIT', "orderable": true},
			    			],
			    		"columnDefs": [{"className": "dt-center", "targets": [6]}],
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
	
	function deleteemptype(id){
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
	}
	
	function loaddeductdetail(id){
		var numberOfDecimal = document.form.numberOfDecimal.value;
		var urlStr = "/track/PayrollServlet";
		
		$.ajax( {
			type : "POST",
			url : urlStr,
			data: {
				Submit:"DEDUCTION_DESCRIPTION",
				HDRID:id
			},
	        success: function (data) {
	        	var objc = JSON.parse(data);
	        	console.log(objc);
	        	$("#empsalarytypes").html("");
	        	if(objc.STATUS == "OK"){
	        		var body = "";
	        		
	        		$('#dname').html(objc.DHDR.DEDUCTION_NAME);
	        		$('#damount').html(parseFloat(objc.DHDR.DEDUCTION_AMOUNT).toFixed(numberOfDecimal));
	        		$('#ddueamount').html(parseFloat(objc.DHDR.DEDUCTION_DUE).toFixed(numberOfDecimal));
	        		
	        		var balamount = 0;

		        	$.each(objc.DDET,function(i,v){
		        		body +='<tr>';
		        		body +='<td class="text-center">';
		        		body +='<p>'+v.DUE_AMOUNT+'</p>';
		        		body +='</td>';
		        		body +='<td class="text-center">';
		        		body +='<p>'+GetMonthName(v.DUE_MONTH)+'</p>';
		        		body +='</td>';
		        		body +='<td class="text-center">';
		        		body +='<p>'+v.DUE_YEAR+'</p>';
		        		body +='</td>';
		        		body +='<td class="text-center">';
		        		body +='<p>'+v.STATUS+'</p>';
		        		body +='</td>';
		        		body +='</tr>';	
		        		if(v.STATUS == "Pending"){
		        			balamount = parseFloat(balamount) + parseFloat(v.DUE_AMOUNT);
		        		}

		        	});
		        	
		        
		        	$('#dbalamount').html(parseFloat(balamount).toFixed(numberOfDecimal));	        	
		        	$(".deduct-popup tbody").html("");
		        	$(".deduct-popup tbody").append(body);
		        	$("#paydeductdetail").modal();

	        	}else{
	        		alert("Error in payroll deduction description");
	        	}

	        }
			
		});
	}
	
	function GetMonthName(monthNumber) {
	    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
	    return months[monthNumber - 1];
	}
</script>
	<div id="paydeductdetail" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title" style="font-weight: bold;">Payment Deduction Details</h4>
		      	</div>
		      	
		      	<div class="modal-body">	
					<div class="row">
			            <div class="col-md-12 popupmodel">
			            	<table>
								<tbody>
									<tr>
										<td style="padding: 10px">Deduction  Name</td>
										<td style="padding: 10px">:</td>
										<td id="dname" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Total Amount</td>
										<td style="padding: 10px">:</td>
										<td id="damount" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Deduction Due Amount</td>
										<td style="padding: 10px">:</td>
										<td id="ddueamount" style="padding: 10px"></td>
									</tr>
									<tr>
										<td style="padding: 10px">Balance Amount</td>
										<td style="padding: 10px">:</td>
										<td id="dbalamount" style="padding: 10px"></td>
									</tr>
								</tbody>
							</table>
			            </div>
			        </div>
			        <strong><p>Due Details</p></strong>
			        <div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table deduct-popup">
							<thead>
								<tr>
									<th>Due Amount</th>
									<th>Month</th>
									<th>Year</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
	</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>