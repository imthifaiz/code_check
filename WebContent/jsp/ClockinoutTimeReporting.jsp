<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Clock In/Out Tracking (Individual)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'TimeReporting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function popUpfullWin(URL) {
 // subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width='+screen.width+',height='+screen.height+',left = 0,top = 0');
   subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width=800,height=800,left = 200,top = 184');
}


function onSubmit()
{
	var trantype=document.form.TRANTYPE.value;
	var event = document.form.EVENTID.value;
	var empid = document.form.CUST_CODE.value;
	
	var radio_choice = false;
	    for (i = 0; i < document.form.TRANTYPE.length; i++)
	    {
	        if (document.form.TRANTYPE[i].checked)
	        radio_choice = true; 
	    }
	    if (!radio_choice)
	    {
	    alert("Please Select Tran Type");
	    return (false);
	    }
	 
	if(event.length<0||event.length==0)
	{		
		alert('Please Enter Registration Event');
		return false;
	}
	else if(empid.length<0||empid.length==0)
	{
		
		alert('Please Enter Employee ID');
		return false;
	}
	else
	{
     	 document.form.action="/track/ClockInOutServlet?Submit=Time_InOut_Tracking";
		 document.form.submit();
	}
 }
/*
if (document.layers)
	  document.captureEvents(Event.KEYDOWN);
	  document.onkeydown =
	    function (evt) { 
	      var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	      if (keyCode == 13)   //13 = the code for pressing ENTER 
	      {
	    	  onSubmit();
	      }
	    };*/
function setFocus(){
	var trantype = document.form.TRANTYPE.value;
	var item = document.form.EVENTID.value;
	if(trantype.length==0 || trantype==null)
	{
		document.form.EVENTID.focus();
	}
	else if(item.length==0 || item==null)
	{
		document.form.EVENTID.focus();
	}else{
	    document.form.CUST_CODE.focus();
	}
}
</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil  = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
String fieldDescError="",PGaction="",plant="";

session= request.getSession();


PGaction     = _strUtils.fString(request.getParameter("action")).trim();
plant = (String)session.getAttribute("PLANT");
String COMPANY="",html = "",cntRec ="false",fieldDesc="",TRANTYPE="",EVENT="",EMPID="",CUSTOMERID="";
TRANTYPE = _strUtils.fString((String)session.getAttribute("TRANTYPE"));
EVENT = _strUtils.fString(request.getParameter("EVENTID")); 
EMPID = _strUtils.fString(request.getParameter("CUST_CODE")); 
fieldDesc = StrUtils.fString(request.getParameter("MSG"));
fieldDescError = StrUtils.fString(request.getParameter("ERRORMSG"));
//CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME"));
 

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<form class="form-horizontal" name="form" method="post" action="ClockinoutTimeReporting.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<br>
  <table  border="0" width="100%" cellspacing="1" cellpadding="2"  bgcolor="" align="center">
 	<tr>
 		<td align="center">
 			<font class="maingreen"><%=fieldDesc%></font>
	        <font class="mainred"><%=fieldDescError%></font>
 		</td>
 	</tr>
 </table>


<div class="form-group">
<label class="control-label col-sm-4" for="Tran Type">Tran Type:</label>
  <div class="col-sm-4">
    <label class="radio-inline">
      <input type="radio" name="TRANTYPE" type = "radio" value="Clockin"   <%if(TRANTYPE.equalsIgnoreCase("CLOCKIN")) {%>checked <%}%> >CLOCK-IN
    </label>
    <label class="radio-inline">
      <input type="radio" name="TRANTYPE" type = "radio" value="Clockout"   <%if(TRANTYPE.equalsIgnoreCase("CLOCKOUT")) {%>checked <%}%>>CLOCK-OUT
    </label>
     </div>
</div>


