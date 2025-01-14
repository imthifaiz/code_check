<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.PoDetDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Inbound Item List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
       <TH align="left"><font color="white">Product</font></TH>
      <TH align="left"><font color="white">Product Desc</font></TH>
      
     
    </TR>
<%
   session = request.getSession();
    PoDetDAO  _PoDetDAO  = new PoDetDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));

    String sBGColor = "";
   try{
   
  
    List listQry =  _PoDetDAO.getInboundItemListByWMS(PLANT,ORDERNO,ITEMNO);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sOrderno=(String)m.get("pono");
     String sItem     = (String)m.get("item");
     String sORDERLNO =(String)m.get("polnno");
    
     String sDesc = new com.track.dao.ItemMstDAO().getItemDesc(PLANT,sItem);
     sDesc      = strUtils.replaceCharacters2Send1(sDesc);
     String sOrdqty=(String)m.get("qtyor");
     String srcqty=(String)m.get("qtyrc");
     String srcbal=(String)m.get("balanceqty");
     String sRef= (String)m.get("ref");
     String sLoc="HOLDINGAREA";
     String sBatch="NOBATCH";
  

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.ITEMNO.value='<%=sItem%>';
      window.opener.form.ITEMDESC.value='<%=sDesc%>';
      window.opener.form.LOC.value='<%=sLoc%>';
      window.opener.form.BATCH.value='<%=sBatch%>';
      window.opener.form.ORDERQTY.value='<%=sOrdqty%>';
      window.opener.form.ORDERLNO.value='<%=sORDERLNO%>';
      window.opener.form.RECEIVEDQTY.value='<%=srcqty%>';
      window.opener.form.REF.value='<%=sRef%>';
      window.opener.form.BALANCEQTY.value='<%=srcbal%>';
     
      window.close();"><%=sItem%></a>
      </td>
       <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
       
     
     
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