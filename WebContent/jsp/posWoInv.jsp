<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="java.util.*" session="true"%>


<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%><html>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<!-- Not in Use - Menus status 0 -->
<head>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<script language="javascript">
 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
</script>

<script language="javascript">
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
</script>

<title>Point Of Sales</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	 StrUtils strUtils     = new StrUtils();
	 Generator generator   = new Generator();
	 userBean _userBean      = new userBean();
	 ITEMMST items = new ITEMMST();
	 String btnString="";
	 //_userBean.setmLogger(mLogger);
	 HashMap<String, String> loggerDetailsHasMap1 = new HashMap<String, String>();
	 loggerDetailsHasMap1.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	 loggerDetailsHasMap1.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
 	 MLogger mLogger1 = new MLogger();
     DecimalFormat decformat = new DecimalFormat("#,##0.00");
     DecimalFormat decformaDiscount = new DecimalFormat("#,##0.0");
     DecimalFormat fltformat = new DecimalFormat("#,###");
 	 mLogger1.setLoggerConstans(loggerDetailsHasMap1);
 	 
	
	
	String fieldDesc="",cursymbol="",DISCITEM="",discountDesc="",cmd="";
	String PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",LOC="",  disccnt="",STOCKQTY="",REMARKS="",gsttax="",action="";
	String html = "";float gstf=0;
	int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;String unitprice="",totalprice="",cntlDiscount="";
	PLANT = (String)session.getAttribute("PLANT");
	String SumColor=""; 
	 Vector poslist=null;
	boolean flag=false;
	sb.setmLogger(mLogger1);
	String gst = sb.getGST("POS",PLANT);
	cursymbol = DbBean.CURRENCYSYMBOL;
	float unitpc=0,totalpc=0,  gstvalCalc=0, totalsum=0,msprice=0;
	action = StrUtils.fString(request.getParameter("action")).trim();
	String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
    REMARKS = StrUtils.fString(request.getParameter("REMARKS")).trim();
   
    
  
	
	if(sTranId.length()>0){
	 poslist = (Vector)session.getValue("poslist");
	 session.setAttribute("tranid",sTranId);
        }
	SCANQTY="1";
	if((String)session.getAttribute("LOCATION")!=null)
	{
		LOC= (String)session.getAttribute("LOCATION");
	}
	
	if((String)session.getAttribute("errmsg")!=null)
	{
	   fieldDesc= (String)session.getAttribute("errmsg");
	   gstf = Float.parseFloat(gst);
	}
	
		
	if(sTranId.length()>0 ){
		
	 	discountDesc=(String)request.getSession().getAttribute("RESULTTOTALDISCOUNT");
	}
	else
	{
		request.getSession().setAttribute("RESULTTOTALDISCOUNT", "");   
		discountDesc="";
	}
      
%>
<%@ include file="body.jsp"%>

<FORM name="form" method="get" action="/track/DynamicProductServlet?">
<br>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Point
		Of Sales</font></TH>
	</TR>
</TABLE>

