<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>

<%
String title = "Kit BOM Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- <script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script> -->

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}

function ExportReport(){
	document.form.action = "/track/ReportServlet?action=KITBOMSUMMARY";
	document.form.submit();
	
} 
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",citem="",qty="",eitem="",remark1="",remark2="",fieldDesc="",allChecked="",result="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));
eitem  = strUtils.fString(request.getParameter("EITEM"));
qty  = strUtils.fString(request.getParameter("QTY"));
result = strUtils.fString(request.getParameter("RESULT"));

if(action.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
}
else if(action.equalsIgnoreCase("resulterror"))
{
	fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
	fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
}

if(action.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  allChecked = strUtils.fString(request.getParameter("allChecked"));

  
  
}



%>
<center>
	<h2><small class="success-msg"><%=result%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <div class="box-title pull-right">
              <button type="button" class="btn btn-default" onClick="window.location.href='create_kitbom.jsp'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              </div>
		</div>
		
 <div class="box-body">
 

<FORM class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?" >  
<INPUT type="hidden" name="RFLAG" value="1">
<div id = "ERROR_MSG"></div>
   
 <div id="target" style="display:none"> 
  
              <h2>
              <small>Parent Product Details</small>
              </h2>
		
  <div class="form-group">
  <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-3">
         <div class="input-group"> 
         <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />   
    		<input class="form-control" type="TEXT" size="20" MAXLENGTH=100 name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&TYPE=PARENTITEM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-3" for="Product Description">Product Description</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="DESC" id="DESC" type="TEXT" value="" size="20" MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&TYPE=PARENTITEM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Description Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>
       
       
 <div class="form-group">
   <label class="control-label col-sm-3" for="Product Detail Description">Product Detail Description</label>
      <div class="col-sm-3">
    		<input class="form-control" name="DETDESC" id="DETDESC" type="TEXT" value="" size="20" MAXLENGTH=100 readonly>
  		</div>        
       </div>
       
     
              <h2>
              <small>Child Product Details</small>
              </h2>
		
<div class="form-group">
  <label class="control-label col-sm-3" for="Product ID">Product ID</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" type="TEXT" size="20" MAXLENGTH=100 name="CITEM" id="CITEM" value="<%=citem%>" onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.CITEM.value+'&PITEM='+form.ITEM.value+'&PTYPE='+form.PTYPE.value+'&TYPE=CHILDITEM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-3" for="Product Description">Product Description</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="CDESC" id="CDESC" type="TEXT" value="" size="20" MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.CITEM.value+'&TYPE=CHILDITEM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Description Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div> 
       
       
<div class="form-group">
   <label class="control-label col-sm-3" for="Product Detail Description">Product Detail Description</label>
      <div class="col-sm-3">
    		<input class="form-control" name="CDETDESC" id="CDETDESC" type="TEXT" value="" size="20" MAXLENGTH=100>
  		</div>        
       </div>
         
   <br>
   <br>

<div class="form-group">
  <label class="control-label col-sm-3" for="Equivalent Product ID">Equivalent Product ID</label>
      <div class="col-sm-3">
        <input class="form-control" type="TEXT" size="20" MAXLENGTH=100 name="EITEM" id="EITEM" value="<%=eitem%>" readonly>         
       </div>
<div class="form-inline">
   <label class="control-label col-sm-3" for="Equivalent Product Description">Equivalent Product Description</label>
      <div class="col-sm-3">
       <input class="form-control" name="EDESC" id="EDESC" type="TEXT" value="" style="width: 100%" MAXLENGTH=100 readonly>           
       </div>
       </div>
       </div> 
       
       
<div class="form-group">
   <label class="control-label col-sm-3" for="Equivalent Product Detail Description">Equivalent Detail Description</label>
      <div class="col-sm-3">
    		<input class="form-control" name="EDETDESC" id="EDETDESC" type="TEXT" value="" size="20" MAXLENGTH=100 readonly>
  		</div>        
       </div>
    
    <br>
  <div class="form-group">
  <div class="col-sm-12" align="center">
  <button type="button" class="btn btn-success" value="View" onClick="onGo()">Search</button>&nbsp;&nbsp;
  <button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();">Clear</button>&nbsp;&nbsp;
<!--   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;   -->
   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
  
   </div>
   </div>
   
    
 <br> 
 <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=fieldDesc%></td></tr>
	
</table>
</div>
</div>

<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
         <button type="button" class="btn btn-success" value="View" onClick="onGo()">Search</button>&nbsp;
         <button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();">Clear</button>&nbsp;  
         <!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> --> 
         <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 

