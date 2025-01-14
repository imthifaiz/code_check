<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Product Details";
String currency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="dist/js/moment.min.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
   document.form.action  = "maint_item.jsp?action=NEW";
   document.form.submit();
}

function onAdd(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "maint_item.jsp?action=ADD";
   document.form.submit();
}
function onUpdate(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "maint_item.jsp?action=UPDATE";
   document.form.submit();
}
function onDelete(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "maint_item.jsp?action=DELETE";
   document.form.submit();
}
function onClear()
{
document.form.ITEM.value="";
document.form.DESC.value="";
document.form.ARTIST.selectedIndex=0;
//document.form.getElementByName("ARTIST").selectedIndex=0;
document.form.UOM.selectedIndex=0;
document.form.ASSET.value="";
document.form.REMARKS.value="";
document.form.MEDIUM.value="";
document.form.STKQTY.value="";
document.form.NETWEIGHT.value="";
document.form.GROSWEIGHT.value="";
document.form.HSCODE.value="";
document.form.COO.value="";
document.form.ITEM_CONDITION.value="";
document.form.TITLE.value="";
//document.form.ISPOSDISCOUNT.value="";
}

function onView(){
    var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "maint_item.jsp?action=VIEW";
   document.form.submit();
}
function onViewMapItem(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
   document.form.action  = "maint_item.jsp?action=VIEWMAPITEM";
   document.form.submit();
}
function onViewItemLoc(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
   document.form.action  = "maint_item.jsp?action=VIEWITEMLOC";
   document.form.submit();
}


</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
String sItemEnb   = "enabled";
String sViewMapItemEnb = "enabled",prdclsid="",clsdesc="",prdtypeid="",prd_brand_id = "",typedesc="",prd_cls_id="",DEPT_DISPLAY_ID="",LOC_ID="",prd_dept_id="",uom="";
String sItem      = "",sItemDesc  = "", sItemUom   = "", sReOrdQty  = "",price="",msprice="",cost="",nonstk="",nonstkid="",
action     = "",sRemark1   = "" , sRemark2  = "", sRemark3   = "",nonstkflag="",ISPARENTCHILD="", NETWEIGHT="", GROSSWEIGHT="", HSCODE="", COO="",
sItemCondition = "",sArtist="",loc="",sTitle="",sMedium="",sRemark="",nonstockflag="",PRODGST="",SUPPLIER="",CUSTOMER_TYPE_ID="",
nonstktypeid="",sUOM="",stkqty="",asset="",isActive="",maxstkqty="",posdiscount="",VINNO="",MODEL="",RENTALPRICE="",SERVICEPRICE="",PURCHASEUOM="",SALESUOM="",RENTALUOM="",SERVICEUOM="",INVENTORYUOM="",
Purchaseuom="",Salesuom="",Rentaluom="",Serviceuom="",Inventoryuom="",StockOnHand="",prddepid="",CommittedStock="",AvailableForSale="",QtyTBShipped="",DIMENSION="",
QtyTBReceived="",ReorderPoint="",Catalog="";

String discount="",OBDiscounttype="",IBDiscounttype="";
StrUtils strUtils = new StrUtils();
ItemUtil itemUtil = new ItemUtil();
DateUtils dateutils = new DateUtils();
PrdTypeUtil prdutil = new PrdTypeUtil();
PrdClassUtil prdcls = new PrdClassUtil();
PrdDeptUtil prddep = new PrdDeptUtil();
PrdBrandUtil prdbrandutil = new PrdBrandUtil();
InvUtil invUtil = new InvUtil();
DOUtil doUtil = new DOUtil();
POUtil poUtil = new POUtil();

itemUtil.setmLogger(mLogger);
prdutil.setmLogger(mLogger);
prdcls.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));

String plant=(String)session.getAttribute("PLANT");
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
String username=(String)session.getAttribute("LOGIN_USER");
sItem     = strUtils.fString(request.getParameter("ITEM"));
if(sItem.length() <= 0) sItem     = strUtils.fString(request.getParameter("ITEM1"));
sItemDesc      =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("DESC")));
System.out.println("sItemDesc 1 : " + sItemDesc);
List prdlist=prdutil.getPrdTypeList("",plant,"");
List prdBrandList=prdbrandutil.getPrdBrandList("",plant,"");
List prdclslst = prdcls.getPrdTypeList("",plant," ");
List prddeplst = prddep.getPrdTypeList("",plant," ");
List uomlist = itemUtil.getUomList(plant," ");
List nonstklst =   itemUtil.getNonStockList(plant,"");   
prd_cls_id=strUtils.fString(request.getParameter("PRD_CLS_ID"));
LOC_ID=strUtils.fString(request.getParameter("LOC_ID"));
prd_dept_id=strUtils.fString(request.getParameter("PRD_DEPT_ID"));
sArtist = strUtils.fString(request.getParameter("ARTIST"));
prd_brand_id=strUtils.fString(request.getParameter("PRD_BRAND_ID"));
// DEPT_DISPLAY_ID=strUtils.fString(request.getParameter("DEPT_DISPLAY_ID"));
price  = strUtils.fString(request.getParameter("PRICE"));
msprice = strUtils.fString(request.getParameter("MINSELLINGPRICE"));
cost= strUtils.fString(request.getParameter("COST"));
sTitle = strUtils.fString(request.getParameter("TITLE"));
//sMedium = strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("MEDIUM")));
sRemark      = strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("REMARKS")));
sItemCondition = strUtils.fString(request.getParameter("ITEM_CONDITION"));
sUOM = strUtils.fString(request.getParameter("UOM"));
stkqty = strUtils.fString(request.getParameter("STKQTY"));
isActive = strUtils.fString(request.getParameter("ISACTIVE"));
nonstkflag = StrUtils.fString(request.getParameter("NONSTKFLAG"));
nonstktypeid = StrUtils.fString(request.getParameter("NONSTKTYPEID"));
loc = "";
NETWEIGHT = StrUtils.fString(request.getParameter("NETWEIGHT"));
GROSSWEIGHT = StrUtils.fString(request.getParameter("GROSSWEIGHT"));
HSCODE = StrUtils.fString(request.getParameter("HSCODE"));
COO = StrUtils.fString(request.getParameter("COO"));
VINNO = StrUtils.fString(request.getParameter("VINNO"));
DIMENSION = StrUtils.fString(request.getParameter("DIMENSION"));
MODEL = StrUtils.fString(request.getParameter("MODEL"));
RENTALPRICE = StrUtils.fString(request.getParameter("RENTALPRICE"));
SERVICEPRICE = StrUtils.fString(request.getParameter("SERVICEPRICE"));
PURCHASEUOM = StrUtils.fString(request.getParameter("PURCHASEUOM"));
SALESUOM = StrUtils.fString(request.getParameter("SALESUOM"));
RENTALUOM = StrUtils.fString(request.getParameter("RENTALUOM"));
SERVICEUOM = StrUtils.fString(request.getParameter("SERVICEUOM"));
INVENTORYUOM = StrUtils.fString(request.getParameter("INVENTORYUOM"));
maxstkqty = StrUtils.fString(request.getParameter("MAXSTKQTY"));
posdiscount = StrUtils.fString(request.getParameter("POSDISCOUNT"));
PRODGST = StrUtils.fString(request.getParameter("PRODGST"));
System.out.println("action : " + action);
System.out.println("sItemDesc2 : " + sItemDesc);
sAddEnb    = "enabled";
      sItemEnb   = "enabled";
      StockOnHand = invUtil.getStockOnHandByProduct(sItem, plant);
      CommittedStock = StrUtils.addZeroes(Double.parseDouble(doUtil.getComittedStockByProduct(sItem, plant)),"3");
      AvailableForSale = StrUtils.addZeroes((Double.parseDouble(StockOnHand) - Double.parseDouble(CommittedStock)),"3");
      QtyTBShipped = StrUtils.addZeroes(Double.parseDouble(doUtil.getQtyToBeShpdByProduct(sItem, plant)),"3");
      QtyTBReceived = StrUtils.addZeroes(Double.parseDouble(poUtil.getQtyToBeRecvByProduct(sItem, plant)),"3");

  	PlantMstDAO plantMstDAO = new PlantMstDAO();
  	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    Catalog = StrUtils.fString(request.getParameter("CATALOG"));
    
    float pricef=0;float costf=0;float minspricef=0; float prodgstf=0;  float Rentalpricef=0; float ServicePricef=0;
	float netWeight =  "".equals(NETWEIGHT) ? 0.0f :  Float.parseFloat(NETWEIGHT);
	float grossWeight = "".equals(GROSSWEIGHT) ? 0.0f :Float.parseFloat(GROSSWEIGHT);
	float productVat = "".equals(PRODGST) ? 0.0f :Float.parseFloat(PRODGST);
	float listPrice = "".equals(price) ? 0.0f :Float.parseFloat(price);
	float costValue = "".equals(cost) ? 0.0f :Float.parseFloat(cost);
	float minSellingPriceValue = "".equals(msprice) ? 0.0f :Float.parseFloat(msprice);
	float RentalPrice = "".equals(RENTALPRICE) ? 0.0f :Float.parseFloat(RENTALPRICE);
	float ServicePrice = "".equals(SERVICEPRICE) ? 0.0f :Float.parseFloat(SERVICEPRICE);
	
	if(netWeight == 0f){
		NETWEIGHT="0.000";
	}if(grossWeight == 0f){
		GROSSWEIGHT="0.000";
	}if(productVat == 0f){
		PRODGST="0.000";
	}if(listPrice == 0f){
		price="0.00000";
	}if(costValue == 0f){
		cost="0.00000";
	}if(minSellingPriceValue == 0f){
		msprice="0.00000";
	}if(RentalPrice == 0f){
		RENTALPRICE="0.00000";
	}if(ServicePrice == 0f){
		SERVICEPRICE="0.00000";
	}
	
	
