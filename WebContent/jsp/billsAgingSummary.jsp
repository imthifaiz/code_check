<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
String title = "Supplier Aging Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String STMNT_DATE = "", FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",pono="",vendname="",PGaction="";

String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

STMNT_DATE = DateUtils.getDate();
FROM_DATE = DateUtils.getDate();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	FROM_DATE =DateUtils.getDate();

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();

String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
File checkImageFile = new File(imagePath);
if (!checkImageFile.exists()) {
	imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
}

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

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Supplier Aging Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
	 	<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <div class="dropdownbox-title pull-right">
                <div class="btn-group" role="group">
                	<!-- <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> -->
                	<button type="button" class="btn btn-default" data-toggle="dropdown">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
				    <ul class="dropdown-menu" style="top:100%">
				      <li><a href="#" onclick="javascript:return onPrint(false);">Print Aging Report</a></li>
				      <li><a href="#" onclick="javascript:return onPrintConsolidated(false);">Print Consolidated Aging Report</a></li>
				    </ul>
				    </div>
				    &nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../accounting/reports'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
				  </div>
		</div>		
		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="" >
				<input type="hidden" name="plant" value="<%=plant%>" >
				<input type="hidden" name="Order" value="BILL" >
				<input type="hidden" name="ORDERNO" >
				<INPUT type="hidden" name="STATE_PREFIX" value="" />
				<INPUT type="hidden" name="vendno" value="" >
				<input type="hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>" />
				<div id="CHK1"></div>
				<div id="target" style="padding: 18px; display:none;">
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2.5">
								<label class="control-label col-sm-2" for="search">Search</label>
							</div>
							<div class="col-sm-2">
								<INPUT name="STATEMENT_DATE" type = "text" value="" size="30" id="STATEMENT_DATE"  
								MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="STATEMENT DATE">
							</div>
							<div class="col-sm-2">
							</div>
							<%-- <div class="col-sm-4 ac-box">  		
					  			<input type="text" class="ac-selected form-control" id="pono" name="pono" 
					  			value="<%=StrUtils.forHTMLTag(pono)%>" placeholder="ORDER NO">
								<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'pono\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
					  		</div> --%>							
						</div>
						<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
					  		<div class="col-sm-2">
								<INPUT name="FROM_DATE" type = "text" value="" size="30"  id="FROM_DATE"
								MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
							</div>
							<div class="col-sm-2">
								<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" 
								size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
							</div>
							<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								 <input type="text" class="ac-selected form-control typeahead" id="vendname" 
								 placeholder="SUPPLIER" name="custName" >				
								<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'custName\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								<!-- <span class="btn-danger input-group-addon" 
								onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.custName.value);">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span>	 -->							
								</div>
							</div>
				  		</div>
				  		<!-- <div class="row" style="padding:3px">
				  			<div class="col-sm-2">
					  		</div>
					  		
					  		<div class="col-sm-4 ac-box">
								<div> 
								 <input type="text" class="ac-selected form-control typeahead" id="auto_billNo" 
								 placeholder="BILL NO" name="NAME" >				
								<span class="select-icon"  
								onclick="$(this).parent().find('input[name=\'"NAME"\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>							
								</div>
							</div>
				  		</div> -->
				  		<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-10 txn-buttons">								
								<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>							
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
			      <div class="col-sm-3">
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
		                      	<select id="searchType" class="form-control" style="height: 30px;">
		                        	<option value="all">All</option>
		                        	<option value="first">First</option>
		                           	<option value="center">Middle</option>
		                           	<option value="last">Last</option>
		                       		</select>
            				</div>
							
							<table id="tableBillAgingSummary" class="table table-bordred table-striped"> 
								<thead>
									<tr><th style="font-size: smaller;">S/N</th>
									<th style="font-size: smaller;">KEY</th>
									<th style="font-size: smaller;">SUPPLIER NAME</th>
									<th style="font-size: smaller;">BILL DATE</th>
									<th style="font-size: smaller;">BILL NO.</th>
									<th style="font-size: smaller;">SUPPLIER INVOICE NO.</th>
									<th style="font-size: smaller;">CURRENCY</th>
									<th style="font-size: smaller;">OVER DUE DAYS</th>
									<th style="font-size: smaller;">BILL AMOUNT</th>
									<th style="font-size: smaller;">OUTSTANDING</th></tr>
								</thead> 
								<tbody>
				 
				</tbody>
		                   <!-- IMTIZIAF -->
        <tfoot style="display: none;">
							<tr class="group">
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th style="text-align: left !important">Grand Total</th>
								<th style="text-align: right !important"></th>																																										
								<th style="text-align: right !important"></th>																																										
							</tr>
						</tfoot>
						<!-- END -->
							</table>
						</div>
					</div>
				</div>
			</form>
		</div>
	 </div>
