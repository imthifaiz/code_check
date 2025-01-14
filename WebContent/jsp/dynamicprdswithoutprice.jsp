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
<!-- Not in Use - Menus status 0 -->
<%
String title = "Goods Issue";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
 <jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>

<!-- <link rel="stylesheet" href="../jsp/css/typeahead.css"> -->
<!-- <link rel="stylesheet" href="../jsp/css/accounting.css"> -->
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
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script type="text/javascript">
 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
</script>
<script >
/* autosize(document.querySelectorAll('textarea')); */
// document.getElementById("BATCH_0").value="NOBATCH";

</script>
<script type="text/javascript">
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
</script>

<title>Goods Issue (with receipt)</title>
</head>

<%
	 StrUtils strUtils     = new StrUtils();
	 Generator generator   = new Generator();
	 userBean _userBean      = new userBean();
	 ITEMMST items = new ITEMMST();
	 String btnString="";
	 HashMap<String, String> loggerDetailsHasMap1 = new HashMap<String, String>();
	 loggerDetailsHasMap1.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	 loggerDetailsHasMap1.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")));
 	 MLogger mLogger1 = new MLogger();
     DecimalFormat decformat = new DecimalFormat("#,##0.00");
     DecimalFormat decformaDiscount = new DecimalFormat("#,##0.0");
     DecimalFormat fltformat = new DecimalFormat("#,###");
 	 mLogger1.setLoggerConstans(loggerDetailsHasMap1);
 	 String fieldDesc="",cursymbol="",DISCITEM="",discountDesc="",cmd="";
	 String refNO="",PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",LOCDESC="",iserrorVal="",LOC="",REASONCODE="",EMP_NAME="", TRANSACTIONDATE="",EMP_ID="", disccnt="",STOCKQTY="",REMARKS="",gsttax="",action="";
	 String html = "",BATCH="",CHKBATCH="";float gstf=0;
	 int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;String unitprice="",totalprice="",cntlDiscount="",AVAILQTY="",REFERENCENO="",serialized="";
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
	 LOCDESC = StrUtils.fString(request.getParameter("LOCDESC")).trim();
	 iserrorVal = StrUtils.fString(request.getParameter("iserrorVal")).trim();
	 LOC = StrUtils.fString(request.getParameter("LOC")).trim();
	 BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
     REMARKS = StrUtils.fString(request.getParameter("REMARKS")).trim();
     CHKBATCH = StrUtils.fString(request.getParameter("CHKBATCH"));
     REASONCODE  = StrUtils.fString(request.getParameter("REASONCODE"));
     refNO=  StrUtils.fString(request.getParameter("REFERENCENO"));
     EMP_NAME  = StrUtils.fString(request.getParameter("EMP_NAME"));
     EMP_ID  = StrUtils.fString(request.getParameter("EMP_ID"));
     TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
     AVAILQTY=StrUtils.fString(request.getParameter("AVAILQTY"));
     cmd =StrUtils.fString(request.getParameter("cmd"));
     serialized=StrUtils.fString(request.getParameter("serialized"));
     String TRANTYPE=StrUtils.fString(request.getParameter("TRANTYPE"));
	 String sUserId = (String) session.getAttribute("LOGIN_USER");
     String uom = su.fString(request.getParameter("UOM")); 
	 String SETCURRENTDATE_PICKANDISSUE = new PlantMstDAO().getSETCURRENTDATE_PICKANDISSUE(PLANT);//Thanzith
	 

     System.out.println("serialized"+serialized);
     DateUtils _dateUtils = new DateUtils();
     String curDate =DateUtils.getDate();
     if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
     String ischecked = "";
     if(REASONCODE=="" || REASONCODE==null){
    	 REASONCODE="NOREASONCODE";
     }
    if(CHKBATCH==null || CHKBATCH=="" ||CHKBATCH.equalsIgnoreCase("true")) 
	{
	  		 ischecked = "checked";
	}
  
	if(sTranId.length()>0){
		poslist = (Vector)session.getValue("poslist");
	     session.setAttribute("tranid",sTranId);
        }
	else{
		 session.putValue("poslist", null);
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
	<% if(poslist!=null && (poslist.size()>0)){
		
if(cmd.equalsIgnoreCase("ADD") || cmd.equalsIgnoreCase("Delete"))
			
		{ %>		
			 <script type="text/javascript">
		      document.addEventListener("DOMContentLoaded", function() {		   	  
		       document.getElementById("item").focus();
		      });
		       	</script>
		<%}%>
		
		<%
		
		if(cmd.equalsIgnoreCase("ViewTran"))
    	 for(int k=0;k<poslist.size();k++)
         {
        	 //pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         LOC = itemord.getLoc();
	         LOCDESC = itemord.getLocDesc();
	         System.out.println("items.setLoc1"+LOC);
         }
	}
%>
<center class="mainred"><%=fieldDesc%></center>
<center><div style="color:green;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"></div></center>
<div class="container-fluid m-t-20">
	<div class="box">
	
<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li>Goods Issue</li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
<div class="box-body">
<center>
<div id="errorMessage" class="mainred">
 
</div>
</center>
<FORM class="form-horizontal" name="form" method="get" id="dynamicprdswithoutpriceForm" action="">
<input type="hidden" id="iserrorVal" value="<%=iserrorVal%>">
<input hidden value="1" name="flagwithbatch" id="flagwithbatch">
<input hidden value="1" name="flagissuewithbatch" id="flagissuewithbatch">
<input hidden value="1" name="goodsissuewithbatch" id="goodsissuewithbatch">
<br>

<center>
<div class="mainred" id="errorMessage">
</div>
</center>

<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
<INPUT type="hidden" name="RCPNO" value="" />
<!-- <INPUT type="hidden" name="POS_TYPE" value="WITHOUTPRICE" /> -->
<INPUT type="hidden" name="TRANTYPE" value="GOODSISSUEWITHBATCH" />

		<div class="form-group">
        <label class="control-label col-sm-2" for="Transaction ID">GINO:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="TRANID" type="TEXT" id="TRANID" value="<%=sTranId%>" size="30"  MAXLENGTH=50>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('list/posTranIDList.jsp?TYPE=GOODSISSUEWITHBATCH');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Transaction Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-4">
		<!-- <button type="button" class="Submit btn btn-default"  onClick="onView();"><b>View</b></button>&nbsp; -->
		<button type="button" class="Submit btn btn-default" id="newGenID" onClick="javascript:onNew();return false;"><b>New</b></button>&nbsp;
		<button type="button" class="Submit btn btn-default"  onClick="onNewPOS();"><b>Clear All</b></button>&nbsp;    	</div> 
 		</div>
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Location">Location:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="LOC" id="LOC" type = "TEXT" value="<%=LOC%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/locList.jsp?LOC_ID='+ form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		</div>
  		
  		<div class="form-group">
  		<label class="control-label col-sm-2" for="Location Description">Location Desc:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="LOCDESC" id="LOCDESC" type = "TEXT" value="<%=LOCDESC%>" size="30"  MAXLENGTH=100 readonly> 
    	</div> 
 		</div>			
		
	<!--<TR>
		<TH ALIGN="right" width="7%">&nbsp;&nbsp;</TH>
		<TD ALIGN="left" width="15%"><INPUT Type=Checkbox  style="border:0;" name = "chkbatch" id="chkbatch" value="chkbatch" onclick="DisplayBatch();" <%=ischecked%>>
                        &nbsp; Default NOBATCH</TD>
		<td></td>
	</TR>    --> 
	
	<div class="form-group">
	<div class="col-sm-6">
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serialized" id="serialized" value="1" onchange="serial();">
                     <b>Serialized</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "defualtQty" id="defualtQty" value="" onchange="DefaultQty();">
                    <b>  Default Quantity</b></lable>
	<INPUT Type=hidden  style="border:0;" name = "bulkcheckout" id="bulkcheckout" value="1" ></input>
        
	</div>
	</div>
	 
	 <div class="form-group">
	 <label class="control-label col-sm-2" for="Scan Product ID">Scan Product ID:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ITEM" type = "TEXT" id="item" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"
    		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return Itemkeypress()}"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="itempopUpwin();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		 <div class="form-inline">
	 <label class="control-label col-sm-1" for="Batch">Batch:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT"
				value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){Batchkeypress();}"  
				MAXLENGTH="30"><input type="hidden" name="BATCH_ID_0" value="-1"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEM.value+'&LOC0='+form.LOC.value+'&BATCH0=&INDEX='+'0'+'&TYPE=GOODSISSUEWITHBATCH'+'&UOM='+form.UOM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
	 	</div>
	 	<div class="form-inline">
  		<label class="control-label col-sm-1" for="Available Qty">Available Qty:</label>
        <div class="col-sm-1">
        <INPUT class="form-control" readonly name="AVAILQTY" type="TEXT" id="AVAILQTY"	value="<%=AVAILQTY%>" size="1" maxlength="10">
    	</div>
    	<label class="control-label col-sm-1" for="Available Qty">Qty:</label>
    	<div class="col-sm-1">
        <INPUT class="form-control" name="QTY" type="TEXT" id="qty"	value="" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" size="1" maxlength="10">
        </div>
        <div class="col-sm-1" style="right:10px">
         <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 115%" id="UOM" onchange="getAvaliableInventoryQty();">
			
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
	 	</div>
		<input type="hidden" name="QTY_0" value="">	
		<input type="hidden" name="ITEMDESC" value="">
			<input type="hidden" name="DISCITEM" value="" />	

