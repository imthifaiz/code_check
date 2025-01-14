<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.PoDetDAO"%>
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
       <TH align="left"><font color="white">Customer Name</font></TH>
      <TH align="left"><font color="white">Item</font></TH>
     
    </TR>
<%
   session = request.getSession();
    PoDetDAO  _PoDetDAO  = new PoDetDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT = strUtils.fString((String)session.getAttribute("PLANT"));
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));

    String sBGColor = "";
   try{
   
    List listQry =  _PoDetDAO.getInboundOrderDetailsByWMS(PLANT,ORDERNO);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sOrderno=(String)m.get("pono");
     String sOrderlno=(String)m.get("polnno");
     String sCustname    = (String)m.get("custname");
     String sItem     = (String)m.get("item");
     String sDesc = new com.track.dao.ItemMstDAO().getItemDesc(PLANT,sItem);
     sDesc      = strUtils.replaceCharacters2Send1(sDesc);
     String sOrdqty=(String)m.get("qtyor");
     String srcqty=(String)m.get("qtyor");
     String sRef= (String)m.get("ref");
     String sLoc="HOLDINGAREA";
     String sBatch="";
  

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.ORDERNO.value='<%=sOrderno%>';
         window.opener.form.ORDERLNO.value='<%=sOrderlno%>';
        window.opener.form.CUSTNAME.value='<%=sCustname%>';
   
      
      window.close();"><%=sOrderno%></a>
      </td>
   
      <td class="main2"><%=sCustname%></td>
      <td class="main2"><%=sItem%></td>
     
     
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