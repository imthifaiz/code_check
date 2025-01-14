<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>


<%
String title = "Location Type Two Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'LOCTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";
String sLocId ="",sLocDesc  =   "",action =   "",isActive="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();
loctypeutil.setmLogger(mLogger);
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sLocId  = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
sLocDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("lOC_TYPE_DESC2")));
isActive  = strUtils.fString(request.getParameter("ISACTIVE"));
%>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../locationtypetwo/summary"><span class="underline-on-hover">Location Type Two Summary</span></a></li>                      
                <li><label>Location Type Two Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->      
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
						onClick="window.location.href='../locationtypetwo/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>		
 <div class="box-body">
<CENTER><strong><%=res%></strong></CENTER>
<form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Location Type ID">Location Type ID</label>
      <div class="col-sm-4">
      		<input name="LOC_TYPE_ID2" id="LOC_TYPE_ID2" type="TEXT" value="<%=sLocId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
		        
  		<INPUT type="hidden" name="ITEM_ID1" value="">			
</div>
   </div>
<div class="form-group">
      <label class="control-label col-sm-4" for="Location Type Description">Location Type Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="lOC_TYPE_DESC2" id="lOC_TYPE_DESC2" type="TEXT" value="<%=sLocDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
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
  </form>
</div>
</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

					