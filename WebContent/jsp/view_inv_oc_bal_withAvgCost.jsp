<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Inventory Summary With Opening/Closing Stock With Average Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>


<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  //document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function ExportReport()
{
  document.form1.action = "/track/ReportServlet?action=ExportInvOpenClosingQty";
   document.form1.submit();
  
}



</script>
<%@ include file="header.jsp"%>
<%
StrUtils strUtils     = new StrUtils();
DateUtils _dateUtils = new DateUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();



String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="", FROM_DATE="",TO_DATE="", ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="",fdate="",tdate="";
String html = "";
int Total=0;
String PRD_CLS_ID="",PRD_CLS_ID1="",LOC_TYPE_ID="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE    = strUtils.fString(request.getParameter("TO_DATE"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
LOC= strUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID= strUtils.fString(request.getParameter("LOC_TYPE_ID"));
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);


if (FROM_DATE == null)
	FROM_DATE = "";
else
	FROM_DATE = FROM_DATE.trim();
String curDate = _dateUtils.getDate();
if (FROM_DATE.length() < 0 || FROM_DATE == null		|| FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE = curDate;


if (TO_DATE == null)
	TO_DATE = "";
else
	TO_DATE = TO_DATE.trim();


PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_oc_bal.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<INPUT name="INV_REP_TYPE" type = "hidden" value="INVOPENCLS_BYLOC_WAVGCOST">

<center><small><%=fieldDesc%></small></center>

  <%-- <center>
  <h1><small><%=fieldDesc%></small></h1>   
    
  </center> --%>
  
  <div id="target" style="display:none">
  <div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
       <INPUT name="FROM_DATE" type="TEXT" value="<%=FROM_DATE%>"	size="30" MAXLENGTH=20 class="form-control datepicker" READONLY>
        </div>
    	</div>    	
    	<div class="form-inline">
    	 <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group" >          
       <INPUT class="form-control datepicker" name="TO_DATE" type="TEXT" value="<%=TO_DATE%>" size="30" MAXLENGTH=20 class="form-control" READONLY>
        </div>
    	</div>
 		</div>
 		</div>

 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" style="width: 100%"  MAXLENGTH=100> 
    	</div> 
 		</div> 
 		</div>  
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product Class ID">Product Class ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product Type">Product Type ID:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>  
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product Brand ID">Product Brand ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Location ID">Location ID/Desc:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div>
 		 <input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">
 		</div>  
         
         <div class="form-group">
       <label class="control-label col-sm-2" for="Location Type">Location Type ID/Desc:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" size="30" MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
    	</div> 
 	<div class="form-inline">
  	<div class=" col-sm-offset-2 col-sm-4">   
  	<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
  	</div>
  	 <input type="hidden" name="LOC_DESC" value="">
  	</div>
  	
<center>
<font><i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;<b>Note : This report may not be accurate, if you import inventory stock on hand through excel.</b></font>
   </center>
    
    </div>
    
      <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
     <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	 <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	 <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
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
          <TH style="background-color: #eaeafa; color:#333; ">Loc</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Product ID</TH>
       	  <TH style="background-color: #eaeafa; color:#333; ">Description</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Class ID</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Type ID</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Brand ID</TH>
          <TH style="background-color: #eaeafa; color:#333; ">PC/PCS/EA UOM</TH>
           <TH style="background-color: #eaeafa; color:#333; ">Opening Qty</TH>
       	  <TH style="background-color: #eaeafa; color:#333; ">Total Received</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Transfer In</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Out</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Total Issued</TH>
           <TH style="background-color: #eaeafa; color:#333; ">Closing Qty</TH>
            <TH style="background-color: #eaeafa; color:#333; ">Avg Unit Cost</TH>
            <TH style="background-color: #eaeafa; color:#333; ">List Price</TH>
            <TH style="background-color: #eaeafa; color:#333; ">Total Cost</TH>
            <TH style="background-color: #eaeafa; color:#333; ">Total Price</TH>
       	  <TH style="background-color: #eaeafa; color:#333; ">Later Recv</TH>
          <TH style="background-color: #eaeafa; color:#333; ">Transfer in</TH>
           <TH style="background-color: #eaeafa; color:#333; ">Out</TH>
            <TH style="background-color: #eaeafa; color:#333; ">Issue </TH>
            <TH style="background-color: #eaeafa; color:#333; ">Stock On Hand</TH>
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
  var productId, desc, prdBrand, prdClass, prdType, fromDt, toDt, loc, loctype,  groupRowColSpan = 10;
function getParameters(){
	return { 
		
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"PRD_BRAND_ID":prdBrand,
		"PRD_CLS_ID":prdClass,
		"PRD_TYPE_ID":prdType,
		"FROM_DATE":fromDt,
		"TO_DATE":toDt,
		"LOC":loc,
		"LOC_TYPE_ID":loctype,
		
		"ACTION": "VIEW_OPEN_CLOSE_BAL_INV_WAVGCOST",
		"PLANT":"<%=PLANT%>"
	}
}  

function onGo(){
	 fromDt = document.form1.FROM_DATE.value;
     toDt = document.form1.TO_DATE.value;
     productId = document.form1.ITEM.value;
     desc = document.form1.PRD_DESCRIP.value;
     prdClass = document.form1.PRD_CLS_ID.value;
     prdType = document.form1.PRD_TYPE_ID.value;
     prdBrand = document.form1.PRD_BRAND_ID.value;
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
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
							for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        					        			
		        			/*data.items[dataIndex]['AVERAGE_COST'] = parseFloat(data.items[dataIndex]['AVERAGE_COST']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['LIST_PRICE'] = parseFloat(data.items[dataIndex]['LIST_PRICE']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['TotalCost'] = parseFloat(data.items[dataIndex]['TotalCost']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['TotalPrice'] = parseFloat(data.items[dataIndex]['TotalPrice']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);*/
		        			
		        			data.items[dataIndex]['AVERAGE_COST'] = addZeroes(parseFloat(data.items[dataIndex]['AVERAGE_COST']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['LIST_PRICE'] = addZeroes(parseFloat(data.items[dataIndex]['LIST_PRICE']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['TotalCost'] = addZeroes(parseFloat(data.items[dataIndex]['TotalCost']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['TotalPrice'] = addZeroes(parseFloat(data.items[dataIndex]['TotalPrice']).toFixed(<%=numberOfDecimal%>));
		        			
		        			
		        			
		        		}
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'LOC', "orderable": true},
  			{"data": 'ITEM', "orderable": true},
  			{"data": 'ITEMDESC', "orderable": true},
  			{"data": 'PRD_CLS_ID', "orderable": true},
  			{"data": 'ITEMTYPE', "orderable": true},
  			{"data": 'PRD_BRAND_ID', "orderable": true},
  			{"data": 'UOM', "orderable": true},
  			{"data": 'OPENING', "orderable": true},
  			{"data": 'TOTRECV', "orderable": true},
  			{"data": 'TOTLTRECV', "orderable": true},
  			{"data": 'TOTLTISS', "orderable": true},
  			{"data": 'TOTAL_ISS', "orderable": true},
  			{"data": 'CLOSING', "orderable": true},
  			{"data": 'AVERAGE_COST', "orderable": true},
  			{"data": 'LIST_PRICE', "orderable": true},
  			{"data": 'TotalCost', "orderable": true},
  			{"data": 'TotalPrice', "orderable": true},
  			{"data": 'RECVED_AFTER', "orderable": true},
  			{"data": 'CTOTLTRECV', "orderable": true},
  			{"data": 'CTOTLTISS', "orderable": true},
  			{"data": 'ISSUED_AFTER', "orderable": true},
  			{"data": 'STOCKONHAND', "orderable": true},
  			],
			"columnDefs": [{"className": "t-right", "targets": [20]}],
			"orderFixed": [ groupColumn, 'asc' ], 
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
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                  		orientation: 'landscape',
                          pageSize: 'A2'
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
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
            		'<TR><td colspan =20 align=right ><b>**Note : This report may not be accurate, if you import inventory stock on hand through excel.</b></td><TR>'+
                    '<TR BGCOLOR="#000066">'+
                    '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product Class ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product Type ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Opening Qty</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total Recved</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Transfer In</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Out</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total Issued</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Closing Qty</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Average Unit Cost</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>List Price</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Total Cost</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Total Price</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Later Recv</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Transfer In</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Out</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Issue</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Stock On Hand</TH>'+
                  
                    '</TR>';
                
}

   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
 onGo();
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>