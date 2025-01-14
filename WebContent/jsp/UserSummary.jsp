<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<!-- RESVI -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%

String title = "User Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"/></script>

<script language="javascript">

var subWin = null;
function popUpWin(URL) {
subWin = window.open(URL, 'UserSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onGo(){
document.form1.submit();
}
</script>

<%
StrUtils strUtils = new StrUtils();
Generator generator = new Generator();
userBean _userBean = new userBean();
ArrayList invQryList = new ArrayList();
_userBean.setmLogger(mLogger);
String fieldDesc="",result;
String PLANT="",COMPANY ="",USERID="",USER_LEVEL="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session = request.getSession();

String PGaction = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String LOGIN_USER =session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
COMPANY = strUtils.fString(request.getParameter("COMPANY"));
USERID = strUtils.fString(request.getParameter("USERID"));
USER_LEVEL = strUtils.fString(request.getParameter("USER_LEVEL"));
fieldDesc = strUtils.fString(request.getParameter("RESULT"));

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

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryEdit=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	
displaySummaryNew = ub.isCheckValAcc("uaNewLevel", PLANT,LOGIN_USER);
displaySummaryLink = ub.isCheckValAcc("summarylnkuaMaintLevel", PLANT,LOGIN_USER);
displaySummaryEdit = ub.isCheckValAcc("edituaMaintLevel", PLANT,LOGIN_USER);
}
if(systatus.equalsIgnoreCase("PAYROLL"))
{
displaySummaryNew = ub.isCheckValPay("uaNewLevel", PLANT,LOGIN_USER);
displaySummaryLink = ub.isCheckValPay("summarylnkuaMaintLevel", PLANT,LOGIN_USER);
displaySummaryEdit = ub.isCheckValPay("edituaMaintLevel", PLANT,LOGIN_USER);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryNew = ub.isCheckValinv("uaNewLevel", PLANT,LOGIN_USER);
displaySummaryLink = ub.isCheckValinv("summarylnkuaMaintLevel", PLANT,LOGIN_USER);
displaySummaryEdit = ub.isCheckValinv("edituaMaintLevel", PLANT,LOGIN_USER);
}
if(PGaction.equalsIgnoreCase("View")){
try{
Hashtable ht = new Hashtable();
if(strUtils.fString(COMPANY).length() > 0) ht.put("DEPT",COMPANY);
if(strUtils.fString(USERID).length() > 0) ht.put("USER_ID",USERID);
invQryList =_userBean.getUserListforCompany(ht,PLANT,COMPANY,USERID,USER_LEVEL);
if(invQryList.size()== 0)
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
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>User Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
<div class="box-header menu-drop">
<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
<div class="box-title pull-right">
					<div class="btn-group" role="group">
					<%if(displaySummaryNew){ %>
						<button type="button" class="btn btn-default"
							onclick="window.location.href='../jsp/createNewUser.jsp'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
					 <%} %> 
				</div>

<h1 style="font-size: 18px; cursor: pointer;"
class="box-title pull-right"
onclick="window.location.href='../home'">
<i class="glyphicon glyphicon-remove"></i>
</h1>
</div>
</div>

<div class="box-body">

<div class="mainred">

</div>

<FORM class="form-horizontal" name="form1" method="post" action="UserSummary.jsp">
<input type="hidden" name="xlAction" value=""/>
<input type="hidden" name="PGaction" value="View"/>

<!--RESVI-->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->

<div class="form-group">
<% if(PLANT.equalsIgnoreCase("track")){%>
<label class="control-label col-form-label col-sm-2" for="Company">Company</label>
<div class="col-sm-3" >
<div class="input-group">
<INPUT name="COMPANY" type="TEXT" value="<%=COMPANY%>" size="20" MAXLENGTH="20" class="form-control">
<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/plant_list_usersummary.jsp?COMPANY='+form1.COMPANY.value);">
<a href="#" data-toggle="tooltip" data-placement="right" title="Company Details">
<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
</div>
</div>
<% }else{%>
<INPUT name="COMPANY" type="hidden" value="<%=PLANT%>" size="20" MAXLENGTH="20"/>
<% }%>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="Group">Group</label>
<div class="col-sm-3" >
<div class="input-group">
<INPUT name="USER_LEVEL"  id="USER_LEVEL" type="TEXT" value="<%=USER_LEVEL%>" size="20" MAXLENGTH="20" class="form-control">
<span class="select-icon"  onclick="$(this).parent().find('input[name=\'USER_LEVEL\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/userLevel.jsp?USER_LEVEL='+form1.USER_LEVEL.value);"> -->
<!-- <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
</div>
</div>
<!-- <div class="form-inline"> -->
<label class="control-label col-sm-2" for="UserId">UserId</label>
<div class="col-sm-3" >
<div class="input-group">
<INPUT name="USERID"  id="USERID" type="TEXT" value="<%=USERID%>" size="20" MAXLENGTH="20" class="form-control">
 <span class="select-icon" onclick="$(this).parent().find('input[name=\'USERID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/user_list_usersummary.jsp?COMPANY='+form1.COMPANY.value+'&USERID='+form1.USERID.value+'&USER_LEVEL='+form1.USER_LEVEL.value);"> -->
<!-- <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
</div>
</div>
<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
<!-- </div> -->
</div>
</FORM>

<br>


<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" >

<thead>
<tr>
<th >S/N</th>
<th>Company</th>
<th>UserId</th>
<th>Name</th>
<th>Group</th>
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
<th></th>

</tr>
</tfoot>
<!-- END -->

<!-- 						 Resvi start the comment line -->

<%-- <tbody>



<%
for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
int iIndex = iCnt + 1;
String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";
Map lineArr = (Map) invQryList.get(iCnt);

%>
<TR>
<TD align="center"><%= iIndex%></TD>
<TD align="center"><%= (String)lineArr.get("DEPT") %></TD>
<!-- &lt;TD align=&quot;center&quot;&gt; &lt;a href =&quot;addNewUser.jsp?action=View&amp;USER_ID1=&lt;%=(String)lineArr.get(&quot;USER_ID&quot;)%&gt;&amp;COMPANY=&lt;%=(String)lineArr.get(&quot;DEPT&quot;)%&gt;&quot;)%&gt;&lt;%=(String)lineArr.get(&quot;USER_ID&quot;)%&gt;&lt;/a&gt;&lt;/TD&gt; -->
<%if(displaySummaryLink){ %>
<TD align="center"><a href="userDetail.jsp?action=View&USER_ID=<%=(String)lineArr.get("USER_ID")%>&USER_ID1=<%=(String)lineArr.get("USER_ID")%>&DEPT=<%=(String)lineArr.get("DEPT")%>">
<%=(String)lineArr.get("USER_ID")%></a></TD>
<% } else { %>
<TD align="center"><%=(String)lineArr.get("USER_ID")%></TD>
<% }%>
<TD align="center"><%= (String)lineArr.get("USER_NAME") %></TD>
<% if(systatus.equalsIgnoreCase("PAYROLL")) { %>
<TD align="center"><%= (String)lineArr.get("USER_LEVEL_PAYROLL") %></TD>
<% } else if(systatus.equalsIgnoreCase("ACCOUNTING")) { %>
<TD align="center"><%= (String)lineArr.get("USER_LEVEL_ACCOUNTING") %></TD>
<% } else { %>
<TD align="center"><%= (String)lineArr.get("USER_LEVEL") %></TD>
<% } %>
<%if(displaySummaryEdit){ %>
<TD align="center"><a href="maintNewUser.jsp?action=View&USER_ID=<%=(String)lineArr.get("USER_ID")%>&USER_ID1=<%=(String)lineArr.get("USER_ID")%>&DEPT=<%=(String)lineArr.get("DEPT")%>"><i class="fa fa-pencil-square-o"></i></a></TD>
<%}else{%>
<TD align="center"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
<%}%>
</TR>

<% }%>
</tbody> --%>

