<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "UOM Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "UomMaster.jsp?action=Clear";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Uom "); return false; }
   document.form1.action  = "UomMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Uom "); return false; }

   
   document.form1.action  = "maint_uom.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Uom "); return false; }
    confirm("Do you want to delete Uom?");    
   document.form1.action  = "maint_uom.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Uom "); return false; }

   document.form1.action  = "maint_uom.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){
     
   document.form1.action  = "maint_uom.jsp?action=Auto_ID";
   document.form1.submit();
}
-->
</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
String action     =   "";
String sItemId ="",sPrdClsId  =   "",sCUomFlag="",sCUom="",
       sItemDesc  = "",isActive="",sDisplay = "",sQtyPerUom = "";

session= request.getSession();
StrUtils strUtils = new StrUtils();
UomUtil uomUtil = new UomUtil();
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEM_DESC")));
isActive = strUtils.fString(request.getParameter("ISACTIVE"));
sDisplay = strUtils.fString(request.getParameter("Display"));
sQtyPerUom = strUtils.fString(request.getParameter("QPUOM"));
sCUomFlag = strUtils.fString(request.getParameter("CUOMFLAG"));
sCUom = strUtils.fString(request.getParameter("CUOM"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));





%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
               <li><a href="../uom/summary"><span class="underline-on-hover">UOM Summary</span></a></li>                       
                <li><label>UOM Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../uom/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>


<form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-2" for="UOM">Unit Of Measure</label>
      <div class="col-sm-4">
      		<input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
		        
  		<INPUT type="hidden" name="ITEM_ID1" value="">			
</div>
   </div>
<div class="form-group">
      <label class="control-label col-sm-2" for="UOM Description">UOM Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
	  <div class="form-group">
      <label class="control-label col-sm-2" for="Display">Display</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="Display" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
       <font><i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;<b>Note : To Display in Purchase,Sales,Direct Sales,Sales Estimate,Rental 
       and Transfer Order Invoice.</b></font>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-2" for="Qty Per UOM">Qty Per UOM</label>
      
      <%if(sCUomFlag.equalsIgnoreCase("1")){ %>
      <div class="col-sm-2">          
        <INPUT  class="form-control" name="QPUOM" type="TEXT" value="<%=sQtyPerUom%>"
			size="50" MAXLENGTH=100 readonly style="width: 120%;">
      </div>
        <div class="col-sm-2">          
        <INPUT  class="form-control" name="CUOM" type="TEXT" value="<%=sCUom%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      <%}else{ %>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="QPUOM" type="TEXT" value="<%=sQtyPerUom%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      <%} %>
      </div>
      <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%> >Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%> >Non Active
    </label>
     </div> 
</div>

<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='uomSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>
  </form>
</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>					