</div>

<!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
        	<div class="btn-toolbar float-right"> 
        		<button type="button" class="btn btn-primary" >Print</button> 
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
          <object id="agingPdfOutput" type="application/pdf" style="height:500px;width:100%;">
            <p>It appears you don't have PDF support in this web browser. <a href="#" id="download-link">Click here to download the PDF</a>.</p>
		  </object>
        </div>
      </div>
      
    </div>
  </div>
  
  <div class="modal fade" id="consolidateModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
        	<h4 class="modal-title pull-left">Preview</h4>
        	<div class="btn-toolbar pull-right"> 
        		<button type="button" class="btn btn-success" onclick="onPrintConsolidated(true)">Print</button> 
        		<button type="button" class="btn btn-default" data-dismiss="modal"> Close </button>
       		</div>
        </div>
        <div class="modal-body">
          <object id="consolidatePdfOutput" type="application/pdf" style="height:500px;width:100%;">
            <p>It appears you don't have PDF support in this web browser. <a href="#" id="download-link">Click here to download the PDF</a>.</p>
		  </object>
        </div>
      </div>
      
    </div>
  </div>
  
  <div class="modal fade" id="outstandingModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
        	<h4 class="modal-title pull-left">Preview</h4>
        	<div class="btn-toolbar pull-right"> 
        		<button type="button" class="btn btn-success" onclick="onPrintOutstanding(true)">Print</button> 
        		<button type="button" class="btn btn-default" data-dismiss="modal"> Close </button>
       		</div>
        </div>
        <div class="modal-body">
          <object id="outstandingPdfOutput" type="application/pdf" style="height:500px;width:100%;">
            <p>It appears you don't have PDF support in this web browser. <a href="#" id="download-link">Click here to download the PDF</a>.</p>
		  </object>
        </div>
      </div>
      
    </div>
  </div>
  
  
<div hidden>
<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>" style="width: 130.00px;" id="logo_content">
<table id="table1">
</table>
<table id="table2">
</table>
<table id="table3">
</table>
<table id="table4">
</table>
</div>
<script>
var ageingDetails;
$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	getLocalStorageValue('billsAgingSummary_STATEMENT_DATE','', 'STATEMENT_DATE');
	//getLocalStorageValue('billsAgingSummary_FROM_DATE','', 'FROM_DATE');
	//getLocalStorageValue('billsAgingSummary_TO_DATE','', 'TO_DATE');
	getLocalStorageValue('billsAgingSummary_VENDNAME','', 'vendname');	
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
	
	$('#vendname').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		      	//return '<p onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')">' + data.VNAME + '</p>';
		    	return '<div onclick="document.form.vendno.value = \''+data.VENDO+'\' "><p class="item-suggestion">Name: ' + data.VNAME 
			    + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
			    + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
				  }
		}
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
				document.form.nTAXTREATMENT.value ="";
				document.getElementById('nTAXTREATMENT').innerHTML="";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
			}
			/* To reset Order number Autosuggestion*/
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
			$('#nTAXTREATMENT').attr('disabled',false);
			if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';
		});
});

var plant = document.form.plant.value;
var tableBillAgingSummary;
var FROM_DATE,TO_DATE,USER,ORDERNO,NAME,groupRowColSpan = 6;

