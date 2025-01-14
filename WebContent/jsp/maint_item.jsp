<%--Commented just to force edit --%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.gates.sqlBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.servlet.ItemMstServlet"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import ="javax.script.ScriptEngineManager"%>
<%@ page import ="javax.script.ScriptEngine"%>
<%@ page import ="javax.script.Invocable"%>

<%
String title = "Edit Product Details";
String currency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String RTYPE = StrUtils.fString(request.getParameter("RTYPE"));
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
<script src="../jsp/js/item.js"></script>
<style>
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -15%;
    top: 10px;
}
</style>
<script type="text/javascript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
   document.form.action  = "../product/new?action=NEW";
   document.form.submit();
}
function onAdd(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
   document.form.action  = "../product/new?action=ADD";
   document.form.submit();
}
function image_edit(){
		$('#userImage').val('');
		var myForm = document.getElementById('form');
		var formData = new FormData(myForm);
	    var userId= form.ITEM.value;
		if(userId){
	    $.ajax({
	        type: 'post',
	        url: "/track/CatalogServlet?Submit=itm_img_edit",
	       	dataType:'html',
	    data:  formData,//{key:val}
	    contentType: false,
	    processData: false,
	      
	        success: function (data) {
	        	console.log(data)
	        	var result =JSON.parse(data).result;
	        	$('#msg').html(result.message); 
	        	$('#productImg').val('');
	        },
	        error: function (data) {
	        	
	            alert(data.responseText);
	        }
	    });
		}else{
			alert("Please enter Product Id");
		}
	        return false; 
	  }
function image_delete(){
	
    //var formData = new FormData($('form')[0]);
    var formData = $('form').serialize();
    var userId= form.ITEM.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=itm_img_delete",
       	dataType:'html',
    data:  formData,//{key:val}
      
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
        	  $('#item_img').attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#item_img0').attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#productImg').val('');
         
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Product Id");
	}
        return false; 
  }	
  
function image_edits(imgcount){
	$('#userImage').val('');
	var myForm = document.getElementById('form');
	var formData = new FormData(myForm);
    var userId= form.ITEM.value;
	if(userId){
	userId = userId+'_'+imgcount;
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=itm_img_edit&ITEMM="+userId+"",
       	dataType:'html',
    data:  formData,//{key:val}
    contentType: false,
    processData: false,
      
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
       	  	$('#productImg'+'_'+imgcount).val('');
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Product Id");
	}
        return false; 
  }
function image_deletes(imgcount){
    var formData = $('form').serialize();
    var userId= form.ITEM.value;
    var delImg= parseFloat(imgcount)-parseFloat(1);
	if(userId){
	userId = userId+'_'+imgcount;
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=itm_img_delete&ITEMM="+userId+"",
       	dataType:'html',
    data:  formData,
      
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
//         	  $('#item_img1').attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#item_img'+delImg).attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#productImg'+'_'+imgcount).val('');
         
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Product Id");
	}
        return false; 
  }	
function isbasicuom(){

	  if (document.getElementById("ISBASICUOM").checked == true) {
		  document.getElementById("ISBASICUOM").value = "1";
		  var basicuom = document.getElementById("Basicuom").value;
		     
			 document.getElementById("purchaseuom").value = basicuom;
			 document.getElementById("salesuom").value = basicuom;
			 document.getElementById("rentaluom").value = basicuom;
			 document.getElementById("serviceuom").value = basicuom;
			 document.getElementById("inventoryuom").value = basicuom;
			 
	    }  else {
	    	 document.getElementById("ISBASICUOM").value = "0";
	    	 document.getElementById("purchaseuom").value = "";
			 document.getElementById("salesuom").value = "";
			 document.getElementById("rentaluom").value = "";
			 document.getElementById("serviceuom").value = "";
			 document.getElementById("inventoryuom").value = "";
	    }
} 

function isposdiscount(){

	  if (document.getElementById("ISPOSDISCOUNT").checked == true) {
		  document.getElementById("ISPOSDISCOUNT").value = "1";
		     
	    }  else {(document.getElementById("ISPOSDISCOUNT").checked == false)
	    	 document.getElementById("ISPOSDISCOUNT").value = "0";
	    }
} 

function isnewarrival(){

	  if (document.getElementById("ISNEWARRIVAL").checked == true) {
		  document.getElementById("ISNEWARRIVAL").value = "1";
		     
	    }  else {(document.getElementById("ISNEWARRIVAL").checked == false)
	    	 document.getElementById("ISNEWARRIVAL").value = "0";
	    }
} 

function istopsellingproduct(){

	  if (document.getElementById("ISTOPSELLING").checked == true) {
		  document.getElementById("ISTOPSELLING").value = "1";
		     
	    }  else {(document.getElementById("ISTOPSELLING").checked == false)
	    	 document.getElementById("ISTOPSELLING").value = "0";
	    }
} 

function UOMpopUpwin(UOMTYPE){
	  var uom = document.form.UOM.value;	
		
		 popUpWin('../jsp/list/uomlist_save.jsp?UOM='+uom+'&UOMTYPE='+UOMTYPE);
	     
		
	}

function onUpdate(){

	var RTYPE= '<%=RTYPE%>';
    var plant   = document.form.plant.value;
    var ITEM   = document.form.ITEM.value;
    var DESC = document.form.DESC.value;
    var uom = document.form.UOM.value;
    var purchaseuom = document.form.PURCHASEUOM.value;
	 var salesuom = document.form.SALESUOM.value;
	 var inventoryuom = document.form.INVENTORYUOM.value;
    var netweight   = document.form.NETWEIGHT.value;
    var grossweight = document.form.GROSSWEIGHT.value;
    var hscode   = document.form.HSCODE.value;
    var coo = document.form.COO.value;
    var isactive = document.form.ACTIVE.value;
    var price = parseFloat(document.getElementById('PRICE').value);
	var mprice = parseFloat(document.getElementById('MINSELLINGPRICE').value);
	var discount=parseFloat(document.getElementById('DISCOUNT').value);
	var minqty = parseFloat(document.form.STKQTY.value);
	var maxqty = parseFloat(document.form.MAXSTKQTY.value);
    var rentalprice = parseFloat(document.getElementById('RENTALPRICE').value);
	var serviceprice = parseFloat(document.getElementById('SERVICEPRICE').value);	
	var flgSuccess="";
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	var vendname = document.form.vendname.value;
	var ISSUPPLIERMANDATORY = document.form.ISSUPPLIERMANDATORY.value;
	
      if(ITEM == "" || ITEM == null)
      {
      alert("Please Enter Product");
      document.form.ITEM.focus(); return false;
      }
  
 
      else if(DESC=="" || DESC == null)
      {
      alert("Please Enter Description");return false;
      }
  
      else if(uom=="" || uom == null)
      {
      alert("Please Enter Base UOM");
      document.form.UOM.focus();return false;
      }
      else if(purchaseuom=="" || purchaseuom == null)
  	{
  		alert("Please Enter Purchase UOM");
  		document.form.PURCHASEUOM.focus();
  		return false;
  	}

  	else if(ISSUPPLIERMANDATORY=="1"){
		 if(vendname == "" || vendname == null){
				alert("Please Select PO Estimate Order Supplier");
				document.form.vendname.focus(); 
				return false;
			
		 }else{

			 }
	 }
  	
  	else if(salesuom=="" || salesuom == null)
  	{
  		alert("Please Enter Sales UOM");
  		document.form.SALESUOM.focus();
  		return false;
  	}
  	else if(inventoryuom=="" || inventoryuom == null)
	{
		alert("Please Enter Inventory UOM");
		document.form.INVENTORYUOM.focus();
		return false;
	}
      else if(!IsNumeric(document.form.NETWEIGHT.value))
  	{
  		alert("Please Enter Valid NetWeight");
  		form.NETWEIGHT.focus();
  		return false;
  	}
	else if(!IsNumeric(document.form.GROSSWEIGHT.value))
  	{
  		alert("Please Enter Valid Grossweight");
  		form.GROSSWEIGHT.focus();
  		return false;
  	}
	else if(!IsNumeric(document.form.STKQTY.value))
	{
		alert("Please Enter Valid Min Stock Quantity");
		form.STKQTY.focus();
		return false;
	}
	else if(!IsNumeric(document.form.MAXSTKQTY.value))
	{
		alert("Please Enter Valid Max Stock Quantity");
		form.MAXSTKQTY.focus();
		return false;
	}
      else if(document.form.PRICE.value == ""){
                alert("Please Enter Price");
                document.form.PRICE.focus();
                return false;
      }	
     
  
    
      else if(!IsNumeric(document.form.PRICE.value))
  	{
  		alert("Please Enter Valid Price");
  		form.PRICE.focus();
  		return false;
  	}
   
    else if(document.form.COST.value == ""){
        alert("Please Enter Cost");
        document.form.COST.focus();
        return false;
    }
      
	
	else if(!IsNumeric(document.form.COST.value))
	{
		alert("Please Enter Valid Cost");
		form.COST.focus();
		return false;
	}
	   else if(!IsNumeric(document.form.MINSELLINGPRICE.value))
	  	{
	  		alert("Please Enter Valid Minimum Selling Price");
	  		form.MINSELLINGPRICE.focus();
	  		return false;
	  	}
	     
	 else if(price < mprice)
 	{
		alert("Price Should not be Less than Min Selling Price");
 	    document.form.PRICE.focus();
 	    return false;
 	}
	else if(!IsNumeric(document.form.PRODGST.value))
	  	{
	  		alert("Please Enter Valid Product VAT");
	  		form.PRODGST.focus();
	  		return false;
	  	}
		
		else if(document.form.RENTALPRICE.value == ""){
	    alert("Please enter Rental Price");
	    document.form.RENTALPRICE.focus();
	    return false;
	  }
	
	else if(!IsNumeric(document.form.RENTALPRICE.value))
	{
		alert("Please Enter Valid Rental Price");
		form.RENTALPRICE.focus();
		return false;
	}
		
		if (!validToThreeDecimal(document.getElementById("NETWEIGHT").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Net Weight (KG)");
			form.NETWEIGHT.focus();
			return false;
		} else if (!validToThreeDecimal(document.getElementById("GROSSWEIGHT").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Gross Weight (KG)");
			form.GROSSWEIGHT.focus();
			return false;
		} 
	else if (!validDecimal(document.getElementById("PRICE").value)) {
		alert("Not more than "+declength+" digits are allowed after decimal value in List Price");
		document.form.PRICE.focus();
		return false;
	}
	else if (!validDecimal(document.getElementById("COST").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Cost");
			document.form.COST.focus();
			return false;
		} else if (!validDecimal(document.getElementById("MINSELLINGPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Minimum Selling Price");
			form.MINSELLINGPRICE.focus();
			return false;
		}else if (!validToThreeDecimal(document.getElementById("PRODGST").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Product VAT");
			form.PRODGST.focus();
			return false;
		}
		/*else if((price-(price*discount)/100)< mprice)
		{
			alert("Please give valid Discount");
		    document.form.DISCOUNT.focus();
		    return false;
		}*/
		
		else if (!validDecimal(document.getElementById("RENTALPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Rental Price");
			document.form.RENTALPRICE.focus();
			return false;
		}/* else if (!validDecimal(document.getElementById("SERVICEPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Service Price");
			document.form.SERVICEPRICE.focus();
			return false;
		} */

		var radio_choice = false;
		for (i = 0; i < document.form.ACTIVE.length; i++) {
			if (document.form.ACTIVE[i].checked)
				radio_choice = true;
		}
		if (!radio_choice) {
			alert("Please select Active or non Active mode.")
			return (false);
		}

		else {
			var val = 0;
			var table = document.getElementById("customerDiscount");
			var rowCount = table.rows.length;
			var discounttype = document.form.OBDISCOUNT.value;
			for (var r = 0, n = table.rows.length; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					var discount = parseFloat(document
							.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r).value);
					var supplier = document.getElementById("CUSTOMER_TYPE_ID_" + r).value;
					if(supplier!="") {
						if(Number.isNaN(discount))
						{
						alert("Please enter customer discount value ");
						document.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r)
								.focus();
						return false;
						}
				} else {
					if(!Number.isNaN(discount))
					{
					alert("Select Customer Type");
					document.getElementById("CUSTOMER_TYPE_ID_" + r)
							.focus();
					return false;
					}
				}
					if (document.form.PRICE.value == ""
							|| document.form.PRICE.value == "0.00"
							|| document.form.PRICE.value == "0") {
						if (discount >= 1) {
							alert("Price Should not be Less than Customer Discount Sales");
							document.form.PRICE.focus();
							return false;
						}
					}
					if (discounttype == "BYPERCENTAGE") {
						if (discount > 100) {
							alert("Customer Discount Sales Order not more than 100%");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
					} else {
						if (discount > parseFloat(document.form.PRICE.value)) {
							alert("Customer Discount Sales Order not more than List Price");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
						if (discount < parseFloat(document
								.getElementById('MINSELLINGPRICE').value)) {
							alert("Customer Discount Sales Order not less than Min Selling Price");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
						var disctprice = document
								.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_"
										+ r).value;
						if (disctprice.indexOf('.') == -1)
							disctprice += ".";
						var disdecNum = disctprice.substring(disctprice
								.indexOf('.') + 1, disctprice.length);

					}
					if (!IsNumeric(document
							.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r).value)) {
						alert("Please Enter Valid Customer Discount Sales Order");
						document.getElementById(
								"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
						return false;
					}

				}
			}

			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						if (document.getElementById("CUSTOMER_TYPE_ID_" + r).value == document
								.getElementById("CUSTOMER_TYPE_ID_" + (r + 1)).value) {
							alert("Customer Type already exists for the product");
							document.getElementById(
									"CUSTOMER_TYPE_ID_" + (r + 1)).focus();
							return false;
						}
					}
				}
			}
			// to validate customer discount sales  end  

			// to validate supplier discount purchase
			var table = document.getElementById("supplierDiscount");
			var rowCount = table.rows.length;
			var discounttype = document.form.IBDISCOUNT.value;
			for (var r = 0, n = table.rows.length; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					
					var discount = parseFloat(document
							.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r).value);
					var supplier = document.getElementById("SUPPLIER_" + r).value;
					if(supplier!="") {
							if(Number.isNaN(discount))
							{
							alert("Please enter supplier discount value ");
							document.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r)
									.focus();
							return false;
							}
					} else {
						if(!Number.isNaN(discount))
						{
						alert("Select Supplier");
						document.getElementById("SUPPLIER_" + r)
								.focus();
						return false;
						}
					}	
					if (document.form.COST.value == ""
							|| document.form.COST.value == "0.00"
							|| document.form.COST.value == "0") {
						if (discount >= 1) {
							alert("Cost Should not be Less than Supplier Discount Purchase");
							document.form.COST.focus();
							return false;
						}
					}
					if (discounttype == "BYPERCENTAGE") {
						if (discount > 100) {
							alert("Supplier Discount Purchase Order not more than 100%");
							document.getElementById(
									"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
							return false;
						}
					} else {
						if (discount > parseFloat(document.form.COST.value)) {
							alert("Supplier Discount Purchase Order not more than COST");
							document.getElementById(
									"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
							return false;
						}
						var disctcost = document
								.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_"
										+ r).value;
						if (disctcost.indexOf('.') == -1)
							disctcost += ".";
						var disdecNum = disctcost.substring(disctcost
								.indexOf('.') + 1, disctcost.length);

					}
					if (!IsNumeric(document
							.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r).value)) {
						alert("Please Enter Valid Supplier Discount Purchase Order");
						document.getElementById(
								"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
						return false;
					}

				}
			}

			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						if (document.getElementById("SUPPLIER_" + r).value == document
								.getElementById("SUPPLIER_" + (r + 1)).value) {
							alert("Supplier already exists for the product");
							document.getElementById("SUPPLIER_" + (r + 1))
									.focus();
							return false;
						}
					}
				}
			}

			// to validate Item supplier 
			var table = document.getElementById("multiitemsup");
			var rowCount = table.rows.length;
			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						if (document.getElementById("ITEMSUPPLIER_" + r).value == document
								.getElementById("ITEMSUPPLIER_" + (r + 1)).value) {
							alert("Supplier already exists for the product");
							document.getElementById("ITEMSUPPLIER_" + (r + 1)).focus();
							return false;
						}
					}
				}
			}
			
			// to validate customer discount sales  end 
			var priceamt = document.getElementById('PRICE').value;
			var mpriceamt = document.getElementById('MINSELLINGPRICE').value;
			var costamt = document.getElementById('COST').value;
			if (priceamt.indexOf('.') == -1)
				priceamt += ".";
			var decNum = priceamt.substring(priceamt.indexOf('.') + 1,
					priceamt.length);

			//to validate inactive product exists in stock
			var errorBoo = false;
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : false,
				data : {
					ITEM : ITEM,
					PLANT : plant,
					ACTION : "CHECK_NONACTIVE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						
						if (isactive == "N") {
						if (data.status == "100") {

							errorBoo = true;     
						}
						} 
						    
					}
				});
			if(errorBoo)
			{
				alert("Product Exists In Inventory Can't Update as Non Active");
				return false;
				
			}

			var chk = confirm("Are you sure you would like to save?");
			if (chk) {
				document.form.DYNAMIC_ALTERNATE_SIZE.value = document
						.getElementById("alternateItemDynamicTable").rows.length;
				document.form.DYNAMIC_CUSTOMERDISCOUNT_SIZE.value = document
						.getElementById("customerDiscount").rows.length;
				document.form.DESCRIPTION_SIZE.value = document
						.getElementById("descriptiontbl").rows.length;
				document.form.PRD_SIZE.value = document
						.getElementById("prdtbl").rows.length;
				document.form.DYNAMIC_SUPPLIERDISCOUNT_SIZE.value = document
						.getElementById("supplierDiscount").rows.length;
				document.form.DYNAMIC_ITEMSUPPLIER_SIZE.value = document
						.getElementById("multiitemsup").rows.length;
				if(RTYPE==="SALES")
				document.form.action = "../product/edit?RTYPE=SALES";
				else if(RTYPE==="INV")
				document.form.action = "../product/edit?RTYPE=INV";
				else
				document.form.action = "../product/edit";
				document.form.submit();
			} else {

				return false;
			}

		}

	}
	function onDelete() {

		var RTYPE='<%=RTYPE%>';
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Enter Prdocuct ID");
			document.form.ITEM.focus();
			return false;
		}
		var delmsg = confirm("Are you sure you would like to delete?");
		if (delmsg) {
			if(RTYPE==="SALES")
			document.form.action = "../product/delete?RTYPE=SALES";
			else if(RTYPE==="INV")
				document.form.action = "../product/delete?RTYPE=INV";
			else 
			document.form.action = "../product/delete";
			document.form.submit();
		} else {
			return false;
		}
	}
	function onClear() {
// 		document.form.ITEM.value = "";
		document.form.DESC.value = "";
		document.form.LOC_0.value = "";
		//document.form.PRD_CLS_ID.selectedIndex = 0;
		//document.form.ARTIST.selectedIndex = 0;
		// Start code added by Deen for product brand on 11/9/12
		//document.form.PRD_BRAND.selectedIndex = 0;
		// End code added by Deen for product brand on 11/9/12
		//document.form.UOM.selectedIndex = 0;
		document.form.REMARKS.value = "";
		//document.form.MANUFACT.value = "";
		document.form.STKQTY.value = "";
		document.form.MAXSTKQTY.value = "";
		document.form.ITEM_CONDITION.value = "";
		document.form.TITLE.value = "";
		document.form.ACTIVE.value = "Y";
		document.form.NETWEIGHT.value = "0.000";
		document.form.GROSSWEIGHT.value = "0.000";
		document.form.HSCODE.value = "";
		document.form.COO.value = "";
		document.form.PRICE.value="0.00000";
		document.form.COST.value="0.00000";
		document.form.MINSELLINGPRICE.value="0.00000";
		document.form.PRODGST.value="0.000";
		document.form.BYCOST.checked=false;
		document.form.BYPRICE.checked=false;
		document.form.DYNAMIC_CUSTOMER_DISCOUNT_0.value="";
		document.form.DYNAMIC_SUPPLIER_DISCOUNT_0.value="";
		document.form.UOM.value="";
		document.form.PRD_CLS_DESC.value="";
		document.form.vendno.value="";
		document.form.vendname.value="";
		document.form.PRD_DEPT_DESC.value="";
// 		document.form.DEPT_DISPLAY_ID.value="";
		document.form.PRD_TYPE_DESC.value="";
		document.form.PRD_BRAND_DESC.value="";
		document.form.VINNO.value = "";
		document.form.DIMENSION.value = "";
		document.form.MODEL.value = "";
		document.form.RENTALPRICE.value="0.00000";
		document.form.SERVICEPRICE.value="0.00000";
		document.form.PURCHASEUOM.value="";
		document.form.SALESUOM.value="";
		document.form.RENTALUOM.value="";
		document.form.SERVICEUOM.value="";
		document.form.INVENTORYUOM.value="";
		document.form.DYNAMIC_ALTERNATE_NAME_1.value="";
		//document.form.ISBASICUOM.checked = false;
		document.form.ISPOSDISCOUNT.checked = false;
		document.form.ISNEWARRIVAL.checked = false;
		document.form.ISTOPSELLING.checked = false;
		document.form.ISCHILDCAL.checked = true;
	}

	function onView() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Product ID");
			document.form.ITEM.focus();
			return false;
		}

		document.form.action = "maint_item.jsp?action=VIEW";
		document.form.submit();
		// alert(document.form.ISPARENTCHILD.value);

	}
	function onViewMapItem() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Product ID");
			document.form.ITEM.focus();
			return false;
		}
		document.form.action = "maint_item.jsp?action=VIEWMAPITEM";
		document.form.submit();
	}
	function onViewItemLoc() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Product ID");
			document.form.ITEM.focus();
			return false;
		}
		document.form.action = "maint_item.jsp?action=VIEWITEMLOC";
		document.form.submit();
	}
	function enableSellingPrice(price){
	 document.form.MINSELLINGPRICE.readOnly =false;
	 validDecimal(price);
	 }
	function validDecimal(str) {

		if (str.indexOf('.') == -1) str += ".";
		var decNum = str.substring(str.indexOf('.')+1, str.length);
		var declength =	parseInt(document.getElementById("numberOfDecimal").value);
		if (decNum.length > declength)
		{
			return false;
			
		}
		return true;
	}function validToThreeDecimal(str) {
		if (str.indexOf('.') == -1)
			str += ".";
		var decNum = str.substring(str.indexOf('.') + 1, str.length);
		 if(decNum.length>3){
			return false;
			
		}
		return true;
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
	String sViewMapItemEnb = "enabled",prdclsid="",clsdesc="",cost="",prdtypeid="",typedesc="",uom="";
	String sItem  = "",sItemDesc  = "", sItemUom   = "",price="",minsprice="", sReOrdQty  = "";
	String action = "",sRemark1   = "" , nonstkflag="" ,sRemark2  = "", sRemark3   = "",prd_cls_id="" ,prd_dept_id="",DEPT_DISPLAY_ID="",prdBrand = "",vendno="",vendname="";
	String sItemCondition = "",sArtist="",sTitle="",sMedium="",sRemark="",sUOM="",stkqty="",isActive="";
	String DYNAMIC_ALTERNATE_SIZE = "",ISPARENTCHILD="",LOC_ID="",LOCID="",prddepid="",nonstk="",nonstkid="",loc="",maxstkqty="",CUSTOMER_TYPE_ID="", DYNAMIC_CUSTOMERDISCOUNT_SIZE="",FLAGSUCCESS="",
		   SUPPLIER="",DYNAMIC_SUPPLIERDISCOUNT_SIZE="",IBDISCOUNT="",NETWEIGHT="",DESCRIPTION_SIZE="",PRD_SIZE="",ISNEWARRIVAL="",ISTOPSELLING="",
		   GROSSWEIGHT="",HSCODE="",COO="",VINNO="",MODEL="",RENTALPRICE="",SERVICEPRICE="",deptdisplay="",PURCHASEUOM="",SALESUOM="",RENTALUOM="",SERVICEUOM="",INVENTORYUOM="",ISCHILDCAL="",ISPOSDISCOUNT="",ISBASICUOM="",sSAVE_RED="",sSAVE_REDELETE="",iscompro="",cppi="",incprice="",incpriceunit="",DIMENSION="",DYNAMIC_ITEMSUPPLIER_SIZE="";
	
	String discount="",PRODGST="",OBDiscounttype="",IBDiscounttype="";
	String plant=(String)session.getAttribute("PLANT");
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
	String taxbylabel = ub.getTaxByLable(plant);
	MovHisDAO mdao = new MovHisDAO(plant);
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	DateUtils dateutils = new DateUtils();
	PrdTypeUtil prdutil = new PrdTypeUtil();
	PrdBrandUtil prdbrandutil = new PrdBrandUtil();
	PrdDeptUtil prddept = new PrdDeptUtil();
	PrdClassUtil prdcls = new PrdClassUtil();
	 ItemMstDAO itemdao = new ItemMstDAO();
	sqlBean sqlbn= new sqlBean();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	itemUtil.setmLogger(mLogger);
	prdutil.setmLogger(mLogger);
	prdcls.setmLogger(mLogger);
	prddept.setmLogger(mLogger);
	action = StrUtils.fString(request.getParameter("action"));
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String username=(String)session.getAttribute("LOGIN_USER");
	sItem     = StrUtils.fString(request.getParameter("ITEM"));
	sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	sItemDesc      = strUtils.replaceCharacters2Recv(sItemDesc);
	prd_cls_id=StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	vendno=StrUtils.fString(request.getParameter("vendno"));
	vendname=StrUtils.fString(request.getParameter("vendname"));
	LOC_ID  = strUtils.fString(request.getParameter("LOC_ID"));
	prd_dept_id=StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
// 	DEPT_DISPLAY_ID=StrUtils.fString(request.getParameter("DEPT_DISPLAY_ID"));
	sArtist = StrUtils.fString(request.getParameter("ARTIST"));
	prdBrand = StrUtils.fString(request.getParameter("PRD_BRAND"));
	sTitle = StrUtils.fString(request.getParameter("TITLE"));
	//sMedium = StrUtils.fString(request.getParameter("MANUFACT"));
	sRemark      = StrUtils.fString(request.getParameter("REMARKS"));
	sItemCondition = StrUtils.fString(request.getParameter("ITEM_CONDITION"));
	sUOM = StrUtils.fString(request.getParameter("UOM"));
	stkqty = StrUtils.fString(request.getParameter("STKQTY"));
	isActive= StrUtils.fString(request.getParameter("ACTIVE"));
	price = StrUtils.fString(request.getParameter("PRICE"));
	minsprice = StrUtils.fString(request.getParameter("MINSELLINGPRICE"));
	cost =  StrUtils.fString(request.getParameter("COST"));
	discount = StrUtils.fString(request.getParameter("DISCOUNT"));
	PRODGST = StrUtils.fString(request.getParameter("PRODGST"));
	nonstkflag = StrUtils.fString(request.getParameter("NONSTOCKFLAG"));
	nonstkid = StrUtils.fString(request.getParameter("NONSTKTYPE"));
	DYNAMIC_ALTERNATE_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_SIZE"));
	DYNAMIC_CUSTOMERDISCOUNT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_CUSTOMERDISCOUNT_SIZE"));
	DESCRIPTION_SIZE = StrUtils.fString(request.getParameter("DESCRIPTION_SIZE"));
	PRD_SIZE = StrUtils.fString(request.getParameter("PRD_SIZE"));
	OBDiscounttype = StrUtils.fString(request.getParameter("OBDISCOUNT"));
	FLAGSUCCESS=StrUtils.fString(request.getParameter("FLAGSUCCESS"));
	loc ="";
	maxstkqty = StrUtils.fString(request.getParameter("MAXSTKQTY"));
	DYNAMIC_SUPPLIERDISCOUNT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIERDISCOUNT_SIZE"));
	DYNAMIC_ITEMSUPPLIER_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_ITEMSUPPLIER_SIZE"));
	IBDiscounttype = StrUtils.fString(request.getParameter("IBDISCOUNT"));
	NETWEIGHT     = StrUtils.fString(request.getParameter("NETWEIGHT"));
	GROSSWEIGHT     = StrUtils.fString(request.getParameter("GROSSWEIGHT"));
	HSCODE     = StrUtils.fString(request.getParameter("HSCODE"));
	COO     = StrUtils.fString(request.getParameter("COO"));
	VINNO     = StrUtils.fString(request.getParameter("VINNO"));
	DIMENSION     = StrUtils.fString(request.getParameter("DIMENSION"));
	MODEL     = StrUtils.fString(request.getParameter("MODEL"));
	RENTALPRICE = StrUtils.fString(request.getParameter("RENTALPRICE"));
	SERVICEPRICE = StrUtils.fString(request.getParameter("SERVICEPRICE"));
	PURCHASEUOM = StrUtils.fString(request.getParameter("PURCHASEUOM"));
	SALESUOM = StrUtils.fString(request.getParameter("SALESUOM"));
	RENTALUOM = StrUtils.fString(request.getParameter("RENTALUOM"));
	SERVICEUOM = StrUtils.fString(request.getParameter("SERVICEUOM"));
	INVENTORYUOM = StrUtils.fString(request.getParameter("INVENTORYUOM"));
	ISCHILDCAL = (request.getParameter("ISCHILDCAL") != null) ? "1": "0";
	ISBASICUOM = (request.getParameter("ISBASICUOM") != null) ? "1": "0";
	ISPOSDISCOUNT  = (request.getParameter("ISPOSDISCOUNT") != null) ? "1": "0";
	ISNEWARRIVAL  = (request.getParameter("ISNEWARRIVAL") != null) ? "1": "0";
	ISTOPSELLING  = (request.getParameter("ISTOPSELLING") != null) ? "1": "0";
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	String SUPPLIER_TYPE_ID = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_IDS"));
	iscompro = strUtils.fString(request.getParameter("ISCOMPRO"));
	if(iscompro.equalsIgnoreCase("NONE"))
		iscompro="0";
	else if(iscompro.equalsIgnoreCase("ISCOMPRO"))
		iscompro="1";
	else
		iscompro="2";
	cppi = strUtils.fString(request.getParameter("CPPI"));
	incprice = strUtils.fString(request.getParameter("INCPRICE"));
	incpriceunit = strUtils.fString(request.getParameter("INCPRICEUNIT"));
	String companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
	String SupValidNumber="";
	CustomerBeanDAO venddao = new CustomerBeanDAO();
	String NOOFSUPPLIER=((String) session.getAttribute("NOOFSUPPLIER"));
	int nosupvalid =venddao.Vendorcount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(nosupvalid>=convl)
		{
			SupValidNumber=NOOFSUPPLIER;
		}
	}
	
