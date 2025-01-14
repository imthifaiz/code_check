<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="java.util.*" session="true"%>
<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%@ page import="com.track.dao.*"%>
<!-- Not in Use - Menus status 0 -->
<%
String title = "Stock Move";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<style type="text/css">
* {EMP_NAME
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
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
	<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/autosize.js"></script>
<script type="text/javascript">
 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
</script>
<script>
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
</script>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
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
 	 String fieldDesc="",cursymbol="",DISCITEM="",discountDesc="",cmd="",TRANTYPE="";
	 String refNO="",PLANT="",ITEM ="",FROM_LOC= "",TO_LOC="",FROM_LOCDESC="",TOLOCDESC="",iserrorVal="",ITEM_DESC="",SCANQTY="",LOC="",REASONCODE="",EMP_NAME="", TRANSACTIONDATE="",EMP_ID="", disccnt="",STOCKQTY="",REMARKS="",gsttax="",action="";
	 String html = "",BATCH="",CHKBATCH="";float gstf=0;
	 int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;String unitprice="",totalprice="",cntlDiscount="",REFERENCENO="";
	 PLANT = (String)session.getAttribute("PLANT");
	 String sUserId = (String) session.getAttribute("LOGIN_USER");
	 String SumColor=""; 
	 Vector poslist=null;
	 boolean flag=false;
	 sb.setmLogger(mLogger1);
	 String gst = sb.getGST("POS",PLANT);
	 cursymbol = DbBean.CURRENCYSYMBOL;
	 float unitpc=0,totalpc=0,  gstvalCalc=0, totalsum=0,msprice=0;
	 action = StrUtils.fString(request.getParameter("action")).trim();
	 String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
	 LOC = StrUtils.fString(request.getParameter("LOC")).trim();
	 BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
     REMARKS = StrUtils.fString(request.getParameter("REMARKS")).trim();
     CHKBATCH = StrUtils.fString(request.getParameter("CHKBATCH"));
     REASONCODE  = strUtils.fString(request.getParameter("REASONCODE"));
     REFERENCENO=strUtils.fString(request.getParameter("REFERENCENO"));
     EMP_ID  = strUtils.fString(request.getParameter("EMP_ID"));
     EMP_NAME  = strUtils.fString(request.getParameter("EMP_NAME"));
     TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
     FROM_LOC = strUtils.fString(request.getParameter("FROM_LOC")).trim();
     TO_LOC = strUtils.fString(request.getParameter("TO_LOC"));
	 FROM_LOCDESC = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();	 
	 TOLOCDESC = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
	 iserrorVal = StrUtils.fString(request.getParameter("iserrorVal")).trim();
	 TRANTYPE=StrUtils.fString(request.getParameter("TRANTYPE")).trim();
	 cmd =strUtils.fString(request.getParameter("cmd"));
	 String uom = su.fString(request.getParameter("UOM"));
     DateUtils _dateUtils = new DateUtils();
     String curDate =_dateUtils.getDate();
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
		//poslist = (Vector)session.getAttribute("poslist");
	      session.setAttribute("tranid",sTranId);
        }
	else{
		session.putValue("poslist", null);
	}
	SCANQTY="1";
	
	if((String)session.getAttribute("errmsg")!=null)
	{
	   fieldDesc= (String)session.getAttribute("errmsg");
	   session.setAttribute("errmsg","");
	   gstf = Float.parseFloat(gst);
	}
	      
%>
	<% 
		if(poslist!=null && (poslist.size()>0)){
			
			if(cmd.equalsIgnoreCase("ADD") || cmd.equalsIgnoreCase("delete"))
				
			{ %>		
				 <script type="text/javascript">
			      document.addEventListener("DOMContentLoaded", function() {		   	  
			       document.getElementById("item").focus();
			      });
			       	</script>
			<%}%>
			
			<%
			
		if(cmd.equalsIgnoreCase("ViewTran")){
    	 for(int k=0;k<poslist.size();k++)
         {
        	 //pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         LOC = itemord.getLoc();
	         FROM_LOCDESC = itemord.getLocDesc();
	         TOLOCDESC = itemord.getToLocDesc();
	         FROM_LOC = itemord.getFromLoc();
	         TO_LOC = itemord.getToLoc();
         }
	}
	}
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Check Company Industry
	if(FROM_LOC.equalsIgnoreCase("") && FROM_LOCDESC.equalsIgnoreCase(""))
	{
		if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
		{
			//FROM_LOC="FREEZER1";
			//FROM_LOCDESC="FREEZER 1";
			TO_LOC="PROCESSING";
			TOLOCDESC="PROCESSING";
		}
	}
	
	if(BATCH.equalsIgnoreCase(""))
		BATCH="NOBATCH"; 
%>
<center>
	<h2>
		<small class="success-msg"></small>
	</h2>
