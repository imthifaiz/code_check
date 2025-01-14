<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Goods Issued Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String dono = StrUtils.fString(request.getParameter("DONO"));
String gino = StrUtils.fString(request.getParameter("GINO"));
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
String custno = StrUtils.fString(request.getParameter("CUSTNO"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="";
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
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
boolean displaySummaryLink=true,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkginotoinvoice", plant,username);
displaySummaryExport = ub.isCheckValAcc("exportlnkginotoinvoice", plant,username);
}
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.GOODS_ISSUED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script src="js/Invoice.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Goods Issued Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>             
              </h1>
		</div> 
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="curency" value="<%=curency%>" hidden>
		<input type="hidden" name="cmd" value="" />
		<input type="hidden" name="CUST_CODE" value="" />
		<!-- <input type="hidden" name="ORDERNO" value="" /> -->
		<input type="hidden" name="DONO" value="" />
		
		<INPUT type="hidden" name="TranId" value="" />
		<INPUT type="hidden" name="STATE_PREFIX" value="" />
		<input type="hidden" value="" name="displayCustomerpop" id="displayCustomerpop" />
		<div class="taxDetails" style="display: none"></div>
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
				<input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER" value="<%=CUSTOMER%>">				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" placeholder="ORDER NO" value="<%=dono%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="gino" name="gino" placeholder="GINO" value="<%=gino%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'gino\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>  		
  		</div> 
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="plno" name="plno" placeholder="PACKING LIST">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'plno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="invoicestatus" id="invoicestatus" class="ac-selected form-control invoicestatus" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoicestatus\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
              <table id="tableBillSummary" class="table table-bordred table-striped">                   
                   <thead>
	                   <th style="font-size: smaller;">DATE</th>
	                   <th style="font-size: smaller;">DONO</th>
	                   <th style="font-size: smaller;">GINO</th>
	                   <th style="font-size: smaller;">CUSTOMER NAME</th>                     
	                   <th style="font-size: smaller;">STATUS</th>
	                   <th style="font-size: smaller;">GINO QTY</th>
	                   <th style="font-size: smaller;">INVOICED QTY</th>
	                   <th style="font-size: smaller;">RETURNED QTY</th>
	                   <!-- <th style="font-size: smaller;">AMOUNT</th> -->
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><!-- <th></th> --></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tableBillSummary;
		 var FROM_DATE,TO_DATE,USER,CUSTOMER,CURENCY,ORDERNO,GINO,STATUS,PLNO, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,
				"CUSTOMER":CUSTOMER,"CURENCY":CURENCY,"ORDERNO":ORDERNO,"GINO":GINO,"STATUS":STATUS,"PLNO":PLNO,
				"ACTION": "VIEW_GINOTOINVOICE_SUMMARY",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form1.FROM_DATE.value;
		    TO_DATE        = document.form1.TO_DATE.value;		    
		    CUSTOMER         = document.form1.CUSTOMER.value;		    
		    CURENCY         = document.form1.curency.value;
		    ORDERNO        = document.form1.ORDERNO.value;
		    GINO        = document.form1.gino.value;
		    STATUS        = document.form1.invoicestatus.value;
		    PLNO		= document.form1.plno.value;
		   	    		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
		   if(CUSTOMER != null    && CUSTOMER != "") { flag = true;}
		   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
		   if(GINO != null     && GINO != "") { flag = true;}
		   if(STATUS != null     && STATUS != "") { flag = true;}
		   if(PLNO != null     && PLNO != "") { flag = true;}
		    
		    var urlStr = "../InvoiceServlet";
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
				        	if(typeof data.items[0].dono === 'undefined'){
				        		return [];
				        	}else {				        		
				        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        				<%if(displaySummaryLink){ %>
				        					data.items[dataIndex]['gino'] = '<a href="../goodsissued/detail?action=View&DONO=' +data.items[dataIndex]['dono']+ '&GINO=' +data.items[dataIndex]['gino']+ '&CUST_NAME=' +data.items[dataIndex]['cust_name']+ '&CUSTNO=' +data.items[dataIndex]['custno']+ '">'+data.items[dataIndex]['gino']+'</a>';
									<%}else{%>
									data.items[dataIndex]['dono'] = data.items[dataIndex]['dono'];			
									<%}%>					        		
				        			data.items[dataIndex]['qty'] =addZeroes(parseFloat(data.items[dataIndex]['qty']).toFixed(3));
				        			data.items[dataIndex]['invoicedqty'] =addZeroes(parseFloat(data.items[dataIndex]['invoicedqty']).toFixed(3));
				        			data.items[dataIndex]['returnedqty'] =addZeroes(parseFloat(data.items[dataIndex]['returnedqty']).toFixed(3));
				        			data.items[dataIndex]['amount'] =data.items[dataIndex]['currency'] + addZeroes(parseFloat(data.items[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>));
				        			if(data.items[dataIndex]['status']=='INVOICED')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='NOT INVOICED')
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
		    			{"data": 'dono', "orderable": true},
		    			{"data": 'gino', "orderable": true},
		    			{"data": 'cust_name', "orderable": true},		    			
		    			{"data": 'status', "orderable": true},
		    			{"data": 'qty', "orderable": true},
		    			{"data": 'invoicedqty', "orderable": true},
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
			            $('.buttons-collection')[0].style.visibility = 'hidden';
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


		  function getcustname(CUSTNO){
				
			}


		  var postatus =   [{
			    "year": "INVOICED",
			    "value": "INVOICED",
			    "tokens": [
			      "INVOICED"
			    ]
			  },
			  {
				    "year": "NOT INVOICED",
				    "value": "NOT INVOICED",
				    "tokens": [
				      "NOT INVOICED"
				    ]
				  },
				  {
					    "year": "NON INVOICEABLE",
					    "value": "NON INVOICEABLE",
					    "tokens": [
					      "NON INVOICEABLE"
					    ]
					  }];

					  

		  var substringMatcher = function(strs) {
			  return function findMatches(q, cb) {
			    var matches, substringRegex;
			    // an array that will be populated with substring matches
			    matches = [];
			    // regex used to determine if a string contains the substring `q`
			    substrRegex = new RegExp(q, 'i');
			    // iterate through the pool of strings and for any string that
			    // contains the substring `q`, add it to the `matches` array
			    $.each(strs, function(i, str) {
			      if (substrRegex.test(str.value)) {
			        matches.push(str);
			      }
			    });
			    cb(matches);
			  };
		};
		function getTable(){
		   return '<TABLE>'+
		          '<TR>'+
		          '<TH><font color="#ffffff" align="left"><b>DATE</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>DONO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>GINO</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>CUSTOMER NAME</TH>'+
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

 /* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
//		    	return '<p onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
		    	return '<div onclick="getcustname(\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
			$(".customerAddBtn").width(menuElement.width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.CUST_CODE.value = "";
				document.form1.nTAXTREATMENT.value ="";
				document.getElementById('nTAXTREATMENT').innerHTML="";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
			}
			$('#nTAXTREATMENT').attr('disabled',false);
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						CNAME : document.form1.CUSTOMER.value,
						DONO : query
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
		    return '<p>' + data.DONO + '</p>';
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#invoice").typeahead('val', '"');
			$("#invoice").typeahead('val', '');
			$("#auto_invoiceNo").typeahead('val', '"');
			$("#auto_invoiceNo").typeahead('val', '');
			$("#plno").typeahead('val', '"');
			$("#plno").typeahead('val', '');
		});

	$('#gino').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICENO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_INVOICENO_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.INVOICENO_MST);
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
			return '<div><p class="item-suggestion">'+data.INVOICENO+'</p></div>';
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
	
	/* To get the suggestion data for Status */
	$("#invoicestatus").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
	
 



	 
 });
 </script>
 

<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>