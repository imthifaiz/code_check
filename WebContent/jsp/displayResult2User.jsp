<%@ include file="header.jsp" %>

<%
String title = "";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">


<%
 String result = (String)session.getAttribute("RESULT");
 if (result == null ) result = "";
%>


<center>
<p>
<font face="arial" size="3"><%= result %></font>
</center>

<br>
<br>

</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="nobackblock" value="1" />
</jsp:include>