</center>
<center><div style="color:green;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"></div></center>
<center class="mainred"><%=fieldDesc%></center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inhouse/stockmove"><span class="underline-on-hover">Stock Move Summary</span> </a></li>                
                <li><label>New Stock Move</label></li>                                    
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../inhouse/stockmove'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<!-- <div class='itemtable container ' style="background-color:#fff;width:98%;height:auto;overflow-x:scroll;margin-left:10px;">
<div class ="panel-heading" style="padding-bottom:0px;">
		<h4 style="font-family:'Ubuntu',sans-serif ! important;font-size:22px;margin-bottom:0px;">Stock Move</h4>
	</div> -->
		<center>
<div id="errorMessage" class="mainred">
 
</div>
</center>
<form class="form-horizontal" name="form" method="get" id="dynamicprdswithoutpriceForm" action="">
<input hidden value="1" name="flagwithbatch" id="flagwithbatch">
<input hidden value="1" name="flagissuewithbatch" id="flagissuewithbatch">
<input hidden value="1" name="goodsissuewithbatch" id="goodsissuewithbatch">
<input hidden value="<%=sUserId %>" name="LOGIN_USER" id="LOGIN_USER">
<input hidden value="1" name="flagwithstockmovewithbatch" id="flagwithstockmovewithbatch">
<input type="hidden" id="iserrorVal" value="<%=iserrorVal%>">
<input type="hidden" id="myurl" name="myurl"/>
<br>

<center>
<div class="mainred" id="errorMessage">
</div>
</center>


<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
<INPUT type="hidden" name="RCPNO" value="" />
<!-- <INPUT type="hidden" name="POS_TYPE" value="WITHOUTPRICE" /> -->
<INPUT type="hidden" name="TRANTYPE" id="TRANTYPE" value="MOVEWITHBATCH" />

<TABLE WIDTH="100%" border="0" cellspacing="1" cellpadding="2"	align="center">
	<TR >
	
		<TH  width="15%" style="text-align:left;"  class="productlabel"  ALIGN="left" >Transaction ID :</TH>
		<TD width="30%">
			<div class="input-group col-sm-8 productdiv">
				<%-- <INPUT class="form-control"  name="TRANID" type="TEXT" id="TRANID" value="<%=sTranId%>" size="20" MAXLENGTH=50>
		<span   class="input-group-addon "><a href="#" data-toggle="tooltip" data-placement="top" title="Transaction Details"	onClick="javascript:popUpWin('../jsp/list/posTranIDList.jsp?TYPE=MOVEWITHBATCH');">
		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
		
		<input type="text" class="form-control" id="TRANID" name="TRANID" value="<%=sTranId%>" size="20" MAXLENGTH=50>
<!-- 							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'TRANID\']').focus()"></span> -->
								<span class="input-group-addon" onClick="onGenID()"> 
								<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 	<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i></a>
				   		 		</span>
				   		 		
			</div>
			</TD>
		

			
		<TD width="30%"><button type="button" class="Submit btn btn-default" onClick="onView();"><b>View</b></button>&nbsp;
<!-- 		<button type="button" class="Submit btn btn-default" onClick="onGenID();"><b>New</b></button>&nbsp; -->
			<button type="button" class="Submit btn btn-default" onClick="onNewPOS();"><b>Clear All</b></button>&nbsp;
			</TD>
		
	</TR>
	
	 <TR>
		         <TH width="15%" class="productlabel"  ALIGN="RIGHT" >From Location&nbsp;&nbsp;: </TH>
		            <TD width="30">
		           <div class="input-group col-sm-8 productdiv">
		             <%--    <INPUT class="form-control"  name="FROM_LOC" id="FROMLOC" type = "TEXT" value="<%=FROM_LOC%>" size="50" 
		            onkeypress="if((event.keyCode=='13') && ( document.form.FROM_LOC.value.length > 0)) {validateLocation(this.value);}"    MAXLENGTH="20">
		             <span  class="input-group-addon ">   <a href="#" data-toggle="tooltip" data-placement="top" title="Location Details" onClick="javascript:popUpWin('../jsp/loc_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value);"><i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
		             
		             <input type="text" class="form-control" id="FROMLOC" name="FROM_LOC"  value="<%=FROM_LOC%>" onchange="checkfromloc(this.value)">
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'FROMLOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
		          </div>
		          </TD>
		          
		    </TR>
		    <tr>
		    
						<TH width="15%"  class="productlabel" ALIGN="left" >Location Description:</TH>
		<TD width="30%" >
		<div class="input-group col-sm-8 productdiv">
			<INPUT readonly name="FROM_LOCDESC" class="form-control" id="FROM_LOCDESC" type="TEXT" value="" size="20" MAXLENGTH=20>			
			
		</div>
			</td>	
		    </tr>
		    <TR>
		         <TH width="15%" class="productlabel" ALIGN="RIGHT" >To Location&nbsp;&nbsp;: </TH>
		         <TD width="30%">
		         <div class="input-group col-sm-8 productdiv">
		           <%-- <INPUT class="form-control" name="TOLOC" id="TOLOC" type="TEXT" value="<%=TO_LOC%>" size="50"  onkeypress="if((event.keyCode=='13') && ( document.form.TOLOC.value.length > 0)) {validateTOLocation(this.value);}"  MAXLENGTH="80" />
		            <span  class="input-group-addon "> <a href="#" data-toggle="tooltip" data-placement="top" title="Location Details" onClick="javascript:popUpWin('../jsp/loc_list_dotransferto.jsp?TOLOC='+form.TOLOC.value+'&TYPE=STOCKMOVE');">
		           <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
		       
		       <input type="text" class="form-control" id="TOLOC" name="TOLOC" value="<%=TO_LOC%>" onchange="checktoloc(this.value)">
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'TOLOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
							 
		         </div>
		         </TD>
		   
		    </TR>
	<tr>
	      
						<TH  width="15%"   class="productlabel" ALIGN="left" >To Location Description:</TH>
		<TD width="30%" >
		<div class="input-group col-sm-8 productdiv">
			<INPUT readonly name="TOLOCDESC" class="form-control" id="TOLOCDESC"TEXT" value="" size="20" MAXLENGTH=20>			
			
		</div>
			</td>	
	</tr>

	</table>
	  <br>
	<table>
	<tr>
	<td>
		 <INPUT Type=Checkbox  style="border:0;" name = "serialized" id="serialized" value="1" onchange="serial();">
                     <b>Serialized    &nbsp;</b> </input>
     </td>
     <td>
	 <INPUT Type=Checkbox  style="border:0;" name = "defualtQty" id="defualtQty" value="1" onchange="DefaultQty();">
                      <b>Default Quantity &nbsp;</b></input>
       </td>
         <td>
       <INPUT Type=hidden  style="border:0;" name = "bulkcheckout" id="bulkcheckout" value="1" onclick="BulkCheckout();">
         </input>
           </td>
      </tr>
     </table>
     <br>
     <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
	<TR>
			<TH  class="productlabel" ALIGN="left" >Scan Product ID: </TH>
			<TD  >
			<div class="input-group col-sm-11 productdiv">
			<%-- <INPUT class="form-control" name="ITEM" type="TEXT" id="item" value="<%=ITEM%>" size="20" MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return Itemkeypress()}"  onchange="Itemkeypress()"><span   class="input-group-addon "><a href="#"
			data-toggle="tooltip" data-placement="top" title="Product ID Details"	onClick="itempopUpwin()">
				 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
				 
