<%@ include file="header.jsp" %>
<title>Authorisation</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript">
<!--
function SetChecked(val)
{
dml=document.userLevelList;
len = dml.elements.length;
var i=0;
for( i=0; i<len; i++)
 {
	if (dml.elements[i].name=='LEVEL_NAME')
		 { dml.elements[i].checked=val; }
 }
 }
-->
</script>
<%@ include file="body.jsp" %>
<%! String userLevels,chkBox; %>
<jsp:useBean id="ub" class="com.track.gates.userBean"  />
<form name="userLevelList" action="authUserLevelSubmit.jsp" method="post">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">GROUP AUTHORIZATION</font>
  </table>
  <br>

  <font face="Times New Roman" size="2">
  <%

  String plant = (String) session.getAttribute("PLANT");
  
  
    String user = (String) session.getAttribute("LOGIN_USER");
    ub.setmLogger(mLogger);
    if (user!=null)   userLevels = ub.getUserLevelsToAuthorise(user);

    if (userLevels.length()<2)
		{
		userLevels = "<tr><td> No records Available </td></tr>";
		chkBox="<br><center><input type=\"button\" value=\"Cancel\"  onClick=\"window.location.href='../home'\">";
		}
		else
		{
		chkBox = " <p><a href=\"javascript:SetChecked(1)\">Check&nbsp;All</a> - <a  href=\"javascript:SetChecked(0)\">Clear&nbsp;All</a>"+
				"<center><input type=\"Submit\" value=\"Authorise\" name=\"Action\">&nbsp;"+
                               "<input type=\"button\" value=\"Cancel\"  onClick=\"window.location.href='../home'\">&nbsp;"+
                                "<input type=\"Submit\" value=\"Reject\" name=\"Action\" onClick=\" return confirm('Are you sure to reject and delete selected user level(s) permanently ?');\">";
		}
%>
  <br>
  <TABLE border="0" CELLSPACING="1" WIDTH="100%">
    <tr bgcolor="navy">
      <th width="10%"><font color="#ffffff">S.No
      <th width="40%"><font color="#ffffff">Level Name
      <th width="25%"><font color="#ffffff">Created By
      <th width="25%"><font color="#ffffff">Created On
    </tr>
  </TABLE>
  <table border="0" cellspacing="1" cellpadding="2" width="100%" bgcolor="#eeeeee">
    <%=userLevels%>
  </table>
  <%=chkBox%> </font>
</form>

<%@ include file="footer.jsp" %>
