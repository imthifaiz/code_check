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

<title>Catalog Image</title>
</head>
<link rel="stylesheet" href="css/style.css" type="text/css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<%
	String loginuser = "", Qty = "", enteredqty = "", action = "", productid = "", plant = "", pageName = "", url = "";
	String catalogPath = "", price = "", description1 = "",detailPage="", product1 = "", description2 = "", description3 = "", uom = "", strIndex = "";
	int counter = 0, nextCnt = 0, prevCnt = 0, iIndex = 0;
	double dprice = 0.0;
	CatalogUtil _catlogutil = new CatalogUtil();
	String servername = request.getServerName();
         int port  = request.getServerPort();
	plant = StrUtils.fString(request.getParameter(IDBConstants.PLANT));
	productid = StrUtils.fString(request.getParameter(IDBConstants.PRODUCTID));
	pageName = StrUtils.fString(request.getParameter("PAGE"));
	Hashtable ht = new Hashtable();
	ht.put(IDBConstants.PLANT, plant);
	ht.put(IDBConstants.PRODUCTID, productid);

	List catloglst = _catlogutil
			.listCatalogs(
					"distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISNULL(DETAILLABEL,'') DETAILLABEL,ISACTIVE ",
					ht, " AND PRODUCTID like '" + productid + "%' ");

	Map catalogmap = (Map) catloglst.get(iIndex);
	catalogPath = (String) catalogmap.get(IDBConstants.CATLOGPATH);
	productid = (String) catalogmap.get(IDBConstants.PRODUCTID);
	price =(String) catalogmap.get(IDBConstants.CATLOGPRICE);
        price = StrUtils.formattwodecNum(price);
	description1 = (String) catalogmap.get(IDBConstants.DESCRIPTION1);
	description2 = (String) catalogmap.get(IDBConstants.DESCRIPTION2);
	description3 = (String) catalogmap.get(IDBConstants.DESCRIPTION3);
	detailPage = (String) catalogmap.get(IDBConstants.DETAILLABEL);
	uom = new ItemMstDAO().getItemUOM(plant, productid);
	//servername="192.168.10.103";
	String delim = "AAABBACCB";
	url = "http://" + servername+ ":"+port+"/track/jsp/mobileEventReg.jsp?ID=" + plant + delim+ productid;
       // url = "http://" + servername+ ":8080/track/jsp/mobileEventReg.jsp?ID=" + plant + delim+ productid;
	if (price.equalsIgnoreCase("0.00"))
		dprice = 0.00;
	else
		dprice = Double.parseDouble(StrUtils.removeFormat(price));
	System.out.println("url" + url + price + dprice);
%>


<body bgcolor="#ffffff">
<form name="form" method="post">
<table align="center" cellpadding="0" cellspacing="0" width="100%">
<tr>	
		<td align="right" class="mobilelabel"><a
			href="instrtnsummry.jsp?PLANT=<%=plant%>&PAGENAME=registerimage&PRODUCTID=<%=productid%>"><font
			size="5" color="#669900"><b>INSTRUCTIONS</b></font></a></td>
	</tr>
</table>

<table align="center" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td> &nbsp;</td>
	</tr>

	<tr>
		
		<td align="center"><img
			src="/track/ReadFileServlet/?fileLocation=<%=catalogPath%>" alt=""
			name="<%=productid%>" width="315" id="<%=productid%>" /></td>
			</tr>
		<tr>
		<td>&nbsp;</td>
		</tr>
	<tr>
		
		<td align="center" class="mobilelabel">&nbsp;<font size="5"><b>Code:</b>&nbsp;<%=productid%>
		</font></td>
		
	</tr>
	<%
		if (dprice > 0) {
	%>
	<tr>
		
		<td align="center" class="mobilelabel">&nbsp;<font size="5"><b>Price:</b>&nbsp;S$<%=price%>/<%=uom%>
		</font></td>
		
	</tr>
	<%
		}
	%>
	<tr>
		
		<td align="center" class="mobilelabel">&nbsp;<font size="5"><b>
		<%=description1%></b> </font></td>
		
	</tr>

	<tr>
		
		<td align="center" class="mobilelabel"><font size="5"> <%=description2%></font></td>
		
	</tr>

	<tr>
		
		<td align="center" class="mobilelabel"><font size="5"><%=description3%></font></td>
		
	</tr>
	<tr>
		<td align="center"  class="mobilelabel"><a
			href="pcMoreDetails.jsp?PLANT=<%=plant%>&CATALOGID=<%=productid%>&PAGENAME=registerimage"><font
			size="5"><b><%=detailPage %></b></font></a></td>

	</tr>
	<tr>
		<td >&nbsp;</td>
	</tr>
	<tr>
		<td align="center" ><a href="#"
			style="border: 0 none; cursor: default; text-decoration: none;">
		<!--<img src="http://api.qrserver.com/v1/create-qr-code/?data=<%=url%>&#38;size=100x100" alt="QR Code" title="" /></a>-->
                       <img src="/track/qrservlet?qrtext=<%=url%>" alt="QR Code" title="" /></a>
                        </td>

	</tr>
	<tr>
		<td >&nbsp;</td>
	</tr>
	<%
		if (pageName.equalsIgnoreCase("EDIT_LOG")) {
	%>
	<TR>
		<TD COLSPAN=2 align="center"><input type="button" name="Back"
			value="Close" class="mobileButtonB" onclick="window.close();">&nbsp;&nbsp;
		</TD>
	</TR>
	<%
		} else {
	%>
	<TR>
		<TD COLSPAN=2 align="center"><input type="button" name="Back"
			value="Close" class="mobileButtonB" onclick="window.close();">&nbsp;&nbsp;
		</TD>
	</TR>
	<%
		}
	%>
	<tr>
		<td >&nbsp;</td>
	</tr>
	<tr valign="top">
		<td align="center" valign="bottom" >&nbsp;</td>
	</tr>


</table>

</form>
</body>
</html>
<script type="text/javascript">
function onBackPage() {		
	var productid="<%=productid%>";
	var pagename="<%=pageName%>"
		if(pagename=='EDIT_LOG'){
	document.form.action = "editCatalog.jsp?action=VIEW&PRODUCTID=<%=productid%>";
	document.form.submit();}
		else
		{
			document.form.action ="sumryCatalogDetails.jsp?action=VIEW&PRODUCTID=<%=productid%>&PLANT=<%=plant%>";
	
			document.form.submit();
		}
	}
</script>

