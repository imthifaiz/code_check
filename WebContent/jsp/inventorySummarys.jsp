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
<%
String title = "Inventory Summary";
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
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/JsBarcode.all.js"></script>
<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Inv_Reports&INV_REP_TYPE=invByProdMultiUOM";
	document.form1.submit();
	
} 
</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%@include file="header.jsp" %>
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
String plant = (String) session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry

	
String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",SORT="",  
TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_CLS_ID1="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",MODEL="";
boolean flag=false;
String resultDesc =  StrUtils.fString(request.getParameter("result"));
String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
// Start code added by Deen for product brand on 11/9/12 
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
//  Start code added by Deen for product brand on 11/9/12 
LOC = StrUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3= StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
MODEL    = StrUtils.fString(request.getParameter("MODEL"));
String uom = StrUtils.fString(request.getParameter("UOM"));
SORT = StrUtils.fString(request.getParameter("SORT"));
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
String printdate=DateUtils.getDateFormatYYMMDD();
String LabelType="Single",BarcodeWidth="3",BarcodeHeight="60",FontSize="30",TextAlign="Center",TextPosition="Bottom",DisplayText="Show";
ArrayList arrCust =new MasterDAO().getBarcodePrint("PRDBARCODE", PLANT, "");
if (arrCust.size() > 0) {
    for(int i =0; i<arrCust.size(); i++) {
    	Map arrCustLine = (Map)arrCust.get(i);
    	LabelType=(String)arrCustLine.get("LABEL_TYPE");
    	BarcodeWidth=(String)arrCustLine.get("BARCODE_WIDTH");
    	BarcodeHeight=(String)arrCustLine.get("BARCODE_HEIGHT");
    	FontSize=(String)arrCustLine.get("FONT_SIZE");
    	TextAlign=(String)arrCustLine.get("TEXT_ALIGN");
    	TextPosition=(String)arrCustLine.get("TEXT_POSITION");
    	DisplayText=(String)arrCustLine.get("DISPLAY_BARCODE_TEXT");
    }            
    }
	
//END
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);

if(SORT.equals(""))
{
	SORT="BOTH";
}

boolean cntRec=false;

%>
<center>
	<h2><small class="success-msg"><%=resultDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>	
                <li><label>Inventory Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->    
                        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
            
              <div class="btn-group" role="group">
              <!-- <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button> -->
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
<FORM class="form-horizontal" name="form1" method="post" action="viewInventoryGroupByProdMultiUOM.jsp">
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
 <h1><small> <%=fieldDesc%></small></h1>      
    
  </center> --%>
  
    
    <div id="target" style="display:none;">
       <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-4 ac-box">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected  form-control typeahead" name="ITEM" ID="ITEM"  type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
    		<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 			<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
 		</div>
 		
      
       <div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT CATEGORY ID"> </label>
      <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    	<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
