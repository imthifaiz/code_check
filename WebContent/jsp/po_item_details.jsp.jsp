<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<title>Stock Re Order Item List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	String PONO           = request.getParameter("PONO");
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	POUtil poUtil         = new POUtil();
	ArrayList poList      = new ArrayList();
	List qryFields   = new ArrayList();
	qryFields.add(IConstants.PONO);     // PONO
	qryFields.add(IConstants.ITEM);     // item
	qryFields.add(IConstants.USERFLD1); // Descriotion
	qryFields.add(IConstants.QTY_ORDER); // Qty order
	qryFields.add(IConstants.QTY_RECV); // Qty Receive
	
	Hashtable htCondition = new Hashtable();
	htCondition.put(IConstants.PONO,PONO);
	poList = poUtil.queryPODET_Col(qryFields,htCondition,"");

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Stock Re Order Item List</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">#</TH>
         <TH><font color="#ffffff" align="center">Item</TH>
         <TH><font color="#ffffff" align="left"><b>Description</TH>
         <TH><font color="#ffffff" align="left"><b>Qty Ord</TH>\
         <TH><font color="#ffffff" align="left"><b>Qty Receive</TH>\
       </tr>
       <%
          for (int iCnt =0; iCnt<poList.size(); iCnt++){
          	  ArrayList lineArr = new ArrayList();
	          lineArr = (ArrayList)poList.get(iCnt);
	          int iIndex = iCnt + 1;
	          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=(String)lineArr.get(0)%></TD>
            <TD><%=(String)lineArr.get(1)%></TD>
            <TD><%=(String)lineArr.get(2)%></TD>
            <TD><%=(String)lineArr.get(3)%></TD>
            <TD><%=(String)lineArr.get(4)%></TD>
           </TR>
       <%}%>

    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>