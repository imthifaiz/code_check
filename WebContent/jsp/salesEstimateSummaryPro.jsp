<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Estimate Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String fieldDesc = "", FROM_DATE="", TO_DATE="", ORDERNO="";
String msg = (String)request.getAttribute("Msg");

boolean displaySummaryExport=false,displaySummaryImport=false,displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryNew = ub.isCheckValAcc("newsalesEstimate", plant,USERID);
displaySummaryLink = ub.isCheckValAcc("summarylnksalesEstimate", plant,USERID);
displaySummaryExport = ub.isCheckValAcc("exportforsalesEstimate", plant,USERID);
displaySummaryImport = ub.isCheckValAcc("importsalesEstimate", plant,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryNew = ub.isCheckValinv("newsalesEstimate", plant,USERID);	
	displaySummaryLink = ub.isCheckValinv("summarylnksalesEstimate", plant,USERID);
	displaySummaryExport = ub.isCheckValinv("exportforsalesEstimate", plant,USERID);
	displaySummaryImport = ub.isCheckValAcc("importsalesEstimate", plant,USERID);
}
displaySummaryImport=true;
//RESVI
String curDate =du.getDateMinusDays();
FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
<jsp:param name="submenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/salesEstimateSummaryPro.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
	              <div class="box-title pull-right">
	              <div class="btn-group" role="group">
             <%if(displaySummaryImport){ %>
             <button type="button" class="btn btn-default"
             onClick="window.location.href='../jsp/importEstimateOrderExcelSheet.jsp'">
						Import Sales Estimate</button>
			      &nbsp;
				  <%}%>
				</div>
	              <%if(displaySummaryNew){ %>
	              	<button type="button" class="btn btn-default" 
	              	onClick="window.location.href='../salesestimate/new1'" 
	              	style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
	              <%}%>
	              </div>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="">
				<div id="target" style="display:none" style="padding: 18px;">
				<input type="text" name="CUST_CODE" hidden>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2.5">
							<label class="control-label col-sm-2" for="search">Search</label>
						</div>
						<div class="col-sm-2">
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" 
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-2">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" 
				  			value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" class="ac-selected  form-control typeahead" 
									id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
								<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
								<span class="btn-danger input-group-addon" 
									onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	
							</div>
						</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" 
				  				value="" placeholder="ORDER NO">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
				  			<input type="text" class="ac-selected form-control" id="Reference" 
				  				name="reference" placeholder="REFERENCE">							 		
			  			</div>
					</div>
					
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">  		
				  			<input type="text" class="ac-selected form-control" id="ORDERTYPE" name="ORDERTYPE" 
				  				value="" placeholder="ORDER TYPE">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				  		</div>
				  		<div class="col-sm-4 ac-box">
					  		<input type="text" name="status" id="status" class="ac-selected form-control" 
					  		placeholder="STATUS" >
					  		<span class="select-icon" 
					  		onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
				  		</div>
					</div>
							<input type="hidden" value="<%=displaySummaryLink%>" name="displaySummaryLink" id="displaySummaryLink" />
							<input type="hidden" value="<%=displaySummaryExport%>" name="displaySummaryExport" id="displaySummaryExport" /> 
					<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
						<div class="col-sm-4 ac-box">
							<button type="button" class="btn btn-success" 
								onClick="javascript:return onGo();">Search</button>
						</div>
					</div>		
				</div>
				</div>
				<div class="form-group">
      				<div class="col-sm-3">
				      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
				      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
			      	</div>
      				<div class="ShowSingle"></div>
       	  		</div>
       	  		<div id="VIEW_RESULT_HERE" class="table-responsive">
       	  			<div class="row">
       	  				<div class="col-sm-12">
       	  					<table id="tableSalesEstimateSummary" class="table table-bordred table-striped">                   
			                   <thead>
									<th style="font-size: smaller;">DATE</th>
									<th style="font-size: smaller;">ORDER NO.</th>
									<th style="font-size: smaller;">CUSTOMER</th>									
									<th style="font-size: smaller;">DELIVERY DATE</th>
									<th style="font-size: smaller;">STATUS</th>
									<th style="font-size: smaller;">AMOUNT</th>
			                   </thead>
			              </table>
       	  				</div>
   	  				</div>
       	  		</div>
			</form>
		</div>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>