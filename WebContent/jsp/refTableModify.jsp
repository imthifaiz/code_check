<%@ include file="header.jsp" %>
<title>Description of Reference Table</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="sr" class="com.track.gates.searchBean"  />
<jsp:useBean id="df" class="com.track.gates.defaultsBean"  />
<%
    String field = request.getParameter("FIELD_NAME");

    String fieldDesc="<tr><td> Please Choose options from the list box shown above</td></tr>";
    if(field.length()>0)
    {
    fieldDesc = sr.getRefTableFieldToEdit(field);
    if(fieldDesc.length()<1) fieldDesc = "<tr><td>No Records Available</td></tr>";
    }
    if (field.length()<1) field = "No Value Chosen";
%>
<form action="refTableModify.jsp" method="post">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REFERENCE TABLE</font>
  </table>
  <br>
  <center>
    <table>
      <tr>
        <td>
          <select name="FIELD_NAME" size="1">
            <option selected value=""> <-- Choose --> </option>
            <%=df.getRefTableFields()%>
          </select>
        </td>
        <td>
          <input type="submit" name="Go" value="Go">
        </td>
      </tr>
    </table>
    <br>
    <TABLE border="0" CELLSPACING="1" WIDTH="100%">
      <tr bgcolor="navy">
        <th width="15%"><font color="#fffff">Display Order
        <th width="20%"><font color="#fffff">Value
        <th width="65%"><font color="#fffff">Description
    </TABLE>
    <font face="Times New Roman">
    <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>
    </font>
    <br>
    <br>
    <center>
      <input type="button" value="Cancel" onClick="window.location.href='indexPage.jsp'">
    </center>
  </center>
</form>
<%@ include file="footer.jsp" %>