<br>
<center>
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<font class="mainred"> <%=fieldDesc%></font>
</table>
</center>
<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
<INPUT name="POS_TYPE" type="hidden" value="NO_INV_CHECK" size="20" MAXLENGTH=50 >
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding="2"
	align="center">
	<TR valign="top">
		<TH ALIGN="right" width="7%" valign="top">Location &nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="10%" valign="top"><INPUT name="LOC"
			type="TEXT" value="<%=LOC%>" size="20" MAXLENGTH=20></TD>
		<td ALIGN="left" valign="top"><a href="#"
			onClick="javascript:popUpWin('list/locList.jsp?LOC_ID='+ form.LOC.value);"><img
			src="images/populate.gif" border="0"></a></td>

		<TH ALIGN="RIGHT" width="7%">Tran Id :</TH>
		<TD ALIGN="left" width="10%"><INPUT name="TRANID" type="TEXT"
			value="<%=sTranId%>" size="20" MAXLENGTH=50></TD>
		<td ALIGN="left" valign="top"><a href="#"
			onClick="javascript:popUpWin('list/posTranIDList.jsp?');"> <img
			src="images/populate.gif" border="0" /> </a></TD>
		<TD><INPUT class="Submit" type="button" name="action"
			value="View" onClick="onView();"></TD>
		<TD><INPUT class="Submit" type="button" name="action" value="New"
			onClick="onGenID();"></TD>
		<TD><INPUT class="Submit" type="button" name="action"
			value="Clear" onClick="onNewPOS();">&nbsp;&nbsp;</TD>
	</TR>

	<TR>
		<TH ALIGN="right" width="7%">&nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="15%"></TD>
		<td></td>
	</TR>


	<TR BGCOLOR="#000066">
		<TH><font color="#ffffff" align="left" width="3%"><b>Chk</TH>
		<TH><font color="#ffffff" align="left" width="7%"><b>Product
		ID</TH>
		<TH><font color="#ffffff" align="left" width="19%"><b>Product
		Description</TH>
		<TH><font color="#ffffff" align="left" width="5%"><b>UP</TH>
		<TH><font color="#ffffff" align="left" width="5%"><b>Discount(%)</TH>
		<TH><font color="#ffffff" align="left" width="5%"><b>Qty</TH>
		<TH><font color="#ffffff" align="left" width="7%"><b>Price</TH>
		<TH><font color="#ffffff" align="left" width="8%"><b>Tax</TH>
		<TH><font color="#ffffff" align="left" width="13%"><b>Total
		Price</TH>


	</TR>
	<% if(poslist!=null && (poslist.size()>0)){
    	  
    	 
    	
         for(int k=0;k<poslist.size();k++)
         {
        	 pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         sumSubTotal = sumSubTotal+itemord.getTotalPrice();
	         sumSubTotal = strUtils.Round(sumSubTotal,2);
	         unitpc = itemord.getUNITPRICE();
	        
	         unitpc = strUtils.Round(unitpc,2);
	         
	         msprice = strUtils.Round(itemord.getMINSPRICE(),2);
	    
	         totalpc = strUtils.Round(itemord.getTotalPrice(),2);
	         disccnt=String.valueOf(itemord.getDISCOUNT()).trim();
	         float fdisccnt=Float.parseFloat(disccnt);
	      
			 STOCKQTY = String.valueOf(itemord.getStkqty());
			 STOCKQTY = StrUtils.formatNum(STOCKQTY);
			 
			 
			 gsttax=String.valueOf(itemord.getGSTTAX()).trim();	
			 pcgsttax=itemord.getPRICEWITHTAX();
			 sumGsttax=sumGsttax+ pcgsttax;
			 
			 
			 cntlDiscount=itemord.getITEM();
			 gstvalCalc=Float.parseFloat(gst);
			
			
			// chkString  =dono+","
			 if(pcgsttax<=0)
			 {
				 float calgsttax=(itemord.getGSTTAX()/100);
				 float taxper=totalpc*calgsttax;
				 pcgsttax=totalpc+taxper;
				
				 sumGsttax=sumGsttax+ pcgsttax;
			 }
         %>
	<TR bgcolor="">
		<TD align="left" width="3%">&nbsp;<font class="textbold"><input
			type="checkbox" name="chk" value="<%=k%>"></input></TD>
		<TD align="left" width="7%">&nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
		<TD align="left" width="19%" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>
		<TD align="right" width="5%" class="textbold">&nbsp;<%=cursymbol%>
		<%if(unitpc==0){%>0.00<%}else{%><%=decformat.format(unitpc)%>
		<%}%>
		</TD>
		<TD align="right" width="3%">&nbsp;<font class="textbold"><%=decformaDiscount.format(fdisccnt)%></TD>


		<TD align="right" width="6%" class="textbold">&nbsp;<%=STOCKQTY%>
		</TD>

		<TD align="right" width="7%" class="textbold">&nbsp;<%=cursymbol%>
		<%if(totalpc==0){%>0.00<%}else{%><%=decformat.format(totalpc)%>
		<%}%>
		</TD>
		<TD align="right" width="6%" class="textbold">&nbsp;<%=gsttax%></TD>
		<TD align="right" width="14%" class="textbold"><%=cursymbol%>
		<%if(pcgsttax==0){%>0.00<%}else{%><%=decformat.format(pcgsttax)%>
		<%}%>
		</TD>
		<TD align="right" width="4%" class="textbold"><INPUT
			type="button" name="btn_<%=k%>" value="Discount"
			onClick="javascript:popUpWinForDiscount('ItemDiscountList.jsp?checkvalues=<%=k%>&ITEM=<%=itemord.getITEM()%>&CURRENTAMOUNT=<%=decformat.format(itemord.getUNITPRICE())%>&MINSPRICE=<%=decformat.format(itemord.getMINSPRICE()) %>&STOCKQTY=<%=itemord.getStkqty()%>');">
		</TD>


	</TR>
	<%
                 
         }} %>
	  <tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" width="14%" class="textbold">&nbsp;<b>SUBTOTAL</b>
		</TD>
		<TD align="right" class="textbold">&nbsp; <%=cursymbol%>
		<%if(sumSubTotal==0){%>0.00<%}else{%><%=decformat.format(sumSubTotal)%>
		<%}%>
		</TD>
	</tr>
	
	
	<% 
		    	
		    	if(sumSubTotal>0 && (String)session.getAttribute("TOTALDISCOUNT")!=null &&  (String)session.getAttribute("TOTALSUBTOTAL")!=null &&  (String)session.getAttribute("TOTALTAX")!=null && !session.getAttribute("TOTALDISCOUNT").equals("") &&  !session.getAttribute("TOTALSUBTOTAL").equals("") &&  !session.getAttribute("TOTALTAX").equals(""))
		    		
		    	{
			    	float totalDiscount=Float.parseFloat((String)session.getAttribute("TOTALDISCOUNT"));
		    		float totalSubTotal=Float.parseFloat((String)session.getAttribute("TOTALSUBTOTAL"));
		    		float totalTax=Float.parseFloat((String)session.getAttribute("TOTALTAX"));
		    		totalsum = totalSubTotal+totalSubTotal*gstvalCalc/100;
	    	    	totalsum = strUtils.Round(totalsum,2); 
		    	   %>
	<%=discountDesc%>

	<tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" class="textbold">&nbsp;<b>TAX</b></TD>
		<TD align="right" class="textbold">&nbsp;<%if(totalTax==0){%>0.00<%}else{%><%=decformat.format(totalTax)%>
		<%}%>
		</TD>

	</tr>

	<tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" class="textbold">&nbsp;<b>TOTAL<b /></TD>

		<TD align="right" class="textbold">&nbsp;<%=cursymbol%>
		<%if(totalDiscount ==0){%>0.00<%}else{%><%=decformat.format(totalDiscount)%>
		<%}%>
		</TD>
		<input type="hidden" name="hiddentotal" value="<%=totalDiscount%>">
		<input type="hidden" name="hiddensubtotal" value="<%=totalSubTotal%>">
		<input type="hidden" name="hiddentax" value="<%=gstvalCalc%>">
		&nbsp;
		<TD width="7%"><INPUT type="button" size="4" align="centert"
			name=totaldiscount value="Discount" 
			onClick="javascript:popUpWinForDiscount('totalDiscountList.jsp?TOTAL='+form.hiddentotal.value+'&SUBTOTAL='+form.hiddensubtotal.value+'&TAX='+form.hiddentax.value);">
		</TD>
	</tr>
	<% }else{
	    	    	         totalGsttax= sumSubTotal*gstvalCalc/100;
	    	    	       
	    	          %>


	<tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" class="textbold">&nbsp;<b>TAX</b></TD>
		<TD align="right" class="textbold">&nbsp;<%if(totalGsttax==0){%>0.00<%}else{%><%=decformat.format(totalGsttax)%>
		<%}%>
		</TD>
	</tr>
	<%  
						 	  String total=""; 
			    	    	  float gstval=Float.parseFloat(gst);
			    	    	  totalsum = sumSubTotal + sumSubTotal*gstval/100;
			    	    	  totalsum = strUtils.Round(totalsum,2); 
			    	    	  session.setAttribute("totalSum",String.valueOf(totalsum));
			    	    	  session.setAttribute("sumSubTotal",String.valueOf(sumSubTotal));
			    	    	  session.setAttribute("gsttax",String.valueOf(sumSubTotal*gstval/100));
			    	     %>
	<tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" class="textbold">&nbsp;<b>TOTAL<b /></TD>
		<TD align="right" class="textbold">&nbsp;<%=cursymbol%>
		<%if(totalsum==0){%>0.00<%}else{%><%=decformat.format(totalsum)%>
		<%}%>
		</TD>
		<input type="hidden" name="hiddentotal"
			value="<%=decformat.format(totalsum)%>">
		<input type="hidden" name="hiddensubtotal"
			value="<%=decformat.format(sumSubTotal)%>">
		<input type="hidden" name="hiddentax" value="<%=gstvalCalc%>">
		&nbsp;
		<TD width="7%"><!--<INPUT type="button" size="4" align="centert"
			name=totaldiscount value="Discount"
			onClick="javascript:popUpWinForDiscount('totalDiscountList.jsp?TOTAL='+form.hiddentotal.value+'&SUBTOTAL='+form.hiddensubtotal.value+'&TAX='+form.hiddentax.value);">
-->
		</TD>
	</tr>
	<%}%>
