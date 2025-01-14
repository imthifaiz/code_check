<%
if (session.isNew() || session.getAttribute("LOGIN_USER") == null)    //  Invalid Session
{
	session.invalidate();
    System.out.println("New Session Divert it to Index Page");
	response.sendRedirect("../login");
	return;
}
/*else
{
	String name = "HSBC";
	String user = (String) session.getAttribute("VALID_USER");
	if(!name.equalsIgnoreCase(user))  // Invalid User
	{
        System.out.println("User Name is blank Divert it to Index Page");
	    response.sendRedirect("../login");
		return;
	}
}*/
%>