// 	List CompanyOutletList = new ArrayList();
// 	List CompanyCompanyByparent= new ParentChildCmpDetDAO().getChildCompanyByparent(plant);
// 	for(int i =0; i<CompanyCompanyByparent.size(); i++) {
// 		Map Comp = (Map)CompanyCompanyByparent.get(i);
// 		String childComp = (String)Comp.get("CHILD_PLANT");
// 		CompanyOutletList= new OutletBeanDAO().getOutletByCompany(sItem, childComp, "");
// 	}


// 	List CompanyOutletList= new OutletBeanDAO().getOutletByCompany(sItem);

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

	List CompanyOutletList= new OutletBeanDAO().getOutletByCompany(sItem,plant);
	List CompanyOutletMinMaxList = new OutletBeanDAO().getOutletByCompanyminmax(sItem,plant);
	if(!empoutlet.isEmpty()) {
		CompanyOutletList= new OutletBeanDAO().getOutletByEmp(sItem,plant,empoutlet);
		CompanyOutletMinMaxList = new OutletBeanDAO().getOutletByCompanyminmaxEmp(sItem,plant,empoutlet);
	}
	
	String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
	 List curQryList=new ArrayList();
		String Supcurrency=plantMstDAO.getBaseCurrency(plant);
		CurrencyDAO currencyDAO = new CurrencyDAO();
	 curQryList = currencyDAO.getCurrencyDetails(currency,plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	        CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
	 }
	 
	float pricef=0;float costf=0;float minspricef=0; float prodgstf=0;  float Rentalpricef=0; float ServicePricef=0;
	float netWeight =  "".equals(NETWEIGHT) ? 0.0f :  Float.parseFloat(NETWEIGHT);
	float grossWeight = "".equals(GROSSWEIGHT) ? 0.0f :Float.parseFloat(GROSSWEIGHT);
	float productVat = "".equals(PRODGST) ? 0.0f :Float.parseFloat(PRODGST);
	float listPrice = "".equals(price) ? 0.0f :Float.parseFloat(price);
	float costValue = "".equals(cost) ? 0.0f :Float.parseFloat(cost);
	float minSellingPriceValue = "".equals(minsprice) ? 0.0f :Float.parseFloat(minsprice);
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
		minsprice="0.00000";
	}if(RentalPrice == 0f){
		RENTALPRICE="0.00000";
	}if(ServicePrice == 0f){
		SERVICEPRICE="0.00000";
	}
	
	
