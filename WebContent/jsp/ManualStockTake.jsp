<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<!-- IMTIZIAF -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->

<!-- <html>
<head> -->
<%
String title = "Stock Take";
%>
<%@include file="sessionCheck.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<!--
<script language="JavaScript" src="js/jquery-1.4.2.js"></script>
--> 

<script>

var subWin = null;
function popUpWin(URL) {
//document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invWithBatchUOM";
	document.form1.submit();
	
} 

</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
String plant = (String) session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String systatus = session.getAttribute("SYSTEMNOW").toString();


String fieldDesc = StrUtils.fString(request.getParameter("result"));
if ("".equals(fieldDesc)){
	fieldDesc = StrUtils.fString(request.getParameter("msg"));
}
	
String USERID ="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_TYPE_ID="",PRD_DESCRIP="", QTY ="",PRD_BRAND_ID= "";
String html = "",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",MODEL="";
double Total=0;
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();
LOC     = StrUtils.fString(request.getParameter("LOC"));
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
BATCH   = StrUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
MODEL = StrUtils.fString(request.getParameter("MODEL"));
String uom = StrUtils.fString(request.getParameter("UOM"));
//IMTIZIAF
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//END
String compindustry = plantMstDAO.getcompindustry(plant);
boolean cntRec=false;

boolean displayAvailableQty=false;
if(systatus.equalsIgnoreCase("INVENTORY"))
	displayAvailableQty = ub.isCheckValinv("showavlqtyonstk", plant,USERID);	
	

%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>
                <li><a href="../inhouse/stocktake"><span class="underline-on-hover">Stock Take Summary</span> </a></li>	
                <li><label>Stock Take</label></li>                             
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
           <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <!-- <div class="box-title pull-right">
              	<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
				onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div> -->
		</div>
<div class="box-body">
<FORM  class="form-horizontal" name="form1" method="post" action="">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<INPUT type="hidden" name="TRANTYPE" value="MANUALSTOCKTAKE" />
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
 <!-- end -->
  
