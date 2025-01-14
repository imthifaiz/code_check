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
String title = "Purchase Order Details with cost";
%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
  <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
   <jsp:param name="submenu" value="<%=IConstants.PURCHASE_REPORTS%>"/>
</jsp:include>

<script src="js/general.js"></script>

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdPoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdPoNo[i].checked = isChk;
              	}
            	
        }
    }
}
function onRePrint(){
	var checkFound = false;
	var len = document.form1.chkdPoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdPoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdPoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdPoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form1.action="/track/DynamicFileServlet?action=printPOInvoiceWITHBATCH&Submit=Print Inbound Order With Cost";
    document.form1.submit();
}

function onRePrintWithOutBatch(){
	var checkFound = false;
	var len = document.form1.chkdPoNo.length;	
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdPoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdPoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdPoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form1.action="/track/DynamicFileServlet?action=printPOInvoiceWITHOUTBATCH&Submit=Print Inbound Order With Cost";
    document.form1.submit();
}
var postatus =   [{
    "year": "PARTIALLY RECEIVED",
    "value": "PARTIALLY RECEIVED",
    "tokens": [
      "PARTIALLY RECEIVED"
    ]
  },
	  {
		    "year": "RECEIVED",
		    "value": "RECEIVED",
		    "tokens": [
		      "RECEIVED"
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
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();
int k=0;
String rowColor="";		
session= request.getSession();
String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "", RECEIVESTATUS="",ORDERTYPE="",DIRTYPE ="",BATCH ="",USER="",newstatus="",
fdate="",tdate="",JOBNO="",PONO="",CUSTOMER="",PGaction="",SUPPLIERTYPE="";
String statusID="", GRNO="";
PGaction         = StrUtils.fString(request.getParameter("PGaction"));
String html = "",cntRec ="false",allChecked = "";
plant = (String)session.getAttribute("PLANT");
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();

boolean displaySummaryLink=false,displaySummaryPrintBatch=false,displaySummaryPrintNoBatch=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryLink = ub.isCheckValAcc("summarylnkpdfcost", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValAcc("printPOInvoiceinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintNoBatch = ub.isCheckValAcc("printPOInvoicenoinvoicebatch", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
	displaySummaryPrintNoBatch=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkpdfcost", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValinv("printPOInvoiceinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintNoBatch = ub.isCheckValinv("printPOInvoicenoinvoicebatch", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
	displaySummaryPrintNoBatch=true;
}
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays(); //resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
String btnContainerDisabled="disabled";
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;  //resvi
if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
USER          = StrUtils.fString(request.getParameter("USER"));
PONO       = StrUtils.fString(request.getParameter("PONO"));
CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
RECEIVESTATUS  = StrUtils.fString(request.getParameter("RECEIVESTATUS"));
newstatus = RECEIVESTATUS;
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
GRNO = StrUtils.fString(request.getParameter("GRNO"));
allChecked = StrUtils.fString(request.getParameter("allChecked"));
SUPPLIERTYPE = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
%>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Purchase Order Details with Cost</label></li>                             
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                       <div class="box-title pull-right">
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" target="_blank">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="STATUS_ID" value="">

		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" id="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/supplierlist.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="SUPPLIER_TYPE_ID" name="SUPPLIER_TYPE_ID"  placeholder="SUPPLIER GROUP"value="<%=StrUtils.forHTMLTag(SUPPLIERTYPE)%>">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="PONO" name="PONO"  placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(PONO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/list/ib_order_list.jsp?PONO='+form1.PONO.value+'&STATUS='+form1.RECEIVESTATUS.value+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="REFERENCE NO">				
  		</div>
		 		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" placeholder="ORDER TYPE" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  	
 
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">                       
        <input type="text" name="RECEIVESTATUS" id="RECEIVESTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(newstatus)%>" placeholder="STATUS" >
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changestatus(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
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
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-10">
     <!--  <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RIB');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>
     
	<INPUT type="Hidden" name="DIRTYPE" value="PO_PRINT">
    <INPUT  name="GRNO" type="Hidden" value="<%=GRNO%>">
  
      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                        &nbsp; Select/Unselect All &nbsp;<div style="display: none;"><input type="checkbox" class="form-check-input" name="printwithgrno" value="printwithgrno" />&nbsp;<strong>Print with GRNO</strong></div>
                     &nbsp;<input type="checkbox" class="form-check-input" name="printwithbillno" value="printwithbillno" />&nbsp;Print with Bill</div>
  </div>
            
      <div id="VIEW_RESULT_HERE" class="table-responsive">
      <table id="tablePOList"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">CHK</th>
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF.NO</th>
		                	<th style="font-size: smaller;">SUPPLIER NAME</th>
		                	<th style="font-size: smaller;">ORDER QTY</th>
		                	<th style="font-size: smaller;">RECEIVED QTY</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  
  <%            
  		POUtil poUtil = new POUtil();
        Map ma = poUtil.getPOReceiptHdrDetails(plant);
   		  
    %>  
    
     <div class="form-group">
  	<div class="col-sm-12" align="center">
  	<%if(displaySummaryPrintBatch){ %>      
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" >PrintWithBatch/Sno</button>
  	<%} %>
  	<%if(displaySummaryPrintNoBatch){ %>   
  	<button type="button" class="Submit btn btn-default"  value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();">PrintWithOutBatch/Sno</button>
  	<%} %>	 
  	</div>
  	</div>
  	
    
  </FORM>
  </div>
  </div>
  </div>
    
   
 <script>
 var tablePOList;
 var FROM_DATE, TO_DATE, DIRTYPE, USER, ORDERNO, JOBNO, RECEIVESTATUS, ORDERTYPE, STATUS_ID,GRNO,SUPPLIERTYPE;
 function getParameters(){
 	return { 
 		FDATE:FROM_DATE,TDATE:TO_DATE,DTYPE:DIRTYPE,SUPPLIERNAME:USER,ORDERNO:ORDERNO,JOBNO:JOBNO,RECEIVESTATUS:RECEIVESTATUS,
 		ORDERTYPE:ORDERTYPE,STATUS_ID:STATUS_ID,TYPE:"IB",INVOICE:"Y",GRNO:GRNO,"SUPPLIERTYPE":SUPPLIERTYPE,
 		ACTION: "VIEW_INBOUND_DETAILS_PRINT",PLANT:"<%=plant%>",LOGIN_USER:"<%=USERID%>"
 	}
 }  
  function onGo(){
//debugger;
   var flag    = "false";

    FROM_DATE      = document.form1.FROM_DATE.value;
    TO_DATE        = document.form1.TO_DATE.value;
    DIRTYPE        = document.form1.DIRTYPE.value;
    USER           = document.form1.CUSTOMER.value;
    ORDERNO        = document.form1.PONO.value;
    JOBNO          = document.form1.JOBNO.value;
    RECEIVESTATUS  = document.form1.RECEIVESTATUS.value;
 //imti start
    if(RECEIVESTATUS=="PARTIALLY RECEIVED")
 	   RECEIVESTATUS="O";
  	else if(RECEIVESTATUS=="RECEIVED")
  		RECEIVESTATUS="C";
 //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
    STATUS_ID      = document.form1.STATUS_ID.value;
    GRNO		   = document.form1.GRNO.value;
    SUPPLIERTYPE     = document.form1.SUPPLIER_TYPE_ID.value;
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(JOBNO != null     && JOBNO != "") { flag = true;}
   if(GRNO != null     && GRNO != "") { flag = true;}
   if(SUPPLIERTYPE != null     && SUPPLIERTYPE != "") { flag = true;}
   var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
   if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
   //storeInLocalStorage('printPOInvoice_FROMDATE', FROM_DATE);
	//storeInLocalStorage('printPOInvoice_TODATE', TO_DATE);
	storeInLocalStorage('printPOInvoice_CUSTOMER', USER);
	storeInLocalStorage('printPOInvoice_SUPPLIER_TYPE_ID',SUPPLIERTYPE);
	storeInLocalStorage('printPOInvoice_ORDERNO', ORDERNO);
	storeInLocalStorage('printPOInvoice_JOBNO', JOBNO);
	storeInLocalStorage('printPOInvoice_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('printPOInvoice_STATUS', RECEIVESTATUS);
   }
   var urlStr = "/track/InboundOrderHandlerServlet";
   if (tablePOList){
	   tablePOList.ajax.url( urlStr ).load();
   }else{
	   tablePOList = $('#tablePOList').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].PONO === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['SNO'] = dataIndex + 1;
		        			data.items[dataIndex]['CHKPO'] = '<INPUT Type=checkbox style=border: 0; name=chkdPoNo value="'+data.items[dataIndex]['PONO']+'" >';
		        			 <%if(displaySummaryLink){ %>
		        			data.items[dataIndex]['PONO'] = '<a href="../purchaseorder/printpdfcostdetails?PONO=' +data.items[dataIndex]['PONO']+ '&INVOICE='+data.items[dataIndex]['INVOICE']+'&FROMDATE='+data.items[dataIndex]['FROMDATE']+'&TODATE='+data.items[dataIndex]['TODATE']+'">' + data.items[dataIndex]['PONO'] + '</a>';
		        			 <%}else{ %>
		        			 data.items[dataIndex]['PONO'] = data.items[dataIndex]['PONO'];
		        			 <%}%>
		        			//imti start
								var lnstat= data.items[dataIndex]['STATUS'];
								   if(lnstat=="O")
									   lnstat="PARTIALLY RECEIVED";							  
								   else if(lnstat=="C")
									   lnstat="RECEIVED";
								 data.items[dataIndex]['STATUS'] = lnstat;
								//imti end
									
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHKPO', "orderable": false},
	        	{"data": 'SNO', "orderable": false},
	        	{"data": 'PONO', "orderable": false},
    			{"data": 'ORDERTYPE', "orderable": false},
    			{"data": 'JOBNUM', "orderable": false},
    			{"data": 'CUSTNAME', "orderable": false},
    			{"data": 'QTYOR', "orderable": false},
    			{"data": 'QTY', "orderable": false},
    			{"data": 'STATUS', "orderable": false},
    			],
			"columnDefs": [{"className": "t-right", "targets": [6, 7]}],
			/*"orderFixed": [ groupColumn, 'asc' ], 
			/*"dom": 'lBfrtip',*/
			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	        buttons: [
	        	
	        ],
			"drawCallback": function ( settings ) {
			}
		});
   }	   
}

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
                     
	        	outPutdata = outPutdata+item.INBOUNDDETAILS
                      	ii++;
	            
	          });
		}else{
		}
      outPutdata = outPutdata +'</TABLE>';
      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
      document.getElementById('spinnerImg').innerHTML ='';
  
 }

