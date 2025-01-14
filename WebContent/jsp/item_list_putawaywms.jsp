<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Transfer In Item List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Product</font></TH>
      <TH align="left"><font color="white">Product Desc</font></TH>
       <TH align="left"><font color="white">Qty</font></TH>
     
    </TR>
<%
   session= request.getSession();
    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT = strUtils.fString((String)session.getAttribute("PLANT"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));

    String sBGColor = "";
   try{
   
    List listQry =  _InvMstDAO.getPutAwayItemByWMS(PLANT,ITEMNO);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sItem     = (String)m.get("item");
     String sDesc = new com.track.dao.ItemMstDAO().getItemDesc(PLANT,sItem);
     sDesc      = strUtils.replaceCharacters2Send1(sDesc);
     String sFromLoc=(String)m.get("FromLoc");
     String sQty=(String)m.get("Qty");
   
    
 

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.ITEMNO.value='<%=sItem%>';
      window.opener.form.ITEMDESC.value='<%=sDesc%>';
      window.opener.form.FROMLOC.value='<%=sFromLoc%>';
      
    
      
      window.close();"><%=sItem%></a>
      </td>
       <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
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