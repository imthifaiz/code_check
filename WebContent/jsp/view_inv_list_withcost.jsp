<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%
String title = "Inventory Summary With Average Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport(){
	 if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
 	}
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invAvgCost";
	document.form1.submit();
	
} 
</script>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();

String fieldDesc="";
String USERID="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_BRAND_ID="",PRD_DESCRIP="", QTY ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String PRD_CLS_ID="",CURRENCYID="",CURRENCYDISPLAY="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOCALEXPENSES="";
boolean flag=false;
String uom = su.fString(request.getParameter("UOM"));
String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
BATCH   = strUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
//Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
//End code added by Deen for product brand on 11/9/12 
FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE    = strUtils.fString(request.getParameter("TO_DATE"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
CURRENCYID= strUtils.fString(request.getParameter("CURRENCYID"));
CURRENCYDISPLAY= strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOCALEXPENSES = strUtils.fString(request.getParameter("LOCALEXPENSES"));
String collectionDate=du.getDate();
String basecurrency=_PlantMstDAO.getBaseCurrency(PLANT);
if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase(""))CURRENCYDISPLAY=basecurrency;
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
boolean cntRec=false;

String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
if(LOCALEXPENSES.equals(""))
{
	LOCALEXPENSES="1";
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_list_withcost.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
    
    <center><small><%=fieldDesc%></small></center>
    
  <%-- <center>
  <h1><small> <%=fieldDesc%></small></h1>
  </center> --%>
  
  <div id="target" style="display:none">
  		<div class="form-group">    	 
  		<label class="control-label col-sm-2" for="Currency ID">
  		<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Currency ID:</label>
        <div class="col-sm-2">
        <div class="input-group">
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
       <INPUT class="form-control" name="CURRENCYDISPLAY" type = "TEXT"  value="<%=CURRENCYDISPLAY%>" size="30"  MAXLENGTH=20>
        <span class="input-group-addon"  onClick="javascript:popUpWin('currencylist.jsp?CURRENCYDISPLAY='+form1.CURRENCYDISPLAY.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
       <INPUT class="form-control datepicker" name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 READONLY>
       </div>
    	</div>
    	</div>
    	<div class="form-inline">
    	 <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-2">
        <div class="input-group" >          
       <INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
        </div>
    	</div>
 		</div>
 		</div>

 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
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
        <INPUT class="form-control" name="BATCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BATCH)%>" style="width: 100%"  MAXLENGTH=40>
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
  		<label class="control-label col-sm-2" for="Product Type">Product Type ID:</label>
       <div class="col-sm-2">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product Brand">Product Brand ID:</label>
        <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div> 
 		</div>
 		 <input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">
 		</div>  
         
         <div class="form-group">
       <label class="control-label col-sm-2" for="Location ID">Location ID/Desc:</label>
       <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>"  size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Location Type">Location Type1/Desc:</label>
        <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>"	size="30" MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Location Type">Location Type2/Desc:</label>
        <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="LOC_TYPE_ID2" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>"	size="30" MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div> 
 		
 		 <div class="form-group">
 		<label class="control-label col-sm-2" for="Model">Select UOM:</label>
        <div class="col-sm-2">
 		 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 100%">
			<option>   </option>
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);					
					for(int i=0 ; i < ccList.size();i++)		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
						
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 <div class="form-inline">
  	<div class="col-sm-offset-1 col-sm-4">   
  	<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
  	</div>
	 </div>	
	 
	 
  	<input type="hidden" name="LOC_DESC" value="">
  	<INPUT name="CURRENCYID" type="hidden" value="<%=CURRENCYID%>" size="1"  MAXLENGTH=80>
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
	<div class="form-group">
      <div class="col-sm-3">
       	  <input type="checkbox" class="form-check-input" name="LOCALEXPENSES" id="withlocalexpenses" value="<%=StrUtils.forHTMLTag(LOCALEXPENSES)%>" Onclick="checkval()" checked />&nbsp;<strong>View By Landed Cost</strong>&nbsp;	
	</div>
	</div>
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordered table-hover dataTable no-footer "
									role="grid" aria-describedby="tableInventorySummary_info">
					<thead>
		                <tr role="row">
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Id</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Loc</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Class ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Type ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Brand ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA Quantity</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory Quantity</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Batch</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Avg Unit Cost</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Avg Unit Cost Per PC</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">List Price</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Qty</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Total Cost</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Total Price</th>
		                </tr>
		            </thead>
		            <tfoot align="right" style="
    display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
 
  </FORM>
  
  <SCRIPT LANGUAGE="JavaScript">
  var tableInventorySummary;
  var productId, desc, loc, prdBrand, prdClass, prdType, fromDt,toDt, batch, currencyId, loctype,loctype2,currencyDisplay, groupRowColSpan = 13,uom,LOCALEXPENSES;
function getParameters(){
	return {
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
		"BATCH":batch,
		"PRD_CLS_ID":prdClass,
		"PRD_TYPE_ID":prdType,
		"PRD_BRAND_ID":prdBrand,
		"CURRENCYID":currencyId,
		"CURRENCYDISPLAY":currencyDisplay,
		"FROM_DATE":fromDt,
		"TO_DATE":toDt,
		"LOC_TYPE_ID":loctype,
		"LOC_TYPE_ID2":loctype2,
		"UOM":uom,
		"LOCALEXPENSES":LOCALEXPENSES,
		"ACTION": "VIEW_INV_SUMMARY_AVG_COST",
		"PLANT":"<%=PLANT%>"
	}
}  


function onGo(){
     productId = document.form1.ITEM.value;
     desc = document.form1.PRD_DESCRIP.value;
     loc = document.form1.LOC.value;
     batch = document.form1.BATCH.value;
     prdClass = document.form1.PRD_CLS_ID.value;
     prdType = document.form1.PRD_TYPE_ID.value;
   // Start code modified by Deen for product brand on 11/9/12  
     prdBrand = document.form1.PRD_BRAND_ID.value;
     currencyId = document.form1.CURRENCYID.value;
     currencyDisplay = document.form1.CURRENCYDISPLAY.value;
     fromDt = document.form1.FROM_DATE.value;
     toDt = document.form1.TO_DATE.value;
     loctype = document.form1.LOC_TYPE_ID.value;
     loctype2 = document.form1.LOC_TYPE_ID2.value;
     uom = document.form1.UOM.value;
     LOCALEXPENSES	= document.form1.LOCALEXPENSES.value; 

     if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
   	}
    if(LOCALEXPENSES != null     && LOCALEXPENSES != "") { flag = true;}
 var urlStr = "../InvMstServlet";
	// Call the method of JQuery Ajax provided
	var groupColumn = 0;
	var totalQty = 0;
	var totalCost = 0;
	var totalPrice = 0;
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
		        			
		        			/*data.items[dataIndex]['TotalCost'] = parseFloat(data.items[dataIndex]['TotalCost']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['TotalPrice'] = parseFloat(data.items[dataIndex]['TotalPrice']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);*/
		        			
		        			data.items[dataIndex]['AVERAGE_COST'] = addZeroes(parseFloat(data.items[dataIndex]['AVERAGE_COST'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['COST_PER_PC'] = addZeroes(parseFloat(data.items[dataIndex]['COST_PER_PC'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['LIST_PRICE'] = addZeroes(parseFloat(data.items[dataIndex]['LIST_PRICE']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['TotalCost'] = addZeroes(parseFloat(data.items[dataIndex]['TotalCost']).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['TotalPrice'] = addZeroes(parseFloat(data.items[dataIndex]['TotalPrice']).toFixed(<%=numberOfDecimal%>));
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
			{"data": 'ITEM', "orderable": true},
 			{"data": 'LOC', "orderable": true},
 			{"data": 'PRDCLSID', "orderable": true},
 			{"data": 'ITEMTYPE', "orderable": true},
 			{"data": 'PRD_BRAND_ID', "orderable": true},
 			{"data": 'ITEMDESC', "orderable": true},
 			{"data": 'STKUOM', "orderable": true},
 			{"data": 'STKQTY', "orderable": true},
			{"data": 'INVENTORYUOM', "orderable": true},
			{"data": 'INVUOMQTY', "orderable": true},
 			{"data": 'BATCH', "orderable": true},
 			{"data": 'AVERAGE_COST', "orderable": true},
 			{"data": 'COST_PER_PC', "orderable": true},
 			{"data": 'LIST_PRICE', "orderable": true},
 			{"data": 'QTY', "orderable": true},
 			{"data": 'TotalCost', "orderable": true},
 			{"data": 'TotalPrice', "orderable": true}
 			],
			"columnDefs": [{"className": "t-right", "targets": [13, 14, 15]}],
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
	                    	extend : 'pdfHtml5',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},	                    	
	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },
	                        <%-- messageBottom: function () { return "<%=collectionDate%>" }, --%>
                 		orientation: 'landscape',
                 		customize: function (doc) {
                 			doc.defaultStyle.fontSize = 16;
                 	        doc.styles.tableHeader.fontSize = 16;
                 	        doc.styles.title.fontSize = 20;
                 	       doc.content[1].table.body[0].forEach(function (h) {
                 	          h.fillColor = '#ECECEC';                 	         
                 	          alignment: 'center'
                 	      });
                 	      doc.styles.tableHeader.color = 'black';
                 	        // Create a footer
                 	        doc['footer']=(function(page, pages) {
                 	            return {
                 	                columns: [
                 	                    '',
                 	                    {
                 	                        // This is the right column
                 	                        alignment: 'right',
                 	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
                 	                    }
                 	                ],
                 	                margin: [10, 0]
                 	            }
                 	        });
                 		},
                         pageSize: 'A2',
                         footer: true
	                    }
	                ]
	            },
	            {
                 extend: 'colvis',
                 columns: ':not(:eq('+groupColumn+')):not(:last)'
             }
	        ],
	        "footerCallback": function ( row, data, start, end, display ) {
	            var api = this.api(), data;
	            var intVal = function ( i ) {
	                return typeof i === 'string' ?
	                    i.replace(/[\$,]/g, '')*1 :
	                    typeof i === 'number' ?
	                        i : 0;
	            };
	         // Total over all pages
	            totalqty = api
	                .column(13)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
	         
	            totalcost = api
                .column(14)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
	            
	            totalprice = api
                .column(15)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
	         // Total over this column 9
	            colTotal_9 = api
	                .column( 10, { page: 'current'} )
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );
	            $( api.column( 12 ).footer() ).html('Total');	            
	            $( api.column( 13 ).footer() ).html(addZeroes(parseFloat(totalqty).toFixed(3)));
	            $( api.column( 14 ).footer() ).html(addZeroes(parseFloat(totalcost).toFixed(2)));
	            $( api.column( 15 ).footer() ).html(addZeroes(parseFloat(totalprice).toFixed(2)));
	        },
			"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var totalCost = 0;
	            var totalPrice = 0;
	            var groupTotalQty = 0;
	            var groupTotalCost = 0;
	            var groupTotalPrice = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
var groupTotalQtyVal=null,groupTotalCostVal=null,groupTotalPriceVal=null;
	                	
	                	if(groupTotalQty==null || groupTotalQty==0){
	                		groupTotalQtyVal="0.000";
	                	}else{
	                		groupTotalQtyVal=addZeroes(parseFloat(groupTotalQty).toFixed(5).replace(/\.?0+$/, ''));
	                	}if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
	                	}else{
	                		/*groupTotalCostVal=parseFloat(groupTotalCost).toFixed(5).replace(/\.?0+$/, '');*/
	                		groupTotalCostVal=addZeroes(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
	                	}if(groupTotalPrice==null || groupTotalPrice==0){
	                		groupTotalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
	                	}else{
	                		/*groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(5).replace(/\.?0+$/, '');*/
	                		groupTotalPriceVal=addZeroes(parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>));
	                	}
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQtyVal + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + groupTotalPriceVal + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                    groupTotalCost = 0;
	                    groupTotalPrice = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', ''));
	                groupTotalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', ''));
	                totalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	var totalQtyVal=null,groupTotalQtyVal=null,totalCostVal=null,groupTotalCostVal=null,totalPriceVal=null,groupTotalPriceVal=null;
	            	if(totalQty==null || totalQty==0){
	            		totalQtyVal="0.000";
                	}else{
                		totalQtyVal=parseFloat(totalQty).toFixed(3);
                	}if(groupTotalQty==null || groupTotalQty==0){
                		groupTotalQtyVal="0.000";
                	}else{
                		groupTotalQtyVal=parseFloat(groupTotalQty).toFixed(3);
                	}if(totalCost==null || totalCost==0){
                		totalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*totalCostVal=parseFloat(totalCost).toFixed(5).replace(/\.?0+$/, '');*/
                		totalCostVal=addZeroes(parseFloat(totalCost).toFixed(<%=numberOfDecimal%>));
                	}if(groupTotalCost==null || groupTotalCost==0){
                		groupTotalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*groupTotalCostVal=parseFloat(groupTotalCost).toFixed(5).replace(/\.?0+$/, '');*/
                		groupTotalCostVal=addZeroes(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
                	}if(totalPrice==null || totalPrice==0){
                		totalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*totalPriceVal=parseFloat(totalPrice).toFixed(5).replace(/\.?0+$/, '');*/
                		totalPriceVal=addZeroes(parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>));
                	}if(groupTotalPrice==null || groupTotalPrice==0){
                		groupTotalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(5).replace(/\.?0+$/, '');*/
                		groupTotalPriceVal=addZeroes(parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>));
                	}
	            	
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalQtyVal + '</td><td class="t-right">' + totalCostVal + '</td><td class="t-right">' + totalPriceVal+ '</td></tr>'
                 );
             	$(rows).eq( currentRow ).after(
             			'<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQtyVal + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + groupTotalPriceVal + '</td></tr>'
                 );
					
             	
             }
	        }
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

$('.buttons-columnVisibility').each(function(){
var $li = $(this),
   $cb = $('<input>', {
           type:'checkbox',
           style:'margin:0 .25em 0 0; vertical-align:middle'}
         ).prop('checked', $(this).hasClass('active') );
$li.find('a').prepend( $cb );
});
	 
$('.buttons-columnVisibility').on('click', 'input:checkbox,li', function(){
var $li = $(this).closest('li'),
   $cb = $li.find('input:checkbox');
$cb.prop('checked', $li.hasClass('active') );
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
            '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Class ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Type ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PC/PCS/EA UOM</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>Average Unit Cost</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>List Price</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Qty</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Cost</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Price</TH>'+
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
 
 function checkval()
 {
	 var checkBox = document.getElementById("withlocalexpenses");
	 if (checkBox.checked == true){		 
		 document.form1.LOCALEXPENSES.value="1";		 
	 }
	 else
		 {		 
		 document.form1.LOCALEXPENSES.value="0";		 
		 }
	 
 }
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>