<%--   <span style="text-align: center;"><small> <%=fieldDesc%></small></span> --%>
<%if(fieldDesc.contains("Stock")){%>
  <center><div style="color:red;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"><%=fieldDesc%></div></center>
<%}else{%>
  <center><div style="color:green;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"><%=fieldDesc%></div></center>
<%}%>
  
   <%-- <center>
  <h1><small> <%=fieldDesc%></small></h1>
     
  </center> --%>
  
  <div id="target" style="display:none;">
   		<div class="form-group">

   		
      <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-3 ac-box">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected  form-control typeahead" name="ITEM" ID="ITEM" type = "TEXT" value="<%=ITEM%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  	 <div class="col-sm-3 ac-box" style="display: none;">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=PRD_DEPT_ID%>"  <% if(compindustry.equals("Pharma")){%>placeholder="SCHEDULED"<% }else{%>placeholder="PRODUCT DEPARTMENT"<% }%> size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 	
       <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>"  <% if(compindustry.equals("Pharma")){%>placeholder="PRODUCT TYPE"<% }else{%>placeholder="PRODUCT CATEGORY"<% }%> size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		
  		    <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>"   <% if(compindustry.equals("Pharma")){%>placeholder="COMBINATION"<% }else{%>placeholder="PRODUCT SUB CATEGORY"<% }%> size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div> 		
 		 	
  		</div> 
  		
  		<div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY ID"> </label> 
        <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>  
  		
      <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type ="TEXT" value="<%=LOC%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 
  		
  		<div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE ONE" size="35" MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div> 		
 		
  		</div> 
  		
  		<div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY ID"> </label> 
     		<div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="BATCH" id="BATCH" type = "TEXT" value="<%=BATCH%>" placeholder="BATCH" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>		
 		
  		</div> 
  		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		<INPUT name="ACTIVE" type = "hidden" value="">
		
		<div class="form-group" style="display: none;">
       <label class="control-label col-sm-2" for="Location"> </label>
     
       <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE TWO" size="35" MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div>
  		
  		  <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" id="LOC_TYPE_ID3" type = "TEXT" value="<%=BATCH%>" placeholder="LOCATION TYPE THREE" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
      
 		</div> 
  		<div class="form-group" style="display: none;">
  		<label class="control-label col-sm-2" for="Batch"> </label>
 
 		   <div class="col-sm-3 ac-box">
        <div class="input-group">
        <INPUT class="ac-selected  form-control typeahead" name="MODEL" id="MODEL" type="TEXT" placeholder="MODEL" value="<%=MODEL%>"	size="35" MAXLENGTH=20>
    	</div>
 		</div>
 		
 		<div class="col-sm-3 ac-box" hidden>
 		 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" placholder="SELECT UOM" name="UOM" id="UOM" style="width: 100%">
			 <option value=""> SELECT UOM </option>	    
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 </div>
	 
	   <div class="form-group">
	   <label class="control-label col-sm-2" for="Search"> </label>
  		<!-- <label class="control-label col-sm-2" for="model">Model:</label> -->
  		 <div class="col-sm-3 ac-box">
 		<button type="button" class="btn btn-success"  onClick="javascript:return onGo();">Search</button>&nbsp;
  		</div>
         
 		</div>		
 		
  		<div class="form-group">
 		 <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description"style="width: 100%"  MAXLENGTH=100>
    	</div>
  		</div>
  		</div>
  		
  	 <div class="form-group" style="display: none;">>
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	</div>
     </div>
     </div> 
     
     <div class="form-group">
	<div class="col-sm-8">
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serializedprd" id="serializedprd" value="1" onchange="serialprd();">
                     <b>Serialized Product</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serializedloc" id="serializedloc" value="1" onchange="serialloc();">
                     <b>Serialized Loc</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serialized" id="serialized" value="1" onchange="serial();">
                     <b>Serialized Qty</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serializedrem" id="serializedrem" value="1" onchange="serialrem();">
                     <b>Serialized Remarks</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serializednobatch" id="serializednobatch" value="1" onchange="serialnobatch();">
                     <b>Set Default NOBATCH</b></lable>
	<INPUT Type=hidden  style="border:0;" name = "bulkcheckout" id="bulkcheckout" value="1" ></input>
        
	</div>
	</div>
	 
	 <div class="form-group">
	 <label class="control-label col-sm-1" for="Product ID">Product ID:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="ac-selected  form-control typeahead" name="ITEMS" ID="ITEMS" type = "TEXT" value="" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEMS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
	 <label class="control-label col-sm-1" for="Loc">LOC:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="ac-selected  form-control typeahead" name="LOCS" ID="LOCS" type ="TEXT" value="" placeholder="LOCATION" size="30"  MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOCS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
	 <label class="control-label col-sm-1" for="Batch">Batch:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="ac-selected  form-control typeahead" name="BATCHS" id="BATCHS" type = "TEXT" value="" placeholder="BATCH" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCHS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
    	<label class="control-label col-sm-1" for="Available Qty">Qty:</label>
    	<div class="col-sm-1" <%if(!displayAvailableQty){ %> style="display: none" <% } %>>
        <INPUT class="form-control" readonly name="AVAILQTY" type="TEXT" id="AVAILQTY"	value="" size="3" maxlength="10" tabindex="-1">
		<input type="hidden" name="QTY_0" value="">
        </div>
    	<div class="col-sm-1">
        <INPUT class="form-control" name="QTY" type="TEXT" id="qty"	value="" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" size="1" maxlength="10">
        <input type="hidden" name="prdqty" id="prdqty"  value="">
        </div>
        <%if(!displayAvailableQty){ %>
        <div class="col-sm-1" style="right:10px">
         <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOMS" style="width: 115%" id="UOMS" onchange="getAvaliableInventoryQty();">
			
					<%
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 <% } %>
    	</div>
  		
	 <div class="form-group">
        <%if(displayAvailableQty){ %>
	 <label class="control-label col-sm-1" for="Product UOM">UOM:</label>
        <div class="col-sm-1" style="right:10px">
         <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOMS" style="width: 115%" id="UOMS" onchange="getAvaliableInventoryQty();">
			
					<%
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 <% } %>
	 <label class="control-label col-sm-1" for="Product ID">Remarks:</label>
	 <div class="col-sm-5">
      	    <div class="input-group">
    		<INPUT class="ac-selected  form-control typeahead" name="REMARKS" ID="REMARKS" type = "TEXT" value="" placeholder="Max 100 characters" size="30"  MAXLENGTH=100>
  		</div>
  		</div>
  	
  		<td ALIGN="left" >
			<div id="add">
			<button type="submit" class="Submit btn btn-default"  id="addbtn" name="action"  onClick="return addaction()"><b>Add</b></button>&nbsp;&nbsp;
