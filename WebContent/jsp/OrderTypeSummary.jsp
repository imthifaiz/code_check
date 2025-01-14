<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- navas -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%
String title = "Order Type Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
     document.form1.submit();
	}
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList OrderTypeQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",ORDERTYPE ="";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	ORDERTYPE     = strUtils.fString(request.getParameter("ORDERTYPE"));
	fieldDesc=StrUtils.fString(request.getParameter("result"));
	//navas
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
	displaySummaryLink = ub.isCheckValAcc("summarylnkordtype", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editordtype", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newordtype", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkordtype", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editordtype", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newordtype", PLANT,USERID);
	}
 	OrderTypeUtil _OrderTypeUtil = new OrderTypeUtil();
 	_OrderTypeUtil.setmLogger(mLogger);
 
	if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      OrderTypeQryList=      _OrderTypeUtil.getOrderTypeListDetails(PLANT,ORDERTYPE);
      if(OrderTypeQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - OrderTypeSummary.jsp ", e); }
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Order Type Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
               <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../ordertype/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              <%} %>
              </div>
		</div>
		
 <div class="box-body">
 
<div class="mainred">
	  	<%-- <CENTER><strong><%=fieldDesc%></strong></CENTER> --%>
	</div>
	
<FORM class="form-horizontal" name="form1" method="post" action="OrderTypeSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT type = "hidden" name="ORDERTYPE1" value ="">
<INPUT type = "hidden" name="ORDERDESC" value ="">
<INPUT type = "hidden" name="TYPE" value ="">
<INPUT type = "hidden" name="REMARKS" value ="">
<INPUT name="ACTIVE" type = "hidden" value="Y"   >
<INPUT name="ACTIVE" type = "hidden" value="N"   >
<!-- navas -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
  
  
<div class="form-group">
<label class="control-label col-sm-4" for="Order Type">Order Type</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="ORDERTYPE" id="ORDERTYPE"type = "TEXT"  value="<%=ORDERTYPE%>" size="20"  MAXLENGTH=100 class="form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/OrderType_list.jsp?ORDERTYPE='+form1.ORDERTYPE.value+'&FORMTYPE=notype');">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
<!--   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
<table id="tabletype" class="table table-bordred table-striped" >
<!--    role="grid" aria-describedby="tableInventorySummary_info" >  -->
   
   <thead style="text-align: center">   
          <tr>  
           <th style="font-size: smaller;">S/N</th>  
           <th style="font-size: smaller;">OrderType</th>  
           <th style="font-size: smaller;">OrderDesc</th>  
           <th style="font-size: smaller;">Type</th>  
           <th style="font-size: smaller;">IsActive</th>  
           <th style="font-size: smaller;">Edit</th>      
          </tr>  
        </thead>
          <!-- navas -->
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
<%-- <tbody>

<%
for (int iCnt =0; iCnt<OrderTypeQryList.size(); iCnt++){
int iIndex = iCnt + 1;
String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";
Map lineArr = (Map) OrderTypeQryList.get(iCnt);
%>

<TR>
<TD align="center"><%=iIndex%></TD>
<%if(displaySummaryLink){ %>
<TD align="center"> &nbsp;
<a href ="OrderTypeDetail.jsp?action=View&ORDERTYPE=<%=(String)lineArr.get("ORDERTYPE")%>&ORDERDESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ORDERDESC"))%>&TYPE=<%=(String)lineArr.get("TYPE")%>&REMARKS=<%=(String)lineArr.get("REMARKS")%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>")%><%=(String)lineArr.get("ORDERTYPE")%></a></TD>
<%}else{ %>
<TD align="center"> &nbsp;
<%=(String)lineArr.get("ORDERTYPE")%></TD>
<%} %>
<TD align="center">&nbsp; <%=strUtils.fString((String)lineArr.get("ORDERDESC"))%></TD>
<TD align="center">&nbsp; <%=strUtils.fString((String)lineArr.get("TYPE"))%></TD>
<TD align="center">&nbsp; <%=strUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
<%if(displaySummaryEdit){ %>
<TD align="center"> &nbsp;
<a href ="maint_orderType.jsp?action=Edit&ORDERTYPE=<%=(String)lineArr.get("ORDERTYPE")%>&ORDERDESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ORDERDESC"))%>&TYPE=<%=(String)lineArr.get("TYPE")%>&REMARKS=<%=(String)lineArr.get("REMARKS")%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"><i class="fa fa-pencil-square-o"></i></a></TD>
<% } else { %>
<TD align="center">&nbsp;<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
<% }%>
</TR>
<%}%>
</tbody> --%>
 
</table>
</div>  
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var ORDTYP, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"ORDTYP":ORDTYP,
				"PLANT":plant,
				"action":"GET_ORDERTYPE_FOR_SUMMARY"
			}
		}
		function onGo(){
			ORDTYP=document.form1.ORDERTYPE.value;
			
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].ORDERTYPE === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].ORDERTYPE;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
												
						        				<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../ordertype/edit?action=Edit&ORDERTYPE='+lineno+'&ORDERDESC=' +data.CUSTOMERTYPELIST[dataIndex].ORDERDESC+'&TYPE=' +data.CUSTOMERTYPELIST[dataIndex].TYPE+'&REMARKS=' +data.CUSTOMERTYPELIST[dataIndex].REMARKS+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<% } else { %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="#"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%  } %>
						        				<%if(displaySummaryLink){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['ORDERTYPE'] = '<a href="../ordertype/detail?action=View&ORDERTYPE='+lineno+'&ORDERDESC=' +data.CUSTOMERTYPELIST[dataIndex].ORDERDESC+'&TYPE=' +data.CUSTOMERTYPELIST[dataIndex].TYPE+'&REMARKS=' +data.CUSTOMERTYPELIST[dataIndex].REMARKS+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'">'+data.CUSTOMERTYPELIST[dataIndex].ORDERTYPE+'</a>';
						        				<%  } %>
							        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'ORDERTYPE', "orderable": true},
				        	{"data": 'ORDERDESC', "orderable": true},
				        	{"data": 'TYPE', "orderable": true},
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
				                    		columns: [0,1,2,3,4]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4]
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
				                    		columns: [0,1,2,3,4]
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
	  </div>
	  </div>
	  </div>
	  
	  
	  
<script>
$(document).ready(function(){
	/* $('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	}); */
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>


<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// UserId Auto Suggestion //
$('#ORDERTYPE').typeahead({
hint: true,
minLength:0,
searchOnFocus: true
},
{
display: 'ORDERTYPE',
async: true,
source: function (query, process,asyncProcess) {
var urlStr = "/track/MasterServlet";
$.ajax( {
type : "POST",
url : urlStr,
async : true,
data : {
PLANT : plant,
ACTION : "GET_ORDERTYPE_FOR_SUMMARY",
ORDTYP : query
},
dataType : "json",
success : function(data) {
return asyncProcess(data.CUSTOMERTYPELIST);
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
return '<p class="item-suggestion">'+ data.ORDERTYPE +'</p>';
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