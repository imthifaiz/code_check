<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%
String title = "Create/Edit Bill Of Materials Formula";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>



<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",citem="",qty="",eitem="",remark1="",remark2="",fieldDesc="",allChecked="";
String puom="";
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));
eitem  = strUtils.fString(request.getParameter("EITEM"));
qty  = strUtils.fString(request.getParameter("QTY"));
String PUOM  = strUtils.fString(request.getParameter("PUOM"));


if(action.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
}
else if(action.equalsIgnoreCase("resulterror"))
{
	fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
	fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
}

if(action.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  allChecked = strUtils.fString(request.getParameter("allChecked"));

  
  
}
if(fieldDesc.equals("Kit BOM Added Successfully")){
	response.sendRedirect("../billofmaterials/summary");
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../billofmaterials/summary"><span class="underline-on-hover">Bill Of Materials Formula Summary</span></a></li>                       
                <li><label>Create/Edit Bill Of Materials Formula</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../billofmaterials/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="container-fluid">

  <%--  <CENTER><strong><%=res%></strong></CENTER> --%>

  <form class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?">
 
   <INPUT type="hidden" name="RFLAG" value="1">
   
  <div id = "ERROR_MSG"></div>

    <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Parent Product</label>
    <div class="col-sm-4 ac-box">
				<div class="input-group">   
    		<input type="TEXT"  name="ITEM" id="ITEM" value="<%=pitem%>" onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}"
			size="20" MAXLENGTH=100  class="ac-selected  form-control typeahead">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value+'&TYPE=PARENTITEM');"> 	
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
  		</div>
  		</div>
  		</div>
  		
  		  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Parent Product Description:</label>
					<div class="col-sm-4">
						<input type="text" readonly class="form-control" id="PITEM_DESC" name="PITEM_DESC" value="" >
					</div>
				</div>       
  		
  		
  		<div class="form-group">
      <label class="control-label col-form-label col-sm-2">Parent Quantity</label>
     <div class="col-sm-2">
				<div class="input-group">     
          <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="1" size="4" MAXLENGTH=50 readonly>
			</div>
            </div>
            <div class="col-sm-2">
    	<input type="text" name="PARENTUOM" id="PARENTUOM" class="form-control uomSearch" placeholder="Parent UOM" value="<%=PUOM%>">
    	<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'PARENTUOM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
            
            </div>
		
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Child Product</label>
      <div class="col-sm-4 ac-box">
				<div class="input-group">  
    		<input type="TEXT"  name="CITEM" id="CITEM" value="<%=citem%>" onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}"
			size="20" MAXLENGTH=100 class="form-control">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'CITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"
   		    onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.CITEM.value+'&PITEM='+form.ITEM.value+'&PTYPE='+form.PTYPE.value+'&TYPE=CHILDITEM');"> 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
   		 	
   		 	
  		</div>
  		</div>
  	</div>
  	
  	  		  	<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Child Product Description:</label>
					<div class="col-sm-4">
						<input type="text" readonly class="form-control" id="CITEM_DESC" name="CITEM_DESC" value="" >
					</div>
				</div>  
  		
  		  <div class="form-group">
      <label class="control-label col-form-label col-sm-2 ">Child Quantity</label>
      <div class="col-sm-2">				        
       <INPUT  class="form-control"  name="QTY" type="TEXT" id="QTY" value="<%=qty%>" size="4" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length>0)){validateQuantity();}">      
  		
    </div>
    <div class="col-sm-2">
    	<input type="text" name="CHILDUOM" id="CHILDUOM" class="form-control uomSearch" placeholder="Child UOM">
    	<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'CHILDUOM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
  </div>

    <div class="form-group" style="display: none">
      <label class="control-label col-form-label col-sm-2">Equivalent Product</label>
        <div class="col-sm-4 ac-box">
		<div class="input-group">  
      	<INPUT type="hidden" name="plant" value=<%=plant%>>   
    		<input type="TEXT"  name="EITEM" id="EITEM" value="<%=eitem%>" onkeypress="if((event.keyCode=='13') && ( document.form.EITEM.value.length > 0)){ValidateEquivalentProduct();}"
			size="20" MAXLENGTH=100 class="form-control">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"
   		  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.EITEM.value+'&TYPE=EQUIVALENTITEM');"> <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		</div>
  		
    </div>
    
    
    
   <br>
     <div class="form-group">        
      <div class="col-sm-12" align="center">
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
         <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
       	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
       	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd()">Add to BOM</button>&nbsp;&nbsp;
      </div>
    </div>
    <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<%-- <tr><td align="center"><%=fieldDesc%></td></tr> --%>
	
</table>
</div>
<br>


<!-- <div class="panel panel-default">
<div class="panel-heading" style="background-color: #ffffff " align="center">
<h3 class="panel-title">Child Product Summary</h3> 
</div>
</div> -->

