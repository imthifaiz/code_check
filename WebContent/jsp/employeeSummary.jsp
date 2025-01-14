<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- ifstart -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- ifend -->
<%
String title = "Employee Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EMPLOYEE%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 
/* function onGo(){
	document.form.action ="employeeSummary.jsp";
   document.form.submit();
} */

function ExportReport(){
	document.form.action = "/track/ReportServlet?action=Export_Employee_Excel";
	document.form.submit();
	
}

function ExportLeaveDetailsReport(){
	document.form.action = "/track/ReportServlet?action=Export_Employee_Leave_Excel";
	document.form.submit();
	
}

function ExportSalaryDetailsReport(){
	document.form.action = "/track/ReportServlet?action=Export_Employee_Salary_Excel";
	document.form.submit();
	
}
</script>

<%


StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean    = new userBean();

List QryList  = new ArrayList();
_userBean.setmLogger(mLogger);
String fieldDesc="";
String PLANT="",CUST_NAME ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
CUST_NAME  = strUtils.fString(request.getParameter("CUST_NAME1"));
PLANT= session.getAttribute("PLANT").toString();  
/* ifstart */
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
/* ifend */
//user access rights 
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
// created by imtiziaf
boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryEdit=false,displaySummaryExportMasterData=false,displaySummaryExportLeaveDetails=false,displaySummaryExportSalaryDetails=false,displaySummaryImportSalaryType=false,displaySummaryImportMasterData=false,displaySummaryImportLeaveType=false;
if(systatus.equalsIgnoreCase("PAYROLL")) {
	displaySummaryNew = ub.isCheckValPay("pnewemployee", PLANT,username);
	displaySummaryLink = ub.isCheckValPay("psummarylnkemployee", PLANT,username);
	displaySummaryEdit = ub.isCheckValPay("peditemoloyee", PLANT,username);
	displaySummaryImportSalaryType = ub.isCheckValPay("pimportemployeesalarytype", PLANT,username);
	displaySummaryImportMasterData = ub.isCheckValPay("pimportemployee", PLANT,username);
	displaySummaryImportLeaveType = ub.isCheckValPay("pimportemployeeleavetype", PLANT,username);
	displaySummaryExportMasterData = ub.isCheckValPay("pexportmasterdata", PLANT,username);
	displaySummaryExportLeaveDetails = ub.isCheckValPay("pexportleavedetails", PLANT,username);
	displaySummaryExportSalaryDetails = ub.isCheckValPay("pexportsalarydetails", PLANT,username);
	
}
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryNew = ub.isCheckValAcc("pnewemployee", PLANT,username);
	displaySummaryEdit = ub.isCheckValAcc("peditemoloyee", PLANT,username);
	displaySummaryImportMasterData = ub.isCheckValAcc("pimportemployee", PLANT,username);
	displaySummaryExportMasterData = ub.isCheckValAcc("pexportmasterdata", PLANT,username);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("pnewemployee", PLANT,username);
	displaySummaryEdit = ub.isCheckValinv("peditemoloyee", PLANT,username);
	displaySummaryImportMasterData = ub.isCheckValinv("pimportemployee", PLANT,username);
	displaySummaryExportMasterData = ub.isCheckValinv("pexportmasterdata", PLANT,username);
}
//end 


