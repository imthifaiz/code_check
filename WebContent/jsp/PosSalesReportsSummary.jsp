
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 13-06-2022 -->
<!-- DESC : POS Sales Reports Summary -->

<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "POS Sales Report";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GroupSummary',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}


	function ExportReport(){
		
	} 
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	
	String FROM_DATE="",TO_DATE="",searchtype="",CUST_NAME  = "",SALES_MAN="",OUTLET_CODE = "",TERMINAL_CODE ="",sOutCode="";
	
	
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	String types =  StrUtils.fString(request.getParameter("srctype"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	String curDate =du.getDateMinusDays();
	if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
		curDate =DateUtils.getDate();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
	searchtype  = strUtils.fString(request.getParameter("srctype"));
	CUST_NAME  = strUtils.fString(request.getParameter("CUST_NAME"));
	SALES_MAN  = strUtils.fString(request.getParameter("SALES_MAN"));
	sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
	
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	String ENABLE_POS = _PlantMstDAO.getispos(PLANT);
	String ISTAXREGISTRED = _PlantMstDAO.getistaxregistred(PLANT);
	String collectionDate=DateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//END
	
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../posreports/reports"><span class="underline-on-hover">POS Reports</span> </a></li>
                <li><label>POS Sales Report</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              
                            <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:tableToExcel('tableprint', 'Sheet1')">
						Export All Data</button>
					&nbsp;
				</div>
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div>
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../posreports/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="PosSalesReportsSummary.jsp">
<a id="exporttable" style="display:none"></a>
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="CUST_CODE" >
<input type="hidden" name="OUTLET_CODE" value="">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
<input type="hidden" name="srctype" value="">

	<div id="target" style="padding: 18px; display:none;">
   	<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
  		<div class="col-sm-2">
			<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
		</div>
		<div class="col-sm-2">
			<input class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
		</div>
  		
       <div class="col-sm-4 ac-box">
    		<input type="text" name="SEARCHTYPE" id="SEARCHTYPE" value="<%=types%>" class="ac-selected form-control" placeholder="SEARCH TYPE" >
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesearchtype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
      	   	<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO"  placeholder="BILL NO">
      	   	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>	 -->
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
    		<INPUT name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=OUTLET_CODE%>" placeholder="OUTLET" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeoutlet(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'OUTLET_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i> </span> -->
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    		<INPUT name="TERMINALNAME" id="TERMINALNAME" type="TEXT" value="<%=TERMINAL_CODE%>" placeholder="TERMINAL" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeterminal(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'TERMINALNAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
    		<INPUT id="CUST_NAME" name="CUST_NAME" type = "TEXT" value="<%=CUST_NAME%>" size="20"  placeholder="CASHIER" MAXLENGTH=100 class="ac-selected form-control">
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecust_name(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    		<INPUT id="SALES_MAN" name="SALES_MAN" type = "TEXT" value="<%=SALES_MAN%>" size="20"  placeholder="SALES PERSON" MAXLENGTH=100 class="ac-selected form-control">
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesales_man(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon"  onclick="$(this).parent().find('input[name=\'SALES_MAN\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
    		<input type="text" name="item" id="item" class="ac-selected form-control" placeholder="PRODUCT" >
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->  		
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control" id="PRD_BRAND_ID" placeholder="PRODUCT BRAND" name="PRD_BRAND_ID">
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeproductbrand(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control" id="PRD_CLS_ID" placeholder="PRODUCT CATEGORY" name="PRD_CLS_ID">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
  		<div class="col-sm-4 ac-box">
    	  	<input type="text" class="ac-selected  form-control" id="PRD_TYPE_ID" placeholder="PRODUCT SUB CATEGORY" name="PRD_TYPE_ID">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeProductType(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 	</div>

 	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
       <div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control" id="PAYMENTTYPE" placeholder="PAYMENT MODE" name="PAYMENTTYPE">
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changepaymenttype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="select-icon" onclick="$(this).parent().find('input[name=\'PAYMENTTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
   	
 		<div class="col-sm-4 ac-box">
  			<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  		</div>
  	</div>
  	</div>
  	
  	      <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	  </div>
         </div>
       	   </div>
  	
</form>
<br>
 
<!-- PDF PRINT -->
<div id="dvContents" style="display:none">
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table printtable" id="tableprint" >
	<caption><%=CNAME%></caption>
	<thead>
	     <tr> 
	     <th style="font-size: smaller;">S/N</th>  
            <th class="ORD" style="font-size: smaller;">DATE</th>
             <th class="ORD" style="font-size: smaller;">TIME</th>
            <th class="ORD" style="font-size: smaller;">BILL</th>
			<th class="ORD" style="font-size: smaller;">CUSTOMER</th>
           <!--  <th class="ORD" style="font-size: smaller;">TRANNO(REFNO)</th> -->
           <th class="ORD" style="font-size: smaller;">PAYMENT MODE</th>
            <th class="ORD" style="font-size: smaller;">AMOUNT</th>
            <th class="ORD" style="font-size: smaller;">DISCOUNT</th>    
            <th class="ORD" style="font-size: smaller;">EXCHANGE</th>    
            <th class="ORD" style="font-size: smaller;">TAX</th>    
            <th class="ORD" style="font-size: smaller;">TOTAL AMOUNT</th>
            
			<th class="PRD" style="font-size: smaller;">PRODUCT ID</th>
			<th class="PRD" style="font-size: smaller;">PRODUCT DESCRIPTION</th>
			<th class="PRD" style="font-size: smaller;">QTY</th>
			<th class="PRD" style="font-size: smaller;">AMOUNT</th>
                
			<th class="CUS" style="font-size: smaller;">CUSTOMER ID</th>
			<th class="CUS" style="font-size: smaller;">CUSTOMER</th>
			<th class="CUS" style="font-size: smaller;">AMOUNT</th>
			<th class="CUS" style="font-size: smaller;">DISCOUNT</th>
			<th class="CUS" style="font-size: smaller;">TAX</th>
			<th class="CUS" style="font-size: smaller;">TOTAL AMOUNT</th>
                
			<th class="CAT" style="font-size: smaller;">CATEGORY</th>
			<th class="CAT" style="font-size: smaller;">QTY</th>
			<th class="CAT" style="font-size: smaller;">AMOUNT</th>
			
            <th class="TRM" style="font-size: smaller;">OUTLET</th>
            <th class="TRM" style="font-size: smaller;">TERMINAL</th>
            <th class="TRM" style="font-size: smaller;">DAY</th>
            <th class="TRM" style="font-size: smaller;">DATE</th>
            <th class="TRM" style="font-size: smaller;">TOTAL SALES</th>
            <th class="TRM" style="font-size: smaller;">TOTAL COST</th>
            <th class="TRM" style="font-size: smaller;">TAX</th> 
			<th class="TRM" style="font-size: smaller;">GP%</th>
            <!-- <th class="TRM" style="font-size: smaller;">OPEN</th>
            <th class="TRM" style="font-size: smaller;">STATUS</th>
            <th class="TRM" style="font-size: smaller;">CLOSE</th>    
            <th class="TRM" style="font-size: smaller;">STATUS</th>
            <th class="TRM" style="font-size: smaller;">DURATION</th> -->    
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
							<% if(searchtype.equals("Bill")) { %> 
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<%}else if(searchtype.equals("Product")) { %>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<%} else if(searchtype.equals("Customer")) { %>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<%}else if(searchtype.equals("Category")) {  %>
								<th class="CAT"></th>
								<th class="CAT"></th>
								<th class="CAT"></th>
								<%} else if(searchtype.equals("Terminal")) { %> 
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<!-- <th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th> -->
								<%} %>
							</tr>
						</tfoot>
						<!-- END -->
	     </TABLE>
</div>
<div style="overflow-x:auto;">
<table id="tablePosSalesSummary" class="table table-bordred table-striped"  style="width: 100%;">  
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th class="ORD" style="font-size: smaller;">DATE</th>
            <th class="ORD" style="font-size: smaller;">TIME</th>
            <th class="ORD" style="font-size: smaller;">BILL</th>
			<th class="ORD" style="font-size: smaller;">CUSTOMER</th>
           <!--  <th class="ORD" style="font-size: smaller;">TRANNO(REFNO)</th> -->
            <th class="ORD" style="font-size: smaller;">PAYMENT MODE</th>
            <th class="ORD" style="font-size: smaller;">AMOUNT</th>
            <th class="ORD" style="font-size: smaller;">DISCOUNT</th>    
            <th class="ORD" style="font-size: smaller;">EXCHANGE</th>    
            <th class="ORD" style="font-size: smaller;">TAX</th>    
            <th class="ORD" style="font-size: smaller;">TOTAL AMOUNT</th>
            
			<th class="PRD" style="font-size: smaller;">PRODUCT ID</th>
			<th class="PRD" style="font-size: smaller;">PRODUCT DESCRIPTION</th>
			<th class="PRD" style="font-size: smaller;">QTY</th>
			<th class="PRD" style="font-size: smaller;">AMOUNT</th>
                
			<th class="CUS" style="font-size: smaller;">CUSTOMER ID</th>
			<th class="CUS" style="font-size: smaller;">CUSTOMER</th>
			<th class="CUS" style="font-size: smaller;">AMOUNT</th>
			<th class="CUS" style="font-size: smaller;">DISCOUNT</th>
			<th class="CUS" style="font-size: smaller;">TAX</th>
			<th class="CUS" style="font-size: smaller;">TOTAL AMOUNT</th>
                
			<th class="CAT" style="font-size: smaller;">CATEGORY</th>
			<th class="CAT" style="font-size: smaller;">QTY</th>
			<th class="CAT" style="font-size: smaller;">AMOUNT</th>
			
			<th class="TRM" style="font-size: smaller;">OUTLET</th>
            <th class="TRM" style="font-size: smaller;">TERMINAL</th>
            <th class="TRM" style="font-size: smaller;">DAY</th>
            <th class="TRM" style="font-size: smaller;">DATE</th>
            <th class="TRM" style="font-size: smaller;">TOTAL SALES</th>
            <th class="TRM" style="font-size: smaller;">TOTAL COST</th>
             <th class="TRM" style="font-size: smaller;">TAX</th> 
			<th class="TRM" style="font-size: smaller;">GP%</th>
            <!-- <th class="TRM" style="font-size: smaller;">OPEN</th>
            <th class="TRM" style="font-size: smaller;">STATUS</th>
            <th class="TRM" style="font-size: smaller;">CLOSE</th>
            <th class="TRM" style="font-size: smaller;">STATUS</th>
            <th class="TRM" style="font-size: smaller;">DURATION</th> -->    
			
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
							<% if(searchtype.equals("Bill")) { %> 
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<th class="ORD"></th>
								<%}else if(searchtype.equals("Product")) { %>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<th class="PRD"></th>
								<%} else if(searchtype.equals("Customer")) { %>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<th class="CUS"></th>
								<%}else if(searchtype.equals("Category")) {  %>
								<th class="CAT"></th>
								<th class="CAT"></th>
								<th class="CAT"></th>
								<%} else if(searchtype.equals("Terminal")) { %> 
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<!-- <th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th>
								<th class="TRM"></th> -->
								<%} %>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div>  
<script>
		
		var tablePosSalesSummary;
		var tablePrint;
		var plant,fdate,tdate,type,orderno,customer,outlet_name,outlet_code,terminalname,cashier,sales_man,item,prd_brand_id,prd_cls_id,prd_type_id,payment_type,groupRowColSpan = 7;
		
		function getParameters(){
			return {
				"FDATE":fdate,
				"TDATE":tdate,
				"TYPE":type,
				"ORDERNO":orderno,
				"CUSTOMERCODE":customer,
				"OUTLET":outlet_code,
				"TERMINAL":terminalname,
				"CASHIER":cashier,
				"SALESMAN":sales_man,
				"ITEM":item,
				"PRD_BRAND_ID":prd_brand_id,
				"PRD_TYPE_ID":prd_type_id,
				"PRD_CLS_ID":prd_cls_id,
				"PAYMENTTYPE":payment_type,
				"ACTION": "VIEW_POS_SALES_ORDER_SUMMARY",
				"PLANT": plant
			}
		}  

		function onGo(){
			var flag    = "false";
			
			plant = document.form.plant.value;
			fdate = document.form.FROM_DATE.value;
			tdate = document.form.TO_DATE.value;
			type = document.form.srctype.value;
			orderno = document.form.ORDERNO.value;
			customer = document.form.CUST_CODE.value;
			//if(type == "Terminal"){
			outlet_name = document.form.OUTCODE.value;
			terminalname = document.form.TERMINAL_CODE.value;
			//} else {
			//outlet_name = document.form.OUTLET_NAME.value;
			//terminalname = document.form.TERMINALNAME.value;
			//}
			
			outlet_code = document.form.OUTCODE.value;
			cashier = document.form.CUST_NAME.value;
			sales_man = document.form.SALES_MAN.value;
			item = document.form.item.value;
			prd_brand_id = document.form.PRD_BRAND_ID.value;
			prd_cls_id = document.form.PRD_CLS_ID.value;
			prd_type_id = document.form.PRD_TYPE_ID.value;
			payment_type = document.form.PAYMENTTYPE.value;

			var urlStr = "../posreports/PosSalesOrderSummary";
			//tablePosSalesSummary.ajax.reload(null, false); 
			// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		   	if(type == "")
		   		//type = "Bill";
		   		type = "Terminal";
		    // End code modified by Deen for product brand on 11/9/12
		    if (tablePosSalesSummary){
		    	tablePosSalesSummary.ajax.url( urlStr ).load();
		    	tablePrint.ajax.url( urlStr ).load();
		    	  if(type == "Bill"){
	    			  $(".ORD").show();
	    			  $(".PRD").hide();
	    			  $(".CUS").hide();
	    			  $(".CAT").hide();
	    			  $(".TRM").hide();

	    			  	tablePosSalesSummary.column(1).visible(true);
	    			  	tablePosSalesSummary.column(2).visible(true);
				    	tablePosSalesSummary.column(3).visible(true);
				    	tablePosSalesSummary.column(4).visible(true);
				    	tablePosSalesSummary.column(5).visible(true);
				    	tablePosSalesSummary.column(6).visible(true);
				    	tablePosSalesSummary.column(7).visible(true);
				    	tablePosSalesSummary.column(8).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePosSalesSummary.column(9).visible(false);
				    	<%} else {%>
				    	tablePosSalesSummary.column(9).visible(true);
				    	<%} %>
				    	tablePosSalesSummary.column(10).visible(true);
				    	tablePosSalesSummary.column(11).visible(false);
				    	tablePosSalesSummary.column(12).visible(false);
				    	tablePosSalesSummary.column(13).visible(false);
				    	tablePosSalesSummary.column(14).visible(false);
				    	tablePosSalesSummary.column(15).visible(false);
				    	tablePosSalesSummary.column(16).visible(false);
				    	tablePosSalesSummary.column(17).visible(false);
				    	tablePosSalesSummary.column(18).visible(false);
				    	tablePosSalesSummary.column(19).visible(false);
				    	tablePosSalesSummary.column(20).visible(false);
				    	tablePosSalesSummary.column(21).visible(false);
				    	tablePosSalesSummary.column(22).visible(false);
				    	tablePosSalesSummary.column(23).visible(false);
				    	tablePosSalesSummary.column(24).visible(false);
				    	tablePosSalesSummary.column(25).visible(false);
				    	tablePosSalesSummary.column(26).visible(false);
				    	tablePosSalesSummary.column(27).visible(false);
				    	tablePosSalesSummary.column(28).visible(false);
				    	tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	/* tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	tablePosSalesSummary.column(32).visible(false);
				    	tablePosSalesSummary.column(33).visible(false); */

				    	tablePrint.column(1).visible(true);
				    	tablePrint.column(2).visible(true);
				    	tablePrint.column(3).visible(true);
				    	tablePrint.column(4).visible(true);
				    	tablePrint.column(5).visible(true);
				    	tablePrint.column(6).visible(true);
				    	tablePrint.column(7).visible(true);
				    	tablePrint.column(8).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePrint.column(9).visible(false);
				    	<%} else {%>
				    	tablePrint.column(9).visible(true);
				    	<%} %>
				    	tablePrint.column(10).visible(true);
				    	tablePrint.column(11).visible(false);
				    	tablePrint.column(12).visible(false);
				    	tablePrint.column(13).visible(false);
				    	tablePrint.column(14).visible(false);
				    	tablePrint.column(15).visible(false);
				    	tablePrint.column(16).visible(false);
				    	tablePrint.column(17).visible(false);
				    	tablePrint.column(18).visible(false);
				    	tablePrint.column(19).visible(false);
				    	tablePrint.column(20).visible(false);
				    	tablePrint.column(21).visible(false);
				    	tablePrint.column(22).visible(false);
				    	tablePrint.column(23).visible(false);
				    	tablePrint.column(24).visible(false);
				    	tablePrint.column(25).visible(false);
				    	tablePrint.column(26).visible(false);
				    	tablePrint.column(27).visible(false);
				    	tablePrint.column(28).visible(false);
				    	tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	/* tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	tablePrint.column(32).visible(false);
				    	tablePrint.column(33).visible(false); */
	    		  }else if(type == "Product"){
	    	  		  $(".PRD").show();
	    	  		  $(".ORD").hide();
	    			  $(".CUS").hide();
	    			  $(".CAT").hide();
	    			  $(".TRM").hide();

	    			  	tablePosSalesSummary.column(1).visible(false);
	    			  	tablePosSalesSummary.column(2).visible(false);
				    	tablePosSalesSummary.column(3).visible(false);
				    	tablePosSalesSummary.column(4).visible(false);
				    	tablePosSalesSummary.column(5).visible(false);
				    	tablePosSalesSummary.column(6).visible(false);
				    	tablePosSalesSummary.column(7).visible(false);
				    	tablePosSalesSummary.column(8).visible(false);
				    	tablePosSalesSummary.column(9).visible(false);
				    	tablePosSalesSummary.column(10).visible(false);
						tablePosSalesSummary.column(11).visible(true);
				    	tablePosSalesSummary.column(12).visible(true);
				    	tablePosSalesSummary.column(13).visible(true);
				    	tablePosSalesSummary.column(14).visible(true);
				    	tablePosSalesSummary.column(15).visible(false);
				    	tablePosSalesSummary.column(16).visible(false);
				    	tablePosSalesSummary.column(17).visible(false);
				    	tablePosSalesSummary.column(18).visible(false);
				    	tablePosSalesSummary.column(19).visible(false);
				    	tablePosSalesSummary.column(20).visible(false);
				    	tablePosSalesSummary.column(21).visible(false);
				    	tablePosSalesSummary.column(22).visible(false);
				    	tablePosSalesSummary.column(23).visible(false);
				    	tablePosSalesSummary.column(24).visible(false);
				    	tablePosSalesSummary.column(25).visible(false);
				    	tablePosSalesSummary.column(26).visible(false);
				    	tablePosSalesSummary.column(27).visible(false);
				    	tablePosSalesSummary.column(28).visible(false);
				    	tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	/* tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	tablePosSalesSummary.column(32).visible(false);
				    	tablePosSalesSummary.column(33).visible(false); */

				    	tablePrint.column(1).visible(false);
	    			  	tablePrint.column(2).visible(false);
				    	tablePrint.column(3).visible(false);
				    	tablePrint.column(4).visible(false);
				    	tablePrint.column(5).visible(false);
				    	tablePrint.column(6).visible(false);
				    	tablePrint.column(7).visible(false);
				    	tablePrint.column(8).visible(false);
				    	tablePrint.column(9).visible(false);
				    	tablePrint.column(10).visible(false);
						tablePrint.column(11).visible(true);
				    	tablePrint.column(12).visible(true);
				    	tablePrint.column(13).visible(true);
				    	tablePrint.column(14).visible(true);
				    	tablePrint.column(15).visible(false);
				    	tablePrint.column(16).visible(false);
				    	tablePrint.column(17).visible(false);
				    	tablePrint.column(18).visible(false);
				    	tablePrint.column(19).visible(false);
				    	tablePrint.column(20).visible(false);
				    	tablePrint.column(21).visible(false);
				    	tablePrint.column(22).visible(false);
				    	tablePrint.column(23).visible(false);
				    	tablePrint.column(24).visible(false);
				    	tablePrint.column(25).visible(false);
				    	tablePrint.column(26).visible(false);
				    	tablePrint.column(27).visible(false);
				    	tablePrint.column(28).visible(false);
				    	tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	/* tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	tablePrint.column(32).visible(false);
				    	tablePrint.column(33).visible(false); */
				    	
	    		  }else if(type == "Customer"){
	    			  $(".CUS").show();
	    			  $(".ORD").hide();
	    			  $(".PRD").hide();
	    			  $(".CAT").hide();
	    			  $(".TRM").hide();

	    			  	tablePosSalesSummary.column(1).visible(false);
	    			  	tablePosSalesSummary.column(2).visible(false);
				    	tablePosSalesSummary.column(3).visible(false);
				    	tablePosSalesSummary.column(4).visible(false);
				    	tablePosSalesSummary.column(5).visible(false);
				    	tablePosSalesSummary.column(6).visible(false);
				    	tablePosSalesSummary.column(7).visible(false);
				    	tablePosSalesSummary.column(8).visible(false);
				    	tablePosSalesSummary.column(9).visible(false);
				    	tablePosSalesSummary.column(10).visible(false);
				    	tablePosSalesSummary.column(11).visible(false);
				    	tablePosSalesSummary.column(12).visible(false);
				    	tablePosSalesSummary.column(13).visible(false);
				    	tablePosSalesSummary.column(14).visible(false);
	    			  	tablePosSalesSummary.column(15).visible(true);
				    	tablePosSalesSummary.column(16).visible(true);
				    	tablePosSalesSummary.column(17).visible(true);
				    	tablePosSalesSummary.column(18).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePosSalesSummary.column(19).visible(false);
				    	<%} else {%>
				    	tablePosSalesSummary.column(19).visible(true);
				    	<%} %>
				    	tablePosSalesSummary.column(20).visible(true);
				    	tablePosSalesSummary.column(21).visible(false);
				    	tablePosSalesSummary.column(22).visible(false);
				    	tablePosSalesSummary.column(23).visible(false);
				    	tablePosSalesSummary.column(24).visible(false);
				    	tablePosSalesSummary.column(25).visible(false);
				    	tablePosSalesSummary.column(26).visible(false);
				    	tablePosSalesSummary.column(27).visible(false);
				    	tablePosSalesSummary.column(28).visible(false);
				    	tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	/* tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	tablePosSalesSummary.column(32).visible(false);
				    	tablePosSalesSummary.column(33).visible(false); */

				    	tablePrint.column(1).visible(false);
	    			  	tablePrint.column(2).visible(false);
				    	tablePrint.column(3).visible(false);
				    	tablePrint.column(4).visible(false);
				    	tablePrint.column(5).visible(false);
				    	tablePrint.column(6).visible(false);
				    	tablePrint.column(7).visible(false);
				    	tablePrint.column(8).visible(false);
				    	tablePrint.column(9).visible(false);
				    	tablePrint.column(10).visible(false);
				    	tablePrint.column(11).visible(false);
				    	tablePrint.column(12).visible(false);
				    	tablePrint.column(13).visible(false);
				    	tablePrint.column(14).visible(false);
	    			  	tablePrint.column(15).visible(true);
				    	tablePrint.column(16).visible(true);
				    	tablePrint.column(17).visible(true);
				    	tablePrint.column(18).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePrint.column(19).visible(false);
				    	<%} else {%>
				    	tablePrint.column(19).visible(true);
				    	<%} %>
				    	tablePrint.column(20).visible(true);
				    	tablePrint.column(21).visible(false);
				    	tablePrint.column(22).visible(false);
				    	tablePrint.column(23).visible(false);
				    	tablePrint.column(24).visible(false);
				    	tablePrint.column(25).visible(false);
				    	tablePrint.column(26).visible(false);
				    	tablePrint.column(27).visible(false);
				    	tablePrint.column(28).visible(false);
				    	tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	/* tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	tablePrint.column(32).visible(false);
				    	tablePrint.column(33).visible(false); */
	    		  }else if(type == "Category"){
	    			  $(".CAT").show();
	    			  $(".ORD").hide();
	    			  $(".CUS").hide();
	    			  $(".PRD").hide();
	    			  $(".TRM").hide();

	    			 	tablePosSalesSummary.column(1).visible(false);
	    			 	tablePosSalesSummary.column(2).visible(false);
				    	tablePosSalesSummary.column(3).visible(false);
				    	tablePosSalesSummary.column(4).visible(false);
				    	tablePosSalesSummary.column(5).visible(false);
				    	tablePosSalesSummary.column(6).visible(false);
				    	tablePosSalesSummary.column(7).visible(false);
				    	tablePosSalesSummary.column(8).visible(false);
				    	tablePosSalesSummary.column(9).visible(false);
				    	tablePosSalesSummary.column(10).visible(false);
				    	tablePosSalesSummary.column(11).visible(false);
				    	tablePosSalesSummary.column(12).visible(false);
				    	tablePosSalesSummary.column(13).visible(false);
	    			  	tablePosSalesSummary.column(14).visible(false);
				    	tablePosSalesSummary.column(15).visible(false);
				    	tablePosSalesSummary.column(16).visible(false);
				    	tablePosSalesSummary.column(17).visible(false);
				    	tablePosSalesSummary.column(18).visible(false);
				    	tablePosSalesSummary.column(19).visible(false);
				    	tablePosSalesSummary.column(20).visible(false);
				    	tablePosSalesSummary.column(21).visible(true);
				    	tablePosSalesSummary.column(22).visible(true);
				    	tablePosSalesSummary.column(23).visible(true);
				    	tablePosSalesSummary.column(24).visible(false);
				    	tablePosSalesSummary.column(25).visible(false);
				    	tablePosSalesSummary.column(26).visible(false);
				    	tablePosSalesSummary.column(27).visible(false);
				    	tablePosSalesSummary.column(28).visible(false);
				    	tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	/* tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	tablePosSalesSummary.column(32).visible(false);
				    	tablePosSalesSummary.column(33).visible(false); */

				    	tablePrint.column(1).visible(false);
	    			 	tablePrint.column(2).visible(false);
				    	tablePrint.column(3).visible(false);
				    	tablePrint.column(4).visible(false);
				    	tablePrint.column(5).visible(false);
				    	tablePrint.column(6).visible(false);
				    	tablePrint.column(7).visible(false);
				    	tablePrint.column(8).visible(false);
				    	tablePrint.column(9).visible(false);
				    	tablePrint.column(10).visible(false);
				    	tablePrint.column(11).visible(false);
				    	tablePrint.column(12).visible(false);
				    	tablePrint.column(13).visible(false);
	    			  	tablePrint.column(14).visible(false);
				    	tablePrint.column(15).visible(false);
				    	tablePrint.column(16).visible(false);
				    	tablePrint.column(17).visible(false);
				    	tablePrint.column(18).visible(false);
				    	tablePrint.column(19).visible(false);
				    	tablePrint.column(20).visible(false);
				    	tablePrint.column(21).visible(true);
				    	tablePrint.column(22).visible(true);
				    	tablePrint.column(23).visible(true);
				    	tablePrint.column(24).visible(false);
				    	tablePrint.column(25).visible(false);
				    	tablePrint.column(26).visible(false);
				    	tablePrint.column(27).visible(false);
				    	tablePrint.column(28).visible(false);
				    	tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	/* tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	tablePrint.column(32).visible(false);
				    	tablePrint.column(33).visible(false); */
	    		  }else if(type == "Terminal"){
	    			  $(".CAT").hide();
	    			  $(".ORD").hide();
	    			  $(".CUS").hide();
	    			  $(".PRD").hide();
	    			  $(".TRM").show();

	    			 	tablePosSalesSummary.column(1).visible(false);
	    			 	tablePosSalesSummary.column(2).visible(false);
				    	tablePosSalesSummary.column(3).visible(false);
				    	tablePosSalesSummary.column(4).visible(false);
				    	tablePosSalesSummary.column(5).visible(false);
				    	tablePosSalesSummary.column(6).visible(false);
				    	tablePosSalesSummary.column(7).visible(false);
				    	tablePosSalesSummary.column(8).visible(false);
				    	tablePosSalesSummary.column(9).visible(false);
				    	tablePosSalesSummary.column(10).visible(false);
				    	tablePosSalesSummary.column(11).visible(false);
				    	tablePosSalesSummary.column(12).visible(false);
				    	tablePosSalesSummary.column(13).visible(false);
	    			  	tablePosSalesSummary.column(14).visible(false);
				    	tablePosSalesSummary.column(15).visible(false);
				    	tablePosSalesSummary.column(16).visible(false);
				    	tablePosSalesSummary.column(17).visible(false);
				    	tablePosSalesSummary.column(18).visible(false);
				    	tablePosSalesSummary.column(19).visible(false);
				    	tablePosSalesSummary.column(20).visible(false);
				    	tablePosSalesSummary.column(21).visible(false);
				    	tablePosSalesSummary.column(22).visible(false);				    	
				    	tablePosSalesSummary.column(23).visible(false);				    	
				    	tablePosSalesSummary.column(24).visible(true);
				    	tablePosSalesSummary.column(25).visible(true);
				    	tablePosSalesSummary.column(26).visible(true);
				    	tablePosSalesSummary.column(27).visible(true);
				    	tablePosSalesSummary.column(28).visible(true);
				    	tablePosSalesSummary.column(29).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePosSalesSummary.column(30).visible(false);
				    	<%} else {%>
				    	tablePosSalesSummary.column(30).visible(true);
				    	<%} %>
				    	tablePosSalesSummary.column(31).visible(true);
				    	/* tablePosSalesSummary.column(29).visible(true);
				    	tablePosSalesSummary.column(30).visible(true);
				    	tablePosSalesSummary.column(31).visible(true);
				    	tablePosSalesSummary.column(32).visible(true);
				    	tablePosSalesSummary.column(33).visible(true); */

				    	tablePrint.column(1).visible(false);
	    			 	tablePrint.column(2).visible(false);
				    	tablePrint.column(3).visible(false);
				    	tablePrint.column(4).visible(false);
				    	tablePrint.column(5).visible(false);
				    	tablePrint.column(6).visible(false);
				    	tablePrint.column(7).visible(false);
				    	tablePrint.column(8).visible(false);
				    	tablePrint.column(9).visible(false);
				    	tablePrint.column(10).visible(false);
				    	tablePrint.column(11).visible(false);
				    	tablePrint.column(12).visible(false);
				    	tablePrint.column(13).visible(false);
	    			  	tablePrint.column(14).visible(false);
				    	tablePrint.column(15).visible(false);
				    	tablePrint.column(16).visible(false);
				    	tablePrint.column(17).visible(false);
				    	tablePrint.column(18).visible(false);
				    	tablePrint.column(19).visible(false);
				    	tablePrint.column(20).visible(false);
				    	tablePrint.column(21).visible(false);
				    	tablePrint.column(22).visible(false);				    	
				    	tablePrint.column(23).visible(false);				    	
				    	tablePrint.column(24).visible(true);
				    	tablePrint.column(25).visible(true);
				    	tablePrint.column(26).visible(true);
				    	tablePrint.column(27).visible(true);
				    	tablePrint.column(28).visible(true);
				    	tablePrint.column(29).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePrint.column(30).visible(false);
				    	<%} else {%>
				    	tablePrint.column(30).visible(true);
				    	<%} %>
				    	tablePrint.column(31).visible(true);
				    	/* tablePrint.column(29).visible(true);
				    	tablePrint.column(30).visible(true);
				    	tablePrint.column(31).visible(true);
				    	tablePrint.column(32).visible(true);
				    	tablePrint.column(33).visible(true); */
	    		  }else{
	    			  $(".ORD").show();
	    			  $(".PRD").hide();
	    			  $(".CUS").hide();
	    			  $(".CAT").hide();
	    			  $(".TRM").hide();

	    			  tablePosSalesSummary.column(1).visible(true);
	    			  	tablePosSalesSummary.column(2).visible(true);
				    	tablePosSalesSummary.column(3).visible(true);
				    	tablePosSalesSummary.column(4).visible(true);
				    	tablePosSalesSummary.column(5).visible(true);
				    	tablePosSalesSummary.column(6).visible(true);
				    	tablePosSalesSummary.column(7).visible(true);
				    	tablePosSalesSummary.column(8).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePosSalesSummary.column(9).visible(false);
				    	<%} else {%>
				    	tablePosSalesSummary.column(9).visible(true);
				    	<%} %>
				    	tablePosSalesSummary.column(10).visible(true);
				    	tablePosSalesSummary.column(11).visible(false);
				    	tablePosSalesSummary.column(12).visible(false);
				    	tablePosSalesSummary.column(13).visible(false);
				    	tablePosSalesSummary.column(14).visible(false);
				    	tablePosSalesSummary.column(15).visible(false);
				    	tablePosSalesSummary.column(16).visible(false);
				    	tablePosSalesSummary.column(17).visible(false);
				    	tablePosSalesSummary.column(18).visible(false);
				    	tablePosSalesSummary.column(19).visible(false);
				    	tablePosSalesSummary.column(20).visible(false);
				    	tablePosSalesSummary.column(21).visible(false);
				    	tablePosSalesSummary.column(22).visible(false);
				    	tablePosSalesSummary.column(23).visible(false);
				    	tablePosSalesSummary.column(24).visible(false);
				    	tablePosSalesSummary.column(25).visible(false);
				    	tablePosSalesSummary.column(26).visible(false);
				    	tablePosSalesSummary.column(27).visible(false);
				    	tablePosSalesSummary.column(28).visible(false);
				    	tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	/* tablePosSalesSummary.column(29).visible(false);
				    	tablePosSalesSummary.column(30).visible(false);
				    	tablePosSalesSummary.column(31).visible(false);
				    	tablePosSalesSummary.column(32).visible(false);
				    	tablePosSalesSummary.column(33).visible(false); */

				    	tablePrint.column(1).visible(true);
				    	tablePrint.column(2).visible(true);
				    	tablePrint.column(3).visible(true);
				    	tablePrint.column(4).visible(true);
				    	tablePrint.column(5).visible(true);
				    	tablePrint.column(6).visible(true);
				    	tablePrint.column(7).visible(true);
				    	tablePrint.column(8).visible(true);
				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				    	tablePrint.column(9).visible(false);
				    	<%} else {%>
				    	tablePrint.column(9).visible(true);
				    	<%} %>
				    	tablePrint.column(10).visible(true);
				    	tablePrint.column(11).visible(false);
				    	tablePrint.column(12).visible(false);
				    	tablePrint.column(13).visible(false);
				    	tablePrint.column(14).visible(false);
				    	tablePrint.column(15).visible(false);
				    	tablePrint.column(16).visible(false);
				    	tablePrint.column(17).visible(false);
				    	tablePrint.column(18).visible(false);
				    	tablePrint.column(19).visible(false);
				    	tablePrint.column(20).visible(false);
				    	tablePrint.column(21).visible(false);
				    	tablePrint.column(22).visible(false);
				    	tablePrint.column(23).visible(false);
				    	tablePrint.column(24).visible(false);
				    	tablePrint.column(25).visible(false);
				    	tablePrint.column(26).visible(false);
				    	tablePrint.column(27).visible(false);
				    	tablePrint.column(28).visible(false);
				    	tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	/* tablePrint.column(29).visible(false);
				    	tablePrint.column(30).visible(false);
				    	tablePrint.column(31).visible(false);
				    	tablePrint.column(32).visible(false);
				    	tablePrint.column(33).visible(false); */
		    	 }
		    }else{
			    tablePosSalesSummary = $('#tablePosSalesSummary').DataTable({
					"processing": true,
					"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
					
				        	/* if(typeof data.items[0].INDEX === 'undefined'){ */
				        	if(data.items.length === 0){
				        		return [];
				        	}else {
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
									data.items[dataIndex]['DONO'] = '<a href="../posreports/salesreportsdetail?TYPE=Order&DONO='+data.items[dataIndex]['DONO']+'">'+data.items[dataIndex]['DONO']+'</a>';
									data.items[dataIndex]['CCUSTOMER'] = '<a href="../posreports/salesreportsdetail?TYPE=Customer&CUSTCODE='+data.items[dataIndex]['CCUSTOMER']+'">'+data.items[dataIndex]['CCUSTOMER']+'</a>';
									data.items[dataIndex]['PITEM'] = '<a href="../posreports/salesreportsdetail?TYPE=Product&ITEM='+data.items[dataIndex]['PITEM']+'&FROMDATE='+ fdate +'&TODATE='+ tdate +'">'+data.items[dataIndex]['PITEM']+'</a>';
				        		}
				        		
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [

		    			{"data": 'INDEX', "orderable": true},
		    			{"data": 'SALESDATE', "orderable": true},
		    			{"data": 'BILLTIME', "orderable": true},
		    			{"data": 'DONO', "orderable": true},
		    			{"data": 'CNAME', "orderable": true},
		    			/* {"data": 'REFNUMBER', "orderable": true}, */
		    			{"data": 'PAYMODE', "orderable": true},
		    			{"data": 'AMOUNT', "orderable": true},
		    			{"data": 'DISCOUNT', "orderable": true},
		    			{"data": 'EXPRICE', "orderable": true},
		    			{"data": 'TAX', "orderable": true},
		    			{"data": 'TOTALAMOUNT', "orderable": true},
		    			{"data": 'PITEM', "orderable": true},
		    			{"data": 'PITEMDESC', "orderable": true},
		    			{"data": 'PQTY', "orderable": true},
		    			{"data": 'PAMOUNT', "orderable": true},
		    			{"data": 'CCUSTOMER', "orderable": true},
		    			{"data": 'CCUSTOMERNAME', "orderable": true},
		    			{"data": 'CAMOUNT', "orderable": true},
		    			{"data": 'CDISCOUNT', "orderable": true},
		    			{"data": 'CTAX', "orderable": true},
		    			{"data": 'CTOTALAMOUNT', "orderable": true},
		    			{"data": 'ZCATEGORY', "orderable": true},
		    			{"data": 'ZQTY', "orderable": true},
		    			{"data": 'ZAMOUNT', "orderable": true},
		    			{"data": 'OUTLET', "orderable": true},
		    			{"data": 'TERMINAL', "orderable": true},
		    			{"data": 'ORDDAY', "orderable": true},
		    			{"data": 'ORDDATE', "orderable": true},
		    			{"data": 'TOTALPRICE', "orderable": true},
		    			{"data": 'TOTALCOST', "orderable": true},
		    			{"data": 'TAX', "orderable": true},
		    			{"data": 'GPPER', "orderable": true},
		    			/* {"data": 'FROMDATE', "orderable": true},
		    			{"data": 'FROMSTATS', "orderable": true},
		    			{"data": 'TODATE', "orderable": true},
		    			{"data": 'TOSTATS', "orderable": true},
		    			{"data": 'DURATION', "orderable": true}, */
		    			
		    			],
					"columnDefs": [ {"className": "t-right", "targets": [6,7,8,9,10,13,14,17,18,19,20,22,23,28,29,30,31] } ],
					"orderFixed": [ ], 
					/*"dom": 'lBfrtip',*/
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			        buttons: [
			        	{
			                extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend : 'excel',
			                    	title: '<%=title %>',
			                    	exportOptions: {
			    	                	columns: [':visible']
			    	                }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	title: '<%=title %>',
			                    	exportOptions: {
			                    		columns: [':visible']
			                    	},
		                    		orientation: 'landscape',
		                            pageSize: 'A4'
			                    }
			                ]
			            },
			            /* {
    	                	extend : 'print',
    	                	exportOptions: {
	    	                	columns: [':visible']
	    	            	}
    	                }, */			            
			            {
		                    extend: 'colvis',
		                    columns: ':not(:eq('+groupColumn+')):not(:last)'
		                }
			        ],
			   	       "order": [],
			   	    "drawCallback": function ( settings ) {
						var groupColumn = 0;
						var groupRowColSpan= 5;
						if(type == "Product" || type == "Customer")
							groupRowColSpan= 2;
						if(type == "Category")
							groupRowColSpan= 1;
						if(type == "Terminal")
							groupRowColSpan= 4;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var totalIssueQty = 0;
			            var totalExprice = 0;
			            var groupTotalIssueQty = 0;
			            var totalIssuePriceQty = 0;
			            var groupTotalIssuePriceQty = 0;
			            var totalTax = 0;
			            var totalunitPrice = 0;
			            var groupTotalUnitPrice=0;
			            var groupTotalTax = 0;
			            var grouptotalExprice = 0;
			            var totalPrice = 0;
			            var groupPrice = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            var totalGstTax=0;
			            var groupTotalGstTax=0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
			                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null;
			               
			                	if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupPrice==null || groupPrice==0){
			                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
			                	}if(totalunitPrice==null || totalunitPrice==0){
			                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
			                	}if(totalGstTax==null || totalGstTax==0){
			                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
			                	}
			                	/* if (i > 0) {
			                		$(rows).eq( i ).before(
					                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td></td></tr>'
					                    );
			                	} */
			                    last = group;
			                    groupEnd = i;    
			                    groupTotalPickQty = 0;
			                    groupTotalIssueQty = 0;
			                    grouptotalExprice = 0;
			                    groupTotalIssuePriceQty = 0;
			                    groupTotalTax = 0;
			                    groupPrice = 0;
			                    totalunitPrice=0;
			                    groupTotalUnitPrice=0;
			                    groupTotalGstTax=0;
			                    
			                }
			                if(type == "Bill") {
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                grouptotalExprice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
			                totalExprice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
			                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
					    	<%} else {%>
					    	groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
					    	<%} %>
			                } else if(type == "Product") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                } else if(type == "Customer") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
						    	<%} else {%>
				                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
						    	<%} %>
			                } else if(type == "Terminal") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				                <%} else {%>
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
						    	<%}%>
				            } else {
				            	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
					        } 
			                
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	
			            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
			            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,
			            	groupPriceVal=null,totalUnitPriceVal=null,groupTotalUnitPriceVal=null,groupTotalGstTaxVal=null,totalGstTaxVal=null,totalExpriceVal=null,grouptotalExpriceVal=null;
			            	
			            	if(totalPickQty==null || totalPickQty==0){
			            		totalPickQtyVal="0.000";
		                	}else{
		                		if(type == "Bill" || type == "Customer")
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
		                		else
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(3);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal="0.000";
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssueQty==null || totalIssueQty==0){
		                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal="0.000";
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
		                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalTax==null || totalTax==0){
		                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(totalExprice==null || totalExprice==0){
		                		totalExpriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalExpriceVal=parseFloat(totalExprice).toFixed(<%=numberOfDecimal%>);
		                	}if(grouptotalExprice==null || grouptotalExprice==0){
		                		grouptotalExpriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		grouptotalExpriceVal=parseFloat(grouptotalExprice).toFixed(<%=numberOfDecimal%>);	
		                	}if(totalPrice==null || totalPrice==0){
		                		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalUnitPrice==null || groupTotalUnitPrice==0){
		                		groupTotalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalUnitPriceVal=parseFloat(groupTotalUnitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalGstTax==null || groupTotalGstTax==0){
		                		groupTotalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalGstTaxVal=parseFloat(groupTotalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}
		                	if(type == "Bill") {
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalExpriceVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalExpriceVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} %>	
		                    );
		                	} else if(type == "Customer") {
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} %>	
		                    );
		                	} else if(type == "Terminal") {
			                	
		                		var gpvalue = ((parseFloat(totalPickQtyVal)-parseFloat(totalIssueQtyVal))/(parseFloat(totalPickQtyVal)))*100;
			                	gpvalue = parseFloat(gpvalue).toFixed(<%=numberOfDecimal%>);
			                	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                		$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">'+gpvalue+'</td></tr>'
			                   
		                		<%} else {%>
		                		$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">'+totalTaxVal+'</td><td class=" t-right">'+gpvalue+'</td></tr>'
			                   
		                		<%} %>
		                		 );
		                	} else {
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td></tr>'
		                    );
		                	}
		                	/* $(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td></td><td></td><td></td><td></td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupPriceVal + '</td><td></td><td></td></tr>'
		                    ); */
		                }
			        } 
				});
		    }
		    tablePrint = $('#tableprint').DataTable({
					"processing": true,
					"lengthMenu": [[-1],["All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
					
				        	if(data.items.length === 0){
				        		return [];
				        	}else {
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [

		    			{"data": 'INDEX', "orderable": true},
		    			{"data": 'SALESDATE', "orderable": true},
		    			{"data": 'BILLTIME', "orderable": true},
		    			{"data": 'DONO', "orderable": true},
		    			{"data": 'CNAME', "orderable": true},
		    			/* {"data": 'REFNUMBER', "orderable": true}, */
		    			{"data": 'PAYMODE', "orderable": true},
		    			{"data": 'AMOUNT', "orderable": true},
		    			{"data": 'DISCOUNT', "orderable": true},
		    			{"data": 'EXPRICE', "orderable": true},
		    			{"data": 'TAX', "orderable": true},
		    			{"data": 'TOTALAMOUNT', "orderable": true},
		    			{"data": 'PITEM', "orderable": true},
		    			{"data": 'PITEMDESC', "orderable": true},
		    			{"data": 'PQTY', "orderable": true},
		    			{"data": 'PAMOUNT', "orderable": true},
		    			{"data": 'CCUSTOMER', "orderable": true},
		    			{"data": 'CCUSTOMERNAME', "orderable": true},
		    			{"data": 'CAMOUNT', "orderable": true},
		    			{"data": 'CDISCOUNT', "orderable": true},
		    			{"data": 'CTAX', "orderable": true},
		    			{"data": 'CTOTALAMOUNT', "orderable": true},
		    			{"data": 'ZCATEGORY', "orderable": true},
		    			{"data": 'ZQTY', "orderable": true},
		    			{"data": 'ZAMOUNT', "orderable": true},
		    			{"data": 'OUTLET', "orderable": true},
		    			{"data": 'TERMINAL', "orderable": true},
		    			{"data": 'ORDDAY', "orderable": true},
		    			{"data": 'ORDDATE', "orderable": true},
		    			{"data": 'TOTALPRICE', "orderable": true},
		    			{"data": 'TOTALCOST', "orderable": true},
		    			{"data": 'TAX', "orderable": true},
		    			{"data": 'GPPER', "orderable": true},
		    			/* {"data": 'FROMDATE', "orderable": true},
		    			{"data": 'FROMSTATS', "orderable": true},
		    			{"data": 'TODATE', "orderable": true},
		    			{"data": 'TOSTATS', "orderable": true},
		    			{"data": 'DURATION', "orderable": true}, */
		    			],
					"columnDefs": [ {"className": "t-right", "targets": [6,7,8,9,10,13,14,17,18,19,20,22,23,28,29,30,31] } ],
					"orderFixed": [ ], 
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			        buttons: [],
			   	    "order": [],
			   	    searching: false,
			        paging: false,
			        info: false,
			        "bLengthChange" : false, //thought this line could hide the LengthMenu
			   	    "drawCallback": function ( settings ) {
						var groupColumn = 0;
						var groupRowColSpan= 5;
						if(type == "Product" || type == "Customer")
							groupRowColSpan= 2;
						if(type == "Category")
							groupRowColSpan= 1;
						if(type == "Terminal")
							groupRowColSpan= 4;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var totalIssueQty = 0;
			            var totalExprice = 0;
			            var groupTotalIssueQty = 0;
			            var totalIssuePriceQty = 0;
			            var groupTotalIssuePriceQty = 0;
			            var totalTax = 0;
			            var totalunitPrice = 0;
			            var groupTotalUnitPrice=0;
			            var groupTotalTax = 0;
			            var grouptotalExprice = 0;
			            var totalPrice = 0;
			            var groupPrice = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            var totalGstTax=0;
			            var groupTotalGstTax=0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
			                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null;
			               
			                	if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupPrice==null || groupPrice==0){
			                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
			                	}if(totalunitPrice==null || totalunitPrice==0){
			                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
			                	}if(totalGstTax==null || totalGstTax==0){
			                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
			                	}
			                    last = group;
			                    groupEnd = i;    
			                    groupTotalPickQty = 0;
			                    groupTotalIssueQty = 0;
			                    grouptotalExprice = 0;
			                    groupTotalIssuePriceQty = 0;
			                    groupTotalTax = 0;
			                    groupPrice = 0;
			                    totalunitPrice=0;
			                    groupTotalUnitPrice=0;
			                    groupTotalGstTax=0;
			                    
			                }
			                if(type == "Bill") {
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                grouptotalExprice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
			                totalExprice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
			                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
					    	<%} else {%>
			                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
					    	<%} %>
			                } else if(type == "Product") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                } else if(type == "Customer") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
						    	<%} else {%>
				                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
						    	<%} %>
			                } else if(type == "Terminal") {
			                	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				                <%} else {%>
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
						    	<%}%>
				            } else {
				            	groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(2)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html().replace(',', '').replace('$', ''));
					        } 
			                
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	
			            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
			            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,
			            	groupPriceVal=null,totalUnitPriceVal=null,groupTotalUnitPriceVal=null,groupTotalGstTaxVal=null,totalGstTaxVal=null,totalExpriceVal=null,grouptotalExpriceVal=null;
			            	
			            	if(totalPickQty==null || totalPickQty==0){
			            		totalPickQtyVal="0.000";
		                	}else{
		                		if(type == "Bill" || type == "Customer")
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
		                		else
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(3);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal="0.000";
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssueQty==null || totalIssueQty==0){
		                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal="0.000";
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
		                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalTax==null || totalTax==0){
		                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(totalExprice==null || totalExprice==0){
		                		totalExpriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalExpriceVal=parseFloat(totalExprice).toFixed(<%=numberOfDecimal%>);
		                	}if(grouptotalExprice==null || grouptotalExprice==0){
		                		grouptotalExpriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		grouptotalExpriceVal=parseFloat(grouptotalExprice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalPrice==null || totalPrice==0){
		                		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalUnitPrice==null || groupTotalUnitPrice==0){
		                		groupTotalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalUnitPriceVal=parseFloat(groupTotalUnitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalGstTax==null || groupTotalGstTax==0){
		                		groupTotalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalGstTaxVal=parseFloat(groupTotalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}
		                	if(type == "Bill") {
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalExprice + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalExprice + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} %>	
		                    );
		                	} else if(type == "Customer") {
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} %>	
		                    );
			                	
		                	} else if(type == "Terminal") {
		                	/* $(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td></td></tr>'
		                    ); */
		                		var gpvalue = ((parseFloat(totalPickQtyVal)-parseFloat(totalIssueQtyVal))/(parseFloat(totalPickQtyVal)))*100;
			                	gpvalue = parseFloat(gpvalue).toFixed(<%=numberOfDecimal%>);
		                		
		                		/* $(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">'+totalTaxVal+'</td><td class=" t-right">'+gpvalue+'</td></tr>'
			                    ); */
			                    
			                	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                		$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">'+gpvalue+'</td></tr>'
			                   
		                		<%} else {%>
		                		$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">'+totalTaxVal+'</td><td class=" t-right">'+gpvalue+'</td></tr>'
			                   
		                		<%} %>
		                		 );
		                	} else {
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td></tr>'
		                    );
		                	}
		                }
			        },
			        "bDestroy": true 
				});



		                	 if(type == "Bill"){
		   	    			  $(".ORD").show();
		   	    			  $(".PRD").hide();
		   	    			  $(".CUS").hide();
		   	    			  $(".CAT").hide();
		   	    			  $(".TRM").hide();

		   	    			  	tablePosSalesSummary.column(1).visible(true);
		   	    			  	tablePosSalesSummary.column(2).visible(true);
		   				    	tablePosSalesSummary.column(3).visible(true);
		   				    	tablePosSalesSummary.column(4).visible(true);
		   				    	tablePosSalesSummary.column(5).visible(true);
		   				    	tablePosSalesSummary.column(6).visible(true);
		   				    	tablePosSalesSummary.column(7).visible(true);
		   				    	tablePosSalesSummary.column(8).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	<%} else {%>
		   				    	tablePosSalesSummary.column(9).visible(true);
		   				    	<%} %>
		   				    	tablePosSalesSummary.column(10).visible(true);
		   				    	tablePosSalesSummary.column(11).visible(false);
		   				    	tablePosSalesSummary.column(12).visible(false);
		   				    	tablePosSalesSummary.column(13).visible(false);
		   				    	tablePosSalesSummary.column(14).visible(false);
		   				    	tablePosSalesSummary.column(15).visible(false);
		   				    	tablePosSalesSummary.column(16).visible(false);
		   				    	tablePosSalesSummary.column(17).visible(false);
		   				    	tablePosSalesSummary.column(18).visible(false);
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	tablePosSalesSummary.column(20).visible(false);
		   				    	tablePosSalesSummary.column(21).visible(false);
		   				    	tablePosSalesSummary.column(22).visible(false);
		   				    	tablePosSalesSummary.column(23).visible(false);
		   				    	tablePosSalesSummary.column(24).visible(false);
		   				    	tablePosSalesSummary.column(25).visible(false);
		   				    	tablePosSalesSummary.column(26).visible(false);
		   				    	tablePosSalesSummary.column(27).visible(false);
		   				    	tablePosSalesSummary.column(28).visible(false);
		   				    	tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	/* tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	tablePosSalesSummary.column(32).visible(false);
		   				    	tablePosSalesSummary.column(33).visible(false); */

		   				    	tablePrint.column(1).visible(true);
		   				    	tablePrint.column(2).visible(true);
		   				    	tablePrint.column(3).visible(true);
		   				    	tablePrint.column(4).visible(true);
		   				    	tablePrint.column(5).visible(true);
		   				    	tablePrint.column(6).visible(true);
		   				    	tablePrint.column(7).visible(true);
		   				    	tablePrint.column(8).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePrint.column(9).visible(false);
		   				    	<%} else {%>
		   				    	tablePrint.column(9).visible(true);
		   				    	<%} %>
		   				    	tablePrint.column(10).visible(true);
		   				    	tablePrint.column(11).visible(false);
		   				    	tablePrint.column(12).visible(false);
		   				    	tablePrint.column(13).visible(false);
		   				    	tablePrint.column(14).visible(false);
		   				    	tablePrint.column(15).visible(false);
		   				    	tablePrint.column(16).visible(false);
		   				    	tablePrint.column(17).visible(false);
		   				    	tablePrint.column(18).visible(false);
		   				    	tablePrint.column(19).visible(false);
		   				    	tablePrint.column(20).visible(false);
		   				    	tablePrint.column(21).visible(false);
		   				    	tablePrint.column(22).visible(false);
		   				    	tablePrint.column(23).visible(false);
		   				    	tablePrint.column(24).visible(false);
		   				    	tablePrint.column(25).visible(false);
		   				    	tablePrint.column(26).visible(false);
		   				    	tablePrint.column(27).visible(false);
		   				    	tablePrint.column(28).visible(false);
		   				    	tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	/* tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	tablePrint.column(32).visible(false);
		   				    	tablePrint.column(33).visible(false); */
		   	    		  }else if(type == "Product"){
		   	    	  		  $(".PRD").show();
		   	    	  		  $(".ORD").hide();
		   	    			  $(".CUS").hide();
		   	    			  $(".CAT").hide();
		   	    			  $(".TRM").hide();

		   	    			  	tablePosSalesSummary.column(1).visible(false);
		   	    			  	tablePosSalesSummary.column(2).visible(false);
		   				    	tablePosSalesSummary.column(3).visible(false);
		   				    	tablePosSalesSummary.column(4).visible(false);
		   				    	tablePosSalesSummary.column(5).visible(false);
		   				    	tablePosSalesSummary.column(6).visible(false);
		   				    	tablePosSalesSummary.column(7).visible(false);
		   				    	tablePosSalesSummary.column(8).visible(false);
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	tablePosSalesSummary.column(10).visible(false);
		   						tablePosSalesSummary.column(11).visible(true);
		   				    	tablePosSalesSummary.column(12).visible(true);
		   				    	tablePosSalesSummary.column(13).visible(true);
		   				    	tablePosSalesSummary.column(14).visible(true);
		   				    	tablePosSalesSummary.column(15).visible(false);
		   				    	tablePosSalesSummary.column(16).visible(false);
		   				    	tablePosSalesSummary.column(17).visible(false);
		   				    	tablePosSalesSummary.column(18).visible(false);
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	tablePosSalesSummary.column(20).visible(false);
		   				    	tablePosSalesSummary.column(21).visible(false);
		   				    	tablePosSalesSummary.column(22).visible(false);
		   				    	tablePosSalesSummary.column(23).visible(false);
		   				    	tablePosSalesSummary.column(24).visible(false);
		   				    	tablePosSalesSummary.column(25).visible(false);
		   				    	tablePosSalesSummary.column(26).visible(false);
		   				    	tablePosSalesSummary.column(27).visible(false);
		   				    	tablePosSalesSummary.column(28).visible(false);
		   				    	tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	/* tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	tablePosSalesSummary.column(32).visible(false);
		   				    	tablePosSalesSummary.column(33).visible(false); */

		   				    	tablePrint.column(1).visible(false);
		   	    			  	tablePrint.column(2).visible(false);
		   				    	tablePrint.column(3).visible(false);
		   				    	tablePrint.column(4).visible(false);
		   				    	tablePrint.column(5).visible(false);
		   				    	tablePrint.column(6).visible(false);
		   				    	tablePrint.column(7).visible(false);
		   				    	tablePrint.column(8).visible(false);
		   				    	tablePrint.column(9).visible(false);
		   				    	tablePrint.column(10).visible(false);
		   						tablePrint.column(11).visible(true);
		   				    	tablePrint.column(12).visible(true);
		   				    	tablePrint.column(13).visible(true);
		   				    	tablePrint.column(14).visible(true);
		   				    	tablePrint.column(15).visible(false);
		   				    	tablePrint.column(16).visible(false);
		   				    	tablePrint.column(17).visible(false);
		   				    	tablePrint.column(18).visible(false);
		   				    	tablePrint.column(19).visible(false);
		   				    	tablePrint.column(20).visible(false);
		   				    	tablePrint.column(21).visible(false);
		   				    	tablePrint.column(22).visible(false);
		   				    	tablePrint.column(23).visible(false);
		   				    	tablePrint.column(24).visible(false);
		   				    	tablePrint.column(25).visible(false);
		   				    	tablePrint.column(26).visible(false);
		   				    	tablePrint.column(27).visible(false);
		   				    	tablePrint.column(28).visible(false);
		   				    	tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	/* tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	tablePrint.column(32).visible(false);
		   				    	tablePrint.column(33).visible(false); */
		   				    	
		   	    		  }else if(type == "Customer"){
		   	    			  $(".CUS").show();
		   	    			  $(".ORD").hide();
		   	    			  $(".PRD").hide();
		   	    			  $(".CAT").hide();
		   	    			  $(".TRM").hide();

		   	    			  	tablePosSalesSummary.column(1).visible(false);
		   	    			  	tablePosSalesSummary.column(2).visible(false);
		   				    	tablePosSalesSummary.column(3).visible(false);
		   				    	tablePosSalesSummary.column(4).visible(false);
		   				    	tablePosSalesSummary.column(5).visible(false);
		   				    	tablePosSalesSummary.column(6).visible(false);
		   				    	tablePosSalesSummary.column(7).visible(false);
		   				    	tablePosSalesSummary.column(8).visible(false);
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	tablePosSalesSummary.column(10).visible(false);
		   				    	tablePosSalesSummary.column(11).visible(false);
		   				    	tablePosSalesSummary.column(12).visible(false);
		   				    	tablePosSalesSummary.column(13).visible(false);
		   				    	tablePosSalesSummary.column(14).visible(false);
		   	    			  	tablePosSalesSummary.column(15).visible(true);
		   				    	tablePosSalesSummary.column(16).visible(true);
		   				    	tablePosSalesSummary.column(17).visible(true);
		   				    	tablePosSalesSummary.column(18).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	<%} else {%>
		   				    	tablePosSalesSummary.column(19).visible(true);
		   				    	<%} %>
		   				    	tablePosSalesSummary.column(20).visible(true);
		   				    	tablePosSalesSummary.column(21).visible(false);
		   				    	tablePosSalesSummary.column(22).visible(false);
		   				    	tablePosSalesSummary.column(23).visible(false);
		   				    	tablePosSalesSummary.column(24).visible(false);
		   				    	tablePosSalesSummary.column(25).visible(false);
		   				    	tablePosSalesSummary.column(26).visible(false);
		   				    	tablePosSalesSummary.column(27).visible(false);
		   				    	tablePosSalesSummary.column(28).visible(false);
		   				    	tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	/* tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	tablePosSalesSummary.column(32).visible(false);
		   				    	tablePosSalesSummary.column(33).visible(false); */

		   				    	tablePrint.column(1).visible(false);
		   	    			  	tablePrint.column(2).visible(false);
		   				    	tablePrint.column(3).visible(false);
		   				    	tablePrint.column(4).visible(false);
		   				    	tablePrint.column(5).visible(false);
		   				    	tablePrint.column(6).visible(false);
		   				    	tablePrint.column(7).visible(false);
		   				    	tablePrint.column(8).visible(false);
		   				    	tablePrint.column(9).visible(false);
		   				    	tablePrint.column(10).visible(false);
		   				    	tablePrint.column(11).visible(false);
		   				    	tablePrint.column(12).visible(false);
		   				    	tablePrint.column(13).visible(false);
		   				    	tablePrint.column(14).visible(false);
		   	    			  	tablePrint.column(15).visible(true);
		   				    	tablePrint.column(16).visible(true);
		   				    	tablePrint.column(17).visible(true);
		   				    	tablePrint.column(18).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePrint.column(19).visible(false);
		   				    	<%} else {%>
		   				    	tablePrint.column(19).visible(true);
		   				    	<%} %>
		   				    	tablePrint.column(20).visible(true);
		   				    	tablePrint.column(21).visible(false);
		   				    	tablePrint.column(22).visible(false);
		   				    	tablePrint.column(23).visible(false);
		   				    	tablePrint.column(24).visible(false);
		   				    	tablePrint.column(25).visible(false);
		   				    	tablePrint.column(26).visible(false);
		   				    	tablePrint.column(27).visible(false);
		   				    	tablePrint.column(28).visible(false);
		   				    	tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	/* tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	tablePrint.column(32).visible(false);
		   				    	tablePrint.column(33).visible(false); */
		   	    		  }else if(type == "Category"){
		   	    			  $(".CAT").show();
		   	    			  $(".ORD").hide();
		   	    			  $(".CUS").hide();
		   	    			  $(".PRD").hide();
		   	    			  $(".TRM").hide();

		   	    			 	tablePosSalesSummary.column(1).visible(false);
		   	    			 	tablePosSalesSummary.column(2).visible(false);
		   				    	tablePosSalesSummary.column(3).visible(false);
		   				    	tablePosSalesSummary.column(4).visible(false);
		   				    	tablePosSalesSummary.column(5).visible(false);
		   				    	tablePosSalesSummary.column(6).visible(false);
		   				    	tablePosSalesSummary.column(7).visible(false);
		   				    	tablePosSalesSummary.column(8).visible(false);
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	tablePosSalesSummary.column(10).visible(false);
		   				    	tablePosSalesSummary.column(11).visible(false);
		   				    	tablePosSalesSummary.column(12).visible(false);
		   				    	tablePosSalesSummary.column(13).visible(false);
		   	    			  	tablePosSalesSummary.column(14).visible(false);
		   				    	tablePosSalesSummary.column(15).visible(false);
		   				    	tablePosSalesSummary.column(16).visible(false);
		   				    	tablePosSalesSummary.column(17).visible(false);
		   				    	tablePosSalesSummary.column(18).visible(false);
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	tablePosSalesSummary.column(20).visible(false);
		   				    	tablePosSalesSummary.column(21).visible(true);
		   				    	tablePosSalesSummary.column(22).visible(true);
		   				    	tablePosSalesSummary.column(23).visible(true);
		   				    	tablePosSalesSummary.column(24).visible(false);
		   				    	tablePosSalesSummary.column(25).visible(false);
		   				    	tablePosSalesSummary.column(26).visible(false);
		   				    	tablePosSalesSummary.column(27).visible(false);
		   				    	tablePosSalesSummary.column(28).visible(false);
		   				    	tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	/* tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	tablePosSalesSummary.column(32).visible(false);
		   				    	tablePosSalesSummary.column(33).visible(false); */

		   				    	tablePrint.column(1).visible(false);
		   	    			 	tablePrint.column(2).visible(false);
		   				    	tablePrint.column(3).visible(false);
		   				    	tablePrint.column(4).visible(false);
		   				    	tablePrint.column(5).visible(false);
		   				    	tablePrint.column(6).visible(false);
		   				    	tablePrint.column(7).visible(false);
		   				    	tablePrint.column(8).visible(false);
		   				    	tablePrint.column(9).visible(false);
		   				    	tablePrint.column(10).visible(false);
		   				    	tablePrint.column(11).visible(false);
		   				    	tablePrint.column(12).visible(false);
		   				    	tablePrint.column(13).visible(false);
		   	    			  	tablePrint.column(14).visible(false);
		   				    	tablePrint.column(15).visible(false);
		   				    	tablePrint.column(16).visible(false);
		   				    	tablePrint.column(17).visible(false);
		   				    	tablePrint.column(18).visible(false);
		   				    	tablePrint.column(19).visible(false);
		   				    	tablePrint.column(20).visible(false);
		   				    	tablePrint.column(21).visible(true);
		   				    	tablePrint.column(22).visible(true);
		   				    	tablePrint.column(23).visible(true);
		   				    	tablePrint.column(24).visible(false);
		   				    	tablePrint.column(25).visible(false);
		   				    	tablePrint.column(26).visible(false);
		   				    	tablePrint.column(27).visible(false);
		   				    	tablePrint.column(28).visible(false);
		   				    	tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	/* tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	tablePrint.column(32).visible(false);
		   				    	tablePrint.column(33).visible(false); */
		   	    		  }else if(type == "Terminal"){
		   	    			  $(".CAT").hide();
		   	    			  $(".ORD").hide();
		   	    			  $(".CUS").hide();
		   	    			  $(".PRD").hide();
		   	    			  $(".TRM").show();

		   	    			 	tablePosSalesSummary.column(1).visible(false);
		   	    			 	tablePosSalesSummary.column(2).visible(false);
		   				    	tablePosSalesSummary.column(3).visible(false);
		   				    	tablePosSalesSummary.column(4).visible(false);
		   				    	tablePosSalesSummary.column(5).visible(false);
		   				    	tablePosSalesSummary.column(6).visible(false);
		   				    	tablePosSalesSummary.column(7).visible(false);
		   				    	tablePosSalesSummary.column(8).visible(false);
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	tablePosSalesSummary.column(10).visible(false);
		   				    	tablePosSalesSummary.column(11).visible(false);
		   				    	tablePosSalesSummary.column(12).visible(false);
		   				    	tablePosSalesSummary.column(13).visible(false);
		   	    			  	tablePosSalesSummary.column(14).visible(false);
		   				    	tablePosSalesSummary.column(15).visible(false);
		   				    	tablePosSalesSummary.column(16).visible(false);
		   				    	tablePosSalesSummary.column(17).visible(false);
		   				    	tablePosSalesSummary.column(18).visible(false);
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	tablePosSalesSummary.column(20).visible(false);
		   				    	tablePosSalesSummary.column(21).visible(false);
		   				    	tablePosSalesSummary.column(22).visible(false);				    	
		   				    	tablePosSalesSummary.column(23).visible(false);				    	
		   				    	tablePosSalesSummary.column(24).visible(true);
		   				    	tablePosSalesSummary.column(25).visible(true);
		   				    	tablePosSalesSummary.column(26).visible(true);
		   				    	tablePosSalesSummary.column(27).visible(true);
		   				    	tablePosSalesSummary.column(28).visible(true);
		   				    	tablePosSalesSummary.column(29).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
						    	tablePosSalesSummary.column(30).visible(false);
						    	<%} else {%>
						    	tablePosSalesSummary.column(30).visible(true);
						    	<%} %>
						    	tablePosSalesSummary.column(31).visible(true);
		   				    	/* tablePosSalesSummary.column(29).visible(true);
		   				    	tablePosSalesSummary.column(30).visible(true);
		   				    	tablePosSalesSummary.column(31).visible(true);
		   				    	tablePosSalesSummary.column(32).visible(true);
		   				    	tablePosSalesSummary.column(33).visible(true); */

		   				    	tablePrint.column(1).visible(false);
		   	    			 	tablePrint.column(2).visible(false);
		   				    	tablePrint.column(3).visible(false);
		   				    	tablePrint.column(4).visible(false);
		   				    	tablePrint.column(5).visible(false);
		   				    	tablePrint.column(6).visible(false);
		   				    	tablePrint.column(7).visible(false);
		   				    	tablePrint.column(8).visible(false);
		   				    	tablePrint.column(9).visible(false);
		   				    	tablePrint.column(10).visible(false);
		   				    	tablePrint.column(11).visible(false);
		   				    	tablePrint.column(12).visible(false);
		   				    	tablePrint.column(13).visible(false);
		   	    			  	tablePrint.column(14).visible(false);
		   				    	tablePrint.column(15).visible(false);
		   				    	tablePrint.column(16).visible(false);
		   				    	tablePrint.column(17).visible(false);
		   				    	tablePrint.column(18).visible(false);
		   				    	tablePrint.column(19).visible(false);
		   				    	tablePrint.column(20).visible(false);
		   				    	tablePrint.column(21).visible(false);
		   				    	tablePrint.column(22).visible(false);				    	
		   				    	tablePrint.column(23).visible(false);				    	
		   				    	tablePrint.column(24).visible(true);
		   				    	tablePrint.column(25).visible(true);
		   				    	tablePrint.column(26).visible(true);
		   				    	tablePrint.column(27).visible(true);
		   				    	tablePrint.column(28).visible(true);
		   				    	tablePrint.column(29).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePrint.column(30).visible(false);
						    	<%} else {%>
						    	tablePrint.column(30).visible(true);
						    	<%} %>
						    	tablePrint.column(31).visible(true);
		   				    	/* tablePrint.column(29).visible(true);
		   				    	tablePrint.column(30).visible(true);
		   				    	tablePrint.column(31).visible(true);
		   				    	tablePrint.column(32).visible(true);
		   				    	tablePrint.column(33).visible(true); */
		   	    		  }else{
		   	    			  $(".ORD").show();
		   	    			  $(".PRD").hide();
		   	    			  $(".CUS").hide();
		   	    			  $(".CAT").hide();
		   	    			  $(".TRM").hide();

		   	    			  tablePosSalesSummary.column(1).visible(true);
		   	    			  	tablePosSalesSummary.column(2).visible(true);
		   				    	tablePosSalesSummary.column(3).visible(true);
		   				    	tablePosSalesSummary.column(4).visible(true);
		   				    	tablePosSalesSummary.column(5).visible(true);
		   				    	tablePosSalesSummary.column(6).visible(true);
		   				    	tablePosSalesSummary.column(7).visible(true);
		   				    	tablePosSalesSummary.column(8).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePosSalesSummary.column(9).visible(false);
		   				    	<%} else {%>
		   				    	tablePosSalesSummary.column(9).visible(true);
		   				    	<%} %>
		   				    	tablePosSalesSummary.column(10).visible(true);
		   				    	tablePosSalesSummary.column(11).visible(false);
		   				    	tablePosSalesSummary.column(12).visible(false);
		   				    	tablePosSalesSummary.column(13).visible(false);
		   				    	tablePosSalesSummary.column(14).visible(false);
		   				    	tablePosSalesSummary.column(15).visible(false);
		   				    	tablePosSalesSummary.column(16).visible(false);
		   				    	tablePosSalesSummary.column(17).visible(false);
		   				    	tablePosSalesSummary.column(18).visible(false);
		   				    	tablePosSalesSummary.column(19).visible(false);
		   				    	tablePosSalesSummary.column(20).visible(false);
		   				    	tablePosSalesSummary.column(21).visible(false);
		   				    	tablePosSalesSummary.column(22).visible(false);
		   				    	tablePosSalesSummary.column(23).visible(false);
		   				    	tablePosSalesSummary.column(24).visible(false);
		   				    	tablePosSalesSummary.column(25).visible(false);
		   				    	tablePosSalesSummary.column(26).visible(false);
		   				    	tablePosSalesSummary.column(27).visible(false);
		   				    	tablePosSalesSummary.column(28).visible(false);
		   				    	tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	/* tablePosSalesSummary.column(29).visible(false);
		   				    	tablePosSalesSummary.column(30).visible(false);
		   				    	tablePosSalesSummary.column(31).visible(false);
		   				    	tablePosSalesSummary.column(32).visible(false);
		   				    	tablePosSalesSummary.column(33).visible(false); */

		   				    	tablePrint.column(1).visible(true);
		   				    	tablePrint.column(2).visible(true);
		   				    	tablePrint.column(3).visible(true);
		   				    	tablePrint.column(4).visible(true);
		   				    	tablePrint.column(5).visible(true);
		   				    	tablePrint.column(6).visible(true);
		   				    	tablePrint.column(7).visible(true);
		   				    	tablePrint.column(8).visible(true);
		   				    	<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		   				    	tablePrint.column(9).visible(false);
		   				    	<%} else {%>
		   				    	tablePrint.column(9).visible(true);
		   				    	<%} %>
		   				    	tablePrint.column(10).visible(true);
		   				    	tablePrint.column(11).visible(false);
		   				    	tablePrint.column(12).visible(false);
		   				    	tablePrint.column(13).visible(false);
		   				    	tablePrint.column(14).visible(false);
		   				    	tablePrint.column(15).visible(false);
		   				    	tablePrint.column(16).visible(false);
		   				    	tablePrint.column(17).visible(false);
		   				    	tablePrint.column(18).visible(false);
		   				    	tablePrint.column(19).visible(false);
		   				    	tablePrint.column(20).visible(false);
		   				    	tablePrint.column(21).visible(false);
		   				    	tablePrint.column(22).visible(false);
		   				    	tablePrint.column(23).visible(false);
		   				    	tablePrint.column(24).visible(false);
		   				    	tablePrint.column(25).visible(false);
		   				    	tablePrint.column(26).visible(false);
		   				    	tablePrint.column(27).visible(false);
		   				    	tablePrint.column(28).visible(false);
		   				    	tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	/* tablePrint.column(29).visible(false);
		   				    	tablePrint.column(30).visible(false);
		   				    	tablePrint.column(31).visible(false);
		   				    	tablePrint.column(32).visible(false);
		   				    	tablePrint.column(33).visible(false); */
		   		    	 }
		}
		

		$('#tablePosSalesSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tablePosSalesSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tablePosSalesSummary').attr('width', '100%');
		});

		  function callback(data){
				
				var outPutdata = getTable();
				var ii = 0;
				var errorBoo = false;
				$.each(data.errors, function(i,error){
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						
					}
				});
				
				if(!errorBoo){
					
			        $.each(data.items, function(i,item){
			        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
		                     
			        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
		                      	ii++;
			            
			          });
				}else{
			}
		      outPutdata = outPutdata +'</TABLE>';
		      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		       document.getElementById('spinnerImg').innerHTML ='';

		   
		 }
					
			


		</script>
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
 