<table>
		<td ALIGN="left" >
			<div id="add">
			<button type="submit" class="Submit btn btn-default"  id="addbtn" name="action"  onClick="return addaction()"><b>Add</b></button>&nbsp;
			
			</div>
			<input type="hidden" name="action1" value="temp">
			</td>
			<td ALIGN="left" >
			<button type="submit" class="Submit btn btn-default" name="action"  onClick="return delaction()"><b>Delete</b></button>&nbsp;
			
			</td>
			<td ALIGN="left" >
			<button type="submit" class="Submit btn btn-default" name="action" value="Hold" onClick="return holdaction()"><b>Hold</b></button>&nbsp;
			</td>
			<!-- <td ALIGN="left" >
			<input class="btn btn-sm btnStyle" type="button" name="action" value="Submit" onclick="return printaction()">
			</td> -->
			
</table>
	<div class="row"><div class="col-sm-12">
	<table id="datatable-column-filter" class="table table-bordered table-hover dataTable no-footer">
	<thead>
		<TH style="width:5%;">Chk</TH>
		<TH style="width:8%;">Product ID</TH>
		<TH style="width:12%;">Product Description</TH>
		<TH width="10%">UOM</TH>
		<TH style="width:6%;">Batch</TH>
		<TH style="width:7%;">Quantity</TH>
		
		
	</thead>
	<% 
	  // 
	  if(poslist!=null && (poslist.size()>0)){
		  System.out.println("poslist.size"+poslist.size());
    	 for(int k=0;k<poslist.size();k++)
         {
        	 ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         STOCKQTY = String.valueOf(itemord.getStkqty());
			 STOCKQTY = StrUtils.formatNum(STOCKQTY);
			if(cmd.equalsIgnoreCase("ViewTran"))
			 {
				
				 LOC = itemord.getLoc();
				 LOCDESC = itemord.getLocDesc();
				 EMP_ID = itemord.getEmpNo();
				 refNO = itemord.getRefNo();
				 TRANSACTIONDATE = itemord.getTranDate();
				 REASONCODE = itemord.getReasonCode();
				 REMARKS = itemord.getRemarks();
				 EMP_NAME = itemord.getEmpName();
			 }
			 
		%>
	<TR bgcolor="">
		<TD align="left" width="3%">&nbsp;<font class="textbold"><input
			type="checkbox" name="chk" value="<%=k%>"></input></TD>
		<TD align="left" width="7%">&nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
		<TD align="left" width="19%" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>
		<TD align="left" width="16%" class="textbold">&nbsp; <%=itemord.getSTKUOM()%></TD>		
		<TD align="left" width="15%" class="textbold">&nbsp; <%=itemord.getBATCH()%></TD>
		<TD align="right" width="7%" class="textbold">&nbsp;<%=STOCKQTY%></TD>
		
	</TR>
	<%
         
         }} %>
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
					<INPUT class="form-control" name="EMP_ID" id="EMP_ID" type = "TEXT" value="<%=EMP_ID%>" size="20"  MAXLENGTH=20>
		            <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_ID='+form.EMP_ID.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
		      	
		      </div>
		</td>
	<th class="productlabel" style="font-size: 15px;" ALIGN="left">Employee Name:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
					<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=20>
		            <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
		      		
					<input type="hidden" name="EMP_LNAME" value="" />
		      </div>
		</td>	


