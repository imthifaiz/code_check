<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Customer List</title>
<link rel="stylesheet" href="../css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Customer/Supplier Code</font></TH>
      <TH align="left"><font color="white">First Name</font></TH>
      <TH align="left"><font color="white">Last Name</font></TH>
    </TR>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

    CustUtil custUtils = new CustUtil();
    custUtils.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String sCustName = strUtils.fString(request.getParameter("custName"));
    String orderType = strUtils.fString(request.getParameter("orderType"));
    session=request.getSession();
    String plant = (String)session.getAttribute("PLANT");
    String sBGColor = "";
    ArrayList arrCust= null;
    if(orderType.equalsIgnoreCase("OUTBOUND")){
    	 arrCust = custUtils.getOutGoingCustomerDetails(plant,sCustName," AND ISACTIVE='Y' ");
    }else if(orderType.equalsIgnoreCase("INBOUND")){
    	 arrCust = custUtils.getVendorListStartsWithName(sCustName,plant," AND ISACTIVE='Y' ORDER BY VNAME asc");
    }
   
   try{
	  
	   if(orderType.equalsIgnoreCase("OUTBOUND")){
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        
        ArrayList arrCustLine = (ArrayList)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get(0);
        String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get(1));
        String sCustNameLast    = strUtils.removeQuotes((String)arrCustLine.get(2));
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.custName.value='<%=strUtils.insertEscp(sCustName1)%>';
      window.close();"><%=sCustCode%></td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(sCustName1)%></td>
       <td class="main2"><%=sCustNameLast%></td>
   </TR>
<%
}
 }
	   if(orderType.equalsIgnoreCase("INBOUND")){
		   for(int i =0; i<arrCust.size(); i++) {
		        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
		        Map arrCustLine = (Map)arrCust.get(i);
		        String sCustCode     = (String)arrCustLine.get("VENDNO");
		        String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
		        String sContactName     = strUtils.replaceCharacters2Send((String)arrCustLine.get("NAME"));
%>
		        <TR bgcolor="<%=sBGColor%>">
		          <td class="main2"><a href="#" onClick="window.opener.form.custName.value='<%=strUtils.insertEscp(sCustName1)%>';
		          window.close();"><%=sCustCode%></td>
		          <td class="main2"><%=strUtils.replaceCharacters2Recv(sCustName1)%></td>
		           <td class="main2"><%=sContactName%></td>
		       </TR>
		    <%
	   }
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





