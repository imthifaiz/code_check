<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.db.util.CatalogUtil"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@page import="com.track.tables.CATALOGMST"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>

<html>
<head>

<title>Mobile Event Registration</title>
</head>
<link rel="stylesheet" href="css/style.css" type="text/css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script>

	function onEventReg() {		
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		var index= document.form.INDEX.value;
		var scanid= document.form.PRODUCTID1.value;
		
		document.form.action = "/track/MobileEventRegServlet?Submit=EVENTREG&PLANT="
				+ plant  + "&PRODUCTID=" + productid+"&ISMEMBER=N";
		
		document.form.submit();
		}
	function onMemReg(){
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		var index= document.form.INDEX.value;
		var scanid= document.form.PRODUCTID1.value;
		document.form.action = "/track/MobileEventRegServlet?Submit=EVENTREG&PLANT="
			+ plant  + "&PRODUCTID=" + productid+"&ISMEMBER=Y";
	
	document.form.submit();	
		
	}

	function onNext() {
		var plant = document.form.PLANT.value;
		var index = document.form.INDEX.value;
		var counter = document.form.COUNTER.value;
		var productid = document.form.PRODUCTID1.value;
		var delim="AAABBACCB";
		index=parseInt(index)+1;
		document.form.INDEX.value = index;
		if(parseInt(index)>parseInt(counter))
			index=counter;
		document.form.action ="mobileEventReg.jsp?ID=" + plant+delim+productid+delim+index;
		document.form.submit();
	}
	function onPrevious(){
		var plant = document.form.PLANT.value;
		var previndex = document.form.INDEX.value;
		var delim="AAABBACCB";		
			var productid = document.form.PRODUCTID1.value;
		previndex = parseInt(previndex)-1;
		document.form.INDEX.value = previndex;
		if(parseInt(previndex)<0)
			previndex=0;
		
		document.form.action = "mobileEventReg.jsp?ID=" + plant+delim+productid+delim+previndex;
		document.form.submit();

		
	}
	function onFirst() {
		var plant = document.form.PLANT.value;
		var index = 0;
		var delim = "AAABBACCB";
		var productid = document.form.PRODUCTID1.value;						
		document.form.action = "mobileEventReg.jsp?ID=" + plant + delim
				+ productid + delim + index;
		document.form.submit();
	}
	function onLast() {
		var plant = document.form.PLANT.value;
		var counter = document.form.COUNTER.value;
		var index = 0;
		var delim = "AAABBACCB";
		var productid = document.form.PRODUCTID1.value;						
		document.form.action = "mobileEventReg.jsp?ID=" + plant + delim
				+ productid + delim + counter;
		document.form.submit();
	}
</script>
<% 
String loginuser ="",Qty="",enteredqty="",action="",productid="",plant="";
String catalogPath="",price="",description1="",product1="",description2="",pageDetails="",description3="",uom="",strIndex="";
    int counter=0,nextCnt=0,prevCnt=0,iIndex=0;
action=request.getParameter("action");
double dprice=0.0;
CatalogUtil _catlogutil = new CatalogUtil();
List orderlist=null;
Vector<CATALOGMST> scanloglst = new Vector<CATALOGMST>();
 
String id = StrUtils.fString(request.getParameter("ID"));
String delim="AAABBACCB";
String[] strarry = id.split(delim);
DecimalFormat decformat = new DecimalFormat("#,##0.00");
  
 plant = strarry[0];
 productid = strarry[1];
  product1 = productid;
  session.setAttribute(IDBConstants.PRODUCTID,productid);
  
	List fullcatloglst=null;
  Hashtable ht = new Hashtable();
  ht.put(IDBConstants.PLANT,plant);
  ht.put(IDBConstants.PRODUCTID,productid);
  ht.put(IDBConstants.ISACTIVE,"Y");

 	 if(strarry.length>2)
 	  {
 		  strIndex = strarry[2];
 			iIndex = Integer.parseInt(strIndex);
 	  }
 		
   fullcatloglst=_catlogutil.listPrevNextCatalogs("distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE ",ht," ");
  	
  
  	counter=fullcatloglst.size()-1;
