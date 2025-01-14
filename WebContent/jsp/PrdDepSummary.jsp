<!-- Created By Resvi - 04.10.21  -->
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
String title = "Product Department Summary";
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


<style>
#tabletype>tbody>tr>td {
	text-align: center;
}
</style>

<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
//   function onGo(){
 
//      document.form1.submit();
// } 
 function onView(){
	   document.form1.action  = "../jsp/PrdDepSummary.jsp?action=VIEW";
	   document.form1.submit();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList locQryList  = new ArrayList();


String fieldDesc="";
String PLANT="",RSN ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
// String PGaction         = strUtils.fString(request.getAttribute("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
/* RSN     = strUtils.fString(request.getParameter("PRD_DEP_ID")); */
// fieldDesc=StrUtils.fString(request.getAttribute("result"));
String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
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
displaySummaryLink = ub.isCheckValAcc("summarylnkproductdept", PLANT,USERID);
displaySummaryEdit = ub.isCheckValAcc("editproductdept", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newproductdept", PLANT,USERID);


}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryLink = ub.isCheckValinv("summarylnkproductdept", PLANT,USERID);
displaySummaryEdit = ub.isCheckValinv("editproductdept", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newproductdept", PLANT,USERID);


}

 LocUtil _LocUtil = new LocUtil();
PrdDeptUtil prddeputil = new PrdDeptUtil();

_LocUtil.setmLogger(mLogger);
prddeputil.setmLogger(mLogger);

if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
      if(strUtils.fString(RSN).length() > 0)  RSN=RSN;
      else
    	  RSN= strUtils.fString(request.getParameter("PRD_DEP_ID1"));
      if(fieldDesc.equalsIgnoreCase("Product Department Deleted Successfully")){
    	  locQryList= prddeputil.getPrdTypeList(RSN="",PLANT,"");
      }
      else{
              locQryList= prddeputil.getPrdTypeList(RSN,PLANT,"");}
      
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
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Product Department Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
               <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../productdept/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
             <%}%>
              </div>
		</div>
		
 <div class="box-body">
 
<%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>
	
<form class="form-horizontal" name="form1" method="post" action="PrdDepSummary.jsp">
		<input type="hidden" name="xlAction" value="">
		<input type="hidden" name="PGaction" value="View">
		<input type="hidden" name="PRD_DEP_ID1" value="">
		<input type="hidden" name="PRD_DEP_DESC" value="">
		<!--RESVI-->
		<input type="hidden" name="plant" value="<%=PLANT%>">
        <!-- end -->
		<div class="form-group">
			<label class="control-label col-sm-2" for="Product Dept ID">Product Department ID</label>
			<div class="col-sm-4" >
			<div class="input-group">
				<INPUT name="PRD_DEP_ID" id="PRD_DEP_ID"  type = "TEXT" value="<%=RSN%>" size="50" MAXLENGTH=100  class="form-control"> 
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEP_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				
   		 	
  			</div>	&nbsp;&nbsp;
  			</div>
  			<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>

		</div>
		<INPUT name="ACTIVE" type = "hidden" value="Y"   >
            <INPUT name="ACTIVE" type = "hidden" value="N"   >
	</form>
	
	<br>
	
<div style="overflow-x:auto;">
<table id="tabletype" class="table table-bordred table-striped" > 
   
   <thead> 
          <tr>  
            <th>S/N</th>  
            <th>Product Department ID</th>  
            <th>Product Department Description</th>
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
      
</table>
</div>
<!-- RESVI -->
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var PRODUCTDEPARTMENTID, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"PRODUCTDEPARTMENTID":PRODUCTDEPARTMENTID,
				"PLANT":plant,
				"action":"GET_PRODUCTDEPARTMENTID_FOR_SUMMARY"
			}
		}
		function onGo(){
			PRODUCTDEPARTMENTID=document.form1.PRD_DEP_ID.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#tabletype').DataTable({
						"processing": true,
// 						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].prd_dep_id === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].prd_dep_id;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
												<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../productdept/edit?action=View&PRD_DEP_ID='+lineno+'&PRD_DEP_DESC=' +data.CUSTOMERTYPELIST[dataIndex].prd_dep_desc+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>
												<%if(displaySummaryLink){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['prd_dep_id'] = '<a href="../productdept/detail?action=View&PRD_DEP_ID='+lineno+'&PRD_DEP_DESC=' +data.CUSTOMERTYPELIST[dataIndex].prd_dep_desc+'&ISACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'">'+data.CUSTOMERTYPELIST[dataIndex].prd_dep_id+'</a>';
												<%} %>
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'prd_dep_id', "orderable": true},
				        	{"data": 'prd_dep_desc', "orderable": true},
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
		<!-- END -->  
	  </div>
	  </div>
	  </div>


<script>
$(document).ready(function(){

    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>


<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';

/* Product Class Auto Suggestion */
$('#PRD_DEP_ID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'prd_dep_id',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
			PRODUCTDEPARTMENTID : query
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
return '<p class="item-suggestion">'+ data.prd_dep_id +'</p>';
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