<!-- 				 <input type="text" name="ITEM" id="item" class="ac-selected form-control"   onchange="checkitems(this.value,this)" onClick="validateProduct()"> -->
				 <input type="text" name="ITEM" id="item" class="ac-selected form-control"  value="<%=ITEM%>" onclick="Itemkeypress()" onchange="Itemkeypress">
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
			</TD>
			<TH   class="productlabel"  ALIGN="left" >Batch:</TH>
			<TD >
			<div class="input-group col-sm-8 productdiv">
<%-- 			<INPUT class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT"
				value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){Batchkeypress();}"
				MAXLENGTH=40><input type="hidden" name="BATCH_ID_0" value="-1" />
				<span   class="input-group-addon "><a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details" onClick="javascript:popUpWin('../jsp/list/OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEM.value+'&LOC0='+form.FROMLOC.value+'&BATCH0=&INDEX='+'0'+'&TYPE=MOVEWITHBATCH'+'&UOM='+form.UOM.value);">
			<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
			
				 <input type="text" name="BATCH_0" id="BATCH_0" value="<%=BATCH%>" class="ac-selected form-control" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH_0\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			
			</div>
			</TD> 
			<TH  class="productlabel" ALIGN="left" >Available Qty: </TH>
			<td>
			<div class="input-group col-sm-8 productdiv">			
			<INPUT class="form-control" readonly name="AVAILQTY" type="TEXT" id="AVAILQTY"	value="" size="3" maxlength="10">
			<input type="hidden" name="QTY_0" value="">
			</div>
			</td>
			
			<TD  >
			<div class="input-group col-sm-8 productdiv">
			<INPUT class="form-control" name="QTY" type="TEXT" id="qty"	value="<%=SCANQTY%>" size="3" maxlength="10">
			</div>
			</td>
			<TD >
			<div class="input-group col-sm-8 productdiv">
			<SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 100%" id="UOM" onchange="getAvaliableInventoryQty();">
			
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
	 
			<input type="hidden" name="ITEMDESC" value="">
			<input type="hidden" name="DISCITEM" value="" />
			<input type="checkbox" hidden name="chkbatch" id="chkbatch" checked />
	</td>	
</table>


 <table>
		<td ALIGN="left" >
		<div id="add">
		 <button type="button" class="Submit btn btn-default" name="action" id="addbtn" onclick="return addaction()"><b>Add</b></button>&nbsp;
			</div>
			<input type="hidden" name="action1" value="temp">
			</td>&nbsp;&nbsp;
			<td ALIGN="left" >
			<button type="button" class="Submit btn btn-default" name="action" onClick="return delaction()"><b>Delete</b></button>&nbsp;
		<!-- 	<input class="btn btn-sm btnStyle" type="Submit" name="action"	value="Delete" onclick="return delaction()"> -->
			</td>&nbsp;&nbsp;
			<td ALIGN="left" >
			<button type="button" class="Submit btn btn-default" name="action"  onclick="return holdaction()"><b>Hold</b></button>&nbsp;
					
			
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
	<% if(poslist!=null && (poslist.size()>0)){
		for(int k=0;k<poslist.size();k++)
         {
        	 //pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         STOCKQTY = String.valueOf(itemord.getStkqty());
			 STOCKQTY = StrUtils.formatNum(STOCKQTY);
			 if(cmd.equalsIgnoreCase("ViewTran")){
				 EMP_ID = itemord.getEmpNo();
				 REFERENCENO = itemord.getRefNo();
				 TRANSACTIONDATE = itemord.getTranDate();
				 REASONCODE = itemord.getReasonCode();
				 REMARKS = itemord.getRemarks();
				 EMP_NAME = itemord.getEmpName();
				 FROM_LOCDESC = itemord.getLocDesc();
		         TOLOCDESC = itemord.getToLocDesc();
		         FROM_LOC = itemord.getFromLoc();
		         TO_LOC = itemord.getToLoc();
			 }
			 %>
	<TR bgcolor="">
		<TD align="left" width="3%">&nbsp;<font class="textbold"><input
			type="checkbox" name="chk" value="<%=k%>"></input></TD>
		<TD align="left" width="7%">&nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
		<TD align="left" width="19%" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>
		<TD align="left" width="16%" class="textbold">&nbsp; <%=itemord.getSTKUOM()%></TD>
		<TD align="left" width="15%" class="textbold">&nbsp; <%=itemord.getBATCH()%></TD>
		<TD align="center" width="7%" class="textbold">&nbsp;<%=STOCKQTY%></TD>
		
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
	<th  class="productlabel" ALIGN="left">Employee ID:</th>
		<td>
			<div class="input-group col-sm-11 productdiv">
					<%-- <INPUT class="form-control" name="EMP_ID" id="EMP_ID" type = "TEXT" value="<%=EMP_ID%>" size="20"  MAXLENGTH=20>
		            <span   class="input-group-addon "> <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details" onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_ID='+form.EMP_ID.value+'&TYPE=KITDEKIT&FORM=form');">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
		      	
		      	<input type="text" class="form-control" id="EMP_ID" name="EMP_ID" onchange="checkemployeess(this.value)" value="">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'EMP_ID\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
		      </div>
		</td>
	<th class="productlabel" ALIGN="left">Employee Name:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
					<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="" size="20" readonly MAXLENGTH=20>
		            <!-- <span   class="input-group-addon "> <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details" onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=KITDEKIT&FORM=form');">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
		      		
					<input type="hidden" name="EMP_LNAME" value="" />
		      </div>
		</td>	


<TH class="productlabel" ALIGN="right">Reference No:</TH>
<td>
<div class="input-group col-sm-11 productdiv">
<INPUT class="form-control" name="REFERENCENO" type="TEXT" id="REFERENCENO" value="<%=REFERENCENO%>" size="20" MAXLENGTH=20>
</div>
</TD>
<th class="productlabel" ALIGN="left">Reason Code:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
			<%-- 	<INPUT class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}"> 
			 <span   class="input-group-addon ">	<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details" onClick="javascript:popUpWin('../jsp/list/ReasonMstList.jsp?ITEM_ID='+form.REASONCODE.value+'&TYPE=KITDEKIT');">
			 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> --%>
			 
			 <input type="text" class="form-control" id="REASONCODE" name="REASONCODE" value="">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'REASONCODE\']').focus()"> <i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</td>	
</tr>

			
</table>
<br>
<table>
	<tr >
		<th  class="productlabel" ALIGN="left">Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<td >
			<div class="input-group col-sm-12 productdiv"  > 
		
			<textarea class="form-control" style="width:212px;" maxlength="99" id="REMARKS" name="REMARKS" type="TEXT" rows="1" value=""  ><%=REMARKS%></textarea>
			   
			   </div>
		</td>
	<th  class="productlabel" ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Transaction Date:&nbsp;&nbsp;&nbsp;</th>
		<td >
			<div class="input-group col-sm-12 productdiv"  > 
		
					<INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" readonly MAXLENGTH="80" >
			
			   
			   </div>
		</td>
	
</tr>
	</table>		  
	
			<br> 
			<center>
			<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
			<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button> -->
			<button type="button" class="Submit btn btn-default" name="action"  onclick="return printaction()"><b>Submit</b></button>&nbsp;
			   <!-- <input class="btn btn-sm btnStyle" type="button" name="action" value="Submit" onclick="return printaction()"> --></center>

</FORM>
</div>
<script >
$(document).ready(function() {

	document.getElementById('myurl').value = window.location.href
	AutoID();
	
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
   	
   	
   	
/* $(".resizetable").colResizable({
	liveDrag:true, 
	gripInnerHtml:"<div class='grip'></div>", 
	draggingClass:"dragging", 
    resizeMode:'fit'
}); */     
});
</script>
<script src="assets/js/jquery.dataTables.min.js"></script>	
	<script src="assets/js/dataTables.colVis.bootstrap.js"></script>	
	<script src="assets/js/dataTables.colReorder.min.js"></script>	
	<script src="assets/js/dataTables.tableTools.min.js"></script>	
	<script src="assets/js/dataTables.bootstrap.js"></script>
	<script src="assets/js/colResizable-1.6.js"></script>
<script language="javascript">
  var index=0;
  if( document.form.ITEM.value.length>0)
	{
			alert("setting focus");
		 
	  }
  function setfocus()
  {	 
	  document.form.FROM_LOC.focus();
	  //document.form.TRANID.focus();
	 // DisplayBatch();
  }
  function Itemkeypress()
    {
	   
      if(document.getElementById("chkbatch").checked == true){
	  if( document.form.ITEM.value.length>0)
	  {
		  var prodId = document.form.ITEM.value;
		  var loc = document.form.FROM_LOC.value;
		  var tloc = document.form.TOLOC.value;
		  if(loc==null||loc=="")
		  {
			  alert("Please Enter From Location!");
			  document.form.FROM_LOC.focus();
			  return false;
		  }
		  if(tloc==null||tloc=="")
		  {
			  alert("Please Enter To Location!");
			  document.form.TOLOC.focus();
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

// 			  document.form.BATCH_0.focus(); 
		  }
		  //document.getElementById("Add").focus();
	  }
      }
      else{document.form.BATCH_0.focus();
		return false;
      }   

  }

  function checkfromloc(loc){	
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
						document.getElementById("FROMLOC").focus();
						$("#FROM_LOC").typeahead('val', '');
						document.getElementById("FROM_LOCDESC").value = "";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checktoloc(loc){	
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
						document.getElementById("TOLOC").focus();
						$("#TOLOC").typeahead('val', '');
						document.getElementById("TOLOCDESC").value = "";
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
					alert("Product Does't Exists");
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
  function validateLocation(locId,locfromto)
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
							if(locfromto=="TOLOC")
							document.getElementById("TOLOCDESC").value = resultVal.locdesc;
							else
							document.getElementById("FROM_LOCDESC").value = resultVal.locdesc;
						} else {
							alert("Not a valid Location");
							if(locfromto=="TOLOC")
								{
							document.getElementById("TOLOC").value = "";
							document.getElementById("TOLOC").focus();
								}
							else
							{
								document.getElementById("FROM_LOC").value = "";
								document.getElementById("FROM_LOC").focus();
							}
						}
					}
				});						
  }
  function addaction()
  {
	  //debugger;
	 var fromlocdesc = document.form.FROM_LOCDESC.value;
	  var tolocdesc = document.form.TOLOCDESC.value;
	 var empname =document.form.EMP_NAME.value;
	  var item = document.form.ITEM.value;	
	  var loc = document.form.FROM_LOC.value;	
	  var toloc = document.form.TOLOC.value;	
	  var tranid = document.form.TRANID.value;	
	  var scanqty = document.form.QTY.value;	
	  var availqty = document.form.AVAILQTY.value;
	  var uom = document.form.UOM.value;
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
		  document.form.FROM_LOC.focus();
		  return false;
	  }
	  if(toloc==null||toloc=="")
	  {
		  alert("Please Enter To Location!");
		  document.form.TOLOC.focus();
		  return false;
	  }
	  if(toloc==loc){
		  alert("Please choose TO LOC different from FROM LOC");
		  document.form.TOLOC.focus();
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
	  if(uom==null||uom=="")
	  {
		  alert("Please Select UOM!");
		  document.form.ITEM.focus();
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
			
			  var loc = document.getElementById("FROMLOC").value;	
			  var toloc = document.getElementById("TOLOC").value;
			  var qty =document.getElementById("qty").value;	
			  var refno = document.getElementById("REFERENCENO").value;
			  var rsncode = document.getElementById("REASONCODE").value;	
			  var remarks = document.getElementById("REMARKS").value;
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
	  
	  document.form.cmd.value="delete" ;
	  document.form.action  = "/track/DynamicProductServlet?cmd=delete";
	 document.form.submit();
	 return false;
	  
	  }     
  
  function printaction(){
	
	  
	var item=document.form.chk;
	 var itemvalue=document.getElementById("item").value;
	  var TOLOCID =document.getElementById("TOLOC").value;
	  if(item == undefined){
		 alert("Add Product to Print");
		 return false;
	  }
	  var tranid=document.form.TRANID.value;
	  if(tranid==null||tranid==""){
		 alert("Generate Transaction Id");
		 return false;
	  }

	        var formData = $('form#dynamicprdswithoutpriceForm').serialize();

				$.ajax({
					type : 'get',
					url : '/track/DynamicProductServlet?cmd=printstockmoveproduct',
					dataType : 'html',
					responseType: 'arraybuffer',
					data : formData,

					success : function(data, status, xhr) {
						
			var result = data.split(":");
			if(result[0] == "Success" && result[2] == "1")
			{
						/*document.form.action="/track/DynamicProductServlet?";
		 	         	document.form.cmd.value="printstockmoveproduct" ;
		 	         	document.form.RCPNO.value=result[1];
						document.form.submit();*/
						onNewPOS();
						setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
			}
			else if(result[0] == "Success" && result[2] == "0"){
				onNewPOS();
				setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
			}
			else{
				$('#appendbody').html(data);
				setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
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
		        url: '/track/DynamicProductServlet?action=StockHold',
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
 		   document.form.cmd.value="MOVENEWTRANID" ;
		   document.form.action  = "/track/DynamicProductServlet?cmd=MOVENEWTRANID";
		   document.form.submit();
	         
		}

 	function AutoID(){
 		var urls = document.form.myurl.value;
 		var tran = document.form.TRANID.value;
 			$.ajax({
		type: "GET",
		url: "../MasterServlet",
		data: {
			"action":'GET_STOCKMOVE_TRANS_ID',
			"URL":urls,
			"TRAN":tran,
		},
		dataType: "json",
		beforeSend: function(){
			showLoader();
		},
		success: function(data) {
			if (data.ERROR_CODE == '100'){
				$("#TRANID").val(data.TRANID);
			}else{
				alert('Unable to get Transaction ID. Please try again later.');
			}
		},
		error: function(data) {
			alert('Unable to get Transaction ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
};
		
	function ClearonNew(){
	   	 document.form.FROM_LOC.value="";
	     document.form.TOLOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REMARKS.value="";
	    // document.form.EXPIREDATE.value="";
	     document.form.REFERENCENO.value="";
	     document.form.FROM_LOCDESC.value="";
		  document.form.TOLOCDESC.value="";
	}
 	 function onNewPOS()
	{    
	     document.form.TRANID.value="";
	     document.form.FROM_LOC.value="";
	     document.form.TOLOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REASONCODE.value="";
	     document.form.TRANSACTIONDATE.value="";
	     document.form.REMARKS.value="";
	     document.form.REFERENCENO.value="";
	     document.form.FROM_LOCDESC.value="";
		  document.form.TOLOCDESC.value="";
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
		    // document.form.submit();
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
		  autosize(document.querySelectorAll('textarea'));
		  $("#BATCH_0").val("NOBATCH");
		  $(document).on('keydown','form input',function(event){
			 // alert("");
		    if(event.keyCode == 13) {
		      event.preventDefault();
		      return false;
		    }
		  });
		  var elemItem = document.getElementById("item");
		  elemItem.onkeyup = function(e){
			  
			  var loc = document.form.FROM_LOC.value;
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
					  
					  document.getElementById("FROMLOC").focus();
					  alert("Please Enter Location!");
					
					  return false;
				  }else{
					  GetUOM();					  
// 				  $( "#BATCH_0" ).focus();
// 				$("#BATCH_0").select();
				  }
			   }
			 } 
		  
		  var elemBATCH_0 = document.getElementById("BATCH_0");
		  elemBATCH_0.onkeyup = function(e){
			  var loc = document.form.FROM_LOC.value; 
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
					  
					  document.getElementById("FROMLOC").focus();
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


		  /* TRANID Auto Suggestion */
			$('#TRANID').typeahead({
				  hint: true,
				  minLength:0,  
				  searchOnFocus: true
				},
				{
				  display: 'TRAN',  
				  source: function (query, process,asyncProcess) {
					var urlStr = "/track/ItemMstServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=PLANT%>",
						ACTION : "GET_TRANID_LIST_FOR_SUGGESTION",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANS);
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
					return '<div onclick="document.form.TRANID.value = \''+data.TRAN+'\'"><p class="item-suggestion">'+data.TRAN+'</p><br/><p class="item-suggestion">No Of Products:'+data.NOPRD+'</p></div>';
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
		 				document.form.TRANID.value = "";
		 				document.getElementById("TRANID").value = "";
		 			}
			});

		  /* From location Auto Suggestion */
			$('#FROMLOC').typeahead({
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
					return '<div onclick="document.form.FROM_LOCDESC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
					}
				  }
				}).on('typeahead:select',function(event,selection){
					if($(this).val() != "")
						document.form.FROM_LOCDESC.value = selection.LOCDESC;
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
		 				document.form.FROM_LOCDESC.value = "";
		 				document.getElementById("FROM_LOCDESC").value = "";
		 			}
			});
			/* To location Auto Suggestion */
			$('#TOLOC').typeahead({
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
					return '<div onclick="document.form.TOLOCDESC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
					}
				  }
				}).on('typeahead:select',function(event,selection){
					if($(this).val() != "")
						document.form.TOLOCDESC.value = selection.LOCDESC; 
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
		 				document.form.TOLOCDESC.value = "";
		 				document.getElementById("TOLOCDESC").value = "";
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
// 					return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
						
					}
				  }
				}).on('typeahead:open',function(event,selection){
					var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
					element.toggleClass("glyphicon-menu-up",true);
					element.toggleClass("glyphicon-menu-down",false);
				}).on('typeahead:close',function(){
						$("#BATCH_0").val("");	 
						document.form.BATCH_0.value = "";
						$("#BATCH_0").typeahead('destroy');
		 				addSuggestionToTable();
						$("#BATCH_0").val("NOBATCH");
// 						document.getElementById("BATCH_0").value = "NOBATCH";
// 						document.form.BATCH_0.value = "NOBATCH";
// 						clearAndReload();
// 						Batchkeypress();
		 				validateProduct();
		 				Itemkeypress();
					var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
					element.toggleClass("glyphicon-menu-up",false);
					element.toggleClass("glyphicon-menu-down",true);
				}).on('typeahead:change',function(event,selection){
		 			if($(this).val() == ""){
		 				document.form.ITEM.value = "";
		 				document.getElementById("item").value = "";
		 			}
		 			/* else{
		 				validateProduct();
		 				Itemkeypress();
			 			} */
			});

			/* Batch Auto suggestion */
			$("#BATCH_0").typeahead({
				  hint: true,
				  minLength:0,  
				  searchOnFocus: true,
				  classNames: {
					 	menu: 'bigdrop'
					  }
				},
				{
				  display: 'BATCH',  
				  source: function (query, process,asyncProcess) {
					var urlStr = "/track/MasterServlet";
					var sLoc = document.form.FROM_LOC.value;
				  	var sItem = document.form.item.value;
				  	var sUom = document.form.UOM.value;
					var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=PLANT%>",
						ACTION : "GET_BATCH_DATA",
						QUERY : query,
						ITEMNO : sItem,
						UOM : sUom,
						LOC : sLoc
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.batches);
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
						console.log(data);
						return '<div"><p class="item-suggestion">Batch: '+data.BATCH+'</p><p class="item-suggestion pull-right">PC/PCS/EA UOM :'+data.PCSUOM+'</p><br/><p class="item-suggestion">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><p class="item-suggestion pull-right">Inventory UOM: '+data.UOM+'</p><br/><p class="item-suggestion">Inventory UOM Quantity: '+data.QTY+'</p><p class="item-suggestion pull-right">Received Date: '+data.CRAT+'</p><br/><p class="item-suggestion">Expiry Date: '+data.EXPIRYDATE+'</p></div>';
					}
				  }
				}).on('typeahead:open',function(event,selection){
					var menuElement = $(this).parent().find(".tt-menu");
					menuElement.next().show();
				}).on('typeahead:close',function(){
					validateProduct();
	 				Itemkeypress();
					var menuElement = $(this).parent().find(".tt-menu");
					setTimeout(function(){ menuElement.next().hide();}, 150);
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
// 				    	return '<p onclick="document.form.EMP_NAME.value = \''+data.FNAME+'\'">' + data.EMPNO + '</p>';		    
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

			var cmdval="<%=cmd%>";
			if(cmdval==="ADD")
				document.getElementById("item").focus();
			else
				setfocus();	
				
		});
	  function DefaultQty(){
			 if (document.getElementById("defualtQty").checked==true ) {
				 document.getElementById("qty").readOnly = true;
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
				  document.getElementById("qty").value=1;
			    }
		  }
		  function getAvaliableInventoryQty(uom)
		  {
			  var itemValue = document.getElementById("item").value;
			  var locValue = document.getElementById("FROMLOC").value;
			  var batch = document.getElementById("BATCH_0").value;	 
			  
				  var urlStr = "/track/ItemMstServlet";
				  
				  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",LOC:locValue,BATCH:batch,UOM:uom, ACTION: "PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetInventoryQty }); 
			  
			  
			
		  }
		  function getAvaliableInventoryQty()
		  {
			  var itemValue = document.getElementById("item").value;
			  var locValue = document.getElementById("FROMLOC").value;
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
				
				if(!errorBoo){
			        $.each(data.items, function(i,item){
			              	document.getElementById("AVAILQTY").value = item.INVENTORYQUANTITY;
			        	
			        });
			       }
			  
		  }
		  
		   var TRANID = document.getElementById("TRANID");
		   TRANID.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	
			    	  document.getElementById("FROMLOC").focus(); 
			      }
			  }