if(price.length()<0){
		price="0";
	}
	
	if(msprice.length()<0){
		msprice="0";
		}

if(cost.length()<0){
		cost="0";
	}
		
	if(PRODGST.length()<0&&PRODGST==null){
		PRODGST="0";
	}
	
	if(RENTALPRICE.length()<0){
		RENTALPRICE="0";
	}
	
	if(SERVICEPRICE.length()<0){
		SERVICEPRICE="0";
	}
	
	String empoutlet = "";
	if(!username.isEmpty()){
		          if (empoutlet.isEmpty()) {
		           ArrayList arrList = new ArrayList();
		           EmployeeDAO movHisDAO = new EmployeeDAO();
		           Hashtable htData = new Hashtable();
		            htData.put("PLANT", plant);
		             htData.put("EMPNO", username);
		               arrList = movHisDAO.getEmployeeDetails("OUTLET", htData, "");
		    
		               if (!arrList.isEmpty()) {
		                   Map m = (Map) arrList.get(0);
		       
		                   empoutlet = (String) m.get("OUTLET");
		        
		               }
		           }
		        }

	//Inventory Summary with Batch/Sno
	Hashtable ht = new Hashtable();
	ArrayList invQryList= new InvUtil().getInvListSummaryWithOutPriceMultiUom(ht,plant,sItem,"","","","","","");
// 	List CompanyOutletList= new OutletBeanDAO().getOutletByCompany(sItem);
	List CompanyOutletList= new OutletBeanDAO().getOutletByCompany(sItem,plant);
	List CompanyOutletMinMaxList = new OutletBeanDAO().getOutletByCompanyminmax(sItem,plant);
	List SupplierPurchaseCostList = new ItemSesBeanDAO().getSupplierCostList(sItem,plant);
	String Averagecost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(plant,PURCHASEUOM,sItem); 
	if(!empoutlet.isEmpty()) {
		CompanyOutletList= new OutletBeanDAO().getOutletByEmp(sItem,plant,empoutlet);
		CompanyOutletMinMaxList = new OutletBeanDAO().getOutletByCompanyminmaxEmp(sItem,plant,empoutlet);
	}
    
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span></a></li>                       
                <li><label>Product Details</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../product/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><font style="font-size:40px;"><%=res%></font></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form" method="post">
  
  <div class="col-sm-8">
<div class="row">
    <div class="form-group">      	
      	<label class="control-label col-form-label col-sm-3 required" for="Product ID">Product Id</label>
      	<div class="col-sm-4">
      	<div class="input-group">
    	<INPUT class="form-control" name="ITEM" readonly type = "TEXT" value="<%=sItem%>" size="20"  MAXLENGTH=20 <%=sItemEnb%> >
			   		<INPUT type = "hidden" name="ITEM1" value = <%=sItem%>>
  		</div>
  		</div>
  		<div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
  		<label class="radio-inline">
				      	<input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
				     	</label>
     	</div>
		</div>
        </div>
        <INPUT type="hidden" name="plant" value=<%=plant%>> 
 		<INPUT type="hidden" name="ITEM1" value=<%=sItem%>>
 		<INPUT type="hidden" name="CURRENCY" value=<%=currency%>>
 		
 	<div class="form-group">
      <label class="control-label col-form-label col-sm-3 required" for="Product Description">Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="DESC" type = "TEXT" value="<%=strUtils.forHTMLTag(sItemDesc)%>" size="20" readonly  MAXLENGTH=50>
      </div>
      <div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
 		<label class="radio-inline"> <input name="NONSTOCKFLAG" type="radio" <%if(nonstkflag.equalsIgnoreCase("N")) {%>  checked="checked" <%}%> 
													onclick="DisplayNonStkType();" value="N" disabled>Stock
												</label> 
		<label class="radio-inline"> <input name="NONSTOCKFLAG" type="radio" <%if(nonstkflag.equalsIgnoreCase("Y")) {%>  checked="checked" <%}%>
													onclick="DisplayNonStkType();" value="Y" disabled>Non Stock
												</label>
  		</div>
		</div>      
    </div>
    <% if(!COMP_INDUSTRY.equals("Retail")) {%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=strUtils.forHTMLTag(sRemark)%>"size="20" MAXLENGTH=100 readonly>
      	</div>
      	<div id="divNonStk" style="display:none;" >
      	<div class="form-inline">
      	<!-- <label class="control-label col-sm-3" style="padding-top: 5px;" id="nonstklbl" for="Stock Type">Non-Stock Type:</label> -->
      	<div class="col-sm-3" id="nonStktd">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="NONSTKTYPE" size="1" maxlength="9">
		<OPTION selected value="0">Choose </OPTION>
		<%       for (int i =0; i<nonstklst.size(); i++){
                 Map map = (Map) nonstklst.get(i);
                 nonstk     = (String) map.get("NONSTOCKDESC");
                 nonstkid = (String) map.get("NONSTKCODE");
        %>
		<OPTION value="<%=nonstkid%>"><%=nonstk%>
		</OPTION>
		<%}%>
		</select>
  		</div>
  	  	</div>
    	</div>
      
      </div>
      <%}else{ %>
          <div class="form-group" style="display:none;">
      <label class="control-label col-form-label col-sm-3" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=strUtils.forHTMLTag(sRemark)%>"size="20" MAXLENGTH=100 readonly>
      	</div>
      	<div id="divNonStk" style="display:none;" >
      	<div class="form-inline">
      	<!-- <label class="control-label col-sm-3" style="padding-top: 5px;" id="nonstklbl" for="Stock Type">Non-Stock Type:</label> -->
      	<div class="col-sm-3" id="nonStktd">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="NONSTKTYPE" size="1" maxlength="9">
		<OPTION selected value="0">Choose </OPTION>
		<%       for (int i =0; i<nonstklst.size(); i++){
                 Map map = (Map) nonstklst.get(i);
                 nonstk     = (String) map.get("NONSTOCKDESC");
                 nonstkid = (String) map.get("NONSTKCODE");
        %>
		<OPTION value="<%=nonstkid%>"><%=nonstk%>
		</OPTION>
		<%}%>
		</select>
  		</div>
  	  	</div>
    	</div>
      
      </div>
      <%} %>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3 required" for="Basic UOM">Base UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=sUOM%>" size="20" MAXLENGTH=100 id="Basicuom" placeholder="Select a UOM" readonly>        
      </div>
      <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISBASICUOM" value="ISBASICUOM" id="ISBASICUOM" onclick="isbasicuom();" disabled>
                     Apply to all UOM</label>
         </div>
	     </div>
	     
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Net Weight">Net Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>" style="width:100%" MAXLENGTH=100 readonly>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Gross Weight">Gross Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=100 readonly>				
      </div>
    </div>
       <%  if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { %>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Location">Location</label>
      <div class="col-sm-4" >                
			<INPUT class="form-control" name="LOC_ID" type="TEXT" value="<%=LOC_ID%>" size="20" MAXLENGTH=100 id="LOC_ID" readonly>
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prddepbtnpop"></i></span>				
      </div>
      </div>
       <% } else {%>
      <input type="hidden" name="LOC_ID" value="<%=LOC_ID%>">
      <% }%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Dimension">Dimension</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="DIMENSION" type="TEXT" value="<%=strUtils.forHTMLTag(DIMENSION)%>" style="width:100%" MAXLENGTH=50 readonly>				
      </div>
      <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" disabled> Allow POS Terminal Discount</label>
         </div>
	     </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Department">Product Department</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_DEPT_ID" type="TEXT" value="<%=prddepid%>" size="20" MAXLENGTH=100 id="PRD_DEPT_ID" readonly>
							
      </div>
      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Class">Product Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_CLS_ID" type="TEXT" value="<%=prdclsid%>" size="20" MAXLENGTH=100 id="PRD_CLS_ID" readonly>
							
      </div>
      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Type">Product Sub Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="ARTIST" type="TEXT" value="<%=sArtist%>" size="20" MAXLENGTH=100 id="ARTIST" readonly>			
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Brand">Product Brand</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_BRAND" type="TEXT" value="<%=prd_brand_id%>" size="20" MAXLENGTH=100 id="PRD_BRAND" readonly>				
      </div>
      
    </div>
    
    
     <div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-3" for="Department Display">Department Display</label>
      <div class="col-sm-4" >                
			<INPUT class="form-control" name="DEPT_DISPLAY_ID"  type="hidden" value="<%=DEPT_DISPLAY_ID%>" size="20" MAXLENGTH=100 id="DEPT_DISPLAY_ID" readonly>				
      </div>
      
    </div>
    
    <INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
    
    <div class="form-group" style="display:none">
      <label class="control-label col-form-label col-sm-3" for="Product VAT" id="TaxLabelOrderManagement"></label>
      <div class="col-sm-4">                
       <INPUT class="form-control" name="PRODGST" id="PRODGST" type="TEXT" value="<%=PRODGST%>" size="20" MAXLENGTH=50   readonly>
      	</div>
      	
    </div>
   
    
    </div>
    </div>
    
    <div class="col-sm-4 text-center">
		<div class="row">
			<div class="col-sm-12"  align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" name="CATALOGPATH" id="item_img" src="../jsp/dist/img/NO_IMG.png"
	     			style="width: 50%;float:revert;padding: 3px;">
			</div>
			
			<div class="form-group">         
        	<strong>Physical Stock</strong>        
    	</div>
    	
    	<div class="form-group">
	      	<label class="control-label col-form-label col-sm-6" for="StockOnHand">Stock On Hand</label>
	      	<div class="col-sm-6">          
	        	<INPUT class="form-control" name="StockOnHand" type="TEXT" value="<%=strUtils.forHTMLTag(StockOnHand)%>" style="width:100%" MAXLENGTH=100 readonly>
	      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-6" for="CommittedStock">Committed Stock</label>
      	<div class="col-sm-6">          
        <INPUT class="form-control" name="CommittedStock" type="TEXT" value="<%=strUtils.forHTMLTag(CommittedStock)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-6" for="AvailableForSale">Available for Sale</label>
      	<div class="col-sm-6">          
        <INPUT class="form-control" name="AvailableForSale" type="TEXT" value="<%=strUtils.forHTMLTag(AvailableForSale)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-6" for="QtyTBShipped">Quantity to be Shipped</label>
      	<div class="col-sm-6">          
        <INPUT class="form-control" name="QtyTBShipped" type="TEXT" value="<%=strUtils.forHTMLTag(QtyTBShipped)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-form-label col-sm-6" for="QtyTBReceived">Quantity to be Received</label>
      	<div class="col-sm-6">          
        <INPUT class="form-control" name="QtyTBReceived" type="TEXT" value="<%=strUtils.forHTMLTag(QtyTBReceived)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-form-label col-sm-6" for="ReorderPoint">Reorder Point</label>
      	<div class="col-sm-6">          
        <INPUT class="form-control" name="ReorderPoint" type="TEXT" value="<%=stkqty%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
	      		<label class="control-label col-form-label col-form-label col-sm-6" for="Dates"></label>
	      		<div class="col-sm-6">          
		        	<select class="form-control" data-toggle="dropdown" style="width:100%" data-placement="right" NAME="D-DATES" size="1" onchange="{getTotals(this.value);}">
						<OPTION selected="selected">This Week</OPTION>
						<OPTION>Previous Week</OPTION>
						<OPTION>This Month</OPTION>
						<OPTION>Previous Month</OPTION>	
						<OPTION>This Quarter</OPTION>
						<OPTION>Previous Quarter</OPTION>
						<OPTION>This Year</OPTION>
						<OPTION>Previous Year</OPTION>
				 	</select>
		      	</div>
	    	</div>
	    	
	    	<%-- <div class="form-group">
		      	<label class="control-label col-form-label col-form-label col-sm-6" for="TotalPurchase">Total Purchase</label>
		      	<div class="col-sm-6">  
		      		<div class="input-group">
			      		<INPUT class="form-control" name="TotalPurchase" id="TotalPurchase" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span>
		      		</div>
		      	</div>
	    	</div> 
	    	
	    	<div class="form-group">
		      	<label class="control-label col-form-label col-sm-6" for="TotalSale">Total Sale:</label>
		      	<div class="col-sm-6"> 
			      	<div class="input-group">         
			        	<INPUT class="form-control" name="TotalSale" id="TotalSale" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span>
			      	</div>
		      	</div>
		    </div>  --%>
	    	<div class="form-group">
		      	<label class="control-label col-form-label col-form-label col-sm-6" for="TotalPurchase">Total Purchase</label>
		      	<div class="col-sm-6">  
		      		<div class="input-group">
			      		<INPUT class="form-control" name="TotalPurchase" id="TotalPurchase" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<%-- <span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span> --%>
		      		</div>
		      	</div>
	    	</div> 
	    	
	    	<div class="form-group">
		      	<label class="control-label col-form-label col-sm-6" for="TotalSale">Total Sale:</label>
		      	<div class="col-sm-6"> 
			      	<div class="input-group">         
			        	<INPUT class="form-control" name="TotalSale" id="TotalSale" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<%-- <span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span> --%>
			      	</div>
		      	</div>
		    </div> 
			
		</div>
		
	</div>
    <div class="col-sm-12">
    <div class="bs-example">
	<ul class="nav nav-tabs" id="myTab" style="font-size: 94%;"> 
     	<li class="nav-item active">
            <a href="#other" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#purchase" class="nav-link" data-toggle="tab">Purchase</a>
        </li>
        <li class="nav-item">
            <a href="#sales" class="nav-link" data-toggle="tab">Sales</a>
        </li>