<!--   		<div class="form-inline"> -->
<!--   		<label class="control-label col-sm-2" for="supplier name">Product Type ID:</label> -->
           <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT SUB CATEGORY" size="30"  MAXLENGTH=20>
    	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
			
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
<!--  		</div>   -->
 		</div>
 		 <input type="hidden" name="LOC_DESC" value="">
 		 <div class="form-group">
       <label class="control-label col-sm-2" for="Product Brand ID"> </label>
        <div class="col-sm-4 ac-box ">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    	<span class="select-icon"   onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
			
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
<!--   		<div class="form-inline"> -->
<!--   		<label class="control-label col-sm-2" for="Location">Location ID/Desc:</label> --> 
		<% if(COMP_INDUSTRY.equals("Retail")) { %> 
       <div class="col-sm-4 ac-box ">
    	<INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION/BRANCH" size="30"  MAXLENGTH=20>
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeLocation(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
    	<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
        </div>
         <%}else{%>
       <div class="col-sm-4 ac-box ">
    	<INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeLocation(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
    	<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
<!-- 		<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
			
<!--     		<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
        <%}%>
<!--  		</div> -->
 		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		   <INPUT name="ACTIVE" type = "hidden" value="">  
 		</div>
 
     <div class="form-group">
     <label class="control-label col-sm-2" for="Location Type Id"> </label>
     <div class="col-sm-4 ac-box">
     <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" placeholder="LOCATION TYPE ONE" size="30" MAXLENGTH=20>
     <input type="hidden" name="Location Type ID" value="">
     <span class="select-icon"   onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	 <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
			
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  	</div>
        
         <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>" placeholder="LOCATION TYPE TWO" size="30" MAXLENGTH=20>
        <input type="hidden" name="Location Type ID" value="">
        <span class="select-icon"   onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	 	<span class="btn-danger input-group-addon"onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
		
<!--    		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"> -->
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
        </div>

 	
  	  	
 		<div class="form-group">
  		<label class="control-label col-sm-2" for="LOCTYPE 3"> </label>
        <div class="col-sm-4 ac-box">
     <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>" placeholder="LOCATION TYPE THREE" size="30" MAXLENGTH=20>
     <input type="hidden" name="Location Type ID" value="">
     <span class="select-icon"   onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	 <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID3='+form1.LOC_TYPE_ID3.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
        </div>
        
         <div class="col-sm-4 ac-box">
    	<INPUT class="form-control" name="MODEL" id="MODEL" type = "TEXT" value="<%=StrUtils.forHTMLTag(MODEL)%>" placeholder="MODEL" size="30"  MAXLENGTH=20>
        </div>
        </div>
        
        <div class="form-group">
  		<label class="control-label col-sm-2" for="SORT"> </label>
        <div class="col-sm-7 ac-box">
     <label class="radio-inline">
      	<INPUT  name="SORT" id="SORT" type = "radio" value="BOTH"    <%if(SORT.equalsIgnoreCase("BOTH")) {%>checked <%}%>>Show By Positive, Negative & Zero Qty</label>
   		<label class="radio-inline">
   		<INPUT name="SORT" id="SORT" type = "radio"  value="POSITIVE"   <%if(SORT.equalsIgnoreCase("POSITIVE")) {%>checked <%}%>>Show By Positive Qty</label>
   		<label class="radio-inline">
   		<INPUT name="SORT" id="SORT" type = "radio"  value="NAGATIVE"   <%if(SORT.equalsIgnoreCase("NAGATIVE")) {%>checked <%}%>>Show By Negative Qty</label>
   		<label class="radio-inline">
   		<INPUT name="SORT" id="SORT" type = "radio"  value="ZERO"   <%if(SORT.equalsIgnoreCase("ZERO")) {%>checked <%}%>>Show By Zero Qty</label>
        </div>
        
         <div class="col-sm-2 ac-box">
         <!-- <div class="col-sm-2 ac-box" style="TEXT-ALIGN: right;"> -->
    	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
        </div>
        </div>
        
        <div class="form-group">
   		<label class="control-label col-sm-2" for="uom"></label>
<!--   		<label class="control-label col-sm-2" for="Description">Description:</label> -->
        <div class="col-sm-4 ac-box" hidden>
        <div class="input-group">
 		 <SELECT class="form-control" data-toggle="dropdown" placeholder="SELECT UOM" data-placement="left" name="UOM" id="UOM" style="width: 100%">
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
 		
 		
 		
 		
        <div class="col-sm-4 ac-box">
  	
<!--   	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--   	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>   -->
  	<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
  	</div>
	
  	   	<div class="form-group">
  	<div class="col-sm-offset-5 col-sm-4">   
      
             <INPUT class="ac-selected  form-control typeahead" name="PRD_DESCRIP" type = "hidden" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" placeholder="Description" style="width: 100%"  MAXLENGTH=100> 
    	
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
<!--       <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp; -->
<!--   	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--   	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='reports.jsp'"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 

     <!--   Start code added by Deen for product brand on 11/9/12 -->
     
     <!--   End code added by Deen for product brand on 11/9/12 -->  
    
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
		                   	<th style="font-size: smaller;">PRODUCT DESC</th>
		                   	<th style="font-size: smaller;">CATALOG</th>
		                   	<th style="font-size: smaller;">LIST PRICE</th>
		                    <th style="font-size: smaller;">COST</th>
		                  	
		                  	<th style="font-size: smaller;">INV.QTY</th>
		                   	<th style="font-size: smaller;">PRINT BARCODE</th>		                   
		                   	<th style="font-size: smaller;">EDIT</th>
		                   	
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Location</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product Class ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product Type ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product Brand ID</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Description</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA UOM</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">PC/PCS/EA Quantity</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory UOM</th> -->
<!-- 		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Inventory Quantity</th> -->
		                </tr>
		            </thead>
		            <tbody>
				 
				</tbody>
		                   <!-- IMTIZIAF -->
        <tfoot style="display: none;">
							<tr class="group">
								<th></th>
<!-- 								<th style="text-align: left !important">Total</th> -->
								<th></th>
								<th></th>
								<th></th>
								
								<th style="text-align: right !important"></th>						
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
  <script type="text/javascript">
  var tableInventorySummary;
  var productId, desc, loc, prdBrand, prdClass,prdDep,prdType, locType,locType2,locType3,model,uom,SORT, groupRowColSpan = 4;
function getParameters(){
	return { 
		"ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
		"PRD_BRAND_ID":prdBrand,
		"PRD_CLS_ID":prdClass,
		"PRD_DEPT_ID":prdDep,
		"PRD_TYPE_ID":prdType,
		"LOC_TYPE_ID":locType, 
		"LOC_TYPE_ID2":locType2, 
		"LOC_TYPE_ID3":locType3, 
		"MODEL": model,
		"UOM": uom,
		"SORT": SORT,
		"ACTION": "VIEW_INV_SUMMARY_GROUPBY_PRD_MULTIUOM_NEW",
		"PLANT":"<%=PLANT%>"
	}
} 

function storeUserPreferences(){
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_ITEM', $('#ITEM').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_PRD_CLS_ID', $('#PRD_CLS_ID').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_LOC', $('#LOC').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_MODEL', $('#MODEL').val());
		storeInLocalStorage('viewInventoryGroupByProdMultiUOM_UOM', $('#UOM').val());
}

function onGo(){
    productId = document.form1.ITEM.value;
    desc = document.form1.PRD_DESCRIP.value;
    loc = document.form1.LOC.value;
    prdDep = document.form1.PRD_DEPT_ID.value;
    prdClass = document.form1.PRD_CLS_ID.value;
    prdType = document.form1.PRD_TYPE_ID.value;
     // Start code modified by Deen for product brand on 11/9/12 
    prdBrand = document.form1.PRD_BRAND_ID.value;
    locType = document.form1.LOC_TYPE_ID.value;
    locType2 = document.form1.LOC_TYPE_ID2.value;
    locType3 = document.form1.LOC_TYPE_ID3.value;
    model = document.form1.MODEL.value;
    uom = document.form1.UOM.value;   
    if (uom=="Select UOM")
   	 uom="";
    SORT = document.form1.SORT.value;
    var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){   
    storeUserPreferences();
    }
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
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
		        		 for (var dataIndex = 0; dataIndex < data.items.length; dataIndex++) {
		        			 var catlog = data.items[dataIndex].CATALOGPATH;
	                            var item = data.items[dataIndex];
	                            item['INVUOMQTY'] = '<a  href="javascript:void(0);" onclick="showMultiCompanyInventory(\'' + item['ITEM'] + '\')">'+item['INVUOMQTY']+'</a>';
	                            item['Print'] = '<a href="javascript:void(0);" onclick="showPrintBarcode(\'' + item['ITEM'] + '\', \'' + item['ITEMDESC'] + '\', \'' + item['UnitPrice'] + '\')"><i class="fa fa-barcode"></i></a>';
	                            item['edit'] = '<a href="../product/edit?RTYPE=INV&ITEM=' + item['ITEM'] + '"><i class="fa fa-pencil-square-o"></i></a>';
	                        	item['IMG'] = '<img alt="" src='+catlog+' width="40px" >';
	                        }
		        		
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'ITEM', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'IMG', "orderable": true},
    			{"data": 'UnitPrice', "orderable": true},
    			{"data": 'COST', "orderable": true},
    			{"data": 'INVUOMQTY', "orderable": true},
    			{"data": 'Print', "orderable": true},
    			{"data": 'edit', "orderable": true}
    			
    			],
			//"columnDefs": [{"className": "t-right", "targets": [6,8]}],
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
	    	                title: function () { var dataview = "<%=title%> - Location: "+loc+"  "; return dataview },
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
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%> \n Location: "+loc+"                                             ."  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%> \n Location: "+loc+"                                             ."  ; return dataview },
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
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%> \n Location: "+loc+"                                                                                                         ."  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%> \n Location: "+loc+"                                                                                                         ."  ; return dataview },
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
	            total = api
	              .column(5)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalOrdVal = api
	              .column(5, {
	                page: 'current'
	              })
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var groupTotalQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html());
	                currentRow = i;
	            } );

	            var rowCount = api.rows().count(); // Total count of rows 	              
	            // Update footer