</TABLE>
<TABLE border="0" width="60%" cellspacing="0" cellpadding="0"
	align="center">
	<TR>
		<TH ALIGN="right" width="7%">Scan Product ID &nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="5%"><INPUT name="ITEM" type="TEXT"
			value="<%=ITEM%>" size="20" MAXLENGTH=50><a href="#"
			onClick="javascript:popUpWin('list/itemList.jsp?ITEM='+form.ITEM.value);"><img
			src="images/populate.gif" border="0"></a></TD>
		<TD width="3%" ALIGN="left"><INPUT name="QTY" type="TEXT"
			value="<%=SCANQTY%>" size="1" maxlength="10"></td>
		<td ALIGN="left" width="3%">
		<div id="add"><input type="Submit" name="action" value="Add" 
		onclick="return addaction()"></div>
		<input type="hidden" name="action1" value="temp"></td>
		<td ALIGN="left" width="4%"><input type="Submit" name="action"
			value="Delete" onclick="return delaction()"></td>
		<td ALIGN="left" width="3%"><input type="button" name="action"
			value="Print" onclick="return printaction()"></td>
		<td ALIGN="left" width="3%"><input type="Submit" name="action"
			value="Hold" onclick="return holdaction()"></td>
		<input type="hidden" name="ITEMDESC" value="">
		<input type="hidden" name="DISCITEM" value="" />

		<tr>
			<th></th>
			<td colspan="6"><br>
			</td>
			<br>
		</tr>
