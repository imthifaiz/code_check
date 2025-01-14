<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "POS Expenses";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes"})%>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell format
function ExportReport()
{
	document.form.action="/track/deleveryorderservlet?Submit=ExportExcelPOSExpensesSummary";
	document.form.submit();
}
//---End Modified by Deen on May 21 2014, Description:To open Sales order summary  in excel powershell format

</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();

int k=0;
String rowColor="";		


session= request.getSession();

String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "",DIRTYPE ="",USER="",DESC="",fdate="",tdate="",OUTLET_NAME="",PGaction="",sOutCode="";
String statusID="",EMP_NAME="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",TERMINAL_NAME="",CUST_NAME="";

DecimalFormat decformat = new DecimalFormat("#,##0.00");
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
sOutCode = StrUtils.InsertQuotes(StrUtils.fString(request.getParameter("OUTCODE")));

PlantMstUtil plantmstutil = new PlantMstUtil();

boolean displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryExport = ub.isCheckValAcc("exportobordersumry", plant,LOGIN_USER);
	displaySummaryExport =true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryExport = ub.isCheckValinv("exportobordersumry", plant,LOGIN_USER);
	displaySummaryExport =true;
}
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays();//resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;//resvi

if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

 DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
USER          = StrUtils.fString(request.getParameter("USER"));
DESC          = StrUtils.fString(request.getParameter("DESC"));
OUTLET_NAME      = StrUtils.fString(request.getParameter("OUTLET_NAME"));

EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
TERMINAL_NAME = StrUtils.fString(request.getParameter("TERMINALNAME"));
CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
%>

