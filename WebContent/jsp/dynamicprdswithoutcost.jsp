<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="java.util.*" session="true"%>
<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%
String title = "Goods Receipt";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
    <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
</jsp:include>
<style type="text/css">
* {
  margin: 0;
  padding: 0;
}
.imgHiper {
     border: 0;
	 margin: 0;
	 padding: 0;
}
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}
</style>
<!-- <script src="assets/js/jquery.min.js"></script> -->
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="javascript">
/* autosize(document.querySelectorAll('textarea')); */
 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
</script>
<script>
/* document.getElementById("BATCH_0").value="NOBATCH"; */

</script>
<script language="javascript">
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
</script>

<title>Goods Receive (with receipt)</title>
</head>

<%
	 StrUtils strUtils     = new StrUtils();
	 Generator generator   = new Generator();
	 userBean _userBean      = new userBean();
	 ITEMMST items = new ITEMMST();
	 String btnString="";
	 HashMap<String, String> loggerDetailsHasMap1 = new HashMap<String, String>();
	 loggerDetailsHasMap1.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	 loggerDetailsHasMap1.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
 	 MLogger mLogger1 = new MLogger();
     DecimalFormat decformat = new DecimalFormat("#,##0.00");
     DecimalFormat decformaDiscount = new DecimalFormat("#,##0.0");
     DecimalFormat fltformat = new DecimalFormat("#,###");
 	 mLogger1.setLoggerConstans(loggerDetailsHasMap1);
 	 String fieldDesc="",cursymbol="",DISCITEM="",discountDesc="",cmd="";
	 String empNO="",refNO="",PLANT="",ITEM ="",ITEM_DESC="",LOCDESC="",iserrorVal="",SCANQTY="",LOC="",REASONCODE="",EMP_NAME="", EMP_ID="", disccnt="", STOCKQTY="",TRANSACTIONDATE="",REMARKS="",gsttax="",action="";
	 String html = "",BATCH="",CHKBATCH="",EXPIREDATE="",POSEXPIREDATE="";float gstf=0;
	 int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;String unitprice="",totalprice="",cntlDiscount="",serialized="",REFERENCENO="",TRANTYPE="";
	 PLANT = (String)session.getAttribute("PLANT");
	 String SumColor=""; 
	 Vector recvlist=null;
	 boolean flag=false;
	 sb.setmLogger(mLogger1);
	 String gst = sb.getGST("POS",PLANT);
	 cursymbol = DbBean.CURRENCYSYMBOL;
	 float unitpc=0,totalpc=0,  gstvalCalc=0, totalsum=0,msprice=0;
	 String uom = su.fString(request.getParameter("UOM"));
	 action = StrUtils.fString(request.getParameter("action")).trim();
	 LOCDESC = StrUtils.fString(request.getParameter("LOCDESC")).trim();
	 String sTranId = StrUtils.fString(request.getParameter("RECVTRANID")).trim();
	 iserrorVal = StrUtils.fString(request.getParameter("iserrorVal")).trim();
	 LOC = StrUtils.fString(request.getParameter("LOC")).trim();
	 BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
     REMARKS = StrUtils.fString(request.getParameter("REMARKS")).trim();
     CHKBATCH = StrUtils.fString(request.getParameter("CHKBATCH"));
     REASONCODE  = strUtils.fString(request.getParameter("REASONCODE"));
     EMP_NAME  = strUtils.fString(request.getParameter("EMP_NAME"));
     EMP_ID		= strUtils.fString(request.getParameter("EMP_ID"));
     TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
     serialized=strUtils.fString(request.getParameter("serialized"));
	 String SETCURRENTDATE_GOODSRECEIPT = new PlantMstDAO().getSETCURRENTDATE_GOODSRECEIPT(PLANT);//Thanzith
     DateUtils _dateUtils = new DateUtils();
     String curDate =_dateUtils.getDate();
     if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
     EXPIREDATE  = strUtils.fString(request.getParameter("EXPIREDATE")); 
     refNO =strUtils.fString(request.getParameter("REFERENCENO"));
     cmd =strUtils.fString(request.getParameter("cmd"));
     TRANTYPE=strUtils.fString(request.getParameter("TRANTYPE"));
     String sUserId = (String) session.getAttribute("LOGIN_USER");
 	System.out.println(strUtils.fString(request.getParameter("cmd")));
     String ischecked = "";
     if(REASONCODE=="" || REASONCODE==null){
    	 REASONCODE="NOREASONCODE";
     }
 
    if(CHKBATCH==null || CHKBATCH=="" ||CHKBATCH.equalsIgnoreCase("true")) 
	{
	  		 ischecked = "checked";
	}
   
	if(sTranId.length()>0){
		recvlist = (Vector)session.getValue("recvlist");
	     session.setAttribute("tranid",sTranId);
        }
	else{
		 session.putValue("recvlist", null);
	}
	/* SCANQTY="1"; */
	if((String)session.getAttribute("errmsg")!=null)
	{
	   fieldDesc= (String)session.getAttribute("errmsg");
	   session.setAttribute("errmsg","");
	   gstf = Float.parseFloat(gst);
	}
	
	if(BATCH.equalsIgnoreCase(""))
		BATCH="NOBATCH";      
%>

	<% if(recvlist!=null && (recvlist.size()>0)){
	if(cmd.equalsIgnoreCase("ViewProduct"))
    	 for(int k=0;k<recvlist.size();k++)
         {
    		 ITEMMST itemord = (ITEMMST)recvlist.elementAt(k);
    		 LOC = itemord.getLoc();
    		 LOCDESC = itemord.getLocDesc();
         }
    	 }
	
	%>
	<center class="mainred"><%=fieldDesc%></center>