</table>
<!-- <TABLE border="0" width="75%" cellspacing="0" cellpadding="0"
	align="center">
	<TR>
		<TH ALIGN="right" width="10%">Payment Mode &nbsp;&nbsp;</TH>


		<TD ALIGN="left" colspan=2 width="30%"><INPUT name="payment"
			type="radio" value="Cash" onclick="enabletb();">&nbsp;<img
			border="0" src="images/cash.png" height=25 width=30>Cash
		&nbsp;&nbsp; &nbsp;&nbsp; <INPUT name="payment" type="radio"
			value="Nets" onclick="enabletb();">&nbsp;<img border="0"
			src="images/nets.bmp" height=30 width=30>&nbsp;Nets
		&nbsp;&nbsp; &nbsp;&nbsp; <INPUT name="payment" type="radio"
			value="Meps" onclick="enabletb();">&nbsp;<img border="0"
			src="images/meps.jpg" height=30 width=30>&nbsp;Meps&nbsp;&nbsp; </td>

		<TD ALIGN="left" colspan=2 width="24%"><INPUT name="payment"
			type="radio" value="Credit Card" onclick="enabletb();">&nbsp;<img
			border="0" src="images/creditcard.bmp" height=30 width=180>&nbsp;&nbsp;Credit
		Card</TD>
		<td ALIGN="center" width="8%"><INPUT name="payment" type="radio"
			value="Other" onclick="enabletb();">Others</TD>
		<td ALIGN="left" width="10%"><INPUT name="paymentmode"
			id="paymentmode" type="TEXT" value=""></td>


	</TR>
	<tr>
		<th></th>
		<td colspan="5"><br>
		</td>
		<br>
	</tr>
	<TR>
		<TH ALIGN="right" width="7%">Remarks &nbsp;&nbsp;</TH>
		<TD ALIGN="left" colspan="5"><INPUT name="REMARKS" type="TEXT"
			value="<%=REMARKS%>" size="20" MAXLENGTH=50></TD>


	</TR>
