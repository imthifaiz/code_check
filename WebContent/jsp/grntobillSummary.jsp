<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Goods Receipt Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String pono = StrUtils.fString(request.getParameter("PONO"));
String grno = StrUtils.fString(request.getParameter("GRNO"));
String vendname = StrUtils.fString(request.getParameter("VEND_NAME"));
String vendno = StrUtils.fString(request.getParameter("VENDNO"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="";
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryNew=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryLink = ub.isCheckValAcc("summarylnkgrn", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportgrn", plant,username);
}
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
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
//RESVI 
String curDate =DateUtils.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.RECEIPT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/Bill.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                            
                <li><label>Goods Receipt Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              
             <%-- <div class="box-title pull-right">
              <button type="button" class="btn btn-default" onClick="window.location.href='createBill.jsp'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
              </div> --%>
		</div> 
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form" method="post" action="" >
		<input type="hidden" name="plant" value="<%=plant%>" >
		<input type="hidden" name="curency" value="<%=curency%>" >
		<INPUT type="hidden" name="STATE_PREFIX" value="" />
		<INPUT type="hidden" name="vendno" value="" >
		<div id="CHK1"></div>
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" id="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
<!-- 				<div class="input-group">  -->
				<input type="text" class="ac-selected form-control" id="billvendname" placeholder="SUPPLIER" name="vendname" value="<%=vendname%>">				
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'billvendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 				</div> -->
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="pono" name="pono" placeholder="ORDER NO" value="<%=pono%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="grno" name="grno" placeholder="GRNO" value="<%=grno%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'grno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="billstatus" id="billstatus" class="ac-selected form-control billstatus" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'billstatus\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
		</FORM>
		<div class="form-group">
      <div class="row">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; ">Hide Advanced Search</a>
      </div>
      </div>
      <div class="ShowSingle">
        </div>
       	  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableBillSummary" class="table table-bordred table-striped">                   
                   <thead>
	                   <tr>
	                   	<th style="font-size: smaller;">DATE</th>
	                   <th style="font-size: smaller;">PONO</th>
	                   <th style="font-size: smaller;">GRNO</th>
	                   <th style="font-size: smaller;">SUPPLIER NAME</th>                     
	                   <th style="font-size: smaller;">STATUS</th>
	                   <th style="font-size: smaller;">GRN QTY</th>
	                   <th style="font-size: smaller;">BILLED QTY</th>
	                   <th style="font-size: smaller;">RETURNED QTY</th>
	                   <!-- <th style="font-size: smaller;">AMOUNT</th> -->
	                   </tr>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><!-- <th></th> --></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</div>
		</div>
		</div>
		<script>
		var plant = document.form.plant.value;
		var tableBillSummary;
		 var FROM_DATE,TO_DATE,USER,SUPPLIER,CURENCY,ORDERNO,GRNO,STATUS, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"SUPPLIER":SUPPLIER,"CURENCY":CURENCY,"ORDERNO":ORDERNO,"GRNO":GRNO,"STATUS":STATUS,
				"ACTION": "VIEW_GRNOTOBILL_SUMMARY",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form.FROM_DATE.value;
		    TO_DATE        = document.form.TO_DATE.value;		    
		    SUPPLIER         = document.form.vendname.value;		    
		    CURENCY         = document.form.curency.value;
		    ORDERNO        = document.form.pono.value;
		    GRNO        = document.form.grno.value;
		    STATUS        = document.form.billstatus.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(SUPPLIER != null    && SUPPLIER != "") { flag = true;}
		   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
		   if(GRNO != null     && GRNO != "") { flag = true;}
		   if(STATUS != null     && STATUS != "") { flag = true;}

		   /* storeInLocalStorage('grntobillSummary_FROMDATE', FROM_DATE);
			storeInLocalStorage('grntobillSummary_TODATE', TO_DATE);
			storeInLocalStorage('grntobillSummary_vendname', SUPPLIER);
			storeInLocalStorage('grntobillSummary_pono', ORDERNO);
			storeInLocalStorage('grntobillSummary_grno', GRNO);
			storeInLocalStorage('grntobillSummary_billstatus', STATUS); */	
		    var urlStr = "../BillingServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableBillSummary){
		    	tableBillSummary.ajax.url( urlStr ).load();
		    }else{
			    tableBillSummary = $('#tableBillSummary').DataTable({
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
				        	if(typeof data.items[0].pono === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['grno'] = '<a href="../goods/detail?action=View&PONO=' +data.items[dataIndex]['pono']+ '&GRNO=' +data.items[dataIndex]['grno']+ '&VEND_NAME=' +data.items[dataIndex]['vendname']+ '&VENDNO=' +data.items[dataIndex]['vendno']+ '">'+data.items[dataIndex]['grno']+'</a>';
				        			<% }%>
				        			data.items[dataIndex]['qty'] =addZeroes(parseFloat(data.items[dataIndex]['qty']).toFixed(3));
				        			data.items[dataIndex]['billedqty'] =addZeroes(parseFloat(data.items[dataIndex]['billedqty']).toFixed(3));
				        			data.items[dataIndex]['returnedqty'] =addZeroes(parseFloat(data.items[dataIndex]['returnedqty']).toFixed(3));
				        			data.items[dataIndex]['amount'] =data.items[dataIndex]['currency'] + addZeroes(parseFloat(data.items[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>));
				        			if(data.items[dataIndex]['status']=='BILLED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='NOT BILLED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'date', "orderable": true},
		    			{"data": 'pono', "orderable": true},
		    			{"data": 'grno', "orderable": true},
		    			{"data": 'vendname', "orderable": true},		    			
		    			{"data": 'status', "orderable": true},
		    			{"data": 'qty', "orderable": true},
		    			{"data": 'billedqty', "orderable": true},
		    			{"data": 'returnedqty', "orderable": true},
		    			/* {"data": 'amount', "orderable": true}, */
		    			
		    			],
		    			"columnDefs": [{"orderSequence":["desc"], "targets": [0]},{"orderable":!1}],
					/* "columnDefs": [{"className": "t-right", "targets": [8]}],
					"orderFixed": [ ], */ 
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

		$('#tableBillSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tableBillSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tableBillSummary').attr('width', '100%');
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
		          '<TH><font color="#ffffff" align="left"><b>DATE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>PONO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>GRNO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>SUPPLIER NAME</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>STATUS</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>QUANTITY </TH>'+
				  /* '<TH><font color="#ffffff" align="left"><b>AMOUNT</TH>'+ */
		           '</TR>';
		              
		}
	
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
	 /* getLocalStorageValue('grntobillSummary_FROMDATE', $('#FROM_DATE').val(), 'FROM_DATE');
	 getLocalStorageValue('grntobillSummary_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('grntobillSummary_vendname', '', 'billvendname');
	 getLocalStorageValue('grntobillSummary_pono', '', 'pono');
	 getLocalStorageValue('grntobillSummary_grno', '', 'grno');
	 getLocalStorageValue('grntobillSummary_billstatus', '', 'billstatus'); */
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