<!-- 			<button type="submit" class="Submit btn btn-danger"  id="addbtn" name="action"  onClick="return resetaction()"><b>Reset Stock Take by Inventory</b></button>&nbsp; -->
<!-- 			<button type=submit class="Submit btn btn-default"  id="clearbtn" name="action"  onClick="return clearaction()"><b>Clear</b></button>&nbsp; -->
			
			</div>
			<input type="hidden" name="action1" value="temp">
			</td>
			
  		 <div class="form-inline" style="display: none;">
	 <label class="control-label col-sm-1" for="Batch">Batch:</label>
	 <div class="col-sm-2">
      	    <div class="input-group">
    		<INPUT class="ac-selected  form-control typeahead" name="BATCH" id="BATCH" type = "TEXT" value="" placeholder="BATCH" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
	 	</div>
    	</div>
    	
    	<div class="form-group" style="display: none">
<!--    <div class="col-sm-12 ac-box">  		 -->
					<label class="control-label col-sm-1" for="view">View By :</label>
				  	<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="1" id="all" Checked onclick="changetype(this.value)"> <b>All</b></label>
  					<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="2" id="done" onclick="changetype(this.value)"> <b>Stock Take Processed Product</b></label>
  					<label class="radio-inline">
  					<input name="VIEWSTATUS" type="radio" value="3" id="notdone" onclick="changetype(this.value)"> <b>Stock Take not Processed Product</b></label>
<!-- 	</div> -->
		</div>
  		
  		<input type="hidden" name="LOC_DESC" value="">
  		<input type="hidden" name="RADIOSEARCH" value="2">
		
  <div id="VIEW_RESULT_HERE" class=table-responsive>
 <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              
              <div class="col-12 col-sm-12 no-padding">
                     	
                     	<label class="control-label col-sm-1" for="Loc">Filter By LOC:</label>
	 					<div class="col-sm-2">
			      	    <div class="input-group">
				    		<INPUT class="ac-selected  form-control typeahead" name="LOCS1" ID="LOCS1" type ="TEXT" value="" placeholder="LOCATION" size="30"  MAXLENGTH=20>
        					<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOCS1\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  						</div>
  						</div>
  						<button type=submit class="Submit btn btn-success"  id="clearbtn" name="action"  onClick="return searchloc()"><b>Filter</b></button>&nbsp;&nbsp;
                    </div>
                    <br></br>
              <div class="col-12 col-sm-12 no-padding">
  		        		<input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
                     	<strong>&nbsp;Select/Unselect All </strong>&nbsp;&nbsp;&nbsp;
                     	<button type="submit" class="Submit btn btn-danger"  id="addbtn" name="action"  onClick="return resetaction()"><b>Reset Stock Take by Inventory</b></button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
              
                    
              	<table id="tableInventorySummary"
									class="table table-bordred table-striped" > 
									<!-- role="grid" aria-describedby="tableInventorySummary_info"> -->
					<thead>
		                <tr role="row">
		                <th style="font-size: smaller;">CHK</th>  
		                <th style="font-size: smaller;">PRODUCT</th>  
		                <th style="font-size: smaller;">DESCRIPTION</th>  
		                <th style="font-size: smaller;">TRAN DATE</th>  
		                   <th style="font-size: smaller;">CATEGORY</th>
		                   <th style="font-size: smaller;">SUB CATEGORY</th>
		                <th style="font-size: smaller;">BRAND</th>  
						<th style="font-size: smaller;">UOM</th> 
		                <th style="font-size: smaller;">LOC</th>  
		                <th style="font-size: smaller;">BATCH</th>  
						<th style="font-size: smaller;">STOCK TAKE QTY</th> 		                  
						<th style="font-size: smaller;">INV.QTY</th> 		                  
						<th style="font-size: smaller;">QTY DIFF</th> 		                  
						<th style="font-size: smaller;">USER</th> 		                  
						<th style="font-size: smaller;">REMARK</th> 		                  
       </tr>
       </thead>
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
  
  <div class="form-group">
  	<div class="col-sm-12" align="center"> 