</TABLE>

<table id="cashtb">
	<tr>
		<td colspan="5">&nbsp;</td>
	</tr>
</table>-->

<br></br>
<table border = "0" width = "50%" cellspacing="0" cellpadding="0" align="center">
	<tr><td>
<input type = "checkbox" name = "multi_payment" value = "multi_payment" onClick = "setDefault();"><b>Multi mode payment</b>
</td>
<TH ALIGN="right">Remarks &nbsp;&nbsp;</TH>
<td>
<INPUT name="REMARKS" type="TEXT" value="<%=REMARKS%>" size="20" MAXLENGTH=50></TD>
</tr>
</table>
<br></br>
<table style="border: 1px solid black;" width = "50%"  align="center">
<tr><td><b>Payment Mode :</b></td>
</tr>
<tr>
<td ><input type = "checkbox" name = "paymentMode1" onclick = "toggle(this,'amt1');" >&nbsp;&nbsp;<img
			border="0" src="images/cash.png" height=30 width=35>&nbsp;&nbsp;Cash</td>
<td><input type = "text" name = 'amt1' id = 'amt1' style="display:none;" onchange = "calculate(this);"></input></td>
</tr>
<tr>
	<td ><input type = "checkbox" name = "paymentMode2" onclick = "toggle(this,'amt2');">&nbsp;&nbsp; <img border="0"
			src="images/nets.bmp" height=35 width=35>&nbsp;&nbsp;Nets</td>
	<td ><input type = "text" name = 'amt2' id = 'amt2' style="display:none;" onchange = "calculate(this);"></input></td>
</tr>
<tr>
<td ><input type = "checkbox" name = "paymentMode3"  onclick = "toggle(this,'amt3');">&nbsp;&nbsp;<img border="0"
			src="images/meps.jpg" height=35 width=35>&nbsp;&nbsp;Meps </td>
<td ><input type = "text" name = 'amt3' id = 'amt3' style="display:none;" onchange = "calculate(this);"></input></td>
</tr>
<tr>
<td><input type = "checkbox" name = "paymentMode4" onclick = "toggle(this,'amt4');">&nbsp;&nbsp;<img
			border="0" src="images/creditcard.bmp" height=35 width=185>&nbsp;&nbsp;Credit Card </td>
<td ><input type = "text" name = 'amt4' id = 'amt4' style="display:none;" onchange = "calculate(this);"></input></td>
</tr>
<tr>
<td><input type = "checkbox" name = "paymentMode5"  onclick = "toggle(this,'amt5');">&nbsp;&nbsp;Voucher&nbsp;&nbsp;</td>
<td ><input type = "text" name = 'amt5' id = 'amt5' style="display:none;" onchange = "calculate(this);"></input></td>
</tr>
<tr>
<td><input type = "checkbox" name = "paymentMode6" onclick = "toggle(this,'amt6');">
&nbsp;&nbsp;Others&nbsp;&nbsp;&nbsp;<input type = "text" id = "otherMode" name = "otherMode"></input></td>
<td ><input type = "text" name = 'amt6' id = 'amt6' style="display:none;" onchange = "calculate(this);"></input>
<input type = "hidden" name = 'paymentmodes' id = 'paymentmodes' ></input>
<input type = "hidden" name = 'amountsToPrint' id = 'amountsToPrint'></input>
<input type = "hidden" name = 'amountsToSave' id = 'amountsToSave' ></input>