$(document).ready(function(){
	onGo();
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
     if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
      
	document.form.srctype.value= "Terminal";
	if($("input[name=srctype]").val() == "Terminal"){
	$(".ORD").hide();
	$(".PRD").hide();
	$(".CUS").hide();
	$(".CAT").hide();
	$(".TRM").show();
	}else{
		$(".TRM").hide();
		}
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	  
	  $("#SEARCHTYPE").typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  name: 'searchtype',
    	  display: 'year',  
    	  source: substringMatcher(searchtype),
    	  limit: 9999,
    	  templates: {
    	  empty: [
    		  '<div style="padding:3px 20px">',
    			'No results found',
    		  '</div>',
    		].join('\n'),
    		suggestion: function(data) {
//     		return '<p>' + data.value + '</p>';
    		return '<div onclick="setType(\''+data.value+'\')"><p>' + data.year + '</p></div>';
    		}
    	  }
    	}).on('typeahead:render',function(event,selection){
    		  
    	}).on('typeahead:open',function(event,selection){
    		
    	}).on('typeahead:close',function(){
    		
    	});

	  /* Order Number Auto Suggestion */
		$('#ORDERNO').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'DONO',  
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/InvoiceServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
							DONO : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.orders);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<p>' + data.DONO + '</p>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.ORDERNO.value = "";
				}
			});

		/* Customer Auto Suggestion */
		$('#CUSTOMER').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();  
				$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.CUST_CODE.value = "";
				}
			});

		/* outlet Auto Suggestion */
		$('#OUTLET_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'OUTLET_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_OUTLET_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
// 			    return '<div><p class="item-suggestion"> ' + data.OUTLET_NAME + '</p></div>';
			    return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				removeterminaldropdown();
	     		addterminaldropdown();
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.OUTCODE.value = "";
				}
				$('#TERMINALNAME').typeahead('val',document.form.TERMINALNAME.value);
				$('#TERMINAL_CODE').val(document.form.TERMINAL_CODE.value);
			});

		/* terminal Auto Suggestion */
		$('#TERMINALNAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TERMINAL_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TERMINAL_DATA",
// 					ONAME : document.form.OUTCODE.value,
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div onclick="document.form.TERMINAL_CODE.value = \''+data.TERMINAL+'\'"><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.TERMINALNAME.value = "";
					document.form.TERMINAL_CODE.value = "";
				}
			});

		/* Employee Auto Suggestion */
		$('#CUST_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "getEmployeeListStartsWithName",
					TYPE : "CASHIER",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMP_MST);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = $(".tt-menu").height()+35;
				top+="px";
				$('.supplierAddBtn').remove();  
				$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
				$(".supplierAddBtn").width($(".tt-menu").width());
				$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			});

		/* sales Auto Suggestion */
		$('#SALES_MAN').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "getEmployeeListStartsWithName",
					TYPE : "SALES",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMP_MST);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = $(".tt-menu").height()+35;
				top+="px";
				$('.supplierAddBtn').remove();  
				$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
				$(".supplierAddBtn").width($(".tt-menu").width());
				$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			});

		/* Product Auto Suggestion */
		$('#item').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'ITEM',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
					ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
					ITEM : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.items);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
					return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
				//return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			});

		/*category Auto Suggestion */
		$('#PRD_CLS_ID').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_CLS_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_CLS_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_CLSMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

		/*SubCategory Auto Suggestion */
		$('#PRD_TYPE_ID').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_TYPE_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_TYPE_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_TYPEMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		/*Brand Auto Suggestion */
		$('#PRD_BRAND_ID').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_BRAND_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_BRAND_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_BRANDMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

		/*Payment_Type Auto Suggestion */
		$('#PAYMENTTYPE').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PAYMENTMODE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/PaymentModeMst";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "GET_PAYMENT_MODE_LIST",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PAYMENTMODE);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
					return '<p>' + data.PAYMENTMODE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

});

