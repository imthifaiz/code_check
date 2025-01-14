<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Outlets Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script src="../jsp/js/bootstrap-datepicker.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GroupSummary',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}


</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "", OUTLET_CODE = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
// 	String PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	boolean displaySummaryEdit=false,displaySummaryNew=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryEdit = ub.isCheckValAcc("outletedit", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("outletnew", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryEdit = ub.isCheckValinv("outletedit", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("outletnew", PLANT,USERID);
	}
	OutletUtil outletUtils = new OutletUtil();
	outletUtils.setmLogger(mLogger);
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String collectionDate=DateUtils.getDate();
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
	//END
	
	if (PGaction.equalsIgnoreCase("View")) {
		try {

			Hashtable ht = new Hashtable();
			if (StrUtils.fString(PLANT).length() > 0)
			ht.put("PLANT", PLANT);
			locQryList = outletUtils.getOutletListStartsWithName(OUTLET_CODE, PLANT);
			if (locQryList.size() == 0) {
// 				fieldDesc = "Data Not Found";
				//fieldDesc = fieldDesc;
			}

		} catch (Exception e) {
		}
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Outlets Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
				<div class="btn-group" role="group">
				<%if(displaySummaryNew){ %>
					<button type="button" class="btn btn-default"
						onclick="window.location.href='../outlets/new'" 
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
					<%} %>
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="OutletSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 
<div class="form-group">

<label class="control-label col-sm-2" for="Outlet Name or ID">Outlet Name</label>
<div class="col-sm-4 ac-box" >

<input type="hidden" name="OUTLET_CODE" value="">
<input type="hidden" name="OUTLET_CODE1" value="">
<INPUT name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=OUTLET_CODE%>" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'OUTLET_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
  	
  	</div>&nbsp;&nbsp;
  	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  	
  	</div>
  	
	</form>
	<br>
 

<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Outlet</th>
            <th style="font-size: smaller;">Outlet Name</th>
			<th style="font-size: smaller;">Contact Person</th>
            <th style="font-size: smaller;">Designation</th>
            <th style="font-size: smaller;">IsActive</th>
            <th style="font-size: smaller;">Edit</th>    
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
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
						<!-- END -->
        
       
</table>
</div>  
<script>
		var plant = document.form.plant.value;
		var tabletype;
		var OUTLETNAME, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"OUTLETNAME":OUTLETNAME,
				"PLANT":plant,
				"action":"GET_OUTLET_FOR_SUMMARY"
			}
		}
		function onGo(){
			OUTLETNAME=document.form.OUTLET_NAME.value;
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
					        	
						        	if(typeof data.OUTLETLIST[0].OUTLET === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.OUTLETLIST.length; dataIndex ++){
						        				var lineno = data.OUTLETLIST[dataIndex].OUTLET;
						        				var sno=dataIndex+1;
						        				data.OUTLETLIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.OUTLETLIST[dataIndex]['EDIT'] = '<a href="../outlets/edit?action=View&OUTLET_CODE='+lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.OUTLETLIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
						        				
						        			}
						        		return data.OUTLETLIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'OUTLET', "orderable": true},
				        	{"data": 'OUTLET_NAME', "orderable": true},
				        	{"data": 'CONTACT_PERSON', "orderable": true},
				        	{"data": 'DESGINATION', "orderable": true},
				        	{"data": 'ISACTIVE', "orderable": true},				        	
			    			{"data": 'EDIT', "orderable": true},
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
				                    	exportOptions: {
				    	                	//columns: [':visible']
				                    		columns: [0,1,2,3,4,5]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		//columns: [':visible']
				                    		columns: [0,1,2,3,4,5]
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'portrait',
				                    	customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 7;
			                     	        doc.styles.tableHeader.fontSize = 7;
			                     	        doc.styles.title.fontSize = 10;
			                     	        doc.styles.tableFooter.fontSize = 7;
				                    	},
			                            pageSize: 'A4'
				                    },
				                    {
				                    	extend : 'pdf',
				                    	footer: true,
				                    	text: 'PDF Landscape',
				                    	exportOptions: {
				                    		//columns: [':visible']
				                    		columns: [0,1,2,3,4,5]
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'landscape',
			                    		customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 6;
			                     	        doc.styles.tableHeader.fontSize = 6;
			                     	        doc.styles.title.fontSize = 8;                     	       
			                     	        doc.content[1].table.widths = "*";
			                     	       doc.styles.tableFooter.fontSize = 7;
				                    	     },
			                            pageSize: 'A4'
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
	onGo();
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	
	/* OUTLET Auto Suggestion */
	$('#OUTLET_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'OUTLET_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_OUTLET_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.POSOUTLETS);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<div><p class="item-suggestion"> ' + data.OUTLET_NAME + '</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	

});

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>