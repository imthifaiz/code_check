<title>Customer Master</title>
<link rel="stylesheet" href="css/style.css">
<jsp:useBean id="sr" class="com.track.gates.searchBean"  />
<jsp:useBean id="df" class="com.track.gates.defaultsBean"  />
<jsp:useBean id="cm" class="com.track.tables.CUSTMST"  />


<%
      String custname   = request.getParameter("CUSTNAME");
      String fieldDesc="<tr><td> Please Choose options from the list box shown above</td></tr>";

      fieldDesc = cm.listAllCUSTMST1(custname);
      if(fieldDesc.length()<1) fieldDesc = "<tr><td>No Records Available</td></tr>";
%>
    <TABLE BORDER="0" CELLSPACING="1" WIDTH="100%">
      <tr  bgcolor="navy">
        <th width="30%"><font color="#ffffff">Customer ID
        <th width="70%"><font color="#ffffff">Customer Name
    </TABLE>
    <table width="100%" border="0" cellspacing="1" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>