var searchtype =   [{
    "year": "BILL",
    "value": "Bill",
    "tokens": [
      "Bill"
    ]
  },
	  	{
    "year": "PRODUCT",
    "value": "Product",
    "tokens": [
      "Product"
    ]
  },
	  	{
    "year": "CUSTOMER",
    "value": "Customer",
    "tokens": [
      "Customer"
    ]
  },
  {
    "year": "CATEGORY",
    "value": "Category",
    "tokens": [
      "Category"
	    ]
	  },
	  {
		    "year": "TERMINAL",
		    "value": "Terminal",
		    "tokens": [
		      "Terminal"
			    ]
			  }];
		
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    matches = [];
	    substrRegex = new RegExp(q, 'i');
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

function setType(value){
	
	document.form.srctype.value= value;

	/*   if(value == "Order"){
		  $(".ORD").show();
		  $(".PRD").hide();
		  $(".CUS").hide();
		  $(".CAT").hide();
	  }
	
 	 if(value == "Product"){
  		  $(".PRD").show();
  		  $(".ORD").hide();
		  $(".CUS").hide();
		  $(".CAT").hide();
	  }
	
	 if(value == "Customer"){
		  $(".CUS").show();
		  $(".ORD").hide();
		  $(".PRD").hide();
		  $(".CAT").hide();
	  }
	
	 if(value == "Category"){
		  $(".CAT").show();
		  $(".ORD").hide();
		  $(".CUS").hide();
		  $(".PRD").hide();
	  } */
	
	
	}