<!--         <li class="nav-item">
            <a href="#rental" class="nav-link" data-toggle="tab">Rental</a>
        </li> -->
        <li class="nav-item">
            <a href="#inventory" class="nav-link" data-toggle="tab">Inventory</a>
        </li>
        <li class="nav-item">
            <a href="#outletminmax" class="nav-link" data-toggle="tab">Outlet MinMax</a>
        </li>
        <% if(COMP_INDUSTRY.equals("Retail")) {%>
		<li class="nav-item">
            <a href="#outlets" class="nav-link" data-toggle="tab">Outlets List Price</a>
        </li>
        <li class="nav-item">
            <a href="#additionaldetail" class="nav-link" data-toggle="tab">Additional Detail Description</a>
        </li>
        <li class="nav-item">
            <a href="#catalogues" class="nav-link" data-toggle="tab">Catalogues</a>
        </li>
        <li class="nav-item">
            <a href="#prds" class="nav-link" data-toggle="tab">Additional Products</a>
        </li>
        <%} %>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
        
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="other">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="HS Code">HS Code</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=strUtils.forHTMLTag(HSCODE)%>" MAXLENGTH=100 readonly>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="COO">COO</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="COO" type="TEXT" value="<%=strUtils.forHTMLTag(COO)%>" MAXLENGTH=100 readonly>				
      </div>
      <div class="col-sm-1">
       <span class="input-group-btn"></span>
    		<input name="COOCURRENCY" type="TEXT" value="" size="10" MAXLENGTH=50  class="form-control" readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="VINNO">VIN NO</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="VINNO" type="TEXT" value="<%=strUtils.forHTMLTag(VINNO)%>" MAXLENGTH=100 readonly>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="MODEL">MODEL</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="MODEL" type="TEXT" value="<%=strUtils.forHTMLTag(MODEL)%>" MAXLENGTH=100 readonly>				
      </div>
    </div>
        
        </div>
        
        <div class="tab-pane fade" id="purchase">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Purchase UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="PURCHASEUOM" type="TEXT" value="<%=PURCHASEUOM%>" size="20" MAXLENGTH=100 id="purchaseuom" readonly>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Cost">Cost</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="COST" id="COST" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(cost), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)" readonly>				
      </div>
      <div class="form-inline">
      <label class="control-label col-form-label col-sm-1" for="Cost">Average Cost</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="AVGCOST" id="AVGCOST" type="TEXT" value="<%=Averagecost%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)" readonly>				
      </div>
      </div>
    </div>
    
	    <div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="supplier name">PO Estimate Order Supplier</label>
        			<div class="col-sm-4">
        					<input type="hidden" name="vendno" value="" >
        					<input type = "hidden" name="custModal">
    		 				<INPUT class=" form-control" id="vendname" value="" name="vendname" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select PO Estimate Order Supplier" readonly>    		 				  						
        			</div>
        			<!-- <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" disabled> Allow POS Terminal Discount</label>
         </div>
	     </div> -->
				</div>
	
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Item Supplier Discount">Secondary Supplier</label>
      <div class="col-sm-4">
			<TABLE id="multiitemsup">
		<TR>
		<TD align="center"><div class="input-group"><INPUT class="form-control vendorSearch" name="ITEMSUPPLIER_0" id="ITEMSUPPLIER_0" readonly placeholder="Select Secondary Supplier"  type = "TEXT" value="" size="55"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.ITEMSUPPLIER_0.value.length > 0)){validateItemSupplier(0);}" readonly>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>
    
    
    <INPUT type="hidden" name="DYNAMIC_SUPPLIERDISCOUNT_SIZE">
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier Discount">Supplier Discount</label>
      <div class="col-sm-4">
			<label class="radio-inline">
      	<INPUT name="IBDISCOUNT" type = "radio"  value="BYCOST"  id="BYCOST" <%if(IBDiscounttype.equalsIgnoreCase("BYCOST")) {%>  checked="checked" <%}%> disabled >By Cost
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(IBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> disabled>By Percentage
    	</label>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier Discount"></label>
      <div class="col-sm-4">
			<TABLE id="supplierDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_SUPPLIER_DISCOUNT_0" id="DYNAMIC_SUPPLIER_DISCOUNT_0" type="TEXT" value="" readonly
		size="20" MAXLENGTH="50"  onkeypress="return isNumberKey(event,this,4)" />&nbsp;</TD>
		<TD align="center"><div class="input-group"><INPUT class="form-control supplierSearch" name="SUPPLIER_0" id="SUPPLIER_0"  type = "TEXT" value="<%=SUPPLIER%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.SUPPLIER_0.value.length > 0)){validateSupplier(0);}"  readonly>
		<INPUT type="hidden" name="DYNAMIC_SUPPLIER_DISCOUNT_SIZE">        
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>   
    
  <!--  <div class="col-sm-4 text-center"> -->
 <div class="form-group">
    <label class="control-label col-form-label col-sm-2" for="Supplier Purchase Cost List">Supplier Purchase Cost List</label>
    
     <div class="col-sm-4" style="margin: 0px; width: 34%; margin-left: -2px;">
					<table class="table table-bordered line-item-table Suppliercost-table">
						<thead>
							<tr>
								
								<th>Supplier</th>
								<th>Cost</th>
							</tr>
						</thead>
						<%if(!SupplierPurchaseCostList.isEmpty()){ %>
						<tbody>
						<%for(int i =0; i<SupplierPurchaseCostList.size(); i++) {
							Map arrCurrLine = (Map)SupplierPurchaseCostList.get(i);
		                    Hashtable htData = new Hashtable();	
		                    String sCUSTNAME = (String)arrCurrLine.get("CustName");
		                    String sUNITCOST = (String)arrCurrLine.get("UNITCOST");
		                    //String maxqty = (String)arrCurrLine.get("MAXQTY");
		                    
		                   /*  String plntdesc="select PLNTDESC FROM PLNTMST WHERE PLANT ='"+cplant+"'";
	         	          	ArrayList plntdescList = new ItemMstUtil().selectForReport(plntdesc,htData,"");
	         	          	Map plnt = new HashMap();
	         	          	if (plntdescList.size() > 0) {
	         	        	  plnt = (Map) plntdescList.get(0);
	         	        	 	plntdesc = StrUtils.fString((String)plnt.get("PLNTDESC")) ;
	         	          	} */
						%>
							<tr>
								
								<td class="text-center">
									<input class="form-control text-left" name="CUSTNAMEPO" type="text" value="<%= sCUSTNAME %>" placeholder="Supplier" autocomplete="off" readonly>
								<td class="text-center">
									<input class="form-control text-left" name="UNITCOSTPO" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(sUNITCOST), numberOfDecimal)%>" placeholder="Cost" autocomplete="off" readonly></td>
							</tr>
						<%} %>
						</tbody>
						<%}else{%>
						<tbody>
						</tbody>
						<%}%>
					</table>
			</div>
    
    </div>
    <!-- </div> -->
        
        </div>
        
        <div class="tab-pane fade" id="sales">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Sales UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="SALESUOM" type="TEXT" value="<%=SALESUOM%>" size="20" MAXLENGTH=100 id="salesuom"  readonly>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="List Price">List Price</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="PRICE" id="PRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(price), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="javascript:enableSellingPrice(this.value);" onkeypress="return isNumberKey(event,this,4)"  readonly>				
      </div>
    </div>
    
    
    <div class="form-group">
      <!-- <label class="control-label col-form-label col-sm-2" for="List Price">Is Combination Product</label>
      <div class="form-inline">
	     <div class="col-sm-2">
	     	<label class="checkbox-inline" style="margin-bottom: 16px;"><INPUT Type=Checkbox  name ="ISCOMPRO" style="border:0;" value="0" id="ISCOMPRO" onclick="iscombpro();" disabled="disabled"></label>
         </div>
	   </div> -->
      
      <!-- <div class="col-sm-4">
      		<INPUT Type=Checkbox  name ="ISCOMPRO" value="0" id="ISCOMPRO" onclick="iscombpro();">
      </div> -->
    </div>
    
             <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Product Type</label>
      <div class="col-sm-6">
      	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked" disabled="disabled">None</label>
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO" disabled="disabled">Is Finished Product</label>				
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI" disabled="disabled">Is Semi Finished Product</label>		
      </div>
     </div>
    
     <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-2 required">Product Price Increase</label>
      <div class="col-sm-6">
			<label class="radio-inline">
      	<INPUT type = "radio"  name="CPPI" type = "radio"  value="BYPRICE"  id="CPPIBYPRICE" checked="checked" disabled="disabled">By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT type = "radio" name="CPPI" type = "radio" value="BYPERCENTAGE"  id = "CPPIBYPERCENTAGE" disabled="disabled">By Percentage
    	</label>
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked" disabled="disabled">None</label> -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO" disabled="disabled">Is Finished Product</label>				 -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI" disabled="disabled">Is Semi Finished Product</label>				 -->
      </div>
    </div>
     <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount">Product Price Increased Value</label>
      <div class="col-sm-3">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICE" id="INCPRICE" style="width: 115%;" onchange="isNumCheck();" readonly>
      </div>
      <div class="col-sm-1">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICEUNIT" id="INCPRICEUNIT" value="" readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Minimum Selling Price">Minimum Selling Price</label>
      <div class="col-sm-4">
			<INPUT  class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(msprice), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)"  readonly>				
      </div>
    </div>
    
    <!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Discount POS(%)">Discount POS(%):</label>
      	<div class="col-sm-3">       -->    
       	<INPUT class="form-control" name="DISCOUNT" id="DISCOUNT" type="hidden" value="<%=discount%>"
		size="20" MAXLENGTH=50>
      	<!-- </div>
    	</div> -->
     
     <INPUT type="hidden" name="DYNAMIC_CUSTOMERDISCOUNT_SIZE">
     
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount">Customer Discount</label>
      <div class="col-sm-4">
			<label class="radio-inline">
      	<INPUT name="OBDISCOUNT" type = "radio"  value="BYPRICE"  id="BYPRICE" <%if(OBDiscounttype.equalsIgnoreCase("BYPRICE")) {%>  checked="checked" <%}%> disabled >By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="OBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(OBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> disabled >By Percentage
    	</label>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount"></label>
      <div class="col-sm-4">
			<TABLE id="customerDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT_0" id="DYNAMIC_CUSTOMER_DISCOUNT_0" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"
		size="20" MAXLENGTH="50" readonly />&nbsp;</TD>
		<TD align="center"><div class="input-group"><INPUT class="form-control customerSearch" name="CUSTOMER_TYPE_ID_0" id="CUSTOMER_TYPE_ID_0"  type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.CUSTOMER_TYPE_ID_0.value.length > 0)){validateCustomerType(0);}" readonly>
		<INPUT type="hidden" name="DYNAMIC_CUSTOMER_DISCOUNT_SIZE">        
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>
      
        
        </div>
        
        <div class="tab-pane fade" id="rental">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Rental UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="RENTALUOM" type="TEXT" value="<%=RENTALUOM%>" size="20" MAXLENGTH=100 id="rentaluom" readonly>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Rental Price">Rental Price</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="RENTALPRICE" id="RENTALPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(RENTALPRICE), numberOfDecimal)%>" size="20" onkeypress="return isNumberKey(event,this,4)"
		MAXLENGTH=50 onchange="validDecimal(this.value)" readonly>				
      </div>
    </div>
        
        </div>
        
        <div class="tab-pane fade" id="inventory">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Inventory UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="INVENTORYUOM" type="TEXT" value="<%=INVENTORYUOM%>" size="20" MAXLENGTH=100 id="inventoryuom" readonly>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Min Stock Quantity">Min Stock Quantity</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="STKQTY" type="TEXT" value="<%=stkqty%>" size="20" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)" readonly>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Max Stock Quantity">Max Stock Quantity</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="MAXSTKQTY" type="TEXT" value="<%=maxstkqty%>"	size="20" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)" readonly>				
      </div>
    </div>
        <INPUT type="hidden" name="LOC_0" id="LOC_0" >
        
        <div class="row" style="margin: 0px;">
              	<table class="table table-bordered">
					<thead>
		                <tr role="row">
		                <th style="font-size: smaller;">LOCATION</th>  
		                <th style="font-size: smaller;">BATCH</th>  
		                <th style="font-size: smaller;">PCS/EA/PKT/KG/LT UOM</th>  
		                <th style="font-size: smaller;">PCS/EA/PKT/KG/LT QTY</th>
						<th style="font-size: smaller;">INV.UOM</th> 
						<th style="font-size: smaller;">INV.QTY</th> 		                  
       </tr>
       </thead>
       <tbody>
						<%
						double dTotalQty=0, invdTotalQty=0;
						if (invQryList.size() > 0) {
					    	for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
					    		 Map lineArr = (Map) invQryList.get(iCnt);
					    		 String qtyValue =(String)lineArr.get("QTY");
					    		 String invqtyValue =(String)lineArr.get("INVUOMQTY");
					    		 float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
					    		 float invqtyVal ="".equals(invqtyValue) ? 0.0f :  Float.parseFloat(invqtyValue);
					    		 
					    		 double qtvar = Double.parseDouble(qtyValue);
					    		 dTotalQty += qtvar;
					 			 qtyValue = StrUtils.addZeroes(qtvar,DbBean.NOOFDECIMALPTSFORWEIGHT);
					 			 
					 			double invqtvar = Double.parseDouble(invqtyValue);
					 			invqtyValue = StrUtils.addZeroes(invqtvar,DbBean.NOOFDECIMALPTSFORWEIGHT);
					 			invdTotalQty += invqtvar;
					    		%>
					    		<tr>
								<td><%=strUtils.fString((String)lineArr.get("LOC"))%></td>
								<td><%=strUtils.fString((String)lineArr.get("BATCH"))%></td>
								<td><%=strUtils.fString((String)lineArr.get("STKUOM"))%></td>
								<td class="text-right"><%=qtyValue%></td>
								<td><%=strUtils.fString((String)lineArr.get("INVENTORYUOM"))%></td>
								<td class="text-right"><%=invqtyValue%></td>
								<%
					    	}
					    }
					%>
					<tr>
						<td></td>
						<td></td>
						<td style="font-weight:bold">Total</td>
						<td style="font-weight:bold" class="text-right"><%=Numbers.toMillionFormat(dTotalQty, DbBean.NOOFDECIMALPTSFORWEIGHT)%></td>
						<td></td>
						<td style="font-weight:bold" class="text-right"><%=Numbers.toMillionFormat(invdTotalQty, DbBean.NOOFDECIMALPTSFORWEIGHT)%></td>
						</tr>
					</tbody>
				</table>
					</div>
  </div>
  
  <div class="tab-pane fade" id="additionaldetail">
        <br>
     
    
    <div class="form-group">
        <TABLE id="descriptiontbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD>
		<label for="Description 1">Detail Description 1</label></TD>		
		<TD style="width: 85%;" align="center"><div class="col-sm-10"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 650px;" aria-hidden="true" onclick="deleteRowDesc('descriptiontbl');return false;"></span>
		<INPUT class="form-control" name="DESCRIPTION0" id="DESCRIPTION0"  placeholder="Max 1000 Characters" type = "TEXT" value="" size="100"  MAXLENGTH=1000>		        
       	</div>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="DESCRIPTION_SIZE" >  		
       
    </div>
        </div>
        
        <div class="tab-pane fade" id="catalogues">
        <br>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image" style="font-weight: bold;">Product Image 1</label>
      		<div class="col-sm-2" style="right: 100px;" align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" id="item_img1" src="../jsp/dist/img/NO_IMG.png" style="width: 50%;float:revert;padding: 3px;">
			</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image" style="font-weight: bold;">Product Image 2</label>
      		<div class="col-sm-2" style="right: 100px;" align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" id="item_img2" src="../jsp/dist/img/NO_IMG.png" style="width: 50%;float:revert;padding: 3px;">
			</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image" style="font-weight: bold;">Product Image 3</label>
      		<div class="col-sm-2" style="right: 100px;" align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" id="item_img3" src="../jsp/dist/img/NO_IMG.png" style="width: 50%;float:revert;padding: 3px;">
			</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image" style="font-weight: bold;">Product Image 4</label>
      		<div class="col-sm-2" style="right: 100px;" align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" id="item_img4" src="../jsp/dist/img/NO_IMG.png" style="width: 50%;float:revert;padding: 3px;">
			</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image" style="font-weight: bold;">Product Image 5</label>
      		<div class="col-sm-2" style="right: 100px;" align="center">				
				<img alt="new image" class="img-thumbnail img-responsive col-sm-3" id="item_img5" src="../jsp/dist/img/NO_IMG.png" style="width: 50%;float:revert;padding: 3px;">
			</div>
    	</div>
        </div>
        
                
        <div class="tab-pane fade" id="prds">
        <br>
    <div class="form-group">
        <TABLE id="prdtbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD>
		<label for="Product 1">Product 1</label></TD>		
		<TD align="center" style="width: 90%;"><div class="col-sm-4"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 500px;" aria-hidden="true" onclick="deleteRowPrd('prdtbl');return false;"></span>
		<INPUT class="form-control itemSearch" name="PRODUCT0" id="PRODUCT0"  placeholder="Select Product" type = "TEXT" value="" size="100"  MAXLENGTH=200>		        
       	</div>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="PRD_SIZE" >  		
       
    </div>
        </div>
        
        <div class="tab-pane fade" id="outletminmax">
        <br>
        	
         <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table outlet-table">
						<thead>
							<tr>
								
								<th>Company</th>
								<th>Outlet</th>
								<th>Current MinQty</th>
								<th>Current MaxQty</th>
							</tr>
						</thead>
						<%if(!CompanyOutletMinMaxList.isEmpty()){ %>
						<tbody>
						<%for(int i =0; i<CompanyOutletMinMaxList.size(); i++) {
							Map arrCurrLine = (Map)CompanyOutletMinMaxList.get(i);
		                    Hashtable htData = new Hashtable();	
		                    String cplant = (String)arrCurrLine.get("PLANT");
		                    String minqty = (String)arrCurrLine.get("MINQTY");
		                    String maxqty = (String)arrCurrLine.get("MAXQTY");
		                    
		                    String plntdesc="select PLNTDESC FROM PLNTMST WHERE PLANT ='"+cplant+"'";
	         	          	ArrayList plntdescList = new ItemMstUtil().selectForReport(plntdesc,htData,"");
	         	          	Map plnt = new HashMap();
	         	          	if (plntdescList.size() > 0) {
	         	        	  plnt = (Map) plntdescList.get(0);
	         	        	 	plntdesc = StrUtils.fString((String)plnt.get("PLNTDESC")) ;
	         	          	}
						%>
							<tr>
								
								<td class="text-center">
									<input class="form-control text-left" name="childcompany" type="text" value="<%= plntdesc %>" placeholder="Company" autocomplete="off" readonly>
									<input class="form-control" name="childcompanyplant" type="hidden" value="<%= (String)arrCurrLine.get("PLANT") %>" ></td>
								<td class="text-center">
									<input class="form-control text-left" name="childoutlet" type="text" value="<%= (String)arrCurrLine.get("OUTLET") %>" placeholder="Outlet" autocomplete="off" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" name="MINQTY" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(minqty), numberOfDecimal)%>" placeholder="MIN QTY" autocomplete="off" readonly></td>
								<td class="text-center">
<!-- 									<input class="form-control text-left" name="newprice" type="text" value="0.00" placeholder="New Price" autocomplete="off" onchange="checkprice(this)" onkeypress="return isNumberKey(event,this,4)"></td> -->
									<input class="form-control text-left" name="MAXQTY" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(maxqty), numberOfDecimal)%>"  placeholder="MAX QTY" autocomplete="off" readonly>
								</td>
									
							</tr>
						<%} %>
						</tbody>
						<%}else{%>
						<tbody>
						</tbody>
						<%}%>
					</table>
			</div>
        </div>
  
  <div class="tab-pane fade" id="outlets">
        <br>
        
         <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table outlet-table">
						<thead>
							<tr>
								<th>Company</th>
								<th>Outlet</th>
								<th>Current List Price</th>
							</tr>
						</thead>
						<%if(!CompanyOutletList.isEmpty()){ %>
						<tbody>
						<%for(int i =0; i<CompanyOutletList.size(); i++) {
							Map arrCurrLine = (Map)CompanyOutletList.get(i);
		                    Hashtable htData = new Hashtable();	
		                    String cplant = (String)arrCurrLine.get("PLANT");
		                    String outletprice = (String)arrCurrLine.get("UNITPRICE");
		                    String plntdesc="select PLNTDESC FROM PLNTMST WHERE PLANT ='"+cplant+"'";
	         	          	ArrayList plntdescList = new ItemMstUtil().selectForReport(plntdesc,htData,"");
	         	          	Map plnt = new HashMap();
	         	          	if (plntdescList.size() > 0) {
	         	        	  plnt = (Map) plntdescList.get(0);
	         	        	 	plntdesc = StrUtils.fString((String)plnt.get("PLNTDESC")) ;
	         	          	}
						%>
							<tr>
								<td class="text-center">
									<input class="form-control text-left" name="childcompany" type="text" value="<%= plntdesc %>" placeholder="Select HS code" autocomplete="off" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" name="childoutlet" type="text" value="<%= (String)arrCurrLine.get("OUTLET") %>" placeholder="Outlet" autocomplete="off" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" name="currentprice" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(outletprice), numberOfDecimal)%>" placeholder="Current Price" autocomplete="off" readonly></td>
							</tr>
						<%} %>
						</tbody>
						<%}else{%>
						<tbody>
						</tbody>
						<%}%>
					</table>
			</div>
        </div>
        
        <div class="tab-pane fade" id="remark">
		<br>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2" for="Remarks1">Remarks1</label>
			<div class="col-sm-4">				
				<textarea  class="form-control" name="ITEM_CONDITION" placeholder="Enter Remarks1" MAXLENGTH=100 readonly><%=sItemCondition%></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2" for="Remarks2">Remarks2</label>
			<div class="col-sm-4">				
				<textarea  class="form-control" name="TITLE" placeholder="Enter Remarks2"  MAXLENGTH=100 readonly><%=sTitle%></textarea>
			</div>
		</div>

		</div>
		
        </div> <!-- div end for tab -->
        
		<!-- </div>
    <div class="col-sm-6">
   
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Service</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-6" for="Service UOM">Service UOM:</label>
      	<div class="col-sm-6">           	 -->
  	<%-- 	<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="PURCHASEUOM" disabled="disabled" size="1" maxlength="9">
		<OPTION selected  value="0">Choose </OPTION> 
       	<%       for (int i =0; i<uomlist.size(); i++){
  				 Map map = (Map) uomlist.get(i);
            	 Serviceuom         = (String) map.get("stkuom");%>
    	<OPTION  value="<%=Serviceuom%>" <%if(SERVICEUOM.equalsIgnoreCase(Serviceuom)){%>selected<%}%>><%=Serviceuom%> </OPTION><%}%>
		</select>
  		</div>
  	  	</div> --%>
  	  	  	  	
  	  	<!-- <div class="form-group">
      	<label class="control-label col-sm-6" for="Service Price">Service Price:</label>
      	<div class="col-sm-6">  -->  
      	<INPUT class="form-control" name="SERVICEUOM" value="<%=Serviceuom%>" type="hidden" v size="20" MAXLENGTH=50 readonly>       
        <INPUT class="form-control" name="SERVICEPRICE" id="SERVICEPRICE" type="hidden" value="<%=new java.math.BigDecimal(SERVICEPRICE).toPlainString()%>" size="20" MAXLENGTH=50 readonly>
      	<!-- </div>
    	</div>
    </div>
    </div>
    </div>
    </div> -->	
		
        </div>
    	
    	</div>
    	</div>    
  
  
  </form>
		</div>
 		</div>
 		</div>
 
