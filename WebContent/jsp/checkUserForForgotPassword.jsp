<%@ include file="header.jsp" %>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=../home">
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=../selectcompany">
<title>User Validation</title>
</head>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="misc"  class="com.track.gates.miscBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />

<body >
<form >
  <br>
  <br>
  <br>
  <center>
    <p>
    <h3><b>Validating User...</b></h3>
<%
    
    int statusCode=0;
    String company="";
    String backBtn   = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> ";
    String result    ="";
    String user_id   = request.getParameter("name").trim();
   
 %>
 <%
    PlantMstUtil _PlantMstUtil =new PlantMstUtil();
   	ArrayList arr  = new ArrayList();
   	
   	result = _PlantMstUtil.sendUserPassword(user_id);	
if(!result.equalsIgnoreCase("")){
%>
   
<jsp:forward page="login.jsp" >
<jsp:param name="warn" value="<%=result%>" />
</jsp:forward>
<%} %>
</center>
</form>

<%@ include file="footer.jsp" %>
