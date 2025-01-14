<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*" session="true"%>
<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@page import="com.track.tables.ITEMMST"%>
<%@page import="java.text.DecimalFormat"%><html>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<!-- Not in Use - Menus status 0 -->

<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
 </script>
<title> Point Of Sales Refund</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	//_userBean.setmLogger(mLogger);
	HashMap<String, String> loggerDetailsHasMap1 = new HashMap<String, String>();
	loggerDetailsHasMap1.put(MLogger.COMPANY_CODE, (String) session
 			.getAttribute("PLANT"));
	loggerDetailsHasMap1.put(MLogger.USER_CODE, StrUtils.fString(
 			(String) session.getAttribute("LOGIN_USER")).trim());
 	MLogger mLogger1 = new MLogger();
 	mLogger1.setLoggerConstans(loggerDetailsHasMap1);
 	DecimalFormat decformat = new DecimalFormat("#,##0.00");
    DecimalFormat decformaDiscount = new DecimalFormat("#,##0.0");
    DecimalFormat fltformat = new DecimalFormat("#,###");
 	float totalsum=0; String cursymbol ="";
	String fieldDesc="";
	Vector poslist=null;
	String PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",LOC="",BATCH="",disccnt="";
	String html = "",Stkqty="",CHKBATCH="",STOCKQTY="",gsttax="",discountDesc="",REMARKS="",REASONCODE="";
	float unitpc=0,totalpc=0; float gstf=0;
	int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;String unitprice="",totalprice="",cntlDiscount="";
	float gstvalCalc=0, msprice=0;
	PLANT = (String)session.getAttribute("PLANT");
	CHKBATCH = StrUtils.fString(request.getParameter("CHKBATCH"));
	REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
	String SumColor=""; 
	boolean flag=false;
	sb.setmLogger(mLogger1);
	String gst = sb.getGST("POS",PLANT);
	//System.out.print("Gst value"+gst);
	cursymbol = DbBean.CURRENCYSYMBOL;
	
	poslist = (Vector)session.getValue("posreflist");
	SCANQTY="1";
	if((String)session.getAttribute("REFUNDLOC")!=null)
		LOC= (String)session.getAttribute("REFUNDLOC");
	
	if((String)session.getAttribute("errmsgref")!=null)
		fieldDesc= (String)session.getAttribute("errmsgref");
    	Vector posReflist = (Vector)session.getValue("posreflist");
		ITEMMST items = new ITEMMST();
		Vector refundlist = new Vector();
		   gstf= Float.parseFloat(gst);
		   String ischecked = "";
		   if(CHKBATCH==null || CHKBATCH=="" ||CHKBATCH.equalsIgnoreCase("true")) 
				   {
			  		 ischecked = "checked";
				   }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="get" action="/track/RefundServlet?" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Point Of Sales Refund</font></TH>
    </TR>
  </TABLE>
  <div id="prdhidden"></div>
  <br>
  <input type="hidden" name="prdedit" value="edit" disabled="disabled">
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
 <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
  <TR valign="top">
        
         <TH ALIGN="right" width="7%">Location &nbsp;&nbsp;</TH>
		 <TD ALIGN="left" width="5%"><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="20"  MAXLENGTH=20 ></TD>
      <td ALIGN="left" valign="top" > <a href="#"
			onClick="javascript: popUpWin('list/locList.jsp?LOC_ID=' + form.LOC.value);"><img
			src="images/populate.gif" border="0"></a></td>               
             
          </TR>	
          <TR >
    <TH ALIGN="right" width="7%"> &nbsp;&nbsp;</TH>
		 <TD ALIGN="left" width="5%"></TD>
         
          </TR>	
 <TR>
		<TH ALIGN="right" width="7%">&nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="15%"><INPUT Type=Checkbox  style="border:0;" name = "chkbatch" id="chkbatch" value="chkbatch" onclick="DisplayBatch();" <%=ischecked%>>
                        &nbsp; Default NOBATCH</TD>
		<td></td>
	</TR>
         <TR BGCOLOR="#000066">
		<TH><font color="#ffffff" align="left" width="3%"><b>Chk</TH>
		<TH><font color="#ffffff" align="left" width="7%"><b>Product
		ID</TH>
		<TH><font color="#ffffff" align="left" width="19%"><b>Product
		Description</TH>
		<TH><font color="#ffffff" align="left" width="5%"><b>Batch</TH>
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
		<TD align="left" width="15%" class="textbold">&nbsp; <%=itemord.getBATCH()%></TD>
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
			onClick="javascript:popUpWinForDiscount('ItemDiscountList.jsp?checkvalues=<%=k%>&ITEM=<%=itemord.getITEM()%>&BATCH=<%=itemord.getBATCH()%>&CURRENTAMOUNT=<%=decformat.format(itemord.getUNITPRICE())%>&MINSPRICE=<%=decformat.format(itemord.getMINSPRICE()) %>&STOCKQTY=<%=itemord.getStkqty()%>&TYPE=POSREFUND');">
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
		<TD width="7%">
		</TD>
	</tr>
	<%}%>