if(price.length()<0){
		price="0";
	}
	
	if(minsprice.length()<0){
			minsprice="0";
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
	
	List prdlist=prdutil.getPrdTypeList("",plant," AND ISACTIVE='Y' ");
	List prdBrandList=prdbrandutil.getPrdBrandList("",plant," AND ISACTIVE ='Y'");
	List prdclslst = prdcls.getPrdTypeList("",plant," AND ISACTIVE='Y' ");
	List prddeplst = prddept.getPrdTypeList("",plant," AND ISACTIVE='Y' ");
        List uomlist = itemUtil.getUomList(plant," AND ISACTIVE='Y' ");
    List nonstklst =   itemUtil.getNonStockList(plant,"");      

	sAddEnb    = "enabled";
	sItemEnb   = "enabled";
        
	
	  if(action.equalsIgnoreCase("UPDATE"))  {
		  try{
	    //sItemEnb = "disabled";
	    sAddEnb  = "disabled";
	    String[] chkdoutlet = request.getParameterValues("checkoutlet");
	    String[] chkddoutlet = request.getParameterValues("checkoutlets");
	    
// 	    String[] newOutletPrice = request.getParameterValues("newprice");
// 	    String[] splant = request.getParameterValues("childcompanyplant");
// 	    String[] sOutlet = request.getParameterValues("childoutlet");
	    if((itemUtil.isExistsItemMst(sItem,plant)))
	    {
	    	 if(loc.length()>0){
	    	 boolean isExistLoc = false;
				LocUtil locUtil = new LocUtil();
				isExistLoc = locUtil.isValidLocInLocmst(plant, loc);
				if(!isExistLoc){
					throw new Exception("Location: " + loc + " is not valid location");
				}
	    	 }
	        Hashtable htCondition = new Hashtable();
	        htCondition.put(IConstants.ITEM,sItem);
	        htCondition.put(IConstants.PLANT,plant);
	        sItemDesc=strUtils.InsertQuotes(sItemDesc);
	        Hashtable htUpdate = new Hashtable();
	        if(stkqty.length()<=0)
	    	{
	    		stkqty="0";
	    	}
	        htUpdate.put(IConstants.ITEM_DESC,sItemDesc);
	        htUpdate.put(IConstants.ITEMMST_REMARK1,strUtils.InsertQuotes(sRemark));
	        htUpdate.put(IConstants.ITEMMST_ITEM_TYPE,sArtist);
	        htUpdate.put(IConstants.PRDBRANDID ,prdBrand);
	     
	        htUpdate.put(IConstants.PRICE,price);//price
	        htUpdate.put("STKUOM",sUOM);
	        htUpdate.put(IConstants.ITEMMST_REMARK4,strUtils.InsertQuotes(sTitle)); //remark4
	        //htUpdate.put(IConstants.ITEMMST_REMARK2,strUtils.InsertQuotes(sMedium)); //remark2
	        htUpdate.put(IConstants.ITEMMST_REMARK3,strUtils.InsertQuotes(sItemCondition)); //remark3
	        htUpdate.put(IConstants.STKQTY,stkqty); //stkqty
	        htUpdate.put(IDBConstants.PRDCLSID,prd_cls_id);	 
	        htUpdate.put(IDBConstants.VENDOR_CODE,vendno);	 
	        htUpdate.put("LOC_ID",LOC_ID);	 
	        htUpdate.put(IDBConstants.PRDDEPTID,prd_dept_id);	 
// 	        htUpdate.put(IDBConstants.DEPTDISPLAY,DEPT_DISPLAY_ID);	 
	        String remark = sRemark+','+sMedium+','+sItemCondition+','+sTitle;
	        htUpdate.put(IDBConstants.PRDCLSID,prd_cls_id);
	        htUpdate.put(IConstants.ISACTIVE,isActive);
	        htUpdate.put(IConstants.MIN_S_PRICE,minsprice);
	        htUpdate.put(IConstants.COST,cost);
	        htUpdate.put(IConstants.DISCOUNT,discount);
	        htUpdate.put(IConstants.PRODGST,PRODGST);
	        htUpdate.put("ITEM_LOC", loc);
	        htUpdate.put("UPBY",username);  
	        htUpdate.put("UPAT",dateutils.getDateTime());
	        htUpdate.put(IConstants.NONSTKFLAG, nonstkflag);
	        htUpdate.put(IConstants.NONSTKTYPEID, nonstkid);
	        htUpdate.put("USERFLD1","N");
	        htUpdate.put(IConstants.MAXSTKQTY,maxstkqty); //maxstkqty
	        htUpdate.put("NETWEIGHT",NETWEIGHT);
	        htUpdate.put("GROSSWEIGHT",GROSSWEIGHT);
	        htUpdate.put("DIMENSION",DIMENSION);
	        htUpdate.put("HSCODE",HSCODE);
	        htUpdate.put("COO",COO);
	        htUpdate.put("VINNO",VINNO);
	        htUpdate.put("MODEL",MODEL);
			htUpdate.put("RENTALPRICE",RENTALPRICE);
	        htUpdate.put("SERVICEPRICE",SERVICEPRICE);
	        htUpdate.put("PURCHASEUOM",PURCHASEUOM);
	        htUpdate.put("SALESUOM",SALESUOM);
	        htUpdate.put("RENTALUOM",RENTALUOM);
	        htUpdate.put("SERVICEUOM",SERVICEUOM);
	        htUpdate.put("INVENTORYUOM",INVENTORYUOM);
	        htUpdate.put("ISCHILDCAL", ISCHILDCAL);
	        htUpdate.put("ISBASICUOM",ISBASICUOM);
	        htUpdate.put("ISPOSDISCOUNT",ISPOSDISCOUNT);
	        htUpdate.put("ISNEWARRIVAL",ISNEWARRIVAL);
	        htUpdate.put("ISTOPSELLING",ISTOPSELLING);
	        htUpdate.put("ISCOMPRO",iscompro);
	        htUpdate.put("CPPI",cppi);
	        htUpdate.put("INCPRICE",incprice);
	        htUpdate.put("INCPRICEUNIT",incpriceunit);
	                
	        mdao.setmLogger(mLogger);
	        Hashtable htm = new Hashtable();
	        htm.put("PLANT",plant);
	        htm.put("DIRTYPE",TransactionConstants.UPD_ITEM);
	        htm.put("RECID","");
	        htm.put("ITEM",sItem);	
	        htm.put("LOC",loc);
	        htm.put("CRBY",username);  htm.put("REMARKS",strUtils.InsertQuotes(remark));  
	        htm.put("UPBY",username);  
	        htm.put("CRAT",dateutils.getDateTime());
	        htm.put("UPAT",dateutils.getDateTime());
	        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
	                                 
	        boolean itemUpdated = itemUtil.updateItem(htUpdate,htCondition);
	        
	          Hashtable htData = new Hashtable();	
	          String posquery="select PLANT,OUTLET from "+plant+"_POSOUTLETS WHERE PLANT ='"+plant+"'";
	          ArrayList posList = new ItemMstUtil().selectForReport(posquery,htData,"");
	          Map pos = new HashMap();
	          if (posList.size() > 0) {
	        	  for (int j=0; j < posList.size(); j++ ) {
	        		  pos = (Map) posList.get(j);
	        		  String outlet = (String) pos.get("OUTLET");
	        		  String posplant = (String) pos.get("PLANT");
	       	       	Hashtable htCond = new Hashtable();
	       	       		htCond.put(IConstants.ITEM,sItem);
    	        		htCond.put(IConstants.PLANT,plant);
    	        		htCond.put("OUTLET",outlet);
	        	    Hashtable hPos = new Hashtable();	
	        		  hPos.put(IConstants.PLANT,plant);
	        		  hPos.put("OUTLET",outlet);
	        		  hPos.put(IConstants.ITEM,sItem);
	        		  hPos.put("SALESUOM",SALESUOM);
	        		  hPos.put(IConstants.PRICE,price);
	        		  hPos.put("CRBY",username);
	        		  hPos.put("CRAT",dateutils.getDateTime());
	        		  if(!(itemUtil.isExistsPosOutletPrice(sItem,outlet,plant))) {
	        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
	        		  }else {
	        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
	        		  }
	        	  }
	          }
	          
	          pos = new HashMap();
	          if (posList.size() > 0) {
	        	  for (int j=0; j < posList.size(); j++ ) {
	        		  pos = (Map) posList.get(j);
	        		  String outlet = (String) pos.get("OUTLET");
	        		  String posplant = (String) pos.get("PLANT");
	       	       	Hashtable htCond = new Hashtable();
	       	       		htCond.put(IConstants.ITEM,sItem);
    	        		htCond.put(IConstants.PLANT,plant);
    	        		htCond.put("OUTLET",outlet);
	        	    Hashtable hPoss = new Hashtable();	
	        		  hPoss.put(IConstants.PLANT,plant);
	        		  hPoss.put("OUTLET",outlet);
	        		  hPoss.put(IConstants.ITEM,sItem);
	        		  hPoss.put("MINQTY",stkqty);
	        		  hPoss.put("MAXQTY",maxstkqty);
	        		  hPoss.put("UPAT",username);
	        		  hPoss.put("UPBY",dateutils.getDateTime());
	        		  if(!(itemUtil.isExistsPosOutletminmax(sItem,outlet,plant))) {
	        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
	        		  }else {
	        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCond);
	        		  }
	        	  }
	          }
	          
	          //update price only in own warehouse & outlet-azees
	          String isprice_updateonly_in_ownoutlet = new PlantMstDAO().getISPRICE_UPDATEONLY_IN_OWNOUTLET(plant);
	          
	        //imthi start ADD PRODUCT to Child based on plantmaster
	          
	          String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
	          boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
	          
	          if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
	        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
	        	  String childplant="";
	        	  Map m = new HashMap();
	        	  if (arrList.size() > 0) {
	        		  for (int i=0; i < arrList.size(); i++ ) {
	        			  m = (Map) arrList.get(i);
	        			  childplant = (String) m.get("CHILD_PLANT");
	        			  if(!(itemUtil.isExistsItemMst(sItem,childplant))) {
	        			  	  htUpdate.put(IConstants.PLANT,childplant);
	        			  	  htUpdate.put(IConstants.ITEM,sItem);
	        				  boolean childitemInserted = itemUtil.insertItem(htUpdate);
				          }else{
				  	        	Hashtable ht = new Hashtable();
					        	ht.put(IConstants.ITEM,sItem);
					        	ht.put(IConstants.PLANT,childplant);
					        	if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
					        		htUpdate.remove(IConstants.PRICE);
	        				  itemUpdated = itemUtil.updateItem(htUpdate,ht);
				          }
	        			  
	        			  //PRD BRAND START
	        			  	Hashtable prdBrands = new Hashtable();
	        			  	Hashtable htBrandtype = new Hashtable();
	        				htBrandtype.clear();
	        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
	        				htBrandtype.put(IDBConstants.PRDBRANDID, prdBrand);
	        				boolean flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
	        				if(prdBrand.length()>0) {
	        				if (flag == false) {
		        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(prdBrand,plant);
		        				if (arrCust.size() > 0) {
		        				String brandId = (String) arrCust.get(0);
		        				String brandDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	        					prdBrands.clear();
	        					prdBrands.put(IDBConstants.PLANT, childplant);
	        					prdBrands.put(IDBConstants.PRDBRANDID, brandId);
	        					prdBrands.put(IDBConstants.PRDBRANDDESC, brandDesc);
	        					prdBrands.put(IConstants.ISACTIVE, isactive);
	        					prdBrands.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        					prdBrands.put(IDBConstants.LOGIN_USER, username);
	        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrands);
		        				}
	        				}
	        				}
	        			  //PRD BRAND END
	        				
	        			  //PRD CLASS START
	        				Hashtable htprdcls = new Hashtable();
	        			  	Hashtable htclass = new Hashtable();
	        			  	htprdcls.clear();
	        			  	htprdcls.put(IDBConstants.PLANT, childplant);
	        				htprdcls.put(IDBConstants.PRDCLSID,prd_cls_id);
	      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
	      					if(prd_cls_id.length()>0) {
	      					if (flag == false) {
	      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(prd_cls_id,plant);
		        				if (arrCust.size() > 0) {
		        				String classId = (String) arrCust.get(0);
		        				String classDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	      						htclass.put(IDBConstants.PLANT, childplant);
	      						htclass.put(IDBConstants.PRDCLSID, classId);
	      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
	      						htclass.put(IConstants.ISACTIVE, isactive);
	      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	      						htclass.put(IDBConstants.LOGIN_USER, username);
	      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
	      						}
	      						}
	      					}
	      				  //PRD CLASS END
	      					
	      				  //PRD TYPE START	
	      					Hashtable htprdtype = new Hashtable();
	        			  	Hashtable htprdtp = new Hashtable();
	        			  	htprdtype.clear();
	      					htprdtype.put(IDBConstants.PLANT, childplant);
	      					htprdtype.put("PRD_TYPE_ID",sArtist);
						    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
						    if(sArtist.length()>0) {
						    if (flag == false) {
						    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(sArtist,plant);
		        				if (arrCust.size() > 0) {
		        				String typeId = (String) arrCust.get(0);
		        				String typedes = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
		        				htprdtp.clear();
						    	htprdtp.put(IDBConstants.PLANT, childplant);
						    	htprdtp.put("PRD_TYPE_ID", typeId);
						    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedes);
						    	htprdtp.put(IConstants.ISACTIVE, isactive);
						    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	htprdtp.put(IDBConstants.LOGIN_USER, username);
               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
		        				}
		        				}
                               }
	      					//PRD TYPE END
						    
						    //PRD DEPT START
						    Hashtable htprddept = new Hashtable();
	        			  	Hashtable htdept = new Hashtable();
	        			  	htprddept.put(IDBConstants.PLANT, childplant);
	        			  	htprddept.put(IDBConstants.PRDDEPTID, prd_dept_id);
	        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
	        			  	if(prd_dept_id.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(prd_dept_id,plant);
		        				if (arrCust.size() > 0) {
		        				String deptId = (String) arrCust.get(0);
		        				String deptdesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
								htdept.put(IDBConstants.PLANT, childplant);
								htdept.put(IDBConstants.PRDDEPTID, deptId);
								htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
								htdept.put(IConstants.ISACTIVE, isactive);
								htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								htdept.put(IDBConstants.LOGIN_USER, username);
								boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
		        				}
		        				}
							}
						   //PRD DEPT END
	        			  	
	        			  //check if Purchase UOM exists 
	        			  	Hashtable htInv = new Hashtable();
	        			  	Hashtable HtPurchaseuom = new Hashtable();
	        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
	        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
	        			  	if(PURCHASEUOM.length()>0) {
	        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END PURCHASE UOM
	        			  	
	        			  //check if Sales UOM exists 
	        			  	Hashtable HtSalesuom = new Hashtable();
	        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
	        			  	HtSalesuom.put("UOM", SALESUOM);
	        			  	if(SALESUOM.length()>0) {
	        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
	        			  	if(SALESUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
		        				}
	        			  	}
	        			  	//END SALES UOM
	        			  	
						   //check if Inventory UOM exists 
	        			  	Hashtable HtInvuom = new Hashtable();
	        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
	        			  	HtInvuom.put("UOM", INVENTORYUOM);
	        			  	flag = new UomUtil().isExistsUom(HtInvuom);
	        			  	if(INVENTORYUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END INV UOM
	        			  	
	        			  	//check if Stk UOM exists 
	        			  	Hashtable HtStkuom = new Hashtable();
	        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
	        			  	HtStkuom.put("UOM", sUOM);
	        			  	flag = new UomUtil().isExistsUom(HtStkuom);
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(sUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
	        			  	}
	        			  	//END STOCK UOM
	        			  	
	        			  //HSCODE
	        			  if(HSCODE.length()>0) {
	        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
							{						
				    			Hashtable htHS = new Hashtable();
				    			htHS.put(IDBConstants.PLANT,childplant);
				    			htHS.put(IDBConstants.HSCODE,HSCODE);
				    			htHS.put(IDBConstants.LOGIN_USER,username);
				    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddHSCODE(htHS);
								boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, childplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",HSCODE);
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        			  	//HSCODE END
	        			  	
	        			  	//COO 
	        			  	if(COO.length()>0) {
	        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
							{						
				    			Hashtable htCoo = new Hashtable();
				    			htCoo.put(IDBConstants.PLANT,childplant);
				    			htCoo.put(IDBConstants.COO,COO);
				    			htCoo.put(IDBConstants.LOGIN_USER,username);
				    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddCOO(htCoo);
								boolean insertflag = new MasterDAO().InsertCOO(htCoo);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, childplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",COO);
								htRecvHis.put(IDBConstants.CREATED_BY,username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        				//COO END
	        				
	        				//SUPPLIER START 
	        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
	        				if (arrCust.size() > 0) {
	        				String sCustCode = (String) arrCust.get(0);
	        				String sCustName = (String) arrCust.get(1);
	        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
	        				{
	        					String sAddr1 = (String) arrCust.get(2);
	        					String sAddr2 = (String) arrCust.get(3);
	        					String sAddr3 = (String) arrCust.get(4);
	        					String sAddr4 = (String) arrCust.get(15);
	        					String sCountry = (String) arrCust.get(5);
	        					String sZip = (String) arrCust.get(6);
	        					String sCons = (String) arrCust.get(7);
	        					String sContactName = (String) arrCust.get(8);
	        					String sDesgination = (String) arrCust.get(9);
	        					String sTelNo = (String) arrCust.get(10);
	        					String sHpNo = (String) arrCust.get(11);
	        					String sEmail = (String) arrCust.get(12);
	        					String sFax = (String) arrCust.get(13);
	        					String sRemarks = (String) arrCust.get(14);
	        					String ISActive = (String) arrCust.get(16);
	        					String sPayTerms = (String) arrCust.get(17);
	        					String sPayMentTerms = (String) arrCust.get(39);
	        					String sPayInDays = (String) arrCust.get(18);
	        					String sState = (String) arrCust.get(19);
	        					String sRcbno = (String) arrCust.get(20);
	        					String CUSTOMEREMAIL = (String) arrCust.get(22);
	        					String WEBSITE = (String) arrCust.get(23);
	        					String FACEBOOK = (String) arrCust.get(24);
	        					String TWITTER = (String) arrCust.get(25);
	        					String LINKEDIN = (String) arrCust.get(26);
	        					String SKYPE = (String) arrCust.get(27);
	        					String OPENINGBALANCE = (String) arrCust.get(28);
	        					String WORKPHONE = (String) arrCust.get(29);
	        					String sTAXTREATMENT = (String) arrCust.get(30);
	        					String sCountryCode = (String) arrCust.get(31);
	        					String sBANKNAME = (String) arrCust.get(32);
	        					String sBRANCH= (String) arrCust.get(33);
	        					String sIBAN = (String) arrCust.get(34);
	        					String sBANKROUTINGCODE = (String) arrCust.get(35);
	        					String companyregnum = (String) arrCust.get(36);
	        					String PEPPOL = (String) arrCust.get(40);
	        					String PEPPOL_ID = (String) arrCust.get(41);
	        					String CURRENCY = (String) arrCust.get(37);
// 	        					String transport = (String) arrCust.get(38);
// 	        					String suppliertypeid = (String) arrCust.get(21);
	        					Hashtable htsup = new Hashtable();
	        					htsup.put(IDBConstants.PLANT,childplant);
	        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
	        					htsup.put(IConstants.VENDOR_NAME, sCustName);
	        					htsup.put(IConstants.companyregnumber,companyregnum);
	        					htsup.put("ISPEPPOL", PEPPOL);
	        					htsup.put("PEPPOL_ID", PEPPOL_ID);
	        					htsup.put("CURRENCY_ID", CURRENCY);
	        					htsup.put(IConstants.NAME, sContactName);
	        					htsup.put(IConstants.DESGINATION, sDesgination);
	        					htsup.put(IConstants.TELNO, sTelNo);
	        					htsup.put(IConstants.HPNO, sHpNo);
	        					htsup.put(IConstants.FAX, sFax);
	        					htsup.put(IConstants.EMAIL, sEmail);
	        					htsup.put(IConstants.ADDRESS1, sAddr1);
	        					htsup.put(IConstants.ADDRESS2, sAddr2);
	        					htsup.put(IConstants.ADDRESS3, sAddr3);
	        					htsup.put(IConstants.ADDRESS4, sAddr4);
	        					if(sState.equalsIgnoreCase("Select State"))
	        						sState="";
	        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
	        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
	        					htsup.put(IConstants.ZIP, sZip);
	        					htsup.put(IConstants.USERFLG1, sCons);
	        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
// 	        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
// 	        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
// 	        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
	        					htsup.put(IConstants.PAYTERMS, "");
			        			htsup.put(IConstants.payment_terms, "");
			        			htsup.put(IConstants.PAYINDAYS, "");
	        					htsup.put(IConstants.ISACTIVE, ISActive);
//	        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//	        					htsup.put(IConstants.TRANSPORTID,transport);
	        					htsup.put(IConstants.SUPPLIERTYPEID,"");
	        					htsup.put(IConstants.TRANSPORTID, "0");
	        					htsup.put(IConstants.RCBNO, sRcbno);
	        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
	        					htsup.put(IConstants.WEBSITE,WEBSITE);
	        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
	        					htsup.put(IConstants.TWITTER,TWITTER);
	        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
	        					htsup.put(IConstants.SKYPE,SKYPE);
	        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
	        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
	        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
	        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        			        	  sBANKNAME="";
// 	        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
	        			          htsup.put(IDBConstants.BANKNAME,"");
	        			          htsup.put(IDBConstants.IBAN,sIBAN);
	        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
	        			          htsup.put("CRAT",new DateUtils().getDateTime());
	        			          htsup.put("CRBY",username);
	        			          htsup.put("Comment1", " 0 ");
	        			          boolean custInserted = new CustUtil().insertVendor(htsup);
	        				}
	        				}
	        				//Supplier END
	        				
				          posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
				          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				       	       	Hashtable htCond = new Hashtable();
				       	       		htCond.put(IConstants.ITEM,sItem);
				       	       		htCond.put(IConstants.PLANT,childplant);
				       	       		htCond.put("OUTLET",outlet);
				       	        Hashtable hPos = new Hashtable();	
				        		  hPos.put(IConstants.PLANT,childplant);
				        		  hPos.put("OUTLET",outlet);
				        		  hPos.put(IConstants.ITEM,sItem);
				        		  hPos.put("SALESUOM",SALESUOM);
				        		  hPos.put("CRBY",username);
				        		  hPos.put("CRAT",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletPrice(sItem,outlet,childplant))) {
				        		  hPos.put(IConstants.PRICE,price);
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
				        		  }else {
				        		  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
				        		  	  hPos.put(IConstants.PRICE,price);
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
				        		  }
				        	  }
				          }
				          
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				       	       	Hashtable htCond = new Hashtable();
				       	       		htCond.put(IConstants.ITEM,sItem);
			    	        		htCond.put(IConstants.PLANT,childplant);
			    	        		htCond.put("OUTLET",outlet);
				        	    Hashtable hPoss = new Hashtable();	
				        		  hPoss.put(IConstants.PLANT,childplant);
				        		  hPoss.put("OUTLET",outlet);
				        		  hPoss.put(IConstants.ITEM,sItem);
				        		  hPoss.put("MINQTY",stkqty);
				        		  hPoss.put("MAXQTY",maxstkqty);
				        		  hPoss.put("UPAT",username);
				        		  hPoss.put("UPBY",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletminmax(sItem,outlet,childplant))) {
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
				        		  }else {
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCond);
				        		  }
				        	  }
				          }
	        		  }
	        	  }
	        	}
	        	  
	          }else if(PARENT_PLANT == null){
	        	  boolean ischild = false;
	        	  
	        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
	        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
	        	  if (arrLi.size() > 0) {
	        	  Map mst = (Map) arrLi.get(0);
	        	  String parent = (String) mst.get("PARENT_PLANT");
	         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  
	        	  if(Ischildasparent){
	        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        			  ischild = true;
	        		  }
	        	  }else{
	        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  ischild = true;
	        	  }
	        	  }
	        	  if(ischild){
	        	  
	        	  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
	        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
	        	  String parentplant = "";
	        	  Map ms = new HashMap();
	        	  if (arrLists.size() > 0) {
	        		  for (int i=0; i < arrLists.size(); i++ ) {
	        			  ms = (Map) arrLists.get(i);
	        			  parentplant = (String) ms.get("PARENT_PLANT");
	        			  if(!(itemUtil.isExistsItemMst(sItem,parentplant))) {
	        			  	  htUpdate.put(IConstants.PLANT,parentplant);
	        			  	  htUpdate.put(IConstants.ITEM,sItem);
	        				  boolean childitemInserted = itemUtil.insertItem(htUpdate);
	        			  }else{
	        				  	Hashtable ht = new Hashtable();
					        	ht.put(IConstants.ITEM,sItem);
					        	ht.put(IConstants.PLANT,parentplant);
					        	if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
					        		htUpdate.remove(IConstants.PRICE);
	        				  itemUpdated = itemUtil.updateItem(htUpdate,ht);
				          }
	        			  
	        			//PRD BRAND START
	        			  	Hashtable prdBrands = new Hashtable();
	        			  	Hashtable htBrandtype = new Hashtable();
	        				htBrandtype.clear();
	        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
	        				htBrandtype.put(IDBConstants.PRDBRANDID, prdBrand);
	        				boolean flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
	        				if(prdBrand.length()>0) {
	        				if (flag == false) {
		        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(prdBrand,plant);
		        				if (arrCust.size() > 0) {
		        				String brandId = (String) arrCust.get(0);
		        				String brandDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	        					prdBrands.clear();
	        					prdBrands.put(IDBConstants.PLANT, parentplant);
	        					prdBrands.put(IDBConstants.PRDBRANDID, brandId);
	        					prdBrands.put(IDBConstants.PRDBRANDDESC, brandDesc);
	        					prdBrands.put(IConstants.ISACTIVE, isactive);
	        					prdBrands.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        					prdBrands.put(IDBConstants.LOGIN_USER, username);
	        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrands);
		        				}
		        				}
	        				}
	        			  //PRD BRAND END
	        				
	        			  //PRD CLASS START
	        				Hashtable htprdcls = new Hashtable();
	        			  	Hashtable htclass = new Hashtable();
	        			  	htprdcls.clear();
	        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
	        				htprdcls.put(IDBConstants.PRDCLSID,prd_cls_id);
	      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
	      					if(prd_cls_id.length()>0) {
	      					if (flag == false) {
	      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(prd_cls_id,plant);
		        				if (arrCust.size() > 0) {
		        				String classId = (String) arrCust.get(0);
		        				String classDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	      						htclass.put(IDBConstants.PLANT, parentplant);
	      						htclass.put(IDBConstants.PRDCLSID, classId);
	      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
	      						htclass.put(IConstants.ISACTIVE, isactive);
	      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	      						htclass.put(IDBConstants.LOGIN_USER, username);
	      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
	      						}
	      						}
	      					}
	      				  //PRD CLASS END
	      					
	      				  //PRD TYPE START	
	      					Hashtable htprdtype = new Hashtable();
	        			  	Hashtable htprdtp = new Hashtable();
	        			  	htprdtype.clear();
	      					htprdtype.put(IDBConstants.PLANT, parentplant);
	      					htprdtype.put("PRD_TYPE_ID",sArtist);
						    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
						    if(sArtist.length()>0) {
						    if (flag == false) {
						    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(sArtist,plant);
		        				if (arrCust.size() > 0) {
		        				String typeId = (String) arrCust.get(0);
		        				String typedes = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
		        				htprdtp.clear();
						    	htprdtp.put(IDBConstants.PLANT, parentplant);
						    	htprdtp.put("PRD_TYPE_ID", typeId);
						    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedes);
						    	htprdtp.put(IConstants.ISACTIVE, isactive);
						    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	htprdtp.put(IDBConstants.LOGIN_USER, username);
               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
		        				}
		        				}
                               }
	      					//PRD TYPE END
						    
						    //PRD DEPT START
						    Hashtable htprddept = new Hashtable();
	        			  	Hashtable htdept = new Hashtable();
	        			  	htprddept.put(IDBConstants.PLANT, parentplant);
	        			  	htprddept.put(IDBConstants.PRDDEPTID, prd_dept_id);
	        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
	        			  	if(prd_dept_id.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(prd_dept_id,plant);
		        				if (arrCust.size() > 0) {
		        				String deptId = (String) arrCust.get(0);
		        				String deptdesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
								htdept.put(IDBConstants.PLANT, parentplant);
								htdept.put(IDBConstants.PRDDEPTID, deptId);
								htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
								htdept.put(IConstants.ISACTIVE, isactive);
								htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								htdept.put(IDBConstants.LOGIN_USER, username);
								boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
		        				}
		        				}
							}
						   //PRD DEPT END
	        			  	
	        			  //check if Purchase UOM exists 
	        			  	Hashtable htInv = new Hashtable();
	        			  	Hashtable HtPurchaseuom = new Hashtable();
	        			  	HtPurchaseuom.put(IDBConstants.PLANT, parentplant);
	        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
	        			  	if(PURCHASEUOM.length()>0) {
	        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, parentplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END PURCHASE UOM
	        			  	
	        			  //check if Sales UOM exists 
	        			  	Hashtable HtSalesuom = new Hashtable();
	        			  	HtSalesuom.put(IDBConstants.PLANT, parentplant);
	        			  	HtSalesuom.put("UOM", SALESUOM);
	        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
	        			  	if(SALESUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, parentplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END SALES UOM
	        			  	
						   //check if Inventory UOM exists 
	        			  	Hashtable HtInvuom = new Hashtable();
	        			  	HtInvuom.put(IDBConstants.PLANT, parentplant);
	        			  	HtInvuom.put("UOM", INVENTORYUOM);
	        			  	flag = new UomUtil().isExistsUom(HtInvuom);
	        			  	if(INVENTORYUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, parentplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END INV UOM
	        			  	
	        			  	//check if Stk UOM exists 
	        			  	Hashtable HtStkuom = new Hashtable();
	        			  	HtStkuom.put(IDBConstants.PLANT, parentplant);
	        			  	HtStkuom.put("UOM", sUOM);
	        			  	flag = new UomUtil().isExistsUom(HtStkuom);
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(sUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, parentplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
	        			  	}
	        			  	//END STOCK UOM
	        			  	
	        			  //HSCODE
	        			  if(HSCODE.length()>0) {
	        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, parentplant)) 
							{						
				    			Hashtable htHS = new Hashtable();
				    			htHS.put(IDBConstants.PLANT,parentplant);
				    			htHS.put(IDBConstants.HSCODE,HSCODE);
				    			htHS.put(IDBConstants.LOGIN_USER,username);
				    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddHSCODE(htHS);
								boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, parentplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",HSCODE);
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        			  	//HSCODE END
	        			  	
	        			  	//COO 
	        			  	if(COO.length()>0) {
	        				if (!new MasterUtil().isExistCOO(COO, parentplant)) 
							{						
				    			Hashtable htCoo = new Hashtable();
				    			htCoo.put(IDBConstants.PLANT,parentplant);
				    			htCoo.put(IDBConstants.COO,COO);
				    			htCoo.put(IDBConstants.LOGIN_USER,username);
				    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddCOO(htCoo);
								boolean insertflag = new MasterDAO().InsertCOO(htCoo);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, parentplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",COO);
								htRecvHis.put(IDBConstants.CREATED_BY,username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        				//COO END
	        				
	        				//SUPPLIER START 
	        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
	        				if (arrCust.size() > 0) {
	        				String sCustCode = (String) arrCust.get(0);
	        				String sCustName = (String) arrCust.get(1);
	        				if (!new CustUtil().isExistVendor(vendno, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
	        				{
	        					String sAddr1 = (String) arrCust.get(2);
	        					String sAddr2 = (String) arrCust.get(3);
	        					String sAddr3 = (String) arrCust.get(4);
	        					String sAddr4 = (String) arrCust.get(15);
	        					String sCountry = (String) arrCust.get(5);
	        					String sZip = (String) arrCust.get(6);
	        					String sCons = (String) arrCust.get(7);
	        					String sContactName = (String) arrCust.get(8);
	        					String sDesgination = (String) arrCust.get(9);
	        					String sTelNo = (String) arrCust.get(10);
	        					String sHpNo = (String) arrCust.get(11);
	        					String sEmail = (String) arrCust.get(12);
	        					String sFax = (String) arrCust.get(13);
	        					String sRemarks = (String) arrCust.get(14);
	        					String ISActive = (String) arrCust.get(16);
	        					String sPayTerms = (String) arrCust.get(17);
	        					String sPayMentTerms = (String) arrCust.get(39);
	        					String sPayInDays = (String) arrCust.get(18);
	        					String sState = (String) arrCust.get(19);
	        					String sRcbno = (String) arrCust.get(20);
	        					String CUSTOMEREMAIL = (String) arrCust.get(22);
	        					String WEBSITE = (String) arrCust.get(23);
	        					String FACEBOOK = (String) arrCust.get(24);
	        					String TWITTER = (String) arrCust.get(25);
	        					String LINKEDIN = (String) arrCust.get(26);
	        					String SKYPE = (String) arrCust.get(27);
	        					String OPENINGBALANCE = (String) arrCust.get(28);
	        					String WORKPHONE = (String) arrCust.get(29);
	        					String sTAXTREATMENT = (String) arrCust.get(30);
	        					String sCountryCode = (String) arrCust.get(31);
	        					String sBANKNAME = (String) arrCust.get(32);
	        					String sBRANCH= (String) arrCust.get(33);
	        					String sIBAN = (String) arrCust.get(34);
	        					String sBANKROUTINGCODE = (String) arrCust.get(35);
	        					String companyregnum = (String) arrCust.get(36);
	        					String PEPPOL = (String) arrCust.get(40);
	        					String PEPPOL_ID = (String) arrCust.get(41);
	        					String CURRENCY = (String) arrCust.get(37);
// 	        					String transport = (String) arrCust.get(38);
// 	        					String suppliertypeid = (String) arrCust.get(21);
	        					Hashtable htsup = new Hashtable();
	        					htsup.put(IDBConstants.PLANT,parentplant);
	        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
	        					htsup.put(IConstants.VENDOR_NAME, sCustName);
	        					htsup.put(IConstants.companyregnumber,companyregnum);
	        					htsup.put("ISPEPPOL", PEPPOL);
	        					htsup.put("PEPPOL_ID", PEPPOL_ID);
	        					htsup.put("CURRENCY_ID", CURRENCY);
	        					htsup.put(IConstants.NAME, sContactName);
	        					htsup.put(IConstants.DESGINATION, sDesgination);
	        					htsup.put(IConstants.TELNO, sTelNo);
	        					htsup.put(IConstants.HPNO, sHpNo);
	        					htsup.put(IConstants.FAX, sFax);
	        					htsup.put(IConstants.EMAIL, sEmail);
	        					htsup.put(IConstants.ADDRESS1, sAddr1);
	        					htsup.put(IConstants.ADDRESS2, sAddr2);
	        					htsup.put(IConstants.ADDRESS3, sAddr3);
	        					htsup.put(IConstants.ADDRESS4, sAddr4);
	        					if(sState.equalsIgnoreCase("Select State"))
	        						sState="";
	        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
	        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
	        					htsup.put(IConstants.ZIP, sZip);
	        					htsup.put(IConstants.USERFLG1, sCons);
	        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
// 	        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
// 	        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
// 	        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
	        					htsup.put(IConstants.PAYTERMS, "");
			        			htsup.put(IConstants.payment_terms, "");
			        			htsup.put(IConstants.PAYINDAYS, "");
	        					htsup.put(IConstants.ISACTIVE, ISActive);
//	        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//	        					htsup.put(IConstants.TRANSPORTID,transport);
	        					htsup.put(IConstants.SUPPLIERTYPEID,"");
	        					htsup.put(IConstants.TRANSPORTID, "0");
	        					htsup.put(IConstants.RCBNO, sRcbno);
	        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
	        					htsup.put(IConstants.WEBSITE,WEBSITE);
	        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
	        					htsup.put(IConstants.TWITTER,TWITTER);
	        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
	        					htsup.put(IConstants.SKYPE,SKYPE);
	        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
	        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
	        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
	        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        			        	  sBANKNAME="";
// 	        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
	        			          htsup.put(IDBConstants.BANKNAME,"");
	        			          htsup.put(IDBConstants.IBAN,sIBAN);
	        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
	        			          htsup.put("CRAT",new DateUtils().getDateTime());
	        			          htsup.put("CRBY",username);
	        			          htsup.put("Comment1", " 0 ");
	        			          boolean custInserted = new CustUtil().insertVendor(htsup);
	        				}
	        				}
	        				//Supplier END
				          posquery="select PLANT,OUTLET from "+parentplant+"_POSOUTLETS WHERE PLANT ='"+parentplant+"'";
				          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				      	       	Hashtable htCond= new Hashtable();
				      	        	htCond.put(IConstants.ITEM,sItem);
				      	        	htCond.put(IConstants.PLANT,parentplant);
				      	        	htCond.put("OUTLET",outlet);
				    	        Hashtable hPos = new Hashtable();	
				        		  hPos.put(IConstants.PLANT,parentplant);
				        		  hPos.put("OUTLET",outlet);
				        		  hPos.put(IConstants.ITEM,sItem);
				        		  hPos.put("SALESUOM",SALESUOM);
				        		  hPos.put("CRBY",username);
				        		  hPos.put("CRAT",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletPrice(sItem,outlet,parentplant))) {
				        		  hPos.put(IConstants.PRICE,price);
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
				        		  }else {
				        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0"))
				        		  		hPos.put(IConstants.PRICE,price);
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
				        		  }
				        	  }
				          }
				          
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				       	       	Hashtable htCond = new Hashtable();
				       	       		htCond.put(IConstants.ITEM,sItem);
			    	        		htCond.put(IConstants.PLANT,parentplant);
			    	        		htCond.put("OUTLET",outlet);
				        	    Hashtable hPoss = new Hashtable();	
				        		  hPoss.put(IConstants.PLANT,parentplant);
				        		  hPoss.put("OUTLET",outlet);
				        		  hPoss.put(IConstants.ITEM,sItem);
				        		  hPoss.put("MINQTY",stkqty);
				        		  hPoss.put("MAXQTY",maxstkqty);
				        		  hPoss.put("UPAT",username);
				        		  hPoss.put("UPBY",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletminmax(sItem,outlet,parentplant))) {
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
				        		  }else {
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCond);
				        		  }
				        	  }
				          }
	        		  }
	        	  }
	        	  
	        	  
	        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
	        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
	        	  Map m = new HashMap();
	        	  if (arrList.size() > 0) {
	        		  for (int i=0; i < arrList.size(); i++ ) {
	        			  m = (Map) arrList.get(i);
	        			  String childplant = (String) m.get("CHILD_PLANT");
	        			  if(childplant!=plant) {
	        			  posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
	        			  if(!(itemUtil.isExistsItemMst(sItem,childplant))) {
	        				  htUpdate.put(IConstants.PLANT,childplant);
	        				  htUpdate.put(IConstants.ITEM,sItem);
	        				  boolean childitemInserted = itemUtil.insertItem(htUpdate);
	        			  }else{
	        				  	Hashtable ht = new Hashtable();
					        	ht.put(IConstants.ITEM,sItem);
					        	ht.put(IConstants.PLANT,childplant);
					        	if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
					        		htUpdate.remove(IConstants.PRICE);
	        				  itemUpdated = itemUtil.updateItem(htUpdate,ht);
				          }
	        			//PRD BRAND START
	        			  	Hashtable prdBrands = new Hashtable();
	        			  	Hashtable htBrandtype = new Hashtable();
	        				htBrandtype.clear();
	        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
	        				htBrandtype.put(IDBConstants.PRDBRANDID, prdBrand);
	        				boolean flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
	        				if(prdBrand.length()>0) {
	        				if (flag == false) {
		        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(prdBrand,plant);
		        				if (arrCust.size() > 0) {
		        				String brandId = (String) arrCust.get(0);
		        				String brandDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	        					prdBrands.clear();
	        					prdBrands.put(IDBConstants.PLANT, childplant);
	        					prdBrands.put(IDBConstants.PRDBRANDID, brandId);
	        					prdBrands.put(IDBConstants.PRDBRANDDESC, brandDesc);
	        					prdBrands.put(IConstants.ISACTIVE, isactive);
	        					prdBrands.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        					prdBrands.put(IDBConstants.LOGIN_USER, username);
	        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrands);
		        				}
		        				}
	        				}
	        			  //PRD BRAND END
	        				
	        			  //PRD CLASS START
	        				Hashtable htprdcls = new Hashtable();
	        			  	Hashtable htclass = new Hashtable();
	        			  	htprdcls.clear();
	        			  	htprdcls.put(IDBConstants.PLANT, childplant);
	        				htprdcls.put(IDBConstants.PRDCLSID,prd_cls_id);
	      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
	      					if(prd_cls_id.length()>0) {
	      					if (flag == false) {
	      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(prd_cls_id,plant);
		        				if (arrCust.size() > 0) {
		        				String classId = (String) arrCust.get(0);
		        				String classDesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
	      						htclass.put(IDBConstants.PLANT, childplant);
	      						htclass.put(IDBConstants.PRDCLSID, classId);
	      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
	      						htclass.put(IConstants.ISACTIVE, isactive);
	      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	      						htclass.put(IDBConstants.LOGIN_USER, username);
	      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
	      						}
	      						}
	      					}
	      				  //PRD CLASS END
	      					
	      				  //PRD TYPE START	
	      					Hashtable htprdtype = new Hashtable();
	        			  	Hashtable htprdtp = new Hashtable();
	        			  	htprdtype.clear();
	      					htprdtype.put(IDBConstants.PLANT, childplant);
	      					htprdtype.put("PRD_TYPE_ID",sArtist);
						    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
						    if(sArtist.length()>0) {
						    if (flag == false) {
						    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(sArtist,plant);
		        				if (arrCust.size() > 0) {
		        				String typeId = (String) arrCust.get(0);
		        				String typedes = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
		        				htprdtp.clear();
						    	htprdtp.put(IDBConstants.PLANT, childplant);
						    	htprdtp.put("PRD_TYPE_ID", typeId);
						    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedes);
						    	htprdtp.put(IConstants.ISACTIVE, isactive);
						    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	htprdtp.put(IDBConstants.LOGIN_USER, username);
               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
		        				}
		        				}
                               }
	      					//PRD TYPE END
						    
						    //PRD DEPT START
						    Hashtable htprddept = new Hashtable();
	        			  	Hashtable htdept = new Hashtable();
	        			  	htprddept.put(IDBConstants.PLANT, childplant);
	        			  	htprddept.put(IDBConstants.PRDDEPTID, prd_dept_id);
	        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
	        			  	if(prd_dept_id.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(prd_dept_id,plant);
		        				if (arrCust.size() > 0) {
		        				String deptId = (String) arrCust.get(0);
		        				String deptdesc = (String) arrCust.get(1);
		        				String isactive = (String) arrCust.get(2);
								htdept.put(IDBConstants.PLANT, childplant);
								htdept.put(IDBConstants.PRDDEPTID, deptId);
								htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
								htdept.put(IConstants.ISACTIVE, isactive);
								htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								htdept.put(IDBConstants.LOGIN_USER, username);
								boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
		        				}
		        				}
							}
						   //PRD DEPT END
	        			  	
	        			  //check if Purchase UOM exists 
	        			  	Hashtable htInv = new Hashtable();
	        			  	Hashtable HtPurchaseuom = new Hashtable();
	        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
	        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
	        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
	        			  	if(PURCHASEUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END PURCHASE UOM
	        			  	
	        			  //check if Sales UOM exists 
	        			  	Hashtable HtSalesuom = new Hashtable();
	        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
	        			  	HtSalesuom.put("UOM", SALESUOM);
	        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
	        			  	if(SALESUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END SALES UOM
	        			  	
						   //check if Inventory UOM exists 
	        			  	Hashtable HtInvuom = new Hashtable();
	        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
	        			  	HtInvuom.put("UOM", INVENTORYUOM);
	        			  	flag = new UomUtil().isExistsUom(HtInvuom);
	        			  	if(INVENTORYUOM.length()>0) {
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
		        				}
	        			  	}
	        			  	//END INV UOM
	        			  	
	        			  	//check if Stk UOM exists 
	        			  	Hashtable HtStkuom = new Hashtable();
	        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
	        			  	HtStkuom.put("UOM", sUOM);
	        			  	flag = new UomUtil().isExistsUom(HtStkuom);
	        			  	if (flag == false) {
	        			  		ArrayList arrCust = new UomUtil().getUomDetails(sUOM,plant);
		        				if (arrCust.size() > 0) {
		        				String uoms = (String) arrCust.get(0);
		        				String uomdesc = (String) arrCust.get(1);
		        				String display = (String) arrCust.get(2);
		        				String qpuom = (String) arrCust.get(3);
		        				String isactive = (String) arrCust.get(4);
	        			  		htInv.clear();
	        			  		htInv.put(IDBConstants.PLANT, childplant);
	        			  		htInv.put("UOM", uoms);
	        			  		htInv.put("UOMDESC", uomdesc);
	        			  		htInv.put("Display", display);
	        			  		htInv.put("QPUOM", qpuom);
	        			  		htInv.put(IConstants.ISACTIVE, isactive);
	        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	        			  		htInv.put(IDBConstants.LOGIN_USER, username);
	        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        				}
	        			  	}
	        			  	//END STOCK UOM
	        			  	
	        			  //HSCODE
	        			  if(HSCODE.length()>0) {
	        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
							{						
				    			Hashtable htHS = new Hashtable();
				    			htHS.put(IDBConstants.PLANT,childplant);
				    			htHS.put(IDBConstants.HSCODE,HSCODE);
				    			htHS.put(IDBConstants.LOGIN_USER,username);
				    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddHSCODE(htHS);
								boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, childplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",HSCODE);
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        			  	//HSCODE END
	        			  	
	        			  	//COO 
	        			  	if(COO.length()>0) {
	        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
							{						
				    			Hashtable htCoo = new Hashtable();
				    			htCoo.put(IDBConstants.PLANT,childplant);
				    			htCoo.put(IDBConstants.COO,COO);
				    			htCoo.put(IDBConstants.LOGIN_USER,username);
				    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//								boolean insertflag = new MasterUtil().AddCOO(htCoo);
								boolean insertflag = new MasterDAO().InsertCOO(htCoo);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, childplant);
								htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",COO);
								htRecvHis.put(IDBConstants.CREATED_BY,username);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
							}
							}
	        				//COO END
	        				
	        				//SUPPLIER START 
	        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
	        				if (arrCust.size() > 0) {
	        				String sCustCode = (String) arrCust.get(0);
	        				String sCustName = (String) arrCust.get(1);
	        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
	        				{
	        					String sAddr1 = (String) arrCust.get(2);
	        					String sAddr2 = (String) arrCust.get(3);
	        					String sAddr3 = (String) arrCust.get(4);
	        					String sAddr4 = (String) arrCust.get(15);
	        					String sCountry = (String) arrCust.get(5);
	        					String sZip = (String) arrCust.get(6);
	        					String sCons = (String) arrCust.get(7);
	        					String sContactName = (String) arrCust.get(8);
	        					String sDesgination = (String) arrCust.get(9);
	        					String sTelNo = (String) arrCust.get(10);
	        					String sHpNo = (String) arrCust.get(11);
	        					String sEmail = (String) arrCust.get(12);
	        					String sFax = (String) arrCust.get(13);
	        					String sRemarks = (String) arrCust.get(14);
	        					String ISActive = (String) arrCust.get(16);
	        					String sPayTerms = (String) arrCust.get(17);
	        					String sPayMentTerms = (String) arrCust.get(39);
	        					String sPayInDays = (String) arrCust.get(18);
	        					String sState = (String) arrCust.get(19);
	        					String sRcbno = (String) arrCust.get(20);
	        					String CUSTOMEREMAIL = (String) arrCust.get(22);
	        					String WEBSITE = (String) arrCust.get(23);
	        					String FACEBOOK = (String) arrCust.get(24);
	        					String TWITTER = (String) arrCust.get(25);
	        					String LINKEDIN = (String) arrCust.get(26);
	        					String SKYPE = (String) arrCust.get(27);
	        					String OPENINGBALANCE = (String) arrCust.get(28);
	        					String WORKPHONE = (String) arrCust.get(29);
	        					String sTAXTREATMENT = (String) arrCust.get(30);
	        					String sCountryCode = (String) arrCust.get(31);
	        					String sBANKNAME = (String) arrCust.get(32);
	        					String sBRANCH= (String) arrCust.get(33);
	        					String sIBAN = (String) arrCust.get(34);
	        					String sBANKROUTINGCODE = (String) arrCust.get(35);
	        					String companyregnum = (String) arrCust.get(36);
	        					String PEPPOL = (String) arrCust.get(40);
	        					String PEPPOL_ID = (String) arrCust.get(41);
	        					String CURRENCY = (String) arrCust.get(37);
// 	        					String transport = (String) arrCust.get(38);
// 	        					String suppliertypeid = (String) arrCust.get(21);
	        					Hashtable htsup = new Hashtable();
	        					htsup.put(IDBConstants.PLANT,childplant);
	        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
	        					htsup.put(IConstants.VENDOR_NAME, sCustName);
	        					htsup.put(IConstants.companyregnumber,companyregnum);
	        					htsup.put("ISPEPPOL", PEPPOL);
	        					htsup.put("PEPPOL_ID", PEPPOL_ID);
	        					htsup.put("CURRENCY_ID", CURRENCY);
	        					htsup.put(IConstants.NAME, sContactName);
	        					htsup.put(IConstants.DESGINATION, sDesgination);
	        					htsup.put(IConstants.TELNO, sTelNo);
	        					htsup.put(IConstants.HPNO, sHpNo);
	        					htsup.put(IConstants.FAX, sFax);
	        					htsup.put(IConstants.EMAIL, sEmail);
	        					htsup.put(IConstants.ADDRESS1, sAddr1);
	        					htsup.put(IConstants.ADDRESS2, sAddr2);
	        					htsup.put(IConstants.ADDRESS3, sAddr3);
	        					htsup.put(IConstants.ADDRESS4, sAddr4);
	        					if(sState.equalsIgnoreCase("Select State"))
	        						sState="";
	        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
	        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
	        					htsup.put(IConstants.ZIP, sZip);
	        					htsup.put(IConstants.USERFLG1, sCons);
	        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
// 	        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
// 	        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
// 	        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
	        					htsup.put(IConstants.PAYTERMS, "");
			        			htsup.put(IConstants.payment_terms, "");
			        			htsup.put(IConstants.PAYINDAYS, "");
	        					htsup.put(IConstants.ISACTIVE, ISActive);
//	        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//	        					htsup.put(IConstants.TRANSPORTID,transport);
	        					htsup.put(IConstants.SUPPLIERTYPEID,"");
	        					htsup.put(IConstants.TRANSPORTID, "0");
	        					htsup.put(IConstants.RCBNO, sRcbno);
	        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
	        					htsup.put(IConstants.WEBSITE,WEBSITE);
	        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
	        					htsup.put(IConstants.TWITTER,TWITTER);
	        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
	        					htsup.put(IConstants.SKYPE,SKYPE);
	        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
	        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
	        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
	        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        			        	  sBANKNAME="";
// 	        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
	        			          htsup.put(IDBConstants.BANKNAME,"");
	        			          htsup.put(IDBConstants.IBAN,sIBAN);
	        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
	        			          htsup.put("CRAT",new DateUtils().getDateTime());
	        			          htsup.put("CRBY",username);
	        			          htsup.put("Comment1", " 0 ");
	        			          boolean custInserted = new CustUtil().insertVendor(htsup);
	        				}
	        				}
	        				//Supplier END
				          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				        	     Hashtable htCond = new Hashtable();
				      	        	htCond.put(IConstants.ITEM,sItem);
				      	        	htCond.put(IConstants.PLANT,childplant);
				      	        	htCond.put("OUTLET",outlet);
				        		  Hashtable hPos = new Hashtable();	
				        		  hPos.put(IConstants.PLANT,childplant);
				        		  hPos.put("OUTLET",outlet);
				        		  hPos.put(IConstants.ITEM,sItem);
				        		  hPos.put("SALESUOM",SALESUOM);
				        		  hPos.put("CRBY",username);
				        		  hPos.put("CRAT",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletPrice(sItem,outlet,childplant))) {
				        		  hPos.put(IConstants.PRICE,price);
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
				        		  }else {
				        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0"))
				        		  		hPos.put(IConstants.PRICE,price);
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
				        		  }
				        	  }
				          }
				          
				          pos = new HashMap();
				          if (posList.size() > 0) {
				        	  for (int j=0; j < posList.size(); j++ ) {
				        		  pos = (Map) posList.get(j);
				        		  String outlet = (String) pos.get("OUTLET");
				        		  String posplant = (String) pos.get("PLANT");
				       	       	Hashtable htCond = new Hashtable();
				       	       		htCond.put(IConstants.ITEM,sItem);
			    	        		htCond.put(IConstants.PLANT,childplant);
			    	        		htCond.put("OUTLET",outlet);
				        	    Hashtable hPoss = new Hashtable();	
				        		  hPoss.put(IConstants.PLANT,childplant);
				        		  hPoss.put("OUTLET",outlet);
				        		  hPoss.put(IConstants.ITEM,sItem);
				        		  hPoss.put("MINQTY",stkqty);
				        		  hPoss.put("MAXQTY",maxstkqty);
				        		  hPoss.put("UPAT",username);
				        		  hPoss.put("UPBY",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletminmax(sItem,outlet,childplant))) {
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
				        		  }else {
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCond);
				        		  }
				        	  }
				          }
				          
	        			  }
	        		  }
	        	  }
	        	  
	        	  }
	          	}
	          }
	        
	        boolean alternateItemRemoved = false;
	       if(itemUtil.isAlternateItemAvalable(plant,sItem)){
	        	alternateItemRemoved= itemUtil.removeAlternateItems(plant,sItem," and ITEM<>ALTERNATE_ITEM_NAME ");
	        	//alternateItemRemoved= itemUtil.removeAlternateItems(plant,sItem);
                        alternateItemRemoved=true;
	        }else{
	        	alternateItemRemoved = true;
	        } 
	        boolean insertAlternateItem = false;
                 List<String> alternateItemNameLists = new ArrayList<String>();
                 
               
	          if(alternateItemRemoved) {
	        	  String alternateItemName = StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_1"));
		        
		        	 System.out.println("DYNAMIC_ALTERNATE_SIZE"+DYNAMIC_ALTERNATE_SIZE);
		        	 int DYNAMIC_ALTERNATE_SIZE_INT = (new Integer(DYNAMIC_ALTERNATE_SIZE)).intValue();
		        	 for(int nameCount = 1; nameCount<=DYNAMIC_ALTERNATE_SIZE_INT;nameCount++){
		        		if(StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_"+nameCount))==""){
		        			break;
		        		}else{
		        			alternateItemNameLists.add(StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_"+nameCount)));
		        		
		        		}
		        	 }

		int index=alternateItemNameLists.indexOf(sItem);
	if(index>=0){
		 alternateItemNameLists.remove(index);
	}
               	 insertAlternateItem = itemUtil.insertAlternateItemLists(plant, sItem, alternateItemNameLists);
               	 
               	if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, sItem, alternateItemNameLists);
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  	if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  insertAlternateItem = itemUtil.insertAlternateItemLists(parentplant, sItem, alternateItemNameLists);
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=plant) {
				        				  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, sItem, alternateItemNameLists);
				        			  }
				        		  	}
				        	  	}
			        	  }	
	             	  }
	          }
               	 
	          }
	          boolean  inserted = false;
	          if(insertAlternateItem) {

	        	  //delete addditional desc
	        	    boolean deleteDesc=itemdao.removeAdditionalDesc(plant,sItem, "");//imti delete addditional desc 24-01-2023
	        	    if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  deleteDesc=itemdao.removeAdditionalDesc(childplant,sItem, "");
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  deleteDesc=itemdao.removeAdditionalDesc(parentplant,sItem, "");
					        		  }
					        	  }
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
					        				  deleteDesc=itemdao.removeAdditionalDesc(childplant,sItem, "");
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             		}
		             	  }
	        	    
	        	  //delete addditional prd
	        	    boolean deletePrd=itemdao.removeAdditionalPrd(plant,sItem, "");//imti delete addditional prd 24-01-2023
	        	    if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  deletePrd=itemdao.removeAdditionalPrd(childplant,sItem, "");
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  deletePrd=itemdao.removeAdditionalPrd(parentplant,sItem, "");
					        		  }
					        	  }
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
					        				  deletePrd=itemdao.removeAdditionalPrd(childplant,sItem, "");
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             		}
		             	  }
	        	    
		        	  //delete customer discount sales
	        	    boolean delete=itemdao.removeCustomerDiscountOB(plant,sItem, "");
	        	    if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  delete=itemdao.removeCustomerDiscountOB(childplant,sItem, "");
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  delete=itemdao.removeCustomerDiscountOB(parentplant,sItem, "");
					        		  }
					        	  }
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
					        				  delete=itemdao.removeCustomerDiscountOB(childplant,sItem, "");
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             		}
		             	  }
	        	  
	        	  
	        	  //end delete customer discount sales
				  
				   //delete Item supplier
		        	    boolean deleteItemSupplier=itemdao.removeItemsupplier(plant,sItem);
		        	    if(PARENT_PLANT != null){
				        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
				        	  if(CHECKplantCompany == null)
				  				CHECKplantCompany = "0";
				        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  Map m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  deleteItemSupplier=itemdao.removeItemsupplier(childplant,sItem);
					        		  	}
					        	  	}
				        	  	}
			             	}else if(PARENT_PLANT == null){
			             		boolean ischild = false;
					        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
					        	  if (arrLi.size() > 0) {
					        	  Map mst = (Map) arrLi.get(0);
					        	  String parent = (String) mst.get("PARENT_PLANT");
					         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
					        	  if(Ischildasparent){
					        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        			  ischild = true;
					        		  }
					        	  }else{
					        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  ischild = true;
					        	  }
					        	  }
					        	  if(ischild){
					        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						        	  String  parentplant ="";
						        	  if (arrLists.size() > 0) {
						        		  for (int i=0; i < arrLists.size(); i++ ) {
						        			  Map ms = (Map) arrLists.get(i);
						        			  parentplant = (String) ms.get("PARENT_PLANT");
						        			  deleteItemSupplier=itemdao.removeItemsupplier(parentplant,sItem);
						        		  }
						        	  }
						        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  Map m = new HashMap();
						        	  if (arrList.size() > 0) {
						        		  for (int i=0; i < arrList.size(); i++ ) {
						        			  m = (Map) arrList.get(i);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  if(childplant!=plant) {
						        				  deleteItemSupplier=itemdao.removeItemsupplier(childplant,sItem);
						        			  }
						        		  	}
						        	  	}
					        	  }	
			             		}
			             	  }
		        	    
			//update Item supplier
		        	    boolean Itemsup=false;  
			        	   int DYNAMIC_ITEMSUPPLIER_SIZE_INT = (new Integer(DYNAMIC_ITEMSUPPLIER_SIZE)).intValue();
			        	   	for(int nameCount = 0; nameCount<=DYNAMIC_ITEMSUPPLIER_SIZE_INT;nameCount++){
				        	if(StrUtils.fString(request.getParameter("ITEMSUPPLIER_"+nameCount))==""){
				        	 			break;
				           		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, sItem);
				    				HM.put(IConstants.VENDNO, StrUtils.fString(request.getParameter("ITEMSUPPLIER_"+nameCount)));
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    Itemsup = itemUtil.insertItemSupplier(HM);
				    			    String prdSup = StrUtils.fString(request.getParameter("ITEMSUPPLIER_"+nameCount));
				    			    boolean itemsupAdded = new ItemMstServlet().additemSupp(plant,sItem,prdSup,username,HM);
				    	     		}
				        	 }//end Item supplier update
				  
	        	  //insert customer discount sales
	        	   boolean OBCustomerDiscount=false;  
	        	   String CUSTOMER_TYPE_DESC;
	        	   int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_CUSTOMERDISCOUNT_SIZE)).intValue();
	        	   System.out.println("DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT : " + DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT);
	        	   	 for(int nameCount = 0; nameCount<=DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT;nameCount++){
		        	if(StrUtils.fString(request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount))==""){
		        	 			break;
		           		}else{
		        			Hashtable HM = new Hashtable();
		        		   	HM.put(IConstants.PLANT, plant);
		    				HM.put(IConstants.ITEM, sItem);
		    				if(OBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")){
		    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount)+"%");
		    				}else{
		    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount));
		    				}
		    				HM.put(IConstants.CUSTOMERTYPEID, StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
		    				CustMstDAO CustMstDAO = new CustMstDAO();
		    				CUSTOMER_TYPE_DESC= CustMstDAO.getCustomerNameByNo(HM);
		    				HM.put(IConstants.CUSTOMERTYPEDESC, CUSTOMER_TYPE_DESC);
		    			    HM.put("CRAT",dateutils.getDateTime());
		    			    HM.put("CRBY",username);
		    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
		    			    String Cust = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount));
		    			    boolean itemsupDiscountAdded = new ItemMstServlet().addCustDiscount(plant,sItem,Cust,username,HM);
		    	     		}
		        	 }
	        	   	 
	        	  //insert add desc
	        	   boolean desc=false;  
	        	   int DESCRIPTION_SIZE_INT = (new Integer(DESCRIPTION_SIZE)).intValue();
	        	   	 for(int nameCount = 0; nameCount<=DESCRIPTION_SIZE_INT;nameCount++){
		        	if(StrUtils.fString(request.getParameter("DESCRIPTION"+nameCount))==""){
		        	 			break;
		           		}else{
		        			Hashtable HM = new Hashtable();
		        		   	HM.put(IConstants.PLANT, plant);
		    				HM.put(IConstants.ITEM, sItem);
		    				HM.put("ITEMDETAILDESC", StrUtils.fString(request.getParameter("DESCRIPTION"+nameCount)));
		    			    HM.put("CRAT",dateutils.getDateTime());
		    			    HM.put("CRBY",username);
		    			    desc = itemUtil.insertDetailDesc(HM);
		    			    
		    			    //IMTHI ADD for CHILD PARENT to insert Additional Detail Description
		    			    if(PARENT_PLANT != null){
					        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
					        	  if(CHECKplantCompany == null)
					  				CHECKplantCompany = "0";
					        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  if (arrList.size() > 0) {
						        		  for (int m=0; m < arrList.size(); m++ ) {
						        			  Map map = (Map) arrList.get(m);
						        			  String childplant = (String) map.get("CHILD_PLANT");
						        			  HM.put(IConstants.PLANT, childplant);
						        			  desc = itemUtil.insertDetailDesc(HM);
						        		  	}
						        	  	}
					        	  	}
		    			    }else if(PARENT_PLANT == null){
		    			    	boolean ischild = false;
						        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						        	  if (arrLi.size() > 0) {
						        	  Map mst = (Map) arrLi.get(0);
						        	  String parent = (String) mst.get("PARENT_PLANT");
						         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
						        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
						        	  if(Ischildasparent){
						        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        			  ischild = true;
						        		  }
						        	  }else{
						        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  ischild = true;
						        	  }
						        	  }
						        	  if(ischild){
						        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
							        	  String  parentplant ="";
							        	  if (arrLists.size() > 0) {
							        		  for (int j=0; j < arrLists.size(); j++ ) {
							        			  Map ms = (Map) arrLists.get(j);
							        			  parentplant = (String) ms.get("PARENT_PLANT");
							        			  HM.put(IConstants.PLANT, parentplant);
							        			  desc = itemUtil.insertDetailDesc(HM);
							        		  }
							        	  }
							        	  
							        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  Map m = new HashMap();
							        	  if (arrList.size() > 0) {
							        		  for (int k=0; k < arrList.size(); k++ ) {
							        			  m = (Map) arrList.get(k);
							        			  String childplant = (String) m.get("CHILD_PLANT");
							        			  if(childplant!=plant) {
							        				  HM.put(IConstants.PLANT, childplant);
							        				  desc = itemUtil.insertDetailDesc(HM);
							        			  }
							        		  	}
							        	  	}
						        	  	}
						        	  }
				             	  }//IMTHI END
		    			    
		    	     		}
		        	 }
	        	   	 
	        	  //insert add prd
	        	   boolean prd=false;  
	        	   int PRD_SIZE_INT = (new Integer(PRD_SIZE)).intValue();
	        	   	 for(int nameCount = 0; nameCount<=PRD_SIZE_INT;nameCount++){
		        	if(StrUtils.fString(request.getParameter("PRODUCT"+nameCount))==""){
		        	 			break;
		           		}else{
		        			Hashtable HM = new Hashtable();
		        		   	HM.put(IConstants.PLANT, plant);
		    				HM.put(IConstants.ITEM, sItem);
		    				HM.put("ADDITIONALITEM", StrUtils.fString(request.getParameter("PRODUCT"+nameCount)));
		    			    HM.put("CRAT",dateutils.getDateTime());
		    			    HM.put("CRBY",username);
		    			    prd = itemUtil.insertAdditionalPrd(HM);
		    			    
		    			    //IMTHI ADD for CHILD PARENT to insert Additional Product
		    			    if(PARENT_PLANT != null){
					        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
					        	  if(CHECKplantCompany == null)
					  				CHECKplantCompany = "0";
					        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  if (arrList.size() > 0) {
						        		  for (int m=0; m < arrList.size(); m++ ) {
						        			  Map map = (Map) arrList.get(m);
						        			  String childplant = (String) map.get("CHILD_PLANT");
						        			  HM.put(IConstants.PLANT, childplant);
						        			  prd = itemUtil.insertAdditionalPrd(HM);
						        		  	}
						        	  	}
					        	  	}
		    			    }else if(PARENT_PLANT == null){
		    			    	boolean ischild = false;
						        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						        	  if (arrLi.size() > 0) {
						        	  Map mst = (Map) arrLi.get(0);
						        	  String parent = (String) mst.get("PARENT_PLANT");
						         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
						        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
						        	  if(Ischildasparent){
						        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        			  ischild = true;
						        		  }
						        	  }else{
						        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  ischild = true;
						        	  }
						        	  }
						        	  if(ischild){
						        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
							        	  String  parentplant ="";
							        	  if (arrLists.size() > 0) {
							        		  for (int j=0; j < arrLists.size(); j++ ) {
							        			  Map ms = (Map) arrLists.get(j);
							        			  parentplant = (String) ms.get("PARENT_PLANT");
							        			  HM.put(IConstants.PLANT, parentplant);
							        			  prd = itemUtil.insertAdditionalPrd(HM);
							        		  }
							        	  }
							        	  
							        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  Map m = new HashMap();
							        	  if (arrList.size() > 0) {
							        		  for (int k=0; k < arrList.size(); k++ ) {
							        			  m = (Map) arrList.get(k);
							        			  String childplant = (String) m.get("CHILD_PLANT");
							        			  if(childplant!=plant) {
							        				  HM.put(IConstants.PLANT, childplant);
							        				  prd = itemUtil.insertAdditionalPrd(HM);
							        			  }
							        		  	}
							        	  	}
						        	  	}
						        	  }
				             	  }//IMTHI END
		    			    
		    	     		}
		        	 }
	   
                    	 //delete supplier discount purchase
		        	    boolean deleteCost=itemdao.removeSupplierDiscountIB(plant,sItem, "");
			             	  
		        	    if(PARENT_PLANT != null){
				        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
				        	  if(CHECKplantCompany == null)
				  				CHECKplantCompany = "0";
				        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  Map m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  deleteCost=itemdao.removeSupplierDiscountIB(childplant,sItem, "");
					        		  	}
					        	  	}
				        	  	}
			             	}else if(PARENT_PLANT == null){
			             		boolean ischild = false;
					        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
					        	  if (arrLi.size() > 0) {
					        	  Map mst = (Map) arrLi.get(0);
					        	  String parent = (String) mst.get("PARENT_PLANT");
					         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
					        	  if(Ischildasparent){
					        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        			  ischild = true;
					        		  }
					        	  }else{
					        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  ischild = true;
					        	  }
					        	  }
					        	  if(ischild){
					        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						        	  String  parentplant ="";
						        	  if (arrLists.size() > 0) {
						        		  for (int i=0; i < arrLists.size(); i++ ) {
						        			  Map ms = (Map) arrLists.get(i);
						        			  parentplant = (String) ms.get("PARENT_PLANT");
						        			  deleteCost=itemdao.removeSupplierDiscountIB(parentplant,sItem, "");
						        		  }
						        	  }
						        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  Map m = new HashMap();
						        	  if (arrList.size() > 0) {
						        		  for (int i=0; i < arrList.size(); i++ ) {
						        			  m = (Map) arrList.get(i);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  if(childplant!=plant) {
						        				  deleteCost=itemdao.removeSupplierDiscountIB(childplant,sItem, "");
						        			  }
						        		  	}
						        	  	}
					        	  }
			             		}
			             	  }
		        	  //end delete customer discount sales
		        	  //insert Supplier discount sales
		        	   boolean IBSupplierDiscount=false;  
		        	   int DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_SUPPLIERDISCOUNT_SIZE)).intValue();
		        	   	for(int nameCount = 0; nameCount<=DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT;nameCount++){
			        	if(StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount))==""){
			        	 			break;
			           		}else{
			           			String SupplierDesc;
			        			Hashtable HM = new Hashtable();
			        		   	HM.put(IConstants.PLANT, plant);
			    				HM.put(IConstants.ITEM, sItem);
			    				HM.put(IConstants.VENDNO, StrUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
			    				VendMstDAO vendMstDAO = new VendMstDAO();
			    				SupplierDesc=vendMstDAO.getVendorNameByNo(HM);
			    				HM.put("VNAME", SupplierDesc);;
			    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
			    				if(IBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")){
			    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount)+"%");
								}else{
			    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount));
								}
			    			    HM.put("CRAT",dateutils.getDateTime());
			    			    HM.put("CRBY",username);
			    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
			    			    String vendor = StrUtils.fString(request.getParameter("SUPPLIER_"+nameCount));
			    			    boolean itemsupDiscountAdded = new ItemMstServlet().addSupDiscount(plant,sItem,vendor,username,HM);
			    	     		}
			        	 }//end insert supplier discount purchase
						 
	            //if(OBCustomerDiscount){
	        	  inserted = mdao.insertIntoMovHis(htm);
	        	  
	        	//IMTHI ADD for CHILD PARENT to insert Activity log
		          	if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int m=0; m < arrList.size(); m++ ) {
				        			  Map map = (Map) arrList.get(m);
				        			  String childplant = (String) map.get("CHILD_PLANT");
				        			  htm.put("PLANT",childplant);
				        			  inserted = mdao.insertIntoMovHis(htm);
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int j=0; j < arrLists.size(); j++ ) {
					        			  Map ms = (Map) arrLists.get(j);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  htm.put("PLANT",parentplant);
					        			  inserted = mdao.insertIntoMovHis(htm);
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int k=0; k < arrList.size(); k++ ) {
					        			  m = (Map) arrList.get(k);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
					        				  htm.put("PLANT",childplant);
					        				  inserted = mdao.insertIntoMovHis(htm);
					        			  }
					        		  	}
					        	  	}
				        	  	}
		             		}
		             	  }//IMTHI END
	            //}
	          }
	           if(COMP_INDUSTRY.equals("Retail")) {
		 		if(chkdoutlet !=null) {
						for(int i =0 ; i < chkdoutlet.length ; i++){
							String j = chkdoutlet[i];
							String newOutletPrices = request.getParameter("newprice_"+j);
							String splants = request.getParameter("childcompanyplant_"+j);
							String sOutlets = request.getParameter("childoutlet_"+j);
							htCondition.clear();
					        htCondition.put(IConstants.ITEM,sItem);
					        htCondition.put(IConstants.PLANT,splants);
					        Hashtable htUptPrice = new Hashtable();
					        htUptPrice.put(IConstants.PRICE,newOutletPrices);
// 					        htUptPrice.put(IConstants.PRICE,newOutletPrice[i]);
			       	       	Hashtable htCond = new Hashtable();
		       	       			htCond.put(IConstants.ITEM,sItem);
	    	        			htCond.put(IConstants.PLANT,splants);
	    	        			htCond.put("OUTLET",sOutlets);
				        	    Hashtable hPos = new Hashtable();
				        	    hPos.put("SALESUOM",SALESUOM);
				        		  hPos.put(IConstants.PLANT,splants);
				        		  hPos.put("OUTLET",sOutlets);
				        		  hPos.put(IConstants.ITEM,sItem);
				        		  hPos.put(IConstants.PRICE,newOutletPrices);
				        		
// 				        		  hPos.put(IConstants.PRICE,newOutletPrice[i]);
				        		  hPos.put("CRBY",username);
				        		  hPos.put("CRAT",dateutils.getDateTime());
				        		  if(!(itemUtil.isExistsPosOutletPrice(sItem,sOutlets,splants))) {
				        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
				        		  }else {
				        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
				        			  itemUpdated = itemUtil.updateItem(htUptPrice,htCondition);
				        		  }
							}
				 	}
	           
	           if(chkddoutlet !=null) {
					for(int i =0 ; i < chkddoutlet.length ; i++){
						String j = chkddoutlet[i];
						String newOutletmin = request.getParameter("MINQTY_"+j);
						String newOutletmax = request.getParameter("MAXQTY_"+j);
						
						String splants = request.getParameter("childcompanyplant_"+j);
						String sOutlets = request.getParameter("childoutlet_"+j);
						htCondition.clear();
				        htCondition.put(IConstants.ITEM,sItem);
				        htCondition.put(IConstants.PLANT,splants);
				        Hashtable htUptPrice = new Hashtable();
//				        htUptPrice.put(IConstants.PRICE,newOutletPrice[i]);
		       	       	Hashtable htCond = new Hashtable();
	       	       			htCond.put(IConstants.ITEM,sItem);
   	        			htCond.put(IConstants.PLANT,splants);
   	        			htCond.put("OUTLET",sOutlets);
			        	    Hashtable hPoss = new Hashtable();	
			        		  hPoss.put(IConstants.PLANT,splants);
			        		  hPoss.put("OUTLET",sOutlets);
			        		  hPoss.put(IConstants.ITEM,sItem);
			        		  
			        		  hPoss.put("MINQTY",newOutletmin);
			        		  hPoss.put("MAXQTY",newOutletmax);
//			        		  hPos.put(IConstants.PRICE,newOutletPrice[i]);
			        		  hPoss.put("CRBY",username);
			        		  hPoss.put("CRAT",dateutils.getDateTime());
			        		  if(!(itemUtil.isExistsPosOutletminmax(sItem,sOutlets,splants))) {
			        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
			        		  }else {
			        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCond);
			        			  
			        		  }
						}
			 	}
	    }
						
	        
	        sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
	        if(itemUpdated&&inserted) {
	                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Product Updated Successfully</font> ";
	                    sSAVE_RED="Employee Updated Successfully";
	                   	            
	          } else {
	                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Product</font>";
	          }
	    	 }else{
	         res = "<font class = "+IConstants.FAILED_COLOR+">Product doesn't not Exists. Try again</font>";
	   }}
      catch(Exception e)
      {
          
                res = "<font class = "+IConstants.FAILED_COLOR+">"+e.getMessage()+"</font>";
      }
		
	} 
	
	else if(action.equalsIgnoreCase("VIEW")){
		try{
		    ArrayList arrItem = itemUtil.getItemDetails(sItem);
		    sItem   = (String)arrItem.get(0);
		    sItemDesc   = (String)arrItem.get(1); 
	   }catch(Exception e)
	   {
	       res="no details found for Product ID : "+  sItem;
	   }
	 
	}
	else if(action.equalsIgnoreCase("PRINT")){
		com.track.dao.LblDet lblDet=new com.track.dao.LblDet();
		sItemDesc=strUtils.InsertQuotes(sItemDesc);
		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT,IConstants.PLANT_VAL);
		ht.put(IConstants.ITEM,sItem);
		ht.put(IConstants.ITEM_DESC,sItemDesc);
		ht.put("SEQNO","");
		ht.put("LBLCOUNT","1");
		ht.put("LBSTATUS","N");
	          
		Hashtable htCond = new Hashtable();
		htCond.put("PLANT",plant);
		htCond.put("ITEM",sItem);
		                       
		if(!(lblDet.isExists(htCond))) // if the item exists already
		{
			boolean itemInserted = lblDet.insertIntoLblDet(ht);
		}
		else
		{
				Hashtable htUpdate = new Hashtable();
				htUpdate.put("LBSTATUS","N");
				lblDet.updateLblDet(htUpdate,htCond);
		}
		
		
	}
        else if(action.equalsIgnoreCase("DELETE")){
        boolean itemDeleted=false;
	    boolean itemProdctuionBOMExist=false;
	    boolean movementhistoryExist=false;
	    Hashtable htmh = new Hashtable();
	    htmh.put(IConstants.ITEM,sItem);
	    htmh.put(IConstants.PLANT,plant);
        movementhistoryExist = mdao.isExisit(htmh," DIRTYPE NOT IN('CREATE_PRODUCT','EDIT_PRODUCT','ADD_ITEM','UPD_ITEM','CNT_ITEM_UPLOAD')");
        Hashtable htinv = new Hashtable();
        htinv.put(IConstants.ITEM,sItem);
        htinv.put(IConstants.PLANT,plant);
        //check in Invmst
        InvMstDAO invdao = new InvMstDAO();
        itemDeleted = invdao.isExisit(htinv," qty>0");
        String tablename =plant+"_PROD_BOM_MST";
        boolean isTableexits = sqlbn.istableExists(tablename);
        if(isTableexits){
 		//check productionbom master
        Hashtable htProductionBom = new Hashtable();
        htProductionBom.put("CITEM",sItem);
        htProductionBom.put(IConstants.PLANT,plant);
        ProductionBomDAO _ProductionBomDAO = new ProductionBomDAO();
        itemProdctuionBOMExist =  _ProductionBomDAO.isExists(htProductionBom);
        System.out.println("isTableexits");
        }
         if(itemDeleted || movementhistoryExist)
        {
           res = "<font class = "+IConstants.FAILED_COLOR+">Product Exists In Inventory or Transactions</font>";
        } else if(itemProdctuionBOMExist){
        	res = "<font class = "+IConstants.FAILED_COLOR+">Product Exists In ProductionBOM</font>";	
        }
	else{
        if(itemUtil.isExistsItemMst(sItem,plant))
        {
          Hashtable htCondition = new Hashtable();
          htCondition.put(IConstants.ITEM,sItem);
          htCondition.put(IConstants.PLANT,plant);
          itemDeleted = itemUtil.deleteItem(htCondition);
          itemDeleted=itemdao.removeAlternateItems(plant,sItem, "");
          
       //delete customer discount sales
   	       boolean delete=itemdao.removeCustomerDiscountOB(plant,sItem, "");
   	  //end delete customer discount sales
   	  
   	    //delete supplier discount purchase
   	   	      boolean deleteCost=itemdao.removeSupplierDiscountIB(plant,sItem, "");
   	  //end delete supplier discount purchase
          
   	  
   	  //imti delete addditional desc 24-01-2023
	        	boolean deleteDesc=itemdao.removeAdditionalDesc(plant,sItem, "");
   	  //imti delete addditional prd 24-01-2023
   	    		boolean deletePrd=itemdao.removeAdditionalPrd(plant,sItem, "");
   	  //imti delete image file in folder path 24-01-2023
				String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				File path = new File(fileLocation);
				File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(sItem) + ".JPEG");
				if (uploadedFile.exists()) {
				uploadedFile.delete();
				}
			List listItem =   new CatalogDAO().getAdditionalImg(plant,sItem);
    		if (listItem.size() > 0) {
    			for (int iCnt =0; iCnt<listItem.size(); iCnt++){
    				Map m=(Map)listItem.get(iCnt);
    				String paths = (String)m.get("CATLOGPATH");
    				File uploadedFiles = new File(paths);
    				if (uploadedFiles.exists()) {
    					uploadedFiles.delete();
    				}
    			}
    		}
   	  //imti delete addditional images 24-01-2023
   	  		  	Hashtable htimg = new Hashtable();
   	  			htimg.put("PRODUCTID", sItem);
   	  			htimg.put("PLANT", plant);
				boolean itemImgDlt = new CatalogDAO().delMst(htimg);
	  //imti END			
   	  
          String remark = sRemark+','+sItemCondition+','+sTitle;
          
             mdao.setmLogger(mLogger);
	        Hashtable htm = new Hashtable();
	        htm.put("PLANT",plant);
	        htm.put("DIRTYPE",TransactionConstants.DEL_ITEM);
	        htm.put("RECID","");
	        htm.put("ITEM",sItem);		
	        htm.put("CRBY",username);  htm.put("REMARKS",strUtils.InsertQuotes(remark));  
	        htm.put("UPBY",username);  
	        htm.put("CRAT",dateutils.getDateTime());
	        htm.put("UPAT",dateutils.getDateTime());
	        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
          if(itemDeleted) {
        	  boolean  inserted = false;
	         
	        	  inserted = mdao.insertIntoMovHis(htm);
	                res = "<font class = "+IConstants.SUCCESS_COLOR+">Product Deleted Successfully</font>";
	                sSAVE_REDELETE ="Delete";
                    sAddEnb    = "disabled";
                    sItem      = "";
                    sItemDesc  = "";
                    sArtist   = "";sUOM="";
                    prdBrand = "";
                    sTitle  = "";
                    //sMedium="";
                    sRemark="";   stkqty=""; sItemCondition="";maxstkqty=""; NETWEIGHT="";GROSSWEIGHT="";HSCODE="";COO="";
                    } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Product</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Product doesn't not Exists. Try again</font>";
    }
  }
    }
	else if (action.equalsIgnoreCase("img_edit")) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		if(isMultipart) {
		UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
		String result = "", strpath = "", catlogpath = "";
		String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
		String filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
		boolean imageSizeflg = false;

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		List items = upload.parseRequest(request);
		Iterator iterator = items.iterator();
		
		plant = StrUtils.fString(
		(String) request.getSession().getAttribute("PLANT")).trim();
		
		String ITEM = "";
		while (iterator.hasNext()) {
	FileItem item = (FileItem) iterator.next();
	if (item.isFormField()) {

		if (item.getFieldName().equalsIgnoreCase("ITEM")) {
			ITEM = StrUtils.fString(item.getString());
		}
	} else if (!item.isFormField() && (item.getName().length() > 0)) {

		String fileName = item.getName();
		long size = item.getSize();

		size = size / 1024;
		// size = size / 1000;
		System.out.println("size of the Image imported :::" + size);
		//checking image size for 2MB
		if (size > 2040) // condtn checking Image size
		{
			result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

			imageSizeflg = true;

		}
		File path = new File(fileLocation);
		if (!path.exists()) {
			boolean status = path.mkdirs();
		}
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");

		// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
		if (uploadedFile.exists()) {
		uploadedFile.delete();
		}
		strpath = path + "/" + fileName;
		catlogpath = uploadedFile.getAbsolutePath();
		if (!imageSizeflg && !uploadedFile.exists())
			item.write(uploadedFile);

		// delete temp uploaded file
		File tempPath = new File(filetempLocation);
		if (tempPath.exists()) {
			File tempUploadedfile = new File(tempPath + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
			if (tempUploadedfile.exists()) {
				tempUploadedfile.delete();
			}
		}
	}
		
		}
		ut.begin();
		 	String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
		 	catlogpath = catlogpath.replace('\\', '/');
			if (ITEM.equals("")) {
				throw new Exception("");
			}
			if (specialcharsnotAllowed.length() > 0) {
				throw new Exception("Product ID  value : '" + ITEM + "' has special characters "
						+ specialcharsnotAllowed + " that are  not allowed ");
			}
			boolean itemInserted = itemUtil.updateItemImage(plant, ITEM, catlogpath);
			if (itemInserted){
				DbBean.CommitTran(ut);

				result = "<font color=\"green\"> Product created successfully</font>";	
			}
			else{
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to create product </font>";
			}	
		}

	} else if (action.equalsIgnoreCase("img_delete")) {

	}
	  
	  ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
		Map plntMap = (Map) plntList.get(0);
		String ISSUPPLIERMANDATORY = (String) plntMap.get("ISSUPPLIERMANDATORY");
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span></a></li>                        
                <li><label>Edit Product Details</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class=" pull-right">
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"  value="productedit"  name="action" onclick="window.location.href='javascript:history.back()'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		 	  </div>
		</div>
				<!-- onclick="window.location.href='../product/summary'"> -->
		
 <div class="box-body">


   <CENTER><strong><font style="font-size:20px;" id="msg"><%=res%></font></strong></CENTER>
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
  <form class="form-horizontal" name="form" method="post" id="form">