<!--        resvi ends the comment line -->

</table>

</div>

<!-- RESVI INCLUDES -->
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var table;
		var GROUP,USER,COMPANY, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"GROUP":GROUP,
				"USER":USER,
				"COMPANY":COMPANY,
				"PLANT":plant,
				"action":"GET_USERADMIN_FOR_USERDETAILS"
			}
		}
		function onGo(){
			GROUP=document.form1.USER_LEVEL.value;
			USER=document.form1.USERID.value;
			COMPANY=document.form1.COMPANY.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (table){
			    	table.ajax.url( urlStr ).load();
			    }else{
			    	table = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.USERLEVEL[0].USER_ID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.USERLEVEL.length; dataIndex ++){
						        				var lineno = data.USERLEVEL[dataIndex].USER_ID;						        				
						        				var sno=dataIndex+1;
						        				data.USERLEVEL[dataIndex]['SNO'] = sno;		
						        				<%if(displaySummaryEdit){ %>				        				
						        				data.USERLEVEL[dataIndex]['EDIT'] = '<a href="../user/edit?action=View&USER_ID='+lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%}else{ %>
						        				data.USERLEVEL[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
												<%if(displaySummaryLink){ %>
												data.USERLEVEL[dataIndex]['DEPT'] =  '<a href="../user/detail?action=View&USER_ID='+lineno+'&USER_ID1='+data.USERLEVEL[dataIndex].USER_ID+'">'+data.USERLEVEL[dataIndex].DEPT+'</a>';
												<%} %>

												//res
												
												<%  if(systatus.equalsIgnoreCase("INVENTORY")){ %>
											    data.USERLEVEL[dataIndex]['USER_LEVEL'] = data.USERLEVEL[dataIndex]['USER_LEVEL'];
											    <% } %> 
											    
											    <% if(systatus.equalsIgnoreCase("ACCOUNTING")){ %>
											          data.USERLEVEL[dataIndex]['USER_LEVEL'] = data.USERLEVEL[dataIndex]['USER_LEVEL_ACCOUNTING'] ;
											    <% } %> 

											    <% if(systatus.equalsIgnoreCase("PAYROLL")){ %>
										          data.USERLEVEL[dataIndex]['USER_LEVEL'] = data.USERLEVEL[dataIndex]['USER_LEVEL_PAYROLL']; 
										         <% } %> 
										         
											    //ends
						        			}
						        		return data.USERLEVEL;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'DEPT', "orderable": true},
				        	{"data": 'USER_ID', "orderable": true},
				        	{"data": 'USER_NAME', "orderable": true},
				        	{"data": 'USER_LEVEL', "orderable": true},
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
				                    		//columns: [':visible']
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
				                    		//columns: [':visible']
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
			    }
			    
			}

		</script>
<!-- ENDS -->
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

    /* UserGroup Auto Suggestion */
	$('#USER_LEVEL').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LEVEL_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_USERADMIN_FOR_USERACCESS",
				GROUP : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.USERLEVEL);
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
		    return '<p class="item-suggestion">'+ data.LEVEL_NAME +'</p>';
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

	/* UserId Auto Suggestion */
	$('#USERID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'USER_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_USERADMIN_FOR_USERDETAILS",
				USER : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.USERLEVEL);
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
		    return '<p class="item-suggestion">'+ data.USER_ID +'</p>';
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