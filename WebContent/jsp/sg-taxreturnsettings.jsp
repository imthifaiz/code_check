<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "GST Settings";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
    <jsp:param name="submenu" value="<%=IConstants.TAX_SETTINGS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
   <script type="text/javascript" src="../jsp/js/taxreturn.js"></script>
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
                <li><label>GST Settings</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 	<div class="alert alert-warning">  <b>Note:</b> Any change you make to the <b>Reporting Period</b> will reflect only from the next return. Please delete the existing returns to change the other settings.  </div>
 	<div class="alert" align="center" style="width:50%;background-color: #d2d6de;">Is your business registered for GST? <input type="radio" id="isregyes" name="isreg" value="yes">
  <label for="yes">Yes</label>
  <input type="radio" id="isregno" name="isreg" value="no">
  <label for="no">No</label></div>
	<form class="form-horizontal" name="taxsettingform" action="/track/TaxSettingServlet?Submit=Save" method="post" >
	<input type="hidden" name="taxid"/>
		<div class="form-group">
		 	<label class="control-label text-dashed-underline col-sm-4 required">Goods and Service Tax ID (GST)</label>
			 <div class="col-sm-2">
			  <input class="form-control" name="TaxCode" type = "TEXT"  size="10"  MAXLENGTH=50>
			  </div>
			  <div class="col-sm-4">
			  <input class="form-control" name="TaxRegNo" type = "TEXT" size="30">
			  </div>
		</div>
		<div class="form-group" style="display:none">
		      <label class="control-label col-sm-4">International Trade</label>
		       <div class="col-sm-4">           	 			
		            <input type="checkbox" name="ENABLE_TRADE" id="ENABLE_TRADE"/>Enable trade with contacts outside U.A.E<br>
		             <small class="d-block form-text">Enable this option if you are doing business with other GCC/Non-GCC countries and also for reverse charge handling.</small>
				</div>
		</div>
		<div class="form-group">
			 <label class="control-label col-sm-4 required">GST Registered On</label>
			 <div class="col-sm-4">
			  <input class="form-control datepicker" name="vatregdate" type = "TEXT"  size="20" >
			  </div>
		</div>	
		<div class="form-group">
			 <label class="control-label col-sm-4 required">Generate First GST Return From</label>
			 <div class="col-sm-4">
			  <input class="form-control datepicker" name="generatefirsttax" type = "TEXT"  size="20" >
			  </div>
		</div>
		<div class="form-group">
			 <label class="control-label col-sm-4">Reporting Period</label>
			 <div class="col-sm-4">
			  	<select class="form-control" data-toggle="dropdown" data-placement="right" name="reportingperiod" id="reportingperiod">
			        <option value="Monthly">Monthly</option>
			        <option value="Quarterly">Quarterly</option>
			        <option value="Half-Yearly">Half-Yearly</option>
			        <option value="Yearly">Yearly</option>
				    <option value="Custom">Custom</option>
				</select>
			  </div>
		</div>
		<div class="form-group">        
	    	<div class="col-sm-offset-4 col-sm-8">
				<button type="submit" class="Submit btn btn-primary" onClick=""><b>Save</b></button>
			</div>
		</div>
	</form>
</div>
</div> 
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>