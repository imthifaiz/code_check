<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Order Status Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'ORDERSTATUS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";
String sStatusId ="",sStatusDesc  =   "",action =   "",isActive="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
OrderStatusUtil ordstatusutil = new OrderStatusUtil();
ordstatusutil.setmLogger(mLogger);
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sStatusId  = strUtils.fString(request.getParameter("ORDERSTATUS"));
sStatusDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("STATUS_DESC")));
isActive  = strUtils.fString(request.getParameter("ISACTIVE"));
//if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Order Status">Order Status:</label>
      <div class="col-sm-4">
      	  
    		<input name="STATUS_ID" type="TEXT" value="<%=sStatusId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
   		 	      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="STATUS_DESC" type="TEXT" value="<%=sStatusDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
 <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>

<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='orderstatusSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
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



