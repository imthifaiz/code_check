<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%@page import="com.track.dao.PlantMstDAO"%>
<%
String title = "Assign Customer Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
 		document.form1.action ="../product/Custproduct"
        document.form1.submit();
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
 	
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList locQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",ITEM ="",AssignedUser="",AssignedLoc="",chkdDoNo="",CUST_NAME="",COMPANY,USERId,USER_LEVEL;
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	ITEM     = strUtils.fString(request.getParameter("ITEM"));
	chkdDoNo     = strUtils.fString(request.getParameter("chkdDoNo"));
	CUST_NAME     = strUtils.fString(request.getParameter("CUST_NAME"));
	fieldDesc=StrUtils.fString(request.getParameter("result"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
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
	

	COMPANY = strUtils.fString(request.getParameter("COMPANY"));
	USERId = strUtils.fString(request.getParameter("USERID"));
	USER_LEVEL = strUtils.fString(request.getParameter("USER_LEVEL"));

%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Assign Customer Product</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
         <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>   
		
 <div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="UserProduct.jsp">
  <input type="hidden" name="xlAction" value="">
  <input type="hidden" name="PGaction" value="View">
  <INPUT type = "hidden" name="REMARKS" value ="">
  <input type="hidden" name="plant" value="<%=PLANT%>">
  <INPUT type = "hidden" name="COMNAME" value ="">
  <INPUT type = "hidden" name="CHK_ADDRESS" value ="Y">
  <INPUT type = "hidden" name="CUSTNO" value ="">
  <INPUT name="ACTIVE" type = "hidden" value="Y"   >
  <INPUT name="ACTIVE" type = "hidden" value="N"   >
  <INPUT name="COMPANY" type="hidden" value="<%=PLANT%>" size="20" MAXLENGTH="20"/>
  <INPUT name="USER_LEVEL" type = "hidden" value=""   >
  <INPUT name="chkdDoNo"  name="chkdDoNo" type = "hidden" value=""   >
  


<div class="form-group">
<label class="control-label col-sm-4" for="Customer Id">Customer</label>
<div class="col-sm-3 ac-box" >
<div class="input-group">
<INPUT name="CUST_NAME" id="CUST_NAME" type = "TEXT" value="" class="ac-selected form-control" placeholder="select a customer">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
  	</div>
  	</div>
  	</div>


<div class="form-group" align="center">
<!--   	<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp; -->
  	</div>

  <br>	
  
  	<div class="row">
  		<div class="col-12 col-sm-12">
  			<label>
  			<input type="checkbox" class="form-check-input"  style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		</div>                        
  	</div>

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
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Product Id</th>
            <th style="font-size: smaller;">Product Description</th>
            <th style="font-size: smaller;">Product Department</th>
             <th style="font-size: smaller;">Product Category</th>
             <th style="font-size: smaller;">Product Sub-Category</th>
             <th style="font-size: smaller;">Product Brand</th>
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
								<th></th>
							</tr>
						</tfoot>
</table>
</div>  
  </FORM>
 <div class="form-group">
  	<div class="col-sm-12" align="center">
  	      <button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAssign();"><b>Assign</b></button>
  	</div>
  	</div>
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var ITEM,ITEMDESC,PRD_DEPT_ID,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,CUST_NAME, groupRowColSpan = 6;

		function onAssign(){
			var checkFound = false;
			var len = document.form1.chkdDoNo.length;
			var CUST_NAME   = document.form1.CUST_NAME.value;
			 var orderLNo; 
			if (len == undefined)
				len = 1;
			/* for ( var i = 0; i < len; i++) {
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
			} */
			if(CUST_NAME == "" || CUST_NAME == null) {alert("Please Enter/Select Customer"); return false; }
// 			form1.setAttribute("target", "_blank");
// 			form1.setAttribute("target");
				document.form1.action  = "/track/product/AssignPrd";
		   		document.form1.submit();
		}

		function getParameters(){
			return {
				"ITEM":ITEM,
				"ITEMDESC":ITEMDESC,
				"PRD_DEPT_ID":PRD_DEPT_ID,
				"PRD_CLS_ID":PRD_CLS_ID,
				"PRD_TYPE_ID":PRD_TYPE_ID,
				"PRD_BRAND_ID":PRD_BRAND_ID,
				"PLANT":plant,
				"CUST_NAME":CUST_NAME,
				"CUSTNO":CUSTNO,
				"action":"GET_USERASSIGNITEM_FOR_SUMMARY"
			}
		}
		function onGo(){
			CUST_NAME=document.form1.CUST_NAME.value;
			CUSTNO=document.form1.CUSTNO.value;	
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],

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
					        	
						        	if(typeof data.SUPPLIERTYPELIST[0].ITEM === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.SUPPLIERTYPELIST.length; dataIndex ++){
						        				var lineno = data.SUPPLIERTYPELIST[dataIndex].ITEM;
						        				var sno=dataIndex+1;
						        				//data.SUPPLIERTYPELIST[dataIndex]['SNO'] = sno;
						        				if (data.SUPPLIERTYPELIST[dataIndex]['CHK'] == 'yes')
						        				data.SUPPLIERTYPELIST[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; checked name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['ITEM']+'" >';
						        				else
						        				data.SUPPLIERTYPELIST[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['ITEM']+'" >';
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHK', "orderable": false},
				        	{"data": 'ITEM', "orderable": true},
				        	{"data": 'ITEMDESC', "orderable": true},
				        	{"data": 'PRD_DEPT_ID', "orderable": true},
				        	{"data": 'PRD_CLS_ID', "orderable": true},
				        	{"data": 'PRD_TYPE_ID', "orderable": true},
				        	{"data": 'PRD_BRAND_ID', "orderable": true},
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
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
    var plant= '<%=PLANT%>';


	$('#CUST_NAME').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
	},
	{
		display: 'CNAME',
		async: true,
		source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax({
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CUSTOMER_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CUSTMST);
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
			   return '<div> <p class="item-suggestion">' + data.CNAME + '</p><br/><p class="item-suggestion">'+data.CUSTNO+'</p> </div>';
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
	}).on('typeahead:select',function(event,selection){
		$("input[name=CUSTNO]").val(selection.CUSTNO);
		onGo();
	});

});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>