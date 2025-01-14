<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<%
String title = "Packing List/Delivery Note Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

/* function ExportReport()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.DONO.value;
  document.form1.JOBNO.value;
  document.form1.DIRTYPE.value;
  document.form1.CUSTOMER.value;
  document.form1.action="/track/DynamicProductServlet?cmd=ExportExcelSummary";
  document.form1.submit();
} */

function checkAll(isChk)
{
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}
function submitForm(actionvalue)
{
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	
	document.form1.action = "/track/deleveryorderservlet?Submit="+actionvalue;
    document.form1.submit();
}


</script>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>

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
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ISSUESTATUS="",ORDERTYPE="",DIRTYPE ="",BATCH ="",USER="",fdate="",tdate="",JOBNO="",DONO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",statusID="",INVOICENO="",GINO="",custCode="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",allChecked = "",EMP_NAME="",CUSTOMERTYPE="";
String fieldDesc="";
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
custCode= StrUtils.fString(request.getParameter("custCode"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
USER          = _strUtils.fString(request.getParameter("USER"));
DONO       = _strUtils.fString(request.getParameter("DONO"));
CUSTOMER      = _strUtils.fString(request.getParameter("CUSTOMER"));
CUSTOMERID      = _strUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = _strUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS  = _strUtils.fString(request.getParameter("ISSUESTATUS"));
statusID = _strUtils.fString(request.getParameter("STATUS_ID"));
EMP_NAME = _strUtils.fString(request.getParameter("EMP_NAME"));
CUSTOMERTYPE = _strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));

allChecked = _strUtils.fString(request.getParameter("allChecked"));
String action   = StrUtils.fString(request.getParameter("action")).trim();
if(action.equalsIgnoreCase("View"))
fieldDesc=(String)request.getSession().getAttribute("RESULT");

