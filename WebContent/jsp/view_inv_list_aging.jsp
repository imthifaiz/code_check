<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Inventory Summary Aging By Days";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<!-- <script language="JavaScript" type="text/javascript" src="js/general.js"></script> -->
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
//document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_AGING_Reports";
	document.form1.submit();
	
} 



/*  function onGo(){
document.form1.action="view_inv_list_withoutpriceqty.jsp";
document.form1.submit();
}   */
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();



String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_DESCRIP="", QTY ="",PRD_BRAND_ID= "";
String html = "",LOC_TYPE_ID="";
double Total=0;


String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
BATCH   = strUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
//Start code added by Lakshmi for product brand on 11/9/12 
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
//End code added by Lakshmi for product brand on 11/9/12 
LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));


boolean cntRec=false;

%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_list_withoutpriceqty.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
 
  <center><small> <%=fieldDesc%></small></center>
  
  <%-- <center><h1><small> <%=fieldDesc%></small></h1></center> --%>
  
   		<div id="target" style="display:none">
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-2">
      	    <div class="input-group">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+convertCharToString(form1.ITEM.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-2">
        <INPUT class="form-control" name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" style="width: 100%"  MAXLENGTH=100> 
    	</div> 
 		</div> 
 		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Batch">Batch:</label>
        <div class="col-sm-2">
        <div class="input-group">
    		<INPUT class="form-control" name="BATCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BATCH)%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('batch_list_inventory.jsp?BATCH='+convertCharToString(form1.BATCH.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
    	</div> 
 		</div> 
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product Class ID">Product Class ID:</label>
       <div class="col-sm-2">
      	    <div class="input-group">
    	<INPUT class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="supplier name">Product Type ID:</label>
        <div class="col-sm-2">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+convertCharToString(form1.PRD_TYPE_ID.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="Product Brand ID">Product Brand ID:</label>
        <div class="col-sm-2">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+convertCharToString(form1.PRD_BRAND_ID.value)+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		</div>  
 		</div>
          
        <!--   Start code added by Lakshmi for product brand on 11/9/12 -->
		
		<div class="form-group">
       <label class="control-label col-sm-2" for="Location">Location ID/Desc:</label>
       <div class="col-sm-2">
      	    <div class="input-group">
    	<INPUT class="form-control" name="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+convertCharToString(form1.LOC.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Location Type">Location Type ID:</label>
        <div class="col-sm-2">
      	<div class="input-group">
    	<INPUT class="form-control" name="LOC_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" size="30"  MAXLENGTH=20>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+convertCharToString(form1.LOC_TYPE_ID.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>
 		<div class="form-inline">
  	<div class=" col-sm-4">   
  	<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
  	</div>  
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
 		
		 <input type="hidden" name="LOC_DESC" value="">
		
      <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tablePOList_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tablePOList"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tablePOList_info">
					<thead>
		                <tr role="row">
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Location</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Batch</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Total Qty</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">1-30 Days</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">31-60 Days</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">61-90 Days</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">90+ Days</th>
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
  var tablePOList;
  var productId, desc, loc, batch, prdBrand, prdClass, prdType, loctype;
  function getParameters(){
  	return { 
  		"ITEM": productId,"PRD_DESCRIP":desc,"LOC":loc,"BATCH":batch,"PRD_CLS_ID":prdClass,"PRD_TYPE_ID":prdType,
  		"PRD_BRAND_ID":prdBrand,"LOC_TYPE_ID":loctype,"ACTION": "VIEW_INV_SUMMARY_AGING_DAYS","PLANT":"<%=PLANT%>", LOGIN_USER:"<%=USERID%>"
  		}
  }  

function onGo(){
     productId = document.form1.ITEM.value;
     desc = document.form1.PRD_DESCRIP.value;
     loc = document.form1.LOC.value;
     batch = document.form1.BATCH.value;
     prdClass = document.form1.PRD_CLS_ID.value;
     prdType = document.form1.PRD_TYPE_ID.value;
     prdBrand = document.form1.PRD_BRAND_ID.value;
     loctype = document.form1.LOC_TYPE_ID.value;
     var urlStr = "/track/InvMstServlet";
     var groupColumn = 0; 
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
		        	if(typeof data.items[0].item === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
  	        "columns": [
  	        	{"data": 'item', "orderable": true},
  	        	{"data": 'ITEMDESC', "orderable": true},
  	        	{"data": 'loc', "orderable": true},
      			{"data": 'batch', "orderable": true},
      			{"data": 'uom', "orderable": true},
      			{"data": 'sumprdQty', "orderable": true},
  	        	{"data": 'days30', "orderable": true},
  	        	{"data": 'days60', "orderable": true},
      			{"data": 'days90', "orderable": true},
      			{"data": 'days120', "orderable": true}
      			],
  			
  			// "columnDefs": [{"className": "t-right", "targets": [4,5,6,7,8]}],
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
                            pageSize: 'A3'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
			
		});
    }
}

  
   <%--  document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/InvMstServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: { ITEM: productId,PRD_DESCRIP:desc,LOC:loc,BATCH:batch,PRD_CLS_ID:prdClass,PRD_TYPE_ID:prdType,PRD_BRAND_ID:prdBrand,LOC_TYPE_ID:loctype,ACTION: "VIEW_INV_SUMMARY_AGING_DAYS",PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
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
                       
	        	outPutdata = outPutdata+item.INVDETAILS
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
                    '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Total Quantity</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>1-30 days</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>31-60 days</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>61-90 days</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>90+ days</TH>'+                    
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