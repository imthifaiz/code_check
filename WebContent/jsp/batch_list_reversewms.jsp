<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title> Batch List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Order No</font></TH>
      <TH align="left"><font color="white">Product ID</font></TH>
      <TH align="left"><font color="white">Product Desc</font></TH>
       <TH align="left"><font color="white">Loc</font></TH>
       <TH align="left"><font color="white">Batch</font></TH>
        <TH align="left"><font color="white">OrderQty</font></TH>
     </TR>
<%
   session=request.getSession();
    RecvDetDAO  _RecvDetDAO  = new RecvDetDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"));
    String sBGColor = "";
   try{
     List listQry =  _RecvDetDAO.getRevBatchListByWMS(PLANT,ORDERNO,ITEMNO,LOC,BATCH);
     for(int i =0; i<listQry.size(); i++) {
      	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
     	String sOrderno=(String)m.get("pono");
     	String sLoc=(String)m.get("loc");
     	String sBatch=(String)m.get("Batch");
        String sItem     = (String)m.get("item");
     	String sDesc = (String)m.get("itemdesc");
      	String sRemark = (String)m.get("remark");
     	String sOrdQty=(String)m.get("qtyor");
     	String sRecqty=(String)m.get("qtyrc");
     	String sRevqty=(String)m.get("qtyrev");
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick=" window.opener.form.BATCH.value='<%=sBatch%>';
       window.opener.form.REF.value='<%=sRemark %>';
       window.opener.form.REVERSEDQTY.value='<%=sRevqty%>';
       window.close();"><%=sOrderno%></a>
      </td>
       <td class="main2"><%=sItem%></td>
       <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
       <td class="main2"><%=sLoc%></td>
       <td class="main2"><%=sBatch%></td>
       <td class="main2"><%=sOrdQty%></td>
   
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