<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Product Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/JsBarcode.all.js"></script>
<script src="../jsp/js/itemsummary.js"></script>
<script>

function setCheckedValue(temp){
	
}
var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
     
function isNumericInput(strString) {
	var strValidChars = "0123456789";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
} 

function ExportReport(){
	document.form.action = "/track/ImportItemServlet?action=Export_Excel";
	document.form.submit();
	
} 
function ExportSupplerDiscount(){
	document.form.action = "/track/ReportServlet?action=SUPPLIERDISCOUNT";
	document.form.submit();
	
} 
function ExportCustomerDiscount(){
	document.form.action = "/track/ReportServlet?action=CUSTOMERDISCOUNT";
	document.form.submit();
	
} 
/* function onGo(){
	document.form.action ="itemSummary.jsp";
var recsPerPage = document.form.RECS_PER_PAGE.value;
 if (isNumericInput(recsPerPage) == false) {
                    alert("Entered Records display Per Page as Number!");
                    document.form.RECS_PER_PAGE.value = "";
                    document.form.RECS_PER_PAGE.focus();
                    return false;
} 
  document.form.submit();

} */
</script>


<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	_userBean.setmLogger(mLogger);
        long listRec=0;
	List locQryList  = new ArrayList();
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",STOCKTYPE="",view_Inv="",RECS_PER_PAGE="";
   


	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
// 	String PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	/* ITEM     = StrUtils.fString(request.getParameter("ITEM")); */
	ITEM_DESC = StrUtils.fString(request.getParameter("ITEM_DESC"));
    PRD_CLS_ID =  StrUtils.fString(request.getParameter("PRD_CLS_ID"));
    PRD_DEPT_ID =  StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
    PRD_TYPE_ID =  StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
    PRD_BRAND_ID =  StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
    STOCKTYPE =  StrUtils.fString(request.getParameter("STOCKTYPE"));
    if(STOCKTYPE.equalsIgnoreCase("Stock"))
    	STOCKTYPE="N";
    else if(STOCKTYPE.equalsIgnoreCase("Non-Stock"))
    	STOCKTYPE="Y";
    
    RECS_PER_PAGE = StrUtils.fString(request.getParameter("RECS_PER_PAGE"));
//     fieldDesc=StrUtils.fString(request.getParameter("result"));
/* String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction"); */
String msg =  StrUtils.fString(request.getParameter("Msg"));
fieldDesc =  StrUtils.fString(request.getParameter("result"));
String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
    boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false,displaySummaryExport=false,displaySummaryImport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryLink = ub.isCheckValAcc("summarylnkuitem", PLANT,username);
	displaySummaryEdit = ub.isCheckValAcc("edituitem", PLANT,username);
	displaySummaryNew = ub.isCheckValAcc("newuitem", PLANT,username);
	displaySummaryExport = ub.isCheckValAcc("exportuitem", PLANT,username);
	displaySummaryImport = ub.isCheckValAcc("importuitem", PLANT,username);
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkuitem", PLANT,username);
	displaySummaryEdit = ub.isCheckValinv("edituitem", PLANT,username);
	displaySummaryNew = ub.isCheckValinv("newuitem", PLANT,username);
	displaySummaryExport = ub.isCheckValinv("exportuitem", PLANT,username);
	displaySummaryImport = ub.isCheckValinv("importuitem", PLANT,username);
	}
	//IMTIZIAF
    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(PLANT);//Check Company Industry
	String Shopify = _PlantMstDAO.getshopify(PLANT);
	String Shopee = _PlantMstDAO.getshopee(PLANT);
    String collectionDate=DateUtils.getDate();
    String printdate=DateUtils.getDateFormatYYMMDD();
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
    int curPage      = 1;
  if(RECS_PER_PAGE.length()==0) 
	  {
	  //RECS_PER_PAGE = "100";
	  listRec=new ItemSesBeanDAO().getProductCount(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT,"");
	  RECS_PER_PAGE = String.valueOf(listRec);
	  }
    int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
    long totalRec     = 0;
    String isDisabled ="disabled";
        view_Inv      = StrUtils.fString(request.getParameter("VIEW_INV"));
String currentPage        = StrUtils.fString(request.getParameter("cur_page"));

