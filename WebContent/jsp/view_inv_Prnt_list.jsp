<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<!-- IMTIZIAF -->
<%@page import="com.track.dao.PlantMstDAO"%>
<%@page import="com.track.dao.ParentChildCmpDetDAO"%>
<!-- END -->
<%
String title = "Inventory Summary With Min/Max/Zero Quantity (by Outlet and Warehouse)";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/JsBarcode.all.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
 // document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invMinQty";
	document.form1.submit();
	
} 

</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%@ include file="header.jsp"%>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
ArrayList invQryListSumTotal  = new ArrayList();
String plant = (String) session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industr

String fieldDesc="";
String USERID="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_BRAND_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",PRD_CLS_ID="",PRD_CLS_ID1="",
        LOC_TYPE_ID="", LOC_TYPE_ID2="",PRD_DEPT_ID="",LOC_TYPE_ID3="",SUPPLIER="",VIEWSTATUS="",TOPUPBY="";
double Total=0;
boolean isWithZero= false;
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
LOC     = StrUtils.fString(request.getParameter("LOC"));
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
String minQty = StrUtils.fString(request.getParameter("MINQTY"));
String withZero = StrUtils.fString(request.getParameter("WITHZERO"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
SUPPLIER = StrUtils.fString(request.getParameter("CUSTOMER"));
VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
TOPUPBY = StrUtils.fString(request.getParameter("TOPUPBY"));
String uom = StrUtils.fString(request.getParameter("UOM"));
String PARENT_PLANTDESC="",PARENT_PLANTDESC1="";
String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENTBYCHILD(plant);
if(PARENT_PLANT==null)
	PARENT_PLANT="PARENT";
else
{
	PARENT_PLANTDESC = new PlantMstDAO().getcmpyname(PARENT_PLANT);
	   if(PARENT_PLANTDESC.length()>15)
		   PARENT_PLANTDESC=PARENT_PLANTDESC.substring(0, 14);

	   List CompanyCompanyByparent = new ParentChildCmpDetDAO().getChildCompanyByparent(PARENT_PLANT);
	   for(int i =0; i<CompanyCompanyByparent.size(); i++) {
	 		Map Comp = (Map)CompanyCompanyByparent.get(i);
	 		String childComp = (String)Comp.get("CHILD_PLANT");
	 		
		if(childComp.equals("C4376460171S2T")){
	 		PARENT_PLANTDESC1 = new PlantMstDAO().getcmpyname(childComp);
	   		if(PARENT_PLANTDESC1.length()>15)
		   		PARENT_PLANTDESC1=PARENT_PLANTDESC1.substring(0, 14);
		}
	   
	   }
}
//IMTIZIAF
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String collectionDate=DateUtils.getDate();
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
//END

if(VIEWSTATUS.equals(""))
{
	VIEWSTATUS="ByAllQty";
}
if(TOPUPBY.equals(""))
{
	TOPUPBY="ByMinQty";
}

if(minQty.length()==0){
    minQty="N";
}else{
    minQty="Y";
}

if(withZero.length()>0){
	isWithZero=true;
}
boolean cntRec=false;


%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Summary With Min/Max/Zero Quantity</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
            
              <!-- <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div> -->
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="view_inv_list.jsp">
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
  
  <%-- <center>
  
    <h1><small><%=fieldDesc%></small></h1>
  
  </center> --%>
  		
 <div id="target" style="display:none;">
		 <div class="form-group">
         <label class="control-label col-sm-2" for="Product ID">Search</label>
         <div class="col-sm-3 ac-box">
      	 <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    	<INPUT class="ac-selected  form-control typeahead" name="ITEM" ID="ITEM" type = "TEXT" value="<%=ITEM%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
    	 <span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/product_list.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('product_list.jsp?ITEM='+form1.ITEM.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  	     <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=PRD_DEPT_ID%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    		 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
<!--  		<div class="form-inline"> -->
<!--        <label class="control-label col-sm-2" for="PRODUCT CATEGORY">PRODUCT CATEGORY ID:</label> -->
         <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    		 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
<!--   		</div> -->
  		</div> 
  		
  		<div class="form-group">
         <label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY ID"> </label>
           <div class="col-sm-3 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" placeholder="PRODUCT SUB CATEGORY" size="30"  MAXLENGTH=20>
     <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT SUB CATEGORY Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		
<!--   		<label class="control-label col-sm-2" for="Product Brand Id">Product Brand ID:</label> -->
      <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--     	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
 		</div> 
<!--  		<div class="form-inline"> -->
<!--        <label class="control-label col-sm-2" for="Location Id"> </label> -->

	<% if(COMP_INDUSTRY.equals("Retail")) { %> 
     <div class="col-sm-3 ac-box">
       <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type = "TEXT" value="<%=LOC%>" placeholder="LOCATION/OUTLET" size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		 <%}else{%>
     <div class="col-sm-3 ac-box">
       <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type = "TEXT" value="<%=LOC%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<%}%>
<!--   		</div> -->
  		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">
 		   <INPUT name="BARCODECOUNT" type = "hidden" value="0">
  		
  		</div>
  		
  		<div class="form-group">
       <label class="control-label col-sm-2" for="Location Type"> </label>
       <div class="col-sm-3 ac-box">
       <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>" placeholder="LOCATION TYPE ONE" size="30" MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		
  		 <div class="col-sm-3 ac-box">
       <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE TWO" size="30" MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>

  		 <div class="col-sm-3 ac-box">
       <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=LOC_TYPE_ID3%>" placeholder="LOCATION TYPE THREE" size="30" MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
	 </div>
	 
	 <div class="form-group">
	  <label class="control-label col-sm-2" > </label>
	   <div class="col-sm-3 ac-box">
       <input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER" value="<%=SUPPLIER%>" size="30" MAXLENGTH=20>
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 			<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
		 
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
	  </div>
	 <div class="form-group">
	  <label class="control-label col-sm-2" > </label>
	  
	   <div class="col-sm-3 ac-box" hidden>
 		 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" placeholder="SELECT UOM" name="UOM" id="UOM" style="width: 100%">
			<option value=""> SELECT UOM </option>	   
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);					
					for(int i=0 ; i < ccList.size();i++){
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
						
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	   		<div class="col-sm-5">
  		<label class=radio-inline>
  		<INPUT name="VIEWSTATUS" type = "radio"  value="ByAllQty"  id="ByAllQty" <%if(VIEWSTATUS.equalsIgnoreCase("ByAllQty")) {%>checked <%}%>><b>By All Qty</b>
  		</label>
  		<label class=radio-inline>
  		<INPUT name="VIEWSTATUS" type = "radio"  value="ByMinQty"  id="ByMinQty" <%if(VIEWSTATUS.equalsIgnoreCase("ByMinQty")) {%>checked <%}%>><b>By Min Qty</b>
  		</label>
  		<label class=radio-inline>
  		<INPUT  name="VIEWSTATUS" type = "radio" value="ByMaxQty"  id = "ByMaxQty" <%if(VIEWSTATUS.equalsIgnoreCase("ByMaxQty")) {%>checked <%}%>><b>By Max Qty</b>
  		</label>
  		<label class=radio-inline>
  		<INPUT  name="VIEWSTATUS" type = "radio" value="ByZeroQty"  id = "ByZeroQty" <%if(VIEWSTATUS.equalsIgnoreCase("ByZeroQty")) {%>checked <%}%>><b>By Zero Qty</b>
  		</label>
  		</div>
  		
  		<input type="hidden" name="LOC_DESC" value="">
<!--   		<label class="control-label col-sm-2" for="Model"> </label> -->
       
	 <div class="form-inline">
  		<div class="col-sm-3">
  		<button type="button" class="btn btn-success"  onClick="javascript:return onGo();">Search</button>&nbsp;
<!--         <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--         <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
        <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  		</div>
  		</div>
  		
  		 <div class="form-inline">
  		
  		  	
<!--   		<label class="control-label col-sm-2" for="Description">Description:</label> -->
        <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description" style="width: 100%"  MAXLENGTH=100>
    	</div>
 		
  		</div>
  		
  		  	</div>
  		  		
  		
  		
  		</div>
  		
  		
  	<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="col-sm-6">
      <label class="control-label col-sm-3" > Topup Qty By </label>
  		<label class=radio-inline>
  		<INPUT name="TOPUPBY" type = "radio"  value="ByMinQty"  <%if(TOPUPBY.equalsIgnoreCase("ByMinQty")) {%>checked <%}%> onClick="javascript:return onGo();"><b>Min Qty</b>
  		</label>
  		<label class=radio-inline>
  		<INPUT  name="TOPUPBY" type = "radio" value="ByMaxQty"  <%if(TOPUPBY.equalsIgnoreCase("ByMaxQty")) {%>checked <%}%> onClick="javascript:return onGo();"><b>Max Qty</b>
  		</label>
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

       
	<!--	Do not remove comment this is not in use
        <TR>
                            
           <TH ALIGN="left" width="5%">&nbsp;&nbsp;</TH>
            <TD width="23%" align=" left">
            <input type="checkbox" name="MINQTY" value="MINQTY" <%if(minQty.equalsIgnoreCase("Y")) {%> checked <%}%>> Show Minimum Qty Stock<BR>
            <input type="checkbox" name="WITHZERO" value="WITHZERO" <%if(isWithZero) {%> checked <%}%>> With Zero<BR>
           </TD>  
		
         
		     
          <TH ALIGN="left" width="10%"></TH>
           <TD width="5"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          <TD width="0%"></TD>
          <TD ALIGN="left" width="29%"></TD>
        </TR>-->
 
 
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
            				<th style="font-size: smaller;">BARCODE</th>
            				<th style="font-size: smaller;">PRODUCT DESC</th>
            				<th style="font-size: smaller;">TOPUP QTY</th>  
            				<th style="font-size: smaller;">LOC</th> 
            				<!-- <th style="font-size: smaller;">LOC DESC</th> 
            				<th style="font-size: smaller;">LOC TYPE ONE</th> --> 
            				<th style="font-size: smaller;">SUPPLIER</th> 
            				<th style="font-size: smaller;">DEPARTMENT</th>  
            				<th style="font-size: smaller;">CATEGORY</th>  
            				<th style="font-size: smaller;">SUB CATEGORY</th>
            				<!-- <th style="font-size: smaller;">PCS/EA/PKT/KG/LT UOM</th>  
            				<th style="font-size: smaller;">PCS/EA/PKT/KG/LT MIN QTY</th>  
            				<th style="font-size: smaller;">PCS/EA/PKT/KG/LT MAX QTY</th>
            				<th style="font-size: smaller;">PCS/EA/PKT/KG/LT QTY</th>  
            				<th style="font-size: smaller;">INV.UOM</th> -->
            				<th style="font-size: smaller;">MIN QTY</th>  
            				<th style="font-size: smaller;">MAX QTY</th>
            				<th style="font-size: smaller;">QTY</th>  
            				<th style="font-size: smaller;"><%=PARENT_PLANTDESC%> INV.QTY</th>  
            				<th style="font-size: smaller;"><%=PARENT_PLANTDESC1%> INV.QTY</th>  
            				<th style="font-size: smaller;">REMARK</th>  
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</TH> -->
<!--        	   <TH style="background-color: #eaeafa; color:#333; text-align: center;">Loc</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT CATEGORY ID</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">PRODUCT SUB CATEGORY ID</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Product Brand</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Description</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right;">PC/PCS/EA Min Quantity</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right">PC/PCS/EA Max Quantity</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right;">PC/PCS/EA Quantity</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: center;">Inventory UOM</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right;">Inventory Min Quantity</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right">Inventory Max Quantity</TH> -->
<!--           <TH style="background-color: #eaeafa; color:#333; text-align: right;">Inventory Quantity</TH> -->
      
     </tr>
       </thead>
         <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th>Grand Total</th>				
								<th></th>
								<th></th>
								<!-- <th></th>
								<th></th> -->
								<th></th>
								<!-- <th></th>
								<th></th>
								<th></th>
								<th></th>-->	
								<th></th> 
<!-- 								<th></th>				 -->
								<th></th>											
								<th></th>											
								<th></th>	
								<th></th>	
								<th></th>	
								<th></th>	
								<th></th>	
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
  var productId, desc, loc, prdBrand,prdDept, prdClass, prdType, loctype,loctype2,loctype3,SUPPLIER,viewstatus,uom,TOPUPBY, groupRowColSpan = 2;
function getParameters(){
	return { 
		 "ITEM": productId,
		 "PRD_DESCRIP":desc,
		 "LOC":loc,
		 "PRD_BRAND_ID":prdBrand,
		 "PRD_DEPT_ID":prdDept,
		 "PRD_CLS_ID":prdClass,
		 "PRD_TYPE_ID":prdType,
		 "LOC_TYPE_ID":loctype,
		 "LOC_TYPE_ID2":loctype2,
		 "LOC_TYPE_ID3":loctype3,
		 "CUSTOMER":SUPPLIER,
		 "VIEWSTATUS":viewstatus,
		 "TOPUPBY":TOPUPBY,
		 "UOM": uom,
		"ACTION": "VIEW_INV_SUMMARY_MIN_QTY",
		"PLANT": "<%=PLANT%>",
	
	}
}  

function storeUserPreferences(){
	storeInLocalStorage('view_inv_list_ITEM', $('#ITEM').val());
	storeInLocalStorage('view_inv_list_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('view_inv_list_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('view_inv_list_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('view_inv_list_LOC', $('#LOC').val());
	storeInLocalStorage('view_inv_list_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('view_inv_list_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('view_inv_list_CUSTOMER', $('#CUSTOMER').val());
	storeInLocalStorage('view_inv_list_UOM', $('#UOM').val());
}

function onGo(){
    productId = document.form1.ITEM.value;
    desc = document.form1.PRD_DESCRIP.value;
    loc = document.form1.LOC.value;
    prdClass = document.form1.PRD_CLS_ID.value;
    prdType = document.form1.PRD_TYPE_ID.value;
    prdDept = document.form1.PRD_DEPT_ID.value;
    prdBrand = document.form1.PRD_BRAND_ID.value;
    loctype = document.form1.LOC_TYPE_ID.value;
    loctype2 = document.form1.LOC_TYPE_ID2.value;
    loctype3 = document.form1.LOC_TYPE_ID3.value;
    SUPPLIER = document.form1.CUSTOMER.value;
    viewstatus=document.form1.VIEWSTATUS.value;
    TOPUPBY=document.form1.TOPUPBY.value;
    uom = document.form1.UOM.value;
    if (uom=="Select UOM")
   	 uom="";
    storeUserPreferences();
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 0;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bPaginate": false,
			lengthMenu: [[-1 ], ["All"]],
			searching: true, // Enable searching
	        search: {
	            regex: true,   // Enable regular expression searching
	            smart: false   // Disable smart searching for custom matching logic
	        },
			//"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"async" : false,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		$('input[name ="BARCODECOUNT"]').val(data.items.length);
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			var REMARK = data.items[dataIndex].REMARK;
		        		 	if(REMARK == null || REMARK == ""){
		        				REMARK = 'Select Remark';
		        			} 
		        			data.items[dataIndex]['REMARK'] = '<input type="text" name="REMARK" placeholder="Enter Remark">';
		        			data.items[dataIndex]['BARCODE'] = '<img style="display: block;" alt="" src="'+data.items[dataIndex].ITEM+'" class="img-fluid" id=barcode'+dataIndex+'><input type="hidden" id="itemprint'+dataIndex+'" name="itemprint" value="'+data.items[dataIndex].ITEM+'">';
		        			data.items[dataIndex]['INVQTY'] = '<a  href="javascript:void(0);" onclick="showMultiCompanyInventory(\'' + data.items[dataIndex].ITEM + '\')">'+data.items[dataIndex].INVQTY+'</a>';
			        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
				{"data": 'ITEM', "orderable": true},
				{"data": 'BARCODE', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'PARENT_TOPUP_QTY', "orderable": true},
    			{"data": 'LOC', "orderable": true},
    			/* {"data": 'LOCDESC', "orderable": true},
    			{"data": 'LOCTYPE', "orderable": true}, */
    			{"data": 'VNAME', "orderable": true},
    			{"data": 'PRD_DEPT_ID', "orderable": true},
       			{"data": 'PRDCLSID', "orderable": true},
    			{"data": 'ITEMTYPE', "orderable": true},
    			/* {"data": 'STKUOM', "orderable": true},
    			{"data": 'MINQTY', "orderable": true},
    			{"data": 'MAXQTY', "orderable": true},
    			{"data": 'QTY', "orderable": true},
    			{"data": 'INVENTORYUOM', "orderable": true}, */
    			{"data": 'INVMINQTY', "orderable": true},
    			{"data": 'INVMAXQTY', "orderable": true},
    			{"data": 'INVQTY', "orderable": true},
    			{"data": 'PARENTINVQTY', "orderable": true},
    			{"data": 'PARENTINVQTY1', "orderable": true},
    			{"data": 'REMARK', "orderable": true}
    			],
			"columnDefs": [{"className": "t-right", "targets": [13]}],
// 		    "columnDefs": [
// 		        { "className": "t-right", "targets": [13] },
// 		        { "targets": [4,5,10,11,12,13,14], "visible": false } 
// 		    ],
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
	                    		format: {
	                    			body: function (data, row, column, node) {
	                    				 if ($(node).find('a').length > 0) {
	                                         return $(node).find('a').text().trim();
	                                     } else {
	                                         return $(node).text().trim();
	                                     }
	 								}
// 	                    			body: function ( data,inner, rowidx, colidx, node ) {
// 	                    		        if ($(node).children("input").length > 0) {
// 	                    		          return $(node).children("input").first().val();
// 	                    		        } else {
// 	                    		          return inner;
// 	                    		        }
// 	                    		      }
	                    			},
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
	                    		format: {
	 								body: function (data, row, column, node) {
	                    				 if ($(node).find('a').length > 0) {
	                                         return $(node).find('a').text().trim();
	                                     } else {
	                                         return $(node).text().trim();
	                                     }
	 								}
// 	                    			body: function ( data,inner, rowidx, colidx, node ) {
// 	                    				if ($(node).children("input").length > 0) {
// 	                    		          return $(node).children("input").first().val();
// 	                    		        } else {
// 	                    		          return inner;
// 	                    		        }
// 	                    		      }
	                    			},
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'portrait',
	                    	customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 10;
                     	       //doc.content[1].table.widths =Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                     	       //doc.content[1].table.widths = "*";
                     	      var arr2 = $('.img-fluid').map(function(){
                                  return this.src;
                             }).get();
             
                 for (var i = 0, c = 1; i < arr2.length; i++, c++) {
                                   doc.content[1].table.body[c][1] = {
                                     image: arr2[i],
                                     width: 50
                                   }
                                     }
	                    	     },
                            pageSize: 'A4'
	                    },
	                    {
	                    	extend : 'pdf',
	                    	footer: true,
	                    	text: 'PDF Landscape',
	                    	exportOptions: {
	                    		format: {
	 								body: function (data, row, column, node) {
	                    				 if ($(node).find('a').length > 0) {
	                                         return $(node).find('a').text().trim();
	                                     } else {
	                                         return $(node).text().trim();
	                                     }
	 								}
//             			body: function ( data,inner, rowidx, colidx, node ) {
//         				if ($(node).children("input").length > 0) {
//         		          return $(node).children("input").first().val();
//         		        } else {
//         		          return inner;
//         		        }
//         		      }
	                    			},
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'landscape',
                    		customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 8;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 8;                     	       
                     	        //doc.content[1].table.widths = "*";
                     	       var arr2 = $('.img-fluid').map(function(){
                                   return this.src;
                              }).get();
              
                  for (var i = 0, c = 1; i < arr2.length; i++, c++) {
                                    doc.content[1].table.body[c][1] = {
                                      image: arr2[i],
                                      width: 50
                                    }
                                      }
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
	        	var parts = data["PARENT_TOPUP_QTY"];
	        	if (parseFloat(parts)<0){
	        		$(row).css('color', 'red');
	        		parts = Math.abs(parseFloat(parts));
	        		$(row).find('td:eq(3)').text(parts); 
	        	}
	        },
			"drawCallback": function ( settings ) {
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var totalinvQty = 0;
	            var totalpinvQty = 0;
	            var totalpinvQty1 = 0;
	            var totalPCSQty = 0;
	            var groupTotalPCSQty = 0;
	            var groupMinPCSQty = 0;
	            var groupTotalQty = 0;
	            var grouptotalinvQty = 0;
	            var grouptotalpinvQty = 0;
	            var grouptotalpinvQty1 = 0;
	            var groupMinQty = 0;
	            var groupRowCount = 0;
	            var groupEnd = 0;
	            var currentRow = 0;

	            var isColumn4Visible = api.column(4).visible();
	            var isColumn5Visible = api.column(5).visible();
	            var isColumn10Visible = api.column(10).visible();
	            var isColumn11Visible = api.column(11).visible();
	            var isColumn12Visible = api.column(12).visible();
	            var isColumn13Visible = api.column(13).visible();
	            var isColumn14Visible = api.column(14).visible();
	            
	            
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	        /*         if ( last !== group ) {
	                	if (i > 0) {
	                		if (parseFloat(groupTotalQty) < parseFloat(groupMinQty) / parseFloat(groupRowCount)){
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right" style="color:red;">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td></td><td></td><td class="t-right" style="color:red;">' + parseFloat(grouptotalinvQty).toFixed(3) + '</td><td class="t-right" style="color:red;">' + parseFloat(grouptotalpinvQty).toFixed(3) + '</td><td class="t-right" style="color:red;">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
				                    );
	                		}else{
		                		$(rows).eq( i ).before(
				                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td></td><td></td><td class="t-right">' + parseFloat(grouptotalinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(grouptotalpinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
				                    );
	                		}
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalPCSQty = 0;
	                    grouptotalinvQty = 0;
	                    grouptotalpinvQty = 0;
	                    groupMinPCSQty = 0;
	                    groupTotalQty = 0;
	                    groupMinQty = 0;
	                    groupRowCount = 0;
	                } */
	                //groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                //grouptotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(17)').html());
	                //grouptotalpinvQty += parseFloat($(rows).eq( i ).find('td:eq(18)').html());
	                //grouptotalpinvQty1 += parseFloat($(rows).eq( i ).find('td:eq(19)').html());
	                //groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(20)').html());
	                
	                groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html());
	                grouptotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                grouptotalpinvQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html());
	                grouptotalpinvQty1 += parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html());
	                
	                groupRowCount += 1;
	                
	                //totalPCSQty+= parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                //totalinvQty+= parseFloat($(rows).eq( i ).find('td:eq(17)').html());
	                //totalpinvQty+= parseFloat($(rows).eq( i ).find('td:eq(18)').html());
	                //totalpinvQty1+= parseFloat($(rows).eq( i ).find('td:eq(19)').html());
	                //totalQty += parseFloat($(rows).eq( i ).find('td:eq(20)').html());
	                
	                totalPCSQty+= parseFloat($(rows).eq( i ).find('td:eq(10)').html());
	                totalinvQty+= parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                totalpinvQty+= parseFloat($(rows).eq( i ).find('td:eq(12)').html());
	                totalpinvQty1+= parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html());
	                currentRow = i;

	                
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
// 	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + parseFloat(totalPCSQty).toFixed(3) + '</td><td></td><td></td><td></td><td class="t-right">' + parseFloat(totalPCSQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalpinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalpinvQty1).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td><td></td></tr>'
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td class="t-right"></td><td class="t-right">' + parseFloat(totalpinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(totalpinvQty1).toFixed(3) + '</td><td></td></tr>'
                    );
	            	/* if (parseFloat(groupTotalQty) < parseFloat(groupMinQty) / parseFloat(groupRowCount)){
	            		$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right" style="color:red;">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td></td><td></td><td class="t-right" style="color:red;">' + parseFloat(grouptotalinvQty).toFixed(3) + '</td><td class="t-right" style="color:red;">' + parseFloat(grouptotalpinvQty).toFixed(3) + '</td><td class="t-right" style="color:red;">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
		                    );
            		}else{
            			$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalPCSQty).toFixed(3) + '</td><td></td><td></td><td></td><td class="t-right">' + parseFloat(grouptotalinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(grouptotalpinvQty).toFixed(3) + '</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
		                    );
            		} */
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
	              //.column(13)
	              .column(10)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              //.column(17)
	              .column(11)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            totalpinvVal = api
	              //.column(18)
	              .column(12)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            totalpinvVal1 = api
	              //.column(19)
	              .column(13)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            totalinvVal = api
	              //.column(20)
	              .column(3)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var totalinvQty = 0;
	            var totalpinvQty = 0;
	            var totalpinvQty1 = 0;
	            var totalPCSQty = 0;
	            var groupTotalPCSQty = 0;
	            var groupMinPCSQty = 0;
	            var groupTotalQty = 0;
	            var grouptotalinvQty = 0;
	            var grouptotalpinvQty = 0;
	            var grouptotalpinvQty1 = 0;
	            var groupMinQty = 0;
	            var groupRowCount = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	            	//groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                //grouptotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(17)').html());
	                //grouptotalpinvQty += parseFloat($(rows).eq( i ).find('td:eq(18)').html());
	                //grouptotalpinvQty1 += parseFloat($(rows).eq( i ).find('td:eq(19)').html());
	                //groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(20)').html());
	                groupTotalPCSQty += parseFloat($(rows).eq( i ).find('td:eq(10)').html());
	                grouptotalinvQty += parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                grouptotalpinvQty += parseFloat($(rows).eq( i ).find('td:eq(12)').html());
	                grouptotalpinvQty1 += parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html());
	                groupRowCount += 1;
	                //totalPCSQty+= parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                //totalinvQty+= parseFloat($(rows).eq( i ).find('td:eq(17)').html());
	                //totalpinvQty+= parseFloat($(rows).eq( i ).find('td:eq(18)').html());
	                //totalpinvQty1+= parseFloat($(rows).eq( i ).find('td:eq(19)').html());
	                //totalQty += parseFloat($(rows).eq( i ).find('td:eq(20)').html());
	                totalPCSQty+= parseFloat($(rows).eq( i ).find('td:eq(10)').html());
	                totalinvQty+= parseFloat($(rows).eq( i ).find('td:eq(11)').html());
	                totalpinvQty+= parseFloat($(rows).eq( i ).find('td:eq(12)').html());
	                totalpinvQty1+= parseFloat($(rows).eq( i ).find('td:eq(13)').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(3)').html());
	                currentRow = i;
	            } );
	              
	            // Update footer
