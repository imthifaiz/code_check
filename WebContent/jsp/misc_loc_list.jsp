<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Loc List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Loc</font></TH>
       <TH align="left"><font color="white">Batch</font></TH>
       <TH align="left"><font color="white">Qty</font></TH>
     
    </TR>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

    InvMstDAO  _InvMstDAO  = new  InvMstDAO();  
    _InvMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
    String userId= session.getAttribute("LOGIN_USER").toString();
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));

    String sBGColor = "";
   try{
   
    List listQry =   _InvMstDAO.getMiscIssueLocByWMS(PLANT,ITEMNO,LOC,userId);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sLoc    = (String)m.get("loc");
     String sBatch    = (String)m.get("batch");
     String sQty    = (String)m.get("qty");


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.LOC.value='<%=sLoc%>';
      window.opener.form.BATCH.focus();window.opener.form.BATCH.select();
      window.close();"><%=sLoc%></a>
  <!--window.opener.form.BATCH.value='<%=sBatch%>'; window.opener.form.BATCH.select();
      window.opener.form.INVQTY.value='<%=sQty%>';-->
      </td>
       <td class="main2"><%=sBatch%></td>
       <td class="main2"><%=sQty%></td>
      
     
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