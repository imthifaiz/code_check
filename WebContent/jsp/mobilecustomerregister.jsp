<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="",result="";
	
    //session= request.getSession();
   
    String plant=(String)session.getAttribute("PLANT");
   	
    	EMAIL= StrUtils.fString(request.getParameter("EMAIL"));
    	PASSWORD= StrUtils.fString(request.getParameter("PASSWORD"));
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
	if(request.getParameter("result")!=null)
		result = request.getParameter("result");
	//HOME= StrUtils.fString(request.getParameter("HOME"));
	//FAX= StrUtils.fString(request.getParameter("FAX"));
	
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>	
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Insert title here</title>
</head>

<form  class="form" name="mobilecustomerregister" method="post"  action="/track/mobilecustomerregisterservlet?">
	<table>
	<!-- Removed logo Image -->
 <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"></td>
   </tr>
</table>
 
	<TABLE  style="background-color:white"  border="0" width="100%" height="50%"; cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
	
	<TR style="height:30px;">
		<TH BGCOLOR="#669900" COLSPAN="11"><FONT color="#ffffff" size=7>Member Registration</FONT>&nbsp;</TH>
	</TR>
	<TR height="5px"></TR>
    <tr><td colspan="11" align="center"><B><font size="5" >
<%=result%></font></B></td></tr>
     <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Customer Name <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
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
		<INPUT name="EMAIL" type="TEXT" 
			value="<%=EMAIL%>" size="20" MAXLENGTH=40 class="inactivemobile"
			onkeypress="if((event.keyCode=='13') && ( document.mobilecustomerregister.EMAIL.value.length > 0)) {EmailAvailability();}" ></TD>
	</TR>
     <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Mobile (UserID for Login) <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="MOBILE" type="TEXT" class="inactivemobile  value="<%=MOBILE%>" size="20" MAXLENGTH=20  >
		</TD>
		
	</TR>
	
	
    <TR height="5px"></TR>
    <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Password <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="PASSWORD" type="PASSWORD"  class="inactivemobile
			value="<%=PASSWORD%>" size="20" MAXLENGTH=40  >
		</TD>
    </TR>
    
    <TR height="5px"></TR>
    <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Confirm Password <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="CONFIRMPASSWORD" type="PASSWORD"  class="inactivemobile"
			value="<%=CONFIRMPASSWORD%>" size="20" MAXLENGTH=40  >
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
	<INPUT  style="height:21px;" width="45%" name="GENDER" type = "radio" value="M" checked="checked"   size="10"  MAXLENGTH="10" >
	<font size=7 >Male</font>
		<INPUT style="height:21px; width="45%" name="GENDER" type = "radio" value="F" size="45"  MAXLENGTH="10" >
	<font size=7 >Female</font>
	</TD>
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="right">	
	   	<TD align="right"  class="mobilelabel" width="40%" style="padding:5px;"><font size=7 >Delivery Address :</font><font >&nbsp;&nbsp; </font></td>
		<TD  width="45%" ></TD>
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
    
 <!--   <TR height="5px"></TR>
    
     <TR align="center">	
	   	<TD  width="40%" align="right"  class="mobilelabel"  style="padding:5px;"><font size=5 >Contact Number  :</font><font >&nbsp;&nbsp; </font></td>
	   	<TD></TD>
	 </TR>
	 
	
	<TR height="5px"></TR>
	
	<TR align="center">
	    <TD align="right"  width="45%" style="padding:5px;"><font size=5 >Home  :</font><font >&nbsp;&nbsp; </font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="HOME" type="TEXT"  class="inactivemobile
			value="<%=HOME%>" size="40" MAXLENGTH=20 >
		</TD>
		
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="center">	
	  <TD align="right"  class="mobilelabel" width="45%" style="padding:7px;"><font size=5 >Fax  :</font><font >&nbsp;&nbsp; </font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="FAX" type="TEXT"  class="inactivemobile
			value="<%=FAX%>" size="40" MAXLENGTH=20>
		</TD>
	
	 </TR>  -->
	 
	<TR height="45px"><td>&nbsp;</td></TR>
	 <TR align="center">
	  
	  <TD colspan="2" align="center"> 
	  	
	   <INPUT type="button"  value="Submit" onClick="if(validate(document.mobilecustomerregister)) {submitForm();}"  class="mobileButton" />
	  	 	
			
	   </TD>
	</TR>
 </TABLE>

</form>

</html>
<script>
function submitForm(){

	
	document.mobilecustomerregister.action="/track/mobilecustomerregisterservlet?action=AddCustomer";
	
    document.mobilecustomerregister.submit();
}

