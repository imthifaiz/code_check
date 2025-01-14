<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
String msg="";
msg = request.getParameter("MSG");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
</head>
<body>
<form >
<table align="center">
<tr><td valign="center"><center><font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<tr><td><font color="green" size="7"> &nbsp;</font></td></tr>
	<tr><td><font color="green" size="7"> &nbsp;</font></td></tr>
	<tr><td><font color="Red" size="8"> Problem in processing the Order</font></td></tr>
	<tr><td><font color="Red" size="8"><%=msg%></font></td></tr>
</table>
</td></tr>

</table>
</font></center>
</form>
</body>
</html>