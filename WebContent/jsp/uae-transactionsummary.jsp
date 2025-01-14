<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Tax Transaction Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String box = StrUtils.fString(request.getParameter("BOX"));
String deschead = StrUtils.fString(request.getParameter("DESCHEAD"));
String taxheader = StrUtils.fString(request.getParameter("taxheader"));
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.TAX_RETURN%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
  <script type="text/javascript" src="js/calendar.js"></script>
  <script type="text/javascript" src="dist/js/moment.min.js"></script>
  <link href="css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/tabulator.min.js"></script>
   <script type="text/javascript" src="js/uae-taxtransactionsummary.js"></script>
  <style>
  .text-dashed-underline:after {
    padding-bottom: 2px;
    border-bottom: 1px dashed #969696!important;
    width:10%;
}
  .requiredlabel {
    color: #b94a48;
}
.form-text {
    color: #999;
}
.d-block {
    display: block!important;
}
.alert-warning {
    background-color: #fff4e7 !important;
    border: 0;
    color: #222 !important;
}
  </style>
 
<%@ include file="header.jsp"%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
		<div class="box-header menu-drop">
			<div class="text-center">
				<h3><small>Vision Auto Spare Parts TR LLC</small></h3>
				<h2><small>Box <span><%=box%></span> - <%=deschead %></small></h2>
				<h3><small>From <span id="taxFrom"></span> To <span id="taxTo"></span></small></h3>
			</div>
			<h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='uae-taxreturnfill.jsp?ID=<%=taxheader%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<form>
<input type="hidden" id="taxheaderid" value="<%=taxheader%>"/>
<input type="hidden" id="box" value="<%=box%>"/>
</form>
		
 <div class="box-body">
 	<div id="taxtransactionsummary"></div>
</div>
</div> 
</div>

<%@include file="uae-taxreturncustompopup.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>