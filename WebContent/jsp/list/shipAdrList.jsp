<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title> Shipping Address List</title>
<link rel="stylesheet" href="../css/style.css">

</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white"> ID</font></TH>
      <TH align="left"><font color="white"> Name</font></TH>
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
    String plant= (String)session.getAttribute("PLANT");
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
    String sOrdtYpe = strUtils.fString(request.getParameter("ORDTYPE"));
    String sBGColor = "";
    ArrayList arrCust =null;
    String sCustCode="",sCustName1="",sContactName="",sTelNo="",sEmail="",sAdd1="",sAdd2="",sAdd3="",sAdd4="",sCity="",sCountry="",sZip="";
    
   try{
	  if(sOrdtYpe.equalsIgnoreCase("Inbound")){
    		arrCust = custUtils.getVendorListStartsWithName(sCustName,plant," AND ISACTIVE='Y' ORDER BY VNAME asc");
	  }else if(sOrdtYpe.equalsIgnoreCase("Outbound")){
		   arrCust = custUtils.getCustomerListStartsWithName(sCustName,plant);
	   }else if(sOrdtYpe.equalsIgnoreCase("Transfer")){
		   arrCust = custUtils.getToAssingeListStartsWithName(sCustName,plant);
	   }


    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
      
        if(sOrdtYpe.equalsIgnoreCase("Inbound")){
         sCustCode     = (String)arrCustLine.get("VENDNO");
         sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
        }else if(sOrdtYpe.equalsIgnoreCase("Outbound")){
            sCustCode     = (String)arrCustLine.get("CUSTNO");
            sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("CNAME"));
           }
        else if(sOrdtYpe.equalsIgnoreCase("Transfer")){
            sCustCode     = (String)arrCustLine.get("ASSIGNENO");
            sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("ASSIGNENAME"));
           }
         sContactName     = strUtils.replaceCharacters2Send((String)arrCustLine.get("NAME"));
         sTelNo     = strUtils.removeQuotes((String)arrCustLine.get("TELNO"));
        // sEmail     = strUtils.removeQuotes((String)arrCustLine.get("EMAIL"));
         sAdd1     = strUtils.removeQuotes((String)arrCustLine.get("ADDR1")) ;
         sAdd2     = strUtils.removeQuotes((String)arrCustLine.get("ADDR2"))+" "+strUtils.removeQuotes((String)arrCustLine.get("ADDR3"));
         sCity    = strUtils.replaceCharacters2Send((String)arrCustLine.get("ADDR4"));
         sCountry    = strUtils.replaceCharacters2Send((String)arrCustLine.get("COUNTRY"));
         sZip     = strUtils.removeQuotes((String)arrCustLine.get("ZIP"));
        
        


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.CUST_NAME.value='<%=strUtils.insertEscp(sCustName1)%>';
        window.opener.form.CONTACTNAME.value='<%=strUtils.insertEscp(sContactName)%>';
        window.opener.form.TELNO.value='<%=sTelNo%>';
        window.opener.form.ADDR1.value='<%=sAdd1%>';
        window.opener.form.ADDR2.value='<%=sAdd2%>';
        window.opener.form.CITY.value='<%=sCity%>';
        window.opener.form.COUNTRY.value='<%=strUtils.insertEscp(sCountry)%>';
        window.opener.form.ZIP.value='<%=sZip%>';
        window.close();"><%=sCustCode%></td>
        <td class="main2"><%=strUtils.replaceCharacters2Recv(sCustName1)%></td>
    
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