<!--   	<button type="button" class="Submit btn btn-success"  value="Print"  name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button> -->
  	<button type=submit class="Submit btn btn-danger"  id="clearbtn" name="action"  onClick="return clearaction()"><b>Delete</b></button>&nbsp;&nbsp;
  	<button type="button" class="Submit btn btn-success"  value="Print"  name="action" onClick="return process()"><b>Stock Take Process</b></button>&nbsp;&nbsp;
  	 </div>
  	</div>
       
     
  </FORM>
  
   <script>
	  function serialprd(){

		  var item = document.form1.ITEMS.value;	
		  if (item==""){
			  alert("Product Shound not be empty");
			  document.getElementById("serializedprd").checked=false;
		  }else{
			  if ( document.getElementById("serializedprd").checked==true ) {
				  document.getElementById("ITEMS").readOnly = true;
				  document.getElementById("ITEMS").value;
				  document.getElementById("ITEMS").style.backgroundColor = "lightgray";
				  $("#ITEMS").typeahead('destroy');
				  storeInLocalStorage('VIEW_ITEMS', $('#ITEMS').val());
				  document.getElementById("serializedprd").value=1;
				  storeInLocalStorage('VIEW_PRDCHK', '1');
			    } else {
				  document.getElementById("ITEMS").readOnly = false;
				  var qtyValue = document.getElementById("ITEMS").value;
				  document.getElementById("ITEMS").innerHTML =qtyValue;
				  document.getElementById("ITEMS").style.backgroundColor = "";
				  enableprd();
				  storeInLocalStorage('VIEW_ITEMS', '');
				  document.getElementById("serializedprd").value='';
				  storeInLocalStorage('VIEW_PRDCHK', '');
			    }
		}
	  }
	  function serialloc(){
		  var loc = document.form1.LOCS.value;	
		  if (loc==""){
			  alert("Location Shound not be empty");
			  document.getElementById("serializedloc").checked=false;
		  }else{
			  if ( document.getElementById("serializedloc").checked==true ) {
				  document.getElementById("LOCS").readOnly = true;
				  document.getElementById("LOCS").value;
				  document.getElementById("LOCS").style.backgroundColor = "lightgray";
				  $("#LOCS").typeahead('destroy');
				  storeInLocalStorage('VIEW_LOCS', $('#LOCS').val());
				  document.getElementById("serializedloc").value=1;
				  storeInLocalStorage('VIEW_LOCCHK', '1');
			    } else {
				  document.getElementById("LOCS").readOnly = false;
				  var qtyValue = document.getElementById("LOCS").value;
				  document.getElementById("LOCS").innerHTML =qtyValue;
				  document.getElementById("LOCS").style.backgroundColor = "";
				  enableloc();
				  storeInLocalStorage('VIEW_LOCS', '');
				  document.getElementById("serializedloc").value='';
				  storeInLocalStorage('VIEW_LOCCHK', '');
		    }
		 }
	  }
	  function serialnobatch(){		  
			  if ( document.getElementById("serializednobatch").checked==true ) {
			  	  $("#BATCHS").typeahead('val', 'NOBATCH');
				  storeInLocalStorage('VIEW_BATCHS', $('#BATCHS').val());
				  document.getElementById("serializednobatch").value=1;
				  storeInLocalStorage('VIEW_BATCHSCHK', '1');
			    } else {
			  	  $("#BATCHS").typeahead('val', '');
				  storeInLocalStorage('VIEW_BATCHS', '');
				  document.getElementById("serializednobatch").value=1;
				  storeInLocalStorage('VIEW_BATCHSCHK', '');
			    }
		  $("#BATCHS").typeahead('destroy');
		  loadbatch();
	  }
	  function serialrem(){
		  var remark=document.form1.REMARKS.value;
		  if (remark==""){
			  alert("Remark Shound not be empty");
			  document.getElementById("serializedrem").checked=false;
		  }else{
			  if ( document.getElementById("serializedrem").checked==true ) {
				  document.getElementById("REMARKS").readOnly = true;
				  document.getElementById("REMARKS").value;
				  document.getElementById("REMARKS").style.backgroundColor = "lightgray";
				  storeInLocalStorage('VIEW_REMARKS', $('#REMARKS').val());
				  document.getElementById("serializedrem").value=1;
				  storeInLocalStorage('VIEW_REMCHK', '1');
			    } else {
				  document.getElementById("REMARKS").readOnly = false;
				  var qtyValue = document.getElementById("REMARKS").value;
				  document.getElementById("REMARKS").innerHTML =qtyValue;
				  document.getElementById("REMARKS").style.backgroundColor = "";
    			  storeInLocalStorage('VIEW_REMARKS', '');
				  document.getElementById("serializedrem").value='';
				  storeInLocalStorage('VIEW_REMCHK', '');
			    }
		 }
	  }
	  function serial(){
		  var qty=document.form1.QTY.value;
			  if ( document.getElementById("serialized").checked==true ) {
				  document.getElementById("qty").readOnly = true;
				  document.getElementById("qty").value=1;
				  storeInLocalStorage('VIEW_QTYS', $('#qty').val());
				  document.getElementById("prdqty").value=1;
				  storeInLocalStorage('VIEW_QTYCHK', '1');
			    } else {
				  document.getElementById("qty").readOnly = false;
				  var qtyValue = document.getElementById("qty").value;
				  document.getElementById("qty").innerHTML =qtyValue;
				  document.getElementById("qty").style.backgroundColor = "";
				  storeInLocalStorage('VIEW_QTYS', '');
				  document.getElementById("prdqty").value='';
				  storeInLocalStorage('VIEW_QTYCHK', '');
			    }
	  }

	  function addaction()
	  {
		  var item = document.form1.ITEMS.value;	
		  var loc = document.form1.LOCS.value;	
		  var qty=document.form1.QTY.value;
		  var batch=document.form1.BATCHS.value;
		  
		  if(item==null||item=="")
		  {
			  alert("Please Scan Product ID!");
			  document.form1.ITEMS.focus();
			  return false;
		  }
		  
		  if(loc==null||loc=="")
		  {
			  alert("Please Enter Location!");
			  document.form1.LOCS.focus();
			  return false;
		  }
		  
		  if(batch==null||batch=="")
		  {
			  alert("Please Select Batch!");
			  document.form1.BATCHS.focus();
			  return false;
		  }
		  
	      if(document.form1.UOMS.value == ""){
	     	    alert("Please enter a UOM value");
	     	    document.form1.UOMS.focus();
	     	    document.form1.UOMS.select();
	     	    return false;
	     }
	      if(qty==null||qty=="")
		  {
			  alert("Please Enter Qty!");
			  document.form1.QTY.focus();
			  return false;
		  }
		  
// 			 document.form1.cmd.value="ADD" ;
			 document.form1.action  = "/track/DynamicProductServlet?cmd=ADD";
			 document.form1.submit();
			 $(this).find("input[name ='ITEMS']").focus();
			 return false;
	  }

	  function resetaction(){


		    if (!confirm("Are you sure you want to Reset Stock?")) {
		        return false;
		    }
		    
			 document.form1.action  = "/track/DynamicProductServlets?cmd=RESETSTK";
			 document.form1.submit();
			 $(this).find("input[name ='ITEMS']").focus();
			 return false;
	  }



	  function clearaction() {

		    var checkedItems = [];
		    $('input[name="CHKSTKID"]:checked').each(function () {
		        checkedItems.push($(this).val());
		    });

		    if (checkedItems.length === 0) {
		        alert("please Select items.");
		        return false;
		    }

		    if (!confirm("Are you sure you want to delete the selected items?")) {
		        return false;
		    }
		    
			document.form1.action  = "/track/DynamicProductServlet?cmd=STKTAKE_REMOVE";
			document.form1.submit();
		}	

	  function searchloc() {
		    onGo();
		}	

	  function checkAll(isChk)
	  {
	  	var len = document.form1.CHKSTKID.length;
	   	var orderLNo; 
	   	if(len == undefined) len = 1;  
	  	if (document.form1.CHKSTKID){
	          for (var i = 0; i < len ; i++){
	  			if(len == 1){
	  				document.form1.CHKSTKID.checked = isChk;
	  			}
	  			else{
	  				document.form1.CHKSTKID[i].checked = isChk;
	  			}   
	          }
	      }
	  }

	  function process(){
			 document.form1.action  = "/track/salesorder/MANUALSTOCKTAKE_ADDPROCESS";
			 document.form1.submit();
			 $("#LOCS1").typeahead('val', '');
			 storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC1', '');
// 			 localstorage.removeItem('view_inv_list_withoutpriceqtyMultiUOM_LOC1');
// 			 localstorage.removeItem('LOCS1');
// 			 getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC1','','');
			 ongo();
			 return false;
	  }

  function changetype(count){
	  $("input[name ='RADIOSEARCH']").val(count);
	  onGo();
	}
	  
  var tableInventorySummary;
  var productId, desc, loc,locs, prdBrand, prdClass,prdDep, prdType, loctype, loctype2,locType3,batch,model,uom,radio, groupRowColSpan = 8;