<center><div style="color:green;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"></div></center>
	<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span> </a></li>
                <li>Goods Receipt</li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
<div class="box-body">
<center>
<div id="errorMessage" class="mainred">
</div> 

</center>
<form class="form-horizontal" name="form" method="get" id="dynamicprdswithoutcostForm" action="">
<input type="hidden" id="iserrorVal" value="<%=iserrorVal%>">
<input hidden value="1" name="flagwithbatch" id="flagwithbatch">
<br>

<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
<INPUT type="hidden" name="RCPNO" value="" />
<!-- <INPUT type="hidden" name="POS_TYPE" value="WITHOUTCOST" /> -->
<INPUT type="hidden" name="TRANTYPE" value="GOODSRECEIPTWITHBATCH" />

 		<div class="form-group">
        <label class="control-label col-sm-2" for="Transaction ID">GRNO:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="RECVTRANID" type="TEXT" id="RECVTRANID" value="<%=sTranId%>" size="30"  MAXLENGTH=50>
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/posTranIDList.jsp?TYPE=GOODSRECEIPTWITHBATCH');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Transaction Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	
								<span class="input-group-addon" id="newGenID"  onClick="onNew()"> 
								<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 	<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i></a>
				   		 		</span>
   		 	
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-4">
		<!-- <button type="button" class="Submit btn btn-default"  onClick="onView();"><b>View</b></button>&nbsp; -->
<!-- 		<button type="button" class="Submit btn btn-default" id="newGenID"  onClick="onNew();"><b>New</b></button>&nbsp; -->
		<button type="button" class="Submit btn btn-default"  onClick="onNewPOS();"><b>Clear All</b></button>&nbsp;    	</div> 
 		</div>
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Location">Location:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
<%--     		<INPUT class="form-control" name="LOC" id="LOC" type = "TEXT" value="<%=LOC%>" size="30"  MAXLENGTH=50> --%>
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/locList.jsp?LOC_ID='+ form.LOC.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	
   		 	     <input type="text" class="form-control" id="LOC" name="LOC"  value="<%=LOC%>" onchange="checkloc(this.value)">
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		</div>
  		
  		<div class="form-group">
  		<label class="control-label col-sm-2" for="Location Description">Location Desc:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="LOCDESC" id="LOCDESC" type = "TEXT" value="<%=LOCDESC%>" size="30"  MAXLENGTH=100 readonly> 
    	</div> 
 		</div>
 		
 		<div class="form-group">
		<div class="col-sm-6">
		<lable class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name = "serialized" id="serialized" value="1" onchange="serial();">
                     <b>Serialized</b></lable>
		<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "defualtQty" id="defualtQty" value="" onchange="DefaultQty();">
                    <b>  Default Quantity</b></lable>
		<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "expirydate" id="expirydate" value="" onchange="ExpiryDate();">
         <B> Same Expiry Date</B></lable>
	</div>
	</div>
	
	<div class="form-group">
	 <label class="control-label col-sm-2" for="Scan Product ID">Scan Product ID:</label>
	 <div class="col-sm-3">
      	    <div class="input-group">
<%--     		<INPUT class="form-control" name="ITEM" id="item" type="TEXT" value="<%=ITEM%>" size="30" MAXLENGTH=50  --%>
<!--     		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return Itemkeypress()}"> -->
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/itemList.jsp?ITEM='+form.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	
   		 	<input type="text" name="ITEM" id="item" class="ac-selected form-control"  value="<%=ITEM%>" onchange="checkitems(this.value,this)">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		 <div class="form-inline">
	 <label class="control-label col-sm-1" for="Batch">Batch:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<input class="form-control pageinputstyle"   name="BATCH_0" id="BATCH_0" type="TEXT"	
    				value="<%=BATCH%>" size="30" 
					maxlength=40 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){Batchkeypress();}">
   		 	<span class="input-group-addon"  onClick="generateBatch(0);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto Generate">
   		 	<i  class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<label class="control-label col-sm-1" for="QTY">Qty:</label>
  		<div class="col-sm-1">
        <input class="form-control" name="QTY" type="TEXT" id="qty"	value="" size="1" maxlength="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return CheckQty()}">
        </div>
        <div class="col-sm-2">
    	 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 50%" id="UOM">
			
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 	</div>
	 	</div>
	 	</div>
	 	<div class="form-group">
  		<label class="control-label col-sm-2" for="Expire Date">Expiry Date:</label>
        <div class="col-sm-2">
        <div class="input-group">
        <INPUT class="form-control datepicker"    name="EXPIREDATE" type="TEXT"  id="EXPIREDATE" value="<%=EXPIREDATE%>" 
				size="20" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.Submit.focus();}">
        
       		</div>
    	</div>
    	
	 	
				
			<input type="hidden" name="QTY_0" value="">
			<input type="hidden" name="ITEMDESC" value="">
			<input type="hidden" name="DISCITEM" value="" />
				
<table>
<tr>
			<td ALIGN="left">
			<div id="add">
			<button type="submit" class="Submit btn btn-default"  name="action"  onClick="return addaction()"><b>Add</b></button>&nbsp;
			</div>
			<input type="hidden" name="action1" value="temp">
			</td>
			<td ALIGN="left" >
			<button type="submit" class="Submit btn btn-default" name="action"  onClick="return delaction()"><b>Delete</b></button>&nbsp;
			</td>
			<td ALIGN="left" >
			<button type="submit" class="Submit btn btn-default"  name="action"  onClick="return holdaction()"><b>Hold</b></button>&nbsp;
			</td>
			</tr>
