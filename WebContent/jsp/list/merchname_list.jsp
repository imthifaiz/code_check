<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>MerchantName List</title>
<SCRIPT LANGUAGE="JavaScript">

function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}

</Script>
<link rel="stylesheet" href="../css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
     
      <TH align="left"><font color="white">Merchant Name</font></TH>
     
    </TR>
<%

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

    MerchantUtil mercutil = new MerchantUtil();
    StrUtils strUtils = new StrUtils();
    session=request.getSession();
    String plant= (String)session.getAttribute("PLANT");
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));

    mercutil.setmLogger(mLogger);
    
    String sBGColor = "";
   try{
    ArrayList arrCust = mercutil.getMerchantListStartsWithName(sCustName,plant);
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrCustLine = (ArrayList)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get(0);
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get(1));
       
        String isactive    = strUtils.removeQuotes((String)arrCustLine.get(16));

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.MERCHANT_NAME.value='<%=sCustName1%>';window.close();"><%=sCustName1%></td>
     
       
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