function removeterminaldropdown(){
	$("#TERMINALNAME").typeahead('destroy');
}

function changeitem(obj){
	 $("#item").typeahead('val', '"');
	 $("#item").typeahead('val', '');
	 $("#item").focus();
	}

function changesearchtype(obj){
	 $("#SEARCHTYPE").typeahead('val', '"');
	 $("#SEARCHTYPE").typeahead('val', '');
	 $("#SEARCHTYPE").focus();
	}
function changeorderno(obj){
	 $("#ORDERNO").typeahead('val', '"');
	 $("#ORDERNO").typeahead('val', '');
	 $("#ORDERNO").focus();
	}
function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}
function changeoutlet(obj){
	 $("#OUTLET_NAME").typeahead('val', '"');
	 $("#OUTLET_NAME").typeahead('val', '');
	 $("#OUTLET_NAME").focus();
	}
function changeterminal(obj){
	 $("#TERMINALNAME").typeahead('val', '"');
	 $("#TERMINALNAME").typeahead('val', '');
	 $("#TERMINALNAME").focus();
	}
function changecust_name(obj){
	 $("#CUST_NAME").typeahead('val', '"');
	 $("#CUST_NAME").typeahead('val', '');
	 $("#CUST_NAME").focus();
	}
function changesales_man(obj){
	 $("#SALES_MAN").typeahead('val', '"');
	 $("#SALES_MAN").typeahead('val', '');
	 $("#SALES_MAN").focus();
	}