CUST_NAME     = strUtils.fString(request.getParameter("CUST_NAME1"));
fieldDesc=StrUtils.fString(request.getParameter("result"));
EmployeeUtil custUtil = new EmployeeUtil();
custUtil.setmLogger(mLogger);
if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
     /*  QryList= custUtil.getEmployeeListStartsWithName(CUST_NAME,PLANT);
      if(QryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      } */
                 
     
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
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                <li><label>Employee Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              
              <div class="btn-group" role="group">
              <!-- //created by imtiziaf// -->  
              <% if (displaySummaryExportMasterData) { %>
              			 <button type="button" class="btn btn-default" data-toggle="dropdown" >Export <span class="caret"></span></button>  
              			 <% } else { %> 
              			 <% if (displaySummaryExportLeaveDetails){ %>
              			 <button type="button" class="btn btn-default" data-toggle="dropdown" >Export <span class="caret"></span></button>
              			 <% }  else {%> 
              			 <% if (displaySummaryExportSalaryDetails){ %>
              			 <button type="button" class="btn btn-default" data-toggle="dropdown" >Export <span class="caret"></span></button>              			            			 
                      	<%  }  else {%> 
                      	<button hidden="button" class="#" data-toggle="dropdown" >Export <span class="caret"></span></button> 
                      	<% } } } %>
                      	
              			 <!-- //end -->
              <ul class="dropdown-menu" style="min-width: 0px;">
              <!-- imtiziaf -->
               <% if (displaySummaryExportMasterData) { %>
              <li id="Export-product"><a href="javascript:ExportReport();">Employee Master</a></li>
              <% } %>
              <!-- end -->
              <% if (displaySummaryExportLeaveDetails) { %>
               <li id="Export-supplier"><a href="javascript:ExportLeaveDetailsReport();">Employee Leave Type</a></li>
               <% } %>
               <% if (displaySummaryExportSalaryDetails) { %>
                <li id="Export-customer"><a href="javascript:ExportSalaryDetailsReport();">Employee Salary Type</a></li>
                <% } %>
                </ul>
					&nbsp;
				</div>
				<div class="btn-group" role="group">
				<!-- <button type="button" class="btn btn-default"
						onClick="window.location.href='importEmployeeSalaryDetExcelSheet.jsp'">
						Import Employee Salary Type</button> -->
						 <% if (displaySummaryImportMasterData) { %>
              			  <button type="button" class="btn btn-default" data-toggle="dropdown" >Import <span class="caret"></span></button>	
              			 <% } else { %> 
              			 <% if (displaySummaryImportLeaveType){ %>
              			  <button type="button" class="btn btn-default" data-toggle="dropdown" >Import <span class="caret"></span></button>	
              			 <% }  else {%> 
              			 <% if (displaySummaryImportSalaryType){ %>
              			  <button type="button" class="btn btn-default" data-toggle="dropdown" >Import <span class="caret"></span></button>	            			            			 
                      	<%  }  else {%> 
                      	<button hidden="button" class="#" data-toggle="dropdown" >Import <span class="caret"></span></button> 
                      	<% } } } %>
											 
              <ul class="dropdown-menu" style="min-width: 0px;">
                <% if (displaySummaryImportMasterData) { %>
              <li id="Export-product"><a href='../payroll/importemployee'>Employee Master</a></li>
              <% } %>
              <% if (displaySummaryImportLeaveType) { %>
               <li id="Export-supplier"><a href='../payroll/importempleave'>Employee Leave Type</a></li>
               <% } %>
                <% if (displaySummaryImportSalaryType) { %>
                <li id="Export-customer"><a href='../payroll/importempsalary'>Employee Salary Type</a></li>
                <% } %>
                </ul>
					&nbsp;
				</div>
				<div class="btn-group" role="group">
				 <% if (displaySummaryNew) { %>
					<button type="button" class="btn btn-default"
						onClick="window.location.href='../payroll/createemployee'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
					<% } %> 
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
 
	
<FORM class="form-horizontal" name="form" method="post" action="../payroll/employee">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUST_CODE" value="">
<input type="hidden" name="L_CUST_NAME" value="">
<input type="hidden" name="ReportType" value="Employee">
<!-- ifstart -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- ifend -->

