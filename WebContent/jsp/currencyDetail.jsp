<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Currency Details";
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
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


<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Curency = (String) session.getAttribute("BASE_CURRENCY");
	
	String res = "";

	
	StrUtils strUtils = new StrUtils();
	DateUtils dateutils = new DateUtils();
	String action = "";
	String  isActive = "";
	
 	


 
	String sCurDesc = "" ,sCurrencyId="", sStatus="";
	String sDisplay="",sRemark="",sCurreqt="",currsq="";	
 	sCurrencyId = strUtils.fString(request.getParameter("CURRENCY_ID"));
	currsq  = "("+ Curency +"/"  +sCurrencyId +")"; 
	sCurDesc = strUtils.fString(request.getParameter("DESC"));
	sDisplay = strUtils.fString(request.getParameter("DISPLAY"));
	sRemark = strUtils.fString(request.getParameter("REMARK"));
	sCurreqt = strUtils.fString(request.getParameter("CURUSEQT"));
	sStatus = strUtils.fString(request.getParameter("STATUS"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);	
    
    double sCurreqtVal ="".equals(sCurreqt) ? 0.0d :  Double.parseDouble(sCurreqt);
    sCurreqt = StrUtils.addZeroes(sCurreqtVal, numberOfDecimal);
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../currency/summary"><span class="underline-on-hover">Currency Summary</span></a></li>                       
                <li><label>Currency Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../currency/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Currency Master">Currency ID</label>
       <div class="col-sm-4">          
        	 			
           <INPUT class="form-control" name="CURRENCY_ID" type="TEXT" value="<%=sCurrencyId %>"
			size="40" MAXLENGTH=20 readonly>
	</div>
  	      </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Currency Description">Description</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DESC" type="TEXT" value="<%=sCurDesc%>"
			size="40" MAXLENGTH=80 readonly>
      </div>
    </div>
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Currency Display">Display</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="DISPLAY" type="TEXT" value="<%=sDisplay%>"
			size="40" MAXLENGTH=80 readonly>
      </div>
    </div>
     
      <!-- //CREATED BY NAVAS -->
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Base Currency">Base Currency</label>
      <div class="col-sm-4">
      <div class="input-group">    
    		<input name="BASECURRENCY" type="TEXT" value="1.000" 
			size="48" MAXLENGTH=50 class="form-control" readonly>
  		    <span class="input-group-btn"></span>
    		<input name="Basecurrency" type="TEXT" value="<%=curency%>"
			size="6" MAXLENGTH=50  class="form-control" readonly>
   		 	</div>
   		 	</div>
            </div>
  
    <!-- //END BY NAVAS -->
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Equivalent Currency">Equivalent Currency</label>
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Equivalent Currency:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">   
    		<input name="CURREQT" type="TEXT" value="<%=sCurreqt%>"
			size="48" MAXLENGTH=50 class="form-control"  readonly>
   		 	<span class="input-group-btn"></span>
    		<input name="currency" type="TEXT" value="<%=currsq%>"
			size="10" MAXLENGTH=50  class="form-control" readonly>
   		 	</div>
   		 	</div>
      </div>
  		
  		
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARK" type="TEXT" value="<%=sRemark%>"
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
    <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='currencySummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
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
