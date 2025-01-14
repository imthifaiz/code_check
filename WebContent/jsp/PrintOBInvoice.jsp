<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Order Summary With Price";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes"})%>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>

<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 21 2014, Description:To open outbound order summary  in excel powershell format
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
  //document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="/track/deleveryorderservlet?Submit=ExportExcelOutboundOrderSummary";
  document.form1.submit();
}
//---End Modified by Deen on May 21 2014, Description:To open outbound order summary  in excel powershell format
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
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ISSUESTATUS="",ORDERTYPE="",DIRTYPE ="",BATCH ="",USER="",fdate="",tdate="",JOBNO="",DONO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",statusID="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",allChecked = "",EMP_NAME="",CUSTOMERTYPE="";

plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));


String curDate =DateUtils.getDateMinusDays();
FROM_DATE=DateUtils.getDateinddmmyyyy(curDate);
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
USER          = StrUtils.fString(request.getParameter("USER"));
DONO       = StrUtils.fString(request.getParameter("DONO"));
CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));

allChecked = StrUtils.fString(request.getParameter("allChecked"));

/*
if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ORD_WITH_PRICE";//OUTBOUNDSUMMARYWITHPRICE
}*/

%>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Order Summary with Price</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" target="_blank">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
 
 <div id="target" style="display:none;">
  		<div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group" >
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
      	</div>
    	</div>
    	</div>
    	</div>
    	
    	<div class="form-group">
        <label class="control-label col-sm-2" for="Ref  No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="JOBNO" id="JOBNO" type = "TEXT" value="<%=StrUtils.forHTMLTag(JOBNO)%>" size="30"  MAXLENGTH=50>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Outbound Order">Order Number:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="DONO" id="DONO" type = "TEXT" value="<%=StrUtils.forHTMLTag(DONO)%>" size="30"  MAXLENGTH=20> 
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'DONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
    	</div> 
 		</div>
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Status">Status:</label>
       <div class="col-sm-3">
      	    <SELECT class="form-control" NAME ="PICKSTATUS" id="PICKSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Customer Name">Customer Name/ID:</label>
       <div class="col-sm-3">
<!--       	<div class="input-group"> -->
    	<INPUT class="form-control" name="CUSTOMER" id="CUSTOMER" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" size="30"  MAXLENGTH=20>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--     	</div>  -->
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Order Type">Order Type:</label>
       <div class="col-sm-3">
<!--       	    <div class="input-group"> -->
    		<INPUT class="form-control" name="ORDERTYPE" id="ORDERTYPE" type = "TEXT" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" size="30"  MAXLENGTH=20>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/displayOrderType.jsp?OTYPE=OUTBOUND&ORDERTYPE='+form1.ORDERTYPE.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		</div> -->
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Customer Type">Customer Type ID:</label>
       <div class="col-sm-3">
<!--       	<div class="input-group"> -->
    	<INPUT class="form-control" name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>" size="30"  MAXLENGTH=20>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--     	</div>  -->
 		</div>
 		</div> 
 		</div>
 		
 			
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Employee">Employee Name/ID:</label>
        <div class="col-sm-3">
<!--       	    <div class="input-group"> -->
    		<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" size="30"  MAXLENGTH=20>
   		 <!-- 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		</div> -->
  		</div>
  		<div class="form-inline">
	  	<div class="col-sm-offset-2 col-sm-4">   
	  	<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  		
<!--   		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
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
      <div class="col-sm-offset-7">
