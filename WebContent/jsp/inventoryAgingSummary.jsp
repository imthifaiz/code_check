<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Inventory Aging Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",pono="",PGaction="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="";
DateUtils _dateUtils = new DateUtils();
FROM_DATE = DateUtils.getDate();

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
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

String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
File checkImageFile = new File(imagePath);
if (!checkImageFile.exists()) {
	imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
}
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<script src="../jsp/js/Aging.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>		
                <li><label>Inventory Aging Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
			<div class="box-title pull-right">
            <!-- <button type="button" class="btn btn-default" onclick="onPrint(false)">
					<i class="fa fa-print" aria-hidden="true"></i>
				</button> -->
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div>
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>			
		</div>
	
	<div class="container-fluid">
		<form class="form-horizontal" name="form1" method="post" action="">
			<input type="hidden" name="PLANT" value="<%=plant%>" >
			<div id="target" style="padding: 18px; display:none;">
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2" style="display:none">
							<INPUT name="STATEMENT_DATE" id="STATEMENT_DATE" type = "text" value="" size="30"  
							MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="STATEMENT DATE">
						</div>
						<div class="col-sm-0">
						</div>
						<div class="col-sm-4 ac-box">
					  		<input type="text" name="item" id="ITEM" class="ac-selected form-control" 
					  		placeholder="PRODUCT" >
					  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
					  		</span>
				  		</div>						
					</div>
					
					<div class="row" style="padding:3px">
				  		<div class="col-sm-2">
				  		</div>
				  		<div class="col-sm-2">
				  			<input name="FROM_DATE" id="FROM_DATE" type = "text" value="" size="30"  MAXLENGTH="20" 
				  			class ="form-control datepicker" readonly placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" 
				  			value="" size="30"  MAXLENGTH="20" readonly placeholder="TO DATE">
				  		</div>
				  		<% if(COMP_INDUSTRY.equals("Retail")) { %> 
				  		<div class="col-sm-4 ac-box">
						 		<input type="text" class="ac-selected form-control typeahead" id="LOC" 
						 		placeholder="LOCATION/BRANCH" name="LOC" >				
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>								 -->
						</div>
						 <%}else{%>			
				  		<div class="col-sm-4 ac-box">
						 		<input type="text" class="ac-selected form-control typeahead" id="LOC" 
						 		placeholder="LOCATION" name="LOC" >				
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>								 -->
						</div>	
						<%}%>		
			  		</div>
			  		
			  		
			  		<div class="row" style="padding:3px">
				  		<div class="col-sm-2">
				  		</div>
				  			<div class="col-sm-4 ac-box">
							 
						 <INPUT class="ac-selected form-control" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>"	placeholder="LOCATION TYPE ONE" size="30" MAXLENGTH=20>
    	                <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
							
						</div>
				  		
				  		<div class="col-sm-4 ac-box">
							
						<INPUT class="ac-selected form-control" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE TWO" size="30" MAXLENGTH=20>
    	                 <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
							
						</div>			
			  		</div>
			  		
			  		<div class="row" style="padding:3px">
				  		<div class="col-sm-2">
				  		</div>
				  			<div class="col-sm-4 ac-box">
							 
					<INPUT class="ac-selected form-control" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=LOC_TYPE_ID3%>" placeholder="LOCATION TYPE THREE" size="30" MAXLENGTH=20>
                   <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
     			
						</div>	
						
				  		<div class="col-sm-4 ac-box" style="display:none">
							 
						 		<input type="text" class="ac-selected form-control typeahead" id="BATCH" 
						 		placeholder="BATCH" name="BATCH" autocomplete="off">				
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'Batch\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 								<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/batch_list_inventory.jsp?BATCH='+form1.BATCH.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>								 -->
							
						</div>	
				  				
			  		</div>
			  		
		
			  		<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-10 txn-buttons">								
								<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>							
							</div>
					</div>
				</div>
			</div>
			</form>
			<div class="form-group">
		      <div class="row">
			      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
			      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
		      </div>
		      
		      <div class="ShowSingle">			      
		      </div>
			</div>
			<div id="VIEW_RESULT_HERE" class="table-responsive">
				<div class="row">
					<div class="col-sm-12">
					
					<div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
            </div>
						<table id="tableinventoryAgingSummary" class="table table-bordred table-striped"> 
							<thead>
								<tr><th style="font-size: smaller;">PRODUCT</th>
								<th style="font-size: smaller;">DESCRIPTION</th>
								<th style="font-size: smaller;">COST</th>
								<th style="font-size: smaller;">LIST PRICE</th>
								<th style="font-size: smaller;">LOC</th>
								<th style="font-size: smaller;">BATCH</th>
								<th style="font-size: smaller;">UOM</th>
								<th style="font-size: smaller;">TOT.QTY</th>
								<th style="font-size: smaller;">1-30 DAYS</th>
								<th style="font-size: smaller;">31-60 DAYS</th>
								<th style="font-size: smaller;">61-90 DAYS</th>
								<th style="font-size: smaller;">90+ DAYS</th></tr>
							</thead>
							<tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th>Total</th>
								<th></th>
								<th></th>
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
						</table>
					</div>
				</div>
			</div>
			</div>
		
	
	</div>

