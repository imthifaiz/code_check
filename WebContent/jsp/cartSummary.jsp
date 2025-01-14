
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.tables.CATALOGMST"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<html>
<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
 function onGo(){
 
     document.form.submit();
}

 function scannextprd()
 {
	 var plant =document.form.PLANT.value;
	 var scanid = document.form.SCANID.value;
	 var index = document.form.INDEX.value;
	 var delim="AAABBACCB";
	 //document.form.action  ="mobileShopping.jsp?PLANT="+plant+"&PRODUCTID="+scanid+"&INDEX="+index;
	  document.form.action  ="mobileShopping.jsp?ID="+plant+delim+scanid+delim+"N"+delim+index;
	 document.form.submit();
	 
 }
</script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<title>Cart Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	
	Vector scanitemList = new Vector<CATALOGMST>();
	
	String fieldDesc = "";
	String PLANT = "", item = "";
	String html = "",imageFile="",Qty="",productid="",price="",loginuser="",desc="";
	int Total = 0;float pricef=0;
        int cntProd=0;
	String SumColor = "",action="";
	double totalPrice=0,lineprice=0,gstval=0;
	boolean flag = false;
	session = request.getSession();
	  DecimalFormat decformat = new DecimalFormat("#,##0.00");
	
	String strtotprice="",scanid="",index="";
	scanitemList= (Vector<CATALOGMST>)session.getAttribute("catloglst");
      
      try{
       cntProd = scanitemList.size();
  
      }catch(Exception e){
           fieldDesc="Session Cookies are disabled,Please enable them to process !";
          }
        
       
	PLANT = (String)session.getAttribute(IDBConstants.PLANT);
        if (((String) session.getAttribute("PLANT")) != null)
        PLANT = (String) session.getAttribute("PLANT");
        if (PLANT == null || PLANT == "")
        PLANT = request.getParameter(IDBConstants.PLANT);
	//loginuser = (String)session.getAttribute("LOGIN_USER");
	String gst = sb.getGST("POS",PLANT);
	gstval = Double.parseDouble(gst);
	CatalogUtil _catalogUtil = new CatalogUtil();
	
		try {

			Hashtable ht = new Hashtable();
			if(request.getParameter("SCANID")!=null)
				scanid =request.getParameter("SCANID"); 
			if(request.getParameter("INDEX")!=null)
				index =request.getParameter("INDEX"); 
			if(request.getParameter("action")!=null)
			{
				action= request.getParameter("action");
				if(action.equalsIgnoreCase("clear"))
				{
//System.out.println("scanitem list size"+scanitemList.size());
			scanitemList.clear();}
			}
                       
			session.setAttribute("catloglst",scanitemList);
			session.setAttribute(IDBConstants.PLANT,PLANT);
                        
			
			if (strUtils.fString(PLANT).length() > 0)
				ht.put("PLANT", PLANT);
			if(item.length()>0)
				ht.put(IDBConstants.PRODUCTID, item);
		//	scanitemList = _catalogUtil.listCatalogs("distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE ",ht,"");

			

		} catch (Exception e) {
		}
	
%>

<FORM name="form" method="post" action="cartSummary.jsp"><input
	type="hidden" name="xlAction" value=""> <input type="hidden"
	name="PGaction" value="View"> 

	<input type="hidden" name="SCANID" value="<%=scanid%>">
	<input type="hidden" name="INDEX" value="<%=index%>">
	<input type="hidden" name="PLANT" value="<%=PLANT%>">
	<br>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#669900" COLSPAN="11" height="25px" ><font color="white" size="7">
	Shopping Cart Summary </font>
	</TH>
	</TR>
</TABLE>

<center><font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<tr><td><font color="green" size="5"> &nbsp;</font></td></tr>
	<tr><td><font color="green" size="5"> </font></td></tr>
</table>
</font></center>
<br>

