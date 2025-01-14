<%@ include file="header.jsp" %>
<title>Reference Table editor</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<form method="post" action="refTableSubmit.jsp">
  <jsp:useBean id="gn"  class="com.track.gates.Generator" />
  <jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
  <jsp:useBean id="sr"  class="com.track.gates.searchBean" />
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REFERENCE TABLE</font>
  </table>
  <br>

  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <div align="center">
<%
        String field = request.getParameter("field");
        System.out.println(field);
        String code  = request.getParameter("code");
        System.out.println(code);
        ArrayList al = sr.getRefTableFieldDescToEdit(field,code);
%>
        <font face="Times New Roman" size="2">
        <table border="0" width="100%">
          <tr>
            <td width="100%">
              <table border="0" width="85%" align="center">
                <tr>
                  <td width="23%">Field Name</td>
                  <td width="29%">
                    <select name="FIELD_NAME" size="1" disabled>
                     <option value="" selected><%=field%></option>
                    </select>
                    <input type="hidden" name="FIELD_NAME" value="<%=field%>">
                  </td>
                </tr>
                <tr>
                  <td width="23%">Field Value</td>
                  <td width="29%" align="left">
                    <input type="text" name="OPTION_CODE" size="27" maxlength="25" value="<%=al.get(1).toString()%>">
                    <input type="hidden" name="OLD_CODE" value="<%=al.get(1).toString()%>">
                  </td>
                  <td width="20%" align="right">Display Order &nbsp;</td>
                  <td width="28%" align="left">
                    <input type="text" name="SHOW_ORDER" size="5" maxlength="3" value="<%=al.get(0).toString()%>">
                  </td>
                </tr>
                <tr>
                  <td width="23%">Field Description </td>
                  <td colspan="3">
                    <input type="text" name="OPTION_DESC" size="60" maxlength="50" value="<%=al.get(2).toString()%>">
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <div align="center">
          <center>
            <p>
              <input type="submit" value="Update" name="submitBtn">
              &nbsp;
              <input type="button" value="Cancel" name="cancelBtn" onClick="window.location.href='indexPage.jsp'">
              &nbsp;
              <input type="submit" value="Delete" name="submitBtn" onClick="return confirm('Are you sure to delete <%=field%> - <%=al.get(0).toString()%> permanently ?');">
            </center>
        </div>
        </font></td>
    </tr>
  </table>
  <div align="center">
    <center>
      <p>&nbsp;</p>
    </center>
  </div>
</form>
<%@ include file="footer.jsp" %>
