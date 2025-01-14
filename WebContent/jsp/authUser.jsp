<%@ include file="header.jsp" %>
<title>Authorisation</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript">
<!--
function SetChecked(val)
{
dml=document.userList;
len = dml.elements.length;
var i=0;
for( i=0; i<len; i++)
 {
	if (dml.elements[i].name=='USER_ID')
		 { dml.elements[i].checked=val; }
 }
 }
-->
</script>
<%@ include file="body.jsp" %>
<%! String users,chkBox; %>
<jsp:useBean id="ub" class="com.track.gates.userBean"  />
<form name="userList" action="authUserSubmit.jsp" method="post">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">USER AUTHORIZATION</font>
  </table>
  <br>

  <font face="Times New Roman" size="2">
  <%
    String login_user = (String) session.getAttribute("LOGIN_USER");
    if (login_user!=null)   users = ub.getUsersToAuthorise(login_user);

    if (users.length()<2)
    {
      users = "<tr><td> No records Available </td></tr>";
      chkBox="<br><center><input type=\"button\" value=\"Cancel\"  onClick=\"window.location.href='indexPage.jsp'\">";
    }
    else
    {
      chkBox = " <p><a href=\"javascript:SetChecked(1)\">Check&nbsp;All</a> - <a  href=\"javascript:SetChecked(0)\">Clear&nbsp;All</a>"+
        "<center><input type=\"Submit\" value=\"Authorise\" name=\"Action\">&nbsp;"+
        "<input type=\"button\" value=\"Cancel\"  onClick=\"window.location.href='indexPage.jsp'\">&nbsp;"+
        "<input type=\"Submit\" value=\"Reject\" name=\"Action\" onClick=\" return confirm('Are you sure to reject and delete selected user(s) permanently ?');\">";
    }
%>
  <br>
  <TABLE border="0" CELLSPACING="1" WIDTH="100%">
    <tr bgcolor="navy">
      <th width="7%"><font color="#ffffff">S.No
      <th width="3%">&nbsp;
      <th width="15%"><font color="#ffffff">User ID
      <th width="25%"><font color="#ffffff">User Name
      <th width="20%"><font color="#ffffff">User Level
      <th width="15%"><font color="#ffffff">Effective Date
      <th width="15%"><font color="#ffffff">Enrolled By
    </tr>
  </TABLE>
  <table border="0" cellspacing="1" cellpadding="5" width="100%"  bgcolor="#eeeeee">
    <%=users%>
  </table>
  <%=chkBox%>
</font>
</form>

<%@ include file="footer.jsp" %>
