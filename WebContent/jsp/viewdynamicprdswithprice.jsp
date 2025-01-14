<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@page import="java.text.DecimalFormat"%>
<%
String title = "Direct Tax Invoice Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
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
}

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

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

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
String CUSTOMERID="",statusID="",INVOICENO="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",allChecked = "",EMP_NAME="",CUSTOMERTYPE="";
PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
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


%>

<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
 <div id="target" style="display:none">
  		<div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group" >
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
      	</div>
    	</div>
    	</div>
    	</div>
    	
    	<div class="form-group">
        <label class="control-label col-sm-2" for="Ref  No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="JOBNO" type = "TEXT" value="<%=StrUtils.forHTMLTag(JOBNO)%>" size="30"  MAXLENGTH=50>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Outbound Order">Transaction ID:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="DONO" type = "TEXT" value="<%=StrUtils.forHTMLTag(DONO)%>" size="32"  MAXLENGTH=100> 
    	</div> 
 		</div>
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Customer Name">Customer Name/ID:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="CUSTOMER" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Invoice No">Invoice No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="INVOICENO" type = "TEXT" value="<%=StrUtils.forHTMLTag(INVOICENO)%>" size="32"  MAXLENGTH=100> 
    	</div> 
 		</div> 
 		</div>
 		
 		<div class="form-group" style="display:none">
       <label class="control-label col-sm-2" for="Order Type">Order Type:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ORDERTYPE" type = "TEXT" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=OUTBOUND&ORDERTYPE='+form1.ORDERTYPE.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Customer Type">Customer Type ID:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group"  style="display:none">
        <label class="control-label col-sm-2" for="Order Status">Order Status:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="STATUS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(statusID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('OrderStatusList.jsp?ORDERSTATUS='+form1.STATUS_ID.value+'&TYPE=OBSUMMARY');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Status Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Employee Name">Employee Name/ID:</label>
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="EMP_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
 		<label class="control-label col-sm-2" for="Status">Status:</label>
       <div class="col-sm-3">
      	    <SELECT class="form-control" NAME ="PICKSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT>
  		</div>
  		<div class="col-sm-offset-2 col-sm-3">   
  		<div class="input-group">
  		<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;  		
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  		<button type="button" style="visibility: hidden;" class="Submit btn btn-default"  onClick="submitForm('Print Multiple Invoice');"><b>Print Sales Order With Price</b></button>&nbsp;
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
      <div class="col-sm-offset-7">
        <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;  		
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  		<button type="button" style="visibility: hidden;" class="Submit btn btn-default"  onClick="submitForm('Print Multiple Invoice');"><b>Print Sales Order With Price</b></button>&nbsp;
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
      <table id="tablePOList" class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tablePOList_info">
					<thead>
		                <tr role="row">
		                	<!-- <th style="background-color: #eaeafa; color:#333; text-align: center;">Chk</th> -->
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">S/N</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Transaction ID</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Transaction Date</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Invoice No</th>
		                	<!-- <th style="background-color: #eaeafa; color:#333; text-align: center;">Order Type</th> -->
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Ref. No.</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Customer Name</th>
		                	<!-- <th style="background-color: #eaeafa; color:#333; text-align: center;">Tax</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Qty.</th> -->		                	
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Total Amount</th>		                	
		                	<!--<th style="background-color: #eaeafa; color:#333; text-align: center; visibility: hidden;">Shipping Cost</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Order Discount</th>-->
		                	<!-- <th style="background-color: #eaeafa; color:#333; text-align: center;">Employee</th> -->
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Status</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Edit</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Print</th>		                	
		                </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  
  </FORM>
 <SCRIPT LANGUAGE="JavaScript">
 var tablePOList;
 var FROM_DATE, TO_DATE, DIRTYPE, USER, ORDERNO, JOBNO, PICKSTATUS, ORDERTYPE, STATUS_ID, EMPNO, CUSTOMERTYPE,INVOICENO;
 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,"PICKSTATUS":PICKSTATUS,
 		"ORDERTYPE":ORDERTYPE,"STATUS_ID":STATUS_ID,"INVOICE":"N","EMPNO":EMPNO,"CUSTOMERTYPE":CUSTOMERTYPE,"INVOICENO":INVOICENO,"ACTION": "VIEW_GOODS_ISSUE_WP_SMRY","PLANT":"<%=plant%>",LOGIN_USER:"<%=USERID%>"
 		
 		
 	}
 }  
  function onGo(){
//debugger;
  
var flag    = "false";

    FROM_DATE      = document.form1.FROM_DATE.value;
    TO_DATE        = document.form1.TO_DATE.value;
    DIRTYPE        = document.form1.DIRTYPE.value;
   USER           = document.form1.CUSTOMER.value;
    ORDERNO        = document.form1.DONO.value;
    JOBNO          = document.form1.JOBNO.value;
    PICKSTATUS     = document.form1.PICKSTATUS.value;
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
    STATUS_ID      = document.form1.STATUS_ID.value; 
   EMPNO = document.form1.EMP_NAME.value;
   CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
   INVOICENO   = document.form1.INVOICENO.value;
  
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
    var urlStr = "/track/OutboundOrderHandlerServlet";
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
 		        	
 		        	if(typeof data.items[0].dono === 'undefined'){
 		        		return [];
 		        	}else {
 		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
 		        			data.items[dataIndex]['SNO'] = dataIndex + 1;
 		        			//<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="+dono+" >
 		        			//data.items[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.items[dataIndex]['dono']+'" >';
 		        			//data.items[dataIndex]['dono'] = '<a href=/track/jsp/PrintDODetails.jsp?DONO=' +data.items[dataIndex]['dono']+ '&INVOICE='+'INVOICE'+'&TYPE='+'TYPE'+'&FROMDATE='+'fdate'+'&TODATE='+'tdate'+'">' + data.items[dataIndex]['dono'] + '</a>';
 		        			data.items[dataIndex]['EDIT'] = '<a href="/track/jsp/dynamicprdswithprice.jsp?TRANID=' +data.items[dataIndex]['dono']+ '&EDIT='+'EDIT'+'"><i class="fa fa-pencil-square-o"></i></a>';
 		        			//data.items[dataIndex]['dono'] = '<a href="/track/jsp/dynamicprdswithprice.jsp?TRANID=' +data.items[dataIndex]['dono']+ '&EDIT='+'EDIT'+'">' + data.items[dataIndex]['dono'] + '</a>';
 		        			//<a href=/track/jsp/PrintDODetails.jsp?DONO=" +dono+ "&INVOICE="+INVOICE+"&TYPE="+TYPE+"&FROMDATE="+fdate+"&TODATE="+tdate+">" + dono + "</a>
 		        			data.items[dataIndex]['PRINT'] = '<a href="/track/DynamicProductServlet?cmd=PrintInvoiceWP&TRANID=' +data.items[dataIndex]['dono'] + '"><i class="fa fa-print"></i></a>';                                              
 		        			data.items[dataIndex]['unitprice'] = parseFloat(data.items[dataIndex]['unitprice']).toFixed(<%=numberOfDecimal%>);
 		        		}
 		        		
 		        		return data.items;
 		        	}
 		        }
 		    },
 	        "columns": [
 	        	//{"data": 'CHKOB', "orderable": false},
 	        	{"data": 'SNO', "orderable": false},
 	        	{"data": 'dono', "orderable": false},
 	        	{"data": 'trandate', "orderable": false},
 	        	{"data": 'invoiceno', "orderable": false},
     			/* {"data": 'orderType', "orderable": false}, */
     			{"data": 'jobnum', "orderable": false},
     			{"data": 'custname', "orderable": false},
     		/* 	{"data": 'tax', "orderable": false},
     			{"data": 'qty', "orderable": false},  */    			
     			{"data": 'unitprice', "orderable": false},
     			//{"data": 'shippingcost', "orderable": false},
     			//{"data": 'orderdiscount', "orderable": false},
     			//{"data": 'empname', "orderable": false},     			
     			{"data": 'status_id', "orderable": false},
     			{"data": 'EDIT', "orderable": false},
     			{"data": 'PRINT', "orderable": false}
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
          //'<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Transaction ID</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Transaction Date</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Invoice No</TH>'+
          //'<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
          //'<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
          //'<TH><font color="#ffffff" align="left"><b>Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Total Amount</TH>'+
         // '<TH><font color="#ffffff" align="left"><b>Shipping Cost</TH>'+
         // '<TH><font color="#ffffff" align="left"><b>Order Discount</TH>'+
        //  '<TH><font color="#ffffff" align="left"><b>Tax%</TH>'+
        //  '<TH><font color="#ffffff" align="left"><b>Order Price</TH>'+
        //  '<TH><font color="#ffffff" align="left"><b>Tax</TH>'+
        //'<TH><font color="#ffffff" align="left"><b>Employee</TH>'+ 
         //'<TH><font color="#ffffff" align="left"><b>Status</TH>'+ 
          '<TH><font color="#ffffff" align="center"><b>Status</TH>'+	
          '<TH><font color="#ffffff" align="center"><b>Edit</TH>'+
          '<TH><font color="#ffffff" align="center"><b>Print</TH>'+
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
 });
 function onPrint(data){
	  alert(data);
	  document.form.cmd.value="PrintInvoiceWP" ;
	   document.form.action  = "/track/DynamicProductServlet?cmd=PrintInvoiceWP&TRANID="+data;
	   document.form.submit();
 }
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
