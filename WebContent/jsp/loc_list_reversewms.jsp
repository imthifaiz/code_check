<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
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
       <TH align="left"><font color="white">Description</font></TH>
     </TR>
<%
    RecvDetDAO  _RecvDetDAO  = new  RecvDetDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String sBGColor = "";
    try{
      List listQry =   _RecvDetDAO.getRevLocByWMS(PLANT,ORDERNO,ITEMNO,LOC);
      for(int i =0; i<listQry.size(); i++) {
      	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
     	String sLoc    = (String)m.get("loc");
        String sLocDesc    = (String)m.get("locdesc");

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.LOC.value='<%=sLoc%>';
      	 window.close();"><%=sLoc%>
      </a>
      </td>
        <td class="main2"><%=sLocDesc%></td>
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