<script>
function onPrint(){
   var ITEM   = document.form.ITEM.value;
   var ITEMDESC = document.form.DESC.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Product ID"); document.form.ITEM.focus(); return false; }

//   document.form.action  = "item_view.jsp?action=PRINT";
  document.form.action  = "PrintItemLabel.jsp?action=PRINT&Item="+ITEM+"&ITEMDESC="+ITEMDESC;
   alert("Do you want to Print?");
   document.form.submit();
}
</script>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    
    var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Product " + d +" (%):";
    if(document.form.ITEM.value!="")
	{
    getProductDetails();
    getProductDetail();
	}
    getCurrentServerTime();
    doRestOfInitialization();
});


function getProductDetails() {
	var productId = document.form.ITEM.value;
	if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
                        async:false ,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					//debugger;
					if (data.status == "100") {
                                        var resultVal = data.result;
                                        var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
                                        
                                        if(resultVal.netweight == null|| resultVal.netweight == 0 ){
                                        	document.form.NETWEIGHT.value="0.000";
                                        }else{
                                        	document.form.NETWEIGHT.value=resultVal.netweight.match(regex)[0];
                                        }if(resultVal.grossweight == null || resultVal.grossweight == 0 ){
                                        	document.form.GROSSWEIGHT.value="0.000";
                                        }else{
                                        	document.form.GROSSWEIGHT.value=resultVal.grossweight.match(regex)[0];
                                        }if(resultVal.PRODGST == null || resultVal.PRODGST == 0 ){
                                        	document.form.PRODGST.value="0.000";
                                        }else{
                                        	document.form.PRODGST.value=parseFloat(resultVal.PRODGST.match(regex)[0]).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORTAX%>);
                                        }if(resultVal.price == null || resultVal.price == 0 ){
                                        	document.form.PRICE.value=addZeroes(parseFloat("0").toFixed(<%=numberOfDecimal%>));
                                        }else{
                                        	/* document.form.PRICE.value=parseFloat(resultVal.price.match(regex)[0]).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>); */
                                        	document.form.PRICE.value=addZeroes(parseFloat(resultVal.price.match(regex)[0]).toFixed(<%=numberOfDecimal%>));
                                        }if(resultVal.cost == null || resultVal.cost == 0 ){
                                        	document.form.COST.value=addZeroes(parseFloat("0").toFixed(<%=numberOfDecimal%>));
                                        }else{
                                        	/* document.form.COST.value=parseFloat(resultVal.cost.match(regex)[0]).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>); */
                                        	document.form.COST.value=addZeroes(parseFloat(resultVal.cost.match(regex)[0]).toFixed(<%=numberOfDecimal%>));
                                        }if(resultVal.minsprice == null || resultVal.minsprice == 0 ){
                                        	document.form.MINSELLINGPRICE.value=addZeroes(parseFloat("0").toFixed(<%=numberOfDecimal%>));
                                        }else{
                                        	document.form.MINSELLINGPRICE.value=addZeroes(parseFloat(resultVal.minsprice.match(regex)[0]).toFixed(<%=numberOfDecimal%>));
                                        }
                                                 document.form.DESC.value = resultVal.sItemDesc;
                                                 document.form.BYCOST.checked=true;
                                                 document.form.BYPRICE.checked=true;
                                                   document.form.ARTIST.value=resultVal.sArtist;
                                                  //  Start code added by Deen for product brand on 11/9/12 
                                                   document.form.PRD_BRAND.value=resultVal.brand;
                                                  //  End code added by Deen for product brand on 11/9/12 
                                                   document.form.UOM.value=resultVal.sUOM; 
                                                   document.form.TITLE.value=resultVal.sTitle;
                                                  // document.form.MANUFACT.value=resultVal.sMedium;
                                                   document.form.ITEM_CONDITION.value=resultVal.sItemCondition;
                                                   document.form.REMARKS.value=resultVal.sRemark;
                                                   document.form.STKQTY.value=resultVal.stkqty;
                                                   document.form.PRD_CLS_ID.value=resultVal.prd_cls_id;
                                                   document.form.vendno.value=resultVal.vendno;
                                                   document.form.vendname.value=resultVal.vendname;
                                                   document.form.PRD_DEPT_ID.value=resultVal.prd_dept_id; 
                                                   document.form.LOC_ID.value=resultVal.LOC_ID; 
//                                                    document.form.DEPT_DISPLAY_ID.value=resultVal.DEPT_DISPLAY_ID; 
                                                   document.form.DISCOUNT.value=resultVal.discount;
                                                   document.form.LOC_0.value=resultVal.loc;
                                                   document.form.NONSTKTYPE.value=resultVal.nonstktypeid;
                                                   document.form.MAXSTKQTY.value=resultVal.maxstkqty;
                                                   document.form.HSCODE.value=resultVal.hscode;
                                                   document.form.COO.value=resultVal.coo;
                                                   document.form.VINNO.value=resultVal.vinno;
                                                   document.form.DIMENSION.value=resultVal.dimension;
                                                   document.form.MODEL.value=resultVal.model;
                                                   
                                                   
												   if(resultVal.RentalPrice == null || resultVal.RentalPrice == 0 ){
                                                   	document.form.RENTALPRICE.value=addZeroes(parseFloat("0").toFixed(<%=numberOfDecimal%>));
                                                   }else{
                                                   	/* document.form.RENTALPRICE.value=parseFloat(resultVal.RentalPrice.match(regex)[0]).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>); */
                                                	   document.form.RENTALPRICE.value=addZeroes(parseFloat(resultVal.RentalPrice.match(regex)[0]).toFixed(<%=numberOfDecimal%>));
                                                   }
                                                   if(resultVal.ServicePrice == null || resultVal.ServicePrice == 0 ){
                                                      	document.form.SERVICEPRICE.value=addZeroes(parseFloat("0").toFixed(<%=numberOfDecimal%>));
                                                      }else{
                                                      	/* document.form.SERVICEPRICE.value=parseFloat(resultVal.ServicePrice.match(regex)[0]).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>)*/;
                                                      	document.form.SERVICEPRICE.value=addZeroes(parseFloat(resultVal.ServicePrice.match(regex)[0]).toFixed(<%=numberOfDecimal%>));
                                                      }
                                                   document.form.PURCHASEUOM.value=resultVal.PurchaseUOM;
                                                   document.form.SALESUOM.value=resultVal.SalesUOM;
                                                   document.form.RENTALUOM.value=resultVal.RentalUOM;
                                                   document.form.SERVICEUOM.value=resultVal.ServiceUOM;
                                                   document.form.INVENTORYUOM.value=resultVal.InventoryUOM;
                                                   
                                                   
                                                   document.form.ISCOMPRO.value=resultVal.iscompro;
                                                   if(resultVal.iscompro == "1"){
                                                	   $('#ISCOMPRO').prop('checked', true);
                                                	   $('#ISCOMPRO_SEMI').prop('checked', false);
                                                	   $('#NONE').prop('checked', false);
                                                	   $(".comproprice").show();
                                                   }else if(resultVal.iscompro == "2"){
                                                	   $('#ISCOMPRO').prop('checked', false);
                                                	   $('#ISCOMPRO_SEMI').prop('checked', true);
                                                	   $('#NONE').prop('checked', false);
                                                	   $(".comproprice").show();                                                   
                                                   }else{
                                                	   $('#ISCOMPRO').prop('checked', false);
                                                	   $('#ISCOMPRO_SEMI').prop('checked', false);
                                                	   $('#NONE').prop('checked', true);
                                                	   /* $(".comproprice").hide(); */ 
                                                   }
                                                   
                                                   if(resultVal.cppi == "BYPRICE"){
                                                	   $('#CPPIBYPRICE').prop('checked', true);
                                                   }else if(resultVal.cppi == "BYPERCENTAGE"){
                                                	   $('#CPPIBYPERCENTAGE').prop('checked', true);
                                                   }else{
                                                	   $('#CPPIBYPRICE').prop('checked', true);
                                                   }
                                                  
                                                    document.form.INCPRICE.value=resultVal.incprice;
                                                    if(resultVal.incpriceunit == "0"){
                                                    	document.form.INCPRICEUNIT.value="<%=currency%>"
                                                    }else{
                                                    	 document.form.INCPRICEUNIT.value=resultVal.incpriceunit;
                                                    }
                                                   
                                                   if(resultVal.ISBASICUOM == 1){
                                                	   document.form.ISBASICUOM.checked = true;
                                                   }else if(resultVal.ISBASICUOM == 0){
                                                	   document.form.ISBASICUOM.checked = false;
                                                   }
                                                   if(resultVal.ISPOSDISCOUNT == 1){
                                                	   document.form.ISPOSDISCOUNT.checked = true;
                                                   }else if(resultVal.ISPOSDISCOUNT == 0){
                                                	   document.form.ISPOSDISCOUNT.checked = false;
                                                   }
                                                   setCheckedValue( document.form.ACTIVE,resultVal.isActive);
                                                   //setCheckedValue( document.form.rdoparentchild,resultVal.ISPARENTCHILD);
                                                   setCheckedValue( document.form.NONSTOCKFLAG,resultVal.nonstkflg);
												   DisplayNonStkType();
                                                   //loadAlternateItemNames(productId);
                                                   loadCustomerDiscount(resultVal.sItem);
                                                   loadSupplierDiscount(resultVal.sItem);
                                                   loadItemSupplier(resultVal.sItem);
                                                   loadDetailDesc(resultVal.sItem);
                                                   loadAddPrd(resultVal.sItem);
												   if(resultVal.coo != ""){
                                                  		cooCountryCurrency(resultVal.coo);
                                                   }else{
                                                   	document.form.COOCURRENCY.value="";
                                                    }
                                                   
                                                 if(resultVal.catalogpath=="")
                                                	 $("#item_img").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath);
                         
					}
				}
			});
		}
	}
	