</TABLE>
           <TABLE border="0" width="65%" cellspacing="0" cellpadding="0" align="center">
   <TR >
         <TH ALIGN="right" width="7%">Scan Product ID &nbsp;&nbsp;</TH>
	 <TD ALIGN="left" width="5%"><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return Itemkeypress()}"><a href="#" onClick="javascript:popUpWin('list/itemList.jsp?ITEM='+form.ITEM.value);" ><img src="images/populate.gif" border="0"></a></TD>
         <TH ALIGN="right" width="7%">Batch &nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="5%"><INPUT name="BATCH_0" id="BATCH_0" type="TEXT"
			value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){Batchkeypress();}"
			MAXLENGTH=40>
			<a href="#" onClick="javascript:popUpWin('list/OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEM.value+'&LOC0='+form.LOC.value+'&BATCH0='+form.BATCH_0.value+'&INDEX='+'0'+'&TYPE='+'POSREFUND');">
		<img src="images/populate.gif" border="0" /> </a></TD>
		<input type="hidden" name="QTY_0" value="">
         <TD width="1%" ALIGN="left" ><INPUT name="QTY" type = "TEXT" value="<%=SCANQTY%>" size="1"   ></td>
         <td ALIGN="center" width="2%" ><input type="Submit" name="action" value="Add" onclick="return addaction()"></td>
         <td ALIGN="left" width="2%" ><input   type="Submit" name="action" value="Delete" onclick="return delaction()"></td>
         <td ALIGN="left" width="3%" ><input   type="Submit" name="action" value="Refund" onclick="return refundaction()"></td>
         <input type="hidden" name="ITEMDESC" value="">
         <input type="hidden" name="totalsum" value='<%=totalsum%>'>
          </TD>
          <tr>
          <th></th>
          <td colspan="5"><br></td><br>
          </tr>
   <TR >
         <TH ALIGN="right" width="7%">Remarks &nbsp;&nbsp;</TH>
	<TD ALIGN="left" width="5%"><INPUT name="REMARKS" type = "TEXT" value="<%=REMARKS%>" size="20"  MAXLENGTH=50 ></TD>
         </TD>
  </TR>
          <TR>
		<TH WIDTH="7%" ALIGN="RIGHT">ReasonCode&nbsp;&nbsp;</TH>
		<TD colspan=5><INPUT name="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="50" MAXLENGTH=80 > 
                <a href="#" onClick="javascript:popUpWin('miscreasoncode.jsp?');"><img src="images/populate.gif" border="0" /> </a>
                </TD>
	</TR>
    </TABLE>
 
  </FORM>
  <script language="javascript">
  setfocus();
  function setfocus()
  {
	  document.form.ITEM.focus();
	  DisplayBatch();
  }
  var currentEnabled = null;
  function DisplayBatch()
  {
	  if(document.getElementById("chkbatch").checked == true){
	         document.getElementById("BATCH_0").value="NOBATCH";
	         document.getElementById("BATCH_0").readOnly = true;
	      
	    }
	    else{
	        	document.getElementById("BATCH_0").value="";
		         document.getElementById("BATCH_0").readOnly = false;
		    }
  }
function enablehidd()
{
	document.form.getElementById('prdhidden').innerHTML ="<input type='hidden' name='prdedit' value='edit'>";
	document.form.prdedit.disabled=false;
}
  
  function submitfm()
  {
	  var item=document.form.ITEM.value;
	  var loc = document.form.LOC.value;
	  var qty = document.form.QTY.value;
	  document.form.action="/track/DynamicProductServlet?action=Add";
      document.form.submit();
	  
  }
	    function Batchkeypress()
		  {
			  if(document.getElementById("chkbatch").checked == false){
				  if( document.form.BATCH_0.value.length>0)
				  {
					  document.getElementById("Add").focus();
				  }
				  
			  }
		  }
	    function Itemkeypress()
	    {
		   
	      if(document.getElementById("chkbatch").checked == true){
		  if( document.form.ITEM.value.length>0)
		  {
			  document.getElementById("Add").focus();
		  }
	      }
	      else{document.form.BATCH_0.focus();
			return false;
	      }   

	  }
 function addaction()
   {
	  	 
	  var item = document.form.ITEM.value;	 
	  if(item==null||item=="")
	  {
	  	  alert("Please Scan Product ID");
	  	  document.form.ITEM.focus();
	  	  return false;
	   }
	  if(document.getElementById("chkbatch").checked == false){

		  var batch = document.form.BATCH_0.value;	 
		  if(batch==null||batch=="")
		  {
			  alert("Please Scan Batch");
			  document.form.BATCH_0.focus();
			  return false;
		  } 

	  
	  }
	   
	 }
 //added by deen to check validation for delete and refund action
	function delaction()
	  {
	  	 	  	
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
	  	
	  }
	function refundaction(){
		
		  var item=document.form.chk;
		  if(item == undefined){
			 alert("Add Product to Refund");
			 return false;
		  }
		  
	  }
	  //end
	  
  </script>
<%@ include file="footer.jsp"%>