<div class="col-sm-8">
<div class="row">
    <div class="form-group">      	
      	<label class="control-label col-form-label col-sm-3 required" for="Product ID">Product Id</label>
      	<div class="col-sm-4">
      	<div class="input-group">
    	<INPUT class="form-control" name="ITEM" type="TEXT" value="<%=sItem%>" size="20"
		onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {getProductDetails();}" MAXLENGTH=50 <%=sItemEnb%> readonly>
  		</div>
  		</div>
  		<div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
  		<label class="radio-inline"> <input type="radio" name="ACTIVE" value="Y" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
										</label> 
		<label class="radio-inline"> <input type="radio"name="ACTIVE" value="N" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
										</label>
     	</div>
		</div>
        </div>
        <INPUT type="hidden" name="plant" value=<%=plant%>> 
 		<INPUT type="hidden" name="ITEM1" value=<%=sItem%>>
 		<INPUT type="hidden" name="CURRENCY" value=<%=currency%>>
 		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
 		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
 		<input type="hidden" name="PRD_CLS_ID" value="<%=prdclsid%>">
 		<input type="hidden" name="PRD_DEPT_ID" value="<%=prddepid%>">
 		<input type="hidden" name="LOC_DESC" value="<%=LOCID%>">
 		<input type="hidden" name="PRD_BRAND" value="<%=prdBrand%>">
 		<input type="hidden" name="ARTIST" value="<%=sArtist%>">
 		<input name="SUPPLIER_TYPE_ID" type="hidden" value="<%=SUPPLIER_TYPE_ID%>">
 		<input name="COMP_INDUSTRY" type="hidden" value="<%=COMP_INDUSTRY%>">
 		<input type = "hidden" name="uomModal">
 		<INPUT type="hidden" name="ISSUPPLIERMANDATORY" id="ISSUPPLIERMANDATORY" value="<%=ISSUPPLIERMANDATORY%>">
 		
 	<div class="form-group">
      <label class="control-label col-form-label col-sm-3 required" for="Product Description">Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="DESC" type="TEXT" value="<%=sItemDesc%>" size="20" MAXLENGTH=100>
      </div>
      <div class="form-inline">
 		<div class="col-sm-3" style="padding: 0px;">
  		<label class="radio-inline"> <input name="NONSTOCKFLAG" type="radio"
													onclick="DisplayNonStkType();" value="N">Stock
												</label> 
		<label class="radio-inline"> <input name="NONSTOCKFLAG" type="radio"
													onclick="DisplayNonStkType();" value="Y">Non Stock
												</label>
     	</div>
		</div>      
    </div>
    <% if(!COMP_INDUSTRY.equals("Retail")) {%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=sRemark%>"	size="20" MAXLENGTH=100>
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
    	<div class="form-inline" style="display: none;">>
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISCHILDCAL" value="ISCHILDCAL" id="ISCHILDCAL" >
                     Is Pack Product</label>
         </div>
	     </div>
      </div>
      <%}else{ %>
      <div class="form-group" style="display:none;">
      <label class="control-label col-form-label col-sm-3" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=sRemark%>"	size="20" MAXLENGTH=100>
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
    	<div class="form-inline" style="display: none;">>
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISCHILDCAL" value="ISCHILDCAL" id="ISCHILDCAL" >
                     Is Pack Product</label>
         </div>
	     </div>
      </div>
      <%} %>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-3 required" for="Basic UOM">Base UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=uom%>" size="20" MAXLENGTH=100 id="Basicuom" placeholder="Select UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'UOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="uombtnpop"></i></span>
      </div>
      <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISBASICUOM" value="ISBASICUOM" id="ISBASICUOM" onclick="isbasicuom();">
                     Apply to all UOM</label>
         </div>
	     </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Net Weight">Net Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="NETWEIGHT" id="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>" style="width:100%" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
      <% if(COMP_INDUSTRY.equals("Retail")) {%>
	<div class="form-inline">
	  <div class="col-sm-3" style="padding: 0px;">
	  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISNEWARRIVAL" value="ISNEWARRIVAL" id="ISNEWARRIVAL" onclick="isnewarrival();"/>New Arrival</label>
      </div>
	</div>
	<%}else{%>
	<div class="form-inline" style="display:none;">
	  <div class="col-sm-3" style="padding: 0px;">
	  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISNEWARRIVAL" value="ISNEWARRIVAL" id="ISNEWARRIVAL" onclick="isnewarrival();"/>New Arrival</label>
      </div>
	</div>
	<%}%>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Gross Weight">Gross Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="GROSSWEIGHT" id="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
      <% if(COMP_INDUSTRY.equals("Retail")) {%>
      	<div class="form-inline">
	  <div class="col-sm-3" style="padding: 0px;">
	  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISTOPSELLING" value="ISTOPSELLING" id="ISTOPSELLING" onclick="istopsellingproduct();"/>Top Selling Product</label>
      </div>
	</div>
	<%}else{%>
      	<div class="form-inline" style="display:none;">
	  <div class="col-sm-3" style="padding: 0px;">
	  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISTOPSELLING" value="ISTOPSELLING" id="ISTOPSELLING" onclick="istopsellingproduct();"/>Top Selling Product</label>
      </div>
	</div>
	<%}%>
    </div>
       <%  if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { %>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Location">Location</label>
      <div class="col-sm-4" >                
			<INPUT class="form-control" name="LOC_ID" type="TEXT" value="<%=LOC_ID%>" size="20" MAXLENGTH=100 id="LOC_ID" placeholder="Select Location">
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
			<INPUT class="form-control" name="DIMENSION" type="TEXT" value="<%=strUtils.forHTMLTag(DIMENSION)%>" style="width:100%" MAXLENGTH=50>				
      </div>
      <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" onclick="isposdiscount();"/> 
	     Allow POS Terminal Discount</label>
         </div>
	     </div>
    </div>
    
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Department">Product Department</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_DEPT_DESC" type="TEXT" value="<%=prddepid%>" size="20" MAXLENGTH=100 id="PRD_DEPT_DESC" placeholder="Select Product Department">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prdeptbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Class">Product Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=prdclsid%>" size="20" MAXLENGTH=100 id="PRD_CLS_DESC" placeholder="Select Product Category">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_CLS_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prclassbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Type">Product Sub Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_TYPE_DESC" type="TEXT" value="<%=sArtist%>" size="20" MAXLENGTH=100 id="PRD_TYPE_DESC" placeholder="Select Product Sub Category">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_TYPE_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prtypbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Product Brand">Product Brand</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_BRAND_DESC" type="TEXT" value="<%=prdBrand%>" size="20" MAXLENGTH=100 id="PRD_BRAND_DESC" placeholder="Select Product Brand">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prbadbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group" style="display: none;">
      <label class="control-label col-form-label col-sm-3" for="Product Brand">Department Display</label>
      <div class="col-sm-4"  >                
			<INPUT class="form-control" name="DEPT_DISPLAY_ID"  style="display:none;" type="hidden" value="<%=deptdisplay%>" size="20" MAXLENGTH=100 id="DEPT_DISPLAY_ID" placeholder="Select Department Display">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prddeptbtnpop"></i></span>				
      </div>
    </div>
    
    <INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
    
    <div class="form-group" style="display:none">
      <label class="control-label col-form-label col-sm-3" for="Product VAT" id="TaxLabelOrderManagement"></label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="PRODGST" id="PRODGST" type="TEXT" value="<%=PRODGST%>" size="20" MAXLENGTH=50  onkeypress="return isNumberKey(event,this,4)">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Alternate Product Barcode">Alternate Product Barcode</label>
      <div class="col-sm-4">                
        <TABLE id="alternateItemDynamicTable">
		<TR>
		<TD> <div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('alternateItemDynamicTable');return false;"></span><INPUT class="form-control" name="DYNAMIC_ALTERNATE_NAME_1" type="TEXT" value="" size="50" MAXLENGTH="50"  /></div></TD>
		</TR>
		</TABLE>
      </div>
      <INPUT	type="hidden" name="DYNAMIC_ALTERNATE_SIZE">
		<INPUT	type="hidden" name="FLAGSUCCESS" value="NOTSUBMIT">
    </div>
    
    <div class="form-group">
					<div class="row">
					<div class="col-sm-3"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow('alternateItemDynamicTable','');return false;">+ Add New Alternate Product Barcode</a>
						</div>
					</div>
				</div>
    
    </div>
    </div>
    
    <div class="col-sm-4 text-center">
		<div class="row">
			<div class="col-sm-12">				
				<img src="../jsp/dist/img/NO_IMG.png" id="item_img"
													name="CATALOGPATH" alt="new image"
													class="img-thumbnail img-responsive col-sm-3"
													style="width: 70%;float:revert; padding: 3px;">

			</div>
		</div>
		<% if(!COMP_INDUSTRY.equals("Retail")) {%>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group" id="btnUpload">
					<label>Upload Product Image:</label> <INPUT style="width: 100%;" class="form-control btn-sm"  name="IMAGE_UPLOAD" type="File" size="20" id ="productImg" MAXLENGTH=100>  
<!-- 					<label>Upload Product Image:</label> <INPUT style="width: 100%;" class="form-control btn-sm"  name="IMAGE_UPLOAD"  onchange="checkImageSize('productImg',this.files[0])" type="File" size="20" id ="productImg" MAXLENGTH=100>   -->
					<br> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image" onClick="image_delete();"> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image" onClick="image_edit();">
				</div>
			</div>
		</div> 
		<%} %>
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
     <!--    <li class="nav-item">
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
			<INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=strUtils.forHTMLTag(HSCODE)%>" size="20" MAXLENGTH=100 id="HSCODE" placeholder="Select HS Code">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'HSCODE\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="hsbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="COO">COO</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="COO" type="TEXT" value="<%=strUtils.forHTMLTag(COO)%>" size="20" MAXLENGTH=100 id="COO" placeholder="Select COO">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'COO\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="coobtnpop"></i></span>				
      </div>
            <div class="col-sm-1">
       <span class="input-group-btn"></span>
    		<input name="COOCURRENCY" type="TEXT" value="" size="10" MAXLENGTH=50  class="form-control" readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="VINNO">VIN NO</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="VINNO" type="TEXT" value="<%=strUtils.forHTMLTag(VINNO)%>" style="width:100%" MAXLENGTH=50>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="MODEL">MODEL</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="MODEL" type="TEXT" value="<%=strUtils.forHTMLTag(MODEL)%>" style="width:100%" MAXLENGTH=50>				
      </div>
    </div>
        
        </div>
        
        <div class="tab-pane fade" id="purchase">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Purchase UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="PURCHASEUOM" type="TEXT" value="<%=PURCHASEUOM%>" size="20" MAXLENGTH=100 id="purchaseuom" placeholder="Select UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PURCHASEUOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="purchaseuombtnpop"></i></span>
      </div>      
      
      
      <label class="control-label col-sm-1" for="Currency">Currency/Rate</label>
    <div class="col-sm-2">
        <input class="form-control ac-selected" name="ECURRENCY" id="ECURRENCY" 
               placeholder="Select a Currency" onchange="checkcurrency(this.value)"value="">
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'ECURRENCY\']').focus()">
            <i class="glyphicon glyphicon-menu-down"></i>
        </span>
    </div>

    <div class="col-sm-2">
        <input class="form-control" name="CURRENCYUSEQT" id="CURRENCYUSEQT" type="text" 
               value="" size="20" maxlength="50" onchange="currencyEQ(this.value)" 
               onkeypress="return isNumberKey(event,this,4)">
    </div>
    
    </div>
    
    
     <div class="form-group row">
    <label class="control-label col-form-label col-sm-2" for="Cost">Cost</label>
    <div class="col-sm-4">
        <input class="form-control" name="COST" id="COST" type="text" 
               value="<%=StrUtils.addZeroes(Double.parseDouble(cost), numberOfDecimal)%>" size="20" maxlength="50" 
               onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)">
    </div>

       <label class="control-label col-sm-1" for="PriceFC">Price.FC/LC</label>
    <div class="col-sm-1">
        <input class="form-control ac-selected" name="CURPRICE" id="CURPRICE" onchange="currencyconv(this.value)" value="">
    </div>
    <div class="col-sm-1">
        <input class="form-control" name="SELCUR" id="SELCUR" type="text" value="" readonly="" style="position: relative;width: 51px;left: -31px;">
        </div>

    <div class="col-sm-1">
        <input class="form-control" name="CONPRICE" id="CONPRICE" type="text" value="" readonly >
    </div>
    <div class="col-sm-1">
        <input class="form-control" name="BASECUR" id="BASECUR" type="text" value="<%=currency%>" readonly="" style="position: relative;width: 51px;left: -33px;">
        </div>
    
    <div class="col-sm-1">
        <label class="checkbox-inline" style="position: relative;left: -74px;bottom: 6px;">
            <input type="checkbox" style="border:0;" name="APPLYCONPRICE" value="APPLYCONPRICE" id="APPLYCONPRICE" onclick="Isapplyconprice(this);"> Apply To Cost
        </label>
    </div>
    