function getParameters(){
	return {
		"FROM_DATE":FROM_DATE,"TO_DATE":TO_DATE,
		"vendorName":USER,"ORDERNO":ORDERNO,
		"NAME":NAME,
		"Submit": "GET_SUPPLIER_AGING_SUMMARY_VIEW",
		"PLANT":"<%=plant%>"
	}
}

function storeUserPreferences(){
	storeInLocalStorage('billsAgingSummary_STATEMENT_DATE', $('#STATEMENT_DATE').val());
	//storeInLocalStorage('billsAgingSummary_FROM_DATE', $('#FROM_DATE').val());
	//storeInLocalStorage('billsAgingSummary_TO_DATE', $('#TO_DATE').val());
	storeInLocalStorage('billsAgingSummary_VENDNAME', $('#vendname').val());
}

function onGo(){
	var flag = "false";
	FROM_DATE = document.form.FROM_DATE.value;
    TO_DATE = document.form.TO_DATE.value;		    
    USER = document.form.custName.value;
    /* ORDERNO = document.form.pono.value;
    NAME = document.form.NAME.value; */
	   
	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

	   if(USER != null    && USER != "") { flag = true;}
	   
	    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
	    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	    	storeUserPreferences();
	    }
	    var urlStr = "../AgeingServlet";
	   	var groupColumn = 1;
	   	var totalQty = 0;
	   	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	    <%-- if(FROM_DATE == null || FROM_DATE == "") {
	    	FROM_DATE = "<%=FROM_DATE%>";
	    } --%>
	    if (tableBillAgingSummary){
	    	tableBillAgingSummary.ajax.url( urlStr ).load();
	    }else{
	    	tableBillAgingSummary = $('#tableBillAgingSummary').DataTable({
				"processing": true,
				"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
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
			        	if(typeof data.items[0] === 'undefined'){
			        		return [];
			        	}else {				        		
			        		 for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
			        			data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
			        			data.items[dataIndex]['outStanding'] = parseFloat(data.items[dataIndex]['outStanding']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
			        		} 
			        		return data.items;
			        	}
			        }
			    },
		        "columns": [
		        	{"data": 'index', "orderable": true},
		        	{"data": 'key', "orderable": true},
	    			{"data": 'vendName', "orderable": true},
	    			{"data": 'billDate', "orderable": true},
	    			{"data": 'bill', "orderable": true},
	    			{"data": 'refnumber', "orderable": true},
	    			{"data": 'currency', "orderable": true},
	    			{"data": 'overDue', "orderable": true},
	    			{"data": 'amount', "orderable": true},
	    			{"data": 'outStanding', "orderable": true},  			
	    			],
				"columnDefs": [{"className": "t-right", "targets": [7,8,9]},{ "visible": false, "targets": 1 }],
				"orderFixed": [ 0, 'asc' ], 
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
		                columns: ':not(:eq(7)):not(:eq(8)):not(:last)'
		            }
		        ],
		        "drawCallback": function ( settings ) {
		            var api = this.api();
		            var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var groupEnd = 0;
		            var currentRow = 0;
		            var balance = 0;
		            var tbalance = 0;
		            var balancetot = 0;
		            var tbalancetot = 0;
		 
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {			            	
		                if ( last !== group ) {
		                	if (i > 0) {
			                    $(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="6"></td><td >Total</td><td class="t-right">'+parseFloat(balance).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td><td class="t-right">'+parseFloat(tbalance).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td></tr>'
			                    );
		               		}
	                	 	last = group;
	                	 	groupEnd = i;
	                	 	balance = 0;
	                	 	tbalance=0;
		            	}
		                floatbalance  = parseFloat($(rows).eq( i ).find('td:eq(7)').html().replaceAll(',',''));
		                tfloatbalance  = parseFloat($(rows).eq( i ).find('td:eq(8)').html().replaceAll(',',''));
		                //balance += parseFloat($(rows).eq( i ).find('td:eq(6)').html());
		                balance += floatbalance;
		                tbalance += tfloatbalance;
		                balancetot += floatbalance;
		                tbalancetot += tfloatbalance;
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	$(rows).eq(currentRow).after(
		                        '<tr class="group"><td colspan="6"></td><td >Grand Total</td><td class="t-right">'+parseFloat(balancetot).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td><td class="t-right">'+parseFloat(tbalancetot).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td></tr>'
	                    );
		            	$(rows).eq( currentRow ).after(
		            			'<tr class="group"><td colspan="6"></td><td >Total</td><td class="t-right">'+parseFloat(balance).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td><td class="t-right">'+parseFloat(tbalance).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+'</td></tr>'
	                    );
		            }
		        },"footerCallback": function(row, data, start, end, display) {
		            var api = this.api(),
		              data;

		            // Remove the formatting to get integer data for summation
		            var intVal = function(i) {
		              return typeof i === 'string' ?
		                i.replace(/[\$,]/g, '') * 1 :
		                typeof i === 'number' ?
		                i : 0;
		            };


		            // Total over this page
		            totalbalVal = api
		              .column(8)
		              .data()
		              .reduce(function(a, b) {
		                return intVal(a) + intVal(b);
		              }, 0);
		              
		            totalOrdVal = api
		              .column(9)
		              .data()
		              .reduce(function(a, b) {
		                return intVal(a) + intVal(b);
		              }, 0);
		              
		            // Update footer
		            $(api.column(8).footer()).html(parseFloat(totalbalVal).toFixed(<%=numberOfDecimal%>));
		            $(api.column(9).footer()).html(parseFloat(totalOrdVal).toFixed(<%=numberOfDecimal%>));
		          }
		        /* "footerCallback": function ( row, data, start, end, display ) {
		            var api = this.api(), data;
		            var intVal = function ( i ) {
		                return typeof i === 'string' ?
		                    i.replace(/[\$,]/g, '')*1 :
		                    typeof i === 'number' ?
		                        i : 0;
		            };
		         // Total over all pages
		            totalord = api
		                .column(6)
		                .data()
		                .reduce( function (a, b) {
		                    return intVal(a) + intVal(b);
		                }, 0 );		         
		            		         
		            $( api.column( 5 ).footer() ).html('Total');
		            $( api.column( 6 ).footer() ).html(addZeroes(parseFloat(totalord).toFixed(3)));		            		            
		        } */
			});
	    	 $("#tableBillAgingSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
		        	tableBillAgingSummary.draw();
		        });
	    }
	}

