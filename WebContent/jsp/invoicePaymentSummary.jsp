<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Receipt Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="",SUPPLIER="";

boolean displaySummaryNew=false,displaySummaryLink=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newPaymentReceived", plant,username);
	displaySummaryLink = ub.isCheckValAcc("summarylnkPaymentReceived", plant,username);
}
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//RESVI
String curDate =du.getDateMinusDays();
FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/InvoicePaymentSummary.js"></script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                <li><label>Receipt Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" id="createNew" onClick="window.location.href='../banking/createinvoicepay'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
              <% } %>
              </div>
              </h1>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<INPUT type = "hidden" name="NOOFDECIMAL" value="<%=numberOfDecimal%>">
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="curency" value="<%=curency%>" hidden>
		<INPUT type = "hidden" name="CUST_CODE" >
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="REFERENCE" name="REFERENCE"  placeholder="REFERENCE">				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
				
			</div>
		</div>
  		</div>
		</div>
		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableInvoicePaymentSummary" class="table table-bordred table-striped">                   
                   <thead>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">PAYMENT#</th>
                    <th style="font-size: smaller;">INVOICE#</th>
                     <th style="font-size: smaller;">REFERENCE</th>
                     <th style="font-size: smaller;">CUSTOMER NAME</th>
                     <th style="font-size: smaller;">ACCOUNT NAME</th>
                     <th style="font-size: smaller;">PAYMENT MODE</th>
                     <th style="font-size: smaller;">PAYMENT TYPE</th>                     
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                      <th style="font-size: smaller;">UNUSED AMOUNT (<%=curency%>)</th>
                    
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</FORM>
		</div>
		
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>