function changeproductbrand(obj){
	 $("#PRD_BRAND_ID").typeahead('val', '"');
	 $("#PRD_BRAND_ID").typeahead('val', '');
	 $("#PRD_BRAND_ID").focus();
	}
function changeProductType(obj){
	 $("#PRD_TYPE_ID").typeahead('val', '"');
	 $("#PRD_TYPE_ID").typeahead('val', '');
	 $("#PRD_TYPE_ID").focus();
	}
function changepaymenttype(obj){
	 $("#PAYMENTTYPE").typeahead('val', '"');
	 $("#PAYMENTTYPE").typeahead('val', '');
	 $("#PAYMENTTYPE").focus();
	}
function changeCategory(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}

function addterminaldropdown(){
	  $('#TERMINALNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TERMINAL_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_TERMINAL_DATA",
				ONAME : document.form.OUTCODE.value,
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.POSOUTLETS);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    //return '<div><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
		    	 return '<div onclick="document.form.TERMINAL_CODE.value = \''+data.TERMINAL+'\'"><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.TERMINALNAME.value = "";
				document.form.TERMINAL_CODE.value = "";
			}
		});

}

function setOutletData(OUTLET,OUTLET_NAME){
	$("input[name=OUTCODE]").val(OUTLET);
}

function getcustname(TAXTREATMENT,CUSTNO){
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form.CUST_CODE.value = CUSTNO;
}