$('#tableBillAgingSummary').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableBillAgingSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableBillAgingSummary').attr('width', '100%');
});
	
function onPrint(dwnPdf){	
    var urlStr = "/track/AgeingServlet?Submit=PrintSupplierAgeingReport";
    var data = $("[name=form]").serialize();
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			ageingDetails = "";
			ageingDetails = data;
			generateAging(dwnPdf);
		}
	});
}
function onPrintOutstanding(dwnPdf){
    var urlStr = "/track/AgeingServlet?Submit=printAgeingReportOutstanding";
    document.form.ORDERNO.value = document.form.pono.value;
    var data = $("[name=form]").serialize();
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			ageingDetails = "";
			ageingDetails = data;
			generateOutstandingAging(dwnPdf);
		}
	});
}

function onPrintConsolidated(dwnPdf){
	var urlStr = "/track/AgeingServlet?Submit=PrintSupplierConsolidatedAgeingReport";
    var data = $("[name=form]").serialize();
    $.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			ageingDetails = "";
			ageingDetails = data;
			generateConsolidateAging(dwnPdf);
		}
	});
}

function generate() {
	
	var img = toDataURL($("#logo_content").attr("src"),
			function(dataUrl) {
				generatePdf(dataUrl);
		  	},'image/jpeg');
		
}

function generateAging(dwnPdf) {
	
	var img = toDataURL($("#logo_content").attr("src"),
		function(dataUrl) {
		generateAgingPdf(dataUrl,dwnPdf);
		},'image/jpeg'
	);
	
}

function generateConsolidateAging(dwnPdf) {
	
	var img = toDataURL($("#logo_content").attr("src"),
		function(dataUrl) {
		generateConsolidateAgingPdf(dataUrl,dwnPdf);
		},'image/jpeg'
	);
	
}