</table>
</div>
	<div class="row"><div class="col-sm-12">
	<table id="datatable-column-filter" class="table table-bordered table-hover dataTable no-footer">
	<thead>
		<TH style="width:5%;">CHK</TH>
		<TH style="width:8%;">PRODUCT ID</TH>
		<TH style="width:12%;">PRODUCT DESCRIPTION</TH>
		<TH width="10%">UOM</TH>		
		<TH style="width:6%;">BATCH</TH>		
		<TH style="width:7%;">QUANTITY</TH>
		<TH style="width:6%;">EXPIRY DATE</TH>
		
	</thead>
	<tbody>
	<% if(recvlist!=null && (recvlist.size()>0)){
		
		if(cmd.equalsIgnoreCase("ADD") || cmd.equalsIgnoreCase("Delete"))
			
		{ %>		
			 <script type="text/javascript">
		      document.addEventListener("DOMContentLoaded", function() {		   	  
		       document.getElementById("item").focus();
		      });
		       	</script>
		<%}%>
		
		<%
    	 for(int k=0;k<recvlist.size();k++)
         {
        	 //pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)recvlist.elementAt(k);
	         STOCKQTY = String.valueOf(itemord.getStkqty());
	         POSEXPIREDATE = itemord.getEXPREDATE();
			 STOCKQTY = StrUtils.formatNum(STOCKQTY);
			 if(cmd.equalsIgnoreCase("ViewProduct"))
			 {
				 LOC = itemord.getLoc();				 
				 EMP_ID = itemord.getEmpNo();
				 refNO = itemord.getRefNo();
				 TRANSACTIONDATE = itemord.getTranDate();
				 REASONCODE = itemord.getReasonCode();
				 REMARKS = itemord.getRemarks();
			     EMP_NAME = itemord.getEmpName();
			     POSEXPIREDATE = itemord.getEXPREDATE();
			     LOCDESC = itemord.getLocDesc();
		       }
			
         %>
	<TR bgcolor="">
		<TD align="left" width="3%">&nbsp;<font class="textbold"><input
			type="checkbox" name="chk" value="<%=k%>"></input></TD>
		<TD align="left" width="10%">&nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
		<TD align="left" width="16%" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>
		<TD align="left" width="16%" class="textbold">&nbsp; <%=itemord.getSTKUOM()%></TD>
		<TD align="left" width="12%" class="textbold">&nbsp; <%=itemord.getBATCH()%></TD>		
		<TD align="center" width="10%" class="textbold">&nbsp;<%=STOCKQTY%></TD>
		<TD align="left" width="10%" class="textbold">&nbsp; <%=POSEXPIREDATE%></TD>
	</TR>
	<%
         
         }} %>
         </tbody>
         </table>
         </div>
         </div>
        <!--   <p class="pull-right"><table>
	  <tr>
		<TD align="left">&nbsp;<font class="textbold"></TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="left" class="textbold">&nbsp;</TD>
		<TD align="right" class="textbold">&nbsp;<b>SUBTOTAL</b>
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
</TABLE></p> -->

<br>

<table border = "0" width = "100%" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<th  class="productlabel" style="font-size: 15px;" ALIGN="left">Employee ID:</th>
		<td>
			<div class="input-group col-sm-11 productdiv">
					<INPUT class="form-control" name="EMP_ID" id="EMP_ID" type = "TEXT" onchange="checkemployeess(this.value)" value="" size="20"  MAXLENGTH=20>
		          <!--   <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_ID='+form.EMP_ID.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details" >
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
		            <span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'EMP_ID\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
						
						
		      	
		      </div>
		</td>	
	<th  class="productlabel" style="font-size: 15px;" ALIGN="left">Employee Name:</th>
		<td>
			<div class="input-group col-sm-11 productdiv">
<%-- 					<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=20> --%>
		           <!--  <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
		      		
					<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="" size="20" readonly MAXLENGTH=20>
					<input type="hidden" name="EMP_LNAME" value="" />
		      </div>
		</td>	

<TH class="productlabel" style="font-size: 15px;" ALIGN="right">Reference No:</TH>
<td>
<div class="input-group col-sm-11 productdiv">
<INPUT class="form-control" name="REFERENCENO" id="REFERENCENO" type="TEXT" value="<%=refNO%>" size="20" MAXLENGTH=20>
</div>
</TD>
<th class="productlabel" style="font-size: 15px;" ALIGN="left">Reason Code:</th>
		<td>
			<div class="input-group col-sm-11 productdiv">
				<INPUT class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}"> 
			    <!-- <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/list/ReasonMstList.jsp?ITEM_ID='+form.REASONCODE.value+'&TYPE=KITDEKIT');">	
			 <a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
			 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
			 <span class="select-icon" onclick="$(this).parent().find('input[name=\'REASONCODE\']').focus()"> <i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</td>	
	
</tr>

</table>
<input type="hidden" name="TRANTYPE" value="GOODSRECEIPTWITHBATCH" />

