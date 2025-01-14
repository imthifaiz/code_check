<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.ArrayList.*"%>
<%@ include file="header.jsp" %>
<!-- Not in Use -->

<html>
<title>CYCLE COUNT</title>
<link rel="stylesheet" href="css/style.css">

<%
  CCUtil ccUtils    = new CCUtil();
  StrUtils strUtils = new StrUtils();
  ArrayList arrList = ccUtils.getCCItemList();
  String sBGColor = "";
  String sLink    = "";
  String res      = "";
  res = strUtils.fString(request.getParameter("res"));
%>
<%@ include file="body.jsp" %>
  <center>
  <br><br>
     <TABLE border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CYCLE COUNT ITEM LIST</font>
      </TABLE>
  <BR>
    <TABLE BORDER="0" CELLSPACING="1" WIDTH="80%" align="center">
      <TR bgcolor="navy">
        <TH width="20%"><font color="#ffffff">Item No</TH>
        <TH width="40%"><font color="#ffffff">Description</TH>
        <TH width="15%"><font color="#ffffff">StartDate</TH>
        <TH width="15%"><font color="#ffffff">End Date</TH>
     </TR>
     <%
      for(int iCnt = 0; iCnt<arrList.size(); iCnt++){
          ArrayList arrLine = (ArrayList)arrList.get(iCnt);
          sBGColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";
          sLink = "<a href=\"CC_itemAdd.jsp?"+
                  "CYCLESDATE="+(String)arrLine.get(2)+
                  "&CYCLEEDATE="+(String)arrLine.get(3)+
                  "&ITEMDESC="+strUtils.replaceCharacters2Send((String)arrLine.get(1))+
                  "&ITEM=" + strUtils.replaceCharacters2Send((String)arrLine.get(0)) + "\">";
     %>
     <TR bgcolor="<%=sBGColor%>">
          <TD><%=sLink%><%=(String)arrLine.get(0)%></a></TD>
          <TD><%=(String)arrLine.get(1)%></TD>
          <TD><%=(String)arrLine.get(2)%></TD>
          <TD><%=(String)arrLine.get(3)%></TD>
     </TR>
    <%
       }
    %>
    <TR><TH colspan = 4><%=res%></TH></TR>

    </TABLE>
    </font>
    <center>
<form action="CC_itemAdd.jsp">
      <input type="submit" value="Add New">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
</form>

    </center>
  </center>

<%@ include file="footer.jsp" %>