<div class="form-group">
      
      <div class="col-sm-2">    
      <label class="checkbox-inline">      
        <INPUT Type=Checkbox name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);"
			size="50" MAXLENGTH=100 onclick="DisplayAddress();"><b>Select/Unselect</b></label>
			</div>
    </div>
		
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
   <div class="form-group">        
      <div class="col-sm-12" align="center">
				<button class="Submit btn btn-default" type="button" onClick="onDelete()">Remove from  BOM</button>
					</div>
					</div>
	
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">
<INPUT type="hidden" name="SAVE_RED" value="<%=res%>">
    
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();  

    var plant= '<%=plant%>'; 
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
				return '<div onclick="document.form.PARENTUOM.value = \''+data.PURCHASEUOM+'\'"><div onclick="document.form.PITEM_DESC.value = \''+data.ITEMDESC+'\'"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+' </p></div></div>';
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
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			if($(this).val() != "")
				onGo(0);
		});
	
    $('#CITEM').typeahead({
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
				return '<div onclick="document.form.CHILDUOM.value = \''+data.PURCHASEUOM+'\'"><div onclick="document.form.CITEM_DESC.value = \''+data.ITEMDESC+'\'"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+' </p></div></div>';
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

	
    $('#EITEM').typeahead({
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
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' </p></div>';
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
    
    /* To get the suggestion data for Uom */
	$('.uomSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p></div>';
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


<script>
onGo(0);


function onClear()
{
	debugger;
	document.getElementById("ITEM").value = ""
	document.getElementById("CITEM").value = ""
	document.getElementById("QTY").value = ""
	document.getElementById("EITEM").value = ""
	document.getElementById("PITEM_DESC").value = ""
	document.getElementById("CITEM_DESC").value = ""
	document.getElementById("PARENTUOM").value = ""
	document.getElementById("CHILDUOM").value = ""
}
function checkAll(isChk)
{
	var len = document.form.chkitem.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkitem)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkitem.checked = isChk;
              		 
              	}
              	else{
              		document.form.chkitem[i].checked = isChk;
              		 
              	}
            	   	
                
        }
    }
}
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}
	}


	
function ValidateChildProduct() {
	var childproduct = document.form.CITEM.value;
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}
	}
function ValidateEquivalentProduct() {
	var equivalentprod = document.form.EITEM.value;
	if(equivalentprod=="" || equivalentprod.length==0 ) {
		alert("Enter Equivalent Product");
		document.getElementById("EITEM").focus();
	}
	}
function validateQuantity() {
	var qty = document.getElementById("QTY").value;
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("QTY").focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} 

	}
	onAdd();
}
function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for ( var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}


function onAdd() {
	var product = document.form.ITEM.value;
	var puom = document.form.PARENTUOM.value;
	var childproduct = document.form.CITEM.value;
	var cuom = document.form.CHILDUOM.value;
	var qty = document.form.QTY.value;
	var equiitem = document.form.EITEM.value;
	
	
	if(product=="" || product.length==0 ) {
		alert("Enter Parent Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	
	if(puom=="" || puom.length==0 ) {
		alert("Enter Parent UOM");
		document.getElementById("PARENTUOM").focus();
		return false;
	}
	
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
		return false;
	}
	if(cuom=="" || cuom.length==0 ) {
		alert("Enter Child UOM");
		document.getElementById("CHILDUOM").focus();
		return false;
	}
	if(qty=="" || qty.length==0 ) {
		alert("Enter Quantity Per");
		document.getElementById("QTY").focus();
		return false;
	}
	
	if(product==childproduct ) {
		alert("Child Product cannot be same as Parent Product.Choose diff child product.");
		document.form.CITEM.value = "";
		document.getElementById("CITEM").focus();
		return false;
	}
	if(product==equiitem) {
		alert("Equivalent Product cannot be same as Parent Product.Choose diff equivalent product.");
		document.form.EITEM.value = "";
		document.getElementById("EITEM").focus();
		return false;
	}
	if(childproduct==equiitem) {
		alert("Equivalent Product cannot be same as Child Product.Choose diff equivalent product.");
		document.form.EITEM.value = "";
		document.getElementById("EITEM").focus();
		return false;
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,CITEM:childproduct,QTY:qty,EITEM:equiitem,PUOM:puom,CUOM:cuom,action: "ADD_KITBOM",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    //document.form.CITEM.value = "";
    document.form.QTY.value = "";
    //document.form.EITEM.value = "";
    $("#CITEM").typeahead('val', '"');
	$("#CITEM").typeahead('val', '');
	$("#EITEM").typeahead('val', '"');
	$("#EITEM").typeahead('val', '');
	$("#CHILDUOM").typeahead('val', '"');
	$("#CHILDUOM").typeahead('val', '');
}


function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	//var childproduct = document.form.CITEM.value;
	
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_KITBOM_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}

function onDelete()
{
	var checkFound = false;
	var chkitems = document.form.chkitem.value;
	 var len = document.form.chkitem.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
   {
		if(len == 1 && (!document.form.chkitem.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkitem.checked)
	     {
	    	 checkFound = true;
	    	 
	     }
	
	     else {
		     if(document.form.chkitem[i].checked){
		    	 checkFound = true;
		    	 
		     }
	     }
         		
       	     
   }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
	  	  document.form.action="/track/ProductionBomServlet?action=DELETE_KITBOM";
	 	 document.form.submit();
	 	 return true;   
		}
		 else {
				return false;
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
		
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center' class='maingreen'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     //document.form.TRANSACTIONNO.select();
	 //document.form.TRANSACTIONNO.focus();
	 if(errorMsg=="Kit BOM Added Successfully")
 	{
 	//document.form.action  = "../billofmaterials/summary?RESULT=Kit BOM Added Successfully";
 	//document.form.submit();
 	}
      	     
}

function getTable(){
    return '<TABLE class="table table-bordred table-striped">'+
    	   '<tr style="background:#ffffff">'+
     		'<th width="5%"><font >Select</font></th>'+
     		'<th width="5%"><font >No</font></th>'+
     		'<th width="10%"><font >Child Product</font></th>'+
     		'<th width="11%"><font >Child Product Desc</font></th>'+
     		'<th width="13%"><font >Child Product Detail Desc</font></th>'+
     		'<th width="10%">Child UOM</th>'+
     		/* '<th width="10%"><font >Equivalent Product</font></th>'+
     		'<th width="11%"><font >Equivalent Product Desc</font></th>'+
     		'<th width="15%"><font >Equivalent Product Detail Desc</font></th>'+ */
     		'<th width="10%"><font >Child BOM QTY</font></th>'+
     		'</tr>';
   
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>