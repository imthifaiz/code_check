<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ClearanceUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<%@page import="com.track.dao.PlantMstDAO"%>

<%
String title = "Clearance Type Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">

<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popWin(URL) {
    subWin = window.open(URL, 'clearanceTypeSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
/*  function onGo(){
 
     document.form1.submit();
} */
 function onView(){
	   document.form1.action  = "../jsp/clearanceTypeSummary.jsp?action=VIEW";
	   document.form1.submit();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList QryList  = new ArrayList();



String fieldDesc="";
String PLANT="",CLEARANCETYPEID ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();

PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();

String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction");
ClearanceUtil _ClearanceUtil = new ClearanceUtil();
boolean displaySummaryEdit=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryEdit = ub.isCheckValAcc("editct", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newct", PLANT,USERID);

}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryEdit = ub.isCheckValinv("editct", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newct", PLANT,USERID);
}

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

if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
		
			if(fieldDesc.equalsIgnoreCase("Clearance Type Deleted Successfully")){
				QryList =  _ClearanceUtil.getClearanceTypeList(CLEARANCETYPEID="",PLANT,"");
			}
			else{
         QryList =  _ClearanceUtil.getClearanceTypeList(CLEARANCETYPEID,PLANT,"");
			}
      
      if(QryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }

 }catch(Exception e) { }
}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Clearance Type Summary</label></li>                                   
            </ul>
            
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../clearanceType/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
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

	

<FORM class="form-horizontal" name="form1" method="post" action="clearanceTypeSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">


<input type="hidden" name="plant" value="<%=PLANT%>">

<div class="form-group">
<label class="control-label col-form-label col-sm-2" for="Clearance Type ID">Clearance Type</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="CLEARANCE_TYPE_ID"  id="CLEARANCE_TYPE_ID" type = "TEXT" value="<%=CLEARANCETYPEID%>" size="20"  MAXLENGTH=100 class="form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'CLEARANCE_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>

  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>

		</div>
		<INPUT name="TYPE" type = "hidden" value="0">
            <INPUT name="TYPE" type = "hidden" value="1">
	</form>
	<br>
  
 <div style="overflow-x:auto;">

 <table id="table" class="table table-bordred table-striped" > 

   
   <thead> 
          <tr>  
            <th >S/N</th>  
            <th>Clearance Type</th>
            <th>Type</th> 
            <th>Edit</th>       
          </tr>  
        </thead> 

        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						

</table>
</div>

<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var CLEARANCETYPE, groupRowColSpan = 5;
		
		function getParameters(){
			return {
				"CLEARANCETYPE":CLEARANCETYPE,
				"PLANT":plant,
				"action":"GET_CLEARANCETYPE_FOR_SUMMARY"
			}
		}
		function onGo(){
			CLEARANCETYPE=document.form1.CLEARANCE_TYPE_ID.value;
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
					        	
						        	if(typeof data.CLEARANCETYPELIST[0].CLEARANCETYPE === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CLEARANCETYPELIST.length; dataIndex ++){
						        				var lineno = data.CLEARANCETYPELIST[dataIndex].CLEARANCETYPE;
						        				data.CLEARANCETYPELIST[dataIndex]['CLEARANCETYPE'] = '<a href="../clearanceType/detail?CLEARANCE_TYPE_ID=' +lineno+'&TYPE=' +data.CLEARANCETYPELIST[dataIndex].TYPE+'">'+lineno+'</a>';
						        				var sno=dataIndex+1;
						        				if(data.CLEARANCETYPELIST[dataIndex]['TYPE']=="0")
						        					data.CLEARANCETYPELIST[dataIndex]['TYPE'] = "Import"
						        					else
							        					data.CLEARANCETYPELIST[dataIndex]['TYPE'] = "Export"		
						        				data.CLEARANCETYPELIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.CLEARANCETYPELIST[dataIndex]['EDIT'] = '<a href="../clearanceType/edit?CLEARANCE_TYPE_ID=' +lineno+'&TYPE=' +data.CLEARANCETYPELIST[dataIndex].TYPE+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CLEARANCETYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
						        				
						        			}
						        		return data.CLEARANCETYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'CLEARANCETYPE', "orderable": true},
				        	{"data": 'TYPE', "orderable": true},
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
		<!-- END -->
</div>  
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
/* 	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	}); */ /* imtiziaf block */
	
    onGo(); /* imtiziaf */
    $('[data-toggle="tooltip"]').tooltip();
});
</script>


<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// CusType Auto Suggestion //
$('#CLEARANCE_TYPE_ID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'CLEARANCETYPE',  
	  async: true,   
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_CLEARANCETYPE_FOR_SUMMARY",
			CLEARANCETYPE : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.CLEARANCETYPELIST);
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
	    return '<div><p class="item-suggestion">'+data.CLEARANCETYPE+'</p></div>';
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