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
   </TR>
<%
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
	(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

    InvMstDAO  _InvMstDAO  = new InvMstDAO(); 
    _InvMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String userId = (String)session.getAttribute("LOGIN_USER");
    String INDEX = strUtils.fString(request.getParameter("INDEX"));
    String FROM_LOC =strUtils.fString(request.getParameter("LOC"));
    String sBGColor = "";
       
   	try{
         List listQry =  _InvMstDAO.getQCReverseLocByWMS(PLANT,FROM_LOC,userId);
        for(int i =0; i<listQry.size(); i++) {
   	    	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     		Map m=(Map)listQry.get(i);
       	    String sFromLoc=(String)m.get("FromLoc");
                  
      %>
	  <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick="window.opener.form.LOC_<%=INDEX%>.value='<%=sFromLoc%>';window.close();"><%=sFromLoc%>
       </a>
      </td>
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