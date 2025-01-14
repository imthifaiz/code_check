<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- ifstart -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- ifend -->
<%
String title = "Contacts Summary";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'contactsummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	ArrayList locQryList = new ArrayList();
	String fieldDesc = "";
	String PLANT = "";
	int Total = 0;
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg = (String)request.getAttribute("Msg");
	fieldDesc = (String)request.getAttribute("result");
	String PGaction = (String)request.getAttribute("PGaction");
	
	
	boolean displaySummaryExport=false,displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryNew = ub.isCheckValAcc("contactnew", PLANT,USERID);
		displaySummaryEdit = ub.isCheckValAcc("contactedit", PLANT,USERID);
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryNew = ub.isCheckValinv("contactnew", PLANT,USERID);	
		displaySummaryEdit = ub.isCheckValinv("contactedit", PLANT,USERID);
	}
	
	/* ifstart */
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
// 	ArrayList al = _PlantMstDAO.getContactHdr(PLANT);
// 	Map map = (Map) al.get(0);
// 	String CNAME = (String) map.get("NAME");
	//String CNAME = "CONTACT";
	/* ifend */
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
String ADD1 = (String) map.get("ADD1");
String ADD2 = (String) map.get("ADD2");
String ADD3 = (String) map.get("ADD3");
String ADD4 = (String) map.get("ADD4");
String STATE = (String) map.get("STATE");
String COUNTRY = (String) map.get("COUNTY");
String ZIP = (String) map.get("ZIP");
String TELNO = (String) map.get("TELNO");

