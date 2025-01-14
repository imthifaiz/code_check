<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Orders</title>
<link rel="stylesheet" href="../css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR BGCOLOR="#000066">
		<TH align="left"><font color="white">Order Number </font></TH>
		<TH align="left"><font color="white"><font color="white">Customer</font>Name</font></TH>
		<TH align="left"><font color="white">Status</font></TH>
	</TR>


	<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

    OrderTypeUtil orderUtil = new OrderTypeUtil();
    orderUtil.setmLogger(mLogger);
    session= request.getSession();
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String moduleType = "CUSTOMERRETURN";
     String status = StrUtils.fString(request.getParameter("STATUS"));
    String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
    String item = StrUtils.fString(request.getParameter("ITEM"));
    String index = StrUtils.fString(request.getParameter("INDEX"));
     
    String sBGColor = "";
    try{
   
     
     ArrayList listQry = orderUtil.getCustomerReturnOrderDetails(plant,moduleType,item,orderNo,status);
     for(int i =0; i<listQry.size(); i++) {
   
      sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      orderNo     = (String)m.get("orderNo");
      String custName    = (String)m.get("CustName");
      String status1      =  (String)m.get("status");
      //String batch     =  (String)m.get("batch");
      //String qty     =  (String)m.get("qty");
  %>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2"><a href="#"
			onClick="window.opener.form.ORDERNO_<%=index%>.value='<%=orderNo%>';
			
      window.close();"><%=orderNo%></a>
		</td>
		<td class="main2"><%=StrUtils.replaceCharacters2Recv(custName)%></td>
		
		<td class="main2"><%=status1%></td>
	</TR>
	<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
	<TR>
		<TH COLSPAN="8">&nbsp;</TH>
	</TR>
	<TR>
		<TH COLSPAN="8" align="center"><a href="#"
			onclick="window.close();"><input type="submit" value="Close"></a></TH>
	</TR>
</table>
</body>
</html>