</div>

<%--     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Cost">Cost</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="COST" id="COST" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(cost), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div> --%>
    
		<INPUT type="hidden" name="DYNAMIC_ITEMSUPPLIER_SIZE">        
     
    <div class="form-group">
    <%if(ISSUPPLIERMANDATORY.equals("1")){%>
		<label class="control-label col-form-label col-sm-2 required" for="supplier name">PO Estimate Order Supplier</label>
		<%}else{%>
		<label class="control-label col-form-label col-sm-2" for="supplier name">PO Estimate Order Supplier</label>
		<%}%>
        	<div class="col-sm-4">
        		<input type="hidden" name="vendno" value="<%=vendno%>" >
        		<input type = "hidden" name="custModal">
    		 	<INPUT class=" form-control" id="vendname" value="<%=vendname%>" name="vendname" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select PO Estimate Order Supplier">
    			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'vendname\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
							</span>    						
        			</div>
        			<!-- <div class="form-inline">
	     <div class="col-sm-3" style="padding: 0px;">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" onclick="isposdiscount();"/> 
	     Allow POS Terminal Discount</label>
         </div>
	     </div> -->
				</div>
	 
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Item Supplier Discount">Secondary Supplier</label>
     <div class="col-sm-4">
			<TABLE id="multiitemsup">
		<TR>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteItemSupplier('multiitemsup');return false;"></span><INPUT class="form-control vendorSearch" name="ITEMSUPPLIER_0" id="ITEMSUPPLIER_0"  type = "TEXT" value="<%=SUPPLIER%>" size="55" placeholder="Select Secondary Supplier" MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.ITEMSUPPLIER_0.value.length > 0)){validateItemSupplier(0);}">
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>
    
    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addItemSupplieredit('multiitemsup','');return false;">+ Add another Supplier</a>
						</div>
					</div>
				</div>
    
    <INPUT type="hidden" name="DYNAMIC_SUPPLIERDISCOUNT_SIZE">
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier Discount">Supplier Discount</label>
      <div class="col-sm-4">
			<label class="radio-inline">
			<INPUT type ="hidden" name="SUPDISCOUNTTYPE" id="SUPDISCOUNTTYPE" value="">
      	<INPUT name="IBDISCOUNT" type = "radio"  value="BYCOST"  id="BYCOST" <%if(IBDiscounttype.equalsIgnoreCase("BYCOST")) {%>  checked="checked" <%}%> onclick="document.form.SUPDISCOUNTTYPE.value = 'BYCOST'" onchange="SuppDiscountType('BYCOST')" >By Cost
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(IBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> onclick="document.form.SUPDISCOUNTTYPE.value = 'BYPERCENTAGE'" onchange="SuppDiscountType('BYPERCENTAGE')" >By Percentage
    	</label>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier Discount"></label>
      <div class="col-sm-4">
			<TABLE id="supplierDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_SUPPLIER_DISCOUNT_0" id="DYNAMIC_SUPPLIER_DISCOUNT_0" onchange="calculateSuppDiscount(this)" type="TEXT" value="" 
		size="20" MAXLENGTH="50"  onkeypress="return isNumberKey(event,this,4)" />&nbsp;</TD>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRowCost('supplierDiscount');return false;"></span><INPUT class="form-control supplierSearch" name="SUPPLIER_0" id="SUPPLIER_0" placeholder="Select Supplier"  type = "TEXT" value="<%=SUPPLIER%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.SUPPLIER_0.value.length > 0)){validateSupplier(0);}">
		<INPUT type="hidden" name="DYNAMIC_SUPPLIER_DISCOUNT_SIZE">        
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>
    
    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowFormatCost('supplierDiscount','','');return false;">+ Add another discount</a>
						</div>
					</div>
				</div>
        
        </div>
        
        <div class="tab-pane fade" id="sales">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Sales UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="SALESUOM" type="TEXT" value="<%=SALESUOM%>" size="20" MAXLENGTH=100 id="salesuom" placeholder="Select UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'SALESUOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="salesuombtnpop"></i></span>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="List Price">List Price</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="PRICE" id="PRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(price), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="javascript:enableSellingPrice(this.value);" onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div>
    
    <div class="form-group">
    </div>
    
         <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Product Type</label>
      <div class="col-sm-6">
      	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" onclick="iscombpro();">None</label>
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO" onclick="iscombprofin();">Is Finished Product</label>				
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI" onclick="iscombprosem();">Is Semi Finished Product</label>		
      </div>
     </div>
    
     <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-2 required">Product Price Increase</label>
      <div class="col-sm-6">
			<label class="radio-inline">
      	<INPUT type = "radio"  name="CPPI" type = "radio"  value="BYPRICE"  id="CPPIBYPRICE" checked="checked">By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT type = "radio" name="CPPI" type = "radio" value="BYPERCENTAGE"  id = "CPPIBYPERCENTAGE" >By Percentage
    	</label>
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked">None</label>				 -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO" >Is Finished Product</label>				 -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI">Is Semi Finished Product</label>				 -->
      </div>
    </div>
     <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount">Product Price Increased Value</label>
      <div class="col-sm-3">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICE" id="INCPRICE" style="width: 115%;" onchange="isNumCheck();" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="col-sm-1">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICEUNIT" id="INCPRICEUNIT" value="" readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Minimum Selling Price">Minimum Selling Price</label>
      <div class="col-sm-4">
			<INPUT  class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(minsprice), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)">				
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
			<INPUT type ="hidden" name="CUSDISCOUNTTYPE" id="CUSDISCOUNTTYPE" value="">
			<INPUT type ="hidden" name="TYPE" id="TYPE" value="EDIT">
      	<INPUT name="OBDISCOUNT" type = "radio"  value="BYPRICE"  id="BYPRICE" <%if(OBDiscounttype.equalsIgnoreCase("BYPRICE")) {%>  checked="checked" <%}%> onclick="document.form.CUSDISCOUNTTYPE.value = 'BYPRICE'" onchange="CusDiscountType('BYPRICE')">By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="OBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(OBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> onclick="document.form.CUSDISCOUNTTYPE.value = 'BYPERCENTAGE'" onchange="CusDiscountType('BYPERCENTAGE')"> By Percentage
    	</label>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount"></label>
      <div class="col-sm-4">
			<TABLE id="customerDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT_0" id="DYNAMIC_CUSTOMER_DISCOUNT_0" onchange="calculateCustDiscount(this)" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"
		size="20" MAXLENGTH="50"  />&nbsp;</TD>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('customerDiscount');return false;"></span><INPUT class="form-control customerSearch" name="CUSTOMER_TYPE_ID_0" id="CUSTOMER_TYPE_ID_0"  placeholder="Select Customer Type" type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.CUSTOMER_TYPE_ID_0.value.length > 0)){validateCustomerType(0);}">
		<INPUT type="hidden" name="DYNAMIC_CUSTOMER_DISCOUNT_SIZE">        
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>
    
    <div class="form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowFormatCustomerType('customerDiscount','','');return false;">+ Add another discount</a>
						</div>
					</div>
				</div>   
        
        </div>
        
        <div class="tab-pane fade" id="rental" type="hidden">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Rental UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="RENTALUOM" type="TEXT" value="<%=RENTALUOM%>" size="20" MAXLENGTH=100 id="rentaluom" placeholder="Select UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'RENTALUOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="rentaluombtnpop"></i></span>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Rental Price">Rental Price</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="RENTALPRICE" id="RENTALPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(RENTALPRICE), numberOfDecimal)%>" size="20" onkeypress="return isNumberKey(event,this,4)"
		MAXLENGTH=50 onchange="validDecimal(this.value)">				
      </div>
    </div>
        
        </div>
        
        <div class="tab-pane fade" id="inventory">
        <br>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Inventory UOM">UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="INVENTORYUOM" type="TEXT" value="<%=INVENTORYUOM%>" size="20" MAXLENGTH=100 id="inventoryuom" placeholder="Select UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'INVENTORYUOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="inventoryeuombtnpop"></i></span>
      </div>      
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Min Stock Quantity">Min Stock Quantity</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="STKQTY" type="TEXT" value="<%=stkqty%>" size="20" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Max Stock Quantity">Max Stock Quantity</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="MAXSTKQTY" type="TEXT" value="<%=maxstkqty%>"	size="20" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div>
        <INPUT type="hidden" name="LOC_0" id="LOC_0" >
        </div>
        
        <div class="tab-pane fade" id="additionaldetail">
        <br>
     
    
    <div class="form-group">
        <TABLE id="descriptiontbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD>
		<label for="Description 1">Detail Description 1</label></TD>		
		<TD align="center"  style="width: 90%;"><div class="col-sm-10"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 650px;" aria-hidden="true" onclick="deleteRowDesc('descriptiontbl');return false;"></span>
		<INPUT class="form-control" name="DESCRIPTION0" id="DESCRIPTION0"  placeholder="Max 1000 Characters" type = "TEXT" value="" size="100"  MAXLENGTH=1000>		        
       	</div>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="DESCRIPTION_SIZE" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-1"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowDescCost('descriptiontbl','','');return false;">+ Add another Description</a>
						</div>
					</div>
				</div>
        </div>
        
        <div class="tab-pane fade" id="catalogues">
        <br>
    	
    	<div class="form-group">
      		<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Main Image</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD" id ="productImg" type="File" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img0" style="width: 100%;">
    		</div>
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Main Image" onClick="image_delete();"> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Main Image" onClick="image_edit();">
		</div>
    	
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 2</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD1" id ="productImg_2" type="File" size="20" MAXLENGTH=100>
<!--         		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD1" id ="productImg_2" onchange="checkImageSize('productImg_2',this.files[0])" type="File" size="20" MAXLENGTH=100> -->
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img1" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image 2" onClick="image_deletes(2);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image 2" onClick="image_edits(2);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 3</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD2" type="File" id ="productImg_3" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img2" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image 3" onClick="image_deletes(3);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image 3" onClick="image_edits(3);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 4</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD3" type="File" id ="productImg_4" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png"  id="item_img3" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image 4" onClick="image_deletes(4);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image 4" onClick="image_edits(4);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 5</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD4" type="File" id ="productImg_5" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img4" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image 5" onClick="image_deletes(5);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image 5" onClick="image_edits(5);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 6</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD5" type="File" id ="productImg_6" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img5" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Image 6" onClick="image_deletes(6);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Image 6" onClick="image_edits(6);">
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

    <div class="form-group">
					<div class="row">
					<div class="col-sm-1"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowPrdCost('prdtbl','','');return false;">+ Add another Product</a>
						</div>
					</div>
				</div>
        </div>
        
        <div class="tab-pane fade" id="outletminmax">
        <br>
        	<div class="col-12 col-sm-12 no-padding">
  		        <input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAllout(this.checked);">
				<strong>&nbsp;Select/Unselect All </strong>
			</div>
			
         <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table outlet-table">
						<thead>
							<tr>
								<th>Check</th>
								<th>Company</th>
								<th>Outlet</th>
								<th>Current MinQty</th>
								<th>Current MaxQty</th>
								<th>New MinQty</th>
								<th>New MaxQty</th>
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
									<input type="checkbox" name="checkoutlets" id="checkoutlets" <%if(!minqty.equalsIgnoreCase("0") || !maxqty.equalsIgnoreCase("0")){ %> checked <%}%>value="<%=i%>" /></td>
								<td class="text-center">
									<input class="form-control text-left" name="childcompany" type="text" value="<%= plntdesc %>" placeholder="Company" autocomplete="off" readonly>
									<input class="form-control" name="childcompanyplant_<%=i%>" type="hidden" value="<%= (String)arrCurrLine.get("PLANT") %>" ></td>
								<td class="text-center">
									<input class="form-control text-left" name="childoutlet_<%=i%>" type="text" value="<%= (String)arrCurrLine.get("OUTLET") %>" placeholder="Outlet" autocomplete="off" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" name="MINQTY" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(minqty), numberOfDecimal)%>" placeholder="MIN QTY" autocomplete="off" readonly></td>
								<td class="text-center">
