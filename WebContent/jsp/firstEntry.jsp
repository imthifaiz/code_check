<%@ page language="java" import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>


<%
String title = "Change Password";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/pwd.js"></script>



<%

String user_id = session.getAttribute("LOGIN_USER").toString();
String pwd     = request.getParameter("pwd");

session.setAttribute("SYSTEMNOW","");
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

%>
 
<div class="container-fluid m-t-20">
<div class="box">

    <p align="center">
    <INPUT type="Hidden" name="USER_ID" value="<%=user_id%>">
    <INPUT type="Hidden" name="PASSWORD" value="<%=pwd%>">

 <br>
    <font face=arial size=2 color=blue><b>Hello <%=user_id%>, Welcome to SUNPRO
    <br>
    <br>
    <b> As this is your first visit to SUNPRO, we recommend you to change password.
    <br>Please change password and start using the system.</b></font>
     <br><br>
	 
<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<form class="form-horizontal" name="form" method="post" action="jsp/pwdSubmitUponLogin.jsp" onSubmit="return validatePwd(this);">
<div class="form-group">
      <label class="control-label col-sm-4" for="Enter New Password">Enter New Password:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type="password" size="30" MAXLENGTH=30 name="NPASSWORD">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Confirm Password">Confirm Password:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type="password" size="30" MAXLENGTH=30 name="CPASSWORD">
      </div>
    </div>
    
    
    <div class="form-group">        
      <div class="col-sm-offset-5 col-sm-7">
      
      	<button type="Submit" class="Submit btn btn-default" value="Update" name="Submit"><b>Update</b></button>&nbsp;&nbsp;
      	

      </div>
    </div>
    
</form>
</div>
</div>
</div>
    
    
 
 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>