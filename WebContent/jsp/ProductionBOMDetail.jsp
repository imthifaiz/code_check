<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<html>
<head>
<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
	function popUpWin(URL) {
		subWin = window.open(URL,'ProductionBOMDetail','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function goBack()
	 {
	   window.history.back();
	 }
	
	function submitForm(actionvalue)
	{
		document.form.action = "/track/WorkOrderServlet?Submit="+actionvalue;
	    document.form.submit();
	}
</script>
<title>Production BOM Detail</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List  BOMList = new ArrayList();
	String fieldDesc = "";
	String PLANT = "", ITEM = "",QTY="";
	String html = "";
	int Total = 0;
	double requiredQty=0,qtyPer=0;
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
	String PGaction = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	ITEM = strUtils.fString(request.getParameter("ITEM"));
	QTY=strUtils.fString(request.getParameter("QTY"));
	ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
	_ProductionBomUtil.setmLogger(mLogger);
	try {
			Hashtable ht = new Hashtable();
			if (strUtils.fString(ITEM).length() > 0)
			BOMList = _ProductionBomUtil.getProductionBOMDetail(ITEM, PLANT, " ORDER BY CITEM");
	    } catch (Exception e) {
	}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="ProductionBOMDetail.jsp"><input
	type="hidden" name="xlAction" value=""> <input type="hidden"
	name="PGaction" value="View">  <br>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"	align="center" bgcolor="#dddddd">
  <input type="hidden" name="ITEM" value="<%=ITEM%>">
  <input type="hidden" name="QTY" value="<%=QTY%>">
	<TR>		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Production BOM Detail</font></TH>
	</TR>
</TABLE>
<br>
<center><font face="Times New Roman" size="4">
<TABLE WIDTH="60%" border="0" cellspacing="1" cellpadding=2	align="center">
	<TR BGCOLOR="#000066">
		<TH><font color="#ffffff" align="center">S/N</TH>
		<TH><font color="#ffffff" align="left"><b>Parent Product</TH>
		<TH><font color="#ffffff" align="left"><b>Child Product</TH>
		<TH><font color="#ffffff" align="left"><b>QtyPer</TH>
		<TH><font color="#ffffff" align="left"><b>Remark1</TH>
		<TH><font color="#ffffff" align="left"><b>Remark2</TH>
		<TH><font color="#ffffff" align="left"><b>Qty Required</TH>
	</TR>
	 <%
	    if(BOMList.size()<=0 ){ %>
		  <TR><TD colspan=8 align=center>No Data Found</TD></TR>
	  <%}%>
	 <%
		for (int iCnt = 0; iCnt < BOMList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			requiredQty=0;
			qtyPer=0;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF": "#dddddd";
			ArrayList lineArr = (ArrayList)BOMList.get(iCnt);
			requiredQty=Double.parseDouble(((String)QTY.toString()));
			qtyPer=Double.parseDouble(((String)lineArr.get(2).toString()));
			requiredQty=requiredQty*qtyPer;
	%>
	<TR bgcolor="<%=bgcolor%>">
		<TD align="left">&nbsp;<%=iIndex%></TD>
		<TD align="left" class="textbold">&nbsp; <%=strUtils.removeQuotes((String) lineArr.get(0))%></TD>
		<TD align="left" class="textbold">&nbsp;<%=strUtils.fString((String) lineArr.get(1))%></TD>
		<TD align="right" class="textbold">&nbsp;<%=decformat.format(qtyPer)%></TD>
		<TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String) lineArr.get(3))%></TD>
		<TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String) lineArr.get(4))%></TD>
		<TD align="right" class="textbold">&nbsp; <%=decformat.format(requiredQty)%></TD>
		
		
	</TR>
	<%
		}
	%>
</TABLE>
<center><br>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
	<td width="42%"></td>
	<td>
	    <input type='button' value="Back" name="Submit" onclick="goBack()" />&nbsp;
	    
          <input type='button' value="Export To Excel" name="Submit" onclick="submitForm('Export To ProductionBOMExcel');" />
	 </td>
	</tr>
</table>
</br>
</center>
</FORM>
<%@ include file="footer.jsp"%>