function getProductDetail() {
	var productId = document.form.ITEM.value;
	if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async:false ,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				ACTION : "GET_PRODUCT_IMG_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                                        var resultVal = data.result;
                                                 if(resultVal.catalogpath1=="")
                                                	 $("#item_img1").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img1").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath1);
                                                 
                                                 if(resultVal.catalogpath2=="")
                                                	 $("#item_img2").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img2").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath2);
                                                 
                                                 if(resultVal.catalogpath3=="")
                                                	 $("#item_img3").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img3").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath3);
                                                 
                                                 if(resultVal.catalogpath4=="")
                                                	 $("#item_img4").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img4").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath4);
                                                 
                                                 if(resultVal.catalogpath5=="")
                                                	 $("#item_img5").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img5").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath5);
                         
					}
				}
			});
		}
	}

var currentServerTime, tableTopIssuedProducts, tableTopReceivedProducts, tableExpiringProducts, tableLowStockProducts;

function getCurrentServerTime(){
	var dataURL = '../DashboardServlet?ACTION=CURRENT_DATE';
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		currentServerTime = data.time[0].CURRENT_DATE;
		doRestOfInitialization();
	});
}

function getTotals(period){
	getTotalReceiptByProduct(period);
	getTotalIssueByProduct(period);
}

 function getTotalReceiptByProduct(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_RECEIPT_BY_PRODUCT&ITEM='+document.form.ITEM.value+'&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.receipts[0].TOTAL_RECEIPT)){
			$('#TotalPurchase').val(parseFloat(0.00000).toFixed(<%=numberOfDecimal%>));
		}else{
	  		$('#TotalPurchase').val(parseFloat(data.receipts[0].TOTAL_RECEIPT).toFixed(<%=numberOfDecimal%>));
		}
	});
}