<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	<TR BGCOLOR="#669900">
		<TH height="9px"><font color="#ffffff" align="center" size="6">S/N</TH>
		<TH height="9px"><font color="#ffffff" align="left" size="6"><b>Description</b></TH>
		<TH height="9px"><font color="#ffffff" align="left" size="6"><b>Unit Price</b></TH>
		<TH height="9px"><font color="#ffffff" align="left" size="6"><b>Qty</b></TH>
		<TH height="9px"><font color="#ffffff" align="left" size="6"><b>Total Price</b></TH>
		<TH height="9px"><font color="#ffffff" align="left" size="6"></TH>
	</TR>
	<%
        if(cntProd>0){
		for (int iCnt = 0; iCnt < scanitemList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";
		CATALOGMST logmst = (CATALOGMST)scanitemList.get(iCnt);
				imageFile = logmst.getCatlogPath();
				productid = logmst.getProductID();
				desc = logmst.getDescription1();
				pricef = logmst.getPrice();
				String pricestr = String.valueOf(pricef);
				pricestr = StrUtils.formattwodecNum(pricestr);
				int quantity = logmst.getQuantity();
				totalPrice = totalPrice + quantity*pricef;
							
				lineprice = logmst.getPrice()*(logmst.getQuantity());
				String strlineprice = String.valueOf(lineprice);
				strlineprice = StrUtils.formattwodecNum(strlineprice);
	%>
	
	<TR bgcolor="<%=bgcolor%>">
		<TD align="center"><font size="6"><%=iIndex%></font></TD>

		<TD align="left">
		<font size="6"><%=logmst.getDescription1()%></font>
			</TD>
			<TD align="right">
			
      	<font size="6">	<%=pricestr%> </font>
			</TD>
			
			<TD align="center">
      	<font size="6">	<%=logmst.getQuantity()%> </font>
			</TD>
			<TD align="right">
      	<font size="6">	<%=strlineprice%> </font>
			</TD>
			<TD align="right">
      	<font size="6">	<INPUT class="mobiletpbtn"  type="BUTTON"
			value="Remove" onClick="onRemove('<%=iCnt%>');" > </font>
			</TD>
			</TR>
	<%
		}
	%>	
	<%
	totalPrice = totalPrice + (totalPrice*gstval)/100;
	 strtotprice =String.valueOf(totalPrice);
	strtotprice = StrUtils.formattwodecNum(strtotprice);
	%>
	<tr align="center"><td  align="right"  colspan="3">&nbsp;</td></tr>
	<% if(gstval>0){ %>
	<tr align="center"><td  align="right"  colspan="4">&nbsp;<font size="6">GST</font></td>
	<td  align="right" class="mobilelabel" >&nbsp;<font size="6"> <%=decformat.format(gstval) %>%</font> </td></tr> <%} %>
	<tr align="center"><td  align="right"  colspan="4">&nbsp;<font size="6">Total</font></td>
	<td  align="right" class="mobilelabel" >&nbsp;<font size="6"> <%=strtotprice%></font> </td></tr>
	
	
	<input type="hidden" name="SHOPPINGBAG" id="SHOPPINGBAG" value="<%=scanitemList.size()%>">
        <%}%>
        <tr><td colspan="5"><font color="Red" size="7"> <%=fieldDesc%></font></td></tr>
        
        <tr align="center"><td  align="right"  colspan="3">&nbsp;</td></tr>
        <tr align="center">
	<td colspan="3" valign="top" align="left"><input type="button" name="Back" id="Back" class="mobileButton2"  value="Back to Catalog" onclick="scannextprd();"></td>
	<td  align="center" colspan="3" valign="top" align="right"><INPUT class="mobileButton1"  type="BUTTON"
			value="Check out" onClick="onCheckout();" ></td>
			</tr>
</TABLE>
</FORM>
<script>
function onRemove(index){
	var urlStr = "/track/CatalogServlet";
	var chk = confirm("Are you sure you would like to remove?");
	if(chk){
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=PLANT%>",
		Submit : "REMOVE",
		INDEX: index
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "100") {
			document.getElementById("SHOPPINGBAG").value=data.shoppingbag;
			window.location=window.location;
		
		} 
	}
});
	}
}
function onCheckout()
{
	var basketsize=document.getElementById("SHOPPINGBAG").value;
	if(parseInt(basketsize)>0){
	 document.form.action  ="mobileLogin.jsp";
	 //document.form.action  ="/track/CatalogServlet?Submit=CHECKOUT";
	 document.form.submit();}
	else
	{
		alert('Shopping bag is empty');
		return false;
	}
	 
}
</script>
