<!-- Not in Use -->
<html>
<head>
<title>Displaying Result</title>
<script langauge = "javaScript">
function onStop(){
	document.form1.action.value="stop";
	document.form1.submit();
}
function onStart(){
	document.form1.action.value="start";
	document.form1.submit();
}
</script>
</head>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<form name = "form1" method = "post" action ="/track/syncDO">
<BR>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Back End Synchronization</font></TH>
    </TR>
  </table>
<BR><BR>
        <center><font color ="green" size ="3" face="Arial"><b>Back End Synchronization Started successfully</b></font></center>
<br><br>
<table cellpadding="5" cellspacing="0" border="0" width=500 align="center">
<tr>
  <td align="center"><input type = "submit" value="Stop Transaction" onClick="JavaScript:onStop()"></td>
  </tr>
  <tr><td colspan="2">&nbsp;</td></tr>
  <tr><td colspan="2">&nbsp;</td></tr>
</table>
<h1>

<input type="hidden" name="action" value="">
</form>
<%@ include file="footer.jsp" %>
</center>








