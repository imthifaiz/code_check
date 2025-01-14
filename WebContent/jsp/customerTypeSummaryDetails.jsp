<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Customer Type Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'LOCTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";
String sCustomerTypeId  ="",sDesc  =   "",action =   "",isActive="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
CustUtil custutil = new CustUtil();
//CustUtil.setmLogger(mLogger);
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
sDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTOMER_TYPE_DESC")));
isActive  = strUtils.fString(request.getParameter("ISACTIVE"));
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
      <label class="control-label col-sm-4" for="Product Type ID">Customer Type ID:</label>
      <div class="col-sm-4">
      	   
    		<input name="CUSTOMER_TYPE_ID" type="TEXT" value="<%=sCustomerTypeId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
			<INPUT type = "hidden" name="ITEM_ID1" value = "" >
   		 	
  	</div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Type Description">Customer Type Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUSTOMER_TYPE_DESC" type="TEXT" value="<%=sDesc %>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    
 <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
    
    <div class="form-group">        
      <div class="col-sm-offset-5 col-sm-7">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='customerTypeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
    </div>
    </div>
    
  </form>
</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

