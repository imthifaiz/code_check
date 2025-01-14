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
 </script>
<title> Point Of Sales</title>
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
 	float totalsum=0; String cursymbol ="";
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",LOC="";
	String html = "",Stkqty="";
	float unitpc=0,totalpc=0; float gstf=0;
	int Total=0; float sumSubTotal=0;
	PLANT = (String)session.getAttribute("PLANT");
	String SumColor=""; 
	boolean flag=false;String REMARKS="";
	sb.setmLogger(mLogger1);
	String gst = sb.getGST("POS",PLANT);
	//System.out.print("Gst value"+gst);
	cursymbol = DbBean.CURRENCYSYMBOL;
	
	Vector poslist = (Vector)session.getValue("posreflist");
	//ITEM = request.getParameter("ITEM");
	//if(request.getParameter("QTY")!=null||request.getParameter("QTY")=="")
	SCANQTY="1";
	if((String)session.getAttribute("REFUNDLOC")!=null)
		LOC= (String)session.getAttribute("REFUNDLOC");
	
	if((String)session.getAttribute("errmsgref")!=null)
		fieldDesc= (String)session.getAttribute("errmsgref");
	      //  System.out.println(request.getParameter("prdedit")+"hidden element");
	
		
		
		Vector posReflist = (Vector)session.getValue("posreflist");
		ITEMMST items = new ITEMMST();
		Vector refundlist = new Vector();
		   gstf= Float.parseFloat(gst);

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/RefundServlet?" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Point Of Sales Refund</font></TH>
    </TR>
  </TABLE>
  <div id="prdhidden"></div>
  <br>
  <input type="hidden" name="prdedit" value="edit" disabled="disabled">
   <input type="hidden" name="POS_TYPE" value="NO_INV_CHECK" >
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
 <TABLE WIDTH="65%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
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
 
     <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="left"><b>Chk</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Description</TH>
          <TH><font color="#ffffff" align="left"><b>UP</TH>
          <TH><font color="#ffffff" align="left"><b>Qty</TH>
          <TH><font color="#ffffff" align="left"><b>Price</TH>
          
      </TR>
      <% if(poslist!=null && (poslist.size()>0)){
         for(int k=0;k<poslist.size();k++)
         {
        	 ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
         sumSubTotal = sumSubTotal+itemord.getTotalPrice();
         sumSubTotal = StrUtils.Round(sumSubTotal,2);
         
         unitpc = itemord.getUNITPRICE();
         unitpc = strUtils.Round(unitpc,2);
         
         totalpc = itemord.getTotalPrice();
         totalpc = strUtils.Round(totalpc,2);
         Stkqty=String.valueOf(itemord.getStkqty());
      Stkqty = StrUtils.formatNum(Stkqty);
       
         
      %>
   	   
          <TR bgcolor = "">
	        <TD align="left" > &nbsp;<font class="textbold"><input type="checkbox" name="chk" value="<%=k%>"></input></TD>    
	            <TD align="left" > &nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
	    	    <TD align="left" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>
	    	    <TD align="center" class="textbold">&nbsp;<%=cursymbol%><%=decformat.format(unitpc)%> </TD> 
	    	    <TD align="center" class="textbold">&nbsp;<%=Stkqty%> </TD> 
	         
	           <TD align="center" class="textbold">&nbsp;<%=cursymbol%><input type="text" name="<%=itemord.getITEM()%>"  value='<%=decformat.format(totalpc)%>' ></TD>	           
	            
          </TR><%}} %>
          <tr> <TD align="left" > &nbsp;<font class="textbold"></TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	   
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="right" class="textbold">&nbsp;<b>SUB TOTAL</b> </TD> 
	            <TD align="center" class="textbold">&nbsp;<%=cursymbol%><%if(sumSubTotal==0){%>0.00<%}else{%><%=decformat.format(sumSubTotal)%><% }%></TD>
	    	    </tr>
          <tr> <TD align="left" > &nbsp;<font class="textbold"></font></TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="right" class="textbold">&nbsp;<b>GST</b> </TD> 
	            <TD align="center" class="textbold">&nbsp;<%=decformat.format(gstf)%>%</TD>
	    	    </tr>
	 <% String total=""; 
	    	    float gstval=Float.parseFloat(gst);
	    	     totalsum = sumSubTotal + sumSubTotal*gstval/100;
	    	    	  totalsum = strUtils.Round(totalsum,2); 	    
	    	    %>
	    <tr> <TD align="left" > &nbsp;<font class="textbold"></TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="left" class="textbold">&nbsp; </TD>
	    	     <TD align="left" class="textbold">&nbsp; </TD>
	    	    <TD align="right" class="textbold">&nbsp;<b>TOTAL<b/> </TD> 
	            <TD align="center" class="textbold">&nbsp;<%=cursymbol%><%if(totalsum==0){%>0.00<%}else{%><%=decformat.format(totalsum)%><% }%></TD>
	    	    </tr>	        	  
            </TABLE>
           <TABLE border="0" width="50%" cellspacing="0" cellpadding="0" align="center">
   <TR >
         <TH ALIGN="right" width="7%">Scan Product ID &nbsp;&nbsp;</TH>
	 <TD ALIGN="left" width="5%"><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50 ><a href="#" onClick="javascript:popUpWin('list/itemList.jsp?ITEM='+form.ITEM.value);" ><img src="images/populate.gif" border="0"></a></TD>
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
		<TD colspan=5><INPUT name="REASONCODE" type="TEXT" value="NOREASONCODE" size="50" MAXLENGTH=80 > 
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
  }
  var currentEnabled = null;
function enablehidd()
{
	alert('edit fn');
	document.form.getElementById('prdhidden').innerHTML ="<input type='hidden' name='prdedit' value='edit'>";
	
	document.form.prdedit.disabled=false;
}
  
  function submitfm()
  {
	  var item=document.form.ITEM.value;
	  var loc = document.form.LOC.value;
	  var qty = document.form.QTY.value;
	  //document.form.action="/track/DynamicProductServlet?action=Add&ITEM="+item+"&LOC="+loc+"&QTY="+qty;
	   document.form.action="/track/DynamicProductServlet?action=Add";
      document.form.submit();
	  
  }
  if (document.layers)
	  document.captureEvents(Event.KEYDOWN);
	  document.onkeydown =
	    function (evt) { 
		  var item=document.form.ITEM.value;
		  var loc = document.form.LOC.value;
		  var qty = document.form.QTY.value;
	      var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	      if (keyCode == 13)   //13 = the code for pressing ENTER 
	      {
	    	  submitfm();
	      }
	    };
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
	  	 /* document.form.action="/track/DynamicProductServlet?action=Delete";
	  	  document.form.submit();*/
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