</td>
</tr>
</table>
<br></br>
<table id = "resultPaymentTable" style="display:none;" border = "0" width = "50%" cellspacing="0" cellpadding="0" align="center">
<tr>
<td><b>Total :</b></td>
<td><input type = "text" name = 'Total'   id = 'Total' value = "0" ></input></td>
</tr>
<tr>
<td><b>Balance :</b></td>
<td><input type = "text" name = 'balance'   id = 'balance'  value = "0" ></input></td>
</tr>

</table>

</FORM>

<script language="javascript">
  setfocus();var index=0;
  if( document.form.ITEM.value.length>0)
	{
			alert("setting focus");
		 
	  }
  function setfocus()
  {	 
	  document.form.ITEM.focus();
	  if( document.form.ITEM.value.length>0)
	  {
		  document.getElementById("Add").focus();
	  }
          //document.form.paymentmode.disabled=true;

  }
//  var currentEnabled = null;


  /*function enabletb()
  {
	  var val = 0;
	  for( i = 0; i < document.form.payment.length; i++ )
	  {
	  if( document.form.payment[i].checked == true )
	  {
	  val = document.form.payment[i].value;
	  if(val=='Other')
	  {
            
	  document.form.paymentmode.disabled=false;
	  document.form.paymentmode.focus();
         // document.getElementById("paymentmode").style.display= "block";

	  }
          if(val=='Cash')
	  {
            if(index==0)
	  addRow('cashtb');
         
	  }
	  else
	  {
         deleteRow('cashtb');
	  
	  }
	  }
	  }

  }*/
  function submitfm()
  {
	  var item=document.form.ITEM.value;
	  var loc = document.form.LOC.value;
	  var qty = document.form.QTY.value;
	  //document.form.action="/track/DynamicProductServlet?action=Add&ITEM="+item+"&LOC="+loc+"&QTY="+qty;
	   document.form.action="/track/DynamicProductServlet?action=Add";
           
      document.form.submit();
	  
  }
 
  function setPriceaftDisc(discnt,item,unitprice,qty)
  {
	     document.form.DISCITEM.value=item;
	     document.form.ITEM.value =item;
	     document.form.action1.value ="Discount";
         document.form.submit();
  }
  
  
 
  
  /*function displaybal()
  {
	  var cashamt=document.form.CASHAMOUNT.value;
      var totalamt ='<%=totalsum%>';
	  var bal =cashamt-totalamt;
	  bal= Math.round(bal*100)/100;
	  document.form.BALAMOUNT.value=bal;
  }*/
  
  
  function addaction()
  {
	 
	  var item = document.form.ITEM.value;	 
	  if(item==null||item=="")
	  {
		  alert("Please Scan Product ID");
		  document.form.ITEM.focus();
		  return false;
	  }
	
	/*  else{
	  document.form.action="/track/DynamicProductServlet?action=Add";
	  document.form.submit();
	  
	  }*/
  }
   function delaction()
  {
	 
	/*  var item = document.form.chk.value;	 commented by deen
	  if(item==null||item=="")
	  {
		  alert("Please Select Any Check box");
		  return false;
	  }
	  else{
		  document.form.action="/track/DynamicProductServlet?action=Delete";
		  document.form.submit();}*/
	//added by deen for validate delete action	  
	  var item = document.form.chk;
	  var checkflag = false;
	  if(item != undefined){
		 if(item.length == undefined && item.checked){
		 	 checkflag = true;	  
	    	}
	  	 else if(item.length > 0){
		 	 for(i=0;i<item.length;i++){
			 	 if(item[i].checked){
				  	checkflag = true;	 
			  	}
		 	 }
	       } 
	  }
	  if(!checkflag){
			alert("Must Select at leat one Product which u want to Delete");
			return false;
		}
	 /* document.form.action="/track/DynamicProductServlet?action=Delete";
	  document.form.submit();*/
	  }     //end
  //added by deen to validate print and hold action	  
  function printaction(){
	var item=document.form.chk;
	  if(item == undefined){
		 alert("Add Product to Print");
		 return false;
	  }
	  
	  var paymentModes = new Array();
		var amountsToPrint = new Array();
		var amountsToSave = new Array();
		 var totalamt ='<%=totalsum%>';
		 var bal = "";
		var j = 0;
		var k = 0;
		var paymentModesSelected = getPaymentModeCount();
		var balance = document.getElementById('balance').value;
		var totalPaid = document.getElementById('Total').value;
		if(paymentModesSelected != 2 && document.getElementById('multi_payment').checked){
				alert("Please select 2 payment Modes");
				return false;
		} 
		if(paymentModesSelected != 1 && !document.getElementById('multi_payment').checked){
			alert("Please select One payment Mode");
			return false;
		}
			
		for(i =1;i<=6;i++){
			if(paymentModesSelected !=1 && document.getElementById('paymentMode'+i).checked){
				var amount = document.getElementById('amt'+i).value;
				if(amount == "0" || amount == ""){
					alert("Key in the value");
					return false;
				}
				paymentModes[j++] = 'paymentMode'+i;
				amountsToPrint[k++] = amount;
				 
			}
			else if(paymentModesSelected == 1 && document.getElementById('paymentMode'+i).checked){
				if(i==1){
					var amount = document.getElementById('amt'+i).value;
					paymentModes[j++] = 'paymentMode'+i;
					amountsToPrint[k++] = amount;
				}
				else{
					paymentModes[j++] = 'paymentMode'+i;
					amountsToPrint[k++] = totalamt;
				}
			}
		}
				
				if(paymentModesSelected == 1){
					if(contains (paymentModes,'paymentMode1')){
						if(Number(amountsToPrint[0]) < Number(totalamt)){
							alert("Please pay the amount correctly.");
							return false;

						}
						else{
							bal = 	Number(amountsToPrint[0]) + Number(balance);
							amountsToSave[0] = Math.round(bal*100)/100;
						}

					}
					else{
						amountsToSave[0] = amountsToPrint[0];

					}


				}
				else{
					if(contains (paymentModes,'paymentMode1')){

						if( Number(totalPaid) < Number(totalamt)){
							alert("Please pay the amount correctly.");
							return false;

						}
						else if(Number(totalPaid) > Number(totalamt)){	
								
							bal = 	Number(amountsToPrint[0]) + Number(balance);		
							amountsToSave[0] = Math.round(bal*100)/100;
							amountsToSave[1] = amountsToPrint[1];
						}
						
					}
					
					else{
						if(totalPaid != totalamt){
							alert("please pay correctly");
							return false;
						}
						else{
							amountsToSave[0] = amountsToPrint[0];
							amountsToSave[1] = amountsToPrint[1];

						}
						
					}


				}

				getPaymentModes(paymentModes);
				document.getElementById('paymentmodes').value = paymentModes;
				document.getElementById('amountsToSave').value = amountsToSave;
				document.getElementById('amountsToPrint').value = amountsToPrint;
				
	         document.form.cmd.value="print" ;
		 document.form.submit();
	
	  
  }

  function toggle(chkbox, group) { 
		var multiModeChecked = document.getElementById('multi_payment').checked;

		var paymentModesSelected = getPaymentModeCount();
		

		if(!multiModeChecked){
			if(paymentModesSelected >1){
				chkbox.checked = false;
				alert("Cannot choose more than 1 payment mode");
						return false;
				
			}
			if(chkbox.name == 'paymentMode1'){
				if(chkbox.checked){
					showObject(group);
					showObject('resultPaymentTable');
				}
				else{
					hideObject(group);
					document.getElementById(group).value =0;
					hideObject('resultPaymentTable');
				}

			}
			
		}
		else{
			if(paymentModesSelected >2){
				chkbox.checked = false;
				alert("Cannot choose more than 2 payment modes");
						return false;
			}
			if(chkbox.checked){
				showObject(group);
			}
			else{
				hideObject(group);
				document.getElementById(group).value =0;
			}
			showObject('resultPaymentTable');
		}
			
	        
		
	}

	function showObject(obj){
		document.getElementById(obj).style.display = "inline";
	}
	function hideObject(obj){
		document.getElementById(obj).style.display = "none";
	}

	function getPaymentModeCount(){
		var count = 0;
		for(i =1;i<=6;i++){
			if(document.getElementById('paymentMode'+i).checked)
				count ++;
		}
		return count;
	}

	function setDefault(){

		for(i =1;i<=6;i++){
			document.getElementById('paymentMode'+i).checked = false;
			hideObject('amt'+i);
			document.getElementById('amt'+i).value =0;
		}
		hideObject('resultPaymentTable');
		document.getElementById('balance').value ="";
		document.getElementById('Total').value ="";
		
	}

	function getTotalAmount(){
		var amt = 0;
		for(i =1;i<=6;i++){
			amt = Number(amt) + Number(document.getElementById('amt'+i).value);
				
		}
		return amt;
	}
	function calculate(obj){	
		 var totalCost ='<%=totalsum%>';
		totalAmt = getTotalAmount();
		totalAmt = Math.round(totalAmt*100)/100;
		document.getElementById('Total').value = totalAmt;
		var bal = totalCost - totalAmt;
		bal= Math.round(bal*100)/100;
		document.getElementById('balance').value = bal;	
	}

	function contains(arr, findValue) {
	    var i = arr.length;
	     
	    while (i--) {
	        if (arr[i] === findValue) return true;
	    }
	    return false;
	}
	function getPaymentModes(paymentModes){

		for(i in paymentModes){
			var mode = paymentModes[i];
			switch(mode)
			{
			case 'paymentMode1':
				paymentModes[i] = 'Cash';
				break;
			case 'paymentMode2':
				paymentModes[i] = 'Nets';
				break;
			case 'paymentMode3':
				paymentModes[i] = 'Meps';
				break;
			case 'paymentMode4':
				paymentModes[i] = 'Credit Card';
				break;
			case 'paymentMode5':
				paymentModes[i] = 'Voucher';
				break;
			case 'paymentMode6':
				paymentModes[i] = document.getElementById("otherMode").value;
				break;
			

		}
		}

		
	}
