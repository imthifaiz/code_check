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
String title = "Supplier Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
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

/* 	function onGo() {
		document.form.action ="vendorSummary.jsp";
		document.form.submit();

	} */
	

	function ExportReport(){
		document.form.action = "/track/ReportServlet?action=Export_Contacts_Excel&ReportType=Supplier";
		document.form.submit();
		
	} 
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "", CUST_CODE = "",SUPPLIER_TYPE_ID="",supplierType="";
	String html = "";
	int Total = 0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
// 	String PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	/* CUST_CODE = StrUtils.fString(request.getParameter("CUST_NAME")); */
	/* SUPPLIER_TYPE_ID=StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID")); */
// 	fieldDesc=StrUtils.fString(request.getParameter("result"));
/* 	String msg = (String)request.getAttribute("Msg");
	fieldDesc = (String)request.getAttribute("result");
	String PGaction = (String)request.getAttribute("PGaction"); */
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false,displaySummaryExport=false,displaySummaryImport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryExport = ub.isCheckValAcc("exportvendor", PLANT,USERID);
	displaySummaryImport = ub.isCheckValAcc("importvendor", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editvendor", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newvendor", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryExport = ub.isCheckValinv("exportvendor", PLANT,USERID);
	displaySummaryImport = ub.isCheckValinv("importvendor", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editvendor", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newvendor", PLANT,USERID);
	}
	CustUtil custUtils = new CustUtil();
	custUtils.setmLogger(mLogger);
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
// 			locQryList = custUtils.getVendorListStartsWithName(CUST_CODE, PLANT," AND SUPPLIER_TYPE_ID LIKE  '%"+SUPPLIER_TYPE_ID+"%'");
			locQryList = custUtils.getVendorListStartsWithName(CUST_CODE, PLANT,SUPPLIER_TYPE_ID);
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
                <li><label>Supplier Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <%if(displaySummaryExport){ %>
              <button type="button" class="btn btn-default value='Export Master Data'"
						onClick="ExportReport();">
						Export Supplier Master</button>
					&nbsp;
					<%}%>
				</div>
              <div class="btn-group" role="group">
              <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default"
							onclick="window.location.href='../supplier/import'">
						Import Supplier</button>
					&nbsp;
					<%}%>
				</div>
				<div class="btn-group" role="group">
				<%if(displaySummaryNew){ %>
					<button type="button" class="btn btn-default"
						onclick="window.location.href='../supplier/new'" 
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
 
<%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>
	
<FORM class="form-horizontal" name="form" method="post" action="vendorSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 
<div class="form-group">
<label class="control-label col-sm-2" for="Supplier Type Desc or ID">Supplier Group Id/Desc</label>
<div class="col-sm-4 ac-box" >

<INPUT name="SUPPLIER_TYPE_ID" id="SUPPLIER_TYPE_ID" type = "TEXT" value="<%=SUPPLIER_TYPE_ID%>" size="20"  MAXLENGTH=100 class="ac-selected form-control">
<span class="select-icon"  onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form.SUPPLIER_TYPE_ID.value+'&TYPE=suppliersummary');">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  	
  	</div>
  	</div>
<div class="form-group">

<label class="control-label col-sm-2" for="Supplier Name or ID">Supplier Name/ID</label>
<div class="col-sm-4 ac-box" >

<input type="hidden" name="CUST_CODE" value="">
<input type="hidden" name="CUST_CODE1" value="">
<INPUT name="CUST_NAME" id="CUST_NAME" type="TEXT" value="<%=CUST_CODE%>" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript: popUpWin('../jsp/vendor_list.jsp?CUST_NAME=' + form.CUST_CODE.value);">
<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  	
  	</div>&nbsp;&nbsp;
  	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  	<%-- <%if(displaySummaryExport){ %>
  <button type="button" class="Submit btn btn-default" value='Export Master Data'  onclick="ExportReport();">Export Master Data</button>&nbsp;&nbsp;
  <%} %> --%>
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
  	</div>
  	
	</form>
	<br>
 