function generateOutstandingAging(dwnPdf) {
	
	var img = toDataURL($("#logo_content").attr("src"),
		function(dataUrl) {
		generateOutstandingAgingPdf(dataUrl,dwnPdf);
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
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	for(i=0;i<ageingDetails.reportContent.length;i++){
		var reportContent = ageingDetails.reportContent[i];
		if(i>0){
			doc.addPage();
		}
		
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
	/* Top Left */	
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
		doc.text(reportContent.lblstatement, 142, 40);
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
		
		/* To Address Start */
		doc.text(reportContent.lblHeader, 16, 72);
		doc.text(reportContent.To_ContactName, 16, 76);
		doc.text(reportContent.To_CompanyName, 16, 80);
		doc.text(reportContent.To_BlockAddress, 16, 84);
		doc.text(reportContent.To_RoadAddress, 16, 88);
		doc.text(reportContent.To_Country + " " + reportContent.To_ZIPCode , 16, 92);
		/* To Address End */
		
		/* var table2body = "";
		table2body += "<tbody>";
		table2body += "<tr>";
		table2body += "<td>"+reportContent.lblamtdue+"</td>";
		table2body += "<td>Enclosed</td>";
		table2body += "</tr>";
		table2body += "<tr>";
		table2body += "<td>"+reportContent.amountdue+"</td>";
		table2body += "<td></td>";
		table2body += "</tr>";
		table2body += "</tbody>";
		$("#table2").html("");
		$("#table2").html(table2body); 
		
		doc.autoTable({
			html : '#table2',
			startY : 80,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0}},
			theme : 'grid',
			styles: {
            lineColor: [0, 0, 0],
			
        }
		});*/
		doc.text("CURRENCY: " + reportContent.currency , 16, 105);
		var table3body = "";
		table3body += "<thead>";
		table3body += "<tr>";
		table3body += "<td>"+reportContent.lblDate+"</td>";
		table3body +="<td>"+reportContent.lblOrderNo+"</td>";
		table3body +="<td>"+reportContent.lblSuplierInvNo+"</td>";
		table3body +="<td>"+reportContent.lblDueDate+"</td>";
		table3body +="<td>"+reportContent.lblAmount+"</td>";
		table3body +="<td>"+reportContent.lblReceived+"</td>";
		table3body +="<td>"+reportContent.lblOverDueDays+"</td>";
		table3body += "<td>"+reportContent.lblBalance+"</td>";
		table3body += "<td>"+reportContent.lblCummBalance+"</td>";
		table3body += "</tr>";
		table3body += "<thead>";
		table3body += "<tbody>";
		for(j=0;j<ageingDetails.reportContentDetails[i].length;j++){
			table3body += "<tr>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].billDate+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].bill+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].refnumber+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].dueDate+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].amount+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].received+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].overDue+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].outStanding+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].cummBal+"</td>";
			table3body += "</tr>";
		}
		table3body += "</tbody>";		
		$("#table3").html("");
		$("#table3").html(table3body);		
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.autoTable({
			html : '#table3',
			startY : 110,
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
				fontSize: 7,						
			},
			columnStyles: {0: {halign: 'right'},1: {halign: 'right'},2: {halign: 'right'},3: {halign: 'right'},4: {halign: 'right'},5: {halign: 'right'},6: {halign: 'right'},7: {halign: 'right'},8: {halign: 'right'}},
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
		
		var table4body = "";
		table4body += "<tbody>";
		table4body += "<tr>";
		table4body += "<td>"+reportContent.lblTotalOS+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lblcurrentdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl30daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl60daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl90daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl90plusdaysdue+"("+reportContent.currency+")</td>";
		table4body += "</tr>";
		table4body += "<tr>";
		table4body += "<td class='t-right'>"+reportContent.amountdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.notDue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v30daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v60daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90plusdaysdue+"</td>";
		table4body += "</tr>";
		table4body += "</tbody>";
		$("#table4").html("");
		$("#table4").html(table4body);
		
		doc.autoTable({
			html : '#table4',
			columnStyles : {0 : {halign : 'right', textColor: 0},1 : {halign : 'right', textColor: 0},2 : {halign : 'right', textColor: 0},3 : {halign : 'right', textColor: 0},4 : {halign : 'right', textColor: 0},5 : {halign : 'right', textColor: 0}},
			theme : 'grid',
			styles: {
			lineColor: [0, 0, 0],
			fontSize: 9
			}
		});
		
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
							
					/*doc.autoTable({
						html : '#table4',
						startY : doc.internal.pageHeight - 50,
						columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0},2 : {halign : 'left', textColor: 0},3 : {halign : 'left', textColor: 0},4 : {halign : 'left', textColor: 0},5 : {halign : 'left', textColor: 0}},
						theme : 'grid',
						styles: {
						lineColor: [0, 0, 0],						
						}
					});*/
		
					doc.text(str, 16, pageHeight - 10);
		}
		
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
	}
	if(dwnPdf){
		doc.save('SupplierAgingReport.pdf');
	}else{
		var object = document.getElementById("agingPdfOutput");

		var clone = object.cloneNode(true);
		var parent = object.parentNode;

		parent.removeChild(object);
		parent.appendChild(clone);
		
		document.getElementById("agingPdfOutput").data = doc.output('datauristring');
		$('#agingModal').modal('show');
	}	
}

function generateConsolidateAgingPdf(dataUrl,dwnPdf){
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	for(i=0;i<ageingDetails.reportContent.length;i++){
		var reportContent = ageingDetails.reportContent[i];
		if(i>0){
			doc.addPage();
		}
		
		var table1body = "";
		table1body += "<tbody>";
		table1body += "<tr>";
		table1body += "<td>"+reportContent.lblDate+"</td>";
		table1body += "<td>"+reportContent.lblamtdue+"</td>";
		table1body += "</tr>";
		table1body += "<tr>";
		table1body += "<td>"+reportContent.CurDate+"</td>";
		table1body += "<td>"+reportContent.amountdue+"</td>";
		table1body += "</tr>";
		table1body += "</tbody>";
		$("#table1").html("");
		$("#table1").html(table1body);
	/* Top Left */	
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
		/*doc.text(reportContent.lblstatement, 162, 40);*/
		doc.autoTable({
			html : '#table1',
			startY : 80,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0}},
			theme : 'grid',
			styles: {
            lineColor: [0, 0, 0],
			
        }
		});
		
		/* To Address Start */
		/* doc.text(reportContent.lblHeader, 16, 72);
		doc.text(reportContent.To_ContactName, 16, 76);
		doc.text(reportContent.To_CompanyName, 16, 80);
		doc.text(reportContent.To_BlockAddress, 16, 84);
		doc.text(reportContent.To_RoadAddress, 16, 88);
		doc.text(reportContent.To_Country + " " + reportContent.To_ZIPCode , 16, 92); */
		/* To Address End */
		
		doc.text("CURRENCY: " + reportContent.currency , 16, 105);
		
		var table3body = "";
		table3body += "<thead>";
		table3body += "<tr>";
		table3body += "<td>"+reportContent.lblDate+"</td>";
		table3body +="<td>"+reportContent.lblOrderNo+"</td>";
		table3body +="<td>"+reportContent.lblSuplierInvNo+"</td>";
		table3body +="<td>"+reportContent.lblDueDate+"</td>";
		table3body +="<td>"+reportContent.lblAmount+"</td>";
		table3body +="<td>"+reportContent.lblReceived+"</td>";
		table3body +="<td>"+reportContent.lblOverDueDays+"</td>";
		table3body += "<td>"+reportContent.lblBalance+"</td>";
		table3body += "<td>"+reportContent.lblCummBalance+"</td>";
		table3body += "</tr>";
		table3body += "<thead>";
		table3body += "<tbody>";
		for(j=0;j<ageingDetails.reportContentDetails[i].length;j++){
			table3body += "<tr>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].billDate+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].bill+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].refnumber+"</td>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].dueDate+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].amount+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].received+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].overDue+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].outStanding+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].cummBal+"</td>";
			table3body += "</tr>";
		}
		table3body += "</tbody>";		
		$("#table3").html("");
		$("#table3").html(table3body);		
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.autoTable({
			html : '#table3',
			startY : 110,
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center'
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ]
			},
			styles: {
	            fontSize: 7,
        	},
			theme : 'plain',
			columnStyles: {0: {halign: 'left'},1: {halign: 'left'},2: {halign: 'left'},3: {halign: 'right'},4: {halign: 'right'},5: {halign: 'center'},6: {halign: 'right'},7: {halign: 'right'},8: {halign: 'right'}},
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
				/*doc.text(str, data.settings.margin.left,
						pageHeight - 10);*/
			}
		});
		
		var table4body = "";
		table4body += "<tbody>";
		table4body += "<tr>";
		table4body += "<td>"+reportContent.lblTotalOS+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lblcurrentdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl30daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl60daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl90daysdue+"("+reportContent.currency+")</td>";
		table4body += "<td>"+reportContent.lbl90plusdaysdue+"("+reportContent.currency+")</td>";
		table4body += "</tr>";
		table4body += "<tr>";
		table4body += "<td class='t-right'>"+reportContent.amountdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.notDue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v30daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v60daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90plusdaysdue+"</td>";
		table4body += "</tr>";
		table4body += "</tbody>";
		$("#table4").html("");
		$("#table4").html(table4body);
		
		doc.autoTable({
			html : '#table4',
			columnStyles : {0 : {halign : 'right', textColor: 0},1 : {halign : 'right', textColor: 0},2 : {halign : 'right', textColor: 0},3 : {halign : 'right', textColor: 0},4 : {halign : 'right', textColor: 0},5 : {halign : 'right', textColor: 0}},
			theme : 'grid',
			styles: {
			lineColor: [0, 0, 0],	
			fontSize: 9
			}
		});
		
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
							
					/*doc.autoTable({
						html : '#table4',
						startY : doc.internal.pageHeight - 50,
						columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0},2 : {halign : 'left', textColor: 0},3 : {halign : 'left', textColor: 0},4 : {halign : 'left', textColor: 0},5 : {halign : 'left', textColor: 0}},
						theme : 'grid',
						styles: {
						lineColor: [0, 0, 0],						
						}
					});*/
		
					doc.text(str, 16, pageHeight - 10);
		}
		
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
	}
	if(dwnPdf){
		doc.save('SupplierConsolidateAgingReport.pdf');
	}else{
		var object = document.getElementById("consolidatePdfOutput");

		var clone = object.cloneNode(true);
		var parent = object.parentNode;

		parent.removeChild(object);
		parent.appendChild(clone);
		
		document.getElementById("consolidatePdfOutput").data = doc.output('datauristring');
		$('#consolidateModal').modal('show');
	}	
}

