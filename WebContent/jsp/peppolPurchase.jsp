<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! 
	@SuppressWarnings({"rawtypes"}) 
%>
<%
String title = "Summary Screen for Purchase Invoice From Peppol";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String fieldDesc = StrUtils.fString(request.getParameter("result"));
if ("".equals(fieldDesc)){
	fieldDesc = StrUtils.fString(request.getParameter("msg"));
}
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",pono="",vendname="",PGaction="";
String SORT = StrUtils.fString(request.getParameter("SORT"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//RESVI 
String curDate =DateUtils.getDateMinusDays();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.INTEGRATIONS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PEPPOL%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../integrations/peppolintegration"><span class="underline-on-hover">Peppol Integration</span> </a></li>                            
                <li><label>Summary Screen for Purchase Invoice to Peppol</label></li>                                   
            </ul>             
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
               <div class="btn-group" role="group">
              </div>
               <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../integrations/peppolintegration'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
		</div>
		</div>
		<div class="container-fluid">
		<form class="form-horizontal" name="form" method="post" action="" >
		<input type="hidden" name="plant" value="<%=plant%>">
		<input type="hidden" name="STATE_PREFIX" value="" >
		<input type="hidden" name="vendno" value="" >
		<input type="hidden" name="nTAXTREATMENT" value="" >
		<div id="CHK1" ></div>
		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" id="FROM_DATE" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<label class="radio-inline">Peppol Status </label>
  		<label class="radio-inline"><INPUT name="SORT" type = "radio"  value="Retrieved"   <%if(SORT.equalsIgnoreCase("Retrieved")) {%>checked <%}%>>Retrieved</label>
      	<label class="radio-inline"><INPUT  name="SORT" type = "radio" value="NotRetrieved"    <%if(SORT.equalsIgnoreCase("NotRetrieved")) {%>checked <%}%>>Not Retrieved</label>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="billstatus" id="billstatus" class="ac-selected form-control billstatus" placeholder="DISPLAY" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'billstatus\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="status" id="status" class="ac-selected form-control status" placeholder="PAYMENT STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
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
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
		
		<div class="container-fluid">
		<div class="row">
		<div class="col-sm-12">
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row">
		<div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableInventorySummary" class="table table-bordred table-striped">                   
                   <thead>
                   <tr><th style="font-size: smaller;">DOC ID</th>
                   <th style="font-size: smaller;">BILL NO</th>
                     <th style="font-size: smaller;">RECIVED AT</th>
                     <th style="font-size: smaller;">EXPIRES AT</th>
                     <th style="font-size: smaller;">PEPPOL STATUS</th>
                     <th style="font-size: smaller;">PEPPOL ACTION</th>
                     </tr>
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th>
		<th></th>
		<th></th>
		<th></th>
		<th></th>
		<th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</div>
		<script>
		var plant = document.form.plant.value;
		var tableInventorySummary;
		var numberOfDecimal = "<%=numberOfDecimal%>";
		 var FROM_DATE,TO_DATE,USER,ORDERNO,ITEM,BILL,GRNO,REFERENCE,STATUS, groupRowColSpan = 5;
		function getParameters(){
			return {
				"Submit": "GET_PPPOL_RECEIVED_DATA",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form.FROM_DATE.value;
		    TO_DATE        = document.form.TO_DATE.value;		    
		    STATUS        = document.form.status.value;
		   
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

		   if(USER != null    && USER != "") { flag = true;}
		   
		    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
			if(ITEM != null     && ITEM != "") { flag = true;}
		    var urlStr = "../peppolreceive";
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
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.PEPPOLRECEIVEDDATALIST[0].ID === 'undefined'){
				        		return [];
				        	}else {				        		
				        		for(var dataIndex = 0; dataIndex < data.PEPPOLRECEIVEDDATALIST.length; dataIndex ++){
				        			
				        			if(data.PEPPOLRECEIVEDDATALIST[dataIndex]['BILL_STATUS']=='1'){
					        			data.PEPPOLRECEIVEDDATALIST[dataIndex]['BILL_STATUS']='RETRIEVED';
				        			data.PEPPOLRECEIVEDDATALIST[dataIndex]['BTNINVOICE'] = 'BILLED';
				        			}else{
					        			data.PEPPOLRECEIVEDDATALIST[dataIndex]['BILL_STATUS']='NOT RETRIEVED';
					        			data.PEPPOLRECEIVEDDATALIST[dataIndex]['BTNINVOICE'] = '<BUTTON  name=btninvoice value="'+data.PEPPOLRECEIVEDDATALIST[dataIndex]['ID']+'" onclick="retinvoice('+data.PEPPOLRECEIVEDDATALIST[dataIndex]['ID']+')" >Retrieve Invoice</BUTTON>'; 
				        			}
				        			
				        		}
				        		console.log(data.PEPPOLRECEIVEDDATALIST);
				        		return data.PEPPOLRECEIVEDDATALIST;
				        	}
				        }
				    },
			        "columns": [
		    			{"data": 'DOCID', "orderable": true},
		    			{"data": 'BILLNO', "orderable": true},
		    			{"data": 'RECEIVEDAT', "orderable": true},
		    			{"data": 'EXPIRESAT', "orderable": true},
		    			{"data": 'BILL_STATUS', "orderable": true},
		    			{"data": 'BTNINVOICE', "orderable": true},
		    			
		    			],
					"columnDefs": [{"className": "text-center", "targets": [3,4]}],
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
			    	                }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	exportOptions: {
			                    		columns: [':visible']
			                    	},
		                    		orientation: 'landscape',
		                            pageSize: 'A3',
		                            	extend : 'pdfHtml5',
		    	                    	exportOptions: {
		    	                    		columns: [':visible']
		    	                    	},	                    	
		    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
		                     		orientation: 'landscape',
		                     		customize: function (doc) {
		                     			doc.defaultStyle.fontSize = 16;
		                     	        doc.styles.tableHeader.fontSize = 16;
		                     	        doc.styles.title.fontSize = 20;
		                     	       doc.content[1].table.body[0].forEach(function (h) {
		                     	          h.fillColor = '#ECECEC';                 	         
		                     	          alignment: 'center'
		                     	      });
		                     	      var rowCount = doc.content[1].table.body.length;
		                     	    for (i = 1; i < rowCount; i++) {                     	     
		                     	     	doc.content[1].table.body[i][0].alignment = 'center';
		                     	    	doc.content[1].table.body[i][1].alignment = 'center';
		                     	     	doc.content[1].table.body[i][2].alignment = 'center';
		                     	     	doc.content[1].table.body[i][3].alignment = 'center';
		                     	     	doc.content[1].table.body[i][4].alignment = 'center';
		                     	     }  
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
			        "order": [],
			        drawCallback: function() {
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
		          '<TH><font color="#ffffff" align="left"><b>Bill</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Reference Number</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Supplier Name</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Status</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Due Date</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Amount</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Balance Due</TH>'+
		          '</TR>';
		              
		}
		
		</script>
		</div>
		
		</div></div></div>
		<div class="form-group">
  	<div class="col-sm-12" align="center">
  	<button type="button" class="Submit btn btn-default"  value="Retrieveall"  name="action" onclick="window.location.href='../peppol/getpeppolreciveptdata'" ><b>Retrieve All from Peppol</b></button>
  	</div>
  	</div>
		</form>
		</div>
		</div>
		</div>
		
<script>
var postatus = [{
    "year": "PAID",
    "value": "PAID",
    "tokens": [
      "PAID"
    ]
  },
  {
    "year": "PARTIALLY PAID",
    "value": "PARTIALLY PAID",
    "tokens": [
      "PARTIALLY PAID"
    ]
  },
  {
	    "year": "OPEN",
	    "value": "NOT PAID",
	    "tokens": [
	      "OPEN"
	    ]
	  }];

var billpostatus =   [{
    "id": "VISIBLE INVOICE",
    "value": "VISIBLE INVOICE",
    "tokens": [
      "VISIBLE INVOICE"
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

	/* To get the suggestion data for Status */
	$(".status").typeahead({
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

	/* To get the suggestion data for Status */
	$(".billstatus").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'billpostatus',
	  display: 'value',  
	  source: substringMatcher(billpostatus),
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
 
 function retinvoice(id){
	 window.location.href = "../peppol/savebill?ID="+id;
	 	/* alert(id);
	 	var urlStr = "../peppol/savebill";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			ACTION : "",
			ID : id
		},
		dataType : "json",
		success : function(data) {
			console.log("gggggggggggggggggggggggggggg");
			console.log(data);
		}
}); */
 } 
 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>