<!-- 									<input class="form-control text-left" name="newprice" type="text" value="0.00" placeholder="New Price" autocomplete="off" onchange="checkprice(this)" onkeypress="return isNumberKey(event,this,4)"></td> -->
									<input class="form-control text-left" name="MAXQTY" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(maxqty), numberOfDecimal)%>"  placeholder="MAX QTY" autocomplete="off" readonly></td>
									<td class="text-center">
									<input class="form-control text-left" name="MINQTY_<%=i%>" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(minqty), numberOfDecimal)%>" placeholder="MIN QTY" autocomplete="off" ></td>
								<td class="text-center">
<!-- 									<input class="form-control text-left" name="newprice" type="text" value="0.00" placeholder="New Price" autocomplete="off" onchange="checkprice(this)" onkeypress="return isNumberKey(event,this,4)"></td> -->
									<input class="form-control text-left" name="MAXQTY_<%=i%>" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(maxqty), numberOfDecimal)%>"  placeholder="MAX QTY" autocomplete="off" ></td>
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
        	<div class="col-12 col-sm-12 no-padding">
  		        <input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
				<strong>&nbsp;Select/Unselect All </strong>
			</div>
			
         <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table outlet-table">
						<thead>
							<tr>
								<th>Check</th>
								<th>Company</th>
								<th>Outlet</th>
								<th>Current List Price</th>
								<th>New Price</th>
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
									<input type="checkbox" name="checkoutlet" id="checkoutlet" value="<%=i%>" /></td>
								<td class="text-center">
									<input class="form-control text-left" name="childcompany" type="text" value="<%= plntdesc %>" placeholder="Company" autocomplete="off" readonly>
									<input class="form-control" name="childcompanyplant_<%=i%>" type="hidden" value="<%= (String)arrCurrLine.get("PLANT") %>" ></td>
								<td class="text-center">
									<input class="form-control text-left" name="childoutlet_<%=i%>" type="text" value="<%= (String)arrCurrLine.get("OUTLET") %>" placeholder="Outlet" autocomplete="off" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" name="currentprice" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(outletprice), numberOfDecimal)%>" placeholder="Current Price" autocomplete="off" readonly></td>
								<td class="text-center">
