<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.LoanDetDAO"%>
<%@ page import="com.track.util.*"%>


<%@page import="com.track.constants.IConstants"%><html>
<head>
<title>Batch List</title>
<link rel="stylesheet" href="../css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Batch</font></TH>
     
    </TR>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    LoanDetDAO  _loanMstDAO  = new LoanDetDAO();  
      _loanMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
     String ORDNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ORDLNNO = strUtils.fString(request.getParameter("ORDERLNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"));

    String sBGColor = "";
   try{
   

    List listQry =  _loanMstDAO.getLoanOrderBatchListToRecv(PLANT,ORDNO,ORDLNNO,ITEMNO,LOC,BATCH);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sBatch   = (String)m.get("batch");
     String spickQty   = (String)m.get("pickQty");
     String srecvQty   = (String)m.get("recQty");
     
    double availQty = Double.parseDouble(spickQty) - Double.parseDouble(srecvQty);
	availQty = StrUtils.RoundDB(availQty,IConstants.DECIMALPTS);

%>
<% if(availQty>0){%>

    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH.value='<%=sBatch%>';
      window.opener.form.QTY.value='<%=availQty%>';
      window.close();"><%=sBatch%></a>
      </td>
 </TR>
 <%}%>
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