function getParameters(){
	return { 
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
		"LOCS":locs,
		"BATCH":batch,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDep,
		"PRD_TYPE_ID":prdType,
		"PRD_BRAND_ID":prdBrand,
		"LOC_TYPE_ID":loctype,
		"LOC_TYPE_ID2":loctype2,
		"LOC_TYPE_ID3":loctype3,
		"MODEL":model,
		"UOM": uom,
		"TYPE": "MANUAL",
		"RADIO":radio,
		"ACTION": "VIEW_MANUALSTK_SUMMARY",
		"PLANT":"<%=PLANT%>"
	}
}  

function storeUserPreferences(){
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_ITEM', $('#ITEM').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC', $('#LOC').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_BATCH', $('#BATCH').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_MODEL', $('#MODEL').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_UOM', $('#UOM').val());
	storeInLocalStorage('view_inv_list_withoutpriceqtyMultiUOM_LOC1', $('#LOCS1').val());
// 	storeInLocalStorage('VIEW_ITEMS', $('#ITEMS').val());
// 	storeInLocalStorage('VIEW_LOCS', $('#LOCS').val());
	storeInLocalStorage('VIEW_BATCHS', $('#BATCHS').val());
// 	storeInLocalStorage('VIEW_QTY', $('#QTY').val());
// 	storeInLocalStorage('REMARKS', $('#REMARKS').val());

}

