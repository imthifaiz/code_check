<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition",
			"attachment;filename=StockTake.xls");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String PLANT = (String) session.getAttribute("PLANT");

	String action = StrUtils.fString(request.getParameter("action"))
			.trim();
	String itemNo = StrUtils.fString(request.getParameter("ITEM"))
			.trim();
         
        String itemDesc = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("DESC")))
        .trim();
	String location = StrUtils.fString(request.getParameter("LOC_ID"))
			.trim();
	
	String LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"))
			.trim();

	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	InvUtil invUtil = new InvUtil();
	ArrayList movQryList = new ArrayList();
	StrUtils _strUtils = new StrUtils();
	StockTakeUtil stockTakeUtil = new StockTakeUtil();
	boolean resetFlag = false;
	String res = "";
	String xlAction = StrUtils.fString(
			request.getParameter("xlAction")).trim();
	
	if (xlAction.equalsIgnoreCase("Reset")) {

		try {
			resetFlag = stockTakeUtil.ResetStkTake();
			if (resetFlag) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">StockTake  Deleted Successfully</font>";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">No Data Found For Stock Take</font>";
			}

		} catch (Exception e) {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">No Data Found For Stock Take</font>";
		}
	}

	if (action != null) {
		if (action.equalsIgnoreCase("export")) {
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment;filename=StockTake.xls");
			
		}
	}
	try {
		HashMap<String, String> inputValues = new HashMap<String, String>();
		if (!itemNo.equals("")) {
			inputValues.put("ITEM", itemNo);
		}
		if (!location.equals("")) {
			inputValues.put("LOC", location);
		}
		movQryList = stockTakeUtil.getStockTakeDetails(PLANT,inputValues,itemDesc,LOC_TYPE_ID);

	} catch (Exception e) {
	}
%>

<html>
<head>

<title>Stock Take</title>
</head>
<body>
<br>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">STOCK
		TAKE</font></TH>
	</TR>
</TABLE>
<br>


<br>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#000066">
		<TH><font color="#ffffff" align="center">SNO</TH>
		<TH><font color="#ffffff" align="left"><b>LOC</TH>
		<TH><font color="#ffffff" align="left"><b>PRODUCT ID</TH>
		<TH><font color="#ffffff" align="left"><b>DESCRIPTION</TH>
		<TH><font color="#ffffff" align="left"><b>BATCH</TH>
		<TH><font color="#ffffff" align="left"><b>STOCKTAKE
		QTY</TH>
		<TH><font color="#ffffff" align="left"><b>INVENTORY
		QTY</TH>
		<TH><font color="#ffffff" align="left"><b>QTY
		DIFFERENCE</TH>
	</tr>
	<%
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {

			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
	%>
	<TR bgcolor="<%=bgcolor%>">
		<TD align="center"><%=iIndex%></TD>
		<TD align="left"><%=(String) lineArr.get("LOC")%></TD>
		<TD><%=(String) lineArr.get("ITEM")%></TD>
		<TD><%=(String) lineArr.get("DESCRIP")%></TD>
		<TD align="left"><%=(String) lineArr.get("BATCH")%></TD>
		<TD align="left"><%=(String) lineArr.get("QTY")%></TD>
		<TD align="center"><%=(String) lineArr.get("INVQTY")%></TD>

		<TD align="left"><%=(String) lineArr.get("DIFFQTY")%></TD>


	</TR>
	<%
		}
	%>

</TABLE>
<br>
</body>
</html>