String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Contacts Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
				<div class="box-title pull-right">
              <div class="btn-group" role="group">
             	<button type="button" class="btn btn-default" onClick="window.location.href='../contact/import'">Import</button>
			      &nbsp;
				</div>
				 <%if(displaySummaryNew){ %>
              	<button type="button" class="btn btn-default" onclick="window.location.href='../contact/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              	 <%}%>
              	</div>
		</div>
		
 <div class="box-body">
 
<FORM class="form-horizontal" name="form1" method="post" action="ContactSummary.jsp">
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="PGaction" value="View"> 
	</form>
	<br>
	
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead>
          <tr>  
            <th style="text-align: left !important;">S/N</th>  
            <th style="text-align: left !important;">Company Name</th>
            <th style="text-align: left !important;">Country</th>
            <th style="text-align: left !important;">Industry</th>
            <th style="text-align: left !important;">Contact Person</th>
            <th style="text-align: left !important;">Contact Number</th>  
            <th style="text-align: left !important;">Email</th>
			<th style="text-align: left !important;">Sales Consultant</th>
            <th style="text-align: left !important;">LifeCycle Stage</th>  
            <th style="text-align: left !important;">Lead Status</th>
            <th style="text-align: left !important;">Remark</th>  
            <th style="text-align: left !important;">Note</th>  
            <th style="text-align: left !important;">Edit</th>  
          </tr>  
        </thead> 
          <!-- ifstart --> 
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
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- ifend -->
</table>
</div>  
<!-- ifstart -->
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"PLANT":plant,
				"action":"GET_CONTACT_DETAILS_SUMMARY"
			}
		}
		function onGo(){
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
					        	
						        	if(typeof data.CONTACTLIST === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CONTACTLIST.length; dataIndex ++){
						        				var lineno = data.CONTACTLIST[dataIndex].ID;
						        				var sno=dataIndex+1;
						        				data.CONTACTLIST[dataIndex]['SNO'] = sno;
						        				<%if(displaySummaryEdit){ %>
						        				data.CONTACTLIST[dataIndex]['EDIT'] = '<a href="../contact/edit?PLANT=' +data.CONTACTLIST[0].PLANT+'&NAME=' +data.CONTACTLIST[0].NAME+'&ID=' +lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%}else{ %>
						        				data.CONTACTLIST[dataIndex]['EDIT']  = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
												data.CONTACTLIST[dataIndex]['REMARK'] = '<input type="text" name="REMARK" placeholder="Enter Remark">';
												//data.CONTACTLIST[dataIndex]['NOTE'] = '<input type="text" name="NOTE" placeholder="Enter Note">';
						        			}
						        		return data.CONTACTLIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'NAME', "orderable": true},
				        	{"data": 'COUNTRY', "orderable": true},
				        	{"data": 'INDUSTRY', "orderable": true},
				        	{"data": 'COMPANY', "orderable": true},
				        	{"data": 'MOBILENO', "orderable": true},
				        	{"data": 'EMAIL', "orderable": true},
				        	{"data": 'SALESPROBABILITY', "orderable": true},
				        	{"data": 'LIFECYCLESTAGE', "orderable": true},
				        	{"data": 'LEAD', "orderable": true},
				        	{"data": 'REMARK', "orderable": true},
				        	{"data": 'NOTE', "orderable": true},
			    			{"data": 'EDIT', "orderable": true},
			    			],
			    			"columnDefs": [{ "visible": false, "targets": [10,11] }],
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
				                    	title: function () { var dataview = "<%=title%>"  ; return dataview },
					                    exportOptions: {
					                    	format: {
				                    			body: function ( inner, rowidx, colidx, node ) {
				                    		        if ($(node).children("input").length > 0) {
				                    		          return $(node).children("input").first().val();
				                    		        } else {
				                    		          return inner;
				                    		        }
				                    		      }
				                    			},
				                    			//columns: [':visible:not(:last-child)']
				                    			columns: [ ':not(:last-child)' ]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	text: 'PDF Portrait',
				                        orientation: 'portrait',
			                            	extend : 'pdfHtml5',
			    	                    	exportOptions: {
			    	                    		format: {
			    	                    			body: function ( inner, rowidx, colidx, node ) {
			    	                    		        if ($(node).children("input").length > 0) {
			    	                    		          return $(node).children("input").first().val();
			    	                    		        } else {
			    	                    		          return inner;
			    	                    		        }
			    	                    		      }
			    	                    			},
			    	                    			//columns: [':visible:not(:last-child)']
			    	                    			columns: [ ':not(:last-child)' ]
					                        },
					                        <% if(fromAddress_BlockAddress.trim().equals("")) {%>
			    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
			    	                    	<%} else {%>
			    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
			    	                    	<%}%>    	                        
			                     		customize: function (doc) {
			                     			doc.defaultStyle.fontSize = 7;
			                     	        doc.styles.tableHeader.fontSize = 7;
			                     	        doc.styles.title.fontSize = 10;                     	       
			                     	        doc.content[1].table.widths = "*";
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
			                             pageSize: 'A4'
			                             //,footer: true
				                    },
				                    {
				                    	extend : 'pdf',
				                    	text: 'PDF Landscape',
			                    		orientation: 'landscape',
			                            	extend : 'pdfHtml5',
			    	                    	exportOptions: {
			    	                    		format: {
			    	                    			body: function ( inner, rowidx, colidx, node ) {
			    	                    		        if ($(node).children("input").length > 0) {
			    	                    		          return $(node).children("input").first().val();
			    	                    		        } else {
			    	                    		          return inner;
			    	                    		        }
			    	                    		      }
			    	                    			},
			    	                    			//columns: [':visible:not(:last-child)']
			    	                    			columns: [ ':not(:last-child)' ]
					                        },
					                        <% if(fromAddress_BlockAddress.trim().equals("")) {%>
			    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
			    	                    	<%} else {%>
			    	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
			    	                    	<%}%>    	                        
			                     		customize: function (doc) {
			                     			doc.defaultStyle.fontSize = 6;
			                     	        doc.styles.tableHeader.fontSize = 6;
			                     	        doc.styles.title.fontSize = 8;                     	       
			                     	        doc.content[1].table.widths = "*";
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
			                             pageSize: 'A4'
			                             //,footer: true
				                    }
				                ]
				            },
				            {
			                    extend: 'colvis',
			                    columns: ':not(:eq(10)):not(:eq(11)):not(:last)'
			                }		                
				        ],
				        "order": [],

					});
			    }
			    
			}

		</script>
		<!-- END -->
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
	onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>