<div style="overflow-x:auto;">
 <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div>
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Supplier ID</th>
            <th style="font-size: smaller;">Supplier Name</th>
			<th style="font-size: smaller;">Supplier Group</th>
            <th style="font-size: smaller;">Contact Name</th>
            <th style="font-size: smaller;">Supplier Phone</th>
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
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
        
        <%-- <tbody>

	
	<%
	String strSupplierType="";
		for (int iCnt = 0; iCnt < locQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF": "#FFFFFF";
			Map lineArr = (Map) locQryList.get(iCnt);
			supplierType=(String)lineArr.get("SUPPLIER_TYPE_ID");
			if(supplierType.equals(""))
			{
				strSupplierType="NOSUPPLIERTYPE";
			}
			else
			{
				strSupplierType=((String)lineArr.get("SUPPLIER_TYPE_ID"));
			}
	%>

	<TR>
		<TD align="center">&nbsp;<%=iIndex%></TD>
	   
		<TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String) lineArr.get("VENDNO"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=strUtils.removeQuotes((String) lineArr.get("VNAME"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=strSupplierType%></TD>
		<TD align="center" class="textbold">&nbsp;<%=StrUtils.fString((String) lineArr.get("NAME"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String) lineArr.get("TELNO"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String) lineArr.get("ISACTIVE"))%></TD>
		<%if(displaySummaryEdit){ %>
		<TD align="center" class="textbold">&nbsp; <a href="maint_vendor.jsp?action=View&CUST_CODE=<%=(String) lineArr.get("VENDNO")%>"%>
		    <i class="fa fa-pencil-square-o"></i></a></TD>
		    <%}else{ %>
		 <TD align="center">&nbsp;<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
		 <% }%>
	</TR>
	<%
		}
	%>
</tbody>   --%>
</table>
</div>  
<script>
		var plant = document.form.plant.value;
		var tabletype;
		var SUPPLIERTYPE,SUPPLIERNAME, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"SUPPLIERTYPE":SUPPLIERTYPE,
				"SUPPLIERNAME":SUPPLIERNAME,
				"PLANT":plant,
				"action":"GET_SUPPLIER_FOR_SUMMARY"
			}
		}
		function onGo(){
			SUPPLIERTYPE=document.form.SUPPLIER_TYPE_ID.value;
			SUPPLIERNAME=document.form.CUST_NAME.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

						searching: true, // Enable searching
				        search: {
				            regex: true,   // Enable regular expression searching
				            smart: false   // Disable smart searching for custom matching logic
				        },
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].VENDNO === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].VENDNO;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../supplier/edit?action=View&CUST_CODE='+lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
						        				
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'VENDNO', "orderable": true},
				        	{"data": 'VNAME', "orderable": true},
				        	{"data": 'SUPPLIERTYPE', "orderable": true},
				        	{"data": 'NAME', "orderable": true},
				        	{"data": 'TELNO', "orderable": true},
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
				                    		columns: [0,1,2,3,4,5,6]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5,6]
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
				                    		columns: [0,1,2,3,4,5,6]
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

			    	$("#table_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
			            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
			             var searchType = $('#searchType').val(); // Get the selected search type
			            if (!searchTerm) return true; // If no search term, show all rows

			           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
			           var searchPattern;

			            // Define the search pattern based on the selected search type
			            if (searchType === 'first') {
			                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
			            } else if (searchType === 'middle') {
			                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
			            } else if (searchType === 'last') {
			                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
			            }
			            // Check each column in the row for a match
			            for (var i = 0; i < data.length; i++) {
			                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
			                if (columnData.match(searchPattern)) {
			                    return true; // Match found, include this row in results
			                }
			            }
			            return false; // No match, exclude this row from results
			        });

			        // Redraw table when the search input changes
			        $('.dataTables_filter input').on('keyup', function () {
			        	table.draw();
			        });
			    }
			    
			}

		</script>
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>

 
$(document).ready(function(){
	getLocalStorageValue('vendorSummary_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
	onGo();
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.SUPPLIER_TYPE_ID+'</p></div>';
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

	/* Supplier Auto Suggestion */
	$('#CUST_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<div><p class="item-suggestion"> ' + data.VNAME + '</p></div>';
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
$('#SUPPLIER_TYPE_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('vendorSummary_SUPPLIER_TYPE_ID', $('#SUPPLIER_TYPE_ID').val());	 
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>