function getTable(){
   return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Supplier Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Received Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Status</TH>'+
         '</tr>';
 
}



function changeorderno(obj){
	 $("#PONO").typeahead('val', '"');
	 $("#PONO").typeahead('val', '');
	 $("#PONO").focus();
	}

function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}


function changecustomertypeid(obj){
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '"');
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '');
	 $("#SUPPLIER_TYPE_ID").focus();
	}

function changeordertype(obj){
	 $("#ORDERTYPE").typeahead('val', '"');
	 $("#ORDERTYPE").typeahead('val', '');
	 $("#ORDERTYPE").focus();
	}

function changestatus(obj){
	 $("#RECEIVESTATUS").typeahead('val', '"');
	 $("#RECEIVESTATUS").typeahead('val', '');
	 $("#RECEIVESTATUS").focus();
	}

function changegrno(obj){
	 $("#GRNO").typeahead('val', '"');
	 $("#GRNO").typeahead('val', '');
	 $("#GRNO").focus();
	}

 $(document).ready(function(){
	 var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	// getLocalStorageValue('printPOInvoice_FROMDATE',  $('#FROM_DATE').val(), 'FROM_DATE');
	// getLocalStorageValue('printPOInvoice_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('printPOInvoice_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('printPOInvoice_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
	 getLocalStorageValue('printPOInvoice_ORDERNO', '', 'PONO');
	 getLocalStorageValue('printPOInvoice_JOBNO', '', 'JOBNO');
	 getLocalStorageValue('printPOInvoice_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('printPOInvoice_STATUS', '', 'RECEIVESTATUS');
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
	/* Supplier Auto Suggestion */
	$('#CUSTOMER').typeahead({
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')"> <p class="item-suggestion">Name: ' + data.VNAME 
		    	   + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
		    	   + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.SUPPLIER_TYPE_ID+'</p></div>';
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
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Order Number Auto Suggestion */
	$('#PONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : query
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
		    return '<p>' + data.PONO + '</p>';
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
				document.form1.ORDERNO.value = "";
			}
		
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
				OTYPE : "PURCHASE",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.ORDERTYPEMST);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
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
				document.form1.ORDERTYPE.value = "";
			}
		
		});
	
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
	
	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
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
			return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p></div>';
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
	/* Product Type Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
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
	/* Product Type Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
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
			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
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
	$("#RECEIVESTATUS").typeahead({
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
