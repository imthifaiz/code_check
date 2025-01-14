<%
if (session.isNew() || session.getAttribute("EMP_LOGIN_USER") == null)    //  Invalid Session
{
	session.invalidate();
    System.out.println("New Session Divert it to Index Page");
	response.sendRedirect("EmployeeLogin.jsp");
	return;
}
%>