<%-- 	          $(api.column(5).footer()).html(parseFloat(totalQty).toFixed(<%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)); --%>
	          $(api.column(1).footer()).html('Total (' + rowCount + ')');  
	          }
// 	        "order":[],
			
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

$(document).on('click', 'img', function() {
    var src = $(this).attr('src');
    
    $('#imageModal .modal-body img').attr('src', src);
    $('#imageModal').modal('show');
});


function showPrintBarcode(item, desc, price) {
    document.printProductBarcodeForm.ITEM_BARCODE.value = item;
    document.printProductBarcodeForm.ITEM_DESC_BARCODE.value = desc;
    document.printProductBarcodeForm.UNITPRICE_BARCODE.value = price;
    document.printProductBarcodeForm.LabelType.value = '<%=LabelType%>';
    document.printProductBarcodeForm.BarcodeWidth.value = '<%=BarcodeWidth%>';
    document.printProductBarcodeForm.BarcodeHeight.value = '<%=BarcodeHeight%>';
    document.printProductBarcodeForm.FontSize.value = '<%=FontSize%>';
    document.printProductBarcodeForm.TextAlign.value = '<%=TextAlign%>';
    document.printProductBarcodeForm.TextPosition.value = '<%=TextPosition%>';
    document.printProductBarcodeForm.DisplayText.value = '<%=DisplayText%>';
    document.printProductBarcodeForm.CNAME.value = '<%=CNAME%>';
    document.printProductBarcodeForm.printdate.value = '<%=printdate%>';
    document.printProductBarcodeForm.PAGE_TYPE.value = 'PRDBARCODEINV';
    ViewPrtBarcode();
    $("#printProductBarcodeModal").modal();
  }