function generateOutstandingAgingPdf(dataUrl,dwnPdf){
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	for(i=0;i<ageingDetails.reportContent.length;i++){
		var reportContent = ageingDetails.reportContent[i];
		if(i>0){
			doc.addPage();
		}
		
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
	/* Top Left */	
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
		
		/* To Address Start */
		doc.text(reportContent.lblHeader, 16, 72);
		doc.text(reportContent.To_ContactName, 16, 76);
		doc.text(reportContent.To_CompanyName, 16, 80);
		doc.text(reportContent.To_BlockAddress, 16, 84);
		doc.text(reportContent.To_RoadAddress, 16, 88);
		doc.text(reportContent.To_Country + " " + reportContent.To_ZIPCode , 16, 92);
		/* To Address End */
		
		var table2body = "";
		table2body += "<tbody>";
		table2body += "<tr>";
		table2body += "<td>"+reportContent.lblamtdue+"</td>";
		table2body += "<td>Enclosed</td>";
		table2body += "</tr>";
		table2body += "<tr>";
		table2body += "<td>"+reportContent.amountdue+"</td>";
		table2body += "<td></td>";
		table2body += "</tr>";
		table2body += "</tbody>";
		$("#table2").html("");
		$("#table2").html(table2body);
		
		doc.autoTable({
			html : '#table2',
			startY : 80,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0}},
			theme : 'grid',
			styles: {
            lineColor: [0, 0, 0],
			
        }
		});
		
		var table3body = "";
		table3body += "<thead>";
		table3body += "<tr>";
		table3body += "<td>"+reportContent.lblDate+"</td>";
		table3body +="<td>"+reportContent.lblOrderNo+"</td>";
		table3body +="<td>"+reportContent.lblAmount+"</td>";
		table3body += "<td>"+reportContent.lblBalance+"</td>";
		table3body += "</tr>";
		table3body += "<thead>";
		table3body += "<tbody>";
		for(j=0;j<ageingDetails.reportContentDetails[i].length;j++){
			table3body += "<tr>";
			table3body += "<td>"+ageingDetails.reportContentDetails[i][j].trandate+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].bill+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].amount+"</td>";
			table3body += "<td class='t-right'>"+ageingDetails.reportContentDetails[i][j].balance+"</td>";
			table3body += "</tr>";
		}
		table3body += "</tbody>";		
		$("#table3").html("");
		$("#table3").html(table3body);		
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.autoTable({
			html : '#table3',
			startY : 110,
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
			columnStyles: {0: {halign: 'left'},1: {halign: 'center'},2: {halign: 'left'},3: {halign: 'left'}},
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
				/*doc.text(str, data.settings.margin.left,
						pageHeight - 10);*/
			}
		});
		
		var table4body = "";
		table4body += "<tbody>";
		table4body += "<tr>";
		table4body += "<td>"+reportContent.lblcurrentdue+"</td>";
		table4body += "<td>"+reportContent.lbl30daysdue+"</td>";
		table4body += "<td>"+reportContent.lbl60daysdue+"</td>";
		table4body += "<td>"+reportContent.lbl90daysdue+"</td>";
		table4body += "<td>"+reportContent.lbl90plusdaysdue+"</td>";
		table4body += "</tr>";
		table4body += "<tr>";
		table4body += "<td class='t-right'>"+reportContent.currentdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v30daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v60daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90daysdue+"</td>";
		table4body += "<td class='t-right'>"+reportContent.v90plusdaysdue+"</td>";
		table4body += "</tr>";
		table4body += "</tbody>";
		$("#table4").html("");
		$("#table4").html(table4body);
		
		doc.autoTable({
			html : '#table4',
			columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0},2 : {halign : 'left', textColor: 0},3 : {halign : 'left', textColor: 0},4 : {halign : 'left', textColor: 0},5 : {halign : 'left', textColor: 0}},
			theme : 'grid',
			styles: {
			lineColor: [0, 0, 0],						
			}
		});
		
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
							
					/*doc.autoTable({
						html : '#table4',
						startY : doc.internal.pageHeight - 50,
						columnStyles : {0 : {halign : 'left', textColor: 0},1 : {halign : 'left', textColor: 0},2 : {halign : 'left', textColor: 0},3 : {halign : 'left', textColor: 0},4 : {halign : 'left', textColor: 0},5 : {halign : 'left', textColor: 0}},
						theme : 'grid',
						styles: {
						lineColor: [0, 0, 0],						
						}
					});*/
		
					doc.text(str, 16, pageHeight - 10);
		}
		
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
	}
	if(dwnPdf){
		doc.save('SupplierOutstandingAgingReport.pdf');
	}else{
		var object = document.getElementById("outstandingPdfOutput");

		var clone = object.cloneNode(true);
		var parent = object.parentNode;

		parent.removeChild(object);
		parent.appendChild(clone);
		
		document.getElementById("outstandingPdfOutput").data = doc.output('datauristring');
		$('#outstandingModal').modal('show');
	}	
}

function getvendname(TAXTREATMENT,VENDO){
	//document.getElementById('nTAXTREATMENT').innerHTML = TAXTREATMENT;
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form.vendno.value =VENDO;
	if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
}
</script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>