
<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>

<%@ page import="com.track.dao.RsnMst"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Misc Reason Code</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Code</font></TH>
      <TH align="left"><font color="white">Description</font></TH>
       
    </TR>
<%
  session = request.getSession(); 
   RsnMst  _RsnMst  = new  RsnMst();  
    StrUtils strUtils = new StrUtils();
    String PLANT = strUtils.fString((String)session.getAttribute("PLANT"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
	String index =  strUtils.fString(request.getParameter("INDEX"));
System.out.println("Index"+index);	
    String sBGColor = "";
   try{
   

    List listQry = _RsnMst.getMiscReasonCodeWMS(PLANT);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sItem     = (String)m.get("rsncode");
     String sDesc = (String)m.get("rsndesc");
  
%>
<%if(index==null||index.equalsIgnoreCase("")){ %>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.REASONCODE.value='<%=sItem%>';
     
      window.close();"><%=sItem%></a>
      </td>
       <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td></TR>
<%}else { %>
     
     <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.REASONCODE_<%=index%>.value='<%=sItem%>';
      window.close();"><%=sItem%></a>
      </td>
       <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
</TR>
<% }%>
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