<div class="form-group">
  <label class="control-label col-sm-4" for="Registration Events">
  <a href="#" data-placement="left">
   	    <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Registration Events:</label>
      <div class="col-sm-4">
         <div class="input-group">    
    		<input class="form-control"  name="EVENTID" id="EVENTID" type = "TEXT" value="<%=EVENT%>" size="20" 
      						onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateEvent();}" MAXLENGTH=50 >
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('EventList.jsp?EVENT='+form.EVENTID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Registration Events Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		<input type="hidden" name="EVENT_DESC" value="">
		<input type="hidden" name="type" value="">        
       </div>
       </div>
       
       
<div class="form-group">
  <label class="control-label col-sm-4" for="Employee ID">
  <a href="#" data-placement="left">
   	    <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Employee ID:</label>
      <div class="col-sm-4">
         <div class="input-group">    
    		<input class="form-control"  name="CUST_CODE" id="CUST_CODE" type = "TEXT" value="<%=EMPID%>" size="20" 
          						onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateEmployee();}" MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_CODE.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		 <input type="hidden" name="L_CUST_NAME" value="">
          <!--   <INPUT class="Submit" type="BUTTON" value="View" onClick="onViewDetails();">-->        
       </div>
          </div>
          
 <div class="form-group">
   <label class="control-label col-sm-4" for="Employee Name">Employee Name:</label>
      <div class="col-sm-4">   
    		<input class="form-control" name="CUST_NAME" type = "TEXT" value="" size="20"  MAXLENGTH=20>
   		</div>        
       </div>
       
<!--  <table id="dynamictb" border="0" width="80%"  cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
	<TR>
          <TH ALIGN="right">Employee Name :&nbsp;&nbsp;</TH>
          <TD align="left" ><INPUT name="CUST_NAME" type = "TEXT" value="" size="20"  MAXLENGTH=20>
            </TD>
          
    </TR>    
  </TABLE>-->
  
  <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" value="Back" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Submit" onClick="onSubmit();"><b>Submit</b></button>&nbsp;&nbsp;
     </div>
       </div>
  
  </form>
  </div>
  </div>
  </div>
  
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

  

<script>
/*setFocus();
window.onload = function hideTable()
{	document.getElementById('dynamictb').style.visibility = "hidden";	
};
*/
function onViewDetails() {
	var custcode = document.form.CUSTNO.value;	
		
	var urlStr = "/track/mobilehandlingservlet";
		
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				PLANT : "<%=plant%>",
				CUSTCODE : custcode,
				ACTION : "VIEW_CUSTDETAIL"
				},
				dataType : "json",
				async: false,
				success : function(data) {
					if (data.status == "101") {
						alert("Session cookies are Disabled/Expired,Please enable cookies to proceed");
                                         
					}
					if (data.status == "100") {
						var resultobj = data.result;
						document.getElementById("CUSTNO").value=resultobj.custcode;
					
				document.getElementById('dynamictb').style.visibility = "visible";	
				document.getElementById("CUSTNAME").value=resultobj.custname;
						
					}
					
				}
			});
		}

function validateEvent() {
	
	var eventId = document.getElementById("EVENTID").value;
	if(eventId=="" || eventId.length==0 ) {
		alert("Enter Event!");
		document.getElementById("EVENTID").focus();
	}else{
		var urlStr = "/track/ClockInOutServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				EVENTID : eventId,
				PLANT : "<%=plant%>",
				Submit : "VALIDATE_EVENT"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.getElementById("CUST_CODE").value = "";
						document.getElementById("CUST_CODE").focus();
					} else {
						alert("Not a valid Event");
						document.getElementById("EVENTID").value = "";
						document.getElementById("EVENTID").focus();
					}
				}
			});
		}
	}
function validateEmployee() {
	
	var empId = document.getElementById("CUST_CODE").value;
	if(empId=="" || empId.length==0 ) {
		alert("Enter Employee!");
		document.getElementById("CUST_CODE").focus();
	}else{
		var urlStr = "/track/ClockInOutServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				CUST_CODE : empId,
				PLANT : "<%=plant%>",
				Submit : "VALIDATE_EMPLOYEE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("CUST_NAME").value = resultVal.empname;
						onSubmit();
					} else {
						alert("Not a valid Employee");
						document.getElementById("CUST_CODE").value = "";
						document.getElementById("CUST_NAME").value = "";
						document.getElementById("CUST_CODE").focus();
					}
				}
			});
		}
	}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