function PrintTable() {
    var printWindow = window.open('', '', '');
    printWindow.document.write('<html><head><title>Print <%=title %></title>');

    //Print the Table CSS.
    var table_style = document.getElementById("table_style").innerHTML;
    printWindow.document.write('<style type = "text/css">');
    printWindow.document.write(table_style);
    printWindow.document.write('</style>');
    printWindow.document.write('</head>');

    //Print the DIV contents i.e. the HTML Table.
    printWindow.document.write('<body>');
    var divContents = document.getElementById("dvContents").innerHTML;
    printWindow.document.write(divContents);
    printWindow.document.write('</body>');

    printWindow.document.write('</html>');
    printWindow.document.close();
    printWindow.print();
}


function yyPrintTable()
{
    var tab_text="<table border='2px'><tr bgcolor='#87AFC6'>";
    var textRange; var j=0;
    tab = document.getElementById("tableprint"); // id of table

    for(j = 0 ; j < tab.rows.length ; j++) 
    {     
        tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
        //tab_text=tab_text+"</tr>";
    }

    tab_text=tab_text+"</table>";
    tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
    tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
    tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE "); 

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
    {
        txtArea1.document.open("txt/html","replace");
        txtArea1.document.write(tab_text);
        txtArea1.document.close();
        txtArea1.focus(); 
        sa=txtArea1.document.execCommand("SaveAs",true,"Say Thanks to Sumit.xls");
    }  
    else                 //other browser not tested on IE 11
        sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));  

    return (sa);
}


