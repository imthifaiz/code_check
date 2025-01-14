<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Tax Adjustments";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
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
		<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
    <jsp:param name="submenu" value="<%=IConstants.TAX_ADJUSTMENTS%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              </div> 
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="curency" value="<%=curency%>" hidden>
	<div class="form-group">
 		<label class="control-label col-sm-2" for="Filter:">Filter:</label>
       <div class="col-sm-3">
      	    <SELECT class="form-control" NAME ="TYPE" onchange="onGo();" >
     		<OPTION   value='All' >All </OPTION>
     		<OPTION   value='This Year' >This Year </OPTION>
     		<OPTION   value='Previous Year' >Previous Year </OPTION>
                           </SELECT>
  		</div>
  		</div>
	<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableBillSummary" class="table table-bordred table-striped">                   
                   <thead>
	                   <th style="font-size: smaller;">DATE</th>
	                   <th style="font-size: smaller;">TAX AUTHORITY</th>
	                   <th style="font-size: smaller;">REF #</th>
	                   <th style="font-size: smaller;">TAX AMOUNT</th>
	                   <th style="font-size: smaller;">AMOUNT</th>
	                   <th style="font-size: smaller;">REASON</th>                     
	                   <th style="font-size: smaller;">REMOVE</th>	                   
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</div>
		</div>
	
		</FORM>
		</div>	
	 </div>
</div>
<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tableBillSummary;
		 var TYPE, CURENCY, groupRowColSpan = 6;
		function getParameters(){
			return {
				"TYPE":TYPE,"CURENCY":CURENCY,
				"action": "VIEW_TAXFILEADJUSTMENT_SUMMARY",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		   TYPE      = document.form.TYPE.value;
		   CURENCY   = document.form.curency.value;
		   	    		   
		   if(TYPE != null     && TYPE != "") { flag = true;}
		    
		    var urlStr = "../TaxReturnServlet";
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
				        	if(typeof data.items[0].date === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        				data.items[dataIndex]['amount'] =data.items[dataIndex]['currency'] + addZeroes(parseFloat(data.items[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>));
				        				data.items[dataIndex]['taxamount'] =data.items[dataIndex]['currency'] + addZeroes(parseFloat(data.items[dataIndex]['taxamount']).toFixed(<%=numberOfDecimal%>));
				        				data.items[dataIndex]['reason'] = '<a title="' +data.items[dataIndex]['reason'] + '"><i class="fa fa-file-text-o"></i></a>';
				        				data.items[dataIndex]['remove'] = '<a onclick="return getConfirmation();" href="/track/TaxReturnServlet?action=Remove&TRANID=' +data.items[dataIndex]['id'] + '"><i class="fa fa-trash-o"></i></a>';
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'date', "orderable": true},
		    			{"data": 'taxauthority', "orderable": true},
		    			{"data": 'ref', "orderable": true},
		    			{"data": 'taxamount', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			{"data": 'reason', "orderable": true},		    			
		    			{"data": 'remove', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [3,4]}],
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
		                     	     doc.content[1].table.body[i][4].alignment = 'right';
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
		          '<TH><font color="#ffffff" align="left"><b>TAX AUTHORITY</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>REF</TH>'+		          
				  '<TH><font color="#ffffff" align="left"><b>TAX AMOUNT</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>AMOUNT</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>REASON</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>DELETE</TH>'+
		           '</TR>';
		              
		}
	
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
 onGo();
 });
 function getConfirmation(){
	   return confirm("Are you sure about removeing this tax adjustment?");
	}
 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>