<div class="container-fluid m-t-20">
	<div class="box">	
    <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>        
                <li><a href="../posreports/reports"><span class="underline-on-hover">POS Reports</span> </a></li>                   
                <li><label>POS Expenses</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                        <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data </button>
					   <%} %>
				</div>
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../posreports/reports'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form" method="post" action="../posreports/expenses" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=plant%>">
<input type="hidden" name="CUST_CODE" >
<input type="hidden" name="OUTLET_CODE" value="">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
<INPUT type="hidden" name="TERMINALCODE" value="">
<INPUT name="DESC" type = "Hidden" value="<%=StrUtils.forHTMLTag(DESC)%>">

		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="OUTLET_NAME" value="<%=StrUtils.forHTMLTag(OUTLET_NAME)%>" placeholder="OUTLETS" name="OUTLET_NAME" >				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeoutlet(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="TERMINALNAME" name="TERMINALNAME" value="<%=StrUtils.forHTMLTag(TERMINAL_NAME)%>"  placeholder="TERMINAL">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeterminal(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'TERMINALNAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
				</div>
  		</div>
		 <div class="col-sm-4 ac-box">
    		<INPUT id="CUST_NAME" name="CUST_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUST_NAME)%>" size="20"  placeholder="CASHIER" MAXLENGTH=100 class="ac-selected form-control">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecust_name(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		</div>
  		
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
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
      <div class="col-sm-offset-9">
  	  </div>
         </div>
       	   </div>
     
<!-- PDF PRINT -->
<div id="dvContents" style="display:none">
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table printtable" id="tableprint">
	<thead>
	     <tr>  
            <th style="font-size: smaller;">S/N</th> 
        	<th style="font-size: smaller;">DATE</th>
        	<th style="font-size: smaller;">ID</th>
            <th style="font-size: smaller;">DESCRIPTION</th>
        	<th style="font-size: smaller;">AMOUNT</th>
          </tr>  
        </thead> 
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>

</TABLE>
</div>
	
<div style="overflow-x:auto;" >
<table id="table" class="table table-bordred table-striped"  style="width: 100%;">  
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th> 
        	<th style="font-size: smaller;">DATE</th>
        	<th style="font-size: smaller;">ID</th>
            <th style="font-size: smaller;">DESCRIPTION</th>
        	<th style="font-size: smaller;">AMOUNT</th>
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div> 
  <div id="spinnerImg" ></div>
    
  </FORM>
 <script>
 var item,fdate,tdate,outlet_name,terminalname,cust_name;
	var tabletype;
	var tablePrint;
	function getParameters(){
		return {
			"ITEM": item,"FDATE":fdate,"TDATE":tdate,
			"OUTLET_NAME":outlet_name,"TERMINALNAME":terminalname,"OUTLETCODE":outletcode,"TERMINALCODE":terminalcode,"CUST_NAME":cust_name,
			"ACTION": "VIEW_POSEXPENSES_SUMMARY_WITH_PRICE","PLANT":"<%=plant%>",SRC:"VOID",LOGIN_USER:"<%=USERID%>"
		}
	}
	function onGo(){
		 plant = document.form.plant.value;
		 fdate = document.form.FROM_DATE.value;
		 tdate = document.form.TO_DATE.value;
		 outlet_name = document.form.OUTLET_NAME.value;
		 outletcode = document.form.OUTCODE.value;
		 terminalname = document.form.TERMINALNAME.value;
		 terminalcode = document.form.TERMINALCODE.value;
		 cust_name = document.form.CUST_NAME.value;
	    
		   var urlStr = "../OutboundOrderHandlerServlet";
		   var groupColumn = 1;	
		    if (tabletype){
		    	tabletype.ajax.url( urlStr ).load();
		    	tableprint.ajax.url( urlStr ).load();
		    }else{
		    	tabletype = $('#table').DataTable({
					"processing": true,
					"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	
				        	if(typeof data.POS[0].id === 'undefined'){
					        		return [];
					        	}else {				        		
					        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
					        			data.POS[dataIndex]['Index'] = dataIndex + 1;
					        			data.POS[dataIndex]['amount'] = parseFloat(data.POS[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>);
					        		}
					        		return data.POS;
					        	}
				        	
				        }
				    },
			        "columns": [
			        	{"data": 'Index', "orderable": true},
		    			{"data": 'expensesdate', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'description', "orderable": true},
		    			{"data": 'amount', "orderable": true}
		    			],
		    		"columnDefs": [{"className": "t-right", "targets": [4]}],
					"orderFixed": [ ], 
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
			                    	/* exportOptions: {
			    	                	columns: [':visible']
			    	                } */
			    	                title: '<%=title %>',
				                    exportOptions: {
				                    	columns: [0,1,2,3,4]
				                    }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	/* exportOptions: {
			                    		columns: [':visible']
			                    	}, */
			                    	title: '<%=title %>',
			                    	exportOptions: {
			                    		columns: [0,1,2,3,4]
			                        },
		                    		orientation: 'landscape',
		                            pageSize: 'A4',
		                            	extend : 'pdfHtml5',
		    	                    	/* exportOptions: {
		    	                    		columns: [':visible']
		    	                    	}, */
		    	                    	exportOptions: {
		    	                    		columns: [0,1,2,3,4]
				                        },
		    	                        <%-- title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview }, --%>    	                        
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
		                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
		                }		                
			        ],
			        "order": [],
			        "drawCallback": function ( settings ) {
						this.attr('width', '100%');
						var groupColumn = 0;
						var groupRowColSpan= 3;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                	/* if (i > 0) {
			                		$(rows).eq( i ).before(
					                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td></tr>'
					                    );
			                	} */
			                    last = group;
			                    groupEnd = i;
			                    groupTotalPickQty = 0;
			                    
			                }
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', ''));
			                   
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>) + '</td></tr>'
		                    );
		                	/* $(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(3) + '</td></tr>'
		                    ); */
		                }
			        }
				});
		    	tableprint = $('#tableprint').DataTable({
					"processing": true,
					"lengthMenu": [[-1],["All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	
				        	if(typeof data.POS[0].id === 'undefined'){
					        		return [];
					        	}else {				        		
					        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
					        			data.POS[dataIndex]['Index'] = dataIndex + 1;
					        			data.POS[dataIndex]['amount'] = parseFloat(data.POS[dataIndex]['amount']).toFixed(<%=numberOfDecimal%>);
					        		}
					        		return data.POS;
					        	}
				        	
				        }
				    },
			        "columns": [
			        	{"data": 'Index', "orderable": true},
		    			{"data": 'expensesdate', "orderable": true},
		    			{"data": 'id', "orderable": true},
		    			{"data": 'description', "orderable": true},
		    			{"data": 'amount', "orderable": true}
		    			],
		    		"columnDefs": [{"className": "t-right", "targets": [4]}],
					"orderFixed": [ ], 
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
					buttons: [],
			        "order": [],
			        searching: false,
			        paging: false,
			        info: false,
			        "bLengthChange" : false, //thought this line could hide the LengthMenu
			        "drawCallback": function ( settings ) {
						this.attr('width', '100%');
						var groupColumn = 0;
						var groupRowColSpan= 3;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                    last = group;
			                    groupEnd = i;
			                    groupTotalPickQty = 0;
			                    
			                }
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', ''));
			                   
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>) + '</td></tr>'
		                    );
		                }
			        }
				});
		    	
		    }
		    
		}
            
  </SCRIPT> 
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
	function changeoutlet(obj){
		 $("#OUTLET_NAME").typeahead('val', '"');
		 $("#OUTLET_NAME").typeahead('val', '');
		 $("#OUTLET_NAME").focus();
		}
	function changeterminal(obj){
		 $("#TERMINALNAME").typeahead('val', '"');
		 $("#TERMINALNAME").typeahead('val', '');
		 $("#TERMINALNAME").focus();
		}
	function changecust_name(obj){
		 $("#CUST_NAME").typeahead('val', '"');
		 $("#CUST_NAME").typeahead('val', '');
		 $("#CUST_NAME").focus();
		}
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

	 /* Employee Auto Suggestion */
		$('#CUST_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "getEmployeeListStartsWithName",
					TYPE : "CASHIER",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMP_MST);
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
			    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = $(".tt-menu").height()+35;
				top+="px";
				$('.supplierAddBtn').remove();  
				$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
				$(".supplierAddBtn").width($(".tt-menu").width());
				$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
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
				
			});


	 $('#OUTLET_NAME').typeahead({
     	  hint: true,
     	  minLength:0,  
     	  searchOnFocus: true
     	},
     	{
     	  display: 'OUTLET_NAME',  
     	  async: true,   
     	  source: function (query, process,asyncProcess) {
     		var urlStr = "/track/MasterServlet";
     		$.ajax( {
     		type : "POST",
     		url : urlStr,
     		async : true,
     		data : {
     			PLANT : "<%=plant%>",
     			ACTION : "GET_OUTLET_DATA",
     			QUERY : query
     		},
     		dataType : "json",
     		success : function(data) {
     			return asyncProcess(data.POSOUTLETS);
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
     		 return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
     			document.form.OUTCODE.value = "";
     		}
     	});


/* TERMINAL*/
  $('#TERMINALNAME').typeahead({
     	  hint: true,
     	  minLength:0,  
     	  searchOnFocus: true
     	},
     	{
     	  display: 'TERMINAL_NAME',  
     	  async: true,   
     	  source: function (query, process,asyncProcess) {
     		var urlStr = "/track/MasterServlet";
     		$.ajax( {
     		type : "POST",
     		url : urlStr,
     		async : true,
     		data : {
     			PLANT : "<%=plant%>",
     			ACTION : "GET_TERMINAL_DATA",
     			QUERY : query
     		},
     		dataType : "json",
     		success : function(data) {
     			return asyncProcess(data.POSOUTLETS);
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
     	    	return '<div onclick="setTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
     		removeterminaldropdown();
     		addterminaldropdown();
     	}).on('typeahead:change',function(event,selection){
     		if($(this).val() == ""){
     			document.form.TERMINALCODE.value = "";
     		}
     	});
	
 });


  function removeterminaldropdown(){
		$("#TERMINALNAME").typeahead('destroy');
	}

	function addterminaldropdown(){
		  $('#TERMINALNAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TERMINAL_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TERMINAL_DATA",
					ONAME : document.form.OUTCODE.value,
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
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
			    	return '<div onclick="setTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
					document.form.TERMINALCODE.value = "";
				}
			});

	}

	 function setOutletData(OUTLET,OUTLET_NAME){
			$("input[name=OUTCODE]").val(OUTLET);
		}

	 function setTerminalData(TERMINAL,TERMINAL_NAME){
			$("input[name=TERMINALCODE]").val(TERMINAL);
		}

	 function PrintTable() {
	        var printWindow = window.open('', '', '');
	        printWindow.document.write('<html><head><title>Print <%=title %></title>');
	 
	        //Print the Table CSS.
	        var table_style = document.getElementById("table_style").innerHTML;
	        printWindow.document.write('<style type = "text/css">');
	        printWindow.document.write(table_style);
	        printWindow.document.write('</style>');
	        printWindow.document.write('</head>');
	 
	        //Print the DIV contents i.e. the HTML Table.
	        printWindow.document.write('<body>');
	        var divContents = document.getElementById("dvContents").innerHTML;
	        printWindow.document.write(divContents);
	        printWindow.document.write('</body>');
	 
	        printWindow.document.write('</html>');
	        printWindow.document.close();
	        printWindow.print();
	    }
 </script>
 <style id="table_style" type="text/css">
 	.printtable
    {
        border: 1px solid #ccc;
        border-collapse: collapse;
    }
 	.printtable th, .printtable td
    {
        padding: 5px;
        border: 1px solid #ccc;
    }
	.printtable tr:last-child {
    font-weight: bold;
	}
	.printtable td:nth-last-child(1) {
    text-align: right;
	}
</style>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
