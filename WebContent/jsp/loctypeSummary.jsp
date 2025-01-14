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
String title = "Location Type One Summary";
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
  function popWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 function onGo(){
 
     document.form1.submit();
}
 function onView(){
	   document.form1.action  = "../jsp/loctypeSummary.jsp?action=VIEW";
	   document.form1.submit();
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
// String PGaction   = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
/* LOCID     = strUtils.fString(request.getParameter("LOC_TYPE_ID")); */
// fieldDesc=StrUtils.fString(request.getParameter("result"));
String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction");
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
	
boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkloctype", PLANT,USERID);
displaySummaryEdit = ub.isCheckValAcc("editloctype", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newloctype", PLANT,USERID);

}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryLink = ub.isCheckValinv("summarylnkloctype", PLANT,USERID);
displaySummaryEdit = ub.isCheckValinv("editloctype", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newloctype", PLANT,USERID);
}

LocUtil _LocUtil = new LocUtil();
LocTypeUtil loctypeutil = new LocTypeUtil();

loctypeutil.setmLogger(mLogger);
_LocUtil.setmLogger(mLogger);
if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
			//if(LOCID.equalsIgnoreCase("")||LOCID==null)	RSN = strUtils.fString(request.getParameter("PRD_TYPE_ID1"));
         
         if(fieldDesc.equalsIgnoreCase("Location Type One Deleted Successfully")){
        	 locQryList =  loctypeutil.getLocTypeList(LOCID="",PLANT,"");
         }
         else{
        	 locQryList =  loctypeutil.getLocTypeList(LOCID,PLANT,"");
         }
      if(locQryList.size()== 0)
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
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Location Type One Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->      
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
               <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../loctypeone/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
               <%}%>
              </div>
		</div>
		
 <div class="box-body">
 
 <%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>

<FORM class="form-horizontal" name="form1" method="post" action="loctypeSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="LOC_DESC" value="">
<!-- imti -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 


<div class="form-group">
<label class="control-label col-sm-2" for="Location Type ID">Location Type ID</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="LOC_TYPE_ID"  id="LOC_TYPE_ID" type = "TEXT" value="<%=LOCID%>" size="20"  MAXLENGTH=100 class="form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="Submit btn btn-success" onClick="javascript:return onGo();">Search</button>
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
        
      <%--   <tbody>
    
         
 
   	  <%
         
         
          for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           
          
            Map lineArr = (Map) locQryList.get(iCnt);
       %>

          <TR>
            <TD align="center"><%=iIndex%></TD>
              <% if (displaySummaryLink) { %>
           <TD align="center"> <a href ="loctypeDetail.jsp?action=View&LOC_TYPE_ID=<%=(String)lineArr.get("LOC_TYPE_ID")%>&LOC_DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("LOC_TYPE_DESC"))%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"%><%=(String)lineArr.get("LOC_TYPE_ID")%></a></TD>
           <% } else { %>
           <TD align="center"> <%=(String)lineArr.get("LOC_TYPE_ID")%></TD>
    	      <% }%> 
    	     <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("LOC_TYPE_DESC"))%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
              <% if (displaySummaryEdit) { %>
              <TD align="center"> <a href ="maint_loctype.jsp?action=View&LOC_TYPE_ID=<%=(String)lineArr.get("LOC_TYPE_ID")%>&LOC_DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("LOC_TYPE_DESC"))%>&ACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"><i class="fa fa-pencil-square-o"></i></a></TD>
              <% } else { %>
	    <TD align="center"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
         <% }%> 	
              
          </TR>
       <%}%>
      </tbody>   --%>
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
				"action":"GET_LOCATIONTYPE_SUMMARY"
			}
		}
		function onGo(){
			LOCATIONTYPE=document.form1.LOC_TYPE_ID.value;
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].LOC_TYPE_ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_ID;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href ="../loctypeone/edit?action=View&LOC_TYPE_ID='+lineno+'&LOC_DESC=' +data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_DESC+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
												<%if(displaySummaryLink){ %>
												data.CUSTOMERTYPELIST[dataIndex]['LOC_TYPE_ID'] = '<a href ="../loctypeone/detail?action=View&LOC_TYPE_ID='+lineno+'&LOC_DESC=' +data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_DESC+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'">'+data.CUSTOMERTYPELIST[dataIndex].LOC_TYPE_ID+'</a>';
												<%} %>
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'LOC_TYPE_ID', "orderable": true},
				        	{"data": 'LOC_TYPE_DESC', "orderable": true},
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


/* location Auto Suggestion */
$('#LOC_TYPE_ID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'LOC_TYPE_ID',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_LOCATIONTYPE_SUMMARY",
			LOCATIONTYPE : query
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
		return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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