<br>
<table>
	<tr >
		<th  class="productlabel" style="font-size: 15px;" ALIGN="left">Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<td >
			<div class="input-group col-sm-12 productdiv"  > 
		
			<textarea class="form-control" style="width:212px;" maxlength="99" id="REMARKS" name="REMARKS" type="TEXT" rows="1" value=""  ><%=REMARKS%></textarea>
			   
			   </div>
		</td>
	<th  class="productlabel required" style="font-size: 15px;" ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Transaction Date:&nbsp;&nbsp;&nbsp;</th>
		<td >
			<div class="input-group col-sm-12 productdiv"  > 
		<%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%>
					<INPUT class="form-control datepicker " name="TRANSACTIONDATE" id="TRANSACTIONDATE"
 					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" readonly MAXLENGTH="80" > 
					<%}else {%>
					<INPUT class="form-control datepicker " name="TRANSACTIONDATE" id="TRANSACTIONDATE"
<%-- 					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" readonly MAXLENGTH="80" > --%>
					value="" type="TEXT" size="10" readonly MAXLENGTH="80" >
			<%} %>
			   </div>
		</td>
	
</tr>
	
	</table>		  
	
			<br> 
			<div class="form-group">        
      	    <div class="col-sm-12" align="center">
      	 <!--    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
			   <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
			   <button type="button" class="Submit btn btn-default" name="action" onClick="return printaction()"><b>Submit</b></button>&nbsp;
			   </div>
			   </div>


<script >
$(document).ready(function() {
	
	
/* 	document.getElementById("qty").value =localStorage.getItem("qtyVal");
	document.getElementById("EXPIREDATE").value =localStorage.getItem("expDate"); */
	document.getElementById("qty").value =1;
	/*document.getElementById("EXPIREDATE").value =localStorage.getItem("expDateVal");*/
	if (localStorage.getItem("defQtyChk") == true || localStorage.getItem("defQtyChk") == 'true'){
		document.getElementById("defualtQty").checked=localStorage.getItem("defQtyChk");
		if(document.getElementById("defualtQty").checked==true){
			document.getElementById("qty").readOnly = true;
			
			}
	}
	
	
	
	if (localStorage.getItem("expDateChk") == true || localStorage.getItem("expDateChk") == 'true'){
		document.getElementById("expirydate").checked=localStorage.getItem("expDateChk");
		if(document.getElementById("expirydate").checked==true){
			document.getElementById("EXPIREDATE").readOnly = true;
			document.getElementById("EXPIREDATE").value =localStorage.getItem("expDateVal");
			}
	} 
	
	
	
	
	
	
	<%if ("1".equals(request.getParameter("serialized"))){%>
		$('#serialized').prop('checked', 'checked');
		serial();
	<%}%>
	<%
	if ("1".equals(request.getParameter("defualtQty"))){%>
		$('#defualtQty').prop('checked', 'checked');
	<%}%>
	<%if ("1".equals(request.getParameter("expirydate"))){%>
		$('#expirydate').prop('checked', 'checked');
	<%}%>	
$('.datatable').dataTable({
	sDom: "R"+
			
			" <'col-sm-12'f>",
				 "bPaginate": false,
				 "bInfo": false,
				 "oLanguage": {"sEmptyTable": " No data available"}
   	});
   	
   	
   	
$(".resizetable").colResizable({
	liveDrag:true, 
	gripInnerHtml:"<div class='grip'></div>", 
	draggingClass:"dragging", 
    resizeMode:'fit'
});   
onNew();
});
</script>
 <script src="../jsp/assets/js/jquery.dataTables.min.js"></script>	 
	<script src="../jsp/assets/js/dataTables.colVis.bootstrap.js"></script>	
	<script src="../jsp/assets/js/dataTables.colReorder.min.js"></script>	
	<script src="../jsp/assets/js/dataTables.tableTools.min.js"></script>	
	<script src="../jsp/assets/js/dataTables.bootstrap.js"></script>
	<script src="../jsp/assets/js/colResizable-1.6.js"></script>
