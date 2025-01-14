<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Product Receive Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
if ("".equals(fieldDesc)){
	fieldDesc = StrUtils.fString(request.getParameter("msg"));
}
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",ORDERNO="",CUSTOMER="",PGaction="",invoice="",STATUS="";

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryLink = ub.isCheckValAcc("summarylnkproductreceive", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportproductreceive", plant,username);
}
if(systatus.equalsIgnoreCase("INVENTORY")) {
	displaySummaryLink = ub.isCheckValinv("summarylnkproductreceive", plant,username);
	displaySummaryExport = ub.isCheckValinv("exportproductreceive", plant,username);
}
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
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
//RESVI
String curDate =du.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRODUCT_RECEIVE%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/ProductReceive.js"></script>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Product Receive Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              </h1>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="cmd" value="<%="cmd"%>" hidden>
		<input type="text" name="TranId" value="<%="TranId"%>" hidden>
		<input type="text" name="curency" value="<%="curency"%>" hidden>
		<input type="text" name="CUST_CODE" hidden>
		<input type="text" name="nTAXTREATMENT" hidden>
		<input type="text" name="STATE_PREFIX" hidden>
		<INPUT type="hidden" name="ORIGIN">
		<INPUT type="hidden" name="DEDUCT_INV"> 
		<input type="text" name="invoice" hidden>
		<INPUT type="hidden" name="EDUCATION"> 
		<input hidden name="displayCustomerpop" id="displayCustomerpop" />
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
				
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="RECEIVE NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="item" id="item" class="ac-selected form-control" placeholder="PRODUCT" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>  		
  		</div>
  		</div>
  		<div class="row" style="display:none">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="plnoInvoice" name="plno" placeholder="PACKING LIST">
		<span class="select-icon" onclick="$(this).parent().find('input[name=\'plno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="auto_invoiceNo" name="invoiceno" placeholder="INVOICE NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoiceno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
  		<div class="col-sm-4 ac-box">
  		<INPUT id="EMP_NAME_SRH" name="EMP_NAME" type = "TEXT" class="ac-selected form-control" placeholder="EMPLOYEE">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" name="status" id="status" class="ac-selected form-control" placeholder="STATUS" >
  				<span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>	
			</div>
		</div>
		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
  	<div class="col-sm-4 ac-box">
  		<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
  		</div>
  	
  		<div class="col-sm-4 ac-box" style="display:none">
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE_SRH" type= "TEXT"  class="ac-selected form-control" placeholder="ORDERTYPE" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<!-- <div class="col-sm-4 ac-box">
				
				
				
			</div> -->
		</div>
  		</div>
		</div>
		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableInventorySummary" class="table table-bordred table-striped">                   
                   <thead>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">PRODUCT RECEIVE NO</th>
                     <th style="font-size: smaller;">CUSTOMER NAME</th>
                     <th style="font-size: smaller;">STATUS</th>
                     <th style="font-size: smaller;">CURRENCY</th>
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                   </thead>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tableInventorySummary;
		var numberOfDecimal = "<%=numberOfDecimal%>";
		var numberOfDecimalqty = "<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>";
		 var FROM_DATE,TO_DATE,USER,ORDERNO,ITEM,INVOICENO,STATUS,PLNO,EMP_NAME,ORDERTYPE, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,"EMP_NAME":EMP_NAME,"ORDERTYPE":ORDERTYPE,
				"CNAME":USER,"ORDERNO":ORDERNO,"ITEM":ITEM,"INVOICENO":INVOICENO,"STATUS":STATUS,"PLNO":PLNO,					
				"ACTION": "VIEW_RETURN_SUMMARY_VIEW",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form1.FROM_DATE.value;
		    TO_DATE        = document.form1.TO_DATE.value;		    
		    USER           = document.form1.CUSTOMER.value;
		    ORDERNO        = document.form1.ORDERNO.value;
		    ITEM        = document.form1.item.value;
		    INVOICENO        = document.form1.invoiceno.value;
		    STATUS        = document.form1.status.value;
		    PLNO		= document.form1.plno.value;
		    EMP_NAME	= document.form1.EMP_NAME.value;
		    ORDERTYPE	= document.form1.ORDERTYPE.value;
		    
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

		   if(USER != null    && USER != "") { flag = true;}
		   
		    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
		    if(ITEM != null     && ITEM != "") { flag = true;}
		    if(INVOICENO != null     && INVOICENO != "") { flag = true;}
		    if(STATUS != null     && STATUS != "") { flag = true;}
		    if(PLNO != null     && PLNO != "") { flag = true;}
		    if(EMP_NAME != null     && EMP_NAME != "") { flag = true;}
		    if(ORDERTYPE != null     && ORDERTYPE != "") { flag = true;}
		    storeUserPreferences();
		    var urlStr = "../productreceive";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableInventorySummary){
		    	tableInventorySummary.ajax.url( urlStr ).load();
		    }else{
			    tableInventorySummary = $('#tableInventorySummary').DataTable({
					"processing": true,
					"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 					"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.items[0].bill === 'undefined'){
				        		return [];
				        	}else {				        		
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			
				        			data.items[dataIndex]['exchangerate'] = parseFloat(data.items[dataIndex]['exchangerate']).toFixed(numberOfDecimalqty).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        			data.items[dataIndex]['amount'] = parseFloat(data.items[dataIndex]['amount']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['bill'] = '<a href="../productreceive/detail?&custno=' +data.items[dataIndex]['custno']+'&PRODUCT_HDR=' +data.items[dataIndex]['invoiceid']+'">'+data.items[dataIndex]['bill']+'</a>';
				        			<% }else{ %>
 				        			data.items[dataIndex]['bill'] = data.items[dataIndex]['bill'];
				        			<% } %>
				        			if(data.items[dataIndex]['status']=='PROCESSED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='Open')
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else if(data.items[dataIndex]['status']=='Draft')
						        		data.items[dataIndex]['status'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'billdate', "orderable": true},
		    			{"data": 'bill', "orderable": true},
		    			{"data": 'custname', "orderable": true},
		    			{"data": 'status', "orderable": true},
		    			{"data": 'currency', "orderable": true},
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [5,6,7]}],
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
			        drawCallback: function() {
			        	<%if(!displaySummaryExport){ %>
			        	$('.buttons-collection')[0].style.display = 'none';
			        	<% } %>
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
					
			        
				}else{
			}
		      outPutdata = outPutdata +'</TABLE>';
		      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		       document.getElementById('spinnerImg').innerHTML ='';

		   
		 }

		function getTable(){
		   return '<TABLE>'+
		          '<TR>'+
		          '<TH><font color="#ffffff" align="left"><b>Date</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Invoice</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Reference Number</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Customer Name</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Status</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Due Date</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Amount</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Balance Due</TH>'+
		          '</TR>';
		              
		}
		
		
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
	 if (document.form1.ORDERTYPE.value == ''){
		   getLocalStorageValue('viewInvoiceSummary','', 'ORDERTYPE_SRH');
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
 });
 function storeUserPreferences(){
		storeInLocalStorage('viewInvoiceSummary', $('#ORDERTYPE_SRH').val());
 }
 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>