String listRecSize =StrUtils.fString(request.getParameter("listRecSize")); 
if(listRecSize.length()==0){
listRecSize ="0";
}
	ItemUtil itemUtil = new ItemUtil();
	itemUtil.setmLogger(mLogger);
	System.out.println("PGAction:"+PGaction);
	if(PGaction.equalsIgnoreCase("View")){
	 try{
               currentPage="1";
                listRec=new ItemSesBeanDAO().getProductCount(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT,"");
               int start =  Integer.parseInt(currentPage);
               int end = Integer.parseInt(currentPage)*recPerPage;
	      Hashtable ht = new Hashtable();
	      if(StrUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,STOCKTYPE,PLANT,"",start,end);
	      if(locQryList.size()== 0)
	      {
	        fieldDesc="Data Not Found";
	      }
             if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
             isDisabled="";
             }
             if (currentPage.length() > 0)
             {
            try  {   curPage = (new Integer(currentPage)).intValue(); 
            System.out.println("curPage :: "+curPage); 
            }
            catch (Exception e)   {   curPage = 1;                                      }
            }
     	 }catch(Exception e) { }
         
   
}
if(PGaction.equalsIgnoreCase("PREVORNEXT")){
 try{

int start = (new Integer(currentPage)-1)*recPerPage;
int end = (new Integer(currentPage)*recPerPage)+1;
   Hashtable ht = new Hashtable();
	      if(StrUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryItemMstForSearchCriteriaNew(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,STOCKTYPE,PLANT,"",start,end);
     if (locQryList != null)  {         totalRec = locQryList.size();
     isDisabled="";
     }
     if (currentPage.length() > 0)
     {
     
    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
    catch (Exception e)   {   curPage = 1;                                      }
    }
     
   
   
 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
}

 //int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
    //if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

    //System.out.println("totalPages :: "+totalPages);
    //System.out.println("curPage :: "+curPage);
    
    String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
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
%>
<SCRIPT>

    <%-- var cur_page     = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records --%>

    // Display previous page of user list
    /* function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form.cur_page.value = cur_page;
       document.form.PGaction.value="PREVORNEXT";
      document.form.action = "../jsp/itemSummary.jsp";
	document.form.submit();
    } */
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    /* function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form.cur_page.value = cur_page;
      document.form.PGaction.value="PREVORNEXT";
      document.form.action = "../jsp/itemSummary.jsp";
      document.form.submit();
      }
    } */
 </SCRIPT>
 <center>
 <%if (msg.equals("")) {%>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
	<%}else{%>
	<h2><small class="error-msg"><%=msg%></small></h2>
	<%}%>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Product Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              
                 <div class="btn-group" role="group">
                 <%if(displaySummaryExport){ %>
              <button type="button" class="btn btn-default" data-toggle="dropdown" >Export <span class="caret"></span></button>
              <ul class="dropdown-menu" style="min-width: 0px;">
              <li id="Export-product"><a href="javascript:ExportReport();">Product Master</a></li>
               <li id="Export-supplier"><a href="javascript:ExportSupplerDiscount();">Supplier Discount</a></li>
                <li id="Export-customer"><a href="javascript:ExportCustomerDiscount();">Customer Discount</a></li>
                </ul>
					&nbsp;
					 <%}%>
				</div>
				
              <div class="btn-group" role="group">
              <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default" data-toggle="dropdown" >Import <span class="caret"></span></button>
						<ul class="dropdown-menu" style="min-width: 0px;">
			            <li id="Import-product"><a onclick="window.location.href='../product/u-cloproduct'">Product</a></li>
			            <li id="Import-product"><a onclick="window.location.href='../product/importitemsupplier'">Product Assigned Supplier</a></li>
			            <li id="Import-product"><a onClick="window.location.href='../product/importminmax'">Min/Max Qty</a></li>
			            <li id="Import-product"><a onClick="window.location.href='../product/importoutletminmax'">Outlet Min/Max Qty</a></li>
			            <% if(COMP_INDUSTRY.equals("Retail")) {%>
			            <li id="Import-product"><a onclick="window.location.href='../product/additional?cmd=desc'">Additional Product Detail Description</a></li>
			            <li id="Import-product"><a onclick="window.location.href='../product/additional?cmd=img'">Additional Product catalog Image</a></li>
			            <li id="Import-product"><a onclick="window.location.href='../product/additional?cmd=item'">Additional Product</a></li>
			            <%}%>
			           <%if(Shopify.equalsIgnoreCase("1")){ %>
			            <li id="Import-product"><a onclick="window.location.href='../product/shopifyproduct'">Shopify Product</a></li>
			            <%} %>
			            <%if (Shopee.equalsIgnoreCase("1")){ %>
						<li id="Import-product"><a onClick= "window.location.href='../product/importshopee'">Shopee Product</a></li><%} %>
						</ul>
						
					&nbsp;
					<%}%>
				</div>
				
				<div class="btn-group" role="group">
				<%if(displaySummaryNew){ %>
					<button type="button" class="btn btn-default"
							onclick="window.location.href='../product/new'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
						
					&nbsp;
					<%}%>
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="itemSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
<INPUT type="hidden" name="VIEW_INV" value="">
  
<div id="target" style="display:none"> 

<div class="form-group">
<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-4 ac-box">
				<!-- <div class="input-group"> --> 
				<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
				<INPUT type="hidden" name="plant" value=<%=PLANT%>>    
				<input type="text" class="ac-selected  form-control typeahead item" id="ITEM" placeholder="Product" name="ITEM" value=<%=ITEM%>>
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/view_product_list.jsp?ITEM='+form.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				<!-- </div> -->
			</div>
  		<div class="col-sm-4 ac-box">
				<!-- <div class="input-group"> -->
				<input type="text" class="ac-selected  form-control typeahead itemdesc" id="ITEM_DESC" placeholder="Product Description" name="ITEM_DESC">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitemdesc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM_DESC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/view_product_list.jsp?ITEM_DESC='+form.ITEM_DESC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
				<!-- </div> -->
			</div>
  		</div>
  		
	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  		
  		<input type="text" class="ac-selected  form-control typeahead ProductDept" id="PRD_DEPT_ID" placeholder="Product Department" name="PRD_DEPT_ID">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductClass.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<!-- <div class="input-group"> -->  		
  		<input type="text" class="ac-selected  form-control typeahead ProductClass" id="PRD_CLS_ID" placeholder="Product Category" name="PRD_CLS_ID">
  		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductClass.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		<!-- </div> -->
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">
  		<input type="text" class="ac-selected  form-control typeahead ProductType" id="PRD_TYPE_ID" placeholder="Product Sub Category" name="PRD_TYPE_ID">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductType.jsp?PRD_TYPE_ID='+form.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  		 -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  		
  		<input type="text" class="ac-selected  form-control typeahead ProductBrand" id="PRD_BRAND_ID" placeholder="Product Brand" name="PRD_BRAND_ID">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		
  		</div>
  		</div>
  		
	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	<div class="col-sm-4 ac-box">
  	<div class="input-group">  		
  		<input type="text" name="STOCKTYPE" id="STOCKTYPE" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(STOCKTYPE)%>" placeholder="Stock Type" >
		<span class="select-icon" onclick="$(this).parent().find('input[name=\'STOCKTYPE\']').focus()">
		<i class="glyphicon glyphicon-menu-down"></i> </span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  
			<div class="col-sm-10 txn-buttons">
			<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
       <!-- <button type="button" class="Submit btn btn-default" value='Export Master Data'  onclick="ExportReport();">Export Master Data</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Export Supplier Discount"   onClick="ExportSupplerDiscount();">Export Supplier Discount</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value='Export Customer Discount'  onclick="ExportCustomerDiscount();">Export Customer Discount</button>&nbsp;&nbsp; -->
       <!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp;
        <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
			</div>
			</div>
			</div>
		</div>  		  		
</div>


        
       <br>   
        </div>
        
        
      <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-4">
      <!-- <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button> -->   
  	  <!-- <button type="button" class="Submit btn btn-default" value='Export Master Data'  onclick="ExportReport();">Export Master Data</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Export Supplier Discount"   onClick="ExportSupplerDiscount();">Export Supplier Discount</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value='Export Customer Discount'  onclick="ExportCustomerDiscount();">Export Customer Discount</button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
      <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div> 
	
<INPUT name="RECS_PER_PAGE" type = "hidden" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4 class="form-control">
	

<div style="overflow-x:auto;">
	<div class="col-12 col-sm-12 no-padding">
		    		<input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAllItem(this.checked);">
                   	<strong>&nbsp;Select/Unselect All </strong>
            </div>
            <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
            </div>
<table id="tabledetail" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Product ID</th>
            <th style="font-size: smaller;">Product Description</th>
            <th style="font-size: smaller;">Catalog</th>
            <!-- <th style="font-size: smaller;">Detail Description</th> -->
            <!-- <th>Manufacturer</th> -->
			<th style="font-size: smaller;">Base UOM</th>
			<th style="font-size: smaller;">Product Department</th>
            <th style="font-size: smaller;">Product Category</th>
            <th style="font-size: smaller;">Product Sub Category</th>
            <th style="font-size: smaller;">Product Brand</th>
			<!-- <th style="font-size: smaller;">Purchase UOM</th> -->
			<!-- <th style="font-size: smaller;">Sales UOM</th> -->
            <th style="font-size: smaller;">List Price</th>
			<th style="font-size: smaller;">Cost</th>
           <!--  <th style="font-size: smaller;">Min.Selling price</th> -->
          <!--   <th style="font-size: smaller;">Rental UOM</th>
            <th style="font-size: smaller;">Rental Price</th> -->
           <!--  <th style="font-size: smaller;">Inventory UOM</th> -->
            <th style="font-size: smaller;">Inventory MaxStkQty</th>
            <th style="font-size: smaller;">Inventory MinStkQty</th>
            <th style="font-size: smaller;">IsActive</th>
            <th style="font-size: smaller;">Print Barcode</th>
            <th style="font-size: smaller;">Edit</th>    
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
        
        <!-- <tbody> -->
   
        
  
   	  <%
           /* for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
				int iIndex = iCnt + 1;
	            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
	            Vector vecItem   = (Vector)locQryList.get(iCnt);
	            System.out.println(" GST Data :"+ vecItem.get(24));
	            
	            String strNetWeight=StrUtils.fString((String)vecItem.get(25));
	            String strGrossWeight=StrUtils.fString((String)vecItem.get(26));
	            String strListprice=StrUtils.fString((String)vecItem.get(12));
	            String strCost=StrUtils.fString((String)vecItem.get(13));
	            String strMinSelPrice=StrUtils.fString((String)vecItem.get(18));
	            String strProductGst=StrUtils.fString((String)vecItem.get(24));
	            String strQty =(String)vecItem.get(8);
	            String maxStkQty =(String)vecItem.get(21);
				String strRentalprice=StrUtils.fString((String)vecItem.get(31));
	            String strServiceprice=StrUtils.fString((String)vecItem.get(32));
	            String catalogPath = StrUtils.fString((String)vecItem.get(39));
	            
	            
	            float netWeight="".equals(strNetWeight) ? 0.0f :  Float.parseFloat(strNetWeight);
	            float grossWeight="".equals(strGrossWeight) ? 0.0f :  Float.parseFloat(strGrossWeight);
	            float listPrice="".equals(strListprice) ? 0.0f :  Float.parseFloat(strListprice);
	            float costValue="".equals(strCost) ? 0.0f :  Float.parseFloat(strCost);
	            float minSellingPriceValue="".equals(strMinSelPrice) ? 0.0f :  Float.parseFloat(strMinSelPrice);
	            float productGST="".equals(strProductGst) ? 0.0f :  Float.parseFloat(strProductGst);
	            float strQtyVal="".equals(strQty) ? 0.0f :  Float.parseFloat(strQty);
	            float maxStkQtyVal="".equals(maxStkQty) ? 0.0f :  Float.parseFloat(maxStkQty);
				float Rentalprice="".equals(strRentalprice) ? 0.0f :  Float.parseFloat(strRentalprice);
	            float Serviceprice="".equals(strServiceprice) ? 0.0f :  Float.parseFloat(strServiceprice);
	            
	            
	            if(netWeight == 0f){
	            	strNetWeight=StrUtils.formatThreeDecimal(strNetWeight);
	            }else{
	            	strNetWeight=strNetWeight.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }if(grossWeight == 0f){
	            	strGrossWeight=StrUtils.formatThreeDecimal(strGrossWeight);
	            }else{
	            	strGrossWeight=strGrossWeight.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }  if(listPrice == 0f){
	            	strListprice=StrUtils.currencyWtoutSymbol(strListprice);
	            }else{
	            	strListprice=strListprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            } if(costValue == 0f){
	            	strCost=StrUtils.currencyWtoutSymbol(strCost);
	            }else{
	            	strCost=strCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }if(minSellingPriceValue == 0f){
	            	strMinSelPrice=StrUtils.currencyWtoutSymbol(strMinSelPrice);
	            }else{
	            	strMinSelPrice=strMinSelPrice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }if(productGST == 0f){
	            	strProductGst=StrUtils.formatThreeDecimal(strProductGst);
	            }else{
	            	strProductGst=strProductGst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }if(strQtyVal == 0f){
	            	strQty=StrUtils.formatThreeDecimal(strProductGst);
	            }else{
	            	strQty=strQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }if(maxStkQtyVal == 0f){
	            	maxStkQty=StrUtils.formatThreeDecimal(strProductGst);
	            }else{
	            	maxStkQty=maxStkQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }
	            
	            
				 if(Rentalprice == 0f){
	            	strRentalprice=StrUtils.currencyWtoutSymbol(strRentalprice);
	            }else{
	            	strRentalprice=strRentalprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            }
				 
	            if(Serviceprice == 0f){
	            	strServiceprice=StrUtils.currencyWtoutSymbol(strServiceprice);
	            }else{
	            	strServiceprice=strServiceprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	            } */
	            
	            /* double dListPrice = Double.parseDouble(strListprice);
	            strListprice = StrUtils.addZeroes(dListPrice, numberOfDecimal);
	            
	            double dCost = Double.parseDouble(strCost);
	            strCost = StrUtils.addZeroes(dCost, numberOfDecimal);
	            
	            double dMinSelPrice = Double.parseDouble(strMinSelPrice);
	            strMinSelPrice = StrUtils.addZeroes(dMinSelPrice, numberOfDecimal);
	            
	            double dRentalprice = Double.parseDouble(strRentalprice);
	            strRentalprice = StrUtils.addZeroes(dRentalprice, numberOfDecimal);
	            
	            double dServiceprice = Double.parseDouble(strServiceprice);
	            strServiceprice = StrUtils.addZeroes(Serviceprice, numberOfDecimal); */
	            
	            
	            //float pricef = Float.parseFloat((String)vecItem.get(12));
	            //pricef = strUtils.Round(pricef,2);
       %>
          <%-- <TR>
                <TD align="center"><%=(String)vecItem.get(17)%></TD> --%>                
                <%-- <TD align="left" ><a href ="itemDetail.jsp?action=View&ITEM=<%=(String)vecItem.get(0)%>&DESC=<%=strUtils.replaceCharacters2Send1((String)vecItem.get(1))%>&ARTIST=<%=(String)vecItem.get(2)%>&UOM=<%=(String)vecItem.get(3)%>&REMARKS=<%=strUtils.replaceCharacters2Send1((String)vecItem.get(4))%>&MEDIUM=<%=strUtils.replaceCharacters2Send1((String)vecItem.get(5))%>&ITEM_CONDITION=<%=(String)vecItem.get(6)%>&TITLE=<%=(String)vecItem.get(7)%>&STKQTY=<%=strQty%>&MAXSTKQTY=<%=maxStkQty%>&PRD_CLS_ID=<%=(String)vecItem.get(10)%>&ISACTIVE=<%=(String)vecItem.get(11)%>&PRICE=<%=strListprice%>&MINSELLINGPRICE=<%=strMinSelPrice%>&ISPARENTCHILD=<%=(String)vecItem.get(14)%>&COST=<%=strCost%>&NONSTKFLAG=<%=(String)vecItem.get(15)%>&NONSTKTYPEID=<%=(String)vecItem.get(16)%>&PRD_BRAND_ID=<%=(String)vecItem.get(19)%>&ITEM_LOC=<%=(String)vecItem.get(20)%>&POSDISCOUNT=<%=(String)vecItem.get(21)%>&PRODGST=<%=strProductGst%>&NETWEIGHT=<%=strNetWeight%>&GROSSWEIGHT=<%=strGrossWeight%>&HSCODE=<%=(String)vecItem.get(27)%>&COO=<%=(String)vecItem.get(28)%>&VINNO=<%=(String)vecItem.get(29)%>&MODEL=<%=(String)vecItem.get(30)%>&RENTALPRICE=<%=strRentalprice%>&SERVICEPRICE=<%=strServiceprice%>&PURCHASEUOM=<%=(String)vecItem.get(33)%>&SALESUOM=<%=(String)vecItem.get(34)%>&RENTALUOM=<%=(String)vecItem.get(35)%>&SERVICEUOM=<%=(String)vecItem.get(36)%>&INVENTORYUOM=<%=(String)vecItem.get(37)%>&CATALOG=<%=(String)vecItem.get(39)%>"><%=(String)vecItem.get(0)%></a></td> --%>
                <%-- <% if (displaySummaryLink) { %>
                <TD align="left" ><a href ="itemDetail.jsp?action=View&ITEM=<%=(String)vecItem.get(0)%>&DESC=<%=strUtils.replaceCharacters2Send1((String)vecItem.get(1))%>"><%=(String)vecItem.get(0)%></a></td>
                <% } else { %>
                <TD align="left" ><%=(String)vecItem.get(0)%></td>
                <% }%>
                <TD align="left"> <%=(String)vecItem.get(1)%></TD>
                <td class="item-img"><img alt="" src=<%=catalogPath%> width="40px" ></TD> --%>
                <%-- <TD align="left"> <%=StrUtils.fString((String)vecItem.get(4))%></TD> --%>
                <%-- <TD align="left"> <%=StrUtils.fString((String)vecItem.get(5))%></TD> --%>
                <%-- <TD align="left"> <%=StrUtils.fString((String)vecItem.get(3))%></TD>
                <TD align="left"> <%=StrUtils.fString((String)vecItem.get(10))%></TD>
                <TD align="left"> <%=StrUtils.fString((String)vecItem.get(2))%></TD>
                <TD align="left"> <%=StrUtils.fString((String)vecItem.get(19))%></TD> --%>
              <%--   <TD align="left"> <%=StrUtils.fString((String)vecItem.get(33))%></TD> --%>
                <%-- <TD align="right"> <%=new java.math.BigDecimal(strCost).toPlainString()%></TD> --%>
                <%-- <TD align="left"> <%=StrUtils.fString((String)vecItem.get(34))%></TD> --%>
                <%-- <TD align="right"> <%=new java.math.BigDecimal(strListprice).toPlainString()%></TD> --%>
               <%--  <TD align="right"> <%=new java.math.BigDecimal(strMinSelPrice).toPlainString()%></TD> --%>
<%--                 <TD align="left"> <%=StrUtils.fString((String)vecItem.get(35))%></TD>
                <TD align="right"> <%=new java.math.BigDecimal(strRentalprice).toPlainString()%></TD> --%>
               <%--  <TD align="left"> <%=StrUtils.fString((String)vecItem.get(37))%></TD> --%>
                <%-- <TD align="right"> <%=maxStkQty%></TD>
                <TD align="right"> <%=strQty%></TD>
                <TD align="left"> <%=StrUtils.fString((String)vecItem.get(11))%></TD>
                <% if (displaySummaryEdit) { %>
                <TD align="center" class="textbold">&nbsp; <a href="maint_item.jsp?ITEM=<%=(String)vecItem.get(0)%>"%><i class="fa fa-pencil-square-o"></i></a></TD>
                <% } else { %>
                <TD align="center" class="textbold">&nbsp; <a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
                <% }%>
          </TR>
       <%}%> --%>
       <!-- </tbody> -->
    
