<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="com.track.util.StrUtils"%>
<%@page import="com.track.constants.IDBConstants"%><html>

<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="",result="",REMARKS="";
	String DELIVERYDATE="",TIMESLOTS="";
	
    //session= request.getSession();
    String plant=(String)session.getAttribute("PLANT");
    
	EMAIL= StrUtils.fString(request.getParameter("EMAIL"));
	
	CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
	MOBILE= StrUtils.fString(request.getParameter("MOBILE"));
	GENDER = StrUtils.fString(request.getParameter("GENDER"));
	UNITNO= StrUtils.fString(request.getParameter("UNITNO"));
	BUILDING= StrUtils.fString(request.getParameter("BUILDING"));
	STREET= StrUtils.fString(request.getParameter("STREET"));
	POSTALCODE= StrUtils.fString(request.getParameter("POSTALCODE"));
	LANDMARK= StrUtils.fString(request.getParameter("LANDMARK"));
	PAYMENTMODE = StrUtils.fString(request.getParameter("PAYMENTMODE"));
	DELIVERYDATE= StrUtils.fString(request.getParameter("DELIVERYDATE"));
	TIMESLOTS= StrUtils.fString(request.getParameter("TIMESLOTS"));
	session.setAttribute("DELIVERYDATE", DELIVERYDATE);
	session.setAttribute("TIMESLOTS", TIMESLOTS);
	
	HashMap detailmap = new HashMap();
	detailmap.put(IDBConstants.EMAIL,EMAIL);
	detailmap.put("CUSTOMERNAME",CUSTOMERNAME);
	detailmap.put("MOBILE",MOBILE);
	detailmap.put("UNITNO",UNITNO);
	detailmap.put("BUILDING",BUILDING);
	detailmap.put("POSTALCODE",POSTALCODE);
	detailmap.put("STREET",STREET);
	detailmap.put("GENDER",GENDER);
	detailmap.put("PAYMENTMODE",PAYMENTMODE);
	if(LANDMARK.length()>0)
	detailmap.put("LANDMARK",LANDMARK);
	else
	detailmap.put("LANDMARK","");	
	
	if(DELIVERYDATE.length()>0)
	 detailmap.put("DELIVERYDATE",DELIVERYDATE);
	else
		detailmap.put("DELIVERYDATE","");	
		
	if(TIMESLOTS.length()>0)
	  detailmap.put("TIMESLOTS",TIMESLOTS);
 	 else
	 detailmap.put("TIMESLOTS","");	
	session.setAttribute("SHOPDETMAP",detailmap);
	
	if(request.getParameter("result")!=null)
		result = request.getParameter("result");
	
	
%>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Mobile Details Confirm</title>
</head>
<form  class="form" name="mobileaddressconfirm" method="post"  action="">
	<br>
<input type="hidden" name="LOGIN_USER" value="<%=MOBILE%>"> 
<table>
<!-- Removed logo Image -->
 <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"></td>
   </tr>
</table> 
	<TABLE  style="background-color:white"  border="0" width="100%"  cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
	
	<TR style="height:30px;">
		<TH BGCOLOR="#669900" COLSPAN="11"><FONT color="#ffffff" size=7>Confirmation </FONT>&nbsp;</TH>
	</TR>
	<TR height="5px"></TR>
    <tr><td colspan="11" align="center"></td></tr>
     <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Customer Name :</font></td>
		<TD align="left" width="45%">
					<font size=7><%=CUSTOMERNAME%> </font>
		</TD>
    </TR>
    <TR height="5px"></TR>
	
	<TR align="center">	
	    
	<TD align="right"  class="mobilelabel" width="45%" height="10%" style="padding:5px;"><font size=7 >Email Address :</font></td>
	 
		<TD align="left" width="45%" height="0">
		<font size=7 > <%=EMAIL%> </font>
		</TD>
	</TR>
     <TR height="5px"></TR>
	<TR align="right">	
	   	<TD align="right"  class="mobilelabel" width="40%" style="padding:5px;"><font size=7 >Mobile Number :</font><font >&nbsp;&nbsp; </font></td>
		<TD  width="45%" align="left"><a href="#"><font size=7><%=MOBILE%></font></a></TD>
	 </TR>
	   <TR height="5px"></TR>
	<TR align="right">	
	   	<TD align="right"  class="mobilelabel" width="40%" style="padding:5px;"><font size=7 >Address :</font><font >&nbsp;&nbsp; </font></td>
		<TD  width="45%" ></TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	
	 <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 ></font></td>
		<TD align="left" width="45%">
	<font size=7 >	<%=UNITNO%> </font>
		</TD>
	 </TR>
	 
	 <TR height="5px"></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 ></font></td>
	
	<TD align="left" width="45%"><font size=7 >	<%=BUILDING%> </font>
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 ></font></td>
		<TD align="left" width="45%">
	<font size=7 >	<%=STREET%> </font>
		</TD>
	</TR>
	
	<TR height="5px"></TR>
	<TR align="center">	
	    <TD  width="45%"></TD>
		<TD align="left" width="45%">
		<font size=7 ><%=LANDMARK%></font>
		</TD>
    </TR>

	<TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Postal Code :</font></td>
		<TD align="left" width="45%">
		<font size=7 ><%=POSTALCODE%></font>
		</TD>
    </TR>
    <TR height="5px"></TR>
	<TR align="center">	
	<TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Remarks :</font></td>
	    
		<TD align="left" width="45%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="REMARKS" type="TEXT"  class="inactivemobile
			value="<%=REMARKS%>" size="20" MAXLENGTH=100  >
		</TD>
    </TR> 
    
    <TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Date :</font></td>
		<TD align="left" width="45%">
		<font size=7 ><%=DELIVERYDATE%></font>
		</TD>
    </TR>
    
    	<TR height="5px"></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="45%" style="padding:5px;"><font size=7 >Delivery Time :</font></td>
		<TD align="left" width="45%">
		<font size=7 ><%=TIMESLOTS%></font>
		</TD>
    </TR>
	 
	<TR height="45px"></TR>
	 <TR align="center">
	  <TD width="40%" align="right">
	  <input type="button" name="Back" id="Back" class="mobileButton"  value="Back" onclick="javaScript:history.go(-1);">
	  </TD>
	  <TD width="60%" align="left"> 
	  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  		  	&nbsp;&nbsp;&nbsp;&nbsp;
	   <INPUT type="button"  value="Confirm" 	onClick="submitForm();"  class="mobileButton1" />
	  	 	
	   </TD>
	</TR>
 </TABLE>

</form>
<script type="text/javascript">
function submitForm(){
	
	//document.mobileaddressconfirm.action="/track/CatalogServlet?Submit=CHECKOUT";
	document.mobileaddressconfirm.action="/track/mobilecustomerregisterservlet?action=ADDNON_MEMBER";
	
    document.mobileaddressconfirm.submit();
}
</script>
</html>
