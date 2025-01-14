
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.track.util.StrUtils"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="java.util.Vector"%>
<%@page import="com.track.tables.CATALOGMST"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mobile Event Registration Login</title>
</head>
<SCRIPT src="js/roll.js" language="JavaScript" type="text/javascript"></SCRIPT>
<SCRIPT src="js/flash.js" type=text/javascript></SCRIPT>
<script type="text/javascript">


</script>
<link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<%
String res="",productid="",qty="",plant="";
res =  StrUtils.fString(request.getParameter("result"));


Vector<CATALOGMST> scanlist = new Vector<CATALOGMST>();
try{
	plant=(String)session.getAttribute("PLANT");
	
   }catch(Exception e){
	e.printStackTrace();
}

%>
<body>
<form name="form" method="post" action="/track/login?action=mobileEventLogin" >
<input type="hidden" name="PLANT"  value="<%=plant%>">
<!-- Remove Logo Image -->
<table  cellpadding="0" cellspacing="0" align="center" width="100%">
 <tr>
      <td width="left"></td>
      <td align="right" ></td>
   </tr>
<tr><td colspan="2">&nbsp;</td></tr>
</table>
<table align = "center" border="0" width="100%" height="10%" cellpadding="0"  bordercolor="cyan">

<TR>
		<TH BGCOLOR="#669900" COLSPAN="11" height="40px"><font color="white" size="7">Login </font></TH>
	</TR>
	<tr>
<td colspan="2">&nbsp;</td></tr>
	<tr><td colspan="11" align="center"><B><font size="5" >
<%=res%></font></B></td></tr>
<tr>
<td colspan="2">&nbsp;</td></tr><tr>
<td colspan="2">&nbsp;</td></tr><tr>
<td colspan="2">&nbsp;</td>
</tr>
     <tr><td align="right"  class="mobilelabel" width="50%" ><font  size="7" color="#000000"><b>Mobile Number &nbsp:&nbsp</b></center></font>
          </td>
          <td width="50%" align="left">

   
           <input name="MOBILENO" type="text" value="" size="20" class="inactivemobile1" maxlength="100" height="50px">

          </td>
        </tr>
        <td colspan="2">&nbsp;</td>
        <tr>
          <td align="right"  class="mobilelabel" width="50%" >
<font  size="7" color="#000000" ><b>Password &nbsp:&nbsp</b></font>
          </td>
          <td width="50%" align="left">

           <input name="pwd"  type="password"  class="inactivemobile1"  value="" size="20" maxlength="10" >
     </td>
        </tr>
<tr height="35px"><td colspan="2" >&nbsp;</td></tr>
<tr>          
          <td colspan="2" align="center" >
          <INPUT type="button"  value="Login"   onClick="javascript:form.submit();"  class="mobileButton" />
           
     </td>
        </tr>
         
</table>

 
</body>
</form>
</html>