function onGo(){
    productId = document.form1.ITEM.value;
    desc = document.form1.PRD_DESCRIP.value;
    loc = document.form1.LOC.value;
    locs = document.form1.LOCS1.value;
    prdDep = document.form1.PRD_DEPT_ID.value;
    batch = document.form1.BATCH.value;
     prdClass = document.form1.PRD_CLS_ID.value;
    prdType = document.form1.PRD_TYPE_ID.value;
    prdBrand = document.form1.PRD_BRAND_ID.value;
    loctype = document.form1.LOC_TYPE_ID.value;
    loctype2 = document.form1.LOC_TYPE_ID2.value;
    loctype3 = document.form1.LOC_TYPE_ID3.value;
    model = document.form1.MODEL.value;
    uom = document.form1.UOM.value;
    radio = document.form1.RADIOSEARCH.value;
    if (uom=="Select UOM")
      	 uom="";
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
    storeUserPreferences();
    }
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
			"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
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
		        			data.items[dataIndex]['QTY'] = parseFloat(data.items[dataIndex]['QTY']).toFixed("3").replace(/\d(?=(\d{3})+\.)/g, "$&,");
		        			data.items[dataIndex]['STKTAKEQTY'] = parseFloat(data.items[dataIndex]['STKTAKEQTY']).toFixed("3").replace(/\d(?=(\d{3})+\.)/g, "$&,");
		        			data.items[dataIndex]['INVUOMQTY'] = parseFloat(data.items[dataIndex]['INVUOMQTY']).toFixed("3").replace(/\d(?=(\d{3})+\.)/g, "$&,");
