<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Customer List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Customer Code</font></TH>
      <TH align="left"><font color="white">First Name</font></TH>
      <TH align="left"><font color="white">Last Name</font></TH>
    </TR>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

    CustUtil custUtils = new CustUtil();
    custUtils.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
    session=request.getSession();
    String plant = (String)session.getAttribute("PLANT");
    String sBGColor = "";
   try{
     ArrayList arrCust = custUtils.getOutGoingCustomerDetails(plant,sCustName," AND ISACTIVE='Y' ");
    
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrCustLine = (ArrayList)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get(0);
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get(1));
        String sCustNameLast    = strUtils.removeQuotes((String)arrCustLine.get(2));
        
        String sContactName     = strUtils.removeQuotes((String)arrCustLine.get(3));
        String sTelNo     = strUtils.removeQuotes((String)arrCustLine.get(4));
        String sEmail     = strUtils.removeQuotes((String)arrCustLine.get(5));
        String sAdd1     = strUtils.removeQuotes((String)arrCustLine.get(6));
        String sAdd2     = strUtils.removeQuotes((String)arrCustLine.get(7));
       
        String sAdd3     = strUtils.removeQuotes((String)arrCustLine.get(8));
        String sAdd4     = strUtils.removeQuotes((String)arrCustLine.get(9));
        String sCountry    = strUtils.removeQuotes((String)arrCustLine.get(10));
        String sRemarks     = strUtils.removeQuotes((String)arrCustLine.get(11));
        String sZip     = strUtils.removeQuotes((String)arrCustLine.get(12));
         String sHpNo     = strUtils.removeQuotes((String)arrCustLine.get(13));

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.CUST_CODE.disabled = true;
      window.opener.form.CUST_CODE.value='<%=sCustCode%>';
      window.opener.form.CUST_CODE1.value='<%=sCustCode%>';
      window.opener.form.CUST_NAME.value='<%=sCustName1%>';
      
      window.opener.form.PERSON_INCHARGE.value='<%=sContactName%>';
      window.opener.form.TELNO.value='<%=sHpNo%>';
      window.opener.form.EMAIL.value='<%=sEmail%>';
      window.opener.form.ADD1.value='<%=sAdd1%>';
      window.opener.form.ADD2.value='<%=sAdd2%>';
      window.opener.form.ADD3.value='<%=sAdd3%>';
      window.opener.form.REMARK2.value='<%=sRemarks%>';
      window.opener.form.ADD4.value='<%=sAdd4%>';
      window.opener.form.COUNTRY.value='<%=sCountry%>';
      window.opener.form.ZIP.value='<%=sZip%>';
         
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





