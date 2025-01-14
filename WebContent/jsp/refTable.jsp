<%@ include file="header.jsp" %>
<title>Reference Table editor</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<form method="post" action="refTableSubmit.jsp">
  <jsp:useBean id="gn"  class="com.track.gates.Generator" />
  <jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REFERENCE TABLE</font>
  </table>
  <br>
  <table border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <table border="0" width="100%">
          <tr>
            <td width="100%">
              <table border="0" width="85%" align="center">
                <tr>
                  <td width="23%">Field Name</td>
                  <td width="29%">
                    <select name="FIELD_NAME" size="1">
                      <%=df.getRefTableFields()%>
                    </select>
                  </td>
                </tr>
                <tr>
                  <td width="23%">Field Value</td>
                  <td width="29%" align="left">
                    <input type="text" name="OPTION_CODE" size="27" maxlength="25">
                    <input type="hidden" name="OLD_CODE" size="27" maxlength="25" value="">
                  </td>
                  <td width="20%" align="right">Display Order &nbsp;</td>
                  <td width="28%" align="left">
                    <input type="text" name="SHOW_ORDER" size="5" maxlength="3">
                  </td>
                </tr>
                <tr>
                  <td width="23%">Field Description </td>
                  <td colspan="3">
                    <input type="text" name="OPTION_DESC" size="60" maxlength="50">
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <div align="center">
          <center>
            <p>
              <input type="submit" value="Save" name="submitBtn">
              &nbsp;
              <input type="reset" value="Reset" name="resetBtn">
              &nbsp;
              <input type="button" value="Cancel" name="cancelBtn" onClick="window.location.href='indexPage.jsp'">
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