<div class="panel panel-default">
<div class="panel-heading" style="background-color: #eaeafa " align="center">
<h3 class="panel-title"><b>Child Product Summary</b></h3> 
</div>
</div>



  <div id="VIEW_RESULT_HERE" class=table-responsive>
 <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tableInventorySummary_info">
					<thead>
		                <tr role="row">
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">No</TH>
       	   <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Prod ID</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Prod Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Prod Detail Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Prod ID</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Prod Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Prod Detail Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Equivalent Prod ID</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Equivalent Prod Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Equivalent Prod Detail Desc</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">BOM QTY</TH>
           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Edit</TH>
          
       </tr>
       </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
  
  <br>
  <div class="form-group">        
      <div class="col-sm-12" align="center">
					<button type="button" class="Submit btn btn-default" value="ExportReport" onClick="javascript:ExportReport();"><b>ExportReport</b></button>&nbsp;&nbsp;
					</div>
					</div>
					
  <input type="hidden" name="PTYPE" id="PTYPE" value="KITBOMSUMMARY">
  

</FORM>




<script>
function onClear()
{
	debugger;
	document.getElementById("ITEM").value = ""
	document.getElementById("DESC").value = ""
	document.getElementById("DETDESC").value = ""
	document.getElementById("CITEM").value = ""
	document.getElementById("CDESC").value = ""
	document.getElementById("CDETDESC").value = ""
	document.getElementById("EITEM").value = ""
	document.getElementById("EDESC").value = ""
	document.getElementById("EDETDESC").value = ""
}
function checkAll(isChk)
{
	var len = document.form.chkitem.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkitem)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkitem.checked = isChk;
              		 
              	}
              	else{
              		document.form.chkitem[i].checked = isChk;
              		 
              	}
            	   	
                
        }
    }
}
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("DESC").value = resultVal.discription;
							document.getElementById("DETDESC").value = resultVal.detaildesc;
							document.form.CITEM.focus();

						} else {
							alert("Not a valid product!");
							document.form.ITEM.value = "";
							document.form.ITEM.focus();
						}
					}
				});
		}
	}
function ValidateChildProduct() {
	var childproduct = document.form.CITEM.value;
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : childproduct,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("CDESC").value = resultVal.discription;
							document.getElementById("CDETDESC").value = resultVal.detaildesc;
							document.form.QTY.focus();

						} else {
							alert("Not a valid child product!");
							document.form.CITEM.value = "";
							document.form.CITEM.focus();
						}
					}
				});
		}
	}
function ValidateEquivalentProduct() {
	var equivalentprod = document.form.EITEM.value;
	if(equivalentprod=="" || equivalentprod.length==0 ) {
		alert("Enter Equivalent Product");
		document.getElementById("EITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : equivalentprod,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("EDESC").value = resultVal.discription;
							document.getElementById("EDETDESC").value = resultVal.detaildesc;
							

						} else {
							alert("Not a valid equivalent product!");
							document.form.EITEM.value = "";
							document.form.EITEM.focus();
						}
					}
				});
		}
	}
function validateQuantity() {
	var qty = document.getElementById("QTY").value;
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("QTY").focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} 

	}
	onAdd();
}
function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for ( var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}


