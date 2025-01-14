<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Generate Inventory Incoming Vs Outgoing (by Product, date & Overall)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>

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
  document.form1.action = "/track/ReportServlet?action=ExportInvVsOutgoing";
   document.form1.submit();

  
}



</script>

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



%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="Goodsreceiveissuesummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
 
  <center><small><%=fieldDesc%></small></center>
  
  <%-- <center><h1><small><%=fieldDesc%></small></h1></center> --%>
  
  <div id="target" style="display:none">
  		<div class="form-group">
        <label class="control-label col-sm-2" for="From_Date">From_Date:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="form-control datepicker" name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 readonly>
   		 	</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="To_Date">To_Date:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 readonly> 
        </div>
 		</div> 
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+convertCharToString(form1.ITEM.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?DESC='+convertCharToString(form1.PRD_DESCRIP.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product Class ID">Product Class ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+convertCharToString(form1.PRD_CLS_ID.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product Type">Product Type ID:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+convertCharToString(form1.PRD_TYPE_ID.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Product Brand">Product Brand ID:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+convertCharToString(form1.PRD_BRAND_ID.value)+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div> 
  		<div class="form-inline">
  		<div class="col-sm-offset-2 col-sm-4">   
  		<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  			<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  		</div>
  		</div>
  		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		<INPUT name="ACTIVE" type = "hidden" value="">
 		</div>
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
        
      <INPUT name="INV_REP_TYPE" type = "hidden" value="INVOPENCLSBYPRODUCT">
    
  <div id="VIEW_RESULT_HERE" class="table-responsive">
      <table id="tablePOList"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tablePOList_info">
					<thead>
		                <tr role="row">
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Total Received</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Total Issued</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Stock On Hand</th>
		                	</tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
      
  </FORM>

  <SCRIPT LANGUAGE="JavaScript">
  var tablePOList;
  var productId, desc, prdBrand, prdClass, prdType, fromDt, toDt;
  function getParameters(){
  	return { 
  		"ITEM": productId,"PRD_DESCRIP":desc,"PRD_BRAND_ID":prdBrand,"PRD_CLS_ID":prdClass,"PRD_TYPE_ID":prdType,"FROM_DATE":fromDt,"TO_DATE":toDt, 
  		"ACTION": "VIEW_INV_VS_OUTGOING","PLANT":"<%=PLANT%>"
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
     var urlStr = "/track/InvMstServlet";
     if (tablePOList){
  	   tablePOList.ajax.url( urlStr ).load();
     }else{
  	   tablePOList = $('#tablePOList').DataTable({
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
  		        			data.items[dataIndex]['SNO'] = dataIndex + 1;
  		        			
  				data.items[dataIndex]['ITEM'] = '<a href="/track/jsp/prdRIDetails.jsp?ITEM=' +data.items[dataIndex]['ITEM']+ '&FROM_DATE='+fromDt+'&TO_DATE='+toDt+'">' +data.items[dataIndex]['ITEM']+ '</a>';
  		        			//"<a href=\"" + "prdRIDetails.jsp?ITEM=" + strUtils.replaceCharacters2Str(item) +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+"\">";
  		        		}
  		        		return data.items;
  		        	}
  		        }
  		    },
  	        "columns": [
  	        	{"data": 'ITEM', "orderable": false},
  	        	{"data": 'ITEMDESC', "orderable": false},
  	        	{"data": 'UOM', "orderable": false},
  	        	{"data": 'TOTRECV', "orderable": false},
      			{"data": 'TOTAL_ISS', "orderable": false},
      			{"data": 'STOCKONHAND', "orderable": false}
      			],
  			//"columnDefs": [{"className": "t-right", "targets": [6, 7]}],
  			/* "orderFixed": [ groupColumn, 'asc' ],  */
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
                              pageSize: 'A3'
  	                    }
  	                ]
  	            },
  	            {
                      extend: 'colvis',
                      /* columns: ':not(:eq('+groupColumn+')):not(:last)' */
                  }
  	        ],
  			"drawCallback": function ( settings ) {
  			}
  		});
     }	   
     //$.ajax({type: "POST",url: urlStr, data: {},dataType: "json", success: callback });

  }
     
    <%--  document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/InvMstServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: { ITEM: productId,PRD_DESCRIP:desc,PRD_BRAND_ID:prdBrand,PRD_CLS_ID:prdClass,PRD_TYPE_ID:prdType,FROM_DATE:fromDt,TO_DATE:toDt, ACTION: "VIEW_INV_VS_OUTGOING",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });

}
 --%>

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
                    '<TR BGCOLOR="#000066">'+
                    '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total Received</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total Issued</TH>'+
                     '<TH><font color="#ffffff" align="Right"><b>Stock On Hand</TH>'+
                    '</TR>';
                
} 

   

              
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