</div>

<div class="modal fade" id="agingModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
      	<div class="modal-header">
        	<h4 class="modal-title pull-left">Preview</h4>
        	<div class="btn-toolbar pull-right"> 
        		<button type="button" class="btn btn-success" onclick="onPrint(true)">Print</button> 
        		<button type="button" class="btn btn-default" data-dismiss="modal"> Close </button>
       		</div>
       	</div>
        <div class="modal-body">
          <object id="pdfOutput" type="application/pdf" style="height:500px;width:100%;">
            <p>It appears you don't have PDF support in this web browser. <a href="#" id="download-link">Click here to download the PDF</a>.</p>
		  </object>
        </div>
      </div>
      
    </div>
  </div>
  
<div hidden>
<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>" style="width:130px;" id="logo_content">
	
<table id="table1">
</table>
<table id="table2">
</table>
</div>

<script>
$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	   if (document.form1.STATEMENT_DATE.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_STATEMENT_DATE','', 'STATEMENT_DATE');
		 } 	
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_ITEM','', 'ITEM');
		 } 
	   if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_FROM_DATE','', 'FROM_DATE');
		 } 
	   if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_TO_DATE','', 'TO_DATE');
		 } 
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('inventoryAgingSummary_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('inventoryAgingSummary_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('inventoryAgingSummary_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.BATCH.value == ''){
		   getLocalStorageValue('inventoryAgingSummary_BATCH','','BATCH');
		}
    }
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
	 var plant= '<%=plant%>';
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
					//ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
					ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_REPORT",
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
	/* location Auto Suggestion */
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
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
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
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
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
var plant = document.form1.PLANT.value;
var tableinvoiceAgingSummary;
var ageingDetails;
var FROM_DATE,TO_DATE,ITEM,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,groupRowColSpan = 6,groupRowColSpans=1;

function getParameters(){
	return {
		"FROM_DATE":FROM_DATE,"TO_DATE":TO_DATE,
		"ITEM":ITEM,"PRD_CLS_ID":PRD_CLS_ID,
		"PRD_TYPE_ID":PRD_TYPE_ID,
		"PRD_BRAND_ID":PRD_BRAND_ID,
		"LOC":LOC,
		"LOC_TYPE_ID":LOC_TYPE_ID,
		"LOC_TYPE_ID2":LOC_TYPE_ID2,
		"LOC_TYPE_ID3":LOC_TYPE_ID3,
		"BATCH":BATCH,
		"Submit": "view_inv_summary_aging_days",
		"PLANT":"<%=plant%>"
	}
}