function getTotalIssueByProduct(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_ISSUE_BY_PRODUCT&ITEM='+document.form.ITEM.value+'&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.issues[0].TOTAL_ISSUE)){
			$('#TotalSale').val(parseFloat(0.00000).toFixed(<%=numberOfDecimal%>));
		}else{
	  		$('#TotalSale').val(parseFloat(data.issues[0].TOTAL_ISSUE).toFixed(<%=numberOfDecimal%>));
		}
	});
} 

function getFromDateForPeriod(period){
	if (period == 'Last 30 days'){
		return moment().add(-30, 'days');//
	}else if (period == 'Today'){
		return moment();//
	}else if (period == 'Tomorrow'){
		return moment().add(1, 'days');//
	}else if (period == 'This Month'){
		return moment().startOf('month');//
	}else if (period == 'This Quarter'){
		return moment().startOf('quarter');//
	}else if (period == 'This Year'){
		return moment().startOf('year');//
	}else if (period == 'Tomorrow (F)'){
		return moment().add(1, 'days');//
	}else if (period == 'This month (F)'){
		return moment().add(1, 'days');//
	}else if (period == 'This quarter (F)'){
		return moment().add(1, 'days');//
	}else if (period == 'This year (F)'){
		return moment().add(1, 'days');//
	}else if (period == 'Previous Month'){
		return moment().add(-1, 'month').startOf('month');//
	}else if (period == 'Previous Quarter'){
		return moment().add(-1, 'quarter').startOf('quarter');//
	}else if (period == 'Previous Year'){
		return moment().add(-1, 'year').startOf('year');//
	}else if (period == 'Next 30 days'){
		return moment().add(1, 'days');//
	}else if (period == 'This Week'){
		return moment().startOf('week');//
	}else if (period == 'Previous Week'){
		return moment().add(-1, 'week').startOf('week');//
	}
}

