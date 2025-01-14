<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Alternate Brand Product Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invByProd";
	document.form1.submit();
	
} 
</script>

<%@include file="header.jsp" %>
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();

String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", 
FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",ALTERNATEITEM="",ALTERNATEDESC="",ALTERNATEBRAND="",ALTERNATEITEMVINNO="",VINNO="",MODEL="",ALTERNATEMODEL="";
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",LOC_TYPE_ID="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
ALTERNATEITEM = strUtils.fString(request.getParameter("ALTERNATEITEM"));
ALTERNATEDESC= strUtils.fString(request.getParameter("ALTERNATEDESC"));
ALTERNATEBRAND= strUtils.fString(request.getParameter("ALTERNATEBRAND"));
MODEL= strUtils.fString(request.getParameter("MODEL"));
ALTERNATEMODEL= strUtils.fString(request.getParameter("ALTERNATEMODEL"));

 LOC = strUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));

ALTERNATEITEMVINNO= strUtils.fString(request.getParameter("ALTERNATEITEMVINNO"));
VINNO= strUtils.fString(request.getParameter("VINNO"));
boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false,displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryLink = ub.isCheckValAcc("summarylnkalternatebrand", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editalternatebrand", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newalternatebrand", PLANT,USERID);
	displaySummaryExport = ub.isCheckValAcc("exportalternatebrand", PLANT,USERID);
	
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkalternatebrand", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editalternatebrand", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newalternatebrand", PLANT,USERID);
	displaySummaryExport = ub.isCheckValinv("exportalternatebrand", PLANT,USERID);

	}
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);



boolean cntRec=false;

%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Altenate Brand Product Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" 	onclick="window.location.href='../alternateproduct/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              <%}%>
              <!-- <button type="button" class="btn btn-default" onclick="window.location.href='../home'">Back</button> -->
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="alternatebrandsummary.jsp">
<input type="hidden" name="plant" id="plant" value="<%=PLANT %>">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
  
  <%-- <center>
 <h1><small> <%=fieldDesc%></small></h1>      
    
  </center> --%>
  
  
  <center><small> <%=fieldDesc%></small></center>
  
  <div id="target" style="display:none;">
    
       <div class="form-group">
       <%--<div class="col-sm-3">
      	     <div class="input-group">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		 	<span class="select-icon"  onClick="javascript:popUpWin('list/item_list_alternatebrand.jsp?ITEM='+form1.ITEM.value+'&TYPE=ITEM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="product Details">
   		 	<i  class="glyphicon glyphicon-search" style="font-size: 20px;"></i></a></span>
  		</div>--%>
  		<div class="row">
  		<div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				 <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="ac-selected form-control typeahead tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: none 0% 0% / auto repeat scroll padding-box padding-box rgb(255, 255, 255);"><input type="text" class="ac-selected form-control typeahead tt-input" id="ITEM" placeholder="Product ID" name="ITEM" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: &quot;Proxima Nova&quot;, &quot;Source Sans Pro&quot;, Helvetica, Arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-0"></div></div></span>				
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/list/item_list_alternatebrand.jsp?ITEM='+form1.ITEM.value+'&TYPE=ITEM');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
				<br>
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();"><b>Search</b></button>
			</div>
			<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				 <span class="twitter-typeahead" style="position: relative; display: inline-block;"><input type="text" class="ac-selected form-control typeahead tt-hint" readonly="" autocomplete="off" spellcheck="false" tabindex="-1" dir="ltr" style="position: absolute; top: 0px; left: 0px; border-color: transparent; box-shadow: none; opacity: 1; background: none 0% 0% / auto repeat scroll padding-box padding-box rgb(255, 255, 255);"><input type="text" class="ac-selected form-control typeahead tt-input" id="PRD_DESCRIP" placeholder="Description" name="PRD_DESCRIP" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top; background-color: transparent;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: &quot;Proxima Nova&quot;, &quot;Source Sans Pro&quot;, Helvetica, Arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><div class="tt-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none;"><div class="tt-dataset tt-dataset-0"></div></div></span>				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DESCRIP\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>								
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>
  		</div>
  		<div class="row">
  	       
  	     </div>
 		</div>
 	 	
 		
 		 <div class="form-group">
  		
    	<INPUT class="form-control" name="LOC" type = "hidden" value="<%=StrUtils.forHTMLTag(LOC)%>" size="30"  MAXLENGTH=20>
    		
       
        <INPUT class="form-control" name="LOC_TYPE_ID" type="hidden" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>"	size="30" MAXLENGTH=20>
   		 	
 		
  	       
 		</div>
                  <input type="hidden" name="PRD_BRAND_DESC" value="">
                  <INPUT name="ACTIVE" type = "hidden" value="">  
             
  	</div>
  	

     <!--   Start code added by Deen for product brand on 11/9/12 -->
     
     <!--   End code added by Deen for product brand on 11/9/12 -->  
    
  <div id="VIEW_RESULT_HERE" class="table-responsive">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: small;">Product ID</th>
		                	<th style="font-size: small;">Description</th>
		                	<th style="font-size: small;">Brand ID</th>
		                	<th style="font-size: small;">VINNO</th>
		                	<th style="font-size: small;">Model</th>
		                	<th style="font-size: small;">Edit</th>
		                			                	
		                </tr>
		            </thead>
				</table>
            		</div>
						</div>
				
  </div>
  <div id="spinnerImg" ></div>
 
  </FORM>
  
  <script type="text/javascript">
  var tableInventorySummary;
  var productId,desc,prdBrand,alernateProductId,alternateBrand,loc,locType, groupRowColSpan = 6;
