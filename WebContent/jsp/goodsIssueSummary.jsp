<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Goods Issue Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String dono = StrUtils.fString(request.getParameter("DONO"));
String gino = StrUtils.fString(request.getParameter("GINO"));
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
String custno = StrUtils.fString(request.getParameter("CUSTNO"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="";

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
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script src="js/Invoice.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>             
              </h1>
		</div> 
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="curency" value="<%=curency%>" hidden>
		<input type="hidden" name="cmd" value="" />
		<INPUT type="hidden" name="TranId" value="" />
		<INPUT type="hidden" name="STATE_PREFIX" value="" />
		<div class="taxDetails" style="display: none"></div>
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER" value="<%=CUSTOMER%>">				
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	
				</div>
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="ORDER NO" value="<%=dono%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="gino" name="gino" placeholder="GINO" value="<%=gino%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'grno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="invoicestatus" id="invoicestatus" class="ac-selected form-control invoicestatus" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoicestatus\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableBillSummary" class="table table-bordred table-striped">                   
                   <thead>
	                   <th style="font-size: smaller;">DATE</th>
	                   <th style="font-size: smaller;">DONO</th>
	                   <th style="font-size: smaller;">GINO</th>
	                   <th style="font-size: smaller;">CUSTOMER NAME</th>                     
	                   <th style="font-size: smaller;">STATUS</th>
	                   <th style="font-size: smaller;">QUANTITY</th>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tableBillSummary;
		 var FROM_DATE,TO_DATE,USER,CUSTOMER,CURENCY,ORDERNO,GINO,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"CUSTOMER":CUSTOMER,"CURENCY":CURENCY,"ORDERNO":ORDERNO,"GINO":GINO,"STATUS":STATUS,
				"ACTION": "VIEW_GINOTOINVOICE_SUMMARY",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form1.FROM_DATE.value;
		    TO_DATE        = document.form1.TO_DATE.value;		    
		    CUSTOMER         = document.form1.CUSTOMER.value;		    
		    CURENCY         = document.form1.curency.value;
		    ORDERNO        = document.form1.ORDERNO.value;
		    GINO        = document.form1.gino.value;
		    STATUS        = document.form1.invoicestatus.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(CUSTOMER != null    && CUSTOMER != "") { flag = true;}
		   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
		   if(GINO != null     && GINO != "") { flag = true;}
		   if(STATUS != null     && STATUS != "") { flag = true;}
		    
		    var urlStr = "../InvoiceServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableBillSummary){
		    	tableBillSummary.ajax.url( urlStr ).load();
		    }else{
			    tableBillSummary = $('#tableBillSummary').DataTable({
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
				        	if(typeof data.items[0].dono === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			data.items[dataIndex]['dono'] = '<a href="/track/jsp/goodsIssueDetail.jsp?action=View&DONO=' +data.items[dataIndex]['dono']+ '&GINO=' +data.items[dataIndex]['gino']+ '&CUST_NAME=' +data.items[dataIndex]['cust_name']+ '&CUSTNO=' +data.items[dataIndex]['custno']+ '">'+data.items[dataIndex]['dono']+'</a>';				        		
				        			data.items[dataIndex]['qty'] =addZeroes(parseFloat(data.items[dataIndex]['qty']).toFixed(3));				        			
				        			if(data.items[dataIndex]['status']=='INVOICED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='NOT INVOICED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'date', "orderable": true},
		    			{"data": 'dono', "orderable": true},
		    			{"data": 'gino', "orderable": true},
		    			{"data": 'cust_name', "orderable": true},		    			
		    			{"data": 'status', "orderable": true},
		    			{"data": 'qty', "orderable": true},
		    			
		    			],
		    			"columnDefs": [{"orderSequence":["desc"], "targets": [0]},{"orderable":!1}],
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
		                     	     doc.content[1].table.body[i][6].alignment = 'right';
		                     	   
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
				});
		    }
		}

		$('#tableBillSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tableBillSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tableBillSummary').attr('width', '100%');
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
		          '<TH><font color="#ffffff" align="left"><b>DONO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>GINO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>CUSTOMER NAME</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>STATUS</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>QUANTITY </TH>'+
		           '</TR>';
		              
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
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>