function getToDateForPeriod(period){
	if (period == 'Last 30 days'){
		return moment().add(-1, 'days');//
	}else if (period == 'Today'){
		return moment();//
	}else if (period == 'Tomorrow'){
		return moment().add(1, 'days');//
	}else if (period == 'This Month'){
		return moment().endOf('month');//
	}else if (period == 'This Quarter'){
		return moment().endOf('quarter');//
	}else if (period == 'This Year'){
		return moment().endOf('year');//
	}else if (period == 'Tomorrow (F)'){
		return moment().add(1, 'days');//
	}else if (period == 'This month (F)'){
		return moment().endOf('month');//
	}else if (period == 'This quarter (F)'){
		return moment().endOf('quarter');//
	}else if (period == 'This year (F)'){
		return moment().endOf('year');//
	}else if (period == 'Previous Month'){
		return moment().add(-1, 'month').endOf('month');//
	}else if (period == 'Previous Quarter'){
		return moment().add(-1, 'quarter').endOf('quarter');//
	}else if (period == 'Previous Year'){
		return moment().add(-1, 'year').endOf('year');//
	}else if (period == 'Next 30 days'){
		return moment().add(30, 'days');//
	}else if (period == 'This Week'){
		return moment().endOf('week');//
	}else if (period == 'Previous Week'){
		return moment().add(-1, 'week').endOf('week');//
	}
}

function getFormattedDate(momentObj, formatString){
	if (typeof formatString === 'undefined'){
		return momentObj.utcOffset(currentServerTime, true).format("DD-MMM-YYYY");
	}else{
		return momentObj.utcOffset(currentServerTime, true).format(formatString);
	}
}

function doRestOfInitialization() {
	getTotals('This Week');
}

if(!((document.form.ITEM.value == "") || (document.form.ITEM.value == null))){	
	loadCustomerDiscount(document.form.ITEM.value);
	loadSupplierDiscount(document.form.ITEM.value);
	loadItemSupplier(document.form.ITEM.value);
	loadDetailDesc(document.form.ITEM.value);
	loadAddPrd(document.form.ITEM.value);
}

function setCheckedValue(radioObj, newValue) {
	if (!radioObj)
		return;
	var radioLength = radioObj.length;
	if (radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for ( var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if (radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}


function loadDetailDesc(productId) {
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_ADDITIONAL_DETAIL_DETAILS"
			},
			
			dataType : "json",
			success : formatDetailDescData
			
		});
	}

