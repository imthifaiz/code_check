<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>Batch List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Batch</font></TH>
      <TH align="left"><font color="white">Qty</font></TH>
   </TR>
<%
    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"));
    String sBGColor = "";
    try{
    	if(BATCH.equalsIgnoreCase("NOBATCH"))
    	{
    		BATCH="";
    	}
  	    List listQry =  _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEMNO,LOC,BATCH);
       	for(int i =0; i<listQry.size(); i++) {
     		sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     		Map m=(Map)listQry.get(i);
     		String sBatch   = (String)m.get("batch");
     		String sQty   = strUtils.formatNum((String)m.get("qty"));
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH.value='<%=sBatch%>';
        window.opener.form.QTY.value='<%=sQty%>';
      window.close();"><%=sBatch%></a>
      </td>
      <td class="main2" align="left"><%=sQty%></td>
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