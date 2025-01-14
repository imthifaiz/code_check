<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>


<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />

<%! String result; %>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
String systatus = session.getAttribute("SYSTEMNOW").toString();
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
ub.setmLogger(mLogger);
    String cancel=  "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='../home'\">";
    if(systatus.equalsIgnoreCase(""))
    		{
    	 cancel=  "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='../selectapp'\">";
    		}
    String nextPage="<input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+cancel;
    String newPwd,user_id,oldPwd,passwordtype="";
	session = request.getSession();
	String plant = session.getAttribute("PLANT").toString();
    user_id  = (String)session.getAttribute("LOGIN_USER");
    newPwd   = request.getParameter("NPASSWORD");
    oldPwd   = request.getParameter("CPASSWORD");
   

    newPwd   = eb.encrypt(newPwd);
    oldPwd   = eb.encrypt(oldPwd);
	ub.setmLogger(mLogger);
    int n = ub.changePasswordUponLogin(user_id, newPwd, plant);
     
    

    if(n==1)		result = "<font class=maingreen>Password has been changed successfully</font><br><br><center>"+cancel;

    else if(n==-2)      result = "<font class=mainred>Your Old Password is not right...Try again <br><br><center>"+nextPage;

    else        	result = "<font class=mainred>Error in changing the password <br> Please try again</font><br><br><center> "+nextPage;

        df.insertToLog(session, "Changing User Password", result);   //  Inserting into the user log

        result = "<h3><b>"+result;

        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

