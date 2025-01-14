<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.db.util.CatalogUtil"%>
<%@ page import="java.util.*"%>
<%@page import="com.track.tables.CATALOGMST"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>

<html>
<head>

<title>Preview Image</title>
</head>
<link rel="stylesheet" href="../css/style.css" type="text/css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<% 
String loginuser ="",Qty="",enteredqty="",action="",productid="",plant="";
String catalogPath="",price="",description1="",product1="",description2="",description3="",uom="",strIndex="";
String detail1="",detail2="",detail3="",detail4="",detail5="",detail6="",detail7="";
catalogPath = (String)session.getAttribute("IMAGEPATH");
Hashtable htcratlog = new Hashtable();
price = StrUtils.fString(request.getParameter("PRICE"));
product1 = StrUtils.fString(request.getParameter(IDBConstants.PRODUCTID));
description1 = StrUtils.fString(request.getParameter(IDBConstants.DESCRIPTION1));
description2 = StrUtils.fString(request.getParameter(IDBConstants.DESCRIPTION2));
description3 = StrUtils.fString(request.getParameter(IDBConstants.DESCRIPTION3));
detail1 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL1));
detail2 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL2));
detail3 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL3));
detail4 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL4));
detail5 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL5));
detail6 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL6));
detail7 = StrUtils.fString(request.getParameter(IDBConstants.DETAIL7));
System.out.println("price"+price+product1);
htcratlog.put(IDBConstants.CATLOGPRICE,price);
htcratlog.put(IDBConstants.PRODUCTID,product1);
htcratlog.put(IDBConstants.DESCRIPTION1,description1);
htcratlog.put(IDBConstants.DESCRIPTION2,description2);
htcratlog.put(IDBConstants.DESCRIPTION3,description3);
htcratlog.put(IDBConstants.CATLOGPATH,"");
htcratlog.put(IDBConstants.DETAIL1,detail1);
htcratlog.put(IDBConstants.DETAIL2,detail2);
htcratlog.put(IDBConstants.DETAIL3,detail3);
htcratlog.put(IDBConstants.DETAIL4,detail4);
htcratlog.put(IDBConstants.DETAIL5,detail5);
htcratlog.put(IDBConstants.DETAIL6,detail6);
htcratlog.put(IDBConstants.DETAIL7,detail7);


 %>


<body bgcolor="#ffffff" >
<form name="form" method="post">


<table align="center" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="center">
		<td colspan="3"></td>
	</tr>
	
	<tr>
	<td></td>
		<td align="center" ><img
	src="/track/ReadFileServlet/?fileLocation=<%=catalogPath%>" alt=""
	name="<%=productid%>" width="20%" id="<%=productid%>" /></td>
		<td></td>
	</tr>
	<tr><td colspan="2" align="center"><input type="button" name="Back"   value="Back" onclick="onBack();"></td></tr>
</table>

	
</form>
</body>
<script type="text/javascript">
function onBack()
{
document.form.action="createCatalog.jsp?action=view";	
document.form.submit();
}

</script>
</html>

