<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = " Bill Of Materials Formula Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
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
if(action.equalsIgnoreCase(""))
	action=  (String)(request.getAttribute("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));
eitem  = strUtils.fString(request.getParameter("EITEM"));
qty  = strUtils.fString(request.getParameter("QTY"));
result = strUtils.fString(request.getParameter("RESULT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryNew=false,displaySummaryExport=false,displaySummaryEdit=false;
if(systatus.equalsIgnoreCase("INVENTORY")) {
	displaySummaryNew = ub.isCheckValinv("kittingNew", plant,sUserId);
	displaySummaryExport = ub.isCheckValinv("kittingExport", plant,sUserId);
	displaySummaryEdit = ub.isCheckValinv("kittingEdit", plant,sUserId);
}

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
                <div class="pull-right">
                <div class="btn-group" role="group">
                <% if (displaySummaryExport) { %>
                <button type="button" class="Submit btn btn-default" value="ExportReport" onClick="javascript:ExportReport();">Export Detail Report</button>&nbsp;&nbsp;
                <%}%>
                </div>
                <div class="btn-group" role="group">
                <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../billofmaterials/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New/Edit</button>&nbsp;
              <%}%>
              </div>
              </div>
		</div>
		
 <div class="container-fluid">
 

<FORM class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?" >  
<INPUT type="hidden" name="RFLAG" value="1">
<input type="hidden" name="EDESC" id="EDESC" value="" >
<input type="hidden" name="EDETDESC" id="EDETDESC" value="" >
<input type="hidden" name="CDETDESC" id="CDETDESC" value="" >
<input type="hidden" name="CDESC" id="CDESC" value="" >
<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
<INPUT type="hidden" name="plant" value=<%=plant%>>
<div id = "ERROR_MSG"></div>
   
 <div id="target" style="display:none"> 
 
 <div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
 <div class="col-sm-4 ac-box">          
            
    		<input class="ac-selected form-control" type="TEXT" size="20" MAXLENGTH=100 name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}" placeholder="Parent Product">
    		<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value+'&TYPE=PARENTITEM');"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->   		 	
  		</div>
  		<div class="col-sm-4 ac-box" style="display: none">
    		<input class="ac-selected form-control" type="TEXT" size="20" MAXLENGTH=100 name="CITEM" id="CITEM" value="<%=citem%>" onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}" placeholder="Child Product">
    		<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.CITEM.value+'&PITEM='+form.ITEM.value+'&PTYPE='+form.PTYPE.value+'&TYPE=CHILDITEM');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
   		 </div>	
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box" style="display: none">
  		<input class="form-control" type="TEXT" size="20" MAXLENGTH=100 name="EITEM" id="EITEM" value="<%=eitem%>" readonly placeholder="Equivalent Product">
  		</div>
  		<div class="col-sm-4">
  		<button type="button" class="btn btn-success" value="View" onClick="onGo()">Search</button>&nbsp;&nbsp;
  		</div>
  		</div>
  		
 </div>

   </div>
   
    
 <br> 
 <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=fieldDesc%></td></tr>
	
</table>
</div>



<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
         
  	</div>
        </div>
       	  </div> 

  <div id="VIEW_RESULT_HERE" class=table-responsive>
 <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              
                <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div>
              	<table id="tableInventorySummary" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
           <TH style="background-color: #ffffff ; color:#333; text-align: left !important;">No</TH>
       	   <TH style="background-color: #ffffff ; color:#333; text-align: left !important;">Parent Product</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: left !important;">Parent Product Desc</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: left !important;">Parent Product Detail Desc</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: left !important;">Parent UOM</TH>
           <!-- <TH style="background-color: #ffffff ; color:#333; text-align: center;">Child Product Desc</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Child Product Detail Desc</TH> -->
           <!-- <TH style="background-color: #ffffff ; color:#333; text-align: center;">Equivalent Product</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Equivalent Product Desc</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Equivalent Product Detail Desc</TH> -->
           <!-- <TH style="background-color: #ffffff ; color:#333; text-align: center;">BOM QTY</TH> 
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Edit</TH>-->
          
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
					
					</div>
					</div>
					
  <input type="hidden" name="PTYPE" id="PTYPE" value="KITBOMSUMMARY">
  

