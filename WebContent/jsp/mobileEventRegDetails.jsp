<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>


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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="",result="";
	String DELIVERYDATE="",TIMESLOTS="";
	
    //session= request.getSession();
        String plant=(String)session.getAttribute("PLANT");
     
        Cookie ck = new Cookie("UNITNO",request.getParameter("UNITNO"));
        
        response.addCookie(ck);       
        
        
	EMAIL= StrUtils.fString(request.getParameter("EMAIL"));
	CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
	UNITNO= StrUtils.fString(request.getParameter("UNITNO"));
	BUILDING= StrUtils.fString(request.getParameter("BUILDING"));
	STREET= StrUtils.fString(request.getParameter("STREET"));
	POSTALCODE= StrUtils.fString(request.getParameter("POSTALCODE"));
	LANDMARK= StrUtils.fString(request.getParameter("LANDMARK"));
	MOBILE= StrUtils.fString(request.getParameter("MOBILE"));
	DELIVERYDATE= StrUtils.fString(request.getParameter("DELIVERYDATE"));
	TIMESLOTS= StrUtils.fString(request.getParameter("TIMESLOTS"));
	
	
%>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Event Registration Details</title>
</head>
<form  class="form" name="mobileeventdetails" method="post"  action="">
	<table>
	<!-- Removed Logo Image -->
 <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"></td>
   </tr>
</table> 
	<TABLE  style="background-color:white"  border="0" width="100%"  cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
	
	<TR style="height:30px;">
		<TH BGCOLOR="#669900" COLSPAN="11"><FONT color="#ffffff" size=7>Leave Your Contact Details</FONT>&nbsp;</TH>
	</TR>
	<TR height="5px"></TR>
    <tr><td colspan="11" align="center"><B><font size="5" >
<%=result%></font></B></td></tr>
     <TR align="center">	
	   	<TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Customer Name <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
	
		<INPUT name="CUSTOMERNAME" type="TEXT"  class="inactivemobile1
			value="<%=CUSTOMERNAME%>" size="20" MAXLENGTH=40 >
		</TD>
    </TR>
    <TR height="5px"></TR>
	
	<TR align="center">	
	    
	<TD align="right"  class="mobilelabel" width="45%" height="10%" style="padding:5px;"><font size=7 >Email Address <font color=red size =7 >*</font>&nbsp;:</font></td>
	 
		<TD align="left"  height="0">
		
		<INPUT name="EMAIL" type="TEXT" class="inactivemobile1
			value="<%=EMAIL%>" size="20" MAXLENGTH=40 
			
		 ></TD>
	</TR>
     <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Mobile Number <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		
		<INPUT name="MOBILE" type="TEXT"  class="inactivemobile1" value="<%=MOBILE%>" size="20" MAXLENGTH=20  >
		</TD>
		
	</TR>
	
	<TR align="right">	
	   	<TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Contact Address :</font><font >&nbsp;&nbsp; </font></td>
		<TD   ></TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	
	 <TR align="center">	
	   	<TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Unit No <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		
		<INPUT name="UNITNO" type="TEXT"  class="inactivemobile1
			value="<%=UNITNO%>" size="20" MAXLENGTH=20  >
		</TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Building <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		
		<INPUT name="BUILDING" type="TEXT"  class="inactivemobile1
			value="<%=BUILDING%>" size="20" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="center">	
	    <TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Street <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		
		<INPUT name="STREET" type="TEXT"  class="inactivemobile1
			value="<%=STREET%>" size="20" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	<TR align="center">	
	    <TD  ></TD>
		<TD align="left" >
		
		<INPUT name="LANDMARK" type="TEXT"  class="inactivemobile1
			value="<%=LANDMARK%>" size="20" MAXLENGTH=20  >
		</TD>
    </TR>
    
    	<TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel"  style="padding:5px;"><font size=7 >Postal Code <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		
		<INPUT name="POSTALCODE" type="TEXT"  class="inactivemobile1" value="<%=POSTALCODE%>" size="20" MAXLENGTH=20  >
		</TD>
    </TR>
    
    <!-- <TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Date  :</font><font color=white>&nbsp;*</font></td>
		<TD align="left" width="45%">
		
		<INPUT type="TEXT" size="18" MAXLENGTH="20" name="DELIVERYDATE"
			value="" class="inactivemobile1" readonly/> 
			<a	href="javascript:show_calendar('mobileeventdetails.DELIVERYDATE');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> 
				<img   src="images\show-calendar.gif" width="24" height="22" border="0" />
			</a>
		</TD>
    </TR>
    
    
    
    <TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Time  :</font><font color=white>&nbsp;*</font></td>
		<TD align="left" width="45%">
		
		<INPUT type="TEXT" size="18" class="inactivemobile1"
		MAXLENGTH="20" name="TIMESLOTS" value="" class="inactiveGry" readonly/>
			<a href="#" onClick="javascript:popUpWin('TimeSlotNonMemberRegList.jsp?TIMESLOTS='+mobileeventdetails.TIMESLOTS.value);"><img src="images/populate.gif" border="0">
			</a>
		</TD>
		
    </TR> -->
		

<input type="hidden" NAME="ISEXIST" ID="ISEXIST" value="">	     
	<TR height="45px"><td>&nbsp;</td></TR>
	 <TR align="center">
	 
	  <TD colspan="2" align="center"> 
	  	
	  	
	   <INPUT type="button"  value="Submit" 	onClick="if(validate(document.mobileeventdetails)) {submitForm();}"  class="mobileButton1" />
	  	 	
			
	   </TD>
	</TR>
 </TABLE>

</form>

</html>
<script>


function validate(mobileenquirydetails)
{
  var frmRoot=document.mobileeventdetails;
    
  
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
	    return true; }
      else
      {
          return false;
      }
	   }
}
function CheckMobileNumber() {
	var mobile = document.mobileeventdetails.MOBILE.value;	
	
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
					if (data.status == "101") {
						alert("Session cookies are Disabled/Expired,Please enable cookies to proceed");
                                         
					}
					if (data.status == "99") {
						alert("Already existing member, Please Login!");
						document.getElementById("ISEXIST").value="Y";
						document.mobileeventdetails.MOBILE.focus();
						return false;
					}
					if (data.status == "100") {
						document.getElementById("ISEXIST").value="N";
					}
				}
			});
		}
function submitForm(){
	CheckMobileNumber(); 
	var isExist = document.getElementById("ISEXIST").value;
	
	if(isExist=='N'){
	document.mobileeventdetails.action="mobileEventRegConfirm.jsp";
	
    document.mobileeventdetails.submit();}
	else
	{
		return false;
	}
    }
function EmailAvailability() {
	var email = document.mobileeventdetails.EMAIL.value;
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	
	if(document.mobileeventdetails.EMAIL.value=="" ||document.mobileeventdetails.EMAIL.value.length==0 )
	{
		alert("Please enter email!");
		document.mobileeventdetails.EMAIL.focus();
		return false;
	}	
	if(reg.test(email)==false)
	 {
			  alert('Invalid Email Address');
			  document.mobileeventdetails.EMAIL.focus();
		      return false;
	  
	}
	return true;
	}


</script>