<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Loc List</title>
<link rel="stylesheet" href="../css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Loc Id</font></TH>
      <TH align="left"><font color="white">Loc Desc</font></TH>
    </TR>
<%
    String plant= (String)session.getAttribute("PLANT");
     String userID= (String)session.getAttribute("LOGIN_USER");
    CustUtil custUtils = new CustUtil();
    StrUtils strUtils = new StrUtils();
    String sLoc = strUtils.fString(request.getParameter("LOC_ID"));

    String sBGColor = "";
   try{
    LocUtil _LocUtil = new LocUtil();
    ArrayList arrLoc = _LocUtil.getAllLocDetails(plant," AND ISACTIVE ='Y' ",userID);
    
    for(int i =0; i<arrLoc.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrLocLine = (Map)arrLoc.get(i);
        String sLocCode    = (String)arrLocLine.get("LOC");
        String sLocDesc   = strUtils.removeQuotes( strUtils.fString((String)arrLocLine.get("LOCDESC")));
        String sRemark      = strUtils.removeQuotes((String)arrLocLine.get("USERFLD1"));
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.LOC_ID.disabled = true;
      window.opener.form1.FRLOC.value='<%=sLocCode%>';
      window.close();"><%=sLocCode%>
      </a>
      </td>
      <td class="main2"><%=sLocDesc%></td>
</TR>
<%
}
}catch(Exception he){he.printStackTrace(); 
System.out.println("Error in reterieving data");}
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





