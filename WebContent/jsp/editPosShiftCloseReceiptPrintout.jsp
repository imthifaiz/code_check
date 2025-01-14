<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit POS Shift Close Receipt Printout";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;
		

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	

	function onClear()
	{
		
		document.form1.PRINTWITHPRODUCT.checked = false;
		document.form1.PRINTWITHCATEGORY.checked = false;
		document.form1.PRINTWITHBRAND.checked = false;
		document.form1.PRINTWITHCLOSINGSTOCK.checked = false;
		
	}
	function onAdd(){
		var checkFound = false;
		var len = document.form1.chkdDoNo.length;
		 var orderLNo; 
		if (document.form1.PRINTWITHCATEGORY.checked){
		if (len == undefined)
			len = 1;
		for ( var i = 0; i < len; i++) {
			if (len == 1 && (!document.form1.chkdDoNo.checked)) {
				checkFound = false;
			}
			else if (len == 1 && document.form1.chkdDoNo.checked) {
				checkFound = true;
			}
			else {
				if (document.form1.chkdDoNo[i].checked) {
					checkFound = true;
				}
			}
		}
		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}
		}
		 document.form1.action  = "/track/GstTypeServlet?action=EDIT_POS_SHIFT_CLOSE_RECEIPT";
		 document.form1.submit();
   	
	}
	

	
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	//String res = "";
	
	String action = "";
        String PrintWRcpt="",PRINTWITHPRODUCT="",PRINTWITHCATEGORY="",PRINTWITHBRAND="",PRINTWITHCLOSINGSTOCK="";
	StrUtils strUtils = new StrUtils();
	String  res =  strUtils.fString(request.getParameter("result"));
	POSUtil posUtil = new POSUtil();
         Map mhdr= posUtil.getPosShiftCloseReceiptHdrDetails(plant);

         PRINTWITHPRODUCT=(String) mhdr.get("PRINTWITHPRODUCT");
         PRINTWITHCATEGORY=(String) mhdr.get("PRINTWITHCATEGORY");
         PRINTWITHBRAND=(String) mhdr.get("PRINTWITHBRAND");
         PRINTWITHCLOSINGSTOCK=(String) mhdr.get("PRINTWITHCLOSINGSTOCK");

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
	}  

	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit POS Shift Close Receipt Printout</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                       <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
  <CENTER><strong><font style="font-size:18px;"><%=res%></font></strong></CENTER> 
 <FORM class="form-horizontal" name="form1" id="editPosShiftCloseReceiptForm" method="post">
 
  <input type="hidden" name="plant" value="<%=plant%>">
      	<div class="form-group">
      	<div class="col-sm-3">
      	<lable class="checkbox-inline"><input type = "checkbox" id = "PRINTWITHPRODUCT" name = "PRINTWITHPRODUCT" value = "PRINTWITHPRODUCT" 
		<%if(PRINTWITHPRODUCT.equals("1")) {%>checked <%}%> size="30" MAXLENGTH=20>Print With Product Summary</lable>
      	</div>
      	</div>
      	
      		<div class="form-group">
      	<div class="col-sm-3">
      	<lable class="checkbox-inline"><input type = "checkbox" id = "PRINTWITHCATEGORY" name = "PRINTWITHCATEGORY" value = "PRINTWITHCATEGORY"  onclick="return openPOSCategory(this.checked);"
		<%if(PRINTWITHCATEGORY.equals("1")) {%>checked <%}%> size="30" MAXLENGTH=20>Print With Category Summary</lable>
      	</div>
      	</div>
      	
      		<div class="form-group">
      	<div class="col-sm-3">
      	<lable class="checkbox-inline"><input type = "checkbox" id = "PRINTWITHBRAND" name = "PRINTWITHBRAND" value = "PRINTWITHBRAND" 
		<%if(PRINTWITHBRAND.equals("1")) {%>checked <%}%> size="30" MAXLENGTH=20>Print With Brand Summary</lable>
      	</div>
      	</div>
      	
      		<div class="form-group">
      	<div class="col-sm-3">
      	<lable class="checkbox-inline"><input type = "checkbox" id = "PRINTWITHCLOSINGSTOCK" name = "PRINTWITHCLOSINGSTOCK" value = "PRINTWITHCLOSINGSTOCK" 
		<%if(PRINTWITHCLOSINGSTOCK.equals("1")) {%>checked <%}%> size="30" MAXLENGTH=20>Print With Closing Stock</lable>
      	</div>
      	</div>
			
		<br>
		
		<div class="row" id="poscateselect">
  		<div class="col-12 col-sm-12">
  			<label>
  			<input type="checkbox" class="form-check-input"  style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		</div>                        
  	</div>
		
		<div id="poscate">
		<div style="overflow-x:auto;" >