function storeUserPreferences(){
	storeInLocalStorage('inventoryAgingSummary_STATEMENT_DATE', $('#STATEMENT_DATE').val());
	storeInLocalStorage('inventoryAgingSummary_ITEM', $('#ITEM').val());
	storeInLocalStorage('inventoryAgingSummary_FROM_DATE', $('#FROM_DATE').val());
	storeInLocalStorage('inventoryAgingSummary_TO_DATE', $('#TO_DATE').val());
	storeInLocalStorage('inventoryAgingSummary_LOC', $('#LOC').val());
	storeInLocalStorage('inventoryAgingSummary_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('inventoryAgingSummary_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('inventoryAgingSummary_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('inventoryAgingSummary_BATCH', $('#BATCH').val());
	
}

function onGo(){
	var flag = "false";
	FROM_DATE = document.form1.FROM_DATE.value;
    TO_DATE = document.form1.TO_DATE.value;
    ITEM = document.form1.item.value;
    LOC = document.form1.LOC.value;
    loctype = document.form1.LOC_TYPE_ID.value;
    loctype2 = document.form1.LOC_TYPE_ID2.value;
    loctype3 = document.form1.LOC_TYPE_ID3.value;
    BATCH = document.form1.BATCH.value;
	   
	if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	if(TO_DATE != null    && TO_DATE != "") { flag = true;}	
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
		storeUserPreferences();
    }
	var urlStr = "../AgeingServlet";
	var groupColumn = 1;
	var totalQty = 0;
	    
    if(FROM_DATE == null || FROM_DATE == "") {
    	FROM_DATE = "<%=FROM_DATE%>";
    }
    if (tableinvoiceAgingSummary){
    	tableinvoiceAgingSummary.ajax.url( urlStr ).load();
    }else{
    	tableinvoiceAgingSummary = $('#tableinventoryAgingSummary').DataTable({
				"processing": true,
				"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
// 				"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

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
			        	if(typeof data.INVDETAILS[0].product === 'undefined'){
			        		return [];
			        	}else {				        		
			        		return data.INVDETAILS;
			        	}
			        }
			    },
		        "columns": [
		        	{"data": 'product', "orderable": true},
	    			{"data": 'description', "orderable": true},
	    			{"data": 'COST', "orderable": true},
	    			{"data": 'UnitPrice', "orderable": true},
	    			{"data": 'location', "orderable": true},
	    			{"data": 'batch', "orderable": true},
	    			{"data": 'uom', "orderable": true},
	    			{"data": 'qty', "orderable": true},
	    			{"data": 'prevdays30', "orderable": true},
	    			{"data": 'prevdays60', "orderable": true},   			
	    			{"data": 'prevdays90', "orderable": true},   			
	    			{"data": 'prevdays120', "orderable": true},   			
	    			],
	    			"columnDefs": [{"className":"text-center", "targets": [7,8,9,10,11]}],
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
			    	                },
			    	                title: '<%=title%>',
			    	                footer: true
			                    },
			                    {
			                    	extend : 'pdf',
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
			        "drawCallback": function(settings) {
			            this.attr('width', '100%');
			            var api = this.api();
			            var rows = api.rows({ page: 'current' }).nodes();
			            var last = null;
			            var groupTotalQty7 = 0;
			            var totalQty7 = 0;
			            var groupTotalQty8 = 0;
			            var totalQty8 = 0;
			            var groupTotalQty9 = 0;
			            var totalQty9 = 0;
			            var groupTotalQty10 = 0;
			            var totalQty10 = 0;
			            var groupTotalQty11 = 0;
			            var totalQty11 = 0;
			            var groupEnd = 0;
			            var currentRow = 0;

			            api.column(groupColumn, { page: 'current' }).data().each(function(group, i) {
			                if (last !== group) {
			                    /*if (i > 0) {
			                        // Add group total row
			                        $(rows).eq(currentRow).after(
			                            '<tr class="group"><td colspan=' + groupRowColSpans + '></td><td></td><td></td><td></td><td></td><td></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty7).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty8).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty9).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty10).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty11).toFixed(3) + '</td></tr>'
			                        );
			                    }*/
			                    last = group;
			                    groupEnd = i;
			                    groupTotalQty7 = 0;
			                    groupTotalQty8 = 0;
			                    groupTotalQty9 = 0;
			                    groupTotalQty10 = 0;
			                    groupTotalQty11 = 0;
			                }
			                groupTotalQty7 += parseFloat($(rows).eq(i).find('td:eq(7)').html());
			                totalQty7 += parseFloat($(rows).eq(i).find('td:eq(7)').html());
			                groupTotalQty8 += parseFloat($(rows).eq(i).find('td:eq(8)').html());
			                totalQty8 += parseFloat($(rows).eq(i).find('td:eq(8)').html());
			                groupTotalQty9 += parseFloat($(rows).eq(i).find('td:eq(9)').html());
			                totalQty9 += parseFloat($(rows).eq(i).find('td:eq(9)').html());
			                groupTotalQty10 += parseFloat($(rows).eq(i).find('td:eq(10)').html());
			                totalQty10 += parseFloat($(rows).eq(i).find('td:eq(10)').html());
			                groupTotalQty11 += parseFloat($(rows).eq(i).find('td:eq(11)').html());
			                totalQty11 += parseFloat($(rows).eq(i).find('td:eq(11)').html());
			                currentRow = i;
			            });

			            if (groupEnd > 0 || rows.length == (currentRow + 1)) {
			                $(rows).eq(currentRow).after(
			                    '<tr class="group"><td colspan=' + groupRowColSpans + '></td><td></td><td></td><td></td><td></td><td></td><td>Total</td><td class="t-right">' + parseFloat(totalQty7).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty8).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty9).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty10).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty11).toFixed(3) + '</td></tr>'
			                );
			            }
			        },
			        "footerCallback": function(row, data, start, end, display) {
			            var api = this.api();
			            var intVal = function(i) {
			                return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
			            };

			         // Total over all pages for column 7
			            var total7 = api.column(7).data().reduce(function(a, b) {
			                return intVal(a) + intVal(b);
			            }, 0);

			            // Total over all pages for column 8
			            var total8 = api.column(8).data().reduce(function(a, b) {
			                return intVal(a) + intVal(b);
			            }, 0);

			            // Total over all pages for column 9
			            var total9 = api.column(9).data().reduce(function(a, b) {
			                return intVal(a) + intVal(b);
			            }, 0);

			            // Total over all pages for column 10
			            var total10 = api.column(10).data().reduce(function(a, b) {
			                return intVal(a) + intVal(b);
			            }, 0);

			            // Total over all pages for column 11
			            var total11 = api.column(11).data().reduce(function(a, b) {
			                return intVal(a) + intVal(b);
			            }, 0);

			            var rows = api.rows({ page: 'current' }).nodes();
			            var last = null;
			            var groupTotalQty7 = 0;
			            var totalQty7 = 0;
			            var groupTotalQty8 = 0;
			            var totalQty8 = 0;
			            var groupTotalQty9 = 0;
			            var totalQty9 = 0;
			            var groupTotalQty10 = 0;
			            var totalQty10 = 0;
			            var groupTotalQty11 = 0;
			            var totalQty11 = 0;
			            var groupEnd = 0;
			            var currentRow = 0;

			            api.column(groupColumn, { page: 'current' }).data().each(function(group, i) {
			                if (last !== group) {
			                    /*if (i > 0) {
			                        // Add group total row
			                        $(rows).eq(currentRow).after(
			                            '<tr class="group"><td colspan=' + groupRowColSpans + '></td><td></td><td></td><td></td><td></td><td></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty7).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty8).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty9).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty10).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty11).toFixed(3) + '</td></tr>'
			                        );
			                    }*/
			                    last = group;
			                    groupEnd = i;
			                    groupTotalQty7 = 0;
			                    groupTotalQty8 = 0;
			                    groupTotalQty9 = 0;
			                    groupTotalQty10 = 0;
			                    groupTotalQty11 = 0;
			                }
			                groupTotalQty7 += parseFloat($(rows).eq(i).find('td:eq(7)').html());
			                totalQty7 += parseFloat($(rows).eq(i).find('td:eq(7)').html());
			                groupTotalQty8 += parseFloat($(rows).eq(i).find('td:eq(8)').html());
			                totalQty8 += parseFloat($(rows).eq(i).find('td:eq(8)').html());
			                groupTotalQty9 += parseFloat($(rows).eq(i).find('td:eq(9)').html());
			                totalQty9 += parseFloat($(rows).eq(i).find('td:eq(9)').html());
			                groupTotalQty10 += parseFloat($(rows).eq(i).find('td:eq(10)').html());
			                totalQty10 += parseFloat($(rows).eq(i).find('td:eq(10)').html());
			                groupTotalQty11 += parseFloat($(rows).eq(i).find('td:eq(11)').html());
			                totalQty11 += parseFloat($(rows).eq(i).find('td:eq(11)').html());
			                currentRow = i;
			            });

			            // Update the footer with the calculated totals
			  <%--           $(api.column(7).footer()).html(parseFloat(total7).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(8).footer()).html(parseFloat(total8).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(9).footer()).html(parseFloat(total9).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(10).footer()).html(parseFloat(total10).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(11).footer()).html(parseFloat(total11).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>

			            $(api.column(7).footer()).html(parseFloat(groupTotalQty7).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(8).footer()).html(parseFloat(groupTotalQty8).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(9).footer()).html(parseFloat(groupTotalQty9).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(10).footer()).html(parseFloat(groupTotalQty10).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            $(api.column(11).footer()).html(parseFloat(groupTotalQty11).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
			            
			        }
			});

    	 $("#tableinventoryAgingSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
        	 tableinvoiceAgingSummary.draw();
         });
	    }
	}

