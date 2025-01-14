<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%@ include file="header.jsp"%>
<%
String title = "Stock Take Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
//document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'StockTake', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function onGo(){
document.form1.action="../inhouse/stocktake";
document.form1.submit();
}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();



String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_CLS_ID="",
PRD_TYPE_ID="",PRD_DESCRIP="", QTY ="",PRD_BRAND_ID= "", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String html = "",LOC_TYPE_ID="",USER="";

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
//Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
//End code added by Deen for product brand on 11/9/12 
FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE    = strUtils.fString(request.getParameter("TO_DATE"));
LOC_TYPE_ID =  strUtils.fString(request.getParameter("LOC_TYPE_ID"));
USER             = strUtils.fString(request.getParameter("USER"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryNew=false;
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("manualstocktake", PLANT,USERID);
}
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
boolean cntRec=false;
String collectionDate=du.getDate();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
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

//RESVI
String curDate =du.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=du.getDateinddmmyyyy(curDate);

%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><label>Stock Take Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="window.location.href='../import/stocktake'">
						Import Stock Take</button>
					&nbsp;
				</div>
			<div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="onExportInventory();">
						Export Inventory Upload Data</button>
					&nbsp;
				</div>
				
                     <!-- <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="onExport();">
						Export All Data</button>
					&nbsp;
				</div> -->
				
				<%if(displaySummaryNew){ %>
                     <div class="btn-group" role="group">
				<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../inhouse/manualstocktake'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              </div>
              <%}%>
				
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
              </div>
              
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../inhouse/stocktake">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
  
  <center><small><%=fieldDesc%></small></center>
  
  <%-- <center><h1><small><%=fieldDesc%></small></h1></center> --%>
   
   <div id="target" style="display:none" style="padding: 18px;">
   <div class="form-group">    	
   
 <!-- 1st row --> 
 
 					<div class="row" style="padding:3px">
						<label class="control-label col-sm-2" for="search">Search</label>
							<div class="col-sm-1" style="width: 12%;">
				  				<INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
				  			</div>
				  			<div class="col-sm-1" style="width: 13%;">
				  				<input class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  			</div>
				  			<div class="col-sm-3 ac-box">
				      	    	<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
				    			<INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT" name="ITEM" id="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
				    			<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i> </span>
							</div>
				  			<div class="col-sm-3 ac-box">
        						<INPUT class="ac-selected  form-control typeahead" placeholder="BATCH" name="BATCH" id="BATCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BATCH)%>" size="30"  MAXLENGTH=50>
        						<span class="select-icon"  onclick="$(this).parent().find('input[name=\'BATCH\']').focus()">
  		  						<i class="glyphicon glyphicon-menu-down"></i></span>
							</div>
					</div>

 <!-- 2rd row -->
					<div class="row" style="padding:3px">
						<label class="control-label col-sm-2" for="search"></label>
							<div class="col-sm-3">
	        					<INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT CLASS" name="PRD_CLS_ID" id="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20> 
	        					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()">
	  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
	    					</div> 
	    					<div class="col-sm-3 ac-box">
	        					<INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT TYPE" name="PRD_TYPE_ID" id="PRD_TYPE_ID"  type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
	        					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()">
	  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
							</div>
							<div class="col-sm-3 ac-box">
	    						<INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT BRAND" name="PRD_BRAND_ID" id="PRD_BRAND_ID"  type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
	    						<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()">
	  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
							</div>
								<input type="hidden" name="PRD_BRAND_DESC" value="">
	 		   					<INPUT name="ACTIVE" type = "hidden" value="">   
					</div>
				
 <!-- 3rd row -->
					<div class="row" style="padding:3px">
						<label class="control-label col-sm-2" for="search"></label>
							<div class="col-sm-3">
	        					<INPUT class="ac-selected  form-control typeahead" placeholder="LOCATION" name="LOC" id="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" size="30"  MAXLENGTH=20> 
	        					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()">
	  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
	    					</div> 
	    					<div class="col-sm-3 ac-box">
	        					<INPUT class="ac-selected  form-control typeahead" placeholder="LOCATION TYPE" name="LOC_TYPE_ID" id="LOC_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" size="30"  MAXLENGTH=20>
	        					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()">
	  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
							</div>
							<div class="col-sm-3 ac-box">
	    						<INPUT class="form-control" placeholder="USER" name="USER" type = "TEXT" value="<%=StrUtils.forHTMLTag(USER)%>" style="width: 100%"  MAXLENGTH=20>
							</div>
							<div class="col-sm-2" style="display: none;">
	        					<INPUT class="form-control" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" style="width: 100%"  MAXLENGTH=100> 
							</div>
					</div>
					<input type="hidden" name="LOC_DESC" value="">
			    	<input type="hidden" name="isWithCost" value="false">
    	 
 					<div class="row" style="padding:3px">
						<label class="control-label col-sm-2" for="search"></label>
						<div class="col-sm-3">
  						<button type="button" class="Submit btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
<!--    	<button type="button" class="Submit btn btn-default"  onClick="onExport();"><b>Export All Data</b></button>&nbsp; -->
<!--    	<button type="button" class="Submit btn btn-default"  onClick="onExportInventory();"><b>Export Inventory Upload Data</b></button>&nbsp; -->
<!--    	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RST');}"> <b>Back</b></button>&nbsp;&nbsp; -->
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
      <div class="col-sm-offset-6">
<!--     <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp; -->
<!--    	<button type="button" class="Submit btn btn-default"  onClick="onExport();"><b>Export All Data</b></button>&nbsp; -->
<!--    	<button type="button" class="Submit btn btn-default"  onClick="onExportInventory();"><b>Export Inventory Upload Data</b></button>&nbsp; -->
<!--    	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RST');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>
   
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary" class="table table-bordred table-striped" role="grid" aria-describedby="tableInventorySummary_info">
<!--               	<table id="tableInventorySummary" class="table table-bordered table-hover dataTable no-footer" role="grid" aria-describedby="tableInventorySummary_info"> -->
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">LOC</th>
		                	<th style="font-size: smaller;">TRANDATE</th>
		                	<th style="font-size: smaller;">TRAN NO</th>
		                	<th style="font-size: smaller;">PRODUCT CLASS ID</th>
		                	<th style="font-size: smaller;">PRODUCT TYPE ID</th>
		                	<th style="font-size: smaller;">PRODUCT BRAND ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">BATCH</th>
		                	<th style="font-size: smaller;">STOCK TAKE QUANTITY</th>
		                	<th style="font-size: smaller;">INVENTORY QUANTITY</th>
		                	<!-- <th style="font-size: smaller;">PC/PCS/EA QUANTITY</th> -->
		                	<th style="font-size: smaller;">INVENTORY QUANTITY DIFF</th>
		                	<!-- <th style="font-size: smaller;">PC/PCS/EA QUANTITY DIFF</th> -->
		                	<th style="font-size: smaller;">USER</th>
		                	<th style="font-size: smaller;">REMARKS</th>
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">REMARKS</th> -->
		                </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
      
  </FORM>
 
  <SCRIPT LANGUAGE="JavaScript" type="text/javascript">
 
  var tableInventorySummary;
  var productId, desc, loc,batch, prdBrand, prdClass, prdType,frmDate,toDate, loctype,user, groupRowColSpan = 6;
function getParameters(){
	return { 
		"ITEM": productId,"PRD_DESCRIP":desc,"LOC":loc,"BATCH":batch,"PRD_CLS_ID":prdClass,"PRD_TYPE_ID":prdType,"PRD_BRAND_ID":prdBrand,
		"FROM_DATE":frmDate,"TO_DATE":toDate,"LOC_TYPE_ID":loctype,"USER":user,
		"ACTION": "VIEW_STOCK_TAKE_SUMMARY",
		"PLANT":"<%=PLANT%>"
	}
}  
  function onExport(){
		document.form1.action = "/track/StockTakeServlet?action=Export_Excel";
		document.form1.submit();
		
	} 
  
  function onExportInventory(){
		document.form1.action = "/track/StockTakeInvUploadServlet?action=Export_ExcelInventoryUpload";
		document.form1.submit();
		
	} 

function onGo(){
     productId = document.form1.ITEM.value;
     desc = document.form1.PRD_DESCRIP.value;
     loc = document.form1.LOC.value;
     batch = document.form1.BATCH.value;
     prdClass = document.form1.PRD_CLS_ID.value;
     prdType = document.form1.PRD_TYPE_ID.value;
     prdBrand = document.form1.PRD_BRAND_ID.value;
     frmDate = document.form1.FROM_DATE.value;
     toDate = document.form1.TO_DATE.value;
     loctype = document.form1.LOC_TYPE_ID.value;
     user = document.form1.USER.value;
     var urlStr = "../InvMstServlet";
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
 		        	if(typeof data.items[0].ITEM === 'undefined'){
 		        		return [];
 		        	}else {
 		        		return data.items;
 		        	}
 		        }
 		    },
 	        "columns": [
     			{"data": 'ITEM', "orderable": true},
     			{"data": 'LOC', "orderable": true},
     			{"data": 'TRANDATE', "orderable": true},
     			{"data": 'TRANNO', "orderable": true},
     			{"data": 'PRDCLSID', "orderable": true},
     			{"data": 'ITEMTYPE', "orderable": true},
     			{"data": 'PRD_BRAND_ID', "orderable": true},
     			{"data": 'ITEMDESC', "orderable": true},
     			{"data": 'STKUOM', "orderable": true},
     			{"data": 'BATCH', "orderable": true},
     			{"data": 'STOCKQTY', "orderable": true},
     			{"data": 'QTY', "orderable": true},
     			//{"data": 'PCSQTY', "orderable": true},
     			{"data": 'strDiffQty', "orderable": true},
     			//{"data": 'PCSDiffQty', "orderable": true},
     			{"data": 'USERID', "orderable": true},
     			{"data": 'REMARKS', "orderable": true}
     			],
 			"columnDefs": [{"className": "t-right", "targets": [7]}],
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
                       
	        	outPutdata = outPutdata+item.STOCKDETAILS
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
                    '<TH><font color="#ffffff" align="left" ><b>Loc</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>TranDate</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>Product Class ID</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>Product Type ID</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>Product Brand ID</TH>'+
                     '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                     '<TH><font color="#ffffff" align="left" ><b>UOM</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>Batch</TH>'+
                    '<TH><font color="#ffffff" align="Right" ><b>Stock Take Quantity</TH>'+
                    '<TH><font color="#ffffff" align="Right" ><b>Inventory Quantity</TH>'+
                    '<TH><font color="#ffffff" align="Right" ><b>PC/PCS/EA Quantity</TH>'+                    
                    '<TH><font color="#ffffff" align="Right" ><b>Inventory Quantity Diff</TH>'+
                    '<TH><font color="#ffffff" align="Right" ><b>PC/PCS/EA Quantity Diff</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>User</TH>'+
                    '<TH><font color="#ffffff" align="left" ><b>Remarks</TH>'+
                    
                    '</TR>';
                
}
   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div></div></div>

                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
 onGo();
 var plant= '<%=PLANT%>'; 
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
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
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
// 			return '<p onclick="document.form.ITEM_DESC.value = \''+data.ITEMDESC+'\'">'+data.ITEM+'</p>';
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

	  /* Product Class Auto Suggestion */
    $('#PRD_CLS_ID').typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  display: 'prd_cls_id',  
    	  source: function (query, process,asyncProcess) {
    		var urlStr = "/track/MasterServlet";
    		$.ajax( {
    		type : "POST",
    		url : urlStr,
    		async : true,
    		data : {
    			PLANT : plant,
    			ACTION : "GET_PRODUCTCLASSID_FOR_SUMMARY",
    			PRODUCTCLASSID : query
    		},
    		dataType : "json",
    		success : function(data) {
    			return asyncProcess(data.CUSTOMERTYPELIST);
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
    return '<p class="item-suggestion">'+ data.prd_cls_id +'</p>';
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

/* product type Auto Suggestion */
	    $('#PRD_TYPE_ID').typeahead({
	  	  hint: true,
	  	  minLength:0,  
	  	  searchOnFocus: true
	  	},
	  	{
	  	  display: 'prd_type_id',  
	  	  source: function (query, process,asyncProcess) {
	  		var urlStr = "/track/MasterServlet";
	  		$.ajax( {
	  		type : "POST",
	  		url : urlStr,
	  		async : true,
	  		data : {
	  			PLANT : plant,
	  			ACTION : "GET_PRD_FOR_SUMMARY",
	  			PRODUCTTYPEID : query
	  		},
	  		dataType : "json",
	  		success : function(data) {
	  			return asyncProcess(data.SUPPLIERTYPELIST);
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
	  return '<p class="item-suggestion">'+ data.prd_type_id +'</p>';
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

/* product brand Auto Suggestion */
	    $('#PRD_BRAND_ID').typeahead({
	  	  hint: true,
	  	  minLength:0,  
	  	  searchOnFocus: true
	  	},
	  	{
	  	  display: 'PRD_BRAND_ID',  
	  	  source: function (query, process,asyncProcess) {
	  		var urlStr = "/track/MasterServlet";
	  		$.ajax( {
	  		type : "POST",
	  		url : urlStr,
	  		async : true,
	  		data : {
	  			PLANT : plant,
	  			ACTION : "GET_PRODUCTBRANDID_FOR_SUMMARY",
	  			PRODUCTBRANDID : query
	  		},
	  		dataType : "json",
	  		success : function(data) {
	  			return asyncProcess(data.PRODUCTBRAND);
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
	  return '<p class="item-suggestion">'+ data.PRD_BRAND_ID +'</p>';
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


	    /* location Auto Suggestion */
		$('#LOC').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'LOC',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.LOC_MST);
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
				return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
	    
	    /* location 1 Auto Suggestion */
		$('#LOC_TYPE_ID').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'LOC_TYPE_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.LOCTYPE_MST);
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
				return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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

		/* Batch Auto Suggestion */
		$('#BATCH').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'BATCH',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/InvMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_BATCH_LIST_FOR_SUGGESTION",
					BATCH : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.batchList);
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
				return '<p>'+data.BATCH+' - '+data.QTY+'</p></div>';
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
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>