function getParameters(){
	return { 
		"ITEM": productId,
		"LOC":loc,
		 "DESC":desc,
		"LOC_TYPE_ID":locType, 
		"ACTION": "GET_ALT_PRODUCT_SUMMARY",
		"PLANT":"<%=PLANT%>"
	}
}  
function onGo(){
	productId = document.form1.ITEM.value;
	desc= document.form1.PRD_DESCRIP.value;
	   /*  loc = document.form1.LOC.value;
	    locType = document.form1.LOC_TYPE_ID.value; */
	    var urlStr = "/track/ItemMstServlet";
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
			        	if(typeof data.items[0].ITEM === 'undefined'){
			        		return [];
			        	}else {
			        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){	
			        			<% if (displaySummaryEdit) { %>
			        			data.items[dataIndex]['EDIT'] = '<a href="../alternateproduct/edit?action=EDIT&ITEM=' +data.items[dataIndex]['ITEM']+'"><i class="fa fa-pencil-square-o"></i></a>';
			        			<% } else { %> 
			        			data.items[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a>';
			        			<% }%>
			        			 <% if (displaySummaryLink) { %>
			        			data.items[dataIndex]['ITEM'] = '<a href="../alternateproduct/detail?action=EDIT&ITEM=' +data.items[dataIndex]['ITEM']+'">'+data.items[dataIndex]['ITEM']+'</a>';
			        			<% } else { %>     
			        			data.items[dataIndex]['ITEM'] = data.items[dataIndex]['ITEM'];
			        			<% }%>
			        			//data.items[dataIndex]['EDIT'] = '<a href="/track/jsp/createalternatebranditem.jsp?ITEM=' +data.items[dataIndex]['ITEM']+ '&ALTERNATEBRANDITEM=' +data.items[dataIndex]['ALTERNATEITEM']+'&DESC=' +data.items[dataIndex]['ITEMDESC']+'&ALTERNATEBRANDDESC=' +data.items[dataIndex]['ALTERNATEITEMDESC']+'&VINNO=' +data.items[dataIndex]['VINNO']+'&ALTERNATEVINNO=' +data.items[dataIndex]['ALTERNATEITEMVINNO']+'&MODEL=' +data.items[dataIndex]['MODEL']+'&ALTERNATEMODEL=' +data.items[dataIndex]['ALTERNATEMODEL']+'&PRD_BRAND_ID=' +data.items[dataIndex]['BRAND']+'&ALTERNATE_PRD_BRAND_ID=' +data.items[dataIndex]['ALTERNATEBRAND']+'"><i class="fa fa-pencil-square-o"></i></a>';
	 		        			
	 		        		}
	 		        		
	 		        		return data.items;
	 		        	}
			        }
			    },
		        "columns": [
	    			{"data": 'ITEM', "orderable": true},
	    			{"data": 'ITEMDESC', "orderable": true},
	    			{"data": 'BRAND', "orderable": true},
	    			{"data": 'VINNO', "orderable": true},
	    			{"data": 'MODEL', "orderable": true},
	    			{"data": 'EDIT', "orderable": true}
	    			//{"data": 'LOC', "orderable": true},
	    			//{"data": 'QTY', "orderable": true}
	    			],
				/* "columnDefs": [{"className": "t-right", "targets": [6,7,8]}], */
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
	                     	     doc.content[1].table.body[i][1].alignment = 'center';
	                     	    doc.content[1].table.body[i][2].alignment = 'center';
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
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.PRODUCT
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
                    '<TH><font color="#ffffff" align="left" width = "15%"><b>Product ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Brand ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Alternate Product</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description<</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Brand Id</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Location</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Quantity</TH>'+
                    '</TR>';
                
}

</SCRIPT>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 var plant=$('#plant').val();
	 onGo();
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
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
	 
	 $('#PRD_DESCRIP').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEMDESC',  
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
			return '<div><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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