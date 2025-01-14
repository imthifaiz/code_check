<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>

<title>Stock Re Order Item List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
ItemUtil itemUtil       = new ItemUtil();
ArrayList itemQryList  = new ArrayList();
itemQryList = itemUtil.getStockReorderItemList();
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">STOCK  REORDER  ITEM  LIST</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">#</TH>
         <TH><font color="#ffffff" align="center">Item</TH>
         <TH><font color="#ffffff" align="left"><b>Item Description</TH>
         <TH><font color="#ffffff" align="left"><b>Re Order Qty</TH>
         <TH><font color="#ffffff" align="left"><b>Qty in Hand</TH>
       </tr>
       <%
          for (int iCnt =0; iCnt<itemQryList.size(); iCnt++){
          ArrayList lineArr = new ArrayList();
          lineArr = (ArrayList)itemQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=(String)lineArr.get(0)%></TD>
            <TD><%=(String)lineArr.get(1)%></TD>
            <TD align= "right"><%=(String)lineArr.get(2)%></TD>
            <TD align= "right"><%=(String)lineArr.get(3)%></TD>
           </TR>
       <%}%>

    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>