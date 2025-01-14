<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Claim Processing";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	
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
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_SUMMARY%>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
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
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='HrClaimSummary.jsp'">
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
								<th style="font-size: smaller;">DATE</th>
								<th style="font-size: smaller;">EMPLOYEE ID</th>
								<th style="font-size: smaller;">EMPLOYEE NAME</th>
			                    <th style="font-size: smaller;">CLAIM</th>
			                    <th style="font-size: smaller;">DESCRIPTION</th>
			                    <th style="font-size: smaller;">FROM PLACE</th>
			                    <th style="font-size: smaller;">TO PLACE</th>
			                    <th style="font-size: smaller;">DISTANCE</th>
			                    <th style="font-size: smaller;">AMOUNT</th>
			                    <th style="font-size: smaller;">REASON</th>
			                    <th style="font-size: smaller;">ACTION</th>
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
		<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableemployeetype;
		var FROM_DATE,TO_DATE,USER,SUPPLIER,BANK,CHEQUENO,TYPE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"CMD":"GET_CLAIM_PROCESS"
			}
		}
		function onGo(){
			   var urlStr = "/track/HrClaimServlet";
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
					        	if(data.CLAIMSUMMARY.length>0){
						        	if(typeof data.CLAIMSUMMARY[0].PLANT === 'undefined'){
						        		return [];
						        	}else {
						        		
						        		for(var dataIndex = 0; dataIndex < data.CLAIMSUMMARY.length; dataIndex ++){
					        				var lineno = data.CLAIMSUMMARY[dataIndex].ID;
					 
					        				data.CLAIMSUMMARY[dataIndex]['PAYMENT'] = '<button type="button" class="btn btn-success" onclick="getpayment('+lineno+')">PROCESS</button>';
					        				
					        			}
						        			
						        		return data.CLAIMSUMMARY;
						        	}
					        	}else{
					        		return [];
					        	}
					        }
					    },
				        "columns": [
				        	{"data": 'CLAIMDATE', "orderable": true},
			    			{"data": 'EMPCODE', "orderable": true},
			    			{"data": 'EMPNAME', "orderable": true},
			    			{"data": 'CLAIMNAME', "orderable": true},
			    			{"data": 'DESCRIPTION', "orderable": true},
			    			{"data": 'FROM_PLACE', "orderable": true},
			    			{"data": 'TO_PLACE', "orderable": true},
			    			{"data": 'DISTANCE', "orderable": true},
			    			{"data": 'AMOUNT', "orderable": true},
			    			{"data": 'REASON', "orderable": true},
			    			{"data": 'PAYMENT', "orderable": true},
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
					                        columns: [0,1,2,3,4,5,6,7,8,9]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                            columns: [0,1,2,3,4,5,6,7,8,9]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
					                            columns: [0,1,2,3,4,5,6,7,8,9]
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
    				window.location.href="EmployeeTypeSummary.jsp?result=Employee type deleted successfully."
    			}
    		}
    	});	
	}
</script>

</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>