<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Customer List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Customer Code</font></TH>
      <TH align="left"><font color="white">First Name</font></TH>
      <TH align="left"><font color="white">Last Name</font></TH>
    </TR>
<%
    CustUtil custUtils = new CustUtil();
    StrUtils strUtils = new StrUtils();
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
    String sBGColor = "";
   try{
    ArrayList arrCust = custUtils.getCustomerListStartsWithName(sCustName);
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrCustLine = (ArrayList)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get(0);
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get(1));
        String sCustNameLast    = strUtils.removeQuotes((String)arrCustLine.get(2));

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.CUST_CODE.disabled = true;
      window.opener.form.CUST_NAME.value='<%=sCustName1%>';
      window.opener.form.CUST_CODE.value='<%=sCustCode%>';
      window.close();"><%=sCustCode%></td>
      <td class="main2"><%=sCustName1%></td>
       <td class="main2"><%=sCustNameLast%></td>
   </TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close"></a></TH>
    </TR>
  </table>

</body>
</html>





