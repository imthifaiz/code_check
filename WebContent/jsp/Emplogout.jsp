<title>Thank You</title>
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<%
    session = request.getSession();
    df.insertToLog(session, "User Logout Information", "Logged out Successfully");   //  Inserting into the user log
    session.invalidate();
    Runtime.getRuntime().gc();
    response.sendRedirect("EmployeeLogin.jsp");
%>