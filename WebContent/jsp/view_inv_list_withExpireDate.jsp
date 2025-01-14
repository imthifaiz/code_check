<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<!-- IMTIZIAF -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%
String title = "Inventory Summary With Expiry Date";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
 // document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invExpiry";
	document.form1.submit();
	
} 

</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%@ include file="header.jsp"%>
<!-- <title>Inventory Summary with expire date</title>
</head>
<link rel="stylesheet" href="css/style.css"> -->
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
String plant = (String) session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industr


String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",EXPIREDATE="",PRD_DEPT_ID="", BATCH="",PRD_TYPE_ID="",PRD_BRAND_ID="",PRD_CLS_ID="",PRD_DESCRIP="", QTY ="",
               LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",VIEWSTATUS="";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = StrUtils.fString(request.getParameter("LOC"));
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
BATCH   = StrUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
String uom = StrUtils.fString(request.getParameter("UOM"));
//IMTIZIAF
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String ADD1 = (String) map.get("ADD1");
String ADD2 = (String) map.get("ADD2");
String ADD3 = (String) map.get("ADD3");
String ADD4 = (String) map.get("ADD4");
String STATE = (String) map.get("STATE");
String COUNTRY = (String) map.get("COUNTY");
String ZIP = (String) map.get("ZIP");
String TELNO = (String) map.get("TELNO");

String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
String CNAME = (String) map.get("PLNTDESC");
//END

boolean cntRec=false;
if(VIEWSTATUS.equals(""))
{
	VIEWSTATUS="ByExpiryDate";
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Summary With Expiry Date</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
                 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right"> 
              
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div>
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_list_withExpireDate.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
 <!-- end --> 
  <span style="text-align: center;"><small><%=fieldDesc%></small></span>
  <!-- 
  span class="select-icon" style="right:45px;"><i class="glyphicon glyphicon-menu-down"></i></span>
			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> 
    		
    		 -->
  
  <div id="target" style="display:none;">
  		<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-3 ac-box">
             <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="form-control" name="ITEM" ID="ITEM" type = "TEXT" value="<%=ITEM%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
    		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=PRD_DEPT_ID%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    	<span class="select-icon"onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
 	       <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    	<span class="select-icon"onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
 		</div> 

		<div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT CATEGORY ID"> </label>
            <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" placeholder="PRODUCT SUB CATEGORY" size="30"  MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT SUB CATEGORY Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
  		
<!--   		<label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY">PRODUCT SUB CATEGORY ID:</label> -->
      <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
 		
<!--   		<label class="control-label col-sm-2" for="Product Brand">Product Brand ID:</label> -->
      <div class="col-sm-3 ac-box">
        <div class="input-group">          
        <INPUT class="form-control datepicker"  name="EXPIREDATE" id="EXPIREDATE" type="TEXT" readonly="readonly" value="<%=EXPIREDATE%>" placeholder="EXPIRY DATE" size="20" MAXLENGTH=80 
        onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}">
        </div>
       	</div>
 		
 		 <input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">  
 		</div>
        
          <!-- End code added by Deen for product brand on 11/9/12  -->
	
		<div class="form-group">
        <label class="control-label col-sm-2" for="Expiry Date"> </label>
        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
         <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="LOC" ID="LOC" type = "TEXT" value="<%=LOC%>" placeholder="LOCATION/BRANCH" size="30"  MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
        </div>
         <%}else{%>
         <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="LOC" ID="LOC" type = "TEXT" value="<%=LOC%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon"   onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
    	<%}%>
    	