<TH class="productlabel" style="font-size: 15px;" ALIGN="right">Reference No:</TH>
<td>
<div class="input-group col-sm-11 productdiv">
<INPUT class="form-control" name="REFERENCENO" type="TEXT" id="REFERENCENO" value="<%=refNO%>" size="20" MAXLENGTH=20>
</div>
</TD>
<th class="productlabel" style="font-size: 15px;" ALIGN="left">Reason Code:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
				<INPUT class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}"> 
			 <span   class="input-group-addon " onClick="javascript:popUpWin('../jsp/list/ReasonMstList.jsp?ITEM_ID='+form.REASONCODE.value+'&TYPE=KITDEKIT');">	
			 <a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
			 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
			</div>
		</td>	
</tr>

</table>
<br>
<table>
	<tr >
		<th  class="productlabel" style="font-size: 15px;" ALIGN="left">Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<td >
			<div class="input-group col-sm-12 productdiv"  > 
		
			<textarea class="form-control" style="width:212px;" maxlength="99" id="REMARKS" name="REMARKS" type="TEXT" rows="1" value=""  ><%=REMARKS%></textarea>
			   
			   </div>
		</td>
<!-- 	<th  class="productlabel" style="font-size: 15px;" ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Transaction Date:&nbsp;&nbsp;&nbsp;</th> -->
	<th  class="productlabel" style="font-size: 15px;" ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Transaction Date:&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></th>
		<td >
			<div class="input-group col-sm-12 productdiv"  > 
		
					<%if(SETCURRENTDATE_PICKANDISSUE.equals("1")){%>
					<INPUT class="form-control datepicker " name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" readonly MAXLENGTH="80" >
					 <%}else {%>
					<INPUT class="form-control datepicker " name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="" type="TEXT" size="10" readonly MAXLENGTH="80" >
			 <%}%>
			   </div>
		</td>
	
