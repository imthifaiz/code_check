<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Project Summary";
StrUtils strUtils     = new StrUtils();
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String fieldDesc = "", FROM_DATE="", TO_DATE="", PROJECTNO="",VIEWSTATUS="" , PROJECTNAME="";
String msg = (String)request.getAttribute("Msg"); 
VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryExport=false,displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryNew = ub.isCheckValAcc("newproject", plant,USERID);
displaySummaryLink = ub.isCheckValAcc("summarylnkproject", plant,USERID);
displaySummaryEdit = ub.isCheckValAcc("editproject", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportproject", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("newproject", plant,USERID);	
	displaySummaryLink = ub.isCheckValinv("summarylnkproject", plant,USERID);
	displaySummaryEdit = ub.isCheckValinv("editproject", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportproject", plant,USERID);
}
PlantMstDAO plantMstDAO = new PlantMstDAO();
String currency=plantMstDAO.getBaseCurrency(plant);
String collectionDate=du.getDate();
ArrayList al = plantMstDAO.getPlantMstDetails(plant);
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
boolean cntRec=false;
if(VIEWSTATUS.equals(""))
{
	VIEWSTATUS="ByProjectDate";
}
String curDate =du.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.PROJECT%>"/>
<jsp:param name="submenu" value="<%=IConstants.PROJECT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/ProjectSummary.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>               
                <li><label>Project Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
	              <div class="btn-group" role="group">
	              <%if(displaySummaryNew){ %>
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../project/new'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
	              <%}%>
	              </div>
	              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
				</div>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="display:none" style="padding: 18px;">
				<input type="text" name="CUST_CODE" hidden>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" 
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" 
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" class="ac-selected  form-control typeahead" 
									id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon"  -->
<!-- 									onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
							</div>
						</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="PROJECTNO" name="PROJECTNO" 
				  				value="<%=StrUtils.forHTMLTag(PROJECTNO)%>" placeholder="PROJECT NO">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'PROJECTNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
				  			<input type="text" class="ac-selected form-control" id="PROJECTNAME" 
				  				name="PROJECTNAME" value="<%=StrUtils.forHTMLTag(PROJECTNAME)%>" placeholder="PROJECT NAME">	
				  				<span class="select-icon" onclick="$(this).parent().find('input[name=\'PROJECTNAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>						 		
			  			</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="status" id="status" class="ac-selected form-control" 
					  		placeholder="STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
						<div class="col-sm-4 ac-box">  		
				  	<label class=radio-inline>
  					<INPUT name="VIEWSTATUS" type = "radio"  value="ByProjectDate"  id=ByProjectDate <%if(VIEWSTATUS.equalsIgnoreCase("ByProjectDate")) {%>checked <%}%>><b>By Project Date</b>
  					</label>
  					<label class=radio-inline>
  					<INPUT  name="VIEWSTATUS" type = "radio" value="ByExpiryDate"  id = "ByExpiryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByExpiryDate")) {%>checked <%}%>><b>By Expiry Date</b>
  					</label>
				  	</div>
					</div>
	<input type="hidden" value="<%=displaySummaryLink%>" name="displaySummaryLink" id="displaySummaryLink" />
	<input type="hidden" value="<%=displaySummaryExport%>" name="displaySummaryExport" id="displaySummaryExport" />
	<input type="hidden" value="<%=displaySummaryEdit%>" name="displaySummaryEdit" id="displaySummaryEdit" /> 
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">
							<button type="button" class="btn btn-success" 
								onClick="javascript:return onGo();">Search</button>
						</div>
					</div>		
				</div>
				</div>
				<div class="form-group">
      				<div class="col-sm-3">
				      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
				      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
			      	</div>
      				<div class="ShowSingle"></div>
       	  		</div>
       	  		<div id="VIEW_RESULT_HERE" class="table-responsive">
       	  			<div class="row">
       	  				<div class="col-sm-12">
       	  					<table id="tableProjectSummary" class="table table-bordred table-striped">                   
			                   <thead>
									<th style="font-size: smaller;">CUSTOMER</th>
									<th style="font-size: smaller;">PROJECT DATE</th>
									<th style="font-size: smaller;">PROJECT NO</th>									
									<th style="font-size: smaller;">PROJECT NAME</th>
									<th style="font-size: smaller;">EXPIRY DATE</th>
									<th style="font-size: smaller;">ESTIMATE COST</th>
									<th style="font-size: smaller;">NUMBER OF MAN-DAY/HOUR</th>
									<th style="font-size: smaller;">STATUS</th>
									<th style="font-size: smaller;">EDIT</th>
			                   </thead>
			              </table>
       	  				</div>
   	  				</div>
       	  		</div>
			</form>
		</div>
	</div>