//System.out.println("countersize"+counter);  
 
	  Map catalogmap = (Map)fullcatloglst.get(iIndex);
	  catalogPath = (String)catalogmap.get(IDBConstants.CATLOGPATH);
	  productid=(String)catalogmap.get(IDBConstants.PRODUCTID);
	  price= (String)catalogmap.get(IDBConstants.CATLOGPRICE);
	
	  description1=  (String)catalogmap.get(IDBConstants.DESCRIPTION1);
	  description2=  (String)catalogmap.get(IDBConstants.DESCRIPTION2);
	  description3=  (String)catalogmap.get(IDBConstants.DESCRIPTION3);
	  pageDetails = (String)catalogmap.get(IDBConstants.DETAILLABEL);
		
	uom = new ItemMstDAO().getItemUOM(plant, productid);
	if(price.equalsIgnoreCase("0.00"))
 		dprice =0.00;
 	else
 		dprice = Double.parseDouble(price);
   

 %>

<body bgcolor="#ffffff" >
<form name="form" method="post">

	<input type="hidden" name="PRODUCTID" value="<%=productid%>" />
	<input type="hidden" name="PRODUCTID1" value="<%=product1%>" />
	<input type="hidden" name="PLANT" value="<%=plant%>" />
	<input type="hidden" name="INDEX" value="<%=iIndex%>" />
	<input type="hidden" name="COUNTER" value="<%=counter%>" />
	<input type="hidden" name="PAGENAME" value="mobileventreg" />
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
<tr><td colspan="3"></td>
</tr>
<!-- Removed logo Image -->
   <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"><a href="instrtnsummry.jsp?PLANT=<%=plant%>&PAGENAME=mobileenquiry" ><font size="12"  color="#669900"><b>INSTRUCTIONS</b></font></a></td>
   </tr>
   <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"></td>
   </tr>
</table>	
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
	<td></td>
		<td align="center" >
<img
	src="/track/ReadFileServlet/?fileLocation=<%=catalogPath%>" alt=""
	name="<%=productid%>" width="90%" id="<%=productid%>" />		
		</td>
		<td></td>
	</tr>
	<tr>

</table>
<table align="center" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="center">
		<td colspan="3"></td>
	</tr>
	
	<td ></td>
		<td align="center" class="mobilelabel">&nbsp;<font size="8"><b>&nbsp;<%=productid %></b>
		</font></td>
		<td ></td>
	</tr>
	
	<% if(dprice>0){ %>
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel">&nbsp;<font size="8"><b>Price:&nbsp;S$<%=decformat.format(dprice)%>/<%=uom%> </b>
		</font></td>
		<td ></td>
	</tr>
	<% }%>
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel">&nbsp;<font size="12"><b>
		<%=description1 %></b> </font></td>
	<td ></td>	
	</tr>
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel"><font size="8"> <%=description2%></font></td>
	 <td ></td>	
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel"><font size="8"><%=description3%></font></td>
	<td ></td>	
	</tr>
	
	<tr>
	<td align="left" ></td>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
      <td ></td>
      <td align="center" class="mobilelabel"><a  href="mobileDetails.jsp?PLANT=<%=plant%>&CATALOGID=<%=productid%>&PAGENAME=mobileenquiry" ><font size="8"  ><b><%=pageDetails%></b></font></a></td>
    
      <td ></td>
   </tr>
   <tr height="45px"><td align="left" ></td>
		<td colspan="2">&nbsp;</td></tr>
	
</table>
<table align="center" cellpadding="0" cellspacing="0" width="100%" bgcolor="#ffffff" height="33px">
	<tr valign="bottom" >
	<td align="left" valign="bottom">
		<INPUT
			class="mobileArrPNButton" type="BUTTON" value="<<"
			onClick="onFirst();"   align="top"
	
></td><td valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
			class="mobileArrButton" type="BUTTON" value="<" 
			onClick="onPrevious();" align="top"
	
></td>
		
		<td align="right" valign="bottom">&nbsp;&nbsp;&nbsp;
		<INPUT class="mobileArrButton" align="top"
			type="BUTTON" value=">" onClick="onNext();" 
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td align="right" valign="bottom">
<INPUT class="mobileArrPNButton" align="top"
			type="BUTTON" value=">>" onClick="onLast();" 
>
		</td></tr>
		<tr>
	<td colspan="4">
	&nbsp;
	</td>
	</tr>
	<tr>
	<td colspan="4">
	&nbsp;
	</td></tr>
				</table>
		
		<table>		
	<tr  width="40%" align="right" >
			<td align="center" ><INPUT
			class="mobileEventBtn" type="BUTTON" value="Registration"
			onClick="onEventReg();"
></td>	<td width="60%" align="center">		
			<INPUT	class="mobileEventBtn" type="BUTTON" value="Member Registration" onClick="onMemReg();" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td></tr>		
			</table>
	
</form>
</body>
</html>

