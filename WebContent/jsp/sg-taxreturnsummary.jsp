<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "GST Return Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
    <jsp:param name="submenu" value="<%=IConstants.TAX_RETURN%>"/>
</jsp:include>
	<%
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
PlantMstDAO plantMstDAO = new PlantMstDAO();
String baseCurrency = plantMstDAO.getBaseCurrency(plant);
String NumberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
%>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/taxreturn.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
  <link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
   <script type="text/javascript" src="../jsp/js/sg-taxreturnsummary.js"></script>
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
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                                              
                <li><label>GST Return Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		<div class="box-header menu-drop">
			<button type="button" class="btn btn-default pull-right" data-toggle="modal"
				data-target="#create_account_modal" onclick="generateTaxReturn()">Generate GST Return</button>
			<a class="btn btn-default pull-left" href="../tax/sg-gstsetting"><i class="fa fa-cog"></i> GST Return Settings</a>	

		</div>

		
 <div class="box-body">
 <input type="hidden" name="numberOfDecimal" value=<%=NumberOfDecimal%>>
 	<input type="hidden" name="baseCurrency" value=<%=baseCurrency%>>
 	<div id="taxreturnsummary-table"></div>
</div>
</div> 
</div>

<%@include file="sg-taxreturncustompopup.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>