%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	
<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li><label>Packing List/Delivery Note Summary</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
					<button type="button" class="btn btn-default"
						onClick="window.location.href='../salestransaction/createpackinglist'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../salesTransactionDashboard'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
 <div id="target" >
 
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
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="DONO" name="DONO" value="<%=StrUtils.forHTMLTag(DONO)%>" placeholder="ORDER NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'DONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="GINO" name="GINO" placeholder="GINO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'GINO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>  		
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" name="INVOICENO" id="INVOICENO" class="ac-selected form-control" placeholder="INVOICE NO" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'INVOICENO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;  		
  						<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  								<button type="button" style="visibility: hidden;" class="Submit btn btn-default"  onClick="submitForm('Print Multiple Invoice');"><b>Print Sales Order With Price</b></button>&nbsp;
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
						<INPUT name="JOBNO" type = "hidden" value="<%=StrUtils.forHTMLTag(JOBNO)%>">
				
			</div>
		</div>
  		</div>
 		
 		</div>
  		
  		
 		<div class="row" style="display:none">
  		<div class="col-12 col-sm-12">   
  		<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	<b>Select/Unselect All</b>
  		</div>
  		</div>
        
      <INPUT type="Hidden" name="DIRTYPE" value="OB_PRINT">
      
      <div id="VIEW_RESULT_HERE" class="table-responsive">
      <table id="tablePOList" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	
		                	<th style="font-size: smaller;">DATE</th>
		                	<th style="font-size: smaller;">ID </th>
		                	<th style="font-size: smaller;">PACKING LIST</th>
		                	<th style="font-size: smaller;">DELIVERY NOTE </th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">GINO</th>
		                	<th style="font-size: smaller;">INVOICE</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">TOTAL NET WEIGHT</th>		                	
		                	<th style="font-size: smaller;">TOTAL GROSS WEIGHT</th>
		                	<th style="font-size: smaller;">TOTAL PACKING</th>
		                	<th style="font-size: smaller;">TOTAL DIMENSION</th>
		                		                	
		                </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  
  </FORM>
 <SCRIPT LANGUAGE="JavaScript">
 var tablePOList;
 var FROM_DATE, TO_DATE, USER, ORDERNO, JOBNO, INVOICENO,GINO;
 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,"GINO":GINO,
 		"INVOICENO":INVOICENO,"ACTION": "VIEW_PACKING_SMRY","PLANT":"<%=plant%>",LOGIN_USER:"<%=USERID%>"
 		
 		
 	}
 }  
  function onGo(){
//debugger;
  
var flag    = "false";

   FROM_DATE      = document.form1.FROM_DATE.value;
   TO_DATE        = document.form1.TO_DATE.value;
 
   USER           = document.form1.CUST_CODE.value;
   ORDERNO        = document.form1.DONO.value;
   JOBNO          = document.form1.JOBNO.value;
   INVOICENO      = document.form1.INVOICENO.value;
   GINO      = document.form1.GINO.value;
 
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   

   if(USER != null    && USER != "") { flag = true;} 
   
    if(JOBNO != null     && JOBNO != "") { flag = true;}
    var urlStr = "../InvMstServlet";
    if (tablePOList){
 	   tablePOList.ajax.url( urlStr ).load();
    }else{
 	   tablePOList = $('#tablePOList').DataTable({
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
 		        	
 		        	if(typeof data.items[0].plno === 'undefined'){
 		        		return [];
 		        	}else {
 		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
 		        			data.items[dataIndex]['SNO'] = dataIndex + 1; 		        			
 		        			//data.items[dataIndex]['EDIT'] = '<a href="/track/jsp/creatednpl.jsp?HID=' +data.items[dataIndex]['HID']+ '&INVOICENO=' +data.items[dataIndex]['INVOICENO']+ '&GINO=' +data.items[dataIndex]['GINO']+ '&DONO=' +data.items[dataIndex]['DONO']+'&custName=' +data.items[dataIndex]['custname']+'&JobNum=' +data.items[dataIndex]['JOBNUM']+'&EDIT='+'EDIT'+'"><i class="fa fa-pencil-square-o"></i></a>'; 		        			                                              
 		        			data.items[dataIndex]['HID'] = '<a href="../salestransaction/packinglistdetail?HID=' +data.items[dataIndex]['HID']+ '&INVOICENO=' +data.items[dataIndex]['INVOICENO']+ '&GINO=' +data.items[dataIndex]['GINO']+ '&DONO=' +data.items[dataIndex]['DONO']+ '&STATUS=' +data.items[dataIndex]['STATUS']+'">'+data.items[dataIndex]['HID']+'</a>';
 		        		}
 		        		
 		        		return data.items;
 		        	}
 		        }
 		    },
 	        "columns": [
 	        
 	        	{"data": 'DATE', "orderable": false},
 	        	{"data": 'HID', "orderable": false},
 	        	{"data": 'plno', "orderable": false},
 	        	{"data": 'dnno', "orderable": false},
 	        	{"data": 'DONO', "orderable": false},
 	        	{"data": 'GINO', "orderable": false},
 	        	{"data": 'INVOICENO', "orderable": false},
 	        	{"data": 'custname', "orderable": false},
 	        	{"data": 'TOTALNETWEIGHT', "orderable": false},
     			{"data": 'TOTALGROSSWEIGHT', "orderable": false},
     			{"data": 'NETPACKING', "orderable": false},  			
     			{"data": 'NETDIMENSION', "orderable": false}
     		
     		 	],
 			
 	        buttons: [
 	        	
 	        ],
 	       	"order": [],
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
                     
	        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
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
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="center">Order No</TH>'+
          '<TH><font color="#ffffff" align="center">Invoice No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Packing List</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Delivery Note</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total Net Weight</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total Gross Weight </TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total Packing</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total Dimension</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Remarks</TH>'+
          '<TH><font color="#ffffff" align="center"><b>Edit</TH>'+
          '</tr>';
              
}
 
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
            
  </SCRIPT> 
  </div></div></div>
   
                <!-- Below Jquery Script used for Show/Hide Function-->
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
    
    /* Order Number Auto Suggestion */
	$('#DONO').typeahead({
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
						PLANT : "<%=plant%>",
						Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						TYPE : "DNPL",
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
			
			$("#INVOICENO").typeahead('val', '"');
			$("#INVOICENO").typeahead('val', '');
			
			$("#GINO").typeahead('val', '"');
			$("#GINO").typeahead('val', '');
		});
	
	
	$('#GINO').typeahead({
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
				PLANT : "<%=plant%>",
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
	
	
	/* Invoice No Auto Suggestion */
	$('#INVOICENO').typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=plant%>",
				Submit : "GET_INVOICE_NO_FOR_AUTO_SUGGESTION",
				ORDERNO : document.form1.DONO.value,
				GINO : document.form1.GINO.value,
				NAME : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.InvoiceDetails);
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
		    return '<p>' + data.INVOICE + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);		});

    
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
						PLANT : "<%=plant%>",
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
// 		    	return '<p onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'">' + data.CNAME + '</p>';
		    	return '<div onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
			$(".customerAddBtn").width($(".tt-menu").width());
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
			}
			/* To reset Order number Autosuggestion*/
			$("#DONO").typeahead('val', '"');
			$("#DONO").typeahead('val', '');
		});
});


 

 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
