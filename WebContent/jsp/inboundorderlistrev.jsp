<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Inbound Order List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Order No </font></TH>
       <TH align="left"><font color="white">Supplier </font></TH>
      
     
    </TR>
<%
   session = request.getSession();
    RecvDetDAO  _RecvDetDAO   = new RecvDetDAO ();  
    StrUtils strUtils = new StrUtils();
    String PLANT = strUtils.fString((String)session.getAttribute("PLANT"));
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));

    String sBGColor = "";
   try{

    List listQry =  _RecvDetDAO.getRevInboundOrderDetailsByWMS(PLANT,ORDERNO);

    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sOrderno=(String)m.get("pono");
     String sCustname    = (String)m.get("custname");
     

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.ORDERNO.value='<%=sOrderno%>';
        window.opener.form.CUSTNAME.value='<%=sCustname%>';
     
      
      window.close();"><%=sOrderno%></a>
      <td class="main2"><%=sCustname%></td>
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