</table>
</div>
<script>
		var plant = document.form.plant.value;
		var tabletype;
		var PRODUCT,PRODUCTDESC,PRODUCTCLS,PRODUCTDEPT,PRODUCTTYPE,STOCKTYPE,PRODUCTBRAND,start,end, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"PRODUCT":PRODUCT,
				"PRODUCTDESC":PRODUCTDESC,
				"PRODUCTCLS":PRODUCTCLS,
				"PRODUCTDEPT":PRODUCTDEPT,
				"PRODUCTTYPE":PRODUCTTYPE,
				"PRODUCTBRAND":PRODUCTBRAND,
				"STOCKTYPE":STOCKTYPE,
				"start":start,
				"end":end,
				"PLANT":plant,
				"action":"GET_PRODUCT_FOR_SUMMARY"
			}
		}
		function onGo(){
			PRODUCT=document.form.ITEM.value;
			PRODUCTDESC=document.form.ITEM_DESC.value;
			PRODUCTCLS=document.form.PRD_CLS_ID.value;
			PRODUCTDEPT=document.form.PRD_DEPT_ID.value;
			PRODUCTTYPE=document.form.PRD_TYPE_ID.value;
			PRODUCTBRAND=document.form.PRD_BRAND_ID.value;
			STOCKTYPE=document.form.STOCKTYPE.value;
			  //imti start
			 if(STOCKTYPE=="Stock")
				 STOCKTYPE="N";
		    else if(STOCKTYPE=="Non-Stock")
		    	STOCKTYPE="Y";
		    //imti end
			start=1;
			end=<%=listRec%>;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#tabledetail').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
                        searching: true, // Enable searching
			                search: {
			                    regex: true,   // Enable regular expression searching
			                    smart: false   // Disable smart searching for custom matching logic
			                },
						"ajax": {
							"type": "GET",
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
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].ITEM;
						        				var desc = data.CUSTOMERTYPELIST[dataIndex].ITEMDESC;
						        				var catlog = data.CUSTOMERTYPELIST[dataIndex].CATALOG;
						        				var price = data.CUSTOMERTYPELIST[dataIndex].PRICE;
						        				var cost = data.CUSTOMERTYPELIST[dataIndex].COST;
						        				var numberOfDecimal ='<%=numberOfDecimal%>';
						        				price = parseFloat(price).toFixed(numberOfDecimal);
						        				cost = parseFloat(cost).toFixed(numberOfDecimal);
						        				var sno=dataIndex+1;
												<% if (displaySummaryLink) { %>
						        				data.CUSTOMERTYPELIST[dataIndex]['ITEM'] = '<a href="../product/detail?action=View&ITEM='+lineno+'&DESC='+desc+'">'+lineno+'</a>';
						        				<%} %>

							        				<%if(displaySummaryEdit){ %>
							        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="../product/edit?ITEM='+lineno+'"><i class="fa fa-pencil-square-o"></i></a>';
												<%}else{ %>
												data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<%} %>

							        				/* data.CUSTOMERTYPELIST[dataIndex]['IMG'] = '<img alt="" src='+catlog+' width="40px" >'; */
							        				data.CUSTOMERTYPELIST[dataIndex]['IMG'] = '<a href="javascript:void(0);" onclick="showImage(\''+catlog+'\')"><i class="fa fa-eye"></i></a>';
							        				data.CUSTOMERTYPELIST[dataIndex]['price'] = price;
							        				data.CUSTOMERTYPELIST[dataIndex]['cost'] = cost;
						        					data.CUSTOMERTYPELIST[dataIndex]['PRINT'] = '<a href="javascript:void(0);" onclick="showPrintBarcode(\''+lineno+'\',\''+desc+'\',\''+price+'\')"><i class="fa fa-barcode"></i></a>';
							        				 
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": '17', "orderable": true},
				        	{"data": 'ITEM', "orderable": true},
				        	{"data": 'ITEMDESC', "orderable": true},
				        	{"data": 'IMG', "orderable": true},
				        	{"data": '3', "orderable": true},
				        	{"data": '40', "orderable": true},
				        	{"data": '10', "orderable": true},
				        	{"data": '2', "orderable": true},
				        	{"data": '19', "orderable": true},
				        	{"data": 'price', "orderable": true},
				        	{"data": 'cost', "orderable": true},
				        	{"data": '21', "orderable": true},
				        	{"data": '8', "orderable": true},
				        	{"data": '11', "orderable": true},				        		        	
				        	{"data": 'PRINT', "orderable": true},				        		        	
			    			{"data": 'EDIT', "orderable": true},
			    			],
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
				                    		//columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
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
				                    		//columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
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
				                    		//columns: [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
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
			                     	       // doc.content[1].table.widths = Array(doc.content[1].table.body[0].length + 1).join('*').split('');
			                     	       doc.styles.tableFooter.fontSize = 7;
				                    	     },
			                            pageSize: 'A4'
				                    }
				                ]
				            },
				            {
			                    extend: 'colvis',
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
				        "order": [],

					});

			    	$("#tabledetail_filter.dataTables_filter").append($("#searchType"));
                 
 $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
		                tabletype.draw();
		            });
			    }
			    
			}
		

	  
	    $(document).on('click', 'img', function() {
	        var src = $(this).attr('src');
	        
	        $('#imageModal .modal-body img').attr('src', src);
	        $('#imageModal').modal('show');
	    });

		</script>

