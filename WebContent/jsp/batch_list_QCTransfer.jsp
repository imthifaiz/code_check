<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<html>
<head>
<title>Batch List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR BGCOLOR="#000066">
		<TH align="left"><font color="white">Batch</font></TH>
		<TH align="left"><font color="white">Qty</font></TH>
	</TR>
	<%
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
			(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
    _InvMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String FROM_LOC = strUtils.fString(request.getParameter("LOC"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"));
    String prdCnt = strUtils.fString(request.getParameter("INDEX"));
    String sBGColor = "";
   try{

    List listQry =  _InvMstDAO.getLocationTransferBatch(PLANT,ITEMNO,FROM_LOC,BATCH);
    for(int i =0; i<listQry.size(); i++) {
     ItemMstDAO itemMD = new ItemMstDAO();
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sBatch    = (String)m.get("batch");
     String sQty=StrUtils.formatNum((String)m.get("qty"));
     String uom = itemMD.getItemUOM(PLANT,ITEMNO);
    
   %>
	<TR bgcolor="<%=sBGColor%>">
	<%if(prdCnt=="") {%>
		<td class="main2"><a href="#"
			onClick=" window.opener.form.BATCH.value='<%=sBatch%>';
            window.opener.form.QTY.value='<%=sQty%>';
            window.opener.form.INVQTY.value='<%=sQty%>';
      		window.opener.form.UOM.value='<%=uom%>';
     
     		window.close();"><%=sBatch%></a>
		</td>
		<td class="main2"><%=strUtils.replaceCharacters2Recv(sQty)%></td>
<% }else {%>		
<td class="main2"><a href="#"
			onClick=" window.opener.form.BATCH_<%=prdCnt%>.value='<%=sBatch%>';
            window.opener.form.INVQTY_<%=prdCnt%>.value='<%=sQty%>';
            window.opener.form.UOM_<%=prdCnt%>.value='<%=uom%>';
     		window.close();"><%=sBatch%></a>
			
		</td>
<td class="main2"><%=strUtils.replaceCharacters2Recv(sQty)%></td>

<% }%>
	</TR>
	<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
	<TR>
		<TH COLSPAN="8">&nbsp;</TH>
	</TR>
	<TR>
		<TH COLSPAN="8" align="center"><a href="#"
			onclick=
	window.close();;
><input type="submit" value="Close"></a></TH>
	</TR>
</table>
</body>
</html>