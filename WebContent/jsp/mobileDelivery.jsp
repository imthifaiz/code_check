<%@page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.db.util.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Calendar"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
	<script language="JavaScript" type="text/javascript"  	src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<script>
	var subWin = null;
	function popUpWin(URL) {
 		subWin = window.open(URL, 'mobileDelivery', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.track.dao.CustMstDAO"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.track.constants.IDBConstants"%><html>

<head>
<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="",CNAME="";
	String DELIVERYDATE="",TIMESLOTS="";
	CustMstDAO _custdao = new CustMstDAO();
    
    String plant=(String)session.getAttribute("PLANT");
  String  loginuser = (String)session.getAttribute("LOGIN_USER");
  TimeSlotUtil _TimeSlotUtil = new TimeSlotUtil();
  List timeslotlist =_TimeSlotUtil.getTimeSlots(plant," ");
  try{
    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,plant);
    ht.put(IDBConstants.USER_ID,loginuser);
    List custlist  = _custdao.selectCustMst("CUSTNO,USER_ID,isnull(CNAME,'') CNAME,ADDR1,ADDR2,ADDR3,ADDR4,HPNO,ZIP,GENDER,EMAIL", ht, "");
    for (int i = 0; i < custlist.size(); i++) {
		Map linemap = (Map)custlist.get(0);
		
		UNITNO = (String)linemap.get("ADDR1");
		BUILDING = (String)linemap.get("ADDR2");
		STREET = (String)linemap.get("ADDR3");
		LANDMARK = (String)linemap.get("ADDR4");
		POSTALCODE = (String)linemap.get("ZIP");
		CUSTOMERNAME = (String)linemap.get("CNAME");
		GENDER = (String)linemap.get("GENDER");
		EMAIL = (String)linemap.get("EMAIL");
		MOBILE = (String)linemap.get("HPNO");
	}	}
  catch(Exception e){
	  throw e;
  }
		
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Delivery Address</title>
</head>
<!-- <body style="background-color:#F0F0F0" > -->

<form  class="form" name="frmdeliveryaddr" method="post"  action="">
 <INPUT type="hidden" name="CUSTOMERNAME" value="<%=CUSTOMERNAME%>">	
 <INPUT type="hidden" name="EMAIL" value="<%=EMAIL%>">		
  <input type="hidden" name="PLANT" value="<%=plant%>" >
  <input type="hidden" name="MOBILE" value="<%=MOBILE%>" >
  <!-- Removed logo Image -->
	<TABLE    border="0" width="100%"  cellspacing="0" cellpadding="0" align="center" >
	 <tr>
      <td width="left"></td>
     </tr>
     <TR >
		<TH BGCOLOR="#669900" colspan="11"><FONT color="#ffffff" size=7>Delivery Address</FONT>&nbsp;</TH>
	</TR>
	
     </table>
 <TABLE    border="0" width="80%"  cellspacing="0" cellpadding="0" align="center" bgcolor="#ffffff">    
	<TR height="35px"><td colspan="2">&nbsp;</td></TR>
	
	 <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="35%"  ><font size=7 >Unit No <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="UNITNO" type="TEXT"  class="inactivemobile1"
			value="<%=UNITNO%>" size="15" MAXLENGTH=20  >
		</TD>
	 </TR>
	 
	 <TR height="15px"><td colspan="2">&nbsp;</td></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="35%" ><font size=7 >Building <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="BUILDING" type="TEXT" width="55%"  class="inactivemobile1"
			value="<%=BUILDING%>" size="15" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="35%" ><font size=7 >Street <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%" >
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="STREET" type="TEXT"  class="inactivemobile1"
			value="<%=STREET%>" size="15" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="center">	
	    <TD  ></TD>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="LANDMARK" type="TEXT"  class="inactivemobile1"
			value="<%=LANDMARK%>" size="15" MAXLENGTH=20  >
		</TD>
    </TR>

		
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="35%" ><font size=7 >Postal Code <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="POSTALCODE" type="TEXT"  class="inactivemobile1"
			value="<%=POSTALCODE%>" size="15" MAXLENGTH=20  >
		</TD>
    </TR>
    
    <TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="left">	
	    <TD align="right"  class="mobilelabel" width="37%" ><font size=7 >Delivery Date  :</font></td>
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
        
    <TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="left">	
	    <TD align="right"  class="mobilelabel" width="37%" ><font size=7 >Delivery Time  :</font></td>
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<SELECT style="width:360px; height:38px; FONT-SIZE: 23px""  NAME="TIMESLOTS" size="1">
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
    
      <TR height="35px"><td colspan="2">&nbsp;</td></TR>
    
 	 <TR height="35px"><td colspan="2">&nbsp;</td></TR>
	 <TR >
	
	  <TD  align="center" colspan=2> 
	  	
	   <INPUT type="button"  value="Submit"  onClick="if(validate(document.frmdeliveryaddr)) {submitForm();}"  class="mobileButton" />
	  	 	
	   </TD>
	</TR>
 </TABLE>

</form>

</html>
<script>
function submitForm(){
		
	       //document.frmdeliveryaddr.action  ="/track/CatalogServlet?Submit=CHECKOUT";
  var day = document.frmdeliveryaddr.DAY.value;
  var month = document.frmdeliveryaddr.MONTH.value;
  var year = document.frmdeliveryaddr.YEAR.value;
  day = parseInt(day);
  month = parseInt(month);
  year = parseInt(year);
  var deliverydate = "";
  if(day>0 || month>0 || year>0)
	  deliverydate=day+"/"+month+"/"+year;
	        document.frmdeliveryaddr.action  ="MobileShoppingConfirm.jsp?DELIVERYDATE="+deliverydate;
		   document.frmdeliveryaddr.submit();
}
function editForm(){

	document.frmdeliveryaddr.action  ="/track/mobilecustomerregisterservlet?action=Edit_del_det";
	
	    document.frmdeliveryaddr.submit();
}
function validate(frmdeliveryaddr)
{
  var frmRoot=document.frmdeliveryaddr;
  var day = document.frmdeliveryaddr.DAY.value;
  var month = document.frmdeliveryaddr.MONTH.value;
  var year = document.frmdeliveryaddr.YEAR.value;
  day = parseInt(day);
  month = parseInt(month);
  year = parseInt(year);

  if(frmRoot.UNITNO.value=="" || frmRoot.UNITNO.value.length==0 )
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
  
  else if(day>0 || month>0 || year>0)
  {
	  //var dt=frmRoot.DELIVERYDATE.value;
	  if (validDate()==false){
		
		    //document.frmdeliveryaddr.DAY.focus();
			return false;
		}
	  else
		{
		  submitForm();  	  
		}
  } 
 
 else
      {
	 submitForm();
	  
	   }
}


/**
 * DHTML date validation script for dd/mm/yyyy. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/datevalidation.asp)
 */
// Declaring valid date character, minimum year and maximum year


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
   return this;
}
function validDate()
{
	var strDay=document.frmdeliveryaddr.DAY.value;
	var strMonth=document.frmdeliveryaddr.MONTH.value;
	var strYear=document.frmdeliveryaddr.YEAR.value;
	var daysInMonth = DaysArray(12);
	var month=parseInt(strMonth);
	var day=parseInt(strDay);
	var year=parseInt(strYear);
	if (month==0 || month<1 || month>12){
		alert("Please choose a valid month");
		document.frmdeliveryaddr.MONTH.focus();
		return false;
	}
	if (day==0 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please choose a valid day");
		document.frmdeliveryaddr.DAY.focus();
		return false;
	}
	if (strYear == 0 ){
		alert("Please choose year");
		document.frmdeliveryaddr.YEAR.focus();
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
	var dtStr=  document.frmdeliveryaddr.DELIVERYDATE.value;
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

    
	

</script>