<input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >

</FORM> 
	  </div>
	  </div>
	  </div>
	         
  <SCRIPT>

  function showImage(src){
	  $('#imageModal .modal-body img').attr('src', src);
      $('#imageModal').modal('show');

	  }

    function validateProduct() {
        var productId = document.form.ITEM.value;
	var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
                                    if (data.status == "100") {
                                            var resultVal = data.result;
                                            document.form.ITEM_DESC.value = resultVal.sItemDesc;
                                     } 
				}
			});
		
	}


              
</SCRIPT>

<script>
$(document).ready(function(){
// 	$('#table').dataTable({
// 		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
// 	});
	//getLocalStorageValue('itemSummary_PRD_CLS_ID', '', 'PRD_CLS_ID');//blocked by Azees -12.09.22
	//getLocalStorageValue('itemSummary_PRD_TYPE_ID', '', 'PRD_TYPE_ID');
	//getLocalStorageValue('itemSummary_PRD_BRAND_ID', '', 'PRD_BRAND_ID');
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
});
</script>  
 
                <!-- Below Jquery Script used for Show/Hide Function-->
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
    
 });
    function showPrintBarcode(item,desc,price)
    {
    	document.printProductBarcodeForm.ITEM_BARCODE.value=item;
    	document.printProductBarcodeForm.ITEM_DESC_BARCODE.value=desc;
    	document.printProductBarcodeForm.UNITPRICE_BARCODE.value=price;
    	document.printProductBarcodeForm.UNITPRICE_BARCODE.value=price;
    	document.printProductBarcodeForm.LabelType.value='<%=LabelType%>';
    	document.printProductBarcodeForm.BarcodeWidth.value='<%=BarcodeWidth%>';
    	document.printProductBarcodeForm.BarcodeHeight.value='<%=BarcodeHeight%>';
    	document.printProductBarcodeForm.FontSize.value='<%=FontSize%>';
    	document.printProductBarcodeForm.TextAlign.value='<%=TextAlign%>';
    	document.printProductBarcodeForm.TextPosition.value='<%=TextPosition%>';
    	document.printProductBarcodeForm.DisplayText.value='<%=DisplayText%>';
    	document.printProductBarcodeForm.CNAME.value='<%=CNAME%>';
    	document.printProductBarcodeForm.printdate.value='<%=printdate%>';
    	ViewPrtBarcode();
    	$("#printProductBarcodeModal").modal();
    }
 
/*  $('#PRD_CLS_ID').on('typeahead:selected', function(evt, item) {//blocked by Azees -12.09.22
	 storeInLocalStorage('itemSummary_PRD_CLS_ID', $('#PRD_CLS_ID').val());	 
 });
 $('#PRD_TYPE_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('itemSummary_PRD_TYPE_ID', $('#PRD_TYPE_ID').val());	 
 });
 $('#PRD_BRAND_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('itemSummary_PRD_BRAND_ID', $('#PRD_BRAND_ID').val());	 
 });
 $('#PRD_DEPT_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('itemSummary_PRD_DEPT_ID', $('#PRD_DEPT_ID').val());	 
 }); */

 function changeitem(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}
 function changeitemdesc(obj){
	 $("#ITEM_DESC").typeahead('val', '"');
	 $("#ITEM_DESC").typeahead('val', '');
	 $("#ITEM_DESC").focus();
	}
 function changeCategory(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}

 </script>
 <jsp:include page="ImageProductModal.jsp"flush="true"></jsp:include>	
<jsp:include page="PrintProductBarcodeModal.jsp"flush="true"></jsp:include>	  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>