function validate(mobilecustomerregister)
{
  var frmRoot=document.mobilecustomerregister;
    
  if(frmRoot.PASSWORD.value=="" || frmRoot.PASSWORD.value.length==0 )
	 {
		alert("Please enter password!");
		frmRoot.PASSWORD.focus();
		return false;
     }
  
  if(frmRoot.CONFIRMPASSWORD.value=="" || frmRoot.CONFIRMPASSWORD.value.length==0 )
	 {
		alert("Please enter confirm password!");
		frmRoot.CONFIRMPASSWORD.focus();
		return false;
  }
  
  if(frmRoot.CONFIRMPASSWORD.value!=frmRoot.PASSWORD.value)
	 {
		alert("confirmation password doesn't  match with the password");
		frmRoot.CONFIRMPASSWORD.focus();
		return false;
     }
  if(frmRoot.CUSTOMERNAME.value=="" || frmRoot.CUSTOMERNAME.value.length==0 )
	 {  
		alert("Please enter customer name!");
		frmRoot.CUSTOMERNAME.focus();
		return false;
  }
  
  if(frmRoot.UNITNO.value=="" || frmRoot.UNITNO.value.length==0 )
	 {  
		alert("Please enter unitno!");
		frmRoot.UNITNO.focus();
		return false;
   }
  if(frmRoot.BUILDING.value=="" || frmRoot.BUILDING.value.length==0 )
	 {  
		alert("Please enter building!");
		frmRoot.BUILDING.focus();
		return false;
    }
  if(frmRoot.STREET.value=="" || frmRoot.STREET.value.length==0 )
	 {  
		alert("Please enter street!");
		frmRoot.STREET.focus();
		return false;
    }
  if(frmRoot.POSTALCODE.value=="" || frmRoot.POSTALCODE.value.length==0 )
	 {  
		alert("Please enter postalcode!");
		frmRoot.POSTALCODE.focus();
		return false;
    }
  if(frmRoot.MOBILE.value=="" || frmRoot.MOBILE.value.length==0 )
	 {  
		alert("Please enter mobile!");
		frmRoot.MOBILE.focus();
		return false;
    }
  if( frmRoot.MOBILE.value.length!=8 )
	 {  
		alert("Enter valid mobile number");
		frmRoot.MOBILE.focus();
		return false;
 }
  if( !IsNumeric(frmRoot.MOBILE.value))
	 {  
		alert("Enter valid mobile number");
		frmRoot.MOBILE.focus();
		return false;
}
  if( !IsNumeric(frmRoot.POSTALCODE.value))
	 {  
		alert("Enter valid postal code");
		frmRoot.POSTALCODE.focus();
		return false;
}
  if( frmRoot.POSTALCODE.value.length!=6 )
	 {  
		alert("Enter valid postal code");
		frmRoot.POSTALCODE.focus();
		return false;
}
  else
      {
	   var flag = EmailAvailability();
	  
	   if(flag){
	    frmRoot.submit();
	    return true;}
	   else{
		   return false;
	   }
	   }
}

function ValidateDate(){
	var dt=  document.mobilecustomerregister.DATEOFBIRTH.value; 
	if (isDate(dt.value)==false){
	dt.focus();
	return false;
	}
	return true;
	}

	var dtCh= "/";
	var minYear=1900;
	var maxYear=2100;
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
	this[i] = 31
	if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
	if (i==2) {this[i] = 29}
	} 
	return this
	}

	function isDate(dtStr){
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
	alert("The date format should be : dd/mm/yyyy")
	return false;
	}
	if (strMonth.length<1 || month<1 || month>12){
	alert("Please enter a valid month")
	return false;
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
	alert("Please enter a valid day")
	return false;
	}
	if (strYear.length != 4 || year==0 || yearmaxYear){
	alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
	return false;
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
	alert("Please enter a valid date")
	return false;
	}
	return true;
	} 

function EmailAvailability() {
	var email = document.mobilecustomerregister.EMAIL.value;
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	

	if(document.mobilecustomerregister.EMAIL.value=="" ||document.mobilecustomerregister.EMAIL.value.length==0 )
	{
		alert("Please enter email!");
		document.mobilecustomerregister.EMAIL.focus();
		return false;
	}
	
	if(reg.test(email)==false)
	 {
			  alert('Invalid Email Address');
			  document.mobilecustomerregister.EMAIL.focus();
		      return false;
	  
	}
	return true;
	}
function validEmail()
{
	var email = document.mobilecustomerregister.EMAIL.value;	
	var urlStr = "/track/mobilehandlingservlet";
	
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		EMAIL : email,
		Submit : "VALID_INTERNET_EMAIL"
		
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "99") {
			alert('Please Enter valid Internet Mail');
		
		} 
	}
});
}
</script>