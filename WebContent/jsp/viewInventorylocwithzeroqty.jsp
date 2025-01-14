<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Inventory Summary Location With Zero Quantity";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<!-- 
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script> -->
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<script language="javascript">

var subWin = null;
function popUpWin(URL) {
 // document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Loc_Report_ZeroQty";
	document.form1.submit();
	
} 

/* 
function onGo(){
document.form1.action="viewInventorylocwithzeroqty.jsp";
document.form1.submit();
} */
</script>
<%@ include file="header.jsp"%>
<!-- <title>Inventory Summary loc with zero qty</title>
</head>
<link rel="stylesheet" href="css/style.css">
 -->
 <%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();



String fieldDesc="";
String USERID ="",PLANT="",LOC ="",QTY ="";
String html = "";
int Total=0;
String SumColor="",LOC_TYPE_ID="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC = strUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));

boolean cntRec=false;


%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="viewInventorylocwithzeroqty.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 
  <center><small><%=fieldDesc%></small></center>
  
  <%-- <center><h1><small> <%=fieldDesc%></small></h1></center> --%>
  
  
  		<div class="form-group">
       <label class="control-label col-sm-2" for="Location ID">Location ID/Desc:</label>
       <div class="col-sm-2">
       <div class="input-group">
       <INPUT class="form-control" name="LOC" type = "TEXT" value="<%=LOC%>" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	  </div>
      </div>
  	  <div class="form-inline">
      <label class="control-label col-sm-2" for="Location Type">Location Type ID/Desc:</label>
      <div class="col-sm-2">
      <div class="input-group">
      <INPUT class="form-control" name="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>"	size="30" MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	  </div>
  	  </div>
  	  <input type="hidden" name="LOC_DESC" value="">
  	  </div>
  	  <div class="form-inline">
  	  <div class=" col-sm-4">
      <button type="button" class="Submit btn btn-default"  onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
      <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;	  
      <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
      </div>
 	  </div> 
  	  </div>   
    
 
   <div id="VIEW_RESULT_HERE" class="table-responsive">
 <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tableInventorySummary_info">
			<thead>
		  <tr role="row">
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Location</TH>
       	  <TH style="background-color: #eaeafa; color:#333; text-align: center;">Location Description</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Location Type</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Location Type Description</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Quantity</TH>
          </tr>
       	  </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
 
    
  </FORM>

  <SCRIPT LANGUAGE="JavaScript">
  var tableInventorySummary;
  var loc, loctype, groupRowColSpan = 3;
function getParameters(){
	return { 
		
		"LOC":loc,
		
		"LOC_TYPE_ID":loctype,
		
		"ACTION": "VIEW_INV_SUMMARY_LOC_ZEROQTY",
		"PLANT":"<%=PLANT%>"
	}
}  


function onGo(){
      loc = document.form1.LOC.value;
     loctype = document.form1.LOC_TYPE_ID.value;

    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 0;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].loc === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['quantity'] = 0;
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'loc', "orderable": true},
    			{"data": 'locdesc', "orderable": true},
       			{"data": 'loc_type_id', "orderable": true},
    			{"data": 'loc_type_desc', "orderable": true},
    			{"data": 'UOM', "orderable": true},
    			{"data": 'quantity', "orderable": false}
    			],
			"columnDefs": [{"className": "t-right", "targets": [5]}],
			"orderFixed": [ groupColumn, 'asc' ], 
			/* "dom": 'lBfrtip', */
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
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A3'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ]
		});
    }
}

$('#tableInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummary').attr('width', '100%');
});
 

function callback(data){
		
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
			}
		});
		
	if(!errorBoo){
		
        $.each(data.items, function(i,item){
        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                   
        	outPutdata = outPutdata+item.PRODUCT
        	ii++;

          });
	}else{
	//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
	}
    outPutdata = outPutdata +'</TABLE>';
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML ='';

 
}

function getTable(){
            return '<TABLE WIDTH="70%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
                    '<TR BGCOLOR="#000066">'+
                    '<TH><font color="#ffffff" align="left" width = "15%"><b>Location</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Location Type</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Quantity</TH>'+
                    '</TR>';
                
}

   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div>
</div>
</div>
 <script>
 $(document).ready(function(){
 onGo();
 });
 </script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
