<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>

<html>
<head>
<title>Remarks List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Remarks</font></TH>

     
     </TR>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	 MasterUtil  _MasterUtil = new MasterUtil();
	 _MasterUtil.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String plant = (String)session.getAttribute("PLANT");
    String remarks = strUtils.fString(request.getParameter("REMAKRS"));
    session=request.getSession();
      String sBGColor = "";
   try{
	  // '"+ item	+"%'"+
	   ArrayList arrList = _MasterUtil.getRemarksList(plant, " remarks like  '"+remarks+"%'");
	   System.out.println("arraysize"+arrList.size());
	    for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			int id=iCnt+1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            Map lineArr = (Map) arrList.get(iCnt);
            String strremarks = strUtils.replaceCharacters2Send((String)lineArr.get("REMARKS"));
           if(strremarks == null || strremarks.equals("")||strremarks.equals("NOREMARKSDETAILS"))
			{
        	   strremarks="";
			}

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="
      window.opener.form.REMARKS1.value='<%=strUtils.insertEscp(strremarks)%>';
      window.close();"><%=strUtils.insertEscp(strremarks)%></td>
        
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





