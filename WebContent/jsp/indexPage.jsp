<%@ include file="header.jsp" %>
<link rel="stylesheet" href="css/style.css">
<title>SUNPRO</title>
<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<h3>
  <center>
     <br><br><br>
     <br>
<b>
<font size="5" color = "#000066" style="Arial Black"><span style="font-size:18.0pt;font-family:Verdana; mso-fareast-font-family:"Times New Roman";mso-bidi-font-family:Arial; color:#000066;mso-ansi-language:EN-US;mso-fareast-language:EN-US;mso-bidi-language: AR-SA">MAX+ </span> 
</font></b>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
  <tr>
    <td width="50%" align="center" ><img border="0" src="images/logon.png" width="354" height="330" ></td>
  </tr>
</table>
<br><br>
    <font face = "Verdana" size="2">Server Last Accessed on <%=gn.getDateAtTime()%>
    </font>
  </center>
</h3>
<%
//      Freeing up unused memory
    session.removeAttribute("MSG_SEARCH");
    session.removeAttribute("LOG_SEARCH");
    session.removeAttribute("RESULT");
%>
<%@ include file="footer.jsp" %>