<!--   		<label class="control-label col-sm-2" for="Location">Location ID/Desc:</label> -->
         <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>"	placeholder="LOCATION TYPE ONE" size="30" MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
        
               
         	<!-- <div class="form-inline">
   		<label class="control-label col-sm-2" for="Batch">Batch:</label> -->
        <div class="col-sm-3 ac-box">
        <INPUT class="form-control" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE TWO" size="30" MAXLENGTH=20>
       <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
    		
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
       	</div>
 		<input type="hidden" name="LOC_DESC" value="">
 		</div>
	
		<div class="form-group">
        <label class="control-label col-sm-2" for="Location Type"> </label>
 <div class="col-sm-3 ac-box">    
        <INPUT class="form-control" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=LOC_TYPE_ID3%>" placeholder="LOCATION TYPE THREE" size="30" MAXLENGTH=20>
       <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div> 
 		 
		<div class="col-sm-3 ac-box">    
       <INPUT class="form-control" name="BATCH" id="BATCH" type = "TEXT" value="<%=BATCH%>" placeholder="BATCH" style="width: 100%"  MAXLENGTH=40>
       <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BATCH\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--        <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/batch_list_inventory.jsp?BATCH='+form1.BATCH.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
 		</div> 
 		
 		<div class="form-inline">
		<!-- <label class="control-label col-sm-1" for="Model"> </label> -->
        <div class="col-sm-3 ac-box" hidden>
 		 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" id="UOM" style="width: 100%">
			<option value=""> SELECT UOM  </option>	  
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);					
					for(int i=0 ; i < ccList.size();i++)		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
						
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 </div>
	 </div>
	 <div class="form-group">
	 	<label class="control-label col-sm-2" for="Product ID"> </label>
  		<div class="col-sm-4">
  		<label class=radio-inline>
  		<INPUT name="VIEWSTATUS" type = "radio"  value="ByExpiryDate"  id="ByExpiryDate" <%if(VIEWSTATUS.equalsIgnoreCase("ByExpiryDate")) {%>checked <%}%>><b>By Expiry Date</b>
  		</label>
  		<label class=radio-inline>
  		<INPUT  name="VIEWSTATUS" type = "radio" value="ViewAll"  id = "ViewAll" <%if(VIEWSTATUS.equalsIgnoreCase("ViewAll")) {%>checked <%}%>><b>View All</b>
  		</label>
  		</div>
  		       		
<!--   		<label class="control-label col-sm-2" for="Description">Description:</label> -->
        <div class="col-sm-3 ac-box">      
       <INPUT class="form-control" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description" style="width: 100%"  MAXLENGTH=100> 
    	</div>
  		
  	
  		<div class="col-sm-4">
  		<button type="button" class="btn btn-success"  onClick="javascript:return onGo();">Search</button>&nbsp;
<!--         <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--         <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
        <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
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
<!--         <button type="button" class="Submit btn btn-default"  onClick="javascript:return onGo();"><b>View</b></button>&nbsp; -->
<!--         <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--         <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
        <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 	
  				
		<!-- 	<br> -->
 <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              
              <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
            </div>
              	<table id="tableInventorySummary"
									 class="table table-bordred table-striped" > 
<!-- 									role="grid" aria-describedby="tableInventorySummary_info"> -->
					<thead>
		                <tr role="row">
		                   <th style="font-size: smaller;">PRODUCT</th>
		                   <th style="font-size: smaller;">LOC</th>
		                   <th style="font-size: smaller;">LOC DESC</th>
		                   <th style="font-size: smaller;">LOC TYPE ONE</th>
		                   <th style="font-size: smaller;">CATEGORY</th>
		                   <th style="font-size: smaller;">SUB CATEGORY</th>
		                   <th style="font-size: smaller;">BRAND</th>
		                   <th style="font-size: smaller;">DESCRIPTION</th>
		                   <th style="font-size: smaller;">BATCH</th>
		                   <th style="font-size: smaller;">EXP.DATE</th>
		                   <th style="font-size: smaller;">PCS/EA/PKT/KG/LT UOM</th>
		                   <th style="font-size: smaller;">PCS/EA/PKT/KG/LT QTY</th>
		                   <th style="font-size: smaller;">INV.UOM</th>
		                   <th style="font-size: smaller;">INV.QTY</th>
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Loc</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT CATEGORY ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT SUB CATEGORY ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product Brand ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>		                	 -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Batch</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Expiry Date</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA Quantity</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory UOM</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory Quantity</th> -->
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
		              <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th style="text-align: left !important">Grand Total</th>
								<th style="text-align: right !important"></th>			
								<th></th>	
								<th style="text-align: right !important"></th>
																												
							</tr>
						</tfoot>
						<!-- END -->
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
</FORM>
  
  <script>
  var tableInventorySummary;
  var productId, desc, loc, batch, prdDept,prdBrand, prdClass, prdType, expDate, loctype, loctype2,loctype3,viewstatus,uom, groupRowColSpan = 8;
function getParameters(){
	return { 
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
		"BATCH":batch,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDept,
		"PRD_BRAND_ID":prdBrand,
		"PRD_TYPE_ID":prdType,
		"EXPIREDATE":expDate,
		"LOC_TYPE_ID":loctype,
		"LOC_TYPE_ID2":loctype2,
		"LOC_TYPE_ID3":loctype3,
		"VIEWSTATUS":viewstatus,
		"UOM": uom,
		"ACTION": "VIEW_INV_SUMMARY_WITH_EXPIRYDT",
		"PLANT":"<%=PLANT%>"
	}
}  

