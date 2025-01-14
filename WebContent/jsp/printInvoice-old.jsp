<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<html>
<head>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
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

function onRePrint(){
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
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHBATCH&Submit=Print Outbound Order With Price";
    document.form1.submit();
}
function onRePrintWithContainer(){
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
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHBATCHANDCONTAINER&Submit=Print Outbound Order";
    document.form1.submit();
}
function onRePrintWithOutBatch(){
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
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHOUTBATCH&Submit=Print Outbound Order With Price";
    document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<title>Outbound Order Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
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
String CUSTOMERID="",statusID="",EMP_NAME="",CUSTOMERTYPE="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",allChecked = "";

plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
String btnContainerDisabled="disabled";
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

/*
if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ORD_WITH_PRICE";//OUTBOUNDSUMMARYWITHPRICE
}*/

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Outbound Order with price</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "15%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   
  
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">&nbsp;To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
       <TR>
          <TH ALIGN="left" >&nbsp;Ref No : </TH>
          <TD><INPUT name="JOBNO" type = "TEXT" value="<%=JOBNO%>" size="20"  MAXLENGTH=20></TD>
                 <TH ALIGN="left" >&nbsp;Outbound Order No : </TH>
          <TD><INPUT name="DONO" type = "TEXT" value="<%=DONO%>" size="20"  MAXLENGTH=20>
              <a href="#" onClick="javascript:popUpWin('list/ob_order_list.jsp?DONO='+form1.DONO.value+'&STATUS='+form1.PICKSTATUS.value+'&PICKISSUE=Y'+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
         </TR>
    
     <TR>
           <TH ALIGN="left" >&nbsp;Status : </TH>
          <TD><SELECT NAME ="PICKSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     	    </SELECT></TD>
           <TH ALIGN="left" >&nbsp;Customer Name or ID:  </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=_strUtils.forHTMLTag(CUSTOMER)%>" size="20"  MAXLENGTH=100>
           <a href="#" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
      </TR>
	 <TR>
           <TH ALIGN="left" >&nbsp;Order Type : </TH>
            <TD><INPUT name="ORDERTYPE" type = "TEXT" value="<%=ORDERTYPE%>" size="20"  MAXLENGTH=20> <a href="#" onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=OUTBOUND&ORDERTYPE='+form1.ORDERTYPE.value);">
           <img src="images/populate.gif" border="0"/>
          </a></TD>
           <TH ALIGN="left">&nbsp;Customer Type Desc or ID:</TH>
			<TD><INPUT name="CUSTOMER_TYPE_ID" type="TEXT" value="<%=CUSTOMERTYPE%>" size="20" MAXLENGTH=50> 
				<a href="#"	onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);">
				<img src="images/populate.gif" border="0"></a>        
          </TD>
        </TR>
         <TR>  
          	<TH ALIGN="left">&nbsp;Order Status:</TH>
			<TD><INPUT name="STATUS_ID" type="TEXT" value="<%=statusID%>" size="20" MAXLENGTH=20> <a href="#" onClick="javascript:popUpWin('OrderStatusList.jsp?ORDERSTATUS='+form1.STATUS_ID.value+'&TYPE=OBSUMMARY');">
					<img src="images/populate.gif" border="0"></a>
				</TD>
       			<TH ALIGN="left" >&nbsp;Employee Name or ID:</TH>
          	<TD align="left"><INPUT name="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=20>
             <a href="#" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');">
            <img src="images/populate.gif" border="0"></a>
        		<input type="button" value="View"  onClick="javascript:return onGo();">
			</td>
        
    </TR>
   
  	
  </TABLE>
  <br>
  
      <INPUT type="Hidden" name="DIRTYPE" value="OB_PRINT">
      <table BORDER = "1" WIDTH = "90%" align="center" bgcolor="#dddddd" >
		<tr>
		<td width = "15%">  
			<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	&nbsp; Select/Unselect All 
         </td>
		<tr>
</table>
      <br>
      <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
  <%            
  		DOUtil poUtil = new DOUtil();
  
        Map ma = poUtil.getDOReceiptInvoiceHdrDetails(plant,"OUTBOUND ORDER");
		       //-----Modified by Deen on Feb 26 2014, Description:To display NOCONTAINER data's in PrintWithContainer/Batch/Sno report
		       if(ma.get("DisplayContainer").equals("1"))
		        {
		            	btnContainerDisabled="";
		        }
    	      
    %>  
    <table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#dddddd">
        <tr>
          <td>
    <table cellspacing="2" cellpadding="0" border="0" width="35%" align="center">
      <tr>
        <td align= "center">
        <input type="button" value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" />&nbsp;&nbsp;    </td>
        <td align= "center">
        <td align= "center">
        <input type="button" value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();" />&nbsp;&nbsp;    </td>
         <td align= "center">
        <input type="button" value="PrintWithContainer/Batch/Sno"  name="action" onclick="javascript:return onRePrintWithContainer();" <%=btnContainerDisabled%> />&nbsp;&nbsp;    </td>
        <td align= "center">      <input type="button" value="Back" onClick="window.location.href='../home'"/>    </td>
       
     </tr>
    </table>
      </td>
        </tr>
      </table>
  </FORM>
 <SCRIPT LANGUAGE="JavaScript">
  
  function onGo(){
debugger;
   var flag    = "false";

   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ORDERNO        = document.form1.DONO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var PICKSTATUS     = document.form1.PICKSTATUS.value;
   var ORDERTYPE      = document.form1.ORDERTYPE.value; 
   var STATUS_ID      = document.form1.STATUS_ID.value; 
   var EMPNO          = document.form1.EMP_NAME.value;
   var CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
    
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
 
   document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/OutboundOrderHandlerServlet";
    
     $.ajax({type: "POST",url: urlStr, data: {FDATE:FROM_DATE,TDATE:TO_DATE,DTYPE:DIRTYPE,CNAME:USER,ORDERNO:ORDERNO,JOBNO:JOBNO,PICKSTATUS:PICKSTATUS,ORDERTYPE:ORDERTYPE,STATUS_ID:STATUS_ID,TYPE:"PICKISSUE",INVOICE:"Y",EMPNO:EMPNO,CUSTOMERTYPE:CUSTOMERTYPE,ACTION: "VIEW_OUTBOUND_DETAILS_PRINT",PLANT:"<%=plant%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
 
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
          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
           '<TH><font color="#ffffff" align="left"><b>Pick Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+ 
          '<TH><font color="#ffffff" align="left"><b>Status</TH>'+ 
          '<TH><font color="#ffffff" align="center"><b>Order Status</TH>'+	     
          '</tr>';
              
}
 
document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
            
  </SCRIPT> 
<%@ include file="footer.jsp"%>