<!--         <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp; -->
<!--   		<button type="button" class="Submit btn btn-default"  onClick="submitForm('Print Multiple Invoice');"><b>Print Sales Order With Price</b></button>&nbsp; -->
<!--   		<button type="button" class="Submit btn btn-default" onClick="window.location.href='home.jsp'"><b>Back</b></button> -->
  		<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','OBO');}"><b>Back</b></button> -->
  	    </div>
         </div>
       	  </div>
 		
 		<div class="row">
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
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">CHK</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">S/N</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">ORDER NO</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">ORDER TYPE</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">REF. NO</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">CUSTOMER NAME</th>
		                	<!-- <th style="background-color: #eaeafa; color:#333; text-align: center;">UOM</th> -->
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">ORDER QTY</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PICK QTY</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">ISSUE QTY</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">EMPLOYEE</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">STATUS</th>
		                	
		                </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  <div class="form-group">
  	<div class="col-sm-12" align="center"> 
  <button type="button" class="Submit btn btn-default"  onClick="submitForm('Print Multiple Invoice');"><b>Print Sales Order With Price</b></button>&nbsp;
  </div>
  </div>
  </FORM>
 <script>
 var tablePOList;
 var FROM_DATE, TO_DATE, DIRTYPE, USER, ORDERNO, JOBNO, PICKSTATUS, ORDERTYPE, STATUS_ID, EMPNO, CUSTOMERTYPE;
 function getParameters(){
 	return {
 		"FDATE":FROM_DATE,"TDATE":TO_DATE,"DTYPE":DIRTYPE,"CNAME":USER,"ORDERNO":ORDERNO,"JOBNO":JOBNO,"PICKSTATUS":PICKSTATUS,
 		"ORDERTYPE":ORDERTYPE,"STATUS_ID":STATUS_ID,"INVOICE":"N","EMPNO":EMPNO,"CUSTOMERTYPE":CUSTOMERTYPE,"ACTION": "VIEW_OUTBOUND_DETAILS_PRINT","PLANT":"<%=plant%>",LOGIN_USER:"<%=USERID%>"
 		
 		
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
  
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
    
    /* storeInLocalStorage('PrintOBInvoice_FROMDATE', FROM_DATE);
	storeInLocalStorage('PrintOBInvoice_TODATE', TO_DATE);
	storeInLocalStorage('PrintOBInvoice_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('PrintOBInvoice_CUSTOMER_TYPE_ID', CUSTOMERTYPE);
	storeInLocalStorage('PrintOBInvoice_DONO', ORDERNO);
	storeInLocalStorage('PrintOBInvoice_JOBNO', JOBNO);
	storeInLocalStorage('PrintOBInvoice_ORDERTYPE',ORDERTYPE);
	storeInLocalStorage('PrintOBInvoice_PICKSTATUS', $('#PICKSTATUS').val());
	storeInLocalStorage('PrintOBInvoice_EMPNAME', EMPNO); */
    var urlStr = "/track/OutboundOrderHandlerServlet";
    if (tablePOList){
 	   tablePOList.ajax.url( urlStr ).load();
    }else{
 	   tablePOList = $('#tablePOList').DataTable({
 			"processing": true,
 			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
//  			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
 		        			data.items[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.items[dataIndex]['dono']+'" >';
 		        			//data.items[dataIndex]['dono'] = '<a href=/track/jsp/PrintDODetails.jsp?DONO=' +data.items[dataIndex]['dono']+ '&INVOICE='+'INVOICE'+'&TYPE='+'TYPE'+'&FROMDATE='+'fdate'+'&TODATE='+'tdate'+'">' + data.items[dataIndex]['dono'] + '</a>';
 		        			data.items[dataIndex]['dono'] = '<a href="../salesorder/pdfdetails?DONO=' +data.items[dataIndex]['dono']+ '&INVOICE='+'INVOICE'+'&TYPE='+ORDERTYPE+'&FROMDATE='+FROM_DATE+'&TODATE='+TO_DATE+'">' + data.items[dataIndex]['dono'] + '</a>';
 		        			//<a href=/track/jsp/PrintDODetails.jsp?DONO=" +dono+ "&INVOICE="+INVOICE+"&TYPE="+TYPE+"&FROMDATE="+fdate+"&TODATE="+tdate+">" + dono + "</a>
 		        		}
 		        		return data.items;
 		        	}
 		        }
 		    },
 	        "columns": [
 	        	{"data": 'CHKOB', "orderable": false},
 	        	{"data": 'SNO', "orderable": false},
 	        	{"data": 'dono', "orderable": false},
     			{"data": 'ordertype', "orderable": false},
     			{"data": 'jobnum', "orderable": false},
     			{"data": 'custname', "orderable": false},
     			/* {"data": 'UOM', "orderable": false}, */
     			{"data": 'qtyor', "orderable": false},
     			{"data": 'qtypick', "orderable": false},
     			{"data": 'qty', "orderable": false},
     			{"data": 'empname', "orderable": false},
     			{"data": 'status', "orderable": false},
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


  function getcustname(CUSTNO){
		
	}

function getTable(){
   return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
          /* '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+ */
           '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Pick Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+ 
         '<TH><font color="#ffffff" align="left"><b>Status</TH>'+ 
         
          '</tr>';
              
}
 
           
  </SCRIPT> 
  </div></div></div>
   
                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 /* getLocalStorageValue('PrintOBInvoice_FROMDATE', $('#FROM_DATE').val(), 'FROM_DATE');
	 getLocalStorageValue('PrintOBInvoice_TODATE', '', 'TO_DATE');
	 getLocalStorageValue('PrintOBInvoice_CUSTOMER', '', 'CUSTOMER');
	 getLocalStorageValue('PrintOBInvoice_CUSTOMER_TYPE_ID', '', 'CUSTOMER_TYPE_ID');
	 getLocalStorageValue('PrintOBInvoice_DONO', '', 'DONO');
	 getLocalStorageValue('PrintOBInvoice_JOBNO', '', 'JOBNO');
	 getLocalStorageValue('PrintOBInvoice_ORDERTYPE', '', 'ORDERTYPE');
	 getLocalStorageValue('PrintOBInvoice_PICKSTATUS', '', 'PICKSTATUS');
	 getLocalStorageValue('PrintOBInvoice_EMPNAME', '', 'EMP_NAME'); */
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
  
  <script>


var plant= '<%=plant%>';
/* Customer Auto Suggestion */
$('#CUSTOMER').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'CNAME',  
	  async: true,   
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
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
		    return '<p onclick="getcustname(\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
// 	    return '<div onclick="getvendname(\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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

/* Customer Type Auto Suggestion */
$('#CUSTOMER_TYPE_ID').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'CUSTOMER_TYPE_ID',  
	  async: true,   
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "getCustomerListTypeData",
			QUERY : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.CUST_TYPE_MST);
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
	    return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p></div>';
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

/* Employee Type Auto Suggestion */
$('#EMP_NAME').typeahead({
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
		
		/* To reset Order number Autosuggestion*/
		$("#ORDERNO").typeahead('val', '"');
		$("#ORDERNO").typeahead('val', '');
		
		
	});


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
			PLANT : plant,
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
		var urlStr = "/track/InvoiceServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
			OTYPE : "SALES",
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

</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