// 		        			data.items[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; name=CHKSTKID value="'+data.items[dataIndex]['ITEM']+'#'+data.items[dataIndex]['LOC']+'#'+data.items[dataIndex]['BATCH']+'" >';
		        			data.items[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; name=CHKSTKID value="'+data.items[dataIndex]['STKID']+'" >';
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHK', "orderable": false},
				{"data": 'ITEM', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'CRDATE', "orderable": true},
       			{"data": 'PRDCLSID', "orderable": true},
    			{"data": 'ITEMTYPE', "orderable": true},
    			{"data": 'PRD_BRAND_ID', "orderable": true},
    			{"data": 'INVENTORYUOM', "orderable": true},
    			{"data": 'LOC', "orderable": true},
    			{"data": 'BATCH', "orderable": true},
    			{"data": 'STKTAKEQTY', "orderable": true},
    			{"data": 'INVUOMQTY', "orderable": true},
    			{"data": 'QTYDIFF', "orderable": true},
    			{"data": 'CRBY', "orderable": true},
    			{"data": 'REMARKS', "orderable": true}
//     			{"data": 'DETDESC', "orderable": true},
//     			{"data": 'STKUOM', "orderable": true},    			
//     			{"data": 'QTY', "orderable": true},
    			],
			"columnDefs": [{"className": "t-right", "targets": [9,10,11]}],
			"orderFixed": [ groupColumn, 'asc' ], 
			/* "dom": 'lBfrtip', */
			 "dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	         buttons: [],
			"drawCallback": function ( settings ) {}
		});
    }
}

   
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
                     '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT UOM</TH>'+                    
                    '<TH><font color="#ffffff" align="Right"><b>PCS/EA/PKT/KG/LT Quantity</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Inventory UOM</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Inventory Quantity</TH>'+
                    '</TR>';
                
}
   
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
              
</SCRIPT>
</div>
</div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
                 <script>
$(document).ready(function(){
	if (document.form1.LOCS1.value == ''){
		getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC1','','LOCS1');
	}
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
	   if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }	
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.BATCH.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_BATCH','','BATCH');
		}
	   if (document.form1.MODEL.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_MODEL','','MODEL');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('view_inv_list_withoutpriceqtyMultiUOM_UOM','','UOM');
		}
	   }
	   if (document.form1.serializedrem.value == ''){
		   getLocalStorageValue('VIEW_REMCHK','','serializedrem');
		}
	   if (document.form1.serializednobatch.value == ''){
		   getLocalStorageValue('VIEW_BATCHSCHK','','serializednobatch');
		}
	   if (document.form1.prdqty.value == ''){
		   getLocalStorageValue('VIEW_QTYCHK','','prdqty');
		}
	   if (document.form1.serializedloc.value == ''){
		   getLocalStorageValue('VIEW_LOCCHK','','serializedloc');
		}
	   if (document.form1.serializedprd.value == ''){
		   getLocalStorageValue('VIEW_PRDCHK','','serializedprd');
		}
		
	   if (document.form1.ITEMS.value == ''){
		   getLocalStorageValue('VIEW_ITEMS','','ITEMS');
		}
	   if (document.form1.LOCS.value == ''){
		   getLocalStorageValue('VIEW_LOCS','','LOCS');
		}
	   if (document.form1.QTY.value == ''){
		   getLocalStorageValue('VIEW_QTYS','','qty');
		}
	   if (document.form1.REMARKS.value == ''){
		   getLocalStorageValue('VIEW_REMARKS','','REMARKS');
		}
	   if (document.form1.BATCHS.value == ''){
		   getLocalStorageValue('VIEW_BATCHS','','BATCHS');
		}

	   if (document.form1.serializedprd.value != '' & document.form1.ITEMS.value != ''){
		   document.getElementById("serializedprd").checked=true;
		   document.getElementById("ITEMS").readOnly = true;
		   document.getElementById("ITEMS").style.backgroundColor = "lightgray";
	   }

	   if (document.form1.serializedloc.value != '' & document.form1.LOCS.value != ''){
		   document.getElementById("serializedloc").checked=true;
		   document.getElementById("LOCS").readOnly = true;
		   document.getElementById("LOCS").style.backgroundColor = "lightgray";
	   }

	   if (document.form1.serializedrem.value != '' & document.form1.REMARKS.value != ''){
		   document.getElementById("serializedrem").checked=true;
		   document.getElementById("REMARKS").readOnly = true;
		   document.getElementById("REMARKS").style.backgroundColor = "lightgray";
	   }

	   if (document.form1.serializednobatch.value != '' & document.form1.BATCHS.value != ''){
		   document.getElementById("serializednobatch").checked=true;
	   }

	   if (document.form1.prdqty.value != ''){
		   document.getElementById("serialized").checked=true;
		   document.getElementById("qty").readOnly = true;
		   document.getElementById("qty").style.backgroundColor = "lightgray";
	   }
    onGo();
