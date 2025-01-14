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
String title = "Generate Multi Product Barcode";
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
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
 

function checkAll(isChk)
{
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}
function onRePrint(type){
		var checkFound = false;
		 var Traveler ;
		 var concatTraveler="";
		 var printqty="0";
		 var unitprice="0";
		 var j=0;
		var len = document.form1.chkdDoNo.length;
		 var orderLNo; 
		if (len == undefined)
			len = 1;
		for ( var i = 0; i < len; i++) {
			if (len == 1 && (!document.form1.chkdDoNo.checked)) {
				checkFound = false;
			}

			else if (len == 1 && document.form1.chkdDoNo.checked) {
				checkFound = true;
				
			}

			else {
				if (document.form1.chkdDoNo[i].checked) {
					checkFound = true;
					
				}
			}

		}
		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}
		
		for ( var i = 0; i < len; i++) {
			if (len == 1 && document.form1.chkdDoNo.checked) {
				var barcode="";
				var str = document.form1.chkdDoNo.value;
				unitprice=document.form1.LISTPRICE[i].value;
				printqty = document.form1.PRINTINGQTY.value;
				Traveler=document.form1.chkdDoNo.value;
				concatTraveler=concatTraveler+Traveler+","+unitprice+","+printqty+"=";	
				
				//alert(barcode);			
			}
			else {
				if (document.form1.chkdDoNo[i].checked) {

					j=j+1;
	                Traveler=document.form1.chkdDoNo[i].value;
	                unitprice=document.form1.LISTPRICE[i].value;
	                printqty=document.form1.PRINTINGQTY[i].value;
	                concatTraveler=concatTraveler+Traveler+","+unitprice+","+printqty+"=";    							
				}
			}
				document.form1.TRAVELER.value=concatTraveler;		
			}
		document.form1.action="/track/inhouse/printbarcode?PAGE_TYPE=MULTIPLEPRODUCT&PRINT_TYPE="+type;
	    document.form1.submit();
	}