</div>
<script>
var tableProjectSummary;
var FROM_DATE,TO_DATE,USER,PROJECTNO,PROJECTNAME,VIEWSTATUS,STATUS, groupRowColSpan = 6;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,"PROJECTNO":PROJECTNO,"PROJECTNAME":PROJECTNAME,"VIEWSTATUS":VIEWSTATUS,"STATUS":STATUS
	}
}  

function onGo(){
   var flag = "false";
    FROM_DATE = document.form1.FROM_DATE.value;
    TO_DATE = document.form1.TO_DATE.value;		    
    USER = document.form1.CUST_CODE.value;
    PROJECTNO = document.form1.PROJECTNO.value;
    PROJECTNAME = document.form1.PROJECTNAME.value;
    STATUS = document.form1.status.value;
    VIEWSTATUS = document.form1.VIEWSTATUS.value;
   var displaySummaryLink = document.form1.displaySummaryLink.value;
   var displaySummaryExport = document.form1.displaySummaryExport.value;
   var displaySummaryEdit = document.form1.displaySummaryEdit.value;
    
    if(FROM_DATE != null     && FROM_DATE != "") { flag = true;} 
   	if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
   	if(USER != null    && USER != "") { flag = true;}
    if(PROJECTNO != null     && PROJECTNO != "") { flag = true;}
    if(PROJECTNAME != null     && PROJECTNAME != "") { flag = true;}
    if(STATUS != null     && STATUS != "") { flag = true;}
    if(VIEWSTATUS != null     && VIEWSTATUS != "") { flag = true;}
    
    var urlStr = "../project/summary";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableProjectSummary){
    	tableProjectSummary.ajax.url( urlStr ).load();
    }else{
    	tableProjectSummary = $('#tableProjectSummary').DataTable({
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
		        	if(typeof data.PROJECT[0] === 'undefined'){
		        		return [];
		        	}else {		
		        		for(var dataIndex = 0; dataIndex < data.PROJECT.length; dataIndex ++){
		        			var lineno = data.PROJECT[dataIndex].PRNO;
		        			if(displaySummaryLink == 'true'){
		        			data.PROJECT[dataIndex]['PRNO'] = '<a href="../project/detail?PRNO='+data.PROJECT[dataIndex]['PRNO']+'">'+data.PROJECT[dataIndex]['PRNO']+'</a>';
		        			}
		        			if(displaySummaryEdit == 'true'){
		        			data.PROJECT[dataIndex]['EDIT'] = '<a href="../project/edit?action=View&PRNO=' +lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
		        			} else {
		        			data.PROJECT[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a>';
		        			}
		        			if(data.PROJECT[dataIndex]['STATUS']=='PROCESSED')
		        				data.PROJECT[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.PROJECT[dataIndex]['STATUS']+'</span>';
		        			else if(data.PROJECT[dataIndex]['STATUS']=='Open')
			        			data.PROJECT[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.PROJECT[dataIndex]['STATUS']+'</span>';
			        		else if(data.PROJECT[dataIndex]['STATUS']=='Draft')
				        		data.PROJECT[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.PROJECT[dataIndex]['STATUS']+'</span>';
			        		else
			        			data.PROJECT[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.PROJECT[dataIndex]['STATUS']+'</span>';	
		        		}
		        		return data.PROJECT;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'CUST_NAME', "orderable": true},
    			{"data": 'DELDATE', "orderable": true},
    			{"data": 'PRNO', "orderable": true},
    			{"data": 'PR_NAME', "orderable": true},
    			{"data": 'EXPIRYDATE', "orderable": true},
    			{"data": 'ESTIMATE_COST', "orderable": true},
    			{"data": 'MANDAY_HOUR', "orderable": true},
    			{"data": 'STATUS', "orderable": true},  
    			{"data": 'EDIT', "orderable": true},
//    			{"data": 'PRESTCOST', "orderable": true},
			],
			/*"columnDefs": [{"className": "t-right", "targets": [6,7]}],*/
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
	    	                	columns: [':visible']
	    	                },
	    	                title: '<%=title%>',
	    	                footer: true
	                    },
	                    {
	                    	extend : 'pdf',
                            footer: true,
	                    	text: 'PDF Portrait',
	                    	exportOptions: {
	                    		columns: [':visible']
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
	                    		columns: [':visible']
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
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
	        "order": [],
	        drawCallback: function() {
	        	if(displaySummaryExport == 'false'){ 
	        	$('.buttons-collection')[0].style.display = 'none';
	        	 } 
	        	}
		});
    }
}

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