/* 		   var FROMLOC = document.getElementById("FROMLOC");
		   FROMLOC.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	  var loc = document.form.FROM_LOC.value;	 
			    	  if(loc==null||loc=="")
			    	  {
			    		  alert("Please From Enter Location!");
			    		  document.form.FROM_LOC.focus();
			    		  return false;
			    	  }
			    	  validateLocation(FROMLOC.value,"FROMLOC");
			    	  getLocation(FROMLOC.value); 
			      }
			  }
			  var elem3 = document.getElementById("TOLOC");
			  elem3.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	  var loc = document.form.TOLOC.value;	 
			    	  if(loc==null||loc=="")
			    	  {
			    		  alert("Please To Enter Location!");
			    		  document.form.TOLOC.focus();
			    		  return false;
			    	  }
			    	  validateLocation(elem3.value,"TOLOC");
			    	  getToLocation(elem3.value);
			      }
			  } */
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
				    	  document.getElementById("FROMLOC").focus(); 
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
				  function itempopUpwin(){
					  var loc = document.form.FROM_LOC.value;	
						
					  
					  if(loc==null||loc=="")
					  {
						  alert("Please Enter Location!");
						  document.form.FROM_LOC.focus();
						  return false;
					  }else{
						
						  popUpWin('../jsp/list/itemList.jsp?ITEM='+form.ITEM.value+'&LOC0='+loc);
					  }
					
					}
				  function getLocation(loc)
				  {
					  var tranid = document.form.TRANID.value;	
					  var fromloc = document.form.FROM_LOC.value;	
					  $.ajax( {
							type : "get",
							url : '/track/DynamicProductServlet?action=FROMLOCCHECK',
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "FROMLOCCHECK",
							TRANTYPE:"MOVEWITHOUTBATCH",
							FROM_LOC:fromloc
						},
						dataType : "html",
						success : function(data) {
							$("#appendbody").html(data);
						var errorValue =document.getElementById("iserrorVal").value;
							
				
							document.getElementById("TRANID").value=tranid;
						 if(errorValue){
							 document.getElementById("FROMLOC").value=fromloc;
								 document.getElementById("FROMLOC").focus(); 
							}else{
								document.getElementById("FROMLOC").value=fromloc; 
								document.getElementById("TOLOC").focus(); 
							} 
						},
						error:function(data)
						{
							
						}
					});
				  }
				  function getToLocation(loc)
				  {
					  var tranid = document.form.TRANID.value;	
					  var fromloc = document.form.FROM_LOC.value;	
					  var toloc = document.form.TOLOC.value;
					  var fromlocdesc = document.form.FROM_LOCDESC.value;
					  $.ajax( {
							type : "get",
							url : '/track/DynamicProductServlet?action=TOLOCCHECK',
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "TOLOCCHECK",
							TRANTYPE:"MOVEWITHOUTBATCH",
							TOLOC:toloc
						},
						dataType : "html",
						success : function(data) {
							$("#appendbody").html(data);
						var errorValue =document.getElementById("iserrorVal").value;
							
				
							document.getElementById("TRANID").value=tranid;
						 if(errorValue){
								document.getElementById("FROMLOC").value=fromloc; 
								document.getElementById("FROM_LOCDESC").value=fromlocdesc; 
							 document.getElementById("TOLOC").value=toloc;
								 document.getElementById("TOLOC").focus(); 
							}else{
								document.getElementById("FROMLOC").value=fromloc; 
								document.getElementById("FROM_LOCDESC").value=fromlocdesc; 
								document.getElementById("TOLOC").value=toloc;
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
								var invuom= data.result.INVENTORYUOM;
				        	document.getElementById("UOM").value =invuom;
				        	getAvaliableInventoryQty(invuom);
				        }
						}
					  
				  }
				  function validateProduct()
				 	{
				 		 
				 		GetUOM();
				 		getAvaliableInventoryQty();
				 	}

				  function clearAndReload() {
					  var inputField = document.getElementById("BATCH_0");
					  inputField.value = ""; // Clear the input field value
					  inputField.dispatchEvent(new Event("input")); // Trigger input event to reload the typeahead
					}
				  function addSuggestionToTable(){

				  /* Batch Auto suggestion */
					$("#BATCH_0").typeahead({
						  hint: true,
						  minLength:0,  
						  searchOnFocus: true,
						  classNames: {
							 	menu: 'bigdrop'
							  }
						},
						{
						  display: 'BATCH',  
						  source: function (query, process,asyncProcess) {
							var urlStr = "/track/MasterServlet";
							var sLoc = document.form.FROM_LOC.value;
						  	var sItem = document.form.item.value;
						  	var sUom = document.form.UOM.value;
							var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
							$.ajax( {
							type : "POST",
							url : urlStr,
							async : true,
							data : {
								PLANT : "<%=PLANT%>",
								ACTION : "GET_BATCH_DATA",
								QUERY : query,
								ITEMNO : sItem,
								UOM : sUom,
								LOC : sLoc
							},
							dataType : "json",
							success : function(data) {
								return asyncProcess(data.batches);
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
								console.log(data);
								return '<div"><p class="item-suggestion">Batch: '+data.BATCH+'</p><p class="item-suggestion pull-right">PC/PCS/EA UOM :'+data.PCSUOM+'</p><br/><p class="item-suggestion">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><p class="item-suggestion pull-right">Inventory UOM: '+data.UOM+'</p><br/><p class="item-suggestion">Inventory UOM Quantity: '+data.QTY+'</p><p class="item-suggestion pull-right">Received Date: '+data.CRAT+'</p><br/><p class="item-suggestion">Expiry Date: '+data.EXPIRYDATE+'</p></div>';
							}
						  }
						}).on('typeahead:open',function(event,selection){
							var menuElement = $(this).parent().find(".tt-menu");
							menuElement.next().show();
						}).on('typeahead:close',function(){
							validateProduct();
			 				Itemkeypress();
							var menuElement = $(this).parent().find(".tt-menu");
							setTimeout(function(){ menuElement.next().hide();}, 150);
						});

				  }
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>