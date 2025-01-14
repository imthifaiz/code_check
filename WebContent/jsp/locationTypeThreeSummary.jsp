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
String title = "Location Type Three Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script language="javascript">

var subWin = null;
  function popWin(URL) {
    subWin = window.open(encodeURI(URL), 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }

</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList locQryList  = new ArrayList();

_userBean.setmLogger(mLogger);

String fieldDesc="";
String PLANT="",LOCID ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
LOCID     = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
fieldDesc=StrUtils.fString(request.getParameter("result"));
//imti
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
	String msg = (String)request.getAttribute("Msg");
boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkloctypethree", PLANT,USERID);
displaySummaryEdit = ub.isCheckValAcc("editloctypethree", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newloctypethree", PLANT,USERID);

}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryLink = ub.isCheckValinv("summarylnkloctypethree", PLANT,USERID);
displaySummaryEdit = ub.isCheckValinv("editloctypethree", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newloctypethree", PLANT,USERID);
}

LocUtil _LocUtil = new LocUtil();
LocTypeUtil loctypeutil = new LocTypeUtil();

loctypeutil.setmLogger(mLogger);
_LocUtil.setmLogger(mLogger);

%>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Location Type Three Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
               <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default"	onClick="window.location.href='../locationtypethree/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
               <%}%>
              </div>
		</div>
		
 <div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="lOC_TYPE_DESC3" value="">
<!-- imti -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 


<div class="form-group">
<label class="control-label col-sm-2" for="Location Type ID">Location Type ID</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="LOC_TYPE_ID3"  id="LOC_TYPE_ID3"  type = "TEXT" value="<%=LOCID%>" size="20"  MAXLENGTH=100 class="form-control">
<!--  <span class="btn-danger input-group-addon" onClick="javascript:popWin('../jsp/LocTypeThreeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);"> -->
<!--   <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="Submit btn btn-success" onClick="javascript:return onGo();">Search</button>
		</div>
		
		<INPUT name="ACTIVE" type = "hidden" value="Y" >
            <INPUT name="ACTIVE" type = "hidden" value="N" >
	</form>
	<br>
 
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead>  
          <tr>  
            <th>S/N</th>  
            <th>Location Type ID</th> 
            <th>Location Type Description</th>
            <th>IsActive</th>
            <th>Edit</th>     
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
							
							</tr>
						</tfoot>
						<!-- END -->
        
</table>
</div>  
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var LOCATIONTYPE, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"LOCATIONTYPE":LOCATIONTYPE,
				"PLANT":plant,
				"action":"GET_LOCATIONTYPETWO_SUMMARY"
			}
		}
		function onGo(){
			LOCATIONTYPE=document.form1.LOC_TYPE_ID3.value;
		       var urlStr = "../locationtypethree/summary";
			/*    var urlStr = "/track/MasterServlet"; */
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].LOC_TYPE_ID3 === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_ID3;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href ="../locationtypethree/edit?action=View&LOC_TYPE_ID3='+lineno+'&lOC_TYPE_DESC3=' +data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_DESC3+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].IsActive+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
												<%if(displaySummaryLink){ %>
												data.CUSTOMERTYPELIST[dataIndex]['LOC_TYPE_ID3'] = '<a href ="../locationtypethree/detail?action=View&LOC_TYPE_ID3='+lineno+'&lOC_TYPE_DESC3=' +data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_DESC3+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].IsActive+'">'+data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_ID3+'</a>';
												<% } %>
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'LOC_TYPE_ID3', "orderable": true},
				        	{"data": 'LOC_TYPE_DESC3', "orderable": true},
				        	{"data": 'IsActive', "orderable": true},				        	
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
    var plant= '<%=PLANT%>'; 
    /* location three Auto Suggestion */
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
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