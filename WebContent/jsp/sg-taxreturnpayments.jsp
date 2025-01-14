<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "GST Payments";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
    <jsp:param name="submenu" value="<%=IConstants.TAX_PAYMENTS%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/taxreturn.css">
<script type="text/javascript" src="js/general.js"></script>
  <script type="text/javascript" src="js/calendar.js"></script>
  <link href="css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/tabulator.min.js"></script>
   <script type="text/javascript" src="js/sg-taxreturnpayments.js"></script>
   <script type="text/javascript" src="dist/js/moment.min.js"></script>
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
.project-tab {

}
.project-tab #tabs{
    background: #007b5e;
    color: #eee;
}
.project-tab #tabs h6.section-title{
    color: #eee;
}
.project-tab #tabs .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
    color: #0062cc;
    background-color: transparent;
    border-color: transparent transparent #f3f3f3;
    border-bottom: 3px solid !important;
    font-size: 16px;
    font-weight: bold;
}
.project-tab .nav-link {
    border: 1px solid transparent;
    border-top-left-radius: .25rem;
    border-top-right-radius: .25rem;
    color: #0062cc;
    font-size: 16px;
    font-weight: 600;
}
.project-tab .nav-link:hover {
    border: none;
}
.project-tab thead{
    background: #f3f3f3;
    color: #333;
}
.project-tab a{
    text-decoration: none;
    color: #333;
    font-weight: 600;
}
  </style>
 
<%@ include file="header.jsp"%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="box-header menu-drop">
		
		</div>

		
 <div class="box-body">
 	<ul class="nav nav-tabs" id="myTab" role="tablist">
  <li class="nav-item">
    <a class="nav-link" id="due-tab" data-toggle="tab" href="#due" role="tab" aria-controls="due"
      aria-selected="true">GST Dues</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="history-tab" data-toggle="tab" href="#history" role="tab" aria-controls="history"
      aria-selected="true">Payment History</a>
  </li>
</ul>
<div class="tab-content" id="myTabContent">
  <div class="tab-pane fade active in" id="due" role="tabpanel" aria-labelledby="due-tab">
    <div id="taxreturnpayments-taxdues"></div>
  </div>
  <div class="tab-pane fade" id="history" role="tabpanel" aria-labelledby="history-tab">
  	<div id="taxreturnpayments-paymenthistory"></div>
    </div>
 
</div>
</div> 
</div>

<%@include file="sg-taxreturnpaymentpopup.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>