<table id="table" class="table table-bordred table-striped" style="width: 100%">    
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Product Category ID</th>
            <th style="font-size: smaller;">Product Category Description</th>
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
		</div>  

		<br>

		<div class="form-group">        
      	<div class="col-sm-offset-6 col-sm-12">
      	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	</div>
    	</div>
		
		</FORM>
		</div></div></div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var PRINTWITHCATEGORY,groupRowColSpan = 2;
		$(document).ready(function(){
		    onGo();
		    $('[data-toggle="tooltip"]').tooltip();
		    if(PRINTWITHCATEGORY=='1')	{
			document.getElementById("poscateselect").style.display = "block"; 	
	 		document.getElementById("poscate").style.display = "block";
			} else {
				document.getElementById("poscateselect").style.display = "none"; 	
		 		document.getElementById("poscate").style.display = "none";
			}
		});
		function getParameters(){
			return {
				"PRINTWITHCATEGORY":PRINTWITHCATEGORY,
				"PLANT":plant,
				"action":"GET_POSCATEGORY_FOR_SUMMARY"
			}
		}
		function onGo(){
			PRINTWITHCATEGORY='<%=PRINTWITHCATEGORY%>';
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
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
					        	
						        	if(typeof data.SUPPLIERTYPELIST[0].PRD_CLS_ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.SUPPLIERTYPELIST.length; dataIndex ++){
						        				if (data.SUPPLIERTYPELIST[dataIndex]['CHK'] == 'yes')
						        				data.SUPPLIERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; checked name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['PRD_CLS_ID']+'" >';
						        				else
						        				data.SUPPLIERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['PRD_CLS_ID']+'" >';
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHKOB', "orderable": false},
				        	{"data": 'PRD_CLS_ID', "orderable": true},
				        	{"data": 'PRD_CLS_DESC', "orderable": true},
			    			],
						"orderFixed": [ ], 
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
					                    	columns: [1,2]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	exportOptions: {
				                    		columns: [1,2]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	exportOptions: {
			    	                    		columns: [1,2]
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
			                     	      
			                     	        doc['footer']=(function(page, pages) {
			                     	            return {
			                     	                columns: [
			                     	                    '',
			                     	                    {
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
			                }		                
				        ],
				        "order": [],

					});
			    }
			    
			}
		function checkAll(isChk)
	 	{
	 		var len = document.form1.chkdDoNo.length;
	 		 var orderLNo; 
	 		 if(len == undefined) len = 1;  
	 	    if (document.form1.chkdDoNo)
	 	    {
	 	        for (var i = 0; i < len ; i++)
	 	        {      
	 	              	if(len == 1){
	 	              		document.form1.chkdDoNo.checked = isChk;
	 	               	}
	 	              	else{
	 	              		document.form1.chkdDoNo[i].checked = isChk;
	 	              	}
	 	            	
	 	        }
	 	    }
	 	}
	 	function openPOSCategory(isChk)
	 	{
		 	if(isChk){
	 		document.getElementById("poscateselect").style.display = "block"; 	
	 		document.getElementById("poscate").style.display = "block";
		 	} else {
	 		document.getElementById("poscateselect").style.display = "none"; 	
	 		document.getElementById("poscate").style.display = "none"; 	
			}
		}
		</script>
		
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