function formatDetailDescData(data) {
	var ii = 0;
	var errorBoo = false;
	if (data.status == "9") {
		errorBoo = true;
	}
	if (!errorBoo) {
		var table = document.getElementById('descriptiontbl');
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		
		for(; rowCount>=0; rowCount--) {
			table.deleteRow(rowCount);
		}
		 $.each(data.items, function(i,item){
			 	   addRowDescCost('descriptiontbl',item.ITEMDETAIL,item.ITEMDETAILDESC);
	    	       setCheckedValue('',item.ITEMDETAILDESC); 	
	     });
		 if (data.items == null || data.items.length == 0){
			 addRowDescCost('descriptiontbl','','');
	     }
	        
	} else {
		//alert("No Data found....!!!!");
	}

}

function loadAddPrd(productId) {
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_ADDITIONAL_PRD_DETAILS"
			},
			
			dataType : "json",
			success : formatAddPrdData
			
		});
	}

function formatAddPrdData(data) {
	var ii = 0;
	var errorBoo = false;
	if (data.status == "9") {
		errorBoo = true;
	}
	if (!errorBoo) {
		var table = document.getElementById('prdtbl');
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		
		for(; rowCount>=0; rowCount--) {
			table.deleteRow(rowCount);
		}
		 $.each(data.items, function(i,item){
			 	   addRowPrdCost('prdtbl',item.ADDITIONAL,item.ADDITIONALITEM);
	    	       setCheckedValue('',item.ADDITIONALITEM); 	
	     });
		 if (data.items == null || data.items.length == 0){
			 addRowPrdCost('prdtbl','','');
	     }
	        
	} else {
		//alert("No Data found....!!!!");
	}

}


function addRowDescCost(tableID,desc,addDesc) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var newrowCount = 1+rowCount;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Detail Description"+rowCount+"\">Detail Description "+newrowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	var itemCell = row.insertCell(1);
	itemCell.style.width ="90%";
	itemCell.style.textAlign ="center";
	var itemCellText =  "<div class=\"col-sm-10\"><div class=\"input-group\"> <INPUT class=\"form-control\" name=\"DESCRIPTION"+rowCount+"\" readonly";
	itemCellText = itemCellText+ " id=\"DESCRIPTION"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+addDesc+"\" MAXLENGTH=\"50\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
}
function addRowPrdCost(tableID,prd,addPrd) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var newrowCount = 1+rowCount;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];

	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Product"+rowCount+"\">Product "+newrowCount+"\</label>&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	var itemCell = row.insertCell(1);
	itemCell.style.width ="93%";
	itemCell.style.textAlign ="center";
	var itemCellText =  "<div class=\"col-sm-6\"> <div class=\"input-group\"> <INPUT class=\"form-control itemSearch\" name=\"PRODUCT"+rowCount+"\" readonly";
	itemCellText = itemCellText+ " id=\"PRODUCT"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+addPrd+"\" placeholder=\"Select Product\"  MAXLENGTH=\"50\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
}

function loadCustomerDiscount(productId) {
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_CUSTOMER_DISCOUNT_DETAILS"
			},
			
			dataType : "json",
			success : formatCustomerDiscountData
			
		});
	}

function formatCustomerDiscountData(data) {
 	var ii = 0;
	var errorBoo = false;
	if (data.status == "9") {
		errorBoo = true;
	}
	if (!errorBoo) {
		var table = document.getElementById('customerDiscount');
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		
		for(; rowCount>=0; rowCount--) {
				table.deleteRow(rowCount);
			}
		 $.each(data.items, function(i,item){
			      var cname = item.CUSTOMERTYPE +'-'+ item.CNAME
	    	       addRowFormatCustomerType('customerDiscount',item.OBDISCOUNT,cname);
	    	       setCheckedValue( document.form.OBDISCOUNT,item.DISCOUNTTYPE); 	
	     });
	     if (data.items == null || data.items.length == 0){
	    	 addRowFormatCustomerType('customerDiscount','','');
	     }
	} else {
		//alert("No Data found....!!!!");
	}

}

function addRowFormatCustomerType(tableID,obDiscount,customerType) {

	
var table = document.getElementById(tableID);
var rowCount = table.rows.length;
var row = table.insertRow(rowCount);
var form = document.forms['form'];


var itemCell = row.insertCell(0);
var itemCellText =  "<INPUT class=\"form-control\" name=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" readonly ";
itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" value=\""+obDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\">&nbsp;";
itemCell.innerHTML = itemCellText;


var itemCell = row.insertCell(1);
var itemCellText =  "<div class=\"input-group\"><INPUT class=\"form-control customerSearch\" name=\"CUSTOMER_TYPE_ID_"+rowCount+"\" readonly ";
itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\" value=\""+customerType+"\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
itemCell.innerHTML = itemCellText;

}


function loadItemSupplier(productId) {
var urlStr = "/track/ItemMstServlet";
$.ajax( {
type : "POST",
url : urlStr,
data : {
	ITEM : productId,
	PLANT : "<%=plant%>",
	ACTION : "LOAD_ITEM_SUPPLIER_DETAILS"
	},
	
	dataType : "json",
	success : formatItemSupplierData
	
});
}

function formatItemSupplierData(data) {
console.log('loading supplier data');
console.log(data);
var ii = 0;
var errorBoo = false;
if (data.status == "9") {
errorBoo = true;
}
if (!errorBoo) {
var table = document.getElementById('multiitemsup');
var rowCount = table.rows.length;
rowCount = rowCount * 1 - 1;

for(; rowCount>=0; rowCount--) {
	table.deleteRow(rowCount);
}
 $.each(data.items, function(i,item){
	       addItemSupplieredit('multiitemsup',item.VENDNO);
	       setCheckedValue('',item.DISCOUNTTYPE); 	
 });
 if (data.items == null || data.items.length == 0){
	 addItemSupplieredit('multiitemsup','');
 }
    
} else {
//alert("No Data found....!!!!");
}

}


function addItemSupplieredit(tableID,vendNo) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];
	var itemCell = row.insertCell(0);
	var itemCellText =  "<div class=\"input-group\">  <INPUT class=\"form-control\"  name=\"ITEMSUPPLIER_"+rowCount+"\" readonly";
	itemCellText = itemCellText+ " id=\"ITEMSUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"55\"   value=\""+vendNo+"\" placeholder=\"Select Supplier\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateItemSupplier("+rowCount+");}\" MAXLENGTH=\"50\" readonly></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
}

function loadSupplierDiscount(productId) {
var urlStr = "/track/ItemMstServlet";
$.ajax( {
type : "POST",
url : urlStr,
data : {
	ITEM : productId,
	PLANT : "<%=plant%>",
	ACTION : "LOAD_SUPPLIER_DISCOUNT_DETAILS"
	},
	
	dataType : "json",
	success : formatSupplierDiscountData
	
});
}

function formatSupplierDiscountData(data) {
console.log('loading supplier data');
console.log(data);
var ii = 0;
var errorBoo = false;
if (data.status == "9") {
errorBoo = true;
}
if (!errorBoo) {
var table = document.getElementById('supplierDiscount');
var rowCount = table.rows.length;
rowCount = rowCount * 1 - 1;

for(; rowCount>=0; rowCount--) {
	table.deleteRow(rowCount);
}
 $.each(data.items, function(i,item){
	       var vname = item.VENDNO +'-'+ item.VNAME
	       addRowFormatCost('supplierDiscount',item.IBDISCOUNT,vname);
	       setCheckedValue(document.form.IBDISCOUNT,item.DISCOUNTTYPE); 	
 });
 if (data.items == null || data.items.length == 0){
	 addRowFormatCost('supplierDiscount','','');
 }
    
} else {
//alert("No Data found....!!!!");
}

}

function addRowFormatCost(tableID,ibDiscount,vendNo) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" readonly ";
	itemCellText = itemCellText+"  id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" value=\""+ibDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"> <INPUT class=\"form-control supplierSearch\" name=\"SUPPLIER_"+rowCount+"\" readonly ";
	itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+vendNo+"\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;
	
	
}

function DisplayNonStkType()
{
 
var val = 0;

for( i = 0; i < document.form.NONSTOCKFLAG.length; i++ )
{
  if( document.form.NONSTOCKFLAG[i].checked == true )
  {
	  val = document.form.NONSTOCKFLAG[i].value;
	  
	  if(val=='Y')
	  {
	   //when we need Non-Stock Type input box uncomment below first line and comment the second line.
        
	   //document.getElementById("divNonStk").style.display = "inline";
		  document.getElementById("divNonStk").style.display = "none";  
	  }else{
        document.getElementById("divNonStk").style.display = "none";
   }
	  
	
  }
  
}

}

function cooCountryCurrency(coo){	
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_COO_CURRENCY_CODE",
			COUNTRY : coo,
		},
		success : function(dataitem) {
			var cur=dataitem.CURRENCY;
	 		$("input[name='COOCURRENCY']").val(cur);
		}
	});	
	}

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