</FORM>




<script>
function onClear()
{
	debugger;
	document.getElementById("ITEM").value = "";
	document.getElementById("CITEM").value = "";
	document.getElementById("EITEM").value = "";
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
	
		}
	}
function ValidateChildProduct() {
	var childproduct = document.form.CITEM.value;
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}
	}
function ValidateEquivalentProduct() {
	var equivalentprod = document.form.EITEM.value;
	if(equivalentprod=="" || equivalentprod.length==0 ) {
		alert("Enter Equivalent Product");
		document.getElementById("EITEM").focus();
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
    document.form.EITEM.value = "";
       
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
		"ACTION": "VIEW_PARENT_KITBOM_SUMMARY_DETAILS",
		"PLANT":"<%=plant%>"
	}
}  

 
function onGo(index) {

	 index = index;
	 product = document.form.ITEM.value;
	
	 cproduct = document.form.CITEM.value;
	
	 eproduct = document.form.EITEM.value;
	

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
				"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

				searching: true, // Enable searching
		        search: {
		            regex: true,   // Enable regular expression searching
		            smart: false   // Disable smart searching for custom matching logic
		        },
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
			        			<% if (displaySummaryEdit) { %>
			        		data.kittingbom[dataIndex]['parentitem'] = '<a href="/track/billofmaterials/edit?ITEM=' +data.kittingbom[dataIndex]['parentitem']+'&DESC='+data.kittingbom[dataIndex]['pdesc']+'&PUOM='+data.kittingbom[dataIndex]['puom']+'">'+data.kittingbom[dataIndex]['parentitem']+'</a>';
			        		<%}else{ %>
			        		data.kittingbom[dataIndex]['parentitem'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#">'+data.kittingbom[dataIndex]['parentitem']+'</a>';
			        		<%} %>
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
			    			{"data": 'puom', "orderable": true},
			    			/* {"data": 'cdesc', "orderable": true},
			    			{"data": 'cdetdesc', "orderable": true},
			    			{"data": 'qty', "orderable": true}, 
			    			{"data": 'DETAIL', "orderable": true}*/
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

		    $("#tableInventorySummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
	        	tableInventorySummary.draw();
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
         		'<th width="9%"><font color="#ffffff">Parent UOM</font></th>'+
         		/* '<th width="9%"><font color="#ffffff">Child Prod Desc</font></th>'+
         		'<th width="11%"><font color="#ffffff">Child Prod Detail Desc</font></th>'+ */
         		/* '<th width="9%"><font color="#ffffff">Equivalent Prod ID</font></th>'+
         		'<th width="9%"><font color="#ffffff">Equivalent Prod Desc</font></th>'+
         		'<th width="11%"><font color="#ffffff">Equivalent Prod Detail Desc</font></th>'+ */
         		/* '<th width="5%"><font color="#ffffff">BOM QTY</font></th>'+ */
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
	 onGo();
	 var plant= '<%=plant%>'; 
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

    /* Product Number Auto Suggestion */
    $('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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
	
    $('#CITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
				return '<div onclick="GetEquivalentProduct(this,\''+ data.ITEM+'\')"><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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
 
 function GetEquivalentProduct(obj,item)
 {
	 var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : document.form.plant.value,
			ITEM : item,
			PITEM : document.form.ITEM.value,
			ACTION : "GET_EQUIVALENT_PRODUCT"
			},
			dataType : "json",
			success : function(data) {
				if (data.ERROR_CODE == "100") {
					document.form.EITEM.value= data.eitem;		
				} else {
					document.form.EITEM.value = "";
				}
			}
		});	

 }
 
 </script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>