function xxPrintTable(){
    var downloadLink;
    var dataType = 'application/vnd.ms-excel';
    var tableSelect = document.getElementById("tableprint");
    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
    
    // Specify file name
    var filename = 'excel_data.xls';
    
    // Create download link element
    downloadLink = document.createElement("a");
    
    document.body.appendChild(downloadLink);
    
    if(navigator.msSaveOrOpenBlob){
        var blob = new Blob(['\ufeff', tableHTML], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob( blob, filename);
    }else{
        // Create a link to the file
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
    
        // Setting the file name
        downloadLink.download = filename;
        
        //triggering the function
        downloadLink.click();
    }
}
</script>
<style id="table_style" type="text/css">
	.printtable
	{
	border: 1px solid #ccc;
	border-collapse: collapse;
	}
	.printtable th, .printtable td
	{
	padding: 5px;
	border: 1px solid #ccc;
	}
	.printtable tr:last-child {
	font-weight: bold;
	}
	.printtable td:nth-last-child(1),
	td:nth-last-child(2) {
	text-align: right;
	}
</style>

<script type="text/javascript">
var tableToExcel = (function() {
  var uri = 'data:application/vnd.ms-excel;base64,'
    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
  return function(table, name) {
	var divContents = document.getElementById("dvContents").innerHTML;
    var ctx = {worksheet: name || 'Worksheet', table: divContents}
    //window.location.href = uri + base64(format(template, ctx))
    document.getElementById("exporttable").href = uri + base64(format(template, ctx));
    document.getElementById("exporttable").download = 'POSSalesReport.xls';
    document.getElementById("exporttable").click();
  }
})()
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>