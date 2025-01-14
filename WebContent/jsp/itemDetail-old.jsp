<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Product Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
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
String sViewMapItemEnb = "enabled",prdclsid="",clsdesc="",prdtypeid="",prd_brand_id = "",typedesc="",prd_cls_id="",uom="";
String sItem      = "",sItemDesc  = "", sItemUom   = "", sReOrdQty  = "",price="",msprice="",cost="",nonstk="",nonstkid="",
action     = "",sRemark1   = "" , sRemark2  = "", sRemark3   = "",nonstkflag="",ISPARENTCHILD="", NETWEIGHT="", GROSSWEIGHT="", HSCODE="", COO="",
sItemCondition = "",sArtist="",loc="",sTitle="",sMedium="",sRemark="",nonstockflag="",PRODGST="",
nonstktypeid="",sUOM="",stkqty="",asset="",isActive="",maxstkqty="",posdiscount="",VINNO="",MODEL="",RENTALPRICE="",SERVICEPRICE="",PURCHASEUOM="",SALESUOM="",RENTALUOM="",SERVICEUOM="",INVENTORYUOM="",
Purchaseuom="",Salesuom="",Rentaluom="",Serviceuom="",Inventoryuom="",StockOnHand="",CommittedStock="",AvailableForSale="",QtyTBShipped="",
QtyTBReceived="",ReorderPoint="",Catalog="";