function holdaction(){
	 	 
	  var item=document.form.chk;
	  if(item == undefined){
		  alert("Add Product to Hold the Transaction");
		  return false;
	  }
	  
  }
  //end
  /*function addRow(tableID) {
   
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
      
	var row = table.insertRow(rowCount);
	//var firstCell = row.insertCell(0);
	//firstCell.innerHTML = "";
        if(rowCount==1)
        {
        var firstCell = row.insertCell(0);
	firstCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        var newemptyCell = row.insertCell(1);
	newemptyCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	var emptyCell = row.insertCell(2);
	emptyCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	var secondCell = row.insertCell(3);
	secondCell.innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Cash Amount : </b><INPUT name=\"CASHAMOUNT\" id=\"CASHAMOUNT\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" onkeyup=\"displaybal();\" \><b></b> ";
        
         var thirdCell = row.insertCell(4);
	thirdCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        var fourthCell = row.insertCell(5);
	
	fourthCell.innerHTML="<b>Balance : </b><INPUT name=\"BALAMOUNT\" id=\"BALAMOUNT\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"10\" \> <b></b> ";
        }
        
}*/

function onGenID()
	{
       var loc = document.form.LOC.value;	 
	  if(loc==null||loc=="")
	  {
		  alert("Please Scan Location");
		  return false;
	  }else{
         document.form.cmd.value="New" ;
		 document.form.submit();}
	}

function onNewPOS()
{    
     document.form.TRANID.value="";
     document.form.LOC.value="";
	
	 document.form.cmd.value="NewPOS" ;
     document.form.submit();
}

  function onView()
	{
       var tranid = document.form.TRANID.value;	 	
	  if(tranid==null||tranid=="")
	  {
		  alert("Please Select TranID");
		  return false;
	  }else{
          
		document.form.cmd.value="View" ;
	    document.form.submit();}
	}

  
	

	/*function deleteRow(tableID) {
	 
			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;
			rowCount = rowCount * 1 - 1;

			if (rowCount == 1) {
				table.deleteRow(rowCount);}
			
	}*/

</script>
<%@ include file="footer.jsp"%>