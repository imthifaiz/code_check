<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "VAT/GST/TAX Type Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}






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
String gsttype ="",sPrdClsId  =   "",
       gstdesc  = "",gstpercent="",remarks="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
UomUtil uomUtil = new UomUtil();
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
gsttype  = strUtils.fString(request.getParameter("GSTTYPE"));
gstdesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("GSTDESC")));
gstpercent = strUtils.fString(request.getParameter("GSTPERCENT"));
remarks = strUtils.fString(request.getParameter("REMARKS"));

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../gst/summary"><span class="underline-on-hover">GST Type Summary</span></a></li>                       
                <li><label>VAT/GST/TAX Type Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../gst/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="VAT/GST/TAX Type">VAT/GST/TAX Type</label>
      <div class="col-sm-4">
      	   
    		<input name="GSTTYPE" type="TEXT" value="<%=gsttype%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
   		 	
  		<INPUT type="hidden" name="ITEM_ID1" value="">
      	      </div>
    </div>
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="VAT/GST/TAX Type Description">Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DESCRIPTION" type="TEXT" value="<%=gstdesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
   
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Percentage">Percentage</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PERCENTAGE" type="TEXT" value="<%=gstpercent%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=remarks%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
     </div>
     
     <div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='gstSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
</div>
</div>
     
    </form>
   </div>
   </div>
   </div>
   


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>