function storeUserPreferences(){
	storeInLocalStorage('view_inv_list_withExpireDate_ITEM', $('#ITEM').val());
	storeInLocalStorage('view_inv_list_withExpireDate_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('view_inv_list_withExpireDate_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('view_inv_list_withExpireDate_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withExpireDate_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('view_inv_list_withExpireDate_EXPIREDATE', $('#EXPIREDATE').val());
	storeInLocalStorage('view_inv_list_withExpireDate_LOC', $('#LOC').val());
	storeInLocalStorage('view_inv_list_withExpireDate_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withExpireDate_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('view_inv_list_withExpireDate_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('view_inv_list_withExpireDate_BATCH', $('#BATCH').val());
	storeInLocalStorage('view_inv_list_withExpireDate_UOM', $('#UOM').val());	
}

function onGo(){
    productId = document.form1.ITEM.value;
    desc = document.form1.PRD_DESCRIP.value;
    loc = document.form1.LOC.value;
    batch = document.form1.BATCH.value;
    prdClass = document.form1.PRD_CLS_ID.value;
    prdDept = document.form1.PRD_DEPT_ID.value;
    prdType = document.form1.PRD_TYPE_ID.value;
    prdBrand = document.form1.PRD_BRAND_ID.value;
    expDate = document.form1.EXPIREDATE.value;
    loctype = document.form1.LOC_TYPE_ID.value;
    loctype2 = document.form1.LOC_TYPE_ID2.value;
    loctype3 = document.form1.LOC_TYPE_ID3.value;
    viewstatus=document.form1.VIEWSTATUS.value;
    uom = document.form1.UOM.value;
    if (uom=="Select UOM")
   	 uom="";
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    storeUserPreferences();
    }
    var urlStr = "../InvMstServlet";
   var groupColumn = 0;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

             searching: true, // Enable searching
		                search: {
		                    regex: true,   // Enable regular expression searching
		                    smart: false   // Disable smart searching for custom matching logic
		                },
             
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'ITEM', "orderable": true},
    			{"data": 'LOC', "orderable": true},
    			{"data": 'LOCDESC', "orderable": true},
    			{"data": 'LOCTYPE', "orderable": true},
    			{"data": 'PRDCLSID', "orderable": true},
    			{"data": 'ITEMTYPE', "orderable": true},
    			{"data": 'PRD_BRAND_ID', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},    			
    			{"data": 'BATCH', "orderable": true},
    			{"data": 'EXPIREDATE', "orderable": true},
    			{"data": 'STKUOM', "orderable": true},
    			{"data": 'QTY', "orderable": true},
    			{"data": 'INVENTORYUOM', "orderable": true},
    			{"data": 'INVQTY', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [9,11]}],
			//"orderFixed": [ groupColumn, 'asc' ],
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
	                    	exportOptions: {
	    	                	columns: [':visible']
	    	                },
	    	                title: '<%=title%>',
	    	                footer: true
	                    },
	                    {
	                    	extend : 'pdf',
                            footer: true,
	                    	text: 'PDF Portrait',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'portrait',
	                    	customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 7;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 10;
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	},
                            pageSize: 'A4'
	                    },
	                    {
	                    	extend : 'pdf',
	                    	footer: true,
	                    	text: 'PDF Landscape',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'landscape',
                    		customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 6;
                     	        doc.styles.tableHeader.fontSize = 6;
                     	        doc.styles.title.fontSize = 8;                     	       
                     	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
	        "order":[],
	        "createdRow": function(row, data, dataIndex){
	        	var parts = data["EXPIREDATE"].split("/");
	        	var dt = new Date(parseInt(parts[2], 10),
	        	                  parseInt(parts[1], 10) - 1,
	        	                  parseInt(parts[0], 10));
	        	if (dt.getTime() < new Date().getTime()){
	        		$(row).css('color', 'red');
	        	}
	        },
			"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var groupTotalQty = 0;
	            var totalPCSQty = 0;
	            var groupTotalPCSQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td></<td><td></<td><td>Total</td><td class="t-right">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalPCSQty = 0;
	                    groupTotalQty = 0;
	                }
	                groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                totalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td></<td><td></<td><td>Grand Total</td><td class="t-right">' + parseFloat(totalPCSQty).toFixed(3) + '</td><td></td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
                    );
                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td></<td><td></<td><td>Total</td><td class="t-right">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
                    );
                }
	        },
	        "footerCallback": function(row, data, start, end, display) {
	            var api = this.api(),
	              data;

	            // Remove the formatting to get integer data for summation
	            var intVal = function(i) {
	              return typeof i === 'string' ?
	                i.replace(/[\$,]/g, '') * 1 :
	                typeof i === 'number' ?
	                i : 0;
	            };

	            // Total over all pages
	            totalQty = api
	              .column(11)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(13)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var groupTotalQty = 0;
	            var totalPCSQty = 0;
	            var groupTotalPCSQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td></<td><td></<td><td>Total</td><td class="t-right">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalPCSQty = 0;
	                    groupTotalQty = 0;
	                }
	                groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                totalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                currentRow = i;
	            } );
	              
	            // Update footer
	           <%--  $(api.column(11).footer()).html(parseFloat(groupTotalPCSQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(13).footer()).html(parseFloat(totalPCSQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>

	            $(api.column(11).footer()).html(parseFloat(groupTotalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(13).footer()).html(parseFloat(totalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	          }
		});


	    $("#tableInventorySummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
             var searchType = $('#searchType').val(); // Get the selected search type
            if (!searchTerm) return true; // If no search term, show all rows

           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
           var searchPattern;

            // Define the search pattern based on the selected search type
            if (searchType === 'first') {
                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
            } else if (searchType === 'middle') {
                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
            } else if (searchType === 'last') {
                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
            }
            // Check each column in the row for a match
            for (var i = 0; i < data.length; i++) {
                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
                if (columnData.match(searchPattern)) {
                    return true; // Match found, include this row in results
                }
            }
            return false; // No match, exclude this row from results
        });

        // Redraw table when the search input changes
        $('.dataTables_filter input').on('keyup', function () {
        	tableInventorySummary.draw();
        });
    }
}

