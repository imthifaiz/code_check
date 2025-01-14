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
String title = "Company Status Summary";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'crm', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
	/* ifstart */
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetailForCrm(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	/* ifend */
	
	
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<FORM class="form-horizontal" name="form1" method="post" action="internalCrmSummary.jsp">
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="PGaction" value="View"> 
	</form>
	<br>
	
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead>
          <tr>  
            <th style="text-align: left !important;">S/N</th>  
            <th style="text-align: left !important;">User</th>
            <th style="text-align: left !important;">Company Code</th>
			<th style="text-align: left !important;">Company Name</th>
            <th style="text-align: left !important;">Contact Person</th>
            <th style="text-align: left !important;">Contact Detail</th>  
            <th style="text-align: left !important;">System Type</th>     
            <th style="text-align: left !important;">Partner</th>     
            <th style="text-align: left !important;">Region</th>     
            <th style="text-align: left !important;">Status</th>     
            <th style="text-align: left !important;">Start Date</th>     
            <th style="text-align: left !important;">Expiry Date</th>     
            <th style="text-align: left !important;">Actual Expired Date</th>     
            <th style="text-align: left !important;">Remarks</th>
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
				"action":"GET_USER_DETAILS_SUMMARY"
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
					        	
						        	if(typeof data.USERLIST[0].COMPANYCODE === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.USERLIST.length; dataIndex ++){
						        				var lineno = data.USERLIST[dataIndex].COMPANYCODE;
						        				var sno=dataIndex+1;
						        				data.USERLIST[dataIndex]['SNO'] = sno;
						        				
						        				if(data.USERLIST[dataIndex]['STATUS']=='Purchased'){
							        				data.USERLIST[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.USERLIST[dataIndex]['STATUS']+'</span>';
						        				}else if(data.USERLIST[dataIndex]['STATUS']=='Trial'){
								        			data.USERLIST[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.USERLIST[dataIndex]['STATUS']+'</span>';
						        				}else{
								        			data.USERLIST[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.USERLIST[dataIndex]['STATUS']+'</span>';
								        			}	
						        				data.USERLIST[dataIndex]['EDIT'] = '<a href="../track/internalcrmedit?PLANT=' +lineno+'&COMPANYNAME=' +data.USERLIST[dataIndex].COMPANYNAME+'"><i class="fa fa-pencil-square-o"></i></a>';
						        			}
						        		return data.USERLIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'USER', "orderable": true},
				        	{"data": 'COMPANYCODE', "orderable": true},
				        	{"data": 'COMPANYNAME', "orderable": true},
				        	{"data": 'PERSON', "orderable": true},
				        	{"data": 'CONTACTDETAIL', "orderable": true},
				        	{"data": 'SYSTEMTYPE', "orderable": true},
				        	{"data": 'PARTNER', "orderable": true},
				        	{"data": 'REGION', "orderable": true},
				        	{"data": 'STATUS', "orderable": true},
				        	{"data": 'SDATE', "orderable": true},
				        	{"data": 'EDATE', "orderable": true},
				        	{"data": 'AEDATE', "orderable": true},
				        	{"data": 'REMARKS', "orderable": true},
			    			{"data": 'EDIT', "orderable": true},
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
					                    	columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
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