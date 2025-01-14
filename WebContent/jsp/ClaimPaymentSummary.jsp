<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Claim Payment Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String fieldDesc = StrUtils.fString(request.getParameter("result"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="";

String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
boolean displaySummaryExport=false,displaySummaryLink=false,displaySummaryEdit=false,displaySummaryPdf=false,displaySummaryEmail=false;
if(systatus.equalsIgnoreCase("PAYROLL")){
	displaySummaryExport = ub.isCheckValPay("pexportClaimPayment", plant,username);
	displaySummaryEdit = ub.isCheckValPay("peditPayrollPayment", plant,username);
	displaySummaryPdf = ub.isCheckValPay("pprintPayrollPayment", plant,username);
	displaySummaryEmail = ub.isCheckValPay("pprintPayrollPayment", plant,username);
	displaySummaryLink = ub.isCheckValPay("pnewClaim", plant,username);
}

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.CLAIM_PAYMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<!-- <script src="js/Bill.js"></script> -->
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
                <li><label>Claim Payment Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <!-- <button type="button" class="btn btn-default" onClick="window.location.href='ProcessClaim.jsp'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp; -->              
              </div>
              </h1>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="curency" value="<%=curency%>" hidden>
		<input type="text" name="DECIMALNO" value="<%=numberOfDecimal%>" hidden>
		<input type="text" name="pono" hidden>
		<INPUT type="hidden" name="STATE_PREFIX" value="" />
		<INPUT type="text" name="vendno" value="" hidden>
		<div id="CHK1"></div>
		<div class="form-group">
      		<div class="ShowSingle">

       		</div>
       	 </div>
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableBillpaymentSummary" class="table table-bordred table-striped">                   
                   <thead>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">PAYMENT</th>
                     <th style="font-size: smaller;">REFERENCE</th>
                     <th style="font-size: smaller;">ACCOUNT NAME</th>
                     <th style="font-size: smaller;">PAYMENT MODE</th>
                     <th style="font-size: smaller;">PAID THROUGH</th>                   
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableBillpaymentSummary;
		 var FROM_DATE,TO_DATE,USER,SUPPLIER,CURENCY,DECIMAL,REFERENCE, groupRowColSpan = 6;
		function getParameters(){
			return {
				"CURENCY":CURENCY,
				"Submit": "VIEW_CLAIM_PAYMENT_SUMMARY",
				"PLANT":"<%=plant%>",
				"DECIMAL":DECIMAL
			}
		}  
		  function onGo(){
		   var flag    = "false";	    
		    CURENCY         = document.form.curency.value;
		    DECIMAL         = document.form.DECIMALNO.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(SUPPLIER != null    && SUPPLIER != "") { flag = true;}		   
		   		    
		    
		    var urlStr = "../HrPayrollPaymentServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableBillpaymentSummary){
		    	tableBillpaymentSummary.ajax.url( urlStr ).load();
		    }else{
			    tableBillpaymentSummary = $('#tableBillpaymentSummary').DataTable({
					"processing": true,
					"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.items[0].id === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			data.items[dataIndex]['id'] = '<a href="/track/jsp/ClaimPaymentDetail.jsp?TRANID=' +data.items[dataIndex]['id']+ '">'+data.items[dataIndex]['id']+'</a>';
				        				
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'payrollpaymentdate', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'reference', "orderable": true},
		    			{"data": 'accountname', "orderable": true},
		    			{"data": 'paymentmode', "orderable": true},
		    			{"data": 'paidthrough', "orderable": true},	
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			],
					//"columnDefs": [{"className": "t-right", "targets": [8,9,10,11]}],
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
		                     	     doc.content[1].table.body[i][7].alignment = 'right';
		                     	   
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
		          '<TH><font color="#ffffff" align="left"><b>PAYMENT</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>REFERENCE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>SUPPLIER NAME</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>PAYMENT MODE</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>PAID THROUHG</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>PAYMENT TYPE</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>AMOUNT</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>UNUSED AMOUNT</TH>'+
		           '</TR>';
		              
		}
	
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
	 onGo();
 });
 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>