<%-- 	            $(api.column(9).footer()).html(parseFloat(totalPCSQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(10).footer()).html(parseFloat(totalinvQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
	            $(api.column(12).footer()).html(parseFloat(totalpinvQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(13).footer()).html(parseFloat(totalpinvQty1).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(3).footer()).html(parseFloat(totalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));

	            <%-- $(api.column(13).footer()).html(parseFloat(totalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(17).footer()).html(parseFloat(totalCostVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(18).footer()).html(parseFloat(totalpinvVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(19).footer()).html(parseFloat(totalpinvVal1).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>));
	            $(api.column(20).footer()).html(parseFloat(totalinvVal).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
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

    loadbarcode();
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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                    
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
         '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT Min Quantity</TH>'+
         '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT Max Quantity</TH>'+
    	 '<TH><font color="#ffffff" align="left"><b>Quantity</TH>'+
    	 '<TH><font color="#ffffff" align="left"><b>Inventory UOM</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Inventory Min Quantity</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Inventory Max Quantity</TH>'+
    	 '<TH><font color="#ffffff" align="left"><b>Inventory Quantity</TH>'+
    	 '<TH><font color="#ffffff" align="left"><b>TopUp Quantity</TH>'+
    	 '<TH><font color="#ffffff" align="left"><b>Remark</TH>'+
         '</TR>';
             
}

//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
           
</SCRIPT>
</div></div></div>


                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
$(document).ready(function(){
	
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('view_inv_list_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('view_inv_list_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('view_inv_list_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('view_inv_list_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('view_inv_list_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }	
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('view_inv_list_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('view_inv_list_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('view_inv_list_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('view_inv_list_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.CUSTOMER.value == ''){
		   getLocalStorageValue('view_inv_list_CUSTOMER','','CUSTOMER');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('view_inv_list_UOM','','UOM');
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

	 /* Supplier Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form1.plant.value,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		   // return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')"> <p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    	return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
			    + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME
			    + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
				}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			/*$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');*/
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
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});

	/* $('#tableInventorySummary tbody tr').each(function () {
		var i=0;
		var barcodeid = "itemprint"+i;
		i++;
	}); */
	
 });

 function loadbarcode()
 {
	 var BARCODECOUNT = parseFloat($('input[name ="BARCODECOUNT"]').val());
	 //alert(BARCODECOUNT);
	 //var tabled=document.getElementById("tableInventorySummary");
	 	//var len = tabled.rows.length;
	 	if(BARCODECOUNT>0){
	 	for ( var i = 0; i < BARCODECOUNT; i++) {
	 		
			var barcodeid = "itemprint" + i;
	 		var itemprint = (document.getElementById(barcodeid).value);
			JsBarcode("#barcode"+i, ""+ itemprint +"", {format: "CODE128", width:"2",  height:"20", displayValue: "false"} );

			toDataURL($("#barcode"+i).attr("src"),
					function(dataUrl) {
				$("#barcode"+i).attr('src', dataUrl);
				  	},'image/jpeg');
	 	}
	 	}
 }
 function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}

 function showMultiCompanyInventory(item) {
	 
	    document.MultiCompanyInventoryForm.ITEM_COM.value = item;
	    document.MultiCompanyInventoryForm.PLANT_COM.value ='<%=PLANT%>';
	    
	    onCompanyLoad();
	    $("#multicompanyinventoryModal").modal();
	  }
 </script>
 
<jsp:include page="multiCompanyInventorySummaryModal.jsp"flush="true"></jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>