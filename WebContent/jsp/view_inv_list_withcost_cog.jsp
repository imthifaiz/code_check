<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%
String title = "Inventory Summary With Average Cost";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport(){
	 if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
 	}
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invAvgCost";
	document.form1.submit();
	
} 
</script>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String plant = (String) session.getAttribute("PLANT");
String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry

String fieldDesc="";
String USERID="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_BRAND_ID="",PRD_DESCRIP="", QTY ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String PRD_CLS_ID="",CURRENCYID="",CURRENCYDISPLAY="",PRD_DEPT_ID="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",LOCALEXPENSES="";
boolean flag=false;
String uom = StrUtils.fString(request.getParameter("UOM"));
String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = StrUtils.fString(request.getParameter("LOC"));
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
BATCH   = StrUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
//Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
//End code added by Deen for product brand on 11/9/12 
FROM_DATE    = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE    = StrUtils.fString(request.getParameter("TO_DATE"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
CURRENCYID= StrUtils.fString(request.getParameter("CURRENCYID"));
CURRENCYDISPLAY= StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
LOCALEXPENSES = StrUtils.fString(request.getParameter("LOCALEXPENSES"));
String collectionDate=DateUtils.getDate();
String basecurrency=_PlantMstDAO.getBaseCurrency(PLANT);
if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase(""))CURRENCYDISPLAY=basecurrency;
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
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
boolean cntRec=false;

String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
/* String LOCALEXPENSESCHK = "checked";
if(LOCALEXPENSES.equals(""))
{
	LOCALEXPENSES="1";	
} */

String LOCALEXPENSESCHK = "";
if(LOCALEXPENSES.equals(""))
{
	LOCALEXPENSES="0";	
}

if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
	LOCALEXPENSESCHK="";
	LOCALEXPENSES="0";	
}

String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Summary with Average Cost</label></li>                                   
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
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_list_withcost.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
    
    <span style="text-align: center;"><small><%=fieldDesc%></small></span>
    
  <%-- <center>
  <h1><small> <%=fieldDesc%></small></h1>
  </center> --%>
  
  <div id="target" style="display:none;">
  		<div class="form-group">    	 
  		<label class="control-label col-sm-2" for="Currency ID">Search</label>
 		<div class="form-inline">
       <!--  <label class="control-label col-sm-1" for="From Date"> </label> -->
        <div class="col-sm-3 ac-box">
        <div class="input-group">          
       <INPUT class="form-control datepicker" name="FROM_DATE" id="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" placeholder="FROM DATE" size="20"  MAXLENGTH=20 READONLY>
       </div>
    	</div>
    	</div>
    	<div class="form-inline">
    	<!--  <label class="control-label col-sm-1" for="To Date"> </label> -->
        <div class="col-sm-3 ac-box">
        <div class="input-group" >          
       <INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" id="TO_DATE" placeholder="TO DATE" size="20"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
        </div>
    	</div>
 		</div>
     <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="ITEM" ID="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
    		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
 		</div>

 		<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID"> </label>
       <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		     <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
 	         <div class="col-sm-3 ac-box">
    	<INPUT class="form-control" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" size="30"  MAXLENGTH=20>
    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT SUB CATEGORY Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div>
 		</div>  
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT CATEGORY ID"> </label>
 <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div>  
  		
  		<!-- <label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY">PRODUCT SUB CATEGORY ID:</label> -->
     <div class="col-sm-3 ac-box">
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
       <INPUT class="form-control" name="CURRENCYDISPLAY" ID="CURRENCYDISPLAY" type = "TEXT"  value="<%=CURRENCYDISPLAY%>" placeholder="CURRENCY" size="30"  MAXLENGTH=20>
       <span class="select-icon"onclick="$(this).parent().find('input[name=\'CURRENCYDISPLAY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/currencylist.jsp?CURRENCYDISPLAY='+form1.CURRENCYDISPLAY.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
        <!-- <span class="input-group-addon"  onClick="javascript:popUpWin('currencylist.jsp?CURRENCYDISPLAY='+form1.CURRENCYDISPLAY.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    	</div>
 		
 		
