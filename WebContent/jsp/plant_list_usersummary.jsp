<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Company List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Company Name</font></TH>
    </TR>
<%
    ArrayList invQryList  = new ArrayList();
    userBean _userBean      = new userBean();
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String COMPANY = strUtils.fString(request.getParameter("COMPANY"));
 
    Hashtable ht = new Hashtable();
    if(strUtils.fString(COMPANY).length() > 0) 
      ht.put("DEPT",COMPANY);
      List listQry  =_userBean.getUserListCompany(ht,PLANT,COMPANY);
      String sBGColor = "";
   try{
        for(int i =0; i<listQry.size(); i++) {
       	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
    	String sCompany   = (String)m.get("DEPT");
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      	<a href="#" onClick=" window.opener.form1.COMPANY.value='<%=sCompany%>';
           window.close();"><%=sCompany%>
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