$('#tableInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan = parseInt(groupRowColSpan) - 1;
	}else{
		groupRowColSpan = parseInt(groupRowColSpan) + 1;
	}
	$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
	$('#tableInventorySummary').attr('width', '100%');
});

$('.buttons-columnVisibility').each(function(){
  var $li = $(this),
      $cb = $('<input>', {
              type:'checkbox',
              style:'margin:0 .25em 0 0; vertical-align:middle'}
            ).prop('checked', $(this).hasClass('active') );
  $li.find('a').prepend( $cb );
});
	 
$('.buttons-columnVisibility').on('click', 'input:checkbox,li', function(){
  var $li = $(this).closest('li'),
      $cb = $li.find('input:checkbox');
  $cb.prop('checked', $li.hasClass('active') );
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
                       
	        	outPutdata = outPutdata+item.INVDETAILS
                        	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
         document.getElementById('spinnerImg').innerHTML ='';

     
   }

function getTable(){
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
            '<TR BGCOLOR="#000066">'+
            '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PRODUCT CATEGORY ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PRODUCT SUB CATEGORY ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Description</TH>'+            
            '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Expiry Date</TH>'+
            '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT UOM</TH>'+
            '<TH><font color="#ffffff" align="Right"><b>PCS/EA/PKT/KG/LT Quantity</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Inventory UOM</TH>'+
            '<TH><font color="#ffffff" align="Right"><b>Inventory Quantity</TH>'+
            '</TR>';
                
}
   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
                 <script>
$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }	
	   if (document.form1.EXPIREDATE.value == ''){
			getLocalStorageValue('view_inv_list_withExpireDate_EXPIREDATE','','EXPIREDATE');
		}
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('view_inv_list_withExpireDate_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('view_inv_list_withExpireDate_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('view_inv_list_withExpireDate_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.BATCH.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_BATCH','','BATCH');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('view_inv_list_withExpireDate_UOM','','UOM');
		}
    }
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script> 
 <script>
 $(document).ready(function(){
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
    var plant= '<%=PLANT%>'; 
    /* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
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
				//ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_REPORT",
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
	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
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

	/* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
// 	return '<p class="item-suggestion">'+ data.prd_dep_id +'</p>';
				return '<div onclick="document.form1.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
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
	
	/* PRODUCT SUB CATEGORY Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_TYPE_ID.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
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
	/* PRODUCT SUB CATEGORY Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_BRAND_ID.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
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
	/* location Auto Suggestion */
	$('#LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
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
			//return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
				return '<div onclick="document.form.LOC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
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
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
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
	

	/* Batch Auto Suggestion */
	$('#BATCH').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_BATCH_LIST_FOR_SUGGESTION",
				BATCH : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.batchList);
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
			return '<p>'+data.BATCH+' - '+data.QTY+'</p></div>';
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

 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>