<!-- 									<input class="form-control text-left" name="newprice" type="text" value="0.00" placeholder="New Price" autocomplete="off" onchange="checkprice(this)" onkeypress="return isNumberKey(event,this,4)"></td> -->
									<input class="form-control text-left" name="newprice_<%=i%>" type="text" value="<%=StrUtils.addZeroes(Double.parseDouble(outletprice), numberOfDecimal)%>"  placeholder="New Price" autocomplete="off" onchange="checkprice(this)" onkeypress="return isNumberKey(event,this,4)"></td>
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
				<textarea  class="form-control" name="ITEM_CONDITION" placeholder="Max 100 Characters" MAXLENGTH=100><%=sItemCondition%></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2" for="Remarks2">Remarks2</label>
			<div class="col-sm-4">				
				<textarea  class="form-control" name="TITLE" placeholder="Max 100 Characters" MAXLENGTH=100><%=sTitle%></textarea>
			</div>
		</div>

		</div>
    	<INPUT class="form-control" name="SERVICEUOM" type="hidden" value="<%=SERVICEUOM%>" size="20" MAXLENGTH=100 readonly id="serviceuom">
        <INPUT class="form-control" name="SERVICEPRICE" id="SERVICEPRICE" type="hidden" value="<%=StrUtils.addZeroes(Double.parseDouble(SERVICEPRICE), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	<!-- </div>
    	</div>
    </div> 
    </div> 
  	  </div>
  	 </div>	-->	
		
        </div>
    
    <div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	 <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" sNewEnb>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" sNewEnb >Save</button>&nbsp;&nbsp;
   		<button type="reset" class="Submit btn btn-default" onClick="return onDelete();" sNewEnb>Delete</button>&nbsp;&nbsp;
      	</div>
    	</div>    
    <br>
<br>
 
<%if (action.equalsIgnoreCase("VIEWMAPITEM")) {%>
<TABLE border="1" width="50%" cellspacing="0" cellpadding="0" 
	align="center" bgcolor="#dddddd"> 
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">View
		Mapping Item</font>
</TABLE>
<TABLE BORDER="0" CELLSPACING="1" WIDTH="50%" align="center">
	<TR bgcolor="navy">
		<TH width="20%"><font color="#ffffff">S. NO</TH>
		<TH width="40%"><font color="#ffffff">Mapping Item</TH>
		<TH width="40%"><font color="#ffffff">DELETE</TH>
	</TR>
	<%
       List listMapItem = itemUtil.getMapItems4KeyItem(sItem);
       if(listMapItem.size()>0) {
       for(int i = 0; i<listMapItem.size(); i++){
       int iIndex = i+1;
       String sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";

       %>
	<TR bgcolor="<%=sBGColor%>">
		<TD width="20%"><%=iIndex%></TD>
		<TD width="40%"><%=(String)listMapItem.get(i)%></TD>
		<TD width="40%" align="center"><a
			href="item_view.jsp?action=ITEMMAPDELETE&keyitem=<%=sItem%>&mapitem=<%=(String)listMapItem.get(i)%>&DESC=<%=strUtils.replaceCharacters2Send(sItemDesc)%>&UOM=<%=sItemUom%>&REORDQTY=<%=sReOrdQty%>">Delete</a></TD>
	</TR>
	<%} }else {%>
	<Th colspan=2><font class="<%=IConstants.FAILED_COLOR%>">No
	Mapping Product available</font></Th>
	<%} %>
</TABLE>
<%} else if(action.equalsIgnoreCase("VIEWITEMLOC")) {%>
<TABLE border="1" width="50%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">View
		Item Location</font>
</TABLE>
<TABLE BORDER="0" CELLSPACING="1" WIDTH="50%" align="center">
	<TR bgcolor="navy">
		<TH width="20%"><font color="#ffffff">S. NO</TH>
		<TH width="40%"><font color="#ffffff">Location</TH>
	</TR>
	<%
       List listItemLoc = itemUtil.getLocationList4Item(sItem);
       if(listItemLoc.size()>0) {
       for(int i = 0; i<listItemLoc.size(); i++){
       int iIndex = i+1;
       String sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";

       %>
	<TR bgcolor="<%=sBGColor%>">
		<TD width="20%"><%=iIndex%></TD>
		<TD width="40%"><%=(String)listItemLoc.get(i)%></TD>
	</TR>
	<%} }else {%>
	<Th colspan=2><font class="<%=IConstants.FAILED_COLOR%>">No
	Product Location available</font></Th>
	<%} %>
</TABLE>

<%}%>    
	
	</div>
</div>
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
    if(document.form.SAVE_RED.value!="")
	{
    <%if(RTYPE.equalsIgnoreCase("SALES")) {%>
	document.form.action  = "../salesorder/salessummary?result=Product Updated Successfully";
	<%}else if(RTYPE.equalsIgnoreCase("INV")) {%>
	document.form.action  = "../inventory/inventorysummary?result=Product Updated Successfully";
	<%}else{ %> 
	document.form.action  = "../product/summary?result=Product Updated Successfully";
	<%}%>
	document.form.submit();
	}
if(document.form.SAVE_REDELETE.value!=""){
    <%if(RTYPE.equalsIgnoreCase("SALES")) {%>
	document.form.action  = "../salesorder/salessummary?result=Product Deleted Successfully";
	<%}else if(RTYPE.equalsIgnoreCase("INV")) {%>
	document.form.action  = "../inventory/inventorysummary?result=Product Deleted Successfully";
	<%}else{ %>
	document.form.action  = "../product/summary?result=Product Deleted Successfully";
	<%}%>
	 document.form.submit();
	}
	
addSuggestionprd();
/* location Auto Suggestion */
$('#LOC_ID').typeahead({
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
			PLANT : "<%=plant%>",
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
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="PRD_LOC_IDAddBtn footer"  data-toggle="modal" data-target="#prdlocModal"> <a href="#"> + Add Product Location</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		$('.PRD_LOC_IDAddBtn').show();
	}).on('typeahead:close',function(){
		setTimeout(function(){ $('.PRD_LOC_IDAddBtn').hide();}, 150);
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
	});
});
</script>
<script>


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
                                                   document.form.PRD_TYPE_DESC.value=resultVal.prd_type_desc;
                                                  //  Start code added by Deen for product brand on 11/9/12 
                                                   document.form.PRD_BRAND.value=resultVal.brand;
                                                   document.form.PRD_BRAND_DESC.value=resultVal.prd_brand_desc;
                                                  //  End code added by Deen for product brand on 11/9/12 
                                                   document.form.UOM.value=resultVal.sUOM; 
                                                   document.form.TITLE.value=resultVal.sTitle;
                                                  // document.form.MANUFACT.value=resultVal.sMedium;
                                                   document.form.ITEM_CONDITION.value=resultVal.sItemCondition;
                                                   document.form.REMARKS.value=resultVal.sRemark;
                                                   document.form.STKQTY.value=resultVal.stkqty;
                                                   document.form.PRD_CLS_ID.value=resultVal.prd_cls_id;
                                                   document.form.PRD_CLS_DESC.value=resultVal.prd_cls_desc;
                                                   document.form.vendno.value=resultVal.vendno;
                                                   document.form.vendname.value=resultVal.vendname;
                                                   document.form.PRD_DEPT_DESC.value=resultVal.prd_dept_desc;
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
                                                	  /*  $(".comproprice")/* .hide() */  
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
                                                   if(resultVal.ISNEWARRIVAL == 1){
                                                	   document.form.ISNEWARRIVAL.checked = true;
                                                   }else if(resultVal.ISNEWARRIVAL == 0){
                                                	   document.form.ISNEWARRIVAL.checked = false;
                                                   }
                                                   if(resultVal.ISTOPSELLING == 1){
                                                	   document.form.ISTOPSELLING.checked = true;
                                                   }else if(resultVal.ISTOPSELLING == 0){
                                                	   document.form.ISTOPSELLING.checked = false;
                                                   }
                                                   if(resultVal.ISCHILDCAL == 1){
                                                	   document.form.ISCHILDCAL.checked = true;
                                                   }else if(resultVal.ISCHILDCAL == 0){
                                                	   document.form.ISCHILDCAL.checked = false;
                                                   }
                                                   setCheckedValue( document.form.ACTIVE,resultVal.isActive);
                                                   //setCheckedValue( document.form.rdoparentchild,resultVal.ISPARENTCHILD);
                                                   setCheckedValue( document.form.NONSTOCKFLAG,resultVal.nonstkflg);
												   DisplayNonStkType();
                                                   loadAlternateItemNames(productId);
                                                   loadCustomerDiscount(resultVal.sItem);
                                                   loadSupplierDiscount(resultVal.sItem);
                                                   loadItemSupplier(resultVal.sItem);
                                                   loadDetailDesc(resultVal.sItem);
                                                   loadAddPrd(resultVal.sItem);
                                                   removeSuggestionSearch();
                                            	   addSuggestionSearch();
                                                    if(resultVal.coo != ""){
                                                   		cooCountryCurrency(resultVal.coo);
                                                    }else{
                                                    	document.form.COOCURRENCY.value="";
                                                     }
                                                   
                                                 if(resultVal.catalogpath==""){
                                                	 $("#item_img").attr("src","../jsp/dist/img/NO_IMG.png");
                                                	 $("#item_img0").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 }
                                                 else{
                                           			 $("#item_img").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath);
                                           			 $("#item_img0").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath);
                                                 }
                         
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
                                                 if(resultVal.catalogpath1 == undefined) 
                                                	 $("#item_img1").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img1").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath1);
                                                 
                                                 if(resultVal.catalogpath2 == undefined) 
                                                	 $("#item_img2").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img2").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath2);
                                                 
                                                 if(resultVal.catalogpath3 == undefined) 
                                                	 $("#item_img3").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img3").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath3);
                                                 
                                                 if(resultVal.catalogpath4 == undefined) 
                                                	 $("#item_img4").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img4").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath4);
                                                 
                                                 if(resultVal.catalogpath5 == undefined) 
                                                	 $("#item_img5").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img5").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath5);
                         
					}
				}
			});
		}
	}
        

function checkitem(itemvalue)
{	
	 if(itemvalue=="" || itemvalue.length==0 ) {
		alert("Enter Item!");
		document.getElementById("ITEM").focus();
		return false;
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                               
						alert("Product Already Exists");
						document.getElementById("ITEM").focus();
						//document.getElementById("ITEM").value="";
						return false;	
						
					} 
					else 
						return true;
					    
				}
			});
		 return true;
		}	
}
function onPrint(){
	   var ITEM   = document.form.ITEM.value;
	   var ITEMDESC = document.form.DESC.value;
	   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
	  		document.form.action  = "PrintItemLabel.jsp?action=PRINT&Item="+ITEM+"&ITEMDESC="+ITEMDESC;
	  		alert("Do you want to Print?")
	  		document.form.submit();
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
	function loadAlternateItemNames(productId) {
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				ACTION : "LOAD_ALTERNATE_ITEM_DETAILS"
				},
				dataType : "json",
				success : formatData
			});
		}

		function formatData(data) {
			var ii = 0;
			var errorBoo = false;
			if (data.status == "99") {
				errorBoo = true;
			}
			
			if (!errorBoo) {
				var table = document.getElementById('alternateItemDynamicTable');
				var rowCount = table.rows.length;
				rowCount = rowCount * 1 - 1;
				for(; rowCount>=0; rowCount--) {
	 				table.deleteRow(rowCount);
				}
				var resultVal = data.result;
				$.each(resultVal, function(i, resultIntVal) {
					addRow('alternateItemDynamicTable', resultIntVal);
				});
			} else {
				alert("No Data found....!!!!");
			}

		}
		function addRow(tableID, textValue) {

			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;
			var row = table.insertRow(rowCount);

/* 			var secondCell = row.insertCell(0);
			var inputTextArea = document.createElement("input"); */
			var newRowCount = rowCount + 1;
/* 			inputTextArea.name = "DYNAMIC_ALTERNATE_NAME_" + newRowCount;
			inputTextArea.className = "form-control";
			inputTextArea.size = "50";
			inputTextArea.type = "text";
			
			inputTextArea.value = textValue; */
			// secondCell.appendChild(document.createElement("br"));
			//secondCell.appendChild(inputTextArea);
			
			var form = document.forms['form'];


			var itemCell = row.insertCell(0);
			var itemCellText =  "<div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRow('alternateItemDynamicTable');return false;\"></span><INPUT class=\"form-control\" name=\"DYNAMIC_ALTERNATE_NAME_"+newRowCount+"\" ";
			itemCellText = itemCellText+ " id=\"DYNAMIC_ALTERNATE_NAME_"+newRowCount+"\" value=\""+textValue+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\"></div>&nbsp;";
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
			    	       addRowFormatCustomerType('customerDiscount',item.OBDISCOUNT,item.CUSTOMERTYPE);
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
	var itemCellText =  "<INPUT class=\"form-control\" name=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" onchange=\"calculateCustDiscount(this)\" value=\""+obDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRow('customerDiscount');return false;\"></span><INPUT class=\"form-control customerSearch\" name=\"CUSTOMER_TYPE_ID_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\" value=\""+customerType+"\" placeholder=\"Select Customer Type\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;
	
	removeSuggestionSearch();
	addSuggestionSearch();
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
	    	       addRowFormatCost('supplierDiscount',item.IBDISCOUNT,item.VENDNO);
	    	       setCheckedValue(document.form.IBDISCOUNT,item.DISCOUNTTYPE); 	
	     });
		 if (data.items == null || data.items.length == 0){
			 addRowFormatCost('supplierDiscount','','');
	     }
	        
	} else {
		//alert("No Data found....!!!!");
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

function deleteRow(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addRowFormatCost(tableID,ibDiscount,vendNo) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" ";
	itemCellText = itemCellText+"  id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" onchange=\"calculateSuppDiscount(this)\" value=\""+ibDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowCost('supplierDiscount');return false;\"></span> <INPUT class=\"form-control supplierSearch\" name=\"SUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+vendNo+"\" placeholder=\"Select Supplier\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;
	
	removeSuggestionSearch();
	addSuggestionSearch();
	
}

function addRowDescCost(tableID,desc,addDesc) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	if(rowCount!=10)
	{
	var newrowCount = 1+rowCount;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Detail Description"+rowCount+"\">Detail Description "+newrowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	itemCell.style.width ="90%";
	itemCell.style.textAlign ="center";
	var itemCellText =  "<div class=\"col-sm-10\"><div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 800px;\" aria-hidden=\"true\" onclick=\"deleteRowDesc('descriptiontbl');return false;\"></span> <INPUT class=\"form-control\" name=\"DESCRIPTION"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"DESCRIPTION"+rowCount+"\" type = \"TEXT\" size=\"20\" placeholder=\"Max 1000 Characters\" onkeypress=\"return blockSpecialCharcter(event)\" value=\""+addDesc+"\" MAXLENGTH=\"1000\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
	}
	else
		{
		alert("Can not add more then 10 detail description ");
		}
}
function addRowPrdCost(tableID,prd,addPrd) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	if(rowCount!=10)
	{
	var newrowCount = 1+rowCount;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Product"+rowCount+"\">Product "+newrowCount+"\</label>&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	itemCell.style.width ="93%";
	itemCell.style.textAlign ="center";
	var itemCellText =  "<div class=\"col-sm-6\"> <div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 500px;\" aria-hidden=\"true\" onclick=\"deleteRowPrd('prdtbl');return false;\"></span> <INPUT class=\"form-control itemSearch\" name=\"PRODUCT"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"PRODUCT"+rowCount+"\" type = \"TEXT\" size=\"20\"  onkeypress=\"return blockSpecialCharcter(event)\" value=\""+addPrd+"\" placeholder=\"Select Product\"  MAXLENGTH=\"50\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
	removeSuggestionSearch();
	addSuggestionprd();
	}
	else
		{
		alert("Can not add more then 10 Product ");
		}
}
function deleteRowCost(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

if(!((document.form.ITEM.value == "") || (document.form.ITEM.value == null))){
	loadAlternateItemNames(document.form.ITEM.value);
	loadCustomerDiscount(document.form.ITEM.value);
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
	   //end
function validateLocation(locId, index) {
		var isValid;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC_"+index).focus();
			return false;
		}else{
			var urlStr = "/track/InboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : false,
				data : {
					LOC : locId,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCATION"
					},
					dataType : "json",
					success : function(data) {
						if (data.status != "100") {
	                               
							alert("Not a valid Location");
							document.getElementById("LOC_"+index).focus();
							document.getElementById("LOC_"+index).value="";
							isValid = false;	
						} 
						else 
							isValid =  true;
					}
				});
			 return isValid;
			}
		}	   
	   
