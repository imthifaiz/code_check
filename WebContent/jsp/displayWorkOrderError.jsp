<%@ include file="header.jsp" %>
<title>Displaying Result</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>

<%
 String result = (String)session.getAttribute("RESULTERROR");
 if (result == null ) result = "";
%>
<br><br><br>
<center>
<p>
<font face="arial"><%= result %></font>
</center>
<%@ include file="footer.jsp" %>