</tr>
	</table>		  
	
			<br>
		<div class="form-group">        
      	<div class="col-sm-12" align="center">
<!--       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
		<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; --> 
			<button type="button" class="Submit btn btn-default" name="action" onClick="return printaction()"><b>Submit</b></button>&nbsp;&nbsp;
			</div>
			
			</div>   
</FORM>
</div></div></div>
<% if(serialized.equalsIgnoreCase("1")) {%>
<script type="text/javascript">
      document.getElementById("serialized").checked==true
	  document.getElementById("defualtQty").checked=true
      document.getElementById("defualtQty").disabled = true;
	  document.getElementById("qty").readOnly = true;
	  document.getElementById("qty").value=1;
</script>
<%}else{ %>
<script type="text/javascript">
 	document.getElementById("defualtQty").checked=false;
	document.getElementById("defualtQty").disabled = false;
	document.getElementById("qty").readOnly = false;
	var qtyValue = document.getElementById("qty").value;
	 document.getElementById("qty").innerHTML =qtyValue;
</script>
<%}%>

<script type="text/javascript">
function itempopUpwin(){
	  var loc = document.form.LOC.value;	
		
	  
	  if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.LOC.focus();
		  return false;
	  }else{
		
		
		 popUpWin('../jsp/list/itemList.jsp?ITEM='+form.ITEM.value+'&LOC0='+loc);
		
	  }
	
	}