<script language="javascript">
$(window).on('beforeunload', function() {
	console.log('in onbeforeunload');
	 localStorage.setItem("defQtyChk",document.getElementById("defualtQty").checked );
	localStorage.setItem("expDateChk",document.getElementById("expirydate").checked); 
    
	 localStorage.setItem("qtyVal",document.getElementById("qty").value);
	 if(document.getElementById("expirydate").checked==true)
	 localStorage.setItem("expDateVal",document.getElementById("EXPIREDATE").value);
	 

   
    /* var date = new Date();
    day = date.getDate();
    month = date.getMonth() + 1;
    year = date.getFullYear();
    var fullDate=[day, month, year].join('/'); */
	 
});  
   

  setfocus();
  var index=0;
  if( document.form.ITEM.value.length>0)
	{
			alert("setting focus");
		 
	  }

  function setfocus()
  {	 
	  document.form.RECVTRANID.focus();
	  //DisplayBatch();
  }
  $(document).ready(function() {
	  $(document).on('keydown','form input',function(event){
		 // alert("");
	    if(event.keyCode == 13) {
	      event.preventDefault();
	      return false;
	    }
	  });
	  var elemItem = document.getElementById("item");
	  elemItem.onkeyup = function(e){
		  
		  var loc = document.form.LOC.value;
		  var prodId = document.form.item.value;
		  var key = e.which;
		  if(key == 13)  // the enter key code
		   {
			  
			  if(prodId==null||prodId=="" || prodId.trim()=="")
			  {
				  alert("Please Scan Product ID!");
				  document.getElementById("item").focus();
				  
				
				  return false;
			  }
				
			  else if(loc==null||loc==""||loc.trim()=="")
			  {
				  
				  document.getElementById("LOC").focus();
				  alert("Please Enter Location!");
				
				  return false;
			  }else{
				  GetUOM();					  
			  $( "#BATCH_0" ).focus();
			$("#BATCH_0").select();
			  }
		   }
		 } 
	  
	  var elemBATCH_0 = document.getElementById("BATCH_0");
	  elemBATCH_0.onkeyup = function(e){
		  var loc = document.form.LOC.value; 
		  var prodId = document.form.item.value;
		  var key = e.which;
		  if(key == 13)  // the enter key code
		   {
			  if(prodId==null||prodId=="" || prodId.trim()=="")
			  {
				  alert("Please Scan Product ID!");
				  document.getElementById("item").focus();
				  
				
				  return false;
			  }
			  if(loc==null||loc==""||loc.trim()=="")
			  {
				  
				  document.getElementById("LOC").focus();
				  alert("Please Enter Location!");
				
				  return false;
			  }else{
				  GetUOM();					  
			  if (document.getElementById("defualtQty").checked==true ) {
					  addaction();
			  }else{
				  $( "#qty" ).focus();
				$("#qty").select();
			  }
			  }
		   }
	  }

	  var elem2 = document.getElementById("qty");
	  elem2.onkeyup = function(e){
	      if(e.keyCode == 13){
	    	 
	    	 addaction();
	    		return false;
	      }
	  }

	  /* From location Auto Suggestion */
		$('#LOC').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'LOC',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : "<%=PLANT%>",
					ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.LOC_MST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div onclick="document.form.LOCDESC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
	 			if($(this).val() == ""){
	 				document.form.LOCDESC.value = "";
	 				document.getElementById("LOCDESC").value = "";
	 			}
		});

		/* Product Number Auto Suggestion */
		$('#item').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'ITEM',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : "<%=PLANT%>",
					ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
					ITEM : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.items);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
					return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Qty</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
	 				validateProduct();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
	 			if($(this).val() == ""){
	 				document.form.ITEM.value = "";
	 				document.getElementById("item").value = "";
	 			}
		});

		/* Employee Auto Suggestion */
		$('#EMP_ID').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "../MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							ACTION : "GET_EMPLOYEE_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.EMPMST);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    	return '<div onclick="document.form.EMP_NAME.value = \''+data.FNAME+'\'"><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">NAME:'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			}).on('typeahead:change',function(event,selection){
	 			if($(this).val() == ""){
	 				document.form.EMP_NAME.value = "";
	 				document.getElementById("EMP_NAME").value = "";
	 			}
		});	

		/* Reason code Auto suggestion*/
		$('#REASONCODE').typeahead({
				hint: true,
				minLength:0,
				searchOnFocus: true
			},
			{
				display: 'rsncode',
				async: true,
				source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,	
					async : true,
					data : {
					PLANT : "<%=PLANT%>",
					ACTION : "GET_RSN_SUMMARY",
					REASONCODE : query
				},
				dataType : "json",
				success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
				}
				});
				},
				limit: 9999,
				templates: {
				empty: [
					'<div style="padding:3px 20px">',
					'No results found',
					'</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<p class="item-suggestion">'+ data.rsncode +'</p>';
				}
				}
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
	 			if($(this).val() == ""){
	 				document.form.REASONCODE.value = "";
	 				document.getElementById("REASONCODE").value = "";
	 			}
		});	
  });
  function CheckQty()
	    	 {
	    	  var prodId = document.form.ITEM.value;
			  var loc = document.form.LOC.value;
			  var batch = document.form.BATCH_0.value;
			  if(loc==null||loc=="")
			  {
				  alert("Please Enter Location!");
				  document.form.LOC.focus();
				  return false;
			  }
			  if(prodId==null||prodId=="" || prodId.trim()=="")
			  {
				  alert("Please Scan Product ID!");
				  document.getElementById("item").focus();
				  return false;
			  }
			  if(batch==null||batch=="" || batch.trim()=="")
			  {
				  alert("Please Scan BATCH!");
				  document.getElementById("BATCH_0").focus();
				  return false;
			  }
			  else
				  {
				  document.getElementById("Add").focus();  
				  }	    	  
	      }
	  
  
  
  function Itemkeypress()
    {
	   
      if(document.getElementById("chkbatch").checked == true){    	  
	  if( document.form.ITEM.value.length>0)
	  {
		  var prodId = document.form.ITEM.value;
		  var loc = document.form.LOC.value;
		  if(loc==null||loc=="")
		  {
			  alert("Please Enter Location!");
			  document.form.LOC.focus();
			  return false;
		  }
		  if(prodId==null||prodId=="" || prodId.trim()=="")
		  {
			  alert("Please Scan Product ID!");
			  document.getElementById("item").focus();
			  return false;
		  }
		  else{
			  GetUOM();

			  document.form.BATCH_0.focus(); 
		  }
		  //document.getElementById("Add").focus();
	  }
      }
      else{document.form.BATCH_0.focus(); 
		return false;
      }   

  }
  function validateLocation(locId)
  {
	  
			var urlStr = "/track/MiscOrderHandlingServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					LOC : locId,
					LOGIN_USER : "<%=sUserId%>",
					PLANT : "<%=PLANT%>",								
					ACTION : "VALIDATE_LOCATION"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("LOCDESC").value = resultVal.locdesc;   
							
						} else {
							alert("Not a valid Location");
							document.getElementById("LOC").value = "";
							document.getElementById("LOC").focus();
						}
					}
				});						
  }
  function generateBatch(index){
		var currentbatch=index;

			var urlStr = "/track/MiscOrderHandlingServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					PLANT : "<%=PLANT%>",
				ACTION : "GENERATE_BATCH"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;
					document.getElementById("BATCH_"+currentbatch).value = resultVal.batchCode;
					if(!(document.form.defualtQty.checked)&&!(document.form.serialized.checked)){
			 				document.form.BATCH_0.focus();
								document.form.BATCH_0.select();
							}
							else{
								document.form.BATCH_0.focus();
								  
							}

				} else {
					alert("Unable to genarate Batch");
					document.getElementById("BATCH_"+currentbatch).value = "";
					document.getElementById("BATCH_"+currentbatch).focus();
				}
			}
		});
		
	}

  function addaction()
  {
	   var item = document.form.ITEM.value;	
	  var loc = document.form.LOC.value;	
	  var tranid = document.form.RECVTRANID.value;	
	  var scanqty = document.form.QTY.value;
	  
	  if((tranid==null||tranid=="")&&(loc==null||loc==""))
	  {
		  alert("Please create Transaction ID & select location before adding product!");
		  document.form.RECVTRANID.focus();
		  return false;
	  }
	  if(tranid==null||tranid=="")
	  {
		  alert("Please Enter Tran Id!");
		  document.form.RECVTRANID.focus();
		  return false;
	  }
	  
	  if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.LOC.focus();
		  return false;
	  }
	  
	  if(document.form.UOM.value == ""){
	   	    alert("Please enter a UOM value");
	   	    document.form.UOM.focus();
	   	    document.form.UOM.select();
	   	    return false;
	   }
	  
	  if(item==null||item=="")
	  {
		  alert("Please Scan Product ID!");
		  document.form.ITEM.focus();
		  return false;
	  }
	  
	  if(scanqty==null||scanqty==""||scanqty=="0")
	  {
		  alert("Please Enter Qty!");
		  document.form.QTY.focus();
		  return false;
	  }
		 document.form.cmd.value="ADD" ;
		 document.form.action  = "/track/DynamicProductServlet?cmd=ADD";
		 document.form.submit();
