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

<title>Mobile Shopping Cart</title>
</head>
<link rel="stylesheet" href="css/style.css" type="text/css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script>
if (document.layers)
	  document.captureEvents(Event.KEYDOWN);
	  document.onkeydown =
	    function (evt) { 
		 
	      var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	      if (keyCode == 13)   //13 = the code for pressing ENTER 
	      {
	    	  onAddCart();
	      }
	    };
	function onAddCart() {		
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		var qty=document.form.QTY.value;
		var index= document.form.INDEX.value;
		var scanid= document.form.PRODUCTID1.value;
		var fqty = parseFloat(qty);
		var flag=true;
		if(qty=="" || qty.length==0 ) {
			flag=false;
			alert("Please Enter Quantity!");
			document.getElementById("QTY").focus();
			return false;
		}
		if(!isInteger(qty))
		{
			alert("Enter Valid Quantity");
			document.getElementById("QTY").focus();
		return false;
		}
		else{
			
			document.form.action = "/track/CatalogServlet?Submit=ADDTOCART&PLANT="
				+ plant + "&QTY=" + qty + "&PRODUCTID=" + productid+"&INDEX="+index+"&SCANID="+scanid;
	
		document.form.submit();
		}
		
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
		
	document.form.action = "mobileShopping.jsp?ID="+plant+delim+productid+delim+"N"+delim+index;	
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
		
                document.form.action = "mobileShopping.jsp?ID="+plant+delim+productid+delim+"N"+delim+previndex;
		document.form.submit();

		
	}
function onClear()
{
	document.form.QTY.value="";
}
function onFirst() {
	var plant = document.form.PLANT.value;
	var index = 0;
	var delim = "AAABBACCB";
	var productid = document.form.PRODUCTID1.value;						
	document.form.action = "mobileShopping.jsp?ID=" + plant + delim
			+ productid + delim +"N"+ delim + index;
	document.form.submit();
}
function onLast() {
	var plant = document.form.PLANT.value;
	var counter = document.form.COUNTER.value;
	//if(counter>0)
	//counter= counter-1;
	var index = 0;
	var delim = "AAABBACCB";
	var productid = document.form.PRODUCTID1.value;						
	document.form.action = "mobileShopping.jsp?ID=" + plant + delim
			+ productid + delim +"N"+ delim + counter;
	document.form.submit();
}

</script>
<% 
String loginuser ="",Qty="",enteredqty="",action="",productid="",plant="";
String catalogPath="",price="",description1="",product1="",description2="",description3="",uom="",strIndex="";
    int counter=0,nextCnt=0,prevCnt=0,iIndex=0;
    String detailsPage="";
action=request.getParameter("action");
System.out.println("action"+action);
CatalogUtil _catlogutil = new CatalogUtil();
List orderlist=null;
Vector<CATALOGMST> scanloglst = new Vector<CATALOGMST>();
if(((Vector)session.getAttribute("catloglst"))!=null)
	scanloglst = (Vector)session.getAttribute("catloglst");
if(((ArrayList)session.getAttribute("MBORDERLIST"))!=null){
	orderlist = (ArrayList)session.getAttribute("MBORDERLIST");
	orderlist.clear();
	session.setAttribute("MBORDERLIST",orderlist);}

session.setAttribute("catloglst",scanloglst);

String id = StrUtils.fString(request.getParameter("ID"));

System.out.println("id:::::::::::::::::::::::::::::::::"+id);
String delim="AAABBACCB";
String[] strarry = id.split(delim);
 String isQRPrdScanned="";

 plant = strarry[0];
 productid = strarry[1];
 try{
  isQRPrdScanned = strarry[2];
 }catch(Exception e){
 }
  product1 = productid;
  session.setAttribute(IDBConstants.PRODUCTID,productid);
  if(isQRPrdScanned.equalsIgnoreCase("Y")){
	
   session.setAttribute("PRODUCT_QR_SCANNED",productid);
  }
	List fullcatloglst=null;
  Hashtable ht = new Hashtable();
  ht.put(IDBConstants.PLANT,plant);
  ht.put(IDBConstants.PRODUCTID,productid);
  ht.put(IDBConstants.ISACTIVE,"Y");
  if(strarry.length>3)
  {
	  strIndex = strarry[3];
		iIndex = Integer.parseInt(strIndex);
  }

 		
   fullcatloglst=_catlogutil.listPrevNextCatalogs("distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISNULL(DETAILLABEL,'') DETAILLABEL,ISACTIVE ",ht," ");
  	
 
  	counter=fullcatloglst.size()-1;
System.out.println("countersize"+counter);  

