<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<!-- imti -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Company Approval Settings Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>

<%

String smsg = (String)request.getAttribute("sMsg");
String emsg = (String)request.getAttribute("eMsg");

%>
<center>
	<h2><small class="success-msg fout"><%=smsg%></small></h2>
</center>
<center>
	<h2><small class="error-msg fout"><%=emsg%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
<!-- 	 IMTI added on 18-03-2022   -->
	              <ul class="breadcrumb backpageul">      	
                	<li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                          
                	<li><label>Company Approval Settings Summary</label></li>                                   
            	</ul>           
<!--     END -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
              <button type="button" class="btn btn-default" onclick="window.location.href='../PlantApprovalSettings/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New/Edit</button>&nbsp;
              </div>
		</div>
		
 <div class="box-body">
 
 <%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>



 
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead>  
          <tr>  
            <th>Company ID</th>  
            <th>Company Name</th> 
            <th>Approval Type</th> 
            <th>Create</th>
            <th>Edit</th>
            <th>Delete</th>     
          </tr>  
        </thead> 
            <!-- imti -->
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
<script LANGUAGE="JavaScript">
		var tabletype;
		var LOCATIONTYPE, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"PLANT":"all",
			}
		}
		function onGo(){
			   var urlStr = "/track/PlantApprovalSettings/getallpltApproveMatrix";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
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
						        	if(typeof data.data[0].ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.data.length; dataIndex ++){

						        				if(data.data[dataIndex]['ISCREATE'] == "1"){
						        					data.data[dataIndex]['ISCREATE'] = 'YES'
												}else{
													data.data[dataIndex]['ISCREATE'] = 'NO'
												}
						        				if(data.data[dataIndex]['ISUPDATE'] == "1"){
						        					data.data[dataIndex]['ISUPDATE'] = 'YES'
												}else{
													data.data[dataIndex]['ISUPDATE'] = 'NO'
												}
						        				if(data.data[dataIndex]['ISDELETE'] == "1"){
						        					data.data[dataIndex]['ISDELETE'] = 'YES'
												}else{
													data.data[dataIndex]['ISDELETE'] = 'NO'
												}
						        			}
						        		return data.data;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'PLANT', "orderable": true},
				        	{"data": 'CNAME', "orderable": true},
				        	{"data": 'APPROVALTYPE', "orderable": true},
				        	{"data": 'ISCREATE', "orderable": true},
				        	{"data": 'ISUPDATE', "orderable": true},				        	
			    			{"data": 'ISDELETE', "orderable": true},
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
					                    	columns: [0,1,2,3]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                    		columns: [0,1,2,3]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
			    	                    		columns: [0,1,2,3]
					                        },
			    	                        title: function () { var dataview = "<%=title%> "  ; return dataview },    	                        
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
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
				        "order": [],

					});
			    }
			    
			}

		</script>
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){

	setTimeout(function() {
        $('.fout').fadeOut('fast');
    }, 5000);
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>