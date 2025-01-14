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
String title = "Reason Summary";
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
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 function onGo(){
 
     document.form1.submit();
}
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	ArrayList locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "", RSN = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
// 	String PGaction = strUtils	.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	RSN = strUtils.fString(request.getParameter("ITEM_ID"));
// 	fieldDesc=StrUtils.fString(request.getParameter("result"));
String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction");
	//navas
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		DateUtils _dateUtils = new DateUtils();
		String collectionDate=_dateUtils.getDate();
		ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
		Map map = (Map) al.get(0);
		String CNAME = (String) map.get("PLNTDESC");
		//END
	
	boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryLink = ub.isCheckValAcc("summarylnkrsn", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editrsn", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newrsn", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkrsn", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editrsn", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newrsn", PLANT,USERID);
	}
	
	LocUtil _LocUtil = new LocUtil();
	_LocUtil.setmLogger(mLogger);
	RsnMstUtil _rsnUtil = new RsnMstUtil();
	_rsnUtil.setmLogger(mLogger);
	if (PGaction.equalsIgnoreCase("View")) {
		try {

			Hashtable ht = new Hashtable();
			if (strUtils.fString(PLANT).length() > 0)
				ht.put("PLANT", PLANT);

			locQryList = _rsnUtil.getReasonMstDetails(RSN, PLANT, "");

			if (locQryList.size() == 0) {
				fieldDesc = "Data Not Found";
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
                <li><label>Reason Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../reasoncode/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              <%}%>
              </div>
		</div>
		
 <div class="box-body">
 
<%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>
<FORM class="form-horizontal" name="form1" method="post" action="rsnSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="ITEM_DESC" value="">
<input type="hidden" name="ITEM_ID1" value="">
<!-- navas -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 
	

<div class="form-group">
<label class="control-label col-sm-2" for="Reason Code">Reason Code</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="ITEM_ID" id="ITEM_ID" type="TEXT" value="<%=RSN%>" size="20" MAXLENGTH=100 class="form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ReasonMstList.jsp?ITEM_ID='+form1.ITEM_ID.value);">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
 <!--  <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
	</form>
	<br>

<div style="overflow-x:auto;">
<table id="table"  class="table table-bordred table-striped" > 
   
   <thead>  
          <tr>  
            <th >S/N</th>  
            <th>Reason Code</th> 
            <th>Reason Description</th>
            <th>IsActive</th>
            <th>Edit</th>      
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
								
							</tr>
						</tfoot>
						<!-- END -->
        
      <%--   <tbody>

	<%
		for (int iCnt = 0; iCnt < locQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";

			Map lineArr = (Map) locQryList.get(iCnt);
	%>

	<TR>
		<TD align="center"><%=iIndex%></TD>
         <% if (displaySummaryLink) { %>
		<TD align="center"><a href="reasonDetail.jsp?action=View&ITEM_ID=<%=(String) lineArr.get("rsncode")%>&ITEM_DESC=<%=strUtils.replaceCharacters2Send((String) lineArr.get("rsndesc"))%>&ISACTIVE=<%=(String) lineArr.get("ISACTIVE")%>"%><%=(String) lineArr.get("rsncode")%></a></TD>
		<% } else { %>
		<TD align="center"><%=(String) lineArr.get("rsncode")%></TD>
		<% }%>  
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String) lineArr.get("rsndesc"))%></TD>
		<TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String) lineArr.get("ISACTIVE"))%></TD>
		 <% if (displaySummaryEdit) { %>
		<TD align="center" class="textbold">&nbsp; <a href="maint_reason.jsp?ITEM_ID=<%=(String) lineArr.get("rsncode")%>&ITEM_DESC=<%=(String)lineArr.get("rsndesc")%>&ACTIVE=<%=(String)lineArr.get("ISACTIVE")%>">
		    <i class="fa fa-pencil-square-o"></i></a></TD>
		     <% } else { %>
		 <TD align="center">&nbsp;<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
	        <% }%> 
	</TR>
	<%
		}
	%>
</tbody>   --%>
</table>
</div>
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var REASONCODE, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"REASONCODE":REASONCODE,
				"PLANT":plant,
				"action":"GET_RSN_SUMMARY"
			}
		}
		function onGo(){
			REASONCODE = document.form1.ITEM_ID.value;
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].rsncode === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
     				var lineno = data.CUSTOMERTYPELIST[dataIndex].rsncode;
     				var sno=dataIndex+1;
     				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
			<%if(displaySummaryEdit){ %>
     				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../reasoncode/edit?ITEM_ID='+lineno+'&ITEM_DESC=' +data.CUSTOMERTYPELIST[dataIndex].rsndesc+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
			<%}else{ %>
				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
			<%} %>
			<%if(displaySummaryLink){ %>
				data.CUSTOMERTYPELIST[dataIndex]['rsncode'] = '<a href="../reasoncode/detail?action=Edit&ITEM_ID='+lineno+'&ITEM_DESC=' +data.CUSTOMERTYPELIST[dataIndex].rsndesc+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'">'+data.CUSTOMERTYPELIST[dataIndex].rsncode+'</a>';
			<%} %>
     			}
			return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'rsncode', "orderable": true},
				        	{"data": 'rsndesc', "orderable": true},
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
	/* $('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	});
     */
     onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>


<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// UserId Auto Suggestion //
$('#ITEM_ID').typeahead({
hint: true,
minLength:0,
searchOnFocus: true
},
{
display: 'rsncode',
async: true,
source: function (query, process,asyncProcess) {
var urlStr = "/track/MasterServlet";
$.ajax( {
type : "POST",
url : urlStr,
async : true,
data : {
PLANT : plant,
ACTION : "GET_RSN_SUMMARY",
REASONCODE : query
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
return '<p class="item-suggestion">'+ data.rsncode +'</p>';
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