return false;		   
		
  }
  
	  function Batchkeypress()
	  {
		  if(document.getElementById("chkbatch").checked == false){
			  if( document.form.BATCH_0.value.length>0)
			  {
				  //document.getElementById("Add").focus();
				  var prodId = document.form.ITEM.value;
				  var loc = document.form.LOC.value;
				  if(loc==null||loc=="")
				  {
					  alert("Please Enter Location!");
					  document.form.LOC.focus();
					  return false;
				  }
		  if(prodId==null||prodId=="" || prodId.trim()=="")
		  {
			  alert("Please Scan Product ID!");
			  document.getElementById("item").focus();
			  
			
			  return false;
		  }
		  else{			  
			  document.form.QTY.focus(); 
		  }
			  }
			  
		  }
	  }
	
 
   function delaction()
  {
	 
	  var item = document.form.chk;
	  var empid =document.getElementById("EMP_ID").value;
	   var empname =document.getElementById("EMP_NAME").value;
		
		  var loc = document.getElementById("LOC").value;	
		  var qty =document.getElementById("qty").value;	
		  var refno = document.getElementById("REFERENCENO").value;
		  var rsncode = document.getElementById("REASONCODE").value;	
		  var remarks = document.getElementById("REMARKS").value;
		  var expireDate = document.getElementById("EXPIREDATE").value;
		  var trnsdate = document.getElementById("TRANSACTIONDATE").value;
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
			alert("Must Select at least one Product which you want to Delete");
			return false;
		}
  
	  document.form.cmd.value="Delete" ;
	  document.form.action  = "/track/DynamicProductServlet?cmd=Delete";
	  /* document.form.submit(); */
	  return true;	 
	  }     
  

  function printaction(){
	  var	RecvtranID =document.getElementById("RECVTRANID").value;
	var item=document.form.chk;
	 var itemvalue=document.getElementById("item").value;
	 

	  if(item == undefined){
		 alert("Add Product to Print");
		 return false;
	  }
	  var tranid=document.form.RECVTRANID.value;
	  if(tranid==null||tranid==""){
		 alert("Generate Transaction Id");
		 return false;
	  }
	  var trandate=document.form.TRANSACTIONDATE.value;
	  if(trandate==null||trandate==""){
		  alert("Please select Transaction Date");	
		 return false;
	  }

	        var formData = $('form#dynamicprdswithoutcostForm').serialize();

				$.ajax({
					type : 'get',
					url : '/track/DynamicProductServlet?cmd=printreceiveproduct',
					dataType : 'html',
					responseType: 'arraybuffer',
					data : formData,

					success : function(data, status, xhr) {
			var result = data.split(":");
			if(result[0] == "Success" && result[2] == "1")
			{
						document.form.action="/track/DynamicProductServlet?";
		 	         	document.form.cmd.value="printreceiveproduct" ;
		 	         	document.form.RCPNO.value=result[1];
						document.form.submit();
						onNewPOS();
						setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Receipt Successfully."); }, 1000);
						
			}
			else if(result[0] == "Success" && result[2] == "0"){
				onNewPOS();
				setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Receipt Successfully."); }, 1000);
			}
			else{
				$('#appendbody').html(data);
				setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Receipt Successfully."); }, 1000);
			}
					

					},
					error : function(data) {

						alert(data.responseText);
					}
				});
	
	  return false;
  }
				
  function getCookie(cookieName){
	  var cookieArray = document.cookie.split(';');
	  for(var i=0; i<cookieArray.length; i++){
	    var cookie = cookieArray[i];
	    while (cookie.charAt(0)==' '){
	      cookie = cookie.substring(1);
	    }
	    cookieHalves = cookie.split('=');
	    if(cookieHalves[0]== cookieName){
	      return cookieHalves[1];
	    }
	  }
	  return "";
	}

 
	function showObject(obj){
		document.getElementById(obj).style.display = "inline";
	}
	function hideObject(obj){
		document.getElementById(obj).style.display = "none";
	}

	
	function contains(arr, findValue) {
	    var i = arr.length;
	     
	    while (i--) {
	        if (arr[i] === findValue) return true;
	    }
	    return false;
	}
	
	function holdaction(){
		var	RecvtranID =document.getElementById("RECVTRANID").value;
		  var item=document.form.chk;
		  if(item == undefined){
			  alert("Add Product to Hold the Transaction");
			  return false;
		  }
		  var formData = $('form#dynamicprdswithoutcostForm').serialize(); 
		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?action=HoldTran',
		        dataType:'html',
		        data:formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		        	onNewPOS();
		        	setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " hold successfully."); }, 1000);
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    return false;
		  
	  }
 	function onGenID()
	{
 		 document.form.cmd.value="NEWID" ;
 		//document.getElementById("qty").value=1;
 		document.getElementById("EXPIREDATE").value="";
 		$('#expirydate').prop('checked', false);
 		$('#defualtQty').prop('checked', false);
 		 document.form.action  = "/track/DynamicProductServlet?cmd=NEWID";
 		 document.form.submit();
		
     
	}
 	function onNew()
 	{
 		
 		var urlStr = "/track/InboundOrderHandlerServlet";
 		$.ajax( {
 			type : "POST",
 			url : urlStr,
 			data : {
 			PLANT : "<%=PLANT%>",
 			ACTION : "GRN"
 			},
 			dataType : "json",
 			success : function(data) {
 				if (data.status == "100") {
 					var resultVal = data.result;				
 					var resultV = resultVal.grno;
 					document.form.RECVTRANID.value= resultV;
 		
 				} else {
 					alert("Unable to genarate GRN NO");
 					document.form.RECVTRANID.value = "";
 				}
 			}
 		});	
 		}
	function onNewPOS()
	{    
	     document.form.RECVTRANID.value="";
	     document.form.LOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REASONCODE.value="";
	     document.form.TRANSACTIONDATE.value="";
	     document.form.REMARKS.value="";
	     document.form.EXPIREDATE.value="";
	     document.form.REFERENCENO.value="";
	     document.form.LOCDESC.value="";
		 document.form.cmd.value="NewPOSReceive" ;
	 
			var formData = $('form#dynamicprdswithoutcostForm').serialize(); 

		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?cmd=NewPOSReceive',
		        dataType:'html',
		        data:  formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		            
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    // document.form.submit();
	}
		function ClearonNew(){
			 document.form.LOC.value="";
		     document.form.EMP_ID.value="";
		     document.form.EMP_NAME.value="";
		     document.form.REMARKS.value="";
		     document.form.EXPIREDATE.value="";
		     document.form.REFERENCENO.value="";
		     document.form.LOCDESC.value="";
		}
	  function onView()
		{
	       var tranid = document.form.RECVTRANID.value;	 	
		  if(tranid==null||tranid=="")
		  {
			  alert("Please Select TranID");
			  return false;
		  }else{ 
			  
				 document.form.cmd.value="ViewProduct" ;
				   document.form.action  = "/track/DynamicProductServlet?cmd=ViewProduct";
				   document.form.submit();
		  }
		}		   
				   
	     
	 function DefaultQty(){
		 
		 if (document.getElementById("defualtQty").checked==true ) {
			 document.getElementById("qty").readOnly = true;
			 var qtyValue=document.getElementById("qty").value;
			 document.getElementById("qty").innerHTML =qtyValue;
			 
		 }
		 else{
			 document.getElementById("qty").readOnly = false;
			 document.getElementById("qty").value=1;
		 }
	 }
	 function ExpiryDate(){
		 
		 if (document.getElementById("expirydate").checked==true ) {
			
			  document.getElementById("EXPIREDATE").readOnly = true;
			 var expiryDateValue = document.getElementById("EXPIREDATE").value;
			 document.getElementById("EXPIREDATE").innerHTML = expiryDateValue;
		 }
		 else{
			 document.getElementById("EXPIREDATE").value="";
			 document.getElementById("EXPIREDATE").readOnly = false;
		 }
	 }
	  
	 
	  function serial(){

		  if ( document.getElementById("serialized").checked==true ) {
			  
			  document.getElementById("defualtQty").checked=true
		        document.getElementById("defualtQty").disabled = true;
			  document.getElementById("qty").readOnly = true;
			  document.getElementById("qty").value=1;
		    } else {
		    	document.getElementById("defualtQty").checked=false;
		        document.getElementById("defualtQty").disabled = false;
			  document.getElementById("qty").readOnly = false;
			  var qtyValue = document.getElementById("qty").value;
			 document.getElementById("qty").innerHTML =qtyValue;
			 
			 document.getElementById("expirydate").checked=false;
		        document.getElementById("expirydate").disabled = false;
		        document.getElementById("EXPIREDATE").readOnly = false;
			  var expiryDateValue = document.getElementById("EXPIREDATE").value;
			 document.getElementById("EXPIREDATE").innerHTML = expiryDateValue;
			  
		    }
		  
	  }
	   var elem3 = document.getElementById("LOC");
		  elem3.onkeyup = function(e){
		      if(e.keyCode == 13){
		    
		    	  var loc = document.form.LOC.value;	 
		    	  if(loc==null||loc=="")
		    	  {
		    		  alert("Please Enter Location!");
		    		  document.form.LOC.focus();
		    		  return false;
		    	  }
		    	  validateLocation(elem3.value);
		    	  getLocation(elem3.value);
		    	 
		      }
		  }
		   var elem4 = document.getElementById("EMP_ID");
			  elem4.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	  document.getElementById("REFERENCENO").focus(); 
			      }
			  }
			  var elem5 = document.getElementById("REFERENCENO");
			  elem5.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	  document.getElementById("REASONCODE").focus(); 
			    	  document.getElementById("REASONCODE").select(); 
			      }
			  }
			  var elem6 = document.getElementById("REASONCODE");
			  elem6.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	  document.getElementById("REMARKS").focus(); 
			      }
			  }
			  var elem7 = document.getElementById("RECVTRANID");
			  elem7.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	    var tranid = document.form.RECVTRANID.value;	 	
			  		  if(tranid==null||tranid=="")
			  		  {
			  			  alert("Please Select TranID");
			  			 document.getElementById("RECVTRANID").focus(); 
			  			  return false;
			  		  }else{
			    	  document.getElementById("LOC").focus(); 
			  		  }
			      }
			  }			  
			  function getLocation(loc)
			  {
				  var tranid = document.form.RECVTRANID.value;	
				  $.ajax( {
						type : "get",
						url : '/track/DynamicProductServlet?action=GETLOC',
						data : {
						PLANT : "<%=PLANT%>",
						ACTION : "GETLOC",
						TRANTYPE:"GOODSRECEIPTWITHBATCH",
						LOC:loc
					},
					dataType : "html",
					success : function(data) {
						$("#appendbody").html(data);
					var errorValue =document.getElementById("iserrorVal").value;
						
			
						document.getElementById("RECVTRANID").value=tranid;
					 if(errorValue){
							 document.getElementById("LOC").focus(); 
						}else{
							 document.getElementById("item").focus(); 
						} 
					},
					error:function(data)
					{
						
					}
				});
			  }
			  /*function getLocation(loc)
			  {
				  var tranid = document.form.RECVTRANID.value;	
				  $.ajax( {
						type : "get",
						url : '/track/DynamicProductServlet?action=GETLOC',
						data : {
						PLANT : "<%=PLANT%>",
						ACTION : "GETLOC",
						TRANTYPE:"GOODSRECEIPTWITHBATCH",
						LOC:loc
					},
					dataType : "html",
					success : function(data) {
						$("#appendbody").html(data);
					var errorValue =document.getElementById("iserrorVal").value;
						
			
						document.getElementById("RECVTRANID").value=tranid;
					 if(errorValue){
							 document.getElementById("LOC").focus(); 
						}else{
					},
					error:function(data)
					{
						
					}
				});
			  }*/
			  function validateProduct()
			  {
				  GetUOM();
			  }
			  function GetUOM()
			  {
				  var itemValue = document.getElementById("item").value;
				 
				
					  var urlStr = "/track/ItemMstServlet";				  
					  
				  
					  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"", ACTION: "PRODUCT_UOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetUOM });
					  

			  }
		 	 function onGetUOM(data){
		  		
				  var errorBoo = false;
					$.each(data.errors, function(i,error){					
						if(error.ERROR_CODE=="99"){
							errorBoo = true;
							document.getElementById("UOM").value=0;
						
						}
					});
					
					if(errorBoo == false){
						if(data.status=="99"){
							alert("Invalid Scan Product!");
							  document.getElementById("item").focus();	
							  return false;
						}
						else{		 
							var puom= data.result.PURCHASEUOM;
			        	document.getElementById("UOM").value =puom;
			        }
					}
				  
			  }


		 	function checkemployeess(employee){	
		 		var urlStr = "/track/MasterServlet";
		 		$.ajax( {
		 			type : "POST",
		 			url : urlStr,
		 			async : true,
		 			data : {
		 				FNAME : employee,
		 				ACTION : "EMPLOYEE_CHECKS"
		 				},
		 				dataType : "json",
		 				success : function(data) {
		 					if (data.status == "99") {
		 						alert("Employee Does't Exists");
		 						document.getElementById("EMP_ID").focus();
		 						$("#EMP_ID").typeahead('val', '');
		 						document.getElementById("EMP_NAME").value = "";
		 						return false;	
		 					} 
		 					else 
		 						return true;
		 				}
		 			});
		 		 return true;
		 	}
		 	function checkloc(loc){	
				var urlStr = "/track/MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						LOC : loc,
						ACTION : "LOC_CHECK"
						},
						dataType : "json",
						success : function(data) {
							if (data.status == "99") {
								alert("Location Does't Exists");
								document.getElementById("LOC").focus();
								$("#LOC").typeahead('val', '');
								document.getElementById("LOC").value = "";
								document.getElementById("LOCDESC").value = "";
								return false;	
							} 
							else 
								return true;
						}
					});
				 return true;
		}

		 	function checkitems(itemvalue,obj){	
		 		var urlStr = "/track/ItemMstServlet";
		 		$.ajax( {
		 			type : "POST",
		 			url : urlStr,
		 			async : true,
		 			data : {
		 				ITEM : itemvalue,
		 				ACTION : "PRODUCT_CHECK"
		 				},
		 				dataType : "json",
		 				success : function(data) {
		 					if (data.status == "99") {
// 		 						alert("Product Does't Exists");
		 							$("#item").typeahead('val', '');
		 							document.getElementById("item").value = "";
		 						return false;	
		 						
		 					} 
		 					else 
		 						return true;
		 				}
		 			});
		 		 return true;
		 	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>