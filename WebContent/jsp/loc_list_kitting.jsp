<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.BomDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Kitting Product List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
       <TH align="left"><font color="white">Loc</font></TH>
          <TH align="left"><font color="white">Description</font></TH>
    </TR>
<%
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
				.getAttribute("PLANT"));
		loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
				(String) session.getAttribute("LOGIN_USER")).trim());
		MLogger mLogger = new MLogger();
		mLogger.setLoggerConstans(loggerDetailsHasMap);
        session = request.getSession();
        BomDAO  bomDAO  = new  BomDAO();  
        bomDAO.setmLogger(mLogger);
        StrUtils strUtils = new StrUtils();
        String PLANT= session.getAttribute("PLANT").toString();
        String ITEM = strUtils.fString(request.getParameter("ITEM"));
        String LOC = strUtils.fString(request.getParameter("LOC"));
     
         String sBGColor = "";
         try{
    	
      
	        List listQry =   bomDAO.getKittingLocByWMS(PLANT,ITEM,LOC);
	        ItemMstDAO itemMstDAO = new ItemMstDAO();
	        itemMstDAO.setmLogger(mLogger);
	        
	        for(int i =0; i<listQry.size(); i++) {
	       
	         sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
	         Map m=(Map)listQry.get(i);
	         String sLoc    = (String)m.get("loc");
	       String slocDesc    = strUtils.fString((String)m.get("locdesc"));
  		
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form1.LOC.value='<%=sLoc%>';
     
     
      window.close();"><%=sLoc%></a>
      </td>
     <td class="main2"><%=slocDesc%></td>
      
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