function onAdd() {
	var product = document.form.ITEM.value;
	var childproduct = document.form.CITEM.value;
	var qty = document.form.QTY.value;
	var equiitem = document.form.EITEM.value;
	
	
	if(product=="" || product.length==0 ) {
		alert("Enter Parent Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
		return false;
	}
	if(qty=="" || qty.length==0 ) {
		alert("Enter Quantity Per");
		document.getElementById("QTY").focus();
		return false;
	}
	
	if(product==childproduct ) {
		alert("Child Product cannot be same as Parent Product.Choose diff child product.");
		document.form.CITEM.value = "";
		document.getElementById("CITEM").focus();
		return false;
	}
	if(product==equiitem) {
		alert("Equivalent Product cannot be same as Parent Product.Choose diff equivalent product.");
		document.form.EITEM.value = "";
		document.getElementById("EITEM").focus();
		return false;
	}
	if(childproduct==equiitem) {
		alert("Equivalent Product cannot be same as Child Product.Choose diff equivalent product.");
		document.form.EITEM.value = "";
		document.getElementById("EITEM").focus();
		return false;
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,CITEM:childproduct,QTY:qty,EITEM:equiitem,action: "ADD_KITBOM",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.CITEM.value = "";
    document.form.CDESC.value = "";
    document.form.CDETDESC.value = "";
    document.form.EITEM.value = "";
    document.form.EDESC.value = "";
    document.form.EDETDESC.value = "";
       
}
 
 // Table Design begins

var tableInventorySummary;
var product, pdesc, pdetaildesc, cproduct, cdesc, cdetaildesc, eproduct, edesc, edetaildesc;
function getParameters(){
	return { 
		"ITEM":product,
		"DESC":pdesc,
		"DETDESC":pdetaildesc,
		"CITEM":cproduct,
		"CDESC":cdesc,
		"CDETDESC":cdetaildesc,
		"EITEM":eproduct,
		"EDESC":edesc,
		"EDETDESC":edetaildesc,
		"ACTION": "VIEW_KITBOM_SUMMARY_DETAILS",
		"PLANT":"<%=plant%>"
	}
}  

 
function onGo(index) {

	 index = index;
	 product = document.form.ITEM.value;
	 pdesc= document.form.DESC.value;
	 pdetaildesc= document.form.DETDESC.value;
	 cproduct = document.form.CITEM.value;
	 cdesc = document.form.CDESC.value;
	 cdetaildesc = document.form.CDETDESC.value;
	 eproduct = document.form.EITEM.value;
	 edesc = document.form.EDESC.value;
	 edetaildesc = document.form.EDETDESC.value;

	 /*if(index == '1'){
		if(product=="" || product.length==0 ) {
			alert("Enter Product");
			document.getElementById("ITEM").focus();
			return false;
			}
	     
		}*/	

	 var urlStr = "../ProductionBomServlet";
	   	// Call the method of JQuery Ajax provided
	   	var groupColumn = 1;
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
			        	if(typeof data.kittingbom[0].id === 'undefined'){
			        		return [];
			        	}else {
			        		for(var dataIndex = 0; dataIndex < data.kittingbom.length; dataIndex ++){
			        		data.kittingbom[dataIndex]['EDIT'] = '<a href="/track/jsp/maint_kitbom.jsp?ITEM=' +data.kittingbom[dataIndex]['parentitem']+'&DESC='+data.kittingbom[dataIndex]['pdesc']+ '"><i class="fa fa-pencil-square-o"></i></a>';
			        		
			        		}
			        		return data.kittingbom;
			        	}
			        }
			    },
			    "columns": [
							{"data": 'id', "orderable": true},
			    			{"data": 'parentitem', "orderable": true},
			       			{"data": 'pdesc', "orderable": true},
			    			{"data": 'pdetdesc', "orderable": true},
			    			{"data": 'childitem', "orderable": true},
			    			{"data": 'cdesc', "orderable": true},
			    			{"data": 'cdetdesc', "orderable": true},
			    			{"data": 'eitem', "orderable": true},
			    			{"data": 'edesc', "orderable": true},
			    			{"data": 'edetdesc', "orderable": true},
			    			{"data": 'qty', "orderable": true},
			    			{"data": 'EDIT', "orderable": true}
			    			],
				//"columnDefs": [{"className": "t-right", "targets": [7]}],
				//"orderFixed": [ groupColumn, 'asc' ], 
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
	                            pageSize: 'A4'
		                    }
		                ]
		            },
		            {
	                    extend: 'colvis',
	                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
	                }
		        ],
		        /* "drawCallback": function ( settings ) {
					this.attr('width', '100%');
		            var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalQty = 0;
		            var groupTotalQty = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	if (i > 0) {
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
				                    );
		                	}
		                    last = group;
		                    groupEnd = i;
		                    groupTotalQty = 0;
		                }
		                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
		                totalQty += parseFloat($(rows).eq( i ).find('td:last').html());
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
	                    );
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
	                    );
	                }
		        } *//**/
				
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
		
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     //document.form.TRANSACTIONNO.select();
	 //document.form.TRANSACTIONNO.focus();
      	     
}

function getTable(){
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="95%"  align = "center" bgcolor="navy">'+
        	   '<tr BGCOLOR="#000066">'+
         		'<th width="3%"><font color="#ffffff">No</font></th>'+
         		'<th width="9%"><font color="#ffffff">Parent Prod ID</font></th>'+
         		'<th width="9%"><font color="#ffffff">Parent Prod Desc</font></th>'+
         		'<th width="11%"><font color="#ffffff">Parent Prod Detail Desc</font></th>'+
         		'<th width="9%"><font color="#ffffff">Child Prod ID</font></th>'+
         		'<th width="9%"><font color="#ffffff">Child Prod Desc</font></th>'+
         		'<th width="11%"><font color="#ffffff">Child Prod Detail Desc</font></th>'+
         		'<th width="9%"><font color="#ffffff">Equivalent Prod ID</font></th>'+
         		'<th width="9%"><font color="#ffffff">Equivalent Prod Desc</font></th>'+
         		'<th width="11%"><font color="#ffffff">Equivalent Prod Detail Desc</font></th>'+
         		'<th width="5%"><font color="#ffffff">BOM QTY</font></th>'+
         		'</tr>';
       
}


//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>


</div>
</div>
</div>
 
             <!-- Below Jquery Script used for Show/Hide Function -->
 <script>
 $(document).ready(function(){
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