function validateCustomerType(index) {
	 var table = document.getElementById("customerDiscount");
	 var rowCount = table.rows.length;
	var scancustomertype = document.getElementById("CUSTOMER_TYPE_ID_"+index).value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				PLANT : "<%=plant%>",
				CUSTOMERTYPE : scancustomertype,
				ACTION : "VALIDATE_CUSTOMER_TYPE"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						for (var r = 0, n = table.rows.length-1; r < n; r++) {
				            for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
				                  if(scancustomertype==document.getElementById("CUSTOMER_TYPE_ID_"+r).value)
				                	{
				                		alert("Customer Type already exists for the product");
				                		document.getElementById("CUSTOMER_TYPE_ID_"+index).focus();
										document.getElementById("CUSTOMER_TYPE_ID_"+index).value="";
										return false;
				                	}
				            }
				        }
				        
				     } else {
						alert("Not a valid Customer Type");
						document.getElementById("CUSTOMER_TYPE_ID_"+index).value = "";
						document.getElementById("CUSTOMER_TYPE_ID_"+index).focus();
					}
				}
			});
		
	}

	function validateSupplier(index) {
	 	 var table = document.getElementById("supplierDiscount");
		 var rowCount = table.rows.length;
		var scansupplier = document.getElementById("SUPPLIER_"+index).value;
		var urlStr = "/track/OutboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					PLANT : "<%=plant%>",
					SUPPLIER : scansupplier,
					ACTION : "VALIDATE_SUPPLIER"
					},
					dataType : "json",
					success : function(data) {
						
						if (data.status == "100") {
							var resultVal = data.result;
							for (var r = 0, n = table.rows.length-1; r < n; r++) {
					            for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					                  if(scancustomertype==document.getElementById("SUPPLIER_"+r).value)
					                	{
					                		alert("Supplier already exists for the product");
					                		document.getElementById("SUPPLIER_"+index).focus();
											document.getElementById("SUPPLIER_"+index).value="";
											return false;
					                	}
					            }
					        }
					        
					     } else {
							alert("Not a valid Supplier");
							document.getElementById("SUPPLIER_"+index).value = "";
							document.getElementById("SUPPLIER_"+index).focus();
						}
					}
				});
			//}
		}
		//onLoad();
			$(document).on('change', '#productImg', function (e) {
				readURLEdit(this);
			});
			$(document).on('change', '#productImg_2', function (e) {
				readURLEdit1(this);
			});
			
			$(document).on('change', '#productImg_3', function (e) {
				readURLEdit2(this);
			});
			$(document).on('change', '#productImg_4', function (e) {
				readURLEdit3(this);
			});
			$(document).on('change', '#productImg_5', function (e) {
				readURLEdit4(this);
			});
			$(document).on('change', '#productImg_6', function (e) {
				readURLEdit5(this);
			});
</script>
   </form> 	
  </div>
 </div>
  </div>
  
  <!-- ----------------Modal-------------------------- -->

<div id="supplierModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form1" id="formsupplierid" method="post">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Supplier</h4>
				</div>
				<div class="modal-body">
					<input name="SUPPLIER_TYPE_DESC" type="hidden" value="">
					<input name="ORDER_TYPE_MODAL" type="hidden" value="PURCHASE">
					<input type="number" id="numberOfDecimal" style="display: none;"
						value=<%=numberOfDecimal%>>
						<INPUT type ="hidden" name="NOFO_DEC" value="<%=numberOfDecimal%>">
						<INPUT type ="hidden" name="username" value="<%=username%>">
						<input type="text" name="PLANT"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="plant"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="LOGIN_USER" style="display: none;"
						value=<%=username%>>
						<input type="hidden" name="TRANSPORTSID" value="<%=transports%>">
						
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Create Supplier ID">Supplier Id</label>
						<div class="col-sm-4">
							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE"
									type="TEXT" value=""
									onchange="checkitem(this.value)" size="50" MAXLENGTH=50
									width="50"> <span
									class="input-group-addon" onClick="onIDGenSupplier();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
  							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG"  value="<%=region%>">
  							<INPUT type="hidden" name="ValidNumber" value="<%=SupValidNumber%>">
						 <INPUT type="hidden" name="CURRENCYID_S" value="<%=DISPLAYID%>">	<!--Resvi -->
  							
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Supplier Name">Supplier Name</label>
						<div class="col-sm-4">

							<INPUT class="form-control" name="CUST_NAME" type="TEXT"
								value="" size="50" MAXLENGTH=100>
						</div>
					</div>
					
					
					<!-- 	                Author Name:Resviya ,Date:9/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="companyregnumber" id="companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=50> 
	  	  </div>
	       </div>
	       </div>
	         <!-- End -->
	       
					
					<!-- 	                Author Name:Resviya ,Date:19/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Supplier
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="SUP_CURRENCY"  id="SUP_CURRENCY" type="TEXT" value="<%=Supcurrency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					 
<!-- end -->
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="supplier type">Supplier Type</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input name="SUPPLIER_TYPE_S" id=SUPPLIER_TYPE_S type="TEXT" placeholder="Select a supplier type"
									value="" size="50" MAXLENGTH=50
									class="form-control"> <span class="select-icon"  onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_S\']').focus()"> 	
   		 						<i class="glyphicon glyphicon-menu-down"></i></span>
							</div>
						</div>
					</div>
					
						<div class="form-group">
      					<label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      						<div class="col-sm-4">           	
    							<input name="transports" id="transports" type="TEXT"  placeholder="Select a transport" size="50" MAXLENGTH=50 class="form-control">
   		 						<span class="select-icon" onclick="$(this).parent().find('input[name=\'transports\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
								</span>
  							</div>
						</div>

					<INPUT type="hidden" id="TaxByLabel" name="taxbylabel"
						value=<%=taxbylabel%>>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Telephone No">Supplier Phone</label>
						<div class="col-sm-4">

							<INPUT name="TELNO" type="TEXT" value="" size="50"
								class="form-control" onkeypress="return isNumber(event)"
								MAXLENGTH="30">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Fax">Supplier
							Fax</label>
						<div class="col-sm-4">

							<INPUT name="FAX" type="TEXT" value="" size="50"
								onkeypress="return isNumber(event)" MAXLENGTH="30"
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Customer Email">Supplier Email</label>
						<div class="col-sm-4">
							<INPUT name="CUSTOMEREMAIL" type="TEXT"
								value="" size="50" MAXLENGTH=50
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
						<div class="col-sm-4">
							<INPUT name="WEBSITE" type="TEXT" value="" size="50"
								MAXLENGTH=50 class="form-control">
						</div>
					</div>


					<div class="bs-example">
						<ul class="nav nav-tabs" id="myTab">
							<li class="nav-item active"><a href="#othersup"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile" class="nav-link"
								data-toggle="tab">Contact Details</a></li>
							<li class="nav-item"><a href="#home" class="nav-link"
								data-toggle="tab">Address</a></li>
							<li class="nav-item"><a href="#bank_cus" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>
							<li class="nav-item"><a href="#remarksup" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<div class="tab-pane fade" id="home">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_S" name="COUNTRY_CODE_S" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
				MasterUtil _MasterUtil=new  MasterUtil();
				ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>
		        <%
       			}
			 %></SELECT>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Unit No">Unit No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Building">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Street">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2" for="City">City</label>
									<div class="col-sm-4">

										<INPUT name="ADDR4" type="TEXT" value="" size="50"
											MAXLENGTH=80 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">State</label>
									<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Postal Code">Postal Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" type="TEXT" value="" size="50"
											MAXLENGTH=10 class="form-control">
									</div>
								</div>

							</div>

							<div class="tab-pane fade" id="profile">
								<br>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Contact Name">Contact Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Designation">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Work phone">Work Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Hand Phone">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Email">Email</label>
									<div class="col-sm-4">

										<INPUT name="EMAIL" type="TEXT" value="" size="50"
											MAXLENGTH="50" class="form-control">
									</div>
								</div>


							</div>

							<div class="tab-pane active" id="othersup">
								<br>

								<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>
		        <%
       			}
			 %></SELECT>
			</div>
		</div>

							<div class="form-group">
							<label class="control-label col-form-label col-sm-2" for="TRN No"
								id="TaxLabel"></label>
							<div class="col-sm-4">

								<INPUT name="RCBNO" type="TEXT" class="form-control"
									value="" size="50" MAXLENGTH="30">
							</div>
							</div>

								<div class="form-group" style="display: none;">
									<label class="control-label col-form-label col-sm-2"
										for="Opening Balance">Opening Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="hidden"
											value=""
											onkeypress="return isNumberKey(event,this,4)" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								
					<div class="form-group">  
        				<label class="control-label col-form-label col-sm-2" for="Payment Terms">Payment Mode</label>
      				<div class="col-sm-4">
     		 			<input id="PAYTERMS" name="PAYTERMS" class="form-control" type="text" value=""  MAXLENGTH=100 placeholder="Select Payment Mode">
		    			<span class="select-icon" onclick="$(this).parent().find('input[name=\'PAYTERMS\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>
			  		</div>
  					</div>  
  					<div class="form-group">
       					<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       				<div class="col-sm-4">
       					<input type="text" class="form-control" id="sup_payment_terms"	name="sup_payment_terms" value="" placeholder="Select Payment Terms">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'sup_payment_terms\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
					<div class="form-inline">
    					<label class="control-label col-form-label col-sm-1">Days</label>
    	    			<input name="SUP_PMENT_DAYS" type="TEXT" value="" class="form-control" size="5" MAXLENGTH=10 readonly>
  					</div>
    				</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Facebook">Facebook Id</label>
									<div class="col-sm-4">
										<INPUT name="FACEBOOK" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Twitter">Twitter Id</label>
									<div class="col-sm-4">
										<INPUT name="TWITTER" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">LinkedIn Id</label>
									<div class="col-sm-4">
										<INPUT name="LINKEDIN" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">Skype Id</label>
									<div class="col-sm-4">
										<INPUT name="SKYPE" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

							</div>
							

<div class="tab-pane fade" id="bank_cus">
        <br>

        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="IBAN">IBAN</label>
      	<div class="col-sm-4">
        <INPUT name="IBAN" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>

       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank</label>
			<div class="col-sm-4 ac-box">				
	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAME" class="form-control"   placeholder="BANKNAME">
	 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	<i class="glyphicon glyphicon-menu-down"></i></span>
	
  	</div>
			</div>
		
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Branch">Branch</label>
      	<div class="col-sm-4">
        <INPUT name="BRANCH" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
    	</div>

        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-4">
        <INPUT name="BANKROUTINGCODE" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>

        </div>

							<div class="tab-pane fade" id="remarksup">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Remarks">Remarks</label>
									<div class="col-sm-4">

										<INPUT name="REMARKS" type="TEXT" value=""
											size="50" MAXLENGTH=100 class="form-control ">
									</div>
								</div>

							</div>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">
							<button type="button" class="btn btn-success" onClick="onAddSupplier();" >
								Save
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>




				</div>
			</div>
		</form>
	</div>
</div>

<jsp:include page="newBankModal.jsp"flush="true"></jsp:include>
<script>
var subWin = null;

$(document).ready(function(){

$('.check').click(function() {
    $('.check').not(this).prop('checked', false);
});

    $('[data-toggle="tooltip"]').tooltip(); 
    var d ="<%=taxbylabel%>";

    document.getElementById('TaxLabel').innerHTML = d +" No.";

    //transport
	$('#transports').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
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
		    return '<p>' + data.TRANSPORT_MODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="transportsAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.transportsAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.transportsAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=TRANSPORTSID]").val(selection.ID);
		});

	/* Payment Mode Auto Suggestion */
	$("#PAYTERMS").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{	  
		  display: 'PAYMENTMODE',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/PaymentModeMst";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : "<%=plant%>",
					action : "GET_PAYMENT_MODE_LIST",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PAYMENTMODE);
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
				return '<p>' + data.PAYMENTMODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal" onclick="document.form.custModal.value=\'cust\'"><a href="#"> + New Payment Mode</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});

	/* Payment Terms Auto Suggestion */
	$('#sup_payment_terms').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'states',
		  display: 'PAYMENT_TERMS',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "/track/PaymentTermsServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : "<%=plant%>",
					ACTION : "GET_PAYMENT_TERMS_DETAILS",
					TERMS : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.terms);
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
		    return '<p>' + data.PAYMENT_TERMS + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.form.custModal.value=\'cust\'"><a href="#"> + Add Payment Terms</a></div>');
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
		}).on('typeahead:select',function(event,selection){
			$("input[name=SUP_PMENT_DAYS]").val(selection.NO_DAYS);
		});

	 /* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_S').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT :  "<%=plant%>",
				ACTION : "GET_SUPPLIER_TYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
				return '<div onclick="document.form1.SUPPLIER_TYPE_ID.value = \''+data.SUPPLIER_TYPE_ID+'\'"><p class="item-suggestion">' + data.SUPPLIER_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.SUPPLIER_TYPE_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="SUPPLIER_TYPE_IDAddBtn footer"  data-toggle="modal" data-target="#suppliertypeModal"> <a href="#"> + Add Supplier Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.SUPPLIER_TYPE_IDAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.SUPPLIER_TYPE_IDAddBtn').hide();}, 180);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.form1.SUPPLIER_TYPE_IDS.value = "";
		});

	/* Bank Name Auto Suggestion */
	$('#BANKNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'NAME',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT :"<%=plant%>",
						action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						NAME : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.orders);
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
		    	  return '<p onclick="document.form1.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
			}
		  }

		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
			
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.employeeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
 
});
function checkSupplier(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Supplier ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CUST_CODE : aCustCode,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "SUPPLIER_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Supplier Already Exists");
							document.getElementById("CUST_CODE").focus();
							//document.getElementById("ITEM").value="";
							return false;
						}
						else
							return true;
					}
	});	
			return true;
	}
}


function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}
function suppliertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#SUPPLIER_TYPE_S").typeahead('val', data.SUPPLIER_TYPE_DESC);
		$("input[name=SUPPLIER_TYPE_ID]").val(data.SUPPLIER_TYPE_ID);
	}
}
function isNumber(evt) {	
  evt = (evt) ? evt : window.event;
  var charCode = (evt.which) ? evt.which : evt.keyCode;
  
  if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
  	if( (charCode!=43 && charCode!=32 && charCode!=45))
  		{
  		
      alert("  Please enter only Numbers.  ");
      return false;
  		}
  	}
  return true;
}
function onAddSupplier(){
	   var CUST_CODE   = document.form1.CUST_CODE.value;
	   var CUST_NAME   = document.form1.CUST_NAME.value;
	//   var companyregnumber   = document.form1.companyregnumber.value;
	   var TAXTREATMENT   = document.form1.TAXTREATMENT.value;
	   var RCBNO   = document.form1.RCBNO.value;
	   var CURRENCY = document.form1.SUP_CURRENCY.value;
	   var region = document.form1.COUNTRY_REG.value;

	   var rcbn = RCBNO.length;
	   
	   var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }
	   
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.form1.CUST_CODE.focus(); return false; }
	   
	   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Supplier Name"); 
	   document.form1.CUST_NAME.focus();
	   return false; 
	   }

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form1.SUP_CURRENCY.focus();
			 return false; 
			 }
		 
	   if(document.form1.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.form1.TAXTREATMENT.focus();
		return false;
		}
	   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form1.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form1.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.form1.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form1.RCBNO.focus();
	   return false; 
	 }
	   if(!IsNumeric(form1.SUP_PMENT_DAYS.value))
	   {
	     alert(" Please Enter Days In Number");
	     form1.SUP_PMENT_DAYS.focus();  form1.SUP_PMENT_DAYS.select(); return false;
	   }
	   if(document.form1.COUNTRY_CODE_S.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE_S.focus();
		 return false;
		}
	   
		var datasend = $('#formsupplierid').serialize();
	   
		var urlStr = "/track/CreateSupplierServlet?action=JADD&reurl=createBill";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(document.form.custModal.value == "cust"){
			$("input[name ='vendno']").val(data.supplier[0].SID); 
			$("input[name ='vendname']").val(data.supplier[0].SName);
			
			document.form1.reset();
			$('#supplierModal').modal('hide');
			}
		}
		});
	   
	}

function onIDGenSupplier()
{
	var plant = "<%=plant%>";
	var urlStr = "/track/CreateSupplierServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : plant,
		action : "JAuto-ID",
		reurl : "createProduct"
	},
	dataType : "json",
	success : function(data) {
		
		$("input[name ='CUST_CODE']").val(data.supplier[0].SID);
		$("input[name ='CUST_CODE1']").val(data.supplier[0].SID);
		
	}
	});
   
}
		

		function OnTaxChange(TAXTREATMENT)
	{
		
		if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
		{
			$("#TaxLabel").addClass("required");
		}
		else
			$("#TaxLabel").removeClass("required");
		}

	function OnBank(BankName)
	{
		$.ajax({
			type : "POST",
			url: '/track/MasterServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "GET_BANK_DATA",
				PLANT : "<%=plant%>",
				NAME : BankName,
			},
			success : function(dataitem) {
				var BankList=dataitem.BANKMST;
				var myJSON = JSON.stringify(BankList);						
				var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
				if (dt) {
				  var result = jQuery.parseJSON(dt);			  
				  var val = result.BRANCH;			  
				  $("input[name ='BRANCH']").val(val);
				}
			}
		});		
	}
	function OnCountry(Country)
	{
		$.ajax({
			type : "POST",
			url: '/track/MasterServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "GET_STATE_DATA",
				PLANT : "<%=plant%>",
				COUNTRY : Country,
			},
			success : function(dataitem) {
				var StateList=dataitem.STATEMST;
				var myJSON = JSON.stringify(StateList);
				//alert(myJSON);
				$('#STATE').empty();
				$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
					 $.each(StateList, function (key, value) {
						   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
					 $('#STATE_C').empty();
						$('#STATE_C').append('<OPTION style="display:none;">Select State</OPTION>');
							 $.each(StateList, function (key, value) {
								   $('#STATE_C').append('<option value="' + value.text + '">' + value.text + '</option>');
								});	 
			}
		});	
		
	}
		
	$('select[name="COUNTRY_CODE"]').on('change', function(){		
	    var text = $("#COUNTRY_CODE option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});
	
	
	function iscombprofin(){
		if (document.getElementById("ISCOMPRO").checked == true) {
			document.getElementById("ISCOMPRO").value = "ISCOMPRO";
			$(".comproprice").show();		
		}
	}
	
	function iscombprosem(){
		if (document.getElementById("ISCOMPRO_SEMI").checked == true) {
			document.getElementById("ISCOMPRO_SEMI").value = "ISCOMPRO_SEMI";
			$(".comproprice").show();
		}
	}
	
	$('input[type=radio][name=CPPI]').change(function() {
		if (this.value == 'BYPRICE') {
			$("input[name ='INCPRICE']").val(parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value));
			$("input[name ='INCPRICEUNIT']").val($("input[name ='CURRENCY']").val());
		}
		else if (this.value == 'BYPERCENTAGE') {
			$("input[name ='INCPRICE']").val(parseFloat("0.00000").toFixed(3));
			$("input[name ='INCPRICEUNIT']").val("%");
		}
	});
	
	
	function isNumCheck(){	
		var baseamount = $("input[name=INCPRICE]").val();
		var amounttype = document.form.CPPI.value;
		var zeroval = "0";
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if(baseamount.match(decimal) || baseamount.match(numbers)) 
			{ 
				if (amounttype == 'BYPRICE') {
					$("input[name ='INCPRICE']").val(parseFloat(baseamount).toFixed(document.getElementById("numberOfDecimal").value));
				}
				else if (amounttype == 'BYPERCENTAGE') {
					$("input[name ='INCPRICE']").val(parseFloat(baseamount).toFixed(3));
				}
			}else{
				alert("Please Enter Valid Price");
				if (amounttype == 'BYPRICE') {
					$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(document.getElementById("numberOfDecimal").value));
				}
				else if (amounttype == 'BYPERCENTAGE') {
					$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(3));
				}
			}
		}else{
			if (amounttype == 'BYPRICE') {
				$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(document.getElementById("numberOfDecimal").value));
			}
			else if (amounttype == 'BYPERCENTAGE') {
				$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(3));
			}
		}
		
	}
	
	
	$('select[name="COUNTRY_CODE_S"]').on('change', function(){		
		var text = $("#COUNTRY_CODE_S option:selected").text();
		// $("input[name ='COUNTRY']").val(text.trim());
		document.form1.COUNTRY.value = text.trim();
		});	
	
</script>
  
  <jsp:include page="newPaymentTermsModal.jsp"flush="true"></jsp:include><!-- imti for add paymentterms -->
 <jsp:include page="newPaymentModeModal.jsp" flush="true"></jsp:include><!-- added imthi for payment mode -->
 <jsp:include page="newTransportModeModal.jsp" flush="true"></jsp:include> <!-- imti for add transport --> 
  <jsp:include page="NewSupplierTypeModal.jsp" flush="true"></jsp:include> <!-- thanzith Add For SupplierType -->
  <jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include>  <!-- Resvi for add product department -->
  <jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category --> 
  <jsp:include page="newProductLocationModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category --> 
  <jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product sub Category -->
  <jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
  <jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
  <jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
  <jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include>  <!-- Thanzith for add product UOM -->
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>