<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.db.util.*"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>	
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script>
	var subWin = null;
	function popUpWin(URL) {
 		subWin = window.open(URL, 'mobileNonMember', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=200,height=200,left = 220,top = 184');
	}
</script>

<html>
<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="";
	String DELIVERYDATE="",TIMESLOTS="";
	
	 
	
    //session= request.getSession();
    String plant=(String)session.getAttribute("PLANT");
    
	EMAIL= StrUtils.fString(request.getParameter("EMAIL"));
	//PASSWORD= StrUtils.fString(request.getParameter("PASSWORD"));
	CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
	PAYMENTMODE= StrUtils.fString(request.getParameter("PAYMENTMODE"));
	//NRIC= StrUtils.fString(request.getParameter("NRIC"));
	//DATEOFBIRTH= StrUtils.fString(request.getParameter("DATEOFBIRTH"));
	//NATIONALITY= StrUtils.fString(request.getParameter("NATIONALITY"));
	GENDER= StrUtils.fString(request.getParameter("GENDER"));
	UNITNO= StrUtils.fString(request.getParameter("UNITNO"));
	BUILDING= StrUtils.fString(request.getParameter("BUILDING"));
	STREET= StrUtils.fString(request.getParameter("STREET"));
	POSTALCODE= StrUtils.fString(request.getParameter("POSTALCODE"));
	LANDMARK= StrUtils.fString(request.getParameter("LANDMARK"));
	MOBILE= StrUtils.fString(request.getParameter("MOBILE"));
	//HOME= StrUtils.fString(request.getParameter("HOME"));
	//FAX= StrUtils.fString(request.getParameter("FAX"));
	DELIVERYDATE= StrUtils.fString(request.getParameter("DELIVERYDATE"));
	TIMESLOTS= StrUtils.fString(request.getParameter("TIMESLOTS"));
	
	TimeSlotUtil _TimeSlotUtil = new TimeSlotUtil();
	 List timeslotlist =_TimeSlotUtil.getTimeSlots(plant," ");
	
	
%>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Insert title here</title>
</head>
<form  class="form" name="frmnonmember" method="post"  action="">
		<table>
<!-- Removed Logo Image -->
 <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"></td>
   </tr>
</table> 
	<TABLE  style="background-color:white"  border="0" width="100%" height="50%"; cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
	
	<TR style="height:30px;">
		<TH BGCOLOR="#669900" COLSPAN="11"><FONT color="#ffffff" size=7>Non-Member </FONT>&nbsp;</TH>
	</TR>
	<TR height="5px"></TR>
    
     <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="47%" style="padding:5px;"><font size=7 >Customer Name <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="43%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="CUSTOMERNAME" type="TEXT"  class="inactivemobile
			value="<%=CUSTOMERNAME%>" size="20" MAXLENGTH=40 >
		</TD>
    </TR>
    <TR height="5px"></TR>
	
	<TR align="center">	
	    
	<TD align="right"  class="mobilelabel" width="45%" height="10%" style="padding:5px;"><font size=7 >Email Address <font color=red size =7 >*</font>&nbsp;:</font></td>
	 
		<TD align="left" width="45%" height="0">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="EMAIL" type="TEXT" class="inactivemobile
			value="<%=EMAIL%>" size="20" MAXLENGTH=40 height="50px"
			onkeypress="if((event.keyCode=='13') && ( document.frmnonmember.EMAIL.value.length > 0)) {EmailAvailability();}"
		 ></TD>
	</TR>
     <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Mobile Number <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="MOBILE" type="TEXT"  class="inactivemobile" value="<%=MOBILE%>" size="20" MAXLENGTH=20  >
		</TD>
		
	</TR>
    <TR height="5px"></TR>
    
    <TR  align="center">
	<TD align="right"  width="45%" style="padding:5px;" class="mobilelabel"><font size=7 >Payment Mode  :</font><font >&nbsp;&nbsp; </font></td>
	
    <TD align="left" width="45%" style="padding:5px;">
    &nbsp;&nbsp;&nbsp;&nbsp;
       <select name="PAYMENTMODE" size="1" style="width:270px; height:28px; FONT-SIZE: 20px"">	
			<option value="Others">Cash</option>
	    </select>
	</TD>
    </TR>
    
    <TR height="5px"></TR>
    
    <TR  align="center">
	<TD align="right"  width="45%" style="padding:5px;" ><font size=7 >Gender  :</font><font >&nbsp;&nbsp; </font></td>
	<TD align="left">
	&nbsp;&nbsp;&nbsp;&nbsp;
	<INPUT  style="height:50px;" width="55%" name="GENDER" type = "radio" value="M" checked="checked"   size="10"  MAXLENGTH="10" >
	<font size=7 >Male</font>
		<INPUT style="height:50px; width="55%" name="GENDER" type = "radio" value="F" size="45"  MAXLENGTH="10" >
	<font size=7 >Female</font>
	</TD>
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="right">	
	   	<TD align="right"  class="mobilelabel" width="49%" style="padding:5px;"><font size=7 >Delivery Address :</font><font >&nbsp;&nbsp; </font></td>
		<TD  width="35%" ></TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	
	 <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Unit No <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="UNITNO" type="TEXT"  class="inactivemobile
			value="<%=UNITNO%>" size="20" MAXLENGTH=20  >
		</TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Building <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="BUILDING" type="TEXT"  class="inactivemobile
			value="<%=BUILDING%>" size="20" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Street <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="STREET" type="TEXT"  class="inactivemobile
			value="<%=STREET%>" size="20" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	<TR align="center">	
	    <TD  width="45%"></TD>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="LANDMARK" type="TEXT"  class="inactivemobile
			value="<%=LANDMARK%>" size="20" MAXLENGTH=20  >
		</TD>
    </TR>

		
	<TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Postal Code <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="POSTALCODE" type="TEXT"  class="inactivemobile
			value="<%=POSTALCODE%>" size="20" MAXLENGTH=20  >
		</TD>
    </TR>
    
    <TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Date  :</font><font color=white>&nbsp;*</font></td>
		<TD align="left" width="45%">
				&nbsp;&nbsp;&nbsp;&nbsp;
		<SELECT style="width:100px; height:38px; FONT-SIZE: 23px""  NAME="DAY" size="1">
				<OPTION selected value="0">Day</OPTION>
				<%for(int day=1;day<=31;day++) {%>
					<OPTION value="<%=day %>">
							<%=day %>				
					</OPTION>
			   <%} %>
			</SELECT>
			&nbsp;&nbsp;&nbsp;&nbsp;<font size="10">/</font>
			<SELECT style="width:110px; height:38px; FONT-SIZE: 23px""  NAME="MONTH" size="1">
				<OPTION selected value="0">Month</OPTION>
				<%for(int month=1;month<=12;month++) {
				String[] montharray={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
				int index=0;
				%>
					<OPTION value="<%=month %>">
							<%=montharray[month-1]%>				
					</OPTION>
			   <% index++;
			   } %>
			</SELECT>
			&nbsp;&nbsp;&nbsp;&nbsp;<font size="10">/</font>
			<SELECT style="width:110px; height:38px; FONT-SIZE: 23px""  NAME="YEAR" size="1">
				<OPTION selected value="0">Year</OPTION>
				<%for(int year=2012;year<=2020;year++) {%>
					<OPTION value="<%=year %>">
							<%=year %>				
					</OPTION>
			   <%} %>
			</SELECT>
		</TD>
    </TR>
    
    
    
    <TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Time  :</font><font color=white>&nbsp;*</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<SELECT style="width:285px; height:38px; FONT-SIZE: 23px""  NAME="TIMESLOTS" size="1">
				<OPTION selected value="">Choose</OPTION>
				<% for (int i =0; i<timeslotlist.size(); i++){
                    Map map = (Map) timeslotlist.get(i);
                    String timeslots     = (String) map.get("timeslots");
				  %>
					<OPTION value="<%=timeslots%>"
							<%if(TIMESLOTS.equalsIgnoreCase(timeslots)){%>selected  <%} %>><%=timeslots%>
							
					</OPTION>
			    <%}%>
			</SELECT>
		</TD>
		
    </TR>
    
 
	 
	<TR height="30px"><td colspan="2">&nbsp;</td></TR>
	 <TR align="center">
	 
	  <TD colspan="2" align="center"> 
	  	
	   <INPUT type="button"  value="Submit" 	onClick="if(validate(document.frmnonmember)) {submitForm();}"  class="mobileButton1" />
	  	 	
<input type="hidden" NAME="ISEXIST" ID="ISEXIST" value="">			
	   </TD>
	</TR>
	<TR height="15px"> <TD colspan="2">&nbsp;</TD></TR>
 </TABLE>

</form>

</html>
<script>


function validate(frmnonmember)
{
  var frmRoot=document.frmnonmember;
  var day = document.frmnonmember.DAY.value;
  var month = document.frmnonmember.MONTH.value;
  var year = document.frmnonmember.YEAR.value;
  day = parseInt(day);
  month = parseInt(month);
  year = parseInt(year);
  
  if(frmRoot.CUSTOMERNAME.value=="" || frmRoot.CUSTOMERNAME.value.length==0 )
	 {  
		alert("Please enter customer name!");
		frmRoot.CUSTOMERNAME.focus();
		return false;
  }
  else if(frmRoot.EMAIL.value=="" || frmRoot.EMAIL.value.length==0 )
	 {  
		alert("Please enter email");
		frmRoot.MOBILE.focus();
		return false;
 } 
  else if(frmRoot.MOBILE.value=="" || frmRoot.MOBILE.value.length==0 )
	 {  
		alert("Please enter mobile");
		frmRoot.MOBILE.focus();
		return false;
 } 
  else if(frmRoot.UNITNO.value=="" || frmRoot.UNITNO.value.length==0 )
	 {  
		alert("Please enter unitno!");
		frmRoot.UNITNO.focus();
		return false;
   }
  else if(frmRoot.BUILDING.value=="" || frmRoot.BUILDING.value.length==0 )
	 {  
		alert("Please enter building!");
		frmRoot.BUILDING.focus();
		return false;
    }
  else if(frmRoot.STREET.value=="" || frmRoot.STREET.value.length==0 )
	 {  
		alert("Please enter street!");
		frmRoot.STREET.focus();
		return false;
    }
  else if(frmRoot.POSTALCODE.value=="" || frmRoot.POSTALCODE.value.length==0 )
	 {  
		alert("Please enter postalcode!");
		frmRoot.POSTALCODE.focus();
		return false;
    }
  
  else if( frmRoot.MOBILE.value.length!=8 )
	 {  
		alert("Enter valid mobile number");
		frmRoot.MOBILE.focus();
		return false;
}
 
  else if(!IsNumeric(frmRoot.MOBILE.value))
	 {  
		alert("Enter valid mobile number");
		frmRoot.MOBILE.focus();
		return false;
  }
  else if(!IsNumeric(frmRoot.POSTALCODE.value))
	 {  
		alert("Enter valid postal code");
		frmRoot.POSTALCODE.focus();
		return false;
}
  else if( frmRoot.POSTALCODE.value.length!=6 )
	 {  
		alert("Enter valid postal code");
		frmRoot.POSTALCODE.focus();
		return false;
}
 else if(day>0 || month>0 || year>0)
  {
	  //var dt=frmRoot.DELIVERYDATE.value;
	  if (validDate()==false){		
		   // document.frmnonmember.DELIVERYDATE.focus();
			return false;
		}
	  else
		{
		  document.frmnonmember.submit();
		   return true;
		}
  } 
 else
{
  var flag=  EmailAvailability();
  return flag;
}
}
function EmailAvailability() {
	var email = document.frmnonmember.EMAIL.value;
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	

	if(document.frmnonmember.EMAIL.value=="" ||document.frmnonmember.EMAIL.value.length==0 )
	{
		alert("Please enter email!");
		document.frmnonmember.EMAIL.focus();
		return false;
	}
	
	if(reg.test(email)==false)
	 {
			  alert('Invalid Email Address');
			  document.frmnonmember.EMAIL.focus();
		      return false;
	  
	}
	return true;
	}
function CheckMobileNumber() {
	var mobile = document.frmnonmember.MOBILE.value;	
	
		var urlStr = "/track/mobilehandlingservlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {				
				PLANT : "<%=plant%>",
				MOBILE : mobile,
				ACTION : "ISEXIST_MOBNO"
				},
				dataType : "json",
				async: false,
				success : function(data) {
             if (data.status == "100") {
						document.getElementById("ISEXIST").value="N";
						return true;
					}
					if (data.status == "99") {
						
						alert("Already existing member, Please Login!");
						document.getElementById("ISEXIST").value="Y";
						document.frmnonmember.MOBILE.focus();
						return false;
					}
					
				}
			});
		}
		