function onPrint(dwnPdf){
	
    /*document.form.action="/NSeries/OrderPaymentServlet?action=printConsolidatedAgeingReport";
    document.form.submit();*/
	var urlStr = "/track/AgeingServlet?Submit=view_inv_summary_aging_days";
    var data = $("form").serialize();
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			if(data.INVDETAILS[0] != ""){
				ageingDetails = "";
				ageingDetails = data;
				generateAging(dwnPdf);
			}else{
				alert("No data available");
			}
		}
	});
}

function generateAging(dwnPdf) {
	var img = toDataURL($("#logo_content").attr("src"),
		function(dataUrl) {
		generateAgingPdf(dataUrl,dwnPdf);
		},'image/jpeg'
	);
}

function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
		var canvas = document.createElement('CANVAS');
		var ctx = canvas.getContext('2d');
		var dataURL;
		canvas.height = this.naturalHeight;
		canvas.width = this.naturalWidth;
		ctx.drawImage(this, 0, 0);
		dataURL = canvas.toDataURL(outputFormat);
		callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
		img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
		img.src = src;
	  }
}

function generateAgingPdf(dataUrl,dwnPdf){
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/*for(i=0;i<ageingDetails.INVDETAILS.length;i++){*/
		var reportContent = ageingDetails.reportContent;
		/*if(i>0){
			doc.addPage();
		}*/
		/* Statement Details */
		var table1body = "";
		table1body += "<tbody>";
		table1body += "<tr>";
		table1body += "<td>"+reportContent.lblDate+"</td>";
		table1body += "<td>"+reportContent.lblstatementno+"</td>";
		table1body += "</tr>";
		table1body += "<tr>";
		table1body += "<td>"+reportContent.CurDate+"</td>";
		table1body += "<td>"+reportContent.statementNo+"</td>";
		table1body += "</tr>";
		table1body += "</tbody>";
		$("#table1").html("");
		$("#table1").html(table1body);
		/* Statement Details */
		
		/* Company Address Start */
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		
		doc.setFontSize(10);
		doc.setFontStyle("bold");
		doc.text(reportContent.fromAddress_CompanyName, 16, 46);

		doc.setFontSize(9);
		doc.setFontStyle("normal");
		doc.text(reportContent.fromAddress_BlockAddress, 16, 50);

		doc.text(reportContent.fromAddress_RoadAddress, 16, 54);

		doc.text(reportContent.fromAddress_Country, 16, 58);
		/* Company Address end */
		
		doc.setFontSize(10);
		doc.text(reportContent.lblstatement, 162, 40);
		doc.autoTable({
			html : '#table1',
			startY : 46,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0}},
			theme : 'grid',
			styles: {
            lineColor: [0, 0, 0],
			
        }
		});
		
		var table2body = "";
		table2body += "<thead>";
		table2body += "<tr>";
		table2body += "<td>Product ID</td>";
		table2body += "<td>Description</td>";
		table2body += "<td>Loc</td>";
		table2body += "<td>Batch</td>";
		table2body += "<td>Total Quantity</td>";
		table2body += "<td>1-30 days</td>";
		table2body += "<td>31-60 days</td>";
		table2body += "<td>61-90 days</td>";
		table2body += "<td>90+ days</td>";
		table2body += "</tr>";
		table2body += "<thead>";
		table2body += "<tbody>";
		for(i=0;i<ageingDetails.INVDETAILS.length;i++){
			table2body += "<tr>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].product+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].description+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].location+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].batch+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].qty+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].prevdays30+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].prevdays60+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].prevdays90+"</td>";
			table2body += "<td>"+ageingDetails.INVDETAILS[i].prevdays120+"</td>";
			table2body += "</tr>";
		}
		table2body += "</tbody>";		
		$("#table2").html("");
		$("#table2").html(table2body);	
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.autoTable({
			html : '#table2',
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center'
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ]
			},
			theme : 'plain',
			styles: {
			lineColor: [0, 0, 0],						
			},
			didDrawPage : function(data) {
				doc.setFontStyle("normal");
				// Footer
				pageNumber = doc.internal.getNumberOfPages();
				var str = "Page " + doc.internal.getNumberOfPages();
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					str = str + " of " + totalPagesExp;
				}
				doc.setFontSize(10);

				// jsPDF 1.4+ uses getWidth, <1.4 uses .width
				var pageSize = doc.internal.pageSize;
				var pageHeight = pageSize.height ? pageSize.height
						: pageSize.getHeight();
				doc.text(str, data.settings.margin.left,
						pageHeight - 10);
			}
		});
	/*}*/
	
	doc.setFontStyle("normal");
	if(pageNumber < doc.internal.getNumberOfPages()){
		// Footer
		var str = "Page " + doc.internal.getNumberOfPages();
		// Total page number plugin only available in jspdf v1.0+
		if (typeof doc.putTotalPages === 'function') {
			str = str + " of " + totalPagesExp;
		}
		doc.setFontSize(10);

		// jsPDF 1.4+ uses getWidth, <1.4 uses .width
		var pageSize = doc.internal.pageSize;
		var pageHeight = pageSize.height ? pageSize.height
				: pageSize.getHeight();

		doc.text(str, 16, pageHeight - 10);
	}
	
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	
	if(dwnPdf){
		doc.save('Inventory Aging Report.pdf');
	}else{
		var object = document.getElementById("pdfOutput");

		var clone = object.cloneNode(true);
		var parent = object.parentNode;

		parent.removeChild(object);
		parent.appendChild(clone);
		
		document.getElementById("pdfOutput").data = doc.output('datauristring');
		$('#agingModal').modal('show');
	}
}

function ExportReport(){
	 
	document.form1.action = "/track/ReportServlet?action=view_inv_summary_aging_days";
	document.form1.submit();
	
} 

</script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>