$(document).ready(function() {
	
	document.getElementById("qty").value =1;
	
	if (localStorage.getItem("defQtyChk") == true || localStorage.getItem("defQtyChk") == 'true'){
		document.getElementById("defualtQty").checked=localStorage.getItem("defQtyChk");
		if(document.getElementById("defualtQty").checked==true){
			document.getElementById("qty").readOnly = true;
			
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
	<%if ("1".equals(request.getParameter("bulkcheckout"))){%>
		$('#bulkcheckout').prop('checked', 'checked');
	<%}%>	
	$('.datatable').dataTable({
	sDom: "R"+
			
			" <'col-sm-12'f>",
				 "bPaginate": false,
				 "bInfo": false,
				 "oLanguage": {"sEmptyTable": " No data available"}
   	});
   	
onNew();	
   	
$(".resizetable").colResizable({
	liveDrag:true, 
	gripInnerHtml:"<div class='grip'></div>", 
	draggingClass:"dragging", 
    resizeMode:'fit'
}); 

});
</script>
<!-- <script src="assets/js/jquery.dataTables.min.js"></script>	 -->
	<script src="assets/js/dataTables.colVis.bootstrap.js"></script>	
	<script src="assets/js/dataTables.colReorder.min.js"></script>	
	<script src="assets/js/dataTables.tableTools.min.js"></script>	
	<script src="assets/js/dataTables.bootstrap.js"></script>
	<script src="assets/js/colResizable-1.6.js"></script>
<script type="text/javascript">
$(window).on('beforeunload', function() {
	
	
	
	localStorage.setItem("defQtyChk",document.getElementById("defualtQty").checked );
	
    
	localStorage.setItem("qtyVal",document.getElementById("qty").value);
	 
});

  setfocus();var index=0;
  /*if( document.form.ITEM.value.length>0)
	{
			alert("setting focus");
		 
	  }*/
  function setfocus()
  {	 
	  document.form.TRANID.focus();
	 // DisplayBatch();
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
  function addaction()
  {
	 var empname =document.form.EMP_NAME.value;
	  var item = document.form.ITEM.value;	
	  var loc = document.form.LOC.value;	
	  var tranid = document.form.TRANID.value;	
	  var scanqty = document.form.QTY.value;	
	  var availqty = document.form.AVAILQTY.value;
	  var qtyValue=document.form.qty.value;
	  
	  if((tranid==null||tranid=="")&&(loc==null||loc==""))
	  {
		  alert("Please create Transaction ID & select location before adding product!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(tranid==null||tranid=="")
	  {
		  alert("Please Enter Tran Id!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.LOC.focus();
		  return false;
	  }
	  if(item==null||item=="")
	  {
		  alert("Please Scan Product ID!");
		  document.form.ITEM.focus();
		  return false;
	  }
      if(document.form.UOM.value == ""){
     	    alert("Please enter a UOM value");
     	    document.form.UOM.focus();
     	    document.form.UOM.select();
     	    return false;
     }
      if(scanqty==null||scanqty==""||scanqty=="0")
	  {
		  alert("Please Enter Qty!");
		  document.form.QTY.focus();
		  return false;
	  }
	  if(parseFloat(scanqty)>parseFloat(availqty)){
     	alert("Scanning Quantity Should not be Greater than Availabe Quantity");
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
				  document.getElementById("Add").focus();
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
			  //var exprydate = document.getElementById("EXPIREDATE").value;
			  var trnsdate = document.getElementById("TRANSACTIONDATE").value;
	
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
			alert("Must Select at least one Product which you want to Delete");
			return false;
		}
	  document.form.cmd.value="Delete" ;
	  document.form.action  = "/track/DynamicProductServlet?cmd=Delete";
	  /* document.form.submit(); */
	  return true;
	 }     
  
  function printaction(){
	  
	var item=document.form.chk;
	 var itemvalue=document.getElementById("item").value;
	  var RecvtranID =document.getElementById("TRANID").value;
	  var dates =document.getElementById("TRANSACTIONDATE").value;
	  var qtyValue=document.form.qty.value;
	  if(item == undefined){
		 alert("Add Product to Print");
		 return false;
	  }
	  var tranid=document.form.TRANID.value;
	  if(tranid==null||tranid==""){
		 alert("Generate Transaction Id");
		 return false;
	  }
	  if(dates==null||dates==""){
		  alert("Please select Transaction Date");
		 return false;
	  }

	        var formData = $('form#dynamicprdswithoutpriceForm').serialize();

				$.ajax({
					type : 'get',
					url : '/track/DynamicProductServlet?cmd=printproduct',
					dataType : 'html',
					responseType: 'arraybuffer',
					data : formData,

					success : function(data, status, xhr) {
			var result = data.split(":");
			if(result[0] == "Success" && result[2] == "1")
			{
						document.form.action="/track/DynamicProductServlet?";
		 	         	document.form.cmd.value="printproduct" ;
		 	         	document.form.RCPNO.value=result[1];
						document.form.submit();
						onNewPOS();
						if(result[3] == "Warning"){
							setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully. <br><span style=\"color: orange;\">Product  ["+result[4]+"] reached the minimun Inventory Quantity</span>"); }, 1000);
						}else{
							setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully."); }, 1000);
						}
			}
			else if(result[0] == "Success" && result[2] == "0"){
				onNewPOS();
				if(result[3] == "Warning"){
					setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully. <br><span style=\"color: orange;\">Product  ["+result[4]+"] reached the minimun Inventory Quantity</span>"); }, 1000);
				}else{
					setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully 1."); }, 1000);
				}
			}
			else{
				$('#appendbody').html(data);
				if(result[3] == "Warning"){
					setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully. <br><span style=\"color: orange;\">Product  ["+result[4]+"] reached the minimun Inventory Quantity</span>"); }, 1000);
				}else{
					setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully."); }, 1000);
				}
// 				setTimeout(function(){ $('#appenddiv').html("Goods issued for the transaction  "+  RecvtranID + " completed successfully."); }, 1000);
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
		var RecvtranID =document.getElementById("TRANID").value;
		  var item=document.form.chk;
		  if(item == undefined){
			  alert("Add Product to Hold the Transaction");
			  return false;
		  }
		  var formData = $('form#dynamicprdswithoutpriceForm').serialize(); 
		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?action=Hold',
		      //  url: '/track/DynamicProductServlet?action=ADD&CHKBATCH='+batchstatus,
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
 		
 		
		  document.form.cmd.value="NEWTRANID" ;
		  document.getElementById("qty").value=1;
	 		
	 		
	 		$('#defualtQty').prop('checked', false);
		   document.form.action  = "/track/DynamicProductServlet?cmd=NEWTRANID";
		   document.form.submit();
		}
 	function onNew()
 	{
 		
 		var urlStr = "/track/OutboundOrderHandlerServlet";
 		$.ajax( {
 			type : "POST",
 			url : urlStr,
 			data : {
 			PLANT : "<%=PLANT%>",
 			ACTION : "GINO"
 			},
 			dataType : "json",
 			success : function(data) {
 				if (data.status == "100") {
 					var resultVal = data.result;				
 					var resultV = resultVal.invno;
 					document.form.TRANID.value= resultV;
 		
 				} else {
 					alert("Unable to genarate GINO");
 					document.form.TRANID.value = "";
 				}
 			}
 		});	
 		}
	function ClearonNew(){
		 document.form.LOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     //document.form.REASONCODE.value="";
	     //document.form.TRANSACTIONDATE.value="";
	     document.form.REMARKS.value="";
	    // document.form.EXPIREDATE.value="";
	     document.form.REFERENCENO.value="";
	     document.form.LOCDESC.value="";
	}
	function onNewPOS()
	{    
	     document.form.TRANID.value="";
	     document.form.LOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REASONCODE.value="";
	     document.form.TRANSACTIONDATE.value="";
	     document.form.REMARKS.value="";
	     document.form.REFERENCENO.value="";
	     document.form.LOCDESC.value="";
		 document.form.cmd.value="NewPOS" ;
	 
			var formData = $('form#dynamicprdswithoutpriceForm').serialize(); 

		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?cmd=NewPOS',
		        dataType:'html',
		        data:  formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		            
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    /* document.form.submit(); */
	}

	  function onView()
		{
	       var tranid = document.form.TRANID.value;	 	
		  if(tranid==null||tranid=="")
		  {
			  alert("Please Select TranID");
			  return false;
		  }else{ 
			  
			   document.form.cmd.value="ViewTran" ;
			   document.form.action  = "/track/DynamicProductServlet?cmd=ViewTran";
			   document.form.submit();
		  }
			   
	       
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
		
		});
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
			    }
		  }
		  function getUOMAvaliableInventoryQty(uom)
		  {
			  var itemValue = document.getElementById("item").value;
			  var locValue = document.getElementById("LOC").value;
			  var batch = document.getElementById("BATCH_0").value;	 
			  			  
				  var urlStr = "/track/ItemMstServlet";
				  
				  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",LOC:locValue,BATCH:batch,UOM:uom, ACTION: "PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetInventoryQty }); 
			  
			  
			
		  }
		  function getAvaliableInventoryQty()
		  {
			  var itemValue = document.getElementById("item").value;
			  var locValue = document.getElementById("LOC").value;
			  var batch = document.getElementById("BATCH_0").value;	 
			  var uom = document.getElementById("UOM").value;			  
				  var urlStr = "/track/ItemMstServlet";
				  
				  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",LOC:locValue,BATCH:batch,UOM:uom, ACTION: "PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetInventoryQty }); 
			  
			  
			
		  }
		  function onGetInventoryQty(data){
			  
			  var errorBoo = false;
				$.each(data.errors, function(i,error){
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						document.getElementById("AVAILQTY").value=0;
					
					}
				});
				
				if(errorBoo == false){
		        	//console.log(data.result.qty);
		        	document.getElementById("AVAILQTY").value = data.result.QTY;
		        }
			  
		  }
		   var elem3 = document.getElementById("LOC");
			  elem3.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	//  document.getElementById("item").focus(); 
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
				    	  getEmployeeName(elem4.value);
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
				  var elem7 = document.getElementById("TRANID");
				  elem7.onkeyup = function(e){
				      if(e.keyCode == 13){
				    	    var tranid = document.form.TRANID.value;	 	
				  		  if(tranid==null||tranid=="")
				  		  {
				  			  alert("Please Select TranID");
				  			 document.getElementById("TRANID").focus(); 
				  			  return false;
				  		  }else{
				    	  document.getElementById("LOC").focus(); 
				  		  }
				      }
				  }
				  function getEmployeeName(empId)
				  {
					  
					  $.ajax( {
							type : "POST",
							url : "EmployeeName.jsp",
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "View",
							EMPID:empId
						},
						dataType : "json",
						success : function(data) {
							
							if(data.ERROR_CODE=="99")
								{
								
								$("#errorMessage").html("Employee Id Not exists");
								}
							else
								{
									$("#errorMessage").html("");
									document.getElementById("EMP_NAME").value = data.EMP_NAME;
								}
							
							
							
						},
						error:function(data)
						{
							
						}
					});
				  }
				 
				  function getLocation(loc)
				  {
					  var tranid = document.form.TRANID.value;	
					  $.ajax( {
							type : "get",
							url : '/track/DynamicProductServlet?action=GETLOC',
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "GETLOC",
							TRANTYPE:"GOODSISSUEWITHBATCH",
							LOC:loc
						},
						dataType : "html",
						success : function(data) {
							$("#appendbody").html(data);
						var errorValue =document.getElementById("iserrorVal").value;
							
				
							document.getElementById("TRANID").value=tranid;
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
								var salesuom= data.result.SALESUOM;
				        	document.getElementById("UOM").value =salesuom;
				        	getUOMAvaliableInventoryQty(salesuom);
				        }
						}
					  
				  }
			 	function validateProduct()
			 	{
			 		GetUOM();
			 		 getAvaliableInventoryQty();			 		
			 	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>