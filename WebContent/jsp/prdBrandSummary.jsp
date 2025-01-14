<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<!-- RESVI -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->

<%
String title = "Product Brand Summary";
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
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
/*  function onGo(){

	   document.form1.action  = "/track/ProductBrandServlet?action=Summary";
	  document.form1.submit();
} */
</script>

<%
String PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String responseMsg = (String)request.getParameter("response");
String productBrandID = "";
/* String productBrandID = request.getParameter("brandID"); */
/* String msg = (String)request.getAttribute("Msg"); */
	
String fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction");

//RESVI
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
//END

boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkprdBrand", PLANT,USERID);
displaySummaryEdit = ub.isCheckValAcc("editprdBrand", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newprdBrand", PLANT,USERID);




}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryLink = ub.isCheckValinv("summarylnkprdBrand", PLANT,USERID);
displaySummaryEdit = ub.isCheckValinv("editprdBrand", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newprdBrand", PLANT,USERID);
}

List brandItemsList = new ArrayList();
if( session.getAttribute("brandItemsList")!=null){
	 brandItemsList = (ArrayList)session.getAttribute("brandItemsList");
	 session.setAttribute("brandItemsList",null);
}

if(responseMsg == null)
	responseMsg = (String)request.getAttribute("response");
if(productBrandID == null)
	productBrandID = "";
%>
<center>
	<h2><small class="success-msg"><%=responseMsg%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Product Brand Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../productbrand/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
               <%}%>
              </div>
		</div>
		
 <div class="box-body">
 
 <%-- <div class="mainred">
	  	<CENTER><strong><%=responseMsg%></strong></CENTER>
	</div> --%>
 
<FORM class="form-horizontal" name="form1" method="post" action="">
  <input type="hidden" name="PRD_BRAND_DESC" value="">
 <INPUT name="ACTIVE" type = "hidden" value="">
 
 <!--RESVI-->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->

 
 
<div class="form-group">
<label class="control-label col-sm-2" for="Product Brand ID">Product Brand ID</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="PRD_BRAND_ID"   id="PRD_BRAND_ID" type = "TEXT" value = "<%=productBrandID%>" size="20"  MAXLENGTH=100 class="form-control">
	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&formName=form1');">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
   </FORM>
   
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
<table id="tabletype" class="table table-bordred table-striped" > 
   
   <thead>  
          <tr>  
            <th >S/N</th>  
            <th>Product Brand ID</th> 
            <th>Product Brand Description</th>
            <th>IsActive</th>
            <th>Edit</th>     
          </tr>  
        </thead> 
        
        <!-- RESVI -->
<tfoot align="right" style="display: none;">
<tr>
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
         for (int iCnt =0; iCnt<brandItemsList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           
          
             Map lineArr = (Map) brandItemsList.get(iCnt);
       %>

          <TR>
            <TD align="center"><%=iIndex%></TD>
            <% if (displaySummaryLink) { %>
           <TD align="center"><%=(String)lineArr.get("PRD_BRAND_ID")%></TD>
           <% } else { %>
           <TD align="center"><%=(String)lineArr.get("PRD_BRAND_ID")%></TD>
             <% }%> 
    	     <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=StrUtils.fString((String)lineArr.get("PRD_BRAND_DESC"))%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=StrUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
              <% if (displaySummaryEdit) { %>
              <TD align="center"><a href="maint_prdBrand.jsp?brandID=<%=(String) lineArr.get("PRD_BRAND_ID")%>&brandDesc=<%=(String) lineArr.get("PRD_BRAND_DESC")%>&isActive=<%=(String) lineArr.get("ISACTIVE")%>"><i class="fa fa-pencil-square-o"></i></a></TD>
              <% } else { %>
              <TD align="center"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
              <% }%> 
          </TR>
       <%}%> 
   </tbody>  --%>
</table>

</div>  
<!-- RESVI INCLUDES -->
<script LANGUAGE="JavaScript">
var plant = document.form1.plant.value;
var tabletype;
var PRODUCTBRANDID, groupRowColSpan = 6;

function getParameters(){
	return {
		"PRODUCTBRANDID":PRODUCTBRANDID,
		"PLANT":plant,
		"action":"GET_PRODUCTBRANDID_FOR_SUMMARY"
			}
		}
		function onGo(){
			PRODUCTBRANDID=document.form1.PRD_BRAND_ID.value;
			
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#tabletype').DataTable({
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
					        	
						        	if(typeof data.PRODUCTBRAND[0].PRD_BRAND_ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.PRODUCTBRAND.length; dataIndex ++){
						        				var lineno = data.PRODUCTBRAND[dataIndex].PRD_BRAND_ID;						        				
						        				var sno=dataIndex+1;
						        				data.PRODUCTBRAND[dataIndex]['SNO'] = sno;
						        				<%if(displaySummaryEdit){ %>
						        				data.PRODUCTBRAND[dataIndex]['EDIT'] = '<a href="../productbrand/edit?brandID=' +lineno+'&brandDesc=' +data.PRODUCTBRAND[dataIndex].PRD_BRAND_DESC+'&isActive=' +data.PRODUCTBRAND[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%}else{ %>
												data.PRODUCTBRAND[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>


						        			}
						        			
						        		return data.PRODUCTBRAND;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'PRD_BRAND_ID', "orderable": true},
				        	{"data": 'PRD_BRAND_DESC', "orderable": true},
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
				                    		columns: [0,1,2,3]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3]
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
				                    		columns: [0,1,2,3]
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
			    	$("#tabletype_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
			        	tabletype.draw();
			        });
			    }
			    
			}

		</script>
<!-- ENDS -->
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
 $(document).ready(function(){
// 	  $('#table').dataTable({
// 		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
// 	});  
	  onGo();
    
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// UserId Auto Suggestion //
$('#PRD_BRAND_ID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'PRD_BRAND_ID',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_PRODUCTBRANDID_FOR_SUMMARY",
			PRODUCTBRANDID : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.PRODUCTBRAND);
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
return '<p class="item-suggestion">'+ data.PRD_BRAND_ID +'</p>';
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