<!--   		<label class="control-label col-sm-2" for="Product Brand">Product Brand ID:</label> -->
<% if(COMP_INDUSTRY.equals("Retail")) { %> 
      <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="LOC" ID="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION/BRANCH" size="30"  MAXLENGTH=20>
    		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		 <%}else{%>
      <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="LOC" ID="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
    		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<%}%>
 		
 		 <input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">
 		</div>  
         
         <div class="form-group">
       <label class="control-label col-sm-2" for="Location ID"> </label>
      <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" placeholder="LOCATION TYPE ONE" size="30" MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div>
 		
 		   <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>" placeholder="LOCATION TYPE TWO" size="30" MAXLENGTH=20>
    		<span class="select-icon"onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div>
 		
 		

        <div class="col-sm-3 ac-box">
    		<INPUT class="form-control" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" placeholder="LOCATION TYPE THREE" size="30" MAXLENGTH=20>
    		<span class="select-icon"onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	   <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
       
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div>
 		</div>
 		
 		<div class="form-group">
       <label class="control-label col-sm-2" for="Location ID"> </label>
       
        <div class="col-sm-3 ac-box" hidden>
        <div class="input-group">
 		 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" placeholder="SELECT UOM" name="UOM" id="UOM" style="width: 100%">
			<option value=""> SELECT UOM </option>	 
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
       <div class="col-sm-3 ac-box">
        <INPUT class="form-control" name="BATCH" id="BATCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BATCH)%>" placeholder="BATCH" style="width: 100%"  MAXLENGTH=40>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BATCH\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--         <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/batch_list_inventory.jsp?BATCH='+form1.BATCH.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
 		</div>
 		
 		<div class="col-sm-4">   
  	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;

  	</div>
  	
 		</div>
 		
	 
  	
 		
 		
 		
 		 <div class="form-group">
 	    <label class="control-label col-sm-2" for="Model"> </label> 
         <div class="col-sm-3 ac-box">
        <INPUT class="form-control" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description" style="width: 100%"  MAXLENGTH=100> 
    	</div> 
	 
	 </div>	
	 
	 
  	<input type="hidden" name="LOC_DESC" value="">
  	<INPUT name="CURRENCYID" type="hidden" value="<%=CURRENCYID%>" size="1"  MAXLENGTH=80>
   		</div> 
   		
   	  <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 
	<div class="form-group">
      <div class="col-sm-3">
       	  <input type="checkbox" class="form-check-input" name="LOCALEXPENSES" id="withlocalexpenses" value="<%=StrUtils.forHTMLTag(LOCALEXPENSES)%>" Onclick="checkval()" <%=LOCALEXPENSESCHK%> />&nbsp;<strong>View By Landed Cost</strong>&nbsp;	
	</div>
	</div>
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
									<!-- role="grid" aria-describedby="tableInventorySummary_info"> -->
					<thead>
		                <tr role="row">
		                <th style="font-size: smaller;">PRODUCT</th>  
		                <th style="font-size: smaller;">LOC</th>  
		                <th style="font-size: smaller;">CATEGORY</th>  
		                <th style="font-size: smaller;">SUB CATEGORY</th>  
		                <th style="font-size: smaller;">BRAND</th>  
		                <th style="font-size: smaller;">DESCRIPTION</th>
		                 <th style="font-size: smaller;">BATCH</th>  
		                <th style="font-size: smaller;">PCS/EA/PKT/KG/LT UOM</th>  
		                <th style="font-size: smaller;">PCS/EA/PKT/KG/LT QTY</th>  
		                <th style="font-size: smaller;">AVG.UNITCOST PER PC</th> 
		                <th style="font-size: smaller;">INV.UOM</th>  
		                <th style="font-size: smaller;">INV.QTY</th>
		                
						<!-- <th style="font-size: smaller;">BILLED QTY</th>
						<th style="font-size: smaller;">UNBILLED QTY</th>  --> 
						
		                 
		                <th style="font-size: smaller;">AVG INV.UNITCOST</th>  
		                
		                 
		                <!-- <th style="font-size: smaller;">LIST PRICE</th>   -->
		                
		                <!-- <th style="font-size: smaller;">QTY</th> -->
		                <th style="font-size: smaller;">TOT INV.COST</th>  
		                
		               <!--  <th style="font-size: smaller;">TOTAL PRICE</th> -->		                
		                	<!-- <th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Id</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Loc</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT CATEGORY ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT SUB CATEGORY ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Product Brand ID</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA Quantity</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory Quantity</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Billed Qty</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Unbilled Qty</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Batch</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Avg Unit Cost</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Avg Unit Cost Per PC</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">List Price</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Qty</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Total Cost</th>
		                	<th  style="background-color: #eaeafa; color:#333; text-align: center;">Total Price</th> -->
		                </tr>
		            </thead>
		                     <tbody>
				 
				</tbody>
		                   <!-- IMTIZIAF -->
        <tfoot style="display: none;">
							<tr class="group">
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
		<th></th>
		<th></th>
		<th style="text-align: right !important">Grand Total</th>
		<th style="text-align: right !important"></th>																																										
		</tr>
	</tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>
 
  </FORM>
  
  <script>
  var tableInventorySummary;
  var productId, desc, loc, prdBrand, prdClass,prdDept, prdType, fromDt,toDt, batch, currencyId, loctype,loctype2,loctype3,currencyDisplay, groupRowColSpan = 12,uom,LOCALEXPENSES;