StrUtils strUtils = new StrUtils();
ItemUtil itemUtil = new ItemUtil();
DateUtils dateutils = new DateUtils();
PrdTypeUtil prdutil = new PrdTypeUtil();
PrdClassUtil prdcls = new PrdClassUtil();
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
List uomlist = itemUtil.getUomList(plant," ");
List nonstklst =   itemUtil.getNonStockList(plant,"");   
prd_cls_id=strUtils.fString(request.getParameter("PRD_CLS_ID"));
sArtist = strUtils.fString(request.getParameter("ARTIST"));
prd_brand_id=strUtils.fString(request.getParameter("PRD_BRAND_ID"));
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
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    Catalog = StrUtils.fString(request.getParameter("CATALOG"));
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><font style="font-size:40px;"><%=res%></font></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form" method="post">
    
    	<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>General</strong></div>
    <div class="panel-body">
    <!--  -->
    <div class="row">
    	<div class="col-sm-9">
	    	<div class="form-group">
		      	<label class="control-label col-sm-4" for="Product ID">Product ID:</label>
		      	<div class="col-sm-4">
			      	<INPUT class="form-control" name="ITEM" readonly type = "TEXT" value="<%=sItem%>" size="20"  MAXLENGTH=20 <%=sItemEnb%> >
			   		<INPUT type = "hidden" name="ITEM1" value = <%=sItem%>>
		   		</div>
		   		<div class="form-inline">
			    	<div class="col-sm-4">
				    	<label class="radio-inline">
				      	<input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
				     	</label>
			     	</div>
		     	</div>		     	
	    	</div>
    	</div>
    	<div class="col-sm-3">
    		<div class="form-inline">
	     		<div class="col-sm-12">
	     			<img src="<%=Catalog%>" alt="new image" class="img-thumbnail img-responsive col-sm-3" 
	     			style="width: 50%;float:right;padding: 3px;">
	     		</div>
	     	</div>
    	</div>
    </div>  
    <!--  -->
    <%-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Product ID">Product ID:</label>
      	<div class="col-sm-3">
	      	<INPUT class="form-control" name="ITEM" readonly type = "TEXT" value="<%=sItem%>" size="20"  MAXLENGTH=20 <%=sItemEnb%> >
	   		<INPUT type = "hidden" name="ITEM1" value = <%=sItem%>>
   		</div>
   		<div class="form-inline">
	    	<div class="col-sm-3">
		    	<label class="radio-inline">
		      	<input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
		    	</label>
		    	<label class="radio-inline">
		      	<input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
		     	</label>
	     	</div>
     	</div>
     	<div class="form-inline">
     		<div class="col-sm-3">
     			<img src="<%=Catalog%>" alt="new image" class="img-thumbnail img-responsive col-sm-3" style="width: 50%;float:right;padding: 3px;">
     		</div>
     	</div>
    </div> --%>
     <div class="form-group">
      	<label class="control-label col-sm-3" for="Product Description">Description:</label>
      	<div class="col-sm-3">
      		<INPUT class="form-control" name="DESC" type = "TEXT" value="<%=strUtils.forHTMLTag(sItemDesc)%>" size="20" readonly  MAXLENGTH=50>
      	</div>
      	<div class="form-inline">
	      	<!-- <label class="control-label col-sm-2" for="Remarks">Stock Type:</label> -->
	    	<div class="col-sm-3">
		    	<label class="radio-inline">
		      	<INPUT name="NONSTOCKFLAG" type = "radio"   value="N" <%if(nonstkflag.equalsIgnoreCase("N")) {%>  checked="checked" <%}%>  disabled="disabled">Stock
		    	</label>
		    	<label class="radio-inline">
		    	<INPUT  name="NONSTOCKFLAG" type = "radio" value="Y" <%if(nonstkflag.equalsIgnoreCase("Y")) {%>  checked="checked" <%}%> disabled="disabled">Non Stock
		    	</label>
	     	</div>
		</div>
		
    </div>
    
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="description">Detailed Description:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=strUtils.forHTMLTag(sRemark)%>"size="20" MAXLENGTH=100 readonly>
      	</div>
      	<%-- <div class="form-inline">
      	<label class="control-label col-sm-2" id="nonstklbl" for="Stock Type">Non-Stock type:</label>
      	<div class="col-sm-3" id="nonStktd">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="NONSTKTYPE" disabled="disabled" size="1" maxlength="9">
		<OPTION selected value="0">Choose</ OPTION>
		<%       for (int i =0; i<nonstklst.size(); i++){
                 Map map = (Map) nonstklst.get(i);
                 nonstk     = (String) map.get("NONSTOCKDESC");
                 nonstkid = (String) map.get("NONSTKCODE");
        %>
		<OPTION value="<%=nonstkid%>" <%if(nonstkid.equalsIgnoreCase(nonstktypeid)){%>selected<%}%>><%=nonstk%>
		</OPTION>
		<%}%>
		</select>
  		</div>
  	  	</div> --%>
  	  	<div class="form-inline">      	
      	<div class="col-sm-6 text-center">          
        	<strong>Physical Stock</strong>
        </div>
    	</div>
    	</div>
    	
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Unit Of Measure">Basic UOM:</label>
      	<div class="col-sm-3">           	
	  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="UOM" disabled="disabled" size="1" maxlength="9">
			<OPTION selected  value="0">Choose </OPTION> 
	       	<%       for (int i =0; i<uomlist.size(); i++){
	  				 Map map = (Map) uomlist.get(i);
	            	 uom         = (String) map.get("stkuom");%>
	    	<OPTION  value="<%=uom%>" <%if(sUOM.equalsIgnoreCase(uom)){%>selected<%}%>><%=uom%> </OPTION><%}%>
			</select>
  		</div>
  		<div class="form-inline">
	      	<label class="control-label col-sm-3" for="StockOnHand">Stock On Hand:</label>
	      	<div class="col-sm-3">          
	        	<INPUT class="form-control" name="StockOnHand" type="TEXT" value="<%=strUtils.forHTMLTag(StockOnHand)%>" style="width:100%" MAXLENGTH=100 readonly>
	      	</div>
    	</div>
  	 </div>
  	 
  	 <div class="form-group">
      	<label class="control-label col-sm-3" for="Net Weight">Net Weight (KG):</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Gross Weight">Gross Weight (KG):</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
      
      	<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Class">Product Class:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control" data-toggle="dropdown" data-placement="right" NAME="PRD_CLS_ID" disabled="disabled" size="1">
		<OPTION selected  value="NOCLASSIFICIATION">Choose </OPTION>
       	<%       for (int i =0; i<prdclslst.size(); i++){
  				 Map map = (Map) prdclslst.get(i);
  			  	 prdclsid         = (String) map.get("prd_cls_id");
     			 clsdesc          = (String) map.get("prd_cls_desc");%>
    	<OPTION  value="<%=prdclsid%>" <%if(prd_cls_id.equalsIgnoreCase(prdclsid)){%>selected<%}%>><%=clsdesc%> </OPTION><%}%>
		</select>
  		</div>
  		<div class="form-inline">
      	<label class="control-label col-sm-3" for="CommittedStock">Committed Stock:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="CommittedStock" type="TEXT" value="<%=strUtils.forHTMLTag(CommittedStock)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
  	  	</div>
  	  
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Type">Product Type:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="ARTIST" disabled="disabled" size="1">
		<OPTION selected  value="NOTYPE">Choose </OPTION>
        <%       for (int i =0; i<prdlist.size(); i++){
                 Map map = (Map) prdlist.get(i);
                 prdtypeid         = (String) map.get("prd_type_id");
                 typedesc             = (String) map.get("prd_type_desc");%>
    	<OPTION  value="<%=prdtypeid%>" <%if(sArtist.equalsIgnoreCase(prdtypeid)){%>selected<%}%>><%=typedesc%> </OPTION><%}%>  
        <OPTION  value="F"   <%if(sArtist.equalsIgnoreCase("F")){%>selected<%}%>>FINISHED GOODS </OPTION>
        <OPTION  value="R" <%if(sArtist.equalsIgnoreCase("R")){%>selected<%}%>>RAW MATERIAL </OPTION> 
        <OPTION  value="A" <%if(sArtist.equalsIgnoreCase("A")){%>selected<%}%>>ASSET </OPTION>
		</select>
  		</div>
  		<div class="form-inline">
      	<label class="control-label col-sm-3" for="AvailableForSale">Available for Sale:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="AvailableForSale" type="TEXT" value="<%=strUtils.forHTMLTag(AvailableForSale)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
  	  	</div>
  	  
 		<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Brand">Product Brand:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="PRD_BRAND" disabled="disabled" size="1">
		<OPTION selected value="NOBRAND">Choose</OPTION>
		<%       for (int i =0; i<prdBrandList.size(); i++){
		         Map map = (Map) prdBrandList.get(i);
				 String prdBrandId         = (String) map.get("PRD_BRAND_ID");
				 String prdBrandDesc             = (String) map.get("PRD_BRAND_DESC"); 
   		%>
		<OPTION value="<%=prdBrandId %>" <%if(prd_brand_id.equalsIgnoreCase(prdBrandId)){%> selected <%}%>><%=prdBrandDesc%></OPTION>
		<%} %>
	 	</select>
  		</div>
  		<div class="form-inline">
      	<label class="control-label col-sm-3" for="QtyTBShipped">Quantity to be Shipped:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="QtyTBShipped" type="TEXT" value="<%=strUtils.forHTMLTag(QtyTBShipped)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div>
  	  	</div>
    	
		<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
		
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Product VAT" id="TaxLabelOrderManagement"></label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="PRODGST" id="PRODGST" type="TEXT" value="<%=PRODGST%>" size="20" MAXLENGTH=50 readonly>
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-3" for="QtyTBReceived">Quantity to be Received:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="QtyTBReceived" type="TEXT" value="<%=strUtils.forHTMLTag(QtyTBReceived)%>" style="width:100%" MAXLENGTH=100 readonly>
      	</div>
    	</div> 
    	</div>
    	<!--  -->
    	
    	<div class="form-group">
	      	<label class="control-label col-sm-3" for="HS Code">HS Code:</label>
	      	<div class="col-sm-3">          
	       		<INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=strUtils.forHTMLTag(HSCODE)%>" MAXLENGTH=100 readonly>
	      	</div>   
	      	<div class="form-inline">
	      	<label class="control-label col-sm-3" for="ReorderPoint">Reorder Point:</label>
	      	<div class="col-sm-3">          
	        <INPUT class="form-control" name="ReorderPoint" type="TEXT" value="<%=stkqty%>" style="width:100%" MAXLENGTH=100 readonly>
	      	</div>
	    	</div>   	
    	</div>
    	
    	<div class="form-group">
	      	<label class="control-label col-sm-3" for="COO">COO:</label>
	      	<div class="col-sm-3">          
	       		<INPUT class="form-control" name="COO" type="TEXT" value="<%=strUtils.forHTMLTag(COO)%>" MAXLENGTH=100 readonly>
	      	</div>
	      	
    	</div>
    	
    	<div class="form-group">
	      	<label class="control-label col-sm-3" for="VINNO">VINNO:</label>
	      	<div class="col-sm-3">          
	       		<INPUT class="form-control" name="VINNO" type="TEXT" value="<%=strUtils.forHTMLTag(VINNO)%>" MAXLENGTH=100 readonly>
	      	</div>
	      	<div class="form-inline">
	      		<label class="control-label col-sm-3" for="Dates"></label>
	      		<div class="col-sm-3">          
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
	      	
    	</div>
    	
    	<div class="form-group">
	      	<label class="control-label col-sm-3" for="MODEL">MODEL:</label>
	      	<div class="col-sm-3">          
	       		<INPUT class="form-control" name="MODEL" type="TEXT" value="<%=strUtils.forHTMLTag(MODEL)%>" MAXLENGTH=100 readonly>
	      	</div>
	      	<div class="form-inline">
		      	<label class="control-label col-sm-3" for="TotalPurchase">Total Purchase:</label>
		      	<div class="col-sm-3">  
		      		<div class="input-group">
			      		<INPUT class="form-control" name="TotalPurchase" id="TotalPurchase" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span>
		      		</div>
		      	</div>
	    	</div>
	      	
    	</div>
    	<!--  -->
    	<div class="form-group">
	      	<label class="control-label col-sm-3" for="Remarks">Remarks1:</label>
	      	<div class="col-sm-3">          
	       		<INPUT class="form-control" name="ITEM_CONDITION" type="TEXT" value="<%=sItemCondition%>" size="20" MAXLENGTH=100 readonly>
	      	</div>
	      	<div class="form-inline">
		      	<label class="control-label col-sm-3" for="TotalSale">Total Sale:</label>
		      	<div class="col-sm-3"> 
			      	<div class="input-group">         
			        	<INPUT class="form-control" name="TotalSale" id="TotalSale" type="TEXT" value="" style="width:100%" MAXLENGTH=100 readonly>
			      		<span class="input-group-addon"><%=session.getAttribute("BASE_CURRENCY")%></span>
			      	</div>
		      	</div>
		    </div>
    	</div>
     	
     	<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks2:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="TITLE" type="TEXT" value="<%=sTitle%>" size="20" MAXLENGTH=100 readonly>
      	</div>
    	</div>
    </div>
    </div>
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Purchase</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Purchase UOM">Purchase UOM:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="UOM" disabled="disabled" size="1" maxlength="9">
		<OPTION selected  value="0">Choose </OPTION> 
       	<%       for (int i =0; i<uomlist.size(); i++){
  				 Map map = (Map) uomlist.get(i);
            	 Purchaseuom         = (String) map.get("stkuom");%>
    	<OPTION  value="<%=Purchaseuom%>" <%if(PURCHASEUOM.equalsIgnoreCase(Purchaseuom)){%>selected<%}%>><%=Purchaseuom%> </OPTION><%}%>
		</select>
  		</div>
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Cost">Cost:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="COST" type="TEXT" value="<%=new java.math.BigDecimal(cost).toPlainString()%>" size="20"	MAXLENGTH=50 readonly>
      	</div>
    	</div>
    </div>
    </div>
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Sales</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Sales UOM">Sales UOM:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="UOM" disabled="disabled" size="1" maxlength="9">
		<OPTION selected  value="0">Choose </OPTION> 
       	<%       for (int i =0; i<uomlist.size(); i++){
  				 Map map = (Map) uomlist.get(i);
            	 Salesuom         = (String) map.get("stkuom");%>
    	<OPTION  value="<%=Salesuom%>" <%if(SALESUOM.equalsIgnoreCase(Salesuom)){%>selected<%}%>><%=Salesuom%> </OPTION><%}%>
		</select>
  		</div>
  		
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="List Price">List Price:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="PRICE" type="TEXT" value="<%=new java.math.BigDecimal(price).toPlainString()%>" size="20" MAXLENGTH=50 readonly>
      	</div>
      	
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Min Selling Price ">Minimum Selling Price:</label>
      	<div class="col-sm-3">          
        <INPUT  class="form-control" name="MINSELLINGPRICE"  type="TEXT" value="<%=new java.math.BigDecimal(msprice).toPlainString()%>" size="20"	MAXLENGTH=50 readonly>
      	</div>
    	</div>
    	
    	    	
    	<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Discount POS(%)">Discount POS(%):</label>
      	<div class="col-sm-3">  -->         
       	<INPUT class="form-control" name="DISCOUNT" id="DISCOUNT" type="hidden" value="<%=posdiscount%>" size="20" MAXLENGTH=50 readonly>
      	<!-- </div>
    	</div> -->
    </div>
    </div>
    	
    	<!-- <div class="row">This div is used to seperate panel side by side
       <div class="col-sm-6"> -->  
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Rental</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Rental UOM">Rental UOM:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="UOM" disabled="disabled" size="1" maxlength="9">
		<OPTION selected  value="0">Choose </OPTION> 
       	<%       for (int i =0; i<uomlist.size(); i++){
  				 Map map = (Map) uomlist.get(i);
            	 Rentaluom         = (String) map.get("stkuom");%>
    	<OPTION  value="<%=Rentaluom%>" <%if(RENTALUOM.equalsIgnoreCase(Rentaluom)){%>selected<%}%>><%=Rentaluom%> </OPTION><%}%>
		</select>
  		</div>
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Rental Price">Rental Price:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="PRICE" type="TEXT" value="<%=new java.math.BigDecimal(RENTALPRICE).toPlainString()%>" size="20" MAXLENGTH=50 readonly>
      	</div>
    	</div>
    </div>
     </div>
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
      	<INPUT class="form-control" name="PURCHASEUOM" value="<%=Serviceuom%>" type="hidden" v size="20" MAXLENGTH=50 readonly>       
        <INPUT class="form-control" name="PRICE" type="hidden" value="<%=new java.math.BigDecimal(SERVICEPRICE).toPlainString()%>" size="20" MAXLENGTH=50 readonly>
      	<!-- </div>
    	</div>
    </div>
    </div>
    </div>
    </div> -->
  	  
    	
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Inventory</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Inventory UOM">Inventory UOM:</label>
      	<div class="col-sm-3">           	
  		<SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="PURCHASEUOM" disabled="disabled" size="1" maxlength="9">
		<OPTION selected  value="0">Choose </OPTION> 
       	<%       for (int i =0; i<uomlist.size(); i++){
  				 Map map = (Map) uomlist.get(i);
  				 Inventoryuom         = (String) map.get("stkuom");%>
    	<OPTION  value="<%=Inventoryuom%>" <%if(INVENTORYUOM.equalsIgnoreCase(Inventoryuom)){%>selected<%}%>><%=Inventoryuom%> </OPTION><%}%>
		</select>
  		</div>
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Min Stock Quantity:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="STKQTY" type="TEXT" value="<%=stkqty%>" size="20" MAXLENGTH=50 readonly>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Max Stock Quantity:</label>
      	<div class="col-sm-3">          
       	<INPUT class="form-control" name="MAXSTKQTY" type="TEXT" value="<%=maxstkqty%>"	size="20" MAXLENGTH=50 readonly>
      	</div>
    	</div>
    	    	
    	<INPUT type="hidden" 	name="LOC_0" id="LOC_0" >
    </div>
    </div>
      
 		<div class="form-group">        
     	<div class="col-sm-12" align="center">
	 	<button type="button" class="Submit btn btn-default" onClick="window.location.href='itemSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
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
    getCurrentServerTime();
    doRestOfInitialization();
});

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
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