<div class="form-group">
<label class="control-label col-form-label col-sm-2" for="Employee Name or ID">Employee Name or ID</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
<INPUT id="CUST_NAME1" name="CUST_NAME1" type = "TEXT" value="<%=CUST_NAME%>" size="20"  MAXLENGTH=100 class="ac-selected form-control">
<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUST_NAME1\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="input-group-addon" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);">
 <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 <!--<span class="btn-danger input-group-addon"
								 onclick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span> -->
								
  	</div>	&nbsp;&nbsp;
  	</div>
  &nbsp;&nbsp;<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  <!-- <button type="button" class="Submit btn btn-default" value='Export Master Data'  onclick="ExportReport();">Export Master Data</button>&nbsp;&nbsp;
  <button type="button" class="Submit btn btn-default" value='Export Leave Details Data'  onclick="ExportLeaveDetailsReport();">Export Leave Details</button>&nbsp;&nbsp;
  <button type="button" class="Submit btn btn-default" value='Export Salary Details Data'  onclick="ExportSalaryDetailsReport();">Export Salary Details</button>&nbsp;&nbsp; -->
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
   <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Employee ID</th>
            <th style="font-size: smaller;">Employee Name</th>
            <th style="font-size: smaller;">Department</th>
            <th style="font-size: smaller;">Designation</th>
            <th style="font-size: smaller;">Employee Email</th>
            <th style="font-size: smaller;">Employee Phone</th>
            <th style="font-size: smaller;">Edit</th>    
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
								<th></th>
							</tr>
						</tfoot>
        
      <%--   <tbody>  
  
 
 
   	  <%
          
          for (int iCnt =0; iCnt<QryList.size(); iCnt++)
          {
			int iIndex = iCnt + 1;
          	String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          	 Map lineArr = (Map) QryList.get(iCnt);

       %>

          <TR>
            <TD class="textbold">&nbsp;<%=iIndex%></TD>
            <% if (displaySummaryLink) { %>
            <TD class="textbold">&nbsp; <a href="maint_employee.jsp?action=VIEWDATA&CUST_CODE=<%=(String) lineArr.get("EMPNO")%>"%><%=strUtils.removeQuotes((String)lineArr.get("EMPNO"))%></a></TD>
           <% } else { %>
           <TD class="textbold">&nbsp; <%=strUtils.removeQuotes((String)lineArr.get("EMPNO"))%></TD>
           <% }%>
           <TD class="textbold">&nbsp; <%=strUtils.removeQuotes((String)lineArr.get("FNAME"))%></TD>
           <TD class="textbold">&nbsp; <%=strUtils.removeQuotes((String)lineArr.get("DEPT"))%></TD>
           <TD class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("DESGINATION"))%></TD>
           <TD class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("EMAIL"))%></TD>
           <TD class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("TELNO"))%></TD>
           <% if (displaySummaryEdit) { %>
           <TD class="textbold">&nbsp; <a href="maint_employee.jsp?action=View&CUST_CODE=<%=(String) lineArr.get("EMPNO")%>"%>
		    <i class="fa fa-pencil-square-o"></i></a></TD>
		    <% } else { %>
		    <TD class="textbold"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
		    <% } %>
          </TR>
       <%}%>
      </tbody>  --%> 
</table>
</div> 
<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tabletype;
		var CUSTOMERNAME, groupRowColSpan = 6;
		
		function getParameters(){
			return {				
				"CUSTOMERNAME":CUSTOMERNAME,
				"PLANT":plant,
				"action":"GET_EMPLOYEE_FOR_SUMMARY"
			}
		}
		function onGo(){		
			CUSTOMERNAME=document.form.CUST_NAME1.value;
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].EMPNO === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].EMPNO;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../payroll/editemployee?action=View&CUST_CODE='+lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'EMPNO', "orderable": true},
				        	{"data": 'FNAME', "orderable": true},
				        	{"data": 'DEPT', "orderable": true},
				        	{"data": 'DESGINATION', "orderable": true},
				        	{"data": 'EMAIL', "orderable": true},
				        	{"data": 'TELNO', "orderable": true},				        	
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
				                    		columns: [0,1,2,3,4,5,6]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5,6]
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
				                    		columns: [0,1,2,3,4,5,6]
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
/* 	 $('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	});  */
    onGo(); 
    $('[data-toggle="tooltip"]').tooltip();
    
    var plant= '<%=PLANT%>';
	/* Employee Type Auto Suggestion */
	$('#CUST_NAME1').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'EMPNO',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getEmployeeListStartsWithName",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.EMP_MST);
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
		    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>