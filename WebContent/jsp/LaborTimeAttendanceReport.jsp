<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 22 2014, Description:To open inbound order summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
  
  document.form.action="/track/ReportServlet?action=LaborTimeAttendance_Details_Excel";
  document.form.submit();
 }
//---End Modified by Deen on May 22 2014, Description:To open inbound order summary  in excel powershell format
function onGo(){

  document.form.action="LaborTimeAttendanceReport.jsp";
  document.form.submit();
}

</script>

<title>Labor Time Attendance Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();


session= request.getSession();
String plant = (String)session.getAttribute("PLANT");

String PLANT="",USERID="",FROM_DATE ="",  TO_DATE = "",CUST_CODE="",CUST_NAME="",WONO="",OPR_SEQNUM="",USER="",fdate="",tdate="",PGaction="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
CUST_CODE     = _strUtils.fString(request.getParameter("CUST_CODE"));
CUST_NAME     = _strUtils.fString(request.getParameter("CUST_NAME"));
USER          = _strUtils.fString(request.getParameter("USER"));
WONO = _strUtils.fString(request.getParameter("WONO"));
OPR_SEQNUM = _strUtils.fString(request.getParameter("OPR_SEQNUM"));




if(PGaction.equalsIgnoreCase("View")){
 
 try{
      


 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="LaborTimeAttendanceReport.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="L_CUST_NAME" value="">
<input type="hidden" name="CUST_CODE" value="<%=CUST_CODE%>">
<input type="hidden" name="ACTIVE" value="">
<input type="hidden" name="EVENT_DESC" value="">
<input type="hidden" name="type" value="">




  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Labor Time Attendance Summary</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%"  cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">&nbsp;To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 <TH ALIGN="left" >&nbsp;Employee : </TH>
         <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="<%=CUST_NAME%>" size="20"  MAXLENGTH=100>
             <a href="#" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
            
         </TD>
    </TR>
       <TR>
         
          <TH ALIGN="left" width="15%">&nbsp;Work Order No:  </TH>
          <TD width="13%"><INPUT type="TEXT" size="20" MAXLENGTH="50" name="WONO" id="WONO"	value="<%=WONO%>"  /> 
                         <a href="#" onClick="javascript:popUpWin('WO_list.jsp?WONO='+form.WONO.value+'&TYPE=WIPSUMMARY');">
                           <img src="images/populate.gif" border="0"/>
                          </a>
          </TD> 
   
           <TH ALIGN="left" width="15%">&nbsp;Operation Seq:  </TH>
          <TD width="13%"><INPUT name="OPR_SEQNUM" id="OPR_SEQNUM" type = "TEXT" value="<%=OPR_SEQNUM%>" size="20"  MAXLENGTH=50 >
              <a href="#" onClick="javascript:popUpWin('OperationSequenceList.jsp?OPERATIONSEQ='+form.OPR_SEQNUM.value+'&TYPE=WIPSUMMARY');"><img
			src="images/populate.gif" border="0"></a> 
          </TD>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp; </TH>
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>
  </TABLE>
   <br>
  
   <br>
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
  <br>
  
     <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   
   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" >  
 
   </td>
   </TR>
    </table>
    
  </FORM>
</html>
<%@ include file="footer.jsp"%>
  <SCRIPT LANGUAGE="JavaScript">
function onGo(){
	var fromDt = document.form.FROM_DATE.value;
    var toDt = document.form.TO_DATE.value;
    var custname = document.form.CUST_NAME.value;
    var wono = document.form.WONO.value;
    var opseq = document.form.OPR_SEQNUM.value;
   
   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/WIPReportingServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {EMPLOYEE:custname,WONO:wono,OPSEQ:opseq,FROM_DATE:fromDt,TO_DATE:toDt,action: "VIEW_LABOR_TIME_ATTENDANCE_REPORT",PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
//  End code modified by Deen for product brand on 11/9/12 
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
                       
	        	outPutdata = outPutdata+item.LABORTIMEDETAILS
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
                    '<TH><font color="#ffffff" align="left"><b>Employee </TH>'+
                    '<TH><font color="#ffffff" align="left"><b>WONO</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Parent Item</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Operation Seq</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Quantity</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Date</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Clock In </TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Clock Out</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Productive Time</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Idle Time</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Total Productive</TH>'+
                    '</TR>';
                
}
   
document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>