//     searchloc();
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
	$('#ITEMS').typeahead({
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
		}).on('typeahead:change',function(event,selection){
			if ( document.getElementById("serializednobatch").checked==true ) {
			$("#BATCHS").typeahead('val', 'NOBATCH');
			} else {
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			document.form1.AVAILQTY.value = "";
			}
			$("#BATCHS").typeahead('destroy');
			loadbatch();
		}).on('typeahead:select',function(event,selection){
			if ( document.getElementById("serializednobatch").checked==true ) {
			$("#BATCHS").typeahead('val', 'NOBATCH');
			} else {
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			document.form1.AVAILQTY.value = "";
			}
			$("#BATCHS").typeahead('destroy');
			loadbatch();
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* Product Category Auto Suggestion */
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
	$('#LOCS').typeahead({
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
		}).on('typeahead:change',function(event,selection){
			if ( document.getElementById("serializednobatch").checked==true ) {
			$("#BATCHS").typeahead('val', 'NOBATCH');
			} else {
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			document.form1.AVAILQTY.value = "";
			}
			$("#BATCHS").typeahead('destroy');
			loadbatch();
		}).on('typeahead:select',function(event,selection){
			if ( document.getElementById("serializednobatch").checked==true ) {
			$("#BATCHS").typeahead('val', 'NOBATCH');
			} else {
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			document.form1.AVAILQTY.value = "";
			}
			$("#BATCHS").typeahead('destroy');
			loadbatch();  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});

	$('#LOCS1').typeahead({
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
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			
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

	/* location 3 Auto Suggestion */
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
	
	$('#BATCHS').typeahead({
	hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	},
	{
	  display: 'BATCH',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_BATCH_DATA_STK",
			QUERY : query,
			ITEMNO : document.form1.ITEMS.value,
			UOM : document.form1.UOMS.value,
			LOC : document.form1.LOCS.value
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.batches);
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
			console.log(data);
			return '<div onclick="document.form1.AVAILQTY.value = \''+data.QTY+'\'"><p class="item-suggestion">'+data.BATCH+'</p></div>';
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

	$(this).find("input[name ='ITEMS']").focus();

 });

 function loadbatch(){
	 var plant= '<%=PLANT%>'; 
	 $('#BATCHS').typeahead({
	hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	},
	{
	  display: 'BATCH',  
	  source: function (query, process,asyncProcess) {
		  if(query==='')
			  query='NOBATCH';
		var urlStr = "/track/MasterServlet";
		//var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_BATCH_DATA_STK",
			QUERY : query,
			ITEMNO : document.form1.ITEMS.value,
			UOM : document.form1.UOMS.value,
			LOC : document.form1.LOCS.value
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.batches);
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
			console.log(data);
			return '<div onclick="document.form1.AVAILQTY.value = \''+data.QTY+'\'"><p class="item-suggestion">'+data.BATCH+'</p></div>';
		}
	  }
	}).on('typeahead:select',function(event,selection){
		if($(this).val() != ""){
		document.form1.AVAILQTY.value = selection.QTY;
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
	 }

 function enableloc(){
	 var plant= '<%=PLANT%>';
	  
	

	 $('#LOCS').typeahead({
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
	 }
 function enableprd(){
	 var plant= '<%=PLANT%>';
	  

	 $('#ITEMS').typeahead({
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
		}).on('typeahead:change',function(event,selection){
			$("#BATCHS").typeahead('val', 'NOBATCH');
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			$("#BATCHS").typeahead('destroy');
			loadbatch();
		}).on('typeahead:select',function(event,selection){
			$("#BATCHS").typeahead('val', 'NOBATCH');
			$("#BATCHS").typeahead('val', '');
			$("#BATCHS").val("");	 
			document.form1.BATCHS.value = "";
			$("#BATCHS").typeahead('destroy');
			loadbatch();
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	 }

 function getAvaliableInventoryQty(uom)
 {
	  var itemValue = document.getElementById("ITEMS").value;
	  var locValue = document.getElementById("LOCS").value;
	  var batch = document.getElementById("BATCHS").value;	 
	  
		  var urlStr = "/track/ItemMstServlet";
		  
		  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",LOC:locValue,BATCH:batch,UOM:uom, ACTION: "PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetInventoryQty }); 
	  
	  
	
 }
 
 function onGetInventoryQty(data){
	  
		
	  
	  var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				document.getElementById("AVAILQTY").value=0;
				
			}
		});
		
		if(!errorBoo){
	        $.each(data.items, function(i,item){
	              	document.getElementById("AVAILQTY").value = item.INVENTORYQUANTITY;
	        	
	        });
	       }
	  
 }
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>