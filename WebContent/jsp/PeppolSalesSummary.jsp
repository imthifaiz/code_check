<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Peppol Sales Order Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("Msg"));
	
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
                <li><label>Peppol Sales Order Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">

					<button type="button" class="btn btn-default"
						onClick="window.location.href='../peppolapi/getsalesorder'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						Sales Order Retrieval</button>
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
								<th style="font-size: smaller;">SALES DATE</th>
								<th style="font-size: smaller;">SALES ORDER NUMBER</th>
								<th style="font-size: smaller;">SUPPLIER NAME</th>
								<th style="font-size: smaller;">ORDER STATUS</th>
								<th style="font-size: smaller;">SHOW XMLS</th>
								<th style="font-size: smaller;">INVOICING</th>
								
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
		<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableemployeetype;
		var FROM_DATE,TO_DATE,USER,SUPPLIER,BANK,CHEQUENO,TYPE,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"CMD":"GET_PEPPOL_SALES_ORDER_SUMMARY"
			}
		}
		function onGo(){
			   var urlStr = "/track/peppolapi/getsalesordersummary";
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
					        	console.log(data.SALESORDER);
					        	console.log(data.SALESORDER[0]);
					        	console.log(data.SALESORDER[0].DONO);
					        	if(data.SALESORDER.length>0){
						        	if(typeof data.SALESORDER[0].DONO === 'undefined'){
						        		return [];
						        	}else {	
						        		for(var dataIndex = 0; dataIndex < data.SALESORDER.length; dataIndex ++){
						        			data.SALESORDER[dataIndex]['sxml'] = '<a href="#" onclick="showxml('+data.SALESORDER[dataIndex]['SALESORDERUUID']+'); return false;">XML FILE</a>';	
						        			data.SALESORDER[dataIndex]['DONO'] = '<a href="../peppolapi/salesdetail?dono='+data.SALESORDER[dataIndex]['DONO']+'">'+data.SALESORDER[dataIndex]['DONO']+'</a>';
						        			if(data.SALESORDER[dataIndex]['ORDERSTATUS'] == "OPEN" || data.SALESORDER[dataIndex]['ORDERSTATUS'] == "Open" || data.SALESORDER[dataIndex]['ORDERSTATUS'] == "open"){
						        				data.SALESORDER[dataIndex]['CINVOICE'] = '<a href="#" onclick="salesflip('+data.SALESORDER[dataIndex]['SALESORDERUUID']+'); return false;">CONVERT TO INVOICE</a>';	
						        			}else{
						        				data.SALESORDER[dataIndex]['CINVOICE'] ='-';
						        			}
						        		}
						        		return data.SALESORDER;
						        	}
					        	}else{
					        		return [];
					        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SALESDATE', "orderable": true},
			    			{"data": 'DONO', "orderable": true},
			    			{"data": 'CustName', "orderable": true},
			    			{"data": 'ORDERSTATUS', "orderable": true},
			    			{"data": 'sxml', "orderable": true},
			    			{"data": 'CINVOICE', "orderable": true},
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
	
	function showxml(id){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/showxml?SID="+id,
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
					console.log(data);	
					window.open(data.URL, '_blank').focus();
			},
	        error: function (data) {	
	        	console.log(data);	
	        	//alert(data.Message);
	        }
		});	
	}
	
	function salesflip(id){
		$.ajax( {
			type : "GET",
			url : "/track/peppolapi/salesfliptoinvoice?SID="+id,
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
</script>

</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>