</script>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
</head>
<%@ include file="header.jsp"%>
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
String USERID ="",PLANT="",LOC ="",  ITEM = "", BATCH="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_TYPE_ID="",PRD_DESCRIP="", QTY ="",PRD_BRAND_ID= "";
String html = "",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",MODEL="",allChecked = "";
double Total=0;
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
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
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
String compindustry = plantMstDAO.getcompindustry(plant);
boolean cntRec=false;
String SELLING_PRICE="&#8377;";
String PRINTDATE_LABEL="Mfd.",printdate="yyMMdd",PRINT_WITH_MODEL="0",PRINT_WITH_PLANT="1";
ArrayList arrCust =new MasterDAO().getBarcodeProductPrint("Product", PLANT, "");
if (arrCust.size() > 0) {
    for(int i =0; i<arrCust.size(); i++) {
    	Map arrCustLine = (Map)arrCust.get(i);
    	PRINTDATE_LABEL=((String)arrCustLine.get("PRINT_DATE_LABEL")!= null)? (String)arrCustLine.get("PRINT_DATE_LABEL"):"Mfd";
    	PRINT_WITH_PLANT=((String)arrCustLine.get("PRINT_WITH_PLANT")!= null)?(String)arrCustLine.get("PRINT_WITH_PLANT"):"0";
    	PRINT_WITH_MODEL=((String)arrCustLine.get("PRINT_WITH_MODEL") != null)?(String)arrCustLine.get("PRINT_WITH_MODEL"):"0";
    	printdate=((String)arrCustLine.get("PRINT_DATE")!= null) ?(String)arrCustLine.get("PRINT_DATE"):"yyMMdd";
    	SELLING_PRICE=((String)arrCustLine.get("PRICESYMBOL")!= null) ?(String)arrCustLine.get("PRICESYMBOL"):"&#8377;";
    }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../inhouse/genbarcode"><span class="underline-on-hover">Generate Barcode</span></a></li>	
                <li><label>Generate Multi Product Barcode</label></li>                                    
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
           <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
						
              	<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
				onclick="window.location.href='../inhouse/genbarcode'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM  class="form-horizontal" name="form1" method="post" action="PrintMultipleProductcode.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<INPUT	type="hidden" name="TRAVELER" value="">
<!-- imtiziaf -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<%-- <input type="hidden" name="SELLING_PRICE" value="<%=SELLING_PRICE%>"> --%>
 <!-- end -->
  
  <span style="text-align: center;"><small> <%=fieldDesc%></small></span>
  
  <div id="target" style="display:none;">
   		<div class="form-group">

   		
      <label class="control-label col-sm-2" for="Product ID">Search</label>
       <div class="col-sm-3 ac-box">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected  form-control typeahead" name="ITEM" ID="ITEM" type = "TEXT" value="<%=ITEM%>" placeholder="PRODUCT" size="30"  MAXLENGTH=50>
   		 	
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  	 <div class="col-sm-3 ac-box">
  	 <INPUT class="ac-selected  form-control typeahead" name="ITEM_DESC" ID="ITEM_DESC" type = "TEXT" value="<%=ITEM%>" placeholder="PRODUCT DESCRIPTION" size="30"  MAXLENGTH=50>
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM_DESC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 	
  	 <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=PRD_DEPT_ID%>"  <% if(compindustry.equals("Pharma")){%>placeholder="SCHEDULED"<% }else{%>placeholder="PRODUCT DEPARTMENT"<% }%> size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 	
  		</div> 
  		
  		<div class="form-group">
       <label class="control-label col-sm-2" for="PRODUCT SUB CATEGORY ID"> </label> 
              <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>"  <% if(compindustry.equals("Pharma")){%>placeholder="PRODUCT TYPE"<% }else{%>placeholder="PRODUCT CATEGORY"<% }%> size="30"  MAXLENGTH=20>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
        <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_TYPE_ID" ID="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>"   <% if(compindustry.equals("Pharma")){%>placeholder="COMBINATION"<% }else{%>placeholder="PRODUCT SUB CATEGORY"<% }%> size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div> 			
        <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
        <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>  
  		
      <div class="col-sm-3 ac-box" style="display:none;">
        <INPUT class="ac-selected  form-control typeahead" name="LOC" ID="LOC" type ="TEXT" value="<%=LOC%>" placeholder="LOCATION" size="30"  MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div> 
  		</div> 
  		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		<INPUT name="ACTIVE" type = "hidden" value="">
                <!--   Start code added by Deen for product brand on 11/9/12 -->
		
		<div class="form-group" style="display:none;">
       <label class="control-label col-sm-2" for="Location"> </label>
     <div class="col-sm-3 ac-box">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE ONE" size="35" MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div> 		
  			<!--<div class="form-inline">
        <label class="control-label col-sm-2" for="Batch">Batch:</label> -->
       <div class="col-sm-3 ac-box" style="display:none;">
        <INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID2%>" placeholder="LOCATION TYPE TWO" size="35" MAXLENGTH=20>
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 		</div>
  		
  		  <div class="col-sm-3 ac-box" style="display:none;">
    		<INPUT class="ac-selected  form-control typeahead" name="LOC_TYPE_ID3" id="LOC_TYPE_ID3" type = "TEXT" value="<%=BATCH%>" placeholder="LOCATION TYPE THREE" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
      
 		</div> 
  		<div class="form-group" style="display:none;">
  		<label class="control-label col-sm-2" for="Batch"> </label>
    <div class="col-sm-3 ac-box">
    		<INPUT class="ac-selected  form-control typeahead" name="BATCH" id="BATCH" type = "TEXT" value="<%=BATCH%>" placeholder="BATCH" size="30"  MAXLENGTH=40>
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
 		   <div class="col-sm-3 ac-box" style="display:none;">
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
  		
  		<div class="form-group">
  		<label class="control-label col-sm-2" for="Search"> Print Date</label>
  		 <div class="col-sm-2 ac-box">
    		<input name="PRINTDATE_LABEL" type="TEXT" value="<%=PRINTDATE_LABEL%>"
			size="20" MAXLENGTH=50 class="form-control">
			
      </div>
		<div class="col-sm-2">
  		<input type="text" class="form-control" id="PRINTDATE" placeholder="Print Date Format yyMMdd" name="PRINTDATE" value="<%=printdate%>" MAXLENGTH=50>
  		</div>
  		<div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtdate" name = "printwithtdate" value = "printwithtdate" checked />Print With Date</label>
      </div>
  		</div>
			<div class="form-group">
			<label class="control-label col-sm-2" for="Search"></label>
  		 <label class="radio-inline">
				      	<input type="radio" name="printWithPLANT" type = "radio" value="1" <%if(PRINT_WITH_PLANT.equals("1")) {%>checked <%}%> >Print With Company Name   
				     	</label>
  		 <label class="radio-inline">
				      	<input type="radio" name="printWithPLANT" type = "radio" value="0" <%if(PRINT_WITH_PLANT.equals("0")) {%>checked <%}%> >Print With Company Email & Hand Phone No.
				     	</label>
  		 
  		</div>
  		
  		    <div class="form-group">
  		    <label class="control-label col-sm-2" for="Search"> List Price</label>
      <div class="col-sm-2">
    		<input name="SELLING_PRICE" type="TEXT" value="<%=SELLING_PRICE%>"
			size="20" MAXLENGTH=250 class="form-control">
			
      </div>
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtmodel" name = "printwithtmodel" value = "printwithtmodel" <%if(PRINT_WITH_MODEL.equals("1")) {%>checked <%}%> />Print With Model</label>
      </div>
    </div>
    
  		
  		<input type="hidden" name="LOC_DESC" value="">
	<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                       &nbsp; Select/Unselect All </strong>&nbsp;
                       <!-- <input type="checkbox" class="form-check-input" name="printwithtdate" value="printwithtdate" />&nbsp;Print with Batch
                       &nbsp; </strong>&nbsp;
                       <input type="checkbox" class="form-check-input" name="printwithtmodel" value="printwithmodel" style="display: none"/>&nbsp;Print with Model
                       &nbsp; </strong>&nbsp;<input type="checkbox" class="form-check-input" name="PRINT_WITH_SIZE" value="PRINT_WITH_SIZE" />&nbsp;Print with Location -->
                    </div>
  </div>	
  <div id="VIEW_RESULT_HERE" class=table-responsive>
 <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordred table-striped" > 
					<thead>
		                <tr role="row">
		                <th style="font-size: smaller;">CHK</th>
		                <th style="font-size: smaller;">PRODUCT</th>  
		                <th style="font-size: smaller;">DESCRIPTION</th>  
		                   <th style="font-size: smaller;">MODEL</th>
		                <th style="font-size: smaller;">LIST PRICE</th>  
		                <th style="font-size: smaller;">PRINTQTY</th>  
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
  	    <!-- <input type="checkbox" class="form-check-input" name="printwithduplicate" value="printwithduplicate"/>&nbsp;<label>&nbsp;Reprint</label>
  	&nbsp;<button type="button" class="Submit btn btn-default"  value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button> -->
  	<button type="button" class="Submit btn btn-default" value="Print" id="GB30X25" name="action" onclick="javascript:return onRePrint('30X25');"><b>Generate Barcode 30X25 mm</b></button>
<button type="button" class="Submit btn btn-default" value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button>
<button type="button" class="Submit btn btn-default" value="Print" name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button>
  	 </div>
  	</div>     
     
  </FORM>
  
   <script>

  var tableInventorySummary;
  var numberOfDecimal = "<%=numberOfDecimal%>";
  var productId, desc, loc, prdBrand, prdClass,prdDep, prdType, loctype, loctype2,locType3,batch,model,uom,start,end, groupRowColSpan = 8;
function getParameters(){
	return { 

		"PRODUCT":productId,
		"PRODUCTDESC":desc,
		"PRODUCTCLS":prdClass,
		"PRODUCTDEPT":prdDep,
		"PRODUCTTYPE":prdType,
		"PRODUCTBRAND":prdBrand,
		"start":start,
		"end":end,
		"action": "GET_PRODUCT_FOR_SUMMARY",
		"PLANT":"<%=PLANT%>"
		
		<%-- "ITEM": productId,
		"PRD_DESCRIP":desc,
		"LOC":loc,
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
		"PLANT":"<%=PLANT%>" --%>
	}
}  

function storeUserPreferences(){
	storeInLocalStorage('view_inv_list_by_Inventory_Product_ITEM', $('#ITEM').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_PRD_CLS_ID', $('#PRD_CLS_ID').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_LOC', $('#LOC').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_BATCH', $('#BATCH').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_MODEL', $('#MODEL').val());
	storeInLocalStorage('view_inv_list_by_Inventory_Product_UOM', $('#UOM').val());
}

function onGo(){
    productId = document.form1.ITEM.value;
    desc = document.form1.PRD_DESCRIP.value;
    loc = document.form1.LOC.value;
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
    if (uom=="Select UOM")
      	 uom="";
    storeUserPreferences();
    start=1;
	end=1;
    var urlStr = "/track/MasterServlet";
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
		        	if(typeof data.CUSTOMERTYPELIST[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
			        		//var price =parseFloat(data.items[dataIndex]['UNITPRICE']).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
			        		var price =parseFloat(data.CUSTOMERTYPELIST[dataIndex]['12']).toFixed(numberOfDecimal);
		        			data.CUSTOMERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.CUSTOMERTYPELIST[dataIndex]['ITEM']+','+data.CUSTOMERTYPELIST[dataIndex]['ITEMDESC']+','+data.CUSTOMERTYPELIST[dataIndex]['30']+'" >';
		        			data.CUSTOMERTYPELIST[dataIndex]['LISTPRICE'] = '<input type="text" name="LISTPRICE" value="'+data.CUSTOMERTYPELIST[dataIndex]['12']+'" class="form-control">';
		        			data.CUSTOMERTYPELIST[dataIndex]['PRINTINGQTY'] = '<input type="text" name="PRINTINGQTY" value="1" class="form-control">';
		        		}
		        		return data.CUSTOMERTYPELIST;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHKOB', "orderable": true},
				{"data": 'ITEM', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": '30', "orderable": true},
    			{"data": 'LISTPRICE', "orderable": true},
    			{"data": 'PRINTINGQTY', "orderable": true}
    			],
// 			"columnDefs": [{"className": "t-right", "targets": [9,10]}],
			"orderFixed": [ groupColumn, 'asc' ], 
			/* "dom": 'lBfrtip', */
			 "dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	         buttons: []
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
	
	   if (document.form1.ITEM.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_ITEM','', 'ITEM');
		 } 	
	   if (document.form1.PRD_DEPT_ID.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_PRD_DEPT_ID','', 'PRD_DEPT_ID');
		} 
	   if (document.form1.PRD_CLS_ID.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_PRD_CLS_ID','', 'PRD_CLS_ID');
		} 	
	   if (document.form1.PRD_TYPE_ID.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_PRD_TYPE_ID','', 'PRD_TYPE_ID');
		}	
	   if (document.form1.PRD_BRAND_ID.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_PRD_BRAND_ID','', 'PRD_BRAND_ID');
		 }	
	   if (document.form1.LOC.value == ''){
			getLocalStorageValue('view_inv_list_by_Inventory_Product_LOC','','LOC');
		}
	   if (document.form1.LOC_TYPE_ID.value == ''){
			getLocalStorageValue('view_inv_list_by_Inventory_Product_LOC_TYPE_ID','','LOC_TYPE_ID');
		}
	   if (document.form1.LOC_TYPE_ID2.value == ''){
			getLocalStorageValue('view_inv_list_by_Inventory_Product_LOC_TYPE_ID2','','LOC_TYPE_ID2');
		}
	   if (document.form1.LOC_TYPE_ID3.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_LOC_TYPE_ID3','','LOC_TYPE_ID3');
		}
	   if (document.form1.BATCH.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_BATCH','','BATCH');
		}
	   if (document.form1.MODEL.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_MODEL','','MODEL');
		}
	   if (document.form1.UOM.value == ''){
		   getLocalStorageValue('view_inv_list_by_Inventory_Product_UOM','','UOM');
		}
// 	$('#table').dataTable({
// 		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
// 	});
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

	$('#ITEM_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEMDESC',  
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
			return '<p onclick="document.form1.ITEM.value = \''+data.ITEM+'\'">'+data.ITEMDESC+'</p>';
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

 });

 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>