System.out.println("Index counter"+iIndex);

	  Map catalogmap = (Map)fullcatloglst.get(iIndex);
	  catalogPath = (String)catalogmap.get(IDBConstants.CATLOGPATH);
	  productid=(String)catalogmap.get(IDBConstants.PRODUCTID);
	  price= (String)catalogmap.get(IDBConstants.CATLOGPRICE);
	  price = StrUtils.formattwodecNum(price);
	  description1=  (String)catalogmap.get(IDBConstants.DESCRIPTION1);
	  description2=  (String)catalogmap.get(IDBConstants.DESCRIPTION2);
	  description3=  (String)catalogmap.get(IDBConstants.DESCRIPTION3);
	  detailsPage = (String)catalogmap.get(IDBConstants.DETAILLABEL);	
	uom = new ItemMstDAO().getItemUOM(plant, productid);
 
 %>


<body bgcolor="#ffffff" >
<form name="form" method="post">

	<input type="hidden" name="PRODUCTID" value="<%=productid%>" />
	<input type="hidden" name="PRODUCTID1" value="<%=product1%>" />
	<input type="hidden" name="PLANT" value="<%=plant%>" />
	<input type="hidden" name="INDEX" value="<%=iIndex%>" />
	<input type="hidden" name="COUNTER" value="<%=counter%>" />
	
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
<tr><td colspan="3"></td>
</tr>
<!-- Removed logo Image -->
   <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"><a href="instrtnsummry.jsp?PLANT=<%=plant%>&PAGENAME=mobileshopping" ><font size="12"  color="#669900"><b>INSTRUCTIONS</b></font></a></td>
   </tr>
   <tr height="10px">
      <td width="left">&nbsp;</td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel">&nbsp;</td>
   </tr>
   <tr>
      <td width="left"></td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel"><a href="mobileOrderStatus.jsp?PLANT=<%=plant%>&PAGENAME=mobileshopping&SCANID=<%=product1%>&INDEX=<%=iIndex%>" ><font size="12"  color="#669900"><b>ORDER STATUS</b></font></a></td>
   </tr>
   <tr >
      <td width="left">&nbsp;</td>
      <td>&nbsp;</td>
      <td align="right" class="mobilelabel">&nbsp;</td>
   </tr>
</table>	
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
	<td></td>
		<td align="center" >
<img
	src="/track/ReadFileServlet/?fileLocation=<%=catalogPath%>" alt=""
	name="<%=productid%>" width="90%"  id="<%=productid%>"  />		
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
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel">&nbsp;<font size="8"><b>Price:&nbsp;S$<%=price%>/<%=uom%> </b>
		</font></td>
		<td ></td>
	</tr>
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
	<tr height="10px">
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
      <td align="center" class="mobilelabel"><a  href="mobileDetails.jsp?PLANT=<%=plant%>&CATALOGID=<%=productid%>&PAGENAME=mobileshopping" ><font size="8"  ><b><%=detailsPage%></b></font></a></td>
    
      <td ></td>
   </tr>
   <tr height="10px"><td align="left" class="mobilelabel"></td>
		<td colspan="2">&nbsp;</td></tr>
	<tr>
	<td ></td>
		<td align="center" class="mobilelabel"><font size="8">Quantity</font>
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="QTY" name="QTY" value="1" class="inactivemobileQty" maxlength="5" size="6" >
		
		</td>
	<td ></td>	
	</tr>
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>	
	<tr>
		<td colspan="3">&nbsp;&nbsp;&nbsp;</td>
	</tr>
	
	<tr valign="top">
	<td align="center"  valign="bottom" colspan="3">
		&nbsp;<INPUT
			class="mobileCartButton" type="BUTTON" value="Add to Cart"
			onClick="onAddCart();" align="top"
>
			</td></tr>
			<tr>
		<td colspan="3">&nbsp;&nbsp;</td>
	</tr>
	
	
</table>
<table align="center" cellpadding="0" cellspacing="0" width="100%" bgcolor="#ffffff" height="33px">
	<tr valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<td align="left" valign="bottom">
		<INPUT
			class="mobileArrPNButton" type="BUTTON" value="<<"
			onClick="onFirst();"   align="top"
	
></td><td valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
			class="mobileArrButton" type="BUTTON" value="<" 
			onClick="onPrevious();" align="top"
	
></td>
		<td align="center"></td>
		<td align="right" valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="mobileArrButton" align="top"
			type="BUTTON" value=">" onClick="onNext();" 
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td align="right" valign="bottom">
<INPUT class="mobileArrPNButton" align="top"
			type="BUTTON" value=">>" onClick="onLast();" 
>
		</td>
	</tr>
	</table>
	
</form>
</body>
</html>

<script>
function onAddCart112()
{
	var qty = document.form.QTY.value;
	if(qty=="" || qty.length==0 ) {
		alert("Enter Quantity!");
		document.getElementById("QTY").focus();
	}

	var urlStr = "/track/CatalogServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		Submit : "ADDTOCART",
		QTY:qty,
		PRODUCTID:"<%=productid%>"
			
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "100") {
			var resultVal = data.result;
			document.form.QTY.value="";
		} else {
			
		}
	}
});	
}
</script>