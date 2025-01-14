<title>Warehouse Master Search</title>
<link rel="stylesheet" href="css/style.css">
<jsp:useBean id="sr" class="com.track.gates.searchBean"  />
<jsp:useBean id="df" class="com.track.gates.defaultsBean"  />
<jsp:useBean id="vm" class="com.track.tables.VENDMST"  />


<%
    String vendname   = request.getParameter("VENDNAME");
    String popUp = request.getParameter("P");

    String fieldDesc="<tr><td> Please Choose options from the list box shown above</td></tr>";

     if(popUp.equalsIgnoreCase("Y")){
      fieldDesc = vm.popAllVENDMST(vendname);
     }else{
      fieldDesc = vm.listAllVENDMST(vendname);
     }
      if(fieldDesc.length()<1) fieldDesc = "<tr><td>No Records Available</td></tr>";
    %>

    <TABLE  CELLSPACING="1" WIDTH="100%">
      <tr  bgcolor="navy">
        <th width="30%"><font color="#ffffff">Vendor ID
        <th width="70%"><font color="#ffffff">Vendor Name
    </TABLE>
    <font face="Times New Roman">
    <table width="100%" border="0" cellspacing="1" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>
    </font>

