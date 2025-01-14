<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Time Attendence Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 22 2014, Description:To open inbound order summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
   document.form.FROM_DATE.value;
  document.form.TO_DATE.value;
  document.form.CUST_NAME.value;
  document.form.EVENTID.value;
 
  document.form.action="/track/ReportServlet?action=TimeAttendance_Details_Excel";
  document.form.submit();
 }
//---End Modified by Deen on May 22 2014, Description:To open inbound order summary  in excel powershell format
function onGo(){

  document.form.action="TimeAttendanceReport.jsp";
  document.form.submit();
}

</script>


<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();


session= request.getSession();
String plant = (String)session.getAttribute("PLANT");

String PLANT="",USERID="",FROM_DATE ="",  TO_DATE = "",CUST_CODE="",CUST_NAME="",EVENTID="",USER="",fdate="",tdate="",PGaction="";
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
EVENTID       = _strUtils.fString(request.getParameter("EVENTID"));



if(PGaction.equalsIgnoreCase("View")){
 
 try{
      


 }catch(Exception e) { }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form" method="post" action="TimeAttendanceReport.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="L_CUST_NAME" value="">
<input type="hidden" name="CUST_CODE" value="<%=CUST_CODE%>">
<input type="hidden" name="ACTIVE" value="">
<input type="hidden" name="EVENT_DESC" value="">
<input type="hidden" name="type" value="">

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
        <label class="control-label col-sm-2" for="Employee Name">Employee ID/Name:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="CUST_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUST_NAME)%>" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Event">Event ID:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="EVENTID" id="EVENTID" type = "TEXT" value="<%=StrUtils.forHTMLTag(EVENTID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('EventList.jsp?EVENT='+form.EVENTID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Event Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
  	 <div class="col-sm-offset-5 col-sm-4">   
  	<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTA');}"> <b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  	</div> 
</div>

<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
      <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTA');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 
   
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
  
    
  </FORM>
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
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
  <SCRIPT LANGUAGE="JavaScript">
function onGo(){
	var fromDt = document.form.FROM_DATE.value;
    var toDt = document.form.TO_DATE.value;
    var custname = document.form.CUST_NAME.value;
    var eventid = document.form.EVENTID.value;
   
   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ClockInOutHandlerServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {EMPLOYEE:custname,EVENTID:eventid,FROM_DATE:fromDt,TO_DATE:toDt,ACTION: "VIEW_INV_TIME_ATTENDANCE_REPORT",PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USERID%>"},dataType: "json", success: callback });
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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.INVDETAILS
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
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center" class="table">'+
                    '<TR style="background-color : #eaeafa">'+
                    '<TH>Employee ID</TH>'+
                    '<TH>Employee Name</TH>'+
                    '<TH>Alternate Employee</TH>'+
                    '<TH>Event</TH>'+
                    '<TH>Date</TH>'+
                    '<TH>Clock In </TH>'+
                    '<TH>Clock Out</TH>'+
                    '<TH>Productive Time</TH>'+
                    '<TH>Idle Time</TH>'+
                    '<TH>Idle/Productive</TH>'+
                    '</TR>';
                
}
   
document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>

