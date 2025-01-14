<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.MasterDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.PlantMstDAO"%>

<%
String title = "Generate Product Barcode";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}


function onRePrint(type){
	
	 var ITEM = document.form1.ITEM.value;
	 var ITEM_DESC = document.form1.ITEM_DESC.value;
	 var price   = document.form1.UNITPRICE.value;
	   var QUANTITY = document.form1.QUANTITY.value;
	   
	    if(ITEM == "" || ITEM == null) {alert("Please enter product Barcode");document.form1.ITEM.focus(); return false; }
	    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please enter description");document.form1.ITEM_DESC.focus(); return false; }
	    if(price == "" || price == null) {alert("Please enter price");document.form1.UNITPRICE.focus(); return false; }
	    if(QUANTITY == "" || QUANTITY == null) {alert("Please enter valid print quantity");document.form1.QUANTITY.focus(); return false; }
	    if(!isNumericInput(QUANTITY)){
				alert("Please enter valid print quantity!");
				document.getElementById("QUANTITY").focus();
				document.getElementById("QUANTITY").select();
		         return false;
			}
	    document.form1.action="/track/inhouse/printbarcode?PAGE_TYPE=PRDBARCODE&PRINT_TYPE="+type;
 	    document.form1.submit();
}


function isNumericInput(strString) {
		var strValidChars = "0123456789.-";
		var strChar;
		var blnResult = true;
		if (strString.length == 0)
			return false;
		//  test strString consists of valid characters listed above
		for (var i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}



</script>
<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
String action     =   "";
String ITEM  ="",
       sItemDesc  = "",sQty="" ,SELLING_PRICE, UNITPRICE="",PRINTDATE_LABEL="Mfd.",printdate="yyMMdd",PRINT_WITH_MODEL="0",PRINT_WITH_PLANT="1";;

session= request.getSession();
StrUtils strUtils = new StrUtils();
PrdTypeUtil prdtypeutil = new PrdTypeUtil();
DateUtils dateutils = new DateUtils();

prdtypeutil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
/* String plant = (String)session.getAttribute("PLANT"); */
ITEM  = strUtils.fString(request.getParameter("ITEM"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
sQty  = strUtils.fString(request.getParameter("QUANTITY"));
UNITPRICE  = strUtils.fString(request.getParameter("UNITPRICE"));
SELLING_PRICE  = strUtils.fString(request.getParameter("SELLING_PRICE"));
sQty="1";
SELLING_PRICE="$";
String fieldDesc = (String)request.getAttribute("Msg");
String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(sPlant);
//String printdate=DateUtils.getDateFormatYYMMDD();

ArrayList arrCust =new MasterDAO().getBarcodeProductPrint("Product", sPlant, "");
		if (arrCust.size() > 0) {
            for(int i =0; i<arrCust.size(); i++) {
            	Map arrCustLine = (Map)arrCust.get(i);
            	PRINTDATE_LABEL=((String)arrCustLine.get("PRINT_DATE_LABEL")!= null)? (String)arrCustLine.get("PRINT_DATE_LABEL"):"Mfd";
            	PRINT_WITH_PLANT=((String)arrCustLine.get("PRINT_WITH_PLANT")!= null)?(String)arrCustLine.get("PRINT_WITH_PLANT"):"0";
            	PRINT_WITH_MODEL=((String)arrCustLine.get("PRINT_WITH_MODEL") != null)?(String)arrCustLine.get("PRINT_WITH_MODEL"):"0";
            	printdate=((String)arrCustLine.get("PRINT_DATE")!= null) ?(String)arrCustLine.get("PRINT_DATE"):"yyMMdd";
            	SELLING_PRICE=((String)arrCustLine.get("PRICESYMBOL")!= null) ?(String)arrCustLine.get("PRICESYMBOL"):"$";
            }
		}

%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><a href="../inhouse/genbarcode"><span class="underline-on-hover">Generate Barcode</span></a></li>	
                <li><label>Generate Product Barcode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				 onclick="window.location.href='../inhouse/genbarcode'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


   <form class="form-horizontal" name="form1" method="post">
   
   <div class="form-group">
				 <label class="control-label col-form-label col-sm-4 required" for="Product Barcode">Product Barcode</label>
				 <div class="col-sm-4">
				<input type="text" size="50" MAXLENGTH=250 class="form-control typeahead item" id="ITEM" placeholder="Product" name="ITEM" value=<%=ITEM%>>
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			</div>
			</div>
			
		<div class="form-group">
				 <label class="control-label col-form-label col-sm-4 required" for="Description">Description</label>
		<div class="col-sm-4">
				<input type="text" size="50" MAXLENGTH=250 class="form-control typeahead itemdesc" id="ITEM_DESC" placeholder="Product Description" name="ITEM_DESC" value="<%=sItemDesc%>">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitemdesc(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			</div>
			</div>
			
			<!-- <div class="form-group">
  		 <label class="control-label col-form-label col-sm-4" for="Category">Category</label>
		<div class="col-sm-4">
  		<input type="text" class="form-control typeahead ProductClass" id="PRD_CLS_ID" placeholder="Product Category" name="PRD_CLS_ID">
  		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
  		</div> -->
			<div class="form-group">
  		 <label class="control-label col-form-label col-sm-4" for="Model">Model</label>
		<div class="col-sm-4">
  		<input type="text" class="form-control typeahead" id="MODEL" placeholder="Model" name="MODEL">  		
  		</div>
  		<div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtmodel" name = "printwithtmodel" value = "printwithtmodel" <%if(PRINT_WITH_MODEL.equals("1")) {%>checked <%}%> />Print With Model</label>
      </div>
  		</div>
			<div class="form-group">
  		 <label class="control-label col-form-label col-sm-4" for="Print Date">Print Date</label>
  		 <div class="col-sm-2">
    		<input name="PRINTDATE_LABEL" type="TEXT" value="<%=PRINTDATE_LABEL%>"
			size="20" MAXLENGTH=50 class="form-control">
			
      </div>
		<div class="col-sm-2">
  		<input type="text" class="form-control" id="PRINTDATE" placeholder="Print Date Format yyMMdd" name="PRINTDATE" value="<%=printdate%>" MAXLENGTH=50>
  		</div>
  		<div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "printwithtdate" name = "printwithtdate" value = "printwithtdate" checked />Print With Date</label>
      </div>
  		</div>
			<div class="form-group">
			<label class="control-label col-form-label col-sm-4" ></label>
  		 <label class="radio-inline">
				      	<input type="radio" name="printWithPLANT" type = "radio" value="1" <%if(PRINT_WITH_PLANT.equals("1")) {%>checked <%}%> >Print With Company Name   
				     	</label>
  		 <label class="radio-inline">
				      	<input type="radio" name="printWithPLANT" type = "radio" value="0" <%if(PRINT_WITH_PLANT.equals("0")) {%>checked <%}%> >Print With Company Email & Hand Phone No.
				     	</label>
  		 
  		</div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Description">List Price</label>
      <div class="col-sm-2">
    		<input name="SELLING_PRICE" type="TEXT" value="<%=SELLING_PRICE%>"
			size="20" MAXLENGTH=250 class="form-control">
			
      </div>
      <div class="col-sm-2">
    		<input name="UNITPRICE" type="TEXT" value="<%=UNITPRICE%>"
			size="20" MAXLENGTH=250 class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Quantity">Print Quantity</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="QUANTITY" id="QUANTITY" type="TEXT" value="<%=sQty%>"
			size="50" MAXLENGTH=15>
      </div>
    </div> 
    
      </form>
      </div>
    </div>
</div>


 <div class="form-group">
<div class="col-sm-12" align="center">
<button type="button" class="Submit btn btn-default" value="Print" id="GB30X25" name="action" onclick="javascript:return onRePrint('30X25');"><b>Generate Barcode 30X25 mm</b></button>
<button type="button" class="Submit btn btn-default" value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button>
<button type="button" class="Submit btn btn-default" value="Print" name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button>

</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    //var plant = document.form1.plant.value;
    /* Product Number Auto Suggestion */
	$('.item').typeahead({
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
				PLANT : "<%=sPlant%>",
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
			return '<div onclick="setprdbarcode(\''+data.ITEMDESC+'\',\''+data.UNITPRICE+'\',\''+data.PRD_CLS_ID+'\',\''+data.MODEL+'\')"><p class="item-suggestion">Name: '+data.ITEM+'</p><br/><p class="item-suggestion">DESC: ' + data.ITEMDESC + '</p></div>';
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

	/* Product Description Number Auto Suggestion */
	$('.itemdesc').typeahead({
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
				PLANT : "<%=sPlant%>",
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
			return '<div onclick="setprdescbarcode(\''+data.ITEM+'\',\''+data.UNITPRICE+'\',\''+data.PRD_CLS_ID+'\')"><p>'+data.ITEMDESC+'</p></div>';
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

	/*Search Product Class Id Auto Suggestion */
	$('.ProductClass').typeahead({
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
				PLANT : "<%=sPlant%>",
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
			/*menuElement.after( "<div class=\"PRD_CLS_IDAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('list/classlist_save.jsp?prdclsid='"+form.PRD_CLS_ID.value+");return false;\"> + Add Product Class</a></div>");*/
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
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
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

function setprdbarcode(ITEM_DESC,UNITPRICE,PRD_CLS_ID,MODEL){
	var numberOfDecimal ='<%=numberOfDecimal%>';
	UNITPRICE = parseFloat(UNITPRICE).toFixed(numberOfDecimal);
	$('input[name ="ITEM_DESC"]').val(ITEM_DESC);
	$('input[name ="UNITPRICE"]').val(UNITPRICE);
	$('input[name ="PRD_CLS_ID"]').val(PRD_CLS_ID);
	$('input[name ="MODEL"]').val(MODEL);
}
function setprdescbarcode(ITEM,UNITPRICE,PRD_CLS_ID){	
	$('input[name ="ITEM"]').val(ITEM);
	$('input[name ="UNITPRICE"]').val(UNITPRICE);
	$('input[name ="PRD_CLS_ID"]').val(PRD_CLS_ID);
}
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>