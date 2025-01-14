<%@ include file="header.jsp" %>
<title>Displaying Result</title>
<%@ include file="body.jsp" %>
<%
 String result = request.getParameter("RESULT");
 System.out.println(result);
%>
<br>
<br>
<br>
<center>
  <p>
  <h3><b><%= result %> </b></h3>
</center>
<%@ include file="footer.jsp" %>