function getParameters(){
	return {
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
		"BATCH":batch,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDept,
		"PRD_TYPE_ID":prdType,
		"PRD_BRAND_ID":prdBrand,
		"CURRENCYID":currencyId,
		"CURRENCYDISPLAY":currencyDisplay,
		"FROM_DATE":fromDt,
		"TO_DATE":toDt,
		"LOC_TYPE_ID":loctype,
		"LOC_TYPE_ID2":loctype2,
		"LOC_TYPE_ID3":loctype3,
		"UOM":uom,
		"LOCALEXPENSES":LOCALEXPENSES,
		"ACTION": "VIEW_INV_SUMMARY_AVG_COST_COG",
		"PLANT":"<%=PLANT%>"
	}
}  

function storeUserPreferences(){
	storeInLocalStorage('view_inv_list_withcost_cog_FROM_DATE', $('#FROM_DATE').val());
	storeInLocalStorage('view_inv_list_withcost_cog_TO_DATE', $('#TO_DATE').val());
	storeInLocalStorage('view_inv_list_withcost_cog_ITEM', $('#ITEM').val());
	storeInLocalStorage('view_inv_list_withcost_cog_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('view_inv_list_withcost_cog_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('view_inv_list_withcost_cog_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withcost_cog_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('view_inv_list_withcost_cog_CURRENCYDISPLAY', $('#CURRENCYDISPLAY').val());
	storeInLocalStorage('view_inv_list_withcost_cog_LOC', $('#LOC').val());
	storeInLocalStorage('view_inv_list_withcost_cog_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withcost_cog_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('view_inv_list_withcost_cog_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('view_inv_list_withcost_cog_BATCH', $('#BATCH').val());
	storeInLocalStorage('view_inv_list_withcost_cog_UOM', $('#UOM').val());
}

function onGo(){
     productId = document.form1.ITEM.value;
     desc = document.form1.PRD_DESCRIP.value;
     loc = document.form1.LOC.value;
     batch = document.form1.BATCH.value;
     prdClass = document.form1.PRD_CLS_ID.value;
     prdDept = document.form1.PRD_DEPT_ID.value;
     prdType = document.form1.PRD_TYPE_ID.value;
   // Start code modified by Deen for product brand on 11/9/12  
     prdBrand = document.form1.PRD_BRAND_ID.value;
     currencyId = document.form1.CURRENCYID.value;
     currencyDisplay = document.form1.CURRENCYDISPLAY.value;
     fromDt = document.form1.FROM_DATE.value;
     toDt = document.form1.TO_DATE.value;
     loctype = document.form1.LOC_TYPE_ID.value;
     loctype2 = document.form1.LOC_TYPE_ID2.value;
     loctype3= document.form1.LOC_TYPE_ID3.value;
     uom = document.form1.UOM.value;
     if (uom=="Select UOM")
    	 uom="";
     LOCALEXPENSES	= document.form1.LOCALEXPENSES.value; 

     if(document.form1.CURRENCYDISPLAY.value.length < 1)
	 {
		alert("Please Select CurrencyID!");
		document.form1.CURRENCYDISPLAY.focus();
		return false;
   	}
    if(LOCALEXPENSES != null     && LOCALEXPENSES != "") { flag = true;}
    
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    	storeUserPreferences();
    }
 var urlStr = "../InvMstServlet";
	// Call the method of JQuery Ajax provided
	var groupColumn = 0;
	var totalQty = 0;
	var totalCost = 0;
	var totalPrice = 0;
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
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			
		        			<%-- data.items[dataIndex]['TotalCost'] = parseFloat(data.items[dataIndex]['TotalCost']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		        			data.items[dataIndex]['TotalPrice'] = parseFloat(data.items[dataIndex]['TotalPrice']).toFixedSpecial(<%=DbBean.NOOFDECIMALPTSFORCURRENCY%>);--%>
		        			
		        			data.items[dataIndex]['AVERAGE_COST'] = addZeroes(parseFloat(data.items[dataIndex]['AVERAGE_COST'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			data.items[dataIndex]['COST_PER_PC'] = addZeroes(parseFloat(data.items[dataIndex]['COST_PER_PC'].replace(',','')).toFixed(<%=numberOfDecimal%>));
		        			<%-- data.items[dataIndex]['LIST_PRICE'] = addZeroes(parseFloat(data.items[dataIndex]['LIST_PRICE']).toFixed(<%=numberOfDecimal%>)); --%>
		        			data.items[dataIndex]['TotalCost'] = addZeroes(parseFloat(data.items[dataIndex]['TotalCost']).toFixed(<%=numberOfDecimal%>));
		        			<%-- data.items[dataIndex]['TotalPrice'] = addZeroes(parseFloat(data.items[dataIndex]['TotalPrice']).toFixed(<%=numberOfDecimal%>)); --%>
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
			{"data": 'ITEM', "orderable": true},
 			{"data": 'LOC', "orderable": true},
 			{"data": 'PRDCLSID', "orderable": true},
 			{"data": 'ITEMTYPE', "orderable": true},
 			{"data": 'PRD_BRAND_ID', "orderable": true},
 			{"data": 'ITEMDESC', "orderable": true},
 			{"data": 'BATCH', "orderable": true},
 			{"data": 'STKUOM', "orderable": true},
 			{"data": 'STKQTY', "orderable": true},
 			{"data": 'COST_PER_PC', "orderable": true},
			{"data": 'INVENTORYUOM', "orderable": true},
			 {"data": 'INVUOMQTY', "orderable": true}, 
			/* {"data": 'BILLEDQTY', "orderable": true},
			{"data": 'UNBILLEDQTY', "orderable": true}, */
 			{"data": 'AVERAGE_COST', "orderable": true},
 			
 			/* {"data": 'LIST_PRICE', "orderable": true}, */
 			/* {"data": 'QTY', "orderable": true}, */
 			{"data": 'TotalCost', "orderable": true}
 			/* {"data": 'TotalPrice', "orderable": true} */
 			],
			 "columnDefs": [{"className": "t-right", "targets": [8,9,11, 12, 13]}], 
			//"orderFixed": [ groupColumn, 'asc' ],
			"orderFixed": [ ], 
			/* "dom": 'lBfrtip', */
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
//                     columns: ':not(:eq('+groupColumn+')):not(:last)'
                    columns: ':not(:eq('+groupColumn+')):not(:eq(12)):not(:last)'
                }
	        ],
	        "order":[],
	        "footerCallback": function ( row, data, start, end, display ) {
	            var api = this.api(), data;
	            var intVal = function ( i ) {
	                return typeof i === 'string' ?
	                    i.replace(/[\$,]/g, '')*1 :
	                    typeof i === 'number' ?
	                        i : 0;
	            };
	         // Total over all pages
	            /* totalqty = api
	                .column(13)
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 ); */

	                var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            var totalQty = 0;
		            var totalCost = 0;
		            var totalPrice = 0;
		            var groupTotalQty = 0;
		            var groupTotalCost = 0;
		            var groupTotalPrice = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	
					var groupTotalQtyVal=null,groupTotalCostVal=null,groupTotalPriceVal=null;
		                	
		                	if(groupTotalQty==null || groupTotalQty==0){
		                		groupTotalQtyVal="0.000";
		                	}else{
		                		groupTotalQtyVal=addZeroes(parseFloat(groupTotalQty).toFixed(5).replace(/\.?0+$/, ''));
		                	}if(groupTotalCost==null || groupTotalCost==0){
		                		groupTotalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
		                	}else{
		                		/*groupTotalCostVal=parseFloat(groupTotalCost).toFixed(5).replace(/\.?0+$/, '');*/
		                		groupTotalCostVal=addZeroes(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
		                	}if(groupTotalPrice==null || groupTotalPrice==0){
		                		groupTotalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
		                	}else{
		                		/*groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(5).replace(/\.?0+$/, '');*/
		                		groupTotalPriceVal=addZeroes(parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>));
		                	}
		                
		                    last = group;
		                    groupEnd = i;
		                    groupTotalQty = 0;
		                    groupTotalCost = 0;
		                    groupTotalPrice = 0;
		                }
		               /*  groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
		                totalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '')); */
		                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
		                totalCost += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
		               /*  groupTotalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', '')); */
		                currentRow = i;
		            } );
	                
	         
	            totalcost = api
                .column(13)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
	            
	           /*  totalprice = api
                .column(17)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 ); */
	         // Total over this column 9
	            /* colTotal_9 = api
	                .column( 10, { page: 'current'} )
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 ); */
	        // alert(totalqty);
	        // alert(totalcost);
	        // alert(totalprice);
	            /* $( api.column( 12 ).footer() ).html('Total');	            
	            $( api.column( 13 ).footer() ).html(addZeroes(parseFloat(totalqty).toFixed(3)));
	            $( api.column( 14 ).footer() ).html(addZeroes(parseFloat(totalcost).toFixed(2)));
	            $( api.column( 15 ).footer() ).html(addZeroes(parseFloat(totalprice).toFixed(2))); */
// 	            $( api.column( 12 ).footer() ).html('Grand Total');	            
	            /* $( api.column( 10 ).footer() ).html(addZeroes(parseFloat(totalqty).toFixed(3))); */
	            $( api.column( 13).footer() ).html(addZeroes(parseFloat(totalcost).toFixed(<%=numberOfDecimal%>)));
	            /* $( api.column( 15 ).footer() ).html(addZeroes(parseFloat(totalprice).toFixed(2))); */
	        },
			"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var totalCost = 0;
	            var totalPrice = 0;
	            var groupTotalQty = 0;
	            var groupTotalCost = 0;
	            var groupTotalPrice = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
var groupTotalQtyVal=null,groupTotalCostVal=null,groupTotalPriceVal=null;
	                	
	                	if(groupTotalQty==null || groupTotalQty==0){
	                		groupTotalQtyVal="0.000";
	                	}else{
	                		groupTotalQtyVal=addZeroes(parseFloat(groupTotalQty).toFixed(5).replace(/\.?0+$/, ''));
	                	}if(groupTotalCost==null || groupTotalCost==0){
	                		groupTotalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
	                	}else{
	                		/*groupTotalCostVal=parseFloat(groupTotalCost).toFixed(5).replace(/\.?0+$/, '');*/
	                		groupTotalCostVal=addZeroes(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
	                	}if(groupTotalPrice==null || groupTotalPrice==0){
	                		groupTotalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
	                	}else{
	                		/*groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(5).replace(/\.?0+$/, '');*/
	                		groupTotalPriceVal=addZeroes(parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>));
	                	}
	                	if (i > 0) {
	                		/* $(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQtyVal + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + groupTotalPriceVal + '</td></tr>'
			                    ); */
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td class="t-right">Total</td><td class="t-right">' + groupTotalCostVal + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                    groupTotalCost = 0;
	                    groupTotalPrice = 0;
	                }
	               /*  groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '')); */
	                groupTotalCost += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
	                totalCost += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', ''));
	               /*  groupTotalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', ''));
	                totalPrice += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', '')); */
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	
	            	var totalQtyVal=null,groupTotalQtyVal=null,totalCostVal=null,groupTotalCostVal=null,totalPriceVal=null,groupTotalPriceVal=null;
	            	if(totalQty==null || totalQty==0){
	            		totalQtyVal="0.000";
                	}else{
                		totalQtyVal=parseFloat(totalQty).toFixed(3);
                	}if(groupTotalQty==null || groupTotalQty==0){
                		groupTotalQtyVal="0.000";
                	}else{
                		groupTotalQtyVal=parseFloat(groupTotalQty).toFixed(3);
                	}if(totalCost==null || totalCost==0){
                		totalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*totalCostVal=parseFloat(totalCost).toFixed(5).replace(/\.?0+$/, '');*/
                		totalCostVal=addZeroes(parseFloat(totalCost).toFixed(<%=numberOfDecimal%>));
                	}if(groupTotalCost==null || groupTotalCost==0){
                		groupTotalCostVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*groupTotalCostVal=parseFloat(groupTotalCost).toFixed(5).replace(/\.?0+$/, '');*/
                		groupTotalCostVal=addZeroes(parseFloat(groupTotalCost).toFixed(<%=numberOfDecimal%>));
                	}if(totalPrice==null || totalPrice==0){
                		totalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*totalPriceVal=parseFloat(totalPrice).toFixed(5).replace(/\.?0+$/, '');*/
                		totalPriceVal=addZeroes(parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>));
                	}if(groupTotalPrice==null || groupTotalPrice==0){
                		groupTotalPriceVal=addZeroes(parseFloat("0.00000").toFixed(<%=numberOfDecimal%>));
                	}else{
                		/*groupTotalPriceVal=parseFloat(groupTotalPrice).toFixed(5).replace(/\.?0+$/, '');*/
                		groupTotalPriceVal=addZeroes(parseFloat(groupTotalPrice).toFixed(<%=numberOfDecimal%>));
                	}
	            	
	            	/* $(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalQtyVal + '</td><td class="t-right">' + totalCostVal + '</td><td class="t-right">' + totalPriceVal+ '</td></tr>'
                 );
             	$(rows).eq( currentRow ).after(
             			'<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQtyVal + '</td><td class="t-right">' + groupTotalCostVal + '</td><td class="t-right">' + groupTotalPriceVal + '</td></tr>'
                 ); */
               /*   $(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalQtyVal + '</td><td class="t-right">' + totalCostVal + '</td></tr>'
                 );
             	$(rows).eq( currentRow ).after(
             			'<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + groupTotalQtyVal + '</td><td class="t-right">' + groupTotalCostVal + '</td></tr>'
                 );  */

                	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + totalCostVal + '</td></tr>'
                 );
             	$(rows).eq( currentRow ).after(
             			'<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td class="t-right">Total</td><td class="t-right">' + groupTotalCostVal + '</td></tr>'
                 );
					
             	
             }
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
            '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT UOM</TH>'+
            '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>Average Unit Cost</TH>'+
         '  <TH><font color="#ffffff" align="left"><b>List Price</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Qty</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Cost</TH>'+
       ' <TH><font color="#ffffff" align="left"><b>Total Price</TH>'+
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
	 if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_FROM_DATE','', 'FROM_DATE');
		 }
	 if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_TO_DATE','', 'TO_DATE');
		 }
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }	
	   if (document.form1.CURRENCYDISPLAY.value == ''){
			getLocalStorageValue('view_inv_list_withcost_cog_CURRENCYDISPLAY','','CURRENCYDISPLAY');
		}
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('view_inv_list_withcost_cog_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('view_inv_list_withcost_cog_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('view_inv_list_withcost_cog_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.BATCH.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_BATCH','','BATCH');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('view_inv_list_withcost_cog_UOM','','UOM');
		}
   }
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
				// : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
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

	/* To get the suggestion data for Currency */
	$("#CURRENCYDISPLAY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CURRENCY_DATA",
					CURRENCY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CURRENCYMST);
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
			return '<div><p onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
 
 function checkval()
 {
	 var checkBox = document.getElementById("withlocalexpenses");
	 if (checkBox.checked == true){		 
		 document.form1.LOCALEXPENSES.value="1";		 
	 }
	 else
		 {		 
		 document.form1.LOCALEXPENSES.value="0";		 
		 }
	 
 }
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>