function showMultiCompanyInventory(item) {
	 
    document.MultiCompanyInventoryForm.ITEM_COM.value = item;
    document.MultiCompanyInventoryForm.PLANT_COM.value ='<%=PLANT%>';
    
    onCompanyLoad();
    $("#multicompanyinventoryModal").modal();
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
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.PRODUCT
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
                    '<TH><font color="#ffffff" align="left"><b>PRODUCT CATEGORY ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>List Price</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Cost</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PCS/EA/PKT/KG/LT UOM</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Inventory Quantity</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Print Barcode</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Edit</TH>'+
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
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_ITEM','', 'ITEM');
 		 } 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_DEPT_ID','', 'PRD_DEPT_ID');
  		} 
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_CLS_ID','', 'PRD_CLS_ID');
  		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_TYPE_ID','', 'PRD_TYPE_ID');
  		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_PRD_BRAND_ID','', 'PRD_BRAND_ID');
 		 }	
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('viewInventoryGroupByProdMultiUOM_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.MODEL.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_MODEL','','MODEL');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('viewInventoryGroupByProdMultiUOM_UOM','','UOM');
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
	
	/* Product Type Number Auto Suggestion */
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
	/* Product Type Number Auto Suggestion */
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
	
	
	
 });

 
 function changeitem(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}
 function changeCategory(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}
 function changeLocation(obj){
	 $("#LOC").typeahead('val', '"');
	 $("#LOC").typeahead('val', '');
	 $("#LOC").focus();
	}

  
  

	
 </script>
  <jsp:include page="ImageProductModal.jsp"flush="true"></jsp:include>
 <jsp:include page="multiCompanyInventorySummaryModal.jsp"flush="true"></jsp:include>
<jsp:include page="PrintProductBarcodeModal.jsp"flush="true"></jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>