<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = " Kitting Dekitting with Bill Of Materials Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
    <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
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

String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryNew=false,displaySummaryExport=false,displaySummaryEdit=false;
if(systatus.equalsIgnoreCase("INVENTORY")) {
	displaySummaryNew = ub.isCheckValinv("kitdekitwithbomnew", sPlant,sUserId);
	displaySummaryEdit = ub.isCheckValinv("kitdekitwithbomedit", sPlant,sUserId);
}

action            = strUtils.fString(request.getParameter("action"));
if(action.equalsIgnoreCase(""))
	action=  (String)(request.getAttribute("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));
eitem  = strUtils.fString(request.getParameter("EITEM"));
qty  = strUtils.fString(request.getParameter("QTY"));
result = strUtils.fString(request.getParameter("RESULT"));
if(result.equalsIgnoreCase(""))
	result = (String)(request.getAttribute("RESULT"));
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

String curDate =du.getDateMinusDays();
String FROM_DATE=du.getDateinddmmyyyy(curDate);
String TO_DATE="";
%>
<center>
	<h2><small class="success-msg"><%=result%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Kitting Dekitting with Bill Of Materials Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <div class="pull-right">
                <div class="btn-group" role="group">
                <!-- <button type="button" class="Submit btn btn-default" value="ExportReport" onClick="javascript:ExportReport();">Export Report</button>&nbsp;&nbsp; -->
                </div>
                <div class="btn-group" role="group">
                <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../kittingdekitting/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
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
    		<input class="ac-selected form-control" type="TEXT" size="20" MAXLENGTH=100 name="KONO" id="KONO" placeholder="Kitting Dekitting No.">
    		<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'KONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	
   		 </div>	  		
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">          
            
    		<input class="ac-selected form-control" type="TEXT" size="20" MAXLENGTH=100 name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}" placeholder="Parent Product">
    		<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	   		 	
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
              	<table id="tableInventorySummary" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">No</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Order No.</TH>
       	   <TH style="background-color: #ffffff ; color:#333; text-align: center;">Parent Product</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Parent Product Desc</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Location</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Batch</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">UOM</TH>
           <TH style="background-color: #ffffff ; color:#333; text-align: center;">Edit</TH>
          
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
					
  <input type="hidden" name="PTYPE" id="PTYPE" value="KITDEKITWITHBOMSUMMARY">
  

</FORM>




<script>
function onClear()
{
	debugger;
	document.getElementById("ITEM").value = "";
	document.getElementById("KONO").value = "";	
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

 
 // Table Design begins

var tableInventorySummary;
var product, pdesc, pdetaildesc, kono, fdate, tdate;
function getParameters(){
	return { 
		"ITEM":product,
		"DESC":pdesc,
		"DETDESC":pdetaildesc,
		"KONO":kono,
		"FDATE":fdate,
		"TDATE":tdate,
		"ACTION": "VIEW_KIT_DEKIT_SUMMARY_DETAILS",
		"PLANT":"<%=plant%>"
	}
}  

 
function onGo(index) {

	 index = index;
	 product = document.form.ITEM.value;
	 fdate = document.form.FROM_DATE.value;
	 tdate = document.form.TO_DATE.value;
	
	 kono = document.form.KONO.value;
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
			        	if(typeof data.kittingbom[0].kono === 'undefined'){
			        		return [];
			        	}else {
			        		for(var dataIndex = 0; dataIndex < data.kittingbom.length; dataIndex ++){
			        		<% if (displaySummaryEdit) { %>
			        		data.kittingbom[dataIndex]['EDIT'] = '<a href="/track/kittingdekitting/edit?KONO=' +data.kittingbom[dataIndex]['kono']+'&ITEM=' +data.kittingbom[dataIndex]['parentitem']+'&DESC='+data.kittingbom[dataIndex]['pdesc']+ '"><i class="fa fa-pencil-square-o"></i></a>';
			        		<%}else{ %>
			        		data.kittingbom[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a>';
			        		<%} %>
			        		}
			        		return data.kittingbom;
			        	}
			        }
			    },
			    "columns": [
							{"data": 'id', "orderable": true},
			    			{"data": 'kono', "orderable": true},
			    			{"data": 'parentitem', "orderable": true},
			       			{"data": 'pdesc', "orderable": true},
			    			{"data": 'LOC_ID', "orderable": true},
			    			{"data": 'BATCH_0', "orderable": true},
			    			{"data": 'uom', "orderable": true},
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
         		'<th width="9%"><font color="#ffffff">Order No.</font></th>'+
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
				ACTION : "GET_BOM_PRODUCT_LIST_FOR_SUGGESTION",
				TYPE : "KITBOM",
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
	
    
	/* Order Number Auto Suggestion */
	$('#KONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'KONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../ProductionBomServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				KONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
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
		    return '<p>' + data.KONO + '</p>';
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.KONO.value = "";
			}
		
		});
	
    
 });
 
  
 </script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>