function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}
function validDate()
{
	var strDay=document.frmnonmember.DAY.value;
	var strMonth=document.frmnonmember.MONTH.value;
	var strYear=document.frmnonmember.YEAR.value;
	var daysInMonth = DaysArray(12);
	var month=parseInt(strMonth);
	var day=parseInt(strDay);
	var year=parseInt(strYear);
	if (month==0 || month<1 || month>12){
		alert("Please choose a valid month");
		document.frmnonmember.MONTH.focus();
		return false;
	}
	if (day==0 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please choose a valid day");
		document.frmnonmember.DAY.focus();
		return false;
	}
	if (strYear == 0 ){
		alert("Please choose year");
		document.frmnonmember.YEAR.focus();
		return false;
	}
	else
	{
		return true;
	}
}
function isDate(){
	var dtCh= "/";
	var minYear=1900;
	var maxYear=2100;
	
	var dtStr=  document.frmnonmember.DELIVERYDATE.value;
	var daysInMonth = DaysArray(12);
	var pos1=dtStr.indexOf(dtCh);
	var pos2=dtStr.indexOf(dtCh,pos1+1);
	var strDay=dtStr.substring(0,pos1);
	var strMonth=dtStr.substring(pos1+1,pos2);
	var strYear=dtStr.substring(pos2+1);
		
	strYr=strYear;
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
	}
	month=parseInt(strMonth);
	day=parseInt(strDay);
	year=parseInt(strYr);
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : dd/mm/yyyy");
		return false;
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month");
		return false;
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day");
		return false;
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false;
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date");
		return false;
	}
return true;
}


function submitForm(){
	var deliverydate="";
	var day = document.frmnonmember.DAY.value;
	var month = document.frmnonmember.MONTH.value;
	var year = document.frmnonmember.YEAR.value;
	day = parseInt(day);
	month = parseInt(month);
	year = parseInt(year);
	if(day>0&&month>0&&year>0)
    deliverydate = day+"/"+month+"/"+year;
	CheckMobileNumber(); 
	var isExist = document.getElementById("ISEXIST").value;	
	if(isExist=='N'){
	//document.frmnonmember.action="/track/mobilecustomerregisterservlet?action=ADDNON_MEMBER";
	document.frmnonmember.action="MobileShoppingConfirm.jsp?DELIVERYDATE="+deliverydate;
    document.frmnonmember.submit();}
	else{
		return false;
	}  
}

</script>